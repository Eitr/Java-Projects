package games;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

@SuppressWarnings("serial")
public class Cave extends JFrame
{
	int frame;
	Rectangle p;
	Rectangle top;
	Rectangle bottom;
	
	Rectangle end;

	int mouse;
	int s;
	int max;

	int speed;


	boolean pressed;
	boolean lose;
	boolean start;
	boolean pause;
	boolean win;

	Rectangle b[] = new Rectangle[200];

	int level;
	int high;

	Color color[] = new Color[50];
	Rectangle tail[] = new Rectangle[50];

	public void createVariables ()
	{
		frame = 0;

		p = new Rectangle (300,300,50,50);
		top = new Rectangle (0,0-450,1200,500);
		bottom = new Rectangle (0,700,1200,500);
		

		mouse = 1;
		s = speed - 5;
		max = speed + 10;
		pressed = false;

		int x = 700;
		int f = 0;
		int l = 10;

		for (int i = f; i < l; i++)
		{
			b[i] = new Rectangle(400*i+x,(int)(Math.random()*250-100+i%3*250),30,250);
//			b[i] = new Rectangle(200*i+x,(int)(Math.random()*400+i%2*400),20,400);
		}

		x += 400 * l + 100;
		f += l; l = 15;

		for (int i = 0; i < l; i++)
		{
			b[f+i] = new Rectangle(400*i+x,(int)(Math.random()*250-100+i%3*250),30,350);
		}

		x += 400 * l + 200;
		f += l; l = 30;

		for (int i = 0; i < l; i++)
		{
			b[f+i] = new Rectangle(100*i+x,(int)(Math.random()*800),30,60);
		}

		x += 100 * l + 200;
		f += l; l = 30;

		for (int i = 0; i < l; i++)
		{
			b[f+i] = new Rectangle(200*i+x,(int)(Math.random()*600),150,80);
		}

		x += 200 * l + 300;
		f += l; l = 40;

		for (int i = 0; i < l; i++)
		{
			b[f+i] = new Rectangle(250*i+x,(int)(Math.random()*200+i%2*400),20,250);
		}

		x += 250 * l + 400;
		f += l; l = 15;

		for (int i = 0; i < l; i++)
		{
			b[f+i] = new Rectangle(600*i+x,(int)(/*Math.random()*/i%4*100+200),400,100);
		}

		x += 600 * l + 800;
		f += l; l = b.length - f;

		for (int i = 0; i < b.length-f; i++)
		{
			b[f+i] = new Rectangle(150*i+x,(int)(Math.random()*600),10,(int)(Math.random()*200));
//			b[f+i] = new Rectangle(300*i+x,i%2*(int)(Math.random()*100+400),10,(int)(Math.random()*200+300));
		}

		x += 150 * l + 800;
		end = new Rectangle(x,0,2000,1000);
		
		level = 1;
		high = 0;



		for (int i = 0; i < tail.length; i++)
			tail[i] = new Rectangle(p.x-i,p.y,0,0);
	}






	class Mouse implements MouseListener
	{
		public void mousePressed (MouseEvent e)
		{
			pressed = true;

			if(!start)
				start = true;
			else if (win)
			{

				int check = high;

				createVariables();

				high = check;
			}
			else if (start && lose)
			{
				try
				{
					int z = JOptionPane.showConfirmDialog(null, "Restart?");


					if (z == 0)
					{


						int check = high;
//						int begin = -b[0].x;

						createVariables();

						high = check;
//						cheat(begin);
					}
					else
					{
						p.y = 300;
//						p.x += 50;
						cheat(100);
						frame = 0;
						pressed = false;
					}

					lose = false;
					
				}catch(Exception error){}

			}


			if (e.getButton() == MouseEvent.BUTTON3)
			{
				if (!pause)
					pause = true;
				else
					pause = false;
			}

			if (e.getButton() == MouseEvent.BUTTON2)
			{
				cheat(Integer.parseInt(JOptionPane.showInputDialog("Level?")));
			}
		}
		public void mouseReleased (MouseEvent e)
		{
			pressed = false;
		}

		public void mouseEntered (MouseEvent e){}
		public void mouseExited (MouseEvent e){}
		public void mouseClicked (MouseEvent e){}
	}




	public void cheat (int z)
	{
		for (int i = 0; i < b.length; i++)
		{
			b[i].x -= z;//*level;
		}
	}







