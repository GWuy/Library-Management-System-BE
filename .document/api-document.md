# LMS API Documentation

This document provides a detailed description of the LMS (Learning Management System) API.

---

## Authentication API

### Register a new user

- **Method:** `POST`
- **Endpoint:** `/api/auth/register`
- **Content-Type:** `multipart/form-data`

This endpoint allows a new user to register with the system. The request must be a multipart form-data request containing both user data and an avatar image.

#### Request Body

The request consists of two parts:

1.  `data`: A JSON object containing the user's registration information.
2.  `avatar`: The user's avatar image file.

##### `data` (JSON Object)

| Field      | Type   | Description                                                                                                   | Constraints                                                                                                                            |
| :--------- | :----- | :------------------------------------------------------------------------------------------------------------ | :------------------------------------------------------------------------------------------------------------------------------------- |
| `fullName` | String | The full name of the user.                                                                                    | Required.                                                                                                                              |
| `email`    | String | The user's email address.                                                                                     | Required, must be a valid email format (e.g., `user@example.com`).                                                                     |
| `phone`    | String | The user's phone number.                                                                                      | Required, must be a valid Vietnamese phone number.                                                                                     |
| `username` | String | The desired username for the account.                                                                         | Required.                                                                                                                              |
| `password` | String | The password for the account.                                                                                 | Required, must be between 8 and 32 characters, and contain at least one uppercase letter, one lowercase letter, one number, and one special character. |

##### `avatar` (File)

-   The user's profile picture.

#### Responses

-   **201 Created:** The user was successfully registered.

    ```json
    {
      "message": "Register successfully",
      "result": {
        "id": "c2f2e2d7-e033-4a13-a982-f6c4bfde2837",
        "fullName": "John Doe",
        "email": "johndoe@example.com",
        "phone": "0987654321",
        "username": "johndoe",
        "role": "USER",
        "avatar": "http://example.com/path/to/avatar.jpg",
        "isDelete": false,
        "isEnable": true,
        "createdBy": "johndoe",
        "updatedBy": "johndoe",
        "createdAt": "2023-10-27T10:00:00Z",
        "updatedAt": "2023-10-27T10:00:00Z"
      }
    }
    ```

-   **400 Bad Request:** The request is invalid (e.g., missing fields, invalid data format).

    ```json
    {
      "message": "Validation failed",
      "errors": {
        "fullName": "Full name is required",
        "email": "Email is invalid",
        "phone": "Phone number is invalid",
        "password": "Password must be between 8 and 32 characters"
      }
    }
    ```
---

## Category API

### Get all categories

- **Method:** `GET`
- **Endpoint:** `/api/categories`

This endpoint retrieves a list of all categories.

#### Responses

-   **200 OK:** A list of categories was successfully retrieved.

    ```json
    {
      "message": "Get all categories successfully",
      "result": [
        {
          "id": 1,
          "name": "Fiction"
        },
        {
          "id": 2,
          "name": "Non-Fiction"
        }
      ]
    }
    ```

### Get category by ID

- **Method:** `GET`
- **Endpoint:** `/api/categories/{id}`

This endpoint retrieves a single category by its ID.

#### Parameters

| Parameter | Type    | Description              |
| :-------- | :------ | :----------------------- |
| `id`      | Integer | The ID of the category. |

#### Responses

-   **200 OK:** The category was successfully retrieved.

    ```json
    {
      "message": "Get category successfully",
      "result": {
        "id": 1,
        "name": "Fiction"
      }
    }
    ```

-   **404 Not Found:** The category with the specified ID was not found.

### Create a new category

- **Method:** `POST`
- **Endpoint:** `/api/categories`
- **Content-Type:** `application/json`

This endpoint creates a new category.

#### Request Body (JSON)

| Field  | Type   | Description              |
| :----- | :----- | :----------------------- |
| `name` | String | The name of the category. |

#### Responses

