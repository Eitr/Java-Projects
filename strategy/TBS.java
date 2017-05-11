/*

	To Do:

		-Create everything having to do with the enemy

		-Create battle sequences

		-Create more variety of random exploration

		-Utilize difficulty settings

		-Create better main menu!

 */

package strategy;

import java.awt.*;
import java.awt.event.*;

import javax.swing.*;

@SuppressWarnings("serial")
public class TBS extends JFrame 
{
	Container cp;
	JPanel mainPanel;
	JPanel optionPanel;
	JButton B[] = new JButton [8];
	JButton wait;
	JLabel info;
	JLabel stats;



	final int MAXX = 30, MAXY = 20;

	Terrain T[][] = new Terrain [MAXX][MAXY];
	int s;
	Structure S[][] = new Structure [MAXX][MAXY];

	int turn;

	Army A[] = new Army [100];
	int armies;


//	Structure selected = null; //new Structure(-1,-1,-1);
	Point selected;
	int army;		//current army selected

	boolean EDIT;
	boolean menu;

	Point start;	//player start location
	int level;		//current level
	int exp;		//current exp (%/100)
	int food;		//current food
	int foodMax;	//maximum food allowed
	int gold;
	int cap;		//population supported
	int pop;		//current population

	Point enemy;

	int peasants;
	int peasant_cost;
	int soldiers;
	int soldier_cost = 25; //remove
	int builders;

	int round;
	boolean roundInfo;

	int consumed;
	int produced;

	boolean startGame;
	int KILL;

	boolean OPTION = true;
	boolean GAME;

	int difficulty;




	/* *****************************************************************************************************
	 * *****************************************************************************************************/



	public void updateInfo (boolean action)
	{
		if (action)
		{
			for (int x = 0; x < S.length; x++)
				for (int y = 0; y < S[x].length; y++)
					if(S[x][y].getTimeLeft() > 0)
					{
						S[x][y].addTime(1);

						if (S[x][y].getTimeLeft() == 0)
						{
							builders -= S[x][y].getBuilders();
							peasants += S[x][y].getBuilders();
						}
					}

			for (int a = 0; a < armies; a++)
			{
				consumed += A[a].getSize() * T[A[a].getX()][A[a].getY()].getTax();
			}

			consumed += (peasants - builders) + (soldiers - getTotalArmies())*3 + builders*2;

			for (int x = 0; x < S.length; x++)
				for (int y = 0; y < S[x].length; y++)
					if (S[x][y].getType() == 2) // check all farms
						produced += S[x][y].getProduction();


			turn++;
		}

//		if(selected.x > -1 && selected.y > -1)
//		if(selected != null)

		pop = peasants + soldiers + builders;

		cap = 0;

		for (int x = 0; x < S.length; x++)
			for (int y = 0; y < S[x].length; y++)
				if (S[x][y].getTimeLeft() <= 0)
					cap += S[x][y].getPop();

		info.setText("Level: "+level+"          Exp: "+exp+"%          Pop: & "+
				pop+" / "+cap+"          $ "+gold+"          Food: "+food+"          #"+turn);



		if(selected != null)
			if(T[selected.x][selected.y].getOwn())
			{
				if (S[selected.x][selected.y].getTimeLeft() == 0)
				{
//					if (S[selected.x][selected.y].getTimeLeft() > 0)

//					else
					stats.setText("[ "+S[selected.x][selected.y].getName().toUpperCase()+" ]"); // prints structure's name


					int type = S[selected.x][selected.y].getType();

					if (type == 0) // empty
						setEmpty();
					else if (type == 1) // base
						setBase();
					else if (type == 2) // farm
						setFarm();
					else if(type == 3) // house
						setHouse();
					else if(type == 4) // barracks
						setBarracks();
				}
				else
				{
					for(int b = 0; b < B.length; b++)
						B[b].setVisible(false);
					stats.setText(S[selected.x][selected.y].getName()+" (under construction)"); // if structure is still building
				}
			}
			else if (army > -1)
			{
//				for (int a = 0; a < armies; a++)
//				if(A[a].getX() == army.x && A[a].getY() == army.y)
				{

					stats.setText("Army : "+(army+1));
				}
			}

		repaint();
	}



	/* *****************************************************************************************************
	 * *****************************************************************************************************/






	public void setEmpty ()
	{
		int b = 0;

		for (; b < 6; b++)
		{
			B[b].setText(Structure.getName(b+2));
			B[b].setVisible(true);
		}

		for (; b < B.length; b++)
			B[b].setVisible(false);
	}
	public void setBase ()
	{
		B[0].setText("Peasant  $"+peasant_cost);
		B[0].setVisible(true);

		B[6].setText("Upgrade");
		B[6].setVisible(true);

		B[7].setText("Destroy");
		B[7].setVisible(true);

		for (int b = 1; b < B.length-2; b++)
			B[b].setVisible(false);

		stats.setText(stats.getText() + "                    Peasants:          Working:  "+getTotalWorkers()+
				"          Building:  "+builders+"          Idle:  "+(peasants-getTotalWorkers()));
	}
	public void setFarm ()
	{
		B[0].setText("Set workers");
		B[0].setVisible(true);

		B[6].setText("Upgrade");
		B[6].setVisible(true);

		B[7].setText("Destroy");
		B[7].setVisible(true);

		for (int b = 1; b < B.length-2; b++)
			B[b].setVisible(false);

		stats.setText(stats.getText() + "          Workers:  "+S[selected.x][selected.y].getWorkers()+
				" / "+S[selected.x][selected.y].getCap());
	}
	public void setHouse ()
	{
		B[6].setText("Upgrade");
		B[6].setVisible(true);

		B[7].setText("Destroy");
		B[7].setVisible(true);

		for (int b = 0; b < B.length-2; b++)
			B[b].setVisible(false);
	}
	public void setBarracks ()
	{
		B[0].setText("Soldier  $"+soldier_cost);
		B[0].setVisible(true);

		B[1].setText("Create Army");
		B[1].setVisible(true);


		B[6].setText("Upgrade");
		B[6].setVisible(true);

		B[7].setText("Destroy");
		B[7].setVisible(true);

		for (int b = 2; b < B.length-2; b++)
			B[b].setVisible(false);

		stats.setText(stats.getText()+"                    Soldiers:          Home:  "+
				(soldiers-getTotalArmies())+"          Away:  "+getTotalArmies());
	}



	/* *****************************************************************************************************
	 * *****************************************************************************************************/



