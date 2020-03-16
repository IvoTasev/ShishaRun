package nl.saxion.playground.squarerootofpopeye.shisharun.entity;

final class Cell {
    boolean topWall = true;          //show does the wall exists
    boolean bottomWall = true;
    boolean rightWall = true;
    boolean leftWall = true;
    boolean visited;            //Used for generating maze
    int col;                     //number of cols the maze has
    int row;                     //number of rows the maze has

    Cell(int col, int row) {
        this.col = col;
        this.row = row;
        visited = false;
    }
}