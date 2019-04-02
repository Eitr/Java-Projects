import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import java.awt.Point;
import java.awt.Color;
import java.awt.Toolkit;
import java.awt.image.BufferedImage;
import java.util.Calendar;
import java.util.TimeZone;

@SuppressWarnings("serial")
public class ClockDisplay extends JFrame {
	int SIZE = 48;
	int range = 1;

	Calendar date;
	int day;
	TimeZone tz = TimeZone.getTimeZone("America/Chicago");
	int tzOffset;
	boolean isNight = false;

	public static void main(String[] args) {
		new ClockDisplay();
	}
	
	public void setDateTime() {
		date = Calendar.getInstance(tz);
		day = date.get(Calendar.DAY_OF_WEEK);
		tzOffset = tz.getOffset(System.currentTimeMillis());
	}

	public ClockDisplay() {
		// Transparent 16 x 16 pixel cursor image.
		BufferedImage cursorImg = new BufferedImage(16, 16, BufferedImage.TYPE_INT_ARGB);
		Cursor blankCursor = Toolkit.getDefaultToolkit().createCustomCursor(cursorImg, new Point(0, 0), "blank cursor");
		getContentPane().setCursor(blankCursor);

		setUndecorated(true);
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		setSize(1260,680);
		setLocationRelativeTo(null);
		GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice().setFullScreenWindow(this);
		setExtendedState(Frame.MAXIMIZED_BOTH);
		
		setDateTime();
		add(new PaintPanel());
		
		setVisible(true);
	}
	

	public class PaintPanel extends JPanel {
		Display display;
		Font font = new Font("Arial",Font.BOLD,72);

		public PaintPanel() {
			display = new Display(SIZE/2,SIZE/2);
			display.setBorderTime(9.25);

			(new Timer (100*range, new Painter())).start();
		}

		class Painter implements ActionListener {
			public void actionPerformed(ActionEvent e) {
				if(loop()) {
					repaint();
				}
			}
		}

		int prevHour = 0;

		boolean loop() {
			double curTime = (System.currentTimeMillis()+tzOffset)/(3600000.0)%24;
			
			if(prevHour != (int)curTime) {
				prevHour = (int)curTime;
				setDateTime();
				isNight = curTime < 7 || curTime > 19;
				//display.setRandomBorderTime();
			}
			display.setTime(curTime);
			//display.setBorderTime(prevHour);
			return(display.updateTime());
		}

		protected void paintComponent(Graphics g) {
			super.paintComponent(g);
			setBackground(Color.black);

			SIZE = this.getParent().getWidth()/22;
			display.xoffset = SIZE/2;
			display.yoffset = this.getParent().getHeight()/2-SIZE*3;
			
			display.draw(g);

			g.setFont(font);
			int weekx = 100, weeky = 100;
			g.drawString("S",weekx,weeky); weekx+=200;
			g.drawString("M",weekx,weeky); weekx+=200;
			g.drawString("T",weekx,weeky); weekx+=200;
			g.drawString("W",weekx,weeky); weekx+=200;
			g.drawString("R",weekx,weeky); weekx+=200;
			g.drawString("F",weekx,weeky); weekx+=200;
			g.drawString("S",weekx,weeky); weekx+=200;

			g.fillRect(200*(day-1)+60,10,150,10);
			g.fillRect(200*(day-1)+60,140,150,10);
			g.fillRect(200*(day-1)+60,10,10,130);
			g.fillRect(200*(day-1)+200,10,10,140);
		}
	}

	/** A set of clocks to form a display */
	public class Display {
		static final double ns = 0.50, ew = 9.25, ne = 0.25, se = 6.25, sw = 5.75, nw = 0.75;

