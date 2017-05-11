package crypt;

import java.awt.*;
import java.awt.event.*;
import java.io.*;

import javax.swing.*;

@SuppressWarnings("serial")
public class Crypt extends JFrame
{
	public GamePanel game= new GamePanel();
	public Crypt()
	{
		getContentPane().add(game);
		game.setFocusable(true);
	}
	static Crypt frame;
	public static void main(String[] args)
	{
		frame= new Crypt();
		frame.setTitle("Crypt");
		frame.setSize(800,600);
		frame.setLocationRelativeTo(null);
		frame.setDefaultCloseOperation(EXIT_ON_CLOSE);
		frame.setVisible(true);
	}
	private class GamePanel extends JPanel
	{
		int count;

		boolean wait;

		int edit[]=new int[2];
		boolean EDIT;
		boolean GRID;
		int TURN;

		int turn;

		/** Output display strings */
		String s[];

		/** Columns (x) */
		int C;
		/** Rows (y) */
		int R;
		/** Beginning x */
		int X;
		/** Beginning y */
		int Y;
		/** Screen x */
		int screenX;
		/** Screen y */
		int screenY;
		/** Grid size */
		int S;
		/** Show hidden block */
		boolean H;

		int maxC=100;
		int maxR=100;

		/** Terrain blocks */
		Terrain b[][];
		/** Character */
		Player c;
		Enemy enemy[]=new Enemy[100];

		Point item[]= new Point[1000];
		Point itemL[]= new Point[1000];
		int I=0;

		NPC npc[]= new NPC[100];
		int N=0;

		int E;

		int command=0;
		int option=0;
		int dir=0;

