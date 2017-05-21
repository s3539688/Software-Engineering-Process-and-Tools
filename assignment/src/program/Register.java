package program;

import org.apache.log4j.Level;
import org.apache.log4j.Logger;

public class Register 
{
	private static Logger log = Logger.getLogger(Register.class);
	private DatabaseConnection connect = new DatabaseConnection();
	
	public Register(){log.setLevel(Level.WARN);}
	
	
	public void registerUser(String fname, String lname, String username, String email, String mobilenumber, String dob, String gender, String password)
	{
		connect.addUser(username, password, 0,2);
		connect.addUserDetails(connect.getUser(username,2).getID(),fname,lname, email, mobilenumber, dob, gender);	
	}
	
	/**
	 * @author Joseph Garner
	 * Checks if the username is equal to one already in the database
	 * @return true if username already taken, else false
	 */
	public boolean checkTakenUsername(String username, int businessID)
	{
		boolean output = true;
		if(!username.equalsIgnoreCase(connect.getUser(username,businessID).getUsername()))
		{
			output = false;
		}
		else
		{
			System.out.println("Username Taken please enter another username");
		}
		if(username.equals("root"))
		{
			output = false;
		}
		return output;
	}
	
	/**
	 * @author Joseph Garner
	 * Checks and validate password
	 * Password must contain at least 1 upper-case letter, 1 lower-case letter, 1 number
	 * and the length is 6 at the minimum
	 * @return true if the pass complies to standard, else false
	 */
	public boolean checkPassword(String _password)
	{
		boolean upper=false;
		boolean lower=false;
		boolean number=false;
		for (char c : _password.toCharArray()) {
		      if (Character.isUpperCase(c)) {
		        upper = true;
		      } else if (Character.isLowerCase(c)) {
		        lower = true;
		      } else if (Character.isDigit(c)) {
		        number = true;
		      }
		    }
		int length = _password.length();
		if(length >= 6 && upper && lower && number)
		{
			log.debug("Upper = "+upper+" Lower = "+lower+" Number = "+number+" Password = "+_password);
			return true;
		}
		else if(length < 6)
		{
			System.out.println("Password is too short please enter a shorter password");
			log.debug("Upper = "+upper+" Lower = "+lower+" Number = "+number+" Password = "+_password);
			return false;
		}
		else if(!upper)
		{
			System.out.println("Password must contain Uppercase characters");
			log.debug("Upper = "+upper+" Lower = "+lower+" Number = "+number+" Password = "+_password);
			return false;
		}
		else if(!lower)
		{
			System.out.println("Password must contain lowercase characters");
			log.debug("Upper = "+upper+" Lower = "+lower+" Number = "+number+" Password = "+_password);
			return false;
		}
		else if(!number)
		{
			System.out.println("Password must contain a number");
			log.debug("Upper = "+upper+" Lower = "+lower+" Number = "+number+" Password = "+_password);
			return false;
		}
		else
		{ 
			System.out.println("Password must contain Uppercase, lowercase and a number");
			log.debug("Upper = "+upper+" Lower = "+lower+" Number = "+number+" Password = "+_password);
			return false; 
		}
	}
}
