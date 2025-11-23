# Weather Oracle

This web application allows you to view the current weather forecast. Simply sign up and search for any city or location by name. Your list of saved locations will be displayed each time you sign in to the application.

It was developed for educational purposes as part of the [Java Backend Learning Course](https://zhukovsd.github.io/java-backend-learning-course/) by Sergey Zhukov.

**Try the application at:**

[https://demo.weather-oracle.ddevuss.com](https://demo.weather-oracle.ddevuss.com)

Please note that the application might not work correctly due to potential issues with the connection to the Open Weather API server.

<img width="1920" height="995" alt="Screenshot 2025-09-05 at 14 16 30" src="https://github.com/user-attachments/assets/c4f32c15-9e3c-472f-a0ed-69c7db366f18" />

## Project Overview:

The project aims to provide a practical learning experience in Java backend development, focusing on:

1. **Technologies Used:**
    - Java: Collections, Object-Oriented Programming (OOP)
    - Design Pattern: MVC
    - Build Tools: Gradle
    - Spring Boot framework
    - Databases: PostgreSQL, Liquibase
    - Testing: Unit testing with JUnit 5, Testcontainer
    - Deployment: Docker and Docker-compose
    - CI/CD via GitHub Actions

2. **Motivation:**
    - Creating a RESTful API application
    - Gaining practical experience with Spring Boot framework
    - Understanding how to connect to another Rest API server
    - Using testcontainer and liquibase
    - Using GitHub Actions

## Installation and Setup Instructions:

**Requirements:**

Before proceeding with the installation, ensure the following prerequisites are met:

- Docker: Docker and docker-compose must be installed to run the application in containers.

To run this project locally, follow these steps:

1. **Download the code from this repository.**

2. **Setup docker-compose.yml**
    - get your own key from [Open Weather](https://openweathermap.org/)
    - create and set up .env file (use an exaple .env.example) into the same directory that contanes docker-compose.yml
    - create network via docker (you can find a special command below)
    - if necessary you can change external port for a container (change ports parameter into docker-compose.yml)

    Use the following command in console to create docker network
   ```bash
   docker network create weather-oracle-net
   ```
   
3. **Build images and run containers:**
   
   Use the following command in console being into directory with docker-compose.yml:
   ```bash
   docker compose --env-file .env up -d
   ```
  The application will be available at http://localhost:{app-port}/ where app-port is 8095 in default.

4. **Using the frontend application**
   
   The frontend is in a separate branch/repo:
   
   [https://github.com/dDevusS/weather-oracle/tree/frontend](https://github.com/dDevusS/weather-oracle/tree/frontend)


## API documentation:

**[https://ddevuss.github.io/weather-oracle/](https://ddevuss.github.io/weather-oracle/)**
<br><br><br><br><br><br>