		public GamePanel()
		{
			turn=1; 
			s= new String[100];
			for(int i=0;i<s.length; i++)
				s[i]="";

			C=80;	//40
			R=60;	//30
			X=0;
			Y=0;
			screenX=5;
			screenY=5;
			S=20;	//20

			c= new Player(1,1,"Bob"/*JOptionPane.showInputDialog("Name?")*/,0/*Integer.parseInt(JOptionPane.showInputDialog("Class?\n1-Knight\n2-Archer\n3-Rogue\n4-Monk\n5-Wizzard\n6-Thief"))*/);

			E=0;
//			enemy[]= new Enemy(10,10,5,4,0,5,0,3,50,0,"blob");

			b= new Terrain[maxC][maxR];
			for(int i=0; i<maxC; i++)
				for(int j=0; j<maxR; j++)
				{
					b[i][j]=new Terrain(1,i,j,false);
				}

			for(int i=0; i<C; i++)
			{
				b[i][0]=new Terrain(0,i,0,false);
				b[i][R-1]=new Terrain(0,i,R-1,false);
			}
			for(int j=0; j<R; j++)
			{
				b[0][j]=new Terrain(0,0,j,false);
				b[C-1][j]=new Terrain(0,C-1,j,false);
			}

			/** Paint Canvas */
			Timer timer = new Timer(25, new Painting());
			timer.start();

			/** Turns Method */
			Timer timer2 = new Timer(25, new Turns());
			timer2.start();


			addMouseListener(new MouseAdapter()
			{
				public void mousePressed(MouseEvent e)
				{					
					if(EDIT)
					{
						if(e.getX()<screenX+800 && e.getX()>screenX && e.getY()<screenY+600 && e.getY()>screenY)
							if(e.getX()<C*S+screenX+X && e.getX()>screenX+X && e.getY()<R*S+screenY+Y && e.getY()>screenY+Y)
							{
//								if(e.getButton()==MouseEvent.BUTTON1)
								if(edit[0]==1)
								{
									b[((e.getX()-screenX-X)/S)][((e.getY()-screenY-Y)/S)].setType(edit[1],H);

									for(int i=0; i<C; i++)
									{
										b[i][0]=new Terrain(0,i,0,false);
										b[i][R-1]=new Terrain(0,i,R-1,false);
									}
									for(int j=0; j<R; j++)
									{
										b[0][j]=new Terrain(0,0,j,false);
										b[C-1][j]=new Terrain(0,C-1,j,false);
									}
								}
								else if(edit[0]==2)
								{

									if(e.getButton()==MouseEvent.BUTTON2)
									{
										System.out.println(((e.getX()-screenX-X)/S)+"");
//										System.out.println(enemy[i].getL().x);
										System.out.println(((e.getY()-screenY-Y)/S)+"");
//										System.out.println(enemy[i].getL().y);
									}
									if(e.getButton()==MouseEvent.BUTTON3)
									{
										for(int i=0; i<E; i++)
										{
											if(((e.getX()-screenX-X)/S)==enemy[i].getL().x && ((e.getY()-screenY-Y)/S)==enemy[i].getL().y)
											{
												E--;
												for(int j=i; j<E; j++)
												{
													enemy[j]= enemy[j+1];
												}

//												break;
											}
											else 
												System.out.println("no");
											System.out.println(((e.getX()-screenX-X)/S)+"");
											System.out.println(enemy[i].getL().x);
											System.out.println(((e.getY()-screenY-Y)/S)+"");
											System.out.println(enemy[i].getL().y);
										}
									}

									if(e.getButton()==MouseEvent.BUTTON1)
									{
										if(edit[1]==0)
										{
											enemy[E]=new Enemy(((e.getX()-screenX-X)/S),((e.getY()-screenY-Y)/S),Integer.parseInt(JOptionPane.showInputDialog("Str?")),Integer.parseInt(JOptionPane.showInputDialog("Dfn?")),Integer.parseInt(JOptionPane.showInputDialog("Wsd?")),Integer.parseInt(JOptionPane.showInputDialog("Vlr?")),Integer.parseInt(JOptionPane.showInputDialog("Agl?")),Integer.parseInt(JOptionPane.showInputDialog("Hp?")),Integer.parseInt(JOptionPane.showInputDialog("Mp?")),JOptionPane.showInputDialog("Name?"),Integer.parseInt(JOptionPane.showInputDialog("$?")),Integer.parseInt(JOptionPane.showInputDialog("Exp?")));
											E++;
										}
										else if(edit[1]==1)
										{
											enemy[E]=new Enemy(((e.getX()-screenX-X)/S),((e.getY()-screenY-Y)/S),1);
											E++;
										}
										else if(edit[1]==2)
										{
											enemy[E]=new Enemy(((e.getX()-screenX-X)/S),((e.getY()-screenY-Y)/S),2);
											E++;
										}
									}
								}
								else if(edit[0]==3)
								{
									if(e.getButton()==MouseEvent.BUTTON1)
									{
										if(edit[1]==0)
										{
											item[I]= new Point(Integer.parseInt(JOptionPane.showInputDialog("Item?")),Integer.parseInt(JOptionPane.showInputDialog("Amount?")));
											itemL[I]=new Point(((e.getX()-screenX-X)/S),((e.getY()-screenY-Y)/S));
											I++;
										}
									}
								}
								else if(edit[0]==4)
								{
									if(e.getButton()==MouseEvent.BUTTON1)
									{
										if(edit[1]==0)
										{
											npc[N]= new NPC(((e.getX()-screenX-X)/S),((e.getY()-screenY-Y)/S),JOptionPane.showInputDialog("Name?"),/*JOptionPane.showInputDialog("1?"),JOptionPane.showInputDialog("2?"),JOptionPane.showInputDialog("3?"),JOptionPane.showInputDialog("4?"),JOptionPane.showInputDialog("5?"),JOptionPane.showInputDialog("6?"),*/Integer.parseInt(JOptionPane.showInputDialog("#?")));
											N++;
										}
									}
								}
							}
					}
				}
			});

			addMouseMotionListener(new MouseMotionAdapter()
			{
				public void mouseDragged(MouseEvent e)
				{					
					if(EDIT)
					{
						if(e.getX()<screenX+800 && e.getX()>screenX && e.getY()<screenY+600 && e.getY()>screenY)
							if(e.getX()<C*S+screenX+X && e.getX()>screenX+X && e.getY()<R*S+screenY+Y && e.getY()>screenY+Y)
							{
//								if(e.getButton()==MouseEvent.BUTTON1)
								if(edit[0]==1)
								{
									b[((e.getX()-screenX-X)/S)][((e.getY()-screenY-Y)/S)].setType(edit[1],H);

									for(int i=0; i<C; i++)
									{
										b[i][0]=new Terrain(0,i,0,false);
										b[i][R-1]=new Terrain(0,i,R-1,false);
									}
									for(int j=0; j<R; j++)
									{
										b[0][j]=new Terrain(0,0,j,false);
										b[C-1][j]=new Terrain(0,C-1,j,false);
									}
								}
							}
					}
				}
			});
			addKeyListener(new KeyAdapter()
			{
				public void keyPressed(KeyEvent e)
				{
					char k=e.getKeyChar();
					if(k=='w')
					{
//						int n=0;
//						for(int i=1; i<c.N; i++)
//						if(c.I.equip[c.item[i].x]>0)
//						n++;
//						String s[]=new String[n];
//						int j=0;
//						for(int i=1; i<c.N; i++)
//						{
//						if(c.I.equip[c.item[i].x]>0)
//						{
//						s[j]=c.I.name[c.item[i].x];
//						j++;
//						}
//						}
//						System.out.println(JOptionPane.getRootFrame());
//						int o=JOptionPane.showOptionDialog(null, "", "Equip?", 0, /*JOptionPane.QUESTION_MESSAGE*/-1, null, new String[]{"hi\nbye"}, null);
//						if(o>=0)
//						{
//						option=o+1;
//						}

						option= Integer.parseInt(JOptionPane.showInputDialog("Equip?")); 
						command=10;
					}
					else if(k=='W')
					{
						option= Integer.parseInt(JOptionPane.showInputDialog("Un-Equip?")); 
						command=11;
					}
					else if(k=='e')
					{
						option= Integer.parseInt(JOptionPane.showInputDialog("Consume?"));
						command=12;
					}
					else if(k=='r')
					{
						option= Integer.parseInt(JOptionPane.showInputDialog("Read?"));
						command=14;
					}
					else if(k=='s')
					{
						command=15;
					}
					else if(k=='d')
					{
						option= Integer.parseInt(JOptionPane.showInputDialog("Drop?"));
//						option.y= Integer.parseInt(JOptionPane.showInputDialog("Quantity?"));
						command=16;
					}
					else if(k=='f')
					{
						wait=true;

//						option= Integer.parseInt(JOptionPane.showInputDialog("Fire?\n1: "+c.M.name[c.spell[1]]));

						if(c.I.attribute[c.item[c.equip[2]].x].y==7 && (c.item[c.equip[1]].x>=21 && c.item[c.equip[1]].x<31))
						{
							option=c.equip[2];
							Display("What direction?");
							command=17;
						}
						else
							wait=false;
					}
					else if(k=='t')
					{
						wait=true;

						option= Integer.parseInt(JOptionPane.showInputDialog("Throw?"));

						if(c.I.attribute[c.item[option].x].y==8 && c.equip[1]!=option)
						{
//							option=c.equip[2];
							Display("What direction?");
							command=18;
						}
						else
							wait=false;
					}
					else if(k=='z')
					{
						wait=true;
						String s="";
						for(int i=0; i<c.S-1; i++)
						{
							s+=(i+1)+": "+c.M.name[c.spell[1]]+"\n";
						}

						option= Integer.parseInt(JOptionPane.showInputDialog("Cast?\n"+s));

						if(c.spell[option]!=0)
						{
							if(c.mp>=c.M.cost[c.spell[option]])
							{
								Display("What direction?");
								command=19;
							}
							else
								Display("Not enough mp");
						}
						else
							wait=false;
					}
					else if(k=='c')
					{
						wait=true;

//						option= Integer.parseInt(JOptionPane.showInputDialog("Throw?"));

//						option=c.equip[2];
						Display("What direction?");
						command=20;
					}
					else

						switch(e.getKeyCode())
						{/*
						case KeyEvent.VK_F8: 
							int t;
							String T="";
							for(int i=1; i<7; i++)
							{
								t=0;
								for(int j=1; j<c.equip.length; j++)
									if(c.equip[j]!=0)
										if(c.I.attribute[c.item[c.equip[j]].x].y==i)
											t+=c.I.attribute[c.item[j].x].x;
								T+=" "+t;
							}
							Display(T); break;*/

						case KeyEvent.VK_NUMPAD8: if(command>=17 && command<=20){dir=1;wait=false;}else command=1; break;
						case KeyEvent.VK_NUMPAD6: if(command>=17 && command<=20){dir=2;wait=false;}else command=2; break;
						case KeyEvent.VK_NUMPAD2: if(command>=17 && command<=20){dir=3;wait=false;}else command=3; break;
						case KeyEvent.VK_NUMPAD4: if(command>=17 && command<=20){dir=4;wait=false;}else command=4; break;
						case KeyEvent.VK_NUMPAD9: if(command>=17 && command<=20){dir=5;wait=false;}else command=5; break;
						case KeyEvent.VK_NUMPAD3: if(command>=17 && command<=20){dir=6;wait=false;}else command=6; break;
						case KeyEvent.VK_NUMPAD1: if(command>=17 && command<=20){dir=7;wait=false;}else command=7; break;
						case KeyEvent.VK_NUMPAD7: if(command>=17 && command<=20){dir=8;wait=false;}else command=8; break;
						case KeyEvent.VK_NUMPAD5: {dir=0;wait=false;} command=9; break;

						case KeyEvent.VK_F9: command=102; break;
						case KeyEvent.VK_F10: command=103; break;
						case KeyEvent.VK_F11: command=104; break;
						case KeyEvent.VK_F12: command=105; break;

//						case KeyEvent.VK_NUMPAD9: R+=1; break;
//						case KeyEvent.VK_NUMPAD7: C+=1; break;
//						case KeyEvent.VK_NUMPAD3: R-=1; break;
//						case KeyEvent.VK_NUMPAD1: C-=1; break;

						/* EDIT mode */
						case KeyEvent.VK_F1: if(EDIT) {EDIT=false;s[1]="";s[2]="";s[3]="";s[4]="";s[5]="";s[6]="";}else EDIT=true; break;
//						case KeyEvent.VK_F2: if(EDIT){ if(GRID)GRID=false;else GRID=true;} break;
						case KeyEvent.VK_F3: if(EDIT)SaveMap(); break;
						case KeyEvent.VK_F4: if(EDIT)LoadMap(); break;

						case KeyEvent.VK_F2: if(EDIT) C+=Integer.parseInt(JOptionPane.showInputDialog("Add columns?")); R+=Integer.parseInt(JOptionPane.showInputDialog("Add rows?")); break;

						case KeyEvent.VK_F5: edit[0]=1; break;
						case KeyEvent.VK_F6: edit[0]=2; break;
						case KeyEvent.VK_F7: edit[0]=3; break;
						case KeyEvent.VK_F8: edit[0]=4; break;

						case KeyEvent.VK_ADD: if(EDIT)S+=1; break;
						case KeyEvent.VK_SUBTRACT: if(EDIT)S-=1; break;

						case KeyEvent.VK_ENTER: if(EDIT) { if(H)H=false;else H=true;} break;
						case KeyEvent.VK_0: edit[1]=0; break;
						case KeyEvent.VK_1: edit[1]=1; break;
						case KeyEvent.VK_2: edit[1]=2; break;
						case KeyEvent.VK_3: edit[1]=3; break;
						case KeyEvent.VK_4: edit[1]=4; break;

						case KeyEvent.VK_UP: if(EDIT)Y-= S; break;
						case KeyEvent.VK_DOWN: if(EDIT)Y+= S; break;
						case KeyEvent.VK_LEFT: if(EDIT)X-= S; break;
						case KeyEvent.VK_RIGHT: if(EDIT)X+= S; break;
						}
				}
			});
		}

