/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package jinger.dev;

/**
 *
 * @author thiago
 */
public class InitException extends Exception{

    public InitException() {
        super();
    }
    public InitException(String message) {
        super(message);
    }
    public InitException(Throwable cause) {
        super(cause);
    }
    public InitException(String message, Throwable cause) {
        super(message, cause);
    }

}
