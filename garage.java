import java.util.*;
import java.io.*;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.concurrent.Semaphore;

class garage{

	int ticketTotal;
	int availableOpenSpaces;
	int availableReservedSpaces;
	Section[][] array;
	int numberFloors = 4;
	int sectionsPerFloor = 4;
	int reservedSections = 1;
	int spacesPerSection = 50;
	CustomerGui gui;
	garageUtilities utilTool = new garageUtilities();
	static Semaphore mutex = new Semaphore(1);
	
	public garage(){
		readGarageFile();
		ticketTotal = 0;
		array = new Section[numberFloors][sectionsPerFloor];
		String sectionLetters[] = {"A", "B", "C", "D", "E", "F", "G", "H", "I", "J", "K", "L", "M", "N", "O", "P", "Q", "R", "S", "T", "U", "V", "W", "X", "Y", "Z"};
		int letterCounter = 0;		
		int counter = 0;
		int resCounter = reservedSections;		
		for (int i = 0; i < numberFloors; i++){
			for (int j = 0; j < sectionsPerFloor; j++){
				String name = "";
				String code = "";
				name += (counter + 1);
				name += sectionLetters[letterCounter];
				code += ((i+1)*99);
				code += ((j+1)*9);
				if (resCounter > 0){
					Section newSec = new ResSec(name, spacesPerSection, code);
					array[i][j] = newSec;
					resCounter --;
				}else{
					Section newSec = new OpenSec(name, spacesPerSection, code);
					array[i][j] = newSec;
				}
				letterCounter ++;
			}
			counter ++;
			letterCounter = 0;
		}
		availableReservedSpaces = ((reservedSections*spacesPerSection) - utilTool.getDailyReservations());
		availableOpenSpaces = (numberFloors*sectionsPerFloor*spacesPerSection)-(availableReservedSpaces);
		
		//Initialization Message

		System.out.println("Gragae Initialized With:");
		System.out.println("------------------------");
		System.out.println(numberFloors + " Floors");
		System.out.println(sectionsPerFloor + " Sections on each floor");
		System.out.println(reservedSections + " Of the total sections marked as reserved");
		System.out.println(spacesPerSection + " Spaces in each section");
		System.out.println("Total number of open spaces: " + availableOpenSpaces);
		System.out.println("Unused Reserved spaces for today: " + availableReservedSpaces);
		System.out.println();
		System.out.println("Current garage occupancy: " + getTicketTotal() + "/" + (availableOpenSpaces + availableReservedSpaces));
		System.out.println("There are " + utilTool.getDailyReservations() + " reservations for today");
		System.out.println();
		hourlyUpdate();
	}

	// "Trash" lines are deleting blank space between each string in the file
	public void readGarageFile(){

		try{
			String trash;
			FileReader myFile = new FileReader("garageInfo.txt");
			Scanner scanMyFile = new Scanner(myFile);
			trash = scanMyFile.next();
			
			numberFloors = scanMyFile.nextInt();
			if (numberFloors < 1){
				numberFloors = 1;
				System.out.println("Cannot create garage with less than 1 floor");
				System.out.println("Number of floors increased to 1");
				System.out.println();
			}
			trash = scanMyFile.next();
			
			sectionsPerFloor = scanMyFile.nextInt();
			if (sectionsPerFloor > 26){
				sectionsPerFloor = 26;
				System.out.println("Maximum number of sections per floor (26) exceeded");
				System.out.println("Section count reduced to 26");
				System.out.println();
			}
			if (sectionsPerFloor < 1){
				sectionsPerFloor = 1;
				System.out.println("Cannot create garage with less than 1 section on each floor");
				System.out.println("Number of sections per floor increased to 1");
				System.out.println();
			}
			trash = scanMyFile.next();
			
			reservedSections = scanMyFile.nextInt();
			if (reservedSections < 0){
				reservedSections = 0;
				System.out.println("Cannot create garage with less than 0 reserved sections");
				System.out.println("Number of reserved sections increased to 0");
				System.out.println();
			}
			if (reservedSections > sectionsPerFloor * numberFloors){
				reservedSections = (sectionsPerFloor * numberFloors);
				System.out.println("Cannot have more reserved sections than total number of sections");
				System.out.println("Reserved section count set to total section count");
				System.out.println();
			}
			trash = scanMyFile.next();

			spacesPerSection = scanMyFile.nextInt();
			if (spacesPerSection < 1){
				spacesPerSection = 1;
				System.out.println("Cannot create garage with less than 1 spae in each section");
				System.out.println("Number of spaces per section increased to 1");
				System.out.println();
			}
			
			
		}catch (Exception ex){
			System.out.println("Error, garage initialization file not found or in incorrect format.");
			System.out.println("Garage initialized with default values instead.");
			System.out.println();
		}
	}

