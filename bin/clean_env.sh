#!/bin/bash
MYSQL_IMAGE_NAME=mysql:latest
MYSQL_NAME=mysql-container

if docker ps --filter name="^$MYSQL_NAME$" | grep "$MYSQL_NAME" >/dev/null; then
  echo "MySQL is running, it will be stopped"
  docker container stop $MYSQL_NAME
else
  echo "MySQL is stopped"
fi

if docker ps -a --filter name="^$MYSQL_NAME$" | grep "$MYSQL_NAME" >/dev/null; then
  echo "MySQL container exists, it will be removed"
  docker container rm $MYSQL_NAME
else
  echo "MySQL is removed"
fi