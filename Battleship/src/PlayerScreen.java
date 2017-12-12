import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;


public class PlayerScreen extends JFrame implements GameState {
    GameStateManager gms;
    Control con;


    public PlayerScreen(String name, boolean show, GameStateManager manager, Control c) {


        super(name);
        gms = manager;
        con = c;
        this.setLayout(new BorderLayout());
        SelfGrid sg = new SelfGrid(name,con);


        this.add(sg, BorderLayout.EAST);
        this.add(new AttackGrid(name, con), BorderLayout.WEST);
        this.add(new JLabel(name), BorderLayout.NORTH);
        JButton next = new JButton("next");
        next.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
            	if(con.nships == 5 )
            		gms.toggleScreen();
            }
        });
        this.add(next, BorderLayout.CENTER);

        TextPanel panel = new TextPanel();
        panel.setPreferredSize(new Dimension(50, 50));
        this.add(panel, BorderLayout.SOUTH);
        

        this.pack();
        this.setVisible(show);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);


    }


    public void hideScreen() {
        this.setVisible(false);

    }

    public void toggleVisability()
    {

        this.setVisible(!this.isVisible());
    }

    public void showScreen() {
        this.setVisible(true);

    }

}