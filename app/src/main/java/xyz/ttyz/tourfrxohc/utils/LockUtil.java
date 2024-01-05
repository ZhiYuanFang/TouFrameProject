package xyz.ttyz.tourfrxohc.utils;


import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.hardware.usb.UsbDevice;
import android.hardware.usb.UsbDeviceConnection;
import android.hardware.usb.UsbManager;
import android.os.Handler;
import android.os.Looper;
import android.serialport.SerialPortFinder;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.style.ForegroundColorSpan;
import android.util.Log;
import android.widget.Toast;

import com.hoho.android.usbserial.driver.UsbSerialDriver;
import com.hoho.android.usbserial.driver.UsbSerialPort;
import com.hoho.android.usbserial.driver.UsbSerialProber;
import com.hoho.android.usbserial.util.HexDump;
import com.hoho.android.usbserial.util.SerialInputOutputManager;
import com.vi.vioserial.NormalSerial;
import com.vi.vioserial.listener.OnNormalDataListener;

import org.greenrobot.eventbus.EventBus;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import android_serialport_api.SerialPort;
import xyz.ttyz.mylibrary.method.ActivityManager;
import xyz.ttyz.toubasemvvm.utils.ToastUtil;
import xyz.ttyz.tourfrxohc.BuildConfig;
import xyz.ttyz.tourfrxohc.models.NetEventModel;

/**
 * @author 投投
 * @date 2023/12/15
 * @email 343315792@qq.com
 */
public class LockUtil {
    private static final String TAG = "LockUtil";
    private static LockUtil lockUtil;
    public static final int BankNumber = 2;
    public static final int MAX_JI = 7;//一个板有几个寄存器(必须相同数量的板)
    private static final String INTENT_ACTION_GRANT_USB = BuildConfig.APPLICATION_ID + ".GRANT_USB";

    private LockDelegate lockDelegate;
    public UsbSerialPort usbSerialPort = null;
    UsbDevice device = null;
    private LockUtil(LockDelegate lockDelegate) {
        this.lockDelegate = lockDelegate;
    }

    public static LockUtil getInstance(LockDelegate lockDelegate) {
        if (lockUtil == null) {
            lockUtil = new LockUtil(lockDelegate);
        }
        lockUtil.lockDelegate = lockDelegate;
        return lockUtil;
    }

    public static void clearCallBack(){
        if (lockUtil != null) {
            lockUtil.lockDelegate = null;
        }
    }


    public boolean connectSerialPort(){
        //查找USB驱动
        UsbManager usbManager = (UsbManager) ActivityManager.getInstance().getSystemService(Context.USB_SERVICE);
        UsbSerialProber usbDefaultProber = UsbSerialProber.getDefaultProber();

        for(UsbDevice item : usbManager.getDeviceList().values()) {
            UsbSerialDriver driver = usbDefaultProber.probeDevice(item);
            if(driver != null) {
                List<UsbSerialPort> usbSerialPorts = driver.getPorts();
                if(usbSerialPorts.size() > 0){
                    usbSerialPort = driver.getPorts().get(0);
                }
                device = item;
                break;
            }
        }
        if(device == null || usbSerialPort == null) {
            ToastUtil.showToast("找不到usb驱动");
            EventBus.getDefault().post(new NetEventModel(false));
            return false;
        }
        // 连接USB驱动

        UsbDeviceConnection usbConnection = usbManager.openDevice(device);
        if (usbConnection == null && !usbManager.hasPermission(device)) {
            PendingIntent usbPermissionIntent = PendingIntent.getBroadcast(ActivityManager.getInstance(), 0, new Intent(INTENT_ACTION_GRANT_USB), PendingIntent.FLAG_MUTABLE);
            usbManager.requestPermission(device, usbPermissionIntent);
            return false;
        }
        if (usbConnection == null) {
            if (!usbManager.hasPermission(device))
                ToastUtil.showToast("连接失败:权限被拒绝");
            else
                ToastUtil.showToast("连接失败:打开失败");
            EventBus.getDefault().post(new NetEventModel(false));
            return false;
        }

        try {
            usbSerialPort.open(usbConnection);
            usbSerialPort.setParameters(9600, 8, 1, UsbSerialPort.PARITY_NONE);

            Log.i(TAG, "tryUSB: 连接成功");
            EventBus.getDefault().post(new NetEventModel(true));
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            Log.e(TAG, "tryUSB: " + e.getMessage());
            EventBus.getDefault().post(new NetEventModel(false));
        }
        return false;
    }

