/*
 * To do: 
 * 
 * -implement waves
 * -implement display for info
 * 
 */


package tower_defense;

import java.awt.*;
import java.awt.event.*;

import javax.swing.*;

@SuppressWarnings("serial")
public class Main extends JFrame
{
//	int frame;
	int s;
	boolean showGrid;

	int maxX;
	int maxY;

//	int offx;
//	int offy;


	Tower T[] = new Tower[100];
	int towers;

	Unit U[] = new Unit[100];
	int units;



	int level;	// EDIT
	int money;	// EDIT

	Point Start;
	Point Finish[] = new Point[4];
	int finishes;

	boolean Path[][];


	int game_phase;
	int edit_mode;


	Rectangle start_button;
	Rectangle B[] = new Rectangle[36];


	int current_unit;

	int selected_button;
	int selected_tower;

	Point m;

	Point hit[] = new Point [100];
	int hits;

	boolean fire[] = new boolean [100];
	int frame_tower[] = new int [100];
	int frame_unit;

	int energy;

	void createVariables ()
	{
//		showGrid = true;

		maxX = 40;
		maxY = 20;

		int tempx = getToolkit().getScreenSize().height/maxY + 1 - 8;
		int tempy = getToolkit().getScreenSize().width/maxX + 1 - 8;

		if (tempx > tempy)
			s = tempy;
		else
			s = tempx;

		s = 26;


		Path = new boolean [maxX][maxY];

		towers = 0;
		units = 0;

		level = 1;
		money = 50;
		energy = 5*level;

		Start = new Point (0,0);


		game_phase = 1; // EDIT

		start_button = new Rectangle (maxX*s-2*s,maxY*s,2*s,s);

		for (int x = 0; x < 6; x++)
			for (int y = 0; y < 6; y++)
			{
				B[x+y*6] = new Rectangle (x*s + maxX*s-9*s,y*s + maxY*s,s,s);
			}

		selected_button = -1;
		selected_tower = -1;

		hits = 0;

	}










	class Mouse implements MouseListener
	{
		public void mousePressed (MouseEvent e)
		{

			/* INSIDE GRID */

			if (m.x>=0 && m.y >=0 && m.x < Path.length*s && m.y < Path[0].length*s)
			{
				Point gm = new Point (m.x/s,m.y/s); // grid mouse



				if (e.getButton() == MouseEvent.BUTTON1)
				{
					if (selected_button > -1)
					{
						if (towers < energy)
						{
							boolean stop = false;
							for (int t = 0; t < towers; t++)
								if (T[t].getX() == gm.x && T[t].getY() == gm.y)
									stop = true;
							if (Path[gm.x][gm.y])
								stop = true;
							if(!stop)
							{
								if (money >= Tower.getCost(selected_button))
								{
									money -= Tower.getCost(selected_button);

									T[towers] = new Tower (selected_button,gm.x,gm.y);
									towers++;
								}
								else
									selected_button = -1;
							}
						}
					}
					else
					{
						for (int t = 0; t < towers; t++)
							if (T[t].getX() == gm.x && T[t].getY() == gm.y)
							{
								selected_tower = t;
								break;
							}
					}
				}

				// EDIT modes

				if (edit_mode == 1)
				{
					if (e.getButton() == MouseEvent.BUTTON1)
					{
						Path[gm.x][gm.y] = true;
					}
					else if (e.getButton() == MouseEvent.BUTTON3)
					{
						Path[gm.x][gm.y] = false;
					}
				}
				else if (edit_mode == 2)
				{
					Start = new Point (gm.x,gm.y);
				}
				else if (edit_mode == 3)
				{
					if (e.getButton() == MouseEvent.BUTTON1)
					{
						if (finishes < Finish.length)
						{
							Finish[finishes] = new Point(gm.x,gm.y);
							finishes++;
						}
					}
					else if (e.getButton() == MouseEvent.BUTTON3)
					{
						finishes--;

						if (finishes < 0)
							finishes = 0;
					}
				}
			}

			/* OUTSIDE */

			else
			{
				if (e.getButton() == MouseEvent.BUTTON1)
				{
					int button = -1;
					for (int b = 0; b < B.length; b++)
						if (checkButton(B[b],m))
						{
							button = b;
						}

//					selected_button = button;
					if (button < 6)
						selected_button = button;
					else if (button == 35)
					{
						if (selected_tower > -1)
						{
							if (money >= T[selected_tower].getLevel()*10)
							{
								money -=T[selected_tower].getLevel()*10;
								T[selected_tower].upgrade();
							}
						}
					}
					else
						selected_button = -1;

					if (game_phase == 1)
					{
						if (checkButton(start_button, m))
						{
							game_phase = 2;
						}
					}
				}
			}


			if (e.getButton() == MouseEvent.BUTTON3)
			{
				selected_button = -1;
				selected_tower = -1;
			}
		}
		public void mouseReleased (MouseEvent e){}
		public void mouseEntered (MouseEvent e){}
		public void mouseExited (MouseEvent e){}
		public void mouseClicked (MouseEvent e){}
	}

	public class MouseMotion implements MouseMotionListener
	{
		public void mouseDragged (MouseEvent e){}
		public void mouseMoved (MouseEvent e)
		{
			m = new Point(e.getX(),e.getY());
		}
	}





	class Keyboard implements KeyListener
	{
		public void keyPressed (KeyEvent e)
		{
			char key = ' ';
			try{
				key = e.getKeyChar();
			}catch(Exception error){}


//			if (e.getKeyCode() == KeyEvent.VK_UP)
//			maxY--;
//			if (e.getKeyCode() == KeyEvent.VK_DOWN)
//			maxY++;
//			if (e.getKeyCode() == KeyEvent.VK_RIGHT)
//			maxX++;
//			if (e.getKeyCode() == KeyEvent.VK_LEFT)
//			maxX--;




			if (key == 'p')
			{
				System.out.println("Start = new Point("+Start.x+", "+Start.y+");");
				for (int f = 0; f < finishes; f++)
					System.out.println("Finish["+f+"] = new Point("+Finish[f].x+", "+Finish[f].y+");");
				System.out.println("finishes = "+finishes+";");
				System.out.println();

				for (int x = 0; x < maxX; x++)
					for (int y = 0; y < maxY; y++)
						if (Path[x][y])
						{
							System.out.println("Path["+x+"]["+y+"] = true;");
						}
			}
			else if (key == 'l')
			{
				int sum = 0;
				for (int x = 0; x < maxX; x++)
					for (int y = 0; y < maxY; y++)
						if (Path[x][y])
							sum++;
				System.out.println("Total Paths: "+sum);
			}

			if (key == '+')
			{
				s++;
			}
			if (key == '-')
			{
				s--;
			}
			if (key == 's')
				System.out.println(s);
			if (key == 't')
				System.out.println(towers);
			if(key == 'h')
			{

				double distance = ( Math.abs( Math.sqrt( Math.pow( T[0].getX() - U[0].getX(), 2) + 
						Math.pow( T[0].getY() - U[0].getY(), 2))));

				System.out.println(distance);
				System.out.println("Tower: "+T[0].getX()+"   "+T[0].getY());
				System.out.println("Unit:  "+U[0].getX()+"   "+U[0].getY());
			}
		}
		public void keyReleased (KeyEvent e){}
		public void keyTyped (KeyEvent e){}
	}










