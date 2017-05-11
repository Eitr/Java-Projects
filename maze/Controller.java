package maze;

import java.awt.event.*;
import java.awt.*;
import javax.swing.*;

import java.io.*;


//import maze.*;

//import java.util.Random;
import java.util.Vector;

public class Controller {
	//Menus
	private MainMenu topMenu;
	
	final static int MAX_VAL_X = ((int)(Toolkit.getDefaultToolkit().getScreenSize().width/24/2.5*2)/2*2+1);
	final static int MAX_VAL_Y = ((int)(Toolkit.getDefaultToolkit().getScreenSize().height/24/2.5*2)/2*2+1);
	
	//Game aspects
	static Vector<RoomPanel> roomPanels;
	//Difficulty -- currently 1-4, ask Michael what these should be
	int difficulty;
	static Vector<RoomModel> roomModels;
	
	Labyrinth lab;
	
	public Controller(){
		
		topMenu = new MainMenu();
		topMenu.mainFrame.setLocationRelativeTo(null);
		topMenu.mainFrame.setVisible(true);
		
		//default difficulty is 1
		difficulty = 1;
		
		//Keep track of RoomPanels
		roomPanels = topMenu.rmVecs;
		//Keep track of corresponding models
		roomModels = new Vector<RoomModel>();
		
		//Add actionListeners defined below:
		topMenu.roomAdded.addActionListener(new MakeModel());
		topMenu.difSlide.addMouseListener(new DifficultyListener());
		topMenu.saveBut.addActionListener(new SaveListener());//These can be reused for menu items too
		topMenu.cancelBut.addActionListener(new CancelListener());
		topMenu.playBut.addActionListener(new PlayListener());
		
		topMenu.addRm.doClick();
	}
	
	
	//listener for the difficulty slider, changes values: int difficulty.
	class DifficultyListener implements MouseListener{
		
		public void mouseClicked(MouseEvent arg0) {
			difficulty = topMenu.difSlide.getValue();
		}
		
		public void mouseReleased(MouseEvent arg0) {
			difficulty = topMenu.difSlide.getValue();
		}
		
		public void mouseEntered(MouseEvent arg0) {}
		public void mouseExited(MouseEvent arg0) {}
		public void mousePressed(MouseEvent arg0) {}
	}
	
	/** This class listens to the hidden button in the MainMenu for a signal
	that a new RoomPanel has been made to make a corresponding model 
	 */
	class MakeModel implements ActionListener{
		public void actionPerformed(ActionEvent arg0) {
			RoomPanel temp = roomPanels.get(roomPanels.size()-1);
			RoomModel mod = new RoomModel(temp.roomName,temp.clr,temp);
			roomModels.add(mod);
			temp.butRemove.addActionListener(new RemoveRoomListener());
			temp.butEdit.addActionListener(new EditButtonListener());
			temp.boxItem.addActionListener(new ItemNumberListener());
			temp.butColor.addActionListener(new ColorChangeListener());
			
			
			if(Model.hasStart(roomModels))
			{	
				Model.setVisitedFalse(roomModels);
				if(Model.canBeReached(Model.getStartRoom(roomModels),mod))
				{
					if(Model.hasFinish(roomModels))
						temp.information.setText("");
					else
						temp.information.setText("No end position yet");
				}
				else
					temp.information.setText("Not Reachable");
			}
			else
				temp.information.setText("No start position yet");
		}
	}
	//When a user presses the remove button
	class RemoveRoomListener implements ActionListener{
		
		public void actionPerformed(ActionEvent e){
			JButton button = (JButton)e.getSource();
			RoomPanel panel = (RoomPanel) button.getParent();
			//RoomModel temp = Controller.findModel(panel.roomName);
			RoomModel temp  = roomModels.get(Controller.findModel(panel.roomName));
			
			if(roomModels.size() == 1)
			{
				JOptionPane.showMessageDialog(null, "You must have at least one room.");
				return;
			}
			
			int answer = JOptionPane.showConfirmDialog(null, "Are you sure you want to remove room: "+
					temp.name + "?", "Removing Room:", JOptionPane.YES_NO_OPTION);
			if(answer == JOptionPane.YES_OPTION){
				topMenu.rmAmount--;
				
				for (int i  = 0; i< temp.portals.size();i++){
					
					PortalModel mod = temp.portals.get(i);
					Vector<PortalModel> ports = mod.room2.portals;
					
					for(int j = 0; j< ports.size();j++){
						if (mod.equivalent(ports.get(j)))
							ports.removeElementAt(j);
					}
					
				}
				roomPanels.removeElement(panel);
				roomModels.removeElement(temp);
				topMenu.rmPan.remove(panel);
				topMenu.updateRms(topMenu.rmPan);
				topMenu.rmPan.updateUI();
				topMenu.scroll.updateUI();
				
				for (int i  = 0; i<roomModels.size(); i++){
					RoomModel mod = roomModels.get(i);
					
					
					if(Model.hasStart(roomModels))
					{	
						Model.setVisitedFalse(roomModels);
						if(Model.canBeReached(Model.getStartRoom(roomModels),mod))
						{
							if(Model.hasFinish(roomModels))
								mod.panel.information.setText("");
							else
								mod.panel.information.setText("No end position yet");
						}
						else
							mod.panel.information.setText("Not Reachable");
					}
					else
						mod.panel.information.setText("No start position yet");
				}
				
				
			}
			
			
		}
	}
	
	
	// TODO
	class SaveListener implements ActionListener{
		
