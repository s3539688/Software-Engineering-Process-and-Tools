package gui;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import java.text.SimpleDateFormat;
import java.util.*;

import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.util.Callback;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextField;
import javafx.scene.control.Toggle;
import javafx.scene.control.ToggleButton;
import javafx.scene.control.ToggleGroup;
import javafx.scene.control.ListCell;
import javafx.scene.image.Image;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.StackPane;
import javafx.stage.Modality;
import javafx.stage.Stage;

import program.Booking;
import program.Employee;
import program.EmployeeWorkingTime;
import program.Controller;
import program.DatabaseConnection;
import program.BusinessMenu;
import program.Service;

import org.apache.log4j.Logger;

public class MainController implements Initializable {

	private static Logger log = Logger.getLogger(MainController.class);
	private Controller program = new Controller();
	private DatabaseConnection connection = new DatabaseConnection();
	private Employee employee = null;
	private Booking booking = null;

	public MainController() {}
	
	/**************
	 * B Owner
	 **************/
	
	@FXML
	ToggleGroup bookingViewGroup = new ToggleGroup();
	
	@FXML
	RadioButton rbCurrentBook, rbPastBook;
	
	@FXML
	StackPane stkBusiness, stkCustomer;

	@FXML
	BorderPane boardPaneEmpAdd, boardPaneEmpOverview;

	@FXML
	Button btnRefreshBooking, btnSearchBookings, btnCancelBooking, btnLogout, btnRefreshEmployee, btnSearchEmployee,
			btnConfirm, btnViewEmpDetails;

	@FXML
	Label lblEmployeeID, lblEmployeeName, lblEmployeePayrate, lblEmployeeTitle;

	@FXML
	Label lblBookingID, lblBookingCustomerID, lblBookingDate, lblBookingStartTime, lblBookingEndTime, lblBookingStatus;

	@FXML
	ListView<Booking> listviewBookings;

	@FXML
	ListView<Employee> listviewEmployees;

	@FXML
	ComboBox<Date> cmbBookingDay;

	@FXML
	TextField txtSearchBookings, txtaddEmpFirstName, txtAddEmpLastName, txtAddEmpPayRate, txtSearchEmployee;

	@FXML
	CheckBox chkbxAddWorkingTimes, chbkViewAllBook;

	@FXML
	GridPane gridpWorkingTimes;

	@FXML
	ToggleButton btnSunMorning, btnSunAfternoon, btnSunEvening, btnMonMorning, btnMonAfternoon, btnMonEvening,
			btnTueMorning, btnTueAfternoon, btnTueEvening;
	@FXML
	ToggleButton btnWedMorning, btnWedAfternoon, btnWedEvening, btnThurMorning, btnThurAfternoon, btnThurEvening,
			btnFriMorning, btnFriAfternoon, btnFriEvening;
	@FXML
	ToggleButton btnSatMorning, btnSatAfternoon, btnSatEvening;
	
	/**************
	 * Customer
	 **************/
	
	@FXML
	Button btnBookAppointment, btnConfirmBooking, btnNext, btnLogoutCustomer, btnBack;
	
	@FXML
	Label lblCustomerName, lblDayDate, lblServiceName, lblServiceDur, lblServicePrice, lblCustBookingDate, lblBookingService, lblBookingPrice, lblBookingDur, lblBookingTime, lblBookingEmp;
	
	@FXML
	ComboBox<Date> cmbDayBooking;
	
	@FXML
	ComboBox<Employee> cmbPreferEmp;
	
	@FXML
	ListView<Service> listviewBookingServices;
	
	@FXML
	StackPane stkpnUserMenu, stkpnBookingMenu, stkpnDateService, stkpnTime, stkpnBookingConfirm;
	
	@FXML
	ToggleButton togbtnMorn, togbtnAft, togbtnEven, togbtnTimeSlot1, togbtnTimeSlot2, togbtnTimeSlot3, togbtnTimeSlot4, togbtnTimeSlot5, togbtnTimeSlot6, togbtnTimeSlot7, togbtnTimeSlot8;
	ToggleButton[] bookingTimes = new ToggleButton[]{togbtnTimeSlot1, togbtnTimeSlot2, togbtnTimeSlot3, togbtnTimeSlot4, togbtnTimeSlot5, togbtnTimeSlot6, togbtnTimeSlot7, togbtnTimeSlot8};
	
	
	@FXML
	Label lblAvail1, lblAvail2, lblAvail3, lblAvail4, lblAvail5, lblAvail6, lblAvail7, lblAvail8;
	Label[] timeAvail = new Label[]{lblAvail1, lblAvail2, lblAvail3, lblAvail4, lblAvail5, lblAvail6, lblAvail7, lblAvail8};
	

