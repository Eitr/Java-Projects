package random;


import java.awt.*;
import java.awt.event.*;

import javax.swing.*;

@SuppressWarnings("serial")

public class Bridget extends JFrame
{

	int b [][];

	int c,r,s;

	int edit = 0;
	Point m = new Point (0,0);

	Point p = new Point (10,10);

	int frame = 0;

	Point f [] = new Point [10];
	
	
	
	
	
	
	
	
	
	
	void createVariables ()
	{
		c = 40;
		r = 20;

		s = 24; // multiple of 2,3,4

		b = new int [c][r];

		for (int x = 0; x < b.length; x++)
			for (int y = 0; y < b[0].length; y++)
			{
				int type = (Math.random() < .8)? 1:(int)(Math.random()*2+2);
				b[x][y] = type;
			}
		
		for (int i = 0; i < f.length; i++)
		{
			f[i] = new Point ((int)(Math.random()*c),(int)(Math.random()*r));
		}
	}











	class Mouse implements MouseListener
	{
		public void mousePressed (MouseEvent e){}
		public void mouseReleased (MouseEvent e){}
		public void mouseEntered (MouseEvent e){}
		public void mouseExited (MouseEvent e){}
		public void mouseClicked (MouseEvent e)
		{
			m = new Point (e.getX()/s,e.getY()/s);

			if (m.x < c && m.y<r)
			{
				b[m.x][m.y] = edit;
			}
		}
	}
	class Keyboard implements KeyListener
	{
		public void keyPressed (KeyEvent e)
		{
			switch(e.getKeyCode())
			{
			case KeyEvent.VK_UP: 	p.y-=1; break;
			case KeyEvent.VK_DOWN: 	p.y+=1; break;
			case KeyEvent.VK_LEFT: 	p.x-=1; break;
			case KeyEvent.VK_RIGHT: p.x+=1; break;
			
			case KeyEvent.VK_1: edit=1; break;
			case KeyEvent.VK_2: edit=2; break;
			case KeyEvent.VK_3: edit=3; break;
			}
		}
		public void keyReleased (KeyEvent e){}
		public void keyTyped (KeyEvent e){}
	}
	class MouseMotion implements MouseMotionListener
	{
		public void mouseMoved (MouseEvent e){}
		public void mouseDragged (MouseEvent e)
		{
			m = new Point (e.getX()/s,e.getY()/s);

			if (m.x >= 0 && m.y >= 0 && m.x < c && m.y<r)
			{
				b[m.x][m.y] = edit;
			}
		}
	}