	public boolean assignSection(boolean reserved){
		int i = 0;
		boolean secFound = false;
		while ((i < numberFloors) && (secFound == false)){
			for (int j=0; j<sectionsPerFloor; j++){
				if ((array[i][j].hasRoom() == true) && (array[i][j].getReserved() == reserved)){
					if (reserved == true){
						System.out.println("Section " + array[i][j].getName() + " chosen, it is a reserved section");
					}else{
						System.out.println("Section " + array[i][j].getName() + " chosen, it is not a reserved section");
					}
					System.out.println();
					array[i][j].printTicket();
					System.out.println("New occupancy of section " + array[i][j].getName() + ": " + array[i][j].getTickets());
					System.out.println();
					secFound = true;
					break;
				}
			}
			i ++;
		}
		if (secFound == false){
			System.out.println();
			System.out.println("Sorry, the garage is at maximum capacity and no spaces are currently available.");
			System.out.println();
			return false;
		}else{
			return true;
		}
	}

	public void assignOpen(){
		try{
			mutex.acquire();
			try{
				if (assignSection(false) == true){
					ticketTotal ++;
					availableOpenSpaces --;
					gui.OpenUpdate(availableOpenSpaces);
					if (availableOpenSpaces < 1){
						gui.OpenFull();
					}
				}
			}finally{
				mutex.release();
			}
		}catch(InterruptedException ie){
			System.out.println(ie);
			System.out.println();
		}
	}

	public void assignReserved(){
		try{
			mutex.acquire();
			try{
				if (assignSection(true) == true){
					ticketTotal ++;
					availableReservedSpaces --;
					gui.ResUpdate(availableReservedSpaces);
					if (availableReservedSpaces < 1){
						gui.ResFull();
					}
				}
			}finally{
				mutex.release();
			}
		}catch(InterruptedException ie){
			System.out.println(ie);
			System.out.println();
		}
	}
	
	public void printGarageInfo(){
		System.out.println("Number of floors: " + numberFloors);
		System.out.println("Number of sections per floor: " + sectionsPerFloor);
		System.out.println("Number of reserved sections: " + reservedSections);
		System.out.println("Number of spaces per section: " + spacesPerSection);
		System.out.println("Current occupancy: " + ticketTotal + "/" + (numberFloors*sectionsPerFloor*spacesPerSection));
		System.out.println();
	}

	public void printArray(){
		for (int i=0; i < numberFloors; i++){
			for (int j=0; j < sectionsPerFloor; j++){
				array[i][j].printInfo();
				System.out.println();
			}
		}
	}
	
	public int getTicketTotal(){
		return ticketTotal;
	}
	
	public void reservedCheckIn(){
		Scanner stdin = new Scanner(System.in);
		System.out.print("Reservation Number: ");
		String resNumber = stdin.next();
		System.out.println();
		if (utilTool.searchReservations(resNumber) == true){
			if (assignSection(true) == true){
				System.out.println("Reservation Validated, Welcome");
				System.out.println();
				ticketTotal ++;
				availableReservedSpaces --;
				utilTool.markReservationComplete(resNumber);
			}
		}else{
			System.out.println("Sorry, no reservation for that number was found");
			System.out.println("If you believe this is an error, please request assistance from a garage employee");
			System.out.println();
		}
	}

