import javax.swing.*;
import javax.swing.plaf.PanelUI;

import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.net.*;

public abstract class BattleGrid extends JPanel {
    public BattleGrid() {
        this.setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        JPanel self = new JPanel();
        self.setLayout(new GridLayout(0,10));

    
               
        for (int i = 0; i < 10; i++)
            for(int j = 0; j < 10; j++) {
            	JPanel cell = getCell(i+j);
                self.add(cell);
            }
        this.add(self);
    }
    
    protected abstract JPanel getCell(int i);
    
    
	}

