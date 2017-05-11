package maze;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Point;

public class Portal 
{
	private int x;
	private int y;
	
	private Color color;
	
	private boolean visited;
	
	private int frame = 0;
	private int r;
	
	
	public Portal (int x, int y, Color c)
	{
		this.x = x;
		this.y = y;
		color = c;
		visited = false;
	}
	
	
	public void draw (Graphics g)
	{
		int s = Labyrinth.size;

		g.setColor(Color.black);
		g.fillRect(x*s, y*s, s, s);
		
		
		int red 	= (color.getRed() 		<= 30)? 0 : color.getRed()		- (int)(Math.random()*30);
		int green 	= (color.getGreen() 	<= 30)? 0 : color.getGreen()	- (int)(Math.random()*30);
		int blue 	= (color.getBlue() 		<= 30)? 0 : color.getBlue()		- (int)(Math.random()*30);
		
		if (visited)		
			g.setColor(new Color(red,green,blue));
		else
//			g.setColor(Color.gray);
			g.setColor(new Color((int)(Math.random()*255),(int)(Math.random()*255),(int)(Math.random()*255)));
		
		frame++;
		
		if(frame%5==0)
		r = s-(int)(Math.random()*s/2);
		g.fillOval(x*s+s/2-r/2, y*s+s/2-r/2, r, r);
		

//		r = (int)(Math.random()*s);
//		g.fillRoundRect(x*s, y*s, s, s, r, r);
	}
	
	
	public int getX ()
	{
		return x;
	}
	
	public int getY ()
	{
		return y;
	}
	
	public Color getColor ()
	{
		return color;
	}
	
	public boolean hasVisited ()
	{
		return visited;
	}
	
	public void setVisited (boolean v)
	{
		visited = v;
	}
	
	public Point getLocation ()
	{
		return new Point(x,y);
	}

}
