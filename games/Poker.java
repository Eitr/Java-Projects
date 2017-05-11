package games;



import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

@SuppressWarnings("serial")
public class Poker extends JFrame
{
  static class GamePanel extends JPanel
  {
    int card[] = new int [52]; //all 52 cards : location of card (-1=deck)
    int P[][]; // [player][hand]
    int s = 40; //grid size
    Point loc[] = new Point [6]; //location of players
    int money[] = new int [6]; //initial amount of $
    int call; //amount to call
    int min; //minimum bid
    int bid[] = new int [6]; //amount player bid
    int turn; //which players turn it is
    boolean fold[] = new boolean [6]; //true if player folded
    int players;
    Rectangle button[] = new Rectangle [4];
    Point m = new Point(0,0); //mouse location on screen
    int phase = 0;
    int shared[] = new int [5];
    boolean check[] = new boolean [6];
    int pot;
    boolean show[] = new boolean [6];
    String name[] = new String [6];
    int rank[][] = new int [6][10];
    boolean winner[] = new boolean [6];
    boolean cont[] = new boolean [6];
    int dealer;
    
    public GamePanel ()
    {
      //initialize cards
      for (int i = 0; i < 52; i++)
        card[i] = -1;
      
      players = 6;//Integer.parseInt(JOptionPane.showInputDialog("# of players?",null));
      
      for (int i = 0; i < players; i++)
        show[i] = true;
      
      P = new int [players][2];
      
      for (int i = 0; i < players; i++)
      {
        money[i] = 1000;
        bid[i] = 0;
        fold[i] = false;
        check[i] = false;
        
        name[i] = "Player "+(i+1);
      }
      
      loc[0] = new Point (150,100);
      loc[1] = new Point (300,100);
      loc[2] = new Point (450,100);
      loc[3] = new Point (150,500-s);
      loc[4] = new Point (300,500-s);
      loc[5] = new Point (450,500-s);
      
      int x=605,y=05;
      
      button[0] = new Rectangle(x,  y,   s*2, s);
      button[1] = new Rectangle(x+s*2+5, y,   s*2, s);
      button[2] = new Rectangle(x,  y+s+5,  s*2, s);
      button[3] = new Rectangle(x+s*2+5, y+s+5,  s*2, s);
      
      
      Timer paint = new Timer (50, new Paint());
      paint.start();
      Timer game = new Timer (50, new Game());
      game.start();
      
      
      addMouseListener(new MouseAdapter(){
        public void mousePressed(MouseEvent e)
        {
          m = new Point(e.getX(),e.getY());
        }
      });
      addKeyListener(new KeyAdapter(){
        public void keyPressed(KeyEvent e)
        {
          int key = 6;
          
          try
          {
            key = Integer.parseInt(e.getKeyChar()+"") - 1;
          }
          catch(Exception x){}
          
          for (int i = 0; i < players; i++)
            if (key == i)
            show[i] = true;
          
          if (key == -1)
            for (int i = 0; i < players; i++)
            show[i] = true;
        }
        public void keyReleased(KeyEvent e)
        {
          int key = -1;
          
          try
          {
            key = Integer.parseInt(e.getKeyChar()+"") - 1;
          }
          catch(Exception x){}
          
          for (int i = 0; i < players; i++)
            if (key == i)
            show[i] = false;
        }
      });
    }
    
    
    
    /*==========================================================================
     * 
     *         PAINT METHOD
     * 
     =========================================================================*/
    
