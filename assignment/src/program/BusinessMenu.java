package program;


import java.util.ArrayList;
import org.apache.log4j.Level;
import org.apache.log4j.Logger;

import gui.SetupController;


public class BusinessMenu
{
	private static Logger log = Logger.getLogger(BusinessMenu.class);
	private Controller controller = new Controller();
	DatabaseConnection connect = new DatabaseConnection();
	public BusinessMenu(){log.setLevel(Level.INFO);}
	//Monday to Friday
	public static String MFearly = "08:00"; //Start Time for day
	public static String MFearlyMidDay = "12:00"; //Early Midday
	public static String MFlateMidDay = "16:00"; //Late midday
	public static String MFlate = "20:00"; // End Time for day
	//Sunday to Saturday
	public static String SSearly = "08:00"; //Start Time for day
	public static String SSearlyMidDay = "12:00"; //Early Midday
	public static String SSlateMidDay = "16:00"; //Late midday
	public static String SSlate = "20:00"; // End Time for day
	
	/**
	 * @author Luke Mason
	 * Used to check information to add a new employee to database
	 */
				
	/**
	 * Changes the worktime labels to the worktime blocks when selecting work times for an employee
	 */
	public void changeWorkTimeLabelsToTimes()
	{
		//TODO
	}
	
				/**
				 * Convert string to Double
				 * @param employeePayRate
				 * @return double
				 */
				public double strPayRateToDouble(String employeePayRate)
				{
					log.info("IN strPayRateToDouble\n");
					double employeePayRate2 = controller.changeInputIntoValidDouble(employeePayRate);//checking for invalid input
					log.info("OUT strPayRateToDouble\n");
					return employeePayRate2;
				}
				
				/**
				 * Checks if employee pay rate is less than 0 (Error)
				 * @param employeePayRate
				 * @return true, or false if pay rate is out of bound
				 */
				public boolean checkEmployeePayRate(double employeePayRate)
				{
					log.info("IN checkEmployeePayRate\n");
					if (employeePayRate < 0||employeePayRate>1000)
					{
						log.info("OUT checkEmployeePayRate\n");
						return false;
					}
					log.info("OUT checkEmployeePayRate\n");
					return true;
				}
				
				/**
				 * Adds an employee to database
				 * @param employeeFName
				 * @param employeeLName
				 * @param employeePayRate
				 */
				public void addEmployee(String employeeFName,String employeeLName,double employeePayRate, int businessID)
				{
					log.info("IN addEmployee-bMenu\n");
					DatabaseConnection connect = new DatabaseConnection();
					String employeeName = employeeFName + " " + employeeLName;//concatenating first and last name into name
					connect.addEmployee(employeeName, employeePayRate,businessID);//adding employee
					log.info("OUT addEmployee-bMenu\n");
				}
				
				/**
				 * Gets the last employeeID in the database
				 * @return an id number of the last employee
				 */
				public int getLastEmployeeId(int businessID)
				{
					log.info("IN getLastEmployeeId\n");
					log.debug("getting id from last employee");
					ArrayList<Employee> employees = connect.getEmployees("",businessID);//adding working times to employee just made
					//This is for if more than one employee has the same name as searched
					int id = -1;
					Employee lastEmp = new Employee();
					for(Employee emp: employees)
					{
						lastEmp = emp;
					}		
					id =lastEmp.getId();
					log.info("OUT getLastEmployeeId\n");
					return id;
				}
				
