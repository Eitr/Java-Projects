package blarg;

import java.awt.*;
import java.awt.event.*;
import java.io.*;

import javax.swing.*;

@SuppressWarnings("serial")
public class Main extends JFrame
{
	/** Creates the GUI (Graphic User Interface) class instance */
	private KeyboardPanel keyboardPanel = new KeyboardPanel();

	/** Adds the keyboardPanel component to the frame and sets visible */
	public Main()
	{
		getContentPane().add(keyboardPanel);
		keyboardPanel.setFocusable(true);
	}

	/** JPanel class in which GUI is created */ 
	static class KeyboardPanel extends JPanel
	{
		/*/ GRAPHICS /*/

		/** Viewing size of grid */
		int s=20;
		/** Max # of columns in grid */
		int column=50;
		/** Max # of rows in grid */
		int row=30;

		/** Strictly editing strings (for now) */
		String string[]=new String[10];

		/*/ EDITING /*/

		int EDIT=0;
		/** If edit mode is on */
		boolean edit=false;

		/**
		 * Editing mode
		 * 0	Terrain
		 * 1	Enemy
		 * 2	Character
		 * 3	Item
		 */
		int eMode=0;

		/** Mouse location on screen */
		Point m = new Point(0,0);
		/** Mouse button */
		int mButton=0;


		/*/ GAME /*/

		/** Game commands */
		int cmd;
		/** Frame counter */
		int frame=0;
		/** Individual terrain blocks */
		Terrain T[][]=new Terrain[column][row];
		/** Character class */
		Character C;
		/** Determines if character is inside a building */
		boolean hidden=false;
		/** Stages of gameplay
		 * 0	Testing?
		 * 1	Character creation
		 * 2
		 * 3	Inventory
		 * 4	Attributes
		 * 5	Skills
		 * 6	Options
		 */
		int phase;

		/** Attribute buttons */
		Rectangle button[]=new Rectangle[23];


		Point attribute		= new Point(1,7);//6
		Point create		= new Point(7,13);//6
		Point profession	= new Point(13,19);//6
		Point ok			= new Point(0,1);//1
		Point gui		= new Point(19,23);//4

		/**	
		 * Condition of capacity
		 * 0	normal
		 * 1	burdened
		 * 2	stressed
		 * 3	strained?
		 */
		int capacity=0;
//		/**
//		* Condition of hunger
//		* 0	normal
//		* 1	?
//		*/
//		int fatigue=0;


		Enemy E[]=new Enemy[21];
		int numE=0;

		/** Determines whose turn it is
		 * 1	Character
		 * 2	Enemies
		 */
		int turn=1;
		/** Total number of moves made in game */
		int moves=0;

		/** Any damage dealt is stored here */
		int dam;

		/** List of items in game */
		Item I[] = new Item[100];
		int numI=0;

		/** List of items in inventory */
		Item N[] = new Item[100];
		int numN=0;



