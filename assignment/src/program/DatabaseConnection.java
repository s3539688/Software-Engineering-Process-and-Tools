package program;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Date;

import org.apache.log4j.Level;
import org.apache.log4j.Logger;

/*
 * follow this link to find out how to add tables and access tables
 * http://www.sqlitetutorial.net/sqlite-java/
 * this is where I will leave my sanity
 */


/**
 * Consists of functions that add and extract information to and from the database
 * @author Luke Mason, Joseph Garner
 *
 */

public class DatabaseConnection
{
	private static Logger log = Logger.getLogger(DatabaseConnection.class);
	private Controller controller = new Controller();
	public DatabaseConnection(){log.setLevel(Level.WARN);}
	private Connection connect()
	{
		/*
		 * creates a connection to the database to be used multiple times in the class
		 */
		String url = "jdbc:sqlite:"+System.getProperty("user.home")+"/resourcing/company.db";
        Connection connect = null;
        try {
            connect = DriverManager.getConnection(url);
        } catch (SQLException sqle) {
            //System.out.println(sqle.getMessage());
            log.warn(sqle.getMessage());
        }
        return connect;
	}
	
	/**
	 * Adds User to database
	 * @param username
	 * @param password
	 * @param accountType
	 */
	public void addUser(String username, String password, int accountType)
	{
		log.info("IN addUser\n");
		/*
		 * account type boolean 1 for business owner 0 for user
		 */
		String query = "INSERT INTO users(username, password, accountType) " + "VALUES('"+username+"','"+password+"','"+accountType+"')";
		try(Connection connect = this.connect(); Statement inject = connect.createStatement())
		{
			/*
			 * Sets the '?' values into the query
			 */
			inject.executeUpdate(query);
			//System.out.println("User Added");
			log.info("User Added\n");
		}
		catch(SQLException sqle)
		{
			//System.out.println(sqle.getMessage());
			log.warn(sqle.getMessage());
		}
		log.info("OUT addUser\n");
	}
	/**
	 * Adds User to database
	 * @param username
	 * @param password
	 * @param accountType
	 */
	public void addUserDetails(int id, String fname, String lname, String email, String phone, String dob, String gender)
	{
		log.info("IN addUserDetails\n");
		/*
		 * account type boolean 1 for business owner 0 for user
		 */
		String query = "INSERT INTO CLIENTDETAILS(id, FName, LName, Email, Phone, DOB, Gender) " + "VALUES("+id+",'"+fname+"','"+lname+"','"+email+"','"+phone+"','"+dob+"','"+gender+"')";
		try(Connection connect = this.connect(); Statement inject = connect.createStatement())
		{
			/*
			 * Sets the '?' values into the query
			 */
			inject.executeUpdate(query);
			//System.out.println("User Added");
			log.info("User Added\n");
		}
		catch(SQLException sqle)
		{
			//System.out.println(sqle.getMessage());
			log.warn(sqle.getMessage());
		}
		log.info("OUT addUserDetails\n");
	}
	
	/**
	 * Gets where the user name keyword matches another users name
	 * @param username
	 * @return User Object
	 */
	public User getUser(int id)
	{
		log.info("IN getUser\n");
		int _id = 0;
		String _username = "null";
		String _password = "null";
		int _accountType = 0;
		String query = "SELECT * FROM users WHERE userID = ?";
		//Creates a null user to return, this can be used to validate user at login
		User databaseUser = null;
		try (Connection connect = this.connect(); PreparedStatement  inject  = connect.prepareStatement(query))
		{
			//Sets '?' to user name in the query
			//crates a user from the found information
			inject.setInt(1,id);
			ResultSet output = inject.executeQuery();
			while (output.next()){
				_id = output.getInt(1);
				_username = output.getString(2);
				_password = output.getString(3);
				_accountType = output.getInt(4);
			}
			databaseUser = new User(_id ,_username, _password, _accountType);
			output.close();
		}
		catch(SQLException sqle)
		{
			//System.out.println("Getting User: "+sqle.getMessage());
			log.warn(sqle.getMessage());
		}
		log.info("OUT getUser\n");
		return databaseUser;
	}
	