				/**
				 * Checks if morning or evening have been selected
				 * @param btnSunMorning
				 * @param btnSunAfternoon
				 * @param btnSunEvening
				 * @param btnMonMorning
				 * @param btnMonAfternoon
				 * @param btnMonEvening
				 * @param btnTueMorning
				 * @param btnTueAfternoon
				 * @param btnTueEvening
				 * @param btnWedMorning
				 * @param btnWedAfternoon
				 * @param btnWedEvening
				 * @param btnThurMorning
				 * @param btnThurAfternoon
				 * @param btnThurEvening
				 * @param btnFriMorning
				 * @param btnFriAfternoon
				 * @param btnFriEvening
				 * @param btnSatMorning
				 * @param btnSatAfternoon
				 * @param btnSatEvening
				 * @return false if morning and evening are true and afternoon is false
				 */
				public boolean checkWorkTimes(boolean btnSunMorning,boolean btnSunAfternoon,boolean btnSunEvening,boolean btnMonMorning,boolean btnMonAfternoon,boolean btnMonEvening,boolean btnTueMorning,boolean btnTueAfternoon,boolean btnTueEvening,boolean btnWedMorning,boolean btnWedAfternoon,boolean btnWedEvening,boolean btnThurMorning,boolean btnThurAfternoon,boolean btnThurEvening,boolean btnFriMorning,boolean btnFriAfternoon,boolean btnFriEvening,boolean btnSatMorning,boolean btnSatAfternoon,boolean btnSatEvening)
				{
					log.info("IN checkWorkTimes\n");
					if(btnSunMorning && !btnSunAfternoon && btnSunEvening)
					{
						String day = "Sunday";
						controller.messageBox("ERROR", "Work Time Error", "Work time selected on "+day+" is invalid","Work time selected on "+day+" is invalid\n <Work times not added>\n Reason: Morning and Evening alone are two seperated blocks of time\n please select a one block of time and try again");
						log.info("OUT checkWorkTimes\n");
						return false;
					}
					else if(btnMonMorning && !btnMonAfternoon && btnMonEvening)
					{
						String day = "Monday";
						controller.messageBox("ERROR", "Work Time Error", "Work time selected on "+day+" is invalid","Work time selected on "+day+" is invalid\n <Work times not added>\n Reason: Morning and Evening alone are two seperated blocks of time\n please select a one block of time and try again");
						log.info("OUT checkWorkTimes\n");
						return false;
					}
					else if(btnTueMorning && !btnTueAfternoon && btnTueEvening)
					{
						String day = "Tuesday";
						controller.messageBox("ERROR", "Work Time Error", "Work time selected on "+day+" is invalid","Work time selected on "+day+" is invalid\n <Work times not added>\n Reason: Morning and Evening alone are two seperated blocks of time\n please select a one block of time and try again");
						log.info("OUT checkWorkTimes\n");
						return false;
					}
					else if(btnWedMorning && !btnWedAfternoon && btnWedEvening)
					{
						String day = "Wednesday";
						controller.messageBox("ERROR", "Work Time Error", "Work time selected on "+day+" is invalid","Work time selected on "+day+" is invalid\n <Work times not added>\n Reason: Morning and Evening alone are two seperated blocks of time\n please select a one block of time and try again");
						log.info("OUT checkWorkTimes\n");
						return false;
					}
					else if(btnThurMorning && !btnThurAfternoon && btnThurEvening)
					{
						String day = "Thursday";
						controller.messageBox("ERROR", "Work Time Error", "Work time selected on "+day+" is invalid","Work time selected on "+day+" is invalid\n <Work times not added>\n Reason: Morning and Evening alone are two seperated blocks of time\n please select a one block of time and try again");
						log.info("OUT checkWorkTimes\n");
						return false;
					}
					else if(btnFriMorning && !btnFriAfternoon && btnFriEvening)
					{
						String day = "Friday";
						controller.messageBox("ERROR", "Work Time Error", "Work time selected on "+day+" is invalid","Work time selected on "+day+" is invalid\n <Work times not added>\n Reason: Morning and Evening alone are two seperated blocks of time\n please select a one block of time and try again");
						log.info("OUT checkWorkTimes\n");
						return false;
					}
					else if(btnSatMorning && !btnSatAfternoon && btnSatEvening)
					{
						String day = "Saturday";
						controller.messageBox("ERROR", "Work Time Error", "Work time selected on "+day+" is invalid","Work time selected on "+day+" is invalid\n <Work times not added>\n Reason: Morning and Evening alone are two seperated blocks of time\n please select a one block of time and try again");
						log.info("OUT checkWorkTimes\n");
						return false;
					}
					log.info("OUT checkWorkTimes\n");
					return true;
				}		
			
