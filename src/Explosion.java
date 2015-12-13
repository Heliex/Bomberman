import org.newdawn.slick.Animation;
import org.newdawn.slick.SpriteSheet;

public class Explosion {
	
	private float x,y;
	private SpriteSheet sheet;
	private Animation[] animation = new Animation[9];
	private boolean isDrawable ;
	
	public Explosion(float x, float y, SpriteSheet sheet)
	{
		this.isDrawable = false;
		this.x = x;
		this.y = y;
		this.sheet = sheet;
		animation[0] = loadAnimation(sheet, 4,0, 0);
		animation[1] = loadAnimation(sheet, 4,0, 1);
		animation[2] = loadAnimation(sheet, 4,0, 2);
		animation[3] = loadAnimation(sheet, 4,0, 3);
		animation[4] = loadAnimation(sheet, 4,0, 4);
		animation[5] = loadAnimation(sheet, 4,0, 5);
		animation[6] = loadAnimation(sheet, 4,0, 6);
		animation[7] = loadAnimation(sheet, 4,0, 7);
		animation[8] = loadAnimation(sheet, 4,0, 8);
	}
	
	public float getX()
	{
		return this.x;
	}
	
	public void setX(float x)
	{
		this.x = x ;
	}
	
	public float getY()
	{
		return this.y;
	}
	
	public void setY(float y)
	{
		this.y = y;
	}
	
	public SpriteSheet getSpriteSheet()
	{
		return this.sheet;
	}
	
	public void setSpriteSheet(SpriteSheet sheet)
	{
		this.sheet = sheet;
	}
	
	public Animation[] getAnimation()
	{
		return this.animation;
	}
	
	public boolean isDrawable()
	{
		return this.isDrawable;
	}
	
	public void setDrawable(boolean isDrawable)
	{
		this.isDrawable = isDrawable;
	}
	public void setAnimation(Animation[] a)
	{
		this.animation = a;
	}
	
	private Animation loadAnimation(SpriteSheet sheet,int startX,int endX , int y)
	{
		Animation animation = new Animation();
		for(int i = startX ; i >= endX ; i--)
		{
			animation.addFrame(sheet.getSprite(i, y),100);
		}
		return animation;
	}
	
	public Animation getAnimation(int index)
	{
		return this.animation[index];
	}
}
