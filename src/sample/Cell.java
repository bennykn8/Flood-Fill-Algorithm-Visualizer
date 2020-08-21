package sample;

import javafx.scene.layout.Pane;
import javafx.scene.shape.Rectangle;

public class Cell extends Pane{
    int x, y;
    Rectangle rect;

    public Cell(int x, int y, char c){
        this.x = x;
        this.y = y;

        rect = new Rectangle();
        rect.setHeight(20);
        rect.setWidth(20);
        rect.setId(Character.toString(c));
        getChildren().addAll(rect);

        rect.setOnMouseClicked(event -> Main.animation(this, x, y));
        rect.setOnMouseEntered(event -> Main.handleCursor(this));
    }

    public Rectangle getRect() {
        return rect;
    }
    public int getX() {
        return x;
    }
    public int getY() {
        return y;
    }
}