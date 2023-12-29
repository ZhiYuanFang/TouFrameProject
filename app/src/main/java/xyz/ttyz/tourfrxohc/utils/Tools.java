package xyz.ttyz.tourfrxohc.utils;

import java.util.ArrayList;
import java.util.List;

/**
 * @author liujiahui
 * @date 2022/8/30 14:38
 */
public class Tools {


    public static int BulidModbusReadHoldingRegister(byte[] pCmd, int nAddr, short nStartAddr, short nRegisterNum) {//读保持寄存器
        pCmd[0] = (byte) nAddr;
        pCmd[1] = 0x03;
        pCmd[2] = (byte) ((byte) (nStartAddr >> 8));
        pCmd[3] = (byte) ((byte) (nStartAddr & 0xff));
        pCmd[4] = (byte) ((byte) (nRegisterNum >> 8));
        pCmd[5] = (byte) ((byte) (nRegisterNum & 0xff));//8个寄存器
        int nCRC16 = CalcCRC16(pCmd,6);
        pCmd[6] = (byte) ((byte) (nCRC16 & 0xff));
        pCmd[7] = (byte) ((byte) ((nCRC16 >> 8) & 0xff));
        return 8;
    }

    public static int BulidModbusWriteCoil(byte[] pCmd, int nAddr, int nStartAddr, byte nAction) {
        pCmd[0] = (byte) nAddr;
        pCmd[1] = 0x05;
        pCmd[2] = (byte) ((byte) (nStartAddr >> 8));
        pCmd[3] = (byte) ((byte) (nStartAddr & 0xff));

        if (1 == nAction) {
            pCmd[4] = (byte) 0xff;
        } else {
            pCmd[4] = 0x00;
        }
        pCmd[5] = 0x00;
        int nCRC16 = CalcCRC16(pCmd, 6);
        pCmd[6] = (byte) ((byte) (nCRC16 & 0xff));
        pCmd[7] = (byte) ((byte) ((nCRC16 >> 8) & 0xff));
        return 8;
    }

    public static int BulidModbusReadInputRegister(byte[] pCmd, int nAddr, int nStartAddr, int nRegisterNum) {//输入持寄存器
        pCmd[0] = (byte) nAddr;
        pCmd[1] = 0x04;
        pCmd[2] = (byte) (nStartAddr >> 8);
        pCmd[3] = (byte) (nStartAddr & 0xff);
        pCmd[4] = (byte) (nRegisterNum >> 8);
        pCmd[5] = (byte) (nRegisterNum & 0xff);//8个寄存器
        int nCRC16 = CalcCRC16(pCmd, 6);
        pCmd[6] = (byte) (nCRC16 & 0xff);
        pCmd[7] = (byte) ((nCRC16 >> 8) & 0xff);
        return 8;
    }

    public static int BulidModbusWrite(byte[] Cmd, int nAddr, int nStartAddr, int nRegisterNum, int nLen, byte[] pData) {
        int nCmdLen;
        Cmd[0] = (byte) nAddr;
        Cmd[1] = 0x78;//120
        Cmd[2] = (byte) (nStartAddr >> 8);
        Cmd[3] = (byte) (nStartAddr & 0xff);
        Cmd[4] = (byte) (nRegisterNum >> 8);
        Cmd[5] = (byte) (nRegisterNum & 0xff);
        Cmd[6] = (byte) (nLen);
        for (int i = 7; i < 7 + nLen; i++) {
            Cmd[i] = pData[i - 7];
        }
        int nCRC16 = CalcCRC16(Cmd, 7 + nLen);
        Cmd[7 + nLen] = (byte) (nCRC16 & 0xff);
        Cmd[8 + nLen] = (byte) ((nCRC16 >> 8) & 0xff);
        nCmdLen = 9 + nLen;
        return nCmdLen;
    }

    public static int BulidModbusWriteHoldingRegister(byte[] pCmd, int nAddr, int nStartAddr, int nValue) {
        pCmd[0] = (byte) nAddr;
        pCmd[1] = 0x06;
        pCmd[2] = (byte) (nStartAddr >> 8);
        pCmd[3] = (byte) (nStartAddr & 0xff);
        pCmd[4] = (byte) (nValue >> 8);
        pCmd[5] = (byte) (nValue & 0xff);
        int nCRC16 = CalcCRC16(pCmd, 6);
        pCmd[6] = (byte) (nCRC16 & 0xff);
        pCmd[7] = (byte) ((nCRC16 >> 8) & 0xff);
        return 8;
    }

