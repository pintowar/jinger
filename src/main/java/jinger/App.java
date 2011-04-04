package jinger;

import jinger.dev.DigitalDevice;
import jinger.dev.FprintImpl;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * Hello world!
 *
 */
public class App {

    public static void main(String[] args) throws IOException {
        DigitalDevice device = new FprintImpl();
        byte[] baites = device.enroll(new FileOutputStream("/tmp/enroll.png"));
        
        boolean matched = false;
        while (!matched) {
            matched = device.verify(baites, new FileOutputStream("/tmp/verify.png"));
            System.out.println("Matched: " + matched);
        }
    }
}
