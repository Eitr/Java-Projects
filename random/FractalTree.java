package random;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

@SuppressWarnings("serial")
public class FractalTree extends JFrame
{
	int sizel = 5;
	int sizeh = 25;
	
	int x = (int)Toolkit.getDefaultToolkit().getScreenSize().getWidth()/4;
	int y = (int)Toolkit.getDefaultToolkit().getScreenSize().getHeight()-50;
	
	int length = 150;
	
	int scalel = 65;
	int scaleh = 85;
	
	int thetal = 0;
	int thetah = 25;
	
	int thickness = 16;
	
	int branchesl = 2;
	int branchesh = 3;
	
	int kill = 2000000;
	int count = 0;
	
	Color color = Color.gray;
	boolean randomColor = false;
	boolean shadeColor = true;
	boolean drawCircles = true;
	
	/////////////////////////////////////////

	JSlider xf = new JSlider(0,(int)Toolkit.getDefaultToolkit().getScreenSize().getWidth(),x);
	JSlider yf = new JSlider(0,(int)Toolkit.getDefaultToolkit().getScreenSize().getHeight(),y);
	JSlider sizelf = new JSlider		(0,40,sizel);
	JSlider sizehf = new JSlider		(0,40,sizeh);
	JSlider scalelf = new JSlider		(50,100,scalel);
	JSlider scalehf = new JSlider		(50,100,scaleh);
	JSlider thetalf = new JSlider		(0,50,thetal);
	JSlider thetahf = new JSlider		(0,50,thetah);
	JSlider brancheslf = new JSlider	(0,6,branchesl);
	JSlider brancheshf = new JSlider	(0,6,branchesh);
	JSlider lengthf = new JSlider		(0, 300, length);
	JSlider thicknessf = new JSlider	(0, 64, thickness);
	
	void update ()
	{
		try
		{
			x = xf.getValue();
			y = yf.getValue();
			sizel = sizelf.getValue();
			sizeh = sizehf.getValue();
			scalel = scalelf.getValue();
			scaleh = scalehf.getValue();
			thetal = thetalf.getValue();
			thetah = thetahf.getValue();
			branchesl = brancheslf.getValue();
			branchesh = brancheshf.getValue();
			length = lengthf.getValue();
			thickness = thicknessf.getValue();
		}
		catch (Exception e)
		{
			JOptionPane.showMessageDialog(null, "Invalid Input", "Error", JOptionPane.ERROR_MESSAGE);
		}
		repaint();
	}
	
	
	public void draw (Graphics2D g, double length, double x, double y, double theta, float thickness)
	{
		count++;
		if ( count > kill )
			return;
		if ( length <= 1)
		{
			if(drawCircles)
				g.drawOval((int)x,(int)y, (int)(Math.random()*6), (int)(Math.random()*6));
			return;
		}
		if ( this.length * Math.pow(Math.random()*(scaleh/100.-scalel/100.)+scalel/100., Math.random()*(sizeh-sizel+1)+sizel) > length )
		{
			if(drawCircles)
				g.drawOval((int)x,(int)y, (int)(Math.random()*6), (int)(Math.random()*6));
			return;
		}
		
		double nx = Math.cos(theta)*length+x;
		double ny = Math.sin(theta)*length+y;
		
		if ( randomColor )
			g.setColor(new Color((int)(Math.random()*255),(int)(Math.random()*255),(int)(Math.random()*255)));
		else if (shadeColor)
		{
			int shade = (int)(Math.random()*3);
			
			if ( shade == 2 )
				g.setColor(color.darker());
			else if ( shade == 1 )
				g.setColor(color.brighter());
			else
				g.setColor(color);
		}
		else
			g.setColor(color);
		
		g.setStroke(new BasicStroke(thickness));
		g.drawLine((int)x,(int)y,(int)nx,(int)ny);
		
		double scale = (Math.random()*(scaleh/100.-scalel/100.)+scalel/100.);
		
		double nlength = length * scale;
		float thicknessn = (float)(thickness * scale * scale);
		
		int branches = (int)(Math.random()*(branchesh-branchesl+1)+branchesl);
		
		int flip = 1;
		
		for ( int i = 0; i < branches; i++)
		{
			draw( g, nlength, nx, ny, theta + (Math.PI*(Math.random()*(thetah/100.-thetal/100.)+thetal/100.))*flip, thicknessn);
			flip*=-1;
		}
	}
	
	
	
	
	
