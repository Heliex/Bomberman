package Graphique;
import java.io.Serializable;
import java.util.LinkedList;

import org.newdawn.slick.Animation;
import org.newdawn.slick.SpriteSheet;
import org.newdawn.slick.geom.Rectangle;

import MainGame.Game;

public class Player implements Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 357065401577665717L;
	private int numColor;
	transient SpriteSheet sheet,mort;
	transient private Animation[] animations = new Animation[8];
	transient private Animation mortAnim;
	float x, y;
	transient private LinkedList<Bomb> bomb = new LinkedList<>();
	private boolean canMoveBomb,isDeadDrawable,canPushPlayer,isDrawable;
	
	public Player(SpriteSheet sheet,int numColor,float x, float y,SpriteSheet mort)
	{
		this.isDrawable = false;
		this.isDeadDrawable = false;
		this.canPushPlayer = false;
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
	
	public Player()
	{
		this.x = 0;
		this.y = 0;
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
	
	public boolean canPushPlayer()
	{
		return this.canPushPlayer;
	}
	
	public void setPushPlayer(boolean canPush)
	{
		this.canPushPlayer = canPush;
	}
	
	public boolean isOnBomb()
	{
		for(int i = 0 ; i < bomb.size() ; i++)
		{
			if(bomb.get(i) != null)
			{
				if(x + Game.TAILLE_CASE/2>= bomb.get(i).getXBomb() && x+ Game.TAILLE_CASE/2 <= bomb.get(i).getXBomb() + Game.TAILLE_CASE && y+ Game.TAILLE_CASE/2 >= bomb.get(i).getYBomb() && y+ Game.TAILLE_CASE/2 <= bomb.get(i).getYBomb() + Game.TAILLE_CASE)
				{
					return true;
				}
			}
		}
		return false;
	}
	
	public Bomb getBombFromCoord(float x, float y)
	{
		for(int i = 0 ; i < bomb.size() ; i++)
		{
			if(bomb.get(i) != null)
			{
				if(x + Game.TAILLE_CASE/2>= bomb.get(i).getXBomb() && x+ Game.TAILLE_CASE/2 <= bomb.get(i).getXBomb() + Game.TAILLE_CASE && y+ Game.TAILLE_CASE/2 >= bomb.get(i).getYBomb() && y+ Game.TAILLE_CASE/2 <= bomb.get(i).getYBomb() + Game.TAILLE_CASE)
				{
					return bomb.get(i);
				}
			}
		}
		return null;
	}
	
	// Check if the player is in explosion of it own bomb
	public boolean isInExplosion()
	{
		if(bomb != null)
		{
			for(Bomb b : bomb)
			{
				if(b != null)
				{
					Explosion e = b.getExplosion();
					if(e != null && e.isDrawable())
					{
						float xGauche = e.getX() - Game.TAILLE_BOMB * Game.TAILLE_EXPLOSION;
						Rectangle rectangleHorizontal = new Rectangle(xGauche,e.getY(),(Game.TAILLE_BOMB * Game.TAILLE_EXPLOSION)*2 + Game.TAILLE_BOMB + Game.TAILLE_EXPLOSION,Game.TAILLE_BOMB);
						float yHaut = e.getY() - Game.TAILLE_BOMB * Game.TAILLE_EXPLOSION;
						Rectangle rectangleVertical = new Rectangle(e.getX(),yHaut,Game.TAILLE_BOMB,(Game.TAILLE_BOMB * Game.TAILLE_EXPLOSION)*2 + Game.TAILLE_BOMB + Game.TAILLE_EXPLOSION);
						if(rectangleHorizontal.contains(x+Game.TAILLE_CASE/2,y+Game.TAILLE_CASE/2) || rectangleVertical.contains(x + Game.TAILLE_CASE/2, y + Game.TAILLE_CASE/2))
						{
							return true;
						}
	 				}
				}
			}
		}
		return false;
	}
	
	// Check if the player is on explosion of an other player
	public boolean isInExplosion(Player p)
	{
		for(Bomb b : p.getBombe())
		{
			if(b != null)
			{
				Explosion e = b.getExplosion();
				if(e != null && e.isDrawable())
				{
					float xGauche = e.getX() - Game.TAILLE_BOMB * Game.TAILLE_EXPLOSION;
					Rectangle rectangleHorizontal = new Rectangle(xGauche,e.getY(),(Game.TAILLE_BOMB * Game.TAILLE_EXPLOSION)*2 + Game.TAILLE_BOMB + Game.TAILLE_EXPLOSION,Game.TAILLE_BOMB);
					float yHaut = e.getY() - Game.TAILLE_BOMB * Game.TAILLE_EXPLOSION;
					Rectangle rectangleVertical = new Rectangle(e.getX(),yHaut,Game.TAILLE_BOMB,(Game.TAILLE_BOMB * Game.TAILLE_EXPLOSION)*2 + Game.TAILLE_BOMB + Game.TAILLE_EXPLOSION);
					if(rectangleHorizontal.contains(x+Game.TAILLE_CASE/2,y+Game.TAILLE_CASE/2) || rectangleVertical.contains(x + Game.TAILLE_CASE/2,y + Game.TAILLE_CASE/2))
					{
						return true;
					}
 				}
			}
		}
		return false;
	}
	
	public String toString()
	{
		return "Position du joueur : X " + x + " Y : " + y ;
	}
	
	public void setDrawable(boolean drawable)
	{
		this.isDrawable = drawable;
	}
	
	public boolean isDrawable()
	{
		return this.isDrawable;
	}
}
