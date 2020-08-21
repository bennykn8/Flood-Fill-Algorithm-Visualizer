package sample;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.scene.Cursor;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.util.ArrayDeque;
import java.util.Queue;

public class Main extends Application {

    static char[][] M;
    static GridPane matrix;

    static Queue<Cell> q;
    static int x, y;
    static int m, n;
    static char oldColor;
    static char newColor = 'R';

    static Timeline simulation;
    static boolean isAnimating = false;
    static int currCycle;
    static int nextCycle;
    static int rep;

    public static void fill(char newColor, Cell source){
        currCycle = 1;
        nextCycle = 0;
        rep = 0;

        m = M.length;
        n = M[0].length;

        q = new ArrayDeque<>();
        q.add(new Cell(x, y, newColor));

        oldColor = M[x][y];
        if(oldColor == newColor){
            return;
        }
        source.getRect().setId(Character.toString(newColor));

        fillClr = newColor;

        simulation = new Timeline(new KeyFrame(Duration.millis(500), Main::step));
        simulation.setCycleCount(Timeline.INDEFINITE);
        simulation.play();
        isAnimating = true;
    }

    static final int[] row = {-1, -1, -1, 0, 0, 1, 1, 1};
    static final int[] col = {-1, 0, 1, -1, 1, -1, 0, 1};
    static char fillClr;

    private static void step(ActionEvent actionEvent) {

        if (q.isEmpty()) {
            simulation.stop();
            isAnimating = false;
            return;
        }

        while (rep != currCycle) {
            rep++;

            Cell node = q.poll();
            x = node.getX();
            y = node.getY();
            M[x][y] = fillClr;

            for (int k = 0; k < row.length; k++)
                if (isValid(x + row[k], y + col[k])) {
                    q.add(new Cell(x + row[k], y + col[k], fillClr));
                    nextCycle++;
                }

        }

        for (Cell blk : q)
            for (Node thing : matrix.getChildren())
                if (GridPane.getColumnIndex(thing) == blk.getY() && GridPane.getRowIndex(thing) == blk.getX()) {
                    Cell c = (Cell)thing;
                    c.getRect().setId(Character.toString(fillClr));
                }

        rep = 0;
        currCycle = nextCycle;
        nextCycle = 0;
    }

    public static boolean isValid(int x, int y){
        boolean notInQueue = true;
        for(Cell p: q){
            if (p.getX() == x && p.getY() == y) {
                notInQueue = false;
            }
        }
        return x >= 0 && x < m && y >= 0 && y < n && M[x][y] == oldColor && notInQueue;
    }

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws Exception{

        HBox topSelect = new HBox();
        topSelect.setId("top");
        ToggleGroup clrSelect = new ToggleGroup();

        String[] labelText = {"Red ","Orange ","Yellow ","Green ","Blue ","Purple "};
        String[] btnId = {"R","O","Y","G","B","P"};

        for(int i = 0; i < 6; i++){
            Label label = new Label(labelText[i]);
            label.setId(btnId[i]);
            RadioButton radBtn = new RadioButton(btnId[i]);
            radBtn.setToggleGroup(clrSelect);
            if(i == 0)
                radBtn.setSelected(true);

            topSelect.getChildren().addAll(label, radBtn);
        }

        clrSelect.selectedToggleProperty().addListener((observable, oldValue, newValue) -> {
                RadioButton rb = (RadioButton)clrSelect.getSelectedToggle();
                newColor = rb.getText().charAt(0);
        });

        M = new char[][]{
                "YYYYYGGPPBBBBBB".toCharArray(),
                "YYYYYYGPPBBOOOO".toCharArray(),
                "GGGGGGGPPPBBOOO".toCharArray(),
                "OOOOOGGGGPPBBBB".toCharArray(),
                "ORRRRRGPPPPPPRR".toCharArray(),
                "OOORRGGPPPRRRRR".toCharArray(),
                "OBORRRRRPPRRGGG".toCharArray(),
                "OBBBBRRPPPGGGOO".toCharArray(),
                "OBBPBBBPPPGOOOO".toCharArray(),
                "RRRPPPPPGGGGGGG".toCharArray(),
                "RPPPGGGGGGGRRRR".toCharArray(),
                "RRRRRPPPPPYYRRR".toCharArray(),
                "RRRPPPBBBBYYYYY".toCharArray(),
                "OOOOOOOBBYYYYYY".toCharArray(),
                "OOOOOOOOOOOOYYY".toCharArray(),
        };

        System.out.println();
        matrix = new GridPane();
        matrix.setVgap(3);
        matrix.setHgap(3);
        matrix.setId("matrix");
        setMatrix(matrix, M);

        VBox layout = new VBox();
        layout.getChildren().addAll(topSelect, matrix);

        Scene scene = new Scene(layout, 500, 500);
        scene.getStylesheets().add("sample/style.css");
        primaryStage.setTitle("Flood Fill Algorithm Visualizer");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    public static void setMatrix(GridPane mat, char[][] M){
        for(int i = 0; i < M.length; i++){
            for(int j = 0; j < M[0].length; j++){
                Cell node = new Cell(i, j, M[i][j]);
                mat.add(node, j, i);
            }
        }
    }

    public static void animation(Cell cell, int X, int Y){
        x = X;
        y = Y;
        if(!isAnimating)
            fill(newColor, cell);
    }

    public static void handleCursor(Cell cell){
        if(!isAnimating)
            cell.setCursor(Cursor.HAND);
        else
            cell.setCursor(Cursor.DEFAULT);
    }
}