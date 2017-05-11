package games;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

@SuppressWarnings("serial")
public class PacMan extends JFrame
{
	/** Creates the KeyboardPanel class istance */
	private Panel keyboardPanel = new Panel();

	/** Adds the keyboardPanel to the frame and sets visible */
	public PacMan()
	{
		getContentPane().add(keyboardPanel);
		keyboardPanel.setFocusable(true);
	}

	/** Main method which creates the frame and its components */
	public static void main(String[] args)
	{
		PacMan frame= new PacMan();
		frame.setTitle("");
		frame.setLocationRelativeTo(null);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setSize(500,500);
		frame.setVisible(true);
	}
	/** JPanel class in which GUI is created */ 
	static class Panel extends JPanel
	{
		int frame = 0;
		
		
		/** 1=object 2=wall */
		int edit=0;
		/** Bridget location */
		Point b=new Point(9,13);
		/** Player location */
		Point p=new Point(2,2);
		/** Enemies locations */
		Point e[]=new Point[12];
		/** Opposite of enemies directions */
		int ed[]=new int[12];
		/** Number of enemies */
		int E;
		/** Player direction */
		int dir=0;
		/** Number of columns */
		int X;
		/** Number of rows */
		int Y;
		/** Size of grid for viewing purposes */
		int size=30;
		/** Each individual grid tile */
		int block[][];
		/** Number of objects collected */
		int item=0;
		/** Enemy movement direction */
		int move[]=new int[12];
		/** Number of objects needed to win */
		int win=0;
		boolean Win=false;
		/** True if lost */
		boolean lose=false;
		/** True if started */
		boolean start=false;
		/** Game time in half seconds */
		int time=0;
		
		int difficulty;
		
