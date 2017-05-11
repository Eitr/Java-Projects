package blarg;

import java.awt.*;

public class Character 
{
	/** Charater location on grid */
	private Point P;

	/** Physical health */
	private int HP;
	/** Mental health */
	private int MP;
	/** Maximum hp */
	private int MAXHP;
	/** Maximum mp */
	private int MAXMP;
	/** Fatigue */
	private double FAT;
	/** Capacity */
	private int CAP;
	/** Experience */
	private int EXP;
	/** Gold */
	private int GP;
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
	/** Attribute points */
	private int AP;
	/** Skill points */
	private int SP;


	/** Line of Site????? */
	int LOS;

	/** Beginning AP */
	private int bAP = 60;




	Item eHead;
	Item eBody;
	Item eArms;
	Item eHands;
	Item eLegs;
	Item eFeet;
	Item eWeapon;







	/**	
	 * Condition of capacity
	 * 0	normal
	 * 1	burdened
	 * 2	stressed
	 * 3	immobile?
	 */
	int vCAP=0;
	/**
	 * Condition of hunger
	 * 0	normal
	 * 1	?
	 */
	int vFAT=0;


	public Character(int x, int y)
	{
		for(int i=0; i<7; i++)
			equip(new Item(0),i);

		P = new Point(x,y);

		FAT 	=		0;
		CAP 	=		0;
		EXP 	=		100;
		GP 		=		0;

		STR 	=		1;
		STM 	=		1;
		DXT 	=		1;
		INT 	=		1;
		WSD 	=		1;
		CHR 	=		1;

		AP 		=		bAP-(STR+STM+DXT+INT+WSD+CHR);
		SP 		=		0;

		MAXHP	=		STM*3;
		MAXMP	=		WSD*2;

		HP 		=		MAXHP;
		MP 		=		MAXMP;
	}
	public boolean equip(int e, Item i)
	{
		if(e == 0)
		{
			if(eWeapon!=null)
				eWeapon = i;
			else return false;
		}
		else if(e == 1)
		{
			if(eHead!=null)
				eHead = i;
			else return false;
		}
		else if(e == 2)
		{
			if(eBody!=null)
				eBody = i;
			else return false;
		}

		return true;
	}
	public void equip(Item item, int b)
	{
		//weapon
		if(b == 0)
		{
			eWeapon = item;
		}
		//Head
		else if(b == 1)
		{
			eHead = item;
		}
		//Body
		else if(b == 2)
		{
			eBody = item;
		}
		//Arms
		else if(b == 3)
		{
			eArms = item;	
		}
		//Hands
		else if(b == 4)
		{
			eHands = item;
		}
		//Legs
		else if(b == 5)
		{
			eLegs = item;
		}
		//Feet
		else if(b == 6)
		{
			eFeet = item;
		}
	}
	private int STR()
	{
		return (eWeapon.STR() + eHead.STR() + eBody.STR() + eArms.STR() + eLegs.STR() + eLegs.STR() + eFeet.STR());
	}
	private int STM()
	{
		return (eWeapon.STM() + eHead.STM() + eBody.STM() + eArms.STM() + eLegs.STM() + eLegs.STM() + eFeet.STM());
	}
	private int DXT()
	{
		return (eWeapon.DXT() + eHead.DXT() + eBody.DXT() + eArms.DXT() + eLegs.DXT() + eLegs.DXT() + eFeet.DXT());
	}
	private int INT()
	{
		return (eWeapon.INT() + eHead.INT() + eBody.INT() + eArms.INT() + eLegs.INT() + eLegs.INT() + eFeet.INT());
	}
	private int WSD()
	{
		return (eWeapon.WSD() + eHead.WSD() + eBody.WSD() + eArms.WSD() + eLegs.WSD() + eLegs.WSD() + eFeet.WSD());
	}
	private int CHR()
	{
		return (eWeapon.CHR() + eHead.CHR() + eBody.CHR() + eArms.CHR() + eLegs.CHR() + eLegs.CHR() + eFeet.CHR());
	}

