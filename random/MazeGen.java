package random;

import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.ConcurrentModificationException;

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

/**
 * @version 09/08/2011 v2
 */
@SuppressWarnings("serial")
public class MazeGen extends JFrame
{
    JPanel optionPanel;
    JSlider sliderX;
    JSlider sliderY;
    JLabel labelSlider;
    JLabel labelSize;
    JCheckBox pathSelect;
    JCheckBox wallSelect;
    JSlider traceSlider;
    
    boolean drawThinWall;
    
    /** List of all the grid locations still being used in maze generation */
    ArrayList<Point> currentPath = new ArrayList<Point>();
    
    /** Width of the grid */
    int maxX;
    /** Height of the grid */
    int maxY;
    /** Pixel size of each grid cell */
    int size;
    
    boolean showPath = true;
    boolean tracing = false;
    boolean pathing = false;
    
    /** The cell grid of the maze */
    int [][] B = new int[maxX][maxY];
    
    Point start = new Point(0,0);    
    Point finish = new Point(0,0);

    /** List of all grid locations of the correct path from start to finish */
    ArrayList<Point> path = new ArrayList<Point>();

    /** Current list of points while tracing is in progress */
    ArrayList<Point> trace = new ArrayList<Point>();
    
    
    
    Point currentCellGeneration = null;
    
    
    
    
    public MazeGen ()
    {
        this.setTitle("Maze");
        this.setDefaultCloseOperation(EXIT_ON_CLOSE);
        this.setSize(Toolkit.getDefaultToolkit().getScreenSize().width,Toolkit.getDefaultToolkit().getScreenSize().height-60);
        this.setLocationRelativeTo(null);
        
        createOptionPanel();
        
        this.setVisible(true);
        this.add(new PaintPanel());
        this.add(optionPanel, BorderLayout.EAST);
    }
    
    
    public void createOptionPanel ()
    {
        optionPanel = new JPanel();
        
        
        pathSelect = new JCheckBox("Show Path");
        class ShowPath implements ChangeListener
        {
            public void stateChanged(ChangeEvent e)
            {
                if(pathSelect.isSelected())
                    showPath = true;
                else
                    showPath = false;
            }
        }
        pathSelect.addChangeListener(new ShowPath());
        pathSelect.setSelected(true);
        
        wallSelect = new JCheckBox("Thin Wall");
        class ThinWall implements ChangeListener
        {
            public void stateChanged(ChangeEvent e)
            {
                if(wallSelect.isSelected())
                    drawThinWall = true;
                else
                    drawThinWall = false;
            }
        }
        wallSelect.addChangeListener(new ThinWall());
        
        
        labelSize = new JLabel("Press generate!");        
        
        sliderX = new JSlider();
        sliderX.setMinimum(9);
        sliderX.setMaximum(525);
        sliderX.setMajorTickSpacing((525-9)/6);
        sliderX.setPaintLabels(true);
        sliderX.setPaintTicks(true);
        
        sliderY = new JSlider();
        sliderY.setMinimum(9);
        sliderY.setMaximum(285);
        sliderY.setMajorTickSpacing((285-9)/6);
        sliderY.setPaintLabels(true);
        sliderY.setPaintTicks(true);
        
        traceSlider = new JSlider();
        traceSlider.setMinimum(1);
        traceSlider.setMaximum(10);
        traceSlider.setMajorTickSpacing(1);
        traceSlider.setPaintLabels(true);
        traceSlider.setPaintTicks(true);
        
        labelSlider = new JLabel("New [ "+sliderX.getValue()+" x "+sliderY.getValue()+" ]");
        
        
        
        class UpdateSliderLabel implements ChangeListener
        {
            public void stateChanged(ChangeEvent arg0)
            {
                labelSlider.setText("New: [ "+sliderX.getValue()+" x "+sliderY.getValue()+" ]");
            }
        }
        sliderX.addChangeListener(new UpdateSliderLabel());
        sliderY.addChangeListener(new UpdateSliderLabel());
        
        
        
        
        class CreateMaze implements ActionListener
        {
            public void actionPerformed (ActionEvent e)
            {
                if(pathing)
                {
                    pathing = false;
                }
                else
                {
                    pathing = true;
                    new Maze(sliderX.getValue(),sliderY.getValue());
                    labelSize.setText("Current: [ "+maxX+" x "+maxY+" ]");
                }
            }
        }
        class TraceMaze implements ActionListener
        {
            public void actionPerformed (ActionEvent e)
            {
                if(tracing)
                {
//                    pathSelect.setSelected(false);
                    tracing = false;
                }
                else
                {
                    pathSelect.setSelected(true);
                    tracing = true;
                    showPath = true;
                    new Path();
                }
            }
        }
        
        
        JButton createButton = new JButton("Generate!");
        createButton.addActionListener(new CreateMaze());
        
        JButton traceButton = new JButton("Trace!");
        traceButton.addActionListener(new TraceMaze());
        
        
        
        ////////////////////////////////////
        
        optionPanel.setLayout(new BoxLayout(optionPanel,BoxLayout.Y_AXIS));
        optionPanel.add(Box.createHorizontalStrut(100));
        optionPanel.add(Box.createVerticalGlue());
        optionPanel.add(labelSize);
        optionPanel.add(Box.createVerticalGlue());
        optionPanel.add(labelSlider);
        optionPanel.add(Box.createVerticalGlue());
        optionPanel.add(Box.createVerticalGlue());
        optionPanel.add(sliderX);
        optionPanel.add(Box.createVerticalGlue());
        optionPanel.add(sliderY);
        optionPanel.add(Box.createVerticalGlue());
        optionPanel.add(createButton);
        optionPanel.add(Box.createVerticalGlue());
        optionPanel.add(Box.createVerticalGlue());
        optionPanel.add(Box.createVerticalGlue());
        optionPanel.add(pathSelect);
        optionPanel.add(Box.createVerticalGlue());
        optionPanel.add(wallSelect);
        optionPanel.add(Box.createVerticalGlue());
        optionPanel.add(traceSlider);
        optionPanel.add(Box.createVerticalGlue());
        optionPanel.add(traceButton);
        optionPanel.add(Box.createVerticalGlue());
    }
    
