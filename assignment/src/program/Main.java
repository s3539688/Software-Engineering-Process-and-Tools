package program;

//import javafx.application.Application;

public class Main
{
	public static void main(String[] args)
	{
		/*
		 * Database class should only be used once at the start, once the database is created there is no need for it
		 * start using "DatabaseConnection" once the database exists
		 * tables are to be writtien before completeion
		 */
		//Database db = new Database("company.db");
		//db.createTable("company.db");
		
		/*
		 * DatabaseConnection is to be used to connect and get data from the database
		 */
		//DatabaseConnection connect = new DatabaseConnection();
		/*
		 * other functions similar to addUser will be added later ie add booking
		 */
		//connect.addUser("William", "Apples22", 0);
		//connect.addUser("david","divad",1);
		/*
		User lame = connect.getUser("William");
		User bob = connect.getUser("david");
//		System.out.println(bob.toString());
//		System.out.println(lame.toString());
		Login login = new Login();
		login.loginMenu();
		*/
		//Controller cont = new Controller();
		//cont.addNewEmployee();
		Login menu = new Login();
		menu.loginMenu();
	}
}