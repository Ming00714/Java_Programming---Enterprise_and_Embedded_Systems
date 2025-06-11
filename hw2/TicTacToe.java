import java.util.Scanner;

public class TicTacToe {
    private enum Cell { EMPTY, X, O }
    private Cell[][] board = new Cell[3][3];

    public TicTacToe() {
        for (int i = 0; i < 3; ++i)
            for (int j = 0; j < 3; ++j)
                board[i][j] = Cell.EMPTY;
    }

    public void printBoard() {
        System.out.println("Current board:");
        for (int i = 0; i < 3; ++i) {
            for (int j = 0; j < 3; ++j) {
                switch (board[i][j]) {
                    case X: System.out.print("X "); break;
                    case O: System.out.print("O "); break;
                    default: System.out.print(". ");
                }
            }
            System.out.println();
        }
    }

    public boolean makeMove(int row, int col, Cell player) {
        if (row < 0 || row >= 3 || col < 0 || col >= 3)
            return false;
        if (board[row][col] != Cell.EMPTY)
            return false;
        board[row][col] = player;
        return true;
    }

    public boolean checkWin(Cell player) {
        for (int i = 0; i < 3; ++i) {
            if (board[i][0] == player && board[i][1] == player && board[i][2] == player) return true;
            if (board[0][i] == player && board[1][i] == player && board[2][i] == player) return true;
        }
        if (board[0][0] == player && board[1][1] == player && board[2][2] == player) return true;
        if (board[0][2] == player && board[1][1] == player && board[2][0] == player) return true;
        return false;
    }

    public boolean isDraw() {
        for (int i = 0; i < 3; ++i)
            for (int j = 0; j < 3; ++j)
                if (board[i][j] == Cell.EMPTY)
                    return false;
        return true;
    }

    public void playGame() {
        Scanner scanner = new Scanner(System.in);
        Cell currentPlayer = Cell.X;

        while (true) {
            printBoard();
            System.out.printf("Player %s, enter your move (row and column): ", currentPlayer);
            int row = scanner.nextInt();
            int col = scanner.nextInt();

            if (!makeMove(row, col, currentPlayer)) {
                System.out.println("Invalid move. Try again.");
                continue;
            }

            if (checkWin(currentPlayer)) {
                printBoard();
                System.out.printf("Player %s wins!\n", currentPlayer);
                break;
            } else if (isDraw()) {
                printBoard();
                System.out.println("It's a draw!");
                break;
            }

            currentPlayer = (currentPlayer == Cell.X) ? Cell.O : Cell.X;
        }

        scanner.close();
    }

    public static void main(String[] args) {
        TicTacToe game = new TicTacToe();
        game.playGame();
    }
}

