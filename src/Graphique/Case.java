package Graphique;
import java.io.Serializable;

import MainGame.Game;

public class Case implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = -3332305466971179346L;
	int x;
	int y;
	String type;
	private boolean hasBonus;
	boolean hasBombe;
	public Case()
	{
		this.x = 0;
		this.y = 0;
		this.setHasBonus(false);
		this.hasBombe = false;
	}
	
	public Case(int x , int y,String type)
	{
		this.x = x ;
		this.y = y;
		this.type = type;
		this.setHasBonus(false);
	}
	
	public int getX()
	{
		return this.x;
	}
	
	public void setX(int x)
	{
		this.x = x ;
	}
	
	public int getY()
	{
		return this.y;
	}
	
	public void setY(int y)
	{
		this.y = y ;
	}
	
	public String getType()
	{
		return type;
	}
	
	public void setType(String type)
	{
		this.type = type;
	}
	
	public String toString()
	{
		return "X : " + this.getX() + " Y :" + this.getY() + " Type de la case : " + this.getType() + " BONUS ? : " + this.hasBonus() + " Bombe posée ? : " + this.hasBombe();
	}
	
	public float getRealX()
	{
		return this.x * Game.TAILLE_CASE;
	}
	
	public float getRealY()
	{
		return this.y * Game.TAILLE_CASE;
	}
	
	public boolean hasBonus()
	{
		return this.isHasBonus();
	}
	
	public void setHasbonus(boolean hasBonus)
	{
		this.setHasBonus(hasBonus);
	}
	
	public boolean hasBombe()
	{
		return this.hasBombe;
	}
	
	public void setHasBombe(boolean hasBombe)
	{
		this.hasBombe = hasBombe;
	}
	public boolean equals(Case c2)
	{
		return (this.x == c2.getX() && this.y == c2.getY()) ? true : false;
	}

	public boolean isHasBonus() {
		return hasBonus;
	}

	public void setHasBonus(boolean hasBonus) {
		this.hasBonus = hasBonus;
	}
}
