package gui;

import java.io.IOException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Optional;
import java.util.ResourceBundle;

import org.apache.log4j.Logger;

import gui.IInterface.ISetup;
import gui.IInterface.IUser;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextField;
import javafx.scene.control.Toggle;
import javafx.scene.control.ToggleButton;
import javafx.scene.control.ToggleGroup;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.image.Image;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.util.Callback;
import program.Booking;
import program.BusinessMenu;
import program.BusinessOwner;
import program.Controller;
import program.Customer;
import program.DatabaseConnection;
import program.Employee;
import program.EmployeeWorkingTime;
import program.Service;
import program.User;

public class BusinessController implements Initializable, IUser  {
	private static Logger log = Logger.getLogger(BusinessController.class);
	private static Controller program = new Controller();
	private DatabaseConnection connection = new DatabaseConnection();
	private Employee employee = null;
	private Booking booking = null;
	int globalEmployeeOption = 0;
	UserFactory userFactory = new UserFactory();
	SetupController setupC = new SetupController();

	public BusinessController() {}

	/**************
	 * Business Owner
	 **************/

	@FXML
	AnchorPane root;
	
	@FXML
	ToggleGroup bookingViewGroup = new ToggleGroup();

	@FXML
	RadioButton rbCurrentBook, rbPastBook;

	@FXML
	BorderPane boardPaneEmpAdd, boardPaneEmpOverview;

	@FXML
	Button btnRefreshBooking, btnSearchBookings, btnCancelBooking, btnLogout, btnRefreshEmployee, btnSearchEmployee,
			btnConfirm, btnViewEmpDetails;

	@FXML
	Label lblEmployeeID, lblEmployeeName, lblEmployeePayrate, lblEmployeeTitle;

	@FXML
	Label lblBookingID, lblBookingCustomerID, lblBookingDate, lblBookingStartTime, lblBookingEndTime, lblBookingStatus,
			lblBookServ;

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

	@FXML
	Label lblServiceID, lblManServiceName, lblManServicePrice, lblServiceDura;

	@FXML
	ListView<Service> listviewManServices;

	@FXML
	Button btnRefershServices, btnDeleteService, btnAddService, btnSearchServices;

	@FXML
	TextField txtSearchService;

	@FXML
	Label lblCustFirstName, lblCustLastName, lblCustUsername, lblCustEmail, lblCustMobile, lblCustDOB, lblCustGender;

	@FXML
	ListView<Customer> listviewCustomers;

	@FXML
	Button btnSearchCustomer, btnDeleteCustomer, btnCreateBooking, btnRefershCustomers;

	@FXML
	TextField txtSearchCustomer;

	private User _user = null;


