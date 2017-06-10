package Graphique;

import org.newdawn.slick.Animation;
import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.SpriteSheet;

import Logique.Case;
import Logique.Player;

public class PlayerUI {
	
	private Player player;
	private int duration;
	private SpriteSheet sheet;
	private Animation[] animation = new Animation[4];
	private static final int  UP = Player.UP, LEFT = Player.LEFT, DOWN = Player.DOWN, RIGHT = Player.RIGHT;
	private static final int HAUTEUR_SPRITE = 32, LARGEUR_SPRITE = 18;
	private Image currentDirection = null;
	
	public PlayerUI(Player p,int duration)
	{
		this.player = p;
		this.duration = duration;
		initPlayerAnimation();
		
	}
	
	
	public void initPlayerAnimation()
	{
		try {
			if(player != null)
			{
				this.sheet = new SpriteSheet("images/Deplacements.png",LARGEUR_SPRITE,HAUTEUR_SPRITE);
				
				this.animation[UP] = new Animation();
				this.animation[LEFT] = new Animation();
				this.animation[DOWN] = new Animation();
				this.animation[RIGHT] = new Animation();
				
				// Animation vers le haut du J
				this.animation[UP].addFrame(sheet.getSubImage(108, player.getNumJoueur() * Case.TAILLE_CASE,LARGEUR_SPRITE,HAUTEUR_SPRITE), duration);
				this.animation[UP].addFrame(sheet.getSubImage(126, player.getNumJoueur() * Case.TAILLE_CASE,LARGEUR_SPRITE,HAUTEUR_SPRITE), duration);
				this.animation[UP].addFrame(sheet.getSubImage(144, player.getNumJoueur() * Case.TAILLE_CASE,LARGEUR_SPRITE,HAUTEUR_SPRITE),duration);
				
				// Animation vers la gauche du J
				this.animation[LEFT].addFrame(sheet.getSubImage(162, player.getNumJoueur() * Case.TAILLE_CASE,LARGEUR_SPRITE,HAUTEUR_SPRITE), duration);
				this.animation[LEFT].addFrame(sheet.getSubImage(180, player.getNumJoueur() * Case.TAILLE_CASE,LARGEUR_SPRITE,HAUTEUR_SPRITE), duration);
				this.animation[LEFT].addFrame(sheet.getSubImage(198, player.getNumJoueur() * Case.TAILLE_CASE,LARGEUR_SPRITE,HAUTEUR_SPRITE),duration);
				
				// Animation vers le bas du J
				this.animation[DOWN].addFrame(sheet.getSubImage(0,  player.getNumJoueur() * Case.TAILLE_CASE,LARGEUR_SPRITE,HAUTEUR_SPRITE), duration);
				this.animation[DOWN].addFrame(sheet.getSubImage(18, player.getNumJoueur() * Case.TAILLE_CASE,LARGEUR_SPRITE,HAUTEUR_SPRITE), duration);
				this.animation[DOWN].addFrame(sheet.getSubImage(36, player.getNumJoueur() * Case.TAILLE_CASE,LARGEUR_SPRITE,HAUTEUR_SPRITE),duration);
				
				// Animation vers la droite du J
				this.animation[RIGHT].addFrame(sheet.getSubImage(54, player.getNumJoueur() * Case.TAILLE_CASE,LARGEUR_SPRITE,HAUTEUR_SPRITE), duration);
				this.animation[RIGHT].addFrame(sheet.getSubImage(72, player.getNumJoueur() * Case.TAILLE_CASE,LARGEUR_SPRITE,HAUTEUR_SPRITE), duration);
				this.animation[RIGHT].addFrame(sheet.getSubImage(90, player.getNumJoueur() * Case.TAILLE_CASE,LARGEUR_SPRITE,HAUTEUR_SPRITE),duration);
				this.currentDirection = this.animation[player.getDirection()].getImage(this.animation[player.getDirection()].getFrame());
			}
			
		} catch (SlickException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	public Animation getAnimation(int direction)
	{
		return this.animation[direction];
	}
	
	public Animation getRightAnimation()
	{
		return this.animation[RIGHT];
	}
	
	public Animation getLeftAnimation()
	{
		return this.animation[LEFT];
	}
	
	public Animation getUpAnimation()
	{
		return this.animation[UP];
	}
	
	public Animation getDownAnimation()
	{
		return this.animation[DOWN];
	}
	
	public int getDuration()
	{
		return this.duration;
	}
	
	public Player getPlayer()
	{
		return this.player;
	}
	
	public void setPlayer(Player p)
	{
		this.player = p;
	}
	
	public Image getCurrentDirection()
	{
		return this.animation[player.getDirection()].getCurrentFrame();
	}
	
	public void setCurrentDirection(int direction)
	{
		if(currentDirection != null && animation[direction] != null)
		{
			this.currentDirection = this.animation[direction].getImage(this.animation[direction].getFrame());
		}
	}
}