    public static int BulidModbusWriteMultipleCoils(byte[] pCmd, int nAddr, int nStartAddr, int nCoils, int nLen, byte[] pData) {
        int nCmdLen;
        pCmd[0] = (byte) nAddr;
        pCmd[1] = 0x0F;
        pCmd[2] = (byte) (nStartAddr >> 8);
        pCmd[3] = (byte) (nStartAddr & 0xff);
        pCmd[4] = (byte) (nCoils >> 8);
        pCmd[5] = (byte) (nCoils & 0xff);
        pCmd[6] = (byte) (nLen);
        for (int i = 7; i < 7 + nLen; i++) {
            pCmd[i] = pData[i - 7];
        }
        int nCRC16 = CalcCRC16(pCmd, 7 + nLen);
        pCmd[7 + nLen] = (byte) (nCRC16 & 0xff);
        pCmd[8 + nLen] = (byte) ((nCRC16 >> 8) & 0xff);
        nCmdLen = 9 + nLen;
        return nCmdLen;
    }

    public static int BulidBrocastModbusWrite(byte[] pCmd, int nAddr, int nValue)
    {
        pCmd[0] = (byte)nAddr;
        pCmd[1] = (byte)0x41;
        pCmd[2] = (byte)0;
        pCmd[3] = (byte)0;
        pCmd[4] = (byte)(nValue >> 8);
        pCmd[5] = (byte)(nValue & 0xff);
        int nCRC16 = CalcCRC16(pCmd, 6);
        pCmd[6] = (byte)(nCRC16 & 0xff);
        pCmd[7] = (byte)((nCRC16 >> 8) & 0xff);
        return 8;
    }


    public static String GetErrorDescription(byte nErrorCode) {
        String str;
        switch (nErrorCode) {
            case 0:
                str = "其它未定义的错误 ";
                break;
            case 1:
                str = "非法功能	从站无法解读功能码";
                break;
            case 2:
                str = "非法数据地址	主站读取或发送非法的寄存器地址";
                break;
            case 3:
                str = "非法数据值	主站发送的数据不完整或字节数错";
                break;
            case 4:
                str = "从设备故障（CRC校验码错）	从站正在执行请求的操作时，产生不可恢复的差错";
                break;
            case 5:
                str = "确认（从站未准备好或无法提供数据）";
                break;
            default:
                str = "其它未定义的错误!!";
                break;
        }
        return str;
    }


