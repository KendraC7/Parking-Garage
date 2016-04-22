import java.util.*; //for Date object, Scanner object  
import java.text.DateFormat;  //for DateFormat object 
import java.text.SimpleDateFormat; //for SimpleDateFormat object

//******************************************************************************
// Abstract so section CANNOT be instantiated but all types of sections 
// Will inherit these variables and methods and be able to override if necessary
//******************************************************************************

abstract class Section{

	String name;
	int spaces;
	int occupancy;
	int tickets;
	String code;
	boolean reserved;

	// **************************
	// CONSTRUCTOR & MISC METHODS
	// **************************

		public Section(String n, int s, String c, boolean r){
			name = n;
			spaces = s;
			occupancy = 0;
			tickets = 0;
			code = c;
			reserved = r;
		}

		public void printInfo(){
			System.out.println("Section: " + name);
			System.out.println("Total Spaces: " + spaces);
			System.out.println("Occupancy: " + occupancy);
			System.out.println("Ticket Count: " + tickets);
			System.out.println("Entrance Code: " + code);
			System.out.println("Reserved: " + reserved);
		}

		public boolean checkCode(String c){
			if (code.equals(c)){
				return true;
			}else{
				return false;
			}
		}

		public boolean hasRoom(){
			if (tickets < spaces){
				return true;
			}else{
				return false;
			}
		}

		public synchronized void printTicket(){
			DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
			Date date = new Date();
			System.out.println("Ticket:");
			System.out.println("-------");
			System.out.println("Date: " + date.toString());
			System.out.println("Section: " + name);
			System.out.println("Entrance Code: " + code);
			System.out.println("-------");
			incrementTickets();
			System.out.println();
		}
	
	// *****************************
	// INCREMENT & DECREMENT METHODS
	// *****************************

		public void incrementOccupancy(){
			occupancy ++;
		}
	
		public void incrementTickets(){
			tickets ++;
		}
	
		public void decrementOccupancy(){
			if (occupancy > 0){			
				occupancy --;
			}else{
				System.out.println("Occupancy already at 0, cannot decrement");
				System.out.println();
			}
		}
	
		public void decrementTickets(){
			if (tickets > 0){
				tickets --;
			}else{
				System.out.println("Ticket count already at 0, cannot decrement");
				System.out.println();
			}
		}
	
	// *****************
	// GET & SET METHODS
	// *****************

		public void setName(String n){
			name = n;
		}

		public String getName(){
			return name;
		}

		public void setSpaces(int s){
			spaces = s;
		}

		public int getSpaces(){
			return spaces;
		}

		public void setOccupancy(int o){
			occupancy = o;
		}

		public int getOccupancy(){
			return occupancy;
		}

		public void setTickets(int t){
			tickets = t;
		}

		public int getTickets(){
			return tickets;
		}

		public void setCode(String c){
			code = c;
		}

		public String getCode(){
			return code;
		}
	
		public void setReserved(boolean r){
			reserved = r;
		}

		public boolean getReserved(){
			return reserved;
		}
	
}

class OpenSec extends Section
{
	boolean reserved;
	public OpenSec(String n, int s, String c)
	{ 
		super(n, s, c, false);
	}
	
}//open section

class ResSec extends Section 
{
	boolean reserved;
	public ResSec(String n, int s, String c)
	{ 
		super(n, s, c, true);
	}
}//reserved section 
