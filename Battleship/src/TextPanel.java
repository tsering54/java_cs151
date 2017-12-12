import javax.swing.*;
import java.awt.*;


public class TextPanel extends JPanel {

    private JTextArea textArea;

    public TextPanel() {
        textArea = new JTextArea();

        setLayout(new BorderLayout());

        add(new JScrollPane(textArea), BorderLayout.CENTER);
    }

    public void appendText(String text) {
        textArea.append(text);
    }
    
    public void setDigit(String text) {
    	if(text.length() == 1)
    		textArea.replaceRange(text, textArea.getText().length() - 1, textArea.getText().length());
    	else
    		textArea.replaceRange(text, textArea.getText().length() - 2, textArea.getText().length());

    }
}