	class Mouse implements MouseListener
	{
		public void mousePressed (MouseEvent e)
		{
			Point m = new Point (e.getX()/s, e.getY()/s); // finds mouse click based on grid

			if(m.x >= 0 && m.y >= 0 && m.x < MAXX && m.y < MAXY) // makes sure mouse is inside grid
			{
				if (e.getButton() == 1) // left mouse click
				{
					if(EDIT)
						T[m.x][m.y].setType(T[m.x][m.y].getType()+1); // EDIT changes terrain type

					selected = m;

					if (T[m.x][m.y].getOwn())
					{
						setNull();
						selected = m;

						updateInfo(false);
					}
					else //if (!T[m.x][m.y].getVisible())
					{
						for (int a = 0; a < armies; a++)
							if(A[a].getX() == m.x && A[a].getY() == m.y)
							{
//								if (army.x != m.x || army.y != m.y)
								{
									setNull();
									army = a;
									updateInfo(false);
								}
							}


						if(army > -1)
						{
//							if ((m.y > 0 && T[m.x][m.y-1].getVisible()) || (m.x > 0 && T[m.x-1][m.y].getVisible()) ||
//							(m.y < MAXY && T[m.x][m.y+1].getVisible()) || (m.x < MAXX && T[m.x+1][m.y].getVisible()))

//							if ((A[army].getX() == m.x+1 && A[army].getY() == m.y) || 
//							(A[army].getX() == m.x-1 && A[army].getY() == m.y) || 
//							(A[army].getX() == m.x && A[army].getY() == m.y+1) || 
//							(A[army].getX() == m.x && A[army].getY() == m.y-1))

							int offx = A[army].getX() - m.x;
							int offy = A[army].getY() - m.y;


							if ((offx == 0 && Math.abs(offy) == 1) || (offy == 0 && Math.abs(offx) == 1))
							{
								if ((T[m.x][m.y].getType() == 3 || T[m.x][m.y].getType() == 4) && !T[m.x][m.y].getVisible())
								{
									T[m.x][m.y].setVisible(true);

									explore(); // EDIT different types of events per terrain type

									updateInfo(true);
								}
								else if (T[m.x][m.y].getType() != 4)
								{
									if (!T[m.x][m.y].getVisible())
									{
										T[m.x][m.y].setVisible(true);

										explore(); // EDIT different types of events per terrain type
									}

									A[army].setLocation(m.x, m.y);

									updateInfo(true);
								}

							}
						}

					}





				}
			}
			if (e.getButton() == 3) // right mouse click
			{
				setNull();
			}
		}

		public void mouseReleased (MouseEvent e){}
		public void mouseClicked (MouseEvent e){}
		public void mouseEntered (MouseEvent e){}
		public void mouseExited (MouseEvent e){}
	}





	/* *****************************************************************************************************
	 * *****************************************************************************************************/








