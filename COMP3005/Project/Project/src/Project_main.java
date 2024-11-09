import java.sql.*;
import java.util.Scanner;
import java.text.DecimalFormat;
import java.util.ArrayList;

public class Project_main {
    public static String CurUser;
    public static Boolean CurOwner;
    public static String database = "Project"; // Enter name of database
    public static String userid = "postgres"; // Enter userid
    public static String passwd = "pikachu88"; // Enter password

    public static void main(String[] args) {

        Control();

    }

    public static void Control(){
        Scanner r = new Scanner(System.in);
        while (true) {
            System.out.println("Select Option \n 1.LOGIN \n 2.CREATE ACCOUNT \n 0.exit");
            String c = r.next();
            if (c.equals("1")) {
                Login();
                break;
            }
            if (c.equals("2")) {
                CreateUser();
                break;
            }
            if (c.equals("0")) {
                break;
            }
        }
    }

    public static void Login(){
        Scanner r = new Scanner(System.in);
        boolean user = false;
        boolean pass = false;
        boolean owner = false;
        int i = 1;
        while (i == 1){
            System.out.println("Please enter username");
            String username = r.next();
            System.out.println("Please enter password");
            String password = r.next();

            try {
                Class.forName("org.postgresql.Driver");
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }

            try (Connection conn = DriverManager.getConnection(
                            "jdbc:postgresql://localhost:5432/" + database,
                    userid, passwd)) {
                try (Statement stmt = conn.createStatement()) {
                    ResultSet rset = stmt.executeQuery(
                            "select username " +
                                    "from Account;");
                    while (rset.next()) {
                        if (rset.getString("username").equals(username)) {
                            user = true;
                        }
                        //System.out.println(rset.getString("password"));
                        //System.out.println(rset.getBoolean("owner_bool"));
                    }
                    rset = stmt.executeQuery(
                            "select password, owner_bool " +
                                    "from Account " +
                                    "where username = '" + username + "';");
                    while (rset.next()) {
                        if (rset.getString("password").equals(password)) {
                            pass = true;
                            owner = rset.getBoolean("owner_bool");
                        }
                        //System.out.println(rset.getString("password"));
                        //System.out.println(rset.getBoolean("owner_bool"));
                    }

                }
            } catch (Exception sqle) {
                System.out.println("Exception: " + sqle);
            }

            if (user && pass) {
                i = 0;
                CurUser = username;
                CurOwner = owner;
                Welcome();
            } else {
                System.out.println("Wrong username or password");
            }
        }
    }

    public static void CreateUser(){
        Scanner r = new Scanner(System.in);
        while(true) {
            System.out.println("Please enter username");
            String username = r.next();
            System.out.println("Please enter password");
            String password = r.next();
            try {
                Class.forName("org.postgresql.Driver");
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }

            try (
                    Connection conn = DriverManager.getConnection(
                            "jdbc:postgresql://localhost:5432/Project",
                            userid, passwd);
                    Statement stmt = conn.createStatement();
            ) {
                try {
                    stmt.executeUpdate(
                            "insert into account values ('" + username + "', '" + password + "',false);"
                    );
                    break;
                } catch (SQLException sqle) {
                    System.out.println("could not add user; try another username");
                }
            } catch (Exception sqle) {
                System.out.println("Exception: " + sqle);
            }
        }
        System.out.println("User Created!");
        System.out.println("Going to Login");
        Login();
    }

    public static void Welcome(){
        System.out.println("Welcome " + CurUser);
        if(CurOwner){
            System.out.println("You are an owner");
        }else{
            System.out.println("You are not an owner");
        }

        Scanner r = new Scanner(System.in);
        while (true) {
            System.out.println("What would you like to do? \n 1.Browse Books \n 2.Go to Checkout \n 3.Link Address \n 4.Logout");
            if (CurOwner){
                System.out.println(" 5.Order More Books \n 6.Generate Reports");
            }
            String c = r.next();
            if (c.equals("1")) {
                Browse();
                break;
            }
            if (c.equals("2")) {
                Checkout();
                break;
            }
            if (c.equals("3")) {
                Link_address();
                break;
            }
            if (c.equals("4")) {
                Logout();
                break;
            }
            if (c.equals("5")) {
                if(CurOwner){
                    addBook();
                    break;
                }
            }
            if (c.equals("6")) {
                if(CurOwner){
                    GenerateReports();
                    break;
                }
            }
        }
    }

