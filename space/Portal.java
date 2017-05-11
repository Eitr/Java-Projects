package space;

import java.awt.*;

public class Portal 
{
	static int range = 120;
	int X;
	int Y;
	int connections;
	Point source_location;
	int source;

	int energy;
	
	
	
//	boolean active;


	public Portal (int x, int y, Point sl, int s)
	{
		X = x;
		Y = y;
//		range = 120;
		connections = 0;
		source_location = sl;
		source = s;
		energy = 10;
//		active = false;
	}

	public void draw (Graphics g)//, boolean active)
	{
		if (isReady())
		{
//			if (active)
//			{
//				g.setColor(Color.yellow);
//				active = false;
//			}
//			else
//				g.setColor(Color.white);

			g.setColor(Color.yellow);
			g.drawLine(X, Y, source_location.x, source_location.y);
		}
		if (isReady())
			g.setColor(Color.red);
		else
			g.setColor(Color.gray);

		g.fillOval(X-4,Y-4,9,9);
		
		if (energy > 0)
		{
			g.setColor(Color.white);
			g.drawString(""+energy, X, Y);
		}
	}





	public void addConnection ()
	{
		connections++;
	}
	public void addEnergy ()
	{
		energy--;
//		active = true;
	}
	public boolean isReady ()
	{
		if (energy <= 0)
			return true;
		return false;
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
		return new Point (X, Y);
	}
	public int getSource ()
	{
		return source;
	}
	public int getConnections ()
	{
		return connections;
	}


	public static int getRange ()
	{
		return range;
	}
}
