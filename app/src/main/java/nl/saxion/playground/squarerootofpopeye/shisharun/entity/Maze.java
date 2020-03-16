package nl.saxion.playground.squarerootofpopeye.shisharun.entity;

import java.util.ArrayList;
import java.util.Random;
import java.util.Stack;

public final class Maze {
    private Cell[][] cells;
    private Cell player;
    private Cell exit;
    private boolean win;
    private int cols;
    private int rows;
    public static final float wallThickness = 4.0f;
    private float cellSize;
    private Random random;

    public Maze(int cols, int rows) {
        this.cols = cols;
        this.rows = rows;
        random = new Random();
        cells = new Cell[cols][rows];
    }

    public int getCols() {
        return cols;
    }

    public int getRows() {
        return rows;
    }

    public Cell[][] getCells() {
        return cells;
    }

    public float getCellSize() {
        return cellSize;
    }

    public Cell getPlayer() {
        return player;
    }

    public Cell getExit() {
        return exit;
    }

    public boolean isWin() {
        return win;
    }

    public void setCellSize(float cellSize) {
        this.cellSize = cellSize;
    }


    /**
     * creates maze
     */
    public void createMaze() {
        Stack<Cell> stack = new Stack<>();
        Cell current, next;

        for (int i = 0; i < cols; i++) {
            for (int j = 0; j < rows; j++) {
                cells[i][j] = new Cell(i, j);
            }
        }

        player = cells[0][0];
        exit = cells[cols - 1][rows - 1];

        current = cells[0][0];
        current.visited = true;
        do {
            next = getNeighbour(current);
            if (next != null) {
                removeWall(current, next);
                stack.push(current);
                current = next;
                current.visited = true;
            } else {
                if (!stack.empty()) {
                    current = stack.pop();
                }
            }
        } while (!stack.empty());
    }

    /**
     * checks if the player reached the exit
     */
    public void checkExit() {
        if (player == exit) {
            win = true;
        } else {
            win = false;
        }
    }

    /**
     * method moves player by changing his col/row
     * @param direction direction to move
     */
    public void movePlayer(PlayerControl direction) {
        switch (direction) {
            case UP:
                if (!player.topWall) {                           //checks the possibility of moving this direction(if the wall exists player can't move)
                    player = cells[player.col][player.row - 1];
                }
                break;

            case DOWN:
                if (!player.bottomWall) {
                    player = cells[player.col][player.row + 1];
                }
                break;

            case LEFT:
                if (!player.leftWall) {
                    player = cells[player.col - 1][player.row];
                }
                break;

            case RIGHT:
                if (!player.rightWall) {
                    player = cells[player.col + 1][player.row];
                }
                break;
        }
    }

    /**
     * method gets the neighbours of the cell
     * used to create maze
     * @param cell cell which neighbours we are getting
     */
    private Cell getNeighbour(Cell cell) {
        ArrayList<Cell> neighbours = new ArrayList<>();

        //checking left neighbour
        if (cell.col > 0) {
            if (!cells[cell.col - 1][cell.row].visited) {
                neighbours.add(cells[cell.col - 1][cell.row]);
            }
        }

        //checking right neighbour
        if (cell.col < cols - 1) {
            if (!cells[cell.col + 1][cell.row].visited) {
                neighbours.add(cells[cell.col + 1][cell.row]);
            }
        }

        //checking top neighbour
        if (cell.row > 0) {
            if (!cells[cell.col][cell.row - 1].visited) {
                neighbours.add(cells[cell.col][cell.row - 1]);
            }
        }

        //checking bottom neighbour
        if (cell.row < rows - 1) {
            if (!cells[cell.col][cell.row + 1].visited) {
                neighbours.add(cells[cell.col][cell.row + 1]);
            }
        }

        if (neighbours.size() > 0) {
            int index = random.nextInt(neighbours.size());
            return neighbours.get(index);
        } else {
            return null;
        }
    }

    /**
     * removes wall to make path
     */
    private void removeWall(Cell current, Cell next) {
        if (current.col == next.col && current.row == next.row + 1) {
            current.topWall = false;
            next.bottomWall = false;
        }

        if (current.col == next.col && current.row == next.row - 1) {
            current.bottomWall = false;
            next.topWall = false;
        }

        if (current.col == next.col + 1 && current.row == next.row) {
            current.leftWall = false;
            next.rightWall = false;
        }

        if (current.col == next.col - 1 && current.row == next.row) {
            current.rightWall = false;
            next.leftWall = false;
        }
    }
}
