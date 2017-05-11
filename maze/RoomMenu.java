package maze;

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.util.Vector;


public class RoomMenu {
	

	JFrame mainFrameRm;
	RoomModel model;
	
	JPanel bigMama;  
	JPanel optionPan;

	JPanel rmSizePan; 
	JPanel prtlPan;
	JPanel posPan;
	JPanel exitRmPan;


	JLabel labRoomNum;
	JLabel labSizeX;
	JLabel labSizeY;
	JLabel labPrtlPlace;
	JLabel labPickRm;
	JLabel labNeedPrtl;

	JSlider xSlide;
	JSlider ySlide;
	int slideXVal;
	int slideYVal;

	JComboBox comboPickRm;

	JButton butPlace;
	JButton butMainMenu;
	JButton butCancelRm;
	JButton butSaveRm;
	JButton butStart;
	JButton butEnd;

	//list of rooms that need portals
	JTextArea rmList;
	JScrollPane scrollRm;

	Grids rmGrid;
	GridFrame fr;

	Vector<RoomModel> rmModVec;
	static int phase;
	

	public RoomMenu(RoomModel thisMod, Vector<RoomModel> roomMod)
	{
		
		Model.emptyTempPorts(roomMod);
		thisMod.prevXsize = thisMod.sizeX;
		thisMod.prevYsize = thisMod.sizeY;
		phase = 0;
		
		mainFrameRm = new JFrame(thisMod.name);
		model = thisMod;
		rmModVec = roomMod;
		
		labSizeX = new JLabel("Size X: " + model.sizeX);
		labSizeY = new JLabel("Size Y: " + model.sizeY);

		xSlide = new JSlider();
		xSlide.setMinimum(model.currMinX/12);
		xSlide.setMaximum(Controller.MAX_VAL_X);
		xSlide.setMajorTickSpacing(10);
		xSlide.setMinorTickSpacing(2);
//		xSlide.setSnapToTicks(true);
		xSlide.setPaintTicks(true);
		xSlide.setPaintLabels(true);
		xSlide.setValue(model.sizeX);
		slideXVal = model.sizeX;

		ySlide = new JSlider();
		ySlide.setMinimum(model.currMinY/12);
		ySlide.setMaximum(Controller.MAX_VAL_Y);
		ySlide.setMajorTickSpacing(10);
		ySlide.setMinorTickSpacing(2);
//		ySlide.setSnapToTicks(true);
		ySlide.setPaintTicks(true);
		ySlide.setPaintLabels(true);
		ySlide.setValue(model.sizeY);
		slideYVal = model.sizeY;

		rmGrid = new Grids(model, model.sizeX+1, model.sizeY+1);

		xSlide.addChangeListener(new ChangeListener() {
			public void stateChanged(ChangeEvent e) {
				int xVal = xSlide.getValue();
//				if(xVal % 2 == 0) 
//				{
//					xVal = (xVal-prevXVal > 0)?  xVal+1:xVal-1;
//				}
				//Link listener to drag-drop grid//////////////////////////////////////////////////
				//link to setting size of the maze
				labSizeX.setText("Size X: " + xVal);
				model.setSizeX(xVal);
				rmGrid.setCol(xVal);
				slideXVal = xVal;
			}
		});
		ySlide.addChangeListener(new ChangeListener() {
			public void stateChanged(ChangeEvent e) {
				int yVal = ySlide.getValue();
//				if(yVal % 2 == 0) 
//				{
//					yVal = (yVal-prevYVal > 0)?  yVal+1:yVal-1;
//				}
				//Link listener to drag-drop grid///////////////////////////////////////////////////
				//link to setting size of the maze
				labSizeY.setText("Size Y: " + yVal);
				model.setSizeY(yVal);
				rmGrid.setRow(yVal);
				slideYVal = yVal;
			}
		});

		class SliderTimer implements ActionListener {
			public void actionPerformed(ActionEvent e) {
				ySlide.setValue(slideYVal);
				xSlide.setValue(slideXVal);
				model.setSizeX(slideXVal);
				model.setSizeY(slideYVal);
				rmGrid.setCol(slideXVal);
				rmGrid.setRow(slideYVal);
			}
		}

		Timer time = new Timer(100, new SliderTimer());
		time.start();


		labPrtlPlace = new JLabel("Portal Placement");
		labPickRm = new JLabel("Pick Room: ");

		comboPickRm = new JComboBox();
		butPlace = new JButton("Place Portal");
		getOptions();


		
		butPlace.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				//Adds an exit portal from current room to room picked.
				//Brings up new frame with grid of picked room to place entrance portal.
				
				JOptionPane.showMessageDialog(null, "Now click on the grid to place the portal.", 
						"New Portal", JOptionPane.INFORMATION_MESSAGE);
				
				RoomModel toRm = (RoomModel)comboPickRm.getSelectedItem();
				fr = new GridFrame(toRm.name, toRm.sizeX, toRm.sizeY);   
				phase = 1;

			}
		});

		butMainMenu = new JButton("Save & Close");
		butMainMenu.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				Model.emptyTempPorts(rmModVec);
				mainFrameRm.dispose();
			}
		});


		butCancelRm = new JButton("Cancel");
		butCancelRm.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				model.sizeX = model.prevXsize;
				model.sizeY = model.prevYsize;
				xSlide.setValue(model.prevXsize);
				ySlide.setValue(model.prevYsize);
				//remove portals from the model's vector of portals that are in the tempPortals (that have been recently added).
				
				Model.removeNewRooms(rmModVec);
				
				comboPickRm.removeAllItems();
				getOptions();
				
				mainFrameRm.dispose();
			}
		});
		butSaveRm = new JButton("Save");
		/*
		 * This saves the size of the room, if it has start and finish points, and the portals 
		 * that are in it.
		 */
		butSaveRm.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				model.prevXsize = slideXVal; //num of columns
				model.prevYsize = slideYVal; // num of rows
				Model.emptyTempPorts(rmModVec);
				if(Model.hasStart(rmModVec)) {
					model.hadStart = true;
				}
				if(Model.hasFinish(rmModVec)) {
					model.hadFinish = true;
				}
				else {
					model.hadStart = false;
					model.hadFinish = false;
				}
			}
			
		});
		// ^^set sizes later

		labNeedPrtl = new JLabel("Currently Need Portal For: ");

		rmList = new JTextArea();
		rmList.setLineWrap(true);
		rmList.setEditable(false);
		rmList.setOpaque(false);
		////////////////////////////////sets LOOK AT TOMORROW!!!

		scrollRm = new JScrollPane(rmList, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
		updateRooms();


		rmSizePan = new JPanel();

		rmSizePan.setLayout(new BoxLayout(rmSizePan, BoxLayout.Y_AXIS));
		rmSizePan.setBorder(BorderFactory.createLineBorder(Color.black));

		rmSizePan.add(labSizeX);
		rmSizePan.add(xSlide);
		rmSizePan.add(labSizeY);
		rmSizePan.add(ySlide);



		prtlPan = new JPanel();

		prtlPan.setLayout(new BoxLayout(prtlPan, BoxLayout.Y_AXIS));
		prtlPan.setBorder(BorderFactory.createLineBorder(Color.black));
		prtlPan.setPreferredSize(new Dimension(200,10));

		prtlPan.add(labPrtlPlace);
		prtlPan.add(Box.createVerticalGlue());
		prtlPan.add(labPickRm);
		prtlPan.add(Box.createVerticalGlue());
		comboPickRm.setPreferredSize(new Dimension(200,10));
		prtlPan.add(comboPickRm);
		prtlPan.add(butPlace);
		prtlPan.add(Box.createVerticalGlue());
		prtlPan.add(labNeedPrtl);
		prtlPan.add(scrollRm);



		posPan = new JPanel();

		posPan.setLayout(new FlowLayout());
		posPan.setBorder(BorderFactory.createLineBorder(Color.black));
		butStart = new JButton("Place Start Point");
		if(Model.hasStart(rmModVec)) butStart.setText("Clear Start Point");
		butStart.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				if(Model.hasStart(rmModVec) != true) {
					phase = 3;
					butStart.setEnabled(false);
				}
				else {
					for(RoomModel mod: rmModVec)
					{
						mod.start = null;
					}
					butStart.setText("Place Start Point");
				}
			}

		});

		butEnd = new JButton("Place End Point");
		if(Model.hasFinish(rmModVec)) butEnd.setText("Clear End Point");
		butEnd.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				if(Model.hasFinish(rmModVec) != true) {
					phase = 4;
					butEnd.setEnabled(false);
				}
				else {
					for(RoomModel mod: rmModVec)
					{
						mod.finish = null;
					}
					butEnd.setText("Place End Point");
				}
			}

		});

		
		posPan.add(butStart);
		posPan.add(butEnd);

		butEnd.setEnabled(true);
		butStart.setEnabled(true);

		exitRmPan = new JPanel();
		exitRmPan.setSize(100,400);
		exitRmPan.setLayout(new FlowLayout());
		exitRmPan.setBorder(BorderFactory.createLineBorder(Color.black));
		exitRmPan.add(butMainMenu);
		exitRmPan.add(butSaveRm);
		exitRmPan.add(butCancelRm);


		optionPan = new JPanel();
		optionPan.setLayout(new BoxLayout(optionPan, BoxLayout.Y_AXIS));
		optionPan.setBorder(BorderFactory.createLineBorder(Color.black));
		optionPan.add(rmSizePan);
		optionPan.add(prtlPan);
		optionPan.add(posPan);
		optionPan.add(exitRmPan);

		bigMama = new JPanel();
		bigMama.setLayout(new BorderLayout());
		bigMama.add(optionPan, BorderLayout.WEST);
		bigMama.add(rmGrid, BorderLayout.CENTER);

		mainFrameRm.add(bigMama);
		mainFrameRm.setVisible(true);
		mainFrameRm.setSize(1200,550);
		mainFrameRm.setResizable(false);
	}
	
	/**
	 * updates combo-box options for placing a portal.
	 */
	private void getOptions() {
		comboPickRm.setEnabled(true);
		butPlace.setEnabled(true);
		Vector<RoomModel> tempVec = new Vector<RoomModel>();
		//create temporary vector identical to rmModVec
		for(int i = 0; i<rmModVec.size();i++){
			tempVec.add(rmModVec.get(i));
		}
		
		//deletes 
		for(int i  = 0; i< model.portals.size(); i++){
			PortalModel port = model.portals.get(i);
			
			tempVec.remove(port.room2);
			
//			for (int j = 0; j < tempVec.size(); j++) {
//				
//				if(port.room2.name == (rmModVec.get(j)).name) {
//					tempVec.remove(rmModVec.get(j));
//					
//					
//				}
//			}
		}
		tempVec.remove(model);
		if (tempVec.size()==0){
			comboPickRm.setEnabled(false);
			butPlace.setEnabled(false);
		}
		for(int i = 0; i < tempVec.size(); i++) {
			comboPickRm.addItem(tempVec.get(i));
		}
		comboPickRm.repaint();
	}
	
	

	@SuppressWarnings("serial")
	class Grids extends JPanel {

		int width, height, rows, columns;
		Point mouse = new Point(0,0);
		RoomModel gridModel;

		public Grids(RoomModel mod, int r, int c) {

			width = c*12;
			height = r*12;
			gridModel = mod;

			setBorder(BorderFactory.createTitledBorder(""));

			rows = r;
			columns = c;

			Dimension d = new Dimension(Controller.MAX_VAL_X*13,
					Controller.MAX_VAL_Y*13);

			setPreferredSize(d);
			setSize(d);

			this.addMouseMotionListener(new MouseMotion());
			this.addMouseListener(new Mouse());

		}

		protected void paintComponent(Graphics g) {
			super.paintComponent(g);
			int k;
			int htOfRow = 12;
			int wdOfRow = 12;


			if(rows < model.currMinY/12)
				setRow(model.currMinY/12);
			if(columns < model.currMinX/12)
				setCol(model.currMinX/12);
			if(rows >= Controller.MAX_VAL_Y)
				setRow(Controller.MAX_VAL_Y);
			if(columns >= Controller.MAX_VAL_X)
				setCol(Controller.MAX_VAL_X);
			
			g.drawRect(0, 0, Controller.MAX_VAL_X*12, Controller.MAX_VAL_Y*12);
			
			for (k = 0; k <= rows; k++) {
				g.drawLine(0, k * htOfRow , width, k * htOfRow);
			}
			for (k = 0; k <= columns; k++) {
				g.drawLine(k*wdOfRow , 0, k*wdOfRow , height);
			}

			if(phase == 1 || phase == 2) {
				g.fillOval(mouse.x-6, mouse.y-6, 12, 12);
			}

			if(phase == 3) {
				g.setColor(Color.GRAY);
				g.fillRect(mouse.x-6, mouse.y-6, 12, 12);
			}
			if(phase == 4) {
				g.setColor(Color.BLACK);
				g.fillRect(mouse.x-6, mouse.y-6, 12, 12);
			}

			for(PortalModel p: gridModel.portals) {
				g.setColor(p.room2.clr);
				g.fillOval(p.posX1*12, p.posY1*12, 12, 12);
			}

			if(gridModel.start != null) {
				g.setColor(Color.GRAY);
				g.fillRect(gridModel.start.x*12, gridModel.start.y*12, 12, 12);
			}

			if(gridModel.finish != null) {
				g.setColor(Color.BLACK);
				g.fillRect(gridModel.finish.x*12, gridModel.finish.y*12, 12, 12);
			}
			
			
			g.setColor(Color.black);
			g.setFont(new Font("Georgia",Font.PLAIN,18));
			g.drawString("(You can also drag the grid around to adjust size.)", 24, Controller.MAX_VAL_Y*12+36);
		}

		public void update(Graphics g) {
			paint(g);
		}

		public void setCol(int c){
			columns = c;
			width = c*12;
			repaint();

		}

		public void setRow(int r) {
			rows = r;
			height = r*12;
			repaint();
		}

		class MouseMotion implements MouseMotionListener {

			//This listener resizes the grid with drag-drop capabilities.
			public void mouseDragged(MouseEvent e) {

				if(RoomMenu.phase == 0) {

					mouse = new Point(e.getX(), e.getY());
					
					if(mouse.x > model.currMinX)
						slideXVal = mouse.x/12;
					else
						slideXVal = model.currMinX/12;
					

					if(mouse.y > model.currMinY)
						slideYVal = mouse.y/12;
					else
						slideYVal = model.currMinY/12;

					setRow(e.getY() / 12);
					setCol(e.getX() / 12);

//					repaint();
				}
				else {
					//Do nothing
				}
			}

			public void mouseMoved(MouseEvent e) {
				mouse = new Point(e.getX(), e.getY());
//				repaint();
			}
		}


		class Mouse implements MouseListener {

			/**
			 * phase=0: the button to place a portal has not been pressed.
			 * phase=1: need to place 1st portal in current room
			 * phase=2: need to place 2nd portal in frame that pops up
			 * phase=3: start position button has been pressed. Place start pos.
			 * phase=4: end position button has been pressed. Place end pos.
			 */
			public void mouseClicked(MouseEvent e) {
				
				if(phase == 1 || phase == 2 || phase == 3 || phase == 4) {
					if(mouse.x < 0 || mouse.y < 0 || mouse.x >= width || mouse.y >= height)
						return;
					
					if(phase == 1) {
						PortalModel port1 = new PortalModel();
						port1.room1 = model;
						port1.posX1 = mouse.x/12;
						port1.posY1 = mouse.y/12;
						model.tempPorts.add(port1); //adds the new portal to the vector tracking changes made.
						
						if(mouse.x>model.currMinX){
							model.currMinX = mouse.x;
							xSlide.setMinimum(model.currMinX/12);
						}
						
						if(mouse.y>model.currMinY){
							model.currMinY = mouse.y;
							ySlide.setMinimum(model.currMinY/12);
						}
						
						port1.room2 = (RoomModel)comboPickRm.getSelectedItem();

						model.portals.add(port1);
						fr.setVisible(true);
						phase = 2;

						JOptionPane.showMessageDialog(null, "Now place the other side of the portal in "+port1.room2.name);
					}
					else if(phase == 2) {
						
						RoomModel tempMod = (RoomModel)comboPickRm.getSelectedItem();
						
						PortalModel port2 = new PortalModel();
						port2.room1 = tempMod;
						port2.posX1 = mouse.x/12;
						port2.posY1 = mouse.y/12;
						
						if(mouse.x>tempMod.currMinX)
							tempMod.currMinX = mouse.x;
						if(mouse.y>tempMod.currMinY)
							tempMod.currMinY = mouse.y;
						
						port2.room2 = model;

						tempMod.portals.add(port2);
						
						tempMod.tempPorts.add(port2); //adds the new portal to the vector tracking changes. 
						
						updateRooms();
						comboPickRm.removeAllItems();
						getOptions();
						
						fr.setVisible(false);
						phase = 0;
					}
					else if(phase == 3) {
						model.start = new Point(e.getX()/12, e.getY()/12);
						if(mouse.x>model.currMinX){
							model.currMinX = mouse.x;
							xSlide.setMinimum(model.currMinX/12);
						}
						
						if(mouse.y>model.currMinY){
							model.currMinY = mouse.y;
							ySlide.setMinimum(model.currMinY/12);
						}

						butStart.setText("Clear Start Point");
						butStart.setEnabled(true);
						phase = 0;
					}
					else if(phase == 4) {
						model.finish = new Point(e.getX()/12, e.getY()/12);
						if(mouse.x>model.currMinX){
							model.currMinX = mouse.x;
							xSlide.setMinimum(model.currMinX/12);
						}
						
						if(mouse.y>model.currMinY){
							model.currMinY = mouse.y;
							ySlide.setMinimum(model.currMinY/12);
						}
						
						butEnd.setText("Clear End Point");
						butEnd.setEnabled(true);
						phase=0;
					}
				}

			}

			public void mouseEntered(MouseEvent arg0) {

			}

			public void mouseExited(MouseEvent arg0) {
			}

			public void mousePressed(MouseEvent arg0) {
			}

			public void mouseReleased(MouseEvent arg0) {
			}

		}
	}

	public class GridFrame extends JFrame {

		Grids grid;       

		GridFrame(String title, int rows, int columns) {
			setTitle(title);
			RoomModel toMod = (RoomModel)comboPickRm.getSelectedItem();
			grid = new Grids(toMod, columns, rows);
			add(grid);

			setSize(grid.width+30, grid.height+30);
			setResizable(false);
			setDefaultCloseOperation(DISPOSE_ON_CLOSE);
			setLocationRelativeTo(null);

		}

		public void setCol(int c) {
			grid.setCol(c);
			//grid.invalidate();
			setSize(grid.width, grid.height);
			repaint();
		}

		public void setRow(int r) {
			grid.setRow(r);
			//grid.invalidate();
			setSize(grid.width, grid.height);
			repaint();

		}

	}
	
	/**
	 * updates the names of the rooms
	 */
	public void updateRooms(){
		
		String s = "";
		for (int i  = 0; i<rmModVec.size(); i++){
			RoomModel mod = rmModVec.get(i);

			Model.setVisitedFalse(rmModVec);
			if(!Model.canBeReached(model , mod)&&mod!=model){
				s +=  mod.name + ", ";
			} 	
			rmList.setText(s);
		}
	}
}
