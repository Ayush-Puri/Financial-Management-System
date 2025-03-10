# Financial Management System

## Overview

This Financial Management System is a RESTful API service for managing saving goals, transactions, and reports. It allows users to register, manage their finances, and generate financial reports. Authentication is required for non-public endpoints.

---
 
## Table Structure with Relations
---<img width="854" alt="Screenshot 2025-01-20 at 10 16 23 AM" src="https://github.com/user-attachments/assets/d136fbd3-8393-47a9-b959-b36158ab3a49" />


## Authentication

All **non-public** API endpoints require authentication using Basic Auth:

```
Authorization: Basic <username> <password>
```

- `<username>` and `<password>` are credentials of an already registered user.
- This ensures that transactions and saving goals are **user-specific** and prevents unauthorized modifications.

---

## API Endpoints

### **Public Endpoints (No Authentication Required)**

| Method | Endpoint         | Description       |
| ------ | ---------------- | ----------------- |
| `POST` | `/public/signup` | User registration |
| `POST` | `/public/login`  | Login user        |

#### **JSON Request Body for `/public/signup` (POST)**

```json
{
  "username": "",
  "email": "",
  "password": ""
}
```
#### **JSON Request Body for `/public/login` (POST)**

```json
{
  "username": "",
  "password": ""
}
```

### **User Endpoints (Requires Authentication)**
}

| Method   | Endpoint            | Description              |
| -------- | ------------------- | ------------------------ |
| `GET`    | `/api/allusers`     | Get all users            |
| `GET`    | `/api/MyCategories` | Get user categories      |
| `GET`    | `/api/user`         | Get current user details |
| `PUT`    | `/api/user`         | Update user details      |
| `DELETE` | `/api/user`         | Delete current user      |

### **Admin Endpoints (Requires Authentication)**

| Method   | Endpoint               | Description          |
| -------- | ---------------------- | -------------------- |
| `DELETE` | `/admin/user/{userId}` | Delete user by Admin |

### **Saving Goals Endpoints (Requires Authentication)**

| Method   | Endpoint               | Description              |
| -------- | ---------------------- | ------------------------ |
| `GET`    | `/savingGoal`          | Get all saving goals     |
| `POST`   | `/savingGoal`          | Create a new saving goal |
| `PUT`    | `/savingGoal/{goalId}` | Update saving goal       |
| `DELETE` | `/savingGoal/{goalId}` | Delete saving goal       |

#### **JSON Request Body for `/savingGoal` (POST)**

```json
{
  "targetamount": 3001.0,
  "targetdate": "16-02-2025"
}
```

### **Transaction Endpoints (Requires Authentication)**

| Method | Endpoint                              | Description          |
| ------ | ------------------------------------- | -------------------- |
| `POST` | `/transaction/commit`                 | Commit a transaction |
| `PUT`  | `/transaction/commit/{transactionId}` | Update transaction   |
| `GET`  | `/transaction/viewAll`                | Get all transactions |

#### **JSON Request Body for `/transaction/commit` (POST)**

```json
{
  "amount": 0,
  "type": "Income",
  "category": "",
  "description": ""
}
```

### **Report Endpoints (Requires Authentication)**

| Method | Endpoint         | Description                    |
| ------ | ---------------- | ------------------------------ |
| `GET`  | `/report`        | Get all reports                |
| `GET`  | `/report/{year}` | Get report for a specific year |

#### **JSON Request Body for `/report` (POST)**

```json
{
  "fromDate": "01-01-2025",
  "uptoDate": "31-12-2025"
}
```

---

## **Important Notes**

1. **Authentication is required for all endpoints except `/public/signup` and `/public/verify`.**
2. Ensure all API requests to secured endpoints include the `Authorization: Basic username password` header.
3. **Date format** for all API requests should be **`dd-MM-yyyy`**.
4. Users can only access and modify their own financial data, ensuring security and isolation.

---

### **Getting Started**

1. Clone the repository:
   ```bash
   git clone https://github.com/Ayush-Puri/SYFE-Financial-Management-Project.git
   ```
2. Install dependencies and run the project.

Report Data & Visualization
All key financial metrics—Income, Expenses, Savings, and Category-wise Spendings—have been calculated within the report. These values can be used with a graphics generator to create pie charts, bar charts, and other visual representations for better financial analysis.
API Testing Demonstration
For a complete walkthrough of API functionality, including testing all endpoints using Postman, refer to the following video:
[]


This README provides a structured and quick reference for using the SYFE Financial Management API effectively.



 