	/**
	 * Shifts character location
	 * @param x horizontal shift
	 * @param y vertical shift
	 */
	public void move(int x, int y)
	{
		P.x+=x;
		P.y+=y;
	}
	/**
	 * Calls character position on grid
	 * @return Character Location
	 */
	public Point getL()
	{
		return P;
	}
	public void setL(Point p)
	{
		P = new Point(p);
	}
	/**
	 * Increases experience and calculates leveling
	 * @param exp EXP to be added
	 * @return TRUE if level up
	 */
	public boolean setLevel(int exp)
	{
		if(EXP%100 + exp/(EXP/100) >= 100)
		{
			AP += 5;
			SP += 1;
			EXP += exp/(EXP/100);
			return true;
		}

		EXP += exp/(EXP/100);

		return false;
	}


	/**
	 * Returns how many times character attacks the enemy
	 * @param eDXT
	 * @return How many times Character attacks
	 */
	public int getMissed(int eDXT)
	{
		int dxt = DXT + DXT();

		for(int i=4; i>1; i--)
			if(dxt-((double)vCAP/10*dxt) >= i*eDXT)
			{
				return i;
			}
		if( 1-(eDXT - dxt)/(double)eDXT > Math.random()*2 )
			return 1;

		return -1;
	}
	/**
	 * Deals damage to the character
	 * @param eSTR
	 */
	public void setDamage(int eSTR)
	{
		int str = STR + STR();

		if(eSTR>str)
		{
			eSTR = str + (int)(Math.random()*(eSTR-str));
		}
		HP -= eSTR;
	}
	public int getDXT()
	{
		return DXT;
	}
	public int getDamage()
	{
		int add = 0;
		int str = STR + STR();


		if(eWeapon!=null)
			add = eWeapon.getAttribute(0);

		if( ((double)CHR/(STR+STM+DXT+INT+WSD+CHR)/2 ) > Math.random() && vCAP==0)
			return ((str + add)*2);
		else
			return (str + add);
	}
	public int getSTR()
	{
		return STR;
	}
	public int getHP()
	{
		return HP;
	}
	public void setFatigue(double fat)
	{
		FAT += fat*(1-(double)STM/(STR+STM+DXT+INT+WSD+CHR));

		if(FAT > 100)
			FAT = 100;

//		return FAT;
	}
	public void setCapacity(int cap)
	{
		CAP += cap;
	}
	public int getCapacity(Item[] N, int numN)
	{
		int weight=0;
		for(int i=0; i<numN; i++)
		{
			weight += N[i].getWeight()*N[i].getQuantity();
		}
		CAP = weight;

		int str = STR +STR();


		if(CAP > str*10)
			vCAP = 3;
		else if(CAP > str*7)
			vCAP = 2;
		else if(CAP > str*4)
			vCAP = 1;
		else
			vCAP = 0;

		return vCAP;
	}
	public int getCHR()
	{
		return CHR;
	}


