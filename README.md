## Prerequisite
1. Docker installed
2. IDE(Intellij/Eclipse) installed
3. Postman installed

## Steps
1. Checkout project
2. Run ./bin/setup_env.sh for database docker container
3. Run ProcessedFutureMovementApplication.main()
4. Trigger POST request to http://localhost:8080/data with Basic authentication(Username: user, Password: password). Data will be loaded into database
5. Trigger POST request to http://localhost:8080/data/json with Basic authentication(Username: user, Password: password) to get result in JSON format
6. Trigger POST request to http://localhost:8080/data/csv with Basic authentication(Username: user, Password: password) to get result in CSV format
7. Run ./bin/clean_env.sh to remove database docker container

## Assumption
1. Data should not be more than 100,000 records
2. Source Data should be put under /src/main/resources/templates/input.txt
3. Basic authentication is used here for demo