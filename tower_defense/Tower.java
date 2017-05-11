package tower_defense;

import java.awt.*;

public class Tower 
{

	int X;
	int Y;
	int speed;
	int range;
	int type;
	String name;
	String info; // EDIT
	int cost;
	int power;
	int level;

	static String NAME[] = new String [6];
//	static int RANGE[] = new int [6];
//	static int SPEED[] = new int [6];
//	static int POWER[] = new int [6];
	static int COST[] = new int [6];


	public Tower (int t, int x, int y)
	{
		type = t;
		X = x;
		Y = y;
		level = 1;

		setValues();




		name 	= NAME[type];
//		range	= RANGE[type];
//		speed	= SPEED[type];
//		power	= POWER[type];
		
		range = 1;
		speed = 1;
		power = 1;
	}
	private static void setValues ()
	{
		int z = 0;

		NAME[z] 	= "Sniper";
//		RANGE[z]	= 3;
//		SPEED[z]	= 2;
//		POWER[z]	= 1;
		COST[z]		= 10;

		z++;

		NAME[z] 	= "Turret";
//		RANGE[z]	= 1;
//		SPEED[z]	= 1;
//		POWER[z]	= 1;
		COST[z]		= 10;

		z++;

		NAME[z] 	= "Cannon";
//		RANGE[z]	= 2;
//		SPEED[z]	= 1;
//		POWER[z]	= 2;
		COST[z]		= 10;
	}
	
	public void upgrade ()
	{
		range = getNextRange();
		speed = getNextSpeed();
		power = getNextPower();
		
		level++;
	}

	public int getNextRange ()
	{
		int r = 0;
		
		if (type == 0)
		{
			r = range + 1;
		}
		else if (type == 1)
		{
			if (level == 5)
				r = range + 1;
			else
				r = range;
		}
		else if (type == 2)
		{
			if (level % 2 ==0)
				r = range + 1;
			else
				r = range;
		}
		
		return r;
	}
	public int getNextSpeed ()
	{
		int s = 0;
		
		if (type == 0)
		{
			s = speed + 1;
		}
		else if (type == 1)
		{
			s = speed + 5;
		}
		else if (type == 2)
		{
			s = speed + 1;
		}

		return s;
	}
	public int getNextPower ()
	{
		int p = 0;
		
		if (type == 0)
		{
			p = power + 2;
		}
		else if (type == 1)
		{
			p = power + 1;
		}
		else if (type == 2)
		{
			p = power + 9;
		}

		return p;
	}
	
	
	
	
	
	
	
	public int getX ()
	{
		return X;
	}
	public int getY ()
	{
		return Y;
	}
	public int getSpeed ()
	{
		return speed;
	}
	public int getRange ()
	{
		return range;
	}
	static int getRange (int t)
	{
		setValues();

//		return RANGE[t]; // EDIT?
		return 1;
	}
	public int getPower ()
	{
		return power;
	}
	public String getName ()
	{
		return name;
	}
	public int getLevel ()
	{
		return level;
	}
	static int getCost (int type)
	{
		return COST[type];
	}
	public void draw (Graphics g, int s)
	{
		if (type == 0)
		{
			g.setColor(new Color(0,200,0));
			g.setColor(Color.green);
			g.fillPolygon(new int[]{X*s, X*s+s/2, X*s+s, X*s+s/2}, new int[]{Y*s+s/2, Y*s+s, Y*s+s/2, Y*s}, 4);
			
//			g.drawRect(X*s,		Y*s,		s,			s);
			g.setColor(new Color(0,50,0));
			g.setColor(Color.black);
			g.fillRect(X*s+s/3,	Y*s+s/3,	s/3+2,		s/3+2);
			
			
		}
		else if (type == 1)
		{
			g.setColor(Color.green);
//			g.drawRect(X*s,		Y*s,		s,			s);
//			g.drawLine(X*s+s/2,	Y*s,		X*s+s/2,	Y*s+s);
//			g.drawLine(X*s,		Y*s+s/2,	X*s+s,		Y*s+s/2);

//			g.fillOval(X*s+s/4,			Y*s+s/4,		s/2,			s/2);
			
			g.fillRect(X*s,			Y*s,		s/3,		s/3);
			g.fillRect(X*s+s*2/3,	Y*s,		s/3,		s/3);
			g.fillRect(X*s,			Y*s+s*2/3,	s/3,		s/3);
			g.fillRect(X*s+s*2/3,	Y*s+s*2/3,	s/3,		s/3);
			
//			g.setColor(Color.black);
			g.drawRect(X*s+s/3,	Y*s+s/3,	s/3,		s/3);
		}
		else if (type == 2)
		{
			g.setColor(Color.green);
//			g.drawRect(X*s,		Y*s,		s,			s);
			
			g.fillOval(X*s,			Y*s,			s/2,		s/2);
			g.fillOval(X*s,			Y*s+s/2,		s/2,		s/2);
			g.fillOval(X*s+s/2,		Y*s,			s/2,		s/2);
			g.fillOval(X*s+s/2,		Y*s+s/2,		s/2,		s/2);

			g.setColor(Color.black);
			g.fillOval(X*s+s/4,	Y*s+s/4,	s/2+1,		s/2+1);
		}



//		g.setColor(Color.white);
//		g.drawOval((int)(X*s-(range*2+1)/2.*s+s/2),(int)(Y*s-(range*2+1)/2.*s+s/2),(range*2+1)*s,(range*2+1)*s);
	}
	public static void draw (Graphics g, int s, int type, int x, int y, boolean mouse)
	{
		if (mouse)
		{
			x -= s/2;
			y -= s/2;
		}
		
		if (type == 0)
		{
			g.setColor(Color.green);
			g.drawRect(x,y,s,s);
			g.drawLine(x+s/2,y,x,y+s/2);
			g.drawLine(x,y+s/2,x+s/2,y+s);
			g.drawLine(x+s/2,y,x+s,y+s/2);
			g.drawLine(x+s/2,y+s,x+s,y+s/2);
		}
		else if (type == 1)
		{
			g.setColor(Color.green);
			g.drawRect(x,y,s,s);
			g.drawOval(x,y,s/2,s/2);
			g.drawOval(x,y+s/2,s/2,s/2);
			g.drawOval(x+s/2,y,s/2,s/2);
			g.drawOval(x+s/2,y+s/2,s/2,s/2);
		}
		else if (type == 2)
		{
			g.setColor(Color.green);
			g.drawRect(x,y,s,s);
			g.drawLine(x+s/2,y,x+s/2,y+s);
			g.drawLine(x,y+s/2,x+s,y+s/2);
		}
//
//		g.setColor(Color.white);
//		g.drawOval((int)(x-(Tower.getRange(type)*2+1)/2.*s)+s/2,(int)(y-(Tower.getRange(type)*2+1)/2.*s)+s/2,
//				(Tower.getRange(type)*2+1)*s,(Tower.getRange(type)*2+1)*s);
	}
}
