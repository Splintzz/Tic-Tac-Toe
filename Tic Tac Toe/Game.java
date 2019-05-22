import java.awt.*;
import javax.swing.*;

public class Game extends JFrame{
    private Board gameBoard;
    private XOButton[] buttons;
    private final int N = 3;

    public Game() {
        super("Tic Tac Toe");
        gameBoard = new Board();
        buttons = new XOButton[N * N];
    }

    public void play() {
        setUp();
        while (true) {
            if(XOButton.isHumansTurn) {
                waitForUser();
            }else {
                gameBoard.makeMove(getRowOfLastMove(XOButton.lastMove), getColumnOfLastMove(XOButton.lastMove));
                if(!gameBoard.isTerminal()) {
                    gameBoard = gameBoard.search();
                    final int move = ((N * gameBoard.getLastRowMove()) + gameBoard.getLastColumnMove());
                    buttons[move].setImage();
                }
            }
            if(gameBoard.isTerminal()) {
                endGame();
            }
        }
    }

    private void setUp() {
        setVisible(true);
        setSize(500, 500);
        setResizable(true);

        JPanel board = new JPanel();
        board.setVisible(true);
        board.setLayout(new GridLayout(3, 3));
        add(board);

        for (int button = 0; button < (N * N); ++button) {
            buttons[button] = new XOButton(button);
            board.add(buttons[button]);
        }
    }

    private void endGame() {
        for (XOButton button : buttons) {
            button.setEnabled(false);
        }

        expressResult();
        reset();
    }

    private void reset() {
        for (XOButton button : buttons) {
            button.reset();
        }

        XOButton.isHumansTurn = true;
        XOButton.lastMove = -1;
        gameBoard = new Board();
    }

    private void expressResult() {
        final int CATS_GAME = 0, PLAYER_ONE_WIN = 1;

        if (gameBoard.getEvaluation() == CATS_GAME) {
            JOptionPane.showMessageDialog(null, "Cats Game", "Result", JOptionPane.INFORMATION_MESSAGE);
        } else if (gameBoard.getEvaluation() == PLAYER_ONE_WIN) {
            JOptionPane.showMessageDialog(null, "Human Wins", "Result", JOptionPane.INFORMATION_MESSAGE);
        } else {
            JOptionPane.showMessageDialog(null, "Computer Wins", "Result", JOptionPane.INFORMATION_MESSAGE);
        }
    }

    private void waitForUser() {
        System.out.print("");
    }

    private int getRowOfLastMove(int lastMove) {
        final int FIRST_ROW_CUTOFF = 3;
        final int SECOND_ROW_CUTOFF = 6;

        if(lastMove < FIRST_ROW_CUTOFF) {
            return 0;
        }else if(lastMove < SECOND_ROW_CUTOFF) {
            return 1;
        }else {
            return 2;
        }
    }

    private int getColumnOfLastMove(int lastMove) {
        final int FIRST_COLUMN_CUTOFF = 0;
        final int SECOND_COLUMN_CUTOFF = 1;

        if(lastMove%N == FIRST_COLUMN_CUTOFF) {
            return 0;
        }else if(lastMove%N == SECOND_COLUMN_CUTOFF) {
            return 1;
        }else {
            return 2;
        }
    }
}