    private static byte[] aucCRCHi = {
            0x00, (byte) 0xC1, (byte) 0x81, 0x40, 0x01, (byte) 0xC0, (byte) 0x80, 0x41, 0x01, (byte) 0xC0, (byte) 0x80, 0x41,
            0x00, (byte) 0xC1, (byte) 0x81, 0x40, 0x01, (byte) 0xC0, (byte) 0x80, 0x41, 0x00, (byte) 0xC1, (byte) 0x81, 0x40,
            0x00, (byte) 0xC1, (byte) 0x81, 0x40, 0x01, (byte) 0xC0, (byte) 0x80, 0x41, 0x01, (byte) 0xC0, (byte) 0x80, 0x41,
            0x00, (byte) 0xC1, (byte) 0x81, 0x40, 0x00, (byte) 0xC1, (byte) 0x81, 0x40, 0x01, (byte) 0xC0, (byte) 0x80, 0x41,
            0x00, (byte) 0xC1, (byte) 0x81, 0x40, 0x01, (byte) 0xC0, (byte) 0x80, 0x41, 0x01, (byte) 0xC0, (byte) 0x80, 0x41,
            0x00, (byte) 0xC1, (byte) 0x81, 0x40, 0x01, (byte) 0xC0, (byte) 0x80, 0x41, 0x00, (byte) 0xC1, (byte) 0x81, 0x40,
            0x00, (byte) 0xC1, (byte) 0x81, 0x40, 0x01, (byte) 0xC0, (byte) 0x80, 0x41, 0x00, (byte) 0xC1, (byte) 0x81, 0x40,
            0x01, (byte) 0xC0, (byte) 0x80, 0x41, 0x01, (byte) 0xC0, (byte) 0x80, 0x41, 0x00, (byte) 0xC1, (byte) 0x81, 0x40,
            0x00, (byte) 0xC1, (byte) 0x81, 0x40, 0x01, (byte) 0xC0, (byte) 0x80, 0x41, 0x01, (byte) 0xC0, (byte) 0x80, 0x41,
            0x00, (byte) 0xC1, (byte) 0x81, 0x40, 0x01, (byte) 0xC0, (byte) 0x80, 0x41, 0x00, (byte) 0xC1, (byte) 0x81, 0x40,
            0x00, (byte) 0xC1, (byte) 0x81, 0x40, 0x01, (byte) 0xC0, (byte) 0x80, 0x41, 0x01, (byte) 0xC0, (byte) 0x80, 0x41,
            0x00, (byte) 0xC1, (byte) 0x81, 0x40, 0x00, (byte) 0xC1, (byte) 0x81, 0x40, 0x01, (byte) 0xC0, (byte) 0x80, 0x41,
            0x00, (byte) 0xC1, (byte) 0x81, 0x40, 0x01, (byte) 0xC0, (byte) 0x80, 0x41, 0x01, (byte) 0xC0, (byte) 0x80, 0x41,
            0x00, (byte) 0xC1, (byte) 0x81, 0x40, 0x00, (byte) 0xC1, (byte) 0x81, 0x40, 0x01, (byte) 0xC0, (byte) 0x80, 0x41,
            0x01, (byte) 0xC0, (byte) 0x80, 0x41, 0x00, (byte) 0xC1, (byte) 0x81, 0x40, 0x01, (byte) 0xC0, (byte) 0x80, 0x41,
            0x00, (byte) 0xC1, (byte) 0x81, 0x40, 0x00, (byte) 0xC1, (byte) 0x81, 0x40, 0x01, (byte) 0xC0, (byte) 0x80, 0x41,
            0x00, (byte) 0xC1, (byte) 0x81, 0x40, 0x01, (byte) 0xC0, (byte) 0x80, 0x41, 0x01, (byte) 0xC0, (byte) 0x80, 0x41,
            0x00, (byte) 0xC1, (byte) 0x81, 0x40, 0x01, (byte) 0xC0, (byte) 0x80, 0x41, 0x00, (byte) 0xC1, (byte) 0x81, 0x40,
            0x00, (byte) 0xC1, (byte) 0x81, 0x40, 0x01, (byte) 0xC0, (byte) 0x80, 0x41, 0x01, (byte) 0xC0, (byte) 0x80, 0x41,
            0x00, (byte) 0xC1, (byte) 0x81, 0x40, 0x00, (byte) 0xC1, (byte) 0x81, 0x40, 0x01, (byte) 0xC0, (byte) 0x80, 0x41,
            0x00, (byte) 0xC1, (byte) 0x81, 0x40, 0x01, (byte) 0xC0, (byte) 0x80, 0x41, 0x01, (byte) 0xC0, (byte) 0x80, 0x41,
            0x00, (byte) 0xC1, (byte) 0x81, 0x40
    };
    private static byte[] aucCRCLo = {
            0x00, (byte) 0xC0, (byte) 0xC1, (byte) 0x01, (byte) 0xC3, (byte) 0x03, (byte) 0x02, (byte) 0xC2, (byte) 0xC6, 0x06, 0x07, (byte) 0xC7,
            0x05, (byte) 0xC5, (byte) 0xC4, (byte) 0x04, (byte) 0xCC, (byte) 0x0C, (byte) 0x0D, (byte) 0xCD, 0x0F, (byte) 0xCF, (byte) 0xCE, 0x0E,
            0x0A, (byte) 0xCA, (byte) 0xCB, (byte) 0x0B, (byte) 0xC9, (byte) 0x09, (byte) 0x08, (byte) 0xC8, (byte) 0xD8, 0x18, 0x19, (byte) 0xD9,
            0x1B, (byte) 0xDB, (byte) 0xDA, (byte) 0x1A, (byte) 0x1E, (byte) 0xDE, (byte) 0xDF, (byte) 0x1F, (byte) 0xDD, 0x1D, 0x1C, (byte) 0xDC,
            0x14, (byte) 0xD4, (byte) 0xD5, (byte) 0x15, (byte) 0xD7, (byte) 0x17, (byte) 0x16, (byte) 0xD6, (byte) 0xD2, 0x12, 0x13, (byte) 0xD3,
            0x11, (byte) 0xD1, (byte) 0xD0, (byte) 0x10, (byte) 0xF0, (byte) 0x30, (byte) 0x31, (byte) 0xF1, 0x33, (byte) 0xF3, (byte) 0xF2, 0x32,
            0x36, (byte) 0xF6, (byte) 0xF7, (byte) 0x37, (byte) 0xF5, (byte) 0x35, (byte) 0x34, (byte) 0xF4, 0x3C, (byte) 0xFC, (byte) 0xFD, 0x3D,
            (byte) 0xFF, (byte) 0x3F, (byte) 0x3E, (byte) 0xFE, (byte) 0xFA, (byte) 0x3A, (byte) 0x3B, (byte) 0xFB, 0x39, (byte) 0xF9, (byte) 0xF8, 0x38,
            0x28, (byte) 0xE8, (byte) 0xE9, (byte) 0x29, (byte) 0xEB, (byte) 0x2B, (byte) 0x2A, (byte) 0xEA, (byte) 0xEE, 0x2E, 0x2F, (byte) 0xEF,
            0x2D, (byte) 0xED, (byte) 0xEC, (byte) 0x2C, (byte) 0xE4, (byte) 0x24, (byte) 0x25, (byte) 0xE5, 0x27, (byte) 0xE7, (byte) 0xE6, 0x26,
            0x22, (byte) 0xE2, (byte) 0xE3, (byte) 0x23, (byte) 0xE1, (byte) 0x21, (byte) 0x20, (byte) 0xE0, (byte) 0xA0, 0x60, 0x61, (byte) 0xA1,
            0x63, (byte) 0xA3, (byte) 0xA2, (byte) 0x62, (byte) 0x66, (byte) 0xA6, (byte) 0xA7, (byte) 0x67, (byte) 0xA5, 0x65, 0x64, (byte) 0xA4,
            0x6C, (byte) 0xAC, (byte) 0xAD, (byte) 0x6D, (byte) 0xAF, (byte) 0x6F, (byte) 0x6E, (byte) 0xAE, (byte) 0xAA, 0x6A, 0x6B, (byte) 0xAB,
            0x69, (byte) 0xA9, (byte) 0xA8, (byte) 0x68, (byte) 0x78, (byte) 0xB8, (byte) 0xB9, (byte) 0x79, (byte) 0xBB, 0x7B, 0x7A, (byte) 0xBA,
            (byte) 0xBE, (byte) 0x7E, (byte) 0x7F, (byte) 0xBF, (byte) 0x7D, (byte) 0xBD, (byte) 0xBC, (byte) 0x7C, (byte) 0xB4, 0x74, 0x75, (byte) 0xB5,
            0x77, (byte) 0xB7, (byte) 0xB6, (byte) 0x76, (byte) 0x72, (byte) 0xB2, (byte) 0xB3, (byte) 0x73, (byte) 0xB1, 0x71, 0x70, (byte) 0xB0,
            0x50, (byte) 0x90, (byte) 0x91, (byte) 0x51, (byte) 0x93, (byte) 0x53, (byte) 0x52, (byte) 0x92, (byte) 0x96, 0x56, 0x57, (byte) 0x97,
            0x55, (byte) 0x95, (byte) 0x94, (byte) 0x54, (byte) 0x9C, (byte) 0x5C, (byte) 0x5D, (byte) 0x9D, 0x5F, (byte) 0x9F, (byte) 0x9E, 0x5E,
            0x5A, (byte) 0x9A, (byte) 0x9B, (byte) 0x5B, (byte) 0x99, (byte) 0x59, (byte) 0x58, (byte) 0x98, (byte) 0x88, 0x48, 0x49, (byte) 0x89,
            0x4B, (byte) 0x8B, (byte) 0x8A, (byte) 0x4A, (byte) 0x4E, (byte) 0x8E, (byte) 0x8F, (byte) 0x4F, (byte) 0x8D, 0x4D, 0x4C, (byte) 0x8C,
            0x44, (byte) 0x84, (byte) 0x85, (byte) 0x45, (byte) 0x87, (byte) 0x47, (byte) 0x46, (byte) 0x86, (byte) 0x82, 0x42, 0x43, (byte) 0x83,
            0x41, (byte) 0x81, (byte) 0x80, (byte) 0x40
    };

//    / <summary>
//    / CRC效验
//    / </summary>
//    / <param name="pucFrame">效验数据</param>
//    / <param name="usLen">数据长度</param>
//    / <returns>效验结果</returns>
    public static int CRC16(byte[] pucFrame, int usLen) {
        int i = 0;
        byte ucCRCHi = (byte) 0xFF;
        byte ucCRCLo = (byte) 0xFF;
        int iIndex = 0x0000;

        while (usLen-- > 0) {
            iIndex = (ucCRCLo ^ pucFrame[i++]);
            ucCRCLo = (byte) (ucCRCHi ^ aucCRCHi[iIndex]);
            ucCRCHi = aucCRCLo[iIndex];
        }
        return (ucCRCHi << 8 | ucCRCLo);
    }