	class PaintPanel extends JPanel
	{
		public void paintComponent (Graphics g)
		{
			frame++;

			super.paintComponent(g);
			setBackground(Color.black);
			g.setColor(Color.white);
			g.setFont(new Font(null, Font.PLAIN, 20));



			for (int x = 0; x < b.length; x++)
				for (int y = 0; y < b[0].length; y++)
				{
					if (b[x][y] == 1)
					{
						g.setColor(new Color(60,20,20));
						g.drawLine(x*s,y*s,x*s+s,y*s+s);
						g.drawLine(x*s,y*s+s,x*s+s,y*s);
					}
					else if (b[x][y] == 2)
					{
//						g.setColor(new Color(0,0,20));
//						g.fillRect(x*s,y*s,s,s);

						g.setColor(new Color(0,0,100));
						g.drawLine(x*s,y*s,x*s+s,y*s+s);
						g.drawLine(x*s,y*s+s,x*s+s,y*s);
						
						g.setColor(new Color(0,0,250));
						if (frame % 40 < 20)
//						if (Math.random() < .99)
						{
							g.drawLine(x*s,y*s,x*s+s,y*s+s);
//							g.drawLine(x*s+s/2,y*s,x*s,y*s+s/2);
//							g.drawLine(x*s+s,y*s+s/2,x*s+s/2,y*s+s);
						}
						else
						{
							g.drawLine(x*s+s/2,y*s,x*s+s,y*s+s/2);
							g.drawLine(x*s,y*s+s/2,x*s+s/2,y*s+s);
						}
					}
					else if (b[x][y] == 3) // tree
					{
						
						// rustling bush??
//						g.setColor(new Color(0,30,0));
//						for (int i = 0; i < 60; i++)
//							g.drawRect(x*s+(int)(Math.random()*s), y*s+(int)(Math.random()*s), 1, 1);
						
						
						
						g.setColor(new Color(0,250,0));
						g.drawLine(x*s+s/2,y*s,x*s+s,y*s+s/2);
						g.drawLine(x*s+s/2,y*s,x*s,y*s+s/2);
						g.drawLine(x*s+s/2,y*s+s,x*s,y*s+s/2);
						g.drawLine(x*s+s/2,y*s+s,x*s+s,y*s+s/2);
					}
					else if (b[x][y] == 4)
					{
						g.setColor(new Color(100,30,30));
						g.drawLine(x*s,y*s,x*s+s,y*s+s);
						g.drawLine(x*s,y*s+s,x*s+s,y*s);
					}
				}

			g.setColor(Color.white);
			g.drawRect(0,0,c*s,r*s);


			g.setColor(Color.white);

			int t = s/4;

			// head
			g.drawOval(p.x*s+s/2-t/2,p.y*s,t,t);

			// torso
			g.drawLine(p.x*s+s/2,p.y*s+t,p.x*s+s/2,p.y*s+t*2);

			// skirt
			g.fillPolygon(new int[]{p.x*s+s/2,p.x*s+s/4,p.x*s+s*3/4}, new int[]{p.y*s+t*2,p.y*s+t*3,p.y*s+t*3}, 3);

//			g.drawLine(p.x*s+s/2,p.y*s+t*2,p.x*s+s/4,p.y*s+t*3);
//			g.drawLine(p.x*s+s/2,p.y*s+t*2,p.x*s+s*3/4,p.y*s+t*3);
//			g.drawLine(p.x*s+s/4,p.y*s+t*3,p.x*s+s*3/4,p.y*s+t*3);

			// legs
			g.drawLine(p.x*s+s/3,p.y*s+t*3,p.x*s+s/3,p.y*s+s);
			g.drawLine(p.x*s+s*2/3,p.y*s+t*3,p.x*s+s*2/3,p.y*s+s);

			//arms
			g.drawLine(p.x*s+s/2,p.y*s+t*2,p.x*s+s/4,p.y*s+t);
			g.drawLine(p.x*s+s/2,p.y*s+t*2,p.x*s+s*3/4,p.y*s+t);
			
			
			
			for (int i = 0; i < f.length; i++)
			{
				g.setColor(new Color((int)(Math.random()*255),(int)(Math.random()*255),(int)(Math.random()*255)));
				
				for (int j = 0; j < 6; j++)
				g.drawOval(f[i].x*s+(int)(Math.random()*s),f[i].y*s+(int)(Math.random()*s),
						(int)(Math.random()*s/2),(int)(Math.random()*s/2));
			}
		}
	}
















	class Game implements ActionListener
	{
		public void actionPerformed (ActionEvent e)
		{

		}
	}
	class Paint implements ActionListener
	{
		public void actionPerformed (ActionEvent e)
		{
			repaint();
		}
	}


	public Bridget ()
	{		
		createVariables();

		Timer game = new Timer (100, new Game());
		game.start();
		Timer paint = new Timer (100, new Paint());
		paint.start();

		PaintPanel paintPanel = new PaintPanel();
		paintPanel.setVisible(true);

		Container cp = getContentPane();
		cp.add(paintPanel);

		cp.addMouseListener(new Mouse());
		cp.addMouseMotionListener(new MouseMotion());
		cp.addKeyListener(new Keyboard());

		cp.setFocusable(true);
	}
	public static void main (String[] Bridget)
	{
		Bridget frame = new Bridget();
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setTitle("");
		frame.setSize(1100, 750);
		frame.setLocation(0,0);
		frame.setVisible(true);
	}
}

