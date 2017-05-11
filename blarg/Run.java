package blarg;

import javax.swing.JFrame;

public class Run 
{
    /** Main method which creates the frame and its components */
	public static void main(String[] args)
	{
    	Main frame= new Main();
        frame.setTitle("Ginnungagap");
        frame.setLocationRelativeTo(null);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(200,200);
        frame.setVisible(true);
	}
}
