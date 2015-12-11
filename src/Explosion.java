import org.newdawn.slick.Animation;
import org.newdawn.slick.SpriteSheet;

public class Explosion {
	
	private float x,y;
	private SpriteSheet sheet;
	private Animation animation;
	private boolean isDrawable ;
	
	public Explosion(float x, float y, SpriteSheet sheet)
	{
		this.isDrawable = false;
		this.x = x;
		this.y = y;
		this.sheet = sheet;
		this.animation = new Animation();
		animation.addFrame(sheet.getSprite(0, 0), 120);
		animation.addFrame(sheet.getSprite(1, 0), 120);
		animation.addFrame(sheet.getSprite(2, 0), 120);
		animation.addFrame(sheet.getSprite(3, 0), 120);
		animation.addFrame(sheet.getSprite(4, 0), 120);
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
	
	public Animation getAnimation()
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
	public void setAnimation(Animation a)
	{
		this.animation = a;
	}
}
