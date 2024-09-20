package juego;

import javax.swing.*;

public class Camara {
    private int x, y;
    private int viewWidth, viewHeight;
    private int levelWidth;

    public Camara(int x, int y, int viewWidth, int viewHeight, int levelWidth) {
        this.x = x;
        this.y = y;
        this.viewWidth = viewWidth;
        this.viewHeight = viewHeight;
        this.levelWidth = levelWidth;
    }

    public void update(int playerX) {
        x = playerX - viewWidth / 2;
        if (x < 0) x = 0; // Don't scroll past the left edge
        if (x > levelWidth - viewWidth) x = levelWidth - viewWidth; // Don't scroll past the right edge
    }

    public int getX() {
        return x;
    }
    
    public int getViewWidth() {
        return viewWidth;
    }
    
    public int getViewHeight() {
        return viewHeight;
    }

    public void reset() {
        x = 0;
    }
}