    /** Generates the maze based on specified width and height, and formats to fit the current screen resolution. */
    public class Maze extends Thread
    {
        public Maze (int width, int height)
        {
            // Gets current screen resolution to determine how big to draw the maze
            int screenWidth = Toolkit.getDefaultToolkit().getScreenSize().width-220;
            int screenHeight = Toolkit.getDefaultToolkit().getScreenSize().height-80;
            
            // Make sure width/height are odd numbers
            width = width / 2 * 2 + 1;
            height = height / 2 * 2 + 1;
            
            // Sets size based on whether height or width is larger
            size = screenWidth / width;
            if ( screenHeight / height < size)
                size = screenHeight / height;
            
            maxX = width;
            maxY = height;
            
            start = new Point (1,1);
            finish = new Point(maxX-2,maxY-2);
            
            // Resize the grid array based on the new size of the grid
            B = new int[maxX][maxY];
            
            // Initialize all the blocks
            for(int x = 0; x < maxX; x++)
                for(int y = 0; y < maxY; y++)
                {
                    B[x][y] = 0;
                }
            
            this.start();
        }
        
        public void run ()
        {
            try
            {
                generateMaze();
                
                pathing = false;
            }
            catch (Exception e)    
            {
                currentPath.removeAll(currentPath);
            }
        }
        
