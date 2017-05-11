package maze;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.KeyEventDispatcher;
import java.awt.KeyboardFocusManager;
import java.awt.Point;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.Timer;

/**
 * List of the required methods for setting up the game:<br>
 * <pre>
 *addMaze              setDifficulty
 *addPortal            setItem
 *setStartLocation     setStartMaze
 *setEndLocation       setEndMaze
 *</pre>
 *	Afterward, call the finalize() method.
 */
public class Labyrinth extends JPanel
{
	private static final long serialVersionUID = 5173608342573714560L;
	
	private Color currentMaze;
	
	private ArrayList<Maze> mazes = new ArrayList<Maze>();
	
	private Player player = new Player(new Point(-1,-1));
	
	private Point startLocation;
	
	private Color startMaze;
	
	private Point endLocation;
	
	private Color endMaze;
	
	private int difficulty;
	
	/** Pixel size of each grid cell */
	static int size = 24;
	/** Width of the grid */
	static int maxX = (int)(Toolkit.getDefaultToolkit().getScreenSize().width/size/2.5*2)/2*2+1;
	/** Height of the grid */
	static int maxY = (int)(Toolkit.getDefaultToolkit().getScreenSize().height/size/2.5*2)/2*2+1;
	
	
	private boolean loading;
	private boolean stopItems = false;
	private boolean win = false;
	
	Image startImage;
	Image endImage1,endImage2;
	
	int r;
	
	long time = System.currentTimeMillis();
	
	
	public Labyrinth ()
	{
		loading = true;
		
		try{
			startImage = this.getToolkit().getImage((this.getClass().getResource("/maze/resources/Start.png")));
			endImage1 = this.getToolkit().getImage((this.getClass().getResource("/maze/resources/Finish1.png")));
			endImage2 = this.getToolkit().getImage((this.getClass().getResource("/maze/resources/Finish2.png")));
			
		} catch(Exception e){JOptionPane.showMessageDialog(null, e.getMessage());}
		
		Timer paintTimer = new Timer(50, new PaintTimerListener());
		paintTimer.start();
		
		this.setVisible(true);
	}
	
	
	
	
	public void finalize ()
	{	
		
		player.setLocation(startLocation);
		currentMaze = startMaze;
		
		//		placeStartLocations();
		
		
		
		//		for(Maze m: mazes)
		//		{
		//			m.generateMaze();
		//		}
		
		getMaze(startMaze).addStart(startLocation);
		getMaze(startMaze).generateMaze();
		
		KeyboardFocusManager.getCurrentKeyboardFocusManager().addKeyEventDispatcher(new MyDispatcher());
		
		loading = false;
	}
	
	
	
	public void playerMove (int dx, int dy)
	{
		if ( getMaze(currentMaze).isPath(player.getX()+dx, player.getY()+dy) )
		{
			player.translate(dx, dy);
			
			
			
			
			if( currentMaze.equals(endMaze) && player.getLocation().equals(endLocation) && !win)
			{
				win = true;
			}
			else
			{		
				
				getMaze(currentMaze).setPlayerPath(player.getX(), player.getY());
				
				// Search Items
				if(!stopItems)
				{
					for (Item i: getMaze(currentMaze).items)
					{
						if (player.getLocation().equals(i.getLocation())) // Landed on a item
						{
							getMaze(currentMaze).items.remove(i);
							
							getMaze(i.color).wakeUp();
							
							new AddItem();
							
							break;
						}
					}
				}
				
				
				// Search Portals
				for (Portal p: getMaze(currentMaze).portals)
				{
					if (player.getLocation().equals(p.getLocation())) // Landed on a portal
					{
						for (Portal newp: getMaze(p.getColor()).portals)
						{
							if (newp.getColor().equals(currentMaze))
							{
								p.setVisited(true);
								newp.setVisited(true);
								player.setLocation(newp.getLocation());
								currentMaze = p.getColor();
								
								getMaze(p.getColor()).setPath(newp.getLocation(), 0);
								
								return;
							}
						}
					}
				}
			}
		}
	}
	
	
	private class AddItem extends Thread
	{
		public AddItem ()
		{
			this.start();
		}
		
		public void run ()
		{
			int numItems = 0;
			for(Maze m: mazes)
			{
				numItems += getMaze(m.color).items.size();
			}		
			while(numItems < (5-difficulty) && !stopItems)
			{
				newItem();
				numItems = 0;
				
				for(Maze m: mazes)
					if(isReachable(m.color))
						numItems += getMaze(m.color).items.size();
			}
		}
		