		/** Creates timers and key/mouse listeners */
		public Panel()
		{
//			difficulty=Integer.parseInt(JOptionPane.showInputDialog("Difficulty (Hardest 1 - 10 Easiest):"));
//			if(JOptionPane.showInputDialog("Default?(y/n)").equals("y"))
			{
				X=19;
				Y=23;
				size=30;
				block=new int[X][Y];

				for(int x=0;x<X;x++)
					for(int y=0;y<Y;y++)
						block[x][y]=0;

				for(int x=0;x<X;x++)
					block[x][0]=2;
				for(int x=0;x<X;x++)
					block[x][Y-1]=2;
				for(int y=0;y<Y;y++)
					block[0][y]=2;
				for(int y=0;y<Y;y++)
					block[X-1][y]=2;

				Default();
			}
//			E=0;
//			for(int i=0;i<E;i++)
//			{
//			e[0]=new Point(18,18);
//			e[1]=new Point(1,18);
//			}


			/** Real time counter */
			final Timer rTimer = new Timer(500, new RealTimer());
			/** Timer for the enemy method */
			final Timer eTimer = new Timer(/*difficulty*1*/500, new EnemyTimer());
//			eTimer.start();
			/** Timer for the repaint() method */
			Timer timer = new Timer(50, new TimerListener());
			timer.start();

			addKeyListener(new KeyAdapter() 
			{                
				public void keyPressed(KeyEvent e)
				{
					switch(e.getKeyCode())
					{
					case KeyEvent.VK_UP: 	if(start){Move(1,p); Item(); dir=1;} break;
					case KeyEvent.VK_RIGHT:	if(start){Move(2,p); Item(); dir=2;} break;
					case KeyEvent.VK_DOWN:	if(start){Move(3,p); Item(); dir=3;} break;
					case KeyEvent.VK_LEFT:	if(start){Move(4,p); Item(); dir=4;} break;

					case KeyEvent.VK_1: edit=1; break;
					case KeyEvent.VK_2: edit=2; break;
					case KeyEvent.VK_3: edit=3; break;
					}
					//repaint();
//					System.out.println(p);
				}
			}   );
			addMouseMotionListener(new MouseMotionAdapter()
			{
				public void mouseDragged(MouseEvent e)
				{
					int x=e.getX();
					int y=e.getY();

					Point m=new Point(x/size,y/size);
//					System.out.println(m);
					if(edit==1)
					{
						block[m.x][m.y]=1;
					}
					if(edit==2)
					{
						block[m.x][m.y]=2;
					}
					if(edit==3)
					{
						block[m.x][m.y]=0;
					}
				}
			});
			addMouseListener(new MouseAdapter()
			{
				public void mousePressed(MouseEvent e)
				{
					if(!start)
					{
						start=true;
						eTimer.start();
						rTimer.start();
					}
					if(edit==0)
					{
						Enemy();
					}
//					int x=e.getX();
//					int y=e.getY();

//					Point m=new Point(x/size,y/size);
////					System.out.println(m);
//					if(e.getButton()==1)
//					{
//					block[m.x][m.y]=1;
//					}
//					if(e.getButton()==2)
//					{
//					block[m.x][m.y]=2;
//					}
//					if(e.getButton()==3)
//					{
//					block[m.x][m.y]=0;
//					}
				}
			});
		}
		public void Default()
		{

			block[9][1]=2;

			block[2][2]=2;
			block[3][2]=2;
			block[5][2]=2;
			block[6][2]=2;
			block[7][2]=2;
			block[9][2]=2;
			block[11][2]=2;
			block[12][2]=2;
			block[13][2]=2;
			block[15][2]=2;
			block[16][2]=2;

			block[2][3]=2;
			block[3][3]=2;
			block[5][3]=2;
			block[6][3]=2;
			block[7][3]=2;
			block[9][3]=2;
			block[11][3]=2;
			block[12][3]=2;
			block[13][3]=2;
			block[15][3]=2;
			block[16][3]=2;

			block[2][5]=2;
			block[3][5]=2;
			block[5][5]=2;
			block[7][5]=2;
			block[8][5]=2;
			block[9][5]=2;
			block[10][5]=2;
			block[11][5]=2;
			block[13][5]=2;
			block[15][5]=2;
			block[16][5]=2;

			block[5][6]=2;
			block[9][6]=2;
			block[13][6]=2;

			block[1][7]=2;
			block[2][7]=2;
			block[3][7]=2;
			block[5][7]=2;
			block[6][7]=2;
			block[7][7]=2;
			block[9][7]=2;
			block[11][7]=2;
			block[12][7]=2;
			block[13][7]=2;
			block[15][7]=2;
			block[16][7]=2;
			block[17][7]=2;

			block[1][8]=2;
			block[2][8]=2;
			block[3][8]=2;
			block[5][8]=2;
			block[13][8]=2;
			block[15][8]=2;
			block[16][8]=2;
			block[17][8]=2;

			block[1][9]=2;
			block[2][9]=2;
			block[3][9]=2;
			block[5][9]=2;
			block[7][9]=2;
			block[8][9]=2;
			block[10][9]=2;
			block[11][9]=2;
			block[13][9]=2;
			block[15][9]=2;
			block[16][9]=2;
			block[17][9]=2;

			block[1][10]=2;
			block[2][10]=2;
			block[3][10]=2;
			block[5][10]=2;
			block[7][10]=2;
			block[11][10]=2;
			block[13][10]=2;
			block[15][10]=2;
			block[16][10]=2;
			block[17][10]=2;

			block[7][11]=2;
			block[11][11]=2;

			block[1][12]=2;
			block[2][12]=2;
			block[3][12]=2;
			block[5][12]=2;
			block[7][12]=2;
			block[8][12]=2;
			block[9][12]=2;
			block[10][12]=2;
			block[11][12]=2;
			block[13][12]=2;
			block[15][12]=2;
			block[16][12]=2;
			block[17][12]=2;

			block[1][13]=2;
			block[2][13]=2;
			block[3][13]=2;
			block[5][13]=2;
			block[13][13]=2;
			block[15][13]=2;
			block[16][13]=2;
			block[17][13]=2;

			block[1][14]=2;
			block[2][14]=2;
			block[3][14]=2;
			block[5][14]=2;
			block[7][14]=2;
			block[8][14]=2;
			block[9][14]=2;
			block[10][14]=2;
			block[11][14]=2;
			block[13][14]=2;
			block[15][14]=2;
			block[16][14]=2;
			block[17][14]=2;

			block[9][15]=2;

			block[2][16]=2;
			block[3][16]=2;
			block[5][16]=2;
			block[6][16]=2;
			block[7][16]=2;
			block[9][16]=2;
			block[11][16]=2;
			block[12][16]=2;
			block[13][16]=2;
			block[15][16]=2;
			block[16][16]=2;

			block[3][17]=2;
			block[15][17]=2;

			block[1][18]=2;
			block[3][18]=2;
			block[5][18]=2;
			block[7][18]=2;
			block[8][18]=2;
			block[9][18]=2;
			block[10][18]=2;
			block[11][18]=2;
			block[13][18]=2;
			block[15][18]=2;
			block[17][18]=2;

			block[5][19]=2;
			block[9][19]=2;
			block[13][19]=2;

			block[2][20]=2;
			block[3][20]=2;
			block[4][20]=2;
			block[5][20]=2;
			block[6][20]=2;
			block[7][20]=2;
			block[9][20]=2;
			block[11][20]=2;
			block[12][20]=2;
			block[13][20]=2;
			block[14][20]=2;
			block[15][20]=2;
			block[16][20]=2;

			block[0][11]=0;
			block[18][11]=0;

			p=new Point(9,17);
			b=new Point(9,13);

			E=12; //number of enemies
			
			e[0]=new Point(8,10);
			e[1]=new Point(9,10);
			e[2]=new Point(10,10);
			e[3]=new Point(8,11);
			e[4]=new Point(9,11);
			e[5]=new Point(10,11);

			e[6]=new Point(8,10);
			e[7]=new Point(9,10);
			e[8]=new Point(10,10);
			e[9]=new Point(8,11);
			e[10]=new Point(9,11);
			e[11]=new Point(10,11);

			for(int i=0;i<E;i++)
			{
				move[i]=1;
				ed[i]=0;
			}

			for(int x=0;x<X;x++)
				for(int y=0;y<Y;y++)
					if(block[x][y]==0)
					{
						block[x][y]=1;
						win++;
					}
		}

