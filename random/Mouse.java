package random;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
/**
 * Shoort meteors for defense
 * @author mrshort
 *
 */
@SuppressWarnings("serial")
public class Mouse extends JFrame
{
    private KeyboardPanel keyboardPanel = new KeyboardPanel();
    
    public Mouse()
    {
        MessagePanel messagePanel = new MessagePanel("Blarg!");
        getContentPane().add(keyboardPanel);
        getContentPane().setLayout(new BorderLayout());
        getContentPane().add(messagePanel);
        
        keyboardPanel.setFocusable(true);
    }
    
    
    static class KeyboardPanel extends JPanel
    {
        static int sx=0;
        static int sy=0;
        
        public static int getx()
        {
            return sx;
        }
        public static int gety()
        {
            return sy;
        }
        
        public KeyboardPanel()
        {            
            addKeyListener(new KeyAdapter() 
            {
                public void keyReleased(KeyEvent e)
                {
                    switch(e.getKeyCode())
                    {}
                    //repaint();
                }
                public void keyPressed(KeyEvent e)
                {
                    switch(e.getKeyCode())
                    {
                        case KeyEvent.VK_A: sx-=1;
                        break;
                        case KeyEvent.VK_D: sx+=1;
                        break;
                        case KeyEvent.VK_W: sy-=1;
                        break;
                        case KeyEvent.VK_S: sy+=1;
                        break;
                        case KeyEvent.VK_X: sx=0;sy=0;
                        break;
                    }
                    //repaint();
                }
            }   );
        }
    }
    
    static class MessagePanel extends JPanel
    {
        private final int MAX=5000;
        
        public int sx=400,sy=550;
        private boolean shot[]=new boolean[MAX];
        private int X[]=new int[MAX],Y[]=new int[MAX];
        private int frame[]=new int[MAX];
        private int scale[]= new int[MAX];
        private int explosion[]= new int[MAX];
        private int sE=75;
        
        
        private final int ENEMIES=40;
        private int ex[]=new int[ENEMIES],ey[]=new int[ENEMIES];
        private boolean e[]= new boolean[ENEMIES];
        private int eframe[]=new int[ENEMIES];
        private int se=15;
        
        
        private int dir=1;
        
        
        /*
        private int X[]= new int[10000];
        private int Y[]= new int[10000];
        */
        
        public MessagePanel(String s)
        {
            Timer timer = new Timer(30, new TimerListener());
            timer.start();
            for(int i=0; i<ENEMIES; i++)
            {
                ex[i]=10+i*20;
                ey[i]=50;
            }
            
            addMouseListener(new MouseAdapter()
            {
                public void mousePressed(MouseEvent e)
                { 
                    for(int i=0; i<MAX; i++)
                    {
                    if(!shot[i])
                        {
                            X[i] = e.getX();
                            Y[i] = e.getY();
                            shot[i]=true;
                            frame[i]=0;
                            scale[i]=(int)Math.sqrt(Math.pow((sx-X[i]),2)+Math.pow((sy-Y[i]),2));
                            i=MAX;
                        }
                    } 
                    
                }
            });
            
            addMouseMotionListener(new MouseMotionAdapter()
            {
                public void mouseDragged(MouseEvent e)
                {
                    for(int i=0; i<MAX; i++)
                    {
                    if(!shot[i])
                        {
                            X[i] = e.getX();
                            Y[i] = e.getY();
                            shot[i]=true;
                            frame[i]=0;
                            scale[i]=(int)Math.sqrt(Math.pow((sx-X[i]),2)+Math.pow((sy-Y[i]),2));
                            i=MAX;
                        }
                    }
                }
            });
        }
        protected void paintComponent(Graphics g)
        {
            super.paintComponent(g);    
            g.setFont(new Font("" , Font.BOLD, 30));  
            setBackground(Color.black);
            g.setColor(Color.yellow);
            //g.fillRect(0,505,800,15);
            g.setColor(Color.blue);
            g.fillRect(sx-5,sy-5,9,9);
            g.setColor(Color.green);
            
            
            sx+=KeyboardPanel.getx();
            sy+=KeyboardPanel.gety();
            
            for(int i=0; i<ENEMIES; i++)
            {
                if(!e[i])
                g.fillOval(ex[i]-(se-1)/2,ey[i]-(se-1)/2,se,se);
                ey[i]+=3*dir;
                if(ey[i]>400)
                    dir=-1;
                else if(ey[i]<0)
                    dir=1;
                
                if(eframe[i]>=1)
                        {
                            g.drawOval(ex[i]-eframe[i]/2,ey[i]-eframe[i]/2, eframe[i], eframe[i]);
                            eframe[i]++;
                            if(eframe[i]>50)
                            {
                                eframe[i]=0;
                                e[i]=true;
                            }
                        }
            }
            
            //g.fillRect(250,250,5,5);
            for(int num=0; num<MAX; num++)
            {
                if(shot[num])
                {
                    if(explosion[num]==0)
                    {
                        g.setColor(Color.green);
                        int x= (sx-X[num])*frame[num]/scale[num];
                        int y= (sy-Y[num])*frame[num]/scale[num];
                        g.drawLine((int)(sx-(x/1.1)),(int)(sy-(y/1.1)),sx-x,sy-y);
                        g.drawOval(X[num],Y[num],4,4);
                        frame[num]+=15;
                        if(frame[num]>= scale[num])
                        {
                            //shot[num]=false;
                            explosion[num]=1;
                        }
                    }
                    else
                    {
                        g.setColor(new Color((int)(Math.random()*250),(int)(Math.random()*250),(int)(Math.random()*250)));
                        g.fillOval(X[num]-explosion[num]/2,Y[num]-explosion[num]/2, explosion[num], explosion[num]);
                        explosion[num]+=5;
                        if(explosion[num]>=sE)
                        {
                            shot[num]=false;
                            explosion[num]=0;
                        }
                    }
                    for(int i=0; i<ENEMIES; i++)
                    {
                        int D= (int)Math.sqrt(Math.pow((X[num]-ex[i]),2)+Math.pow((Y[num]-ey[i]),2));
                        //if(sx-x>ex[i]-(sex-1)/2 && sx-x<ex[i]+(sex-1)/2 && sy-y>ey[i]-(sey-1)/2 && sy-y<ey[i]+(sey-1)/2)
                        if( D <= explosion[num]/2 )
                            eframe[i]=1;
                        
                    }
                }
            }
            /*
            for(int j=0; j<i; j++)
                    g.fillRect(X[j],Y[j],2,2);
                    */
            //g.drawString("?", x, y);
            //g.drawString(x+"     "+y, 200, 200);
        }
        
    
        class TimerListener implements ActionListener
        {
            public void actionPerformed(ActionEvent e)
            {
                repaint();
            }
        }
    }
    
    public static void main(String[] args)
    {
        Mouse frame= new Mouse();
        frame.setTitle("");
        frame.setLocationRelativeTo(null);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(200,100);
        frame.setVisible(true);
    }    
    
}
    
        


