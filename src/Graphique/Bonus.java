package Graphique;
import java.io.Serializable;

import org.newdawn.slick.Image;

public class Bonus implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 2494919354680379842L;
	private String nom;
	private Image image;
	private boolean isDrawable;
	private int x,y;
	private long timer;
	public Bonus(String nom, Image image)
	{
		this.timer = Bomb.getTime();
		this.nom = nom;
		this.image = image;
		this.x = 0;
		this.y = 0;
		this.isDrawable = false;
	}
	
	public String getNom()
	{
		return this.nom;
	}
	
	public void setNom(String nom)
	{
		this.nom = nom;
	}
	
	public Image getImage()
	{
		return this.image;
	}
	
	public void setImage(Image image)
	{
		this.image = image;
	}
	
	public boolean getDrawable()
	{
		return this.isDrawable;
	}
	
	public void setDrawable(boolean drawable)
	{
		this.isDrawable = drawable;
	}
	
	public int getX()
	{
		return this.x;
	}
	
	public void setX(int x)
	{
		this.x = x;
	}
	
	public int getY()
	{
		return this.y ;
	}
	
	public void setY(int y)
	{
		this.y = y;
	}
	
	public String toString()
	{
		return "X : " + x + " Y : " + y + " Nom : " + nom ;
	}
	
	public long getTimer()
	{
		return this.timer;
	}
	
	public void setTimer(long timer)
	{
		this.timer = timer;
	}
}
