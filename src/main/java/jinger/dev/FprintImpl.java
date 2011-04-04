/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package jinger.dev;

import com.sun.jna.Memory;
import com.ochafik.lang.jnaerator.runtime.NativeSize;
import java.awt.image.BufferedImage;
import java.awt.image.WritableRaster;
import javax.imageio.ImageIO;
import com.sun.jna.ptr.PointerByReference;
import com.sun.jna.Pointer;
import java.io.IOException;
import java.io.OutputStream;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import static fprint.FprintLibrary.*;

/**
 *
 * @author thiago
 */
public class FprintImpl implements DigitalDevice {

    private Logger logger = LoggerFactory.getLogger(FprintImpl.class);

    private fp_dev init() throws InitException {
        if (fp_init() < 0) {
            throw new IllegalStateException("Failed to initialize libfprint");
        }
        PointerByReference discoveredDevs = fp_discover_devs();
        fp_dscv_dev ddev = discoverDevice(discoveredDevs);
        fp_dev dev = fp_dev_open(ddev);
        fp_dscv_devs_free(discoveredDevs);
        if (dev == null) {
            throw new InitException("Could not open device.");
        }
        return dev;
    }

    private fp_dscv_dev discoverDevice(PointerByReference discoveredDevs) throws InitException {
        fp_dscv_dev ddev = new fp_dscv_dev(discoveredDevs.getValue());
        if (ddev == null) {
            throw new InitException("Could not discover devices");
        }
        fp_driver drv = fp_dscv_dev_get_driver(ddev);
        logger.info("Driver found: " + fp_driver_get_full_name(drv).getString(0));
        return ddev;
    }

    private void outputImage(PointerByReference img, OutputStream output) throws IOException {
        fp_img im = new fp_img(img.getValue());
        int h = fp_img_get_height(im);
        int w = fp_img_get_width(im);
        byte[] buf = fp_img_get_data(im).getByteArray(0, (h * w));
        BufferedImage bim = new BufferedImage(w, h, BufferedImage.TYPE_BYTE_GRAY);
        WritableRaster raster = bim.getRaster();
        for (int i = 0; i < h; i++) {
            for (int j = 0; j < w; j++) {
                raster.setSample(j, i, 0, buf[i * w + j]);
            }
        }

        ImageIO.write(bim, "png", output);
        fp_img_free(im);
    }

    @Override
    public byte[] enroll(OutputStream output) throws IOException {
        fp_dev dev = null;
        PointerByReference enrolledPrint = null;
        try {
            dev = init();
            int r = -1;
            enrolledPrint = new PointerByReference();
            logger.info("You will need to successfully scan your finger "
                    + fp_dev_get_nr_enroll_stages(dev) + " times to "
                    + "complete the process.");
            do {
                PointerByReference img = new PointerByReference();
                r = fp_enroll_finger_img(dev, enrolledPrint, img);
                outputImage(img, output);
                if (r < 0) {
                    throw new OpenException("Enroll failed with error " + r);
                }

                switch (r) {
                    case fp_enroll_result.FP_ENROLL_COMPLETE:
                        logger.info("Enroll complete!");
                        break;
                    case fp_enroll_result.FP_ENROLL_FAIL:
                        throw new OpenException("Enroll failed, something wen't wrong :(");
                    case fp_enroll_result.FP_ENROLL_PASS:
                        logger.info("Enroll stage passed. Yay!");
                        break;
                    case fp_enroll_result.FP_ENROLL_RETRY:
                        logger.info("Didn't quite catch that. Please try again.");
                        break;
                    case fp_enroll_result.FP_ENROLL_RETRY_TOO_SHORT:
                        logger.info("Your swipe was too short, please try again.");
                        break;
                    case fp_enroll_result.FP_ENROLL_RETRY_CENTER_FINGER:
                        logger.info("Didn't catch that, please center your finger on the "
                                + "sensor and try again.");
                        break;
                    case fp_enroll_result.FP_ENROLL_RETRY_REMOVE_FINGER:
                        logger.info("Scan failed, please remove your finger and then try "
                                + "again.");
                        break;
                }
            } while (r != fp_enroll_result.FP_ENROLL_COMPLETE);

            fp_dev_close(dev);
            fp_exit();
            fp_print_data data = new fp_print_data(enrolledPrint.getValue());
            PointerByReference bufData = new PointerByReference();
            NativeSize size = fp_print_data_get_data(data, bufData);
            Pointer p = new fp_print_data(enrolledPrint.getValue()).getPointer();
            byte[] bytes = new byte[size.intValue()];
            p.read(0, bytes, 0, bytes.length);
            return bytes;
        } catch (InitException ie) {
            fp_exit();
            throw new IOException(ie.getMessage(), ie);
        } catch (OpenException oe) {
            fp_dev_close(dev);
            fp_exit();
            throw new IOException(oe.getMessage(), oe);
        }
    }

    @Override
    public boolean verify(byte[] bytes, OutputStream output) throws IOException {
        fp_dev dev = null;
        Pointer p = new Memory(bytes.length);
        p.write(0, bytes, 0, bytes.length);
        int r = -1;
        try {
            dev = init();
            do {
                logger.info("Scan your finger now.");
                PointerByReference img = new PointerByReference();
                fp_print_data data = new fp_print_data(p);
                r = fp_verify_finger_img(dev, data, img);
                outputImage(img, output);
                if (r < 0) {
                    throw new OpenException("verification failed with error "
                            + r + " :(");
                }
                switch (r) {
                    case fp_verify_result.FP_VERIFY_NO_MATCH:
                        fp_dev_close(dev);
                        fp_exit();
                        return false;
                    case fp_verify_result.FP_VERIFY_MATCH:
                        fp_dev_close(dev);
                        fp_exit();
                        return true;
                    case fp_verify_result.FP_VERIFY_RETRY:
                        logger.info("Scan didn't quite work. Please try again.");
                        break;
                    case fp_verify_result.FP_VERIFY_RETRY_TOO_SHORT:
                        logger.info("Swipe was too short, please try again.");
                        break;
                    case fp_verify_result.FP_VERIFY_RETRY_CENTER_FINGER:
                        logger.info("Please center your finger on the sensor and try again.");
                        break;
                    case fp_verify_result.FP_VERIFY_RETRY_REMOVE_FINGER:
                        logger.info("Please remove finger from the sensor and try again.");
                        break;
                }
            } while (true);

        } catch (InitException ie) {
            fp_exit();
            throw new IOException(ie.getMessage(), ie);
        } catch (OpenException oe) {
            fp_dev_close(dev);
            fp_exit();
            throw new IOException(oe.getMessage(), oe);
        }
    }
}