		private void newItem ()
		{
			int totalItems = 0;
			for(Maze m: mazes)
			{
				totalItems+= m.itemsRemaining;
			}
			if (totalItems <= 0)
			{
				stopItems = true;
				return;
			}
			
			int size = 0;
			int kill = 50;
			
			for(Maze m: mazes)
			{
				if(isReachable(m.color))
					size++;
			}
			
			Color reachableMazes [] = new Color[size];
			int id = 0;
			
			for(Maze m: mazes)
			{
				if(isReachable(m.color))
				{
					try { reachableMazes[id] = m.color; }
					catch(IndexOutOfBoundsException e) { return; }
					id++;
				}
			}
			
			
			
			int colorLocation = 0;
			
			if ( Math.random()*100 < (difficulty-1)*33 )
			{
				do
				{
					colorLocation = (int)(Math.random()*size);
				}
				while( reachableMazes[colorLocation].equals(currentMaze) && size > 1 );
			}
			else
			{
				//				for(int c = 0; c < size; c++)
				//					if (reachableMazes[colorMaze].equals(reachableMazes[c]))
				//					{
				//						colorLocation = c;
				//						break;
				//					}
				try 
				{
					//					while ( getMaze(reachableMazes[colorLocation]).itemsSpawned >= getMaze(reachableMazes[colorLocation]).maxItems)
					{
						colorLocation = (int)(Math.random()*size);
						
						//						kill--;
						//						if(kill < 0)
						//							return;
					}
				} catch(NullPointerException e) { return; }	
			}
			
			
			int colorMaze = 0;
			
			if ( Math.random()*100 < (difficulty-1)*33 )
			{
				do
				{
					colorMaze = (int)(Math.random()*size);
				}
				while(reachableMazes.length > 1 && reachableMazes[colorMaze].equals(reachableMazes[colorLocation]));
			}
			else
			{
				colorMaze = colorLocation;
				
				//				for(int c = 0; c < size; c++)
				//					if (currentMaze.equals(reachableMazes[c]))
				//					{
				//						colorMaze = c;
				//						break;
				//					}
			}
			
			try {
				while ( getMaze(reachableMazes[colorMaze]).itemsSpawned >= getMaze(reachableMazes[colorMaze]).maxItems)
				{
					colorMaze = (int)(Math.random()*size);
					
					kill--;
					if(kill < 0)
						return;
				}
			} catch(NullPointerException e) { return; }
			
			
			
			
			
			int px;
			int py;
			
			kill = 100;
			
			do
			{
				px = (int)(Math.random()*Math.abs(getMaze(reachableMazes[colorLocation]).maxx-3))/2*2+1;
				py = (int)(Math.random()*Math.abs(getMaze(reachableMazes[colorLocation]).maxy-3))/2*2+1;
				
				kill--;
				if(kill < 0)
					return;
			}
			while ( !getMaze(reachableMazes[colorLocation]).isPath(px, py) || !isPathClear(reachableMazes[colorLocation],px,py) );
			
			
			
			getMaze(reachableMazes[colorLocation]).items.add(new Item(px,py,reachableMazes[colorMaze]));
			getMaze(reachableMazes[colorMaze]).itemsSpawned++;
		}
		
	}
	
	private boolean isPathClear (Color c, int x, int y)
	{
		for(Portal p: getMaze(c).portals)
		{
			if(p.getLocation().equals(new Point(x,y)))
				return false;
		}
		
		for(Item i: getMaze(c).items)
		{
			if(i.getLocation().equals(new Point(x,y)))
				return false;
		}
		
		if(c.equals(startMaze) && startLocation.equals(new Point(x,y)))
			return false;
		
		if(c.equals(endMaze) && endLocation.equals(new Point(x,y)))
			return false;
		
		return true;
	}
	
	
	private boolean isReachable (Color maze)
	{
		for (Maze m: mazes)
		{
			m.visited = false;
		}		
		if(recursiveReachable(startMaze, maze))
			return true;
		return false;
	}
	
