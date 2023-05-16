/* Skeleton code copyright (C) 2008, 2022 Paul N. Hilfinger and the
 * Regents of the University of California.  Do not distribute this or any
 * derivative work without permission. */

package ataxx;

import java.util.ArrayList;
import java.util.Random;

import static java.lang.Math.max;
import static java.lang.Math.min;

/**
 * A Player that computes its own moves.
 */
class AIPlayer extends Player {

    /** Maximum minimax search depth before going to static evaluation. */
    private static final int MAX_DEPTH = 3;
    /**
     * A position magnitude indicating a win (for red if positive, blue
     * if negative).
     */
    private static final int WINNING_VALUE = Integer.MAX_VALUE - 20;
    /** A magnitude greater than a normal value. */
    private static final int INFTY = Integer.MAX_VALUE;

    /** the standard for the endgame */
    private static final int ENDGAME = 20;

    /**
     * A new AI for GAME that will play MYCOLOR. SEED is used to initialize
     * a random-number generator for use in move computations. Identical
     * seeds produce identical behaviour.
     */
    AIPlayer(Game game, PieceState myColor, long seed) {
        super(game, myColor);
        _random = new Random(seed);
    }

    @Override
    boolean isAuto() {
        return true;
    }

    @Override
    String getAtaxxMove() {
        if (!getAtaxxBoard().couldMove(getMyState())) {
            getAtaxxGame().reportMove(Move.pass(), getMyState());
            return "-";
        }
        // Main.startTiming();
        Move move = findMove();
        // Main.endTiming();
        getAtaxxGame().reportMove(move, getMyState());
        return move.toString();
    }

    /**
     * Return a move for me from the current position, assuming there
     * is a move.
     */
    private Move findMove() {
        Board b = new Board(getAtaxxBoard());
        // System.out.println(b);
        lastFoundMove = null;
        // 这个评估函数读与红色玩家来说是越大越好，对于蓝色玩家来说是越小越好
        // System.out.println(PieceState.EMPTY.ordinal());
        int curEmpty = 49 - b.getColorNums(PieceState.RED) - b.getColorNums(PieceState.BLUE) - (49 - b.getUnblockedNum());
//         System.out.println(curEmpty);
        if (curEmpty >= 20) {
            if (getMyState() == PieceState.RED) {
                minMax0(b, MAX_DEPTH+1, true, 1, -INFTY, INFTY);
            } else if (getMyState() == PieceState.BLUE) {
                minMax0(b, MAX_DEPTH, true, -1, -INFTY, INFTY);
            }
            return lastFoundMove;
        } else {
            if (getMyState() == PieceState.RED) {
                minMax1(b, MAX_DEPTH+1, true, 1, -INFTY, INFTY);
            } else if (getMyState() == PieceState.BLUE) {
                minMax1(b, MAX_DEPTH, true, -1, -INFTY, INFTY);
            }
            return lastFoundMove;
        }
//        if (getMyState() == PieceState.RED) {
//            minMax0(b, MAX_DEPTH+1, true, 1, -INFTY, INFTY);
//        } else if (getMyState() == PieceState.BLUE) {
//            minMax0(b, MAX_DEPTH, true, -1, -INFTY, INFTY);
//        }
//        return lastFoundMove;
    }

    /** The move found by the last call to the findMove method above. */
    private Move lastFoundMove;

