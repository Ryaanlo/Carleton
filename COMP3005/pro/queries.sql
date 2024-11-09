--finds all usernames
select username
from Account;

--(Daniel used as example)retreives password and a boolean (true if account is an owner) for a certain account
select password, owner_bool
from Account
where username = 'Daniel';

--finds usernames that hae an account linked
select username
from Account_add;

--(Daniel used as example)retrives address of certain user
select Country, province, city, street, number
from Account_add
where username = 'Daniel';

--finds total amount of Books
select count(ID)
from book;

--Selects ID and Title of a book
select ID, title
from Book; 

--(B-1 used as example) retrives quantity for a certain book
select quantity
from Book
where ID = 'B-1'; 

--(J. K. Rowling used as example) gets the number of authors by a certain name
select count(ID)
from author
where name = 'J. K. Rowling';

--(J. K. Rowling used as emaple) get the ID for an author by a certain name
select ID
from author
where name = 'J. K. Rowling';

--Selects total number of publishers
select count(id)
from publisher; 

--Selects all ID's and Names of Publishers
select id, name
from publisher; 

--gets total number of authors
select count(ID)
from author;

--(J. K. Rowling used as example) gets books with matching author
select *
from (author join auth_book on author.id = auth_book.a_id) join book on b_id = book.id
where name = 'J. K. Rowling';

--(Fantasy used as example) gets books with matching genre
select *
from book
where genre = 'Fantasy';
 
--(BookWork used as example) gets books with matching publisher
select *
from (publisher join book_pub on publisher.id = book_pub.p_id) join book on book.id = b_id
where publisher = 'BookWork';

--(200 used as example) gets books with minimum requirement pages 
select *
from book
where pages >= '200';

--(10 used as example) gets books with maximum price
select *
from book
where price <= '10';

--gets book with matching ID
select *
from book
where id = 'B-1';

--gets genre of books with total sold per genre
select genre, sum(sold)
from book
group by genre;

--gets genre of books with total earned per genre
select genre, sum(sold*price)
from book
group by genre;

--gets author of books with total sold per author
select name, sum(sold)
from (author join auth_book on author.id = auth_book.a_id) join book on b_id = book.id
group by name;

--gets total earned from sales
select sum(sold*price)
from book;

