package crypt;

import java.awt.*;

public class Item 
{
	int num=500;
	
//	int item[]=new int[num];
	String name[]=new String[num];
	int price[]=new int[num];
	/** 1 str,2 dfn,3 wsd,4 vlr,5 stm,6 agl,7 shoot,8 throw,9 spell,10 hp,11 mp,12 exh,13 hp/mp,14 hp/exh,15 mp/exh,16 hp/mp/exh,17 gold, */
	Point attribute[]=new Point[num];
	String icon[]=new String[num];
	boolean stack[]=new boolean[num];
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
	int equip[]=new int[num];
	int distance[]=new int[num];
	String info[]=new String[num];
	
	public Item()
	{
		/* Null item */
		price[0]=0;
		attribute[0]=new Point(0,0);
		name[0]=" ";
		icon[0]="";
		distance[0]=0;
		info[0]="";
		equip[0]=0;
		
		int i;
		for(int j=0; j<num; j++)
		{
			stack[j]=false;
			distance[j]=0;
		}
		
		/* ////////////////////////////////////////////////////
		//////////////////// W E A P O N S ////////////////////
		/////////////////////////////////////////////////////*/
		
		/* Sword (1-20) */
		i=1;
		for(int j=i; j<i+20; j++)icon[j]=" \u2193";
		
		price[i]=0;
		attribute[i]=new Point(1,1);
		equip[i]=1;
		name[i]="Short Sword";
		info[i]="A basic little sword: Str+1";
		i++;

		/* Bow (21-30) */
		i=21;
		for(int j=i; j<i+10; j++)icon[j]=" )"; //Bow

		price[i]=0;
		attribute[i]=new Point(1,1);
		equip[i]=1;
		name[i]="Wood Bow";
		info[i]="A basic wooden bow: Str+1";
		i++;
		
		/* Arrow (31-40) */
		i=31;
		for(int j=i; j<i+10; j++)icon[i]="\u27B5 "; //Arrow
		
		price[i]=0;
		attribute[i]=new Point(2,7);
		equip[i]=2;
		stack[i]=true;
		distance[i]=7;
		name[i]="Arrow";
		info[i]="A basic wooden arrow: Str=2 Dis=7";
		i++;

		/* Axe (41-60) */
		i=41;
		for(int j=i; j<i+20; j++)icon[j]=" \u21BE";

		price[i]=0;
		attribute[i]=new Point(1,1);
		equip[i]=1;
		name[i]="Hatchet";
		info[i]="A basic short axe: Str+1";
		i++;
		
		/* Claw (61-80) */
		i=61;
		for(int j=i; j<i+20; j++)icon[j]=" #";

		price[i]=0;
		attribute[i]=new Point(1,1);
		equip[i]=1;
		name[i]="Leather Gloves";
		info[i]="A basic pair of gloves: Str+1";
		i++;
		
		/* Staff (81-100) */
		i=81;
		for(int j=i; j<i+20; j++)icon[j]=" /";

		price[i]=0;
		attribute[i]=new Point(1,1);
		equip[i]=1;
		name[i]="Wood Staff";
		info[i]="A basic wooden staff: Str+1";
		i++;

		/* Dagger (101-120) */
		i=101;
		
		for(int j=i; j<i+20; j++)icon[j]="\u2198";

		price[i]=0;
		attribute[i]=new Point(1,8);
		equip[i]=1;
		distance[i]=5;
		name[i]="Knife";
		info[i]="A basic knife: Str+1 Dis=5";
		i++;
		

		/* ////////////////////////////////////////////////////
		////////////////////// A R M O R //////////////////////
		/////////////////////////////////////////////////////*/

		/* Helmet (121-130) */
		i=121;
		for(int j=i; j<i+10; j++)icon[j]="\u264E";

		price[i]=0;
		attribute[i]=new Point(1,2);
		equip[i]=3;
		name[i]="Cap";
		info[i]="A basic hat: Dfn+1";
		i++;

		/* Body (131-140) */
		i=131;
		for(int j=i; j<i+10; j++)icon[j]="\u2624";

		price[i]=0;
		attribute[i]=new Point(0,2);
		equip[i]=4;
		name[i]="Clothes";
		info[i]="Basic clothing: Dfn+0";
		i++;

		/* Shield (141-150) */
		i=141;
		for(int j=i; j<i+10; j++)icon[j]="\u2609";

		price[i]=0;
		attribute[i]=new Point(1,6);
		equip[i]=2;
		name[i]="Wood Shield";
		info[i]="A basic wooden shield: Agl+1";
		i++;

		/* Footwear (151-160) */
		i=151;
		for(int j=i; j<i+10; j++)icon[j]="\u2647";

		price[i]=0;
		attribute[i]=new Point(1,5);
		equip[i]=5;
		name[i]="Leather Boots";
		info[i]="A basic pair of boots: Stm+1";
		i++;


		/* ////////////////////////////////////////////////////
		//////////////////// S P E L L S //////////////////////
		/////////////////////////////////////////////////////*/

		/* Tome (161-260) */
		i=161;
		for(int j=i; j<i+100; j++)icon[j]="\u2709";

		price[i]=0;
		attribute[i]=new Point(1,9);
		equip[i]=0;
		name[i]="W-Strike Tome";
		info[i]="Water Strike Tome";
		i++;
		

		/* ////////////////////////////////////////////////////
		/////////////// C O M E S T I B L E S /////////////////
		/////////////////////////////////////////////////////*/
		
		/* Food (261-320) */
		i=261;
		for(int j=i; j<i+60; j++)icon[j]="\u2740";

		price[i]=0;
		attribute[i]=new Point(2,2);
		equip[i]=0;
		stack[i]=true;
		name[i]="Food Ration";
		i++;
		price[i]=0;
		attribute[i]=new Point(2,2);
		equip[i]=0;
		stack[i]=true;
		name[i]="Food Ration"; //rotten
		i++;		
		price[i]=0;
		attribute[i]=new Point(5,10);
		equip[i]=0;
		stack[i]=true;
		name[i]="Apple";
		i++;
		price[i]=0;
		attribute[i]=new Point(2,11);
		equip[i]=0;
		stack[i]=true;
		name[i]="Egg";
		i++;
		price[i]=0;
		attribute[i]=new Point(3,12);
		equip[i]=0;
		stack[i]=true;
		name[i]="Cheese";
		i++;
		
		i=291;
		price[i]=0;
		attribute[i]=new Point(2,16);
		equip[i]=0;
		stack[i]=true;
		name[i]="Canteen";
		i++;
				

		/* ////////////////////////////////////////////////////
		/////////////// A C C E S S O R I E S /////////////////
		/////////////////////////////////////////////////////*/
		
		/* Ring (321-340) */
		i=321;
		for(int j=i; j<i+20; j++)icon[j]="\u2641";

		price[i]=0;
		attribute[i]=new Point(2,4);
		equip[i]=6;
		name[i]="Ring";
		i++;

		/* Amulet (341-360) */
		i=341;
		for(int j=i; j<i+20; j++)icon[j]="\u2649";

		price[i]=0;
		attribute[i]=new Point(2,3);
		equip[i]=8;
		name[i]="Amulet";
		i++;

		
		/* ////////////////////////////////////////////////////
		//////////////////// O T H E R ////////////////////////
		/////////////////////////////////////////////////////*/

		/* Gold (361-361) */
		i=361;
		for(int j=i; j<i+1; j++)icon[j]="$";

		price[i]=0;
		attribute[i]=new Point(1,17);
		equip[i]=0;
		stack[i]=true;
		name[i]="Gold";
		i++;
		
		
		
		
		
	}
}
