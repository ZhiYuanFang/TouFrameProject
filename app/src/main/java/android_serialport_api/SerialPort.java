package android_serialport_api;

import android.util.Log;

import java.io.File;
import java.io.FileDescriptor;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class SerialPort {

	private static final String TAG = "SerialPort";

	/*
	 * Do not remove or rename the field mFd: it is used by native method close();
	 */
	private FileDescriptor mFd;
	private FileInputStream mFileInputStream;
	private FileOutputStream mFileOutputStream;

	public SerialPort(File device, int baudrate, int flags) throws SecurityException, IOException {

		/* Check access permission */

		if (!device.canRead() || !device.canWrite()) {
			try {
				/* Missing read/write permission, trying to chmod the file */
				System.out.println("-----------------------");
				Process su;
				su = Runtime.getRuntime().exec("/system/bin/sh");
				System.out.println("串口"+su);
				String cmd = "chmod 666 " + device.getAbsolutePath() + "\n"
						+ "exit\n";
				System.out.println("9999"+device.getAbsolutePath());
				su.getOutputStream().write(cmd.getBytes());
				System.out.println("串口");
				if ((su.waitFor() != 0) || !device.canRead()
						|| !device.canWrite()) {
					System.out.println("串口1");
					//throw new SecurityException();
				}
			} catch (Exception e) {
				System.out.println("串口3");
				e.printStackTrace();
				throw new SecurityException();
			}
		}
		System.out.println("串口2");
		mFd = open(device.getAbsolutePath(), baudrate, flags);
		if (mFd == null) {
			Log.e(TAG, "native open returns null");
			throw new IOException();
		}

		mFileInputStream = new FileInputStream(mFd);
		mFileOutputStream = new FileOutputStream(mFd);


	}

	// Getters and setters
	public InputStream getInputStream() {
		return mFileInputStream;
	}

	public OutputStream getOutputStream() {
		return mFileOutputStream;
	}

	// JNI
	private native static FileDescriptor open(String path, int baudrate, int flags);
	public native void close();
	static {
		System.out.println("==============================");
		System.loadLibrary("serial_port");
	}
}
