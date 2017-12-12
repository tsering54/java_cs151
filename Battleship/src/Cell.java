import java.awt.Color;
import java.util.Observer;

import javax.swing.JPanel;

abstract class Cell extends JPanel implements Observer {
    public int x;
    public int y;
    ship s;
    Control con;
    Color current;

}
