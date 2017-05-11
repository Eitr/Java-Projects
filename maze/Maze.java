package maze;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Point;
import java.util.ArrayList;
import java.util.ConcurrentModificationException;

enum CellType {WALL,PATH,EMPTY};

public class Maze 
{
	Color color;
	
	private Point start = null;
	
	String name;
	
	final int maxx;
	final int maxy;
	
	final int size = Labyrinth.size;
	
	ArrayList<Portal> portals = new ArrayList<Portal>();	
	
	ArrayList<Item> items = new ArrayList<Item>();
	int maxItems = 1;
	int itemsRemaining = maxItems;
	int itemsSpawned = 0;
	
	boolean visited = false;
	
	ArrayList<Point> currentPath = new ArrayList<Point>();
	
	private CellType [][] grid;
	
	private int [][][][] path;
	
	int newItem = 0;
	
	boolean started = false;
	
	
	public Maze (Color c, int width, int height)
	{
		color = c;
		
		if(width < Labyrinth.maxX) 	maxx = width/2*2+1;
		else 					maxx = Labyrinth.maxX;
		
		
		if(height < Labyrinth.maxY) 	maxy = height/2*2+1;
		else					maxy = Labyrinth.maxY;
		
		
		// Resize the grid array based on the new size of the grid
		grid = new CellType[maxx][maxy];
		path = new int[maxx][maxy][2][8];
		
		
		
		// Initialize all the blocks
		for(int x = 0; x < maxx; x++)
			for(int y = 0; y < maxy; y++)
			{
				grid[x][y] = CellType.EMPTY;
				
				for(int i = 0; i < 2; i++)
					for(int j = 0; j < 8; j++)
					{
						path[x][y][i][j] = -1;
					}
			}
	}
	
	public void generateMaze ()
	{
		start = new Point(start);
		
		started = true;
		
		createBoundary();
		
		currentPath.add(start);
		
		new GenerateMaze();
	}
	
	public void wakeUp ()
	{		
		itemsRemaining--;
		
		// Continue generating the maze until there are no more grid locations to work on            
		new GenerateMaze();

	}
	
	/** Generates the outer border walls of the maze and sets the start location */
	private void createBoundary ()
	{
		for(int x = 0; x < maxx; x+=2)
		{
			for(int y = 0; y < maxy; y++)
			{
				grid[x][y] = CellType.WALL;
			}
		}
		for(int x = 0; x < maxx; x++)
		{
			for(int y = 0; y < maxy; y+=2)
			{
				grid[x][y] = CellType.WALL;
			}
		}
		
		grid[start.x][start.y] = CellType.PATH;
		
	}

	
	
	/** Generates the maze based on specified width and height, and formats to fit the current screen resolution. */
	private class GenerateMaze extends Thread
	{
		public GenerateMaze ()
		{            
			this.start();
		}
		
		public void run () 
		{
			generateMaze();

			if (itemsRemaining == 0)
				fillEmpty();
		}
		
		public void generateMaze ()
		{			
			
			while(currentPath.size() > 0)
			{
				
				int sum = 0;
				for(int x = 0; x < maxx; x++)
					for(int y = 0; y<maxy; y++)
						if(grid[x][y] == CellType.PATH)
							sum++;
				
				if ( ((maxItems-itemsRemaining+1.0)/(maxItems+1.0))*((maxx-1)*(maxy-1)/2) < sum )
				{
					if (newItem == 0)
						newItem = 1;
					
					if (itemsRemaining != 0)
						return;
				}
				
				// A temporary list is used to prevent concurrent modification exceptions when adding/removing points
				ArrayList<Point> tempList = new ArrayList<Point>();
				
				// Randomly put some of the points to use in the current working order
				for(Point p: currentPath)
				{
					if(Math.random() > .9)
						if(!tempList.contains(p))
							tempList.add(p);
				}
				
				// Generate a new cell on each of the points in the current working order
				// Using lists instead of complete recursion prevents stack overflow
				for(Point p: tempList)
				{
					//                	if(Math.random() > .8)
					//                        currentPath.remove(p);
					//                    else
					{
						try
						{
							recursiveGenerator(p.x,p.y);
						}
						catch (Exception e)
						{
							System.err.println("Stopped generating maze: ");
							e.printStackTrace();
						}
						
					}
				}
				
			}
		}
		
