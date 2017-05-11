package space;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

@SuppressWarnings("serial")
public class Space extends JFrame
{
	/* MOUSE LOCATION */
	Point m;


	int X = 0; // TEST
	int Y = 0; // TEST

	int frame = 0;


	Rectangle [] button = new Rectangle [8];
	int button_selected;
	Rectangle cancel;

	/* =======================================================================*/



	/* BACKGROUND STARS */
	Point [] background = new Point [6000];

	/* ASTEROIDS */
	Rectangle [] asteroid = new Rectangle [40];
	int A;

	/* PORTALS */
	Portal [] portal = new Portal [100];
//	boolean [] portal_active = new boolean [100];
	int P;

	/* STRUCTURES */
	Structure [] structure = new Structure [100];
	int S;



//	boolean build = false; // TEST
	int source = -2; // TEST




	int money;
	int energy;
	int max_energy;



	void createVariables ()
	{
		m = new Point (0,0);

		P = 0;
		A = asteroid.length;
		button_selected = 0;
		money = 10000;
		energy = 0;
		max_energy = 20;

		for (int i = 0; i < button.length; i++)
		{
			button[i] = new Rectangle(0, i*50, 100, 50);
		}
		cancel = new Rectangle(0, button.length*50, 100, 50);


		Point start = new Point (600,300); // TEST






		/* BACKGROUND STARS */
		for (int i = 0; i < background.length; i++)
			background[i] = new Point((int)(Math.random()*5000-2500), (int)(Math.random()*5000-2500));


		/* ASTEROIDS */
		for (int i = 0; i < asteroid.length; i++)
			asteroid[i] = new Rectangle((int)(Math.random()*500+start.x-250), (int)(Math.random()*1000+start.y-500), (int)(Math.random()*150+150), 0);


		structure[0] = new Structure(start.x, start.y, 0, -1);
		S = 1;

	}











	class Mouse implements MouseListener
	{
		public void mousePressed (MouseEvent e)
		{
			button_selected = getButton();

			if (button_selected > -1)
				if (money >= Structure.getCost(button_selected))
				{
					// portal
					if (button_selected == 0)
					{
						if (P < portal.length)
						{
							if (source == -1)
							{
								portal[P] = new Portal (m.x-X, m.y-Y, structure[0].getLocation(), -1);
								P++;
								money -= Structure.getCost(0);
							}
							else if (source > -1)
								if (portal[source].getConnections() < 4)
								{
									portal[P] = new Portal (m.x-X, m.y-Y, portal[source].getLocation(), source);
									portal[source].addConnection();
									P++;
									money -= Structure.getCost(0);
								}
						}
					}
					// miner
					else if (button_selected == 1)
					{
						if (S < structure.length)
						{
							if (source > -1)
								if (portal[source].getConnections() < 4)
								{
									structure[S] = new Structure(m.x-X, m.y-Y, button_selected, source);
									S++;

//									portal[P] = new Portal (m.x-X, m.y-Y, portal[source].getLocation());
									portal[source].addConnection();
//									P++;
									money -= Structure.getCost(button_selected);
								}
						}
					}
				}
				else
					button_selected = -1;
		}
		public void mouseReleased (MouseEvent e){}
		public void mouseEntered (MouseEvent e){}
		public void mouseExited (MouseEvent e){}
		public void mouseClicked (MouseEvent e){}
	}

