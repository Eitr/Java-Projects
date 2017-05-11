package maze;

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Point;
import java.awt.Toolkit;

public class Player
{
	int direction = 3;
	
	private Point location;
    
    Image frames [][] = new Image[4][4];
    
	final int size = Labyrinth.size;
    
	
	public Player (Point p)
	{
		location = new Point(p);
		
		try 
        {
        	for(int d = 0; d < 4; d++)
        	for(int i = 0; i < 4; i++)
        	{
        		switch(i)
        		{
        		
        		case 0: frames[d][i]= Toolkit.getDefaultToolkit().getImage((this.getClass().getResource("/maze/resources/p"+(1+d*3)+".png"))); break;
        		case 1: frames[d][i]= Toolkit.getDefaultToolkit().getImage((this.getClass().getResource("/maze/resources/p"+(2+d*3)+".png"))); break;
        		case 2: frames[d][i]= Toolkit.getDefaultToolkit().getImage((this.getClass().getResource("/maze/resources/p"+(3+d*3)+".png"))); break;
        		case 3: frames[d][i]= Toolkit.getDefaultToolkit().getImage((this.getClass().getResource("/maze/resources/p"+(2+d*3)+".png"))); break;
        		
        		}
        		
        	}
        		
        } 
        catch (Exception e) { }
        
	}
		
	public Point getLocation ()
	{
		return location;
	}
	
	public void translate (int dx, int dy)
	{
		setLocation( new Point(location.x+dx,location.y+dy) );
	}
	
	public void setLocation (Point p)
	{
		location = new Point(p);
	}
	public int getX ()
	{
		return location.x;
	}
	
	public int getY ()
	{
		return location.y;
	}
	
    
    long time = System.currentTimeMillis();
   
    int frame = 0; 
    
	
	public void draw (Graphics g)
	{
		if ( (System.currentTimeMillis() - time) > 50  )
		{
			frame = (frame+1)%4;
			time = System.currentTimeMillis();
		}
		
		Graphics2D g2 = (Graphics2D)(g);

		g2.scale(2, 2);
		
        g.drawImage(frames[direction-1][frame], location.x*size/2-size/4,location.y*size/2-size/4,null);
        
        g2.scale(.5, .5);
        
//		g.setColor(Color.red);
//        g.fillOval(location.x*size, location.y*size, size, size);
		
	}

}