	class PaintPanel extends JPanel
	{
		public void paintComponent (Graphics g)
		{
			super.paintComponent(g);
			setBackground(Color.black);


			if (showGrid)
			{
				g.setColor(Color.white);
				for (int x = 0; x <= maxX; x++)
				{
					g.drawLine(x*s, 0, x*s,maxY*s);
					g.drawString(x+"",x*s,s);
				}
				for (int y = 0; y <= maxY; y++)
				{
					g.drawLine(0, y*s,maxX*s,y*s);
					g.drawString(y+"",0,y*s+s);
				}
			}





			// PATHS

//			g.setColor(Color.blue);
			g.setColor(new Color(0,0,80));
			for (int x = 0; x < maxX; x++)
			{
				for (int y = 0; y < maxY; y++)
				{
					if (Path[x][y])
					{
						g.setColor(new Color(0,0,80));
						g.fillRect(x*s,y*s,s,s);
						g.setColor(new Color(0,0,0));
						g.drawRect(x*s,y*s,s,s);
					}
				}
			}

			// TOWERS

			for (int t = 0; t < towers; t ++)
			{
				T[t].draw(g,s);
			}

			// UNITS

			for (int u = 0; u < units; u ++)
			{
				if (!U[u].isDead())
					U[u].draw(g,s);
			}

			// BUTTONS

			g.setColor(Color.green);
			for (int b = 0; b < B.length; b++)
			{
				g.drawRect(B[b].x, B[b].y, B[b].width, B[b].height);
				
				if (b < 3)
					Tower.draw(g, s, b, B[b].x, B[b].y, false);

				g.setFont(new Font(null,Font.PLAIN,s));
				if (b == 35)
					g.drawString("U", B[b].x+5, B[b].y+s);
			}

			// START BUTTON

			g.setColor(Color.blue);
			g.fillRect(start_button.x, start_button.y, start_button.width, start_button.height);
			g.setColor(Color.black);
			g.setFont(new Font(null,Font.PLAIN,s));
			g.drawString("start",start_button.x, start_button.y+s);

			

			// LEVEL
			
			g.setColor(Color.green);
			g.setFont(new Font(null,Font.BOLD,s));
			g.drawString(""+level,maxX*s-2*s,maxY*s+3*s);
			
			
			
			// MONEY

			g.drawString("$ "+money,maxX*s-2*s,maxY*s+5*s);

			
			
			
			
			// START

//			g.setColor(Color.blue);
			g.setColor(new Color(0,0,255));
			g.fillRect(Start.x*s, Start.y*s, s, s);
			g.setColor(Color.black);
			g.drawString("S",Start.x*s, Start.y*s+s);

			// FINISHES

			for (int f = 0; f < finishes; f++)
			{
				g.setColor(Color.orange);
				g.fillRect(Finish[f].x*s, Finish[f].y*s, s, s);
				g.setColor(Color.black);
				g.drawString("E",Finish[f].x*s, Finish[f].y*s+s);
			}
			
			
			
			
			
			// TOWER SHOOTING

			g.setColor(Color.white);
			for (int h = 0; h < hits; h++)
			{
				g.drawLine(T[hit[h].x].getX()*s+s/2, T[hit[h].x].getY()*s+s/2,
						U[hit[h].y].getX()*s+s/2, U[hit[h].y].getY()*s+s/2);

				g.drawLine(T[hit[h].x].getX()*s+s/2+1 ,T[hit[h].x].getY()*s+s/2+1,
						U[hit[h].y].getX()*s+s/2, U[hit[h].y].getY()*s+s/2);
				g.drawLine(T[hit[h].x].getX()*s+s/2-1 ,T[hit[h].x].getY()*s+s/2+1,
						U[hit[h].y].getX()*s+s/2, U[hit[h].y].getY()*s+s/2);
				g.drawLine(T[hit[h].x].getX()*s+s/2-1 ,T[hit[h].x].getY()*s+s/2-1,
						U[hit[h].y].getX()*s+s/2, U[hit[h].y].getY()*s+s/2);
				g.drawLine(T[hit[h].x].getX()*s+s/2+1 ,T[hit[h].x].getY()*s+s/2-1,
						U[hit[h].y].getX()*s+s/2, U[hit[h].y].getY()*s+s/2);

			}
			hits = 0;






			// SELECTION

			if (selected_button > -1)
			{
				Tower.draw(g,s,selected_button,m.x,m.y, true);

				g.setColor(Color.white);
				g.drawOval((int)(m.x-(Tower.getRange(selected_button)*2+1)/2.*s),
						(int)(m.y-(Tower.getRange(selected_button)*2+1)/2.*s),
						(Tower.getRange(selected_button)*2+1)*s,(Tower.getRange(selected_button)*2+1)*s);
			}
			if (selected_tower > -1)
			{
				int range = T[selected_tower].getRange(); 
				g.setColor(Color.white);
				g.drawOval((int)(T[selected_tower].getX()*s-(range*2+1)/2.*s+s/2),
						(int)(T[selected_tower].getY()*s-(range*2+1)/2.*s+s/2),(range*2+1)*s,(range*2+1)*s);


				g.setColor(Color.white);

				for (int i = 0; i < 6; i++)
					g.drawString(""+T[selected_tower].getName().charAt(i), s, maxY*s + 2*s + i*s);

				g.setColor(Color.white);

				g.drawString("L", 								s*3,	maxY*s + 2*s);
				g.drawString(""+T[selected_tower].getLevel(), 	s*5,	maxY*s + 2*s);

				g.drawString("R", 								s*3,	maxY*s + 4*s);
				g.drawString(""+T[selected_tower].getRange(), 	s*5,	maxY*s + 4*s);

				g.drawString("S", 								s*3,	maxY*s + 5*s);
				g.drawString(""+T[selected_tower].getSpeed(), 	s*5,	maxY*s + 5*s);

				g.drawString("P", 								s*3,	maxY*s + 6*s);
				g.drawString(""+T[selected_tower].getPower(), 	s*5,	maxY*s + 6*s);


				g.setColor(Color.cyan);

//				g.drawString("L", 								s*3,	maxY*s + 2*s);
				g.drawString(""+(T[selected_tower].getLevel()+1), 	s*7,	maxY*s + 2*s);

//				g.drawString("R", 								s*3,	maxY*s + 4*s);
				g.drawString(""+T[selected_tower].getNextRange(), 	s*7,	maxY*s + 4*s);

//				g.drawString("S", 								s*3,	maxY*s + 5*s);
				g.drawString(""+T[selected_tower].getNextSpeed(), 	s*7,	maxY*s + 5*s);

//				g.drawString("P", 								s*3,	maxY*s + 6*s);
				g.drawString(""+T[selected_tower].getNextPower(), 	s*7,	maxY*s + 6*s);

			}
		}
	}
















