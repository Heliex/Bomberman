package Commande;

import java.io.Serializable;

public class DeplacerJoueur implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = -851253316355382515L;
	
	private int num;
	private int direction;
	public DeplacerJoueur(int num,int direction)
	{
		this.num = num;
		this.direction = direction;
	}
	
	
	public int getNum()
	{
		return this.num;
	}
	
	public void setNum(int num)
	{
		this.num = num;
	}
	
	public int getDirection()
	{
		return this.direction;
	}
	
	public void setDirection(int direction)
	{
		this.direction = direction;
	}
	
}