    protected void paintComponent (Graphics g)
    {
      super.paintComponent(g);
      setBackground(Color.white);
      g.setColor(Color.black);
      g.setFont(new Font("", Font.PLAIN, 20));
      
      for(int i = 0; i < players; i++)
      {
        g.setColor(Color.black);
        
        int x = loc[i].x;
        int y = loc[i].y;
        
        g.drawRect (x - s, y, s, s);
        g.drawRect (x, y, s, s);
        
        if (show[i])
        {
          g.drawString (printNum(P[i][0]/4), x-s*7/8, y+s*3/4);
          g.drawString (printNum(P[i][1]/4), x+s/8, y+s*3/4);
          
          if (P[i][0]%2 == 0)
          {
            g.setColor(Color.red);
            g.drawString (printFace(P[i][0]), x-s*2/5, y+s*3/4);
          }
          else
          {
            g.setColor(Color.blue);
            g.drawString (printFace(P[i][0]), x-s*2/5, y+s*3/4);
          }
          if (P[i][1]%2 == 0)
          {
            g.setColor(Color.red);
            g.drawString (printFace(P[i][1]), x+s*3/5, y+s*3/4);
          }
          else
          {
            g.setColor(Color.blue);
            g.drawString (printFace(P[i][1]), x+s*3/5, y+s*3/4);
          }
        }
        
        g.setColor(Color.black);
        g.drawString (name[i], x-s, y-s); /////////////////////// NAME of PLAYER
        g.drawString ("$ "+money[i], x-s, y-s/4);
        
        if (fold[i])
          g.drawString ("FOLD", x-s/2, y+s*3/2);
        else if (check[i])
          g.drawString ("Check", x-s/2, y+s*3/2);
        else if(phase < 8)
        {
          g.setColor(new Color(50,250,50));
          g.drawString (bid[i]+"", x-s/2, y+s*3/2);
        }
        else
        {
          if(winner[i])
            g.drawString ("Win!", x-s/2, y+s*3/2);
          
        }
      }
      
      g.setColor(Color.black);
      
      g.drawRect(0, 0, 600, 600);
      
      g.drawRect(300-s*2, 300-s/2, s*5, s);
      g.drawRect(300-s*1, 300-s/2, s, s);
      g.drawRect(300+s*1, 300-s/2, s, s);
      
      for (int i = 0; i < button.length; i++)
      {
        g.setColor(Color.black);
        
        g.fillRect(button[i].x,button[i].y,button[i].width,button[i].height);
        
        g.setColor(Color.white);
        
        if(i==0)
          g.drawString("Check",button[i].x+5,button[i].y+s-10);
        else if(i==1)
          g.drawString("Fold",button[i].x+5,button[i].y+s-10);
        else if(i==2)
          g.drawString("Call",button[i].x+5,button[i].y+s-10);
        else if(i==3)
          g.drawString("Raise",button[i].x+5,button[i].y+s-10);
      }
      
      g.setColor(Color.magenta);
      if(phase<9)
        g.drawOval(loc[turn].x-s*2,loc[turn].y-s*2,s*4,s*4);
      else
      {
        for (int i = 0; i < players; i++)
        {
          if(winner[i])
            g.drawOval(loc[i].x-s*2,loc[i].y-s*2,s*4,s*4);      
        }
      }
      
      if (phase > 5)
      {
        for(int i = 0; i < 5; i++)
        {
          g.setColor(Color.black);
          g.drawString(printNum(shared[i]/4),220+s*i+s/20,300+10);
          
          if (shared[i]%2 == 0)
            g.setColor(Color.red);
          else
            g.setColor(Color.blue);
          
          g.drawString (printFace(shared[i]),220+s*i+s/2,300+10);
        }
      }
      else if (phase > 3)
      {
        for(int i = 0; i < 4; i++)
        {
          g.setColor(Color.black);
          g.drawString(printNum(shared[i]/4),220+s*i+s/20,300+10);
          
          if (shared[i]%2 == 0)
            g.setColor(Color.red);
          else
            g.setColor(Color.blue);
          
          g.drawString (printFace(shared[i]),220+s*i+s/2,300+10);
        }
      }
      else if (phase > 1)
      {
        for(int i = 0; i < 3; i++)
        {
          g.setColor(Color.black);
          g.drawString(printNum(shared[i]/4),220+s*i+s/20,300+10);
          
          if (shared[i]%2 == 0)
            g.setColor(Color.red);
          else
            g.setColor(Color.blue);
          
          g.drawString (printFace(shared[i]),220+s*i+s/2,300+10);
        }
      }
      
      g.setColor(Color.black);
      g.drawString("Pot: "+pot,300-s,300+s);
      g.drawString("Call: "+(call-bid[turn]),300-s,300-s*3/4);
    }
    
    
    
    /*==========================================================================
     * 
     *         GAME METHOD
     * 
     =========================================================================*/
    
