# AI Life OS - Sample API Requests

## 1. Authentication

### Signup
```http
POST http://localhost:8080/auth/signup
Content-Type: application/json

{
  "username": "sungjinwoo",
  "email": "sung@hunter.com",
  "password": "password123"
}
```

### Login
```http
POST http://localhost:8080/auth/login
Content-Type: application/json

{
  "username": "sungjinwoo",
  "password": "password123"
}
```
**-> Returns JWT token. Use this token in the header `Authorization: Bearer <token>` for subsequent requests.**

## 2. Problems
### Get All Problems
```http
GET http://localhost:8080/problems
Authorization: Bearer <token>
```

### Create Problem
```http
POST http://localhost:8080/problems
Authorization: Bearer <token>
Content-Type: application/json

{
  "title": "Two Sum",
  "description": "Given an array of integers...",
  "difficulty": "EASY"
}
```

## 3. Submissions
### Submit Solution
```http
POST http://localhost:8080/submissions
Authorization: Bearer <token>
Content-Type: application/json

{
  "problemId": 1,
  "codeSnippet": "public int[] twoSum(int[] nums, int target) { return new int[]{0,1}; }"
}
```

## 4. Tasks
### Create Task
```http
POST http://localhost:8080/tasks
Authorization: Bearer <token>
Content-Type: application/json

{
  "title": "Review Graph Algorithms",
  "type": "STUDY",
  "date": "2024-05-15"
}
```

### Complete Task
```http
PUT http://localhost:8080/tasks/1/complete
Authorization: Bearer <token>
```

## 5. Dashboard & Leaderboard
### Get Dashboard
```http
GET http://localhost:8080/dashboard/1
Authorization: Bearer <token>
```

### Get Leaderboard
```http
GET http://localhost:8080/leaderboard
Authorization: Bearer <token>
```

## 6. AI Evaluation
### Mock Evaluate Answer
```http
POST http://localhost:8080/ai/evaluate
Authorization: Bearer <token>
Content-Type: application/json

{
  "submissionId": 1,
  "answer": "O(N) time complexity using HashMap"
}
```
