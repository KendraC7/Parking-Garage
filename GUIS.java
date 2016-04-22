import java.awt.*; // for layout managers
 import java.awt.event.*; //to respond to events
 import javax.swing.*; // for GUI components
 import java.awt.Font; //to set font style and size
 import javax.swing.border.Border; //for label borders


//create actual CustomerGui object in file that assigns sections 

class CustomerGui implements ActionListener  //need to implement ActionListener to respond to events
 {		
	protected JFrame frame;  	//main frame to be shown
	protected JPanel ORL;		//panel to contain all related components for the open/reservation lane 
	protected int ORLbtnWid;  	//open/reservation lane's buttons' width
	protected int ORLbtnHgh;	//open/reservation lane's buttons' height
	protected JLabel ORLlblOpen;	//open/reservation lane label showing open section occupancy
	protected JLabel ORLlblRes; 	//open/reservation lane label showing reserved section occupancy
	protected JButton ORLbtnOpen;	//open/reservation lane button for selecting an open section
	protected JButton ORLbtnRes;	//open/reservation lane button for selecting a reserved section
	protected JPanel OL;		//panel to contain all related components for the open lane
	protected int OLbtnWid;		//open lane's button's width	
	protected int OLbtnHgh;		//open lane's button's height 
	protected JLabel OLlblInfo;	//open lane label showing open section occupancy
	protected JButton OLbtnEnter;	//open lane button for selecting an open section
	protected garage Garage;	//garage object connected to the GUI
			 
		
	public CustomerGui(garage g, int SO, int SR) 	//constructor
	{
		Garage = g;			//gare object connected to the GUI
		int startOpen = SO;		//starting number of available open parking spots
		int startReserved = SR;		//starting number of available reserved parking spots
		frame = new JFrame();		//initialize frame
		frame.setForeground(Color.WHITE);	//frame color
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);	//close frame when x is pressed
		frame.setTitle("Garage Check-in User Kiosks sample"); 	//title of frame window
		frame.setLayout(new GridLayout(2,2,3,5)); 	//set layout manager of frame. layout params = 2 row, 2 column, vrtcl gap, hrzntl gap
		frame.setLocation(new Point(300, 100)); 	//set frame's position. how far down/ inside window 
		frame.setSize(new Dimension(600,600)); 		//set frame's start size (width,height)
		
		ORL = new JPanel(new GridLayout(2,1,3,0)); //panel for all open/reservation lane related content to placed inside frame. 2rows, 1 column
		ORL.setBorder(BorderFactory.createLineBorder(Color.MAGENTA,3));
		OL = new JPanel(new GridLayout(2,1,0,8));   //panel for all Open lane content to be placed inside frame 
		OL.setBorder(BorderFactory.createLineBorder(Color.MAGENTA,3));
		JPanel Blnk1 = new JPanel();	//blank space filler panel to add to frame to get desired layout
		JPanel Blnk2 = new JPanel();    
		
		ORLbtnWid = 130;	//to set preferred width of open/reservation lane button
		ORLbtnHgh = 50;		//to set preferred height of open/reservation lane button
		OLbtnWid = 30;		//to set preferred width of open lane button
		OLbtnHgh = 50;		//to set preferred height of open lane button
		
		Font ORLbtnOpenFnt = new Font("Times",Font.BOLD,20); //set font and text size for label
		Font ORLlblOpenFnt = new Font("Times",Font.PLAIN,16);
		Font ORLbtnResFnt = new Font("Times",Font.BOLD,20);
		Font ORLlblResFnt = new Font("Times",Font.PLAIN,16);
		Font OLlblFont = new Font("Times",Font.PLAIN,16); 
		Font OLbtnFont = new Font("Times",Font.BOLD,40);
		
		Border ORLOpenLblBdr = BorderFactory.createLineBorder(Color.BLUE,2);  //create line borders for labels
		Border ORLResLblBdr = BorderFactory.createLineBorder(Color.RED,2);
		Border OLlblBorder = BorderFactory.createLineBorder(Color.BLACK,2);		

		ORLlblOpen = new JLabel("Open Spots: "+startOpen); 	//create label with starting open occupancy displayed	
		ORLlblOpen.setBorder(ORLOpenLblBdr); 			//set label border
		ORLlblOpen.setFont(ORLlblOpenFnt);			//set label font
			
		
		ORLlblRes = new JLabel("Reserved Spots: "+startReserved); 	//create label with starting reserved occupancy displayed	
		ORLlblRes.setBorder(ORLResLblBdr);			
		ORLlblRes.setFont(ORLlblResFnt);
		
		OLlblInfo = new JLabel("Spots Available: "+startOpen); 		
		OLlblInfo.setBorder(OLlblBorder);
		OLlblInfo.setFont(OLlblFont);		
		OLlblInfo.setPreferredSize(new Dimension(10,30));  //set label size
		
		ORLbtnOpen = new JButton("Open"); 		//create button. provide name as param
		ORLbtnOpen.setForeground(Color.BLUE); 		//set button text color
		ORLbtnOpen.setPreferredSize(new Dimension(ORLbtnWid,ORLbtnHgh)); //set button preferred size		
		ORLbtnOpen.setFont(ORLbtnOpenFnt);					//set button font
		
		ORLbtnRes = new JButton("Reserved"); 
		ORLbtnRes.setForeground(Color.RED); 		
		ORLbtnRes.setPreferredSize(new Dimension(ORLbtnWid,ORLbtnHgh)); 
		ORLbtnRes.setFont(ORLbtnResFnt);
		
		OLbtnEnter = new JButton("Enter"); 
		OLbtnEnter.setForeground(Color.BLACK); 
		OLbtnEnter.setPreferredSize(new Dimension(OLbtnWid,OLbtnHgh)); 
		OLbtnEnter.setFont(OLbtnFont);

		ORLbtnOpen.addActionListener(this);		//to repsond to button being pressed
		ORLbtnRes.addActionListener(this);		
		OLbtnEnter.addActionListener(this);	
		
		JPanel ORLSecInfo = new JPanel(new FlowLayout()); //info panel to holdand format two labels together
		ORLSecInfo.add(ORLlblOpen); 			//add label to Panel
		ORLSecInfo.add(ORLlblRes); 
		ORL.add(ORLSecInfo); //add info panel to panel for all open/reservation lane content

		JPanel ORLSecType = new JPanel(new FlowLayout()); 	//panel to hold and format two buttons together	
		ORLSecType.add(ORLbtnOpen);
		ORLSecType.add(ORLbtnRes);		 
		ORL.add(ORLSecType);	//add button panel to panel for all open/reservation lane content
		
		OL.add(OLlblInfo);		//add single open lane occpuancy label to panel for all open lane content
		OL.add(OLbtnEnter);		//add single open lane button to panel for all open lane content
		
		frame.add(ORL); 		//add the panel with all open/reservation lane content to the frame
		frame.add(Blnk1);		//add the space filler panel 
		frame.add(Blnk2);
		frame.add(OL);			//add the panel with all open lane content to the frame
		frame.pack(); 			//set objects' size to preferred sizes
		frame.setVisible(true);		//so frame actually shows on screen
	}//Constructor
	
	
	public void actionPerformed(ActionEvent e)  //function to respond to button press
	{
		if (e.getSource() == ORLbtnOpen)  //determine which button was pressed, one to find an open section or reserved section
		{ 
			Garage.assignOpen();	//indicate button to find open section was pressed
		}//ORL-Open sec btn pressed
		else
			if (e.getSource() == ORLbtnRes) 
			{ 
				Garage.assignReserved();	//indiate button to find a reserved section was pressed
			}//ORL-Res sec btn pressed
			else
			{
				Garage.assignOpen();
			}//OL- Enter garage btn pressed
	}// action
	
	
