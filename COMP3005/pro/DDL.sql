create table Author
	(ID		varchar(15),
	 Name		varchar(30),
	 primary key (ID)
	);


create table Book
	(ID		varchar(15), 
	 Title			varchar(50) not null, 
	 Genre		varchar(20),
	 Quantity	numeric(3,0),
	 Sold		numeric(5,0),
	 Revenue_per		numeric(2,0),
	 Pages		numeric(4,0) check (Pages > 0),
	 Price		numeric(5,2) check (Price > 0),
	 primary key (ID)
	);

create table Auth_Book
	(b_ID		varchar(15), 
	 a_ID		varchar(15), 
	 primary key (b_ID),
	 foreign key (b_ID) references Book (ID)
		on delete cascade,
	 foreign key (a_ID) references Author (ID)
		on delete set null
	);


create table Publisher
	(ID		varchar(15), 
     Name			varchar(15) not null,
	 Email		varchar(50), 
	 BankAccount		numeric(10,2),
	 primary key (ID)
	);

create table Book_Pub
	(b_ID			varchar(15), 
	 p_ID			varchar(15), 
	 primary key (b_ID),
	 foreign key (b_ID) references Book (ID)
		on delete cascade,
	 foreign key (p_ID) references Publisher (ID)
		on delete set null
	);

create table PhoneNumber
	(Number			varchar(13), 
	 ID		varchar(15),
	 primary key (Number),
	 foreign key (ID) references Publisher
		on delete set null
	);

create table Address
	(Country			varchar(30), 
	 Province			varchar(30), 
	 City		varchar(30), 
	 Street		varchar(30),
	 Number		numeric(5,0),
	 primary key (Country, Province, City, Street, Number)
	);

create table Purchase
	(Order_Number			varchar(15), 
	 Location				varchar(20),
	 Billing_Info			varchar(30), 
	 primary key (Order_Number)
	);

create table Order_Add
	(Order_Number			varchar(15),
	 Country			varchar(30), 
	 Province			varchar(30), 
	 City		varchar(30), 
	 Street		varchar(30),
	 Number		numeric(5,0),
	 primary key (Order_Number),
	 foreign key (Order_Number) references Purchase
		on delete cascade,
	 foreign key (Country, Province, City, Street, Number) references Address
		on delete set null
	);

create table Account
	(Username		varchar(20),
	 Password		varchar(20),
	 Owner_Bool		boolean,
	 primary key (Username)
	);

create table Account_Order
	(Username		varchar(20), 
	 Order_Number		varchar(15),
	 primary key (Order_Number),
	 foreign key (Order_Number) references Purchase
		on delete cascade,
	 foreign key (Username) references Account
		on delete set null
	);
	
create table Order_Book
	(b_id		varchar(15), 
	 Order_Number		varchar(15),
	 primary key (Order_Number),
	 foreign key (Order_Number) references Purchase
		on delete cascade,
	 foreign key (b_id) references Book (ID)
		on delete set null
	);
	
create table Account_Add
	(Username			varchar(20),
	 Country			varchar(30), 
	 Province			varchar(30), 
	 City		varchar(30), 
	 Street		varchar(30),
	 Number		numeric(5,0),
	 primary key (Username),
	 foreign key (Username) references Account
		on delete cascade,
	 foreign key (Country, Province, City, Street, Number) references Address
		on delete set null
	);
	
create table Pub_Add
	(p_id			varchar(15),
	 Country			varchar(30), 
	 Province			varchar(30), 
	 City		varchar(30), 
	 Street		varchar(30),
	 Number		numeric(5,0),
	 primary key (p_id),
	 foreign key (p_id) references Publisher
		on delete cascade,
	 foreign key (Country, Province, City, Street, Number) references Address
		on delete set null
	);


Insert into author
values ('A-1','J. R. R. Tolkien');

insert into book
values ('B-1', 'The Lord of the Rings: The Fellowship of the Ring', 'Fantasy', 100, 10, 50, 423, 10.88);

insert into auth_book (b_id, a_id)
values ('B-1', 'A-1');

insert into publisher
values ('P-1', 'Allen & Unwin', 'internationalsales@allenandunwin.com', 300000.00);

insert into book_pub
values ('B-1', 'P-1');

insert into book
values ('B-2', 'The Lord of the Rings: The Two Towers', 'Fantasy', 104, 6, 50, 352, 14.99);

insert into auth_book (b_id, a_id)
values ('B-2', 'A-1');

insert into book
values ('B-3', 'The Lord of the Rings: The Return of The King', 'Fantasy', 50, 7, 50, 416, 7.99);

insert into auth_book (b_id, a_id)
values ('B-3', 'A-1');

insert into book
values ('B-4', 'The Hobbit, or There and Back Again', 'Fantasy', 68, 17, 50, 310, 15.99);

