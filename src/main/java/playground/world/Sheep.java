package playground.world;

import neural.Network;
import neural.NetworkEventsListener;
import org.apache.commons.math3.util.Pair;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.EnumSet;
import java.util.List;
import java.util.function.BooleanSupplier;

public class Sheep extends WorldObject {

    public static final int DIRECTION_UP = 0;
    public static final int DIRECTION_RIGHT = 1;
    public static final int DIRECTION_DOWN = 2;
    public static final int DIRECTION_LEFT = 3;
    private final WorldObjectType[][] view;
    public final int rbViewY;
    public final int tlViewY;
    public final int tlViewX;
    public final int rbViewX;
    public final int viewXSize;
    public final int viewYSize;
    private final Network network;
    private final boolean[] muscles = new boolean[4];
    private final boolean[] pain = new boolean[4];

    public Sheep(long seed, int tlViewX, int tlViewY, int rbViewX, int rbViewY, int limitSize, List<NetworkEventsListener> listeners) {
        super(WorldObjectType.SHEEP, seed);
        this.rbViewY = rbViewY;
        this.tlViewY = tlViewY;
        this.tlViewX = tlViewX;
        this.rbViewX = rbViewX;

        viewXSize = rbViewX - tlViewX + 1;
        viewYSize = rbViewY - tlViewY + 1;
        view = new WorldObjectType[viewXSize][viewYSize];

        List<BooleanSupplier> additionalInputs = new ArrayList<>();
        List<Pair<BooleanSupplier, Runnable>> reflexes = new ArrayList<>();

        for (WorldObjectType worldObjectType : EnumSet.of(WorldObjectType.WOLF)) {
            for (int i = 0; i < view.length; i++) {
                for (int j = 0; j < view[i].length; j++) {
                    int xPos = i;
                    int yPos = j;
                    BooleanSupplier booleanSupplier = () -> view[xPos][yPos] == worldObjectType;
                    if (xPos == -tlViewX && yPos == -tlViewY - 1 && worldObjectType == WorldObjectType.WOLF) {
                        reflexes.add(Pair.create(booleanSupplier, () -> muscles[DIRECTION_DOWN] = true));
                    } else if (xPos == -tlViewX + 1 && yPos == -tlViewY && worldObjectType == WorldObjectType.WOLF) {
                        reflexes.add(Pair.create(booleanSupplier, () -> muscles[DIRECTION_LEFT] = true));
                    } else if (xPos == -tlViewX && yPos == -tlViewY + 1 && worldObjectType == WorldObjectType.WOLF) {
                        reflexes.add(Pair.create(booleanSupplier, () -> muscles[DIRECTION_UP] = true));
                    } else if (xPos == -tlViewX - 1 && yPos == -tlViewY && worldObjectType == WorldObjectType.WOLF) {
                        reflexes.add(Pair.create(booleanSupplier, () -> muscles[DIRECTION_RIGHT] = true));
                    } else {
                        additionalInputs.add(booleanSupplier);
                    }
                }
            }
        }

        List<BooleanSupplier> trainingInputs = new ArrayList<>();
        for (int i = 0; i < pain.length; i++) {
            int pos = i;
            trainingInputs.add(() -> pain[pos]);
        }

        network = new Network(listeners, limitSize);

        additionalInputs.forEach(network::addReceptor);
        reflexes.forEach(r -> network.addReflex(r.getFirst(), r.getSecond()));
        trainingInputs.forEach(network::addPainReceptor);

    }

    public int getNeuralNetworkGlobalTime(){
        return network.getTimestamp();
    }

        /**
         * When this worldObject is bumping other worldObject with specified action
         *
         * @param targetObject       target object to hit
         * @param targetObjectAction action
         */
    @Override
    public void bump(WorldObject targetObject, Action targetObjectAction) {
        switch (targetObjectAction) {
            case UP -> pain[DIRECTION_UP] = true;
            case LEFT -> pain[DIRECTION_LEFT] = true;
            case RIGHT -> pain[DIRECTION_RIGHT] = true;
            case DOWN -> pain[DIRECTION_DOWN] = true;
            case STAND -> throw new IllegalStateException("It is not possible to hit something without moving.");
        }
    }

    /**
     * When this worldObject is hit by other worldObject with specified action
     *
     * @param hitterObject       hitter object
     * @param hitterObjectAction hitter's action
     */
    @Override
    public void onBumped(WorldObject hitterObject, Action hitterObjectAction) {
        switch (hitterObjectAction) {
            case UP -> pain[DIRECTION_DOWN] = true;
            case LEFT -> pain[DIRECTION_RIGHT] = true;
            case RIGHT -> pain[DIRECTION_LEFT] = true;
            case DOWN -> pain[DIRECTION_UP] = true;
            case STAND -> throw new IllegalStateException("It is not possible to hit something without moving.");
        }
    }

    @Override
    public Action makeDecision(World world) {
        Arrays.fill(muscles, false);
        world.map(x + tlViewX, y + tlViewY, view);
        network.tick();
        Arrays.fill(pain, false);
        return musclesToAction();
    }

    private Action musclesToAction() {
        if (muscles[DIRECTION_UP] && !muscles[DIRECTION_RIGHT] && !muscles[DIRECTION_DOWN] && !muscles[DIRECTION_LEFT]) {
            return Action.UP;
        } else if (!muscles[DIRECTION_UP] && muscles[DIRECTION_RIGHT] && !muscles[DIRECTION_DOWN] && !muscles[DIRECTION_LEFT]) {
            return Action.RIGHT;
        } else if (!muscles[DIRECTION_UP] && !muscles[DIRECTION_RIGHT] && muscles[DIRECTION_DOWN] && !muscles[DIRECTION_LEFT]) {
            return Action.DOWN;
        } else if (!muscles[DIRECTION_UP] && !muscles[DIRECTION_RIGHT] && !muscles[DIRECTION_DOWN] && muscles[DIRECTION_LEFT]) {
            return Action.LEFT;
        } else {
//            if (muscles[DIRECTION_UP] || muscles[DIRECTION_RIGHT] || muscles[DIRECTION_DOWN] || muscles[DIRECTION_LEFT]) {
//                pain[0] = true;
//            }
            return Action.STAND;
        }
    }

    public static Builder newBuilder(long seed, int tlViewX, int tlViewY, int rbViewX, int rbViewY) {
        return new Builder(seed, tlViewX, tlViewY, rbViewX, rbViewY);
    }

    public boolean hasPain() {
        for (boolean b : pain) {
            if (b){
                return true;
            }
        }
        return false;
    }

    public static class Builder {
        public final int rbViewY;
        public final int tlViewY;
        private final long seed;
        public final int tlViewX;
        public final int rbViewX;
        private int limitSize = 0;

        public Builder(long seed, int tlViewX, int tlViewY, int rbViewX, int rbViewY) {
            this.seed = seed;
            this.tlViewX = tlViewX;
            this.tlViewY = tlViewY;
            this.rbViewX = rbViewX;
            this.rbViewY = rbViewY;
        }

        public Builder withNetworkLimitSize(int limitSize) {
            this.limitSize = limitSize;
            return this;
        }

        public Sheep build(List<NetworkEventsListener> listeners) {
            return new Sheep(seed, tlViewX, tlViewY, rbViewX, rbViewY, limitSize, listeners);
        }
    }

}