		// Clock hands formation for each digit
		final double form [][][] = {
			{{se,ew,ew,ew,sw},{ns,se,ew,sw,ns},{ns,ns,ew,ns,ns},{ns,ns,ew,ns,ns},{ns,ne,ew,nw,ns},{ne,ew,ew,ew,nw}},    // 0
			{{ew,ew,se,sw,ew},{ew,ew,ns,ns,ew},{ew,ew,ns,ns,ew},{ew,ew,ns,ns,ew},{ew,ew,ns,ns,ew},{ew,ew,ne,nw,ew}},    // 1
			{{se,ew,ew,ew,sw},{ne,ew,ew,sw,ns},{se,ew,ew,nw,ns},{ns,se,ew,ew,nw},{ns,ne,ew,ew,sw},{ne,ew,ew,ew,nw}},    // 2
			{{se,ew,ew,ew,sw},{ne,ew,ew,sw,ns},{se,ew,ew,nw,ns},{ne,ew,ew,sw,ns},{se,ew,ew,nw,ns},{ne,ew,ew,ew,nw}},    // 3
			{{se,sw,ew,se,sw},{ns,ns,ew,ns,ns},{ns,ne,ew,nw,ns},{ne,ew,ew,sw,ns},{ew,ew,ew,ns,ns},{ew,ew,ew,ne,nw}},    // 4
			{{se,ew,ew,ew,sw},{ns,se,ew,ew,nw},{ns,ne,ew,ew,sw},{ne,ew,ew,sw,ns},{se,ew,ew,nw,ns},{ne,ew,ew,ew,nw}},    // 5
			{{se,sw,ew,ew,ew},{ns,ns,ew,ew,ew},{ns,ne,ew,ew,sw},{ns,se,ew,sw,ns},{ns,ne,ew,nw,ns},{ne,ew,ew,ew,nw}},    // 6
			{{se,ew,ew,ew,sw},{ne,ew,ew,sw,ns},{ew,ew,ew,ns,ns},{ew,ew,ew,ns,ns},{ew,ew,ew,ns,ns},{ew,ew,ew,ne,nw}},    // 7
			{{se,ew,ew,ew,sw},{ns,se,ew,sw,ns},{ns,ne,ew,nw,ns},{ns,se,ew,sw,ns},{ns,ne,ew,nw,ns},{ne,ew,ew,ew,nw}},    // 8
			{{se,ew,ew,ew,sw},{ns,se,ew,sw,ns},{ns,ne,ew,nw,ns},{ne,ew,ew,sw,ns},{ew,ew,ew,ns,ns},{ew,ew,ew,ne,nw}}};   // 9


		int xoffset, yoffset;
		// Display grid of individual clocks
		private Clock display [][]; 
		// Locations on the grid
		private Point digit []; // 00:00
		private Point colon;

		public Display(int xoff, int yoff) {
			xoffset = xoff;
			yoffset = yoff;

			display = new Clock[22][6];
			digit = new Point[] {new Point(0,0),new Point(5,0),new Point(12,0),new Point(17,0)};
			colon = new Point(10,1);

			for (int x = 0; x < display.length; x++) {
				for (int y = 0; y < display[0].length; y++) {
					display[x][y] = new Clock(SIZE*x+xoff,SIZE*y+yoff);
					display[x][y].min = (int)(Math.random()*60);
					display[x][y].hour = (int)(Math.random()*12);
					display[x][y].goalMin = (int)(Math.random()*60);
					display[x][y].goalHour = (int)(Math.random()*12);
					display[x][y].updateAngle();
				}
			}
		}

		public void setTime(double time) {
			int min = (int)((time-(int)time)*60)%60;
			int hour = ((int)time-1+12)%12+1;

			// Each display has 4 digits
			setDigit(0, hour/10);
			setDigit(1, hour%10);
			setDigit(2, min/10);
			setDigit(3, min%10);
		}

		private void setDigit(int d, int time) {
			for (int x = 0; x < 5; x++) {
				for (int y = 0; y < 6; y++) {
					display[x+digit[d].x][y+digit[d].y].setGoalTime(form[time][y][x]);
				}
			}
		}

		private boolean updateTime() {
			boolean alive = false;
			for (int x = 0; x < display.length; x++) {
				for (int y = 0; y < display[0].length; y++) {
					if(display[x][y].updateTime(range,0)) {
						alive = true;
					}
				}
			}
			return alive;
		}


		private void setBorderTime(double time) {
			for (int x = 0; x < display.length; x++) {
				for (int y = 0; y < display[0].length; y++) {
					boolean able = true;
					for (int d = 0; d < digit.length; d++) {
						if (x >= digit[d].x && x < digit[d].x+5 && y >= digit[d].y && y < digit[d].y+6) {
							able = false;
							break;
						}
					}
					if (x >= colon.x && x < colon.x+2 && y >= colon.y && y < colon.y+4) {
						if ( x == colon.x && (y == colon.y || y == colon.y+2) )
							display[x][y].setGoalTime(6.25);
						else if ( x == colon.x && (y == colon.y+1 || y == colon.y+3) )
							display[x][y].setGoalTime(0.25);
						else if ( x == colon.x+1 && (y == colon.y || y == colon.y+2) )
							display[x][y].setGoalTime(5.75);
						else if ( x == colon.x+1 && (y == colon.y+1 || y == colon.y+3) )
							display[x][y].setGoalTime(0.75);
					} else if (able) {
						display[x][y].setGoalTime(time);
					}
				}
			}
		}