				/**
				 * Passes all the button values in its parameters and then assigns work times according to those times to the specified id.
				 * @param id
				 * @param btnSunMorning
				 * @param btnSunAfternoon
				 * @param btnSunEvening
				 * @param btnMonMorning
				 * @param btnMonAfternoon
				 * @param btnMonEvening
				 * @param btnTueMorning
				 * @param btnTueAfternoon
				 * @param btnTueEvening
				 * @param btnWedMorning
				 * @param btnWedAfternoon
				 * @param btnWedEvening
				 * @param btnThurMorning
				 * @param btnThurAfternoon
				 * @param btnThurEvening
				 * @param btnFriMorning
				 * @param btnFriAfternoon
				 * @param btnFriEvening
				 * @param btnSatMorning
				 * @param btnSatAfternoon
				 * @param btnSatEvening
				 * @return true
				 */
				public boolean addWorkingTimes(int id,boolean btnSunMorning,boolean btnSunAfternoon,boolean btnSunEvening,boolean btnMonMorning,boolean btnMonAfternoon,boolean btnMonEvening,boolean btnTueMorning,boolean btnTueAfternoon,boolean btnTueEvening,boolean btnWedMorning,boolean btnWedAfternoon,boolean btnWedEvening,boolean btnThurMorning,boolean btnThurAfternoon,boolean btnThurEvening,boolean btnFriMorning,boolean btnFriAfternoon,boolean btnFriEvening,boolean btnSatMorning,boolean btnSatAfternoon,boolean btnSatEvening)
				{
					log.info("IN addWorkingTimes\n");
					connect.clearWorkTimes(id);	
					log.debug("Added Work Times\n");
					addDayWorkingTime(id,1,btnSunMorning, btnSunAfternoon, btnSunEvening);						
					addDayWorkingTime(id,2,btnMonMorning, btnMonAfternoon, btnMonEvening);															
					addDayWorkingTime(id,3,btnTueMorning, btnTueAfternoon, btnTueEvening);													
					addDayWorkingTime(id,4,btnWedMorning, btnWedAfternoon, btnWedEvening);															
					addDayWorkingTime(id,5,btnThurMorning, btnThurAfternoon, btnThurEvening);							
					addDayWorkingTime(id,6,btnFriMorning, btnFriAfternoon, btnFriEvening);										
					addDayWorkingTime(id,7,btnSatMorning, btnSatAfternoon, btnSatEvening);																					
					log.info("OUT addWorkingTimes\n");
					return true;
				}
				
				/**
				 * used for getStartEndTimes by giving a number referring to time block
				 * @param morning
				 * @param afternoon
				 * @param evening
				 * @return number referring to a time block, -1 if time block is invalid
				 */
				public int getWorkTimes(boolean morning, boolean afternoon, boolean evening)
				{
					log.info("IN getWorkTimes\n");
					if(morning && afternoon && evening)
					{
						log.info("OUT getWorkTimes\n");
						return 1;
					}
					else if(morning && afternoon)
					{
						log.info("OUT getWorkTimes\n");
						return 2;
					}
					else if(afternoon && evening)
					{
						log.info("OUT getWorkTimes\n");
						return 3;
					}
					else if(morning && evening)
					{
						log.info("OUT getWorkTimes\n");
						return -1;
					}
					else if(morning)
					{
						log.info("OUT getWorkTimes\n");
						return 4;
					}
					else if(afternoon)
					{
						log.info("OUT getWorkTimes\n");
						return 5;
					}
					else if(evening)
					{
						log.info("OUT getWorkTimes\n");
						return 6;
					}
					else
					{
						log.info("OUT getWorkTimes\n");
						return 0;
					}
				}
				
				/**
				 * Assigns timeBlock number to a startTime and endTime
				 * @param checkTimes
				 * @return array of 2 String's objects; Start Time and End Times
				 */
				public String[] getStartEndTimes(int checkTimes, int dayBlock)
				{
					log.info("IN getStartEndTimes\n");
					String[] array = new String[2];
					array[0] = "";//Start Time
					array[1] = "";//End Time
					String early = "";
					String earlyMidDay = "";
					String lateMidDay = "";
					String late = "";
					//0 = weekdays, 1 = weekend
					if(dayBlock == 0)
					{
						early = MFearly; //Start Time for day
						earlyMidDay = MFearlyMidDay; //Early Midday
						lateMidDay = MFlateMidDay; //Late midday
						late = MFlate; // End Time for day
						//Sunday to Saturday
					}
					else if(dayBlock == 1)
					{
						early = SSearly; //Start Time for day
						earlyMidDay = SSearlyMidDay; //Early Midday
						lateMidDay = SSlateMidDay; //Late midday
						late = SSlate; // End Time for day
					}
					else
					{
						return array;
					}
					switch(checkTimes)
					{
						case 0:
							break;
						case 1: array[0] = early;
								array[1] = late;
								break;
						case 2: 
							array[0] = early;
							array[1] = lateMidDay;
								break;
						case 3: 
							array[0] = earlyMidDay;
							array[1] = late;
								break;
						case 4: 
							array[0] = early;
							array[1] = earlyMidDay;
								break;
						case 5: 
							array[0] = earlyMidDay;
							array[1] = lateMidDay;
								break;
						case 6: 
							array[0] = lateMidDay;
							array[1] = late;
								break;
						default:
							//controller.messageBox("WARN", "Error: Something happened with dayOfWeek", "dayOfWeek did not register to a day","Please consult Luke Mason for the crap coding");
							break;
					}
					log.info("OUT getStartEndTimes\n");
					return array;
				}
			
