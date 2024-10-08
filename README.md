# Weather Oracle

This web application allows you to view the current weather forecast. Simply sign up and search for any city or location by name. Your list of saved locations will be displayed each time you sign in to the application.

It was developed for educational purposes as part of the [Java Backend Learning Course](https://zhukovsd.github.io/java-backend-learning-course/) by Sergey Zhukov.

**You can check in this application on:**

[http://37.27.92.25:8095](http://37.27.92.25:8095)

Please note that the application might not work correctly due to potential issues with the connection to the Open Weather API server.

![Снимок экрана от 2024-07-26 14-24-09](https://github.com/user-attachments/assets/a12e8813-5605-48c8-8ba9-e232fcd837a4)

## Project Overview:

The project aims to provide a practical learning experience in Java backend development, focusing on:

1. **Technologies Used:**
    - Java: Collections, Object-Oriented Programming (OOP)
    - Design Pattern: MVC(S)
    - Build Tools: Gradle
    - Spring Boot framework
    - Thymeleaf
    - Databases: PostgeSQL, liquibase
    - Frontend: HTML/CSS, Bootstrap
    - Testing: Unit testing with JUnit 5, testcontainer
    - Deployment: Docker and Docker-compose

2. **Motivation:**
    - Creating a client-server application with a web interface
    - Gaining practical experience with Spring Boot framework
    - Designing a simple web interface with Bootsrtap and Thymeleaf
    - Understanding how to connect to another Rest API server
    - Using testcontainer and liquibase

## Installation and Setup Instructions:

**Requirements:**

Before proceeding with the installation, ensure the following prerequisites are met:

- Docker: Docker and docker-compose must be installed to run the application in containers.

To run this project locally, follow these steps:

1. **Download application from this repository.**

2. **Setup docker-compose.yml**
    - change ports for weather-oracle_db, weather-oracle_app and another options if it is necessary
    - set OPENWEATHER_API_KEY (your have to get your own key on [Open Weather](https://openweathermap.org/))
   
3. **Build images and run containers:**
   
   Use follow command in console being into application's directory:
   ```bash
   docker-compose up --build
   ```
  Application will be access on http://localhost:{app-port}/ where app-port is 8095 in default.


  
