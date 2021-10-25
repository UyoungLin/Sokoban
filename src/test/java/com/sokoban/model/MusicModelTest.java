package com.sokoban.model;

import com.sokoban.game.StartMeUp;
import javafx.application.Platform;
import org.junit.jupiter.api.Test;
import java.io.InputStream;
import static org.junit.jupiter.api.Assertions.*;

class MusicModelTest {

    InputStream inputStream;
    StartMeUp gameEngine;

    @Test
    void testIsPlayingMusic() {
        Platform.startup(() -> {
            inputStream = getClass().getClassLoader().getResourceAsStream("SampleGame.skb");
            gameEngine = StartMeUp.getInstance(inputStream);
            boolean check = gameEngine.getMusic().isPlayingMusic();
            assertFalse(check);
        });
    }
}