    /**
     * Find a move from position BOARD and return its value, recording
     * the move found in _foundMove iff SAVEMOVE. The move
     * should have maximal value or have value > BETA if SENSE==1,
     * and minimal value or value < ALPHA if SENSE==-1. Searches up to
     * DEPTH levels. Searching at level 0 simply returns a static estimate
     * of the board value and does not set _foundMove. If the game is over
     * on BOARD, does not set _foundMove.
     */
    // private double minMax0(Board board, int depth, boolean saveMove, double
    // sense,
    // double alpha, double beta) {
    // /* We use WINNING_VALUE + depth as the winning value so as to favor
    // * wins that happen sooner rather than later (depth is larger the
    // * fewer moves have been made. */
    // if (depth == 0 || board.getWinner() != null) {
    // return staticScore(board, WINNING_VALUE + depth);
    // }
    // Move best = null;
    // double bestScore = 0;
    // ArrayList<Move> allPossibleMoves = new ArrayList<>();
    // if (sense == 1) {
    // if (board.moveLegal(Move.pass())) {
    // allPossibleMoves.add(Move.pass());
    // } else {
    // bestScore = -INFTY;
    // allPossibleMoves = possibleMoves(board,board.nextMove());
    // for (Move possible : allPossibleMoves) {
    // Board copy = new Board(board);
    // copy.createMove(possible);
    // double response = minMax0(copy, depth - 1, false,
    // -1, alpha, beta);
    // if (response > bestScore) {
    // best = possible;
    // bestScore = response;
    // alpha = max(alpha, bestScore);
    // if (alpha >= beta) {
    // break;
    // }
    // }
    // }
    // }
    // } else if (sense == -1) {
    // if (board.moveLegal(Move.pass())) {
    // allPossibleMoves.add(Move.pass());
    // } else {
    // bestScore = INFTY;
    // allPossibleMoves = possibleMoves(board,board.nextMove());
    // for (Move possible : allPossibleMoves) {
    // Board copy = new Board(board);
    // copy.createMove(possible);
    // double response = minMax0(copy, depth - 1, false,
    // 1, alpha, beta);
    // if (response < bestScore) {
    // bestScore = response;
    // best = possible;
    // beta = min(beta, bestScore);
    // if (alpha >= beta) {
    // break;
    // }
    // }
    // }
    // }
    // }
    // if (saveMove) {
    // lastFoundMove = best;
    // }
    // return bestScore;
    // }

    private double minMax0(Board board, int depth, boolean saveMove, double sense,
                           double alpha, double beta) {
        /*
         * We use WINNING_VALUE + depth as the winning value so as to favor
         * wins that happen sooner rather than later (depth is larger the
         * fewer moves have been made.
         */
        if (depth == 0 || board.getWinner() != null) {
            return staticScore(board, WINNING_VALUE + depth);
        }
        Move best = null;
        double bestScore = 0;
        ArrayList<Move> allPossibleMoves = new ArrayList<>();
        if (sense == 1) {
            if (board.moveLegal(Move.pass())) {
                allPossibleMoves.add(Move.pass());
            } else {
                bestScore = -INFTY;
                allPossibleMoves = possibleMoves(board, board.nextMove());
                for (Move possible : allPossibleMoves) {
                    Board copy = new Board(board);
                    copy.createMove(possible);
                    double response = minMax0(copy, depth - 1, false,
                            -1, alpha, beta);
                    if (response > bestScore) {
                        best = possible;
                        bestScore = response;
                        alpha = max(alpha, bestScore);
                        if (alpha >= beta) {
                            break;
                        }
                    }
                }
            }
        } else if (sense == -1) {
            if (board.moveLegal(Move.pass())) {
                allPossibleMoves.add(Move.pass());
            } else {
                bestScore = INFTY;
                allPossibleMoves = possibleMoves(board, board.nextMove());
                for (Move possible : allPossibleMoves) {
                    Board copy = new Board(board);
                    copy.createMove(possible);
                    double response = minMax0(copy, depth - 1, false,
                            1, alpha, beta);
                    if (response < bestScore) {
                        bestScore = response;
                        best = possible;
                        beta = min(beta, bestScore);
                        if (alpha >= beta) {
                            break;
                        }
                    }
                }
            }
        }
        if (saveMove) {
            lastFoundMove = best;
        }
        return bestScore;
    }

