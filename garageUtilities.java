import java.io.*;
import java.util.*;

public class garageUtilities{
	
	public garageUtilities(){
		try{
			File outFile = new File("HourlyUpdate.txt");
			PrintWriter output = new PrintWriter(outFile);
			output.close();
		}catch(Exception ex){
			System.out.println("Unable to make hourly update file");
			System.out.println();
		}
	}

	public boolean checkPassword(String password) {
		boolean match = false;		
		try {
			String myLine = "";
			String readPassword;
			FileReader myFile = new FileReader("employeePasswords.txt");
			Scanner scanMyFile = new Scanner(myFile);

			while (scanMyFile.hasNext()) {
				myLine = scanMyFile.nextLine();
				Scanner scanMyLine = new Scanner(myLine);
				readPassword = scanMyLine.next();

				if (readPassword.equals(password)) {
					match = true;
				}
           		}
			myFile.close();
		}catch(Exception ex){
			System.out.println("Password file not found, new file created with default password");
			System.out.println();
			try{
				File outFile = new File("employeePasswords.txt");
				PrintWriter output = new PrintWriter(outFile);
				output.println("admin");
				output.close();
			}catch(Exception ex2){
				System.out.println("Password file not found, and system is unable to replace it");
				System.out.println();
			}
		}
		return match;
	}

	public void AddPassword() {
		Scanner in = new Scanner(System.in);
		System.out.println("Please input the password you want to add");
		System.out.print("New Password: ");
		String password = in.next();
		System.out.println();
		String tempDoc = "";
		String myLine;
		
		if(checkPassword(password) == false){
			try {	
				FileReader myFile = new FileReader("employeePasswords.txt");
				Scanner scanMyFile = new Scanner(myFile);
	
				while (scanMyFile.hasNext()) {
					myLine = scanMyFile.nextLine();
					Scanner scanMyLine = new Scanner(myLine);
					tempDoc = tempDoc + myLine + "\n";
				}
				tempDoc = tempDoc + password;
				myFile.close();
				File outFile = new File("employeePasswords.txt");
				PrintWriter output = new PrintWriter(outFile);
				output.println(tempDoc);
				output.close();
				System.out.println("Password added to list");
				System.out.println();
			}catch(Exception ex){
				System.out.println("Password file not found, creating new one");
				System.out.println();
			}
		}else{
			System.out.println("Error, that password is already in use");
			System.out.println();
		}
	}

	public void DeletePassword() {
		boolean match = false;		
		try{
			//Checks length of password list
			FileReader passFile = new FileReader("employeePasswords.txt");
			Scanner lineChecker = new Scanner(passFile);
			int lineCounter = 0;
			String trash;
			while (lineChecker.hasNext()) {
				trash = lineChecker.nextLine();
				lineCounter ++;
			}

			if(lineCounter > 1){
				Scanner in = new Scanner(System.in);
				System.out.println("Please input the password you want to delete");
				System.out.print("Password to delete: ");
				String password = in.next();
				System.out.println();
				String tempDoc = "";
				String myLine = "";
				String readPassword;
				FileReader myFile = new FileReader("employeePasswords.txt");
				Scanner scanMyFile = new Scanner(myFile);

				while (scanMyFile.hasNext()) {
					myLine = scanMyFile.nextLine();
					Scanner scanMyLine = new Scanner(myLine);
					readPassword = scanMyLine.next();

					if (readPassword.equals(password)) {
						match = true;
						System.out.println("Password Deleted");
						System.out.println();
					} else {
						tempDoc = tempDoc + myLine + "\n";
					}
				}

				myFile.close();
				File outFile = new File("employeePasswords.txt");
				PrintWriter output = new PrintWriter(outFile);
				output.println(tempDoc);
				output.close();
				if(match == false){
					System.out.println("Specified password not found, unable to remove from list");
					System.out.println();
				}
			}else{
				System.out.println("Only one password remains in the file, unable to delete the only password");
				System.out.println();
			}
		} catch (Exception ex) {
			System.out.println("Unable to locate password file");
		}
	}

	public void DeleteAccountMaster() {
		String accountNumber;
		Scanner in = new Scanner(System.in);
		System.out.println("Please enter the account number of the customer whose account you wish to delete");
		System.out.println();
		System.out.print("Input: ");
		accountNumber = in.next();
		System.out.println();
		boolean match = false;
		String name = "";

		try {
			String tempDoc = "";
			String myLine = "";
			String readAccount;
			FileReader myFile = new FileReader("ReservationMasterFile.txt");
			Scanner scanMyFile = new Scanner(myFile);

			while (scanMyFile.hasNext()) {
				myLine = scanMyFile.nextLine();
				Scanner scanMyLine = new Scanner(myLine);
				readAccount = scanMyLine.next();

				if (readAccount.equals(accountNumber)) {
					match = true;
					name = name + scanMyLine.next();
					name = name + " ";
					name = name + scanMyLine.next();
				} else {
					tempDoc = tempDoc + myLine + "\n";
				}
			}

			myFile.close();
			File outFile = new File("ReservationMasterFile.txt");
			PrintWriter output = new PrintWriter(outFile);
			output.println(tempDoc);
			output.close();
		} catch (Exception ex) {
			System.out.println("\n  " + ex);
		}

		if (match) {
			System.out.println("Account belonging to " + name + " successfully deleted");
			System.out.println();
		} else {
			System.out.println("Unable to locate account with specified number, no account was deleted");
			System.out.println();
		}

	}