	class Game implements ActionListener
	{
		public void actionPerformed (ActionEvent e)
		{

			if (game_phase == 1)
			{
			}
			else if (game_phase == 2)
			{
				if (level == 1)
				{
					units = 10;

					for (int u = 0; u < units; u++)
						U[u] = new Unit (0,Start.x,Start.y);
				}
				else if (level == 2)
				{
					units = 20;

					for (int u = 0; u < units; u++)
						U[u] = new Unit (0,Start.x,Start.y);
				}
				else if (level == 3)
				{
					units = 10;

					for (int u = 0; u < units; u++)
						U[u] = new Unit (1,Start.x,Start.y);
				}
				else if (level == 4)
				{
					units = 10;

					for (int u = 0; u < units; u++)
						U[u] = new Unit (2,Start.x,Start.y);
				}
				
				
				
				
				else
				{
					units = 20;

					for (int u = 0; u < 10; u++)
						U[u] = new Unit (1,Start.x,Start.y);

					for (int u = 10; u < units; u++)
						U[u] = new Unit (2,Start.x,Start.y);
				}


				for (int t = 0; t < towers; t++)
				{
					fire[t] = false;
					frame_tower[t] = 0;
				}
				current_unit = 1;
				game_phase = 3;
			}
			else if (game_phase == 3)
			{
				for (int t = 0; t < towers; t++)
				{
					if (fire[t])
						frame_tower[t]++;

					boolean dont_fire = true;

					for (int u = 0; u < current_unit; u++)
					{
						if (!U[u].isDead())
							if (!(U[u].getX() == Start.x && U[u].getY() == Start.y))
							{
								double distance = ( Math.abs( Math.sqrt( Math.pow( T[t].getX() - U[u].getX(), 2) + 
										Math.pow( T[t].getY() - U[u].getY(), 2))));

								if (distance <= T[t].getRange() + .5)
								{
									dont_fire = false;
									if (frame_tower[t]%(50-T[t].getSpeed()) == 0)
									{
										hit[hits] = new Point (t,u);
										hits++;

										U[u].setDamage(T[t].getPower());

										if (U[u].isDead())
										{
											money += U[u].getMoney();
//											units--;
//											current_unit--;
										}

										fire[t] = true;

										break;
									}
									fire[t] = true;
								}								
							}
					}
					if (dont_fire)
						if (frame_tower[t]%(50-T[t].getSpeed()) == 0)
						{
							fire[t] = false;
							frame_tower[t] = 0;
						}
				}



				frame_unit++;

				for (int u = 0; u < current_unit; u++)
				{
					if (frame_unit%(10-U[u].getSpeed()) == 0)
					{
						U[u].move(Path);

						for (int f = 0; f < finishes; f++)
							if (U[u].getX() == Finish[f].x && U[u].getY() == Finish[f].y)
								U[u].setDead(); // EDIT
					}
				}
				if (current_unit >= units) // EDIT
				{
//					System.out.println("Win!!!!!!!!!!!");
					boolean complete = true;
					for (int u = 0; u < units; u++)
						if (!U[u].isDead())
							complete = false;
					if (complete)
					{
						game_phase = 1;
						level++;
					}
				}
				else 
				{
//					System.out.println(current_unit+"    "+units);
					if (frame_unit%((10-U[current_unit-1].getSpeed())*U[current_unit-1].getSkip()) == 0)
						if (current_unit < units)
						{
//							U[current_unit] = new Unit (0,Start.x,Start.y);
							current_unit++;
						}
				}
//				System.out.println(current_unit);

			}
		}
	}














	boolean checkButton (Rectangle b, Point m)
	{
//		if (m.x>b.x && m.x<b.x+b.width && m.y>b.y && m.y<b.y+b.height)
		if(b.contains(m))  // im an idiot ^
			return true;
		return false;
	}


	class Paint implements ActionListener
	{
		public void actionPerformed (ActionEvent e)
		{
			repaint();
		}
	}


