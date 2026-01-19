# Task Management API 📝


##  Features

###  Authentication & Security
- User Registration & Login
- JWT Authentication (Access & Refresh Tokens)
- Role-Based Authorization (User / Admin)
- Password Hashing
- API Rate Limiting

### ✅ Task Management
- Create, Read, Update, Delete Tasks
- Task filtering and search
- Role-based task access
- Task statistics

### 👑 Admin Controls
- User management
- Role updates
- User deletion
## 🔑 Authentication APIs

### User Registration
**POST**
/api/v1/auth/register

**Request Body**
| Field | Type | Required |
|------|------|----------|
| name | String | Yes |
| email | String | Yes |
| password | String | Yes |
| role | String | No (default: user) |

**Validations**
- Unique email
- Password minimum 8 characters
- Must include uppercase, lowercase, and number
- Password is hashed before storing

---

### User Login
**POST**
/api/v1/auth/login
| Field        | Type   | Required |
| ------------ | ------ | -------- |
| **email**    | String | **Yes**  |
| **password** | String | **Yes**  |
/api/v1/auth/logout

Authentication required.

---

### Refresh Token
**POST**
/api/v1/auth/refresh
| Field            | Type   | Required |
| ---------------- | ------ | -------- |
| **refreshToken** | String | **Yes**  |
## ✅ Task Management APIs

### Create Task
**POST**
/api/v1/uploadTask

Authentication required.

**Fields**
- title
- description
- status
- priority
- dueDate

`createdBy` is automatically set to the logged-in user.

---

### Get All Tasks
**GET**
/api/v1/task

**Authorization**
- User: Own tasks only
- Admin: All tasks

**Filters**
/api/v1/tasks/:id
| Parameter | Type   | Required |
| --------- | ------ | -------- |
| **id**    | Number | **Yes**  |

/api/v1/admin/users

Admin only. Passwords are excluded.

---

### Update User Role
**PUT**
/admin/users/1/role

Roles:
- user
- admin

---

### Delete User
**DELETE**
/admin/users/{user_id}

Roles:
- user
- admin

---
### Task Stat
/admin/task/stats

Returns:
- Total tasks
- Completed tasks
- Pending tasks
- Tasks grouped by priority

Users see their stats, admins see global stats.

---

