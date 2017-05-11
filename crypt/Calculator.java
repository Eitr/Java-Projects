package crypt;

import javax.swing.*;

import java.awt.event.*;

@SuppressWarnings("serial")
public class Calculator extends JFrame
{
	Listener panel= new Listener();
	static boolean print;
	static int x;
	
	ImageIcon pic=new ImageIcon();
	
	
	public Calculator()
	{
		pic.setImage(null);
		
		getContentPane().add(panel);
		panel.setFocusable(true);
	}
	private class Listener extends JPanel
	{
		public Listener()
		{
			Timer timer = new Timer(25, new TimerListener());
			timer.start();

			addKeyListener(new KeyAdapter()
			{
				public void keyPressed(KeyEvent e)
				{
					x=e.getKeyChar();
					print=true;
				}
			});
		}
	}
	class TimerListener implements ActionListener
	{
		public void actionPerformed(ActionEvent e)
		{
			if(get())
			{
				System.out.println(getInt());
				set(false);
			}
		}
	}
	public static boolean get()
	{
		return print;
	}
	public static void set(boolean x)
	{
		print=x;
	}
	public static int getInt()
	{
		return x;
	}
	static Calculator frame;
	public static void main(String[] args)
	{
		frame= new Calculator();
		frame.setTitle("Crypt");
		frame.setSize(5,5);
		frame.setLocationRelativeTo(null);
		frame.setDefaultCloseOperation(EXIT_ON_CLOSE);
		frame.setVisible(true);
	}
}
