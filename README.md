# Spring Java Online Shop

## Description
This project is a Spring Boot application that includes various features such as data persistence, security, web functionalities, and session management. It uses Gradle as the build tool and is configured to use Java 17.

## Features
- Spring Boot
- Spring Data JPA
- Spring Security (Custom Security Annotation Base Impl)
- Thymeleaf
- Validation
- Web
- Session Management
- JWT Authentication
- PostgreSQL Database

## Requirements
- Java 17
- Gradle
- PostgreSQL

## Setup
1. Clone the repository:
    ```sh
    git clone https://github.com/yourusername/your-repo-name.git
    ```
2. Navigate to the project directory:
    ```sh
    cd your-repo-name
    ```
3. Build the project:
    ```sh
    ./gradlew build
    ```
4. Run the application:
    ```sh
    ./gradlew bootRun
    ```

## Configuration
- Update the `application.properties` file with your database configuration:
    ```properties
    spring.datasource.url=jdbc:postgresql://localhost:5432/yourdatabase
    spring.datasource.username=yourusername
    spring.datasource.password=yourpassword
    ```

## Usage
- Access the application at `http://localhost:8080`.

## Testing
- Run tests using:
    ```sh
    ./gradlew test
    ```

## Dependencies
- Spring Boot Starter Data JPA
- Spring Boot Starter Security
- Spring Boot Starter Thymeleaf
- Spring Boot Starter Validation
- Spring Boot Starter Web
- Spring Session Core
- Spring Security Crypto
- Thymeleaf Extras Spring Security
- SLF4J API
- JJWT API
- Lombok
- PostgreSQL Driver
- Hibernate JPA Model Generator
- Spring Boot Starter Test
- Spring Security Test
- JUnit Platform Launcher

## License
This project is licensed under the MIT License - see the [LICENSE](LICENSE) file for details.