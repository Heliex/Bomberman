
public class Case {

	int x;
	int y;
	String type;
	
	public Case()
	{
		this.x = 0;
		this.y = 0;
	}
	
	public Case(int x , int y,String type)
	{
		this.x = x ;
		this.y = y;
		this.type = type;
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
	
	public String toString()
	{
		return "X : " + this.getX() + " Y :" + this.getY() + " Type de la case : " + this.getType();
	}
	
	public float getRealX()
	{
		return this.x * Game.TAILLE_CASE;
	}
	
	public float getRealY()
	{
		return this.y * Game.TAILLE_CASE;
	}
}
