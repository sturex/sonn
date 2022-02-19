package playground.world;

public enum Util {
    ;

    public static int gwc(int size, int coord) {
        int ret = coord;

        if (coord < 0) {
            ret = size + coord;
        }
        if (coord > size - 1) {
            ret = coord - size;
        }

        return ret;
    }
}
