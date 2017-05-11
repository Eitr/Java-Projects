package blarg;

public class Edit 
{
	public static void main(String[] arg)
	{
		int DXT = 19;
		int eDXT = 10;

		boolean end=false;

		for(int i=4; i>1; i--)
			if(DXT >= i*eDXT)
			{
				System.out.println(""+i);
				end=true;
				break;
			}

		int a=0;

		if(!end)
			for(int i=0; i<20; i++)
			{
				a=0;
				for(int j=0; j<1000; j++)
				{
					if( 1-(eDXT - i)/(double)eDXT > Math.random()*2 )
						a++;
				}
				System.out.println(i+":   "+a+"    "+(a/10)+"%");
			}	

		System.out.println("Done");
	}
}
