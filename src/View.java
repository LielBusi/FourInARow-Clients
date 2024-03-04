import java.util.Scanner;

public class View implements IView
{

    private int rows;
    private int cols;
    private boolean gameOver;

    private char[][] board;
    private final Presenter presenter;

    public View()
    {
        this.presenter = new Presenter(this);
    }

    public void SetRows(int rows) {
        this.rows = rows;
    }

    public void SetCols(int cols) {
        this.cols = cols;
    }

    private void initBoard() {

        for(int i = 0; i < rows; i++)
        {
            for(int j = 0; j < cols; j++)
            {
                board[i][j] = '_';
            }
        }
    }

    public void DisplayBoard()
    {
        System.out.println("The game board: ");
        for(int i = 0; i < rows; i++)
        {
            for(int j = 0; j < cols; j++)
            {
                System.out.print(board[i][j] + " ");
            }
            System.out.println();
        }
    }

    public void startGame()
    {
        presenter.GetOrder(); // The presenter will choose the right procedure to activate
    }

    // If this player is first - firstly he enters move, and then get one
    public void First()
    {
        // The first player decide about the board size and send it to the server

        /* Assumption: the board size is legal! */

        Scanner sc = new Scanner(System.in);  // Create a Scanner object
        System.out.print("Please enter row size of board: ");
        this.rows = sc.nextInt();
        System.out.print("Please enter col size of board: ");
        this.cols = sc.nextInt();

        presenter.SetBoardSize(this.rows, this.cols);
        board = new char[rows][cols];
        initBoard();
        FirstLoopGame();
    }

    // If this player is second - firstly he gets from the server move and the enters one
    // A note: The second player must let the server know each move (his or the other players) if the game over.

    public void Second()
    {
        int col;
        Scanner sc = new Scanner(System.in);

        // The second player get from the server the board size
        presenter.GetBoardSize();
        this.board = new char[this.rows][this.cols];
        initBoard();

        SecondLoopGame();
    }

    public void FirstLoopGame()
    {
        DisplayBoard();
        Scanner sc = new Scanner(System.in);
        this.gameOver = false;

        while(!gameOver)
        {
            int col;

            do {
                System.out.print("Please enter col: ");
                col = sc.nextInt();
            } while (!presenter.CheckMove(col));

            gameOver = presenter.userChoice(col, false);

            if (!gameOver)
            {
                // receive move of the second player from server
                gameOver = presenter.getMove();
            }
        }
        /* getting from client if open another game */
        AnotherGameOptionFirst();
    }

    public void SecondLoopGame()
    {
        DisplayBoard();
        Scanner sc = new Scanner(System.in);
        int col;
        this.gameOver = false;

        while(!gameOver)
        {
            gameOver = presenter.getMove();
            presenter.SendStatus();

            if (!gameOver)
            {
                do {
                    System.out.print("Please enter col: ");
                    col = sc.nextInt();
                } while (!presenter.CheckMove(col));

                gameOver = presenter.userChoice(col, false);
                presenter.SendStatus();
            }
        }
        /* Getting answer from player1 if there is another play */
        presenter.IsAnotherGame();
    }

    public void UpdateUIBoard(int row, int col, char newSign)
    {
        //UI - ממשק משתמש
        board[row][col] = newSign;
        DisplayBoard();
    }

    public void displayMessage(String message) {
        System.out.println(message);
    }

    public void clearBoard()
    {
        for(int i = 0; i < rows; i++)
        {
            for(int j = 0; j < cols; j++)
            {
                board[i][j] = '_';
            }
        }
    }

    /* Getting from player 1 if he wants to play another game */
    public void AnotherGameOptionFirst()
    {
        Scanner sc = new Scanner(System.in);
        System.out.print("Would you like to play another game? yes/no: ");
        presenter.restartGame(sc.nextLine(), 1);
    }
}