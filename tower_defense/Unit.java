package tower_defense;

import java.awt.*;

public class Unit 
{
	int X;
	int Y;
	int type;
	int speed;
	int health;
	String name;
	String info;
	int money;

	int dir;
	int skip;
	
	boolean dead;

	public Unit (int t, int x, int y)
	{
		type = t;
		X = x;
		Y = y;

		dir = 0;
		dead = false;

		if (type == 0)
		{
			name 	= "Fred";
			speed 	= 1;
			skip 	= 3;
			health 	= 1;
			money 	= 1;
		}
		else if (type == 1)
		{
			name 	= "Fred";
			speed 	= 1;
			skip 	= 3;
			health 	= 4;
			money 	= 1;
		}
		else if (type == 2)
		{
			name 	= "Fred";
			speed 	= 15;
			skip 	= 3;
			health 	= 1;
			money 	= 1;
		}
	}
	public int getMoney ()
	{
		return money;
	}
	public int getX ()
	{
		return X;
	}

	public int getY ()
	{
		return Y;
	}
	public int getSkip ()
	{
		return skip;
	}
	public void setDamage (int power)
	{
		health -= power;
		
		if (health <= 0)
			dead = true;
	}
	public boolean isDead ()
	{
		return dead;
	}
	public void setDead ()
	{
		dead = true;
	}
	public void draw (Graphics g, int s)
	{
		if (type == 0)
		{
			g.setColor(Color.red);
			g.fillOval(X*s+s/4,Y*s+s/4,s/2,s/2);
		}
		else if (type == 1)
		{
			g.setColor(Color.red);
			g.fillRect(X*s+s/4,Y*s+s/4,s/2,s/2);
		}
		else if (type == 2)
		{
			g.setColor(Color.red);
			g.fillPolygon(new int[]{X*s, X*s+s/2, X*s+s, X*s+s/2}, new int[]{Y*s+s/2, Y*s+s, Y*s+s/2, Y*s}, 4);
		}
	}
	public void move (boolean Path[][])  // check whether able to move
	{
		int z;
		int kill = 0; // prevents infinite loop if move is impossible
		
		while (true)
		{
			z = (int)(Math.random()*4 + 1);
			
			while (z == dir)
				z = (int)(Math.random()*4 + 1);

			
			if (z == 1)
			{
				if (Path[X][Y-1])
					break;
			}
			else if (z == 2)
			{
				if (Path[X+1][Y])
					break;
			}
			else if (z == 3)
			{
				if (Path[X][Y+1])
					break;
			}
			else if (z == 4)
			{
				if (Path[X-1][Y])
					break;
			}
			kill++;
			if (kill > 500)
				dir = 0;
			if (kill > 1000)
			{
				z = 0;
				break;
			}
		}
		
		if (z == 1)
		{
			Y--;
			dir = 3;
		}
		else if (z == 2)
		{
			X++;
			dir = 4;
		}
		else if (z == 3)
		{
			Y++;
			dir = 1;
		}
		else if (z == 4)
		{
			X--;
			dir = 2;
		}

	}
	public int getSpeed ()
	{
		return speed;
	}
}