-   **201 Created:** The category was successfully created.

    ```json
    {
      "message": "Category created successfully",
      "result": {
        "id": 3,
        "name": "Science"
      }
    }
    ```

-   **400 Bad Request:** The request is invalid (e.g., missing `name`).

### Update a category

- **Method:** `PUT`
- **Endpoint:** `/api/categories/{id}`
- **Content-Type:** `application/json`

This endpoint updates an existing category.

#### Parameters

| Parameter | Type    | Description              |
| :-------- | :------ | :----------------------- |
| `id`      | Integer | The ID of the category. |

#### Request Body (JSON)

| Field  | Type   | Description              |
| :----- | :----- | :----------------------- |
| `name` | String | The new name of the category. |

#### Responses

-   **200 OK:** The category was successfully updated.

    ```json
    {
      "message": "Category updated successfully",
      "result": {
        "id": 1,
        "name": "Updated Fiction"
      }
    }
    ```

-   **400 Bad Request:** The request is invalid (e.g., missing `name`).
-   **404 Not Found:** The category with the specified ID was not found.

### Delete a category

- **Method:** `DELETE`
- **Endpoint:** `/api/categories/{id}`

This endpoint deletes a category by its ID.

#### Parameters

| Parameter | Type    | Description              |
| :-------- | :------ | :----------------------- |
| `id`      | Integer | The ID of the category. |

#### Responses

-   **200 OK:** The category was successfully deleted.

    ```json
    {
      "message": "Category deleted successfully",
      "result": ""
    }
    ```

-   **404 Not Found:** The category with the specified ID was not found.
---

## Book API

### Get all books (with filters)

- **Method:** `GET`
- **Endpoint:** `/api/books/all`

This endpoint retrieves a list of all **AVAILABLE** books. Supports optional filtering by title, author, published year range, and categories. If no filter parameters are provided, all available books are returned.

#### Query Parameters

| Parameter      | Type           | Description                                      | Required |
| :------------- | :------------- | :----------------------------------------------- | :------- |
| `title`        | String         | Filter by book title (case-insensitive, partial match). | No       |
| `author`       | String         | Filter by author name (case-insensitive, partial match). | No       |
| `fromYear`     | Integer        | Minimum published year (inclusive).              | No       |
| `toYear`       | Integer        | Maximum published year (inclusive).              | No       |
| `categoryIds`  | Array[Integer] | Filter by one or more category IDs. Books belonging to any of the selected categories will be returned. | No       |

#### Example Requests

```
GET /api/books/all
GET /api/books/all?title=clean
GET /api/books/all?author=martin&fromYear=2000&toYear=2020
GET /api/books/all?categoryIds=1&categoryIds=3
GET /api/books/all?title=code&author=martin&fromYear=2005&toYear=2015&categoryIds=1&categoryIds=2
```

#### Responses

-   **200 OK:** A list of books was successfully retrieved.

    ```json
    {
      "success": true,
      "message": "Get all books successfully",
      "data": [
        {
          "id": 1,
          "title": "Clean Code",
          "author": "Robert C. Martin",
          "publisher": "Prentice Hall",
          "isbn": "978-0132350884",
          "publishedYear": 2008,
          "quantity": 5,
          "availableQuantity": 3,
          "status": "AVAILABLE",
          "description": "A handbook of agile software craftsmanship",
          "categories": [
            {
              "id": 1,
              "name": "Programming",
              "description": "Programming books"
            }
          ]
        }
      ]
    }
    ```

### Get book by ID

- **Method:** `GET`
- **Endpoint:** `/api/books/detail/{id}`

This endpoint retrieves a single book by its ID.

#### Parameters

| Parameter | Type    | Description        |
| :-------- | :------ | :----------------- |
| `id`      | Integer | The ID of the book. |

#### Responses

