package playground.world;

import playground.world.swingdrawer.WorldDrawer;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.stream.Stream;

public class World {

    private final WorldObject[][] gridObjects;
    private final List<WorldObject> listObjects = new ArrayList<>();
    public final int xSize;
    public final int ySize;
    private WorldDrawer drawer;
    private int sleep = 0;

    private World(int xSize, int ySize) {
        this.xSize = xSize;
        this.ySize = ySize;
        gridObjects = new WorldObject[xSize][ySize];
    }

    private void put(WorldObject worldObject, int x, int y) {
        assert isEmpty(x, y);
        assert !listObjects.contains(worldObject);
        listObjects.add(worldObject);
        gridObjects[x][y] = worldObject;
        worldObject.x = x;
        worldObject.y = y;
    }

    private boolean isEmpty(int x, int y) {
        return get(x, y) == null;
    }

    private void moveTo(WorldObject worldObject, int x, int y) {
        assert isEmpty(x, y);
        assert get(worldObject.x, worldObject.y) == worldObject;
        gridObjects[worldObject.x][worldObject.y] = null;
        gridObjects[x][y] = worldObject;
        worldObject.x = x;
        worldObject.y = y;
    }

    private WorldObject get(int x, int y) {
        return gridObjects[x][y];
    }

    private WorldObjectType getType(int x, int y) {
        return gridObjects[x][y] == null ? WorldObjectType.NONE : gridObjects[x][y].objectType;
    }

    public void map(int tlX, int tlY, WorldObjectType[][] view) {
        tlX = Util.gwc(xSize, tlX);
        tlY = Util.gwc(ySize, tlY);

        for (int i = 0; i < view.length; i++) {
            for (int j = 0; j < view[i].length; j++) {
                view[i][j] = getType(Util.gwc(xSize, tlX + i), Util.gwc(ySize, tlY + j));
            }
        }

    }

    private final Iterator<WorldObject> iterator = new Iterator<>() {
        private int curPos = 0;

        @Override
        public boolean hasNext() {
            if (curPos >= listObjects.size()) {
                curPos = 0;
                drawer.repaint();
                if (sleep != 0) {
                    try {
                        Thread.sleep(sleep);
                    } catch (InterruptedException ignored) {
                    }
                }
            }
            return !listObjects.isEmpty();
        }

        @Override
        public WorldObject next() {
            return listObjects.get(curPos++);
        }
    };

    private void iterate() {
        while (iterator.hasNext()) {
            WorldObject worldObject = iterator.next();
            Action action = worldObject.makeDecision(this);
            if (action != Action.STAND) {
                int intentionX = getTargetX(worldObject.x, action);
                int intentionY = getTargetY(worldObject.y, action);
                if (isEmpty(intentionX, intentionY)) {
                    moveTo(worldObject, intentionX, intentionY);
                } else {
                    WorldObject targetObject = get(intentionX, intentionY);
                    targetObject.onBumped(worldObject, action);
                    worldObject.bump(targetObject, action);
                }
            }
        }
    }

    /**
     * Get target X coordinate for next move
     *
     * @param x      current X coordinate
     * @param action Action returned from decision making system
     * @return target X coordinate for next move
     */
    private int getTargetX(int x, Action action) {
        if (action == Action.LEFT) {
            return Util.gwc(xSize, x - 1);
        } else if (action == Action.RIGHT) {
            return Util.gwc(xSize, x + 1);
        }
        return x;
    }

    /**
     * Get target Y coordinate for next move
     *
     * @param y      current Y coordinate
     * @param action Action returned from decision making system
     * @return target Y coordinate for next move
     */
    private int getTargetY(int y, Action action) {
        if (action == Action.UP) {
            return Util.gwc(ySize, y - 1);
        } else if (action == Action.DOWN) {
            return Util.gwc(ySize, y + 1);
        }
        return y;
    }

    public static Builder newBuilder(int xSize, int ySize) {
        return new Builder(xSize, ySize);
    }

    private void setDrawer(WorldDrawer drawer) {
        this.drawer = drawer;
    }

    public Stream<WorldObject> stream() {
        return listObjects.stream();
    }

    public static class Builder {

        private final World world;

        public Builder(int xSize, int ySize) {
            world = new World(xSize, ySize);
        }

        public Builder addObject(WorldObject object, int x, int y) {
            if (world.isEmpty(x, y)) {
                world.put(object, x, y);
            }
            return this;
        }

        public void build(WorldDrawer worldDrawer) {
            worldDrawer.setWorld(world);
            world.setDrawer(worldDrawer);
            world.iterate();
        }

        public Builder withSleep(int sleep) {
            world.setSleep(sleep);
            return this;
        }
    }

    private void setSleep(int sleep) {
        this.sleep = sleep;
    }

}
