/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package jinger.device;

import java.io.IOException;
import java.io.OutputStream;
import jinger.message.Messenger;

/**
 *
 * @author thiago
 */
public interface DigitalDevice {

    public byte[] enroll(OutputStream output) throws IOException;

    public boolean verify(byte[] bytes, OutputStream output) throws IOException;

    public Messenger getMessenger();
}
