import javax.swing.*;
import java.awt.*;

public abstract class BattleGrid extends JPanel {
    public BattleGrid(Control c) {
        this.setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        JPanel self = new JPanel();
        self.setLayout(new GridLayout(0,10));
        con = c;

        for (int i = 0; i < 10; i++)
            for(int j = 0; j < 10; j++) {
                JPanel cell = getCell(i,j,con);
                self.add(cell);
            }

        this.add(self);

    }

    Control con;
    protected abstract JPanel getCell(int i, int j, Control c);
}

