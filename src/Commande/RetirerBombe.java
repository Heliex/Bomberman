package Commande;

import java.io.Serializable;

public class RetirerBombe implements Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 2155786143610478318L;
	private int numClient;
	
	public RetirerBombe(int numClient)
	{
		this.numClient = numClient;
	}
	
	public int getNum()
	{
		return this.numClient;
	}
	
	public void setNum(int num)
	{
		this.numClient = num;
	}

}
