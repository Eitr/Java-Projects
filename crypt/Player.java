package crypt;

import java.awt.*;

public class Player 
{
	String name; 
	/** Position */
	Point P;

	int str;
	int dfn;
	int wsd;
	int vlr;
	int stm;
	int agl;
	int lvl;
	int exp;
	int maxexp;
	int hp;
	int mp;
	int maxhp;
	int maxmp;
	double exh;
	int gp;
//	int type;

	boolean death;

	int N=1;
	Point item[]=new Point[29];
	Item I= new Item();

	int S=1;
	int spell[]=new int[10];
	Magic M= new Magic();


	/** 0=non
	 * 1=rhand
	 * 2=lhand
	 * 3=head
	 * 4=body
	 * 5=feet
	 * 6=rfinger
	 * 7=lfinger
	 * 8=neck
	 */
	int equip[]=new int[9];

	public Player(int x, int y, String name, int t)
	{
		this.name=name;
		for(int i=0; i<29; i++)
		{
			item[i]= new Point(0,0);
		}

		for(int i=0; i<9; i++)
			equip[i]=0;

		P= new Point(x,y);

		if(t==1)
		{
			//Knight
			setStatistics(11,13,5,9,8,4,40,0);
			setItem(1,1);
		}
		else if(t==2) 
		{
			//Archer
			setStatistics(7,9,9,13,5,7,25,9);
			setItem(21,1);
			setItem(31,10);
		}
		else if(t==3)
		{
			//Rogue
			setStatistics(13,11,3,7,11,5,20,0);
			setItem(41,1);
		}
		else if(t==4)
		{
			//Monk
			setStatistics(9,5,10,6,13,8,35,13);
			setItem(61,1);
		}
		else if(t==5)
		{
			//Wizzard
			setStatistics(5,7,13,8,6,11,45,22);
			setItem(81,1);
			setItem(161,1);
		}
		else if(t==6)
		{
			//Thief
			setStatistics(8,4,7,11,7,13,30,5);
			setItem(101,1);
		}
		else if(t==0)
			//Default
		{
			setStatistics(9,9,8,8,8,8,40,20);
			for(int i=1; i<=101; i+=20)
				setItem(i,1);
			for(int i=121; i<=151; i+=10)
				setItem(i,1);
			setItem(321,1);
			setItem(341,1);
			setItem(161,1);

			setItem(31,10);

			setItem(263,10);
			setItem(264,10);
			setItem(265,10);

			setItem(291,10);

			setSpell(1);
		}
	}
	private void setStatistics(int str, int dfn, int wsd, int vlr, int stm, int agl, int maxhp, int maxmp)
	{
		this.str=str;
		this.dfn=dfn;
		this.wsd=wsd;
		this.vlr=vlr;
		this.stm=stm;
		this.agl=agl;
		this.maxhp=maxhp;
		this.maxmp=maxmp;
		hp=maxhp;
		mp=maxmp;
		lvl=1;
		maxexp= ((lvl+1)*(lvl+1))-lvl;
		exp=0;
		exh=0;
		gp=0;
	}
	public void Draw(Graphics g, int s, int x, int y, int sx, int sy)
	{
		g.setColor(Color.yellow);
		g.setFont(new Font("", Font.PLAIN,(int)(s)));
		g.drawString("X",P.x*s+x+sx+3,P.y*s+y+sy+s-1);
		g.setFont(new Font("", Font.PLAIN,15));
	}

	public Point getL()
	{
		return P;
	}
	/**
	 * Moves character with regards to current position
	 * @param x-added horizontal movement
	 * @param y-added vertical movement
	 */
	public void Move(int x, int y)
	{
		P.x+=x;
		P.y+=y;
	}
	/**
	 * Determines if character misses
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
	/**
	 * Determines damage dealt
	 * @param d
	 * @return
	 */
	public int getDamage(int d)
	{
		int D=0;
		for(int i=1; i<equip.length; i++)
			if(I.attribute[item[equip[i]].x].y==1 ||I.attribute[item[equip[i]].x].y==11)
				D+=I.attribute[item[equip[1]].x].x;

		return (D+str-d/4);
	}
	public int getMagicDamage(int s)
	{
		int D=0;
//		for(int i=1; i<equip.length; i++)
//		if(I.attribute[item[equip[i]]].y==1)
//		D+=I.attribute[item[equip[1]]].x;

		return (D+(M.attribute[spell[s]].x));
	}
	/**
	 * Determines damage recieved
	 * @param d
	 */
	public void setDamage(int d)
	{
		int D=0;
		for(int i=1; i<equip.length; i++)
			if(I.attribute[item[equip[i]].x].y==2)
				D+=I.attribute[item[equip[i]].x].x;
		hp-=(d-((dfn+D)/4));
	}
	public boolean getDeath()
	{
		return death;
	}
	public void setExhaustion(double d)
	{
		exh= (exh+(d/stm));
	}
	public void setExperience(int e)
	{
		exp+=e;
		if(exp>=maxexp)
		{
			int dif=exp-maxexp;
			lvl++;
			exp=dif;
			maxexp= ((lvl+1)*(lvl+1))-lvl;
		}
	}
	public boolean setItem(int i, int num)
	{
		if(i==0)
			return false;
		boolean store=false;

		for(int j=1; j<29; j++)
		{
			if(I.stack[i])
			{
				if(i==item[j].x)
				{
					item[j]= new Point(i,num+item[j].y);
					store=true;
					break;
				}
			}
		}
		if(!store)
		{
			if(N>28)
				return true;
			if(I.stack[i])
			{
				item[N]=new Point(i,num);
				N++;
			}
			else
				for(int j=0; j<num; j++)
				{
					item[N]=new Point(i,1);
					N++;
				}
		}

		return false;

//		item[N].x=i;
//		if(I.stack[i] || num==1)
//		item[N].y=num;
//		else
//		for(int j=1; j<num; j++)
//		{
//		N++;
//		item[N]=new Point(i,1);
//		}
//		N++;
//		return false;
	}
	public String getItem(int i)
	{
		if(item[i].x>0)
			return I.name[item[i].x];
		else
			return "";
	}
	public void removeItem(int i, int num)
	{

		item[i]=new Point(item[i].x, item[i].y-num);
		if(item[i].y==0)
		{
			for(int k=1; k<equip.length; k++)
				if(i==equip[k])
					equip[k]=0;
			N-=1;
			for(int j=i; j<=N; j++)
			{
				for(int k=0; k<equip.length; k++)
					if(equip[k]==j)
					{
						equip[k]=j-1;
						System.out.println("move");
					}
			}
			for(int j=i; j<N; j++)
			{
				item[j]= item[j+1];
			}	

			item[N]= new Point(0,0);
		}


//		if(!I.stack[item[i].x])
//		{
//		N-=1;
//		for(int j=i; j<N; j++)
//		{
//		item[j]= item[j+1];
//		}
//		item[N]= new Point(0,0);
//		}
//		else
//		{
//		item[i]=new Point(item[i].x, num-1);
//		}
	}
	public void setSpell(int s)
	{
		boolean learn=true;
		for(int i=0; i<S; i++)
			if(spell[i]==s)
				learn=false;
		if(learn)
		{
			spell[S]=s;
			S++;
		}
	}
	public void Update()
	{
		if(hp<=0)
			death=true;
		if(hp>maxhp)
			hp=maxhp;
		if(mp>maxmp)
			mp=maxmp;
		if(exh<0)
			exh=0;
	}

}
