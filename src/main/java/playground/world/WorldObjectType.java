package playground.world;

public enum WorldObjectType {
    NONE(0), WOLF(1), SHEEP(2);

    public int getValue() {
        return value;
    }

    private final int value;

    WorldObjectType(int value) {
        this.value = value;
    }
}