	public void drawAttribute(Graphics g, int s)
	{		
		int ox=15;
		int oy=15;

		g.drawString("Attribute Points: "+AP, (ox)*s, (oy-5)*s);
		g.drawString("HP: "+MAXHP, (ox)*s, (oy-3)*s);
		g.drawString("MP: "+MAXMP, (ox+15)*s, (oy-3)*s);

		g.drawString("Strength: ", 	(ox)*s,  oy*s);
		g.drawString(STR+"",  		(ox+5)*s,  oy*s);
		g.drawString("Intellect: ", (ox+15)*s,  oy*s);
		g.drawPolygon(new int[]{(ox-3)*s,(ox-2)*s,(int)((ox-2.5)*s)}, new int[]{oy*s,oy*s,(oy-1)*s}, 3);
//		g.drawPolygon(new int[]{(ox-3)*s,(ox-2)*s,(int)((ox-2.5)*s)}, new int[]{oy*s,oy*s,(oy+1)*s}, 3);
		g.drawPolygon(new int[]{(ox+15-3)*s,(ox+15-2)*s,(int)((ox+15-2.5)*s)}, new int[]{oy*s,oy*s,(oy-1)*s}, 3);
//		g.drawPolygon(new int[]{(ox+15-3)*s,(ox+15-2)*s,(int)((ox+15-2.5)*s)}, new int[]{oy*s,oy*s,(oy+1)*s}, 3);
		g.drawString(INT+"",  		(ox+20)*s, oy*s);	oy+=5;
		g.drawString("Stamina: ", 	(ox)*s,  oy*s);
		g.drawString(STM+"",  		(ox+5)*s,  oy*s);
		g.drawString("Wisdom: ", 	(ox+15)*s,  oy*s);
		g.drawPolygon(new int[]{(ox-3)*s,(ox-2)*s,(int)((ox-2.5)*s)}, new int[]{oy*s,oy*s,(oy-1)*s}, 3);
//		g.drawPolygon(new int[]{(ox-3)*s,(ox-2)*s,(int)((ox-2.5)*s)}, new int[]{oy*s,oy*s,(oy+1)*s}, 3);
		g.drawPolygon(new int[]{(ox+15-3)*s,(ox+15-2)*s,(int)((ox+15-2.5)*s)}, new int[]{oy*s,oy*s,(oy-1)*s}, 3);
//		g.drawPolygon(new int[]{(ox+15-3)*s,(ox+15-2)*s,(int)((ox+15-2.5)*s)}, new int[]{oy*s,oy*s,(oy+1)*s}, 3);
		g.drawString(WSD+"",  		(ox+20)*s, oy*s);	oy+=5;
		g.drawString("Dexterity: ", (ox)*s,  oy*s);
		g.drawString(DXT+"",  		(ox+5)*s,  oy*s);
		g.drawString("Charisma: ", 	(ox+15)*s,  oy*s);
		g.drawPolygon(new int[]{(ox-3)*s,(ox-2)*s,(int)((ox-2.5)*s)}, new int[]{oy*s,oy*s,(oy-1)*s}, 3);
//		g.drawPolygon(new int[]{(ox-3)*s,(ox-2)*s,(int)((ox-2.5)*s)}, new int[]{oy*s,oy*s,(oy+1)*s}, 3);
		g.drawPolygon(new int[]{(ox+15-3)*s,(ox+15-2)*s,(int)((ox+15-2.5)*s)}, new int[]{oy*s,oy*s,(oy-1)*s}, 3);
//		g.drawPolygon(new int[]{(ox+15-3)*s,(ox+15-2)*s,(int)((ox+15-2.5)*s)}, new int[]{oy*s,oy*s,(oy+1)*s}, 3);
		g.drawString(CHR+"",  		(ox+20)*s, oy*s);	oy+=5;

	}
	public void drawCreation(Graphics g, int s)
	{		
		int ox=15;
		int oy=15;

		g.drawString("Attribute Points: "+AP, (ox)*s, (oy-5)*s);
		g.drawString("HP: "+MAXHP, (ox)*s, (oy-3)*s);
		g.drawString("MP: "+MAXMP, (ox+15)*s, (oy-3)*s);

		g.drawString("Strength: ", 	(ox)*s,  oy*s);
		g.drawString(STR+"",  		(ox+5)*s,  oy*s);
		g.drawString("Intellect: ", (ox+15)*s,  oy*s);
		g.drawPolygon(new int[]{(ox-3)*s,(ox-2)*s,(int)((ox-2.5)*s)}, new int[]{oy*s,oy*s,(oy-1)*s}, 3);
		g.drawPolygon(new int[]{(ox-3)*s,(ox-2)*s,(int)((ox-2.5)*s)}, new int[]{oy*s,oy*s,(oy+1)*s}, 3);
		g.drawPolygon(new int[]{(ox+15-3)*s,(ox+15-2)*s,(int)((ox+15-2.5)*s)}, new int[]{oy*s,oy*s,(oy-1)*s}, 3);
		g.drawPolygon(new int[]{(ox+15-3)*s,(ox+15-2)*s,(int)((ox+15-2.5)*s)}, new int[]{oy*s,oy*s,(oy+1)*s}, 3);
		g.drawString(INT+"",  		(ox+20)*s, oy*s);	oy+=5;
		g.drawString("Stamina: ", 	(ox)*s,  oy*s);
		g.drawString(STM+"",  		(ox+5)*s,  oy*s);
		g.drawString("Wisdom: ", 	(ox+15)*s,  oy*s);
		g.drawPolygon(new int[]{(ox-3)*s,(ox-2)*s,(int)((ox-2.5)*s)}, new int[]{oy*s,oy*s,(oy-1)*s}, 3);
		g.drawPolygon(new int[]{(ox-3)*s,(ox-2)*s,(int)((ox-2.5)*s)}, new int[]{oy*s,oy*s,(oy+1)*s}, 3);
		g.drawPolygon(new int[]{(ox+15-3)*s,(ox+15-2)*s,(int)((ox+15-2.5)*s)}, new int[]{oy*s,oy*s,(oy-1)*s}, 3);
		g.drawPolygon(new int[]{(ox+15-3)*s,(ox+15-2)*s,(int)((ox+15-2.5)*s)}, new int[]{oy*s,oy*s,(oy+1)*s}, 3);
		g.drawString(WSD+"",  		(ox+20)*s, oy*s);	oy+=5;
		g.drawString("Dexterity: ", (ox)*s,  oy*s);
		g.drawString(DXT+"",  		(ox+5)*s,  oy*s);
		g.drawString("Charisma: ", 	(ox+15)*s,  oy*s);
		g.drawPolygon(new int[]{(ox-3)*s,(ox-2)*s,(int)((ox-2.5)*s)}, new int[]{oy*s,oy*s,(oy-1)*s}, 3);
		g.drawPolygon(new int[]{(ox-3)*s,(ox-2)*s,(int)((ox-2.5)*s)}, new int[]{oy*s,oy*s,(oy+1)*s}, 3);
		g.drawPolygon(new int[]{(ox+15-3)*s,(ox+15-2)*s,(int)((ox+15-2.5)*s)}, new int[]{oy*s,oy*s,(oy-1)*s}, 3);
		g.drawPolygon(new int[]{(ox+15-3)*s,(ox+15-2)*s,(int)((ox+15-2.5)*s)}, new int[]{oy*s,oy*s,(oy+1)*s}, 3);
		g.drawString(CHR+"",  		(ox+20)*s, oy*s);	oy+=5;

	}

