import java.awt.Point;

public class ship {
	
	int addC;
	public SelfCell[] scArr = {null, null, null};
	int hits;
	int startX;

	
	public ship(int x)
	{
		startX = x;
		hits = 0;
		addC = 0;
	}
	
	public void addCell(SelfCell c)
	{
		scArr[addC] = c;
		addC++;
	}
	
	public boolean checkIfPresent(int x, int y)
	{

		for(int i = 0; i < scArr.length; i++)
			if(x == scArr[i].x && y == scArr[i].y)
				return true;
		
		return false;
	}
	//delete
	public void print()
	{

		for(int i = 0; i < scArr.length; i++)
			System.out.println(scArr[i].getX() + " " + scArr[i].getY());
			
		
	}
}