insert into auth_book (b_id, a_id)
values ('B-4', 'A-1');

insert into author
values('A-2', 'J. K. Rowling');

insert into book
values ('B-5', 'Harry Potter and the Philosophers Stone', 'Fantasy', 160, 30, 65, 223, 10.99);

insert into auth_book (b_id, a_id)
values ('B-5', 'A-2');

insert into publisher
values ('P-2', 'Gen Publishing', 'GenPublishing@genpublishing.com', 500000.00);

insert into book_pub
values ('B-5', 'P-2');

insert into book
values ('B-6', 'Harry Potter and the Chamber of Secrets', 'Fantasy', 48, 5, 65, 251, 10.99);

insert into auth_book (b_id, a_id)
values ('B-6', 'A-2');

insert into book_pub
values ('B-6', 'P-2');

insert into book
values ('B-7', 'Harry Potter and the Prisoner of Azkaban', 'Fantasy', 25, 1, 65, 317, 10.59);

insert into auth_book (b_id, a_id)
values ('B-7', 'A-2');

insert into book_pub
values ('B-7', 'P-2');

insert into book
values ('B-8', 'Harry Potter and the Goblet of Fire', 'Fantasy', 78, 20, 65, 636, 14.00);

insert into auth_book (b_id, a_id)
values ('B-8', 'A-2');

insert into book_pub
values ('B-8', 'P-2');

insert into book
values ('B-9', 'Harry Potter and the Order of the Pheonix', 'Fantasy', 59, 8, 65, 766, 15.79);

insert into auth_book (b_id, a_id)
values ('B-9', 'A-2');

insert into book_pub
values ('B-9', 'P-2');

insert into book
values ('B-10', 'Harry Potter and the Half-Blood Prince', 'Fantasy', 36, 3, 65, 607, 8.99);

insert into auth_book (b_id, a_id)
values ('B-10', 'A-2');

insert into book_pub
values ('B-10', 'P-2');

insert into book
values ('B-11', 'Harry Potter and the Deathly Hallows', 'Fantasy', 48, 16, 65, 607, 20.99);

insert into auth_book (b_id, a_id)
values ('B-11', 'A-2');

insert into book_pub
values ('B-11', 'P-2');

insert into author
values('A-3', 'Ernest Cline');

insert into book
values ('B-12', 'Ready Player One', 'Science Fiction', 36, 3, 70, 374, 13.99);

insert into auth_book (b_id, a_id)
values ('B-12', 'A-3');

insert into book_pub
values ('B-12', 'P-1');

insert into book
values ('B-13', 'Armada', 'Science Fiction', 13, 0, 45, 368, 20.00);

insert into auth_book (b_id, a_id)
values ('B-13', 'A-3');

insert into book_pub
values ('B-13', 'P-1');

insert into author
values('A-4', 'Frank Herbert');

insert into book
values ('B-14', 'Dune', 'Science Fiction', 134, 39, 60, 412, 21.99);

insert into publisher
values ('P-3', 'BookWork', 'business@bookworm.com', 150000.00);

insert into auth_book (b_id, a_id)
values ('B-14', 'A-4');

insert into book_pub
values ('B-14', 'P-3');

insert into book
values ('B-15', 'Dune Messiah', 'Science Fiction', 69, 25, 40, 256, 19.49);

insert into auth_book (b_id, a_id)
values ('B-15', 'A-4');

insert into book_pub
values ('B-15', 'P-3');

insert into phonenumber
values ('800-560-9000','P-1');

insert into phonenumber
values ('800-560-9001','P-1');

insert into phonenumber
values ('800-555-1559','P-2');

insert into phonenumber
values ('800-123-4567','P-3');

insert into phonenumber
values ('800-456-7890','P-3');

insert into address
values ('United States','California','Los Angeles','fake st.',400);

insert into pub_add
values ('P-1','United States','California','Los Angeles','fake st.',400);

insert into address
values ('Canada','Ontario','Toronto','Younge st.',590);

insert into pub_add
values ('P-2','Canada','Ontario','Toronto','Younge st.',590);

insert into address
values ('United States','New York','New York','Carleton st.',1000);

insert into pub_add
values ('P-3','United States','New York','New York','Carleton st.',1000);

insert into account
values ('Daniel','Lawrence',true);

insert into account
values ('Issac','password',false);

insert into address
values ('Canada','Ontario','Ottawa','Sussex dr.',24);

insert into account_add
values ('Daniel','Canada','Ontario','Ottawa','Sussex dr.',24);

insert into address
values ('Canada','Ontario','Ottawa','Ottawa st.',1234);

insert into account_add
values ('Issac','Canada','Ontario','Ottawa','Ottawa st.',1234);



