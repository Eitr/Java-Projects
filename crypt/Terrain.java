package crypt;

import java.awt.*;

public class Terrain 
{

	private Point P;
	/** Terrain type 
	 * 0=wall
	 * 1=ground	
	 * 2=tree	
	 * 3=water	*/	
	private int T;
	private boolean H;

	public Terrain(int t, int x, int y,boolean h)
	{
		T=t;
		P=new Point(x,y);
		H=h;
	}
	public void Draw(Graphics g, int s, int x, int y, int sx, int sy, boolean h,int c)
	{
		if(P.x*s+x>=0 && P.y*s+y>=0 && P.x*s+x<800 && P.y*s+y<600)
		{
			if(T==0 && !H)
			{
				g.setColor(Color.red);
				g.drawRect(P.x*s+x+sx, P.y*s+y+sy, s, s);
				g.drawLine(P.x*s+x+sx, P.y*s+y+sy, P.x*s+x+sx+s, P.y*s+y+sy+s);
				g.drawLine(P.x*s+x+sx+s, P.y*s+y+sy, P.x*s+x+sx, P.y*s+y+sy+s);
			}
			else if((!h && !H)||(h && H))
				if(T==0)
				{
					g.setColor(Color.red);
					g.drawRect(P.x*s+x+sx, P.y*s+y+sy, s, s);
					g.drawLine(P.x*s+x+sx, P.y*s+y+sy, P.x*s+x+sx+s, P.y*s+y+sy+s);
					g.drawLine(P.x*s+x+sx+s, P.y*s+y+sy, P.x*s+x+sx, P.y*s+y+sy+s);
				}
				else if(T==1)
				{
					g.setColor(new Color(5,50,5));
					g.drawRect(P.x*s+x+sx, P.y*s+y+sy, s, s);
				}
				else if(T==2)
				{
					g.setColor(Color.green);
					g.drawLine(P.x*s+x+sx+s/2, P.y*s+y+sy, P.x*s+x+sx+s, P.y*s+y+sy+s/2);
					g.drawLine(P.x*s+x+sx+s/2, P.y*s+y+sy, P.x*s+x+sx, P.y*s+y+sy+s/2);
					g.drawLine(P.x*s+x+sx+s/2, P.y*s+y+sy+s, P.x*s+x+sx+s, P.y*s+y+sy+s/2);
					g.drawLine(P.x*s+x+sx+s/2, P.y*s+y+sy+s, P.x*s+x+sx, P.y*s+y+sy+s/2);
				}
				else if(T==3)
				{
					g.setColor(Color.blue);
					if(c%50<=25)
					{
						g.drawLine(P.x*s+x+sx+s/2, P.y*s+y+sy, P.x*s+x+sx, P.y*s+y+sy+s/2);
						g.drawLine(P.x*s+x+sx+s/2, P.y*s+y+sy+s, P.x*s+x+sx+s, P.y*s+y+sy+s/2);
					}
					else
					{
						g.drawLine(P.x*s+x+sx, P.y*s+y+sy+s, P.x*s+x+sx+s, P.y*s+y+sy);
					}
					g.drawRect(P.x*s+x+sx, P.y*s+y+sy, s, s);
				}
		}

	}
	public boolean CheckMove()
	{
		if(T==1)
			return true;
		else return false;
	}
	public boolean CheckThrow()
	{
		if(T==1 || T==3)
			return true;
		else return false;
	}
	public int getType()
	{
		return T;
	}	
	public void setType(int t,boolean h)
	{
		T=t;
		H=h;
	}	
	public boolean getHidden()
	{
		return H;
	}
	public Point getL()
	{
		return P;
	}
}
