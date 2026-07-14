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