	public void hourlyUpdate() {
		Timer timer = new Timer();
		TimerTask hourlyTask = new TimerTask() {
			@Override
			public void run() {

				Calendar cal = Calendar.getInstance();
				DateFormat dateFormat = new SimpleDateFormat("MM/dd HH:mm:ss");
				Date date = new Date();
				String update = "Time: " + date.toString() + "   " + "Occupancy: " + ticketTotal;
				writeHourlyUpdate(update);
			}
		};

		// schedule the task to run starting now and then every hour...
        	timer.schedule(hourlyTask, 0l, 1000 * 5);
	}

	public void writeHourlyUpdate(String update) {

		try {
			String tempDoc = "";
			String myLine;
			FileReader myFile = new FileReader("HourlyUpdate.txt");
			Scanner scanMyFile = new Scanner(myFile);

			while (scanMyFile.hasNext()) {
				myLine = scanMyFile.nextLine();
				tempDoc = tempDoc + myLine + "\n";
			}

			myFile.close();
			tempDoc = tempDoc + update + "\n";
			File outFile = new File("HourlyUpdate.txt");
			PrintWriter output = new PrintWriter(outFile);
			output.println(tempDoc);
			output.close();
		} catch (Exception ex) {
			System.out.println("Hourly update file missing or in incorrect format");
			System.out.println();
		}
	}

	//*************************
	//Sensor Simulation Methods
	//*************************

	void sensorMenu(){
		int input = 0;
		while (input != 4){
			
			System.out.println("-----------------------------");
			System.out.println("         Sensor Menu");
			System.out.println("-----------------------------");
			System.out.println("1. Enter A Section");
			System.out.println("2. Exit A Section");
			System.out.println("3. Exit The Garage");
			System.out.println("4. Return To Main Menu");
			System.out.println();			
			
			System.out.print("Selection: ");
			Scanner stdin = new Scanner(System.in);
			try{
				input = stdin.nextInt();
			}catch(Exception ex){
				System.out.println("Invalid Input");
			}
			System.out.println();
			switch (input){
				case 1:
					enterSection();
					break;
				case 2:
					exitSection();
					break;
				case 3:
					exitGarage();
					break;
				case 4:
					System.out.println("Returning To Main Menu");
					System.out.println();
					break;
				default:
					System.out.println("Invalid Input");
					System.out.println();
			}
		}
	}

	void enterSection(){
		Scanner stdin = new Scanner(System.in);
		System.out.println("What is the floor number of the section you are trying to enter?");
		System.out.println();
		System.out.print("Input: ");

		try{
			int floor = stdin.nextInt();
			floor --;
			System.out.println();
			System.out.println("What is the section letter for this section? Please enter it as a number (A=0, B=1, etc...)");
			System.out.println();
			System.out.print("Input: ");
			int section = stdin.nextInt();
			System.out.println();

			if ((floor <= numberFloors) && (section <= sectionsPerFloor)){
				System.out.println("You have chosen section " + array[floor][section].getName());
				System.out.println();
				if(array[floor][section].getOccupancy() < array[floor][section].getSpaces()){
					System.out.print("Enter Entrance Code: ");
					String code = stdin.next();
					System.out.println();
					if (code.equals(array[floor][section].getCode())){
						System.out.println("Entrance Code Verified");
						System.out.println("Barrier to section " + array[floor][section].getName() + " opened");
						System.out.println("Occupancy of section " + array[floor][section].getName() + " incremented");
						System.out.println();
						array[floor][section].incrementOccupancy();
					}else{
						System.out.println("Incorrect Entrance Code");
						System.out.println("Entrance Denied");
						System.out.println();
					}
				}else{
					System.out.println("This section is at maximum occupancy, unable to enter");
					System.out.println("If you were assigned a ticket for this section, seek assistance from a garage employee");
					System.out.println();
				}

			}else{
				System.out.println("Error, there is no such section in this garage.");
				System.out.println();
			}

		}catch(Exception ex){
			System.out.println();
			System.out.println("Invalid input. Section entering aborted.");
			System.out.println();
		}
	}

