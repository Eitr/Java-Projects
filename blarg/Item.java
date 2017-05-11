package blarg;

import java.awt.*;

@SuppressWarnings("unused")
public class Item 
{
	
	private int T;
	private int cost;
	private int weight;
	private String name;
	private String info;
	private String icon;
	private Point P;
	private boolean stackable;
	private int quantity;
	
	int eT;
	int STR;
	int STM;
	int DXT;
	int INT;
	int WSD;
	int CHR;
	
	public Item(int t)
	{
		T = t;
		
		
		
		// Editing item
		if(T == 0)
		{
			cost		=	10;
			weight		=	2;
			name		=	"Crap";
			info		=	"Stuff you don't need.";
			icon		=	"*";
			stackable	=	false;
			quantity	=	1;
			cost		=	0;

			eT			=	0;
			STR 		=	0;
			STM 		=	0;
			DXT 		=	0;
			INT 		=	0;
			WSD 		=	0;
			CHR 		=	0;
		}
		
		if(T>0 && T<=20)
		{
			if(T==1)
			{;
			weight		=	2;
			name		=	"Wooden Sword";
			info		=	"Training weapon for swordsmen.";
			icon		=	"|";
			stackable	=	false;
			quantity	=	1;
			cost		=	0;

			eT			=	0;
			STR 		=	2;
			STM 		=	0;
			DXT 		=	0;
			INT 		=	0;
			WSD 		=	0;
			CHR 		=	0;
			}
		}
		
		
		
		
		
		
	}
	public int getEquipType()
	{
		return eT;
	}
	public int getAttribute(int a)
	{
		if(a==0)
			return STR;
		
		return 0;
	}
	public Point getL()
	{
		return P;
	}
	public String getIcon()
	{
		return icon;
	}
	public int getType()
	{
		return T;
	}
	public String getName()
	{
		return name;
	}
	public int getWeight()
	{
		return weight;
	}
	public void addQuantity(int i)
	{
		quantity += i;
	}
	public int getQuantity()
	{
		return quantity;
	}
	public boolean getStackable()
	{
		return stackable;
	}
	public void setL(Point p)
	{
		P = new Point(p);
	}
	public String getInfo()
	{
		return info;
	}
	public int STR()
	{
		return STR;
	}
	public int STM()
	{
		return STM;
	}
	public int DXT()
	{
		return DXT;
	}
	public int INT()
	{
		return INT;
	}
	public int WSD()
	{
		return WSD;
	}
	public int CHR()
	{
		return CHR;
	}
}