    public static void Browse(){
        Scanner r = new Scanner(System.in).useDelimiter("\n");
        while (true) {
            System.out.println("How would you like to browse by? \n 1.Author \n 2.Genre \n 3.Publisher \n 4.Number of pages \n 5.Price \n 6.Go back to main menu");

            String c = r.next();
            if (c.equals("1")) {
                System.out.println("Which book author are you looking for?");
                String author = r.next();
                try (
                        Connection conn = DriverManager.getConnection(
                                "jdbc:postgresql://localhost:5432/" + database,
                                userid, passwd);
                        Statement stmt = conn.createStatement();
                ) {
                    ResultSet rset = stmt.executeQuery(
                            "select * " +
                                    "from (author join auth_book on author.id = auth_book.a_id) join book on b_id = book.id " +
                                    "where name = '" + author + "';");
                    if (!rset.isBeforeFirst()) {
                        System.out.println("No books with matching author.");
                    }else {
                        System.out.println("Books in store: ");
                        while (rset.next()) {
                            System.out.println("ID: " + rset.getString("id") +
                                    " | Title: " + rset.getString("title") +
                                    " | Genre: " + rset.getString("genre") +
                                    " | Available in store: " + rset.getString("quantity") +
                                    " | # of pages: " + rset.getString("pages") +
                                    " | Price: $" + rset.getString("price"));
                        }
                    }

                } catch (Exception sqle) {
                    System.out.println("Exception: " + sqle);
                }
            }
            if (c.equals("2")) {
                System.out.println("Which genre are you looking for?");
                String genre = r.next();
                try (
                        Connection conn = DriverManager.getConnection(
                                "jdbc:postgresql://localhost:5432/" + database,
                                userid, passwd);
                        Statement stmt = conn.createStatement();
                ) {
                    ResultSet rset = stmt.executeQuery(
                            "select * " +
                                    "from book " +
                                    "where genre = '" + genre + "';");
                    if (rset.next()) {
                        System.out.println("No books with matching genre.");
                    }else {
                        System.out.println("Books in store: ");
                        while (rset.next()) {
                            System.out.println("ID: " + rset.getString("id") +
                                    " | Title: " + rset.getString("title") +
                                    " | Genre: " + rset.getString("genre") +
                                    " | Available in store: " + rset.getString("quantity") +
                                    " | # of pages: " + rset.getString("pages") +
                                    " | Price: $" + rset.getString("price"));
                        }
                    }


                } catch (Exception sqle) {
                    System.out.println("Exception: " + sqle);
                }
            }
            if (c.equals("3")) {
                System.out.println("What publisher are you looking for?");
                String publisher = r.next();
                try (
                        Connection conn = DriverManager.getConnection(
                                "jdbc:postgresql://localhost:5432/" + database,
                                userid, passwd);
                        Statement stmt = conn.createStatement();
                ) {
                    ResultSet rset = stmt.executeQuery(
                            "select * " +
                                    "from (publisher join book_pub on publisher.id = book_pub.p_id) join book on book.id = b_id " +
                                    "where publisher = '" + publisher + "';");
                    if (!rset.isBeforeFirst()) {
                        System.out.println("No books with matching publisher.");
                    }else {
                        System.out.println("Books in store: ");
                        while (rset.next()) {
                            System.out.println("ID: " + rset.getString("id") +
                                    " | Title: " + rset.getString("title") +
                                    " | Genre: " + rset.getString("genre") +
                                    " | Available in store: " + rset.getString("quantity") +
                                    " | # of pages: " + rset.getString("pages") +
                                    " | Price: $" + rset.getString("price"));
                        }
                    }

                } catch (Exception sqle) {
                    System.out.println("Exception: " + sqle);
                }
            }
            if (c.equals("4")) {
                System.out.println("At least how many pages for each book are you looking for?");
                String pages = r.next();
                try (
                        Connection conn = DriverManager.getConnection(
                                "jdbc:postgresql://localhost:5432/" + database,
                                userid, passwd);
                        Statement stmt = conn.createStatement();
                ) {
                    ResultSet rset = stmt.executeQuery(
                            "select * " +
                                    "from book " +
                                    "where pages >= '" + pages + "';");
                    if (!rset.isBeforeFirst()) {
                        System.out.println("No books with matching pages.");
                    }else {
                        System.out.println("Books in store: ");
                        while (rset.next()) {
                            System.out.println("ID: " + rset.getString("id") +
                                    " | Title: " + rset.getString("title") +
                                    " | Genre: " + rset.getString("genre") +
                                    " | Available in store: " + rset.getString("quantity") +
                                    " | # of pages: " + rset.getString("pages") +
                                    " | Price: $" + rset.getString("price"));
                        }
                    }

                } catch (Exception sqle) {
                    System.out.println("Exception: " + sqle);
                }
            }
            if (c.equals("5")) {
                System.out.println("What is the max price?");
                String price = r.next();
                try (
                        Connection conn = DriverManager.getConnection(
                                "jdbc:postgresql://localhost:5432/" + database,
                                userid, passwd);
                        Statement stmt = conn.createStatement();
                ) {
                    ResultSet rset = stmt.executeQuery(
                            "select * " +
                                    "from book " +
                                    "where price <= '" + price + "';");
                    if (!rset.isBeforeFirst()) {
                        System.out.println("No books with matching price.");
                    }else {
                        System.out.println("Books in store: ");
                        while (rset.next()) {
                            System.out.println("ID: " + rset.getString("id") +
                                    " | Title: " + rset.getString("title") +
                                    " | Genre: " + rset.getString("genre") +
                                    " | Available in store: " + rset.getString("quantity") +
                                    " | # of pages: " + rset.getString("pages") +
                                    " | Price: $" + rset.getString("price"));
                        }
                    }

                } catch (Exception sqle) {
                    System.out.println("Exception: " + sqle);
                }
            }
            if (c.equals("6")) {
                Welcome();
                break;
            }
        }
    }