		/** Creates timers and key/mouse listeners */
		public KeyboardPanel()
		{
			for(int i=0; i<10; i++)
				string[i]="";

			C = new Character(1,1);

			cmd=0;

			for(int x=0; x<column; x++)
				for(int y=0; y<row; y++)
					T[x][y]=new Terrain(0,x,y);

			/*/ BUTTONS /*/

			int sx=15;
			int sy=15-1;

			int ox=sx;
			int oy=sy;
			for(int i=attribute.x; i < attribute.y ; i++)
			{
//				for(int j=0; j<2; j++)
				if(i == (attribute.y-attribute.x)/2+attribute.x)
				{
					oy = sy;
					ox += 15;
				}
				button[i]=new Rectangle(ox-3,oy,s,s);
				oy+=5;
			}
			ox=sx;
			oy=sy+1;
			for(int i= create.x; i < create.y; i++)
			{
//				for(int j=0; j<2; j++)
				if(i == (create.y-create.x)/2+create.x)
				{
					oy = sy+1;
					ox += 15;
				}
				button[i]=new Rectangle(ox-3,oy,s,s);
				oy+=5;
			}
			ox=sx;
			oy=sy-5;
			for(int i=profession.x; i<profession.y; i++)
			{
				button[i]=new Rectangle(ox+30-3,oy,s,s);
				oy+=3;
			}
			button[ok.x]=new Rectangle(ox+40-3,oy+6,s,s);


			for(int i=gui.x; i<gui.y; i++)
			{
				button[i]=new Rectangle(0,0,0,0);
			}


			/*/ STARTING ATTRIBUTES /*/
			phase = 2;
			C.setAttribute(18, 0);
			loadMap();


			/** Timer for the repaint() method */
			Timer timer = new Timer(50, new Paint());
			timer.start();

			Timer timer2 = new Timer(50, new Turn());
			timer2.start();

			addMouseListener(new MouseAdapter() 
			{

				public void mousePressed(MouseEvent e)
				{
					m = e.getLocationOnScreen();
					mButton = e.getButton();

					int x = m.x / s;
					int y = m.y / s-1;

					if(edit)
					{
						/*/ TERRAIN EDITING /*/

						if(eMode == 0)
						{
							if(x<column && y<row && x>=0 && y>=0)
							{
								if(e.getButton()==3)
									T[x][y].setType(0,hidden);
								else 
									T[x][y].setType(EDIT,hidden);
							}
						}
						/*/ ENEMY EDITING /*/

						else if(eMode == 1)
						{
							if(e.getButton() == 3)
							{
								for(int i=0; i<numE; i++)
									if(E[i].getL().x == x && E[i].getL().y == y)
										removeEnemy(i);
							}
							else if(numE<E.length)
							{
								E[numE]= new Enemy(1,x,y);
//								E[numE]= new Enemy(x,y,Integer.parseInt(JOptionPane.showInputDialog("Type?")));
								numE++;
							}
						}

						else if(eMode == 2)
						{
							C.setL(new Point(x,y));
						}
						/*/ ITEM EDITING /*/
						else if(eMode == 3)
						{
							if(e.getButton() == 3)
							{
								for(int i=0; i<numI; i++)
									if(I[i].getL().x == x && I[i].getL().y == y)
										removeI(i);
							}
							else
							{
								I[numI] = new Item(1);
								I[numI].setL(new Point(x,y));
								numI++;
							}
						}
					}

					/*/ BUTTON ASSIGNMENT /*/

					int a=-1;
					for(int i=0; i<button.length; i++)
					{
						if(x==button[i].x && y==button[i].y)
							a = i;
					}

					if(a>=gui.x && a<gui.y)
					{
						phase = a-gui.x+3;

						printClear();
					}

					/*/ Character Creation /*/
					if(phase == 1)
					{
						if(a>=attribute.x && a<attribute.y)
						{
//							if(a%2==0)
//							{
//							a /= 2;
							C.setAttribute(a-attribute.x,1);
//							}
						}
						else if(a>=create.x && a<create.y)
						{
							if(a>=create.x && a<create.y)
							{
//								else
//								{
//								if(phase == 1)
//								{
//								a /= 2;
								C.setAttribute(a-create.x,-1);
//								}
//								}
							}
						}							
						else if(a>=profession.x && a<profession.y)
						{
							C.setAttribute(a-profession.x,0);

							if(a == profession.x)
							{
								string[0]= "Knight! ";	//Class
								string[1]= "Main attribute: Stamina ";	//Main Attribute
								string[2]= " ";	//Special Ability
								string[3]= "Weapon: sword ";	//Weapon
								string[4]= " ";	//Info
								string[5]= " ";	//
							}
							else if(a == profession.x+1)
							{
								string[0]= "Ranger! ";	//Class
								string[1]= "Main attribute: Charisma ";	//Main Attribute
								string[2]= " ";	//Special Ability
								string[3]= "Weapon: bow ";	//Weapon
								string[4]= " ";	//Info
								string[5]= " ";	//
							}
							else if(a == profession.x+2)
							{
								string[0]= "Monk!? ";	//Class
								string[1]= "Main attribute: Wisdom ";	//Main Attribute
								string[2]= " ";	//Special Ability
								string[3]= "Weapon: claw ";	//Weapon
								string[4]= " ";	//Info
								string[5]= " ";	//
							}
							else if(a == profession.x+3)
							{
								string[0]= "Wizard! ";	//Class
								string[1]= "Main attribute: Intellect ";	//Main Attribute
								string[2]= " ";	//Special Ability
								string[3]= "Weapon: staff ";	//Weapon
								string[4]= " ";	//Info
								string[5]= " ";	//
							}
							else if(a == profession.x+4)
							{
								string[0]= "Ninja! ";	//Class
								string[1]= "Main attribute: Dexterity ";	//Main Attribute
								string[2]= " ";	//Special Ability
								string[3]= "Weapon: spear... or dagger? ";	//Weapon
								string[4]= " ";	//Info
								string[5]= " ";	//
							}
							else if(a == profession.x+5)
							{
								string[0]= "ROGUE... ";	//Class
								string[1]= "Main attribute: Strength ";	//Main Attribute
								string[2]= " ";	//Special Ability
								string[3]= "Weapon: axe ";	//Weapon
								string[4]= " ";	//Info
								string[5]= " ";	//
							}
						}
					}
					/*/ Inventory /*/
					else if(phase == 3)
					{

					}
					/*/ Attributes /*/
					else if(phase == 4)
					{
						if(a>=attribute.x && a<attribute.y)
						{
//							if(a%2==0)
//							{
//							a /= 2;
							C.setAttribute(a-attribute.x,1);
//							}
						}
					}
					/*/ Skills /*/
					else if(phase == 5)
					{

					}
					/*/ Options /*/
					else if(phase == 6)
					{

					}


					if(a == ok.x)
					{
						phase = 2;
						printClear();
					}

				}
			}   );

			addMouseMotionListener(new MouseMotionAdapter() 
			{

				public void mouseDragged(MouseEvent e)
				{
					m = e.getLocationOnScreen();

					int x = m.x / s;
					int y = m.y / s-1;

					if(edit)
					{
						if(eMode == 0)
						{
							if(x<column && y<row && x>=0 && y>=0)
							{
								if(mButton!=3)
									T[x][y].setType(EDIT,hidden);
								else
									T[x][y].setType(0,hidden);
							}
						}
					}
				}
			}   );

			addKeyListener(new KeyAdapter() 
			{

				public void keyPressed(KeyEvent e)
				{
					char key = e.getKeyChar();

					/** Sets EDIT number without error of parsing a string */
					try{EDIT = Integer.parseInt(key+"");}
					catch(Exception e1){}

					//Drop item
					if(key == 'd')
						cmd = 6;
					else if(key == 'e')
						cmd = 8;



					switch(e.getKeyCode())
					{
//					case KeyEvent.VK_UP:  	C.Move(0,-1);	break;
//					case KeyEvent.VK_DOWN:  C.Move(0,1);	break;
//					case KeyEvent.VK_LEFT:  C.Move(-1,0);	break;
//					case KeyEvent.VK_RIGHT: C.Move(1,0);	break;

					case KeyEvent.VK_UP:  	if(phase==2)cmd = 1;	break;
					case KeyEvent.VK_DOWN:  if(phase==2)cmd = 3;	break;
					case KeyEvent.VK_LEFT:  if(phase==2)cmd = 4;	break;
					case KeyEvent.VK_RIGHT: if(phase==2)cmd = 2;	break;
					case KeyEvent.VK_SPACE: if(phase==2)cmd = 5;	break;

//					case KeyEvent.VK_O: C.HP--;	break;
//					case KeyEvent.VK_P: C.HP++;	break;

					case KeyEvent.VK_I: if(phase==2){phase = 3;printClear();}else if(phase==3)phase = 2;	break;
					case KeyEvent.VK_A: if(phase==2){phase = 4;printClear();}else if(phase==4)phase = 2;	break;
					case KeyEvent.VK_S: if(phase==2){phase = 5;printClear();}else if(phase==5)phase = 2;	break;
					case KeyEvent.VK_O: if(phase==2){phase = 6;printClear();}else if(phase==6)phase = 2;	break;

					case KeyEvent.VK_H: if(!hidden)hidden = true; else hidden = false;	break;
					case KeyEvent.VK_F1: if(!edit)edit = true; else edit = false;	break;

					case KeyEvent.VK_F2: saveMap();	break;
					case KeyEvent.VK_F4: loadMap();	break;

					case KeyEvent.VK_F5: eMode = 0;	break;
					case KeyEvent.VK_F6: eMode = 1;	break;
					case KeyEvent.VK_F7: eMode = 2;	break;
					case KeyEvent.VK_F8: eMode = 3;	break;

					case KeyEvent.VK_P: printLine("-");	break;

					case KeyEvent.VK_K: C.setLevel(10);	break;

					}
					//repaint();
				}
			}   );
		}

