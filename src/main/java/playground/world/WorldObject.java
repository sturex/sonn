package playground.world;

import java.util.Random;

public abstract class WorldObject {
    public int x;
    public int y;
    protected final WorldObjectType objectType;

    protected final Random random;

    public WorldObject(WorldObjectType objectType, long seed) {
        this.objectType = objectType;
        random = new Random(seed);
    }

    public abstract void bump(WorldObject targetObject, Action targetObjectAction);

    public abstract void onBumped(WorldObject hitterObject, Action hitterObjectAction);

    public abstract Action makeDecision(World world);
}