	class Game implements ActionListener
	{
		public void actionPerformed (ActionEvent e)
		{
			if (!lose && start && !pause)
			{
				frame++;

				p.y -= mouse;

				if (pressed)
					mouse += s;
				else
					mouse -= s;

				if (mouse > max)
					mouse = max;
				else if (mouse < -max)
					mouse = -max;


				for (int i = 0; i < b.length; i++)
				{
					b[i].x -= speed;//*level;

					if(p.intersects(b[i]))
						lose = true;
				}
				end.x -= speed;
				if(p.intersects(top))
					lose = true;
				else if(p.intersects(bottom))
					lose = true;

				if (frame%15 == 0)
				{
					level++;

					top.y += 1;
					bottom.y -= 1;
				}
				if (frame%80 == 0)
				{
					p.width--;
					p.height--;
				}
				
				if (p.intersects(end))
					win = true;

				if (frame > high)
					high = frame;

//				if(frame%5==0)
				moveTail();
			}
		}
	}

	public void moveTail ()
	{
		for (int i = tail.length-1; i > 0; i--)
		{
			color[i] = color[i-1];
			tail[i] = tail[i-1];
			tail[i].x -= speed;
		}
		color[0] = new Color ((int)(Math.random()*200+50),0,0);
		tail[0] = new Rectangle(p.x,p.y,p.width,p.height);
	}



	class GamePanel extends JPanel
	{
		public GamePanel()
		{
			Timer paint = new Timer (50, new Paint());
			paint.start();
		}
		class Paint implements ActionListener
		{
			public void actionPerformed (ActionEvent e)
			{
				repaint();
			}
		}





		public void paintComponent(Graphics g)
		{
			super.paintComponent(g);
			setBackground(Color.black);


			for (int i = 0; i < tail.length; i++)
			{
				g.setColor(color[i]);
				g.fillRect(tail[i].x,tail[i].y,tail[i].width,tail[i].height);
			}

			g.setColor(new Color((int)(Math.random()*200+50),0,0));//(int)(Math.random()*255),(int)(Math.random()*255)));
//			g.setColor(Color.red);
			g.fillRect(p.x, p.y, p.width, p.height);



			g.setColor(Color.yellow);
			for (int i = 0; i < b.length; i++)
			{
				g.setColor(Color.yellow);
				g.fillRect(b[i].x,b[i].y,b[i].width,b[i].height);
				g.setColor(Color.blue);
				g.drawString(i+"", b[i].x+b[i].width/3,b[i].y+b[i].height/2);
			}
			g.setColor(Color.yellow);
			g.fillRect(end.x,end.y,end.width,end.height);

			g.setColor(Color.magenta);
			g.fillRect(top.x,top.y,top.width,top.height);
			g.fillRect(bottom.x,bottom.y,bottom.width,bottom.height);

			g.setColor(Color.blue);
			g.setFont(new Font(null,Font.BOLD,30));
			g.drawString("High: "+high+"  Current: "+frame,700,40);


			if (!start)
			{
				g.setColor(Color.green);
				g.setFont(new Font(null,Font.BOLD,100));
				g.drawString("Click to start!",300,300);
			}
			else if (win)
			{
				g.setColor(Color.blue);
				g.setFont(new Font(null,Font.BOLD,300));
				g.drawString("WIN!",200,500);
				g.setColor(Color.green);
				g.setFont(new Font(null,Font.BOLD,100));
				g.drawString("Click to start",200,600);
				g.setFont(new Font(null,Font.PLAIN,30));
				g.drawString(""+b[0].x,200,40);
			}
			else if (lose)
			{
				g.setColor(Color.blue);
				g.setFont(new Font(null,Font.BOLD,300));
				g.drawString("LOSE",200,500);
				g.setColor(Color.green);
				g.setFont(new Font(null,Font.BOLD,100));
				g.drawString("Click to start",200,600);
				g.setFont(new Font(null,Font.PLAIN,30));
				g.drawString(""+b[0].x,200,40);
			}
		}
	}















	public Cave ()
	{		

		speed = 20;
		try
		{
			int z = JOptionPane.showOptionDialog(null, "Difficulty?", "Select Difficulty", 0, 
					JOptionPane.QUESTION_MESSAGE, null, new String[]{"Easy","Normal","Hard","Expert"}, null);

			speed = 10 + z*5;
//			speed = 200;
			
		}catch(Exception error){}
		

		createVariables();

		Timer game = new Timer (100, new Game());
		game.start();

		GamePanel panel = new GamePanel();
		panel.addMouseListener(new Mouse());
		Container cp = getContentPane();
		cp.add(panel);
		panel.setVisible(true);
	}
	public static void main (String[] Bridget)
	{
		Cave frame = new Cave();
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setTitle("");
		frame.setSize(1100, 750);
		frame.setLocation(0,0);
		frame.setVisible(true);
	}
}
