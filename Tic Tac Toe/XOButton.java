import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class XOButton extends JButton implements ActionListener {
    private int buttonID;
    public static int lastMove;
    private boolean isMarked = false;
    public static boolean isHumansTurn = true;


    public XOButton(int buttonID) {
        this.buttonID = buttonID;
        setVisible(true);
        this.addActionListener(this);
    }

    public void actionPerformed(ActionEvent e) {
        lastMove = buttonID;
        setImage();
    }

    public void setImage() {
        if(!isMarked) {
            ImageIcon X = new ImageIcon(this.getClass().getResource("X.png"));
            ImageIcon O = new ImageIcon(this.getClass().getResource("O.png"));
            if (isHumansTurn) {
                setIcon(X);
            } else {
                setIcon(O);
            }
            isMarked = true;
            isHumansTurn = !isHumansTurn;
        }
    }

    public void reset() {
        setEnabled(true);
        setVisible(true);
        setIcon(null);
        isMarked = false;
    }
}
