public class Board {
    private final int N = 3;
    private final int NEGATIVE_INFINITY = -100;
    private final int POSITIVE_INFINITY = +100;
    private final int MAX_NUMBER_OF_MOVES_THAT_CAN_BE_MADE = 9;

    private final char PLAYER_ONE = 'X';
    private final char PLAYER_TWO = 'O';
    private final char EMPTY = '-';

    private int stateValue, movesMade, lastRowMove, lastColumnMove;
    private boolean playerOneTurn;
    private char[][] board;

    public Board() {
        movesMade = 0;
        playerOneTurn = true;
        board = new char[N][N];
        for (int row = 0; row < N; ++row) {
            for (int column = 0; column < N; ++column) {
                board[row][column] = EMPTY;
            }
        }
    }

    private Board(Board board) {
        this.board = new char[N][N];
        for (int row = 0; row < N; ++row) {
            for (int column = 0; column < N; ++column) {
                this.board[row][column] = board.board[row][column];
            }
        }
       this.movesMade = board.movesMade;
       this.playerOneTurn = board.playerOneTurn;
    }

    public void makeMove(int row, int column) {
        board[row][column] = playerOneTurn ? PLAYER_ONE : PLAYER_TWO;
        playerOneTurn = !playerOneTurn;
        ++movesMade;
        lastRowMove = row;
        lastColumnMove = column;
    }

    public boolean isTerminal() {
        return ((movesMade == MAX_NUMBER_OF_MOVES_THAT_CAN_BE_MADE) || threeInARow());
    }

    public int getEvaluation() {
        final int PLAYER_ONE_VICTORY = -1, PLAYER_TWO_VICTORY = 1, CATS_GAME = 0;
        if(threeInARow()) {
            if (playerOneTurn) {
                return PLAYER_ONE_VICTORY;
            }else {
                return PLAYER_TWO_VICTORY;
            }
        }
        return CATS_GAME;
    }

    private boolean threeInARow() {
        return (threeInARowDiagonally() || threeInARowHorizontally() || threeInARowVertically());
    }

    private boolean threeInARowDiagonally() {
        final char MIDDLE_SQUARE = board[1][1];
        final char TOP_RIGHT_SQUARE = board[0][2];
        final char TOP_LEFT_SQUARE = board[0][0];
        final char BOTTOM_LEFT_SQUARE = board[2][0];
        final char BOTTOM_RIGHT_SQUARE = board[2][2];

        return ((MIDDLE_SQUARE != EMPTY) &&
                ((MIDDLE_SQUARE == TOP_RIGHT_SQUARE && MIDDLE_SQUARE == BOTTOM_LEFT_SQUARE) ||
                (MIDDLE_SQUARE == TOP_LEFT_SQUARE && MIDDLE_SQUARE == BOTTOM_RIGHT_SQUARE)));
    }

    private boolean threeInARowHorizontally() {
        int consecutiveCount = 1;
        final int THREE_IN_A_ROW = 3;

        for (int row = 0; row < N; ++row) {
            for (int column = 0; column < (N-1); ++column) {
                if(board[row][column] != EMPTY && board[row][column] == board[row][column+1]) {
                    ++consecutiveCount;
                }
            }
            if(consecutiveCount == THREE_IN_A_ROW) {
                return true;
            }
            consecutiveCount = 1;
        }
        return false;
    }

    private boolean threeInARowVertically() {
        int consecutiveCount = 1;
        final int THREE_IN_A_ROW = 3;

        for (int column = 0; column < N; ++column) {
            for (int row = 0; row < (N-1); ++row) {
                if(board[row][column] != EMPTY && board[row][column] == board[row+1][column]) {
                    ++consecutiveCount;
                }
            }
            if(consecutiveCount == THREE_IN_A_ROW) {
                return true;
            }
            consecutiveCount = 1;
        }
        return false;
    }

    private int getNumberOfAvailableMoves() {
        return (MAX_NUMBER_OF_MOVES_THAT_CAN_BE_MADE - movesMade);
    }

    private boolean moveIsPossible(int row, int column) {
        return (board[row][column] == EMPTY);
    }

    private Board[] getNextStates() {
        final int NUMBER_OF_MOVES = getNumberOfAvailableMoves();
        Board[] moves = new Board[NUMBER_OF_MOVES];

        for (int newMove = 0, row = 0, column = 0; newMove < NUMBER_OF_MOVES; ++newMove) {
            while(!moveIsPossible(row, column)) {
                ++column;
                if(column == N) {
                    column = 0;
                    ++row;
                }
            }//find a move that is possible to make
            moves[newMove] = new Board(this);
            moves[newMove].makeMove(row, column);
            ++column;
            if(column == N) {
                column = 0;
                ++row;
            }
            //put the state with the board in the array, then make the move on the board
        }
        return moves;
    }

    public int getLastRowMove() {
        return lastRowMove;
    }

    public int getLastColumnMove() {
        return lastColumnMove;
    }

    public Board search(){
        return search(this, NEGATIVE_INFINITY, POSITIVE_INFINITY);
    }

    private Board search(Board board, int alpha, int beta) {
        Board[] moves = board.getNextStates();
        Board least = moves[0];
        for(Board child : moves){
            child.stateValue = (maxValue(child, alpha, beta));
            if(child.stateValue < least.stateValue) {
                least = child;
            }
        }
        return least;
    }

    private int maxValue(Board board, int alpha, int beta) {
        if(board.isTerminal()) {
            return board.getEvaluation();
        }

        board.stateValue = (NEGATIVE_INFINITY);
        Board[] moves = board.getNextStates();

        for(Board move : moves) {
            board.stateValue = (Math.max(board.stateValue, minValue(move, alpha, beta)));
            if(board.stateValue >= beta) {
                return board.stateValue;
            }
            alpha = Math.max(alpha, board.stateValue);
        }
        return board.stateValue;
    }

    private int minValue(Board board, int alpha, int beta) {
        if(board.isTerminal()) {
            return board.getEvaluation();
        }

        board.stateValue = (POSITIVE_INFINITY);
        Board[] moves = board.getNextStates();

        for(Board move : moves) {
            board.stateValue = (Math.min(board.stateValue, maxValue(move, alpha, beta)));
            if(board.stateValue <= alpha) {
                return board.stateValue;
            }
            beta = Math.min(beta, board.stateValue);
        }
        return board.stateValue;
    }
}
