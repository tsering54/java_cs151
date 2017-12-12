import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.Observable;
import java.util.Observer;

public class AttackCell extends Cell{


    
    TextPanel scorePanel = new TextPanel();


    public AttackCell(int i, int j, Control c) {
        super();

        x = j;
        y = i;
        con = c;
        s = null;

        con.acc.addObserver(this);
        
        this.addMouseListener(new AttackCellListener(this));
    }

    protected class AttackCellListener implements MouseListener {

        private int n;
        AttackCell cell;


        public AttackCellListener(AttackCell c) {
            current = Color.white;
            cell = c;
        }


        @Override
        public void mouseClicked(MouseEvent e) {
        	if(con.canAttack)
        	{
        		con.canAttack = false;
        		con.attacks++;
        		con.attackPanel.setDigit("" + con.attacks);
            
        		if (con.attackArr[x][y] == 1) {
        			current = Color.green;
        			con.hitCount++;
                
        			con.hitPanel.setDigit(" " + con.hitCount);
                
        			cell.s.hits++;
                
        			con.attackArr[x][y] = 3;
                
        			if (con.hitCount == 15)
                    	con.scorePanel.appendText("winner");


                	if (cell.s.hits == 3)
                	{
                		con.shipsSunk++;
                		con.shipsSunkPanel.setDigit("" + con.shipsSunk);                	
                		con.gms.setOpponetsShipsSunk();
                	}

        		} 
        		else
        		{
        			con.attackArr[x][y] = 2;
        			current = Color.red;
        		}

            cell.setBackground(current);
        	}
        }


        @Override
        public void mouseEntered(MouseEvent e) {
            if (current != Color.green)
                cell.setBackground(Color.cyan);

        }

        @Override
        public void mouseExited(MouseEvent e) {

            cell.setBackground(current);

        }

        @Override
        public void mousePressed(MouseEvent e) {
            // TODO Auto-generated method stub

        }

        @Override
        public void mouseReleased(MouseEvent e) {
            // TODO Auto-generated method stub

        }

    }

	@Override
	public void update(Observable arg0, Object arg1) {

		if(((ship)arg1).checkIfPresent(x, y))
		{
			s = (ship)arg1;

			
		}
	}


}