package nl.saxion.playground.squarerootofpopeye.shisharun.entity;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;

import nl.saxion.playground.squarerootofpopeye.R;

import static android.graphics.Bitmap.createScaledBitmap;

public class GameView extends View {
    private Maze maze;
    private Paint wallPaint, playerPaint, exitPaint;
    private float hMargin;                              // height margin between maze and canvas end
    private float vMargin;                              // width margin between maze and canvas end
    private static int lastAssignedCols = 6;
    private static int lastAssignedRows = 8;
    private static final int maxColLimit = 10;
    private static final int maxRowLimit = 12;
    private int cols;
    private int rows;
    Bitmap playerChosenCharacter;
    Bitmap playerImage;
    private Bitmap homeBitmap;
    private Bitmap scaledHomeBitmap;

    public GameView(Context context, AttributeSet attrs) {
        super(context, attrs);

        cols = lastAssignedCols;
        rows = lastAssignedRows;
        maze = new Maze(cols, rows);

        if (lastAssignedCols < maxColLimit && lastAssignedRows < maxRowLimit) {
            lastAssignedCols += 1;
            lastAssignedRows += 1;
        }

        setUpPaintProperties();
        playerChosenCharacter = BitmapFactory.decodeResource(context.getResources(), R.drawable.adventurer_idle);
        homeBitmap = BitmapFactory.decodeResource(context.getResources(), R.drawable.house);

        maze.createMaze();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        canvas.drawColor(Color.BLACK);

        int width = getWidth();
        int height = getHeight();
        int cols = maze.getCols();
        int rows = maze.getRows();

        if ((width / height) < (cols / rows)) {
            maze.setCellSize(width / (cols + 1.0f));
        } else {
            maze.setCellSize(height / (rows + 1.0f));

            hMargin = ((width - cols * maze.getCellSize()) / 2);
            vMargin = ((height - rows * maze.getCellSize()) / 2);

            canvas.translate(hMargin, vMargin);

            float cellSize = maze.getCellSize();

            drawMaze(canvas, cols, rows, cellSize);

            float margin = cellSize / 10;

            playerImage = createScaledBitmap(playerChosenCharacter,
                    (int) (cellSize - margin),
                    (int) (cellSize - margin),
                    true);

            canvas.drawBitmap(playerImage,
                    maze.getPlayer().col * cellSize + margin,
                    maze.getPlayer().row * cellSize + margin,
                    null);

            scaledHomeBitmap = createScaledBitmap(homeBitmap,
                    (int) (cellSize - margin),
                    (int) (cellSize - margin),
                    true);

            canvas.drawBitmap(scaledHomeBitmap,
                    maze.getExit().col * cellSize + margin,
                    maze.getExit().row * cellSize + margin,
                    null);
        }
    }

    public boolean atExit() {
        return maze.isWin();
    }

    public void updatePlayer(PlayerControl direction) {
        switch (direction) {
            case UP:
                maze.movePlayer(direction);
                break;

            case DOWN:
                maze.movePlayer(direction);
                break;

            case LEFT:
                maze.movePlayer(direction);
                break;

            case RIGHT:
                maze.movePlayer(direction);
                break;
        }
        maze.checkExit();
        invalidate();
    }

    /**
     * draws the maze
     * @param canvas canvas to draw
     * @param cols   number of cols in maze
     * @param rows   number of rows in maze
     * @param cellSize  size of each cell
     */
    private void drawMaze(Canvas canvas, int cols, int rows, float cellSize) {
        for (int i = 0; i < cols; i++) {
            for (int j = 0; j < rows; j++) {
                if (maze.getCells()[i][j].topWall) {
                    canvas.drawLine(i * cellSize, j * cellSize, (i + 1) * cellSize, j * cellSize, wallPaint);
                }

                if (maze.getCells()[i][j].leftWall) {
                    canvas.drawLine(i * cellSize, j * cellSize, i * cellSize, (j + 1) * cellSize, wallPaint);
                }

                if (maze.getCells()[i][j].bottomWall) {
                    canvas.drawLine(i * cellSize, (j + 1) * cellSize, (i + 1) * cellSize, (j + 1) * cellSize, wallPaint);
                }

                if (maze.getCells()[i][j].rightWall) {
                    canvas.drawLine((i + 1) * cellSize, j * cellSize, (i + 1) * cellSize, (j + 1) * cellSize, wallPaint);
                }
            }
        }
    }

    private void setUpPaintProperties() {
        wallPaint = new Paint();
        wallPaint.setColor(Color.WHITE);
        wallPaint.setStrokeWidth(Maze.wallThickness);
        playerPaint = new Paint();
        playerPaint.setColor(Color.WHITE);
        playerPaint.setStyle(Paint.Style.STROKE);
        exitPaint = new Paint();
        exitPaint.setColor(Color.YELLOW);
    }
}
