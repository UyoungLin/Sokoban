package com.sokoban.game;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.awt.*;
import static org.junit.jupiter.api.Assertions.*;

class GameGridTest {

    private final int SIZE = 12;
    private final int NP = 11;
    private final int DE = 1;
    Point source;
    Point delta;
    GameGrid gameGrid = new GameGrid(SIZE, SIZE);
    GameObject obj = GameObject.fromChar('W');
    GameObject obj2 = GameObject.fromChar('S');

    @BeforeEach
    void setUp() {
        source = new Point(NP, NP);
        delta = new Point(DE,DE);
        gameGrid.putGameObjectAt(obj2, NP, NP);
    }

    @Test
    void testTranslatePoint() {
        Point process = GameGrid.translatePoint(source, delta);
        assertEquals(process, new Point(SIZE, SIZE));
    }

    @Test
    void testPutGameObjectAt() {
        boolean put = gameGrid.putGameObjectAt(obj, NP, NP);
        assertTrue(put);
    }

    @Test
    void testGetGameObjectAt() {
        assertEquals(gameGrid.getGameObjectAt(NP, NP), obj2);
    }

    @Test
    void removeGameObjectAt() {
        gameGrid.removeGameObjectAt(new Point(NP, NP));
        assertEquals(gameGrid.getGameObjectAt(NP, NP), null);
    }
}