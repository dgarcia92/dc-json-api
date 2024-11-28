package dgarcia.jsonapi.mapper;

public enum MaxDepth {
    NONE(0),
    ONE(1),
    TWO(2),
    THREE(3),
    FOUR(4),
    FIVE(5),
    SIX(6),
    SEVEN(7),
    EIGHT(8),
    NINE(9),
    TEN(10),
    ALL(Integer.MAX_VALUE);

    private final int depth;

    MaxDepth(int depth) {
        this.depth = depth;
    }

    public int getDepth() {
        return depth;
    }
}
