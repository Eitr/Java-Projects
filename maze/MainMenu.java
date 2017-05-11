package maze;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Hashtable;
import java.util.Random;
import java.util.Vector;


public class MainMenu 
{

	int rmAmount = 0;
	int curRoomCount = 0;

	Vector<RoomPanel> rmVecs = new Vector<RoomPanel>() ;

	Random numGen = new Random();
	Integer[] numItems = {1,2,3,4,5,6,7,8,9,10};
	JPanel rmPan;
	JScrollPane scroll;
	JPanel difPan;
	JSlider difSlide;
	JLabel labGmDif;

	JPanel exitPan;
	JButton saveBut;
	JButton playBut;
	JButton cancelBut;
	
	//Used only to signal the Controller class that a new room has been added
	//NOT VISIBLE EVER
	JButton roomAdded;
	public JFrame mainFrame;
	
	
	JLabel infoLabel[];
	JButton addRm;

	public MainMenu()
	{

		mainFrame = new JFrame();

		rmPan = new JPanel();
		JPanel bigPan = new JPanel();
		bigPan.setLayout(new BoxLayout(bigPan,BoxLayout.Y_AXIS));
		difPan = new JPanel();
		difSlide = new JSlider(1,4,1);

		Hashtable<Integer, JLabel> table = new Hashtable<Integer, JLabel>();
		table.put(1, new JLabel("Easy"));
		table.put(2, new JLabel("Normal"));
		table.put(3, new JLabel("Pro"));
		table.put(4, new JLabel("Marathon"));


		difSlide.setLabelTable(table);
		difSlide.setPaintLabels(true);
		difSlide.setSnapToTicks(true);

		labGmDif = new JLabel("Difficulty: ");


		difPan.add(labGmDif);
		difPan.add(difSlide);

		exitPan = new JPanel();
		saveBut = new JButton("Save");
		saveBut.setSize(50, 25);
		saveBut.setEnabled(false); // TODO
		playBut = new JButton("Play Game");
		playBut.setSize(50, 25);
		cancelBut = new JButton("Cancel");
		cancelBut.setSize(50, 25);

		exitPan.setLayout(new FlowLayout());
		exitPan.add(saveBut);
		exitPan.add(playBut);
		exitPan.add(cancelBut);
		
		//Signal button
		roomAdded = new JButton();

		bigPan.setLayout(new BoxLayout(bigPan, BoxLayout.Y_AXIS));


		scroll = new JScrollPane(rmPan, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
		
		Dimension d = new Dimension(400, 400);
		scroll.setPreferredSize(d);

		//		rmPanHelper.setLayout(new BoxLayout(rmPanHelper, BoxLayout.Y_AXIS));
		rmPan.setLayout(new GridLayout(10, 1, 5, 5));



		/////////////// action listener for the add room button /////////////////////////
		addRm = new JButton("Add Room");
		
		
		infoLabel = new JLabel[]{new JLabel("Name"),new JLabel("Color"),new JLabel("Edit"),new JLabel("# Items"),new JLabel("Remove"),new JLabel("Issues")};
		
		JPanel infoPanel = new JPanel();
		infoPanel.setLayout(new BoxLayout(infoPanel, BoxLayout.X_AXIS));
		int id = 0;
		
		infoPanel.add(Box.createHorizontalStrut(40));
		infoPanel.add(infoLabel[id++]);
		infoPanel.add(Box.createHorizontalStrut(80));
		infoPanel.add(infoLabel[id++]);
		infoPanel.add(Box.createHorizontalStrut(45));
		infoPanel.add(infoLabel[id++]);
		infoPanel.add(Box.createHorizontalStrut(40));
		infoPanel.add(infoLabel[id++]);
		infoPanel.add(Box.createHorizontalStrut(40));
		infoPanel.add(infoLabel[id++]);
		infoPanel.add(Box.createHorizontalStrut(40));
		infoPanel.add(infoLabel[id++]);
		infoPanel.add(Box.createHorizontalStrut(140));
			
		

		bigPan.add(difPan);
		bigPan.add(Box.createVerticalGlue());
		bigPan.add(addRm);
		bigPan.add(Box.createVerticalGlue());
		bigPan.add(infoPanel);
		bigPan.add(Box.createVerticalGlue());
		bigPan.add(scroll);
		bigPan.add(Box.createVerticalGlue());
		bigPan.add(exitPan);
		bigPan.add(Box.createVerticalGlue());
		mainFrame.add(bigPan);

		addRm.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {

				if(rmAmount<10)
				{
					makeRmPan();
					updateRms(rmPan);
					rmPan.updateUI();
					scroll.updateUI();
					roomAdded.doClick();
					
				}
				//Does nothing if 10 rooms already
			}
		});


		mainFrame.setSize(700,600);
		mainFrame.setResizable(false);
		mainFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);



	}

	public void updateRms(JPanel rmPan)
	{
		
		for(int ndx = 0; ndx < rmVecs.size(); ++ndx)
		{
			rmPan.add(rmVecs.get(ndx));
		}
		
	}

	public void makeRmPan()
	{
		RoomPanel newPan = new RoomPanel(curRoomCount, numItems);
		rmVecs.add(newPan);

		rmAmount++;
		curRoomCount++;

	}

}

@SuppressWarnings("serial")
class RoomPanel extends JPanel{
	Color clr;
	String roomName;
	JButton butColor;
	JButton butEdit;
	JComboBox boxItem;
	JButton butRemove;
	JLabel information;
	JTextField room;
	
	public RoomPanel(int roomNum, Integer[] numItems){
		
//		setLayout(new FlowLayout(FlowLayout.LEADING));
		setLayout(new BoxLayout(this, BoxLayout.X_AXIS));
		
		//String for room name set to current (Room  #roomnumber)
		roomName = "Room " + (roomNum + 1);
		room = new JTextField(roomName);
		room.setPreferredSize(new Dimension(140, 25));
		room.setMaximumSize(new Dimension(140, 25));
		room.setSize(140, 25);
		
		
		//Random color for the Color Button.
		Random numGen = new Random();
		clr = new Color(numGen.nextInt(256), numGen.nextInt(256), numGen.nextInt(256));
		butColor = new JButton("  Set  ");
		butColor.setBackground(clr);
		butColor.setSize(50, 25);
		
		//Edit Button
		butEdit = new JButton("   ?   ");
		butEdit.setSize(50, 25);
		
		//Item ComboBox
//		JLabel labItems = new JLabel("#Items:");
		boxItem = new JComboBox(numItems);
		boxItem.setPreferredSize(new Dimension(80,25));
		boxItem.setSize(80,25);
		boxItem.setMaximumSize(new Dimension(80,25));
		
		//Remove Button
		butRemove = new JButton("   X   ");
		butRemove.setSize(50,25);
		
		//Informational  label
		information = new JLabel("");
		information.setSize(100, 25);
		information.setMinimumSize(new Dimension(100,20));
		information.setPreferredSize(new Dimension(100,20));
		
		//Add components to this
		
		add(Box.createHorizontalStrut(10));
		add(room);
		add(Box.createHorizontalStrut(10));
		add(butColor);
		add(Box.createHorizontalStrut(10));
		add(butEdit);
		add(Box.createHorizontalStrut(10));
//		add(labItems);
		add(boxItem);
		add(Box.createHorizontalStrut(10));
		add(butRemove);
		add(Box.createHorizontalStrut(10));
		add(information);
		add(Box.createHorizontalStrut(10));
		
		
	}
	
	public void rename(String str){
		roomName = str;
	}
	
	public void repickColor(Color color){
		clr = color;
		butColor.setBackground(clr);
	}
}