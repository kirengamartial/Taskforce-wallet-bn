# TaskForce Wallet Backend API

A robust Spring Boot application providing RESTful API services for the TaskForce Wallet application, featuring secure authentication, transaction management, and PostgreSQL integration.

## 🌐 Live Deployments

- Primary API (Render): [https://taskforce-wallet-bn.onrender.com](https://taskforce-wallet-bn.onrender.com)
- Secondary API (AWS): [http://ec2-13-60-163-227.eu-north-1.compute.amazonaws.com](http://ec2-13-60-163-227.eu-north-1.compute.amazonaws.com)

## 🚀 Features

- **Secure Authentication**
    - JWT-based authentication system
    - User registration and login
    - Token-based session management

- **Account Management**
    - Multiple account types support (bank, mobile money, cash)
    - Account balance tracking
    - Transaction history

- **Transaction Processing**
    - Credit and debit transaction support
    - Category-based transaction organization
    - Transaction filtering and searching

- **Data Persistence**
    - PostgreSQL database integration
    - Efficient data modeling
    - Secure credential storage

## 🛠️ Technology Stack

- **Framework:** Spring Boot
- **Language:** Java 21
- **Database:** PostgreSQL
- **Security:** Spring Security with JWT
- **Build Tool:** Maven
- **Containerization:** Docker

## 📋 Prerequisites

- Java Development Kit (JDK) 21
- Maven
- Docker (optional)
- PostgreSQL (if running locally)

## 🚀 Getting Started

### Local Development

1. **Clone the repository**
   ```bash
   git clone https://github.com/kirengamartial/Taskforce-wallet-bn.git
   cd Taskforce-wallet-bn
   ```

2. **Configure environment variables**
   Create a `.env` file in the root directory:
   ```env
   DB_URL=jdbc:postgresql://your-database-url/dbname
   DB_USERNAME=your_username
   DB_PASSWORD=your_password
   FRONTEND_URL=http://localhost:5173,https://taskforce-wallet.vercel.app
   ```

3. **Build the application**
   ```bash
   mvn clean install
   ```

4. **Run the application**
   ```bash
   mvn spring-boot:run
   ```

### Docker Deployment

1. **Build the Docker image**
   ```bash
   docker build -t wallet-app .
   ```

2. **Run with Docker Compose**
   ```bash
   docker-compose up
   ```

## 📡 API Endpoints

### Authentication
- **POST** `/api/users/register` - Register new user
  ```json
  {
    "username": "username",
    "password": "password"
  }
  ```

- **POST** `/api/users/login` - User login
  ```json
  {
    "username": "username",
    "password": "password"
  }
  ```
  Response:
  ```json
  {
    "accessToken": "jwt_token",
    "username": "username",
    "userId": 1,
    "tokenType": "Bearer "
  }
  ```

### Accounts
- **GET** `/api/accounts` - Get all accounts
- **GET** `/api/accounts/user/{userId}` - Get user accounts
- **POST** `/api/accounts` - Create new account
- **PUT** `/api/accounts/{id}` - Update account
- **DELETE** `/api/accounts/{id}` - Delete account

## 🔒 Security

- JWT-based authentication
- Password encryption using BCrypt
- CORS configuration for frontend integration
- Protected endpoints requiring authentication

## 🐳 Docker Configuration

```dockerfile
FROM eclipse-temurin:21-jdk-alpine
COPY target/*.jar app.jar
EXPOSE 8080
ENTRYPOINT ["java","-jar", "/app.jar"]
```

Docker Compose configuration:
```yaml
version: "3.8"
services:
  app:
    build: .
    container_name: wallet_app
    ports:
      - "8080:8080"
    environment:
      - DB_URL=${DB_URL}
      - DB_USERNAME=${DB_USERNAME}
      - DB_PASSWORD=${DB_PASSWORD}

networks:
  default:
    driver: bridge
```

## 📝 Environment Variables

| Variable | Description |
|----------|-------------|
| DB_URL | PostgreSQL database URL |
| DB_USERNAME | Database username |
| DB_PASSWORD | Database password |
| FRONTEND_URL | Allowed frontend origins for CORS |

## 👨‍💻 Author

Martial Kirenga - [GitHub Profile](https://github.com/kirengamartial)
