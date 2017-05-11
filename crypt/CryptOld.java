package crypt;

import java.awt.*;
import java.awt.event.*;

import javax.swing.*;
import java.awt.Color;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

public class CryptOld extends JFrame
{
	private static final long serialVersionUID = 1L;

	private KeyboardPanel keyboardPanel = new KeyboardPanel();

	public CryptOld()
	{
		getContentPane().add(keyboardPanel);
		keyboardPanel.setFocusable(true);
	}

	public static void main(String[] args)
	{
		CryptOld frame= new CryptOld();
		frame.setTitle("");
		frame.setLocationRelativeTo(null);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setSize(200,200);
		frame.setVisible(true);
	}

	static class KeyboardPanel extends JPanel
	{
		private static final long serialVersionUID = 1L;

		
		///////// Other / //////////////
		
		/** Frame Counter */
		int count;
		/** File name to save/load */
		String file= "test";		
		
		///////// Editing  /////////////
		
		boolean edit=true;
		/** Grid Size */
		int s=16;
		/** Sreen Y Minimum */
		final int Y1;
		/** Sreen X Minimum */
		final int X1;
		/** Number of Columns (by s) */
		final int C;
		/** Number of Rows (by s) */
		final int R;
		/** Mouse location (x/y) */
		Point m;
		/** Mouse location (grid) */
		Point M;
		/** Editing mode
		 * 		0: none
		 * 		1: block
		 * 		2: chara		 */
		int mode=0;
		
		///////// Game  //////////////
		
		/** Fog of LOS */
		int b[][];
		/** Wall */
		boolean B[][];
		/** Gold */
		int gold=0;
		boolean G[];
		
		///////// Character  /////////
		
		/** Character location (x/y) */
		int x=0+s/4,y=s-s/4;
		/** Character location (grid) */
		int X=3, Y=3;
		/** Character Sprite */
		char chara='x';
		
		///////// Statistics  /////////
		
		int strength;
		int wisdom;
		int agility;
		int hp;
		int maxhp;
		int mp;
		int maxmp;

		

		public KeyboardPanel()
		{

			Y1=10;
			X1=15;
			x+=X1;
			y+=Y1;
			C=1000/s;
			R=500/s;
			b= new int[C][R];
			B= new boolean[C][R];
			m=new Point(0,0);
			M=new Point(0,0);

			Timer timer = new Timer(50, new TimerListener());
			timer.start();

			addMouseListener(new MouseAdapter()
			{				
				public void mousePressed(MouseEvent e)
				{
					m=new Point(e.getX(),e.getY());
					M=new Point((m.x-X1)/s,(m.y-Y1)/s);

					if(mode==1)
					{
						if(e.getButton()==MouseEvent.BUTTON1)
							B[M.x][M.y]=true;
						if(e.getButton()==MouseEvent.BUTTON3)
							B[M.x][M.y]=false;
					}
					if(mode==2)
					{

						X=M.x;
						Y=M.y;
					}
				}
			});

			addKeyListener(new KeyAdapter() 
			{

				public void keyPressed(KeyEvent e)
				{
					switch(e.getKeyCode())
					{
//					case KeyEvent.VK_UP: y-=s; break;
//					case KeyEvent.VK_DOWN: y+=s; break;
//					case KeyEvent.VK_LEFT: x-=s; break;
//					case KeyEvent.VK_RIGHT: x+=s; break;              

					case KeyEvent.VK_UP: Y-=1; 
//					b[X][Y]=1; 
					if(B[X][Y])Y+=1; break;
					case KeyEvent.VK_DOWN: Y+=1; 
//					b[X][Y]=1; 
					if(B[X][Y])Y-=1; break;
					case KeyEvent.VK_LEFT: X-=1;
//					b[X][Y]=1; 
					if(B[X][Y])X+=1; break;
					case KeyEvent.VK_RIGHT: X+=1; 
//					b[X][Y]=1; 
					if(B[X][Y])X-=1; break;    


					case KeyEvent.VK_0: mode=0; break;
					case KeyEvent.VK_1: mode=1; break;
					case KeyEvent.VK_2: mode=2; break;
					case KeyEvent.VK_F1: edit=false; b[X][Y]=1; break;
					case KeyEvent.VK_F2: edit=true; 
					for(int i=0; i<C; i++)for(int j=0; j<R; j++)b[i][j]=0; break;
					case KeyEvent.VK_F3: try {	Save();} catch (IOException e1) {} break;
					case KeyEvent.VK_F4: try {Load();} catch (IOException e2) {} break;

					}
					//repaint();
				}
			}   );
		}