		public void actionPerformed(ActionEvent arg0) {
			System.out.println("Saving game...");
		}
	}
	
	
	class CancelListener implements ActionListener{
		
		public void actionPerformed(ActionEvent e) {
			int answer = JOptionPane.showConfirmDialog(null, "Do you want to save before closing " +
					"maze?");
			if(answer == JOptionPane.YES_OPTION){
				topMenu.saveBut.doClick();
			}
			
			if (answer != JOptionPane.CANCEL_OPTION){
				System.exit(0);
				
			}
		}
	}
	
	class PlayListener implements ActionListener{
		
		public void actionPerformed(ActionEvent e) {
			
			if (!finalizeRooms())
				return;
			//First check if it is a viable map
			if(Model.canPlay(roomModels)){
				System.out.println("Launching the player.");
				final Labyrinth lab = new Labyrinth();
				lab.setDifficulty(difficulty);
				RoomModel temp;
				for (int i = 0; i< roomModels.size(); i++){
					
					temp = roomModels.get(i);
					lab.addMaze(temp.clr, temp.sizeX, temp.sizeY);
					PortalModel port;
					
					for(int j = 0; j<temp.portals.size(); j++){
						port = temp.portals.get(j);
						lab.addPortal(port.room1.clr, port.posX1, port.posY1, port.room2.clr);
					}
					lab.setItem(temp.clr, temp.numItems);
					lab.setMazeName(temp.clr, temp.name);
					
				}
				lab.setStartLocation(Model.getStartRoom(roomModels).start);
				lab.setStartMaze(Model.getStartRoom(roomModels).clr);
				lab.setEndLocation(Model.getEndRoom(roomModels).finish);
				lab.setEndMaze(Model.getEndRoom(roomModels).clr);
				
				lab.finalize();
				JFrame gameFrame = new JFrame();
				gameFrame.setSize(800,800);
				gameFrame.setExtendedState(JFrame.MAXIMIZED_BOTH);
				
				topMenu.mainFrame.setVisible(false);
				gameFrame.setVisible(true);
				gameFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
				gameFrame.add(lab);
				
				
				
				////////////////////////////////////////
				
				
				
				MenuItem save = new MenuItem("Save");
				MenuItem load = new MenuItem("Load");
				
				save.addActionListener( new ActionListener() {
					public void actionPerformed(ActionEvent e) {
						saveGame();	} } );
				
				load.addActionListener( new ActionListener() {
					public void actionPerformed(ActionEvent e) {
						loadGame();	} } );
				
				
				Menu file = new Menu("File");
				file.add(save);
				file.add(load);
				
				
				MenuBar menu = new MenuBar();
				menu.add(file);
				
				gameFrame.setMenuBar(menu);
			}
			else {
				System.out.println("Can't Play:");
				if(!Model.hasStart(roomModels))
					System.out.println("Doesn't have a start position");
				if(!Model.hasFinish(roomModels))
					System.out.println("Doesn't have a finish position");
				if(!Model.enoughPortals(roomModels))
					System.out.println("Doesn't have portals for each room");
			}
			
			
		}
		
		
		
		private boolean finalizeRooms() {
			for(int i  = 0; i < roomModels.size(); i++){
				RoomModel mod = roomModels.get(i);
				String test = mod.panel.room.getText();
				if (test != mod.name){
					for (int j = 0; j<roomModels.size(); j++){
						
						if (i == j){
							continue;
						}
						
						if(roomModels.get(j).name.equals(test)){
							JOptionPane.showMessageDialog(null, "The room name" + test + " is already in use");
							return false;
						}
					}
					mod.name = test;
					mod.panel.roomName = test;
				}
			}
			return true;
		}
	}
	
	
	
