package blarg;

import java.awt.*;

public class Terrain 
{
	/** Type of terrain
	 * 0 Empty
	 * 1 Block
	 * 2 Tree
	 * 3 Water
	 * 4
	 */
	private int T;
	/** Block location on grid */
	Point P;
	/** If terrain is hidden (inside buildings) */
	private boolean H;

	public Terrain(int type, int x, int y)
	{
		T = type;
		P = new Point(x,y);
	}
	/**
	 * 
	 * @param g-graphics (g)
	 * @param s-grid size
	 */
	public void draw(Graphics g, int s, int f, boolean h)
	{
		int x = P.x*s;
		int y = P.y*s;

		/*/ Wall /*/
		if(T == 1)
		{
			g.setColor(new Color(250,0,0));
			g.drawRect(x,y,s,s);
			g.drawLine(x,y,x+s,y+s);
			g.drawLine(x,y+s,x+s,y);
		}
		if((H && h) || (!H && !h))
		{
			/*/ Empty /*/
			if(T == 0)
			{
				g.setColor(new Color(0,30,0));
//				g.drawRect(x,y,s,s);
				g.drawLine(x,y,x+s,y+s);
				g.drawLine(x,y+s,x+s,y);
			}

			/*/ Tree /*/
			if(T == 2)
			{
				g.setColor(new Color(0,250,0));
				g.drawLine(x,y+s/2,x+s/2,y);
				g.drawLine(x+s/2,y,x+s,y+s/2);
				g.drawLine(x,y+s/2,x+s/2,y+s);
				g.drawLine(x+s/2,y+s,x+s,y+s/2);
			}
			/*/ Water /*/
			else if(T == 3)
			{
				g.setColor(new Color(0,0,250));
//				g.drawRect(x,y,s,s);
				if(f%40<25)
				{
					g.drawLine(x,y+s/2,x+s/2,y);
					g.drawLine(x+s/2,y+s,x+s,y+s/2);
				}
				else
					g.drawLine(x,y+s,x+s,y);
			}
			/*/ Dirt /*/
			else if(T == 4)
			{
				g.setColor(new Color(60,50,0));
//				for(int i=0; i<200; i++)
//				g.drawOval((int)(Math.random()*s+x), (int)(Math.random()*s+y), s/10, s/10);

//				g.drawLine(x,y,x+s,y+s);
//				g.drawLine(x,y+s,x+s,y);

//				g.drawLine(x+s/2,y,x+s/2,y+s);
//				g.drawLine(x,y+s/2,x+s,y+s/2);

				g.drawLine(x+s/3,y,x+s/3,y+s);
				g.drawLine(x+s*2/3,y,x+s*2/3,y+s);
				g.drawLine(x,y+s/3,x+s,y+s/3);
				g.drawLine(x,y+s*2/3,x+s,y+s*2/3);

//				g.drawLine(x,y,x+s,y+s);
//				g.drawLine(x,y+s,x+s,y);

//				g.drawRect(x,y,s,s);
			}
			/*/ Vertical /*/
			else if(T == 5)
			{
				g.setColor(new Color(150,150,150));
				g.fillRect(x+s/3,y,s/3,s);
			}
			/*/ Horizontal /*/
			else if(T == 6)
			{
				g.setColor(new Color(150,150,150));
				g.fillRect(x, y+s/3, s, s/3);
			}
			/*/ Brace /*/
			else if(T == 7)
			{
				g.setColor(new Color(150,150,150));
				g.fillRect(x+s/3,y,s/3,s);
				g.fillRect(x, y+s/3, s, s/3);
			}
		}
	}
	public boolean getHidden()
	{
		return H;
	}
	public int getType()
	{
		return T;
	}
	public void setType(int t, boolean h)
	{
		T = t;
		H = h;
	}
	public Point getL()
	{
		return P;
	}
	public boolean checkMove()
	{
		if(T!=0 && T!=4)
			return false;
		else return true;
	}
}