	class Button implements ActionListener
	{
		public void actionPerformed (ActionEvent e)
		{
			int type = S[selected.x][selected.y].getType();
			int current = -1;
			for (int b = 0; b < B.length; b++)
				if(B[b] == e.getSource())
					current = b;

			if (type == 0) // empty (building structures)
			{
				current += 2;

				int choose = -1;
				try
				{
					choose = JOptionPane.showConfirmDialog(null, "$"+Structure.getCost(current)+
							"   #"+Structure.getTime(current)+"   &"+Structure.getBuilders(current), 
							Structure.getName(current), JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
				}
				catch(Exception error){}


				if (choose == 0)
				{
					if (gold >= Structure.getCost(current))
						if (Structure.getBuilders(current) <= peasants - getTotalWorkers())
						{
							S[selected.x][selected.y] = new Structure (current, selected.x, selected.y);

							gold -= Structure.getCost(current);
							builders += S[selected.x][selected.y].getBuilders();
							peasants -= S[selected.x][selected.y].getBuilders();

							setNull();
							updateInfo(true);
						}
						else
							error ("Not enough idle peasants for building.\nConsider training more peasants.");
					else
						error ("More gold required.\nConsider exploring more.");
				}
			}
			else
			{
				if (type == 1) // base
				{
					if (current == 0) // peasant
					{
						if (gold >= peasant_cost)
							if(pop < cap)
							{
								gold -= peasant_cost;
								peasants += 1;

								if (peasants % 10 == 0)
									updateInfo(true);
								else
									updateInfo(false);
							}
							else
								error ("You have reached your maximum population.\nConsider building or upgrading more houses.");
						else
							error ("More gold required.\nConsider exploring more.");
					}
				}
				else if (type == 2) // farm
				{
					if (current == 0) // set workers
					{
						setWorkers();

						for(int b = 0; b < B.length; b++)
							B[b].setFocusable(false);
					}
				}
				else if (type == 3) // house
				{

				}
				else if (type == 4) // barracks
				{
					if (current == 0) // soldier
					{
						if (gold >= soldier_cost)
							if(pop < cap)
							{
								gold -= soldier_cost;
								soldiers += 1;

								if (soldiers % 10 == 0)
									updateInfo(true);
								else
									updateInfo(false);
							}
							else
								error ("You have reached your maximum population.\nConsider building or upgrading more houses.");
						else
							error ("More gold required.\nConsider exploring more.");
					}
					else if (current == 1) // create Army
					{
						int size = 0;
						try
						{
							size = Integer.parseInt(JOptionPane.showInputDialog(null, "How many soldiers for your army?", 
									"Army Size", JOptionPane.QUESTION_MESSAGE));
						}
						catch (Exception error){}

						if(size > 0)
							if(size <= soldiers - getTotalArmies())
							{
								//army name?

								if (level == 1)
									A[armies] = new Army(size, (enemy.x-start.x)/Math.abs(enemy.x-start.x)+start.x,
											(enemy.y-start.y)/Math.abs(enemy.y-start.y)+start.y);
								else if (level > 1)
									A[armies] = new Army(size, (enemy.x-start.x)/Math.abs(enemy.x-start.x)*2+start.x,
											(enemy.y-start.y)/Math.abs(enemy.y-start.y)*2+start.y);

								armies++;

								updateInfo(true);
							}
							else
								error ("More soldiers required.\nConsider training more soldiers.");
					}
				}






				if (current == 6) // upgrading
				{
					int choose = -1;
					try
					{
						choose = JOptionPane.showConfirmDialog(null, "$"+S[selected.x][selected.y].getUpgradeCost(),
								"Upgrade", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
					}
					catch(Exception error){}

					if (choose == 0) // if yes is selected
						if(level >= (S[selected.x][selected.y].getLevel() + 1))
							if(gold >= (S[selected.x][selected.y].getUpgradeCost()))
							{
								S[selected.x][selected.y].setUpgrade();

								gold -= S[selected.x][selected.y].getUpgradeCost();

								updateInfo(true);
//								setNull();
							}
							else
								error ("More gold required.\nConsider exploring more.");
						else
							error ("Experience level not high enough.");
				}
				else if (current == 7) // destroy
				{
					int choose = -1;
					try
					{
						choose = JOptionPane.showConfirmDialog(null, "",
								"Destroy", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
					}
					catch(Exception error){}

					if (choose == 0) // if yes is selected
					{
						S[selected.x][selected.y] = new Structure (0, selected.x, selected.y);
						setNull();
						updateInfo(true);
					}
				}
			}
		}
	}



	/* *****************************************************************************************************
	 * *****************************************************************************************************/



	public void setNull ()
	{
		selected = null;
		army = -1;

		for(int b = 0; b < B.length; b++)
			B[b].setVisible(false);

		stats.setText("");
	}

	public int readNum (String s)
	{
		int z = -1;

		try
		{
			z = Integer.parseInt(JOptionPane.showInputDialog(null, s,"",JOptionPane.QUESTION_MESSAGE));
		}
		catch (Exception error){};

		return z;
	}

	public void error (String s)
	{
		getToolkit().beep();
		JOptionPane.showMessageDialog(null, s,"Error",JOptionPane.WARNING_MESSAGE);
	}

	public int getTotalWorkers ()
	{
		int sum = 0;
		for (int x = 0; x < S.length; x++)
			for (int y = 0; y < S[x].length; y++)
			{
				sum += S[x][y].getWorkers();
			}
		return sum;
	}
	public int getTotalArmies ()
	{
		int sum = 0;
		for (int a = 0; a < armies; a++)
		{
			sum += A[a].getSize();
		}
		return sum;
	}


	public void setWorkers ()
	{
		int hire = -1;

		hire = readNum("Task how many workers?");

		if (hire > -1)
		{
			if (hire <= peasants)
				if (hire <= S[selected.x][selected.y].getCap())
					if (hire <= peasants - getTotalWorkers()+S[selected.x][selected.y].getWorkers())
					{
						S[selected.x][selected.y].setWorkers(hire);

						updateInfo(false);
						setNull();
					}
					else
						error ("Not enough idle peasants. ("+(peasants-getTotalWorkers()-builders)+" / "+
								hire+")\nConsider training more peasants or\nLowering the amount of workers.");
				else
					error ("Cannot support this many workers.\n" +
					"Consider upgrading this structure.");
			else
				error ("You do not have that many peasants.\nConsider training more peasants.");
		}
	}







	/* *****************************************************************************************************
	 * *****************************************************************************************************/






	public void explore ()
	{
		int random = (int)(Math.random() * 100);

		if (random < 45) // 45%
		{

		}
		else if (random < 65) // 20%
		{
			if (T[selected.x][selected.y].getType() == 0) // grass
			{

			}
			else if (T[selected.x][selected.y].getType() == 1) // dirt
			{

			}
			else if (T[selected.x][selected.y].getType() == 2) // forest
			{

			}
			else if (T[selected.x][selected.y].getType() == 3) // mountain
			{

			}
			else if (T[selected.x][selected.y].getType() == 4) // water
			{

			}
		}
		else if (random < 90) // 25%   GOLD
		{
			random = (int)(Math.random() * 50) + 1;

			JOptionPane.showMessageDialog(null, "Plundered some gold:  $ "+random, 
					"Exploration", JOptionPane.INFORMATION_MESSAGE);

			gold += random;
		}
		else if (random < 95) // 5%   PEASANTS
		{
			random = (int)(Math.random() * 5) + 1;

			JOptionPane.showMessageDialog(null, "Found stranded peasants:  & "+random, 
					"Exploration", JOptionPane.INFORMATION_MESSAGE);

			peasants += random;
		}
		else if (random < 100) // 5%    FOOD
		{
			random = (int)(Math.random() * 50) + 1;

			JOptionPane.showMessageDialog(null, "Plundered some food:  "+random, 
					"Exploration", JOptionPane.INFORMATION_MESSAGE);

			food += random;
		}
	}






	/* *****************************************************************************************************
	 * *****************************************************************************************************/






	public int battle (boolean initiative, int pSize, int eSize, String eName)
	{
		if (initiative) // player chooses outcome
		{
			int z = -1;

			try
			{
			z = JOptionPane.showOptionDialog(null, "Prepare for "+eName+"!", "Battle", 0, 
			JOptionPane.QUESTION_MESSAGE, null, new String[]{"Conquest","Raid","Flee"}, 0);
			}
			catch(Exception e){}

			if (z == -1)
			{
				return -1;
			}

			/* CONQUEST */

			else if (z == 0)
			{
				JOptionPane.showMessageDialog(null, "Your soldiers: "+pSize+"\nEnemy soldiers: "+eSize, 
						"Pre-Game", JOptionPane.INFORMATION_MESSAGE);




				Point result = fight(pSize,eSize,1);

				while (result.x > 0 && result.y > 0)
					result = fight(result.x,result.y,1);


				if (result.x > 0)
				{
					int reward = (int)(Math.random()*(eSize*soldier_cost));
					gold += reward;

					JOptionPane.showMessageDialog(null, "You survivied!\nRemaining soldiers: "+result.x+
							"\nPlundered: "+reward, "Results", JOptionPane.INFORMATION_MESSAGE);
				}
				else
				{
					JOptionPane.showMessageDialog(null, "You didn't survive...\nRemaining enemies: "+result.y,
							"Results", JOptionPane.INFORMATION_MESSAGE);
				}
				return result.x;
			}

			/* RAID */

			else if (z == 1)
			{
				JOptionPane.showMessageDialog(null, "Your soldiers: "+pSize+"\nEnemy soldiers: "+eSize, 
						"Pre-Game", JOptionPane.INFORMATION_MESSAGE);



				Point result = fight(pSize,eSize,2);


				if (result.x > 0)
				{
					int reward = (int)(Math.random()*((eSize-result.y)*soldier_cost))*4;
					gold += reward;

					JOptionPane.showMessageDialog(null, "You survivied!\nRemaining soldiers: "+result.x+
							"\nPlundered: "+reward, "Results", 
							JOptionPane.INFORMATION_MESSAGE);
				}
				else
				{
					JOptionPane.showMessageDialog(null, "You didn't survive...\nRemaining enemies: "+result.y,
							"Results", JOptionPane.INFORMATION_MESSAGE);
				}
				return result.x;
			}

			/* FLEE */

			else if (z == 2)
			{
				JOptionPane.showMessageDialog(null, "Your soldiers: "+pSize+"\nEnemy soldiers: "+eSize, 
						"Pre-Game", JOptionPane.INFORMATION_MESSAGE);


				Point result = fight(pSize,eSize,3);


				if (result.x > 0)
				{
					JOptionPane.showMessageDialog(null, "You escaped!\nRemaining soldiers: "+result.x, 
							"Results", JOptionPane.INFORMATION_MESSAGE);
				}
				else
				{
					JOptionPane.showMessageDialog(null, "You didn't survive...\nRemaining enemies: "+result.y,
							"Results", JOptionPane.INFORMATION_MESSAGE);
				}
				return result.x;
			}
		}

		/* SURPRISED */

		else
		{
			JOptionPane.showMessageDialog(null, "Surprise attack from "+eName+"!", 
					"Battle", JOptionPane.INFORMATION_MESSAGE);
//
			JOptionPane.showMessageDialog(null, "Your soldiers: "+pSize+"\nEnemy soldiers: "+eSize, 
					"Pre-Game", JOptionPane.INFORMATION_MESSAGE);


			Point result = fight(pSize,eSize,4);


			if (result.x > 0)
			{
				JOptionPane.showMessageDialog(null, "You survivied!\nRemaining soldiers: "+result.x, 
						"Results", JOptionPane.INFORMATION_MESSAGE);
			}
			else
			{
				JOptionPane.showMessageDialog(null, "You didn't survive...\nRemaining enemies: "+result.y,
						"Results", JOptionPane.INFORMATION_MESSAGE);
			}
			return result.x;	
		}
		return -1;
	}
	
	
	public int printBattle (boolean initiative, int pSize, int eSize, String eName, int z) //re-enter z
	{
		if (initiative) // player chooses outcome
		{
//			int z = -1;

//			try
//			{
//			z = JOptionPane.showOptionDialog(null, "Prepare for "+eName+"!", "Battle", 0, 
//			JOptionPane.QUESTION_MESSAGE, null, new String[]{"Conquest","Raid","Flee"}, 0);
//			}
//			catch(Exception e){}

			if (z == -1)
			{
				return -1;
			}

			/* CONQUEST */

			else if (z == 0)
			{
//				JOptionPane.showMessageDialog(null, "Your soldiers: "+pSize+"\nEnemy soldiers: "+eSize, 
//						"Pre-Game", JOptionPane.INFORMATION_MESSAGE);




				Point result = fight(pSize,eSize,1);

				while (result.x > 0 && result.y > 0)
					result = fight(result.x,result.y,1);


				if (result.x > 0)
				{
					int reward = (int)(/*Math.random()*/(eSize*soldier_cost))/2;
					gold += reward;

//					JOptionPane.showMessageDialog(null, "You survivied!\nRemaining soldiers: "+result.x+
//							"\nPlundered: "+reward, "Results", JOptionPane.INFORMATION_MESSAGE);
					

					System.out.println("You survivied!\nRemaining soldiers: "+result.x+
							"\nRemaining enemies: "+result.y+"\nPlundered: "+reward);
				}
				else
				{
//					JOptionPane.showMessageDialog(null, "You didn't survive...\nRemaining enemies: "+result.y,
//							"Results", JOptionPane.INFORMATION_MESSAGE);
					
					System.out.println("You didn't survive...\nRemaining enemies: "+result.y);
				}
				return result.x;
			}

			/* RAID */

			else if (z == 1)
			{
//				JOptionPane.showMessageDialog(null, "Your soldiers: "+pSize+"\nEnemy soldiers: "+eSize, 
//						"Pre-Game", JOptionPane.INFORMATION_MESSAGE);



				Point result = fight(pSize,eSize,2);


				if (result.x > 0)
				{
					int reward = (int)(/*Math.random()*/((eSize-result.y)*soldier_cost))*2;
					gold += reward;

//					JOptionPane.showMessageDialog(null, "You survivied!\nRemaining soldiers: "+result.x+
//							"\nRemaining enemies: "+result.y+"\nPlundered: "+reward, "Results", 
//							JOptionPane.INFORMATION_MESSAGE);

					System.out.println("You survivied!\nRemaining soldiers: "+result.x+
							"\nRemaining enemies: "+result.y+"\nPlundered: "+reward);
				}
				else
				{
//					JOptionPane.showMessageDialog(null, "You didn't survive...\nRemaining enemies: "+result.y,
//							"Results", JOptionPane.INFORMATION_MESSAGE);
					

					System.out.println("You didn't survive...\nRemaining enemies: "+result.y);
				}
				return result.x;
			}

			/* FLEE */

			else if (z == 2)
			{
//				JOptionPane.showMessageDialog(null, "Your soldiers: "+pSize+"\nEnemy soldiers: "+eSize, 
//						"Pre-Game", JOptionPane.INFORMATION_MESSAGE);


				Point result = fight(pSize,eSize,3);


				if (result.x > 0)
				{
//					JOptionPane.showMessageDialog(null, "You escaped!\nRemaining soldiers: "+result.x, 
//							"Results", JOptionPane.INFORMATION_MESSAGE);

					System.out.println("You survivied!\nRemaining soldiers: "+result.x+
							"\nRemaining enemies: "+result.y);
				}
				else
				{
//					JOptionPane.showMessageDialog(null, "You didn't survive...\nRemaining enemies: "+result.y,
//							"Results", JOptionPane.INFORMATION_MESSAGE);
					

					System.out.println("You didn't survive...\nRemaining enemies: "+result.y);
				}
				return result.x;
			}
		}

		/* SURPRISED */

		else
		{
//			JOptionPane.showMessageDialog(null, "Surprise attack from "+eName+"!", 
//					"Battle", JOptionPane.INFORMATION_MESSAGE);
//
//			JOptionPane.showMessageDialog(null, "Your soldiers: "+pSize+"\nEnemy soldiers: "+eSize, 
//					"Pre-Game", JOptionPane.INFORMATION_MESSAGE);


			Point result = fight(pSize,eSize,4);


			if (result.x > 0)
			{
//				JOptionPane.showMessageDialog(null, "You survivied!\nRemaining soldiers: "+result.x, 
//						"Results", JOptionPane.INFORMATION_MESSAGE);
				

				System.out.println("You survivied!\nRemaining soldiers: "+result.x+
						"\nRemaining enemies: "+result.y);
			}
			else
			{
//				JOptionPane.showMessageDialog(null, "You didn't survive...\nRemaining enemies: "+result.y,
//						"Results", JOptionPane.INFORMATION_MESSAGE);
				

				System.out.println("You didn't survive...\nRemaining enemies: "+result.y);
			}
			return result.x;	
		}
		return -1;
	}


	public Point fight (int pSize, int eSize, int type) //??? 1==conquest,2==raid,3==flee,4==surprise
	{
		int enemy = eSize;
		int player = pSize;

		int offe = 1;
		int offp = 1;

		int run = 0;

		if(enemy>player)
		{
			offe = enemy/player;
			run = pSize;
		}
		else
		{
			offp = player/enemy;
			run = eSize;
		}

		if (type == 1) //conquest
			offp *= 2;
		if (type == 2) //raid
		{
			run /= 3 + 1;
			offe = offe/2 + 1;
		}
		if (type == 3) //flee
		{
			offp = 0;
			run /= 9 + 1;
		}
		if (type == 4) //suprise
			offe *= 2;



		for (int i = 0; i < run; i++)
		{
			for (int j = 0; j < offp; j++)
//				if (Math.random() > .5 - u/25.0) //EDIT upgrades change %
//				if (Math.random() > .5) //EDIT upgrades change %
			{
				enemy -= 1; //EDIT upgrades change #
			}
			for (int j = 0; j < offe; j++)
//				if (Math.random() > .5) //EDIT upgrades change %
			{
				player -= 1; //EDIT upgrades change #
			}
			if (enemy < 0 || player < 0)
				break;
		}

		return new Point (player,enemy);
	}





	/*=======================================================================
	 *
	 *					PAINT METHOD
	 *
	 *=======================================================================*/



	public class PaintPanel extends JPanel
	{

		protected void paintComponent (Graphics g)
		{
			super.paintComponent(g);
			setBackground(Color.black);
			g.setColor(Color.red);



			for (int y = 0; y < T[0].length; y++)
				for (int x = 0; x < T.length; x++)
					T[x][y].draw(g, s);

			for (int y = 0; y < S[0].length; y++)
				for (int x = 0; x < S.length; x++)
					S[x][y].draw(g,s);

			for (int a = 0; a < armies; a++)
				A[a].draw(g,s);


//			g.setFont(new Font("", Font.PLAIN, s/3));
//			g.setColor(Color.white);
//			g.drawString((soldiers-getTotalArmies())+"",start.x*s,start.y*s+s);

			g.setColor(Color.red);
			g.drawRect(0,0,T.length*s,T[0].length*s);
		}
	}














	class GameTimer implements ActionListener
	{
		public void actionPerformed (ActionEvent e)
		{
			if (OPTION)
			{
				cp.removeAll();
				cp.add(optionPanel);

				optionPanel.setVisible(true);
				cp.setVisible(true);
				optionPanel.setFocusable(true);
				cp.setFocusable(true);

				OPTION = false;
			}
			else if (GAME)
			{
				cp.removeAll();
				cp.add(mainPanel);


				setTerrain();
				setGame();

				GAME = false;
			}
			else if (EDIT)
			{
				for (int y = 0; y < T[0].length; y++)
					for (int x = 0; x < T.length; x++)
						T[x][y].setVisible(true);

				updateInfo(false);

				EDIT = false;
			}



			if(startGame)
			{
//				repaint();
//				updateInfo(false);

				if (turn % round == 0 && roundInfo)
				{
//					consumed = peasants + soldiers*3 + builders*2;

//					produced = 0;

//					for (int x = 0; x < S.length; x++)
//					for (int y = 0; y < S.length; y++)
//					if (S[x][y].getType() == 2) // check all farms
//					produced += S[x][y].getProduction()



					JOptionPane.showMessageDialog(null, "Food consumed: "+consumed+"\nFood produced: "+produced, 
							"Round "+(turn/round), JOptionPane.PLAIN_MESSAGE);

					food -= consumed;
					food += produced;

					consumed = 0;
					produced = 0;


					roundInfo = false;

					updateInfo(false);
				}
				else if (turn % round == round / 2)
					roundInfo = true;
			}
		}
	}


















	public void setTerrain ()
	{
		KILL = 10000;

		int pick = (int)(Math.random()*4)+1;

		if(pick == 1)
		{
			start = new Point (3,3);
			enemy = new Point (MAXX-3-1,MAXY-3-1);
		}
		else if(pick == 2)
		{
			start = new Point (MAXX-3-1,3);
			enemy = new Point (3,MAXY-3-1);
		}
		else if(pick == 3)
		{
			start = new Point (3,MAXY-3-1);
			enemy = new Point (MAXX-3-1,3-1);
		}
		else if(pick == 4)
		{
			start = new Point (MAXX-3-1,MAXY-3-1);
			enemy = new Point (3,3);
		} 
		level = 1;

		// Initialize terrain

		for (int y = 0; y < T[0].length; y++)
			for (int x = 0; x < T.length; x++)
				T[x][y] = new Terrain (0,x,y);

//		int x = (int)(Math.random() * MAXX);
//		int y = (int)(Math.random() * MAXY);

		int x;
		int y;

		int ox = 0;
		int oy = 0;

		int kill = 0;




		/*		MOUNTAIN RANGE		*/
		for (int i = 0; i < 3; i++)
		{
			x = (int)(Math.random() * MAXX);
			y = (int)(Math.random() * MAXY);

			ox = 0;
			oy = 0;

			while (T[x+ox][y+oy].getType() == 1)
			{
				x = (int)(Math.random() * MAXX);
				y = (int)(Math.random() * MAXY);
			}

			T[x][y] = new Terrain (3 , x, y); // start location

			for(int m = 0; m < MAXX*MAXY/20; m++)
			{
				kill = 0;
				while (T[x+ox][y+oy].getType() == 3 || T[x+ox][y+oy].getType() == 1)
				{
					kill++;

					ox = (int)(Math.random() * 3) - 1; // -1 to 1
					oy = (int)(Math.random() * 3) - 1;

					while(x+ox<0 || y+oy<0 || x+ox>=MAXX || y+oy>=MAXY)
					{
						kill++;

						ox = (int)(Math.random() * 3) - 1; // -1 to 1
						oy = (int)(Math.random() * 3) - 1;

						if(kill > KILL)
							break;
					}
					if(kill > KILL)
						break;
				}
				if(kill > KILL)
					break;

				x += ox;
				y += oy;

				T[x][y] = new Terrain (3 , x, y);

				ox = 0;
				oy = 0;
			}
		}



		/*		WATER SPOTS		*/
		for (int i = 0; i < 6; i++)
		{
			x = (int)(Math.random() * MAXX);
			y = (int)(Math.random() * MAXY);

			ox = 0;
			oy = 0;

			while (T[x+ox][y+oy].getType() == 1)
			{
				x = (int)(Math.random() * MAXX);
				y = (int)(Math.random() * MAXY);
			}

			if(x>0)
				T[x-1][y] = new Terrain (4 , x-1, y); // start location
			else
				T[x+1][y] = new Terrain (4 , x+1, y); // start location
			T[x][y] = new Terrain (4 , x, y); // start location

			for(int w = 0; w < MAXX*MAXY/120; w++)
			{
				kill = 0;
				int borders = 0;

				while (T[x+ox][y+oy].getType() == 4 || T[x+ox][y+oy].getType() == 1 || borders < 2)
				{
					kill++;
					borders = 0;

					ox = (int)(Math.random() * 3) - 1; // -1 to 1
					oy = (int)(Math.random() * 3) - 1;

					while(x+ox<0 || y+oy<0 || x+ox>=MAXX || y+oy>=MAXY)
					{
						kill++;

						ox = (int)(Math.random() * 3) - 1; // -1 to 1
						oy = (int)(Math.random() * 3) - 1;

						if(kill > KILL)
							break;
					}
					if(kill > KILL)
						break;

					if(x+ox+1 < MAXX)
						if(T[x+ox+1][y+oy].getType() == 4)
							borders++;
					if(x+ox+1 < MAXX && y+oy+1 < MAXY)
						if(T[x+ox+1][y+oy+1].getType() == 4)
							borders++;
					if(x+ox-1 >= 0)
						if(T[x+ox-1][y+oy].getType() == 4)
							borders++;
					if(x+ox-1 >= 0 && y+oy-1 >= 0)
						if(T[x+ox-1][y+oy-1].getType() == 4)
							borders++;
					if(y+oy+1 < MAXY)
						if(T[x+ox][y+oy+1].getType() == 4)
							borders++;
					if(x+ox-1 >= 0 && y+oy+1 < MAXY)
						if(T[x+ox-1][y+oy+1].getType() == 4)
							borders++;
					if(y+oy-1 >= 0)
						if(T[x+ox][y+oy-1].getType() == 4)
							borders++;
					if(x+ox+1 < MAXX && y+oy-1 >= 0)
						if(T[x+ox+1][y+oy-1].getType() == 4)
							borders++;

					if(kill > KILL)
						break;
				}
				if(kill > KILL)
					break;


				x += ox;
				y += oy;

				T[x][y] = new Terrain (4 , x, y);

				ox = 0;
				oy = 0;
			}
		}

		/*		FORESTS		*/
		for (int i = 0; i < 2; i++)
		{
			x = (int)(Math.random() * MAXX);
			y = (int)(Math.random() * MAXY);

			ox = 0;
			oy = 0;

			while (T[x+ox][y+oy].getType() == 3 || T[x+ox][y+oy].getType() == 1 || T[x+ox][y+oy].getType() == 4 )
			{
				x = (int)(Math.random() * MAXX);
				y = (int)(Math.random() * MAXY);
			}
			if(x>0)
				T[x-1][y] = new Terrain (2 , x-1, y); // start location
			else
				T[x+1][y] = new Terrain (2 , x+1, y); // start location
			T[x][y] = new Terrain (2 , x, y); // start location

			for(int w = 0; w < MAXX*MAXY/10; w++)
			{
				kill = 0;
				int borders = 0;

				while (T[x+ox][y+oy].getType() == 2 || T[x+ox][y+oy].getType() == 1 ||
						T[x+ox][y+oy].getType() == 3 || T[x+ox][y+oy].getType() == 4 || borders < 2)
				{
					kill++;
					borders = 0;

					ox = (int)(Math.random() * 3) - 1; // -1 to 1
					oy = (int)(Math.random() * 3) - 1;

					while(x+ox<0 || y+oy<0 || x+ox>=MAXX || y+oy>=MAXY)
					{
						kill++;

						ox = (int)(Math.random() * 3) - 1; // -1 to 1
						oy = (int)(Math.random() * 3) - 1;

						if(kill > KILL)
							break;
					}
					if(kill > KILL)
						break;

					if(x+ox+1 < MAXX)
						if(T[x+ox+1][y+oy].getType() == 2)
							borders++;
					if(x+ox+1 < MAXX && y+oy+1 < MAXY)
						if(T[x+ox+1][y+oy+1].getType() == 2)
							borders++;
					if(x+ox-1 >= 0)
						if(T[x+ox-1][y+oy].getType() == 2)
							borders++;
					if(x+ox-1 >= 0 && y+oy-1 >= 0)
						if(T[x+ox-1][y+oy-1].getType() == 2)
							borders++;
					if(y+oy+1 < MAXY)
						if(T[x+ox][y+oy+1].getType() == 2)
							borders++;
					if(x+ox-1 >= 0 && y+oy+1 < MAXY)
						if(T[x+ox-1][y+oy+1].getType() == 2)
							borders++;
					if(y+oy-1 >= 0)
						if(T[x+ox][y+oy-1].getType() == 2)
							borders++;
					if(x+ox+1 < MAXX && y+oy-1 >= 0)
						if(T[x+ox+1][y+oy-1].getType() == 2)
							borders++;

					if(kill > KILL)
						break;
				}
				if(kill > KILL)
					break;


				x += ox;
				y += oy;

				T[x][y] = new Terrain (2 , x, y);

				ox = 0;
				oy = 0;
			}
		}


		/*		DIRT PATH		*/
		for (int i = 0; i < 1; i++)
		{
//			x = (int)(Math.random() * MAXX);
//			y = (int)(Math.random() * MAXY);

			ox = 0;
			oy = 0;

			int offx = (enemy.x - start.x)/Math.abs(enemy.x - start.x); // which x direction to enemy
			int offy = (enemy.y - start.y)/Math.abs(enemy.y - start.y); // which y direction to enemy

			x = start.x;//+offx*2;
			y = start.y;//+offx*2;

//			T[x-offx][y-offy] = new Terrain (1 , x-offx, y-offy); // start location
			T[x][y] = new Terrain (1 , x, y); // start location


			for(int w = 0; w < MAXX*MAXY/10; w++)
			{
				kill = 0;
				int borders = 0;

				while (borders < 1)
				{
					kill++;
					borders = 0;

					if (enemy.x - x != 0)
						offx = (enemy.x - x)/Math.abs(enemy.x - x);
					else
						offx = 0;
					if (enemy.y - y != 0)
						offy = (enemy.y - y)/Math.abs(enemy.y - y);
					else
						offy = 0;

					ox = (int)(Math.random() * 2) * offx; // -1 or 1
					oy = (int)(Math.random() * 2) * offy;

					while(x+ox<0 || y+oy<0 || x+ox>=MAXX || y+oy>=MAXY)
					{
						kill++;

						ox = (int)(Math.random() * 2) * offx; // -1 or 1
						oy = (int)(Math.random() * 2) * offy;

						if(kill > KILL)
							break;
					}
					if(kill > KILL)
						break;

					if(x+ox+1 < MAXX)
						if(T[x+ox+1][y+oy].getType() == 1)
							borders++;
//					if(x+ox+1 < MAXX && y+oy+1 < MAXY)
//					if(T[x+ox+1][y+oy+1].getType() == 1)
//					borders++;
					if(x+ox-1 >= 0)
						if(T[x+ox-1][y+oy].getType() == 1)
							borders++;
//					if(x+ox-1 >= 0 && y+oy-1 >= 0)
//					if(T[x+ox-1][y+oy-1].getType() == 1)
//					borders++;
					if(y+oy+1 < MAXY)
						if(T[x+ox][y+oy+1].getType() == 1)
							borders++;
//					if(x+ox-1 >= 0 && y+oy+1 < MAXY)
//					if(T[x+ox-1][y+oy+1].getType() == 1)
//					borders++;
					if(y+oy-1 >= 0)
						if(T[x+ox][y+oy-1].getType() == 1)
							borders++;
//					if(x+ox+1 < MAXX && y+oy-1 >= 0)
//					if(T[x+ox+1][y+oy-1].getType() == 1)
//					borders++;

					if(kill > KILL)
						break;
				}
				if(kill > KILL)
					break;


				x += ox;
				y += oy;

				T[x][y] = new Terrain (1 , x, y);

				ox = 0;
				oy = 0;
			}
		}
	}












	/* 
	 * *********************************************************************************
	 * *********************************************************************************
	 * *********************************************************************************
	 * *********************************************************************************
	 * *********************************************************************************
	 * *********************************************************************************
	 * *********************************************************************************
	 * *********************************************************************************
	 * *********************************************************************************
	 * *********************************************************************************
	 * *********************************************************************************
	 * *********************************************************************************
	 * *********************************************************************************
	 * *********************************************************************************
	 * *********************************************************************************
	 * *********************************************************************************
	 */





	public void setGame()
	{

		// Initialize structures

		for (int y = 0; y < S[0].length; y++)
			for (int x = 0; x < S.length; x++)
				S[x][y] = new Structure (0,0,0);

		// set size based on screen resolution (30)
		s = getToolkit().getScreenSize().width/43+1;


		// Creates beginning visibility

		int width = level + 1;
		for(int x = -1 * width; x <= width; x++) // go from -r to r
		{
			int height = Math.abs( ( Math.abs(x) - width ) ) * 2 + 1; // it just works... o.O

			for(int y = 0 - height / 2; y < height / 2 + 1; y++)
			{
				if(x+start.x >= 0 && y+start.y >= 0 && x+start.x < T.length && y+start.y < T[0].length)
				{
					T[x+start.x][y+start.y] = new Terrain (0, x+start.x, y+start.y);
					T[x+start.x][y+start.y].setVisible(true);
				}
			}
		}

		// Creates player terrain and structure locations

		width = level;
		for(int x = -1 * width; x <= width; x++) // go from -r to r
		{
			int height = Math.abs( ( Math.abs(x) - width ) ) * 2 + 1; // it just works... o.O

			for(int y = 0 - height / 2; y < height / 2 + 1; y++)
			{
				T[x+start.x][y+start.y].setOwn(true);
				S[x+start.x][y+start.y] = new Structure (0,x+start.x,y+start.y);
			}
		}

		S[start.x][start.y] = new Structure (1,start.x,start.y);




		EDIT = false; // EDIT blah i have no idea...

		turn = 0;
		round = 15;

		exp = 0;
		food = 20;
		gold = 500;
		cap = 0;
		pop = 0;

		peasants		= 0;
		peasant_cost	= 15;
		soldiers		= 0;
		soldier_cost	= 25;
		builders		= 0;

		selected = null;
		army = -1;

		consumed = 0;
		produced = 0;

		armies = 0;

		difficulty = 0; //EDIT

		updateInfo(false);




		startGame = true;
	}







	public TBS()
	{
		createFileMenu();

		cp = getContentPane();
		cp.setLayout(new BorderLayout());		

		createGamePanels();

		createOptionPanels();


		Timer gameTimer = new Timer (50, new GameTimer());
		gameTimer.start();

	}

	class DifficultyButton implements ActionListener
	{
		public void actionPerformed (ActionEvent e)
		{
			int z = -1;

			String previous = "";

			if (difficulty == 1)
				previous = "Easy";
			else if (difficulty == 2)
				previous = "Normal";
			else if (difficulty == 3)
				previous = "Hard";

			try
			{
				z = JOptionPane.showOptionDialog(null, "Currently: "+previous, "Select Difficulty", 0, 
						JOptionPane.QUESTION_MESSAGE, null, new String[]{"Easy","Normal","Hard","Dificult","Expert","LOL","OMG","WTF","God","Ultima","Death"}, null);

				difficulty = z + 1;
			}catch(Exception error){}

		}
	}

	public void createOptionPanels ()
	{
		optionPanel = new JPanel();
		optionPanel.setLayout(new BorderLayout());

		JLabel title = new JLabel("[T]urn [B]ased [S]trategy");
		title.setFont(new Font(null,Font.BOLD,20));
		title.setHorizontalAlignment(JLabel.CENTER);
		JPanel optionsPanel = new JPanel();



		//////////////////////////////////////////////////////////////////////////////////////////////////////


		JButton difficultyButton = new JButton ("Dificulty");
		difficultyButton.addActionListener(new DifficultyButton());

		optionsPanel.add(difficultyButton);


		JButton mapButton = new JButton ("Map");
		mapButton.addActionListener(new DifficultyButton());

		optionsPanel.add(mapButton);









		class Test implements ActionListener
		{
			public void actionPerformed (ActionEvent e)
			{
				/*
				for(int x = 25; x < 200; x += 25)
					for(int y = 25; y < 200; y += 25)					
						for(int i = 0; i < 4; i++)
						{
							System.out.println("Type:   "+i);
							System.out.println();
							System.out.println("Your soldiers: "+x+"\nEnemy soldiers: "+y);
							printBattle(true,x,y,"rats",i);
							if (i == 3)
								printBattle(false,x,y,"rats",i);
							System.out.println();
							System.out.println();
							System.out.println();
						}
				*/
			}
		}



		JButton testButton = new JButton ("TEST");
		testButton.addActionListener(new Test());

		optionsPanel.add(testButton);















		/*

		JLabel difficultyLabel = new JLabel ("Difficulty: ");

		easy = new JRadioButton ("Easy");
		normal = new JRadioButton ("Normal",true);
		hard = new JRadioButton ("Hard");

//		easy.addActionListener(new Difficulty());
//		normal.addActionListener(new Difficulty());
//		hard.addActionListener(new Difficulty());


		ButtonGroup difficulty = new ButtonGroup();
		difficulty.add(easy);
		difficulty.add(normal);
		difficulty.add(hard);

		optionsPanel.setLayout(new GridLayout(9,1,10,10));

		JPanel dPanel = new JPanel();

		dPanel.add(difficultyLabel);
		dPanel.add(easy);
		dPanel.add(normal);
		dPanel.add(hard);

		optionsPanel.add(dPanel);

//		optionsPanel.add(difficultyLabel);
//		optionsPanel.add(easy);
//		optionsPanel.add(normal);
//		optionsPanel.add(hard);



		optionsPanel.setMaximumSize(new Dimension (20,100)); // what the fuck!!!!?



		JPanel gridPanel = new JPanel();
		gridPanel.setLayout(new GridLayout (3,2));

		JLabel grid = new JLabel ("Grid: ");
		JLabel empty = new JLabel ();

		JSlider maxx = new JSlider (10,50,30);
		JSlider maxy = new JSlider (10,50,20);


		maxx.setPreferredSize(new Dimension(0,0));
		maxy.setPreferredSize(new Dimension(0,0));

		JLabel maxxInfo =  new JLabel (maxx.getValue()+": ");
		JLabel maxyInfo =  new JLabel (maxy.getValue()+": ");

//		JTextArea maxx = new JTextArea ("20");
//		JTextArea maxy = new JTextArea ("30");
//		maxxInfo.setPreferredSize(new Dimension(10,10));
//		grid.setMaximumSize(new Dimension(10,10));
//		maxxInfo.setMaximumSize(new Dimension(10,10));
//		maxyInfo.setMaximumSize(new Dimension(10,10));

		gridPanel.add(grid);
		gridPanel.add(empty);
		gridPanel.add(maxxInfo);
		gridPanel.add(maxx);
		gridPanel.add(maxyInfo);
		gridPanel.add(maxy);

//		optionsPanel.add(grid);
//		optionsPanel.add(empty);
//		optionsPanel.add(maxxInfo);
//		optionsPanel.add(maxyInfo);
//		optionsPanel.add(maxx);
//		optionsPanel.add(maxy);

		optionsPanel.add(gridPanel);

		 */

		//////////////////////////////////////////////////////////////////////////////////////////////////////

		JButton start = new JButton ("Start");

		class Start implements ActionListener
		{public void actionPerformed (ActionEvent e)
		{GAME = true;}}

		start.addActionListener(new Start());
		JPanel bottomPanel = new JPanel();
		bottomPanel.add(start);





		JButton tutorial = new JButton ("Tutorial");

		class Tutorial implements ActionListener
		{public void actionPerformed (ActionEvent e)
		{getToolkit().beep();JOptionPane.showMessageDialog
		(null,"Tutorial not available yet...","SUCKER!", // EDIT create tutorial
				JOptionPane.ERROR_MESSAGE);}}

		tutorial.addActionListener(new Tutorial());
		bottomPanel.add(tutorial);



		JButton online = new JButton ("Online");

		class Online implements ActionListener
		{public void actionPerformed (ActionEvent e)
		{getToolkit().beep();JOptionPane.showMessageDialog
		(null,"HA!   Yeah right.","Hmph...", // EDIT ?
				JOptionPane.ERROR_MESSAGE);}}

		online.addActionListener(new Online());
		bottomPanel.add(online);

		//////////////////////////////////////////////////////////////////////////////////////////////////////

		optionPanel.add(title, BorderLayout.NORTH);
//		optionPanel.add(gamePanel, BorderLayout.CENTER);
		optionPanel.add(bottomPanel, BorderLayout.SOUTH);
		optionPanel.add(optionsPanel, BorderLayout.EAST); 
	}

	public void createGamePanels ()
	{
		PaintPanel paintPanel = new PaintPanel();
		paintPanel.setFocusable(true);
//		paintPanel.addKeyListener(new Keyboard());
		paintPanel.addMouseListener(new Mouse());

		JPanel buttonsPanel = new JPanel();

		buttonsPanel.setLayout(new GridLayout(12,1,10,10));

		for(int b = 0; b < B.length; b++)
		{
			B[b] = new JButton("");
			B[b].addActionListener(new Button());

			B[b].setVisible(false);
			buttonsPanel.add(B[b]);
		}

		JButton empty1 = new JButton();
		empty1.setVisible(false);
		JButton empty2 = new JButton();
		empty2.setVisible(false);
		buttonsPanel.add(empty1);
		buttonsPanel.add(empty2);

		wait = new JButton ("Wait");

		class Wait implements ActionListener{
			public void actionPerformed(ActionEvent e)
			{updateInfo(true);}}

		wait.addActionListener(new Wait());
		buttonsPanel.add(wait);

		info = new JLabel(" ");
		stats = new JLabel(" ");

		JPanel topPanel = new JPanel();
		topPanel.add(info);
		JPanel bottomPanel = new JPanel();
		bottomPanel.add(stats);



		mainPanel = new JPanel();
		mainPanel.setLayout(new BorderLayout());

		mainPanel.add(topPanel, BorderLayout.NORTH);
		mainPanel.add(paintPanel, BorderLayout.CENTER);
		mainPanel.add(buttonsPanel, BorderLayout.EAST);
		mainPanel.add(bottomPanel, BorderLayout.SOUTH);

//		cp.add(mainPanel);

//		cp.add(topPanel, BorderLayout.NORTH);
//		cp.add(gamePanel, BorderLayout.CENTER);
//		cp.add(buttonsPanel, BorderLayout.EAST);
//		cp.add(bottomPanel, BorderLayout.SOUTH);
	}





	public static void main (String[] Bridget)
	{
		TBS frame = new TBS();
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setTitle("");
		frame.setSize(1100, 750);
		frame.setLocation(0,0);
		frame.setVisible(true);
	}




	public void createFileMenu()
	{
		JMenu file = new JMenu("File");


		JMenuItem edit = new JMenuItem("Edit");

		class Edit implements ActionListener{
			public void actionPerformed(ActionEvent e)
			{EDIT = true;}}

		edit.addActionListener(new Edit());

		file.add(edit);




		JMenuItem exit = new JMenuItem("Exit");

		class Exit implements ActionListener{
			public void actionPerformed(ActionEvent e)
			{System.exit(0);}}

		exit.addActionListener(new Exit());

		file.add(exit);



		JMenuBar menuBar = new JMenuBar();
		setJMenuBar(menuBar);
		menuBar.add(file);
	}
}
