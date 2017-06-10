package Logique;



public class Case extends LogiqueComponent {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1416035684381624228L;
	private String type;
	
	public static final int TAILLE_CASE = 32;
	public final static String WALL = "WALL",GROUND= "GROUND", GRASSGROUND = "GRASSGROUND",INDESTRUCTIBLE ="INDESTRUCTIBLE",
			HOUSE = "HOUSE", WOOD = "WOOD", GROUNDGRASSTEXAS="GROUNDGRASSTEXAS", GROUNDTEXAS="GROUNDTEXAS", EMPTY = "EMPTY",DECOR="DECOR" ;
	
	public Case()
	{
		super();
		this.type = "VIDE";
	}
	
	public Case(int x , int y,String type)
	{
		super(x,y);
		this.type = type;
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
		return "X : " + this.getX() + " Y :" + this.getY() + " Type de la case : " + this.getType();
	}
	
	public boolean equals(Case c2)
	{
		return (this.getX() == c2.getX() && this.getY() == c2.getY() && this.getType().equals(c2.getType())) ? true : false;
	}
}