		/** Calls repaint() every interval */
		class Paint implements ActionListener
		{
			public void actionPerformed(ActionEvent e)
			{
				repaint();
			}
		}
		/**
		 * Main game class with all calculations and such
		 * @author mrshort
		 */
		class Turn implements ActionListener
		{
			public void actionPerformed(ActionEvent e)
			{
				{
					if(!edit)
					{
						/*/ CHARACTER CREATION /*/
						if(phase == 1)
						{

						}


						/*/ CHARACTER TURN /*/
						if(turn == 1)
						{
//							boolean end = false;

							int x=0;
							int y=0;
							if(cmd == 1)
								y = -1;
							else if(cmd == 2)
								x = 1;
							else if(cmd == 3)
								y = 1;
							else if(cmd == 4)
								x = -1;

							/*/ Character attacking Enemy /*/
							if(cmd>0 && cmd<=5)
							{
								for(int i=0; i<numE; i++)
								{
									if(E[i].getL().x == C.getL().x+x && E[i].getL().y == C.getL().y+y)
									{
										x=0;
										y=0;

										printLine("");

										int a = C.getMissed(10);

										if(a>0)
										{
											for(int j=0; j<a; j++)
											{
												dam = C.getDamage();

												if(E[i].setDamage(dam))
												{
													print("You kill the "+E[i].getName()+"!");
													C.setLevel(E[i].getExp());

													Item item = E[i].getItem();
													if(item!=null)
													{
														I[numI]=new Item(item.getType());
														I[numI].setL(E[i].getL());
														numI++;
													}
													removeEnemy(i);
													break;
												}
												else
													print("You hit the "+E[i].getName()+".  ("+E[i].getHP()+") ");
											}
											if(dam>C.getSTR())
												print(" (Critical)");


											C.setFatigue(.08);
										}
										else
										{
											print("You miss the "+E[i].getName()+"...");

											C.setFatigue(.06);
										}
									}
								}


								/*/ MOVEMENT /*/

								if(cmd!=5)
									if(capacity == 3)
									{
										printLine("You are too encumbered to move. Lighten your load first. ");
									}
									else if(T[C.getL().x + x][C.getL().y + y].checkMove())
									{
										C.move(x, y);

										C.setFatigue(.02);
									}
									else
										cmd = 0;

								if(T[C.getL().x][C.getL().y].getHidden())
									hidden = true;
								else
									hidden = false;

								if(cmd == 5)
								{
									C.setFatigue(.01);			
									cmd = 0;
								}


								/*/ Character picking up Item /*/
								if(cmd!=0)
									for(int i=0; i<numI; i++)
									{
										if(I[i].getL().equals(C.getL()))
										{
											printLine("");

											print("You pick up "+I[i].getName()+"("+I[i].getQuantity()+"). ");

											boolean added=false;
											for(int n=0; n<numN; n++)
											{
												if(N[n].getType() == I[i].getType())
												{
													if(I[i].getStackable())
													{
														N[n].addQuantity(I[i].getQuantity());
														added=true;
													}
													break;
												}
											}
											if(!added)
											{
												N[numN] = I[i];
												numN++;
											}

											removeI(i);
											i--;


											capacity = C.getCapacity(N, numN);
											//normal
											if(capacity == 0)
											{

											}
											//burdened
											else if(capacity == 1)
											{
												print("You are slightly encumbered. ");
											}
											//stressed
											else if(capacity == 2)
											{
												print("Movement is difficult. ");
											}
											//immobile?
											else if(capacity == 3)
											{
												print("You are unable to move with this load. ");
												cmd = 5;
											}
//											printLine("");
										}
									}
							}
							// Drop
							else if(cmd == 6)
							{
//								print("Drop which item?");
//								phase=7;
								int n = -1;
								try{n = Integer.parseInt(JOptionPane.showInputDialog("Drop which item?"))-1;}
								catch(Exception e1){printLine("That's a silly thing to drop. ");}

								if(numN>n && n>-1)
								{
									printLine("Dropped "+N[n].getName()+"("+N[n].getQuantity()+"). ");

									I[numI] = N[n];
									I[numI].setL(C.getL());
									numI++;

									removeN(n);

									capacity = C.getCapacity(N, numN);
								}
								else
									printLine("You can't drop what you don't have. ");
								cmd = 0;
							}
							// Equip
							else if(cmd == 8)
							{
								int n = -1;
								try{n = Integer.parseInt(JOptionPane.showInputDialog("Drop which item?"))-1;}
								catch(Exception e1){printLine("That's a silly thing to drop. ");}

								if(numN>n && n>-1)
								{
									printLine("Equiped "+N[n].getName()+"("+N[n].getQuantity()+"). ");
									
									C.equip(N[n],N[n].getEquipType());
								}
								else
									printLine("You can't equip what you don't have. ");
								cmd = 0;								
							}

							if(cmd!=0 && phase == 2)
							{
								cmd = 0;
								turn = 2;
								moves++;
							}
						}
						
						
						
						
						
						
						
						
						
						/*/ ENEMY TURNS /*/
						if(turn == 2)
						{	
							for(int i=0; i<numE; i++)
							{
//								boolean end = true;
								cmd = 0;

								int x=0;
								int y=0;

								int dx = (C.getL().x-E[i].getL().x);
								int dy = (C.getL().y-E[i].getL().y);

								if(Math.random() > .2)
									// If Character is in enemie's LOS
									if( Math.sqrt(Math.pow(dx,2) + Math.pow(dy,2)) <= E[i].getLOS() )
									{
										if(Math.abs(dx) > Math.abs(dy))
										{
											if(dx>0)
												x += 1;
											else
												x -= 1;
										}
										else if(Math.abs(dx) < Math.abs(dy))
										{
											if(dy>0)
												y += 1;
											else
												y -= 1;
										}
										else
										{
											if( Math.random() > .5 )
											{
												if(dx>0)
													x += 1;
												else
													x -= 1;
											}
											else
											{
												if(dy>0)
													y += 1;
												else
													y -= 1;
											}
										}
									}
									else
									{
										cmd = (int)(Math.random()*5);
									}
								else
								{
									cmd = (int)(Math.random()*5);
								}


								if(cmd == 1)
									y = -1;
								else if(cmd == 2)
									x = 1;
								else if(cmd == 3)
									y = 1;
								else if(cmd == 4)
									x = -1;


								for(int j=0; j<numE; j++)
								{
									if(E[j].getL().x == E[i].getL().x+x && E[j].getL().y == E[i].getL().y+y)
									{
										x=0;
										y=0;
									}
								}

								if(E[i].getL().x+x == C.getL().x && E[i].getL().y+y == C.getL().y)
								{
									x=0;
									y=0;

									printLine("");

									int a = E[i].getMissed(C.getDXT());

									if(a>0)
										for(int j=0; j<a; j++)
										{
											dam = E[i].getDamage();

											C.setDamage(dam);
											print(" The "+E[i].getName()+" hits!  ");


											if(dam>E[i].getSTR())
												print(" (Critical)");
										}
									else
										print(" The "+E[i].getName()+" misses...");

								}

								if(T[E[i].getL().x + x][E[i].getL().y + y].checkMove())
									E[i].move(x, y);

							}
							cmd = 0;
							turn = 1;
						}
					}
				}
			}
		}
		public void print(String msg)
		{
			string[0] += msg;
		}
		public void printClear()
		{
			for(int i=0; i<6; i++)
				printLine("");
		}
		public void printLine(String msg)
		{
			string[5] = string[4];
			string[4] = string[3];
			string[3] = string[2];
			string[2] = string[1];
			string[1] = string[0];

			string[0] = msg;
		}