		class Painting implements ActionListener
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
			setBackground(Color.black);
			int size=15;
			g.setFont(new Font("" , Font.PLAIN, size));



			if(!c.getDeath())
			{

				/* ////////////////////////////////////////////////////
				//////////////////// D R A W I N G ////////////////////
				/////////////////////////////////////////////////////*/

				/* Tiles */
				for(int i=0; i<C; i++)
					for(int j=0; j<R; j++)
						b[i][j].Draw(g, S, X, Y, screenX, screenY,H,count);

				/* Items */
				g.setColor(Color.orange);
				g.setFont(new Font("", Font.PLAIN, 18));
				for(int i=0; i<I; i++)
					g.drawString(c.I.icon[item[i].x], itemL[i].x*S+screenX+X+2, itemL[i].y*S+screenY+Y+S-2);

				/* Character */
				c.Draw(g, S, X, Y, screenX, screenY);

				/* Enemy */
				for(int i=0; i<E; i++)
					enemy[i].Draw(g, S, X, Y, screenX, screenY);

				/* NPC */
				for(int i=0; i<N; i++)
					npc[i].Draw(g, S, X, Y, screenX, screenY);

				/* Menus */
				g.setColor(Color.red);
//				g.drawRect(screenX, screenY, 800, 600);
				g.setColor(Color.magenta);
				g.drawRect(screenX, screenY+600+10, 800, 100);
				g.setColor(Color.cyan);
				g.drawRect(screenX+800+10, screenY, 200, 710);

				g.drawLine(screenX+800+10, screenY+220, screenX+800+10+200, screenY+220);
				g.drawLine(screenX+800+10, screenY+240, screenX+800+10+200, screenY+240);
				g.drawLine(screenX+800+10, screenY+280, screenX+800+10+200, screenY+280);

				g.drawLine(screenX+800+10+25, screenY+280, screenX+800+10+25, screenY+280+430);


				/* Menu Strings */
				if(EDIT)
				{
					s[1]=("Character: "+c.getL());
					s[2]=("C: "+C+"  R: "+R+"   "+(S*C)+" x "+(S*R)+"   S: "+S);
					s[3]=turn+"   "+TURN;
					s[4]="E: "+E;
					s[5]="Damage: "+c.getDamage(0);
					s[6]="Edit: "+EDIT+"  Hidden: "+H;

					if(edit[0]==1)
					{
						s[7]="0- wall";
						s[8]="1- ground";
						s[9]="2- tree";
						s[10]="3- water";
					}
					else if(edit[0]==2)
					{
						s[7]="0- custom";
						s[8]="1- blob";
						s[9]="2-";
						s[10]="3-";
					}

					s[17]="0- terrain";
					s[18]="1- enemies";
					s[19]="2- npc's";
					s[20]="3- items";

					for(int i=7; i<=10; i++)
						g.drawString(s[i],screenX+800+10+5,screenY+10+(i-6)*15);
					for(int i=17; i<=20; i++)
						g.drawString(s[i],screenX+800+10+5+100,screenY+10+(i-16)*15);
				}
				else
				{
					/* Screen panning */
					if(c.getL().x*S+X<+10*S && X<0)
						X+=S;
					if(c.getL().x*S+X>800-10*S && X>-1*C*S+40*S)
						X-=S;
					if(c.getL().y*S+Y<+10*S && Y<0)
						Y+=S;
					if(c.getL().y*S+Y>600-10*S && Y>-1*R*S+30*S)
						Y-=S;


					s[7]="Lvl: ";
					s[8]="Hp: ";
					s[9]="Mp: ";
					s[10]="Exp: ";
					s[11]="Exh: ";
					s[12]="Str: ";
					s[13]="Dfn: ";
					s[14]="Wsd: ";
					s[15]="Vlr: ";
					s[16]="Agl: ";
					s[17]="Sta: ";
					s[18]="Gp: ";


					s[19]=""+c.lvl;
					s[20]=c.hp+"/"+c.maxhp;
					s[21]=""+c.mp+"/"+c.maxmp;
					s[22]=c.exp+" / "+c.maxexp;
					s[23]=""+(int)c.exh+"%";
					s[24]=""+c.str;
					s[25]=""+c.dfn;
					s[26]=""+c.wsd;
					s[27]=""+c.vlr;
					s[28]=""+c.agl;
					s[29]=""+c.stm;
					s[30]=""+c.gp;

					for(int i=7; i<=18; i++)
						g.drawString(s[i],screenX+800+10+5,screenY+10+(i-6)*15);
					for(int i=19; i<=30; i++)
						g.drawString(s[i],screenX+800+10+5+50,screenY+10+(i-18)*15);


					s[40]="cnf";
					s[41]="psn";
					s[42]="cnf";
					s[43]="psn";
					s[44]="cnf";
					s[45]="psn";

					s[46]="hst";
					s[47]="hst";
					s[48]="hst";

					g.drawString(s[40], screenX+800+10+5, 		screenY+258);
					g.drawString(s[41], screenX+800+10+5, 		screenY+258+15);
					g.drawString(s[42], screenX+800+10+5+70, 	screenY+258);
					g.drawString(s[43], screenX+800+10+5+70, 	screenY+258+15);
					g.drawString(s[44], screenX+800+10+5+70+70, screenY+258);
					g.drawString(s[45], screenX+800+10+5+70+70, screenY+258+15);

					g.drawString(s[46], screenX+800+10+5, 		screenY+237);
					g.drawString(s[47], screenX+800+10+5+70, 	screenY+237);
					g.drawString(s[48], screenX+800+10+5+70+70, screenY+237);

					/* Items */
					for(int i=1; i<c.item.length; i++)
					{
						s[i+49]=c.getItem(i);
						g.drawString((i)+"", screenX+800+10+5, screenY+300+(i-1)*15);
						g.drawString(s[i+49], screenX+800+10+5+25+20, screenY+300+(i-1)*15);

						g.drawString(c.I.icon[c.item[i].x], screenX+800+10+5+25, screenY+300+(i-1)*15);

						for(int j=0; j<c.equip.length; j++)
						{
							if(c.equip[j]==i)
							{
								g.drawString("E", screenX+800+10+185, screenY+300+(i-1)*15);
							}
							else
								g.drawString("", screenX+800+10+185, screenY+300+(i-1)*15);							
						}
						if(c.item[i].y>9)
							g.drawString(""+c.item[i].y, screenX+800+10+167, screenY+300+(i-1)*15);	
						else if(c.item[i].y>0)
							g.drawString(""+c.item[i].y, screenX+800+10+175, screenY+300+(i-1)*15);
					}
					for(int i=0; i<c.item.length; i++)
					{
					}

				}

				g.drawString(s[1],screenX+5,screenY+600+10+15);
				g.drawString(s[2],screenX+5,screenY+600+10+2*15);
				g.drawString(s[3],screenX+5,screenY+600+10+3*15);
				g.drawString(s[4],screenX+5,screenY+600+10+4*15);
				g.drawString(s[5],screenX+5,screenY+600+10+5*15);
				g.drawString(s[6],screenX+5,screenY+600+10+6*15);

				if(!(count%10==0))
					g.drawString("\\/",screenX+400,screenY+600+10+7*15);


				/* Hidden */
				if(!EDIT)
				{
					if(b[c.getL().x][c.getL().y].getHidden())
						H=true;
					if(H)
						if(!b[c.getL().x][c.getL().y].getHidden())
							H=false;
				}
				else
				{
					/* Viewing rectagnle */
					g.setColor(Color.orange);
					g.drawRect(screenX+400-(int)(800/(20./S))/2, screenY+300-(int)(600/(20./S))/2, (int)(800/(20./S)), (int)(600/(20./S)));

				}

				/* Grid */
				if(GRID)
				{
					g.setColor(Color.yellow);
					for(int x=0; x<(800/S); x++)
						g.drawLine(screenX+x*S, screenY, screenX+x*S, screenY+600);
					for(int y=0; y<(600/S); y++)
						g.drawLine(screenX, screenY+y*S, screenX+800, screenY+y*S);
				}
			}
			else
			{
				g.setColor(Color.white);
				g.setFont(new Font("",Font.PLAIN, 200));
				g.drawString("Game", 200, 200);
				g.drawString("Over", 205, 400);
			}

		}