	/**
	 * Draws the Character onto the canvas directly
	 * @param g Graphics
	 * @param s Grid size
	 */
	public void draw(Graphics g, int s, int column, int row)
	{
		int x = P.x*s;
		int y = P.y*s;

		g.setColor(Color.yellow);
		g.setFont(new Font("", Font.PLAIN,(int)(s)));
		g.drawString("X",x+4,y+s-2);


		/*/ GUI /*/
		g.setFont(new Font("", Font.PLAIN,s*3/4));

		int c = column*s;
		int l=2;

		g.drawString("George the Devourer", c+2*s, l*s);	l++;	l++;

		g.drawString("Lvl: "+(EXP/100),     c+2*s, l*s);
		g.drawString("Exp: ", c+7*s, l*s);
		g.drawString((EXP%100)+"%",  c+10*s,  l*s);	l++;	l++;
		g.drawString("$ "+GP, c+2*s, l*s);
		g.drawString("Exh: ", c+7*s, l*s);
		g.drawString((int)(FAT*100)+"%",  c+10*s,  l*s);
		l++;	l++;
		g.drawString("HP: "+HP+" / "+MAXHP, c+2*s, l*s);

		g.setColor(Color.red);
		g.fillRect(c+2*s, l*s+s/4, 10*s, s/2);
		g.setColor(Color.green);
		g.fillRect(c+2*s, l*s+s/4, (int)((double) HP / MAXHP*10*s), s/2);
		g.setColor(Color.yellow);
		l++;
		l++;
		g.drawString("MP: "+MP+" / "+MAXMP, c+2*s, l*s);
		g.setColor(Color.red);
		g.fillRect(c+2*s, l*s+s/4, 10*s, s/2);
		g.setColor(Color.green);
		g.fillRect(c+2*s, l*s+s/4, (int)((double) MP / MAXMP*10*s), s/2);
		g.setColor(Color.yellow);
		l++;
		l++;	l++;

		g.setFont(new Font("", Font.PLAIN,s/2));
		g.drawString("STR: ", 			c+2*s,  l*s);
		g.setFont(new Font("", Font.PLAIN,s*3/4));

		if(STR()>0)	g.drawString(STR+" +"+STR(),	c+4*s,  l*s);
		else 		g.drawString(STR+"",			c+4*s,  l*s);
					g.setFont(new Font("", Font.PLAIN,s/2));
					g.drawString("INT: ", 			c+7*s,  l*s);
					g.setFont(new Font("", Font.PLAIN,s*3/4));
		if(INT()>0)	g.drawString(INT+" +"+INT(),	c+9*s, l*s);
		else		g.drawString(INT+"",				c+9*s, l*s);	l++;
					g.setFont(new Font("", Font.PLAIN,s/2));
					g.drawString("STM: ", 			c+2*s,  l*s);
					g.setFont(new Font("", Font.PLAIN,s*3/4));
		if(STM()>0)	g.drawString(STM+" +"+STM(),	c+4*s,  l*s);
		else		g.drawString(STM+"",			c+4*s,  l*s);
					g.setFont(new Font("", Font.PLAIN,s/2));
					g.drawString("WSD: ", 			c+7*s,  l*s);
					g.setFont(new Font("", Font.PLAIN,s*3/4));
		if(WSD()>0)	g.drawString(WSD+" +"+WSD(),	c+9*s, l*s);
		else		g.drawString(WSD+"",			c+9*s, l*s);	l++;
					g.setFont(new Font("", Font.PLAIN,s/2));
					g.drawString("DXT: ", 			c+2*s,  l*s);
					g.setFont(new Font("", Font.PLAIN,s*3/4));
		if(DXT()>0)	g.drawString(DXT+" +"+DXT(),	c+4*s,  l*s);
		else		g.drawString(DXT+"",			c+4*s,  l*s);
					g.setFont(new Font("", Font.PLAIN,s/2));
					g.drawString("CHR: ", 			c+7*s,  l*s);
					g.setFont(new Font("", Font.PLAIN,s*3/4));
		if(CHR()>0)	g.drawString(CHR+" +"+CHR(),	c+9*s, l*s);
		else		g.drawString(CHR+"",		c+9*s, l*s);	
		
		l++;		
		l++;
		g.drawString("AP: "+AP, c+2*s, l*s);
		g.drawString("SP: "+SP, c+7*s, l*s);	l++;

		if(vCAP==1)
			g.drawString("Burdened", c+2*s, l*s);
		else if(vCAP==2)
			g.drawString("Stressed", c+2*s, l*s);
		else if(vCAP==3)
			g.drawString("Strained", c+2*s, l*s);

//		g.drawString("Cap:"+CAP+" | "+vCAP, c+2*s, l*s);


//		g.drawString("STR: "+STR, c+2*s, 4*s);
//		g.drawString("STM: "+STM, c+2*s, 5*s);
//		g.drawString("DXT: "+DXT, c+2*s, 6*s);
//		g.drawString("INT: "+INT, c+6*s, 4*s);
//		g.drawString("WSD: "+WSD, c+6*s, 5*s);
//		g.drawString("CHR: "+CHR, c+6*s, 6*s);

//		FAT 	=		0;
//		CAP 	=		0;
//		AP 		=		0;
//		SP 		=		0;

	}

