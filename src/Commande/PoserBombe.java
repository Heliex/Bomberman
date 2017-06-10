package Commande;

import java.io.Serializable;

public class PoserBombe implements Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 378291580271272671L;
	private int numClient;
	
	public PoserBombe(int num)
	{
		this.numClient = num;
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