		class Turns implements ActionListener
		{
			public void actionPerformed(ActionEvent e)
			{
				if(!c.getDeath())
				{

					/* ////////////////////////////////////////////////////
				//////////////////// T U R N S ////////////////////////
				/////////////////////////////////////////////////////*/

					if(!wait)
					{


						/* Character turn */
						if(turn==1)
						{

							/* Movement */
							if(command>0 && command<10)
							{
								int x=0;
								int y=0;
								if(command==1)
									y=-1;
								else if(command==2)
									x=1;
								else if(command==3)
									y=1;
								else if(command==4)
									x=-1;
								else if(command==5)
								{
									x=1;
									y=-1;
								}
								else if(command==6)
								{
									x=1;
									y=1;
								}
								else if(command==7)
								{
									x=-1;
									y=1;
								}
								else if(command==8)
								{
									x=-1;
									y=-1;
								}

								if(b[c.getL().x+x][c.getL().y+y].CheckMove())
								{
									boolean move=true;
									for(int i=0; i<N; i++)
										if(npc[i].getL().x==c.getL().x+x && npc[i].getL().y==c.getL().y+y)
											move=false;
									if(move)
									{
										characterAttack(x, y);
										turn++;
									}
								}
							}
							else if(command==5)
							{
								turn++;
								c.setExhaustion(.2);
							}
							/*
							if(command==1)
							{
								if(b[c.getL().x][c.getL().y-1].Check())
								{
									for(int i=0; i<N; i++)

										if(npc[i].getL().x==c.getL().x && npc[i].getL().y==c.getL().y-1)
										{

										}
									characterAttack(0, -1);
									turn++;
								}
							}
							else if(command==2)
							{
								if(b[c.getL().x+1][c.getL().y].Check())
								{
									characterAttack(1, 0);
									turn++;
								}
							}
							else if(command==3)
							{
								if(b[c.getL().x][c.getL().y+1].Check())
								{
									characterAttack(0, 1);
									turn++;
								}
							}
							else if(command==4)
							{
								if(b[c.getL().x-1][c.getL().y].Check())
								{
									characterAttack(-1, 0);
									turn++;
								}
							}
							else if(command==5)
							{
								turn++;
								c.setExhaustion(.2);
							}
							else if(command==6)
							{
								if(b[c.getL().x+1][c.getL().y-1].Check())
								{
									characterAttack(1, -1);
									turn++;
								}
							}
							else if(command==7)
							{
								if(b[c.getL().x+1][c.getL().y+1].Check())
								{
									characterAttack(1, 1);
									turn++;
								}
							}
							else if(command==8)
							{
								if(b[c.getL().x-1][c.getL().y+1].Check())
								{
									characterAttack(-1, 1);
									turn++;
								}
							}
							else if(command==9)
							{
								if(b[c.getL().x-1][c.getL().y-1].Check())
								{
									characterAttack(-1, -1);
									turn++;
								}
							} */
							/* Equip */
							else if(command==10)
							{
								if(c.item[option].x!=0 && c.equip[c.I.equip[c.item[option].x]]==0 && c.I.equip[c.item[option].x]>0)
								{
									c.equip[c.I.equip[c.item[option].x]]=option;
								}
							}
							/* Un-Equip */
							else if(command==11)
							{
								if(c.item[option].x!=0 && c.equip[c.I.equip[c.item[option].x]]==option)
								{
									c.equip[c.I.equip[c.item[option].x]]=0;
								}
							}
							/* Consume */
							else if(command==12)
							{
								if(c.item[option].x!=0 && c.I.attribute[c.item[option].x].y>=10 && c.I.attribute[c.item[option].x].y<=16)
								{
									dir=c.I.attribute[c.item[option].x].y;
									if(dir==10)
										c.hp+=c.I.attribute[c.item[option].x].x;
									else if(dir==11)
										c.mp+=c.I.attribute[c.item[option].x].x;
									else if(dir==12)
										c.exh-=c.I.attribute[c.item[option].x].x;
									else if(dir==13)
									{
										c.hp+=c.I.attribute[c.item[option].x].x;
										c.exh-=c.I.attribute[c.item[option].x].x;
									}
									else if(dir==14)
									{
										c.mp+=c.I.attribute[c.item[option].x].x;
										c.exh-=c.I.attribute[c.item[option].x].x;
									}
									else if(dir==15)
									{
										c.hp+=c.I.attribute[c.item[option].x].x;
										c.mp+=c.I.attribute[c.item[option].x].x;
									}
									else if(dir==16)
									{
										c.hp+=c.I.attribute[c.item[option].x].x;
										c.mp+=c.I.attribute[c.item[option].x].x;
										c.exh-=c.I.attribute[c.item[option].x].x;
									}
									c.removeItem(option, 1);
								}
							}
							/* Use */
							else if(command==13)
							{

							}
							/* Read */
							else if(command==14)
							{
								if(c.item[option].x!=0 && c.I.attribute[c.item[option].x].y==9)
								{
									c.setSpell(c.I.attribute[c.item[option].x].x);
									c.removeItem(option, 1);
								}
							}
							/* Pick-Up */
							else if(command==15)
							{
								for(int i=0; i<I; i++)
								{
									if(c.getL().x==itemL[i].x && c.getL().y==itemL[i].y)
									{
										if(c.I.attribute[item[i].x].y==17)
										{
											c.gp+=item[i].y;

											removeItem(i);
											turn++;
											break;
										}
										else if(c.setItem(item[i].x, item[i].y))
											Display("You're carrying too much.");
										else
										{
											removeItem(i);
											turn++;
											break;
										}
									}
								}
							}
							/* Drop */
							else if(command==16)
							{
								if(c.item[option].x!=0)
								{
									setItem(c.item[option].x,1,c.getL().x,c.getL().y);
									c.removeItem(option, 1);
									turn++;
								}
							}
							/* Shoot */
							else if(command==17)
							{
								int x=0;
								int y=0;
								boolean hit=false;
								for(int j=1; j<=c.I.distance[c.item[option].x]; j++)
								{
									x=0;
									y=0;

									if(dir==1)
										y=-j;
									else if(dir==2)
										x=j;
									else if(dir==3)
										y=j;
									else if(dir==4)
										x=-j;
									else if(dir==5)
									{
										x=j*2/3;
										y=-j*2/3;
									}
									else if(dir==6)
									{
										x=j*2/3;
										y=j*2/3;
									}
									else if(dir==7)
									{
										x=-j*2/3;
										y=j*2/3;
									}
									else if(dir==8)
									{
										x=-j*2/3;
										y=-j*2/3;
									}

									if(characterDistantAttack(x,y,option,1))
									{
										hit=true;
										break;
									}
//									if(hit)
//									break;
								}
								if(!hit)
								{
									setItem(c.item[option].x, 1,c.getL().x+x,c.getL().y+y);
									c.removeItem(option,1);
								}
								turn++;
							}
							/* Throw */
							else if(command==18)
							{
								int x=0;
								int y=0;
								boolean hit=false;
								for(int j=1; j<=c.I.distance[c.item[option].x]; j++)
								{
									x=0;
									y=0;

									if(dir==1)
										y=-j;
									else if(dir==2)
										x=j;
									else if(dir==3)
										y=j;
									else if(dir==4)
										x=-j;
									else if(dir==5)
									{
										x=j*2/3;
										y=-j*2/3;
									}
									else if(dir==6)
									{
										x=j*2/3;
										y=j*2/3;
									}
									else if(dir==7)
									{
										x=-j*2/3;
										y=j*2/3;
									}
									else if(dir==8)
									{
										x=-j*2/3;
										y=-j*2/3;
									}

									if(characterDistantAttack(x,y,option,2))
									{
										hit=true;
										break;
									}
									if(hit)
										break;
								}
								if(!hit)
								{
									setItem(c.item[option].x, 1,c.getL().x+x,c.getL().y+y);
									c.removeItem(option,1);
								}
							}
							/* Cast */
							else if(command==19)
							{
								if(c.M.attribute[c.spell[option]].y==1)
								{
									c.mp-=c.M.cost[c.spell[option]];
									if(dir==1)
									{
										for(int y=1; y<=c.M.distance[c.spell[option]]; y++)
										{
											for(int i=0; i<E; i++)
												characterMagicAttack(i,0,-y);
										}
									}
									else if(dir==2)
									{
										for(int x=1; x<=c.M.distance[c.spell[option]]; x++)
										{
											for(int i=0; i<E; i++)
												characterMagicAttack(i,x,0);
										}
									}
									else if(dir==3)
									{
										for(int y=1; y<=c.M.distance[c.spell[option]]; y++)
										{
											for(int i=0; i<E; i++)
												characterMagicAttack(i,0,y);
										}
									}
									else if(dir==4)
									{
										for(int x=1; x<=c.M.distance[c.spell[option]]; x++)
										{
											for(int i=0; i<E; i++)
												characterMagicAttack(i,-x,0);
										}
									}

								}

								turn++;

							}
							/* Talk */
							else if(command==20)
							{
								int i;
								boolean talk=false;
								for(i=0; i<N; i++)
								{
									if(dir==1)
										if(npc[i].getL().x==c.getL().x && npc[i].getL().y==c.getL().y-1)
											talk=true;
									if(dir==2)
										if(npc[i].getL().x==c.getL().x+1 && npc[i].getL().y==c.getL().y)
											talk=true;
									if(dir==3)
										if(npc[i].getL().x==c.getL().x && npc[i].getL().y==c.getL().y+1)
											talk=true;
									if(dir==4)
										if(npc[i].getL().x==c.getL().x-1 && npc[i].getL().y==c.getL().y)
											talk=true;
									if(dir==5)
										if(npc[i].getL().x==c.getL().x+1 && npc[i].getL().y==c.getL().y-1)
											talk=true;
									if(dir==6)
										if(npc[i].getL().x==c.getL().x+1 && npc[i].getL().y==c.getL().y+1)
											talk=true;
									if(dir==7)
										if(npc[i].getL().x==c.getL().x-1 && npc[i].getL().y==c.getL().y+1)
											talk=true;
									if(dir==8)
										if(npc[i].getL().x==c.getL().x-1 && npc[i].getL().y==c.getL().y-1)
											talk=true;
									if(talk)
										break;
								}
								if(talk)
								{
									String s[]=npc[i].Print();
									Display(npc[i].name+":");
									for(int j=0; j<npc[i].I; j++)
									{
										Display(s[j]);
									}
								}
							}
							/* Stats */
							else if(command==98)
							{

							}

							/* Info */
							else if(command==99)
							{

							}
							/* Cheat */
							else if(command==100)
							{

							}
							/* Options */
							else if(command==101)
							{

							}
							/* Save */
							else if(command==102)
							{
								SaveCharacter();
							}
							/* Load */
							else if(command==103)
							{
								LoadCharacter();
							}
							/* Help */
							else if(command==104)
							{
//								JOptionPane.showMessageDialog(null, "hi", "Help", JOptionPane.PLAIN_MESSAGE);
								int o=JOptionPane.showOptionDialog(null, "", "Help", 0, JOptionPane.QUESTION_MESSAGE, null, new String[]{"Commands","2","hi"}, 0);
								Integer.parseInt("");
								if(o==0)
								{
									JOptionPane.showMessageDialog(null,"hi","Commands",-1);
								}
							}
							/* Quit */
							else if(command==105)
							{
								if(frame!=null)
								{
									frame.dispose();
								}
								try
								{
									System.exit(0);
								}
								catch(Exception e1){}
							}
						}







						/* Enemy turn */
						else if(turn>=2 && turn<=E+1)
						{
							for(int i=0; i<E; i++)
							{
								if(!enemy[i].getDeath())
								{
									double x=0;
									double y=0;
									if(TURN%3!=0 && enemy[i].getL().distance(c.getL())<10)
									{
										x=enemy[i].getL().x-c.getL().x;
										y=enemy[i].getL().y-c.getL().y;


										x=Math.round((double)x/(Math.abs(x)*-1));
										y=Math.round((double)y/(Math.abs(y)*-1));
										/*
										if(Math.abs(x)>Math.abs(y))
										{
											x/=Math.abs(x)*-1;
											y=0;
										}
										else
										{
											y/=Math.abs(y)*-1;
											x=0;
										}
										 */
										if(b[enemy[i].getL().x+(int)x][enemy[i].getL().y+(int)y].CheckMove())
										{
											boolean move=true;
											for(int j=0; j<E; j++)
												if(enemy[j].getL().x==enemy[i].getL().x+x && enemy[j].getL().y==enemy[i].getL().y+y)
													move=false;
											if(move)
											{
												enemyAttack((int)x, (int)y, i);
											}
											turn++;

										}
										else
										{
											x=(Math.random()*2-1);
											y=(Math.random()*2-1);
//											if(Math.abs(x)==1 && Math.abs(y)==1)
											{
												if(Math.abs(x)>Math.abs(y))
												{
													if(Math.abs(x)>.75)
														y=0;
												}
												else
												{
													if(Math.abs(y)>.75)
														x=0;
												}

												x=(int)Math.round(Math.random()*2-1);
												y=(int)Math.round(Math.random()*2-1);
											}
											if(b[enemy[i].getL().x+(int)x][enemy[i].getL().y+(int)y].CheckMove())
											{
												boolean move=true;
												for(int j=0; j<E; j++)
													if(enemy[j].getL().x==enemy[i].getL().x+x && enemy[j].getL().y==enemy[i].getL().y+y)
														move=false;
												if(move)
												{
													enemyAttack((int)x, (int)y, i);
												}
												turn++;
											}
											else
												turn++;
										}
									}
									else
									{
										x=(int)Math.round(Math.random()*2-1);
										y=(int)Math.round(Math.random()*2-1);
//										if(Math.abs(x)==1 && Math.abs(y)==1)
										{
											if(Math.abs(x)>Math.abs(y))
											{
												if(Math.abs(x)>.75)
													y=0;
											}
											else
											{
												if(Math.abs(y)>.75)
													x=0;
											}

											x=(int)Math.round(Math.random()*2-1);
											y=(int)Math.round(Math.random()*2-1);
										}
										if(b[enemy[i].getL().x+(int)x][enemy[i].getL().y+(int)y].CheckMove())
										{
											boolean move=true;
											for(int j=0; j<E; j++)
												if(enemy[j].getL().x==enemy[i].getL().x+x && enemy[j].getL().y==enemy[i].getL().y+y)
													move=false;
											if(move)
											{
												enemyAttack((int)x, (int)y, i);
											}
											turn++;
										}
										else
											turn++;
									}

								}
								else
									turn++;
							}
						}
						if(turn>1+E)
						{
							turn=1;
							TURN++;
							if(TURN%25==0)
								if(c.hp<c.maxhp)
									c.hp++;
						}

						if(count>=1000000000)
							count=0;

//						if(command!=19)
						{
							command=0;
//							option=0;
						}
						dir=0;
						c.Update();
					}
				}
			}
		}