		public void removeEnemy(int e)
		{
			for(int i=e; i<numE; i++)
			{
				E[i] = E[i+1];
			}
			numE--;
		}
		public void removeI(int e)
		{
			for(int i=e; i<numI; i++)
			{
				I[i] = I[i+1];
			}
			numI--;
		}
		public void removeN(int e)
		{
			for(int i=e; i<numI; i++)
			{
				N[i] = N[i+1];
			}
			numN--;
		}


		/** Called from TimerListener; paints the frame */
		protected void paintComponent(Graphics g)
		{
			frame++;
			super.paintComponent(g);
			setBackground(Color.black);
			g.setColor(Color.white);
			g.setFont(new Font("" , Font.PLAIN, s/2));



			/*/ GAME PHASES /*/

			if(phase==2)
			{
				/*/ EDITING /*/

				if(edit)
				{
					g.setColor(Color.white);
					g.setFont(new Font("" , Font.PLAIN, s/2));

					//Grid x & y numbering
					for(int i=0; i<column+16; i++)
						g.drawString(i+"", s*i+s/4, s*row+s-s/4);
					for(int i=0; i<row+16; i++)
						g.drawString(i+"", 0+s*column+s/4, s*i+s-s/4);

					//Statistics area grid
					g.setColor(new Color(50,50,50));
					for(int i=+1; i<column+16; i++)
						g.drawLine(s*i,0,s*i,600);
					for(int i=1; i<row+20; i++)
						g.drawLine(s,s*i,1400,s*i);
				}

				/*/ GAME SPRITES /*/

				//Draws the terrain
				for(int x=0; x<column; x++)
					for(int y=0; y<row; y++)
						T[x][y].draw(g,s,frame,hidden);

				//Draws the character
				C.draw(g,s,column,row);
				//Draws the enemies
				for(int i=0; i<numE; i++)
					E[i].draw(g,s,column,row);
				//Draws the Items
				for(int i=0; i<numI; i++)
				{
					g.setColor(new Color(150,50,150));
					g.setFont(new Font("" , Font.PLAIN, s));
					g.drawString(I[i].getIcon(),I[i].getL().x*s+s/4,I[i].getL().y*s+s+s/2);
				}


			}
			//Character Creation
			else if(phase == 1)
			{
				g.setFont(new Font("" , Font.PLAIN, s*2));
				g.drawString("Character Creation",0,s*4);
				g.setFont(new Font("" , Font.PLAIN, s));
				C.drawCreation(g, s);

//				for(int i=attribute.x; i<create.y; i++)
//				{
//				g.drawRect(button[i].x*s,button[i].y*s,button[i].width,button[i].height);
//				}

				for(int i=profession.x; i<profession.y; i++)
				{

					g.drawRect(button[i].x*s,button[i].y*s,button[i].width,button[i].height);

					String c="";

					if(i==profession.x+0)
						c="Knight";
					if(i==profession.x+1)
						c="Ranger";
					if(i==profession.x+2)
						c="Monk";
					if(i==profession.x+3)
						c="Wizard";
					if(i==profession.x+4)
						c="Ninja";
					if(i==profession.x+5)
						c="Rogue";

					g.drawString(""+c,(button[i].x+2)*s,(button[i].y+1)*s);

				}
			}
			//Inventory
			else if(phase == 3)
			{

				for(int i=0; i<numN; i++)
				{
					int dx=1;
					int dy=i;

					for(int j=1; j<6; j++)
					{
						if(i+1>15*j)
						{
							dx++;
							dy-=15;
						}
					}

					g.setFont(new Font("" , Font.PLAIN, s));
					g.drawString((i+1)+":", -14*s+16*s*dx, 10*s+dy*s);

					g.setFont(new Font("" , Font.PLAIN, s));
					g.drawString(""+N[i].getName(), -12*s+16*s*dx, 10*s+dy*s);

					g.setFont(new Font("" , Font.PLAIN, s/2));
					g.drawString(""+N[i].getQuantity(), 16*s*dx, 10*s+dy*s);
				}
			}
			//Attributes
			else if(phase == 4)
			{
				g.setFont(new Font("" , Font.PLAIN, s));
				C.drawAttribute(g, s);
			}
			//Skills
			else if(phase == 5)
			{
				g.setFont(new Font("" , Font.PLAIN, s));

			}
			//Options
			else if(phase == 6)
			{
				g.setFont(new Font("" , Font.PLAIN, s));

			}
			if(phase!=2)
			{
				g.setFont(new Font("" , Font.PLAIN, s));
				g.drawString("OK",(button[ok.x].x)*s,(button[ok.x].y+1)*s);
			}

			/*/ PRINTOUT /*/

			g.setColor(Color.yellow);
			g.setFont(new Font("" , Font.PLAIN, (int)(s*3/4)));
			for(int i=0; i<string.length; i++)
				g.drawString(string[i],2*s,s*row-s*6+s*3+s*(string.length-i)-s/5);

			g.drawString(""+moves,column*s+12*s,row*s+4*s);

			/*/ GUI /*/
			g.setColor(Color.cyan);
			g.drawRect(s, s*row+s, s*column-2*s, s*6);
			g.drawRect(s*column+s, s, s*12, s*row-2*s);
//			g.drawRect(s*column+s, s*20, s*12, s*8);


			g.drawLine(s*column+s, s*20, s*column+s+12*s, s*20);
			g.drawLine(s*column+s, s*24, s*column+s+12*s, s*24);
			g.drawLine(s*column+s, s*28, s*column+s+12*s, s*28);

			for(int i=gui.x; i<gui.y; i++)
			{
				g.drawRect(button[i].x*s,button[i].y*s,button[i].width,button[i].height);
			}
			for(int i=0; i<4; i++)
				g.drawString("A",column*s+2*s+3*i*s,row*s-s);

		}

