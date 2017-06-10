package Commande;

import java.io.Serializable;

public class StopperDeplacementJoueur implements Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 8225199584831634674L;
	private int num;
	
	public StopperDeplacementJoueur(int num)
	{
		this.num = num;
	}
	
	public int getNum()
	{
		return this.num;
	}
	
	public void setNum(int num)
	{
		this.num = num;
	}

}