        public void generateMaze () throws Exception
        {
            pathing = true;
            // New maze so remove everything from the previous path
            path.removeAll(path);
            trace.removeAll(trace);
            
            createBoundary();
            
            // start the maze by destroying a wall
            Point temp = destroyWall(start.x,start.y);
            temp = new Point(findEmpty(temp.x,temp.y));
            B[temp.x][temp.y] = 2;
            
            
            currentPath.add(start);
            
            // Continue generating the maze until there are no more grid locations to work on            
            while(currentPath.size() > 0)
            {
                // A temporary list is used to prevent concurrent modification exceptions when adding/removing points
                ArrayList<Point> tempList = new ArrayList<Point>();
                
                // Randomly put some of the points to use in the current working order
                for(Point p: currentPath)
                {
                    if(Math.random() > .5)
                        tempList.add(p);
                }
                
                // Generate a new cell on each of the points in the current working order
                // Using lists instead of complete recursion prevents stack overflow
                for(Point p: tempList)
                {
                    if(Math.random() > .8 && currentPath.size() > 6)
                        currentPath.remove(p);
                    else
                    {
                        recursiveGenerator(p.x,p.y);
                        
                        if(!pathing)
                            throw new Exception();
                        
                        if( Math.random() > .9 )
                            recursiveGenerator(p.x,p.y);
                    }
                }
            }
            currentCellGeneration = null;
            
            fillEmpty();
            
            new Path();
            
        }
                
        private void recursiveGenerator (int x, int y) throws Exception
        {            
            currentPath.remove(new Point(x,y)); // Remove this point from the current working order
            
            long time = System.currentTimeMillis(); // Wait time to visualize the creation  
            if(Math.random()*20>traceSlider.getValue())
            	while(System.currentTimeMillis() - time < 501-traceSlider.getValue()*10-400)
            	{ if(!pathing) throw new Exception();}
            
            
            int cx = 0;
            int cy = 0;
            
            int kill = 30; // If the while loop gets caught without exiting, the kill variable will force it to stop
            
            do
            {
                do
                {
                    kill--;
                    if(kill <= 0) return; // If the loop has been spinning so long without progress, this location is a dead end and so we quit
                    
                    // Generate a random number that is either 2, 0, or -2 for the change in x and y directions
                    cx = ((int)(Math.random() * 3) - 1)*2;
                    cy = ((int)(Math.random() * 3) - 1)*2;
                }
                while( (     ( Math.abs(cx) == 2 && Math.abs(cy) == 2 )                             // Prevents diagonally movement. i.e. it can either move horizontally or vertically, but not both
                        ||     ( Math.abs(cx) == 0 && Math.abs(cy) == 0 )                             // Prevents no movement. i.e. it HAS to move in some direction
                        ||     (x+cx <= 0 || x+cx >= maxX-1 || y+cy <= 0 || y+cy >= maxY-1 )));    // Prevents the point from moving off of the screen, keeping it in bounds
            }
            while ( B[x+cx][y+cy] != 0 ); // Make sure the new location is an empty cell to move to

            Point temp = null; // The location for the new cell    
            
            currentCellGeneration = new Point(x+cx,y+cy); // Point to display on screen that is currently being worked on
            
            x += cx/2;
            y += cy/2;
            
            B[x][y] = 2; // Set the wall in between this point and the new point to a path
            
            
            // Find the empty cell next to our current position
            for (int ox = x-1; ox <= x + 1; ox++)
            {
                for (int oy = y-1; oy <= y + 1; oy++)
                {
                    if (B[ox][oy] == 0)
                    {
                        temp = new Point (ox,oy);
                    }
                }
            }
                        
            B[temp.x][temp.y] = 2; // Make the new cell a path too
            
            // Add this new location to the working order twice to create splits
            // This prevents the maze from dieing prematurely by dead end paths            
            currentPath.add(new Point(temp.x,temp.y));
            if(Math.random()>.5)
            	currentPath.add(new Point(temp.x,temp.y));
        }
        
