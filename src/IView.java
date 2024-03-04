public interface IView
{
    /* The presenter must be able to set view board dynamic size and */
    void SetRows(int rows);
    void SetCols(int cols);

    /* The presenter will use the game loops of the first and second player to start another game */
    void FirstLoopGame();
    void SecondLoopGame();

    /* The presenter needs a function for the first time entering a play in the system.
    To be able to get the board size from the first player.
    And also set the board size of the other player.
     */

    void First();
    void Second();

    /* The presenter want to clear the board when entering a new game */
    void clearBoard();

    /* All views must have DisplayBoard function for UI */
    void DisplayBoard();

    /* After each move the board must update according to the presenter */
    void UpdateUIBoard(int row, int col, char ch);

    /* In the end of each game, the view must display the users message about how the game ends */
    void displayMessage(String message);

}