    public static void Checkout() {
        ArrayList<String> basket = new ArrayList<>();

        // User inputted checkout
        Scanner r = new Scanner(System.in);
        System.out.println("Please input the ID of all the book you'd like to add to the cart! Input Checkout to finish and checkout.");

        while (true) {

            String id = r.next();
            if (id.equalsIgnoreCase("Checkout")) {
                break;
            }

            try (Connection conn = DriverManager.getConnection(
                            "jdbc:postgresql://localhost:5432/" + database,
                            userid, passwd);
                 Statement stmt = conn.createStatement();) {
                ResultSet rset = stmt.executeQuery(
                        "select * " +
                                "from book " +
                                "where id = '" + id + "';");
                if (!rset.isBeforeFirst()) {
                    System.out.println("No books with that ID.");
                }else {
                    rset.next();
                    basket.add(rset.getString("id"));
                }
            } catch (Exception sqle) {
                System.out.println("Exception: " + sqle);
            }
        }

        if (basket.size() == 0){
            System.out.println("No books in basket to be checked out, returning to main menu.");
        } else {
            // Checking out books
            for (int i = 0; i < basket.size(); ++i){
                System.out.println("Checking out: ");

                try (Connection conn = DriverManager.getConnection(
                        "jdbc:postgresql://localhost:5432/" + database,
                        userid, passwd);
                     Statement stmt = conn.createStatement();) {
                    ResultSet rset = stmt.executeQuery(
                            "select * " +
                                    "from book " +
                                    "where id = '" + basket.get(i) + "';");
                    while (rset.next()) {
                        System.out.println("ID: " + rset.getString("id") +
                                " | Title: " + rset.getString("title") +
                                " | Genre: " + rset.getString("genre") +
                                " | Available in store: " + rset.getString("quantity") +
                                " | # of pages: " + rset.getString("pages") +
                                " | Price: $" + rset.getString("price"));
                    }

                    // Create order

                    // Update book quantity

                    // Update book sold amount

                    // Update Publisher bank account

                } catch (Exception sqle) {
                    System.out.println("Exception: " + sqle);
                }
            }
        }

        // Return to main menu
        Welcome();
    }

