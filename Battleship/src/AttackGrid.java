import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.net.*;

/**
Represents the player's own grid
*/
public class AttackGrid extends BattleGrid {
    public AttackGrid(String name) {
        super();
        
    }

    @Override
    protected JPanel getCell(int i)
    {
        JPanel panel = new JPanel();
        panel.setBackground(Color.white);
        panel.setBorder(BorderFactory.createLineBorder(Color.red, 5));
        panel.setPreferredSize(new Dimension(20, 20)); // for demo purposes only
        
        panel.addMouseListener(new AttackCellListner(panel, i));
        return panel;
    }
    
    protected class AttackCellListner implements MouseListener  {

    	private int n;
    	JPanel cell;
    	public AttackCellListner(JPanel c, int i)
    	{
    		n = i;
    		cell = c;
    	}
    	@Override
    	public void mouseClicked(MouseEvent e) {
    		
    		System.out.println("clicked!: " + n);
    		cell.setBackground(Color.green); //placeholder color to indicate feedback
    		cell.repaint();

    	}
    	

    	@Override
    	public void mouseEntered(MouseEvent e) {
    		// TODO Auto-generated method stub
    		
    	}

    	@Override
    	public void mouseExited(MouseEvent e) {
    		// TODO Auto-generated method stub
    		
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
}