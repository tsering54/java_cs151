import javax.swing.*;
import java.awt.*;

/**
 Represents the player's own grid
 */
public class AttackGrid extends BattleGrid {
	

	   
    public AttackGrid(String name, Control c) {

        super(c);
        this.add(c.scorePanel);
        
    }

    @Override
    protected JPanel getCell(int i, int j, Control c) {

        JPanel panel = new AttackCell(i, j, c);
        panel.setBackground(Color.white);
        panel.setBorder(BorderFactory.createLineBorder(Color.red, 1));

        panel.setPreferredSize(new Dimension(50, 50)); // for demo purposes only

        return panel;
    }


}