    public static void Link_address(){
        if (Check_address()) {
            while (true) {
                Scanner r = new Scanner(System.in);
                System.out.println("Please enter your Country");
                String Country = r.next();
                System.out.println("enter your Province/State");
                String Province = r.next();
                System.out.println("enter your City");
                String City = r.next();
                System.out.println("enter your Street");
                String Street = r.next();
                int number;
                while (true) {
                    System.out.println("enter your Number");
                    String NumStr = r.next();
                    try {
                        number = Integer.parseInt(NumStr);
                        break;
                    } catch (NumberFormatException ex) {
                        System.out.println("Please enter a valid Number");
                    }
                }
                try {
                    Class.forName("org.postgresql.Driver");
                } catch (ClassNotFoundException e) {
                    e.printStackTrace();
                }

                try (Connection conn = DriverManager.getConnection(
                        "jdbc:postgresql://localhost:5432/Project",
                        userid, passwd)) {
                    try (Statement stmt = conn.createStatement()) {
                        try {
                            stmt.executeUpdate(
                                    "insert into address values ('" + Country + "', '" + Province + "', '" + City + "','" + Street + "','" + number + "');"
                            );

                        } catch (SQLException sqle) {
                            System.out.println("Please enter a valid Address");
                        }
                        try {
                            stmt.executeUpdate(
                                    "insert into account_add values ('" + CurUser + "','" + Country + "', '" + Province + "', '" + City + "','" + Street + "','" + number + "');"
                            );

                        } catch (SQLException sqle) {
                            System.out.println("Please enter a valid Address");
                        }
                        break;
                    }
                } catch (Exception sqle) {
                    System.out.println("Exception: " + sqle);
                }
            }
            System.out.println("Address Successfully Linked!");
        }
        Welcome();
    }

    public static boolean Check_address(){
        Scanner r = new Scanner(System.in);
        boolean hasAddress = false;
        String Country = "";
        String Province = "";
        String City = "";
        String Street = "";
        int Number  = 0;
        try {
            Class.forName("org.postgresql.Driver");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }

        try (Connection conn = DriverManager.getConnection(
                "jdbc:postgresql://localhost:5432/Project",
                userid, passwd)) {
            try (Statement stmt = conn.createStatement()) {
                ResultSet rset = stmt.executeQuery(
                        "select username " +
                                "from Account_add;");
                while (rset.next()) {
                    if (rset.getString("username").equals(CurUser)) {
                        hasAddress = true;
                    }
                }
                if (hasAddress) {
                    while (true) {
                        System.out.println("You already have an address linked. Overwrite it? \n 1.Yes \n 2.No");
                        String c = r.next();
                        if (c.equals("1")) {
                            rset = stmt.executeQuery(
                                    "select Country, province, city, street, number " +
                                            "from Account_add " +
                                            "where username = '" + CurUser + "';");
                            while (rset.next()) {
                                Country = rset.getString("Country");
                                Province = rset.getString("Province");
                                City = rset.getString("City");
                                Street = rset.getString("Street");
                                Number = rset.getInt("Number");
                            }
                            try {
                                stmt.executeUpdate(
                                        "delete from address " +
                                                "where (country, province, city, street, number) = " +
                                                "(('" + Country + "', '" + Province + "','" + City + "','" + Street + "', " + Number + "));"
                                );
                            } catch (SQLException sqle) {
                                System.out.println("could not delete address");
                                break;
                            }
                            try {
                                stmt.executeUpdate(
                                        "delete from account_add " +
                                                "where username = '" + CurUser + "';"
                                );
                            } catch (SQLException sqle) {
                                System.out.println("could not delete account address");
                                break;
                            }
                            break;
                        } else if (c.equals("2")) {
                            return false;
                        } else {
                            System.out.println("Please enter a valid option");
                        }
                    }
                } else {
                    return true;
                }


            }
        } catch (Exception sqle) {
            System.out.println("Exception: " + sqle);
        }

        return true;
    }

    public static void addBook(){
        Scanner r = new Scanner(System.in);
        while (true) {
            System.out.println("Order a... \n 1.Pre-existing book \n 2.New book");
            String c = r.next();
            if (c.equals("1")) {
                ExistingBook();
                break;
            }
            if (c.equals("2")) {
                NewBook();
                break;
            }

        }
    }