	class MouseMotion implements MouseMotionListener
	{
		public void mouseMoved (MouseEvent e)
		{
			m = new Point (e.getX(), e.getY());
		}
		public void mouseDragged (MouseEvent e)
		{
			m = new Point (e.getX(), e.getY());
		}
	}
	class Keyboard implements KeyListener
	{
		public void keyPressed (KeyEvent e)
		{
			if (e.getKeyCode() == KeyEvent.VK_UP)
				Y+=10;
			if (e.getKeyCode() == KeyEvent.VK_DOWN)
				Y-=10;
			if (e.getKeyCode() == KeyEvent.VK_RIGHT)
				X-=10;
			if (e.getKeyCode() == KeyEvent.VK_LEFT)
				X+=10;
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

			g.setFont(new Font(null, Font.PLAIN, 15));




			/* ============================================================================================= */


			g.translate(X, Y);


			/* STRUCTURES */
			for (int i = 0; i < S; i++)
				structure[i].draw(g);


			/* BACKGROUND STARS */
			g.setColor(Color.white);
			for (int i = 0; i < background.length; i++)
				g.drawRect( background[i].x, background[i].y, 0, 0);

			/* ASTEROIDS */
			g.setColor(Color.blue);
			for (int i = 0; i < A; i++)
			{
				g.fillOval( asteroid[i].x-asteroid[i].width/20, asteroid[i].y-asteroid[i].width/20, 
						asteroid[i].width/10, asteroid[i].width/10);
			}

			/* PORTALS */
			for (int i = 0; i < P; i++)
			{
				portal[i].draw(g);//, portal_active[i]);
			}



			/* PORTAL LINKS */

			if (button_selected == 0)
			{
				g.setColor(Color.white);
				boolean build = false;
				if (distance(new Point(m.x - X, m.y - Y),structure[0].getLocation()) < Portal.getRange())
				{
					build = true;
					source = -1;
					g.drawLine( m.x-X, m.y-Y, structure[0].getX(), structure[0].getY());
				}
				else
				{
					double min = Portal.getRange()+1;
					for (int i = 0; i < P; i++)
					{
						double d = distance(new Point(m.x - X, m.y - Y), portal[i].getLocation()); 
						if (d < Portal.getRange())
						{
							if (d < min)
							{
								min = d;
								build = true;
								source = i;
							}
						}
					}
					if (build)
					{
						g.drawLine( m.x-X, m.y-Y, portal[source].getX(), portal[source].getY());
					}
				}
				if (!build)
					source = -2;
			}


			/* STRUCTURE LINKS */

			if (button_selected > 0)
			{
				g.setColor(Color.white);
				boolean build = false;
//				if (distance(new Point( m. x - X, m.y - Y), structure[button_selected].getLocation()) < Portal.getRange())
//				{
//				build = true;
//				source = -1;
//				g.drawLine( m. x-X, m.y-Y, structure[0].getX(), structure[button_selected].getY());
//				}
//				else
				{
					double min = Portal.getRange()+1;
					for (int i = 0; i < P; i++)
					{
						double d = distance(new Point( m.x - X, m.y - Y), portal[i].getLocation()); 
						if (d < Portal.getRange())
						{
							if (d < min)
							{
								min = d;
								build = true;
								source = i;
							}
						}
					}
					if (build)
					{
						if (button_selected == 1)
						{
							for (int i = 0; i < A; i++)
							{
								double d = distance(new Point( m.x - X, m.y - Y), asteroid[i].getLocation());

								if (d < Structure.getRange(1))
								{
									g.setColor(Color.blue);
									g.drawLine( m.x-X, m.y-Y, asteroid[i].x, asteroid[i].y);
								}
							}
							g.setColor(Color.white);
							g.drawLine( m.x-X, m.y-Y, portal[source].getX(), portal[source].getY());
						}
					}
				}
				if (!build)
					source = -2;
			}



			/* ============================================================================================= */
			/* ============================================================================================= */
			/* ==================================	STATIC ELEMENTS		==================================== */
			/* ============================================================================================= */
			/* ============================================================================================= */






			g.translate(-X, -Y);



			/*	==============================		MENU	========================== */

//			int offx = 5;
//			int offy = 5;

			g.setColor(Color.GREEN);
			g.drawString(X+"    "+Y, 800,200); // TEST

			g.setColor(new Color(50, 50, 50));
			g.fillRect(0, 0, 100, 700);

			for (int i = 0; i < button.length; i++)
			{
				if (button_selected == i)
				{
					g.setColor(new Color(150, 150, 150));
					g.fillRect(button[i].x, button[i].y, button[i].width, button[i].height);
				}

				g.setColor(new Color(50, 250, 50));
				g.drawRect(button[i].x, button[i].y, button[i].width, button[i].height);

//				if (i > 0)
				g.drawString(""+Structure.getName(i), button[i].x + 5, button[i].y + 40);
//				else
//				g.drawString("Portal", button[i].x + 10, button[i].y + 40);

				g.drawString("$ "+Structure.getCost(i), button[i].x + button[i].width - 40, button[i].y + 40);
			}

			g.drawRect(cancel.x, cancel.y, cancel.width, cancel.height);
			g.setFont(new Font(null, Font.BOLD, 20));
			g.drawString("Cancel", 5, (button.length + 1)* button[0].height - button[0].height/3);
			g.drawString("$ "+money, 10, (button.length + 2)* button[0].height);

			g.drawString("E "+energy, 10, (button.length + 3)* button[0].height);
			g.fillRect( 0, (button.length + 4)* button[0].height, (int)((double)energy / max_energy * button[0].width), 10);
//			g.setColor(Color.black);
			g.drawRect( 0, (button.length + 4)* button[0].height, button[0].width, 10);


			/* ==================================================================== */


			/* MOUSE */
			g.setColor(Color.green);
			g.drawOval(m.x-2, m.y-2, 5, 5);
			g.fillOval(m.x-1, m.y-1, 3, 3);

			g.drawLine(m.x, m.y-5, m.x, m.y+5);
			g.drawLine(m.x-5, m.y, m.x+5, m.y);
		}
	}