    class Game implements ActionListener
    {
      public void actionPerformed (ActionEvent e)
      {
        if (phase == 0)
        {
          min = 10;
          call = min;
          turn = dealer;
          pot = 0;
          for (int i = 0; i < players; i++)
          {
            check[i] = false;
            fold[i] = false;
            bid[i] = 0;
            winner[i] = false;
          }
          playerDeal();
          phase++;
        }
        else if (phase == 1)
        {
          bid();
        }
        else if (phase == 2) // FLOP
        {
          publicDeal();
          
          for (int i = 0; i < players; i++)
          {
            pot += bid[i];
            call = 0;
            bid[i] = 0;
            check[i] = false;
          }
          
          turn = dealer;
          phase++;
        }
        else if (phase == 3)
        {
          bid();
        }
        else if (phase == 4) // TURN
        {
          publicDeal();
          
          for (int i = 0; i < players; i++)
          {
            pot += bid[i];
            call = 0;
            bid[i] = 0;
            check[i] = false;
          }
          
          turn = dealer;
          phase++;
        }
        else if (phase == 5)
        {
          bid();
        }
        else if (phase == 6) // RIVER
        {
          publicDeal();
          
          for (int i = 0; i < players; i++)
          {
            pot += bid[i];
            call = 0;
            bid[i] = 0;
            check[i] = false;
          }
          
          turn = dealer;
          phase++;
        }
        else if (phase == 7)
        {
          bid();
        }
        else if (phase == 8) // GATHER
        {
          for (int i = 0; i < players; i++)
          {
            pot += bid[i];
            call = 0;
            bid[i] = 0;
            check[i] = false;
          }
          phase++;
        }
        else if (phase == 9) // WINNER
        {
          for (int p = 0; p < players; p++)
          {
            // Three of a kind
            
            int card = -1;
            
            
            /* I HATE LIFE */
            
            
            // Single pair
            
            card = -1;
            
            for (int d = 0; d < shared.length; d++) // go through shared pile
            {
              for (int c = 0; c < P[p].length; c++) // check player's hand
              {
                if (shared[d]/4 == P[p][c]/4)
                  if(shared[d]/4 > card)
                  card = shared[d]/4;
              }
              for (int i = 0; i < shared.length; i++) // check shared pile
              {
                if( i != d) // same card
                  if (shared[i]/4 == shared[d]/4)
                  if(shared[i]/4 > card)
                  card = shared[i]/4;
              }
            }
            if(P[p][0]/4 == P[p][1]/4) // go through player's hand
            {
              if(P[p][0]/4 > card)
                card = P[p][0]/4;
            }
            
            rank[p][1] = card;
            
            
            // Double pair
            
            card = -1;
            
            for (int d = 0; d < shared.length; d++) // go through shared pile
            {
              for (int c = 0; c < P[p].length; c++) // check player's hand
              {
                if (shared[d]/4 == P[p][c]/4)
                  if(shared[d]/4 > card)
                  if(shared[d]/4 != rank[p][1])
                  card = shared[d]/4;
              }
              for (int i = 0; i < shared.length; i++) // check shared pile
              {
                if( i != d) // same card
                  if (shared[i]/4 == shared[d]/4)
                  if(shared[i]/4 > card)
                  if(shared[i]/4 != rank[p][1])
                  card = shared[i]/4;
              }
            }
            if(P[p][0]/4 == P[p][1]/4) // go through player's hand
            {
              if(P[p][0]/4 > card)
                if(P[p][0]/4 != rank[p][1])
                card = P[p][0]/4;
            }
            
            
            if(card > -1)
            {
              rank[p][2] = rank[p][1];
              rank[p][1] = card;
            }
            else
              rank[p][2] = card;
            
            
            
            
            // Single card
            
            card = -1;
            
            for (int c = 0; c < P[p].length; c++)
            {
              if (P[p][c]/4 > card)
                if(P[p][c] != rank[p][1] || P[p][c] != rank[p][2])
                card = P[p][c]/4;
            }
            rank[p][0] = card;
            
          }
          
          /*
           *  WINNER
           */
          
          int win = 0;
          
          // TIE ???
          boolean end = false;
          
          for(int p = 0; p < players; p++)
          {
//      winner[p] = true;
            
            cont[p] = true;
          }
          if(!fold[0])
            winner[0] = true;
          else if(!fold[1])
            winner[1] = true;
          else if(!fold[2])
            winner[2] = true;
          else if(!fold[3])
            winner[3] = true;
          else if(!fold[4])
            winner[4] = true;
          else if(!fold[5])
            winner[5] = true;
          
          for (int i = 2; i >= 0 /* rank[p].length */; i--)
          {
            if(!end)
            {
              end = true;
              for(int p = 0; p < players; p++)
              {
                if(fold[p])
                  cont[p] = false;
                
                if(cont[p])
                {
//         if(p == 0)
//         {
//         System.out.println("              : "+rank[win][i]);
//         }
                  
                  if (rank[p][i] > rank[win][i])
                  {
                    for(int l = 0; l < players; l++)
                      winner[l] = false;
                    
                    winner[p] = true;
                    
                    win = p;
                    
//          cont[p] = true;
                  }
                  else if (rank[p][i] == rank[win][i])
                  {
                    winner[p] = true;
                    end = false;
                    
//          cont[p] = true;
                  }
                  else
                    winner[p] = false;
                }
              }
            }
            
            for(int p = 0; p < players; p++)
            {
              if(!winner[p])
                cont[p] = false;
            }
            
          }
          
//     for(int p = 0; p < players; p++)
//     {
//      if(winner[p])
//       System.out.println("winner: "+(1+p));
//     }
          
          
          phase++;
        }
        if(phase == 10)
        {
          for (int i = 0; i < 52; i++)
            card[i] = -1;     
          
          int winners = 0;
          
          for (int i = 0; i < players; i++)
            if(winner[i])
            winners++;
          
          pot /= winners;
          
          for (int i = 0; i < players; i++)
            if(winner[i])
            money[i] += pot;
          
          dealer++;
          if(dealer >= players)
            dealer = 0;
          phase++;
          
//     int card[] = new int [52]; //all 52 cards : location of card (-1=deck)
//     int P[][]; // [player][hand]
//     int s = 40; //grid size
//     Point loc[] = new Point [6]; //location of players
//     int money[] = new int [6]; //initial amount of $
//     int call; //amount to call
//     int min; //minimum bid
//     int bid[] = new int [6]; //amount player bid
//     int turn; //which players turn it is
//     boolean fold[] = new boolean [6]; //true if player folded
//     int players;
//     Rectangle button[] = new Rectangle [4];
//     Point m = new Point(0,0); //mouse location on screen
//     int phase = 8;
//     int shared[] = new int [5];
//     boolean check[] = new boolean [6];
//     int pot;
//     boolean show[] = new boolean [6];
//     String name[] = new String [6];
//     int rank[][] = new int [6][10];
//     boolean winner[] = new boolean [6];
//     boolean cont[] = new boolean [6];
        }
        else if(phase == 11)
        {
          phase++;
          
          String s = "";
          
          for(int p = 0; p < players; p++)
            if(winner[p])
            for(int r = 0; r < 3; r++)
            if(rank[p][r] > -1)
          {
//         System.out.println(r+"   : "+rank[p][r]);
            
            s = reason(p,r);
          }
          
          
          if(JOptionPane.showConfirmDialog(null,s) == JOptionPane.OK_OPTION)
            phase = 0;
        }
      }
    }
    
