package space;

import java.awt.*;

public class Structure 
{
	int X;
	int Y;
	int type;
	int energy;

	int range;
	int speed;

	int source;

	static String [] NAME = new String [8];
	static int [] COST = new int [8];
	static int [] ENERGY = new int [8];
	static int [] RANGE = new int [8];
	static int [] SPEED = new int [8];



	private static int [] SIZE = new int [8];

	public Structure (int x, int y, int t, int s)
	{
		X = x;
		Y = y;
		type = t;

		source = s;

		int z = 0;

		NAME[z] = "Portal";
		COST[z] = 10;
		ENERGY[z] = 0; // command
		SIZE[z] = 21; // command

		z++;

		NAME[z] = "Miner";
		COST[z] = 20;
		ENERGY[z] = 20;
		SIZE[z] = 15;
		RANGE[z] = 60;
		SPEED[z] = 10;

		z++;

		NAME[z] = "Laser";
		COST[z] = 100;
		ENERGY[z] = 10;
		SIZE[z] = 0;

		z++;

		NAME[z] = "Missile";
		COST[z] = 500;
		ENERGY[z] = 10;
		SIZE[z] = 0;

		z++;

		NAME[z] = "Repair";
		COST[z] = 300;
		ENERGY[z] = 10;
		SIZE[z] = 0;


		speed = SPEED[type];
		energy = ENERGY[type];
	}

	public void draw (Graphics g)
	{
		if (!isReady())
		{
			g.setColor(Color.gray);
		}
		else
		{
			if (type == 0)
			{
				g.setColor(Color.red);
//				g.fillRect(X-10, Y-10, 20, 20);
			}
			else if (type == 1)
			{
				g.setColor(new Color(200,0,200));
//				g.fillRect(X-, Y-10, , 20);
			}
		}
		g.fillRect(X-SIZE[type]/2, Y-SIZE[type]/2, SIZE[type], SIZE[type]);

		if (energy > 0)
		{
			g.setColor(Color.white);
			g.drawString(""+energy, X, Y);
		}
	}


	public int getX ()
	{
		return X;
	}
	public int getY ()
	{
		return Y;
	}
	public Point getLocation ()
	{
		return new Point (X,Y);
	}
	public int getEnergy ()
	{
		return energy;
	}
	public int getType ()
	{
		return type;
	}
	public  int getSpeed ()
	{
		return speed;
	}

	public void addEnergy ()
	{
		energy--;
	}
	public boolean isReady ()
	{
		if (energy <= 0)
			return true;
		return false;
	}
	public int getSource ()
	{
		return source;
	}


	public static String getName (int t)
	{
		return NAME[t];
	}
	public static int getCost (int t)
	{
		return COST[t];
	}
	public static int getRange (int t)
	{
		return RANGE[t];
	}
}
