import java.awt.Point;


public class Health 
{
	float hp=1;
	Point attachedTo;
	boolean isAttack;
	
	Health(Point pos)
	{
		attachedTo = pos; 
		isAttack = false;
	}
	public void isDamaged()
	{
		hp++;
	}
	public void isDamaged(float index)
	{
		hp+=index;
	}
	public boolean isDead()
	{
		if(hp>10)
			return true;
		else
			return false;
	}
}
