package Commande;

import java.io.Serializable;

public class EffacerBombe implements Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 378291580271272671L;
	private int numClient;
	
	public EffacerBombe(int num)
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