	//  ADDING ACCOUNT TO THE MASTER FILE 
	public void AddAccountMaster() {
		System.out.println(" Formatting Example: \n " + "1111 Adam levine March 13 2016 1" +"\n");
		Scanner in = new Scanner(System.in);
		String accountNumber;
		String firstName;
		String lastName;
		String month;
		String day;
		String year;
		String totalVisits;
		boolean valid = false;

		System.out.println("What is the customer's account number?");
		System.out.println();
		System.out.print("Input: ");
		accountNumber = in.next();
		System.out.println();

		System.out.println("What is the customer's first name?");
		System.out.println();
		System.out.print("Input: ");
		firstName = in.next();
		System.out.println();
		
		System.out.println("What is the customer's last name?");
		System.out.println();
		System.out.print("Input: ");
		lastName = in.next();
		System.out.println();

		System.out.println("Please enter the current month (Example: January)");
		System.out.println();
		System.out.print("Input: ");
		month = in.next();
		System.out.println();
		
		System.out.println("Please enter the current day of the month (Example: 21)");
		System.out.println();
		System.out.print("Input: ");
		day = in.next();
		System.out.println();

		System.out.println("Please enter the current year (Example: 2016)");
		System.out.println();
		System.out.print("Input: ");
		year = in.next();
		System.out.println();

		System.out.println("How many times has the customer been to this garage? (Example: 5)");
		System.out.println();
		System.out.print("Input: ");
		totalVisits = in.next();
		System.out.println();
     
		try {
			boolean match = false;
			String tempDoc = "";
			String myLine = "";
			String readAccount = "";

			FileReader myFile = new FileReader("ReservationMasterFile.txt");
			Scanner scanMyFile = new Scanner(myFile);

			while (scanMyFile.hasNext()) {
				myLine = scanMyFile.nextLine();
				Scanner scanMyLine = new Scanner(myLine);
				readAccount = scanMyLine.next();

				if (readAccount.equals(accountNumber)) {
					match = true;
				}

				tempDoc = tempDoc + myLine + "\n";
			}

            		if (match == false) {
				tempDoc = tempDoc + accountNumber + " " + firstName + " " + lastName + " " + month + " " + day + " " + year + " " + totalVisits;
				System.out.println("Account successfully created");
				System.out.println();
			} else {
				System.out.println("Unable to add, there is already a customer with that account number in the master file");
				System.out.println();
			}
			myFile.close();
			File outFile = new File("ReservationMasterFile.txt");
			PrintWriter output = new PrintWriter(outFile);
			output.println(tempDoc);
			output.close();
		} catch (Exception ex) {
			System.out.println(ex);
		}
	}

	public void manualReservationCheck(){
		Scanner stdin = new Scanner(System.in);
		System.out.print("Input reservation number to check: ");
		String resNumber = stdin.next();
		System.out.println();
		if (searchReservations(resNumber) == true){
			System.out.println("Reservation Verified");
			String reservationInfo = getResInfo(resNumber);
			System.out.println(reservationInfo);
			System.out.println();
			System.out.println("Would you like to mark this reservation as completed? (Press 'y' for yes)");
			System.out.println();
			System.out.print("Input: ");
			String input = stdin.next();
			System.out.println();
			if (input.equals("y") || input.equals("Y")){
				markReservationComplete(resNumber);
			}else{
				System.out.println("Reservation left open");
				System.out.println();
			}
		}else{
			System.out.println("No reservation found for provided number");
			System.out.println();
		}
	}

	public boolean searchReservations(String reservation) {
		boolean match = false;		
		try {
			String myLine = "";
			String readReservation;
			FileReader myFile = new FileReader("DailyReservations.txt");
			Scanner scanMyFile = new Scanner(myFile);

			while (scanMyFile.hasNext()) {
				myLine = scanMyFile.nextLine();
				Scanner scanMyLine = new Scanner(myLine);
				readReservation = scanMyLine.next();

				if (readReservation.equals(reservation)) {
					match = true;
					break;
				}
			}
			myFile.close();
		} catch (Exception ex) {
			System.out.println("Unable to locate reservation file");
			System.out.println();
		}
		return match;
	}
	
	public void markReservationComplete(String reservation){
		String reservationDocumentLine = getResInfo(reservation);
		updateCompletedReservations(reservationDocumentLine);
		String myLine;
		String tempdoc = "";
		try{		
			FileReader myFile = new FileReader("DailyReservations.txt");
			Scanner scanMyFile = new Scanner(myFile);

			while (scanMyFile.hasNext()){
				myLine = scanMyFile.nextLine();
				if (myLine.equals(reservationDocumentLine)){
					//Do nothing
				}else{
					tempdoc = tempdoc + myLine + "\n";
				}
			}
			myFile.close();
			File outFile = new File("DailyReservations.txt");
			PrintWriter output = new PrintWriter(outFile);
			output.println(tempdoc);
			output.close();
			System.out.println("Reservation marked as completed");
			System.out.println();
		}catch(Exception ex){
			System.out.println("Daily reservation file missing or in incorrect format, unable to edit");
			System.out.println();
		}
	}