		private void setRandomBorderTime() {
			for (int x = 0; x < display.length; x++) {
				for (int y = 0; y < display[0].length; y++) {
					boolean able = true;
					for (int d = 0; d < digit.length; d++) {
						if (x >= digit[d].x && x < digit[d].x+5 && y >= digit[d].y && y < digit[d].y+6) {
							able = false;
							break;
						}
					}
					if (x >= colon.x && x < colon.x+2 && y >= colon.y && y < colon.y+4) {
						if ( x == colon.x && (y == colon.y || y == colon.y+2) )
							display[x][y].setGoalTime(6.25);
						else if ( x == colon.x && (y == colon.y+1 || y == colon.y+3) )
							display[x][y].setGoalTime(0.25);
						else if ( x == colon.x+1 && (y == colon.y || y == colon.y+2) )
							display[x][y].setGoalTime(5.75);
						else if ( x == colon.x+1 && (y == colon.y+1 || y == colon.y+3) )
							display[x][y].setGoalTime(0.75);
					} else if (able) {
						display[x][y].setGoalTime(Math.random()*12);
					}
				}
			}
		}


		private void addTime(int m, int h) {
			for (int x = 0; x < display.length; x++) {
				for (int y = 0; y < display[0].length; y++) {
					display[x][y].updateTime(m, h);
				}
			}
		}

		public void draw(Graphics g) {
			for (int x = 0; x < display.length; x++) {
				for (int y = 0; y < display[0].length; y++) {
					// remove top/bottom clocks on center colon
					if(!((x == colon.x && y == colon.y-1)||(x == colon.x+1 && y == colon.y-1)||
						(x == colon.x && y == colon.y+4)||(x == colon.x+1 && y == colon.y+4)))
					{
						display[x][y].draw(g,x*SIZE+xoffset,y*SIZE+yoffset);
					}
				}
			}
		}
	}


	/** Each individual analog clock */
	public class Clock {
		private double minAngle = 0;
		private double hourAngle = 0;
		int min = 0;
		int hour = 0;
		int goalMin = 0;
		int goalHour = 0;
		int x = 0;
		int y = 0;

		public Clock(int x, int y) {
			this.x = x;
			this.y = y;
		}

		public void setGoalTime(double time) {
			goalMin = (int)Math.round((time-(int)time)*60)%60;
			goalHour = (int)time%12;
		}

		public boolean updateTime(int addmin, int addhour) {
			if(Math.abs((goalMin+30)%60 - (min+30)%60) > range || goalHour != hour) {
				int offsethr = goalHour-hour;
				int offsetmin = goalMin-min;

				if (offsethr == 0) {
					min = (min + ((offsetmin<0)? -1:+1) * addmin);
				} else {
					min = (min + ((offsethr>=6 || (offsethr>-6 && offsethr<0))? -1:+1) * addmin);
				}
				hour = (hour + addhour) % 12;

				if (min >= 60) {
					hour = (hour+1)%12;
					min = min % 60;
				}
				else if (min < 0) {
					hour = (hour-1+12)%12;
					min = (min+60) % 60;
				}

				updateAngle();
				return true;
			} else {
				return false;
			}
		}

		public void updateAngle() {
			minAngle = min/60.0 * Math.PI*2 - Math.PI/2;
			hourAngle = hour/12.0 * Math.PI*2 + (min/60.0*Math.PI/6.0) - Math.PI/2;
		}

		public void setAngle(double num) {
			min = (int)(num%1*60);
			hour = (int)num;
			minAngle = min/60.0 * Math.PI*2 - Math.PI/2;
			hourAngle = hour/12.0 * Math.PI*2 - Math.PI/2;
		}

		private int r=0,g=255,b=0;
		private int mode = 1;

		public void draw(Graphics g2,int x,int y) {
			int s = 4;
			int max = isNight? 100:250;
			switch (mode) {
				case 1: if(b<max) b+=s;else mode++; break;
				case 2: if(g>10)  g-=s;else mode++; break;
				case 3: if(r<max) r+=s;else mode++; break;
				case 4: if(b>10)  b-=s;else mode++; break;
				case 5: if(g<max) g+=s;else mode++; break;
				case 6: if(r>10)  r-=s;else mode=1; break;
			}
			g2.setColor(new Color(r,g,b));

			//g2.drawLine(x,y,(int)(x+SIZE*.5*Math.cos(minAngle)),(int)(y+SIZE*.5*Math.sin(minAngle)));
			//g2.drawLine(x,y,(int)(x+SIZE*.4*Math.cos(hourAngle)),(int)(y+SIZE*.4*Math.sin(hourAngle)));

			// Draw a few lines to make it thicker
			for(int j = -2; j<=2; j++) {
				for(int k = -2; k<=2; k++) {
					g2.drawLine(x+j,y+k,(int)(x+SIZE*.5*Math.cos(minAngle))+j,(int)(y+SIZE*.5*Math.sin(minAngle))+k);
					g2.drawLine(x+j,y+k,(int)(x+SIZE*.4*Math.cos(hourAngle))+j,(int)(y+SIZE*.4*Math.sin(hourAngle))+k);
				}
			}
		}
	}

}