		public void characterAttack(int x, int y)
		{
			if(E==0)
			{
				c.Move(x,y);
				c.setExhaustion(.5);
			}
			else
				for(int i=0; i<E; i++)
				{
					if(enemy[i].getL().x == (c.getL().x+x) && enemy[i].getL().y==(c.getL().y+y) && !enemy[i].getDeath())
					{
						if(!c.getMiss(enemy[i].agl))
						{
							Display("You attack the "+enemy[i].getName()+": Hit!");
							enemy[i].setDamage(c.getDamage(enemy[i].dfn));
							Kill(i);
							c.setExhaustion(1.25);
						}
						else
						{
							Display("You attack the "+enemy[i].getName()+": Miss!");
							c.setExhaustion(.75);
						}

						i=E;
					}
					else if(i==E-1)
					{
						c.Move(x,y);
						c.setExhaustion(.5);
					}
				}
		}
		public void characterMagicAttack(int i,int x, int y)
		{
			if(enemy[i].getL().x==c.getL().x+x && enemy[i].getL().y==c.getL().y+y)
			{
				if(!c.getMiss(enemy[i].agl))
				{
					Display("You cast the spell: It hits the "+enemy[i].name+"!");
					enemy[i].setDamage(c.getMagicDamage(option));
					Kill(i);
				}
				else
					Display("You cast the spell: It misses the "+enemy[i].name+"!");
			}
		}
		public boolean characterDistantAttack(int x,int y, int t, int o)
		{
//			for(int i=0; i<C; i++)
//			for(int j=0; j<R; j++)
//			if(b[i][j].getL().x==c.getL().x+x && b[i][j].getL().y==c.getL().y+y)
			{
				if(!b[c.getL().x+x][c.getL().y+y].CheckThrow())
				{
					if(y>0)
						y-=1;
					else if(y<0)
						y+=1;
					if(x>0)
						x-=1;
					else if(x<0)
						x+=1;

					if(b[c.getL().x+x][c.getL().y+y].getType()==3)
					{
						c.removeItem(option,1);
//						removeItem(I-1);
						Display("Bloop");
						return true;
					}
					
					setItem(c.item[option].x, 1,c.getL().x+x,c.getL().y+y);
					c.removeItem(option,1);
					return true;
				}
				else if(b[c.getL().x+x][c.getL().y+y].getType()==3 && (x==c.I.distance[c.item[option].x] || y==c.I.distance[c.item[option].x]))
				{
					c.removeItem(option,1);
//					removeItem(I-1);
					Display("Bloop");
					return true;
				}
			}

			for(int i=0; i<E; i++)
				if(enemy[i].getL().x==c.getL().x+x && enemy[i].getL().y==c.getL().y+y)
				{
					if(!c.getMiss(enemy[i].agl))
					{
						Display("It hits the "+enemy[i].name+"!");
						if(o==1)
							enemy[i].setDamage(c.I.attribute[c.item[option].x].x);
						else if(o==2)
							enemy[i].setDamage(c.getDamage(enemy[i].dfn-c.I.attribute[c.item[option].x].x));
						Kill(i);
					}
					else
						Display("It misses the "+enemy[i].name+"!");

					setItem(c.item[option].x, 1,c.getL().x+x,c.getL().y+y);
					c.removeItem(option,1);

					return true;
				}
			return false;
		}
		public void enemyAttack(int x, int y, int i)
		{
			if(c.getL().x == (enemy[i].getL().x+x) && c.getL().y == (enemy[i].getL().y+y))
			{
				if(!enemy[i].getMiss(c.agl))
				{
					Display("The "+enemy[i].getName()+" attacks: Hit!");
					c.setDamage(enemy[i].str);
				}
				else
					Display("The "+enemy[i].getName()+" attacks: Miss!");
			}
			else
			{
				enemy[i].Move(x,y);
			}
		}
		public void Kill(int i)
		{
			if(enemy[i].getDeath())
			{
				Display("You kill the "+enemy[i].getName()+".");
				setItem(361,enemy[i].gp, enemy[i].getL().x,enemy[i].getL().y);
//				c.gp+=enemy[i].gp;
				c.setExperience(enemy[i].exp);

				setItem(enemy[i].item.x,enemy[i].item.y,enemy[i].getL().x,enemy[i].getL().y);


				E--;
				for(int j=i; j<E; j++)
				{
					enemy[j]= enemy[j+1];
				}
			}
		}
		public void setItem(int i, int num, int x, int y)
		{
			if(i==0)
				return;
			boolean store=false;
			for(int j=0; j<I; j++)
			{
				if(c.I.stack[i])
				{
					if(x==itemL[j].x && y==itemL[j].y)
					{
						if(i==item[j].x)
						{
							item[j]= new Point(i,num+item[j].y);
							store=true;
							break;
						}
					}
				}
			}
			if(!store)
			{
				if(c.I.stack[i])
				{
					item[I]=new Point(i,num);
					itemL[I]=new Point(x,y);
					I++;
				}
				else
					for(int j=0; j<num; j++)
					{
						item[I]=new Point(i,1);
						itemL[I]=new Point(x,y);
						I++;
					}
			}

		}
		public void removeItem(int i)
		{
			I--;
			for(int j=i; j<I; j++)
			{
				item[j]= item[j+1];
				itemL[j]= itemL[j+1];
			}
		}
		public void Display(String string)
		{
			s[1]=s[2];
			s[2]=s[3];
			s[3]=s[4];
			s[4]=s[5];
			s[5]=s[6];
			s[6]=string;
		}
		public void SaveCharacter()
		{
			try 
			{
				JFileChooser chooser = new JFileChooser();
				if(chooser.showSaveDialog(null) == JFileChooser.APPROVE_OPTION)
				{
					File selected = chooser.getSelectedFile();
					DataOutputStream output = new DataOutputStream(new FileOutputStream(selected));

					output.writeUTF(c.name);

					output.writeInt(13);

					output.close();
				}
			}
			catch(IOException e) {}
		}
		public void LoadCharacter()
		{
			try 
			{
				JFileChooser chooser = new JFileChooser();
				if(chooser.showOpenDialog(null) == JFileChooser.APPROVE_OPTION)
				{
					File selected = chooser.getSelectedFile();
					DataInputStream input = new DataInputStream(new FileInputStream(selected));

					c.name=input.readUTF();

					Display(input.readInt()+"");

					input.close();
				}
			}
			catch(IOException e) {}
		}
		public void SaveMap()
		{
			try 
			{
				JFileChooser chooser = new JFileChooser();
				if(chooser.showSaveDialog(null) == JFileChooser.APPROVE_OPTION)
				{
					File selected = chooser.getSelectedFile();
					DataOutputStream output = new DataOutputStream(new FileOutputStream(selected));

					output.writeInt(C);
					output.writeInt(R);

					for(int i=0; i<C; i++)
						for(int j=0; j<R; j++)
						{
							output.writeInt(b[i][j].getType());
							output.writeBoolean(b[i][j].getHidden());
						}
					output.writeInt(E);
					for(int i=0; i<E; i++)
					{
						output.writeInt(enemy[i].getL().x);
						output.writeInt(enemy[i].getL().y);
						output.writeInt(enemy[i].type);
					}

					output.writeInt(I);
					for(int i=0; i<I; i++)
					{
						output.writeInt(item[i].x);
						output.writeInt(item[i].y);
						output.writeInt(itemL[i].x);
						output.writeInt(itemL[i].y);
					}

					output.close();
				}
			}
			catch(IOException e) {}
		}
		public void LoadMap()
		{
			try 
			{
				JFileChooser chooser = new JFileChooser();
				if(chooser.showOpenDialog(null) == JFileChooser.APPROVE_OPTION)
				{
					File selected = chooser.getSelectedFile();
					DataInputStream input = new DataInputStream(new FileInputStream(selected));


					C=input.readInt();
					R=input.readInt();

					for(int i=0; i<maxC; i++)
						for(int j=0; j<maxR; j++)
						{
							b[i][j]=new Terrain(1,i,j,false);
						}

					for(int i=0; i<C; i++)
						for(int j=0; j<R; j++)
						{
							b[i][j].setType(input.readInt(),input.readBoolean());
						}


					for(int i=0; i<C; i++)
					{
						b[i][0]=new Terrain(0,i,0,false);
						b[i][R-1]=new Terrain(0,i,R-1,false);
					}
					for(int j=0; j<R; j++)
					{
						b[0][j]=new Terrain(0,0,j,false);
						b[C-1][j]=new Terrain(0,C-1,j,false);
					}

					E=input.readInt();
					for(int i=0; i<E; i++)
					{
						enemy[i]=new Enemy(input.readInt(),input.readInt(),input.readInt());
					}
					I=input.readInt();
					for(int i=0; i<I; i++)
					{
						item[i]=new Point(input.readInt(),input.readInt());
						itemL[i]=new Point(input.readInt(),input.readInt());
					}

					input.close();
				}
			}
			catch(IOException e) {}
		}
	}
}