	/**
	 * Gets where the user name keyword matches another users name
	 * @param username
	 * @return User Object
	 */
	public User getUser(String username)
	{
		log.info("IN getUser\n");
		int _id = 0;
		String _username = "null";
		String _password = "null";
		int _accountType = 0;
		String query = "SELECT * FROM users WHERE username like ?";
		//Creates a null user to return, this can be used to validate user at login
		User databaseUser = null;
		try (Connection connect = this.connect(); PreparedStatement  inject  = connect.prepareStatement(query))
		{
			//Sets '?' to user name in the query
			//crates a user from the found information
			inject.setString(1,"%" + username + "%");
			ResultSet output = inject.executeQuery();
			while (output.next()){
				_id = output.getInt(1);
				_username = output.getString(2);
				_password = output.getString(3);
				_accountType = output.getInt(4);
			}
			databaseUser = new User(_id ,_username, _password, _accountType);
			output.close();
		}
		catch(SQLException sqle)
		{
			//System.out.println("Getting User: "+sqle.getMessage());
			log.warn(sqle.getMessage());
		}
		log.info("OUT getUser\n");
		return databaseUser;
	}
	
	/**
	 * Gets where the user name keyword matches another users name
	 * @param id
	 * @return User Object
	 */
	public User getCustomer(int id)
	{
		log.info("IN getCustomer\n");
		int _id = 0;
		String _FName = "null";
		String _LName = "null";
		String _Email = "null";
		String _Phone = "null";
		String _DOB = "null";
		String _Gender = "null";
		String query = "SELECT * FROM CLIENTDETAILS WHERE id = ?";
		//Creates a null user to return, this can be used to validate user at login
		Customer databaseCustomer = null;
		try (Connection connect = this.connect(); PreparedStatement  inject  = connect.prepareStatement(query))
		{
			//Sets '?' to user name in the query
			//crates a user from the found information
			inject.setInt(1,id);
			ResultSet output = inject.executeQuery();
			while (output.next()){
				_id = output.getInt(1);
				_FName = output.getString(2);
				_LName = output.getString(3);
				_Email = output.getString(4);
				_Phone = output.getString(5);
				_DOB = output.getString(6);
				_Gender = output.getString(7);
			}
			databaseCustomer = new Customer(_id ,_FName, _LName, _Phone, _DOB, _Gender, _Email);
			output.close();
		}
		catch(SQLException sqle)
		{
			//System.out.println("Getting User: "+sqle.getMessage());
			log.warn(sqle.getMessage());
		}
		log.info("OUT getCustomer\n");
		return databaseCustomer;
	}
	/**
	 * Gets where the user name keyword matches another users name
	 * @param id
	 * @return User Object
	 */
	public ArrayList<Customer> getAllCustomer()
	{
		log.info("IN getCustomer\n");
		ArrayList<Customer> customers = new ArrayList<Customer>();
		int _id = 0;
		String _FName = "null";
		String _LName = "null";
		String _Email = "null";
		String _Phone = "null";
		String _DOB = "null";
		String _Gender = "null";
		String query = "SELECT * FROM CLIENTDETAILS";
		//Creates a null user to return, this can be used to validate user at login
		try (Connection connect = this.connect(); PreparedStatement  inject  = connect.prepareStatement(query))
		{
			//Sets '?' to user name in the query
			//crates a user from the found information
			ResultSet output = inject.executeQuery();
			while (output.next()){
				_id = output.getInt(1);
				_FName = output.getString(2);
				_LName = output.getString(3);
				_Email = output.getString(4);
				_Phone = output.getString(5);
				_DOB = output.getString(6);
				_Gender = output.getString(7);
				customers.add(new Customer(_id ,_FName, _LName, _Phone, _DOB, _Gender, _Email));
			}
			output.close();
		}
		catch(SQLException sqle)
		{
			//System.out.println("Getting User: "+sqle.getMessage());
			log.warn(sqle.getMessage());
		}
		log.info("OUT getCustomer\n");
		return customers;
	}
	