		/** Move method for both player and enemy
		 * 
		 * @param dir: direction it's moving
		 * @param o: point of object
		 * @return true if able to move
		 */
		public boolean Move(int dir, Point o)
		{
			if(dir==1)
			{
				o.y-=1;
				if(o.y<0)
					o.y=Y-1;
				else if(block[o.x][o.y]==2)
				{
					o.y+=1;
					return false;
				}
//				else if(o!=p)
//				{
//					for(int i=0; i<E; i++)
//					{
//						if(o!=e[i])
//						{
//							if(o.x==e[i].x && o.y==e[i].y)
//							{
////								o.y+=1;
//								return true;
//							}
//						}
//					}
//				}
			}
			else if(dir==2)
			{
				o.x+=1;
				if(o.x>=X)
					o.x=0;
				else if(block[o.x][o.y]==2)
				{
					o.x-=1;
					return false;
				}
//				else if(o!=p)
//				{
//					for(int i=0; i<E; i++)
//					{
//						if(o!=e[i])
//						{
//							if(o.x==e[i].x && o.y==e[i].y)
//							{
////								o.x-=1;
//								return true;
//							}
//						}
//					}
//				}
			}
			else if(dir==3)
			{
				o.y+=1;
				if(o.y>=Y)
					o.y=0;
				else if(block[o.x][o.y]==2)
				{
					o.y-=1;
					return false;
				}
//				else if(o!=p)
//				{
//					for(int i=0; i<E; i++)
//					{
//						if(o!=e[i])
//						{
//							if(o.x==e[i].x && o.y==e[i].y)
//							{
////								o.y-=1;
//								return true;
//							}
//						}
//					}
//				}
			}
			else if(dir==4)
			{
				o.x-=1;
				if(o.x<0)
					o.x=X-1;
				else if(block[o.x][o.y]==2)
				{
					o.x+=1;
					return false;
				}
//				else if(o!=p)
//				{
//					for(int i=0; i<E; i++)
//					{
//						if(o!=e[i])
//						{
//							if(o.x==e[i].x && o.y==e[i].y)
//							{
////								o.x+=1;
//								return true;
//							}
//						}
//					}
//				}
			}
			for(int i=0; i<E; i++)
				if(e[i].x==p.x && e[i].y==p.y && win!=item)
					if(!Win)
						lose=true;
			return true;
		}

		public void Item()
		{
			if(block[p.x][p.y]==1)
			{
				block[p.x][p.y]=0;
				item+=1;
			}
			if(win==item && p.x==b.x && p.y==b.y)// *************************************************
			{
				Win=true;
			}
		}

