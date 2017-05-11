package games;

import java.awt.*;
import javax.swing.*;
import java.awt.event.*;
import java.awt.geom.Point2D;

public class Snake extends JFrame
{
	private static final long serialVersionUID = -2219902844090816356L;

	int maxPlayers = 1;
	int size = 12;
	int speed = 6;
	int [] dir;
	int [] rot;
	boolean [][] turning;
	int width;
	int height;
	Point2D [][] player;
	char [][] keys = new char[3][4];
	int [] index;
	Point [] item;
	int loser;
	int explode;
	boolean lockCanvas = true;


	public static void main (String[] args)
	{
		(new Snake()).setVisible(true);
	}

	public Snake ()
	{
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		setSize(600,600);
		setLocationRelativeTo(null);

		width = getToolkit().getScreenSize().width-20;
		height = getToolkit().getScreenSize().height-100;    

		reset();

		keys[0] = new char[] {'w','d','s','a'};
		keys[1] = new char[] {'8','6','5','4'};
		keys[2] = new char[] {'i','l','k','j'};


		this.addKeyListener(new Keyboard());
		add(new PaintPanel());
	}

	private void reset ()
	{
		lockCanvas = true;

		player = new Point2D [width*height/size][maxPlayers];
		dir = new int[maxPlayers];
		rot = new int[maxPlayers];
		turning = new boolean [2][maxPlayers];
		index = new int[maxPlayers];
		item = new Point[2];

		for (int i = 0; i < maxPlayers; i++)
		{
			player[0][i] = new Point2D.Double(width/2, i*size*6+6*size);
			rot[i] = 0;
			index[i] = 1;

			for(int l = 0; l < 2; l++)
			{
				player[index[i]][i] = new Point2D.Double(-2*size,-2*size);
				index[i]++;
			}
			dir[i] = 1;
		}
		loser = -1;
		explode = 100;

		for (int i = 0; i < item.length; i++)
		{
			item[i] = new Point((int)(Math.random()*width), (int)(Math.random()*height));
		}
		lockCanvas = false;
	}

	class Keyboard implements KeyListener
	{
		public void keyPressed(KeyEvent event)
		{
			String key = (event.getKeyChar()+"").toLowerCase();

			if (key.compareTo("r") == 0)
				reset();

			if (key.compareTo("p") == 0)
			{
				maxPlayers = (maxPlayers==1? 2:(maxPlayers==2? 3:1));
				reset();
			}
			
			if (key.compareTo("t") == 0)
			{
				size = size==12? 24:12;
			}

			for (int p = 0; p < maxPlayers; p++)
			{
				for (int k = 0; k < 4; k++)
					if (key.compareTo((keys[p][k]+"")) == 0)
					{
						dir[p] = k+1;

						switch (dir[p])
						{
						case 1: break;
						case 2: //rot[p] = (rot[p] + speed + 360) % 360; 
							turning[0][p] = true; break;
						case 3: break;
						case 4: //rot[p] = (rot[p] - speed + 360) % 360; 
							turning[1][p] = true;  break;
						}
					}
			}
		}
		public void keyReleased(KeyEvent key)
		{
			for (int p = 0; p < maxPlayers; p++)
			{
				for (int k = 0; k < 4; k++)
					if (key.getKeyChar() == (keys[p][k]))
					{
						switch (dir[p])
						{
						case 1: break;
						case 2: turning[0][p] = false; turning[1][p] = false; break;
						case 3: break;
						case 4: turning[1][p] = false; turning[0][p] = false; break;
						}
					}
			}
		}
		public void keyTyped(KeyEvent arg0){}
	}


	public class PaintPanel extends JPanel
	{
		private static final long serialVersionUID = -9137061573092868879L;

		public PaintPanel ()
		{
			(new Timer (20, new Painter())).start();
		}

		public void move (int p, int x, int y)
		{
			for (int i = index[p]-1; i > 0; i--)
			{
				player[i][p] = player[i-1][p];
			}
			//      player[0][p] = new Point2D.Double(player[0][p].getX() + x, player[0][p].getY() + y);
			player[0][p] = new Point2D.Double (
					(player[0][p].getX() + size*Math.cos(rot[p]*Math.PI/180.0)), 
					(player[0][p].getY() + size*Math.sin(rot[p]*Math.PI/180.0)));

			for (int s = 0; s < maxPlayers; s++)
				for (int i = 0; i < index[s]; i++)
				{
					if (player[0][p].distance(player[i][s]) < .9*size && !(i == 0 && s == p))
					{
						loser = p;
						return;
					}
				}

			if(player[0][p].getX() < 0)
				player[0][p].setLocation(width,player[0][p].getY());
			else if(player[0][p].getX() >= width)
				player[0][p].setLocation(0,player[0][p].getY());
			if(player[0][p].getY() < 0)
				player[0][p].setLocation(player[0][p].getX(),height);
			else if(player[0][p].getY() >= height)
				player[0][p].setLocation(player[0][p].getX(),0);
		}

		int frame = 0;

