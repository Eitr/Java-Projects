package games;

import java.awt.*;
import javax.swing.*;
import javax.swing.event.*;

@SuppressWarnings("serial")
public class Coloring extends JFrame 
{
	public static void main(String[] Bridget)
	{
		new Coloring();
	}
	public Coloring ()
	{
		color = new JPanel();
		add(color, BorderLayout.CENTER);
		createControlPanel();
		setSampleColor();
		setSize(300,400);
		setLocationRelativeTo(null);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setVisible(true);
	}
	public void createControlPanel ()
	{
		class ColorListener implements ChangeListener
		{
			public void stateChanged(ChangeEvent e)
			{
				setSampleColor();
			}
		}
		
		ChangeListener listener = new ColorListener();

		red = new JSlider (0,255,150);
		red.addChangeListener(listener);
		
		green = new JSlider (0,255,150);
		green.addChangeListener(listener);
		
		blue = new JSlider (0,255,150);
		blue.addChangeListener(listener);
		
		JPanel cp = new JPanel();
		cp.setLayout(new GridLayout(3,2));

		RED = new JLabel();
		GREEN = new JLabel();
		BLUE = new JLabel();
		

//		cp.add(new JLabel("Red"));
		cp.add(RED);
		cp.add(red);
//		cp.add(new JLabel("Green"));
		cp.add(GREEN);
		cp.add(green);
//		cp.add(new JLabel("Blue"));
		cp.add(BLUE);
		cp.add(blue);
		
		add(cp, BorderLayout.SOUTH);
	}
	
	public void setSampleColor()
	{
		int r = (int)(red.getValue());
		int g = (int)(green.getValue());
		int b = (int)(blue.getValue());

		RED.setText("Red: "+red.getValue());
		GREEN.setText("Green: "+green.getValue());
		BLUE.setText("Blue: "+blue.getValue());
		
		color.setBackground(new Color(r,g,b));
		color.repaint();		
	}
	
	JPanel color;
	JSlider red;
	JSlider green;
	JSlider blue;
	JLabel RED;
	JLabel GREEN;
	JLabel BLUE;
}