	/**
	 * initializes the stage
	 * 
	 * @author Joseph
	 */
	@Override
	public void initialize(URL url, ResourceBundle rb) {
		BusinessOwner BO = connection.getOneBusiness(program.business().getBusinessId());
		if(BO == null)
		{
			ISetup setup = userFactory.getSetup("SetUp");
			setup.getSetup();
			BO = connection.getOneBusiness(program.business().getBusinessId());
		}
		if(BO != null)
		{
			String wds = program.timeToStr(BO.getWeekdayStart());
			String wde = program.timeToStr(BO.getWeekdayEnd());
			String wes = program.timeToStr(BO.getWeekendStart());
			String wee = program.timeToStr(BO.getWeekendEnd());
			setupC.assignOpenClosingTimesToGlobal(wds, wde, wes, wee);
		}
		else{
			Stage stage= (Stage) btnRefershServices.getScene().getWindow();
			stage.close();
		}
		if(connection.getOneBusiness(program.getUser().getBusinessID()).color() >= 1){
			setCI();
		}
		initialiseWorkTimeBusinessMenu();
		
		loadDaySelect();
		loadallServices();
			rbCurrentBook.setSelected(true);
			loadListViewEmp("");
			listviewEmployees.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<Employee>() {
				@Override
				public void changed(ObservableValue<? extends Employee> observable, Employee oldValue,
						Employee newValue) {
					if (newValue != null) {
						employee = newValue;
						lblEmployeeID.setText(Integer.toString(employee.getId()));
						lblEmployeeName.setText(employee.getName());
						lblEmployeePayrate.setText(Double.toString(employee.getPayRate()));
					}
				}
			});
			loadListViewBook();
			loadallCustomers();
			listviewManServices.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<Service>() {
				@Override
				public void changed(ObservableValue<? extends Service> observable, Service oldValue, Service newValue) {
					if (newValue != null) {
						lblServiceID.setText(Integer.toString(newValue.getID()));
						lblManServiceName.setText(newValue.getName());
						lblManServicePrice.setText("$" + Double.toString(newValue.getPrice()));
						lblServiceDura.setText(Integer.toString(newValue.getLengthMin()));
					}
				}
			});
			listviewCustomers.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<Customer>() {
				@Override
				public void changed(ObservableValue<? extends Customer> observable, Customer oldValue,
						Customer newValue) {
					if (newValue != null) {
						lblCustFirstName.setText(newValue.getFName());
						lblCustLastName.setText(newValue.getLName());
						lblCustEmail.setText(newValue.getEmail());
						lblCustMobile.setText(newValue.getPhone());
						lblCustDOB.setText(newValue.getDOB());
						lblCustGender.setText(newValue.getGender());
					}
				}
			});
			cmbBookingDay.valueProperty().addListener(new ChangeListener<Date>() {
				@Override
				public void changed(ObservableValue ov, Date t, Date t1) {
					loadListViewBook();
				}
			});

			bookingViewGroup.selectedToggleProperty().addListener(new ChangeListener<Toggle>() {
				public void changed(ObservableValue<? extends Toggle> ov, Toggle old_toggle, Toggle new_toggle) {
					loadDaySelect();
					loadListViewBook();
				}
			});
			listLVBookini();
		}

	public void initialiseWorkTimeBusinessMenu()
	{
		String MFearly = BusinessMenu.MFearly; //Start Time for day
		String MFearlyMidDay = BusinessMenu.MFearlyMidDay; //Early Midday
		String MFlateMidDay = BusinessMenu.MFlateMidDay; //Late midday
		String MFlate = BusinessMenu.MFlate; // End Time for day
		String SSearly = BusinessMenu.SSearly; //Start Time for day
		String SSearlyMidDay = BusinessMenu.SSearlyMidDay; //Early Midday
		String SSlateMidDay = BusinessMenu.SSlateMidDay; //Late midday
		String SSlate = BusinessMenu.SSlate; // End Time for day
		
		String sSMorning = SSearly+" - "+SSearlyMidDay;
		String sSAfternoon = SSearlyMidDay+" - "+SSlateMidDay;
		String sSEvening = SSlateMidDay+" - "+SSlate;
		
		String mFMorning = MFearly +" - "+MFearlyMidDay;
		String mFAfternoon = MFearlyMidDay +" - "+MFlateMidDay;
		String mFEvening = MFlateMidDay +" - "+MFlate;
		
		btnSunMorning.setText(sSMorning);
		btnSunAfternoon.setText(sSAfternoon);
		btnSunEvening.setText(sSEvening);
		
		btnSatMorning.setText(sSMorning);
		btnSatAfternoon.setText(sSAfternoon);
		btnSatEvening.setText(sSEvening);
		
		btnMonMorning.setText(mFMorning); 
		btnMonAfternoon.setText(mFAfternoon);
		btnMonEvening.setText(mFEvening); 
		
		btnTueMorning.setText(mFMorning);
		btnTueAfternoon.setText(mFAfternoon);
		btnTueEvening.setText(mFEvening);
		
		btnWedMorning.setText(mFMorning);
		btnWedAfternoon.setText(mFAfternoon);
		btnWedEvening.setText(mFEvening);
		
		btnThurMorning.setText(mFMorning);
		btnThurAfternoon.setText(mFAfternoon);
		btnThurEvening.setText(mFEvening);
		
		btnFriMorning.setText(mFMorning);
		btnFriAfternoon.setText(mFAfternoon);
		btnFriEvening.setText(mFEvening);
	}
	
	/**
	 * Returns User to login
	 * 
	 * @author Joseph Garner
	 */
	private void loadListViewEmp(String name) {
		ArrayList<Employee> empArray = new ArrayList<>(connection.getEmployees(name,program.business().getBusinessId()));
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
								setText(t.getName());
							} else {
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
		log.debug("LOGGER: rbPastBook - " + rbPastBook.isSelected());
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
		if (cmbBookingDay.getSelectionModel().getSelectedItem() != null) {
			listviewBookings.getItems().clear();
			Calendar cal = Calendar.getInstance();
			Date now = null;
			SimpleDateFormat dateView = new SimpleDateFormat("dd/MM/yyyy");
			ArrayList<Booking> bookArray = new ArrayList<>(connection.getAllBooking(program.business().getBusinessId()));
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
									setText(t.getBookingID() + " "
											+ connection.getCustomer(t.getCustomerId()).getFullName() + " "
											+ program.timeToStr(t.getStartTime()));
								} else {
									listviewBookings.setPlaceholder(new Label("No Bookings"));
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
	public void loadAllBookings() {
		if (cmbBookingDay.getSelectionModel().getSelectedItem() != null) {
			listviewBookings.getItems().clear();
			ArrayList<Booking> bookArray = new ArrayList<>(connection.getAllBooking(program.business().getBusinessId()));
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
									setText(t.getBookingID() + " "
											+ connection.getCustomer(t.getCustomerId()).getFullName() + " "
											+ program.timeToStr(t.getStartTime()));
								} else {
									listviewBookings.setPlaceholder(new Label("No Bookings"));
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
	 * loads all Services into list view
	 * 
	 * @author Joseph Garner
	 */
	@FXML
	public void loadallServices() {
		listviewManServices.getItems().clear();
		ArrayList<Service> serviceArray = new ArrayList<>(connection.getAllServices(program.business().getBusinessId()));
		ObservableList<Service> serviceList = FXCollections.observableList(serviceArray);
		log.debug("LOGGER: List length:" + serviceArray.size());
		if (serviceList != null) {
			listviewManServices.setItems(serviceList);
			listviewManServices.setCellFactory(new Callback<ListView<Service>, ListCell<Service>>() {
				@Override
				public ListCell<Service> call(ListView<Service> p) {

					ListCell<Service> cell = new ListCell<Service>() {
						@Override
						protected void updateItem(Service t, boolean bln) {
							super.updateItem(t, bln);
							if (t != null) {
								log.debug("LOGGER: added:" + t.getName());
								setText(t.getName());
							} else {
								listviewManServices.setPlaceholder(new Label("No Services"));
							}
						}
					};
					return cell;
				}
			});
		}
	}

	/**
	 * loads all customers into list view
	 * 
	 * @author Joseph Garner
	 */
	@FXML
	public void loadallCustomers() {
		listviewCustomers.getItems().clear();
		ArrayList<Customer> serviceArray = new ArrayList<>(connection.getAllCustomer(program.business().getBusinessId()));
		ObservableList<Customer> serviceList = FXCollections.observableList(serviceArray);
		log.debug("LOGGER: List length:" + serviceArray.size());
		if (serviceList != null) {
			listviewCustomers.setItems(serviceList);
			listviewCustomers.setCellFactory(new Callback<ListView<Customer>, ListCell<Customer>>() {
				@Override
				public ListCell<Customer> call(ListView<Customer> p) {

					ListCell<Customer> cell = new ListCell<Customer>() {
						@Override
						protected void updateItem(Customer t, boolean bln) {
							super.updateItem(t, bln);
							if (t != null) {
								log.debug("LOGGER: added:" + t.getFName());
								setText(t.getFName() + " " + t.getLName());
							} else {
								listviewCustomers.setPlaceholder(new Label("No Customer"));
							}
						}
					};
					return cell;
				}
			});
		}
	}
	
	/**
	 * Logs the user out
	 * 
	 * @author Luke Mason
	 */
	@FXML
	public void logout() {
		//stage = (Stage) stkBusiness.getScene().getWindow();
		Alert alert = new Alert(AlertType.CONFIRMATION);
		alert.setTitle("Logout");
		alert.setHeaderText("Logout of Account?");
		alert.setContentText("Are you sure?");

		Optional<ButtonType> result = alert.showAndWait();
		if (result.get() == ButtonType.OK) {
			//stage.hide();
			program.setUser(null);
			Stage st = (Stage) btnLogout.getScene().getWindow();
			st.close();
		} else {
			return;
		}
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
	public void viewAllBookings() {
		if (chbkViewAllBook.isSelected()) {
			cmbBookingDay.setDisable(true);
			rbPastBook.setDisable(true);
			rbPastBook.setSelected(false);
			rbCurrentBook.setDisable(true);
			rbCurrentBook.setSelected(false);
			loadAllBookings();
		} else {
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
		log.debug("LOGGER: Entered searchBookings function");
		listviewBookings.getItems().clear();
		Calendar cal = Calendar.getInstance();
		Date now = null;
		SimpleDateFormat dateView = new SimpleDateFormat("dd/MM/yyyy");
		ArrayList<Booking> bookArray = new ArrayList<>(connection.getAllBooking(program.business().getBusinessId()));
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
			boolean checkF = program.searchMatch(fname.toUpperCase(), txtSearchBookings.getText().toUpperCase());
			boolean checkL = program.searchMatch(lname.toUpperCase(), txtSearchBookings.getText().toUpperCase());
			boolean checkFL = program.searchMatch(fullName.toUpperCase(), txtSearchBookings.getText().toUpperCase());
			log.debug("LOGGER: Users Name - " + fullName);
			if (checkF || checkL || checkFL) {
				if (chbkViewAllBook.isSelected()) {
					bookArrayView.add(b);
				} else if (!chbkViewAllBook.isSelected() && dateView.format(now)
						.equals(dateView.format(cmbBookingDay.getSelectionModel().getSelectedItem()))) {
					bookArrayView.add(b);
					log.debug("LOGGER: Booking added - " + b.toString());
				} else {
				}
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
										+ " " + program.timeToStr(t.getStartTime()));
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
	 * @author Luke Mason
	 */
	@FXML
	public void cancelBooking() {
		log.debug("LOGGER: Entered cancelBooking function");
		if (listviewBookings.getSelectionModel().getSelectedIndex() < 0) {
			program.messageBox("WARN", "oops", "A Booking has not been Selected",
					"Please Select a booking and try again");
			return;
		}
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
	 * Shows add employee note tabs should be disabled
	 * 
	 * @author Luke Mason
	 */

	/**************
	 * EMPLOYEE
	 **************/
	@FXML
	public void showAddNewEmp() {
		boardPaneEmpAdd.setVisible(true);
		boardPaneEmpOverview.setVisible(false);
	}

	/**
	 * Returns User to manage employees
	 * 
	 * @author Luke Mason
	 */
	@FXML
	public void cancelAddNewEmp() {
		boardPaneEmpAdd.setVisible(false);
		boardPaneEmpOverview.setVisible(true);
		// Resetting the add Employee board to default values
		txtaddEmpFirstName.setText("");
		txtAddEmpLastName.setText("");
		txtAddEmpPayRate.setText("");
		chkbxAddWorkingTimes.setSelected(false);
		setAllTogglesToFalse();
		globalEmployeeOption = 0;
		lblEmployeeTitle.setText("Add New Employee");
		gridpWorkingTimes.setDisable(true);
	}

	/**
	 * Enables and disables working times
	 * 
	 * @author Joseph Garner
	 */
	@FXML
	public void allowWorkingTimes() {
		if (chkbxAddWorkingTimes.isSelected()) {
			gridpWorkingTimes.setDisable(false);
		} else {
			gridpWorkingTimes.setDisable(true);
		}
	}

	/**
	 * Creates an Employee
	 * 
	 * @author Luke Mason
	 */
	@FXML
	public void createEmp() {
		int businessID = program.business().getBusinessId();
		BusinessMenu bMenu = new BusinessMenu();
		boolean firstName = !program.checkInputToContainInvalidChar(txtaddEmpFirstName.getText());
		log.debug("First Name = " + firstName + "\n");
		if (!firstName) {
			program.messageBox("ERROR", "First Name Invalid", "First Name Invalid",
					"First Name entered is not a valid first name\nReason: First name contains invalid characters");
			return;
		}
		boolean lastName = !program.checkInputToContainInvalidChar(txtAddEmpLastName.getText());
		log.debug("last Name = " + lastName + "\n");
		if (!lastName) {
			program.messageBox("ERROR", "Last Name Invalid", "Last Name Invalid",
					"Last Name entered is not a valid last name\nReason: Last name contains invalid characters");
			return;
		}
		double payRate = bMenu.strPayRateToDouble(txtAddEmpPayRate.getText());
		log.debug("pay rate = " + payRate + "\n");
		boolean PayRate = bMenu.checkEmployeePayRate(payRate);
		log.debug("is pay rate okay? " + PayRate + "\n");
		if (!PayRate) {
			program.messageBox("ERROR", "Pay Rate Invalid", "Pay Rate Invalid",
					"Pay rate entered is not a valid pay rate\nReason: Pay Rate is not 0 - 1000");
			return;
		}

		if (PayRate && firstName && lastName) {
			log.debug("globalEmployeeOption = " + globalEmployeeOption + "\n");
			log.debug("chkboxWorkingTimes = " + chkbxAddWorkingTimes.isSelected() + "\n");
			if (chkbxAddWorkingTimes.isSelected()) {
				if (!bMenu.checkWorkTimes(btnSunMorning.isSelected(), btnSunAfternoon.isSelected(),
						btnSunEvening.isSelected(), btnMonMorning.isSelected(), btnMonAfternoon.isSelected(),
						btnMonEvening.isSelected(), btnTueMorning.isSelected(), btnTueAfternoon.isSelected(),
						btnTueEvening.isSelected(), btnWedMorning.isSelected(), btnWedAfternoon.isSelected(),
						btnWedEvening.isSelected(), btnThurMorning.isSelected(), btnThurAfternoon.isSelected(),
						btnThurEvening.isSelected(), btnFriMorning.isSelected(), btnFriAfternoon.isSelected(),
						btnFriEvening.isSelected(), btnSatMorning.isSelected(), btnSatAfternoon.isSelected(),
						btnSatEvening.isSelected())) {
					return;
				}
				if (globalEmployeeOption == 0) {
					bMenu.addEmployee(txtaddEmpFirstName.getText(), txtAddEmpLastName.getText(), payRate,businessID);
					int id = bMenu.getLastEmployeeId(businessID);
					bMenu.addWorkingTimes(id, btnSunMorning.isSelected(), btnSunAfternoon.isSelected(),
							btnSunEvening.isSelected(), btnMonMorning.isSelected(), btnMonAfternoon.isSelected(),
							btnMonEvening.isSelected(), btnTueMorning.isSelected(), btnTueAfternoon.isSelected(),
							btnTueEvening.isSelected(), btnWedMorning.isSelected(), btnWedAfternoon.isSelected(),
							btnWedEvening.isSelected(), btnThurMorning.isSelected(), btnThurAfternoon.isSelected(),
							btnThurEvening.isSelected(), btnFriMorning.isSelected(), btnFriAfternoon.isSelected(),
							btnFriEvening.isSelected(), btnSatMorning.isSelected(), btnSatAfternoon.isSelected(),
							btnSatEvening.isSelected());
					program.messageBox("SUCCESS", "New Employee Added", "New Employee Added",
							txtaddEmpFirstName.getText() + " " + txtAddEmpLastName.getText() + " with $" + payRate
									+ "/h was Added!");
				} else {
					Employee employee = listviewEmployees.getSelectionModel().getSelectedItem();
					int employeeID = employee.getId();
					changeEmployeesDetails(employeeID, txtaddEmpFirstName.getText(), txtAddEmpLastName.getText(),
							payRate);
					bMenu.addWorkingTimes(employeeID, btnSunMorning.isSelected(), btnSunAfternoon.isSelected(),
							btnSunEvening.isSelected(), btnMonMorning.isSelected(), btnMonAfternoon.isSelected(),
							btnMonEvening.isSelected(), btnTueMorning.isSelected(), btnTueAfternoon.isSelected(),
							btnTueEvening.isSelected(), btnWedMorning.isSelected(), btnWedAfternoon.isSelected(),
							btnWedEvening.isSelected(), btnThurMorning.isSelected(), btnThurAfternoon.isSelected(),
							btnThurEvening.isSelected(), btnFriMorning.isSelected(), btnFriAfternoon.isSelected(),
							btnFriEvening.isSelected(), btnSatMorning.isSelected(), btnSatAfternoon.isSelected(),
							btnSatEvening.isSelected());
					program.messageBox("SUCCESS", "Employee Details Updated", "Employee Details Updated",
							txtaddEmpFirstName.getText() + " " + txtAddEmpLastName.getText() + " with $" + payRate
									+ "/h was updated!");
					lblEmployeeTitle.setText("Add New Employee");
					globalEmployeeOption = 0;
				}
			} else {
				if (globalEmployeeOption == 0) {
					bMenu.addEmployee(txtaddEmpFirstName.getText(), txtAddEmpLastName.getText(), payRate,businessID);
					program.messageBox("SUCCESS", "SUCCESS", "New Employee Added", txtaddEmpFirstName.getText() + " "
							+ txtAddEmpLastName.getText() + " with $" + payRate + "/h was Added!");
				} else {
					Employee employee = listviewEmployees.getSelectionModel().getSelectedItem();
					int employeeID = employee.getId();
					setAllTogglesToFalse();// wiping the work times
					changeEmployeesDetails(employeeID, txtaddEmpFirstName.getText(), txtAddEmpLastName.getText(),
							payRate);
					bMenu.addWorkingTimes(employeeID, btnSunMorning.isSelected(), btnSunAfternoon.isSelected(),
							btnSunEvening.isSelected(), btnMonMorning.isSelected(), btnMonAfternoon.isSelected(),
							btnMonEvening.isSelected(), btnTueMorning.isSelected(), btnTueAfternoon.isSelected(),
							btnTueEvening.isSelected(), btnWedMorning.isSelected(), btnWedAfternoon.isSelected(),
							btnWedEvening.isSelected(), btnThurMorning.isSelected(), btnThurAfternoon.isSelected(),
							btnThurEvening.isSelected(), btnFriMorning.isSelected(), btnFriAfternoon.isSelected(),
							btnFriEvening.isSelected(), btnSatMorning.isSelected(), btnSatAfternoon.isSelected(),
							btnSatEvening.isSelected());
					program.messageBox("SUCCESS", "Employee Details Updated", "Employee Details Updated",
							txtaddEmpFirstName.getText() + " " + txtAddEmpLastName.getText() + " with $" + payRate
									+ "/h was updated!");
					lblEmployeeTitle.setText("Add New Employee");
					globalEmployeeOption = 0;
				}
			}
		}
		refreshEmployeeView();
		cancelAddNewEmp();
	}

	/**
	 * updates the employees names and payrates in database
	 * 
	 * @param empID
	 * @param fName
	 * @param lName
	 * @param pRate
	 */
	public void changeEmployeesDetails(int empID, String fName, String lName, double pRate) {
		String name = fName + " " + lName;
		connection.updateEmployeeName(empID, name);
		connection.updateEmployeePayRate(empID, pRate);
	}

	/**
	 * Sets all the workTime toggles to false
	 */
	public void setAllTogglesToFalse() {
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
	}

	/**
	 * Resets the filtered employee list view
	 * 
	 * @author Luke Mason
	 */
	@FXML

	public void refreshEmployeeView() {
		txtSearchEmployee.setText("");
		loadListViewEmp("");
		lblEmployeeID.setText("");
		lblEmployeeName.setText("");
		lblEmployeePayrate.setText("");
		lblEmployeeTitle.setText("");
	}

	/**
	 * Filters the list of employees
	 * 
	 * @author Luke Mason
	 */
	@FXML

	public void searchEmployee() {
		String name = txtSearchEmployee.getText();
		loadListViewEmp(name);
	}

	/**
	 * Views Employees Details
	 * 
	 * @author Luke Mason
	 */
	@FXML

	public void viewEmpDetails() {
		BusinessMenu bMenu = new BusinessMenu();
		// Initializing variables
		int employeeID = -1;
		String ePayRate = "";
		lblEmployeeTitle.setText("Change Employee Details");
		// Checking if an item has been selected
		if (listviewEmployees.getSelectionModel().getSelectedIndex() < 0) {
			program.messageBox("WARN", "oops", "An Employee has not been Selected",
					"Please Select an employee and try again");
			return;
		}
		Employee employee = listviewEmployees.getSelectionModel().getSelectedItem();
		employeeID = employee.getId();
		// Getting first and last name from full name string
		String empFullName = employee.getName();
		int index = empFullName.indexOf(' ');
		if (index < 0) {
			program.messageBox("ERROR", "No 'space character' in employee Name", "Please manually change that employee","Please UPDATE the employee name direct from database");
			return;
		}
		String eFirstName = empFullName.substring(0, index);
		String eLastName = empFullName.substring(index + 1);
		// converting double to string
		double EPayRate = employee.getPayRate();
		ePayRate = "" + EPayRate;
		// Assigning textFields with known Strings
		txtaddEmpFirstName.setText(eFirstName);
		txtAddEmpLastName.setText(eLastName);
		txtAddEmpPayRate.setText(ePayRate);
		ArrayList<EmployeeWorkingTime> workTimes = new ArrayList<EmployeeWorkingTime>();
		// work time contains, WorkTimeID|EmployeeID|Date|StartTime|EndTime
		workTimes = connection.getEmployeeWorkingTimes(employeeID);
		int checkBox = 0;
		log.debug("Work Time Size = " + workTimes.size());
		for(int i = 0; i < workTimes.size(); i++)
		{
			String startTime = program.timeToStr(workTimes.get(i).getStartTime());
			String endTime = program.timeToStr(workTimes.get(i).getEndTime());
			log.info("Start Time ="+startTime+"\n");
			log.info("End Time ="+endTime+"\n");
			int timeBlock;
			if(workTimes.get(i).getDayOfWeek() != 1 && workTimes.get(i).getDayOfWeek()!= 7)
			{
				timeBlock = bMenu.getTimeBlock(startTime,endTime,0);
			}
			else
			{
				timeBlock = bMenu.getTimeBlock(startTime,endTime,1);
			}
			if (timeBlock == -1) {
				log.warn("INVALID TIME BLOCK DETECTED\n");
			}
			else
			{
				changeButtonsOfDay(workTimes.get(i).getDayOfWeek(), timeBlock);
				++checkBox;
			}
		}
		if (checkBox == 0) {
			chkbxAddWorkingTimes.setSelected(false);
			gridpWorkingTimes.setDisable(true);
		} else {
			chkbxAddWorkingTimes.setSelected(true);
			gridpWorkingTimes.setDisable(false);
		}
		globalEmployeeOption = 1;
		showAddNewEmp();
	}

	/**
	 * @author Luke Mason
	 * @editor David
	 * Display Toggle buttons status, from the time block given 
	 * @param timeBlock
	 * @param btnMorn
	 * @param btnAft
	 * @param btnEven
	 */
	public void diplayTogglesFromTimeBlock(int timeBlock, ToggleButton btnMorn, ToggleButton btnAft, ToggleButton btnEven){
		if (timeBlock == 1) {
			btnMorn.setSelected(true);
			btnAft.setSelected(true);
			btnEven.setSelected(true);
			
		} else if (timeBlock == 2) {
			btnMorn.setSelected(true);
			btnAft.setSelected(true);
			btnEven.setSelected(false);
			
		} else if (timeBlock == 3) {
			btnMorn.setSelected(false);
			btnAft.setSelected(true);
			btnEven.setSelected(true);
			
		} else if (timeBlock == 4) {
			btnMorn.setSelected(true);
			btnAft.setSelected(false);
			btnEven.setSelected(false);
			
		} else if (timeBlock == 5) {
			btnMorn.setSelected(false);
			btnAft.setSelected(true);
			btnEven.setSelected(false);
			
		} else if (timeBlock == 6) {
			btnMorn.setSelected(false);
			btnAft.setSelected(false);
			btnEven.setSelected(true);
			
		}
	}
	
	
	/**
	 * sets a certain day's toggles according to a combination of morning,
	 * afternoon and evening
	 * @author Luke Mason
	 * @param dayOfWeek
	 * @param timeBlock
	 */
	public void changeButtonsOfDay(int dayOfWeek, int timeBlock) {
		switch (dayOfWeek) {
		case 1:
			diplayTogglesFromTimeBlock(timeBlock, btnSunMorning, btnSunAfternoon, btnSunEvening);
			break;
		case 2:
			diplayTogglesFromTimeBlock(timeBlock, btnMonMorning, btnMonAfternoon, btnMonEvening);
			break;
		case 3:
			diplayTogglesFromTimeBlock(timeBlock, btnTueMorning, btnTueAfternoon, btnTueEvening);
			break;
		case 4:
			diplayTogglesFromTimeBlock(timeBlock, btnWedMorning, btnWedAfternoon, btnWedEvening);
			break;
		case 5:
			diplayTogglesFromTimeBlock(timeBlock, btnThurMorning, btnThurAfternoon, btnThurEvening);
			break;
		case 6:
			diplayTogglesFromTimeBlock(timeBlock, btnFriMorning, btnFriAfternoon, btnFriEvening);
			break;
		case 7:
			diplayTogglesFromTimeBlock(timeBlock, btnSatMorning, btnSatAfternoon, btnSatEvening);
			break;
		default:
			program.messageBox("WARN", "Error: Something happened with dayOfWeek",
					"dayOfWeek did not register to a day", "Please consult Luke Mason for the crap coding");
		}
	}

	/**
	 * Deletes Employee
	 * 
	 * @author Luke Mason
	 */
	@FXML
	public void deleteEmployee() {
		log.debug("LOGGER: Entered deleteEmployee function");
		if (listviewEmployees.getSelectionModel().getSelectedIndex() < 0) {
			program.messageBox("WARN", "oops", "An Employee has not been Selected",
					"Please Select an employee and try again");
			return;
		}
		Alert alert = new Alert(AlertType.CONFIRMATION);
		alert.setTitle("Delete Employee");
		alert.setHeaderText("Delete Selected Employee");
		alert.setContentText("Are you sure?");

		Optional<ButtonType> result = alert.showAndWait();
		if (result.get() == ButtonType.OK) {
			Employee employee = listviewEmployees.getSelectionModel().getSelectedItem();
			int employeeID = employee.getId();
			connection.clearWorkTimes(employeeID);
			connection.deleteEmployee(employeeID);
			Alert feedback = new Alert(AlertType.INFORMATION);
			feedback.setTitle("Delete Employee");
			feedback.setHeaderText("Employee has been deleted");
			feedback.showAndWait();
			refreshEmployeeView();

		} else {
			return;
		}
	}

	/**************
	 * SERVICES
	 **************/

	/**
	 * Searches Services List
	 * 
	 * @author Joseph Garner
	 */
	@FXML
	public void searchServices() {
		loadListViewEmp(txtSearchService.getText());
	}

	/**
	 * Creates a new service
	 * 
	 * @author Joseph Garner
	 */
	@FXML
	public void addService() {
		try {
			Stage secondaryStage = new Stage();
			secondaryStage.getIcons().add(new Image("images/ic_collections_bookmark_black_48dp_2x.png"));
			Parent root = FXMLLoader.load(getClass().getResource("serviceLayout.fxml"));
			secondaryStage.setTitle("");
			secondaryStage.setResizable(false);
			secondaryStage.setScene(new Scene(root));
			secondaryStage.initModality(Modality.APPLICATION_MODAL);
			secondaryStage.showAndWait();
			secondaryStage.setTitle("Add Service");
		} catch (IOException ioe) {
			log.warn(ioe.getMessage());
		}
		log.debug("false");
		loadallServices();

	}

	/**
	 * Deletes a Service
	 * 
	 * @author Luke Mason
	 */
	@FXML
	public void deleteService() {
		log.debug("LOGGER: Entered deleteService function");
		if (listviewManServices.getSelectionModel().getSelectedIndex() < 0) {
			program.messageBox("WARN", "oops", "An Service has not been Selected",
					"Please Select a Service and try again");
			return;
		}
		Alert alert = new Alert(AlertType.CONFIRMATION);
		alert.setTitle("Delete Service");
		alert.setHeaderText("Delete Selected Service");
		alert.setContentText("Are you sure?");

		Optional<ButtonType> result = alert.showAndWait();
		if (result.get() == ButtonType.OK) {
			Service service = listviewManServices.getSelectionModel().getSelectedItem();
			int serviceID = service.getID();
			System.out.println("Service ID = " + serviceID);
			connection.deleteService(serviceID);
			Alert feedback = new Alert(AlertType.INFORMATION);
			feedback.setTitle("Delete Service");
			feedback.setHeaderText("Service has been deleted");
			feedback.showAndWait();
			lblServiceID.setText("");
			lblManServiceName.setText("");
			lblManServicePrice.setText("");
			lblServiceDura.setText("");
			refreshService();
		} else {
			return;
		}
	}

	/**
	 * Refresh all Services
	 * 
	 * @author Luke Mason
	 */
	@FXML
	public void refreshService() {
		loadallServices();
	}

	/**************
	 * BMENU CUSTOMERS
	 **************/

	/**
	 * Searches Customers
	 * 
	 * @author Joseph Garner
	 */
	@FXML
	public void searchCustomers() {
		listviewCustomers.getItems().clear();
		ArrayList<Customer> customerArray = new ArrayList<>(connection.getAllCustomer(program.business().getBusinessId()));
		ArrayList<Customer> customerArrayView = new ArrayList<>();
		String fname = null;
		String lname = null;
		String fullName = null;
		for (Customer c : customerArray) {
			fname = connection.getCustomer(c.getID()).getFName();
			lname = connection.getCustomer(c.getID()).getLName();
			fullName = fname + " " + lname;
			boolean checkF = program.searchMatch(fname.toUpperCase(), txtSearchCustomer.getText().toUpperCase());
			boolean checkL = program.searchMatch(lname.toUpperCase(), txtSearchCustomer.getText().toUpperCase());
			boolean checkFL = program.searchMatch(fullName.toUpperCase(), txtSearchCustomer.getText().toUpperCase());
			log.debug("LOGGER: Users Name - " + fullName);
			if (checkF || checkL || checkFL) {
				customerArrayView.add(c);
			}
		}
		ObservableList<Customer> customerList = FXCollections.observableList(customerArrayView);
		log.debug("LOGGER: List length:" + customerArrayView.size());
		if (customerList != null) {
			listviewCustomers.setItems(customerList);
			listviewCustomers.setCellFactory(new Callback<ListView<Customer>, ListCell<Customer>>() {
				@Override
				public ListCell<Customer> call(ListView<Customer> p) {

					ListCell<Customer> cell = new ListCell<Customer>() {
						@Override
						protected void updateItem(Customer t, boolean bln) {
							super.updateItem(t, bln);
							if (t != null) {
								log.debug("LOGGER: added:" + t.getFName());
								setText(t.getFName() + " " + t.getLName());
							} else {
								listviewCustomers.setPlaceholder(new Label("No Customer"));
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
	 * Creates a booking for selected customer
	 * 
	 * @author Joseph Garner
	 */
	@FXML
	public void createBooking() {
		if (listviewCustomers.getSelectionModel().getSelectedItem() == null) {
			program.messageBox("ERROR", "Error", "A Customer Has Not Been Chosen", "Please select a customer");
			return;
		}
		_user = program.getUser();
		program.setUser(connection.getUser(listviewCustomers.getSelectionModel().getSelectedItem().getID()));
		System.out.println("OUT OF HERE 3");
		log.debug("LOGGER: Selected user ID - " + listviewCustomers.getSelectionModel().getSelectedItem().getID());
		log.debug("LOGGER: set user - " + program.getUser());
		program.bmb = true;		
			IUser customer = userFactory.getUser("Customer");
			customer.getUserWindow();
			program.setUser(_user);
			program.bmb = false;
		/*
		if (listviewCustomers.getSelectionModel().getSelectedItem() == null) {
			program.messageBox("ERROR", "Error", "A Customer Has Not Been Chosen", "Please select a customer");
			return;
		}
		btnRToOwnMen.setDisable(false);
		stkBusiness.setVisible(false);
		stkCustomer.setVisible(true);
		_user = program.getUser();
		program.setUser(connection.getUser(listviewCustomers.getSelectionModel().getSelectedItem().getID()));
		log.debug("LOGGER: user ID - " + listviewCustomers.getSelectionModel().getSelectedItem().getID());
		log.debug("LOGGER: user - " + program.getUser());
		newBook.setCus(program.getUser().getID());
		lblCustomerName.setText(listviewCustomers.getSelectionModel().getSelectedItem().getFullName());
		*/
	}
	
	
	private void listLVBookini(){
		listviewBookings.setPlaceholder(new Label("No Bookings"));
		listviewCustomers.setPlaceholder(new Label("No Customer"));
		listviewEmployees.setPlaceholder(new Label("No Employees"));
		listviewManServices.setPlaceholder(new Label("No Services"));
		listviewBookings.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<Booking>() {
			@Override
			public void changed(ObservableValue<? extends Booking> observable, Booking oldValue, Booking newValue) {
				if (newValue != null) {
					booking = newValue;
					lblBookingID.setText(Integer.toString(booking.getBookingID()));
					lblBookingCustomerID.setText(connection.getCustomer(booking.getCustomerId()).getFullName());
					lblBookingDate.setText(program.dateToStr(booking.getDate()));
					lblBookServ.setText(connection.getService(booking.getService()).getName());
					lblBookingStartTime.setText(program.timeToStr(booking.getStartTime()));
					lblBookingEndTime.setText(program.timeToStr(booking.getEndTime()));
					lblBookingStatus.setText(booking.getStatus());
				}
			}
		});
	}
	
	@Override
	public boolean getUserWindow(){
		try {
			Stage secondaryStage = new Stage();
			secondaryStage.getIcons().add(new Image("images/ic_collections_bookmark_black_48dp_2x.png"));
			Parent root = FXMLLoader.load(getClass().getResource("businessLayout.fxml"));
			secondaryStage.setTitle("Business Application");
			secondaryStage.setMinWidth(800);
			secondaryStage.setMinHeight(650);
			secondaryStage.setMaxWidth(1000);
			secondaryStage.setMaxHeight(850);
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



	
	@FXML
	public void showCustom(){
		try {
			Stage secondaryStage = new Stage();
			secondaryStage.getIcons().add(new Image("images/ic_collections_bookmark_black_48dp_2x.png"));
			Parent roots = FXMLLoader.load(getClass().getResource("mizeLayout.fxml"));
			secondaryStage.setTitle("Business Application");
			secondaryStage.setResizable(false);
			secondaryStage.setScene(new Scene(roots));
			secondaryStage.initModality(Modality.APPLICATION_MODAL);
			secondaryStage.showAndWait();
		} catch (IOException ioe) {
			log.warn(ioe.getMessage());
		}
		setCI();
	}

	@Override
	public void setCI() {
		root.setStyle(program.setColor());
		log.debug(program.setColor());
	}
	
	
}
