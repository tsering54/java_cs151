import java.util.Observable;

public class SelfCellController extends Observable {

	public void setSelfCells(ship s) {
        
        this.setChanged();
        notifyObservers(s);
    }
	
    public void reflectAttacks()
    {

            this.setChanged();
  
            notifyObservers();
 
    }
}