    public static void ExistingBook(){
        Scanner r = new Scanner(System.in);
        try {
            Class.forName("org.postgresql.Driver");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }

        try (Connection conn = DriverManager.getConnection(
                "jdbc:postgresql://localhost:5432/Project",
                userid, passwd)) {
            try (Statement stmt = conn.createStatement()) {
                ResultSet rset = stmt.executeQuery(
                        "select count(ID) " +
                                "from book; ");
                int totbook = 0;
                while (rset.next()) {
                    totbook = rset.getInt("count");
                }

                String[] Books = new String[totbook];
                rset = stmt.executeQuery(
                        "select ID, title " +
                                "from Book; ");
                int i = 0;

                while (rset.next()) {
                    Books[i] = rset.getString("ID");
                    System.out.println(" " + (i + 1) + "." + Books[i] + " " + rset.getString("title"));
                    i++;
                }

                while (true) {
                    String c = r.next();
                    int test;
                    try {
                        test = Integer.parseInt(c);
                    } catch (NumberFormatException ex) {
                        System.out.println("Please enter a valid option");
                        continue;
                    }

                    if (test <= (i + 1) && test > 0) {
                        int Add;
                        while (true) {
                            System.out.println("Order How many?");
                            String AddStr = r.next();
                            try {
                                Add = Integer.parseInt(AddStr);
                                if(!(Add > 0)){
                                    System.out.println("enter a number valid Number");
                                    continue;
                                }
                                break;
                            } catch (NumberFormatException ex) {
                                System.out.println("Please enter a valid Number");
                            }
                        }
                        rset = stmt.executeQuery(
                                "select quantity " +
                                        "from Book " +
                                        "where ID = '"+Books[test-1]+"'; ");
                        int Quan = 0;
                        while (rset.next()) {
                            Quan = rset.getInt("quantity");
                        }
                        try {
                            stmt.executeUpdate(
                                    "Update Book " +
                                            "Set quantity = "+ (Quan + Add) + " " +
                                            "where ID = '" + Books[test-1] + "';"
                            );
                        } catch (SQLException sqle) {
                            System.out.println("Could not add Book");
                        }
                        break;
                    } else {
                        System.out.println("Please enter a valid option");
                    }
                }
            }
        } catch (Exception sqle) {
            System.out.println("Exception: " + sqle);
        }
        System.out.println("Successfully ordered new books");
    }

