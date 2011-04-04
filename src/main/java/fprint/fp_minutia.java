package fprint;

import com.ochafik.lang.jnaerator.runtime.Structure;
import com.sun.jna.ptr.IntByReference;

public class fp_minutia extends Structure<fp_minutia, fp_minutia.ByValue, fp_minutia.ByReference> {

    public int x;
    public int y;
    public int ex;
    public int ey;
    public int direction;
    public double reliability;
    public int type;
    public int appearing;
    public int feature_id;
    public IntByReference nbrs;
    public IntByReference ridge_counts;
    public int num_nbrs;

    public fp_minutia() {
        super();
        initFieldOrder();
    }

    protected void initFieldOrder() {
        setFieldOrder(new java.lang.String[]{"x", "y", "ex", "ey", "direction", "reliability", "type", "appearing", "feature_id", "nbrs", "ridge_counts", "num_nbrs"});
    }

    protected ByReference newByReference() {
        return new ByReference();
    }

    protected ByValue newByValue() {
        return new ByValue();
    }

    protected fp_minutia newInstance() {
        return new fp_minutia();
    }

    public static fp_minutia[] newArray(int arrayLength) {
        return Structure.newArray(fp_minutia.class, arrayLength);
    }

    public static class ByReference extends fp_minutia implements Structure.ByReference {
    };

    public static class ByValue extends fp_minutia implements Structure.ByValue {
    };
}
