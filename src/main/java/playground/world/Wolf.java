package playground.world;

public class Wolf extends WorldObject {

    private Action action = Action.getRandom(random);

    public Wolf(long seed) {
        super(WorldObjectType.WOLF, seed);
    }

    public void bump(WorldObject targetObject, Action targetObjectAction) {
        action = action.reversed();
    }

    @Override
    public void onBumped(WorldObject hitterObject, Action hitterObjectAction) {
        if (action == Action.STAND) {
            action = hitterObjectAction;
        } else {
            action = action.reversed();
        }
    }

    @Override
    public Action makeDecision(World world) {
        assert action != null;
        return action;
    }
}
