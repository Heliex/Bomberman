import org.newdawn.slick.Animation;
import org.newdawn.slick.SpriteSheet;

public class Player {
	
	//private int numColor;
	SpriteSheet sheet;
	private Animation[] animations = new Animation[8];
	float x, y;
	
	public Player(SpriteSheet sheet,int numColor,float x, float y)
	{
		//Switch(numColor)
		this.x = x ;
		this.y = y;
		this.sheet = sheet;
		this.animations[0] = loadAnimation(sheet,0,1,0);
		this.animations[1] = loadAnimation(sheet,8,9,0);
		this.animations[2] = loadAnimation(sheet,6,7,0);
		this.animations[3] = loadAnimation(sheet,3,4,0);
		this.animations[4] = loadAnimation(sheet,0,2,0);
		this.animations[5] = loadAnimation(sheet,8,10,0);
		this.animations[6] = loadAnimation(sheet,6,8,0);
		this.animations[7] = loadAnimation(sheet,3,5,0);
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
}