-   **200 OK:** The book was successfully retrieved.

    ```json
    {
      "success": true,
      "message": "Get book successfully",
      "data": {
        "id": 1,
        "title": "Clean Code",
        "author": "Robert C. Martin",
        "publisher": "Prentice Hall",
        "isbn": "978-0132350884",
        "publishedYear": 2008,
        "quantity": 5,
        "availableQuantity": 3,
        "status": "AVAILABLE",
        "description": "A handbook of agile software craftsmanship",
        "categories": [
          {
            "id": 1,
            "name": "Programming",
            "description": "Programming books"
          }
        ]
      }
    }
    ```

-   **404 Not Found:** The book with the specified ID was not found.

### Create a new book

- **Method:** `POST`
- **Endpoint:** `/api/books/create`
- **Content-Type:** `application/json`

This endpoint creates a new book. At least one category must be assigned.

#### Request Body (JSON)

| Field           | Type          | Description                          | Constraints                                    |
| :-------------- | :------------ | :----------------------------------- | :--------------------------------------------- |
| `title`         | String        | The title of the book.               | Required. Max 255 characters.                  |
| `author`        | String        | The author of the book.              | Optional. Max 255 characters.                  |
| `publisher`     | String        | The publisher of the book.           | Optional. Max 255 characters.                  |
| `isbn`          | String        | The ISBN of the book.                | Optional. Max 50 characters. Must be unique.   |
| `publishedYear` | Integer       | The year the book was published.     | Optional.                                      |
| `quantity`      | Integer       | The total quantity of the book.      | Required. Must be at least 1.                  |
| `description`   | String        | A description of the book.          | Optional.                                      |
| `categoryIds`   | Array[Integer]| The IDs of the categories to assign. | Required. At least one category must be given. |

#### Example Request

```json
{
  "title": "Clean Code",
  "author": "Robert C. Martin",
  "publisher": "Prentice Hall",
  "isbn": "978-0132350884",
  "publishedYear": 2008,
  "quantity": 5,
  "description": "A handbook of agile software craftsmanship",
  "categoryIds": [1, 3]
}
```

#### Responses

-   **201 Created:** The book was successfully created.

    ```json
    {
      "success": true,
      "message": "Book created successfully",
      "data": {
        "id": 1,
        "title": "Clean Code",
        "author": "Robert C. Martin",
        "publisher": "Prentice Hall",
        "isbn": "978-0132350884",
        "publishedYear": 2008,
        "quantity": 5,
        "availableQuantity": 5,
        "status": "AVAILABLE",
        "description": "A handbook of agile software craftsmanship",
        "categories": [
          {
            "id": 1,
            "name": "Programming",
            "description": "Programming books"
          }
        ]
      }
    }
    ```

-   **400 Bad Request:** The request is invalid (e.g., missing `title`, missing `categoryIds`, `quantity` < 1).
-   **404 Not Found:** One or more category IDs do not exist.
-   **409 Conflict:** A book with the same ISBN already exists.

### Update a book

- **Method:** `PUT`
- **Endpoint:** `/api/books/update/{id}`
- **Content-Type:** `application/json`

This endpoint updates an existing book.

#### Parameters

| Parameter | Type    | Description        |
| :-------- | :------ | :----------------- |
| `id`      | Integer | The ID of the book. |

#### Request Body (JSON)

| Field           | Type          | Description                          | Constraints                                    |
| :-------------- | :------------ | :----------------------------------- | :--------------------------------------------- |
| `title`         | String        | The title of the book.               | Required. Max 255 characters.                  |
| `author`        | String        | The author of the book.              | Optional. Max 255 characters.                  |
| `publisher`     | String        | The publisher of the book.           | Optional. Max 255 characters.                  |
| `isbn`          | String        | The ISBN of the book.                | Optional. Max 50 characters. Must be unique.   |
| `publishedYear` | Integer       | The year the book was published.     | Optional.                                      |
| `quantity`      | Integer       | The total quantity of the book.      | Required. Must be at least 1.                  |
| `description`   | String        | A description of the book.          | Optional.                                      |
| `categoryIds`   | Array[Integer]| The IDs of the categories to assign. | Required. At least one category must be given. |