	public Main ()
	{
		createMenus();
		createVariables();
		createMap();

		Timer game = new Timer (50, new Game());
		game.start();
		Timer paint = new Timer (100, new Paint());
		paint.start();

		PaintPanel panel = new PaintPanel();
		Container cp = getContentPane();
		cp.addMouseListener(new Mouse());
		cp.addMouseMotionListener(new MouseMotion());
		cp.addKeyListener(new Keyboard());
		cp.add(panel);
		panel.setVisible(true);
		cp.setFocusable(true);
	}
	public void createMenus()
	{
		/* FILE menu */

		JMenu file = new JMenu("File");

		JMenuItem exit = new JMenuItem("Exit");

		class Exit implements ActionListener{
			public void actionPerformed(ActionEvent e)
			{System.exit(0);}}

		exit.addActionListener(new Exit());
		file.add(exit);


		/* EDIT menu */

		JMenu edit = new JMenu("Edit");


		JMenuItem grid = new JMenuItem("Grid");
		class Grid implements ActionListener{
			public void actionPerformed(ActionEvent e)
			{if(showGrid)showGrid = false;else showGrid = true;}}
		grid.addActionListener(new Grid());
		edit.add(grid);


		JMenuItem path = new JMenuItem("Path");
		class Path implements ActionListener{
			public void actionPerformed(ActionEvent e)
			{edit_mode = 1;}}
		path.addActionListener(new Path());
		edit.add(path);


		JMenuItem start = new JMenuItem("Start");
		class Start implements ActionListener{
			public void actionPerformed(ActionEvent e)
			{edit_mode = 2;}}
		start.addActionListener(new Start());
		edit.add(start);


		JMenuItem finish = new JMenuItem("Finish");
		class Finish implements ActionListener{
			public void actionPerformed(ActionEvent e)
			{edit_mode = 3;}}
		finish.addActionListener(new Finish());
		edit.add(finish);



		JMenuBar menuBar = new JMenuBar();
		setJMenuBar(menuBar);
		menuBar.add(file);
		menuBar.add(edit);
	}
	public static void main (String[] Bridget)
	{
		Main frame = new Main();
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setTitle("");
		frame.setSize(1100, 750);
		frame.setLocation(0,0);
		frame.setVisible(true);
	}
	void createMap ()
	{
		int z = -1;

		try
		{
			z = JOptionPane.showOptionDialog(null, "Select difficulty:", "Map Selection", 0, 
					JOptionPane.QUESTION_MESSAGE, null, new String[]{"Easy","Normal","Hard"}, null);
		}
		catch(Exception error){}

		if (z == 0) // easy
		{
			z = -1;
			try
			{
				z = JOptionPane.showOptionDialog(null, "Select map:", "Map Selection", 0, 
						JOptionPane.QUESTION_MESSAGE, null, new String[]{"Spiral","Loop","Distance"}, null);
			}
			catch(Exception error){}


			if (z == 0) // spiral
			{
				Start = new Point(19, 10);
				Finish[0] = new Point(8, 2);
				finishes = 1;

				Path[8][2] = true;
				Path[8][3] = true;
				Path[8][4] = true;
				Path[8][5] = true;
				Path[8][6] = true;
				Path[8][7] = true;
				Path[8][8] = true;
				Path[8][9] = true;
				Path[8][10] = true;
				Path[8][11] = true;
				Path[8][12] = true;
				Path[8][13] = true;
				Path[8][14] = true;
				Path[8][15] = true;
				Path[8][16] = true;
				Path[8][17] = true;
				Path[8][18] = true;
				Path[9][18] = true;
				Path[10][2] = true;
				Path[10][3] = true;
				Path[10][4] = true;
				Path[10][5] = true;
				Path[10][6] = true;
				Path[10][7] = true;
				Path[10][8] = true;
				Path[10][9] = true;
				Path[10][10] = true;
				Path[10][11] = true;
				Path[10][12] = true;
				Path[10][13] = true;
				Path[10][14] = true;
				Path[10][15] = true;
				Path[10][16] = true;
				Path[10][18] = true;
				Path[11][2] = true;
				Path[11][16] = true;
				Path[11][18] = true;
				Path[12][2] = true;
				Path[12][4] = true;
				Path[12][5] = true;
				Path[12][6] = true;
				Path[12][7] = true;
				Path[12][8] = true;
				Path[12][9] = true;
				Path[12][10] = true;
				Path[12][11] = true;
				Path[12][12] = true;
				Path[12][13] = true;
				Path[12][14] = true;
				Path[12][16] = true;
				Path[12][18] = true;
				Path[13][2] = true;
				Path[13][4] = true;
				Path[13][14] = true;
				Path[13][16] = true;
				Path[13][18] = true;
				Path[14][2] = true;
				Path[14][4] = true;
				Path[14][6] = true;
				Path[14][7] = true;
				Path[14][8] = true;
				Path[14][9] = true;
				Path[14][10] = true;
				Path[14][11] = true;
				Path[14][12] = true;
				Path[14][14] = true;
				Path[14][16] = true;
				Path[14][18] = true;
				Path[15][2] = true;
				Path[15][4] = true;
				Path[15][6] = true;
				Path[15][12] = true;
				Path[15][14] = true;
				Path[15][16] = true;
				Path[15][18] = true;
				Path[16][2] = true;
				Path[16][4] = true;
				Path[16][6] = true;
				Path[16][8] = true;
				Path[16][9] = true;
				Path[16][10] = true;
				Path[16][12] = true;
				Path[16][14] = true;
				Path[16][16] = true;
				Path[16][18] = true;
				Path[17][2] = true;
				Path[17][4] = true;
				Path[17][6] = true;
				Path[17][8] = true;
				Path[17][10] = true;
				Path[17][12] = true;
				Path[17][14] = true;
				Path[17][16] = true;
				Path[17][18] = true;
				Path[18][2] = true;
				Path[18][4] = true;
				Path[18][6] = true;
				Path[18][8] = true;
				Path[18][10] = true;
				Path[18][12] = true;
				Path[18][14] = true;
				Path[18][16] = true;
				Path[18][18] = true;
				Path[19][2] = true;
				Path[19][4] = true;
				Path[19][6] = true;
				Path[19][8] = true;
				Path[19][12] = true;
				Path[19][14] = true;
				Path[19][16] = true;
				Path[19][18] = true;
				Path[20][2] = true;
				Path[20][4] = true;
				Path[20][6] = true;
				Path[20][8] = true;
				Path[20][12] = true;
				Path[20][14] = true;
				Path[20][16] = true;
				Path[20][18] = true;
				Path[21][2] = true;
				Path[21][4] = true;
				Path[21][6] = true;
				Path[21][8] = true;
				Path[21][9] = true;
				Path[21][10] = true;
				Path[21][11] = true;
				Path[21][12] = true;
				Path[21][14] = true;
				Path[21][16] = true;
				Path[21][18] = true;
				Path[22][2] = true;
				Path[22][4] = true;
				Path[22][6] = true;
				Path[22][14] = true;
				Path[22][16] = true;
				Path[22][18] = true;
				Path[23][2] = true;
				Path[23][4] = true;
				Path[23][6] = true;
				Path[23][7] = true;
				Path[23][8] = true;
				Path[23][9] = true;
				Path[23][10] = true;
				Path[23][11] = true;
				Path[23][12] = true;
				Path[23][13] = true;
				Path[23][14] = true;
				Path[23][16] = true;
				Path[23][18] = true;
				Path[24][2] = true;
				Path[24][4] = true;
				Path[24][16] = true;
				Path[24][18] = true;
				Path[25][2] = true;
				Path[25][4] = true;
				Path[25][5] = true;
				Path[25][6] = true;
				Path[25][7] = true;
				Path[25][8] = true;
				Path[25][9] = true;
				Path[25][10] = true;
				Path[25][11] = true;
				Path[25][12] = true;
				Path[25][13] = true;
				Path[25][14] = true;
				Path[25][15] = true;
				Path[25][16] = true;
				Path[25][18] = true;
				Path[26][2] = true;
				Path[26][18] = true;
				Path[27][2] = true;
				Path[27][3] = true;
				Path[27][4] = true;
				Path[27][5] = true;
				Path[27][6] = true;
				Path[27][7] = true;
				Path[27][8] = true;
				Path[27][9] = true;
				Path[27][10] = true;
				Path[27][11] = true;
				Path[27][12] = true;
				Path[27][13] = true;
				Path[27][14] = true;
				Path[27][15] = true;
				Path[27][16] = true;
				Path[27][17] = true;
				Path[27][18] = true;
			}
			else if (z == 1) // loop
			{
				Start = new Point(18, 15);
				Finish[0] = new Point(20, 15);
				finishes = 1;

				Path[2][3] = true;
				Path[2][4] = true;
				Path[2][5] = true;
				Path[2][6] = true;
				Path[2][7] = true;
				Path[2][8] = true;
				Path[2][9] = true;
				Path[2][10] = true;
				Path[2][11] = true;
				Path[2][12] = true;
				Path[2][13] = true;
				Path[2][14] = true;
				Path[2][15] = true;
				Path[3][3] = true;
				Path[3][15] = true;
				Path[4][3] = true;
				Path[4][15] = true;
				Path[5][3] = true;
				Path[5][15] = true;
				Path[6][3] = true;
				Path[6][5] = true;
				Path[6][6] = true;
				Path[6][7] = true;
				Path[6][10] = true;
				Path[6][11] = true;
				Path[6][12] = true;
				Path[6][13] = true;
				Path[6][14] = true;
				Path[6][15] = true;
				Path[7][3] = true;
				Path[7][5] = true;
				Path[7][7] = true;
				Path[7][10] = true;
				Path[8][3] = true;
				Path[8][5] = true;
				Path[8][7] = true;
				Path[8][8] = true;
				Path[8][9] = true;
				Path[8][10] = true;
				Path[8][12] = true;
				Path[8][13] = true;
				Path[8][14] = true;
				Path[8][15] = true;
				Path[9][3] = true;
				Path[9][5] = true;
				Path[9][12] = true;
				Path[9][15] = true;
				Path[10][3] = true;
				Path[10][5] = true;
				Path[10][6] = true;
				Path[10][7] = true;
				Path[10][8] = true;
				Path[10][9] = true;
				Path[10][10] = true;
				Path[10][11] = true;
				Path[10][12] = true;
				Path[10][15] = true;
				Path[11][3] = true;
				Path[11][15] = true;
				Path[12][3] = true;
				Path[12][6] = true;
				Path[12][7] = true;
				Path[12][8] = true;
				Path[12][10] = true;
				Path[12][11] = true;
				Path[12][12] = true;
				Path[12][15] = true;
				Path[13][3] = true;
				Path[13][6] = true;
				Path[13][8] = true;
				Path[13][10] = true;
				Path[13][12] = true;
				Path[13][15] = true;
				Path[14][3] = true;
				Path[14][6] = true;
				Path[14][8] = true;
				Path[14][10] = true;
				Path[14][12] = true;
				Path[14][13] = true;
				Path[14][14] = true;
				Path[14][15] = true;
				Path[15][3] = true;
				Path[15][6] = true;
				Path[15][8] = true;
				Path[15][10] = true;
				Path[16][3] = true;
				Path[16][4] = true;
				Path[16][5] = true;
				Path[16][6] = true;
				Path[16][8] = true;
				Path[16][10] = true;
				Path[17][8] = true;
				Path[17][10] = true;
				Path[18][3] = true;
				Path[18][4] = true;
				Path[18][5] = true;
				Path[18][6] = true;
				Path[18][7] = true;
				Path[18][8] = true;
				Path[18][10] = true;
				Path[18][11] = true;
				Path[18][12] = true;
				Path[18][13] = true;
				Path[18][14] = true;
				Path[19][3] = true;
				Path[20][3] = true;
				Path[20][4] = true;
				Path[20][5] = true;
				Path[20][6] = true;
				Path[20][7] = true;
				Path[20][8] = true;
				Path[20][10] = true;
				Path[20][11] = true;
				Path[20][12] = true;
				Path[20][13] = true;
				Path[20][14] = true;
				Path[20][15] = true;
				Path[21][8] = true;
				Path[21][10] = true;
				Path[22][3] = true;
				Path[22][4] = true;
				Path[22][5] = true;
				Path[22][6] = true;
				Path[22][8] = true;
				Path[22][10] = true;
				Path[23][3] = true;
				Path[23][6] = true;
				Path[23][8] = true;
				Path[23][10] = true;
				Path[24][3] = true;
				Path[24][6] = true;
				Path[24][8] = true;
				Path[24][10] = true;
				Path[24][12] = true;
				Path[24][13] = true;
				Path[24][14] = true;
				Path[24][15] = true;
				Path[25][3] = true;
				Path[25][6] = true;
				Path[25][8] = true;
				Path[25][10] = true;
				Path[25][12] = true;
				Path[25][15] = true;
				Path[26][3] = true;
				Path[26][6] = true;
				Path[26][7] = true;
				Path[26][8] = true;
				Path[26][10] = true;
				Path[26][11] = true;
				Path[26][12] = true;
				Path[26][15] = true;
				Path[27][3] = true;
				Path[27][15] = true;
				Path[28][3] = true;
				Path[28][5] = true;
				Path[28][6] = true;
				Path[28][7] = true;
				Path[28][8] = true;
				Path[28][9] = true;
				Path[28][10] = true;
				Path[28][11] = true;
				Path[28][12] = true;
				Path[28][15] = true;
				Path[29][3] = true;
				Path[29][5] = true;
				Path[29][12] = true;
				Path[29][15] = true;
				Path[30][3] = true;
				Path[30][5] = true;
				Path[30][7] = true;
				Path[30][8] = true;
				Path[30][9] = true;
				Path[30][10] = true;
				Path[30][12] = true;
				Path[30][13] = true;
				Path[30][14] = true;
				Path[30][15] = true;
				Path[31][3] = true;
				Path[31][5] = true;
				Path[31][7] = true;
				Path[31][10] = true;
				Path[32][3] = true;
				Path[32][5] = true;
				Path[32][6] = true;
				Path[32][7] = true;
				Path[32][10] = true;
				Path[32][11] = true;
				Path[32][12] = true;
				Path[32][13] = true;
				Path[32][14] = true;
				Path[32][15] = true;
				Path[33][3] = true;
				Path[33][15] = true;
				Path[34][3] = true;
				Path[34][15] = true;
				Path[35][3] = true;
				Path[35][15] = true;
				Path[36][3] = true;
				Path[36][4] = true;
				Path[36][5] = true;
				Path[36][6] = true;
				Path[36][7] = true;
				Path[36][8] = true;
				Path[36][9] = true;
				Path[36][10] = true;
				Path[36][11] = true;
				Path[36][12] = true;
				Path[36][13] = true;
				Path[36][14] = true;
				Path[36][15] = true;
			}
			else if (z == 2) // distance
			{
				Start = new Point(34, 15);
				Finish[0] = new Point(4, 4);
				finishes = 1;

				Path[4][4] = true;
				Path[4][5] = true;
				Path[4][6] = true;
				Path[4][7] = true;
				Path[4][8] = true;
				Path[4][10] = true;
				Path[4][11] = true;
				Path[4][12] = true;
				Path[4][13] = true;
				Path[4][14] = true;
				Path[4][15] = true;
				Path[5][8] = true;
				Path[5][10] = true;
				Path[5][15] = true;
				Path[6][4] = true;
				Path[6][5] = true;
				Path[6][6] = true;
				Path[6][7] = true;
				Path[6][8] = true;
				Path[6][10] = true;
				Path[6][11] = true;
				Path[6][12] = true;
				Path[6][13] = true;
				Path[6][15] = true;
				Path[7][4] = true;
				Path[7][13] = true;
				Path[7][15] = true;
				Path[8][4] = true;
				Path[8][5] = true;
				Path[8][6] = true;
				Path[8][7] = true;
				Path[8][8] = true;
				Path[8][9] = true;
				Path[8][10] = true;
				Path[8][11] = true;
				Path[8][12] = true;
				Path[8][13] = true;
				Path[8][15] = true;
				Path[9][15] = true;
				Path[10][4] = true;
				Path[10][5] = true;
				Path[10][6] = true;
				Path[10][7] = true;
				Path[10][8] = true;
				Path[10][9] = true;
				Path[10][10] = true;
				Path[10][11] = true;
				Path[10][12] = true;
				Path[10][13] = true;
				Path[10][15] = true;
				Path[11][4] = true;
				Path[11][13] = true;
				Path[11][15] = true;
				Path[12][4] = true;
				Path[12][5] = true;
				Path[12][6] = true;
				Path[12][7] = true;
				Path[12][8] = true;
				Path[12][10] = true;
				Path[12][11] = true;
				Path[12][12] = true;
				Path[12][13] = true;
				Path[12][15] = true;
				Path[13][8] = true;
				Path[13][10] = true;
				Path[13][15] = true;
				Path[14][4] = true;
				Path[14][5] = true;
				Path[14][6] = true;
				Path[14][7] = true;
				Path[14][8] = true;
				Path[14][10] = true;
				Path[14][11] = true;
				Path[14][12] = true;
				Path[14][13] = true;
				Path[14][14] = true;
				Path[14][15] = true;
				Path[15][4] = true;
				Path[16][4] = true;
				Path[16][5] = true;
				Path[16][6] = true;
				Path[16][7] = true;
				Path[16][8] = true;
				Path[16][9] = true;
				Path[16][10] = true;
				Path[16][11] = true;
				Path[16][12] = true;
				Path[16][13] = true;
				Path[16][14] = true;
				Path[16][15] = true;
				Path[17][15] = true;
				Path[18][4] = true;
				Path[18][5] = true;
				Path[18][6] = true;
				Path[18][11] = true;
				Path[18][12] = true;
				Path[18][13] = true;
				Path[18][15] = true;
				Path[19][4] = true;
				Path[19][6] = true;
				Path[19][8] = true;
				Path[19][9] = true;
				Path[19][10] = true;
				Path[19][11] = true;
				Path[19][13] = true;
				Path[19][15] = true;
				Path[20][4] = true;
				Path[20][6] = true;
				Path[20][7] = true;
				Path[20][8] = true;
				Path[20][13] = true;
				Path[20][14] = true;
				Path[20][15] = true;
				Path[21][4] = true;
				Path[22][4] = true;
				Path[22][5] = true;
				Path[22][6] = true;
				Path[22][7] = true;
				Path[22][8] = true;
				Path[22][9] = true;
				Path[22][10] = true;
				Path[22][11] = true;
				Path[22][12] = true;
				Path[22][13] = true;
				Path[22][14] = true;
				Path[22][15] = true;
				Path[23][15] = true;
				Path[24][4] = true;
				Path[24][5] = true;
				Path[24][6] = true;
				Path[24][7] = true;
				Path[24][8] = true;
				Path[24][9] = true;
				Path[24][11] = true;
				Path[24][12] = true;
				Path[24][13] = true;
				Path[24][14] = true;
				Path[24][15] = true;
				Path[25][4] = true;
				Path[25][9] = true;
				Path[25][11] = true;
				Path[26][4] = true;
				Path[26][6] = true;
				Path[26][7] = true;
				Path[26][8] = true;
				Path[26][9] = true;
				Path[26][11] = true;
				Path[26][12] = true;
				Path[26][13] = true;
				Path[26][14] = true;
				Path[26][15] = true;
				Path[27][4] = true;
				Path[27][6] = true;
				Path[27][15] = true;
				Path[28][4] = true;
				Path[28][6] = true;
				Path[28][7] = true;
				Path[28][8] = true;
				Path[28][9] = true;
				Path[28][10] = true;
				Path[28][11] = true;
				Path[28][12] = true;
				Path[28][13] = true;
				Path[28][14] = true;
				Path[28][15] = true;
				Path[29][4] = true;
				Path[30][4] = true;
				Path[30][6] = true;
				Path[30][7] = true;
				Path[30][8] = true;
				Path[30][9] = true;
				Path[30][10] = true;
				Path[30][11] = true;
				Path[30][12] = true;
				Path[30][13] = true;
				Path[30][14] = true;
				Path[30][15] = true;
				Path[31][4] = true;
				Path[31][6] = true;
				Path[31][15] = true;
				Path[32][4] = true;
				Path[32][6] = true;
				Path[32][7] = true;
				Path[32][8] = true;
				Path[32][9] = true;
				Path[32][11] = true;
				Path[32][12] = true;
				Path[32][13] = true;
				Path[32][14] = true;
				Path[32][15] = true;
				Path[33][4] = true;
				Path[33][9] = true;
				Path[33][11] = true;
				Path[34][4] = true;
				Path[34][5] = true;
				Path[34][6] = true;
				Path[34][7] = true;
				Path[34][8] = true;
				Path[34][9] = true;
				Path[34][11] = true;
				Path[34][12] = true;
				Path[34][13] = true;
				Path[34][14] = true;
			}
		}
		else if (z == 1) // normal
		{
			z = -1;
			try
			{
				z = JOptionPane.showOptionDialog(null, "Select map:", "Map Selection", 0, 
						JOptionPane.QUESTION_MESSAGE, null, new String[]{"Hills","Split","N/A"}, null);
			}
			catch(Exception error){}


			if (z == 0) // hills
			{
				Start = new Point(4, 3);
				Finish[0] = new Point(34, 3);
				finishes = 1;

				Path[4][4] = true;
				Path[4][5] = true;
				Path[4][6] = true;
				Path[4][7] = true;
				Path[4][8] = true;
				Path[4][9] = true;
				Path[4][10] = true;
				Path[4][11] = true;
				Path[4][12] = true;
				Path[4][13] = true;
				Path[4][14] = true;
				Path[5][14] = true;
				Path[5][15] = true;
				Path[6][15] = true;
				Path[6][16] = true;
				Path[7][16] = true;
				Path[8][15] = true;
				Path[8][16] = true;
				Path[9][14] = true;
				Path[9][15] = true;
				Path[10][5] = true;
				Path[10][6] = true;
				Path[10][7] = true;
				Path[10][8] = true;
				Path[10][9] = true;
				Path[10][10] = true;
				Path[10][11] = true;
				Path[10][12] = true;
				Path[10][13] = true;
				Path[10][14] = true;
				Path[11][4] = true;
				Path[11][5] = true;
				Path[12][3] = true;
				Path[12][4] = true;
				Path[13][3] = true;
				Path[14][3] = true;
				Path[14][4] = true;
				Path[15][4] = true;
				Path[15][5] = true;
				Path[16][5] = true;
				Path[16][6] = true;
				Path[16][7] = true;
				Path[16][8] = true;
				Path[16][9] = true;
				Path[16][10] = true;
				Path[16][11] = true;
				Path[16][12] = true;
				Path[16][13] = true;
				Path[16][14] = true;
				Path[17][14] = true;
				Path[17][15] = true;
				Path[18][15] = true;
				Path[18][16] = true;
				Path[19][16] = true;
				Path[20][15] = true;
				Path[20][16] = true;
				Path[21][14] = true;
				Path[21][15] = true;
				Path[22][5] = true;
				Path[22][6] = true;
				Path[22][7] = true;
				Path[22][8] = true;
				Path[22][9] = true;
				Path[22][10] = true;
				Path[22][11] = true;
				Path[22][12] = true;
				Path[22][13] = true;
				Path[22][14] = true;
				Path[23][4] = true;
				Path[23][5] = true;
				Path[24][3] = true;
				Path[24][4] = true;
				Path[25][3] = true;
				Path[26][3] = true;
				Path[26][4] = true;
				Path[27][4] = true;
				Path[27][5] = true;
				Path[28][5] = true;
				Path[28][6] = true;
				Path[28][7] = true;
				Path[28][8] = true;
				Path[28][9] = true;
				Path[28][10] = true;
				Path[28][11] = true;
				Path[28][12] = true;
				Path[28][13] = true;
				Path[28][14] = true;
				Path[29][14] = true;
				Path[29][15] = true;
				Path[30][15] = true;
				Path[30][16] = true;
				Path[31][16] = true;
				Path[32][15] = true;
				Path[32][16] = true;
				Path[33][14] = true;
				Path[33][15] = true;
				Path[34][3] = true;
				Path[34][4] = true;
				Path[34][5] = true;
				Path[34][6] = true;
				Path[34][7] = true;
				Path[34][8] = true;
				Path[34][9] = true;
				Path[34][10] = true;
				Path[34][11] = true;
				Path[34][12] = true;
				Path[34][13] = true;
				Path[34][14] = true;
			}
			else if (z == 1) // split
			{
				Start = new Point(36, 10);
				Finish[0] = new Point(7, 5);
				Finish[1] = new Point(7, 15);
				finishes = 2;

				Path[3][3] = true;
				Path[3][4] = true;
				Path[3][5] = true;
				Path[3][6] = true;
				Path[3][7] = true;
				Path[3][8] = true;
				Path[3][12] = true;
				Path[3][13] = true;
				Path[3][14] = true;
				Path[3][15] = true;
				Path[3][16] = true;
				Path[3][17] = true;
				Path[4][3] = true;
				Path[4][8] = true;
				Path[4][12] = true;
				Path[4][17] = true;
				Path[5][3] = true;
				Path[5][8] = true;
				Path[5][12] = true;
				Path[5][17] = true;
				Path[6][3] = true;
				Path[6][8] = true;
				Path[6][12] = true;
				Path[6][17] = true;
				Path[7][3] = true;
				Path[7][4] = true;
				Path[7][5] = true;
				Path[7][8] = true;
				Path[7][12] = true;
				Path[7][15] = true;
				Path[7][16] = true;
				Path[7][17] = true;
				Path[8][8] = true;
				Path[8][12] = true;
				Path[9][8] = true;
				Path[9][12] = true;
				Path[10][8] = true;
				Path[10][12] = true;
				Path[11][4] = true;
				Path[11][5] = true;
				Path[11][6] = true;
				Path[11][7] = true;
				Path[11][8] = true;
				Path[11][12] = true;
				Path[11][13] = true;
				Path[11][14] = true;
				Path[11][15] = true;
				Path[11][16] = true;
				Path[12][4] = true;
				Path[12][16] = true;
				Path[13][4] = true;
				Path[13][16] = true;
				Path[14][4] = true;
				Path[14][16] = true;
				Path[15][4] = true;
				Path[15][16] = true;
				Path[16][4] = true;
				Path[16][16] = true;
				Path[17][4] = true;
				Path[17][16] = true;
				Path[18][4] = true;
				Path[18][5] = true;
				Path[18][6] = true;
				Path[18][7] = true;
				Path[18][8] = true;
				Path[18][12] = true;
				Path[18][13] = true;
				Path[18][14] = true;
				Path[18][15] = true;
				Path[18][16] = true;
				Path[19][8] = true;
				Path[19][9] = true;
				Path[19][11] = true;
				Path[19][12] = true;
				Path[20][9] = true;
				Path[20][11] = true;
				Path[21][9] = true;
				Path[21][11] = true;
				Path[22][9] = true;
				Path[22][11] = true;
				Path[23][8] = true;
				Path[23][9] = true;
				Path[23][11] = true;
				Path[23][12] = true;
				Path[24][3] = true;
				Path[24][4] = true;
				Path[24][5] = true;
				Path[24][6] = true;
				Path[24][7] = true;
				Path[24][8] = true;
				Path[24][12] = true;
				Path[24][13] = true;
				Path[24][14] = true;
				Path[24][15] = true;
				Path[24][16] = true;
				Path[24][17] = true;
				Path[25][3] = true;
				Path[25][17] = true;
				Path[26][3] = true;
				Path[26][17] = true;
				Path[27][3] = true;
				Path[27][17] = true;
				Path[28][3] = true;
				Path[28][17] = true;
				Path[29][3] = true;
				Path[29][4] = true;
				Path[29][5] = true;
				Path[29][6] = true;
				Path[29][7] = true;
				Path[29][8] = true;
				Path[29][9] = true;
				Path[29][11] = true;
				Path[29][12] = true;
				Path[29][13] = true;
				Path[29][14] = true;
				Path[29][15] = true;
				Path[29][16] = true;
				Path[29][17] = true;
				Path[30][9] = true;
				Path[30][11] = true;
				Path[31][9] = true;
				Path[31][11] = true;
				Path[32][9] = true;
				Path[32][11] = true;
				Path[33][9] = true;
				Path[33][10] = true;
				Path[33][11] = true;
				Path[34][10] = true;
				Path[35][10] = true;

			}
			else if (z == 2)
			{
				Start = new Point(14, 10);
				finishes = 0;

				Path[10][5] = true;
				Path[10][6] = true;
				Path[10][7] = true;
				Path[10][8] = true;
				Path[10][9] = true;
				Path[10][10] = true;
				Path[10][11] = true;
				Path[10][12] = true;
				Path[10][13] = true;
				Path[10][14] = true;
				Path[10][15] = true;
				Path[11][5] = true;
				Path[11][10] = true;
				Path[11][15] = true;
				Path[12][5] = true;
				Path[12][10] = true;
				Path[12][15] = true;
				Path[13][5] = true;
				Path[13][10] = true;
				Path[13][15] = true;
				Path[14][5] = true;
				Path[14][6] = true;
				Path[14][7] = true;
				Path[14][8] = true;
				Path[14][9] = true;
				Path[14][11] = true;
				Path[14][12] = true;
				Path[14][13] = true;
				Path[14][14] = true;
				Path[14][15] = true;
				Path[15][5] = true;
				Path[15][10] = true;
				Path[15][15] = true;
				Path[16][5] = true;
				Path[16][10] = true;
				Path[16][15] = true;
				Path[17][5] = true;
				Path[17][10] = true;
				Path[17][15] = true;
				Path[18][5] = true;
				Path[18][10] = true;
				Path[18][15] = true;
				Path[19][5] = true;
				Path[19][6] = true;
				Path[19][7] = true;
				Path[19][8] = true;
				Path[19][9] = true;
				Path[19][10] = true;
				Path[19][11] = true;
				Path[19][12] = true;
				Path[19][13] = true;
				Path[19][14] = true;
				Path[19][15] = true;
			}
		}
		else if (z == 2) // hard
		{
			z = -1;
			try
			{
				z = JOptionPane.showOptionDialog(null, "Select map:", "Map Selection", 0, 
						JOptionPane.QUESTION_MESSAGE, null, new String[]{"Wheel","Run","N/A"}, null);
			}
			catch(Exception error){}


			if (z == 0) // hills
			{
				Start = new Point(20, 9);
				Finish[0] = new Point(13, 2);
				Finish[1] = new Point(27, 2);
				Finish[2] = new Point(13, 16);
				Finish[3] = new Point(27, 16);
				finishes = 4;

				Path[13][2] = true;
				Path[13][3] = true;
				Path[13][4] = true;
				Path[13][5] = true;
				Path[13][6] = true;
				Path[13][7] = true;
				Path[13][8] = true;
				Path[13][9] = true;
				Path[13][10] = true;
				Path[13][11] = true;
				Path[13][12] = true;
				Path[13][13] = true;
				Path[13][14] = true;
				Path[13][15] = true;
				Path[13][16] = true;
				Path[14][2] = true;
				Path[14][9] = true;
				Path[14][16] = true;
				Path[15][2] = true;
				Path[15][9] = true;
				Path[15][16] = true;
				Path[16][2] = true;
				Path[16][9] = true;
				Path[16][16] = true;
				Path[17][2] = true;
				Path[17][9] = true;
				Path[17][16] = true;
				Path[18][2] = true;
				Path[18][9] = true;
				Path[18][16] = true;
				Path[19][2] = true;
				Path[19][9] = true;
				Path[19][16] = true;
				Path[20][2] = true;
				Path[20][3] = true;
				Path[20][4] = true;
				Path[20][5] = true;
				Path[20][6] = true;
				Path[20][7] = true;
				Path[20][8] = true;
				Path[20][10] = true;
				Path[20][11] = true;
				Path[20][12] = true;
				Path[20][13] = true;
				Path[20][14] = true;
				Path[20][15] = true;
				Path[20][16] = true;
				Path[21][2] = true;
				Path[21][9] = true;
				Path[21][16] = true;
				Path[22][2] = true;
				Path[22][9] = true;
				Path[22][16] = true;
				Path[23][2] = true;
				Path[23][9] = true;
				Path[23][16] = true;
				Path[24][2] = true;
				Path[24][9] = true;
				Path[24][16] = true;
				Path[25][2] = true;
				Path[25][9] = true;
				Path[25][16] = true;
				Path[26][2] = true;
				Path[26][9] = true;
				Path[26][16] = true;
				Path[27][2] = true;
				Path[27][3] = true;
				Path[27][4] = true;
				Path[27][5] = true;
				Path[27][6] = true;
				Path[27][7] = true;
				Path[27][8] = true;
				Path[27][9] = true;
				Path[27][10] = true;
				Path[27][11] = true;
				Path[27][12] = true;
				Path[27][13] = true;
				Path[27][14] = true;
				Path[27][15] = true;
				Path[27][16] = true;
			}
			else if (z == 1)
			{
				Start = new Point(4, 10);
				Finish[0] = new Point(35, 10);
				finishes = 1;

				Path[5][10] = true;
				Path[6][10] = true;
				Path[7][10] = true;
				Path[8][10] = true;
				Path[9][10] = true;
				Path[10][10] = true;
				Path[11][10] = true;
				Path[12][10] = true;
				Path[13][10] = true;
				Path[14][10] = true;
				Path[15][10] = true;
				Path[16][10] = true;
				Path[17][10] = true;
				Path[18][10] = true;
				Path[19][10] = true;
				Path[20][10] = true;
				Path[21][10] = true;
				Path[22][10] = true;
				Path[23][10] = true;
				Path[24][10] = true;
				Path[25][10] = true;
				Path[26][10] = true;
				Path[27][10] = true;
				Path[28][10] = true;
				Path[29][10] = true;
				Path[30][10] = true;
				Path[31][10] = true;
				Path[32][10] = true;
				Path[33][10] = true;
				Path[34][10] = true;
			}
		}
		if (z < 0)
			System.exit(0);
	}
}