        private Point findEmpty (int x, int y)
        {
            for (int ox = x-1; ox <= x + 1; ox++)
            {
                for (int oy = y-1; oy <= y + 1; oy++)
                {
                    if (B[ox][oy] == 0)
                        return new Point (ox,oy);
                }
            }
            return null;
        }
        
        private Point destroyWall (int x, int y)
        {
            int cx = 0;
            int cy = 0;
            
            int kill = 40;
            
            do
            {
                do
                {
                    kill--;
                    if(kill <= 0)
                        return null;
                    
                    cx = ((int)(Math.random() * 3) - 1)*2;
                    cy = ((int)(Math.random() * 3) - 1)*2;
                }
                while(( Math.abs(cx) == 2 && Math.abs(cy) == 2 ) || ( Math.abs(cx) == 0 && Math.abs(cy) == 0 ) || (x+cx <= 0 || x+cx >= maxX-1 || y+cy <= 0 || y+cy >= maxY-1 ));
            }
            while ( B[x+cx][y+cy] != 0 );
            
            x += cx/2;
            y += cy/2;
            
            B[x][y] = 2;
            
            return new Point(x,y);
        }
        
        /** Generates the outer border walls of the maze and sets the start location */
        private void createBoundary ()
        {
            for(int x = 0; x < maxX; x+=2)
            {
                for(int y = 0; y < maxY; y++)
                {
                    B[x][y] = 1;
                }
            }
            for(int x = 0; x < maxX; x++)
            {
                for(int y = 0; y < maxY; y+=2)
                {
                    B[x][y] = 1;
                }
            }
            
            B[start.x][start.y] = 2;
            
        }
        
        /** Fill in any left over unvisited cells in the grid */
        private void fillEmpty ()
        {
            for (int x = 0; x < maxX; x++)
            {
                for (int y = 0; y < maxY; y++)
                {
                    if (B[x][y] == 0)
                    {
                        // Fill in the actual cell
                        B[x][y] = 2;
                        // Randomly destroy a wall surrounding it to connect it to the rest of the maze
                        if(x > 1 && y > 1 && y < maxY - 2)
                            B[x][y + ((int)(Math.random()*2)*2-1)] = 2;
                        else
                            B[x+1][y] = 2;
                    }
                }
            }
        }
        
    }
    
    /** Generates a connected solution path from the start point to the finish point. */
    public class Path extends Thread
    {
        
        public Path ()
        {
            tracing = true;
            this.start();
        }        
        
        public void run ()
        {    
            // Clear the old path first
            trace.removeAll(trace);
            
            try    
            {
                recursivePath(start.x,start.y,0);

                path.removeAll(path);
                for(Point p: trace)
                {
                    path.add(p);
                }
                        
                tracing = false;        
            }
            catch (Exception e)    {} // Thrown when this thread should be destroyed
            
            // Restart the maze generation process if the maze has no solution
            if (trace.size() < 4)
                new Maze(maxX,maxY);
        }
        
        public boolean recursivePath (int x, int y, int d) throws Exception
        {
            if (!tracing)
                throw new Exception();
            
            long time = System.currentTimeMillis();
            while(System.currentTimeMillis() - time < 501-traceSlider.getValue()*50) {}
            
            trace.add(new Point(x,y));
            
            // Sentinel case: Once the finish has been found we're done
            if(x == finish.x && y == finish.y)
                return true;
            
            // Check right
            
            int dx = 1;
            int dy = 0;
            
            if(d != 4)
                if(directionPath(x,y,dx,dy))
                    return true;
            
            // Check left
            
            dx = -1;
            dy = 0;
            
            if(d != 2)
                if(directionPath(x,y,dx,dy))
                    return true;
            
            // Check down
            
            dx = 0;
            dy = 1;
            
            if(d != 1)
                if(directionPath(x,y,dx,dy))
                    return true;
            
            // Check up
            
            dx = 0;
            dy = -1;
            
            if(d != 3)
                if(directionPath(x,y,dx,dy))
                    return true;
            
            return false;
        }
        