	void exitSection(){
		Scanner stdin = new Scanner(System.in);
		System.out.println("What is the floor number of the section you are trying to exit?");
		System.out.println();
		System.out.print("Input: ");

		try{
			int floor = stdin.nextInt();
			floor --;
			System.out.println();
			System.out.println("What is the section letter for this section? Please enter it as a number (A=0, B=1, etc...)");
			System.out.println();
			System.out.print("Input: ");
			int section = stdin.nextInt();
			System.out.println();

			if ((floor <= numberFloors) && (section <= sectionsPerFloor)){
				System.out.println("You have chosen section " + array[floor][section].getName());
				System.out.println();
				if(array[floor][section].getOccupancy() > 0){
					System.out.println("Vehicle exiting section " + array[floor][section].getName());
					System.out.println("Occupancy of section " + array[floor][section].getName() + " decremented");
					System.out.println();
					array[floor][section].decrementOccupancy();
				}else{
					System.out.println("Negative occupancy detected, setting section occupancy to 0");
					System.out.println();
					array[floor][section].setOccupancy(0);
				}
			}else{
				System.out.println("Error, there is no such section in this garage.");
				System.out.println();
			}

		}catch(Exception ex){
			System.out.println();
			System.out.println("Invalid input. Section exiting aborted.");
			System.out.println();
		}
	}

	void exitGarage(){
		Scanner stdin = new Scanner(System.in);
		System.out.println("What is the floor number of the section you were assigned to?");
		System.out.println();
		System.out.print("Input: ");

		try{
			int floor = stdin.nextInt();
			floor --;
			System.out.println();
			System.out.println("What is the section letter for this section? Please enter it as a number (A=0, B=1, etc...)");
			System.out.println();
			System.out.print("Input: ");
			int section = stdin.nextInt();
			System.out.println();

			if ((floor <= numberFloors) && (section <= sectionsPerFloor)){
				System.out.println("You are turning in a ticket for section " + array[floor][section].getName());
				System.out.println("Please hand your ticket over to the garage attendant");
				System.out.println();
				System.out.println("Employee confirmation needed to confirm transaction");
				System.out.println();
				boolean confirm = false;
				String selection = "t";
				while (confirm != true && (selection.equals("t") || selection.equals("T"))){
					System.out.print("Enter Employee Password To Confirm: ");
					String password = stdin.next();
					System.out.println();
					if (utilTool.checkPassword(password) == true){
						confirm = true;
					}else{
						System.out.println("Password Rejected");
						System.out.println("Press T to try again or press any other key to abort garage exit");
						System.out.println();
						System.out.print("Selection: ");
						selection = stdin.next();
						System.out.println();
					}
				}
				if(confirm == true){
					System.out.println("Password Verified");
					System.out.println("Ticket count for section " + array[floor][section].getName() + " decremented");
					System.out.println();
					array[floor][section].decrementTickets();
					System.out.println("Exit Barrier Opened");
					System.out.println("Thank you for using our parking garage and have a nice day!");
					System.out.println();
				}else{
					System.out.println("Garage exiting aborted");
					System.out.println();
				}
			}else{
				System.out.println("Error, there is no such section in this garage.");
				System.out.println();
			}

		}catch(Exception ex){
			System.out.println();
			System.out.println("Invalid input. Garage exiting aborted.");
			System.out.println();
		}
	}	

	//******************
	//EMPLOYEE INTERFACE
	//******************

