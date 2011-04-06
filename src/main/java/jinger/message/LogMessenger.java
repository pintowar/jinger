/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package jinger.message;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author thiago
 */
public class LogMessenger implements Messenger {

    private Logger logger;

    public LogMessenger(Class clazz) {
        logger = LoggerFactory.getLogger(clazz);
    }

    @Override
    public void message(String message) {
        logger.info(message);
    }
}
