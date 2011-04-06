package jinger.ui;

import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;
import jinger.dev.DigitalDevice;
import org.apache.pivot.wtk.BoxPane;
import org.apache.pivot.wtk.Button;
import org.apache.pivot.wtk.ButtonPressListener;
import org.apache.pivot.wtk.HorizontalAlignment;
import org.apache.pivot.wtk.ImageView;
import org.apache.pivot.wtk.Orientation;
import org.apache.pivot.wtk.PushButton;
import org.apache.pivot.wtk.media.Image;
import org.apache.pivot.wtk.media.Picture;

/**
 *
 * @author thiago
 */
public class Enroll extends BoxPane {

    private final int imageWidth = 384;
    private final int imageHeight = 289;
    private ImageView imageView;
    private PushButton enrollButton;
    private DigitalDevice device;

    public Enroll(DigitalDevice device) {
        this.device = device;
        setOrientation(Orientation.VERTICAL);
        getStyles().put("horizontalAlignment", HorizontalAlignment.CENTER);
        imageView = new ImageView();
        imageView.setPreferredSize(imageWidth, imageHeight);
        add(imageView);
        enrollButton = new PushButton();
        enrollButton.setButtonData("Enroll");
        enrollButton.getButtonPressListeners().add(new ButtonPressListener() {

            @Override
            public void buttonPressed(Button button) {
                enrollAction(button);
            }
        });
        add(enrollButton);
    }

    private void enrollAction(Button button) {
        Image img;
        try {
            img = new Picture(bufferedImage());
            imageView.setImage(img);
        } catch (FileNotFoundException ex) {
            Logger.getLogger(Enroll.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(Enroll.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private BufferedImage bufferedImage() throws FileNotFoundException, IOException {
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        byte[] template = device.enroll(bos);
        templateAction(template);
        BufferedImage bim = ImageIO.read(new ByteArrayInputStream(bos.toByteArray()));
        return bim;
    }

    public void templateAction(byte[] template) {
        //System.out.println("Template size: " + template.length);
    }
}