    private List<String> readList = new ArrayList<>();

    private void usbRead(){
        readList.clear();
        if(usbSerialPort == null){
            waitRead = false;
            return ;
        }
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    byte[] readData = new byte[1024];
                    int size = 0;
                    while ((size = usbSerialPort.read(readData, 2000)) > 0){
                        readList.addAll(Tools.ByteArrToHexArr(readData, 0, size));
                    }
                    Log.i(TAG, "usbRead: " + readList);

                    if(getFunCode(readList) == 5){
                        if(lockDelegate != null){
                            lockDelegate.callBackOpen(getDoorNumber(readList));
                        }
                    }
                    if(getFunCode(readList) == 4){
                        int address = convert2java_door_state(readList);
                        if(address == BankNumber){//遍历所有板之后才回调处理完成的数据
                            if(lockDelegate != null){
                                lockDelegate.callBackState(allDoorStatusArr);
                                readAllKeyState();//追加读状态功能进入发送队列排队
                            }
                        }
                    }
                    waitRead = false;
                    if(waitSend.size() > 0){// 如果有等待队列，继续发送
                        justSend(waitSend.pop());
                    }
                    EventBus.getDefault().post(new NetEventModel(true));
                } catch (IOException e) {
                    e.printStackTrace();
                    Log.e(TAG, "usbRead: " + e.getMessage() );
                    waitRead = false;
                    EventBus.getDefault().post(new NetEventModel(false));
                }
            }
        }).start();
    }

    private int getFunCode (List<String> x16List) {
        if(x16List.size() < 4){
            return 0;
        }
        try {
            return Integer.parseInt(x16List.get(1));
        } catch (NumberFormatException e){
            return 0;
        }
    }

    //region 具体门 从1开始
    private int getDoorNumber(List<String> x16List){
        if(x16List.size() < 4){
            return -1;
        }
        int address = Integer.parseInt(x16List.get(0));
        if(getFunCode(x16List) == 5){
            return hexToDec(x16List.get(3)) + 1 + (address - 1) * 50;
        }
        return -1;
    }

    //16 -> 10
    private int hexToDec(String hex) {
        int result = 0;
        for (int i = 0; i < hex.length(); i++) {
            int digit = Character.digit(hex.charAt(i), 16);
            result = result * 16 + digit;
        }
        return result;
    }
    //endregion

    //region 状态
    private static boolean[] allDoorStatusArr = new boolean[BankNumber * 50 + 24];

    // 讲返回码x16转成java逻辑
    // 针对返回当前板地址
    private int convert2java_door_state(List<String> x16List){
        if(x16List.size() < 4){
            return 0;
        }
        int address = Integer.parseInt(x16List.get(0));
        if(getFunCode(x16List) == 4){
            //是读状态的功能
            for(int x16ItemPos = 3; x16ItemPos < x16List.size() - 2; x16ItemPos ++){
                //从第一个寄存器开始: 第i ~ i+8 钥匙的状态
                byte x16Item = Tools.HexToByte(x16List.get(x16ItemPos));
                int wCRC = 0xFFFF;
                wCRC = wCRC ^ (0xFF & x16Item);
                for (int i = 1; i <= 8; i++){
                    //第 i*（x16ItemPos-2）* address 个锁的状态
                    // 2023/12/25 两个50锁是这样， 如果第三个24锁必须是地址三才行 , 不能加两个24锁
                    int pos = i + 8 * (x16ItemPos-3)+ ((address - 1) * 50) - 1;
                    if(pos >= allDoorStatusArr.length) break;//寄存器中的钥匙超上限 则停止循环
//                    Log.i(TAG, "convert2java_door_state: i: " + i + " x16ItemPos: " + x16ItemPos + " address: " + address);
                    allDoorStatusArr[pos] = (wCRC & 0x0001) == 1;

                    wCRC = wCRC >> 1;//看下一个锁的状态
                }

            }
        }
        return address;
    }
    //endregion

    // 打开钥匙：01050000FF008C3A 单路
    public void openKey(int keyNumber) {
        int address =(int)(keyNumber / 51) + 1;//第几个板
        keyNumber = keyNumber - (address - 1) * 50;//在第address板中，第几个锁
        byte[] m_cmd = new byte[128];
        int nLen = Tools.BulidModbusWriteCoil(m_cmd, address, keyNumber - 1, (byte) 1);
        sendSerialPort(new SendModel(nLen, m_cmd));
        readAllKeyState();// 跟读状态
    }
    // 打开全部钥匙
    public void openAllKey(){
        for(int i = 1; i <= BankNumber; i ++){
            multiOpen(i, 0, 50);
        }
        readAllKeyState();// 跟读状态
    }
    // 读全部钥匙状态
    public void readAllKeyState() {
        // 遍历两个板
        for (int address = 1 ; address <= BankNumber; address ++) {
            byte[] m_cmd = new byte[128];
            int nLen = Tools.BulidModbusReadInputRegister(m_cmd, address, 0, MAX_JI);
            sendSerialPort(new SendModel(nLen, m_cmd));
        }
    }


    private void sendSerialPort(String hex) {
        byte[] cur = Tools.HexToByteArr(hex);
        byte[] m_cmd = new byte[128];
        System.arraycopy(cur, 0, m_cmd, 0, cur.length);
        sendSerialPort(new SendModel(cur.length, m_cmd));
    }

    LinkedList<SendModel> waitSend = new LinkedList<>();//等待发送指令队列
    private static class SendModel{
        int nLen;//有效长度
        byte[] m_cmd;//字节

        public SendModel(int nLen, byte[] m_cmd) {
            this.nLen = nLen;
            this.m_cmd = m_cmd;
        }
    }
    private boolean waitRead = false;//是否需要等待读流
    private void sendSerialPort(SendModel sendModel) {

        List<String> x16List = Tools.ByteArrToHexArr(sendModel.m_cmd, 0, sendModel.nLen);
        int funNumber = getFunCode(x16List);//功能码
        if(funNumber != 4){
            //如果不是读,插入第一
            waitSend.addFirst(sendModel);
        } else
            waitSend.addLast(sendModel);//读状态往后排
        if(!waitRead){// 如果不用等待读流
            justSend(waitSend.pop());
        }
    }

    private void justSend(SendModel sendModel){
        waitRead = true;
        if(sendModel == null){
            waitRead = false;
            return;
        }
        String hex = Tools.ByteArrToHex(sendModel.m_cmd, 0, sendModel.nLen);

        Log.i(TAG, "sendSerial: 发送命令 -> " + hex);
        try {
            usbSerialPort.write(Tools.hexStringToByte(hex), 2000);
            usbRead();
        } catch (IOException e) {
            Log.e(TAG, "sendSerialPort: 串口数据发送失败：" + e.getMessage());
            waitRead = false;
        }
    }

    /**
     * 打开多个通道
     *
     * @param nStartAddr 开始位置
     * @param nCoils     结束位置
     */
    public void multiOpen(int address, int nStartAddr, int nCoils) {
        byte[] data = new byte[20];
        for (int i = 0; i < 20; i++) {
            data[i] = (byte) 0xff;
        }
        int nData;
        if(nCoils % 8 == 0 ){
            nData = nCoils/ 8;
        } else {
            nData = (nCoils/ 8) + 1;
        }
        byte[] m_cmd = new byte[128];
        int nLen = Tools.BulidModbusWriteMultipleCoils(m_cmd, address, nStartAddr, nCoils, nData, data);
        sendSerialPort(new SendModel(nLen, m_cmd));
    }

    public interface LockDelegate {
        void callBackOpen(int keyNumber);//一个门打开了
        void callBackState(boolean[] readArr);//所有门的状态
    }

}