    public String reason(int p, int r)
    {
      String s = "";
      
      if(r == 2)
        s = "Two pair:  "+printNum(rank[p][2])+"'s & "+printNum(rank[p][1])+"'s, "+printNum(rank[p][0])+" kicker";
      else if(r == 1)
        s = "One pair:  "+printNum(rank[p][1])+"'s, "+printNum(rank[p][0])+" kicker";
      else if(r == 0)
        s = "High card:  "+printNum(rank[p][0]);
      
      
      
//   if (num <= 10)
//   s += num;
//   else if (num == 11)
//   s += "J";
//   else if (num == 12)
//   s += "Q";
//   else if (num == 13)
//   s += "K";
//   else if (num == 14)
//   s += "A";
      
      return s;
    }
    
    public String printNum(int card)
    {
      String s = "";
      int num = card + 2; //card number
      
      if (num <= 10)
        s += num;
      else if (num == 11)
        s += "J";
      else if (num == 12)
        s += "Q";
      else if (num == 13)
        s += "K";
      else if (num == 14)
        s += "A";
      
      return s;
    }
    public String printFace(int card)
    {
//   String s = "";
      int face = card%4; //card number
      
      if (face == 0)
        return "*";
      else if (face == 1)
        return "#";
      else if (face == 2)
        return "?";
      else //if (face == 3)
        return "%";
    }
    
    
    
    
    
    
    
    
    
    
    
    
    