	public class PaintPanel extends JPanel
	{
		public void paintComponent (Graphics weirdo)
		{
			Graphics2D g = (Graphics2D)weirdo;
			super.paintComponent(g);
			setBackground(Color.black);
			g.setColor(Color.white);
			g.setStroke(new BasicStroke(thickness));
			draw(g,length,x,y,-Math.PI/2, thickness);
			count = 0;
		}
	}
	
	public FractalTree ()
	{
		setTitle("Random-Fractal Tree");
		setSize(1180,700);
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		setLocationRelativeTo(null);

		
		////////////////////////////////////////////////////////
		
		

		JButton colorb = new JButton("Select");
		colorb.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e)
			{
				Color newColor = JColorChooser.showDialog(null, "Choose the color!", color);
				
				if(newColor != null)
					color = newColor;
				randomColor = false;
				update();
			}
		});

		JButton randomb = new JButton("Random");
		randomb.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e)
			{
				randomColor = true;
				update();
			}
		});

		JCheckBox shadeb = new JCheckBox("Shade");
		shadeb.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e)
			{
				if(shadeColor) shadeColor = false;
				else shadeColor = true;
				update();
			}
		});
		shadeb.setSelected(true);

		JCheckBox circles = new JCheckBox("Circles");
		circles.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e)
			{
				if(drawCircles) drawCircles = false;
				else drawCircles = true;
				update();
			}
		});
		circles.setSelected(true);
		
		
		

		xf.setToolTipText("X-coordinate");
		yf.setToolTipText("Y-coordinate");
		sizelf.setToolTipText("Minimum");
		sizehf.setToolTipText("Maximum");
		scalelf.setToolTipText("Minimum");
		scalehf.setToolTipText("Maximum");
		thetalf.setToolTipText("Minimum");
		thetahf.setToolTipText("Maximum");
		brancheslf.setToolTipText("Minimum");
		brancheshf.setToolTipText("Maximum");
		lengthf.setToolTipText("Length of starting branch");
		thicknessf.setToolTipText("Size of starting branch.");
		
		
		
		JPanel locationp = new JPanel();
		JPanel sizep = new JPanel();
		JPanel scalep = new JPanel();
		JPanel thetap = new JPanel();
		JPanel branchesp = new JPanel();
		JPanel lengthp = new JPanel();
		JPanel thicknessp = new JPanel();
		JPanel colorp = new JPanel();

		locationp.setLayout(new BoxLayout(locationp, BoxLayout.Y_AXIS));
		sizep.setLayout(new BoxLayout(sizep, BoxLayout.Y_AXIS));
		scalep.setLayout(new BoxLayout(scalep, BoxLayout.Y_AXIS));
		thetap.setLayout(new BoxLayout(thetap, BoxLayout.Y_AXIS));
		branchesp.setLayout(new BoxLayout(branchesp, BoxLayout.Y_AXIS));
		lengthp.setLayout(new BoxLayout(lengthp, BoxLayout.Y_AXIS));
		thicknessp.setLayout(new BoxLayout(thicknessp, BoxLayout.Y_AXIS));
		colorp.setLayout(new GridLayout(2,2));
		
		///////////////////////////////////////////
		

		xf.setMajorTickSpacing((int)Toolkit.getDefaultToolkit().getScreenSize().getWidth()/5);
		xf.setMinorTickSpacing((int)Toolkit.getDefaultToolkit().getScreenSize().getWidth()/10);
		xf.setPaintLabels(true);
		xf.setPaintTicks(true);
		xf.setSnapToTicks(true);
		
		yf.setMajorTickSpacing((int)Toolkit.getDefaultToolkit().getScreenSize().getHeight()/5);
		yf.setMinorTickSpacing((int)Toolkit.getDefaultToolkit().getScreenSize().getHeight()/10);
		yf.setPaintLabels(true);
		yf.setPaintTicks(true);
		yf.setSnapToTicks(true);

		sizelf.setMajorTickSpacing(sizelf.getMaximum()/10);
		sizelf.setMinorTickSpacing(sizelf.getMaximum()/40);
		sizelf.setPaintLabels(true);
		sizelf.setPaintTicks(true);
		sizelf.setSnapToTicks(true);

		sizehf.setMajorTickSpacing(sizehf.getMaximum()/10);
		sizehf.setMinorTickSpacing(sizehf.getMaximum()/40);
		sizehf.setPaintLabels(true);
		sizehf.setPaintTicks(true);
		sizehf.setSnapToTicks(true);

		scalelf.setMajorTickSpacing(scalelf.getMaximum()/10);
		scalelf.setMinorTickSpacing(scalelf.getMaximum()/20);
		scalelf.setPaintLabels(true);
		scalelf.setPaintTicks(true);
		scalelf.setSnapToTicks(true);

		scalehf.setMajorTickSpacing(scalehf.getMaximum()/10);
		scalehf.setMinorTickSpacing(scalehf.getMaximum()/20);
		scalehf.setPaintLabels(true);
		scalehf.setPaintTicks(true);
		scalehf.setSnapToTicks(true);

		thetalf.setMajorTickSpacing(thetalf.getMaximum()/5);
		thetalf.setMinorTickSpacing(thetalf.getMaximum()/10);
		thetalf.setPaintLabels(true);
		thetalf.setPaintTicks(true);
		thetalf.setSnapToTicks(true);

		thetahf.setMajorTickSpacing(thetahf.getMaximum()/5);
		thetahf.setMinorTickSpacing(thetahf.getMaximum()/10);
		thetahf.setPaintLabels(true);
		thetahf.setPaintTicks(true);
		thetahf.setSnapToTicks(true);
		
		brancheslf.setMajorTickSpacing(brancheslf.getMaximum()/6);
		brancheslf.setPaintLabels(true);
		brancheslf.setPaintTicks(true);
		brancheslf.setSnapToTicks(true);
		
		brancheshf.setMajorTickSpacing(brancheshf.getMaximum()/6);
		brancheshf.setPaintLabels(true);
		brancheshf.setPaintTicks(true);
		brancheshf.setSnapToTicks(true);

		lengthf.setMajorTickSpacing(lengthf.getMaximum()/6);
		lengthf.setMinorTickSpacing(lengthf.getMaximum()/30);
		lengthf.setPaintLabels(true);
		lengthf.setPaintTicks(true);
		lengthf.setSnapToTicks(true);

		thicknessf.setMajorTickSpacing(thicknessf.getMaximum()/8);
		thicknessf.setMinorTickSpacing(thicknessf.getMaximum()/16);
		thicknessf.setPaintLabels(true);
		thicknessf.setPaintTicks(true);
		thicknessf.setSnapToTicks(true);
		
		
		
		//////////////////////////////////////////
		
		
		locationp.add(xf);
		locationp.add(yf);
		sizep.add(sizelf);
		sizep.add(sizehf);
		scalep.add(scalelf);
		scalep.add(scalehf);
		thetap.add(thetalf);
		thetap.add(thetahf);
		branchesp.add(brancheslf);
		branchesp.add(brancheshf);
		lengthp.add(lengthf);
		thicknessp.add(thicknessf);
		colorp.add(colorb);
		colorp.add(randomb);
		colorp.add(shadeb);
		colorp.add(circles);
		
		locationp.setBorder(BorderFactory.createTitledBorder	("Location (x,y)"));
		sizep.setBorder(BorderFactory.createTitledBorder		("Size"));
		scalep.setBorder(BorderFactory.createTitledBorder		("Scale (%)"));
		thetap.setBorder(BorderFactory.createTitledBorder		("Angle (%)"));
		branchesp.setBorder(BorderFactory.createTitledBorder	("Branches"));
		lengthp.setBorder(BorderFactory.createTitledBorder		("Length"));
		thicknessp.setBorder(BorderFactory.createTitledBorder	("Thickness"));
		colorp.setBorder(BorderFactory.createTitledBorder		("Color"));
		
		int width = 500;
		
		locationp.setMaximumSize(new Dimension(width,0));
		sizep.setMaximumSize(new Dimension(width,0));
		scalep.setMaximumSize(new Dimension(width,0));
		thetap.setMaximumSize(new Dimension(width,0));
		branchesp.setMaximumSize(new Dimension(width,0));
		lengthp.setMaximumSize(new Dimension(width,0));
		thicknessp.setMaximumSize(new Dimension(width,0));
		colorp.setMaximumSize(new Dimension(width,0));

		JButton create = new JButton("Create");
		create.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e)
			{update();}});
		create.setAlignmentX(CENTER_ALIGNMENT);

		JButton help = new JButton("Help");
		help.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e)
			{
				int z = -1;
				try{
					z = JOptionPane.showOptionDialog(null, "Choose a category:", "Help", 0, 
							JOptionPane.QUESTION_MESSAGE, null, new String[]{"Location","Size","Scale","Angle","Branches","Length","Thickness","Color"}, null);
				}catch (Exception error) {}
				
				if ( z == -1 ) 
					return;
				
				String message = "";
				String type = "";
				
				switch (z)
				{
					case 0:  message = "Placement of the bottom side of the first branch. \n" +
					"Bounds are set to be inside the screen resolution, \nso as not to lose where your tree is!"; type = "Location"; break;
					case 1:  message = "Determines the depth of the tree. \nIt is the number of branches in succession " +
							"\nfrom the base to the tip of the tree."; type = "Size"; break;
					case 2:  message = "Shrinking factor. A percentage of which to " +
							"\nmultiply the previous branch'es length by."; type = "Scale"; break;
					case 3:  message = "Curvature. A percentage of PI to curve the " +
							"\nnext branch from the previous branch'es angle."; type = "Angle"; break;
					case 4:  message = "Number of recursive splits at the end of every branch."; type = "Branches"; break;
					case 5:  message = "Length of the initial branch. Will determine overall " +
							"\nsize of the tree based on the scale factor."; type = "Length"; break;
					case 6:  message = "Width of the first branch. Each successive branch " +
							"\nwill scale down making it look more realistic."; type = "Thickness"; break;
					case 7:  message = "Select allows you to choose a color to shade the tree." +
							"\nRandom will create a random color for every branch." +
							"\nShade toggles whether to shade the tree or not." +
							"\nCircles adds tiny ovals to the ends of each branch."; type = "Color"; break;
				}
				
				JOptionPane.showMessageDialog(null,message,type,JOptionPane.PLAIN_MESSAGE);
			}});
		help.setAlignmentX(CENTER_ALIGNMENT);
		
		JPanel endP = new JPanel();
		endP.setLayout(new BoxLayout(endP,BoxLayout.X_AXIS));
		endP.add(Box.createGlue());
		endP.add(help);
		endP.add(Box.createGlue());
		endP.add(create);
		endP.add(Box.createGlue());
		endP.setBorder(BorderFactory.createTitledBorder(""));
		
		JPanel cPanel = new JPanel();
		cPanel.setLayout(new BoxLayout(cPanel, BoxLayout.Y_AXIS));

		JScrollPane pane = new JScrollPane(cPanel);
		
		cPanel.add(Box.createGlue());
		
		JLabel title = new JLabel("Controls");
		title.setAlignmentX(CENTER_ALIGNMENT);
		title.setFont(new Font("Georgia",Font.BOLD,36));
		cPanel.add(title);
		
		cPanel.add(Box.createGlue());
		
		cPanel.add(locationp);
		cPanel.add(sizep);
		cPanel.add(scalep);
		cPanel.add(thetap);
		cPanel.add(branchesp);
		cPanel.add(lengthp);
		cPanel.add(thicknessp);
		cPanel.add(colorp);
		
		cPanel.add(endP);
		
		cPanel.add(Box.createGlue());
		
		///////////////////////////////////////////////////////
		
		setLayout(new BorderLayout());
		
		PaintPanel paint = new PaintPanel();
		paint.setVisible(true);
		add(paint);
		
		add(pane, BorderLayout.EAST);
		
		setVisible(true);
	}
	
	public static void main (String[] args)
	{
		new FractalTree();
	}
}
