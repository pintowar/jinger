/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package jinger.dev;

/**
 *
 * @author thiago
 */
public class OpenException extends Exception{

    public OpenException() {
        super();
    }
    public OpenException(String message) {
        super(message);
    }
    public OpenException(Throwable cause) {
        super(cause);
    }
    public OpenException(String message, Throwable cause) {
        super(message, cause);
    }

}
