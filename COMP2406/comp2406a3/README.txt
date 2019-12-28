To start: node server.js

Modules install:
npm install

GET /questions:
status: 
0 (success)
1 (no results or not enough questions to fulfill request)
2 (invalid token)

results:
Array of question objects
Not sent if status is 1 or 2. 

Query parameters supported:
limit: integer – the number of desired questions 
difficulty: integer – the desired difficulty ID 
category: integer – the desired category ID 
token: string – the session ID


POST /sessions:
Create new session

GET /sessions 
Get all sessions

DELETE /sessions/:sessionid
Delete session with specified id