    public void bid()
    {
      
      // BID ROUND
      boolean end = false;
      
//   while(!end)
      {
        if(fold[turn])
          turn++;
        else
        {
          int action = pressed();
          
          if (action == 0) //check
          {
            if(call == 0)
            {
              check[turn] = true;
              turn++;
            }
          }
          else if (action == 1) //fold
          {
            fold[turn] = true;
            check[turn] = false;
            turn++;
          }
          else if (action == 2) //call
          {
            if (call > 0)
            {
              check[turn] = false;
              money[turn] -= call-bid[turn];
              bid[turn] = call;
              turn++;
            }
          }
          else if (action ==3) //raise
          {
            int amount = 0;
            try
            {
              amount = Math.abs(Integer.parseInt(JOptionPane.showInputDialog("$?",null)));
            }
            catch(Exception x){}
            
            if (amount > 0)
            {
              check[turn] = false;
              call += amount;
              money[turn] -= call-bid[turn];
              bid[turn] = call;
              turn++;
            }
          }
        }
        
        if (turn >= players)
          turn = 0;
        
        
        end = true; 
        
        if (call == 0)
          for (int i = 0; i < players; i++)
        {
          if(!check[i] && !fold[i])
            end = false;
        }
        else
          for (int i = 0; i < players; i++)
        {
          if(bid[i] != call && !fold[i])
            end = false;
        }
        
      }
      if(end)
        phase++;
    }
    
    
    public void publicDeal()
    {
      int draw = (int) (Math.random() * 52); //picks random card from deck
      
      if (phase == 2)
      {
        for (int c = 0; c < 3; c++)
        {
          while (card[draw] != -1) //makes sure card is in the deck
            draw = (int) (Math.random() * 52);
          
          shared[c] = draw;
          card[draw] = 6; //sets which player has this card from deck
        }
      }
      else if (phase == 4)
      {
//    for (int c = 0; c < 3; c++)
        {
          while (card[draw] != -1) //makes sure card is in the deck
          draw = (int) (Math.random() * 52);
          
          shared[3] = draw;
          card[draw] = 6; //sets which player has this card from deck
        }
      }
      else if (phase == 6)
      {
//    for (int c = 0; c < 3; c++)
        {
          while (card[draw] != -1) //makes sure card is in the deck
          draw = (int) (Math.random() * 52);
          
          shared[4] = draw;
          card[draw] = 6; //sets which player has this card from deck
        }
      }
    }
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    /*============================================================================================
     =============================================================================================
     =============================================================================================
     ============================================================================================*/
    /*============================================================================================
     =============================================================================================
     =============================================================================================
     ============================================================================================*/
    
    
    
    
    
    
    
    
    
    /**
     * Checks whether button[i] was pressed
     */
    public int pressed ()
    {
      for(int i = 0; i < button.length; i++)
      {
        if(m.x > button[i].x && m.y > button[i].y && 
           m.x < button[i].x+button[i].width && m.y < button[i].y+button[i].height)
        {
          m = new Point (0,0);
          return i;
        }
      }
      return -1;
    }
    
    /**
     * Deals cards to all players currently playing, and fills up their hand  
     */
    public void playerDeal ()
    {
      int draw = (int) (Math.random() * 52); //picks random card from deck
      
      for (int p = 0; p < players; p++)
        for (int c = 0; c < P[p].length; c++)
      {
        while (card[draw] != -1) //makes sure card is in the deck
          draw = (int) (Math.random() * 52);
        
        P[p][c] = draw; //sets card to players hand
        card[draw] = p; //sets which player has this card from deck
      }
    }
    
    /**
     * Method returns string value of card given
     * @param card number (0-51)
     * @return String representing card
     */
    public String print (int card)
    {
      String s = "";
      int num = card/4 + 2; //card number
      int f = card%4; //card suit
      
      if (num <= 10)
        s += num;
      else if (num == 11)
        s += "J";
      else if (num == 12)
        s += "Q";
      else if (num == 13)
        s += "K";
      else if (num == 14)
        s += "A";
      
//   s += " ";
      
      if (f == 0)
        s += "*";
      else if (f == 1)
        s += "#";
      else if (f == 2)
        s += "?";
      else if (f == 3)
        s += "%";
      
      return s;
    }
    class Paint implements ActionListener
    {
      public void actionPerformed (ActionEvent e)
      {
        repaint();
      }
    }
  }
  class ButtonListener implements ActionListener
  {
    public void actionPerformed (ActionEvent e)
    {
      System.out.println("Ahh!!");
    }
  }
  
  public Poker ()
  {
//  JPanel GUI = new JPanel();
//  ButtonListener listener = new ButtonListener();
//  JButton check = new JButton("Check");
//  check.addActionListener(listener);
//  check.setSize(50, 10);
//  check.setLocation(610, 50);
//  GUI.add(check);
    
    GamePanel panel = new GamePanel();
//  panel.setSize(600, 600);
    
    Container cp = getContentPane();
    
    cp.add(panel);
//  cp.add(GUI);
//    panel.setFocusable(true);
  }
  
  public static void main (String[] args)
  {
    Poker frame = new Poker();
    frame.setTitle("Texas Hold'Em");
    frame.setLocationRelativeTo(null);
    frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    frame.setSize(800,600);
    frame.setVisible(true);
    frame.setLocation(0, 0);
  }
}
