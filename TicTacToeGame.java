import java.util.Scanner;

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
    private String[] player;
    private String playerO;
    private String playerX;
    private int turns = 0; //turns taken

    // TODO 4: Implement necessary methods to manage the games of Tic Tac Toe
    public TicTacToeGame(String thisPlayer, String otherPlayer, boolean isX){
        this.isX = isX;
        this.playerO = otherPlayer;
        this.playerX = thisPlayer;
        this.player = new String[]{playerX, playerO};
    }

    private char[][] tttArray = new char[3][3];

    public void initialize() {
        int count = 0;
        for (int x = 0; x < tttArray.length; x++) {
            for (int y = 0; y < tttArray.length; y++) {
                tttArray[x][y] = (char) (count + '0');
                count++;
            }
        }
    }

    public void playGame(int move) {
        int row = 0;
        int col = 0;

        int index = this.turns % 2;

        if(index == 0){
            for (int x = 0; x < tttArray.length; x++) {
                for (int y = 0; y < tttArray.length; y++) {
                    if (tttArray[x][y] == move + '0') {
                        row = x;
                        col = y;
                    }
                }
            }

            if (tttArray[row][col] != 'X' && tttArray[row][col] != 'O') {
                tttArray[row][col] = 'X';
            }
        }
        else{
            for (int x = 0; x < tttArray.length; x++) {
                for (int y = 0; y < tttArray.length; y++) {
                    if (tttArray[x][y] == move + '0') {
                        row = x;
                        col = y;
                    }
                }
            }

            if (tttArray[row][col] != 'X' && tttArray[row][col] != 'O') {
                tttArray[row][col] = 'O';
            }
        }
        this.turns++;
    }
    public void displayBoard(){
        System.out.println();
        System.out.println("----------");
        for (int x = 0; x < tttArray.length; x++) {
            for (int y = 0; y < tttArray.length; y++) {
                if (y == 2) {
                    System.out.print(tttArray[x][y]);
                } else {
                    System.out.print(tttArray[x][y] + " | ");
                }
            }
            System.out.println();
            System.out.println("----------");
        }
    }

    public boolean playerXCheck() {
        //top row
        if(tttArray[0][0] == 'X' && tttArray[0][1] == 'X' && tttArray[0][2] == 'X'){
            return true;
        }
        //mid row
        if(tttArray[1][0] == 'X' && tttArray[1][1] == 'X' && tttArray[1][2] == 'X'){
            return true;
        }
        //bot row
        if(tttArray[2][0] == 'X' && tttArray[2][1] == 'X' && tttArray[2][2] == 'X'){
            return true;
        }
        //left col
        if(tttArray[0][0] == 'X' && tttArray[1][0] == 'X' && tttArray[2][0] == 'X'){
            return true;
        }
        //mid col
        if(tttArray[0][1] == 'X' && tttArray[1][1] == 'X' && tttArray[2][1] == 'X'){
            return true;
        }
        //right col
        if(tttArray[0][1] == 'X' && tttArray[1][1] == 'X' && tttArray[2][1] == 'X'){
            return true;
        }
        //diagonal bottom left to top right
        if(tttArray[2][0] == 'X' && tttArray[1][1] == 'X' && tttArray[0][2] == 'X'){
            return true;
        }
        //diagonal bottom left to top right
        if(tttArray[0][0] == 'X' && tttArray[1][1] == 'X' && tttArray[2][0] == 'X'){
            return true;
        }

        return false;
    }

    public boolean playerOCheck() {
        //top row
        if(tttArray[0][0] == 'O' && tttArray[0][1] == 'O' && tttArray[0][2] == 'O'){
            return true;
        }
        //mid row
        if(tttArray[1][0] == 'O' && tttArray[1][1] == 'O' && tttArray[1][2] == 'O'){
            return true;
        }
        //bot row
        if(tttArray[2][0] == 'O' && tttArray[2][1] == 'O' && tttArray[2][2] == 'O'){
            return true;
        }
        //left col
        if(tttArray[0][0] == 'O' && tttArray[1][0] == 'O' && tttArray[2][0] == 'O'){
            return true;
        }
        //mid col
        if(tttArray[0][1] == 'O' && tttArray[1][1] == 'O' && tttArray[2][1] == 'O'){
            return true;
        }
        //right col
        if(tttArray[0][1] == 'O' && tttArray[1][1] == 'O' && tttArray[2][1] == 'O'){
            return true;
        }
        //diagonal bottom left to top right
        if(tttArray[2][0] == 'O' && tttArray[1][1] == 'O' && tttArray[0][2] == 'O'){
            return true;
        }
        //diagonal bottom left to top right
        if(tttArray[0][0] == 'O' && tttArray[1][1] == 'O' && tttArray[2][0] == 'O'){
            return true;
        }

        return false;
    }

    public char[][] getArray(){
        return this.tttArray;
    }

    public int takeTurn(int index){
        return 1;
    }
    public char getWinner(){
        return ' ';
    }
    //return 1 if tied 0 if not
    public boolean isTied(){
        int count = 0;
        for (int x = 0; x < tttArray.length; x++) {
            for (int y = 0; y < tttArray.length; y++) {
                if (tttArray[x][y] == 'X' || tttArray[x][y] == 'O') {
                    count++;
                }
            }
        }

        if(count == 8){
            return true;
        }
        else{
            return false;
        }
    }
    public char getSpace(int index){
        return ' ';
    }
    public String toString(){
        return "";
    }
}