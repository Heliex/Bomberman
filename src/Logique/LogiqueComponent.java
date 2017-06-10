package Logique;

import java.io.Serializable;

public abstract class LogiqueComponent implements Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -4563773287039899390L;
	private Position p;
	private boolean isDrawable;
	
	public LogiqueComponent()
	{
		this.p = new Position();
		this.isDrawable = false;
	}
	
	public LogiqueComponent(int x, int y)
	{
		this.p = new Position(x,y);
		this.isDrawable = false;
	}
	
	public LogiqueComponent(float f, float g, boolean isDrawable)
	{
		this.p = new Position(f,g);
		this.isDrawable = isDrawable;
	}
	
	public float getX()
	{
		return this.p.getX();
	}
	
	public float getY()
	{
		return this.p.getY();
	}
	
	public boolean isDrawable()
	{
		return this.isDrawable;
	}
	
	public void setX(float x)
	{
		this.p.setX(x);
	}
	
	public void setY(float y)
	{
		this.p.setY(y);
	}
	
	public void setDrawable(boolean drawable)
	{
		this.isDrawable = drawable;
	}
	
	public Position getPosition()
	{
		return this.p;
	}
	
	public void setPosition(Position p)
	{
		this.p = p;
	}

}
