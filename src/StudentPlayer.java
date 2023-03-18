import static java.lang.Math.max;
import static java.lang.Math.min;

public class StudentPlayer extends Player{

    private final int maxdepth = 1
            ;

    public StudentPlayer(int playerIndex, int[] boardSize, int nToConnect) {
        super(playerIndex, boardSize, nToConnect);
    }

    @Override
    public int step(Board board) {
        int v;
        int maxV = -10000000;
        int col = 0;
        int alpha = -10000000;
        int beta = 10000000;

        for(int i = 0; i < 7; i++){
            Board boardCopy= new Board(board);
            if(boardCopy.stepIsValid(i)){
                boardCopy.step(2, i);
                v = minValue(boardCopy, maxdepth, alpha, beta);
                if(v > maxV){
                    maxV = v;
                    col = i;
                }
            }
        }
        return col;
    }

    private int maxValue(Board board, int depth, int alpha, int beta){
        int v = -10000000;
        int score = 0;


        if(depth == 0 || board.gameEnded()){
            if(board.gameEnded()){
                if(board.getWinner() == 1)
                    return -1000;
                else if(board.getWinner() == 2)
                    return 1000;
            }
            score += inARow(board.getLastPlayerRow(), board.getLastPlayerColumn(), board) +
                    inACol(board.getLastPlayerRow(), board.getLastPlayerColumn(), board) +
                    inDiagonally(board.getLastPlayerRow(), board.getLastPlayerColumn(), board) +
                    inSkewDiagonally(board.getLastPlayerRow(), board.getLastPlayerColumn(), board);
            return score;
        }

        for(int i = 0; i < 7; i++){
            Board boardCopy= new Board(board);
            if(boardCopy.stepIsValid(i)){
                boardCopy.step(2, i);
                v = max(v, minValue(boardCopy, depth-1, alpha, beta));
            }
            if(v >= beta){
                return v;
            }
            alpha = max(alpha, v);
        }
        return v;
    }

    private int minValue(Board board, int depth, int alpha, int beta){
        int v = 10000000;
        int score = 0;

        if(depth == 0 || board.gameEnded()){
            if(board.gameEnded()){
                if(board.getWinner() == 1)
                    return -1000;
                else if(board.getWinner() == 2)
                    return 1000;
            }
            score += inARow(board.getLastPlayerRow(), board.getLastPlayerColumn(), board) +
                    inACol(board.getLastPlayerRow(), board.getLastPlayerColumn(), board) +
                    inDiagonally(board.getLastPlayerRow(), board.getLastPlayerColumn(), board) +
                    inSkewDiagonally(board.getLastPlayerRow(), board.getLastPlayerColumn(), board);
            return score;
        }

        for(int i = 0; i < 7; i++){
            Board boardCopy= new Board(board);
            if(boardCopy.stepIsValid(i)){
                boardCopy.step(1, i);
                v = min(v, maxValue(boardCopy, depth-1, alpha, beta));
            }
            if(v <= alpha){
                return v;
            }
            beta = min(beta, v);
        }
        return v;
    }

    private int getScore(int player, int opponent, int empty) {
        int score = 0;

        if(player == 4){
            score += 1000;
        }
        else if(player == 3 && empty == 1){
            score += 15;
        }
        else if(player == 2 && empty == 2){
            score += 2;
        }
        else if(opponent == 2 && empty == 0){
            score -= 2;
        }
        else if(opponent == 3 && empty == 1){
            score -= 10;
        }
        return score;
    }

    private int inARow(int row, int col, Board board) {
        int[][] state = board.getState();
        int score = 0;

        for (int r = 0; r < boardSize[0]; r++){
            for(int c = 0; c < boardSize[1] - nToConnect + 1; c++){
                int xInRow = 0;
                int oInRow = 0;
                int emptyInRow = 0;
                for(int i = 0; i < nToConnect; i++){
                    if(state[r][c + i] == 1){
                        xInRow++;
                    }else if(state[r][c + i] == 2){
                        oInRow++;
                    }else{
                        emptyInRow++;
                    }
                }
                score += getScore(oInRow, xInRow, emptyInRow);
            }
        }

        return score;
    }

    private int inACol(int row, int col, Board board) {
        int[][] state = board.getState();
        int score = 0;

        for (int c = 0; c < boardSize[1]; c++){
            for(int r = 0; r < boardSize[0] - nToConnect + 1; r++){
                int xInRow = 0;
                int oInRow = 0;
                int emptyInRow = 0;
                for(int i = 0; i < nToConnect; i++){
                    if(state[r + i][c] == 1){
                        xInRow++;
                    }else if(state[r + i][c] == 2){
                        oInRow++;
                    }else{
                        emptyInRow++;
                    }
                }
                score += getScore(oInRow, xInRow, emptyInRow);
            }
        }

        return score;
    }

    private int inDiagonally(int row, int col, Board board) {
        int[][] state = board.getState();
        int score = 0;

        for(int r = nToConnect - 1; r < boardSize[0]; r++){
            for(int c = 0; c < boardSize[1] - nToConnect + 1; c++){
                int xInRow = 0;
                int oInRow = 0;
                int emptyInRow = 0;
                for(int i = 0; i < nToConnect; i++){
                    if(state[r - i][c + i] == 1){
                        xInRow++;
                    }else if(state[r - i][c + 1] == 2){
                        oInRow++;
                    }else{
                        emptyInRow++;
                    }
                }
                score += getScore(oInRow, xInRow, emptyInRow);
            }
        }


        return score;
    }

    private int inSkewDiagonally(int row, int col, Board board) {
        int[][] state = board.getState();
        int score = 0;

        for(int r = 0; r < nToConnect - 1; r++){
            for(int c = 0; c < boardSize[1] - nToConnect + 1; c++){
                int xInRow = 0;
                int oInRow = 0;
                int emptyInRow = 0;
                for(int i = 0; i < nToConnect; i++){
                    if(state[r + i][c + i] == 1){
                        xInRow++;
                    }else if(state[r + i][c + 1] == 2){
                        oInRow++;
                    }else{
                        emptyInRow++;
                    }
                }
                score += getScore(oInRow, xInRow, emptyInRow);
            }
        }

        return score;
    }
}
