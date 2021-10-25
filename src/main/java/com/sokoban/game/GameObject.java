package com.sokoban.game;

/**
 * The enum GameObject shows all the possible values of the game object.
 * @author Yuyang LIN-modified
 */
public enum GameObject {

    /** The wall in the game */
    WALL('W'),

    /** The floor in the game */
    FLOOR(' '),

    /** The crate in the game, for the keeper to move */
    CRATE('C'),

    /** The diamond in the game, the target to reach */
    DIAMOND('D'),

    /** The keeper in the game, the character for user to control */
    KEEPER('S'),

    /** User put the crate on the diamond in the game */
    CRATE_ON_DIAMOND('O'),

    /** The debug object in the game */
    DEBUG_OBJECT('=');

    private final char symbol;

    /**
     * Constructor to initialize the GameObject.
     * @param symbol
     */
    GameObject(final char symbol) {
        this.symbol = symbol;
    }

    /**
     * Deal with the lower case symbol.
     * @param c     Input char
     * @return {@code t} if == symbol, else {@code WALL}
     */
    public static GameObject fromChar(char c) {
        for (GameObject t : GameObject.values()) {
            if (Character.toUpperCase(c) == t.symbol) {
                return t;
            }
        }

        return WALL;
    }

    /**
     * Get the string representation of symbol.
     * @return string representation
     */
    public String getStringSymbol() {
        return String.valueOf(symbol);
    }

    /**
     * Get the char representation of symbol.
     * @return symbol
     */
    public char getCharSymbol() {
        return symbol;
    }
}