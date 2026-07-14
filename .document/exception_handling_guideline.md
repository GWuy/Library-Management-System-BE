# Exception Handling Guideline

Tài liệu này quy định cách sử dụng Exception trong project LMS để toàn bộ team thống nhất cách xử lý lỗi.

---

# Mục lục

- Exception Flow
- Danh sách Exception
- Khi nào dùng từng Exception
- Ví dụ thực tế
- Những điều KHÔNG nên làm
- Best Practices

---

# Exception Flow

Luồng xử lý chuẩn của project

```
Controller
      │
      ▼
Service
      │
      ├── throw ResourceNotFoundException
      ├── throw BadRequestException
      ├── throw UnauthorizedException
      ├── throw ForbiddenException
      └── throw ConflictException
               │
               ▼
GlobalExceptionHandler
               │
               ▼
HTTP Response
```

**Lưu ý**

- Controller KHÔNG tự bắt Exception.
- Service chỉ cần `throw`.
- `GlobalExceptionHandler` sẽ tự chuyển Exception thành HTTP Response.

---

# Danh sách Exception

```
exception
│
├── GlobalExceptionHandler.java
├── ResourceNotFoundException.java
├── BadRequestException.java
├── UnauthorizedException.java
├── ForbiddenException.java
└── ConflictException.java
```

---

# 1. ResourceNotFoundException

HTTP Status

```
404 NOT FOUND
```

## Dùng khi

Không tìm thấy dữ liệu trong Database.

Ví dụ

- Không tìm thấy User
- Không tìm thấy Book
- Không tìm thấy Borrow Request
- Không tìm thấy Category

Ví dụ

```java
User user = userRepository.findById(id)
        .orElseThrow(() ->
                new ResourceNotFoundException("User not found."));
```

---

# 2. BadRequestException

HTTP Status

```
400 BAD REQUEST
```

## Dùng khi

Request gửi lên không hợp lệ.

Ví dụ

- Due Date nhỏ hơn Borrow Date
- Quantity <= 0
- Dữ liệu không đúng business rule

Ví dụ

```java
if(request.getQuantity() <= 0){
    throw new BadRequestException(
            "Quantity must be greater than 0.");
}
```

Không dùng cho Validation Annotation như

```
@NotBlank
@NotNull
@Email
```

Các annotation này sẽ được Spring xử lý.

---

# 3. UnauthorizedException

HTTP Status

```
401 UNAUTHORIZED
```

## Dùng khi

Người dùng chưa đăng nhập hoặc Token không hợp lệ.

Ví dụ

- JWT hết hạn
- JWT sai
- Username/password sai

Ví dụ

```java
throw new UnauthorizedException(
        "Invalid username or password.");
```

---

# 4. ForbiddenException

HTTP Status

```
403 FORBIDDEN
```

## Dùng khi

Đã đăng nhập nhưng không có quyền.

Ví dụ

Borrower muốn xóa Book.

```java
if(currentUser.getRole() != Role.STAFF){
    throw new ForbiddenException(
            "You don't have permission.");
}
```

Ví dụ

Staff cố sửa User khác.

```java
throw new ForbiddenException(
        "Access denied.");
```

---

# 5. ConflictException

HTTP Status

```
409 CONFLICT
```

## Dùng khi

Dữ liệu bị trùng.

Ví dụ

- Email đã tồn tại
- Username đã tồn tại
- ISBN đã tồn tại

```java
if(userRepository.existsByEmail(request.getEmail())){
    throw new ConflictException(
            "Email already exists.");
}
```

---

# Không được làm

## Không return ResponseEntity trong Service

❌ Sai

```java
return ResponseEntity.badRequest().body(...);
```

✅ Đúng

```java
throw new BadRequestException("...");
```

---

## Không try-catch Exception trong Controller

❌ Sai

```java
try{

}catch(Exception e){

}
```

✅ Đúng

```java
return service.create(request);
```

---

## Không throw Exception chung

❌ Sai

```java
throw new RuntimeException("...");
```

❌ Sai

```java
throw new Exception("...");
```

✅ Đúng

```java
throw new ResourceNotFoundException(...);
```

---

# Ví dụ thực tế

## UserService

```java
public UserResponse create(UserRequest request){

    if(userRepository.existsByEmail(request.getEmail())){
        throw new ConflictException(
                "Email already exists.");
    }

    User user = mapper.toEntity(request);

    return mapper.toResponse(
            userRepository.save(user));
}
```

---

## BookService

```java
Book book = bookRepository.findById(id)
        .orElseThrow(() ->
                new ResourceNotFoundException(
                        "Book not found."));
```

---

## BorrowService

```java
if(book.getAvailableQuantity() == 0){
    throw new BadRequestException(
            "Book is out of stock.");
}
```

---

## AdminService

```java
if(currentUser.getRole() != Role.ADMIN){
    throw new ForbiddenException(
            "Admin permission required.");
}
```

---

# Error Response Format

Tất cả API sẽ trả về cùng format.

```json
{
  "timestamp": "2026-07-14T12:30:00",
  "status": 404,
  "error": "Not Found",
  "message": "User not found.",
  "path": "/api/users/1"
}
```

---

# HTTP Status Mapping

| Exception | HTTP Status |
|------------|------------|
| ResourceNotFoundException | 404 |
| BadRequestException | 400 |
| UnauthorizedException | 401 |
| ForbiddenException | 403 |
| ConflictException | 409 |
| Exception | 500 |

---

# Best Practices

✅ Service chỉ xử lý Business Logic.

✅ Không xử lý HTTP Status trong Service.

✅ Không xử lý JSON Error trong Controller.

✅ Không dùng RuntimeException trực tiếp.

✅ Chỉ throw Custom Exception.

✅ GlobalExceptionHandler chịu trách nhiệm trả Response.

---

# Quy tắc của Team

- Không viết `try-catch` trong Controller nếu chỉ để trả lỗi.
- Không `return ResponseEntity.badRequest()` trong Service.
- Mọi lỗi nghiệp vụ phải dùng Custom Exception.
- Mọi API phải trả về cùng một định dạng ErrorResponse.
- Nếu cần thêm loại lỗi mới (ví dụ `DuplicateBorrowException`), hãy tạo một Custom Exception mới và bổ sung vào `GlobalExceptionHandler`.