	public String getResInfo(String reservation){
		String info = "";
		try {
			String myLine = "";
			String readReservation;
			FileReader myFile = new FileReader("DailyReservations.txt");
			Scanner scanMyFile = new Scanner(myFile);

			while (scanMyFile.hasNext()) {
				myLine = scanMyFile.nextLine();
				Scanner scanMyLine = new Scanner(myLine);
				readReservation = scanMyLine.next();

				if (readReservation.equals(reservation)) {
					info = myLine;
					break;
				}
			}
			myFile.close();
		} catch (Exception ex) {
			System.out.println("Unable to locate reservation file");
			System.out.println();
		}
		return info;
	}

	public void updateCompletedReservations(String newLine){
		String myLine;
		String tempdoc = "";
		try{		
			FileReader myFile = new FileReader("CompletedReservations.txt");
			Scanner scanMyFile = new Scanner(myFile);

			while (scanMyFile.hasNext()){
				myLine = scanMyFile.nextLine();
				tempdoc = tempdoc + myLine + "\n";
			}
			myFile.close();
			tempdoc = tempdoc + newLine + "\n";
			File outFile = new File("CompletedReservations.txt");
			PrintWriter output = new PrintWriter(outFile);
			output.println(tempdoc);
			output.close();
		}catch(Exception ex){
			System.out.println("Daily reservation file missing or in incorrect format, unable to edit");
			System.out.println();
		}
	}

	public void updateMaster() {
		try {
			String myLine;
			FileReader myFile = new FileReader("CompletedReservations.txt");
			Scanner scanMyFile = new Scanner(myFile);

			while (scanMyFile.hasNext()) {
				myLine = scanMyFile.nextLine();
				Scanner scanMyLine = new Scanner(myLine);
				String[] tokens = myLine.split(" ");

				masterUpdater(tokens[0], tokens[1], tokens[2], tokens[3], tokens[4], tokens[5]);
			}
			myFile.close();
			File outFile = new File("CompletedReservations.txt");
			PrintWriter output = new PrintWriter(outFile);
			output.close();
			System.out.println("Master File Updated");
			System.out.println();
		} catch (Exception ex) {
			System.out.println("Error, 'completed reservations' file missing or in incorrect format.");
			System.out.println("Unable to update master file without it.");
			System.out.println();
		}
	}

	// Update information in the Master File

	public void masterUpdater(String accountNumber, String firstName, String lastName, String month, String day, String year) {

		try {
			int internalSearchCount = 0;
			int totalLine = getDailyReservations();
			String tempDoc = "";
			String myLine;
			String totalVisitString = "";
			String fileAcNo = "";
			int totalVisit = 0;
			int wordCounter = 0;
			FileReader myFile = new FileReader("ReservationMasterFile.txt");
			Scanner scanMyFile = new Scanner(myFile);

			while (scanMyFile.hasNext()) {
				myLine = scanMyFile.nextLine();
				Scanner scanMyLine = new Scanner(myLine);
				fileAcNo = scanMyLine.next();

				// Match found means that this customer has been to the garage before
				if (fileAcNo.equals(accountNumber)) {
					tempDoc = tempDoc + accountNumber + " " + firstName + " " + lastName + " " + month + " " + day + " " + year;

					while (wordCounter < 6){
						totalVisitString = scanMyLine.next();
						wordCounter++;
					}

					totalVisit = Integer.parseInt(totalVisitString);
					totalVisit++;
					tempDoc = tempDoc + " " + totalVisit + "\n";

				} else { 
					tempDoc = tempDoc + myLine + "\n";
					internalSearchCount++;

				}
			}
			// Match not found after reading entire document means that this is a new customer.
			if (internalSearchCount == totalLine) {
				tempDoc = tempDoc + accountNumber + " " + firstName + " " + lastName + " " + month + " " + day + " " + year + " 1";
			}

			myFile.close();
			File outFile = new File("ReservationMasterFile.txt");
			PrintWriter output = new PrintWriter(outFile);
			output.println(tempDoc);
			output.close();
		} catch (Exception ex) {
			System.out.println("Master file missing or in incorrect format");
			System.out.println();
		}
	}

	// getDailyReservations method returns the total number of reservations for the day.
	public int getDailyReservations() {
		int counter = 0;
		try {
			FileReader myFile = new FileReader("ReservationMasterFile.txt");
			Scanner scanMyFile = new Scanner(myFile);

			while (scanMyFile.hasNext()) {
				counter++;
				scanMyFile.nextLine();
			}
		} catch (Exception ex) {
			System.out.println("Reservation master file missing or in incorrect format");
			System.out.println();
		}
		return counter;
	}

}