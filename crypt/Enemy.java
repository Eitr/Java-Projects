package crypt;

import java.awt.*;

public class Enemy 
{
	private Point P;

	String name;

	int str;
	int dfn;
	int wsd;
	int vlr;
	int agl;
	int exp;
	int hp;
	int mp;
	int maxhp;
	int maxmp;
	int gp;
	int type;

	Point item=new Point(0,0);

	boolean death;

	public Enemy(int x, int y, int str, int dfn, int wsd, int vlr, int agl, int maxhp, int maxmp, String name,int gp,int exp)
	{
		P= new Point(x,y);
		this.str=str;
		this.dfn=dfn;
		this.wsd=wsd;
		this.vlr=vlr;
//		this.stm=stm;
		this.agl=agl;
		this.maxhp=maxhp;
		this.maxmp=maxmp;
		this.name=name;
		this.gp=gp;
		this.exp=exp;
		hp=maxhp;
		mp=maxmp;
	}
	public Enemy(int x, int y, int t)
	{
		P= new Point(x,y);
		this.type=t;
		if(type==1)
		{
			setStatistics(5,0,0,7,3,8,0,"Blob",1,1);
//			item= new Point(1,1);
		}
		if(type==2)
		{
			setStatistics(5,0,0,7,3,8,0,"Goblin",1,1);
			item= new Point(361,10);
		}
	}
	private void setStatistics(int str, int dfn, int wsd, int vlr, int agl, int maxhp, int maxmp,String name, int gp, int exp)
	{
		this.str=str;
		this.dfn=dfn;
		this.wsd=wsd;
		this.vlr=vlr;
		this.agl=agl;
		this.maxhp=maxhp;
		this.maxmp=maxmp;
		this.name=name;
		this.gp=gp;
		this.exp=exp;
		hp=maxhp;
		mp=maxmp;
	}
	public void Draw(Graphics g, int s, int x, int y, int sx, int sy)
	{
		if(!death)
		{
			g.setColor(Color.WHITE);
			g.setFont(new Font("", Font.PLAIN,(int)(s)));
			g.drawString("O",P.x*s+x+sx+2,P.y*s+y+sy+s-1);
			g.setFont(new Font("", Font.PLAIN,15));
		}
	}

	public Point getL()
	{
		return P;
	}
	public void Move(int x, int y)
	{
		P.x+=x;
		P.y+=y;
	}

	/**
	 * Determines if enemy misses
	 * @param a
	 * @return
	 */
	public boolean getMiss(int a)
	{
		if(Math.random()>(((double)a/(vlr+a))))
			return false;
		else
			return true;
	}
	public int getDamage(int d)
	{
		return (str-d/4);
	}
	public void setDamage(int d)
	{
		hp-=d;
		if(hp<=0)
			death=true;
	}
	public String getName()
	{
		return name;
	}
	public boolean getDeath()
	{
		return death;
	}
}
