comp2406a5 - Ryan Lo (101117765)

User Profile: Also added total quiz score

Modules:
npm install

To start:
create database folder
mongod --dbpath=database
node database-initializer.js
node server.js

localhost:3000 will direct you to homepage
Where you can get a quiz, look at all the users (with public profiles) or login
Logging in requires username and the password (same as username)
Once logged in you can set your profile to private, do a quiz, or logout
If logged in, completing a quiz will update your score.