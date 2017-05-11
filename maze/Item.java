package maze;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Point;

public class Item 
{
	int x;

	int y;

	Color color;	

	long time = System.currentTimeMillis();

	public Item (int x, int y, Color c)
	{
		this.x = x;
		this.y = y;
		color = c;
	}


	public void draw (Graphics g)
	{
		int s = Labyrinth.size;

		if((System.currentTimeMillis()-time)%2 == 1)
			g.setColor(Color.WHITE);
		else
			g.setColor(Color.BLACK);
		g.setFont(new Font("Georgia",Font.BOLD,(int)(s*1.65)));
		g.drawString("X",x*s-4,y*s+s+2);

		int red 	= (color.getRed() 		<= 30)? 0 : color.getRed()		- (int)(Math.random()*30);
		int green 	= (color.getGreen() 	<= 30)? 0 : color.getGreen()	- (int)(Math.random()*30);
		int blue 	= (color.getBlue() 		<= 30)? 0 : color.getBlue()		- (int)(Math.random()*30);

		g.setColor(new Color(red,green,blue));

		g.setFont(new Font("Georgia",Font.BOLD,(int)(s*1.25)));
		g.drawString("X",x*s,y*s+s);

	}

	public Point getLocation ()
	{
		return new Point (x,y);
	}

}
