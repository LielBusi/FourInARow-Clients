public class Model
{

    // Holding a board

    private int[][] gameBoard;
    private int rows;

    private int cols;
    // holding the game board
    private int[][] board;
    // holding amount of pieces in each row - an array with size of rows
    private int[] countArr;

    // we have seven directions to check each time
    private final int[][] directionArr =
            {{1, 1}, {-1, -1}, {1, -1}, {-1, 1}, {0, 1}, {0, -1}, {1, 0}};

    private int moves;
    public Model(int rows, int cols)
    {
        this.rows = rows;
        this.cols = cols;
        this.moves = 0;

        InitBoard();
    }

    private void InitBoard()
    {
        // In java, by default all bytes are zero in initializing the board
        board = new int[this.rows][this.cols];
        countArr = new int[this.cols];
    }

    public int MakeMove(int col)
    {
        if (CheckMove(col))
        {
            board[this.rows - ++countArr[col]][col] = (moves++ & 1) + 1;
            return this.rows - countArr[col]; // return the row we put the new piece
        }
        return -1;
    }

    public boolean CheckMove(int col)
    {
        return col >= 0 && col < this.cols && countArr[col] < this.rows;
    }

    private boolean IsStrikeNotEnd(int row, int col)
    {
        // check the strike of the last player to play ---- ((moves & 1) ^ 1) + 1

        return row >= 0 && row < this.rows && col >= 0 && col < this.cols  && this.board[row][col] == ((moves & 1) ^ 1) + 1;
    }

    private int ExploreDirection (int row, int col, int dRow, int dCol, int strike)
    {
        // check bounds and current strike
        if (IsStrikeNotEnd(row + dRow, col + dCol))
        {
            return ExploreDirection(row + dRow, col + dCol, dRow, dCol, strike + 1);
        }
        return strike;
    }

    // call this function with strike of zero at start - in the recursion we will make it one
    public boolean CheckWin(int row, int col)
    {
        // If there are three neighbors to the current move, return true.
        // we need to check 4 directions:
        // horizontal   -
        // vertical     | (just down)
        // diagonal 1   /
        // diagonal 2   \

        // we have 7 directions to check. let's do it with count array
        int [] strikeArray = new int [7];

        for (int i = 0; i < this.directionArr.length; i++)
        {
            strikeArray[i] = ExploreDirection(row, col, directionArr[i][0], directionArr[i][1], 1);
        }

        // checking vertical down
        if (strikeArray[6] >= 4)
        {
            return true;
        }

        // check the rest
        for (int i = 0; i < 6; i+=2)
        {
            // to not count the original play twice
            if (strikeArray[i] + strikeArray[i + 1] - 1 >= 4)
            {
                return true;
            }
        }

        return false;
    }

    public boolean CheckDraw()
    {
        return moves == this.rows * this.cols;
    }

    public char GetCurrentPlayer()
    {
        return (char) ((((this.moves & 1) ^ 1) + 1) + '0');
    }

    // For the next game the presenter will create new Model with the last model board size
    // So we need getters of those.

    public int GetRows() {
        return rows;
    }

    public int GetCols() {
        return cols;
    }
}