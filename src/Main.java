
import java.awt.Cursor;
import java.awt.Image;
import java.awt.MouseInfo;
import java.awt.Point;
import java.awt.Toolkit;
import java.awt.Window;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.io.File;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JOptionPane;

public class Main extends JFrame implements MouseListener
{
	GamePanel gp = new GamePanel();
	int position[][] = new int[6][9];
	public Main()
	{
		playSound();
		// init array
		for(int y = 0 ; y < 6 ; y++)
			for(int x = 0 ; x < 9 ;x ++)
				position[y][x]=0;
		
		setSize(800, 600);
		add(gp);
		addMouseListener(this);
		setTitle("2D Game Programming");
		setLocationRelativeTo(null);
		setDefaultCloseOperation(this.EXIT_ON_CLOSE);
		setVisible(true);
	}
	
	public void playSound(){
		try{
		String filename = "Levels.wav";
		File soundfile = new File(filename);
		AudioInputStream audioIn = AudioSystem.getAudioInputStream(soundfile);
		Clip clip = AudioSystem.getClip();
		clip.open(audioIn);
		clip.start();
		}catch(Exception e){}
		}
	
	public void changeCursor(int index)
	{
		Toolkit toolkit = Toolkit.getDefaultToolkit();
		Image cursorImage = toolkit.getImage("cursor.gif");
		if (index ==1 )
		{
			cursorImage = toolkit.getImage("Image/peashooter1.png");
		}
		else if(index ==2)
		{
			cursorImage = toolkit.getImage("Image/sunflower1.png");
		}
		else
		{
			cursorImage = toolkit.getImage("Image/peaball.png");
		}
	    Point cursorHotSpot = new Point(0,0);
	    Cursor customCursor = toolkit.createCustomCursor(cursorImage, cursorHotSpot, "Cursor");
	    this.setCursor(customCursor);
	}
	
	public static void main(String[] args) 
	{
		new Main();
	}

	int index = 0;
	@Override
	public void mouseClicked(MouseEvent e) 
	{
		if( e.getY()<100 )
		{
			if(e.getX() >=90 && e.getX()<=90+50)
			{
				index = 1;
				
			}
			if(e.getX() >=150 && e.getX()<=150+50)
			{
				index = 2;
			}
		}
		if( e.getY() > 100 && index!=0 )
		{
			if( gp.score<50 )
			{
				JOptionPane.showMessageDialog(null, "Insuficient Money");
				return;
			}
			if( gp.score<100 && index == 1 )
			{
				JOptionPane.showMessageDialog(null, "Insuficient Money");
				return;
			}
			if( position[e.getY()/90][e.getX()/80] == 0)
			{
				if( index ==1  ) gp.score -= 100;
				if ( index == 2 ) gp.score-= 50;
				gp.summon(index,((e.getX()/80)+1)*80-75+36,((e.getY()/90)+1)*90-80);
				index = 0;
				position[e.getY()/90][e.getX()/80] = 1;
			}
			else
			{
				JOptionPane.showMessageDialog(null, "Cant Place a Plant Here !! ");
			}
		}
		changeCursor(index);
	}

	@Override
	public void mousePressed(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseReleased(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseEntered(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseExited(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

}
