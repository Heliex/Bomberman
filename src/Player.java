import java.util.LinkedList;

import org.newdawn.slick.Animation;
import org.newdawn.slick.SpriteSheet;

public class Player {
	
	private int numColor;
	SpriteSheet sheet,mort;
	private Animation[] animations = new Animation[8];
	private Animation mortAnim;
	float x, y;
	private LinkedList<Bomb> bomb = new LinkedList<>();
	private boolean canMoveBomb,isDeadDrawable;
	
	public Player(SpriteSheet sheet,int numColor,float x, float y,SpriteSheet mort)
	{
		this.isDeadDrawable = false;
		this.numColor = numColor;
		this.mort = mort;
		this.x = x ;
		this.y = y;
		this.sheet = sheet;
		this.canMoveBomb = false;
		this.animations[0] = loadAnimation(sheet,6,7,numColor);
		this.animations[1] = loadAnimation(sheet,9,10,numColor);
		this.animations[2] = loadAnimation(sheet,0,1,numColor);
		this.animations[3] = loadAnimation(sheet,3,4,numColor);
		this.animations[4] = loadAnimation(sheet,6,8,numColor);
		this.animations[5] = loadAnimation(sheet,9,11,numColor);
		this.animations[6] = loadAnimation(sheet,0,2,numColor);
		this.animations[7] = loadAnimation(sheet,3,5,numColor);
		this.mortAnim = new Animation();
		mortAnim.addFrame(mort.getSprite(0, numColor), 100);
		mortAnim.addFrame(mort.getSprite(1, numColor), 100);
		mortAnim.addFrame(mort.getSprite(2, numColor), 100);
		mortAnim.addFrame(mort.getSprite(3, numColor), 100);
	}
	
	private Animation loadAnimation(SpriteSheet sheet,int startX,int endX , int y)
	{
		Animation animation = new Animation();
		for(int i = startX ; i < endX ; i++)
		{
			animation.addFrame(sheet.getSprite(i, y),100);
		}
		return animation;
	}
	
	public Animation getAnimation(int i)
	{
		return this.animations[i];
	}
	
	
	public Animation[] getAllAnimation()
	{
		return this.animations;
	}
	
	public float getX()
	{
		return this.x;
	}
	
	public void setX(float x)
	{
		this.x = x;
	}
	
	public float getY()
	{
		return this.y;
	}
	
	public void setY(float y)
	{
		this.y = y;
	}
	
	public int getNumColor()
	{
		return this.numColor;
	}
	
	public void setNumColor(int numColor)
	{
		this.numColor = numColor;
	}
	
	public LinkedList<Bomb> getBombe()
	{
		return this.bomb;
	}
	
	public boolean canMoveBomb()
	{
		return this.canMoveBomb;
	}
	
	public void setMoveBomb(boolean canMove)
	{
		this.canMoveBomb = canMove;
	}
	
	public boolean isDeadDrawable()
	{
		return this.isDeadDrawable;
	}
	
	public void setIsDeadDrawable(boolean isDead)
	{
		this.isDeadDrawable = isDead;
	}
	
	public Animation getDead()
	{
		return this.mortAnim;
	}
	
	public void setDead(Animation anim)
	{
		this.mortAnim = anim;
	}
}
