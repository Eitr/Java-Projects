package crypt;

import java.awt.*;

import javax.swing.JOptionPane;

public class NPC 
{
	Point P;
	String name;
	Point attribute;
	int I;
	String s[];
	
	public NPC(int x, int y, String name, /*String s1, String s2, String s3, String s4, String s5, String s6,*/ int i)
	{
		this.P= new Point(x,y);
		this.name=name;
//		s[0]=s1;
//		s[1]=s2;
//		s[2]=s3;
//		s[3]=s4;
//		s[4]=s5;
//		s[5]=s6;
		this.I=i;
		s=new String[i];
		for(int j=0; j<I; j++)
		{
			s[j]=JOptionPane.showInputDialog(j+"?");
		}
	}
	public void Draw(Graphics g, int s, int x, int y, int sx, int sy)
	{
		g.setColor(Color.yellow);
		g.setFont(new Font("", Font.PLAIN,(int)(s)));
		g.drawString("S",P.x*s+x+sx+3,P.y*s+y+sy+s-1);
		g.setFont(new Font("", Font.PLAIN,15));
	}
	public String[] Print()
	{
		return s;
	}
	public String getName()
	{
		return name;
	}
	public Point getL()
	{
		return P;
	}	
}