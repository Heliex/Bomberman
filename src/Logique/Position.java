package Logique;

import java.io.Serializable;

public class Position implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private float x;
	private float y;
	
	public Position(float x, float y )
	{
		this.x = x;
		this.y = y;
	}
	
	public Position()
	{
		this.x = 0;
		this.y = 0;
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
		return this.y ;
	}
	
	public void setY(float y)
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
