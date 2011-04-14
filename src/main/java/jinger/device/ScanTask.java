/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package jinger.device;

import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import javax.imageio.ImageIO;
import org.apache.pivot.util.concurrent.Task;
import org.apache.pivot.util.concurrent.TaskExecutionException;

/**
 *
 * @author thiago
 */
public class ScanTask extends Task<BufferedImage> {

    private DigitalDevice device;

    public ScanTask(DigitalDevice device) {
        this.device = device;
    }

    @Override
    public BufferedImage execute() throws TaskExecutionException {
        try {
            ByteArrayOutputStream bos = new ByteArrayOutputStream();
            byte[] template = device.enroll(bos);
            templateAction(template);
            BufferedImage bim = ImageIO.read(new ByteArrayInputStream(bos.toByteArray()));
            return bim;
        } catch (IOException ioe) {
            throw new TaskExecutionException(ioe);
        }
    }

    public void templateAction(byte[] template) {
        //System.out.println("Template: " + Arrays.toString(template));
    }
}