	void employeeInterface(){
		Scanner in = new Scanner(System.in);
		int choice = 0;

		System.out.print("Enter your employee password: ");
		String employeePassword = in.next();
		System.out.println();
		if (utilTool.checkPassword(employeePassword)) {
			System.out.println("Password Verified");
			System.out.println();
			while(choice != 13){

				System.out.println("--------------------------------------------");
				System.out.println("           Employee Access Menu:");
				System.out.println("--------------------------------------------");
				System.out.println("1. Check A Reservation");
				System.out.println("2. Manually Print A Ticket");
				System.out.println("3. Manually Open A Section Barrier");
				System.out.println("4. View Current Garage Status");
				System.out.println("5. View Status Of An Individual Section");
				System.out.println("6. Change Occupancy Of A Section");
				System.out.println("7. Change Ticket Count Of A Section");
				System.out.println("8. Add A New Customer Account To Master Doc");
				System.out.println("9. Delete A Customer Account From Master Doc");
				System.out.println("10. Add New Employee Password");
				System.out.println("11. Delete An Employee Password");
				System.out.println("12. Run End-Of-Day Master File Update");
				System.out.println("13. Return To Main Menu");
				System.out.println();
				System.out.print("Selection: ");
				
				try{
					// input
					choice = in.nextInt();
					System.out.println();

					switch (choice) {
						case 1:
							utilTool.manualReservationCheck();
							break;
						case 2:
							manualAssign();
							break;
						case 3:
							barrierOverride();
							break;
						case 4:
							printArray();
							break;
						case 5:
							viewSection();
							break;
						case 6:
							changeOccupancy();
							break;
						case 7:
							changeTickets();
							break;
						case 8:
							utilTool.AddAccountMaster();
							break;
						case 9:
							utilTool.DeleteAccountMaster();
							break;
						case 10:
							utilTool.AddPassword();
							break;
						case 11:
							utilTool.DeletePassword();
							break;
						case 12:
							utilTool.updateMaster();
							break;
						case 13:
							System.out.println("Returning To Main Menu");
							System.out.println();
							break;
						default:
							System.out.println("Invalid Input");
							System.out.println();
							break;
					}
				}catch(Exception ex){
					System.out.println("Invalid Input");
					System.out.println();
				}
			}
		} else {
			System.out.println("Invalid password");
			System.out.println("Press T to try again or press any other key to return to main menu");
			System.out.println();
			System.out.print("Selection: ");
			String selection = in.next();
			System.out.println();
			if(selection.equals("t") || selection.equals("T")){
				employeeInterface();
			}else{
				System.out.println("Returning To Main Menu");
				System.out.println();
			}
		}
	}

	// ***********************
	// GARAGE OVERRIDE METHODS
	// ***********************

	public void viewSection(){
		Scanner stdin = new Scanner(System.in);
		System.out.println("What is the floor number of the section you are trying to view?");
		System.out.println();
		System.out.print("Input: ");

		try{
			int floor = stdin.nextInt();
			floor --;
			System.out.println();
			System.out.println("What is the section letter for this section? Please enter it as a number (A=0, B=1, etc...)");
			System.out.println();
			System.out.print("Input: ");
			int section = stdin.nextInt();
			System.out.println();

			if ((floor <= numberFloors) && (section <= sectionsPerFloor)){
				System.out.println("You have chosen section " + array[floor][section].getName());
				System.out.println();
				System.out.println("Section " + array[floor][section].getName() + ":");
				System.out.println("Ticket Count: " + array[floor][section].getTickets() + "/" + array[floor][section].getSpaces());
				System.out.println("Current Occupancy: " + array[floor][section].getOccupancy() + "/" + array[floor][section].getSpaces());
				System.out.println();

			}else{
				System.out.println("Error, there is no such section in this garage.");
				System.out.println();
			}

		}catch(Exception ex){
			System.out.println();
			System.out.println("Invalid input. Section viewing aborted.");
			System.out.println();
		}
	}

