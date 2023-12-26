#!/bin/bash
MYSQL_IMAGE_NAME=mysql:latest
MYSQL_NAME=mysql-container

if ! docker images --format "$MYSQL_IMAGE_NAME" | grep -q "$MYSQL_IMAGE_NAME"; then
  echo "MYSQL does not exist, it will be installed"
  docker pull $MYSQL_IMAGE_NAME
  MYSQL_ID=$(docker images $MYSQL_IMAGE_NAME --format "{{.ID}}")
else
  echo "MYSQL already existed"
  MYSQL_ID=$(docker images $MYSQL_IMAGE_NAME --format "{{.ID}}")
fi
echo "MYSQL[$MYSQL_IMAGE_NAME]: $MYSQL_ID"

# Run local MySQL
if docker ps --filter name="$MYSQL_NAME" | grep "$MYSQL_NAME" >/dev/null; then
  echo "MYSQL container is running, it will be stopped"
  docker container stop $MYSQL_NAME
else
  echo "MYSQL container is stopped"
fi

if docker ps -a --filter name="^$MYSQL_NAME$" | grep "$MYSQL_NAME" >/dev/null; then
  echo "MYSQL container exists, it will be removed"
  docker container rm $MYSQL_NAME
else
  echo "MYSQL container removed"
fi

echo "MYSQL is creating..."
docker run -d --name $MYSQL_NAME -p 3307:3306 -e MYSQL_ROOT_PASSWORD=password $MYSQL_IMAGE_NAME
echo "MYSQL restarted..."

# Wait for the container to start
sleep 10

# Connect to the MySQL container and create the table
docker exec -i $MYSQL_NAME mysql -u root -ppassword <<EOF
CREATE DATABASE IF NOT EXISTS processed_future_movement;
USE processed_future_movement;
CREATE TABLE client_information (
  id INT AUTO_INCREMENT PRIMARY KEY,
  client_type VARCHAR(5) NOT NULL,
  client_number VARCHAR(5) NOT NULL,
  account_number VARCHAR(5) NOT NULL,
  sub_account_number VARCHAR(5) NOT NULL,
  INDEX idx_client_information (client_type, client_number, account_number, sub_account_number)
);
CREATE TABLE product_information (
  id INT AUTO_INCREMENT PRIMARY KEY,
  exchange_code VARCHAR(5) NOT NULL,
  product_group_code VARCHAR(3) NOT NULL,
  symbol VARCHAR(6) NOT NULL,
  expiration_date VARCHAR(8) NOT NULL,
  INDEX idx_product_information (exchange_code, product_group_code, symbol, expiration_date)
);
CREATE TABLE transaction (
  id INT AUTO_INCREMENT PRIMARY KEY,
  client_information_id INT NOT NULL,
  product_information_id INT NOT NULL,
  transaction_amount INT NOT NULL,
  FOREIGN KEY (client_information_id) REFERENCES client_information(id),
  FOREIGN KEY (product_information_id) REFERENCES product_information(id)
);
EOF