    public static void NewBook(){
        Scanner r = new Scanner(System.in).useDelimiter("\n");
        String Title;
        String Genre;
        int Quantity;
        int Revenue_per;
        int Pages;
        double Price;
        String Author;
        while (true) {
            System.out.println("Please enter The books Title");
            Title = r.next();
            if(!(Title.length() <= 50 && Title.length() > 0)){
                System.out.println("Enter a Title less than 50 characters");
            }else{
                break;
            }
        }
        while (true) {
            System.out.println("enter the genre");
            Genre = r.next();
            if(Genre.length() > 30){
                System.out.println("Enter a genre less than 20 characters");
            }else{
                break;
            }
        }
        System.out.println("Quantity");
        while (true) {
            String QuanStr = r.next();
            try {
                Quantity = Integer.parseInt(QuanStr);
                if(Quantity <= 0){
                    System.out.println("enter a number greater then 0");
                    continue;
                }
                break;
            } catch (NumberFormatException ex) {
                System.out.println("Please enter a valid Number");
            }
        }
        System.out.println("Revenue percent (out of 100) to publisher");
        while (true) {
            String RevStr = r.next();
            try {
                Revenue_per = Integer.parseInt(RevStr);
                if(!(Revenue_per > 0 && Revenue_per <= 100)){
                    System.out.println("enter a number between 0 and 100");
                    continue;
                }
                break;
            } catch (NumberFormatException ex) {
                System.out.println("Please enter a valid Number");
            }
        }
        System.out.println("Page count");
        while (true) {
            String PageStr = r.next();
            try {
                Pages = Integer.parseInt(PageStr);
                if(!(Pages > 0 && Pages <= 9999)){
                    System.out.println("enter a number between 1 and 9999");
                    continue;
                }
                break;
            } catch (NumberFormatException ex) {
                System.out.println("Please enter a valid Number");
            }
        }
        System.out.println("Price");
        while (true) {
            String PriceStr = r.next();
            try {
                Price = Double.parseDouble(PriceStr);
                DecimalFormat df = new DecimalFormat("0.00");
                PriceStr = df.format(Price);
                Price = Double.parseDouble(PriceStr);
                if(Price <= 0.00){
                    System.out.println("enter a number greater than 0");
                    continue;
                }
                break;
            } catch (NumberFormatException ex) {
                System.out.println("Please enter a valid Number");
            }
        }
        int BookID = 0;
        try {
            Class.forName("org.postgresql.Driver");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }

        try (Connection conn = DriverManager.getConnection(
                "jdbc:postgresql://localhost:5432/Project",
                userid, passwd)) {
            try (Statement stmt = conn.createStatement()) {
                ResultSet rset = stmt.executeQuery(
                        "select count(ID) " +
                                "from book;");
                while (rset.next()) {
                    BookID = rset.getInt("count");
                }
                try {
                    stmt.executeUpdate(
                            "insert into book values ( 'B-" + (BookID + 1) + "','" + Title + "','" + Genre + "'," + Quantity + ",0," + Revenue_per + "," + Pages + "," + Price + ");"
                    );
                } catch (SQLException sqle) {
                    System.out.println("Could not add Book");
                }
            }
        } catch (Exception sqle) {
            System.out.println("Exception: " + sqle);
        }
        while (true) {
            System.out.println("Author");
            Author = r.next();
            if(Author.contains("'")){
                System.out.println("Please Enter Valid name");
            }else if(Author.length() > 30 ){
                System.out.println("Please name of less then 30 characters");
            }else{
                break;
            }
        }
        int i = 0;
        try {
            Class.forName("org.postgresql.Driver");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }

        try (Connection conn = DriverManager.getConnection(
                "jdbc:postgresql://localhost:5432/Project",
                userid, passwd)) {
            try (Statement stmt = conn.createStatement()) {
                ResultSet rset = stmt.executeQuery(
                        "select count(ID) " +
                                "from author " +
                                "where name = '" + Author + "';");
                while (rset.next()) {
                    i = rset.getInt("count");
                }
                if (i > 1) {
                    String[] NumAuth = new String[i];
                    System.out.println("Which by that name?");
                    rset = stmt.executeQuery(
                            "select ID " +
                                    "from author " +
                                    "where name = '" + Author + "';");
                    int j = 0;
                    while (rset.next()) {
                        NumAuth[j] = rset.getString("ID");
                        System.out.println(" " + (j + 1) + "." + NumAuth[j]);
                        j++;
                    }
                    System.out.println(" 0.New Author");
                    while (true) {
                        String c = r.next();
                        int test;
                        try {
                            test = Integer.parseInt(c);
                        } catch (NumberFormatException ex) {
                            System.out.println("Please enter a valid option");
                            continue;
                        }
                        if (c.equals("0")) {
                            newAuthor(Author, BookID);
                        } else if (!(test <= (j + 1) && test >= 0)) {
                            System.out.println("Please enter a valid option");
                        } else {
                            System.out.println("('B-" + (BookID + 1) + "','" + NumAuth[test - 1] + "');");
                            try {
                                stmt.executeUpdate(
                                        "insert into auth_book values ('B-" + (BookID + 1) + "','" + NumAuth[test - 1] + "');"
                                );
                            } catch (SQLException sqle) {
                                System.out.println("Could not add Book To Author");
                            }
                            break;
                        }
                    }
                } else if (i == 1) {
                    System.out.println("Which Author by that name? \n 1.Already existing one \n 2.new one");
                    String c = r.next();
                    while (true) {
                        if (c.equals("1")) {
                            String AuthorID = "";
                            rset = stmt.executeQuery(
                                    "select ID " +
                                            "from author " +
                                            "where name = '" + Author + "';");
                            while (rset.next()) {
                                AuthorID = rset.getString("ID");
                            }
                            try {
                                stmt.executeUpdate(
                                        "insert into auth_book values ('B-" + (BookID + 1) + "','" + AuthorID + "');"
                                );
                            } catch (SQLException sqle) {
                                System.out.println("Could not add Author to Book");
                            }
                            break;
                        }
                        if (c.equals("2")) {
                            newAuthor(Author, BookID);
                            break;
                        }
                    }
                } else {
                    newAuthor(Author, BookID);
                }
                while (true) {
                    System.out.println("Is the publisher... \n 1.new \n 2.a repeat");
                    String c = r.next();
                    if (c.equals("1")) {
                        String PubName;
                        String PubEmail;
                        double PubAcc;
                        String[] PhoneNums;
                        String Country;
                        String Province;
                        String City;
                        String Street;
                        int Number;
                        while (true) {
                            System.out.println("Whats the name of the publisher");
                            PubName = r.next();
                            if(PubName.contains("'")){
                                System.out.println("Please enter a valid name");
                            }else if(PubName.length() > 15){
                                System.out.println("Please enter a name of less then 15 characters");
                            }else{
                                break;
                            }
                        }
                        while (true){
                            System.out.println("Whats the email of the publisher");
                            PubEmail = r.next();
                            if(PubEmail.contains("'")){
                                System.out.println("Please enter a valid email");
                            }else if(PubEmail.length() > 50){
                                System.out.println("Please enter an email of less then 50 characters");
                            }else{
                                break;
                            }
                        }
                        while (true) {
                            System.out.println("Whats the bank account of the publisher");
                            String BankStr = r.next();
                            try {
                                PubAcc = Double.parseDouble(BankStr);
                                DecimalFormat df = new DecimalFormat("0.00");
                                BankStr = df.format(PubAcc);
                                PubAcc = Double.parseDouble(BankStr);
                                if(!(PubAcc > 0.00 && PubAcc < 100000000.00)){
                                    System.out.println("enter a number greater than 0 and less then 100,000,000");
                                    continue;
                                }
                                break;
                            } catch (NumberFormatException ex) {
                                System.out.println("Please enter a valid Number");
                            }
                        }
                        while (true) {
                            System.out.println("How many phone Number do they have?");
                            String PhoneStr = r.next();
                            try{
                                int NumPhone = Integer.parseInt(PhoneStr);
                                PhoneNums = new String[NumPhone];
                                for(int k = 0; k < NumPhone; k++){
                                    while (true) {
                                        System.out.println("Enter phone number # " + k);
                                        PhoneNums[k] = r.next();
                                        if(PhoneNums[k].length() > 13){
                                            System.out.println("Please enter less then 13 characters");
                                        }else{
                                            break;
                                        }
                                    }
                                }
                                break;
                            }catch (NumberFormatException ex) {
                                System.out.println("Please enter a valid Number");
                            }
                        }
                        while (true){
                            System.out.println("Country");
                            Country = r.next();
                            if(Country.contains("'")){
                                System.out.println("Please enter a valid Country");
                            }else if(Country.length() > 30){
                                System.out.println("Please enter an Country of less then 30 characters");
                            }else{
                                break;
                            }
                        }
                        while (true){
                            System.out.println("Province / State");
                            Province = r.next();
                            if(Province.contains("'")){
                                System.out.println("Please enter a valid Province");
                            }else if(Province.length() > 30){
                                System.out.println("Please enter an Province of less then 30 characters");
                            }else{
                                break;
                            }
                        }
                        while (true){
                            System.out.println("City");
                            City = r.next();
                            if(City.contains("'")){
                                System.out.println("Please enter a valid City");
                            }else if(City.length() > 30){
                                System.out.println("Please enter an City of less then 30 characters");
                            }else{
                                break;
                            }
                        }
                        while (true){
                            System.out.println("Street");
                            Street = r.next();
                            if(Street.contains("'")){
                                System.out.println("Please enter a valid Street");
                            }else if(Street.length() > 30){
                                System.out.println("Please enter an Street of less then 30 characters");
                            }else{
                                break;
                            }
                        }
                        while (true) {
                            System.out.println("Number");
                            String NumStr = r.next();
                            try {
                                Number = Integer.parseInt(NumStr);
                                break;
                            } catch (NumberFormatException ex) {
                                System.out.println("Please enter a valid Number");
                            }
                        }
                        rset = stmt.executeQuery(
                                "select count(id) " +
                                        "from publisher; ");
                        int totPub = 0;
                        while (rset.next()) {
                            totPub = rset.getInt("count");
                        }
                        try {
                            stmt.executeUpdate(
                                    "insert into publisher values ('P-" + (totPub+1) + "','" + PubName + "','"+ PubEmail +"','"+ PubAcc +"');"
                            );
                            for (String phoneNum : PhoneNums) {
                                stmt.executeUpdate(
                                        "insert into PhoneNumber values ('" + phoneNum + "','P-" + (totPub + 1) + "');"
                                );
                            }
                            stmt.executeUpdate(
                                    "insert into book_pub values ('B-" + (BookID + 1) + "','P-" + (totPub +1) + "');"
                            );
                            stmt.executeUpdate(
                                    "insert into Address values ('" + Country + "','" + Province + "','" + City + "','"+Street+"'," + Number + ");"
                            );
                            stmt.executeUpdate(
                                    "insert into Pub_Add values ('P-"+ (totPub + 1) +"','" + Country + "','" + Province + "','" + City + "','"+Street+"'," + Number + ");"
                            );

                        } catch (SQLException sqle) {
                            System.out.println("Could not add Publisher");
                        }
                        break;
                    } else if (c.equals("2")) {
                        System.out.println("Which Publisher?");
                        rset = stmt.executeQuery(
                                "select count(id) " +
                                        "from publisher; ");
                        int totPub = 0;
                        while (rset.next()) {
                            totPub = rset.getInt("count");
                        }
                        String[] Pubs = new String[totPub];
                        rset = stmt.executeQuery(
                                "select id, name " +
                                        "from publisher; ");
                        int j = 0;
                        while (rset.next()) {
                            Pubs[j] = rset.getString("id");
                            System.out.println(" " + (j + 1) + "." + Pubs[j] + " " + rset.getString("name"));
                            j++;
                        }
                        while (true) {
                            String c2 = r.next();
                            int test;
                            try {
                                test = Integer.parseInt(c2);
                            } catch (NumberFormatException ex) {
                                System.out.println("Please enter a valid option");
                                continue;
                            }

                            if (test <= (j + 1) && test > 0) {
                                try {
                                    stmt.executeUpdate(
                                            "insert into book_pub values ('B-" + (BookID + 1) + "','" + Pubs[test - 1] + "');"
                                    );
                                } catch (SQLException sqle) {
                                    System.out.println("Could not add Book To Publisher");
                                }
                                break;
                            } else {
                                System.out.println("Please enter a valid option");
                            }
                        }
                        break;
                    } else {
                        System.out.println("Please enter a valid option");
                    }
                }

            }
        } catch (Exception sqle) {
            System.out.println("Exception: " + sqle);
        }
        System.out.println("Successfully Added Book");
        Welcome();

    }