	private boolean recursiveReachable (Color current, Color goal)
	{
		if (current.equals(goal))
			return true;
		
		if (getMaze(current).visited)
			return false;
		getMaze(current).visited = true;
		
		for (Portal p: getMaze(current).portals)
		{
			if (getMaze(current).isPortalOnPath(p))
			{
				if (recursiveReachable(p.getColor(),goal))
					return true;
			}
		}
		
		return false;
	}
		
	
	private Maze getMaze (Color c)
	{
		for(Maze m: mazes)
		{
			if (m.color == c)
				return m;
		}
		return null;
	}
	
	
	private class MyDispatcher implements KeyEventDispatcher 
	{
		public boolean dispatchKeyEvent(KeyEvent e) 
		{
			if (e.getID() == KeyEvent.KEY_PRESSED) 
			{
				switch (e.getKeyCode())
				{
					case KeyEvent.VK_UP: 	playerMove(0,-1);	setDirection(1); break;
					case KeyEvent.VK_DOWN: 	playerMove(0, 1);	setDirection(3); break;
					case KeyEvent.VK_LEFT: 	playerMove(-1,0);	setDirection(4); break;
					case KeyEvent.VK_RIGHT:	playerMove( 1,0);  	setDirection(2); break;
					case KeyEvent.VK_W: 	playerMove(0,-1);	setDirection(1); break;
					case KeyEvent.VK_S: 	playerMove(0, 1);	setDirection(3); break;
					case KeyEvent.VK_A: 	playerMove(-1,0);	setDirection(4); break;
					case KeyEvent.VK_D:	playerMove( 1,0);  	setDirection(2); break;				
				}
			}
			return false;
		}
	}
	
	
	private class PaintTimerListener implements ActionListener
	{
		public void actionPerformed(ActionEvent e)
		{
			for (Maze m: mazes)
			{
				if (m.newItem == 1)
				{
					m.newItem = 2;
					new AddItem();
				}
			}
			
			
			repaint();
		}
	}
	
	
	protected void paintComponent(Graphics g)
	{
		super.paintComponent(g);
		setBackground(Color.black);
		int textSize = size/2;
		g.setFont(new Font("" , Font.PLAIN, textSize));
		g.setColor(Color.white);
		
		if (loading)
		{
			g.setFont(new Font("Georgia", Font.BOLD, 100));
			g.drawString("LOADING...", 100, 300);
			
		}
		else
		{
			
			draw(g);
			
			if(win)
			{
				if(System.currentTimeMillis()-time > 1000)
				{
					time = System.currentTimeMillis();
					r = (r+1)%mazes.size();
				}
				currentMaze = mazes.get(r).color;
				
				g.setColor(new Color((int)(Math.random()*255),(int)(Math.random()*255),(int)(Math.random()*255)));
				g.setFont(new Font("Georgia", Font.BOLD, 200));
				g.drawString("You won!",100,300);
				
			}
			else
			{
				if(System.currentTimeMillis()-time > 1000)
				{
					time = System.currentTimeMillis();
					generatePortalPaths();
				}
				
			}
		}
		
	}
	
	private void draw (Graphics g)
	{
		getMaze(currentMaze).draw(g);
		
		
		if(getMaze(currentMaze).color.equals(startMaze))
		{
			if( startImage != null)
			{
				g.drawImage(startImage, startLocation.x*size, startLocation.y*size,null);	
			}
			else
			{
				g.setColor(Color.white);
				g.fillRect(startLocation.x*size, startLocation.y*size, size, size);
			}
		}
		if(getMaze(currentMaze).color.equals(endMaze))
		{
			if( endImage1 != null || endImage2 != null)
			{
				if( ( System.currentTimeMillis() - time ) % 2 == 1 )
					g.drawImage(endImage1, endLocation.x*size, endLocation.y*size, null);
				else
					g.drawImage(endImage2, endLocation.x*size, endLocation.y*size, null);
			}
			else
			{
				g.setColor(Color.white);
				g.fillRect(endLocation.x*size, endLocation.y*size, size, size);
				g.setColor(Color.black);
				g.drawLine(endLocation.x*size, endLocation.y*size,(endLocation.x+1)*size, (endLocation.y+1)*size);
				g.drawLine((endLocation.x+1)*size, endLocation.y*size,endLocation.x*size, (endLocation.y+1)*size);
			}
		}
		
		g.setColor(Color.white);
		//		g.fillRect(0, maxY*size, maxX*size, 300);
		//		g.setColor(Color.black);
		g.setFont(new Font("Georgia",Font.BOLD, size*2));
		g.drawString(getMaze(currentMaze).name, 100, maxY*size+size*2);
		g.drawString((getMaze(currentMaze).maxItems-getMaze(currentMaze).itemsRemaining)
				+" / "+getMaze(currentMaze).maxItems, 800, maxY*size+size*2);
		
		
		if(!win)
			player.draw(g);
	}
	
