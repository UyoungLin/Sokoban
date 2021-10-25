package com.sokoban.game;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.net.URISyntaxException;

import static org.junit.jupiter.api.Assertions.*;

class GraphicObjectTest {

    private final int D_UP = 0;

    @Test
    void testGiveDirection() {
        GraphicObject.giveDirection((short) D_UP);
        assertEquals(GraphicObject.direction, D_UP);
    }

}