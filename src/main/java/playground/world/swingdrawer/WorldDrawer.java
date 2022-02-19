package playground.world.swingdrawer;

import playground.world.Sheep;
import playground.world.Util;
import playground.world.Wolf;
import playground.world.World;

import javax.swing.*;
import java.awt.*;

public class WorldDrawer extends JComponent {

    public static final int OFFSET_X = 10;
    public static final int OFFSET_Y = 10;
    private final int cellSize;

    public void setWorld(World world) {
        this.world = world;
    }

    private World world;

    public WorldDrawer(int cellSize) {
        this.cellSize = cellSize;
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        Shapes.drawRect(g, OFFSET_X, OFFSET_Y, world.xSize * cellSize, world.ySize * cellSize, Color.LIGHT_GRAY, Color.LIGHT_GRAY);
        Shapes.drawGrid(g, OFFSET_X, OFFSET_Y, cellSize, world.xSize, world.ySize);
        world.stream().forEach(worldObject -> {

            if (worldObject instanceof Wolf) {
                Shapes.drawCircle(g,
                        OFFSET_X + worldObject.x * cellSize,
                        OFFSET_Y + worldObject.y * cellSize,
                        cellSize,
                        Color.BLUE,
                        Color.BLUE);
            } else if (worldObject instanceof Sheep) {
                Shapes.drawCircle(g,
                        OFFSET_X + worldObject.x * cellSize,
                        OFFSET_Y + worldObject.y * cellSize,
                        cellSize,
                        Color.WHITE,
                        Color.GREEN);
                paintView(g, (Sheep) worldObject);
                int globalTime = ((Sheep) worldObject).getNeuralNetworkGlobalTime();
                Shapes.drawText(g, globalTime, 20, 40, Color.WHITE);
            }
        });
    }



    private void paintView(Graphics g, Sheep agent) {

        int tlx;
        int tly;
        int rbx;
        int rby;

        tlx = Util.gwc(world.xSize, agent.x + agent.tlViewX);
        tly = Util.gwc(world.ySize, agent.y + agent.tlViewY);
        rbx = Util.gwc(world.xSize, agent.x + agent.rbViewX + 1);
        rby = Util.gwc(world.ySize, agent.y + agent.rbViewY + 1);

        Color color = Color.WHITE;
        if (agent.hasPain()){
            color = Color.RED;
        }

//        g.setColor(Color.BLUE);
//        g.drawString(agent.getDirection().toString(), xStart + cellSize * agent.x, yStart + cellSize * agent.y);

        if (tlx >= rbx) {
            if (tly >= rby) {
                paintViewBorder(g, color, 0, 0, rbx, rby);
                paintViewBorder(g, color, tlx, tly, world.xSize, world.ySize);
                paintViewBorder(g, color, 0, tly, rbx, world.ySize);
                paintViewBorder(g, color, tlx, 0, world.xSize, rby);

            } else {
                paintViewBorder(g, color, tlx, tly, world.xSize, rby);
                paintViewBorder(g, color, 0, tly, rbx, rby);
            }
        } else {
            if (tly >= rby) {
                paintViewBorder(g, color, tlx, tly, rbx, world.ySize);
                paintViewBorder(g, color, tlx, 0, rbx, rby);
            } else {
                paintViewBorder(g, color, tlx, tly, rbx, rby);
            }
        }

    }

    private void paintViewBorder(Graphics g, Color color, int tlx, int tly, int rbx, int rby) {

        if (tlx == rbx || tly == rby) {
            return;
        }

        g.setColor(color);
        int sizeX = (rbx - tlx) * cellSize;
        int sizeY = (rby - tly) * cellSize;
        g.drawRect(OFFSET_X + cellSize * tlx, OFFSET_Y + cellSize * tly, sizeX, sizeY);
    }


}
