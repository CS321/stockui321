/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package System;

import java.io.IOException;
import java.util.logging.FileHandler;
import java.util.logging.Formatter;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

/**
 *
 * @author treybriggs
 */
public class LogHandler {

    static private FileHandler fileTxt;
    static private SimpleFormatter formatterTxt;

    /**
     *
     * @throws IOException
     */
    static public void setup() throws IOException {
        Logger logger = Logger.getLogger(Logger.GLOBAL_LOGGER_NAME);
        logger.setLevel(Level.INFO);
        fileTxt = new FileHandler("stocks.log");
        formatterTxt = new SimpleFormatter();
        fileTxt.setFormatter(formatterTxt);
        logger.setUseParentHandlers(false);
        logger.addHandler(fileTxt);
    }
}