	/**
	 * initializes the stage
	 * 
	 * @author Joseph
	 */
	@Override
	public void initialize(URL url, ResourceBundle rb) {
		stkBusiness.setVisible(true);
		stkCustomer.setVisible(false);
		boolean var = login();
		if (var == true) {
			if (program.getUser().getAccountType() == 1) {
				stkBusiness.setVisible(true);
				stkCustomer.setVisible(false);
			} else {
				stkBusiness.setVisible(false);
				stkCustomer.setVisible(true);
			}
		} else {
			Platform.exit();
			System.exit(0);
		}
		rbCurrentBook.setSelected(true);
		loadListViewEmp("");
		listviewEmployees.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<Employee>() {
			@Override
			public void changed(ObservableValue<? extends Employee> observable, Employee oldValue, Employee newValue) {
				if (newValue != null) {
					employee = newValue;
					lblEmployeeID.setText(Integer.toString(employee.getId()));
					lblEmployeeName.setText(employee.getName());
					lblEmployeePayrate.setText(Double.toString(employee.getPayRate()));
				}
			}
		});
		loadDaySelect();
		loadListViewBook();
		listviewBookings.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<Booking>() {
			@Override
			public void changed(ObservableValue<? extends Booking> observable, Booking oldValue, Booking newValue) {
				if (newValue != null) {
					booking = newValue;
					lblBookingID.setText(Integer.toString(booking.getBookingID()));
					lblBookingCustomerID.setText(connection.getCustomer(booking.getCustomerId()).getFullName());
					lblBookingDate.setText(program.convertDateToString(booking.getDate()));
					lblBookingStartTime.setText(program.convertTimeToString(booking.getStartTime()));
					lblBookingEndTime.setText(program.convertTimeToString(booking.getEndTime()));
					lblBookingStatus.setText(booking.getStatus());
				}
			}
		});

		cmbBookingDay.valueProperty().addListener(new ChangeListener<Date>() {
			@Override
			public void changed(ObservableValue ov, Date t, Date t1) {
				loadListViewBook();
			}
		});
		
		bookingViewGroup.selectedToggleProperty().addListener(new ChangeListener<Toggle>(){
		    public void changed(ObservableValue<? extends Toggle> ov, Toggle old_toggle, Toggle new_toggle) {
		    	loadDaySelect();
		    	loadListViewBook();
		     } 
		});
		
		cmbDayBooking.valueProperty().addListener(new ChangeListener<Date>() {
			@Override
			public void changed(ObservableValue ov, Date t, Date t1) {
				//TODO
			}
		});
	}

	/**
	 * Returns User to login
	 * 
	 * @author Joseph Garner
	 */
	private void loadListViewEmp(String name) {
		ArrayList<Employee> empArray = new ArrayList<>(connection.getEmployees(name));
		ObservableList<Employee> empList = FXCollections.observableList(empArray);
		log.debug("LOGGER: List length:" + empArray.size());
		if (empList != null) {
			listviewEmployees.setItems(empList);
			listviewEmployees.setCellFactory(new Callback<ListView<Employee>, ListCell<Employee>>() {
				@Override
				public ListCell<Employee> call(ListView<Employee> p) {

					ListCell<Employee> cell = new ListCell<Employee>() {
						@Override
						protected void updateItem(Employee t, boolean bln) {
							super.updateItem(t, bln);
							if (t != null) {
								setText(t.getId() + " " + t.getName() + " " + t.getPayRate());
							}
							else{
								listviewEmployees.setPlaceholder(new Label("No Employees"));
							}
						}
					};
					return cell;
				}
			});
		} else {
			log.warn("Unable to load Employees");
		}
	}

	/**
	 * Loads the current and past 7 days into a combo box
	 * 
	 * @author Joseph Garmer
	 */
	@FXML
	public void loadDaySelect() {
		SimpleDateFormat dayFormat = new SimpleDateFormat("EEEE");
		Calendar date = Calendar.getInstance();
		ArrayList<Date> dateArray = new ArrayList<>();
		log.debug("LOGGER: rbPastBook - "+rbPastBook.isSelected());
		if (rbPastBook.isSelected()) {
			date.add(Calendar.DAY_OF_MONTH, -1);
			for (int i = 0; i < 7; i++) {
				log.debug("LOGGER: Date - " + date);
				dateArray.add(date.getTime());
				date.add(Calendar.DAY_OF_MONTH, -1);
			}
		} else {
			for (int i = 0; i < 7; i++) {
				log.debug("LOGGER: Date - " + date);
				dateArray.add(date.getTime());
				date.add(Calendar.DAY_OF_MONTH, 1);
			}
		}
		ObservableList<Date> dateList = FXCollections.observableList(dateArray);

		if (dateList != null) {
			cmbBookingDay.setItems(dateList);
			cmbBookingDay.getSelectionModel().select(0);
			cmbBookingDay.setCellFactory(new Callback<ListView<Date>, ListCell<Date>>() {

				@Override
				public ListCell<Date> call(ListView<Date> p) {

					final ListCell<Date> cell = new ListCell<Date>() {

						@Override
						protected void updateItem(Date t, boolean bln) {
							super.updateItem(t, bln);

							if (t != null) {
								setText(dayFormat.format(t.getTime()));
							} else {
								setText(null);
							}
						}

					};

					return cell;
				}
			});
			cmbBookingDay.setButtonCell(new ListCell<Date>() {
				@Override
				protected void updateItem(Date t, boolean bln) {
					super.updateItem(t, bln);
					if (t != null) {
						setText(dayFormat.format(t.getTime()));
					} else {
						setText(null);
					}

				}
			});
		}
	}

	/**
	 * loads bookings into list view
	 * 
	 * @author Joseph Garner
	 */
	private void loadListViewBook() {
		if(cmbBookingDay.getSelectionModel().getSelectedItem() != null)
		{
			listviewBookings.getItems().clear();
			Calendar cal = Calendar.getInstance();
			Date now = null;
			SimpleDateFormat dateView = new SimpleDateFormat("dd/MM/yyyy");
			ArrayList<Booking> bookArray = new ArrayList<>(connection.getAllBooking());
			ArrayList<Booking> bookArrayView = new ArrayList<>();
			for (Booking b : bookArray) {
				cal.setTime(b.getDate());
				now = cal.getTime();
				log.debug("LOGGER: selected day - " + cmbBookingDay.getSelectionModel().getSelectedItem());
				log.debug("LOGGER: booking date - " + dateView.format(now));
				if (dateView.format(now).equals(dateView.format(cmbBookingDay.getSelectionModel().getSelectedItem()))) {
					bookArrayView.add(b);
					log.debug("LOGGER: Booking added - " + b.toString());
				}
			}
			ObservableList<Booking> bookList = FXCollections.observableList(bookArrayView);
			log.debug("LOGGER: List length:" + bookArrayView.size());
			if (bookList != null) {
				listviewBookings.setItems(bookList);
				listviewBookings.setCellFactory(new Callback<ListView<Booking>, ListCell<Booking>>() {
					@Override
					public ListCell<Booking> call(ListView<Booking> p) {
	
						ListCell<Booking> cell = new ListCell<Booking>() {
							@Override
							protected void updateItem(Booking t, boolean bln) {
								super.updateItem(t, bln);
								if (t != null) {
									setText(t.getBookingID() + " " + connection.getCustomer(t.getCustomerId()).getFullName()
											+ " " + program.convertTimeToString(t.getStartTime()));
								}
								else{
									listviewEmployees.setPlaceholder(new Label("No Bookings"));
								}
							}
						};
						return cell;
					}
				});
			} else {
				log.warn("Unable to load Employees");
			}
		}
	}
	
	/**
	 * loads all bookings into list view
	 * 
	 * @author Joseph Garner
	 */
	public void loadAllBookings(){
		if(cmbBookingDay.getSelectionModel().getSelectedItem() != null)
		{
			listviewBookings.getItems().clear();
			ArrayList<Booking> bookArray = new ArrayList<>(connection.getAllBooking());
			ObservableList<Booking> bookList = FXCollections.observableList(bookArray);
			log.debug("LOGGER: List length:" + bookArray.size());
			if (bookList != null) {
				listviewBookings.setItems(bookList);
				listviewBookings.setCellFactory(new Callback<ListView<Booking>, ListCell<Booking>>() {
					@Override
					public ListCell<Booking> call(ListView<Booking> p) {
	
						ListCell<Booking> cell = new ListCell<Booking>() {
							@Override
							protected void updateItem(Booking t, boolean bln) {
								super.updateItem(t, bln);
								if (t != null) {
									setText(t.getBookingID() + " " + connection.getCustomer(t.getCustomerId()).getFullName()
											+ " " + program.convertTimeToString(t.getStartTime()));
								}
								else{
									listviewEmployees.setPlaceholder(new Label("No Bookings"));
								}
							}
						};
						return cell;
					}
				});
			} else {
				log.warn("Unable to load Employees");
			}
		}
	}

	/**************
	 * LOGIN
	 **************/
	
	/**
	 * Logs the user out
	 * 
	 * @author [Programmer]
	 */
	@FXML
	public void logout()
	{
		
	}
	
	/**
	 * Returns User to login
	 * 
	 * @author [Programmer]
	 */
	@FXML
	public boolean login() {
		log.debug("Login Started");
		try {
			Stage secondaryStage = new Stage();
			secondaryStage.getIcons().add(new Image("images/ic_collections_bookmark_black_48dp_2x.png"));
			Parent root = FXMLLoader.load(getClass().getResource("loginLayout.fxml"));
			secondaryStage.setTitle("Login");
			secondaryStage.setResizable(false);
			secondaryStage.setScene(new Scene(root));
			secondaryStage.initModality(Modality.APPLICATION_MODAL);
			secondaryStage.showAndWait();
			if (program.getUser() != null) {
				return true;
			}
		} catch (IOException ioe) {
			log.warn(ioe.getMessage());
		}
		log.debug("false");
		return false;
	}

	/**************
	 * BOOKINGS
	 **************/

	/**
	 * Displays all bookings to user
	 * 
	 * @author Joseph Garner
	 */
	@FXML
	public void viewAllBookings()
	{
		if(chbkViewAllBook.isSelected())
    	{
    		cmbBookingDay.setDisable(true);
    		rbPastBook.setDisable(true);
    		rbPastBook.setSelected(false);
    		rbCurrentBook.setDisable(true);
    		rbCurrentBook.setSelected(false);
    		loadAllBookings();
    	}
    	else{
    		cmbBookingDay.setDisable(false);
    		rbPastBook.setDisable(false);
    		rbCurrentBook.setDisable(false);
    		rbCurrentBook.setSelected(true);
    		loadListViewBook();
    	}
	}
	
	/**
	 * Program Allows the user to search and displays bookings
	 * 
	 * @author Joseph Garner
	 */
	@FXML
	public void searchBookings() {
		// TODO
		log.debug("LOGGER: Entered searchBookings function");
		listviewBookings.getItems().clear();
		Calendar cal = Calendar.getInstance();
		Date now = null;
		SimpleDateFormat dateView = new SimpleDateFormat("dd/MM/yyyy");
		ArrayList<Booking> bookArray = new ArrayList<>(connection.getAllBooking());
		ArrayList<Booking> bookArrayView = new ArrayList<>();
		String fname = null;
		String lname = null;
		String fullName = null;
		for (Booking b : bookArray) {
			cal.setTime(b.getDate());
			now = cal.getTime();
			fname = connection.getCustomer(b.getCustomerId()).getFName();
			lname = connection.getCustomer(b.getCustomerId()).getLName();
			fullName = fname + " " + lname;
			boolean checkF = program.searchMatch(fname.toUpperCase( ), txtSearchBookings.getText().toUpperCase( ));
			boolean checkL = program.searchMatch(lname.toUpperCase( ), txtSearchBookings.getText().toUpperCase( ));
			boolean checkFL = program.searchMatch(fullName.toUpperCase( ), txtSearchBookings.getText().toUpperCase( ));
			log.debug("LOGGER: Users Name - "+fullName);
			if (checkF || checkL || checkFL) {
				if(chbkViewAllBook.isSelected()){
					bookArrayView.add(b);
				}
				else if (!chbkViewAllBook.isSelected() && dateView.format(now).equals(dateView.format(cmbBookingDay.getSelectionModel().getSelectedItem()))){
					bookArrayView.add(b);
					log.debug("LOGGER: Booking added - " + b.toString());
				}
				else{}
			}
		}
		ObservableList<Booking> bookList = FXCollections.observableList(bookArrayView);
		log.debug("LOGGER: List length:" + bookArrayView.size());
		if (bookList != null) {
			listviewBookings.setItems(bookList);
			listviewBookings.setCellFactory(new Callback<ListView<Booking>, ListCell<Booking>>() {
				@Override
				public ListCell<Booking> call(ListView<Booking> p) {

					ListCell<Booking> cell = new ListCell<Booking>() {
						@Override
						protected void updateItem(Booking t, boolean bln) {
							super.updateItem(t, bln);
							if (t != null) {
								setText(t.getBookingID() + " " + connection.getCustomer(t.getCustomerId()).getFullName()
										+ " " + program.convertTimeToString(t.getStartTime()));
							}
						}
					};
					return cell;
				}
			});
		} else {
			program.messageBox("WARN", "Search Result", "No Search Results Found", "");
		}
	}

	/**
	 * Cancels booking (does not remove the booking from view)
	 * 
	 * @author [Programmer]
	 */
	@FXML
	public void cancelBooking() {
		// TODO
		log.debug("LOGGER: Entered cancelBooking function");
		Alert alert = new Alert(AlertType.CONFIRMATION);
		alert.setTitle("Cancel Booking");
		alert.setHeaderText("Cancel Selected Booking");
		alert.setContentText("Are you sure?");

		Optional<ButtonType> result = alert.showAndWait();
		if (result.get() == ButtonType.OK) {
			Booking book = null;
			book = listviewBookings.getSelectionModel().getSelectedItem();
			connection.cancelBooking(book.getBookingID());
			Alert feedback = new Alert(AlertType.INFORMATION);
			feedback.setTitle("Cancel Booking");
			feedback.setHeaderText("Booking has been cancelled");
			feedback.showAndWait();

		} else {
			return;
		}
		listviewBookings.getSelectionModel().clearSelection();
		loadListViewBook();
	}
	
	
	/**
	 * Allows the user view past bookings
	 * 
	 * @author Joseph Garner
	 */
	@FXML
	public void lookToThePast()
	{
		
	}

	/**
	 * Displays and refreshes booking view to all
	 * 
	 * @author Joseph Garner
	 */
	@FXML
	public void refreshBookingView() {
		log.debug("LOGGER: Entered refreshBookingView function");
		txtSearchBookings.setText("");
		loadListViewBook();
	}

	/**
	 * Shows add employee
	 * note tabs should be disabled
	 * @author Luke Mason
	 */

	/**************
	 * EMPLOYEE
	 **************/
	@FXML
	public void showAddNewEmp() {
		boardPaneEmpAdd.setVisible(true);
		boardPaneEmpOverview.setVisible(false);
		// TODO
	}

	/**
	 * Returns User to manage employees
	 * @author Luke Mason
	 */
	@FXML
	public void cancelAddNewEmp() {
		boardPaneEmpAdd.setVisible(false);
		boardPaneEmpOverview.setVisible(true);
		//Resetting the add Employee board to default values
		txtaddEmpFirstName.setText("");
		txtAddEmpLastName.setText(""); 
		txtAddEmpPayRate.setText(""); 
		chkbxAddWorkingTimes.setSelected(false);
		btnSunMorning.setSelected(false);
		btnSunAfternoon.setSelected(false);
		btnSunEvening.setSelected(false);
		btnMonMorning.setSelected(false);
		btnMonAfternoon.setSelected(false);
		btnMonEvening.setSelected(false);
		btnTueMorning.setSelected(false);
		btnTueAfternoon.setSelected(false);
		btnTueEvening.setSelected(false);
		btnWedMorning.setSelected(false);
		btnWedAfternoon.setSelected(false);
		btnWedEvening.setSelected(false);
		btnThurMorning.setSelected(false);
		btnThurAfternoon.setSelected(false);
		btnThurEvening.setSelected(false);
		btnFriMorning.setSelected(false);
		btnFriAfternoon.setSelected(false);
		btnFriEvening.setSelected(false);
		btnSatMorning.setSelected(false);
		btnSatAfternoon.setSelected(false);
		btnSatEvening.setSelected(false);
		// TODO
	}

	/**
	 * Enables and disables working times
	 * 
	 * @author [Joseph Garner]
	 */
	@FXML
	public void allowWorkingTimes() {
		if (chkbxAddWorkingTimes.isSelected()) {
			gridpWorkingTimes.setDisable(false);
		} else {
			gridpWorkingTimes.setDisable(true);
		}
		// TODO
	}

	/**
	 * Creates an Employee
	 * 
	 * @author Luke Mason
	 */
	@FXML
	public void createEmp() 
	{
		BusinessMenu bMenu = new BusinessMenu();
		boolean firstName = bMenu.checkEmployeeFirstOrLastName(txtaddEmpFirstName.getText());
		if(!firstName)
		{
			program.messageBox("ERROR", "First Name Invalid", "First Name Invalid","First Name entered is not a valid first name\nReason: First name contains invalid characters");
			return;
		}
		boolean lastName = bMenu.checkEmployeeFirstOrLastName(txtAddEmpLastName.getText());
		if(!lastName)
		{
			program.messageBox("ERROR", "Last Name Invalid", "Last Name Invalid","Last Name entered is not a valid last name\nReason: Last name contains invalid characters");
			return;
		}
		double payRate = bMenu.strPayRateToDouble(txtAddEmpPayRate.getText());
		boolean PayRate = bMenu.checkEmployeePayRate(payRate);
		if(!PayRate)
		{
			program.messageBox("ERROR", "Pay Rate Invalid", "Pay Rate Invalid","Pay rate entered is not a valid pay rate\nReason: Pay Rate is not 0 - 1000");
			return;
		}
		if(PayRate && firstName && lastName)
		{
			if(chkbxAddWorkingTimes.isSelected())
			{
				bMenu.option2AddEmployeeAndWorkingTimes(txtaddEmpFirstName.getText()
						,txtAddEmpLastName.getText(), payRate, btnSunMorning.isSelected(), btnSunAfternoon.isSelected()
						, btnSunEvening.isSelected(), btnMonMorning.isSelected(), btnMonAfternoon.isSelected(), btnMonEvening.isSelected()
						, btnTueMorning.isSelected(), btnTueAfternoon.isSelected(), btnTueEvening.isSelected(), btnWedMorning.isSelected()
						, btnWedAfternoon.isSelected(), btnWedEvening.isSelected(), btnThurMorning.isSelected(), btnThurAfternoon.isSelected()
						, btnThurEvening.isSelected(), btnFriMorning.isSelected(), btnFriAfternoon.isSelected(), btnFriEvening.isSelected()
						, btnSatMorning.isSelected(), btnSatAfternoon.isSelected(), btnSatEvening.isSelected());
				program.messageBox("SUCCESS", "New Employee Added", "New Employee Added",txtaddEmpFirstName.getText()+" "+txtAddEmpLastName.getText()+" with $"+payRate+"/h was Added!"); 
			}
			else
			{
				bMenu.option1AddEmployee(txtaddEmpFirstName.getText(),txtAddEmpLastName.getText(), payRate);	
				program.messageBox("SUCCESS", "SUCCESS", "New Employee Added",txtaddEmpFirstName.getText()+" "+txtAddEmpLastName.getText()+" with $"+payRate+"/h was Added!");
			}
		}
		refreshEmployeeView();
		cancelAddNewEmp();
	}

	/**
	 * Resets the filtered employee list view
	 * @author Luke Mason
	 */
	@FXML

	public void refreshEmployeeView()
	{
		txtSearchEmployee.setText("");
		loadListViewEmp("");
	}

	/**
	 * Filters the list of employees
	 * @author Luke Mason
	 */
	@FXML

	public void searchEmployee()
	{
		String name = txtSearchEmployee.getText();
		loadListViewEmp(name);
	}

	/**
	 * Views Employees Details
	 * @author Luke Mason
	 */
	@FXML

	public void viewEmpDetails()
	{
		BusinessMenu bMenu = new BusinessMenu();
		program.messageBox("SUCCESS", "Not Implemented","","");

		int employeeID = -1;
		String eFirstName = "";
		String eLastName = "";
		String ePayRate = "";
		boolean v1 = false;
		boolean v2 = false;
		boolean v3 = false;
		boolean v4 = false;
		boolean v5 = false;
		boolean v6 = false;
		boolean v7 = false;
		boolean v8 = false;
		boolean v9 = false;
		boolean v10 = false;
		boolean v11 = false;
		boolean v12 = false;
		boolean v13 = false;
		boolean v14 = false;	
		boolean v15 = false;
		boolean v16 = false;
		boolean v17 = false;
		boolean v18 = false;
		boolean v19 = false;
		boolean v20 = false;
		boolean v21 = false;
		boolean v22 = false;
		
		txtaddEmpFirstName.setText(eFirstName);
		txtAddEmpLastName.setText(eLastName); 
		txtAddEmpPayRate.setText(ePayRate); 
		
		chkbxAddWorkingTimes.setSelected(v1);
		
		btnSunMorning.setSelected(v2);
		btnSunAfternoon.setSelected(v3);
		btnSunEvening.setSelected(v4);
		btnMonMorning.setSelected(v5);
		btnMonAfternoon.setSelected(v6);
		btnMonEvening.setSelected(v7);
		btnTueMorning.setSelected(v8);
		btnTueAfternoon.setSelected(v9);
		btnTueEvening.setSelected(v10);
		btnWedMorning.setSelected(v11);
		btnWedAfternoon.setSelected(v12);
		btnWedEvening.setSelected(v13);
		btnThurMorning.setSelected(v14);
		btnThurAfternoon.setSelected(v15);
		btnThurEvening.setSelected(v16);
		btnFriMorning.setSelected(v17);
		btnFriAfternoon.setSelected(v18);
		btnFriEvening.setSelected(v19);
		btnSatMorning.setSelected(v20);
		btnSatAfternoon.setSelected(v21);
		btnSatEvening.setSelected(v22);
		
		//lblEmployeeTitle.setText("Change Employee Details");
		//Get chosen employee ID
		ArrayList<EmployeeWorkingTime> workTimes = new ArrayList<EmployeeWorkingTime>();
		workTimes = bMenu.getEmployeeWorkTimes(employeeID);
		
	}

	/**
	 * Deletes Employee
	 * @author Luke Mason
	 */
	@FXML
	public void deleteEmplyee() {
		// TODO
	}
	
	
	
	/**************
	 * Customer
	 **************/
	
	/**
	 * Enters to booking phase
	 * @author [Programmer]
	 */
	@FXML
	public void startBooking(){
		//TODO
		if(stkpnUserMenu.isVisible())
		{
			stkpnUserMenu.setVisible(false);
			stkpnBookingMenu.setVisible(true);
			return;
		}
		
	}
	
	/**
	 * Moves the customer forward
	 * @author [Programmer]
	 */
	@FXML
	public void nextView(){
		if(stkpnDateService.isVisible() && stkpnBookingMenu.isVisible())
		{
			stkpnDateService.setVisible(false);
			stkpnTime.setVisible(true);
			//TODO
			return;
		}
		if(stkpnTime.isVisible() && stkpnBookingMenu.isVisible())
		{
			stkpnTime.setVisible(false);
			stkpnBookingMenu.setVisible(false);
			stkpnBookingConfirm.setVisible(true);
			//TODO
			return;
		}
		
	}
	
	/**
	 * Moves the customer backward
	 * @author Joseph Garner
	 */
	@FXML
	public void backView(){
		if(stkpnDateService.isVisible() && stkpnBookingMenu.isVisible())
		{
			stkpnBookingMenu.setVisible(false);
			stkpnUserMenu.setVisible(true);
			return;
		}
		if(stkpnTime.isVisible() && stkpnBookingMenu.isVisible())
		{
			stkpnTime.setVisible(false);
			stkpnDateService.setVisible(true);
			return;
		}
		if(stkpnBookingConfirm.isVisible())
		{
			stkpnBookingConfirm.setVisible(false);
			stkpnTime.setVisible(true);
			stkpnBookingMenu.setVisible(true);
			return;
		}
		
	}
	
	/**
	 * Deletes Employee
	 * @author [Programmer]
	 */
	@FXML
	public void confirmBooking(){
		//TODO
	}
}