	public double distance (Point f, Point l)
	{
		return Math.sqrt( Math.pow(f.x - l.x, 2) + Math.pow(f.y - l.y, 2));
	}
	public int getButton ()
	{
		for (int i = 0; i < button.length; i++)
		{
			if (m.x > button[i].x && m.y > button[i].y && m.x < button[i].x+button[i].width && m.y < button[i].y+button[i].height)
			{
				return i;
			}
		}
		if (m.x > cancel.x && m.y > cancel.y && m.x < cancel.x+cancel.width && m.y < cancel.y+cancel.height)
			return -1;
		return button_selected;
	}















	class Game implements ActionListener
	{
		public void actionPerformed (ActionEvent e)
		{
			frame++;
			if (frame%2 == 0)
			{
				energy++;
				if (energy > max_energy)
					energy = max_energy;
			}
			
			/* PORTAL ENERGY */

//			if (frame)
			for (int p = 0; p < P; p++)
			{
				if (!portal[p].isReady())
				{
					if (energy > 0)
					{
						if (portal[p].getSource() == -1)
						{
							portal[p].addEnergy();
							energy--;
						}
						else if (portal[portal[p].getSource()].isReady())
						{
							portal[p].addEnergy();
							energy--;
//							int temp = p;

//							while (true)
//							{
//							if (portal[temp].getSource() == -1)
//							break;
//							portal_active[portal[temp].getSource()] = true;
//							temp = portal[temp].getSource();
//							}
						}
					}
				}
			}

			/* SRUCTURES */

			for (int s = 1; s < S; s++)
			{
				if (!structure[s].isReady())
				{
					if (energy > 0)
					{
						if (portal[structure[s].getSource()].isReady())
						{
							structure[s].addEnergy();
							energy--;
//							break;
						}
					}
				}
				else
				{
					if (structure[s].getType() == 1)
						if (frame % structure[s].getSpeed() == 0)
							for (int a = 0; a < A; a++)
							{
								double d = distance(structure[s].getLocation(), new Point(asteroid[a].x, asteroid[a].y));

								if (d < Structure.getRange(1))
								{
									asteroid[a].width--;
									money++;



									if (asteroid[a].width <= 0)
									{
										A--;

										for (int i = a; i < A; i++)
										{
											asteroid[i] = asteroid[i+1];
										}
									}

									break;
								}
							}
				}
			}
		}
	}





















	class Paint implements ActionListener
	{
		public void actionPerformed (ActionEvent e)
		{
			repaint();
		}
	}


	public Space ()
	{		
		createVariables();

		Timer game = new Timer (100, new Game());
		game.start();
		Timer paint = new Timer (10, new Paint());
		paint.start();

		PaintPanel paintPanel = new PaintPanel();
//		paintPanel.addMouseListener(new Mouse());
//		paintPanel.addMouseMotionListener(new MouseMotion());
//		paintPanel.addKeyListener(new Keyboard());
		Container cp = getContentPane();
		cp.add(paintPanel);
		paintPanel.setVisible(true);


		cp.addMouseListener(new Mouse());
		cp.addMouseMotionListener(new MouseMotion());
		cp.addKeyListener(new Keyboard());

		cp.setFocusable(true);

		Image image=getToolkit().createImage(new byte[0]);
		setCursor(getToolkit().createCustomCursor(image, new Point(), "none"));
	}
	public static void main (String[] Bridget)
	{
		Space frame = new Space();
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setTitle("");
		frame.setSize(1100, 700);
		frame.setLocation(0,0);
		frame.setVisible(true);
	}
}