	/**
	 * This function finds a selection of employees that matches the string name
	 * @param name
	 * @return ArrayList<Employee> Objects
	 */
	public ArrayList<Employee> getEmployees(String name)
	{
		log.info("IN getEmployees\n");
		ArrayList<Employee> databaseEmployee = new ArrayList<Employee>();
		int id = 0;
		double payRate = 0;
		String query = "SELECT * FROM EMPLOYEES WHERE name like ? "; 

		try (Connection connect = this.connect(); PreparedStatement  inject  = connect.prepareStatement(query))
		{
			//Sets '?' to user name in the query
			//crates a user from the found information
			inject.setString(1,"%"+name+"%");
			ResultSet output = inject.executeQuery();
			while (output.next()){
				id = output.getInt(1);
				name = output.getString(2);
				payRate = output.getDouble(3);
				databaseEmployee.add(new Employee(id ,name, payRate));
			}
			output.close();
		}
		catch(SQLException sqle)
		{
			//System.out.println("Getting Employee: "+sqle.getMessage());
			log.warn(sqle.getMessage());
		}
		log.info("OUT getEmployees\n");
		return databaseEmployee;
	}

	/**
	 * Gets the employee with matching ID from database
	 * @param employeeID
	 * @return Employee Object
	 */
	public Employee getEmployee(int employeeID)
	{
		log.info("IN getEmployee\n");
		Employee databaseEmployee = null;
		int id = 0;
		String name = "";
		double payRate = 0;
		String query = "SELECT * FROM EMPLOYEES WHERE employeeID like ? "; 

		try (Connection connect = this.connect(); PreparedStatement  inject  = connect.prepareStatement(query))
		{
			//Sets '?' to user name in the query
			//crates a user from the found information
			inject.setInt(1,employeeID);
			ResultSet output = inject.executeQuery();
			while (output.next()){
				id = output.getInt(1);
				name = output.getString(2);
				payRate = output.getDouble(3);
			}
			databaseEmployee = new Employee(id ,name, payRate);
			output.close();
		}
		catch(SQLException sqle)
		{
			//System.out.println("Getting Employee: "+sqle.getMessage());
			log.warn(sqle.getMessage());
		}
		log.info("OUT getEmployee\n");
		return databaseEmployee;
	}
	
	/**
	 * Adds an employee to the database
	 * @param name
	 * @param payRate
	 */
	public void addEmployee(String name, double payRate)
	{
		log.info("IN addEmployee\n");
		String query = "INSERT INTO EMPLOYEES(name, payRate) " + "VALUES ('" + name + "'," + payRate + ");";
		try(Connection connect = this.connect(); Statement inject = connect.createStatement())
		{
			inject.executeUpdate(query);
			log.info("Employee '"+name+"' Added\n");
		}
		catch(SQLException sqle)
		{
			//System.out.println(sqle.getMessage());
			log.warn(sqle.getMessage());
		}
		log.info("OUT addEmployee\n");
	}
	
	/**
	 * Adds ONE employee working time to the database
	 * @param empID
	 * @param date
	 * @param startTime
	 * @param endTime
	 */
	public void addEmployeeWorkingTime(int empID, String date, String startTime, String endTime)
	{
		log.info("IN addEmployeeWorkingTimeToDatabase\n");
		String query = "INSERT INTO EMPLOYEES_WORKING_TIMES(employeeID, date, startTime, endTime) " + "VALUES ("+ empID +",'"+ date +"','"+ startTime +"','"+ endTime +"');";
		try(Connection connect = this.connect(); Statement inject = connect.createStatement())
		{
			inject.executeUpdate(query);
			log.info("Work Time Added - EmpID " + empID+ " date: "+date+" Start Time: "+startTime+" End Time: "+endTime+"\n");
		}
		catch(SQLException sqle)
		{
			//System.out.println(sqle.getMessage());
			//System.out.println(date);
			log.warn(sqle.getMessage());
			log.info(date);
		}
		log.info("OUT addEmployeeWorkingTimeToDatabase\n");
	}
	
	/**
	 * Delete user from database
	 * @param userName
	 */
	public void deleteUser(String userName)
	{
		log.info("IN deleteUser\n");
		String query = "DELETE FROM users WHERE username LIKE '" + userName + "'";
		try(Connection connect = this.connect(); Statement inject = connect.createStatement())
		{
			inject.executeUpdate(query);
			log.info("User " + userName + " deleted\n");
		}
		catch(SQLException sqle)
		{
			//System.out.println(sqle.getMessage());
			log.warn(sqle.getMessage());
		}
		log.info("OUT deleteUser\n");
	}
	
