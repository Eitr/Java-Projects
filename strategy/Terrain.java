package strategy;

import java.awt.*;

public class Terrain 
{
	private int type;
	private boolean visible;
	private boolean own;
	private int X,Y;
	int tax;

	public Terrain (int t,int x,int y)
	{
		type = t;
		visible = false;
		own = false;
		X = x;
		Y = y;
		
		if (type == 0) // empty
			tax = 2;
		else if (type == 1) // dirt
			tax = 1;
		else if (type == 2) // forest
			tax = 3;
		else if (type == 3) // mountain?
			tax = 7;
	}
	public void setVisible (boolean v)
	{
		visible = v;
	}
	public boolean getVisible()
	{
		return visible;
	}
	public void setOwn (boolean o)
	{
		own = o;
	}
	public void setType (int t)
	{
		type = t%5;
	}
	public int getType ()
	{
		return type;
	}
	public boolean getOwn()
	{
		return own;
	}
	public int getTax ()
	{
		return tax;
	}
	public void draw (Graphics g,int s)
	{
		if (visible)
		{
			g.setColor(new Color(0,200,0));
			g.fillRect(X*s+1, Y*s+1, s-1, s-1);
			
			if (type == 0) //empty
			{
//				g.setColor(new Color(50,200,50));
//				g.fillRect(X*s+1, Y*s+1, s-1, s-1);
			}
			else if (type == 1) //dirt
			{
//				g.setColor(new Color(50,250,50));
//				g.fillRect(X*s+1, Y*s+1, s-1, s-1);
				
				g.setColor(new Color(250,170,0));
				g.fillRect(X*s+1, Y*s+1, s/2-1, s/2-1);
				g.fillRect(X*s+s/2+1, Y*s+s/2+1, s/2-1, s/2-1);
				
				
//				for (int i = 0; i < s*10; i++)
//					g.fillRect(X*s+1+(int)(Math.random()*s), Y*s+1+(int)(Math.random()*s), 2, 2);
			}
			else if (type == 2) //forest
			{
				g.setColor(new Color(0,100,0));
				g.fillOval(X*s+s/4+1, Y*s+1, s/2-1, s/2-1);
				g.fillOval(X*s+1, Y*s+s/2+1, s/2-1, s/2-1);
				g.fillOval(X*s+s/2+1, Y*s+s/2+1, s/2-1, s/2-1);
			}
			else if (type == 3) //mountain
			{
				g.setColor(new Color(150,75,0));
				g.fillPolygon(new int[]{X*s,X*s+s,X*s+s/2},new int[]{Y*s+s,Y*s+s,Y*s}, 3);
			}
			else if (type == 4) //water
			{
				g.setColor(new Color(0,0,250));
				g.fillRect(X*s+1, Y*s+1, s-1, s-1);
			}
		}
//		else
//		{
//			g.setColor(Color.black);
//			g.fillRect(X*s, Y*s, s, s);
//		}

		if(own)
		{
			g.setColor(new Color(200,200,200));
			g.fillRect(X*s+1, Y*s+1, s-1, s-1);
			
		}

	}
}