		private void recursiveGenerator (int x, int y) throws Exception
		{            
			currentPath.remove(new Point(x,y)); // Remove this point from the current working order
			
			long time = System.currentTimeMillis(); // Wait time to visualize the creation       
			while(System.currentTimeMillis() - time < 40000/(maxx*maxy)) { }
			
			
			int cx = 0;
			int cy = 0;
			
			int kill = 20; // If the while loop gets caught without exiting, the kill variable will force it to stop
			
			do
			{
				do
				{
					kill--;
					if(kill <= 0)
					{
						if(Math.random()>.75)
						{
							int rand = (int)(Math.random()*2)*2-1;
							
							if(Math.random()>.5)
							{
								if(inBounds(x+rand,y)) grid[x+rand][y] = CellType.PATH;
							}
							else
							{
								if(inBounds(x,y+rand)) grid[x][y+rand] = CellType.PATH;
							}
						}
						
						return; // If the loop has been spinning so long without progress, this location is a dead end and so we quit
					}
					
					// Generate a random number that is either 2, 0, or -2 for the change in x and y directions
					cx = ((int)(Math.random() * 3) - 1)*2;
					cy = ((int)(Math.random() * 3) - 1)*2;
				}
				while( (     ( Math.abs(cx) == 2 && Math.abs(cy) == 2 )             // Prevents diagonally movement. i.e. it can either move horizontally or vertically, but not both
						||   ( Math.abs(cx) == 0 && Math.abs(cy) == 0 )             // Prevents no movement. i.e. it HAS to move in some direction
						||   !inBounds(x+cx,y+cy)));							   	// Prevents the point from moving off of the screen, keeping it in bounds
			}
			while ( grid[x+cx][y+cy] != CellType.EMPTY ); // Make sure the new location is an empty cell to move to
			
			Point temp = null; // The location for the new cell    
			
			x += cx/2;
			y += cy/2;
			
			grid[x][y] = CellType.PATH; // Set the wall in between this point and the new point to a path
			
			
			// Find the empty cell next to our current position
			for (int ox = x-1; ox <= x + 1; ox++)
			{
				for (int oy = y-1; oy <= y + 1; oy++)
				{
					if (grid[ox][oy] == CellType.EMPTY)
					{
						temp = new Point (ox,oy);
						break;
					}
				}
			}
			
			grid[temp.x][temp.y] = CellType.PATH; // Make the new cell a path too
			
			// Add this new location to the working order twice to create splits
			// This prevents the maze from dieing prematurely by dead end paths            
			currentPath.add(new Point(temp.x,temp.y));            
			currentPath.add(new Point(temp.x,temp.y));
		}
		

		/** Fill in any left over unvisited cells in the grid */
		private void fillEmpty ()
		{
//			int sum = 0;
//			for (int x = 0; x < maxx; x++)
//				for (int y = 0; y < maxy; y++)
//					if (grid[x][y] == CellType.EMPTY)
//						sum++;
//			if( maxx*maxy/8 < sum) // TODO
//			{
//				System.err.println("Terminated prematurely :(");
//				return false;
//			}
			
			
			for (int x = 0; x < maxx; x++)
			{
				for (int y = 0; y < maxy; y++)
				{
					if (grid[x][y] == CellType.EMPTY)
					{
						// Fill in the actual cell
						grid[x][y] = CellType.PATH;
						// Randomly destroy a wall surrounding it to connect it to the rest of the maze
						if(y > 1 && y < maxy - 2)
							grid[x][y + ((int)(Math.random()*2)*2-1)] = CellType.PATH;
						else if(x > 1)
							grid[x-1][y] = CellType.PATH;
						else
							grid[x+1][y] = CellType.PATH;
					}
				}
			}
			
			return;
		}
		
		private boolean inBounds (int x, int y)
		{
			return !(x <= 0 || x >= maxx-1 || y <= 0 || y >= maxy-1 );
		}
	}
	
	
	public boolean isPath (int x, int y)
	{
		return ( grid[x][y] == CellType.PATH );
	}
	
	public void setPath (Point p, int dy)
	{
		grid[p.x][p.y+dy] = CellType.PATH;
	}
	
	
	
	public void draw (Graphics g)
	{
		// Draw the grid
		
		for(int y = 0; y < maxy; y++)
			for(int x = 0; x < maxx; x++)
			{
				if(grid[x][y]!=null)
					switch(grid[x][y])
					{
						case EMPTY:{g.setColor(new Color(0,0,0));break;}
						case WALL:{g.setColor(new Color(0,0,0));break;}
						
						case PATH:{
							int r = 10;
							
							int red 	= (color.getRed() 		<= r)? 0 : color.getRed()		- (int)(Math.random()*r);
							int green 	= (color.getGreen() 	<= r)? 0 : color.getGreen()		- (int)(Math.random()*r);
							int blue 	= (color.getBlue() 		<= r)? 0 : color.getBlue()		- (int)(Math.random()*r);
							
							g.setColor(new Color(red,green,blue));
							
							break;}
					}
				else
				{
					System.err.println("Null grid location: ("+x+","+y+")");
				}
				
				if (path[x][y][0][0] > 0)
				{	
					g.fillPolygon(path[x][y][0],path[x][y][1],8);
				}
				else
					g.fillRect(x*size,y*size,size,size);
				
				
			}
		
		
		for (Portal p: portals)
		{
			p.draw(g);
		}
		try
		{
			for (Item i: items)
			{
				i.draw(g);
			}
		}
		catch(ConcurrentModificationException e){System.err.println("Fixed item draw.");}
		
	}
	
	
	public void addPortal (Portal p)
	{
		portals.add(p);
	}
	
	public void removePortal (int x, int y)
	{
		for (Portal p: portals)
		{
			if (p.getX() == x && p.getY() == y)
			{
				portals.remove(p);
				break;
			}
		}
	}
	
	
	public boolean isPortalOnPath (Portal p)
	{
		return ( grid[p.getX()][p.getY()] == CellType.PATH );
	}
	
	
	public void setPlayerPath (int x, int y)
	{
		path[x][y][0] = new int[]{
				(int)(Math.random()*size/2)+x*size,(int)(Math.random()*size/2)+x*size+size/2,x*size+size,x*size+size,(int)(Math.random()*size/2)+x*size+size/2,(int)(Math.random()*size/2)+x*size,x*size,x*size};
		path[x][y][1] = new int[]{
				y*size,y*size,(int)(Math.random()*size/2)+y*size,(int)(Math.random()*size/2)+y*size+size/2,y*size+size,y*size+size,(int)(Math.random()*size/2)+y*size+size/2,(int)(Math.random()*size/2)+y*size};
	}
	
	
	public void addStart (Point p)
	{
		start = p;
		currentPath.add(p);
	}
	
}





