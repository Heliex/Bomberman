package Logique;

import java.io.Serializable;

public class Position implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private int x;
	private int y;
	
	public Position(int x, int y )
	{
		this.x = x;
		this.y = y;
	}
	
	public Position()
	{
		this.x = 0;
		this.y = 0;
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
		StringBuilder sb = new StringBuilder();
		sb.append(" X : " + getX() + " Y " + getY() + "\n");
		return sb.toString();
	}
}
