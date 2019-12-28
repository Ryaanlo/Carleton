Modules:
npm install

To start:
create database folder
mongod --dbpath=database
node database-initializer.js
node server.js

localhost:3000 will direct you to homepage
Where you can browse all questions/quizzes/createquiz

browsing questions query:
category : string 	(many different options)
difficulty : string 	(easy, medium, hard)

browsing quizzes query:
creator : string	(name of creator)
tag : string		(name of tag)

creating a quiz:
input creator, tags (separated by spaces for more than one)
choose the category & difficulty
Add questions by clicking the add button
Remove questions by clicking the remove button