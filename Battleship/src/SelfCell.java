import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.Observable;
import java.util.Observer;

public class SelfCell extends Cell {



    public SelfCell(int i, int j, Control c)
    {
        super();

        this.setPreferredSize(new Dimension(50,50));
        x = j;
        y = i;
        con = c;
        s = null;
        current = Color.black;
        con.scc.addObserver(this);

        this.addMouseListener(new SelfCellListner(this));

    }

    protected class SelfCellListner implements MouseListener  {

        private int n;
        SelfCell cell;


        public SelfCellListner(SelfCell c)
        {

            cell = c;
        }

        @Override
        public void mouseClicked(MouseEvent e) {
        		con.placeShip(x, y);
        }


        @Override
        public void mouseEntered(MouseEvent e) {
        }

        @Override
        public void mouseExited(MouseEvent e) {


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
        if(con.getSelfArrAtIndex(x, y) == 1)
        {
        	current = Color.yellow;
            this.setBackground(current);
            if(s == null)
            {
 
            	s = ((ship)arg1);
            	s.addCell(this);

            }
        }
        

        if(con.getSelfArrAtIndex(x, y) == 2)
        {
        	current = Color.red;
            this.setBackground(current);   	
        }

        if(con.getSelfArrAtIndex(x, y) == 3)
        {
        	current = Color.green;
            this.setBackground(current);   	
        }        
    }
}