	/**
	 * Delete Booking from database
	 * @param id - allows the user to delete a select booking
	 */
	public void deleteBooking(int id)
	{
		
	}
	
	/**
	 * Gets the employee's working times from database
	 * @param employeeId
	 * @return ArrayList <EmployeeWorkingTime> Objects
	 */
	public ArrayList<EmployeeWorkingTime> getEmployeeWorkingTimes(int employeeId)
	{
		log.info("IN getEmployeeWorkingTimes\n");
		ArrayList<EmployeeWorkingTime> databaseWorkingTime = new ArrayList<EmployeeWorkingTime>();
		int id = 0;
		int empID = 0;
		Date date, startTime, endTime;
		String query = "SELECT * FROM EMPLOYEES_WORKING_TIMES WHERE employeeID = ?"; 

		try (Connection connect = this.connect(); PreparedStatement  inject  = connect.prepareStatement(query))
		{
			//Sets '?' to user name in the query
			//crates a user from the found information
			inject.setInt(1,employeeId);
			ResultSet output = inject.executeQuery();
			while (output.next())
			{
				id = output.getInt(1);
				empID = output.getInt(2);
				date = controller.strToDate(output.getString(3));
				startTime = controller.strToTime(output.getString(4));
				endTime = controller.strToTime(output.getString(5));
				databaseWorkingTime.add(new EmployeeWorkingTime(id,empID,date,startTime,endTime));				
			}
			output.close();
		}
		catch(SQLException sqle)
		{
			//System.out.println("Getting Working Time: "+sqle.getMessage());
			log.warn(sqle.getMessage());
		}
		log.info("OUT getEmployeeWorkingTimes\n");
		return databaseWorkingTime;
	}
	
	/*/**
	 * Rounds a double to x amount of decimal places then return the rounded double
	 * @param value
	 * @param places
	 * @return
	 */
	/*public static double round(double value, int places) {
	    if (places < 0) throw new IllegalArgumentException();

	    long factor = (long) Math.pow(10, places);
	    value = value * factor;
	    long tmp = Math.round(value);
	    return (double) tmp / factor;
	}*/
	
	/**
	 * Drops table name from database
	 * @param tableName
	 * @return
	 */
	public boolean dropTable(String tableName)
	{
		log.info("IN dropTable\n");
		String query = "DROP TABLE IF EXISTS '"+ tableName +"' ";
		try(Connection connect = this.connect(); Statement inject = connect.createStatement())
		{
			inject.executeUpdate(query);
			log.info("Table "+ tableName +"Added\n");
			log.info("OUT dropTable\n");
			return true;
		}
		catch(SQLException sqle)
		{
			//System.out.println(sqle.getMessage());
			log.warn(sqle.getMessage());
			log.info("OUT dropTable\n");
			return false;
		}
		
	}
	
	/**
	 * Drops table name from database
	 * @param username
	 * @return
	 */
	public boolean dropUser(String username)
	{
		log.info("IN dropUser\n");
		String query = "DELETE FROM USERS WHERE username like '"+ username +"' ";
		try(Connection connect = this.connect(); Statement inject = connect.createStatement())
		{
			inject.executeUpdate(query);
			//System.out.println("Table "+ tableName +"");
			log.info("User "+username+" Dropped\n");
			log.info("OUT dropUser\n");
			return true;
		}
		catch(SQLException sqle)
		{
			//System.out.println(sqle.getMessage());
			log.warn(sqle.getMessage());
			log.info("OUT dropUser\n");
			return false;
		}
	}
	/**
	 * Adds one booking to the database
	 * @param userId
	 * @param date
	 * @param startTime
	 * @param endTime
	 * @param description
	 */
	public void addBooking (int userId,int empID, String date, String startTime, String endTime, int service, String status)
	{
		log.info("IN addBookingToDatabase\n");
		//bookingID is made in the database
		String query = "INSERT INTO BOOKINGS (userID,employeeID,date,startTime,endTime, serviceID,status)" + "VALUES(" + userId + ","+empID+",'" + date + "','" + startTime + "','" + endTime + "',"+service+",'" + status + "');";
		try(Connection connect = this.connect(); Statement inject = connect.createStatement())
		{
			inject.executeUpdate(query);
			log.info("Booking Added - User ID: "+userId+" Date: "+date+" Start Time: "+startTime+" End Time: "+endTime+" ServiceNmb: "+service+" Status: "+status+"\n");
		}
		catch(SQLException sqle)
		{
			log.warn(sqle.getMessage());
		}
		log.info("OUT addBookingToDatabase\n");
	}
	
