import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Rectangle;
import java.util.Random;
import java.util.Vector;

import javax.swing.ImageIcon;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

public class GamePanel extends JPanel 
{
	Random rand = new Random();
	
	int score = 200;
	// animation index
	private int plantindex = 1;
	private int zombieAnimation = 1;
	
	// vector character for zombie and plant
	private Vector<Point> zombie = new Vector<Point>(100,100);
	private Vector<Point> pea = new Vector<Point>(100,100);
	private Vector<Point> sun = new Vector<Point>(200,200);
	Vector<Health> zombieHealth = new Vector<Health>();
	Vector<Health> peaHealth = new Vector<Health>();
	Vector<Health> sunHealth = new Vector<Health>();
	
	private Vector<Point> bullet = new Vector<Point>();
	
	private long lastTime, currTime;
	
	// collition
	private Vector<Rectangle> zombieRect = new Vector<Rectangle>();
	private Vector<Rectangle> peaRect = new Vector<Rectangle>(); 
	private Vector<Rectangle> sunRect = new Vector<Rectangle>(); 
	private Vector<Rectangle> bulletRect = new Vector<Rectangle>(); 

	Thread th = new Thread(new Runnable() {
		
		@Override
		public void run() {
			
			while(true)
			{
				if(plantindex < 6)
				{
					plantindex++;
				}
				else
				{
					plantindex=1;
				}
				
				if(zombieAnimation < 18 && zombieAnimation!=0)
				{
					zombieAnimation++;
				}
				else if(zombieAnimation != 0)
				{
					zombieAnimation=1;
				}
				else
				{
				}
				
				repaint();
				
				try {
					Thread.sleep(40);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			
		}
	});
	
	private Vector<Point> coin = new Vector<Point>();
	
	public GamePanel()
	{
		setDoubleBuffered(true);
		lastTime = System.currentTimeMillis();
		th.start();
		
		int position[] = new int[5];
		for(int i = 0 ; i < 5 ; i++)
			position[i]=0;
		
		for(int i = 0 ; i < 3 ; i++)
		{
			int random;
			while(true)
			{
				random = rand.nextInt(5);
				if(position[random]==0)
				{
					position[random] = random*100+50;
					break;
				}
				else
				{
					continue;
				}
			}
			zombie.add(new Point(800,position[random]));
			zombieHealth.add(new Health(zombie.get(i)));
		}
	}
	
	public void summon(int index, int x ,int y)
	{
		if (index == 1)
		{
			pea.add(new Point(x,y));
			peaHealth.add(new Health(pea.lastElement()));
		}
		else if(index==2)
		{
			sun.add(new Point(x,y));
			sunHealth.add(new Health(sun.lastElement()));
		}
	}
	
	public void drawHealth(Graphics2D g2d)
	{
		for(int i = 0 ; i < zombieHealth.size() ; i++)
		{
			g2d.drawImage(new ImageIcon("Image/heartfill"+(int)zombieHealth.get(i).hp+".png").getImage(), 
					zombieHealth.get(i).attachedTo.x, zombieHealth.get(i).attachedTo.y, null);
		}
		for(int i = 0 ; i < peaHealth.size() ; i++)
		{
			g2d.drawImage(new ImageIcon("Image/heartfill"+(int)peaHealth.get(i).hp+".png").getImage(), 
					peaHealth.get(i).attachedTo.x, peaHealth.get(i).attachedTo.y, null);
		}
		for(int i = 0 ; i < sunHealth.size() ; i++)
		{
			g2d.drawImage(new ImageIcon("Image/heartfill"+(int)sunHealth.get(i).hp+".png").getImage(), 
					sunHealth.get(i).attachedTo.x, sunHealth.get(i).attachedTo.y, null);
		}
	}
	
	public void draw(Graphics2D g2d)
	{
		// pea
		for(int i = 0 ; i < pea.size() ; i++)
		{
			g2d.drawImage(new ImageIcon("Image/peashooter"+plantindex+".png").getImage(), pea.get(i).x, pea.get(i).y,null);
			peaRect.add(new Rectangle(pea.get(i).x, pea.get(i).y,50,50));
		}
		
		// sun
		for(int i = 0 ; i < sun.size() ; i++)
		{
			g2d.drawImage(new ImageIcon("Image/sunflower"+plantindex+".png").getImage(), 
					sun.get(i).x, sun.get(i).y,null);
			sunRect.add(new Rectangle(sun.get(i).x, sun.get(i).y,80,80));
		}
		
		// zombie
		for(int i = 0 ; i < zombie.size() ; i++)
		{
			zombieRect.add(new Rectangle(zombie.get(i).x, zombie.get(i).y+10,50,80));
			if(!zombieHealth.get(i).isAttack)
			{
				g2d.drawImage(new ImageIcon("Image/normalzombie"+zombieAnimation+".png").getImage(), 
						zombie.get(i).x, zombie.get(i).y,null);
				
				if(zombie.get(i).x > -100)
				{
					zombie.get(i).x --;
				}
				else
					zombie.get(i).x = 800;
			}
			else
			{
				if( zombieAnimation>16 )
					zombieAnimation = 1;
				g2d.drawImage(new ImageIcon("Image/normalzombieattack"+(zombieAnimation%7+1)+".png").getImage(), 
						zombie.get(i).x, zombie.get(i).y,null);
			}
		}
	}
	
	public void addCoin()
	{
		int x,y;
		x = rand.nextInt(8)*100;
		y = 0;
		
		coin.add(new Point(x,y));
	}
	
	public void drawCoin(Graphics2D g2d)
	{
		for(int i = 0 ; i < coin.size() ; i++)
		{
			g2d.drawImage(new ImageIcon("Image/sun.png").getImage(), coin.get(i).x, coin.get(i).y, null);
			
			if( coin.get(i).y <= (rand.nextInt(3)+2*100) )
			{
				coin.get(i).y++;
			}
			else
			{
				coin.remove(i);
				score+=100;
			}
		}
	}
	
	@Override
	public void paint(Graphics g) 
	{
		//super.paint(g);
		Graphics2D g2d = (Graphics2D) g;
		
		//init back rect
		zombieRect.removeAllElements();
		bulletRect.removeAllElements();
		peaRect.removeAllElements();
		sunRect.removeAllElements();
		
		// background
		g2d.drawImage(new ImageIcon("Image/backgroundsurvival.jpg").getImage(), 0, 0,null);
		g2d.drawImage(new ImageIcon("Image/peashooter_card_shine.png").getImage(), 90, 10,null);
		g2d.drawImage(new ImageIcon("Image/sunflower_card_shine.png").getImage(), 90+60, 10,null);
		
		drawCoin(g2d);
		// draw
		draw(g2d);
		drawHealth(g2d);
		
		// bullet
		for(int i = 0 ; i < bullet.size() ; i++)
		{
			if(bullet.get(i).x <= 800)
				bullet.get(i).x += 2;
			g2d.drawImage(new ImageIcon("Image/fireball.png").getImage(),bullet.get(i).x , bullet.get(i).y,null);

			bulletRect.add(new Rectangle(bullet.get(i).x , bullet.get(i).y,50,50));
		}
		
		// update bullet shoot interval
		currTime = System.currentTimeMillis();
		if(currTime - lastTime > 5000)
		{
			addCoin();
			lastTime = currTime;
			if(zombie.size()!=0)
			{
				for(int i = 0 ; i < pea.size() ; i++)
				{
					for(int j = 0 ; j < zombie.size() ; j++)
						if( (pea.get(i).y/110)==(zombie.get(j).y/110) )
						{
							shoot(pea.get(i).x,pea.get(i).y);
							lastTime = currTime;
						}
				}
			}
		}
		
		// zombie attack plant
				try{
					for(int i = 0 ; i < zombie.size();i++)
					{
						for(int j = 0 ; j < pea.size();j++)
						{
							if(zombieRect.get(i).intersects(peaRect.get(j)))
							{
								zombieHealth.get(i).isAttack = true;
								peaHealth.get(j).isDamaged((float)0.05);
								if( peaHealth.get(j).isDead() )
								{
									pea.remove(j);
									peaHealth.remove(j);
									zombieHealth.get(i).isAttack = false;
									zombieHealth.get(i).hp++;
								}
							}
						}
						for(int j = 0 ; j < sun.size();j++)
						{
							if(zombieRect.get(i).intersects(sunRect.get(j)))
							{
								zombieHealth.get(i).isAttack = true;
								sunHealth.get(j).isDamaged((float)0.05);
								if( sunHealth.get(j).isDead() )
								{
									sun.remove(j);
									sunHealth.remove(j);
									zombieHealth.get(i).isAttack = false;
									zombieHealth.get(i).hp++;
								}
							}
						}
					}
				}catch(Exception e){}
		
		// collition
		try{
			for(int i = 0 ; i < bullet.size();i++)
			{
				for(int j = 0 ; j < zombie.size();j++)
				{
					if(bulletRect.get(i).intersects(zombieRect.get(j)))
					{
						bullet.remove(i);
						zombieHealth.get(j).isDamaged();
						if(zombieHealth.get(j).isDead())
						{
							zombie.remove(j);
							zombieHealth.remove(j);
							score+=100;
						}
					}
				}
			}
		}catch(Exception e){}
		
		g2d.setColor(Color.green);
		g2d.drawString(""+score,550 ,30);
	}
	
	public void shoot(int x , int y)
	{
		bullet.add(new Point(x,y));
	}
}
