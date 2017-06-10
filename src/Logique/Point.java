package Logique;

public class Point {
	
	private float x;
	private float y;
	
	public Point()
	{
		this.x = 0;
		this.y = 0;
	}
	
	public Point(float x , float y)
	{
		this.x = x;
		this.y = y;
	}
	
	public float getX()
	{
		return this.x;
	}
	
	public void setX(int x)
	{
		this.x = x;
	}
	
	public float getY()
	{
		return this.y;
	}
	
	public void setY(int y)
	{
		this.y = y;
	}
	
	public String toString()
	{
		StringBuilder sb = new StringBuilder();
		sb.append(" X : " + x + "  Y : " + y + "\n");
		return sb.toString();
	}

}