				/**
				 * Checks return value of checkWorkingTimes and getStartEndTimes. 
				 * Then Calls set7dayRosterTime.
				 * @param day
				 * @param employeeID
				 * @param date
				 * @param morning
				 * @param afternoon
				 * @param evening
				 * @return true, or false if invalid
				 */
				public boolean addDayWorkingTime(int employeeID,int dayOfWeek,boolean morning, boolean afternoon, boolean evening)
				{
					log.info("IN addDayWorkingTime");
					Employee emp = connect.getEmployee(employeeID);
					int businessID = emp.getBusinessID();
					BusinessOwner BO = connect.getOneBusiness(businessID);
					if(BO == null)
					{
						log.debug("BO IS NULLL FOR THIS WORKTIME BUSINESS");
						return false;
					}
					if(BO != null)
					{
						String wds = controller.timeToStr(BO.getWeekdayStart());
						String wde = controller.timeToStr(BO.getWeekdayEnd());
						String wes = controller.timeToStr(BO.getWeekendStart());
						String wee = controller.timeToStr(BO.getWeekendEnd());
						SetupController setupC = new SetupController();
						setupC.assignOpenClosingTimesToGlobal(wds, wde, wes, wee);
					}
					// -Day- is not used but is there if needed
					String[] array = new String[2];
					int check = getWorkTimes(morning, afternoon, evening);
					if(dayOfWeek != 1 && dayOfWeek != 7)
					{
						array = getStartEndTimes(check,0);
					}
					else
					{
						array = getStartEndTimes(check,1);
					}
					if(check == -1)
					{
						log.info("OUT addDayWorkingTime");
						return false;
					}
					if(array[0].equals(""))
					{
						log.info("OUT addDayWorkingTime");
						return false;//No working times assigned to this day
					}
					if(dayOfWeek < 1 || dayOfWeek > 7)
					{
						log.info("OUT addDayWorkingTime");
						return false;//day 0 does not exist, day 8 does not exist
					}
					connect.addEmployeeWorkingTime(employeeID, dayOfWeek, array[0], array[1],emp.getBusinessID());
					log.info("OUT addDayWorkingTime");
					return true;
				}
				
				/**
				 * Get Time Block of the day from a combination of morning, afternoon and evening
				 * @param startTime
				 * @param endTime
				 * @return an integer representing a combination of morning,afternoon and evening, 
				 * 			-1 if time block is invalid
				 */
				public int getTimeBlock(String startTime, String endTime, int dayBlock)
				{
					log.info("IN getTimeBlock");
					String early = "";
					String earlyMidDay = "";
					String lateMidDay = "";
					String late = "";
					//0 = weekdays, 1 = weekend
					if(dayBlock == 0)
					{
						log.debug("Work Time is WeekDay");
						early = MFearly; //Start Time for day
						earlyMidDay = MFearlyMidDay; //Early Midday
						lateMidDay = MFlateMidDay; //Late midday
						late = MFlate; // End Time for day
						//Sunday to Saturday
					}
					else if(dayBlock == 1)
					{
						log.debug("Work Time is WeekEnd");
						early = SSearly; //Start Time for day
						earlyMidDay = SSearlyMidDay; //Early Midday
						lateMidDay = SSlateMidDay; //Late midday
						late = SSlate; // End Time for day
					}
					else
					{
						log.info("OUT getTimeBlock");
						return -1;
					}
						if(startTime.equals(early) && endTime.equals(late))
						{
							log.info("OUT getTimeBlock");
							return 1;
						}
						else if(startTime.equals(early) && endTime.equals(lateMidDay))
						{
							log.info("OUT getTimeBlock");
							return 2;
						}
						else if(startTime.equals(earlyMidDay) && endTime.equals(late))
						{
							log.info("OUT getTimeBlock");
							return 3;
						}
						else if(startTime.equals(early) && endTime.equals(earlyMidDay))
						{
							log.info("OUT getTimeBlock");
							return 4;
						}
						else if(startTime.equals(earlyMidDay) && endTime.equals(lateMidDay))
						{
							log.info("OUT getTimeBlock");
							return 5;
						}
						else if(startTime.equals(lateMidDay) && endTime.equals(late))
						{
							log.info("OUT getTimeBlock");
							return 6;
						}
						else
						{	
							log.info("OUT getTimeBlock");
							return -1;
						}
				}
		
}
