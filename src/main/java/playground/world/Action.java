package playground.world;

import java.util.Random;

public enum Action {
    UP, LEFT, RIGHT, DOWN, STAND;

    public Action reversed(){
        return switch (this){
            case UP -> DOWN;
            case LEFT -> RIGHT;
            case RIGHT -> LEFT;
            case DOWN -> UP;
            case STAND -> STAND;
        };
    }

    public static Action getRandom(Random random) {
        return values()[(Math.abs(random.nextInt()) % values().length)];
    }
}