    /*CRC16校验*/
    public static int CalcCRC16(byte[] pArray,int length)
    {
        int wCRC = 0xFFFF;
        int CRC_Count = length;
        int i;
        int num = 0;
        while (CRC_Count > 0)
        {
            CRC_Count--;
            wCRC = wCRC ^ (0xFF & pArray[num++]);
            for (i = 0; i < 8; i++)
            {
                if ((wCRC & 0x0001) == 1)
                {
                    wCRC = wCRC >> 1 ^ 0xA001;
                }
                else
                {
                    wCRC = wCRC >> 1;
                }
            }
        }
        return wCRC;
    }

    /**
     * 计算CRC16校验码
     *
     * @param bytes
     * @return
     */
    public static int getCRC(byte[] bytes) {
        int CRC = 0x0000ffff;
        int POLYNOMIAL = 0x0000a001;
        int i, j;
        for (i = 0; i < bytes.length; i++) {
            CRC ^= ((int) bytes[i] & 0x000000ff);
            for (j = 0; j < 8; j++) {
                if ((CRC & 0x00000001) != 0) {
                    CRC >>= 1;
                    CRC ^= POLYNOMIAL;
                } else {
                    CRC >>= 1;
                }
            }
        }
        return CRC;
    }

    /**
     * 把16进制字符串转换成字节数组
     *
     * @param
     * @return byte[]
     */
    public static byte[] hexStringToByte(String hex) {
        int len = (hex.length() / 2);
        byte[] result = new byte[len];
        char[] achar = hex.toCharArray();
        for (int i = 0; i < len; i++) {
            int pos = i * 2;
            result[i] = (byte) (toByte(achar[pos]) << 4 | toByte(achar[pos + 1]));
        }

        return result;
    }

