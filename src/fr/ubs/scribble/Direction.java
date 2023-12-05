package fr.ubs.scribble;

/**
 * A direction for the shapes drawing, resizing, or moving
 * 
 * @author Pascale Launay
 */
public enum Direction
{
    /** north direction */
    NORTH, 
    /** south direction */
    SOUTH, 
    /** east direction */
    EAST, 
    /** west direction */
    WEST,
    /** north-east direction */
    NORTH_EAST, 
    /** north-west direction */
    NORTH_WEST,
    /** south-east direction */
    SOUTH_EAST, 
    /** south-west direction */
    SOUTH_WEST;

    /**
     * Check whether the direction has a 'north' component
     * 
     * @return true if the direction is N, NW or NE
     */
    public boolean isNorth()
    {
        return this == NORTH || this == NORTH_WEST || this == NORTH_EAST;
    }

    /**
     * Check whether the direction has a 'south' component
     * 
     * @return true if the direction is S, SW or SE
     */
    public boolean isSouth()
    {
        return this == SOUTH || this == SOUTH_WEST || this == SOUTH_EAST;
    }

    /**
     * Check whether the direction has a 'west' component
     * 
     * @return true if the direction is W, NW or SW
     */
    public boolean isWest()
    {
        return this == WEST || this == NORTH_WEST || this == SOUTH_WEST;
    }
    
    /**
     * Check whether the direction has a 'east' component
     * 
     * @return true if the direction is E, NE or SE
     */
    public boolean isEast()
    {
        return this == EAST || this == NORTH_EAST || this == SOUTH_EAST;
    }
}
