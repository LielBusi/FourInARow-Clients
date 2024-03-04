public class Presenter
{
    private IView view;
    private Model model;
    private boolean isFirst;
    private boolean isGameOver = false;

    private Communication comm = new Communication();

    public Presenter(View view)
    {
        this.view = view;
        comm.OpenCommunication(); // Open socket with server
        // getting from the server if this server is first or second
        this.isFirst = comm.receiveMove().contains("first");
    }

    /* Activate the right function for view according to which player is it */
    public void GetOrder()
    {
        if (isFirst)
        {
            view.displayMessage("You are first! Set the game board size, make sure the size is legal... ");
            view.First();
        }
        else
        {
            view.displayMessage("You are second!");
            view.Second();
        }
    }

    public void restartGame(String message, int player)
    {
        /* send the answer of the client to server */
        if (player == 1) {
            comm.sendMove(message);
        }

        if (message.contains("yes"))
        {
            this.isGameOver = false; // we are starting new game!
            // open a new model with the same board size
            this.model = new Model(model.GetRows(), model.GetCols());
            view.clearBoard();

            switch (player)
            {
                case 1:
                    view.FirstLoopGame();
                    break;
                case 2:
                    view.SecondLoopGame();
            }
        }
    }

    /* the function return true if the game is over. It's already gets legal move*/
    public boolean userChoice(int col, boolean isOtherPlayer)
    {
        int row;

        // send move to the server if it's not the other player
        if (!isOtherPlayer) {
            comm.sendMove(Integer.toString(col));
        }

        // update the board of the client

        row = model.MakeMove(col);
        // view -> update UI board
        view.UpdateUIBoard(row, col, model.GetCurrentPlayer());

        // check win
        if (model.CheckWin(row, col))
        {
            view.displayMessage("player " + model.GetCurrentPlayer() + " won");
            isGameOver = true;
            return true;
        }

        // check draw
        else if (model.CheckDraw())
        {
            view.displayMessage("it's tie...");
            isGameOver = true;
            return true;
        }

        return false;
    }

    /* get move of the second player from server */
    public boolean getMove()
    {
        int col;

        col = Integer.parseInt(comm.receiveMove());
        return userChoice(col, true);
    }

    /* A function that send the board size to the server - which player 1 decides on */
    public void SetBoardSize(int rows, int cols)
    {
        // create the model
        this.model = new Model(rows, cols);

        // send to the server the board size
        comm.sendMove(Integer.toString(rows));
        comm.sendMove(Integer.toString(cols));
    }

    /* A function that get from the server the board size */
    public void GetBoardSize()
    {
        int rows, cols;

        // amount of rows in board
        rows = Integer.valueOf(comm.receiveMove());
        cols = Integer.valueOf(comm.receiveMove());

        // create the model
        this.model = new Model(rows, cols);

        // set the rows and cols for the view
        this.view.SetRows(rows);
        this.view.SetCols(cols);
    }

    /* A function that send the server the status of the game after each move - only for player 2 */
    public void SendStatus()
    {
        if (isGameOver)
        {
            comm.sendMove("game over");
        }
        else
        {
            comm.sendMove("still playing");
        }
    }

    public boolean CheckMove(int col)
    {
        return model.CheckMove(col);
    }

    /* Player2 get from server if to play another game */
    public void IsAnotherGame()
    {
        restartGame(comm.receiveMove(), 2);
    }
}