        /**
         * Mechanics of finding the correct path. Called on all 4 directions of each grid location.
         * @param x Horizontal location of the current grid cell
         * @param y Vertical location of the current grid cell
         * @param dx X-direction to look next
         * @param dy Y-direction to look next
         * @return True if the finish has been found, False otherwise
         * @throws Exception Thrown when thread should be destroyed
         */
        private boolean directionPath (int x, int y, int dx, int dy) throws Exception
        {
            // Determines the direction the path is heading, so as to not back track and create an infinite loop
            int d = 0; if(dx>0)    d=2; else if(dx<0) d=4; else if(dy>0) d=3; else if(dy<0) d=1;
            
            // Check if the cell in the given direction is inside the bounds of the grid
            if( !(x+dx*2 <= 0 || y+dy*2 <= 0 || x+dx*2 >= maxX || y+dy*2 >= maxY) )
                // The new path to check cannot have a wall in between them
                if ( B[x+dx][y+dy] != 1 )
                {
                    trace.add(new Point(x+dx,y+dy));
                    if(recursivePath(x+dx*2,y+dy*2,d))
                    {
                        return true;
                    }
                    else
                    {
                        trace.remove(new Point(x+dx,y+dy));
                        trace.remove(new Point(x+dx*2,y+dy*2));
                    }
                }
            
            if (!tracing)
                throw new Exception();
            
            return false;
        }
        
    }
    
    
    /** Panel that contains the paint canvas */
    class PaintPanel extends JPanel
    {
        public PaintPanel()
        {
            this.addMouseMotionListener(new MouseMotionInput());
            Timer paintTimer = new Timer(20, new PaintTimerListener());
            paintTimer.start();
        }
        
        private class MouseMotionInput implements MouseMotionListener
        {
            public boolean isInBounds(Point p)
            {
                return ( p.x > 0 && p.y > 0 && p.x < maxX -1 && p.y < maxY - 1 );
            }
            
            public void mouseDragged(MouseEvent e)
            {
                Point m = new Point((e.getX())/size,(e.getY())/size);
                
                
                
                switch(e.getButton())
                {
                    case 0:
                    {
                        
                        if(isInBounds(m) && B[m.x][m.y] != 1)
                        {
                            if(        B[m.x-1][m.y] == 3 ||
                                    B[m.x][m.y-1] == 3 ||
                                    B[m.x+1][m.y] == 3 ||
                                    B[m.x][m.y+1] == 3 ||
                                    (m.x == start.x && m.y == start.y) )
                                B[m.x][m.y] = 3;
                        }
                        break;
                    }
                    case 1:
                    {
                        new Maze(m.x,m.y);
                        break;
                    }
                }
                
            }
            
            public void mouseMoved(MouseEvent e)
            {
                
            }
        }
        
