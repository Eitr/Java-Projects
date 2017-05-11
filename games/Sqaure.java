package games;

import javax.swing.JFrame;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

@SuppressWarnings("serial")
public class Sqaure extends JFrame
{
	class Listener implements ActionListener
	{
		public void actionPerformed(ActionEvent e)
		{
			for(int x = 0; x < b.length; x++)
				for(int y = 0; y < b[x].length; y++)
				{
					if(e.getSource() == b[x][y])
					{
						if(x == 0 && y == 0)
						{
//							Switch(b[0][0]);
							Switch(b[0][1]);
							Switch(b[1][0]);
						}
						else if(x == 0 && y == 1)
						{
//							Switch(b[0][1]);
							Switch(b[0][0]);
							Switch(b[1][1]);
							Switch(b[0][2]);
						}
						else if(x == 0 && y == 2)
						{
							Switch(b[0][1]);
							Switch(b[1][2]);
//							Switch(b[0][2]);
						}
						else if(x == 1 && y == 0)
						{
//							Switch(b[1][0]);
							Switch(b[0][0]);
							Switch(b[2][0]);
							Switch(b[1][1]);
						}
						else if(x == 1 && y == 1)
						{
//							Switch(b[1][1]);
							Switch(b[1][0]);
							Switch(b[0][1]);
							Switch(b[2][1]);
							Switch(b[1][2]);
						}
						else if(x == 1 && y == 2)
						{
							Switch(b[1][1]);
							Switch(b[0][2]);
							Switch(b[2][2]);
//							Switch(b[1][2]);
						}
						else if(x == 2 && y == 0)
						{
//							Switch(b[2][0]);
							Switch(b[1][0]);
							Switch(b[2][1]);
						}
						else if(x == 2 && y == 1)
						{
//							Switch(b[2][1]);
							Switch(b[2][0]);
							Switch(b[1][1]);
							Switch(b[2][2]);
						}
						else if(x == 2 && y == 2)
						{
//							Switch(b[2][2]);
							Switch(b[2][1]);
							Switch(b[1][2]);
						}
					}
				}
		}
	}
	public void Switch(JButton button)
	{
		if(button.getBackground() == Color.blue)
			button.setBackground(Color.red);
		else
			button.setBackground(Color.blue);
	}

	JButton b[][] = new JButton [3][3];
	public Sqaure()
	{
		JPanel p = new JPanel();
		p.setLayout(new GridLayout(3,3));


		for(int x = 0; x < b.length; x++)
			for(int y = 0; y < b[x].length; y++)
			{
				b[x][y] = new JButton("O");

				b[x][y].setBackground(Color.blue);
				b[x][y].addActionListener(new Listener());

				p.add(b[x][y]);
			}

		Container cp = getContentPane();

		cp.add(p);
	}
	public static void main(String[] args)
	{
		Sqaure frame = new Sqaure();
		frame.setTitle("Texas Hold'Em");
		frame.setLocationRelativeTo(null);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setSize(400,400);
		frame.setVisible(true);
		frame.setLocation(0, 0);
	}
}
