package playground.world.swingdrawer;

import playground.world.Action;

import java.awt.*;

public class Shapes {

    public static void drawSquare(Graphics g, int x, int y, int size, Color color, Color outerColor) {
        g.setColor(color);
        g.fillRect(x + 2, y + 2, size - 2, size - 2);
        g.setColor(outerColor);
        g.drawRect(x + 1, y + 1, size - 1, size - 1);
    }

    public static void drawCircle(Graphics g, int x, int y, int size, Color color, Color outerColor) {
        g.setColor(outerColor);
        g.fillOval(x, y, size, size);
        g.setColor(color);
        g.fillOval(x + 1, y + 1, size - 2, size - 2);
    }

    public static void drawRect(Graphics g, int x, int y, int sizeX, int sizeY, Color color, Color outerColor) {
        g.setColor(color);
        g.fillRect(x + 1, y + 1, sizeX - 1, sizeY - 1);
        g.setColor(outerColor);
        g.drawRect(x, y, sizeX, sizeY);
    }

    public static void drawText(Graphics g, String text, int x, int y, Color color) {
        g.setColor(color);
        g.drawString(text, x, y);
    }

    public static void drawText(Graphics g, int value, int x, int y, Color color) {
        g.setColor(color);
        g.drawString(String.valueOf(value), x, y);
    }

    public static void drawDirectedTriangle(Graphics g, Action action, int x, int y, int size, Color color) {
        g.setColor(color);

        int[] xPoints = new int[4];
        int[] yPoints = new int[4];

        switch (action) {

            case UP -> {
                xPoints[0] = x + size / 2;
                yPoints[0] = y;
                xPoints[1] = x + size;
                yPoints[1] = y + size;
                xPoints[2] = x;
                yPoints[2] = y + size;
                xPoints[3] = xPoints[0];
                yPoints[3] = yPoints[0];
            }
            case LEFT -> {
                xPoints[0] = x;
                yPoints[0] = y + size / 2;
                xPoints[1] = x + size;
                yPoints[1] = y;
                xPoints[2] = x + size;
                yPoints[2] = y + size;
                xPoints[3] = xPoints[0];
                yPoints[3] = yPoints[0];
            }
            case DOWN -> {
                xPoints[0] = x + size / 2;
                yPoints[0] = y + size;
                xPoints[1] = x;
                yPoints[1] = y;
                xPoints[2] = x + size;
                yPoints[2] = y;
                xPoints[3] = xPoints[0];
                yPoints[3] = yPoints[0];
            }
            case RIGHT -> {
                xPoints[0] = x + size;
                yPoints[0] = y + size / 2;
                xPoints[1] = x;
                yPoints[1] = y + size;
                xPoints[2] = x;
                yPoints[2] = y;
                xPoints[3] = xPoints[0];
                yPoints[3] = yPoints[0];
            }
            default -> throw new IllegalStateException("Unexpected value: " + action);
        }

        g.drawPolyline(xPoints, yPoints, 4);
    }

    public static void drawGrid(Graphics g, int xStart, int yStart, int cellSize, int xDim, int yDim) {
        g.setColor(new Color(141, 141, 141));

        int xSize = xDim * cellSize;
        int ySize = yDim * cellSize;

        g.drawRect(xStart, yStart, xSize, ySize);
        //draw vertical lines
        for (int x = 1; x < xDim; x++) {
            g.drawLine(xStart + x * cellSize, yStart, xStart + x * cellSize, yStart + ySize);
        }
        //draw horizontal lines
        for (int y = 1; y < yDim; y++) {
            g.drawLine(xStart, yStart + y * cellSize, xStart + xSize, yStart + y * cellSize);
        }
    }

}