	public void saveGame ()
	{
		try
		{
			JFileChooser fc = new JFileChooser();
			
			fc.showSaveDialog(null);
			if(fc.getSelectedFile() == null)
				return;
			
			while(fc.getSelectedFile().exists())
			{
				if ( JOptionPane.showConfirmDialog(null, "Overwrite existing file?","Warning",JOptionPane.YES_NO_OPTION) == 0 )
					break;
				fc.setSelectedFile(null);
				
				fc.showSaveDialog(null);
				
				if(fc.getSelectedFile() == null)
					return;
			}
			
			FileOutputStream fos = new FileOutputStream(fc.getSelectedFile());
			ObjectOutputStream oos = new ObjectOutputStream(fos);
			
			oos.writeObject(lab);
			
			oos.close();
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
	}
	
	public void loadGame ()
	{
		try
		{
			JFileChooser fc = new JFileChooser();
			
			fc.showOpenDialog(null);
			if(fc.getSelectedFile() == null)
				return;
			
			
			FileInputStream fis = new FileInputStream(fc.getSelectedFile());
			ObjectInputStream ois = new ObjectInputStream(fis);
			
//			lab.setVisible(false);
//			lab = new Labyrinth();
//			lab = (Labyrinth)(ois.readObject());
//			lab.updateUI();
//			lab.validate();
//			JFrame gameFrame = new JFrame();
//			gameFrame.setSize(800,800);
//			gameFrame.setExtendedState(JFrame.MAXIMIZED_BOTH);
//			gameFrame.setVisible(true);
//			gameFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
//			gameFrame.add(lab);
//			lab.setVisible(true);
			
			ois.close();
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
	}
	
	class EditButtonListener implements ActionListener{
		
		public void actionPerformed(ActionEvent e) {
			JButton button = (JButton)e.getSource();
			RoomPanel panel = (RoomPanel) button.getParent();
			String newName = panel.room.getText();
			//RoomModel temp = Controller.findModel(panel.roomName);
			RoomModel temp  = roomModels.get(Controller.findModel(panel.roomName));
			if(Model.nameAvailable(newName, temp, roomModels)){
				if(!finalizeRooms())
					return;
				RoomMenu editMen = new RoomMenu(temp, roomModels);
				editMen.butMainMenu.addActionListener(new MainMenuButtonListener());
				JFrame frame = editMen.mainFrameRm;
				frame.setLocationRelativeTo(panel);
				frame.setVisible(true);
				
			}
			
		}
		private boolean finalizeRooms() {
			for(int i  = 0; i < roomModels.size(); i++){
				RoomModel mod = roomModels.get(i);
				String test = mod.panel.room.getText();
				if (test != mod.name){
					for (int j = 0; j<roomModels.size(); j++){
						
						if (i == j){
							continue;
						}
						
						if(roomModels.get(j).name.equals(test)){
							JOptionPane.showMessageDialog(null, "The room name" + test + " is already in use");
							return false;
						}
					}
					mod.name = test;
					mod.panel.roomName = test;
				}
			}
			return true;
		}
		
	}
	
	
	
	
	//Used to find the corresponding RoomModel from the RoomPanel's
	//roomName
	public static int findModel(String str){
		for (int i = 0; i< roomModels.size(); i++){
			if(str == roomModels.get(i).name)
				return i;
			//return roomModels.get(i);
		}
		return -1;
	}
	
	
	
	/*The following listeners have to deal with connecting a certain
	 * model to a frame displaying it.
	 */
	
	//When user presses color button.
	class ColorChangeListener implements ActionListener{
		public void actionPerformed(ActionEvent e) {
			
			
			JButton button = (JButton)e.getSource();
			
			RoomPanel panel = (RoomPanel) button.getParent();
			RoomModel temp  = roomModels.get(Controller.findModel(panel.roomName));
			//RoomModel temp = Controller.findModel(panel.roomName);
			
			if (temp != null){
				Color col = JColorChooser.showDialog(null, "Choose a color for this room", button.getBackground());
				if (col == null)
					return;
				if ( (col.getRed()+col.getBlue()+col.getGreen() ) < 50 )
					JOptionPane.showMessageDialog(null, "This color will make the game hard to play because it is so dark.", "Warning",JOptionPane.WARNING_MESSAGE);
				
				boolean check = true;
				for (int i = 0; i< roomModels.size(); i++){
					if(roomModels.get(i) != temp && roomModels.get(i).clr.equals(col))
						check = false;
				}
				if(check){
					panel.repickColor(col);
					temp.clr = col;
					
				}
				else{
					JOptionPane.showMessageDialog(null, "That color is already in use");
				}
				
			}
		}
	}
	
	//When user selects and item number from the combobox.
	class ItemNumberListener implements ActionListener{
		
		public void actionPerformed(ActionEvent e) {
			JComboBox box = (JComboBox)e.getSource();
			RoomPanel panel = (RoomPanel) box.getParent();
			//RoomModel temp = Controller.findModel(panel.roomName);
			RoomModel temp  = roomModels.get(Controller.findModel(panel.roomName));
			if (temp != null){
				temp.numItems = (Integer) box.getSelectedItem();
				
			}
		}
	}
	
	
	class MainMenuButtonListener implements ActionListener{
		
		public void actionPerformed(ActionEvent arg0) {
			for (int i  = 0; i<roomModels.size(); i++){
				RoomModel mod = roomModels.get(i);
				
				
				if(Model.hasStart(roomModels))
				{	
					Model.setVisitedFalse(roomModels);
					if(Model.canBeReached(Model.getStartRoom(roomModels),mod))
					{
						if(Model.hasFinish(roomModels))
							mod.panel.information.setText("");
						else
							mod.panel.information.setText("No end position yet");
					}
					else
						mod.panel.information.setText("Not Reachable");
				}
				else
					mod.panel.information.setText("No start position yet");
			}
		}
		
	}
}