//call update functions each time section is assigned or cars leave sec. Param = occupancy numbers	
	public void OpenUpdate(int spots)	//update labels with open section occupancy information 
	{
		if (spots > -1)
		{
			ORLlblOpen.setText("Open spots: "+spots); 	//update label in open/reservation lane
			OLlblInfo.setText("Open spots: "+spots); 	//update label in open lane
		}
	}//OpenUpdate()
	
	public void ResUpdate(int spots) 	//update labels with reserved section occupancy information
	{
		if (spots > -1)
		{
			ORLlblRes.setText("Reserved spots: "+spots); //update label in open/reservation lane
		}
	}//ResUpdate()
	
//call full functions when all spots in open or res secs have been assigned	
	public void ResFull() 			//indicate NO MORE available parking spaces in the reserved sections
	{
		ORLbtnRes.setEnabled(false);	//disable reserved parking button (no longer able to be clicked)
		ORLlblRes.setText("Reserved spots: 0"); //update reserved section occupancy information
	}//ResFull()
	
	public void OpenFull() 			//indicate NO MORE available parking spaces in the open sections
	{
		ORLbtnOpen.setEnabled(false);	//disable open parking button 
		OLbtnEnter.setEnabled(false);
		ORLlblOpen.setText("Open spots: 0");	//update open section occupancy label
		OLlblInfo.setText("Open spots: 0");
	}//OpenFull()
	
//call Avail functions when open or res secs were full but now spot(s) available	
	public void ResAvail(int spots) 			//indicate THERE IS at least one availble parking space in the reserved sections
	{
			ORLbtnRes.setEnabled(true);			//enable reserved parking button
			ORLlblRes.setText("Reserved spots: "+spots);	//update reserved section occupancy label
	}//ResAvail
	
	public void OpenAvail(int spots)  //indicate THERE IS at least one availble parking space in the open sections
	{
		ORLbtnOpen.setEnabled(true);	//enable open parking button
		OLbtnEnter.setEnabled(true);
		ORLlblOpen.setText("Open spots: "+spots);	//update open section occupancy label
		OLlblInfo.setText("Open spots: "+spots);		
	}//OpenAvail()
	
}//OpenResLane class
