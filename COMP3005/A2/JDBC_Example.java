import java.sql.*;
import java.util.Scanner;
import java.util.ArrayList;

public class JDBC_Example {
    public static void main(String[] args) {
		final String database = "School"; // Enter name of database
		final String userid = "postgres"; // Enter userid
		final String passwd = "passrd"; // Enter password
		ArrayList<String> prereqs = new ArrayList<>();
        try (Connection connection = DriverManager.getConnection("jdbc:postgresql://localhost:5432/" + database, userid, passwd);
            Statement statement = connection.createStatement();
			)
			{
			Scanner input = new Scanner(System.in);
			System.out.println("Enter the course id: ");
			String course = input.nextLine();
            
            ResultSet resultSet = statement.executeQuery("select * "
			+ "from prereq "
			+ "where course_id = '" + course + "'");
            
			while (resultSet.next()) {
                //System.out.println(resultSet.getString("prereq_id") + ", " + resultSet.getString(2));
				prereqs.add(resultSet.getString("prereq_id"));
            }
 
        } catch (Exception sqle) {
            System.out.println("Exception: " + sqle);
        }
		System.out.println("Printing out all the prerequisites: ");
		for (int i = 0; i < prereqs.size(); i ++){
			System.out.println(prereqs.get(i));
		}
    }
}

// import java.sql.*;
// import java.util.Scanner;
// import java.util.Arrays;

// public class JDBC_Example {
// 	public static void main(String[] args) {
// 		final String database = "School"; // Enter name of database
// 		final String userid = "postgres"; // Enter userid
// 		final String passwd = "pikachu88"; // Enter password
		
// 		try (Connection con = DriverManager.getConnection("jdbc:postgresql://localhost:5432/" + database, userid, passwd);
// 			Statement s = con.createStatement();
// 		)
// 		{
// 			String q;
// 			String c;
// 			ResultSet result;
// 			int maxCourse = 0;
// 			q = "select count(*) as C from course";
// 			result = s.executeQuery(q);
// 			if (!result.next())
// 				System.out.println("Unexpected empty result.");
// 			else
// 				maxCourse = Integer.parseInt(result.getString("C"));


// 			int numCourse = 0, oldNumCourse = -1;
// 			String[] prereqs = new String [maxCourse];
// 			Scanner krb = new Scanner(System.in);
// 			System.out.print("Input a course id (number): ");
// 			String course = krb.next();
// 			String courseString = "" + '\'' + course + '\'';
// 			while (numCourse != oldNumCourse) {
// 						for (int i = oldNumCourse + 1; i < numCourse; i++) {
// 							courseString += ", " + '\'' + prereqs[i] + '\'' ;
// 					}

// 					oldNumCourse = numCourse;
// 					q = "select prereq_id from prereq where course_id in ("
					
// 					+ courseString + ")";
// 					result = s.executeQuery(q);

// 					while (result.next()) {
// 						c = result.getString("prereq_id");
// 						boolean found = false;
// 						for (int i = 0; i < numCourse; i++)
// 							found |= prereqs[i].equals(c);
// 							if (!found) prereqs[numCourse++] = c;
// 				}
// 				courseString = "" + '\'' + prereqs[oldNumCourse] + '\'';
// 			}
// 			Arrays.sort(prereqs, 0, numCourse);

// 			System.out.print("The courses that must be taken prior to "+ course + " are: ");

// 			for (int i = 0; i < numCourse; i++)
// 				System.out.print ((i==0?" ":", ") + prereqs[i]);

// 			System.out.println();
// 		} catch(Exception e){
// 			e.printStackTrace();
// 		}
// 	}
// }

