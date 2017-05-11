package blarg;

import java.awt.*;
import javax.swing.*;

@SuppressWarnings("unused")
public class Enemy 
{
	String name;
	/** Enemy location on grid */
	private Point P;

	/** Physical health */
	private int HP;
	/** Mental health */
	private int MP;
	/** Maximum hp */
	private int MAXHP;
	/** Maximum mp */
	private int MAXMP;
	
	/** Exp given to character upon defeat */
	private int EXP;

	/** Strength */
	private int STR;
	/** Stamina */
	private int STM;
	/** Dexterity */
	private int DXT;
	/** Intellect */
	private int INT;
	/** Wisdom */
	private int WSD;
	/** Charisma */
	private int CHR;
	
	/** Enemy type
	 * 0 = Edit
	 * 1 = blob?
	 */
	private int T;
	
	private int LOS;
	
	private Item I;


	public Enemy(int t, int x, int y)
	{
		P = new Point(x,y);
		
		name = "Blarg";
		
		EXP 	=		10;

		STR 	=		4;
		STM 	=		4;
		DXT 	=		5;
		INT 	=		5;
		WSD 	=		5;
		CHR 	=		6;

		MAXHP	=		STM*3;
		MAXMP	=		WSD*2;
		HP 		=		MAXHP;
		MP 		=		MAXMP;
		
		LOS		=		6;
		
		I		=		new Item(0);
		
		T = t;
		
		if(T == 0)
		{
			STR = Integer.parseInt(JOptionPane.showInputDialog("STR?"));
		}
	}
	/**
	 * Shifts enemy location
	 * @param x horizontal shift
	 * @param y vertical shift
	 */
	public void move(int x, int y)
	{
		P.x+=x;
		P.y+=y;
	}
	/**
	 * Calls enemy position on grid
	 * @return Character Location
	 */
	public Point getL()
	{
		return P;
	}

	public void draw(Graphics g, int s, int column, int row)
	{
		int x = P.x*s;
		int y = P.y*s;

		g.setColor(Color.white);
		g.setFont(new Font("", Font.PLAIN,(int)(s)));
		g.drawString("O",x+4,y+s-2);
		g.drawOval(x-LOS*s,y-LOS*s,(LOS*2+1)*s,(LOS*2+1)*s);
	}
	public int getLOS()
	{
		return LOS;
	}
	public int getMissed(int eDXT)
	{
		for(int i=4; i>1; i--)
			if(DXT >= i*eDXT)
			{
				return i;
			}
		if( 1-(eDXT - DXT)/(double)eDXT > Math.random()*2 )
			return 1;

		return -1;
	}
	/**
	 * Deals damage to the enemy
	 * @param eSTR
	 * @return TRUE if dead
	 */
	public boolean setDamage(int eSTR)
	{
		int damage = eSTR;
		if(damage>STR)
		{
			damage = STR + (int)(Math.random()*(damage-STR));
		}
		HP -= damage;
		if(HP <= 0)
			return true;
		else return false;
	}
	public String getName()
	{
		return name;
	}
	public int getExp()
	{
		return EXP;
	}
	public int getDXT()
	{
		return DXT;
	}
	public int getDamage()
	{
		if( ((double)CHR/(STR+STM+DXT+INT+WSD+CHR)/2) > Math.random())
			return (STR*2);
		else
			return STR;
	}
	public int getSTR()
	{
		return STR;
	}
	public int getHP()
	{
		return HP;
	}
	public int getType()
	{
		return T;
	}
	public Item getItem()
	{
		return null;
	}
}
