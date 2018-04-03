public class TicTacToeGame {

    private static final char PLAYERX = 'X';     // Helper constant for X player
    private static final char PLAYERO = 'O';     // Helper constant for O player
    private static final char SPACE = ' ';       // Helper constant for spaces

    /*
    Sample TicTacToe Board
      0 | 1 | 2
     -----------
      3 | 4 | 5
     -----------
      6 | 7 | 8
     */
    private boolean isX;
    private String otherPlayer;

    // TODO 4: Implement necessary methods to manage the games of Tic Tac Toe
    public TicTacToeGame(String otherPlayer, boolean isX){
        this.isX = isX;
        this.otherPlayer = otherPlayer;
    }

    public int takeTurn(int index){
        return 1;
    }
    public char getWinner(){
        return ' ';
    }
    //return 1 if tied 0 if not
    public int isTied(){
        return 1;
    }
    public char getSpace(int index){
        return ' ';
    }
    public String toString(){
        return "";
    }
}