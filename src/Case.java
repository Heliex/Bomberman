
public class Case {

	int x;
	int y;
	String type;
	boolean hasBonus;
	public Case()
	{
		this.x = 0;
		this.y = 0;
		this.hasBonus = false;
	}
	
	public Case(int x , int y,String type)
	{
		this.x = x ;
		this.y = y;
		this.type = type;
		this.hasBonus = false;
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
	
	public void setType(String type)
	{
		this.type = type;
	}
	
	public String toString()
	{
		return "X : " + this.getX() + " Y :" + this.getY() + " Type de la case : " + this.getType() + " BONUS ? : " + this.hasBonus();
	}
	
	public float getRealX()
	{
		return this.x * Game.TAILLE_CASE;
	}
	
	public float getRealY()
	{
		return this.y * Game.TAILLE_CASE;
	}
	
	public boolean hasBonus()
	{
		return this.hasBonus;
	}
	
	public void setHasbonus(boolean hasBonus)
	{
		this.hasBonus = hasBonus;
	}
	
	public boolean equals(Case c2)
	{
		return (this.x == c2.getX() && this.y == c2.getY()) ? true : false;
	}
}
