# CityViewer

## Description
This is a simple application that allows you to view and edit the list of cities.
The back-emd is written in Java 17 using the SpringBoot 3 framework.
The front-end is written in Angular 15.
The application uses the H2 database, which is stored in memory.

## Installation
To run the application, you need to install Java 17 SDK and Node.js.

## Usage
### Database
- database requires docker runtime
- run `docker run --name cityviewer-db -e POSTGRES_PASSWORD=mysecretpassword -d -p 65432:5432 postgres`
- create new database in container: `cityviewer`

### Back-end
To run the back-end, you need to run the following command in the terminal:
`./gradlew bootRun` - to run the back-end
It will start on port `9876`.

### Front-end
To run the front-end, you need to run the following command in the terminal:
- `cd frontend/cityviewer-ui` - to locate the front-end root directory
- `npm install` - to install dependencies
- `ng serve` - to start the front-end

It will start on port `4200`.

If all set open `http://localhost:4200/` in your browser.

## Tools:
- Swagger is available at: `http://localhost:9876/swagger-ui/index.html`

## Test credentials
- username: admin, password: 1234 (admin has ROLE_ALLOW_EDIT)
- username: user, password: 1234

