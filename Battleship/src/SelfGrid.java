import javax.swing.*;

import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.net.*;

/**
Represents the player's own grid
*/
public class SelfGrid extends BattleGrid {
    public SelfGrid(String name) {
        super();
        
    }

    @Override
    protected JPanel getCell(int i)
    {
        JPanel panel = new JPanel();
        panel.setBackground(Color.black);
        panel.setBorder(BorderFactory.createLineBorder(Color.blue, 5));
        panel.setPreferredSize(new Dimension(20, 20)); // for demo purposes only
        panel.addMouseListener(new SelfCellListner(panel, i));
        return panel;
    }
    
    protected class SelfCellListner implements MouseListener  {

    	private int n;
    	JPanel cell;
    	public SelfCellListner(JPanel c, int i)
    	{
    		n = i;
    		cell = c;
    	}
    	@Override
    	public void mouseClicked(MouseEvent e) {
    		
    		System.out.println("clicked!: " + n);
    		cell.setBackground(Color.green);
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


