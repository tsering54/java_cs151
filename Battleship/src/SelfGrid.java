import javax.swing.*;
import java.awt.*;

/**
 Represents the player's own grid
 */
public class SelfGrid extends BattleGrid {

    public SelfGrid(String name, Control c) {
        super(c);

    }

    @Override
    protected JPanel getCell(int i, int j, Control c)
    {

        SelfCell panel = new SelfCell(i, j, c);
        panel.setBackground(Color.black);
        panel.setBorder(BorderFactory.createLineBorder(Color.blue, 1));
        // for demo purposes only

        return panel;
    }




}