        private class PaintTimerListener implements ActionListener
        {
            public void actionPerformed(ActionEvent e)
            {
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
            
            // Draw the grid
            
            for(int y = 0; y < maxY; y++)
                for(int x = 0; x < maxX; x++)
                {
                    if(!drawThinWall)
                    {
                        switch(B[x][y])
                        {
                            case 0:{g.setColor(new Color(100,100,100));break;}
                            case 1:{g.setColor(new Color(0,0,0));break;}
                            case 2:{g.setColor(new Color(250,250,250));break;}
                            case 3:{g.setColor(new Color(0,250,0));break;}
                        }
                        
                        g.fillRect(x*size,y*size,size,size);
                    }
                    else
                    {
                        switch(B[x][y])
                        {
                            case 0:{g.setColor(new Color(100,100,100));break;}
                            case 1:{g.setColor(new Color(0,0,0));break;}
                            case 2:{g.setColor(new Color(250,250,250));break;}
                            case 3:{g.setColor(new Color(0,0,250));break;}
                        }
                        
                        int s = 1;
                        
                        if(x%2==0 && y%2==0)     g.fillRect(    x*size/2+(x*s+1)/2,        y*size/2+(y*s+1)/2,        s,        s);
                        else if(y%2==0)            g.fillRect(    (x-1)*size/2+(x*s+1)/2,    y*size/2+(y*s+1)/2,        size,    s);
                        else if(x%2==0)            g.fillRect(    x*size/2+(x*s+1)/2,        (y-1)*size/2+(y*s+1)/2,    s,        size);
                        else                    g.fillRect(    (x-1)*size/2+(x*s+1)/2,    (y-1)*size/2+(y*s+1)/2,    size,    size);
                        
                    }                    
                }
            
            // Draw the solution path
            
            try
            {
                if(showPath)
                {
                    if(tracing)
                    {
                        for(Point p: trace)
                        {
                            g.setColor(new Color(0,0,(int)(Math.random()*50)+200));
                            if(!drawThinWall)
                            {
                                g.fillRect(p.x*size,p.y*size,size,size);
                            }
                            else
                            {
                                int s = 1;
                                int x = p.x;
                                int y = p.y;
                                
                                if(x%2==0 && y%2==0)     g.fillRect(    x*size/2+(x*s+1)/2,        y*size/2+(y*s+1)/2,        s,        s);
                                else if(y%2==0)            g.fillRect(    (x-1)*size/2+(x*s+1)/2,    y*size/2+(y*s+1)/2,        size,    s);
                                else if(x%2==0)            g.fillRect(    x*size/2+(x*s+1)/2,        (y-1)*size/2+(y*s+1)/2,    s,        size);
                                else                    g.fillRect(    (x-1)*size/2+(x*s+1)/2,    (y-1)*size/2+(y*s+1)/2,    size,    size);
                            }
                        }
                    }
                    else
                    {
                        for(Point p: path)
                        {
                            g.setColor(new Color(0,0,(int)(Math.random()*50)+200));
                            if(!drawThinWall)
                            {
                                g.fillRect(p.x*size,p.y*size,size,size);
                            }
                            else
                            {
                                int s = 1;
                                int x = p.x;
                                int y = p.y;
                                
                                if(x%2==0 && y%2==0)     g.fillRect(    x*size/2+(x*s+1)/2,        y*size/2+(y*s+1)/2,        s,        s);
                                else if(y%2==0)            g.fillRect(    (x-1)*size/2+(x*s+1)/2,    y*size/2+(y*s+1)/2,        size,    s);
                                else if(x%2==0)            g.fillRect(    x*size/2+(x*s+1)/2,        (y-1)*size/2+(y*s+1)/2,    s,        size);
                                else                    g.fillRect(    (x-1)*size/2+(x*s+1)/2,    (y-1)*size/2+(y*s+1)/2,    size,    size);
                            }
                        }
                    }
                }
            }
            catch(ConcurrentModificationException e){}
            
            // Draw the start and finish locations
            
            g.setColor(new Color((int)(Math.random()*100)+150,0,0));
            if(!drawThinWall)
            {
                g.fillRect(start.x*size,start.y*size,size,size);
                g.fillRect(finish.x*size,finish.y*size,size,size);
            }
            else
            {
                int s = 1;
                g.fillRect(    (start.x-1)*size/2+(start.x*s+1)/2,    (start.y-1)*size/2+(start.y*s+1)/2,    size,    size);
                g.fillRect(    (finish.x-1)*size/2+(finish.x*s+1)/2,    (finish.y-1)*size/2+(finish.y*s+1)/2,    size,    size);
            }
            
            if(currentCellGeneration != null)
            {
                g.setColor(new Color((int)(Math.random()*250),(int)(Math.random()*250),(int)(Math.random()*250)));
                g.fillRect(currentCellGeneration.x*size,currentCellGeneration.y*size,size,size);
            }
            
        }
    }
    
    public static void main (String[] blarg)
    {
        new MazeGen();
    }
    
}