	private void generatePortalPaths ()
	{
		for(Maze m: mazes)
		{
			for(Portal p: m.portals)
			{
				if(m.isPortalOnPath(p) && isReachable(m.color))
				{
					for(Maze om: mazes)
					{
						if(om.color.equals(p.getColor()))
						{
							for(Portal op: om.portals)
							{
								if(op.getColor().equals(m.color))
								{
									if(!om.isPortalOnPath(op))
									{
										om.addStart(op.getLocation());
										
										if(op.getLocation().y +1 < om.maxy)
											om.setPath(op.getLocation(), 1);
										else
											om.setPath(op.getLocation(), -1);
										
										
										if(!om.started)
										{
											om.generateMaze();
										}
									}
								}
							}
						}
					}
				}
			}
		}
	}
	
	
	
	
	/**
	 * Adds a maze to the list of rooms.
	 * @param color The identity of this room
	 * @param width Width of the grid
	 * @param height Height of the grid
	 */
	public void addMaze (Color color, int width, int height)
	{
		mazes.add(new Maze(color,width,height));
	}
	
	/**
	 * Adds a portal to the list of the specified room.
	 * @param maze Color of the maze to be added to
	 * @param x X-location of the portal
	 * @param y Y-location of the portal
	 * @param c Color of the room the portal leads to
	 */
	public void addPortal (Color maze, int x, int y, Color c)
	{
		Maze m = getMaze(maze);
		{
			for (Portal p: m.portals)
			{
				if ( p.getColor().equals(c) )
				{
					JOptionPane.showMessageDialog(null, "Only one portal link between any two mazes.", "Warning", JOptionPane.WARNING_MESSAGE);
					return;
				}
			}
			
			m.addPortal(new Portal(x/2*2+1,y/2*2+1,c));
		}
	}
	
	public void removePortal (Color maze, int x, int y)
	{
		getMaze(maze).removePortal(x, y);
	}
	
	/**
	 * Sets the amount of items needed to unlock the specified room.
	 * @param maze Color of the room the items belong to
	 * @param num The total amount of items required 
	 */
	public void setItem (Color maze, int num)
	{
		getMaze(maze).maxItems = num;
		getMaze(maze).itemsRemaining = num;
	}
	
	public Point getStartLocation ()
	{
		return startLocation;
	}
	
	public Color getStartMaze ()
	{
		return startMaze;
	}
	
	public Point getEndLocation ()
	{
		return endLocation;
	}
	
	public Color getEndMaze ()
	{
		return endMaze;
	}
	
	/**
	 * Sets the location of the start position on the grid.
	 * @param p The (x,y) of the start cell
	 */
	public void setStartLocation (Point p)
	{
		startLocation = new Point((p.x-1)/2*2+1,(p.y-1)/2*2+1);
	}
	
	/**
	 * Specifies which room the start location belongs to
	 * @param maze Color of the start maze
	 */
	public void setStartMaze (Color maze)
	{
		startMaze = maze;
	}
	
	/**
	 * Sets the location of the end position on the grid.
	 * @param p The (x,y) of the end cell
	 */
	public void setEndLocation (Point p)
	{
		endLocation = new Point((p.x-1)/2*2+1,(p.y-1)/2*2+1);
	}
	
	/**
	 * Specifies which room the end location belongs to
	 * @param maze Color of the end maze
	 */
	public void setEndMaze (Color maze)
	{
		endMaze = maze;
	}
	
	/**
	 * Sets the difficulty of the game, which determines how items are spawned
	 * @param dif A number ranging from 1 to 4 (easy -> hard)
	 */
	public void setDifficulty (int dif)
	{
		difficulty = dif;
	}
	
	
	
	public String getMazeName (Color maze)
	{
		return getMaze(maze).name;
	}
	
	public void setMazeName (Color maze, String name)
	{
		getMaze(maze).name = name;
	}
	
	private void setDirection (int dir)
	{
		player.direction = dir;
	}
	
}


