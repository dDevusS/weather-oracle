# Weather Oracle - Frontend

This web application allows you to view the current weather forecast. Simply sign up and search for any city or location by name. Your list of saved locations will be displayed each time you sign in to the application.
The frontend proxies API calls to the backend under the `/api` path.

**Try the application at:**

[https://demo.weather-oracle.ddevuss.com](https://demo.weather-oracle.ddevuss.com)

Please note that the application might not work correctly due to potential issues with the connection to the Open Weather API server.

<img width="1920" height="995" alt="Screenshot 2025-09-05 at 14 16 30" src="https://github.com/user-attachments/assets/c4f32c15-9e3c-472f-a0ed-69c7db366f18" />

## Project Overview:

The project aims to provide a practical learning experience in frontend development, focusing on:

1. **Technologies Used:**
    - Angular framework
    - SPA
    - Nginx
    - TypeScript
    - CSS
    - Deployment: Docker and Docker-compose
    - CI/CD via GitHub Actions

2. **Motivation:**
    - Creating a SPA application
    - Gaining practical experience with Angular framework
    - Understanding how to get, refresh and remove JWT tokens storing in cookies include a http only cookie
    - Using GitHub Actions

## Installation and Setup Instructions:

**Requirements:**

Before proceeding with the installation, ensure the following prerequisites are met:

- Docker: Docker and Docker Compose must be installed to run the application in containers.
- The backend application must be available on the local machine

Complete the backend setup from the main branch first, then follow the steps below.

To run this project locally, follow these steps:

1. **Download the code from this repository.**
   
2. **Build images and run containers:**
   
   Use the following command in console being into directory with docker-compose.yml:
   ```bash
   docker compose up -d
   ```
  The application will be available at http://localhost:{app-port}/ where app-port default is 8080

