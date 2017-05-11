package strategy;

import java.awt.*;

public class Structure 
{
	private int type;
	private int X,Y;
	private int level;

	private String name;
	private int cost;
	private int time;
	private int pop;
	private int cap;
	private int workers;
	private int builders;

	private static String NAME[] = new String [10];
	private static int COST[] = new int [10];
	private static int TIME[] = new int [10];
	private static int POP[] = new int [10];
	private static int CAP[] = new int [10];
	private static int BUILDERS[] = new int [10];


	public Structure (int t, int x, int y)
	{
		type = t;
		X = x;
		Y = y;
		level = 1;
		workers = 0;

		int i = 0;

		NAME[i] = "Empty";
		COST[i] = 0;
		TIME[i] = 0;
		POP[i] = 0;
		CAP[i] = 0;
		BUILDERS[i] = 0;

		i++; // 1

		NAME[i] = "Base";
		COST[i] = 0;
		TIME[i] = 0;
		POP[i] = 15;
		CAP[i] = 0;
		BUILDERS[i] = 0;

		i++; // 2

		NAME[i] = "Farm";
		COST[i] = 35;
		TIME[i] = 3;
		POP[i] = 0;
		CAP[i] = 5;
		BUILDERS[i] = 1;

		i++; // 3

		NAME[i] = "House";
		COST[i] = 25;
		TIME[i] = 5;
		POP[i] = 10;
		CAP[i] = 0;
		BUILDERS[i] = 2;

		i++; // 4

		NAME[i] = "Barracks";
		COST[i] = 60;
		TIME[i] = 10;
		POP[i] = 0;
		CAP[i] = 0;
		BUILDERS[i] = 5;

		i++; // 5

		NAME[i] = "Armory";
		COST[i] = 1000;
		TIME[i] = 0;
		POP[i] = 0;
		CAP[i] = 0;
		BUILDERS[i] = 0;

		i++; // 6

		NAME[i] = "Treasury";
		COST[i] = 1000;
		TIME[i] = 0;
		POP[i] = 0;
		CAP[i] = 0;
		BUILDERS[i] = 0;

		i++; // 7

		NAME[i] = "Temple";
		COST[i] = 1000;
		TIME[i] = 0;
		POP[i] = 0;
		CAP[i] = 0;
		BUILDERS[i] = 0;

		

//		i++; // 8

//		NAME[i] = "Base";
//		COST[i] = 0;
//		TIME[i] = 0;


		name = 	NAME[t];
		cost = COST[t];
		time = TIME[t];
		pop = POP[t];
		cap = CAP[t];
		builders = BUILDERS[t];
	}
	public int getUpgradeCost ()
	{
		return (cost*(level+1));
	}
	public void setUpgrade ()
	{
		level++;
		cap += CAP[type];
	}
	public int getProduction ()
	{
		if (type == 2) // farm
		{
			return (int)(Math.pow(workers,2));
		}
		return 0;
	}
	public int getTimeLeft ()
	{
		return time;
	}
	public void addTime (int turn)
	{
		time -= turn;
	}
	public void setWorkers (int w)
	{
		workers = w;
	}
	public int getWorkers ()
	{
		return workers;
	}
	public int getLevel ()
	{
		return level;
	}
	public int getCost ()
	{
		return cost;
	}
	public String getName ()
	{
		return name;
	}
	public int getType ()
	{
		return type;
	}
	public int getCap ()
	{
		return cap;
	}
	public int getBuilders ()
	{
		return builders;
	}
	
//	public int getCost ()
//	{
//	return cost;
//	}
	public int getPop ()
	{
		return pop;
	}
	public int setTime (int z)
	{
		time -= z;

		return time;
	}
	public static int getCost (int t)
	{
		return COST[t];
	}
	public static int getTime (int t)
	{
		return TIME[t];
	}
	public static String getName (int t)
	{
		return NAME[t];
	}
	public static int getBuilders (int t)
	{
		return BUILDERS[t];
	}
	
	public void draw (Graphics g, int s)
	{
		g.setFont(new Font(null,Font.BOLD,s/2));

		{
			// Outline

			g.setColor(Color.blue);
//			g.drawRect(X*s-2, Y*s-2, s+4, s+4);
//			g.drawRect(X*s-1, Y*s-1, s+2, s+2);
		}

		if (time > 0)
		{
			g.setColor(Color.black);
			g.drawString("#",X*s+s/3, Y*s+s-s/3);
		}
		else
		{
			if (type == 0) // empty
			{
				g.setColor(Color.magenta);
//				g.drawString("o",X*s+s/3, Y*s+s-s/2);
			}
			else if (type == 1) // base
			{
//				g.setColor(Color.yellow);
//				g.fillRect(X*s+s/3, Y*s, s/3, s);
//				g.fillRect(X*s, Y*s+s/3, s, s/3);

				g.setColor(Color.black);
				g.drawString("C",X*s+s/3, Y*s+s-s/3);
			}
			else if (type == 2) // farm
			{
				g.setColor(Color.black);
				g.drawString("F",X*s+s/3, Y*s+s-s/3);
			}
			else if (type == 3) // house
			{
				g.setColor(Color.black);
				g.drawString("H",X*s+s/3, Y*s+s-s/3);
			}
			else if (type == 4) // bararcks
			{
				g.setColor(Color.black);
				g.drawString("B",X*s+s/3, Y*s+s-s/3);
			}
		}
	}

//	public static String getCost (int t)
//	{
//	return (cost[t]+"");
//	}
}