	/**
	 * Gets a booking from database
	 * @param cusID
	 * @return ArrayList <Booking> Objects
	 */
	public ArrayList<Booking> getBooking (int customerID)
	{
		log.info("IN getBooking\n");
		ArrayList<Booking> databaseBookingTime = new ArrayList<Booking>();
		int bookingID = 0;
		int cusID = 0;
		int empID = 0;
		Date date, startTime, endTime;
		String desc;
		int service;
		String query = "SELECT * FROM BOOKINGS WHERE userID = ? "; 

		try (Connection connect = this.connect(); PreparedStatement  inject  = connect.prepareStatement(query))
		{
			//Sets '?' to user name in the query
			//crates a user from the found information
			inject.setInt(1,customerID);
			ResultSet output = inject.executeQuery();
			while (output.next())
			{
				bookingID = output.getInt(1);
				cusID = output.getInt(2);
				empID = output.getInt(3);
				date = controller.strToDate(output.getString(4));
				startTime = controller.strToTime(output.getString(5));
				endTime = controller.strToTime(output.getString(6));
				service = output.getInt(7);
				desc=output.getString(8);
				databaseBookingTime.add(new Booking(bookingID,cusID,empID,date,startTime,endTime,service,desc));				
			}
			output.close();
		}
		catch(SQLException sqle)
		{
			log.warn(sqle.getMessage());
		}
		log.info("OUT getBooking\n");
		return databaseBookingTime;
	}
	
	/**
	 * Gets all booking object from database
	 * @param cusID
	 * @return ArrayList <Booking> Objects
	 */
	public ArrayList<Booking> getAllBooking ()
	{
		log.info("IN getAllBooking\n");
		ArrayList<Booking> databaseBookingTime = new ArrayList<Booking>();
		int bookingID = 0;
		int cusID = 0;
		int empID = 0;
		Date date, startTime, endTime;
		String desc;
		int service;
		String query = "SELECT * FROM BOOKINGS"; 

		try (Connection connect = this.connect(); PreparedStatement  inject  = connect.prepareStatement(query))
		{
			//Sets '?' to user name in the query
			//crates a user from the found information
			//inject.setInt(1,customerID);
			ResultSet output = inject.executeQuery();
			while (output.next())
			{
				bookingID = output.getInt(1);
				cusID = output.getInt(2);
				empID = output.getInt(3);
				date = controller.strToDate(output.getString(4));
				startTime = controller.strToTime(output.getString(5));
				endTime = controller.strToTime(output.getString(6));
				service = output.getInt(7);
				desc=output.getString(8);
				databaseBookingTime.add(new Booking(bookingID,cusID,empID,date,startTime,endTime,service,desc));				
			}
			output.close();
		}
		catch(SQLException sqle)
		{
			log.warn(sqle.getMessage());
		}
		log.info("OUT getAllBooking\n");
		return databaseBookingTime;
	}
	
	/**
	 * Gets one booking from database
	 * @param bookID
	 * @return one <Booking> Object
	 */
	public Booking getOneBooking(int bookID)
	{
		log.info("IN getOneBooking\n");
		Booking getBooking = null;
		int bookingID = 0;
		int cusID = 0;
		int empID = 0;
		Date date, startTime, endTime;
		int service;
		String desc;
		String query = "SELECT * FROM BOOKINGS WHERE id = ?"; 

		try (Connection connect = this.connect(); PreparedStatement  inject  = connect.prepareStatement(query))
		{
			//Sets '?' to user name in the query
			//crates a user from the found information
			inject.setInt(1, bookID);
			ResultSet output = inject.executeQuery();
			while (output.next())
			{
				bookingID = output.getInt(1);
				cusID = output.getInt(2);
				empID = output.getInt(3);
				date = controller.strToDate(output.getString(4));
				startTime = controller.strToTime(output.getString(5));
				endTime = controller.strToTime(output.getString(6));
				service = output.getInt(7);
				desc=output.getString(8);
				getBooking = new Booking(bookingID,cusID,empID,date,startTime,endTime,service,desc);				
			}
			output.close();
		}
		catch(SQLException sqle)
		{
			log.warn(sqle.getMessage());
		}
		log.info("OUT getOneBooking\n");
		return getBooking;
	}
	
