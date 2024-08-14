
# Library Management System

  

This is a simple Library Management System built using Spring Boot, Spring Data R2DBC, and PostgreSQL. The system allows for managing books, patrons, and borrowing records.

  

## Table of Contents

  

- [Features](#features)

- [Technologies Used](#technologies-used)

- [Setup Instructions](#setup-instructions)

- [API Endpoints](#api-endpoints)

- [Error Handling](#error-handling)

- [Contributing](#contributing)

- [License](#license)

  

## Features

  

- Manage books (add, update, delete, and retrieve)

- Manage patrons (add, update, delete, and retrieve)

- Manage borrowing records (borrow and return books)

- Error handling for common scenarios such as missing entities

  

## Technologies Used

  

- Java 17

- Spring Boot

- Spring Data R2DBC

- PostgreSQL

- Project Reactor (for reactive programming)

- Lombok (for reducing boilerplate code)

- Maven (for dependency management)

  

## Setup Instructions


  

### Prerequisites

  

- Java 17 or higher

- PostgreSQL

- Intellij IDE

  

### Clone the Repository

  

```
git  clone  https://github.com/MuhammadMagdy98/assessment.git

cd  assessment
```


### Create a PostgreSQL database:


`CREATE DATABASE library_db;` 

2.  Update the `application.properties` file with your database credentials:



```
spring.r2dbc.url=r2dbc:postgresql://localhost:5432/library_db
spring.r2dbc.username=<your-username>
spring.r2dbc.password=<your-password>

spring.liquibase.enabled=false
spring.jpa.hibernate.ddl-auto=update
``` 


## API Endpoints

### Book Management

-   **GET /api/books**: Retrieve a list of all books
  
**Response**:
```json
{
  "responseMeta": {
    "status": 200,
    "message": null,
    "success": true,
    "error": null
  },
  "data": [
    {
      "id": 1,
      "title": "Effective Java",
      "author": "Joshua",
      "publicationYear": "2024-08-12T04:19:28.476Z",
      "isBorrowed": true,
      "isbn": "ISBN 0-061-96436-0-0"
    },
    {
      "id": 3,
      "title": "C++ How to program",
      "author": "Harvey",
      "publicationYear": "2024-08-12T04:19:28.476Z",
      "isBorrowed": true,
      "isbn": "ISBN 0-061-96436-0"
    }
  ],
  "errorDisplay": ""
}
```
-   **GET /api/books/{id}**: Retrieve details of a specific book by ID
  
**Response**
```json
{
  "responseMeta": {
    "status": 200,
    "message": null,
    "success": true,
    "error": null
  },
  "data": 
    {
      "id": 1,
      "title": "Effective Java",
      "author": "Joshua",
      "publicationYear": "2024-08-12T04:19:28.476Z",
      "isBorrowed": true,
      "isbn": "ISBN 0-061-96436-0-0"
    }
  "errorDisplay": ""
}
```

-   **POST /api/books**: Add a new book to the library
  
**Request Body**:
```json
{
  "title": "New Book Title",
  "author": "New Author",
  "ISBN": "1122334455",
  "publicationYear": "2024-05-10T00:00:00Z"
}

``` 
-   **PUT /api/books/{id}**: Update an existing book's information
  
**Request Body**:
```json
{
    "title": "Updated Book Title",
    "author": "Updated Author",
    "ISBN": "9988776655",
    "publicationYear": "2024-07-20T00:00:00Z"
}
```
-   **DELETE /api/books/{id}**: Remove a book from the library

### Patron Management

-   **GET /api/patrons**: Retrieve a list of all patrons
-   **GET /api/patrons/{id}**: Retrieve details of a specific patron by ID
-   **POST /api/patrons**: Add a new patron to the system
  
**Request Body**:
```json
{
    "name": "New Patron",
    "contactInfo": "newpatron@example.com"
}
```
-   **PUT /api/patrons/{id}**: Update an existing patron's information
  
**Request Body**:
```json
{
    "name": "New Patron",
    "contactInfo": "newpatron@example.com"
}
```
-   **DELETE /api/patrons/{id}**: Remove a patron from the system

### Borrowing Records

-   **POST /api/borrow/{bookId}/patron/{patronId}**: Allow a patron to borrow a book
-   **POST /api/return/{bookId}/patron/{patronId}**: Allow a patron to return a borrowed book

### Error Handling

The application uses custom exceptions to handle common errors. For example, if a book or patron is not found, a `404 Not Found` response is returned with an appropriate error message.

### Example Error Response

json


```json
{
    "responseMeta": {
        "status": 404,
        "message": "Book is not found",
        "success": true,
        "error": null
    },
    "data": null,
    "errorDisplay": ""
}
```