	public void changeOccupancy(){

		Scanner stdin = new Scanner(System.in);
		System.out.println("What is the floor number of the section you are trying to change the occupancy of?");
		System.out.println();
		System.out.print("Input: ");

		try{
			int floor = stdin.nextInt();
			floor --;
			System.out.println();
			System.out.println("What is the section letter for this section? Please enter it as a number (A=0, B=1, etc...)");
			System.out.println();
			System.out.print("Input: ");
			int section = stdin.nextInt();
			System.out.println();

			if ((floor <= numberFloors) && (section <= sectionsPerFloor)){
				System.out.println("You have chosen section " + array[floor][section].getName());
				System.out.println();
				System.out.println("What do you want to set the occpancy to?");
				System.out.println();
				System.out.print("Input: ");
				int newOccupancy = stdin.nextInt();
				System.out.println();
				array[floor][section].setOccupancy(newOccupancy);
				System.out.println("Successfully set new occupancy.");
				System.out.println();

			}else{
				System.out.println("Error, there is no such section in this garage.");
				System.out.println();
			}

		}catch(Exception ex){
			System.out.println();
			System.out.println("Invalid input. Occupancy editing aborted.");
			System.out.println();
		}
	}

	public void changeTickets(){

		Scanner stdin = new Scanner(System.in);
		System.out.println("What is the floor number of the section you are trying to change the ticket count of?");
		System.out.println();
		System.out.print("Input: ");

		try{
			int floor = stdin.nextInt();
			System.out.println();
			floor --;
			System.out.println("What is the section letter for this section? Please enter it as a number (A=0, B=1, etc...)");
			System.out.println();
			System.out.print("Input: ");
			int section = stdin.nextInt();
			System.out.println();

			if ((floor <= numberFloors) && (section <= sectionsPerFloor)){
				System.out.println("You have chosen section " + array[floor][section].getName());
				System.out.println();
				System.out.println("What do you want to set the ticket count to?");
				System.out.println();
				System.out.print("Input: ");
				int newTickets = stdin.nextInt();
				System.out.println();

				//Updates ticket count for GUI before making change

				int oldTickets = array[floor][section].getTickets();
				int ticketDifference = newTickets - oldTickets;
				if (array[floor][section].getReserved() == true){
					availableReservedSpaces -= ticketDifference;
					gui.ResUpdate(availableReservedSpaces);
					if (availableReservedSpaces < 1){
						gui.ResFull();
					}
				}else{
					availableOpenSpaces -= ticketDifference;
					gui.OpenUpdate(availableOpenSpaces);
					if (availableOpenSpaces < 1){
						gui.OpenFull();
					}
				}

				//Updates actual section ticket count

				array[floor][section].setOccupancy(newTickets);
				System.out.println("Successfully set new ticket count.");
				System.out.println();

			}else{
				System.out.println("Error, there is no such section in this garage.");
				System.out.println();
			}

		}catch(Exception ex){
			System.out.println();
			System.out.println("Invalid input. Ticket count editing aborted.");
			System.out.println();
		}
	}		
	
