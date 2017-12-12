import java.util.Observable;

public class AttackCellController extends Observable {
   
	public void setAttackArr(ship s) {
        
        this.setChanged();
        notifyObservers(s);
    }
}
