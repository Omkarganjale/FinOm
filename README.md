# FinOm - Demo Banking Application

## Overview
FinOm is a banking application that provides APIs to manage user accounts, perform transactions, and query account details. This application is built using **Spring Boot**, **H2 Database**, and adheres to RESTful principles.

---

## Features
- Create new accounts with automatically assigned account numbers.
- Query account balance and account holder details.
- Perform credit, debit, and fund transfer transactions.

---

## Technologies Used
- **Java 17**
- **Spring Boot 3.4.1**
- **Spring Data JPA**
- **H2 Database** (for testing)
- **MySQL** (production database)
- **Swagger/OpenAPI** for API documentation
- **MapStruct** for object mapping
- **Lombok** for boilerplate code reduction

---

## Setup Instructions

### Prerequisites
- Java 17
- Maven
- MySQL (optional for production setup)

### Steps to Run
1. Clone the repository:
   ```bash
   git clone <repository-url>
   cd FinOm
   ```

2. Build the project:
   ```bash
   mvn clean install
   ```

3. Run the application:
   ```bash
   mvn spring-boot:run
   ```

4. Access Swagger API documentation:
    - URL: `http://localhost:8080/swagger-ui.html`

---

## Application Configuration

### `application.properties`
```properties
spring.application.name=FinOm
spring.datasource.url=jdbc:mysql://localhost:3306/finom
spring.datasource.driver-class-name=com.mysql.cj.jdbc.Driver
spring.datasource.username=<your-username>
spring.datasource.password=<your-password>
spring.jpa.hibernate.ddl-auto=update
```
- Replace `<your-username>` and `<your-password>` with your MySQL credentials.
- For testing, the application uses an H2 in-memory database automatically.

---

## API Endpoints

### **User Management**

#### 1. Create New Account
**POST** `/api/user/add`
- **Description**: Creates a new user account.
- **Request Body**: `UserRequest`
- **Response**: `BankResponse`
- **Response Code**: `201 CREATED`

#### 2. Enquire Account Balance
**GET** `/api/user/enquire/accountBalance`
- **Description**: Returns the balance for a given account.
- **Request Body**: `EnquiryRequest`
- **Response**: `BankResponse`
- **Response Code**: `203 SUCCESS`

#### 3. Enquire Account Name
**GET** `/api/user/enquire/accountName`
- **Description**: Returns the account holder's name.
- **Request Body**: `EnquiryRequest`
- **Response**: `String`
- **Response Code**: `203 SUCCESS`

### **Transactions**

#### 4. Credit Transaction
**POST** `/api/user/transaction/credit`
- **Description**: Credits an amount to the user's account.
- **Request Body**: `TransactionRequest`
- **Response**: `BankResponse`
- **Response Code**: `202 SUCCESS`

#### 5. Debit Transaction
**POST** `/api/user/transaction/debit`
- **Description**: Debits an amount from the user's account.
- **Request Body**: `TransactionRequest`
- **Response**: `BankResponse`
- **Response Code**: `202 SUCCESS`

#### 6. Transfer Funds
**POST** `/api/user/transaction/transfer`
- **Description**: Transfers funds from one account to another.
- **Request Body**: `TransferRequest`
- **Response**: `BankResponse`
- **Response Code**: `202 SUCCESS`

---

## Data Models

### `UserRequest`
```json
{
  "firstName": "string",
  "lastName": "string",
  "gender": "string",
  "email": "string",
  "phoneNumber": "string",
  "address": "string",
  "stateOfOrigin": "string"
}
```

### `BankResponse`
```json
{
  "status": "string",
  "message": "string",
  "data": "object"
}
```

### `EnquiryRequest`
```json
{
  "accountNumber": "string"
}
```

### `TransactionRequest`
```json
{
  "accountNumber": "string",
  "amount": "number"
}
```

### `TransferRequest`
```json
{
  "sourceAccount": "string",
  "targetAccount": "string",
  "amount": "number"
}
```

---

## Testing
- Use **H2 Database** for unit testing.
- Configure your `application-test.properties`:

```properties
spring.datasource.url=jdbc:h2:mem:testdb
spring.datasource.driver-class-name=org.h2.Driver
spring.datasource.username=sa
spring.datasource.password=password
spring.jpa.hibernate.ddl-auto=create-drop
```


---


## License
This project is licensed under the [MIT License](LICENSE).
