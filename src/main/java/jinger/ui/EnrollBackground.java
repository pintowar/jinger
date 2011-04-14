package jinger.ui;

import java.awt.image.BufferedImage;
import java.util.Arrays;
import jinger.device.DigitalDevice;
import jinger.device.ScanTask;
import org.apache.pivot.util.concurrent.Task;
import org.apache.pivot.util.concurrent.TaskListener;
import org.apache.pivot.wtk.ActivityIndicator;
import org.apache.pivot.wtk.BoxPane;
import org.apache.pivot.wtk.Button;
import org.apache.pivot.wtk.ButtonPressListener;
import org.apache.pivot.wtk.HorizontalAlignment;
import org.apache.pivot.wtk.ImageView;
import org.apache.pivot.wtk.Orientation;
import org.apache.pivot.wtk.PushButton;
import org.apache.pivot.wtk.StackPane;
import org.apache.pivot.wtk.TaskAdapter;
import org.apache.pivot.wtk.media.Picture;

/**
 *
 * @author thiago
 */
public class EnrollBackground extends BoxPane {

    private final int imageWidth = 384;
    private final int imageHeight = 289;
    private StackPane imagePane;
    private ImageView imageView;
    private ActivityIndicator activityIndicator;
    private DigitalDevice device;
    private PushButton enrollButton;
    public EnrollBackground(DigitalDevice device) {
        this.device = device;
        setOrientation(Orientation.VERTICAL);
        getStyles().put("horizontalAlignment", HorizontalAlignment.CENTER);
        imagePane = new StackPane();
        imagePane.setPreferredSize(imageWidth, imageHeight);
        add(imagePane);
        imageView = new ImageView();
        imageView.setPreferredSize(imageWidth, imageHeight);
        imagePane.add(imageView);
        activityIndicator = new ActivityIndicator();
        activityIndicator.setPreferredSize(imageWidth, imageHeight);
        //imagePane.add(activityIndicator);
        enrollButton = new PushButton();
        enrollButton.setButtonData("Enroll");
        enrollButton.getButtonPressListeners().add(new ButtonPressListener() {

            @Override
            public void buttonPressed(Button button) {
                enrollButton.setEnabled(false);
                imagePane.remove(imageView);
                imagePane.add(activityIndicator);
                imagePane.repaint();
                activityIndicator.setActive(true);
                ScanTask scanTask = new ScanTask(EnrollBackground.this.device);
                scanTask.execute(new TaskAdapter<BufferedImage>(new EnrollListener()));
            }
        });
        add(enrollButton);
    }

    private class EnrollListener implements TaskListener<BufferedImage> {

        @Override
        public void taskExecuted(Task<BufferedImage> task) {
            enrollButton.setEnabled(true);
            activityIndicator.setActive(false);
            imagePane.remove(activityIndicator);
            imageView.setImage(new Picture(task.getResult()));
            imagePane.add(imageView);
            imagePane.repaint();
        }

        @Override
        public void executeFailed(Task<BufferedImage> task) {
            enrollButton.setEnabled(true);
            activityIndicator.setActive(false);
            imagePane.remove(activityIndicator);
            imageView.setImage("/error.png");
            imagePane.add(imageView);
            imagePane.repaint();
            device.getMessenger().message(task.getFault().getMessage());
        }
    }
}