	public void setAttribute(int a, int sign)
	{
		if(sign == 1)
		{
			if(AP>0)
			{
				if(a==0)
					STR += 1;
				else if(a==1)
				{
					STM += 1;
					MAXHP =	STM*3;
					HP += 3;
				}
				else if(a==2)
					DXT += 1;
				else if(a==3)
					INT += 1;
				else if(a==4)
				{
					WSD += 1;
					MAXMP =	WSD*2;
					MP+=2;
				}
				else if(a==5)
					CHR += 1;

				AP -= 1;
			}
		}
		else if(sign == -1)
		{
			if(a==0 && STR>1)
			{
				STR -= 1;
				AP += 1;
			}
			else if(a==1 && STM>1)
			{
				STM -= 1;
				MAXHP =	STM*3;
				HP -= 3;
				AP += 1;
			}
			else if(a==2 && DXT>1)
			{
				DXT -= 1;
				AP += 1;
			}
			else if(a==3 && INT>1)
			{
				INT -= 1;
				AP += 1;
			}
			else if(a==4 && WSD>1)
			{
				WSD -= 1;
				MAXMP =	WSD*2;
				MP -= 2;
				AP += 1;
			}
			else if(a==5 && CHR>1)
			{
				CHR -= 1;
				AP += 1;
			}
		}
		else if(sign == 0)
		{
			int cSTM = STM;
			int cWSD = WSD;

//			STR	STM	DXT	INT	WSD	CHR
//			Knight		2	1	3	4	5	6
//			Ranger		6	5	2	3	4	1
//			Monk		3	6	4	5	1	2
//			Wizard		5	4	6	1	2	3
//			Ninja		4	3	1	2	6	5
//			Rogue		1	2	5	6	3	4

			int cAP = AP+STR+STM+DXT+INT+WSD+CHR;

			//Knight
			if(a==0)
			{
				STR 	=		(int)(cAP*.25);
				STM 	=		(int)(cAP*.35);
				DXT 	=		(int)(cAP*.15);
				INT 	=		(int)(cAP*.10);
				WSD 	=		(int)(cAP*.10);
				CHR 	=		(int)(cAP*.05);
			}
			//Ranger
			else if(a==1)
			{
				STR 	=		(int)(cAP*.05);
				STM 	=		(int)(cAP*.15);
				DXT 	=		(int)(cAP*.25);
				INT 	=		(int)(cAP*.10);
				WSD 	=		(int)(cAP*.15);
				CHR 	=		(int)(cAP*.30);
			}
			//Monk
			else if(a==2)
			{
				STR 	=		(int)(cAP*.15);
				STM 	=		(int)(cAP*.10);
				DXT 	=		(int)(cAP*.15);
				INT 	=		(int)(cAP*.05);
				WSD 	=		(int)(cAP*.30);
				CHR 	=		(int)(cAP*.25);
			}
			//Wizzard
			else if(a==3)
			{
				STR 	=		(int)(cAP*.05);
				STM 	=		(int)(cAP*.15);
				DXT 	=		(int)(cAP*.05);
				INT 	=		(int)(cAP*.35);
				WSD 	=		(int)(cAP*.20);
				CHR 	=		(int)(cAP*.20);
			}
			//Ninja
			else if(a==4)
			{
				STR 	=		(int)(cAP*.10);
				STM 	=		(int)(cAP*.20);
				DXT 	=		(int)(cAP*.35);
				INT 	=		(int)(cAP*.20);
				WSD 	=		(int)(cAP*.05);
				CHR 	=		(int)(cAP*.10);
			}
			//Rogue
			else if(a==5)
			{
				STR 	=		(int)(cAP*.35);
				STM 	=		(int)(cAP*.30);
				DXT 	=		(int)(cAP*.10);
				INT 	=		(int)(cAP*.05);
				WSD 	=		(int)(cAP*.05);
				CHR 	=		(int)(cAP*.15);
			}

			if(a==18)
			{
				STR 	=		(int)(cAP*.16667);
				STM 	=		(int)(cAP*.16667);
				DXT 	=		(int)(cAP*.16667);
				INT 	=		(int)(cAP*.16667);
				WSD 	=		(int)(cAP*.16667);
				CHR 	=		(int)(cAP*.16667);	
			}

			AP = cAP-(STR+STM+DXT+INT+WSD+CHR);


			MAXHP 	=		STM*3;
			MAXMP 	=		WSD*2;

			HP += (STM - cSTM)*3;
			MP += (WSD - cWSD)*2;
		}
	}
}
