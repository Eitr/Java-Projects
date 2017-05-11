package strategy;

import java.awt.*;

public class Army 
{
	private int X,Y;
	private int size;
	
	public Army (int soldiers, int x, int y)
	{
		X = x;
		Y = y;
		size = soldiers;
	}
	
	public int getSize ()
	{
		return size;
	}
	
	public int getX ()
	{
		return X;
	}
	public int getY ()
	{
		return Y;
	}
	public void setLocation (int x, int y)
	{
		X = x;
		Y = y;
	}
	
	public void draw (Graphics g, int s)
	{
		g.setFont(new Font("", Font.PLAIN, s/3));
		g.setColor(Color.white);
		g.drawString(size+"", X*s+2, Y*s+s-2);
		
		boolean end = false;
		Point B[] = new Point[s];

		int x;
		int y;
		
		for (int i = 0; i < size; i++) //draw all people 1-400
		{
			end = false;
			
			while(!end)
			{
				end = true;
				x = (int)(Math.random()*(s-2)+1);
				y = (int)(Math.random()*(s-2)+1);
				B[i] = new Point (x,y);
				for (int b = 0; b < i; b++) //check if location is already used
					if (B[b].x == x && B[b].y == y)
						end = false;
			}
			g.drawRect(B[i].x+s*X,B[i].y+s*Y,0,0);
		}
		
		/*
			g.setFont(new Font("", Font.PLAIN, s/3));
			int X;
			int Y;
			boolean end;
			Point B[] = new Point [9999];
			for (int y = 0; y <= 20; y++)
				for (int x = 0; x <= 20; x++)
				{
					g.setColor(Color.blue);
					g.drawRect(x*s,y*s,s,s);
					g.setColor(Color.red);
					for (int i = 0; i < x+y*20; i++) //draw all people 1-400
					{
						do
						{
							end = true;
							X = (int)(Math.random()*s);
							Y = (int)(Math.random()*s);
							B[i] = new Point (X,Y);
							for (int b = 0; b < i; b++) //check if location is already used
								if (B[b].x == X && B[b].y == Y)
									end = false;
						}
						while(!end);
						g.drawRect(B[i].x+s*x,B[i].y+s*y,0,0);						
					}
					g.setColor(Color.yellow);	
					g.drawString((x+y*20)+"",x*s,y*s+s);
				}
			*/
			
			
	}
}