		class Painter implements ActionListener
		{
			public void actionPerformed (ActionEvent e)
			{
				if(lockCanvas)
					return;
				frame++;

				if (loser >= 0)
				{ 
					if (explode > 0)
					{
						explode--;
						for (int p = 0; p < maxPlayers; p++)
						{
							for (int i = 0; i < index[p]; i++)
							{
								player[i][p] = new Point2D.Double( player[i][p].getX()+(Math.random()<.5? 1:-1), player[i][p].getY()+(Math.random()<.5? 1:-1) ); 
							}
						}
					}
				}
				else if (frame%(5-24/size) == 0)
				{					
					for (int p = 0; p < maxPlayers; p++)
					{
						for (int i = 0; i < item.length; i++)
						{
							if (player[0][p].distance(item[i]) < 1.3*size)
							{
								for (int l = 0; l < 12/maxPlayers; l++)
								{
									player[index[p]][p] = new Point2D.Double(-2*size,-2*size);
									index[p]++;
								}
								item[i] = new Point ((int)(Math.random()*(width-100)+50), (int)(Math.random()*(height-100)+50));
							}
						}

						move(p,0,0);
					}
				}

				for (int p = 0; p < maxPlayers; p++)
				{
					if(turning[0][p])
						rot[p] = (rot[p] + speed + 360) % 360;
					else if(turning[1][p])
						rot[p] = (rot[p] - speed + 360) % 360;

					//					switch(dir[p])
					//					{
					//					case 1: move(p,0,-1); break;
					//					case 2: move(p,1,0); break;
					//					case 3: move(p,0,1); break;
					//					case 4: move(p,-1,0); break;
					//					}
				}


				repaint();
			}
		}

		protected void paintComponent (Graphics g)
		{
			if(lockCanvas)
				return;
			super.paintComponent(g);
			g.drawString("[R] Reset       [P] # Players (1-3)       [T] Change size", 100,height+26);
			g.setFont(new Font("Georgia",Font.BOLD,448));

			setBackground(Color.black);
			
			g.setColor(new Color(10,10,10));
			g.drawString("SNAKE", 50,700);

			g.setColor(new Color(0,0,(int)(Math.random()*50+200)));
			//			for (int x = 0; x <= width; x+=(int)(Math.random()*width/size))
			//				for (int y = 0; y <= height; y+=(int)(Math.random()*height/size))
			//				{					
			//					g.drawRect(x+1, y, 0, 0);
			//					g.drawRect(x-1, y, 0, 0);
			//					g.drawRect(x, y+1, 0, 0);
			//					g.drawRect(x, y-1, 0, 0);
			//				}
			for (int i = 0; i < 100; i++)
			{
				//				g.drawRect((int)(Math.random()*width), (int)(Math.random()*height), 0, 0);
				int x = (int)(Math.random()*size*width);
				int y = (int)(Math.random()*size*height);
				g.drawLine(x+(int)(Math.random()*size*2), y+(int)(Math.random()*size*2), x+(int)(Math.random()*size*2), y+(int)(Math.random()*size*2));
			}

			g.drawRect(0, 0, width+size, height+size);

			for(int i = 0; i < item.length; i++) 
			{
				g.setColor(new Color((int)(Math.random()*100+150),(int)(Math.random()*100+150),(int)(Math.random()*100+150)));
				g.fillRect(item[i].x, item[i].y, size, size);
			}
			g.setColor(Color.white);

			for(int p = 0; p < maxPlayers; p++)
			{
				for(int i = 0; i < index[p]; i++)      
				{
					switch(p)
					{
					case 0: g.setColor(new Color((int)(Math.random()*50+200),0,0)); break;
					case 1: g.setColor(new Color(0,(int)(Math.random()*50+200),0)); break;
					case 2: g.setColor(new Color(0,(int)(Math.random()*50+200),(int)(Math.random()*50+200))); break;
					}

					g.fillPolygon(new int[]{  (int)(player[i][p].getX()+Math.random()*size/2),
							(int)(player[i][p].getX()+Math.random()*size/2)+size/2,
							(int)(player[i][p].getX()+size),
							(int)(player[i][p].getX()+size),
							(int)(player[i][p].getX()+Math.random()*size/2)+size/2,
							(int)(player[i][p].getX()+Math.random()*size/2),
							(int)(player[i][p].getX()+0),
							(int)(player[i][p].getX()+0)
					},
					new int[]{  (int)(player[i][p].getY()+0),
							(int)(player[i][p].getY()+0),
							(int)(player[i][p].getY()+Math.random()*size/2),
							(int)(player[i][p].getY()+Math.random()*size/2)+size/2,
							(int)(player[i][p].getY()+size),
							(int)(player[i][p].getY()+size),
							(int)(player[i][p].getY()+Math.random()*size/2)+size/2,
							(int)(player[i][p].getY()+Math.random()*size/2)
					}, 8);
				}
			}

//			if (loser != -1)
//			{
//				g.setColor(new Color((int)(Math.random()*100+150),(int)(Math.random()*100+150),(int)(Math.random()*100+150)));
//				g.drawString("Player " + (loser+1) +" is a loser!", 100,height/2);
//			}

		}
	}
}