    private double minMax1(Board board, int depth, boolean saveMove, int sense,
                           double alpha, double beta) {
        /*
         * We use WINNING_VALUE + depth as the winning value so as to favor
         * wins that happen sooner rather than later (depth is larger the
         * fewer moves have been made.
         */
        if (depth == 0 || board.getWinner() != null) {
            return staticScore(board, WINNING_VALUE + depth);
        }
        Move best = null;
        double bestScore = 0.0;
        ArrayList<Move> allPossibleMoves = new ArrayList<>();
        if (sense == 1) {
            if (board.moveLegal(Move.pass())) {
                allPossibleMoves.add(Move.pass());
            } else {
                bestScore = -INFTY;
                allPossibleMoves = possibleMoves(board, board.nextMove());
                for (Move possible : allPossibleMoves) {
                    Board copy = new Board(board);
                    copy.createMove(possible);
                    double response = minMax1(copy, depth - 1, false,
                            -1, alpha, beta) + 0.01 * evaluateMove(board, possible);
                    if (response > bestScore) {
                        best = possible;
                        bestScore = response;
                        alpha = max(alpha, bestScore);
                        if (alpha >= beta) {
                            break;
                        }
                    }
                }
            }
        } else if (sense == -1) {
            if (board.moveLegal(Move.pass())) {
                allPossibleMoves.add(Move.pass());
            } else {
                bestScore = INFTY;
                allPossibleMoves = possibleMoves(board, board.nextMove());
                for (Move possible : allPossibleMoves) {
                    Board copy = new Board(board);
                    copy.createMove(possible);
                    double response = minMax1(copy, depth - 1, false,
                            1, alpha, beta) + evaluateMove(board, possible);
                    if (response < bestScore) {
                        bestScore = response;
                        best = possible;
                        beta = min(beta, bestScore);
                        if (alpha >= beta) {
                            break;
                        }
                    }
                }
            }
        }
        if (saveMove) {
            lastFoundMove = best;
        }
        return bestScore;
    }

    /**
     * Return a heuristic value for BOARD. This value is +- WINNINGVALUE in
     * won positions, and 0 for ties.
     */
    private double staticScore(Board board, int winningValue) {
        // int totalSpace = 49;
        // int endGameStandard = 20;// set the criteria of what is an endgame
        //
        // if (board.getColorNums(PieceState.EMPTY) == endGameStandard){
        // return evaluateMove(board.);
        // }
        PieceState winner = board.getWinner();
        if (winner != null) {
            return switch (winner) {
                case RED -> winningValue;
                case BLUE -> -winningValue;
                default -> 0;
            };
        }
        if (board.nextMove() == PieceState.RED) {
            return board.getColorNums(PieceState.RED) - board.getColorNums(PieceState.BLUE);
        } else {
            return board.getColorNums(PieceState.BLUE) - board.getColorNums(PieceState.RED);
        }
    }

    /** Pseudo-random number generator for move computation. */
    private Random _random = new Random();

    /** The move found by the last call to the findMove method above. */

    /**
     * Return all possible moves for a color.
     *
     * @param board   the current board.
     * @param myColor the specified color.
     * @return an ArrayList of all possible moves for the specified color.
     */
    private ArrayList<Move> possibleMoves(Board board, PieceState myColor) {
        ArrayList<Move> possibleMoves = new ArrayList<>();
        for (char row = '7'; row >= '1'; row--) {
            for (char col = 'a'; col <= 'g'; col++) {
                int index = Board.index(col, row);
                if (board.getContent(index) == myColor) {
                    ArrayList<Move> addMoves = assistPossibleMoves(board, row, col);
                    possibleMoves.addAll(addMoves);
                }
            }
        }
        return possibleMoves;
    }// find given color all the possible move

    /**
     * Returns an Arraylist of legal moves.
     *
     * @param board the board for testing
     * @param row   the row coordinate of the center
     * @param col   the col coordinate of the center
     */
    private ArrayList<Move> assistPossibleMoves(Board board, char row, char col) {
        ArrayList<Move> assistPossibleMoves = new ArrayList<>();
        for (int i = -2; i <= 2; i++) {
            for (int j = -2; j <= 2; j++) {
                if (i != 0 || j != 0) {
                    char row2 = (char) (row + j);
                    char col2 = (char) (col + i);
                    Move currMove = Move.move(col, row, col2, row2);
                    if (board.moveLegal(currMove)) {
                        assistPossibleMoves.add(currMove);
                    }
                }
            }
        }
        return assistPossibleMoves;
    } // find all the possible move by given col and row

