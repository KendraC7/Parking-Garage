import java.awt.*; // for layout managers
 import java.awt.event.*; //to respond to events
 import javax.swing.*; // for GUI components
 import java.awt.Font; //to set font style and size
 import javax.swing.border.Border; //for label borders
 //package GUI;
 
 //BLACK, BLUE, CYAN, DARK_GRAY, GRAY, GREEN, LIGHT_GRAY, MAGENTA, ORANGE, PINK, RED, WHITE, YELLOW

class CustomerGui implements ActionListener
 {		
	protected JFrame frame;
	protected JPanel ORL;
	protected int ORLbtnWid;
	protected int ORLbtnHgh;		
	protected JLabel ORLlblOpen;
	protected JLabel ORLlblRes;
	protected JButton ORLbtnOpen;
	protected JButton ORLbtnRes;		
	protected JPanel OL;	
	protected int OLbtnWid;
	protected int OLbtnHgh;		
	protected JLabel OLlblInfo;
	protected JButton OLbtnEnter;
	protected garage Garage;
			 
		
	//public CustomerGui(garage g) //add garage object to constructor 
	public CustomerGui(garage g, int SO, int SR) 
	{
		Garage = g;
		int startOpen = SO;
		int startReserved = SR;
		frame = new JFrame();
		frame.setForeground(Color.WHITE);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setTitle("Garage Check-in User Kiosks sample"); //title of frame window
		frame.setLayout(new GridLayout(2,2,3,5)); //2 row, 2 column, vrtcl gap, hrzntl gap
		frame.setLocation(new Point(300, 100)); //how far down/ inside window starts
		frame.setSize(new Dimension(600,600)); //width,height
		
		ORL = new JPanel(new GridLayout(2,1,3,0)); //2rows, 1 column
		ORL.setBorder(BorderFactory.createLineBorder(Color.MAGENTA,3));
		OL = new JPanel(new GridLayout(2,1,0,8));
		OL.setBorder(BorderFactory.createLineBorder(Color.MAGENTA,3));
		JPanel Blnk1 = new JPanel();
		JPanel Blnk2 = new JPanel();
		
		ORLbtnWid = 130;
		ORLbtnHgh = 50;
		OLbtnWid = 30;
		OLbtnHgh = 50;	
		
		Font ORLbtnOpenFnt = new Font("Times",Font.BOLD,20);
		Font ORLlblOpenFnt = new Font("Times",Font.PLAIN,16);
		Font ORLbtnResFnt = new Font("Times",Font.BOLD,20);
		Font ORLlblResFnt = new Font("Times",Font.PLAIN,16);
		Font OLlblFont = new Font("Times",Font.PLAIN,16); //text style and font
		Font OLbtnFont = new Font("Times",Font.BOLD,40);
		
		Border ORLOpenLblBdr = BorderFactory.createLineBorder(Color.BLUE,2);
		Border ORLResLblBdr = BorderFactory.createLineBorder(Color.RED,2);
		Border OLlblBorder = BorderFactory.createLineBorder(Color.BLACK,2);		

		ORLlblOpen = new JLabel("Open Spots: "+startOpen); 			
		ORLlblOpen.setBorder(ORLOpenLblBdr); //set border of label
		ORLlblOpen.setFont(ORLlblOpenFnt);		
			
		
		ORLlblRes = new JLabel("Reserved Spots: "+startReserved); 		
		ORLlblRes.setBorder(ORLResLblBdr);			
		ORLlblRes.setFont(ORLlblResFnt);
		
		OLlblInfo = new JLabel("Spots Available: "+startOpen); //create label		
		OLlblInfo.setBorder(OLlblBorder);
		OLlblInfo.setFont(OLlblFont);		
		OLlblInfo.setPreferredSize(new Dimension(10,30));
		
		ORLbtnOpen = new JButton("Open"); //create button. provide name as param
		ORLbtnOpen.setForeground(Color.BLUE); //set color property
		ORLbtnOpen.setPreferredSize(new Dimension(ORLbtnWid,ORLbtnHgh)); //width,height			
		ORLbtnOpen.setFont(ORLbtnOpenFnt);	
		
		ORLbtnRes = new JButton("Reserved"); //create button. provide name as param
		ORLbtnRes.setForeground(Color.RED); //set color property		
		ORLbtnRes.setPreferredSize(new Dimension(ORLbtnWid,ORLbtnHgh)); //(width,height
		ORLbtnRes.setFont(ORLbtnResFnt);
		
		OLbtnEnter = new JButton("Enter"); //create button. provide name as param
		OLbtnEnter.setForeground(Color.BLACK); //set text color
		OLbtnEnter.setPreferredSize(new Dimension(OLbtnWid,OLbtnHgh)); 
		OLbtnEnter.setFont(OLbtnFont);

		ORLbtnOpen.addActionListener(this);
		ORLbtnRes.addActionListener(this);		
		OLbtnEnter.addActionListener(this);	
		
		JPanel ORLSecInfo = new JPanel(new FlowLayout()); //1
		ORLSecInfo.add(ORLlblOpen); //add label to Panel
		ORLSecInfo.add(ORLlblRes); 
		ORL.add(ORLSecInfo);

		JPanel ORLSecType = new JPanel(new FlowLayout()); 		
		ORLSecType.add(ORLbtnOpen);
		ORLSecType.add(ORLbtnRes);		 
		ORL.add(ORLSecType);
		
		OL.add(OLlblInfo);
		OL.add(OLbtnEnter);
		
		frame.add(ORL);
		frame.add(Blnk1);
		frame.add(Blnk2);
		frame.add(OL);
		frame.pack(); //set objects' size to preferred sizes
		frame.setVisible(true);				//so frame actually shows on screen
	}//Constructor
	
	
	//if AFTER BUTTON IS PRESSED one more section can't be assigned -> disable 
	public void actionPerformed(ActionEvent e)
	{
		if (e.getSource() == ORLbtnOpen) 
		{ 
			ORLlblOpen.setText("update: "); 
			System.out.println("open pressed");
			Garage.assignOpen();
		}//ORL-Open sec btn pressed
		else
			if (e.getSource() == ORLbtnRes) 
			{ 
				ORLlblRes.setText("update: ");
				System.out.println("reserved pressed");
				Garage.assignReserved();
			}//ORL-Res sec btn pressed
			else
			{
				OLlblInfo.setText("update");
				System.out.println("enter pressed");
				Garage.assignOpen();
			}//OL- Enter garage btn pressed
	}// action
	
	
//call update functions each time section is assigned or cars leave sec. Param = occupancy numbers	
	public void OpenUpdate(int spots)
	{
		if (spots > -1)
		{
			ORLlblOpen.setText("Open spots: "+spots);
			OLlblInfo.setText("Open spots: "+spots);
		}
	}//OpenUpdate()
	
	public void ResUpdate(int spots) 
	{
		if (spots > -1)
		{
			ORLlblRes.setText("Reserved spots: "+spots);
		}
	}//ResUpdate()
	
//call Avail functions when open or res secs were full but now spot(s) available	
	public void ResAvail(int spots) //enables reserved parking button
	{
			ORLbtnRes.setEnabled(true);
			ORLlblRes.setText("Reserved spots: "+spots);
	}//ResAvail
	
	public void OpenAvail(int spots) //enables open parking button
	{
		ORLbtnOpen.setEnabled(true);
		OLbtnEnter.setEnabled(true);
		ORLlblOpen.setText("Open spots: "+spots);
		OLlblInfo.setText("Open spots: "+spots);		
	}//OpenAvail()
	
//call Full functions when all spots in open or res secs have been assigned	
	public void ResFull() //disables reserved parking button
	{
		ORLbtnRes.setEnabled(false);
		ORLlblRes.setText("Reserved spots: 0");
	}//ResFull()
	
	public void OpenFull() //disables open parking buttons
	{
		ORLbtnOpen.setEnabled(false);
		OLbtnEnter.setEnabled(false);
		ORLlblOpen.setText("Open spots: 0");
		OLlblInfo.setText("Open spots: 0");
	}//OpenFull()
}//OpenResLane class


/*  To complete #5, I could try a simple while statement something like for example when open spots are full:    
while (NumOpenSpots >= numOpenTickets) {btn is disabled}
and same for the reserved sections. Let me know if you have another idea. If you think of a way to do it on your own just notice the isFull functions do not have parameters,
 but the update functions need the number of spots left as parameters.*/



//create gui in file that assigns sections 
public class GUIS
{
	public static void main(String[] args) 
	{
		//CustomerGui gui = new CustomerGui(); //construct/show GUI
		//gui.ResUpdate(4);
		//gui.OpenUpdate(100);
		//gui.ResFull();
	}//main
}//GUIS class