package Graphique;

import org.newdawn.slick.Animation;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.SpriteSheet;



public class BombeUI {
	
	private Animation animation;
	private SpriteSheet sheet;
	private int duration;
	private boolean looping;
	public static final int LARGEUR_SPRITE = 16, HAUTEUR_SPRITE = 16;
	
	public BombeUI(boolean looping, int duration)
	{
		try {
			this.duration = duration;
			this.sheet = new SpriteSheet("images/Bombe.png",LARGEUR_SPRITE,HAUTEUR_SPRITE);
			this.animation = new Animation();
			this.animation.addFrame(sheet.getSubImage(0,0,LARGEUR_SPRITE,HAUTEUR_SPRITE),duration);
			this.animation.addFrame(sheet.getSubImage(16,0,LARGEUR_SPRITE,HAUTEUR_SPRITE),duration);
			this.animation.addFrame(sheet.getSubImage(32, 0,LARGEUR_SPRITE,HAUTEUR_SPRITE),duration);
			this.animation.setLooping(looping);
		} catch (SlickException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
	public boolean isLooping()
	{
		return this.looping;
	}
	
	public Animation getAnimation()
	{
		return this.animation;
	}
	
	public int getDuration()
	{
		return this.duration;
	}
	
	public void setDuration(int duration)
	{
		this.duration = duration;
	}
	
}