		class TimerListener implements ActionListener
		{
			public void actionPerformed(ActionEvent e)
			{
				repaint();
			}
		}

		protected void paintComponent(Graphics g)
		{
			count++;
			super.paintComponent(g);
			setBackground(Color.lightGray);
			g.setFont(new Font("" , Font.PLAIN, s));


			g.setColor(Color.red);
			for(int i=0; i<C+1; i++)
				g.drawLine(i*s+X1, 0+Y1, i*s+X1, R*s+Y1);
			for(int j=0; j<R+1; j++)
				g.drawLine(0+X1, j*s+Y1, C*s+X1, j*s+Y1);

			g.setColor(Color.yellow);
			g.drawString(""+chara, X*s+s/4+X1, Y*s-s/4+Y1+s);



			
			for(int i=0; i<C; i++)
				for(int j=0; j<R; j++)
				{
					if(b[i][j]==1 || b[i][j]==2)
						b[i][j]=2;
					else 
						b[i][j]=0;
				}
			
			int size= 2;
			for(int i=-1*size; i<=size; i++)
			{
				if(Math.abs(i)>=1)
				{
					for(int j=-1*Math.abs(Math.abs(i)-size) ; j<=Math.abs(Math.abs(i)-size); j++)
						b[X+i][Y+j]=1;
				}
				else 
				{
					for(int j=-1*size; j<=size; j++)
					{
						b[X][Y+j]=1;
					}
				}
			}
				
//			b[X][Y]=1;
//			b[X-1][Y]=1;
//			b[X][Y-1]=1;
//			b[X+1][Y]=1;
//			b[X][Y+1]=1;
			
			
			
			
			if(edit)
			{
				g.drawString(m.x+"  "+m.y, 25, 550);
				g.drawString(M.x+"  "+M.y, 25, 575);
				g.drawString(X+"  "+Y, 25, 600);


				g.setColor(Color.darkGray);
				for(int i=0; i<C; i++)
					for(int j=0; j<R; j++)
						if(B[i][j])
							g.fillRect(i*s+X1,j*s+Y1,s,s);

			}
			else
			{

				g.setColor(Color.gray);
				for(int i=0; i<C; i++)
					for(int j=0; j<R; j++)
						if(b[i][j]==2)
							g.fillRect(i*s+X1,j*s+Y1,s,s);


				g.setColor(Color.BLUE);
				for(int i=0; i<C; i++)
					for(int j=0; j<R; j++)
						if(B[i][j])
							g.fillRect(i*s+X1,j*s+Y1,s,s);

				g.setColor(Color.darkGray);
				for(int i=0; i<C; i++)
					for(int j=0; j<R; j++)
						if(b[i][j]==0)
							g.fillRect(i*s+X1,j*s+Y1,s,s);
			}

		}

		
		
		
		
		
		public void Save() throws IOException
		{
			DataOutputStream output = new DataOutputStream(new FileOutputStream("C:/Documents and Settings/Owner/Desktop/"+file+".txt"));

			output.writeInt(X);
			output.writeInt(Y);

			for(int i=0; i<C; i++)
				for(int j=0; j<R; j++)
					output.writeBoolean(B[i][j]);

			output.close();
		}
		public void Load() throws IOException
		{
			DataInputStream input = new DataInputStream(new FileInputStream("C:/Documents and Settings/Owner/Desktop/"+file+".txt"));

			X=input.readInt();
			Y=input.readInt();

			for(int i=0; i<C; i++)
				for(int j=0; j<R; j++)
					B[i][j]=input.readBoolean();

			input.close();
		}
	}

}
