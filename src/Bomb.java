import org.lwjgl.Sys;
import org.newdawn.slick.Animation;
import org.newdawn.slick.SpriteSheet;

public class Bomb {
	
	private Animation animation;
	private SpriteSheet sheet;
	private float xBomb,yBomb;
	private long firstTime;
	private Explosion explosion;
	
	public Bomb(SpriteSheet sheet,float x, float y,Explosion explosion)
	{
		this.explosion = explosion;
		firstTime = getTime();
		this.xBomb = x;
		this.yBomb = y;
		this.sheet = sheet;
		this.animation = new Animation();
		animation.addFrame(sheet.getSprite(0, 0),120);
		animation.addFrame(sheet.getSprite(1, 0),120);
		animation.addFrame(sheet.getSprite(2, 0), 120);
	}
	
	public Animation getAnimation()
	{
		return this.animation;
	}
	
	public void setAnimation(Animation a)
	{
		this.animation = a;
	}
	
	public SpriteSheet getSheet()
	{
		return this.sheet;
	}
	
	public void setSheet(SpriteSheet sheet)
	{
		this.sheet = sheet;
	}
	
	public float getXBomb()
	{
		return this.xBomb;
	}
	
	public void setXBomb(float x)
	{
		this.xBomb = x;
	}
	
	public float getYBomb()
	{
		return this.yBomb;
	}
	
	public void setYBomb(float y)
	{
		this.yBomb = y;
	}
	
	public static long getTime()
	{
		return (Sys.getTime() * 1000) / Sys.getTimerResolution();
	}
	
	public long getFirstTime()
	{
		return this.firstTime;
	}
	
	public String toString()
	{
		return "XBomb : " + this.getXBomb() + " YBomb : " + this.getYBomb() ;
	}
	
	public Explosion getExplosion()
	{
		return this.explosion;
	}
}