    public static void newAuthor(String Author, int BookID){
        try {
            Class.forName("org.postgresql.Driver");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }

        try (Connection conn = DriverManager.getConnection(
                "jdbc:postgresql://localhost:5432/Project",
                userid, passwd)) {
            try (Statement stmt = conn.createStatement()) {
                int totalAuthor = 0;
                ResultSet rset = stmt.executeQuery(
                        "select count(ID) " +
                                "from author ");
                while (rset.next()) {
                    totalAuthor = rset.getInt("count");
                }
                try {
                    stmt.executeUpdate(
                            "insert into author values ('A-" + (totalAuthor + 1) + "','" + Author + "');"
                    );
                    stmt.executeUpdate(
                            "insert into auth_book values ('B-" + (BookID + 1) + "','A-" + totalAuthor + "');"
                    );
                } catch (SQLException sqle) {
                    System.out.println("Could not add Author");
                }
            }
        } catch (Exception sqle) {
            System.out.println("Exception: " + sqle);
        }
    }

    public static void Logout(){
        CurOwner = false;
        CurUser = "";
        Control();
    }

    public static void GenerateReports(){
        System.out.println("Generating Report: ");

        // Total sold per genre
        try (Connection conn = DriverManager.getConnection(
                "jdbc:postgresql://localhost:5432/" + database,
                userid, passwd);
             Statement stmt = conn.createStatement();) {
            ResultSet rset = stmt.executeQuery(
                    "select genre, sum(sold) " +
                            "from book " +
                            "group by genre;");

            while (rset.next()) {
                System.out.println("Genre: " + rset.getString("genre") + " - Total number of sales: " + rset.getString("sum"));
            }

            // Total sales per genre
            rset = stmt.executeQuery(
                    "select genre, sum(sold*price) " +
                            "from book " +
                            "group by genre;");

            while (rset.next()) {
                System.out.println("Genre: " + rset.getString("genre") + " - Total sales: $" + rset.getString("sum"));
            }

            // Total sales per author
            rset = stmt.executeQuery(
                    "select name, sum(sold) " +
                            "from (author join auth_book on author.id = auth_book.a_id) join book on b_id = book.id " +
                            "group by name;");

            while (rset.next()) {
                System.out.println("Author: " + rset.getString("name") + " - Total number of sales: " + rset.getString("sum"));
            }

            // Total sales
            rset = stmt.executeQuery(
                    "select sum(sold*price) " +
                            "from book;");

            while (rset.next()) {
                System.out.println("Total sales: $" + rset.getString("sum"));
            }

        } catch (Exception sqle) {
            System.out.println("Exception: " + sqle);
        }

        // Return to main menu.
        Welcome();
    }
}