		public void saveMap()
		{
			try 
			{
//				JFileChooser chooser = new JFileChooser();
//				if(chooser.showSaveDialog(null) == JFileChooser.APPROVE_OPTION)
				{
//					File selected = chooser.getSelectedFile();
					DataOutputStream output = new DataOutputStream(new FileOutputStream("C:/Users/mrshort/Desktop/DATA"));

					for(int x=0; x<column; x++)
						for(int y=0; y<row; y++)
						{
							output.writeInt(T[x][y].getL().x);
							output.writeInt(T[x][y].getL().y);
						}
					for(int x=0; x<column; x++)
						for(int y=0; y<row; y++)
						{
							output.writeInt(T[x][y].getType());
							output.writeBoolean(T[x][y].getHidden());
						}

					output.writeInt(C.getL().x);
					output.writeInt(C.getL().y);

					output.writeInt(numE);
					for(int i=0; i<numE; i++)
					{
						output.writeInt(E[i].getType());
						output.writeInt(E[i].getL().x);
						output.writeInt(E[i].getL().y);
					}

					output.writeInt(numI);
					for(int i=0; i<numI; i++)
					{
						output.writeInt(I[i].getType());
						output.writeInt(I[i].getL().x);
						output.writeInt(I[i].getL().y);
					}

					output.close();
				}
			}
			catch(IOException e) {}
		}

		public void loadMap()
		{
			try 
			{
//				JFileChooser chooser = new JFileChooser();
//				if(chooser.showOpenDialog(null) == JFileChooser.APPROVE_OPTION)
				{
//					File selected = chooser.getSelectedFile();

					DataInputStream input = new DataInputStream(new FileInputStream("C:/Users/mrshort/Desktop/DATA"));


					for(int x=0; x<column; x++)
						for(int y=0; y<row; y++)
						{
							T[x][y]=new Terrain(1,input.readInt(),input.readInt());
						}

					for(int x=0; x<column; x++)
						for(int y=0; y<row; y++)
						{
							T[x][y].setType(input.readInt(),input.readBoolean());
						}

					C.setL(new Point(input.readInt(),input.readInt()));

					numE=input.readInt();
					for(int i=0; i<numE; i++)
					{
						E[i]=new Enemy(input.readInt(),input.readInt(),input.readInt());
					}

					numI=input.readInt();
					for(int i=0; i<numI; i++)
					{
						I[i]=new Item(input.readInt());
						I[i].setL(new Point(input.readInt(),input.readInt()));
					}

					input.close();
				}
			}
			catch(IOException e) {}
		}
	}
}