    private double evaluateMove(Board board, Move move) {
        if (move == null)
            return 0;

        double score = 0;
        PieceState myColor = board.nextMove();
        PieceState enemyColor = (myColor == PieceState.RED) ? PieceState.BLUE : PieceState.RED;

        // s1 for each enemy stone taken over
        Board hypotheticalBoard = new Board(board);
        hypotheticalBoard.createMove(move);
        int capturedStones = board.getColorNums(enemyColor) - hypotheticalBoard.getColorNums(enemyColor);
        score += capturedStones;

        // s2 for each own stone around target
        int ownStonesAroundTarget = countOwnNeighbors(hypotheticalBoard, move.col1(), move.row1());
        score += 0.4 * ownStonesAroundTarget;

        // s3 if move is not a jump move
        if (!move.isJump()) {
            score += 0.7;
        } else {
            // s4 for each own stone around the source square (if the move is a jump move)
            int ownStonesAroundSource = countOwnNeighbors(board, move.col0(), move.row0());
            score += 0.4 * ownStonesAroundSource;
        }

        // If S < 0, then we set S = 0
        return Math.max(score, 0);
    }

    // // Helper function to count the number of stones of a certain color adjacent
    // to a certain square
    // private int countAdjacentStones(Board board, Square square, PieceState color)
    // {
    // int count = 0;
    // for (Square neighbor : square.getNeighbors()) {
    // if (board.getPieceState(neighbor) == color) {
    // count++;
    // }
    // }
    // return count;
    // }

    private int countOppNeighbors(Board board, char row, char col) {

        // int count = 0;
        // for (int i = -2; i <= 2; i++) {
        // for (int j = -2; j <= 2; j++) {
        // char row2 = (char) (row + j);
        // char col2 = (char) (col + i);
        // if (board.getContent(Board.index(col2, row2)) == PieceState.RED) {
        // count++;
        // }
        // }
        // }
        // return count;

        int opponentPieces = 0;
        PieceState opponentColor = getOpponentColor();

        for (int i = -2; i <= 2; i++) {
            for (int j = -2; j <= 2; j++) {
                if (i != 0 || j != 0) { // Exclude the centerpiece itself
                    char row2 = (char) (row + j);
                    char col2 = (char) (col + i);
                    int index = Board.index(col2, row2);
                    if ((opponentColor != PieceState.BLOCKED) && (opponentColor != PieceState.EMPTY)) {
                        if (board.getContent(index) == opponentColor) {
                            opponentPieces++;
                        }
                    }

                }
            }
        }
        return opponentPieces;
    }

    private PieceState getOpponentColor() {
        // if RED returns BLUE, vice versa
        // if BLOCKED/ EMPTY return themselves
        if (getMyState() == PieceState.RED) {
            return PieceState.BLUE;
        } else if (getMyState() == PieceState.BLUE) {
            return PieceState.RED;
        } else {
            return getMyState();
        }
    }

    private int countOwnNeighbors(Board board, char row, char col) {

        int numPieces = 0;
        PieceState ownColor = getMyState();

        for (int i = -2; i <= 2; i++) {
            for (int j = -2; j <= 2; j++) {
                if (i != 0 || j != 0) { // Exclude the centerpiece itself
                    char row2 = (char) (row + j);
                    char col2 = (char) (col + i);
                    if (row2 > 0 && row2 < 8 && col2 > 0 && col2 < 8) {
                        int index = Board.index(col2, row2);
                        // System.out.println(index);
                        if ((ownColor != PieceState.BLOCKED) && (ownColor != PieceState.EMPTY)) {
                            if (board.getContent(index) == ownColor) {
                                numPieces++;
                            }
                        }
                    }
                }
            }
        }
        return numPieces;
    }

    // A simple function to count the number of empty neighbors around a piece
    private int countEmptyNeighbors(Board board, char row, char col) {
        int count = 0;
        for (int i = -2; i <= 2; i++) {
            for (int j = -2; j <= 2; j++) {
                char row2 = (char) (row + j);
                char col2 = (char) (col + i);
                if (board.getContent(Board.index(col2, row2)) == PieceState.EMPTY) {
                    count++;
                }
            }
        }
        return count;
    }
}