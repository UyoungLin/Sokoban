package com.sokoban.logger;

import com.sokoban.game.StartMeUp;
import java.io.File;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.logging.FileHandler;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

/**
 * The class GameLogger produces a log file and stores it.
 * @author Yuyang LIN-modified
 */
public class GameLogger extends Logger {

    private static GameLogger m_instance;
    private static Logger m_logger = Logger.getLogger("GameLogger");
    private DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
    private Calendar calendar = Calendar.getInstance();

    /**
     * Constructor to initialize the log file.
     * @throws IOException
     */
    private GameLogger() throws IOException {
        super("GameLogger", null);

        File directory = new File(System.getProperty("user.dir") + "/" + "logs");
        directory.mkdirs();

        FileHandler fh = new FileHandler(directory + "/" + StartMeUp.GAME_NAME + ".log");
        m_logger.addHandler(fh);
        SimpleFormatter formatter = new SimpleFormatter();
        fh.setFormatter(formatter);
    }

    /**
     * A public method to get the instance of the class.
     * (Singleton Pattern)
     * @return {@code m_instance}   the instance of the class
     * @throws IOException  IO is not available
     */
    public static synchronized GameLogger getInstance() throws IOException {
        if (m_instance == null) {
            m_instance = new GameLogger();
        }
        return m_instance;
    }

    /**
     * Create a message with time and error messages on it.
     * @param message   the error message string
     * @return {@code dateFormat.format(calendar.getTime()) + " -- " + message}
     *      a message with time and error messages on it
     */
    private String createFormattedMessage(String message) {
        return dateFormat.format(calendar.getTime()) + " -- " + message;
    }

    /**
     * Log an INFO message.
     * @param message   A string of message
     */
    public void info(String message) {
        m_logger.info(createFormattedMessage(message));
    }

    /**
     * Log an WARNING message.
     * @param message   A string of message
     */
    public void warning(String message) {
        m_logger.warning(createFormattedMessage(message));
    }

    /**
     * Log a SEVERE message.
     * @param message   A string of message
     */
    public void severe(String message) {
        m_logger.severe(createFormattedMessage(message));
    }
}