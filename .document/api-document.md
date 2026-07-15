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
