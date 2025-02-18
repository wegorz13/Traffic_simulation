package model;

public enum MoveDirection {
    NORTH,
    EAST,
    WEST,
    SOUTH;

    public MoveDirection next(){
        return switch (this){
            case NORTH ->  MoveDirection.EAST;
            case WEST ->  MoveDirection.NORTH;
            case SOUTH ->  MoveDirection.WEST;
            case EAST ->  MoveDirection.SOUTH;
        };
    }

    public MoveDirection previous(){
        return switch (this){
            case NORTH ->MoveDirection.WEST;
            case WEST ->  MoveDirection.SOUTH;
            case SOUTH -> MoveDirection.EAST;
            case EAST ->  MoveDirection.NORTH;
        };

    }
    
    public static MoveDirection fromString(String string){
        return switch (string){
            case "north" -> MoveDirection.NORTH;
            case "east" -> MoveDirection.EAST;
            case "west" -> MoveDirection.WEST;
            case "south" -> MoveDirection.SOUTH;

            default -> throw new IllegalStateException("Unexpected value: " + string);
        };
    }
}