    //-------------------------------------------------------
    //转hex字符串转字节数组
    public static byte[] HexToByteArr(String inHex) {
        byte[] result;
        int hexlen = inHex.length();
        if (isOdd(hexlen) == 1) {
            hexlen++;
            result = new byte[(hexlen / 2)];
            inHex = "0" + inHex;
        } else {
            result = new byte[(hexlen / 2)];
        }
        int j = 0;
        for (int i = 0; i < hexlen; i += 2) {
            result[j] = HexToByte(inHex.substring(i, i + 2));
            j++;
        }
        return result;
    }

    //Hex字符串转byte
    public static byte HexToByte(String inHex) {
        return (byte) Integer.parseInt(inHex, 16);
    }



    // 判断奇数或偶数，位运算，最后一位是1则为奇数，为0是偶数
    public static int isOdd(int num) {
        return num & 1;
    }

    //字节数组转转hex字符串，可选长度
    public static String ByteArrToHex(byte[] inBytArr, int offset, int byteCount) {
        StringBuilder strBuilder = new StringBuilder();
        int j = byteCount;
        for (int i = offset; i < j; i++) {
            strBuilder.append(Byte2Hex(Byte.valueOf(inBytArr[i])));
        }
        return strBuilder.toString();
    }

    public static List<String> ByteArrToHexArr(byte[] inBytArr, int offset, int byteCount) {
        List<String>  list = new ArrayList<>();

        int j = byteCount;
        for (int i = offset; i < j; i++) {
            list.add(Byte2Hex(inBytArr[i]));
        }
        return list;
    }

    //1字节转2个Hex字符
    public static String Byte2Hex(Byte inByte) {
        return String.format("%02x", new Object[]{inByte}).toUpperCase();
//        return Integer.toHexString(Integer.parseInt())

//        String hex = Integer.toHexString(inByte & 0xFF);
//        if(hex.length() < 2){
//            hex = "0" + hex;
//        }
//        return hex;
    }

    private static int toByte(char c) {
        byte b = (byte) "0123456789ABCDEF".indexOf(c);
        return b;
    }


    //字符串加空格
    public static String subStringToComma(String str, Integer regex) {
        char[] chars = str.toCharArray();
        String temp = "";
        StringBuilder result = new StringBuilder();
        for (int i = 0; i <= chars.length - 1; i++) {
            temp += chars[i];
            if (i == chars.length - 1) {
                result.append(temp);
                break;
            }
            if (temp.length() >= regex) {
                temp += " ";
                result.append(temp);
                temp = "";
            }
        }
        return result.toString();
    }

    public static int getInt(byte[] bytes)
    {
        return (0xff & bytes[0]) | (0xff00 & (bytes[1] << 8)) | (0xff0000 & (bytes[2] << 16)) | (0xff000000 & (bytes[3] << 24));
    }
}