		public void Enemy()
		{
			for(int i=0;i<E;i++)
			{
//				System.out.println("Supposed to: "+ed[i]);

//				if(Move(ed[i],e[i])){}
				do
				{
//					System.out.println(move[i]+" "+ed[i]);

					move[i]= (int)(Math.random()*4+1);
					while(move[i]==ed[i])
					{
						move[i]= (int)(Math.random()*4+1);
					}

//					System.out.println();

//					System.out.println("ah! :"+move);
				}
				while(!Move(move[i],e[i]));

				if(move[i]==1)
					ed[i]=3;
				if(move[i]==2)
					ed[i]=4;
				if(move[i]==3)
					ed[i]=1;
				if(move[i]==4)
					ed[i]=2;

//				ed[i]=move[i];
//				System.out.println(ed[i]);
//				System.out.println();
				
			}
		}
		/** Counts in seconds */
		class RealTimer implements ActionListener
		{
			public void actionPerformed(ActionEvent e)
			{
				if(!Win)
					time+=1;
			}
		}
		/** Calls Enemy() every interval */
		class EnemyTimer implements ActionListener
		{
			public void actionPerformed(ActionEvent e)
			{
				Enemy();
			}
		}
		/** Calls repaint() every interval */
		class TimerListener implements ActionListener
		{
			public void actionPerformed(ActionEvent e)
			{
				repaint();
			}
		}

		/** Called from TimerListener; paints the frame */
		protected void paintComponent(Graphics g)
		{
			frame++;
			
			super.paintComponent(g);
			setBackground(Color.black);
			g.setColor(Color.cyan);
			g.setFont(new Font("" , Font.PLAIN, 10));

//			for(int x=0;x<=X;x++)
//			{
//				g.drawLine(x*size,0,x*size,size*Y);            	
//			}
//			for(int y=0;y<=Y;y++)
//			{
//				g.drawLine(0,y*size,size*X,y*size);
//			}

			for(int x=0;x<X;x++)
				for(int y=0;y<Y;y++)
				{
					if(block[x][y]==1)
					{
						g.setColor(Color.red);
						g.fillOval(x*size+size*3/8, y*size+size*3/8, size/4, size/4);
					}
					if(block[x][y]==2)
					{
						g.fillRoundRect(x, y, 0,0,0,0);
						g.setColor(Color.green);
//						g.fillRect(x*size, y*size, size, size);
						g.drawRoundRect(x*size, y*size, size, size, 10, 10);
					}
				}

//			g.setColor(Color.green);
			g.setColor(Color.blue);
			for(int i=0;i<E;i++)
//				g.fillRect(e[i].x*size, e[i].y*size, size, size);
				g.fillOval(e[i].x*size, e[i].y*size, size, size);
			

			g.setColor(Color.yellow);
			int px=p.x*size;
			int py=p.y*size;
//			g.fillPolygon(new int[]{px+size/2,px,px+size/2,px+size*3/4,px+size/2,px+size*3/4}, 
//					new int[]{py,py+size/2,py+size,py+size*3/4,py+size/2,py+size/4}, 6);

			if(frame%6>2)
			{
			if(dir==1)
				g.fillArc(px, py, size, size, 135, 270);
			else if(dir==2)
				g.fillArc(px, py, size, size, 45, 270);
			else if(dir==3)
				g.fillArc(px, py, size, size, 315, 270);
			else if(dir==4)
				g.fillArc(px, py, size, size, 225, 270);
			else
				g.fillOval(px, py, size, size);
			}
			else
				g.fillOval(px, py, size, size);
			
			if(frame>100000)
				frame=0;
			
			g.setColor(Color.black);

//			for(int x=0;x<X;x++)
//			for(int y=0;y<Y;y++)
//			g.drawString(x+" "+y, x*size, y*size+size);

			g.setFont(new Font("" , Font.BOLD, 200));
			g.setColor(Color.white);
			if(lose)
				g.drawString("Lose!",2*size,10*size);
			else if(Win)
				g.drawString("Win!",3*size,10*size);
			
//			g.setColor(Color.yellow);
			int bx=b.x*size;
			int by=b.y*size;
			g.fillPolygon(new int[]{bx+size/2,bx,bx+size/2,bx+size}, new int[]{by,by+size/2,by+size,by+size/2}, 4);

			g.setColor(Color.white);
			g.setFont(new Font("" , Font.BOLD, 20));
			g.drawString(item+"/"+win,size,size*3/4);
			g.drawString(""+time,size*17,size*3/4);
			}
	}
}