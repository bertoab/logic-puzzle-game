// Cesar Pimentel
package com.mcnz.test.gradletest;

public class PuzzleGame {
    // TO-DO, implement PuzzleDefinition, Board & PuzzleValidator objects once their classes exist.

    // Board board;
    // PuzzleDefinition definition;
    // PuzzleValidator validator;
    int errorsAmount;

    /*
    PuzzleGame(PuzzleDefinition definition,Board board, PuzzleValidator validator) {

    }
    */

    public int[][] getNextHint() {
        // Placeholder for now, this holds no meaning yet.
        return new int[2][2];
    }

    // Will clear all errors present and return how many.
    public int clearError() {
        return errorsAmount;
    }

    @Override
    public String toString() {
        return super.toString();
    }

    @Override
    public boolean equals(Object obj) {
        return super.equals(obj);
    }

}