	/*
	 * set booking status to 'cancel'
	 * @param bookID
	 * @return true or false
	 */
	public boolean cancelBooking(int bookID)
	{
		log.info("IN cancelBooking\n");
		ArrayList<Booking> bookList = getAllBooking();
		Boolean exists = false;
		for(Booking b : bookList){
			if(b.getBookingID() == bookID){
				exists = true;
			}
		}
		if(exists == false){
			log.debug("Book ID " + bookID + " does not exists!\n");
			log.info("OUT cancelBooking\n");
			return false;
		}
		String query = "UPDATE BOOKINGS SET status = 'canceled' WHERE id = " + bookID;
		
		try(Connection connect = this.connect(); Statement inject = connect.createStatement())
		{
			inject.executeUpdate(query);
			log.debug("Book ID "+ bookID +" cancelled\n");
			log.info("OUT cancelBooking\n");
			return true;
		}
		catch(SQLException sqle)
		{
			//System.out.println(sqle.getMessage());
			log.warn(sqle.getMessage());
			log.info("OUT cancelBooking\n");
			return false;
		}
	}
	/**
	 * @author Luke Mason
	 * @param EmployeeID
	 * @return
	 */
	public boolean clearWorkTimes(int employeeID)
	{
		log.info("IN deleteUser\n");
		String query = "DELETE FROM  EMPLOYEES_WORKING_TIMES WHERE employeeID = '" + employeeID + "';";
		try(Connection connect = this.connect(); Statement inject = connect.createStatement())
		{
			inject.executeUpdate(query);
			log.info("WorkTimes for ID " + employeeID + " deleted!!\n");
		}
		catch(SQLException sqle)
		{
			//System.out.println(sqle.getMessage());
			log.warn(sqle.getMessage());
		}
		log.info("OUT deleteUser\n");
		return false;
	}
	public void updateEmployeeName(int empID,String name)
	{
		log.info("IN updateEmployeeName\n");
		String query = "UPDATE employees SET name = '"+name+"' WHERE employeeID = "+empID+";";
		try(Connection connect = this.connect(); Statement inject = connect.createStatement())
		{
			inject.executeUpdate(query);
			log.info("name for ID " + empID + " updated\n");
		}
		catch(SQLException sqle)
		{
			//System.out.println(sqle.getMessage());
			log.warn(sqle.getMessage());
		}
		log.info("OUT updateEmployeeName\n");
	}
	public void updateEmployeePayRate(int empID, double pRate)
	{
		log.info("IN updateEmployeePayRate\n");
		String query = "UPDATE employees SET payRate = '"+pRate+"' WHERE employeeID = "+empID+";";
		try(Connection connect = this.connect(); Statement inject = connect.createStatement())
		{
			inject.executeUpdate(query);
			log.info("name for ID " + empID + " updated\n");
		}
		catch(SQLException sqle)
		{
			//System.out.println(sqle.getMessage());
			log.warn(sqle.getMessage());
		}
		log.info("OUT updateEmployeePayRate\n");
	}
	public boolean deleteEmployee(int employeeID)
	{
		log.info("IN deleteEmployee\n");
		String query = "DELETE FROM EMPLOYEES WHERE employeeID = '" + employeeID + "';";
		try(Connection connect = this.connect(); Statement inject = connect.createStatement())
		{
			inject.executeUpdate(query);
			log.info("Employee " + employeeID + " deleted!\n");
		}
		catch(SQLException sqle)
		{
			//System.out.println(sqle.getMessage());
			log.warn(sqle.getMessage());
		}
		log.info("OUT deleteEmployee\n");
		return false;
	}
	/**
	 * Gets all working times from database
	 * @param employeeId
	 * @return ArrayList <EmployeeWorkingTime> Objects
	 */
	public ArrayList<EmployeeWorkingTime> getAllWorkingTimes()
	{
		log.info("IN getAllWorkingTimes\n");
		ArrayList<EmployeeWorkingTime> databaseWorkingTime = new ArrayList<EmployeeWorkingTime>();
		int id = 0;
		int empID = 0;
		Date date, startTime, endTime;
		String query = "SELECT * FROM EMPLOYEES_WORKING_TIMES;"; 

		try (Connection connect = this.connect(); PreparedStatement  inject  = connect.prepareStatement(query))
		{
			//Sets '?' to user name in the query
			//crates a user from the found information
			ResultSet output = inject.executeQuery();
			while (output.next())
			{
				id = output.getInt(1);
				empID = output.getInt(2);
				date = controller.strToDate(output.getString(3));
				startTime = controller.strToTime(output.getString(4));
				endTime = controller.strToTime(output.getString(5));
				databaseWorkingTime.add(new EmployeeWorkingTime(id,empID,date,startTime,endTime));				
			}
			output.close();
		}
		catch(SQLException sqle)
		{
			//System.out.println("Getting Working Time: "+sqle.getMessage());
			log.warn(sqle.getMessage());
		}
		log.info("OUT getAllWorkingTimes\n");
		return databaseWorkingTime;
	}
	
