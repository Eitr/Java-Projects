package maze;

import java.awt.*;
import javax.swing.*;

@SuppressWarnings("serial")
public class TestFrame extends JFrame
{
	Labyrinth game = new Labyrinth ();
	
	
	private void createMazes ()
	{
		game.addMaze(Color.blue, 601,601);
		game.addMaze(Color.yellow,23,23);
		game.addMaze(Color.green,44,12);
		game.addMaze(Color.magenta, 32, 43);

		game.addPortal(Color.blue,11,11,Color.yellow);
		game.addPortal(Color.yellow,1,9,Color.blue);
		game.addPortal(Color.yellow,19,1,Color.green);
		game.addPortal(Color.green,3,9,Color.yellow);
		game.addPortal(Color.blue, 11,9,Color.magenta);
		game.addPortal(Color.magenta,5,1,Color.blue);
		game.addPortal(Color.magenta,11,21,Color.green);
		game.addPortal(Color.green,5,1,Color.magenta);
		

		game.setItem(Color.blue, 6);
		game.setItem(Color.yellow, 6);
		game.setItem(Color.green, 6);
		game.setItem(Color.magenta, 6);


		game.setStartLocation(new Point(1,1));
		game.setEndLocation(new Point(30,36));
		
		game.setStartMaze(Color.blue);
		game.setEndMaze(Color.magenta);


		game.setMazeName(Color.blue, "Blue");
		game.setMazeName(Color.yellow, "Yellow");
		game.setMazeName(Color.green, "Green");
		game.setMazeName(Color.magenta, "Magenta");
		
		
		game.setDifficulty(2);
		
		game.finalize();
	}
	
	
	public TestFrame ()
	{
        this.setTitle("Maze Test Frame");
        this.setDefaultCloseOperation(EXIT_ON_CLOSE);
        this.setSize(Toolkit.getDefaultToolkit().getScreenSize().width,Toolkit.getDefaultToolkit().getScreenSize().height-45);
        this.setLocationRelativeTo(null);

        createMazes();
        
        this.setVisible(true);
        this.add(game);
	}
	
	public static void main (String[] blarg)
	{
		new TestFrame();
	}
	
}
