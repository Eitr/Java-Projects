package random;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

@SuppressWarnings("serial")
public class Fun extends JFrame
{



	int col = 1300;
	int row = 900;

	boolean [][] map = new boolean[col][row];
	int [] colors = new int[col];

	int red = (int)(Math.random()*50+50);
	int blue = (int)(Math.random()*50+50);


	void createVariables ()
	{
		int start = (int)(Math.random()*300);



		colors[0] = 200;

		int factor = 1;
		int plus = 1;

		for(int i = 1; i < col; i++)
		{
			int last = start; 

			if ((int)(Math.random()*10+1) > 8)
			{
				factor *= -1;
			}

			if ((int)(Math.random()*2 + 1) > 1)
			{
				plus = ((int)(Math.random()*  (int)(Math.random()*8)  +1));
			}

			start = last + factor*plus;

			colors[i] = colors[i-1] - factor*plus;

			if (colors [i] > 240)
				colors[i] -= 10;
			if (colors [i] < 100)
				colors[i] += 10;

			if (start < 0)
				start = 0;
			if (start >= row)
				start = row-1;

			for (int y = 0; y < start; y++)
			{
				map[i][y] = false;
			}
			
			for (int y = start; y < row; y++)
			{
				map[i][y] = true;
			}

		}

		
	}











	class Mouse implements MouseListener
	{
		public void mousePressed (MouseEvent e)
		{
			if(e.getButton() == 1)
				repaint();
			else
			{
				int mx = e.getPoint().x;
				int my = e.getPoint().y;

				
				int r = 6;
				
				for(int x = -1*r; x < r; x++)
				{
					for(int y = -1*Math.abs(x); y < Math.abs(x); y++)
					{
						map[x+mx][y+my] = false;
					}
				}
				
				repaint();
			}
		}
		public void mouseReleased (MouseEvent e){}
		public void mouseEntered (MouseEvent e){}
		public void mouseExited (MouseEvent e){}
		public void mouseClicked (MouseEvent e){}
	}
	class Keyboard implements KeyListener
	{
		public void keyPressed (KeyEvent e){}
		public void keyReleased (KeyEvent e){}
		public void keyTyped (KeyEvent e){}
	}
	class MouseMotion implements MouseMotionListener
	{
		public void mouseMoved (MouseEvent e){}
		public void mouseDragged (MouseEvent e){}
	}










	class PaintPanel extends JPanel
	{
		public void paintComponent (Graphics weirdo)
		{
			Graphics2D g = (Graphics2D)(weirdo);
			super.paintComponent(g);

			setBackground(Color.black);
			g.setColor(Color.white);
			g.setFont(new Font(null, Font.PLAIN, 20));


			for (int x = 0; x < col; x++)
			{
				g.setColor(new Color(red, colors[x], blue));
				for (int y = 0; y < row; y++)
				{
					if(map[x][y])
						g.drawRect(x,y,0,0);
				}
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
			//			repaint();
		}
	}


	public Fun ()
	{		
		createVariables();

		Timer game = new Timer (100, new Game());
		Timer paint = new Timer (100, new Paint());
		game.start();
		paint.start();

		PaintPanel paintPanel = new PaintPanel();
		paintPanel.setVisible(true);

		//		Container cp = getContentPane();
		add(paintPanel);

		addMouseListener(new Mouse());
		addMouseMotionListener(new MouseMotion());
		addKeyListener(new Keyboard());

		setTitle("");
		setSize(1100, 750);
		setLocationRelativeTo(null);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setVisible(true);
		setFocusable(true);
	}
	public static void main (String[] Bridget)
	{
		new Fun();
	}
}
