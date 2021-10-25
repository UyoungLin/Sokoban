package com.sokoban.game;

import javafx.application.Platform;
import org.junit.BeforeClass;
import org.junit.jupiter.api.Test;

import java.awt.*;
import java.io.IOException;
import java.io.InputStream;

import static org.junit.jupiter.api.Assertions.*;

class LevelTest {

    InputStream inputStream;
    StartMeUp gameEngine;

    @BeforeClass
    public static void setup() {
        Platform.startup(() -> {});
    }

    @Test
    void testGetName() {
        inputStream = getClass().getClassLoader().getResourceAsStream("SampleGame.skb");
        gameEngine = StartMeUp.getInstance(inputStream);
        final String name = "Hello";
        assertEquals(gameEngine.getCurrentLevel().getName(), name);
        try {
            assert inputStream != null;
            inputStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    @Test
    void testGetIndex() {
        Platform.startup(() -> {
            inputStream = getClass().getClassLoader().getResourceAsStream("SampleGame.skb");
            gameEngine = StartMeUp.getInstance(inputStream);
            assertEquals(gameEngine.getCurrentLevel().getIndex(), 0);
            try {
                assert inputStream != null;
                inputStream.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        });

    }

    @Test
    void testGetKeeperPosition() {
        inputStream = getClass().getClassLoader().getResourceAsStream("SampleGame.skb");
        gameEngine = StartMeUp.getInstance(inputStream);
        final int x = 15;
        final int y = 8;
        assertEquals(gameEngine.getCurrentLevel().getKeeperPosition(), new Point(x, y));
        try {
            assert inputStream != null;
            inputStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Test
    void isComplete() {
        inputStream = getClass().getClassLoader().getResourceAsStream("SampleGame.skb");
        gameEngine = StartMeUp.getInstance(inputStream);
        boolean check = gameEngine.getCurrentLevel().isComplete();
        assertFalse(check);
        try {
            assert inputStream != null;
            inputStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}