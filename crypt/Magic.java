package crypt;

import java.awt.Point;

public class Magic 
{

	int num=500;
	
	String name[]=new String[num];
	int price[]=new int[num];
	/** 1=attack, */
	Point attribute[]=new Point[num];
	int distance[]=new int[num];
	int cost[]=new int[num];
	
//	String icon[]=new String[num];
//	boolean stack[]=new boolean[num];
//	int equip[]=new int[num];
	
	public Magic()
	{
		name[0]="";
		price[0]=0;
		attribute[0]=new Point(0,0);
		

		int i;
		
		
		/* ////////////////////////////////////////////////////
		//////////////////// O F F E N S E ////////////////////
		/////////////////////////////////////////////////////*/
		
		/* Water (1-10) */
		i=1;

		price[i]=0;
		attribute[i]=new Point(10,1);
		distance[i]=5;
		cost[i]=2;
		name[i]="Water Strike";
		
		
		
	}
}
