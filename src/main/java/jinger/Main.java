package jinger;

import jinger.dev.DigitalDevice;
import jinger.dev.FprintImpl;
import jinger.message.Messenger;
import jinger.ui.Enroll;
import org.apache.pivot.wtk.Alert;
import org.apache.pivot.wtk.Application;
import org.apache.pivot.wtk.DesktopApplicationContext;
import org.apache.pivot.wtk.Display;
import org.apache.pivot.wtk.Window;

/**
 *
 * @author thiago
 */
public class Main implements Application {

    private Window window;
    private DigitalDevice digitalDevice;

    @Override
    public void startup(Display dspl, org.apache.pivot.collections.Map<String, String> map) throws Exception {
        digitalDevice = new FprintImpl(new Messenger() {

            @Override
            public void message(String message) {
                Alert.alert(message, window);
            }
        });
        window = new Window();
        window.setTitle("Jinger Enroll");
        Enroll enroll = new Enroll(digitalDevice);

        window.setContent(enroll);
        window.open(dspl);
    }

    @Override
    public void suspend() throws Exception {
    }

    @Override
    public void resume() throws Exception {
    }

    @Override
    public boolean shutdown(boolean bln) throws Exception {
        if (window != null) {
            window.close();
        }
        return false;
    }

    public static void main(String[] args) {
        DesktopApplicationContext.main(Main.class, args);
    }
}