	/**
	 * Get Work Times on given date
	 * @param Date
	 * @return array of work times
	 * @author Luke Mason
	 */
	public ArrayList<EmployeeWorkingTime> getWorkTimesOnDate(String Date)
	{
		log.info("IN getWorkTimesOnDate");
		ArrayList<EmployeeWorkingTime> workTimesOnDate = new ArrayList<EmployeeWorkingTime>();
		ArrayList<EmployeeWorkingTime> workTimes = new ArrayList<EmployeeWorkingTime>();
		workTimes = getAllWorkingTimes();
		for(EmployeeWorkingTime ewt: workTimes)
		{
			Date date = ewt.getDate();
			String dateStr = controller.dateToStr(date);
			if(dateStr.equals(Date))
			{
				workTimesOnDate.add(ewt);
			}
		}
		log.info("OUT getWorkTimesOnDate");
		return workTimesOnDate;
	}
	
	/**
	 * Get Active Bookings on given date
	 * @param Date
	 * @return array of bookings
	 * @author Luke Mason
	 */
	public ArrayList<Booking> getActiveBookingsOnDate(String Date)
	{
		log.info("IN getActiveBookingsOnDate");
		ArrayList<Booking> bookings = new ArrayList<Booking>();
		ArrayList<Booking> ActiveBookingsOnDate = new ArrayList<Booking>();
		bookings = getAllBooking();//gets all bookings from database
		for(Booking bk: bookings)
		{
			Date date = bk.getDate();
			String dateStr = controller.dateToStr(date);
			if(dateStr.equals(Date))
			{
				if(bk.getStatus().equals("active"))
				{
					ActiveBookingsOnDate.add(bk);
				}
			}
		}
		log.info("OUT getActiveBookingsOnDate");
		return ActiveBookingsOnDate;
	}
	
	/**
	 * adds a service to the database
	 * @param Service Object
	 * @author Joseph Garner
	 */
	public void addSerice(Service service)
	{
		String query = "INSERT INTO SERVICES(service, length, cost) " + "VALUES('"+service.getName()+"',"+service.getLengthMin()+","+service.getPrice()+")";
		try(Connection connect = this.connect(); Statement inject = connect.createStatement())
		{
			inject.executeUpdate(query);
			log.info("User Added\n");
		}
		catch(SQLException sqle)
		{
			log.warn(sqle.getMessage());
		}
	}
	