#### Example Request

```json
{
  "title": "Clean Code (2nd Edition)",
  "author": "Robert C. Martin",
  "publisher": "Prentice Hall",
  "isbn": "978-0132350884",
  "publishedYear": 2008,
  "quantity": 10,
  "description": "Updated description",
  "categoryIds": [1, 2]
}
```

#### Responses

-   **200 OK:** The book was successfully updated.

    ```json
    {
      "success": true,
      "message": "Book updated successfully",
      "data": {
        "id": 1,
        "title": "Clean Code (2nd Edition)",
        "author": "Robert C. Martin",
        "publisher": "Prentice Hall",
        "isbn": "978-0132350884",
        "publishedYear": 2008,
        "quantity": 10,
        "availableQuantity": 8,
        "status": "AVAILABLE",
        "description": "Updated description",
        "categories": [
          {
            "id": 1,
            "name": "Programming",
            "description": "Programming books"
          },
          {
            "id": 2,
            "name": "Software Engineering",
            "description": "SE books"
          }
        ]
      }
    }
    ```

-   **400 Bad Request:** The request is invalid, or the new quantity is less than the number of currently borrowed books.
-   **404 Not Found:** The book or one of the category IDs was not found.
-   **409 Conflict:** A book with the same ISBN already exists (excluding the current book).

### Delete a book

- **Method:** `DELETE`
- **Endpoint:** `/api/books/delete/{id}`

This endpoint deletes a book by its ID.

#### Parameters

| Parameter | Type    | Description        |
| :-------- | :------ | :----------------- |
| `id`      | Integer | The ID of the book. |

#### Responses

-   **200 OK:** The book was successfully deleted.

    ```json
    {
      "success": true,
      "message": "Book deleted successfully",
      "data": ""
    }
    ```

-   **404 Not Found:** The book with the specified ID was not found.

---

## Borrow Record API

### Search active borrow record

- **Method:** `GET`
- **Endpoint:** `/api/borrow-records/search`

Search for an active (BORROWING) record for a specific borrower and book.

#### Query Parameters

| Parameter | Type    | Description           | Constraints |
| :-------- | :------ | :-------------------- | :---------- |
| `borrowerId` | Integer | ID of the borrower. | Required.   |
| `bookId`   | Integer | ID of the book.       | Required.   |

#### Responses

-   **200 OK:** Borrow record found.

    ```json
    {
      "success": true,
      "message": "Borrow record found.",
      "data": {
        "id": 1,
        "borrowerId": 1,
        "borrowerName": "John Doe",
        "bookId": 1,
        "bookTitle": "Clean Code",
        "borrowDate": "2023-10-27T10:00:00Z",
        "dueDate": "2023-11-10T10:00:00Z",
        "returnDate": null,
        "status": "BORROWING"
      }
    }
    ```

-   **404 Not Found:** Borrow record not found or already returned.

    ```json
    {
      "success": false,
      "message": "Borrow record not found or already returned.",
      "timestamp": "2023-10-27T11:00:00",
      "path": "/api/borrow-records/search"
    }
    ```

### Return a book

- **Method:** `POST`
- **Endpoint:** `/api/borrow-records/{borrowId}/return`

Process the return of a borrowed book.

#### Parameters

| Parameter | Type    | Description            |
| :-------- | :------ | :--------------------- |
| `borrowId` | Integer | The ID of the borrow record. |

#### Responses

-   **200 OK:** Book returned successfully.

    ```json
    {
      "success": true,
      "message": "Book returned successfully.",
      "data": null
    }
    ```

-   **404 Not Found:** Borrow record not found or already returned.

    ```json
    {
      "success": false,
      "message": "Borrow record not found or already returned.",
      "timestamp": "2023-10-27T11:00:00",
      "path": "/api/borrow-records/1/return"
    }
    ```
---