	public void manualAssign(){

		Scanner stdin = new Scanner(System.in);
		System.out.println("What is the floor number of the section you need a ticket for?");
		System.out.println();
		System.out.print("Input: ");

		try{
			int floor = stdin.nextInt();
			System.out.println();
			floor --;
			System.out.println("What is the section letter for this section? Please enter it as a number (A=0, B=1, etc...)");
			System.out.println();
			System.out.print("Input: ");
			int section = stdin.nextInt();
			System.out.println();

			if ((floor <= numberFloors) && (section <= sectionsPerFloor)){
				System.out.println("You have chosen section " + array[floor][section].getName());
				System.out.println();

				if (array[floor][section].getTickets() < array[floor][section].getSpaces()){
					array[floor][section].printTicket();
					ticketTotal ++;
					if(array[floor][section].getReserved() == false){
						availableOpenSpaces --;
					}else{
						availableReservedSpaces --;
					}
					System.out.println("Manual section assignment successful.");
					System.out.println();

				}else{
					System.out.println("Error, the section you have chosen is already at capacity, proceed anyway?");
					System.out.println();
					System.out.print("Input (y/n): ");
					String selection = stdin.next();
					System.out.println();

					//Updates GUI before prinitng ticket

					if ((selection.compareTo("y") == 0) || (selection.compareTo("Y") == 0)){
						
						if (array[floor][section].getReserved() == true){
							availableReservedSpaces -= 1;
							gui.ResUpdate(availableReservedSpaces);
							if (availableReservedSpaces < 1){
								gui.ResFull();
							}
						}else{
							availableOpenSpaces -= 1;
							gui.OpenUpdate(availableOpenSpaces);
							if (availableOpenSpaces < 1){
								gui.OpenFull();
							}
						}
						
						//Prints Ticket

						array[floor][section].printTicket();
						ticketTotal ++;
						System.out.println("Manual section assignment successful.");
						System.out.println();

					}else{
						System.out.println("Manual section assignment aborted.");
						System.out.println();
					}
				}
			}else{
				System.out.println("Error, there is no such section in this garage.");
				System.out.println();
			}

		}catch(Exception ex){
			System.out.println();
			System.out.println("Invalid input. Manual section assignment aborted.");
			System.out.println();
		}
	}

	// This method doesn't DO anything, because there is no physical barrier to control, instead it prints a message saying the hypothetical barrier has been opened.

	public void barrierOverride(){
		Scanner stdin = new Scanner(System.in);
		System.out.println("What is the floor number of the section whose barrier you want to open?");
		System.out.println();
		System.out.print("Input: ");

		try{
			int floor = stdin.nextInt();
			floor --;
			System.out.println();
			System.out.println("What is the section letter for this section? Please enter it as a number (A=0, B=1, etc...)");
			System.out.println();
			System.out.print("Input: ");
			int section = stdin.nextInt();
			System.out.println();

			if ((floor <= numberFloors) && (section <= sectionsPerFloor)){
				System.out.println("You have chosen section " + array[floor][section].getName());
				System.out.println();
				System.out.println("Barrier to section " + array[floor][section].getName() + " opened");
				System.out.println();
			}else{
				System.out.println("Error, there is no such section in this garage.");
				System.out.println();
			}

		}catch(Exception ex){
			System.out.println();
			System.out.println("Invalid input. Barrier opening aborted.");
			System.out.println();
		}
	}
	
	// ****************
	// MAIN MENU METHOD
	// ****************

	void mainMenu(){
		int input = 0;
		while (input != 5){
			
			System.out.println("-----------------------------");
			System.out.println("         Main Menu");
			System.out.println("-----------------------------");
			System.out.println("1. Drive Up Customer GUI");
			System.out.println("2. Reserved Customer Check-In");
			System.out.println("3. Sensor Simulation");
			System.out.println("4. Employee Access");
			System.out.println("5. Quit");
			System.out.println();			
			
			System.out.print("Selection: ");
			Scanner stdin = new Scanner(System.in);
			try{
				input = stdin.nextInt();
			}catch(Exception ex){
				System.out.println("Invalid Input");
			}
			System.out.println();
			switch (input){
				case 1:
					gui = new CustomerGui(this, availableOpenSpaces, availableReservedSpaces);
					break;
				case 2:
					reservedCheckIn();
					break;
				case 3:
					sensorMenu();
					break;
				case 4:
					employeeInterface();
					break;
				case 5:
					System.out.println("Now Quitting");
					System.out.println();
					System.exit(0);
					break;
				default:
					System.out.println("Invalid Input");
					System.out.println();
			}
		}
	}
	
	// *****************
	// MAIN METHOD BELOW
	// *****************

	public static void main(String[] args){
		System.out.println();
		garage myGarage = new garage();
		myGarage.mainMenu();
	}
}