	/**
	 * Gets All services
	 * @return array of services
	 * @author Joseph Garner
	 */
	public ArrayList<Service> getAllServices(String input)
	{
		int id = 0;
		String _service = "null";
		int length = 0;
		double cost = 0;
		ArrayList<Service> services = new ArrayList<Service>();
		String query = "SELECT * FROM Services WHERE service like ?;"; 
		try (Connection connect = this.connect(); PreparedStatement  inject  = connect.prepareStatement(query))
		{
			inject.setString(1, "%"+input+"%");
			ResultSet output = inject.executeQuery();
			while (output.next())
			{
				id = output.getInt(1);
				_service = output.getString(2);
				length = output.getInt(3);
				cost = output.getDouble(4);
				services.add(new Service(id, _service, length, cost));
			}
			output.close();
		}
		catch(SQLException sqle)
		{
			log.warn(sqle.getMessage());
		}
		return services;
	}
	
	/**
	 * Gets a service
	 * @return service Object
	 * @param Service ID
	 * @author Joseph Garner
	 */
	public Service getService(int id)
	{
		Service service = new Service();
		int _id = 0;
		String _service = "null";
		int length = 0;
		double cost = 0;
		String query = "SELECT * FROM SERVICES WHERE id = ?"; 

		try (Connection connect = this.connect(); PreparedStatement  inject  = connect.prepareStatement(query))
		{
			//Sets '?' to user name in the query
			//crates a user from the found information
			inject.setInt(1, id);
			ResultSet output = inject.executeQuery();
			while (output.next())
			{
				_id = output.getInt(1);
				_service = output.getString(2);
				length = output.getInt(3);
				cost = output.getDouble(4);
				service = new Service(_id, _service, length, cost);			
			}
			output.close();
		}
		catch(SQLException sqle)
		{
			log.warn(sqle.getMessage());
		}
		return service;
	}
	
	/**
	 * Gets a service
	 * @return service Object
	 * @param Service Name
	 * @author Joseph Garner
	 */
	public Service getService(String name)
	{
		Service service = new Service();
		int id = 0;
		String _service = "null";
		int length = 0;
		double cost = 0;
		String query = "SELECT * FROM SERVICES WHERE service like ?"; 

		try (Connection connect = this.connect(); PreparedStatement  inject  = connect.prepareStatement(query))
		{
			//Sets '?' to user name in the query
			//crates a user from the found information
			inject.setString(1,"%"+name+"%");
			ResultSet output = inject.executeQuery();
			while (output.next())
			{
				id = output.getInt(1);
				_service = output.getString(2);
				length = output.getInt(3);
				cost = output.getDouble(4);
				service = new Service(id, _service, length, cost);			
			}
			output.close();
		}
		catch(SQLException sqle)
		{
			log.warn(sqle.getMessage());
		}
		return service;
	}
	public void deleteService(int id)
	{
		log.info("IN deleteService\n");
		String query = "DELETE FROM SERVICES WHERE id = '" + id + "';";
		try(Connection connect = this.connect(); Statement inject = connect.createStatement())
		{
			inject.executeUpdate(query);
			log.info("Service " + id + " deleted!\n");
		}
		catch(SQLException sqle)
		{
			log.warn(sqle.getMessage());
		}
		log.info("OUT deleteService\n");
	}
	
	/**
	 * Create booking in database using booking object
	 * @param booking
	 * @return
	 * @author Bryan
	 */
	public void createBooking(Booking book){
		log.info("IN addBookingToDatabase\n");
		//bookingID is made in the database
		log.debug("LOGGER: emp ID  booking- "+ book.getEmployee());
		String query = "INSERT INTO BOOKINGS (userID,employeeID,date,startTime,endTime, serviceID,status)" + "VALUES(" + book.getCustomerId() + ","+book.getEmployeeID()+",'" + controller.dateToStr(book.getDate()) + "','" + controller.timeToStr(book.getStartTime()) + "','" + controller.timeToStr(book.getEndTime()) + "',"+book.getService()+",'" + book.getStatus() + "');";
		try(Connection connect = this.connect(); Statement inject = connect.createStatement())
		{
			inject.executeUpdate(query);
			log.info("Booking Added - User ID: "+book.getCustomerId()+" Date: "+book.getDate()+" Start Time: "+book.getStartTime()+" End Time: "+book.getEndTime()+" ServiceNmb: "+book.getService()+" Status: "+book.getStatus()+"\n");
		}
		catch(SQLException sqle)
		{
			log.warn(sqle.getMessage());
		}
		log.info("OUT addBookingToDatabase\n");
	}
}
