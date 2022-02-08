package xyz.ttyz.tourfrxohc.jni;
public class JNITools {
    static {
        System.loadLibrary("jnidemo3");
    }

    public static native int add(int a, int b);
}
