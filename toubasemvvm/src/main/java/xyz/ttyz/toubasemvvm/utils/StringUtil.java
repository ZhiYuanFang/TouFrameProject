package xyz.ttyz.toubasemvvm.utils;

import android.graphics.Color;
import android.text.SpannableString;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.TextUtils;
import android.text.style.AbsoluteSizeSpan;
import android.text.style.ForegroundColorSpan;
import android.text.style.StrikethroughSpan;


import androidx.core.content.ContextCompat;

import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.net.URLDecoder;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import xyz.ttyz.tou_example.ActivityManager;

public class StringUtil {

    public static String helthInfo(String codeStr){
        int code = -1;
        if(StringUtil.isNumeric(codeStr)){
            code = Integer.parseInt(codeStr);
        }
        switch (code){
            case 1:
                return "绿码";

            case 2:
                return "黄码";

            case 3:
                return "红码";

            case 4:
                return "灰码";

            default: return "未知";
        }
    }

    public static int helthColor(String codeStr){
        int code = -1;
        if(StringUtil.isNumeric(codeStr)){
            code = Integer.parseInt(codeStr);
        }
        switch (code){
            case 1:
                return ContextCompat.getColor(ActivityManager.getInstance(), android.R.color.holo_green_dark);

            case 2:
                return Color.parseColor("#FF9800");

            case 3:
                return ContextCompat.getColor(ActivityManager.getInstance(), android.R.color.holo_red_dark);

            case 4:
                return ContextCompat.getColor(ActivityManager.getInstance(), android.R.color.darker_gray);

            default: return ContextCompat.getColor(ActivityManager.getInstance(), android.R.color.black);
        }
    }

    public static String nucleinInfo(String codeStr){
        int code = -1;
        if(StringUtil.isNumeric(codeStr)){
            code = Integer.parseInt(codeStr);
        }
        switch (code){
            case 1:
                return "校验通过";

            case 2:
                return "校验不通过";


            default: return "无结果";
        }
    }

    public static int nucleinColor(String codeStr){
        int code = -1;
        if(StringUtil.isNumeric(codeStr)){
            code = Integer.parseInt(codeStr);
        }
        switch (code){
            case 1:
                return ContextCompat.getColor(ActivityManager.getInstance(), android.R.color.holo_green_dark);

            case 2:
                return Color.parseColor("#FF9800");

            default: return ContextCompat.getColor(ActivityManager.getInstance(), android.R.color.black);
        }
    }

    public static boolean string2bool (String tf){
        return StringUtil.safeString(tf).toUpperCase().equals("T");
    }


    /**
     * 获取string值,保证不是null且为字符串类型
     */
    public static String safeString(Object obj) {
        if (null == obj || obj.toString().toLowerCase().equals("null")) {
            return "";
        }
        return String.valueOf(obj);
    }


    /**
     * 解决播放数量的显示 超过1w 显示1.几万
     *
     * @param readCount
     * @return
     */
    public static String showCount(long readCount) {
        String count = readCount + "";
        if (readCount > 9999) {
            double cot = ((double) readCount) / 10000;
            count = retainDecimal(1, cot) + "w";
        }
        return count;
    }

    /**
     * 判断是否是emoji表情
     *
     * @param str
     * @return
     */
    public static boolean isHasEmoji(String str) {
        String pattern = "[\\ud800\\udc00-\\udbff\\udfff\\ud800-\\udfff]";
        Matcher matcher = Pattern.compile(pattern).matcher(str);
        boolean b = matcher.find();
        return b;
    }

    /**
     * 判断手机号格式
     *
     * @param moblie
     * @return
     */
    public static boolean matchMobile(String moblie) {
        return moblie.length() == 11;
//        String regex = "^((13[0-9])|(15[0-9])|(18[0-9]))\\d{8}$";
//        Pattern p = Pattern.compile(regex, Pattern.CASE_INSENSITIVE);
//        Matcher m = p.matcher(moblie);
//        return m.matches();
    }

    /**
     * 半角转换为全角
     *
     * @param input
     * @return
     */
    public static String ToDBC(String input) {
        char[] c = input.toCharArray();
        for (int i = 0; i < c.length; i++) {
            if (c[i] == 12288) {
                c[i] = (char) 32;
                continue;
            }
            if (c[i] > 65280 && c[i] < 65375)
                c[i] = (char) (c[i] - 65248);
        }
        return new String(c);
    }

    /**
     * 去除特殊字符或将所有中文标号替换为英文标号
     *
     * @param str
     * @return
     */
    public static String stringFilter(String str) {
        str = str.replaceAll("【", "[").replaceAll("】", "]")
                .replaceAll("！", "!").replaceAll("：", ":");// 替换中文标号
        String regEx = "[『』]"; // 清除掉特殊字符
        Pattern p = Pattern.compile(regEx);
        Matcher m = p.matcher(str);
        return m.replaceAll("").trim();
    }

    /**
     * 验证输入密码条件(字符与数据同时出现)
     *
     * @param password
     * @return
     */
    public static boolean matchPassword(String password) {
        String regex = "[A-Za-z]+[0-9]";
        Pattern p = Pattern.compile(regex);
        Matcher m = p.matcher(password);
        return m.matches();
    }

    /**
     * 获取字符串的首字母
     *
     * @param memo
     * @return
     */
    public static String getLetter(String memo) {
        if (memo == null || "".equals(memo)) {
            return "#";
        }
        CharacterParser characterParser = CharacterParser.getInstance();

        //汉字转换成拼音
        String pinyin = characterParser.getSelling(memo);
        String sortString = pinyin.substring(0, 1).toUpperCase();

        // 正则表达式，判断首字母是否是英文字母
        if (sortString.matches("[A-Z]")) {
            return sortString.toUpperCase();
        } else {
            return "#";
        }
    }

    public static class CharacterParser {
        private static int[] pyvalue = new int[]{-20319, -20317, -20304, -20295, -20292, -20283, -20265, -20257, -20242, -20230, -20051, -20036, -20032,
                -20026, -20002, -19990, -19986, -19982, -19976, -19805, -19784, -19775, -19774, -19763, -19756, -19751, -19746, -19741, -19739, -19728,
                -19725, -19715, -19540, -19531, -19525, -19515, -19500, -19484, -19479, -19467, -19289, -19288, -19281, -19275, -19270, -19263, -19261,
                -19249, -19243, -19242, -19238, -19235, -19227, -19224, -19218, -19212, -19038, -19023, -19018, -19006, -19003, -18996, -18977, -18961,
                -18952, -18783, -18774, -18773, -18763, -18756, -18741, -18735, -18731, -18722, -18710, -18697, -18696, -18526, -18518, -18501, -18490,
                -18478, -18463, -18448, -18447, -18446, -18239, -18237, -18231, -18220, -18211, -18201, -18184, -18183, -18181, -18012, -17997, -17988,
                -17970, -17964, -17961, -17950, -17947, -17931, -17928, -17922, -17759, -17752, -17733, -17730, -17721, -17703, -17701, -17697, -17692,
                -17683, -17676, -17496, -17487, -17482, -17468, -17454, -17433, -17427, -17417, -17202, -17185, -16983, -16970, -16942, -16915, -16733,
                -16708, -16706, -16689, -16664, -16657, -16647, -16474, -16470, -16465, -16459, -16452, -16448, -16433, -16429, -16427, -16423, -16419,
                -16412, -16407, -16403, -16401, -16393, -16220, -16216, -16212, -16205, -16202, -16187, -16180, -16171, -16169, -16158, -16155, -15959,
                -15958, -15944, -15933, -15920, -15915, -15903, -15889, -15878, -15707, -15701, -15681, -15667, -15661, -15659, -15652, -15640, -15631,
                -15625, -15454, -15448, -15436, -15435, -15419, -15416, -15408, -15394, -15385, -15377, -15375, -15369, -15363, -15362, -15183, -15180,
                -15165, -15158, -15153, -15150, -15149, -15144, -15143, -15141, -15140, -15139, -15128, -15121, -15119, -15117, -15110, -15109, -14941,
                -14937, -14933, -14930, -14929, -14928, -14926, -14922, -14921, -14914, -14908, -14902, -14894, -14889, -14882, -14873, -14871, -14857,
                -14678, -14674, -14670, -14668, -14663, -14654, -14645, -14630, -14594, -14429, -14407, -14399, -14384, -14379, -14368, -14355, -14353,
                -14345, -14170, -14159, -14151, -14149, -14145, -14140, -14137, -14135, -14125, -14123, -14122, -14112, -14109, -14099, -14097, -14094,
                -14092, -14090, -14087, -14083, -13917, -13914, -13910, -13907, -13906, -13905, -13896, -13894, -13878, -13870, -13859, -13847, -13831,
                -13658, -13611, -13601, -13406, -13404, -13400, -13398, -13395, -13391, -13387, -13383, -13367, -13359, -13356, -13343, -13340, -13329,
                -13326, -13318, -13147, -13138, -13120, -13107, -13096, -13095, -13091, -13076, -13068, -13063, -13060, -12888, -12875, -12871, -12860,
                -12858, -12852, -12849, -12838, -12831, -12829, -12812, -12802, -12607, -12597, -12594, -12585, -12556, -12359, -12346, -12320, -12300,
                -12120, -12099, -12089, -12074, -12067, -12058, -12039, -11867, -11861, -11847, -11831, -11798, -11781, -11604, -11589, -11536, -11358,
                -11340, -11339, -11324, -11303, -11097, -11077, -11067, -11055, -11052, -11045, -11041, -11038, -11024, -11020, -11019, -11018, -11014,
                -10838, -10832, -10815, -10800, -10790, -10780, -10764, -10587, -10544, -10533, -10519, -10331, -10329, -10328, -10322, -10315, -10309,
                -10307, -10296, -10281, -10274, -10270, -10262, -10260, -10256, -10254};
        public static String[] pystr = new String[]{"a", "ai", "an", "ang", "ao", "ba", "bai", "ban", "bang", "bao", "bei", "ben", "beng", "bi", "bian",
                "biao", "bie", "bin", "bing", "bo", "bu", "ca", "cai", "can", "cang", "cao", "ce", "ceng", "cha", "chai", "chan", "chang", "chao", "che",
                "chen", "cheng", "chi", "chong", "chou", "chu", "chuai", "chuan", "chuang", "chui", "chun", "chuo", "ci", "cong", "cou", "cu", "cuan",
                "cui", "cun", "cuo", "da", "dai", "dan", "dang", "dao", "de", "deng", "di", "dian", "diao", "die", "ding", "diu", "dong", "dou", "du",
                "duan", "dui", "dun", "duo", "e", "en", "er", "fa", "fan", "fang", "fei", "fen", "feng", "fo", "fou", "fu", "ga", "gai", "gan", "gang",
                "gao", "ge", "gei", "gen", "geng", "gong", "gou", "gu", "gua", "guai", "guan", "guang", "gui", "gun", "guo", "ha", "hai", "han", "hang",
                "hao", "he", "hei", "hen", "heng", "hong", "hou", "hu", "hua", "huai", "huan", "huang", "hui", "hun", "huo", "ji", "jia", "jian",
                "jiang", "jiao", "jie", "jin", "jing", "jiong", "jiu", "ju", "juan", "jue", "jun", "ka", "kai", "kan", "kang", "kao", "ke", "ken",
                "keng", "kong", "kou", "ku", "kua", "kuai", "kuan", "kuang", "kui", "kun", "kuo", "la", "lai", "lan", "lang", "lao", "le", "lei", "leng",
                "li", "lia", "lian", "liang", "liao", "lie", "lin", "ling", "liu", "long", "lou", "lu", "lv", "luan", "lue", "lun", "luo", "ma", "mai",
                "man", "mang", "mao", "me", "mei", "men", "meng", "mi", "mian", "miao", "mie", "min", "ming", "miu", "mo", "mou", "mu", "na", "nai",
                "nan", "nang", "nao", "ne", "nei", "nen", "neng", "ni", "nian", "niang", "niao", "nie", "nin", "ning", "niu", "nong", "nu", "nv", "nuan",
                "nue", "nuo", "o", "ou", "pa", "pai", "pan", "pang", "pao", "pei", "pen", "peng", "pi", "pian", "piao", "pie", "pin", "ping", "po", "pu",
                "qi", "qia", "qian", "qiang", "qiao", "qie", "qin", "qing", "qiong", "qiu", "qu", "quan", "que", "qun", "ran", "rang", "rao", "re",
                "ren", "reng", "ri", "rong", "rou", "ru", "ruan", "rui", "run", "ruo", "sa", "sai", "san", "sang", "sao", "se", "sen", "seng", "sha",
                "shai", "shan", "shang", "shao", "she", "shen", "sheng", "shi", "shou", "shu", "shua", "shuai", "shuan", "shuang", "shui", "shun",
                "shuo", "si", "song", "sou", "su", "suan", "sui", "sun", "suo", "ta", "tai", "tan", "tang", "tao", "te", "teng", "ti", "tian", "tiao",
                "tie", "ting", "tong", "tou", "tu", "tuan", "tui", "tun", "tuo", "wa", "wai", "wan", "wang", "wei", "wen", "weng", "wo", "wu", "xi",
                "xia", "xian", "xiang", "xiao", "xie", "xin", "xing", "xiong", "xiu", "xu", "xuan", "xue", "xun", "ya", "yan", "yang", "yao", "ye", "yi",
                "yin", "ying", "yo", "yong", "you", "yu", "yuan", "yue", "yun", "za", "zai", "zan", "zang", "zao", "ze", "zei", "zen", "zeng", "zha",
                "zhai", "zhan", "zhang", "zhao", "zhe", "zhen", "zheng", "zhi", "zhong", "zhou", "zhu", "zhua", "zhuai", "zhuan", "zhuang", "zhui",
                "zhun", "zhuo", "zi", "zong", "zou", "zu", "zuan", "zui", "zun", "zuo"};
        private StringBuilder buffer;
        private String resource;
        private static CharacterParser characterParser = new CharacterParser();

        public static CharacterParser getInstance() {
            return characterParser;
        }

        public String getResource() {
            return resource;
        }

        public void setResource(String resource) {
            this.resource = resource;
        }

        /**
         * 汉字转成ASCII码 * * @param chs * @return
         */
        private int getChsAscii(String chs) {
            int asc = 0;
            try {
                byte[] bytes = chs.getBytes("gb2312");
                if (bytes == null || bytes.length > 2 || bytes.length <= 0) {
                    throw new RuntimeException("illegal resource string");
                }
                if (bytes.length == 1) {
                    asc = bytes[0];
                }
                if (bytes.length == 2) {
                    int hightByte = 256 + bytes[0];
                    int lowByte = 256 + bytes[1];
                    asc = (256 * hightByte + lowByte) - 256 * 256;
                }
            } catch (Exception e) {
                System.out.println("ERROR:ChineseSpelling.class-getChsAscii(String chs)" + e);
            }
            return asc;
        }

        /**
         * 单字解析 * * @param str * @return
         */
        public String convert(String str) {
            String result = null;
            int ascii = getChsAscii(str);
            if (ascii > 0 && ascii < 160) {
                result = String.valueOf((char) ascii);
            } else {
                for (int i = (pyvalue.length - 1); i >= 0; i--) {
                    if (pyvalue[i] <= ascii) {
                        result = pystr[i];
                        break;
                    }
                }
            }
            return result;
        }

        /**
         * 词组解析 * * @param chs * @return
         */
        public String getSelling(String chs) {
            String key, value;
            buffer = new StringBuilder();
            for (int i = 0; i < chs.length(); i++) {
                key = chs.substring(i, i + 1);
                if (key.getBytes().length >= 2) {
                    value = convert(key);
                    if (value == null) {
                        value = "unknown";
                    }
                } else {
                    value = key;
                }
                buffer.append(value);
            }
            return buffer.toString();
        }

        public String getSpelling() {
            return this.getSelling(this.getResource());
        }

    }

    /**
     * <p>Checks if a CharSequence is whitespace, empty ("") or null.</p>
     * <p/>
     * <pre>
     * StringUtils.isBlank(null)      = true
     * StringUtils.isBlank("")        = true
     * StringUtils.isBlank(" ")       = true
     * StringUtils.isBlank("bob")     = false
     * StringUtils.isBlank("  bob  ") = false
     * </pre>
     *
     * @param cs the CharSequence to check, may be null
     */
    public static boolean isBlank(CharSequence cs) {
        int strLen;
        if (cs == null || (strLen = cs.length()) == 0) {
            return true;
        }
        for (int i = 0; i < strLen; i++) {
            if (Character.isWhitespace(cs.charAt(i)) == false) {
                return false;
            }
        }
        return true;
    }


    private static boolean isChinese(char c) {
        Character.UnicodeBlock ub = Character.UnicodeBlock.of(c);
        return ub == Character.UnicodeBlock.CJK_UNIFIED_IDEOGRAPHS
                || ub == Character.UnicodeBlock.CJK_COMPATIBILITY_IDEOGRAPHS
                || ub == Character.UnicodeBlock.CJK_UNIFIED_IDEOGRAPHS_EXTENSION_A
                || ub == Character.UnicodeBlock.GENERAL_PUNCTUATION
                || ub == Character.UnicodeBlock.CJK_SYMBOLS_AND_PUNCTUATION
                || ub == Character.UnicodeBlock.HALFWIDTH_AND_FULLWIDTH_FORMS;
    }

    public static boolean isChinese(String string) {
        char[] ch = string.toCharArray();
        for (int i = 0; i < ch.length; i++) {
            char c = ch[i];
            if (isChinese(c)) {
                return true;
            }
        }
        return false;
    }

    public static boolean isConSpeCharacters(String string) { //判断是否包含特殊字符 包含返回true 否则返回false
        return string.replaceAll("[\u4e00-\u9fa5]*[a-z]*[A-Z]*\\d*\\s*", "").length() != 0;
    }

    /**
     * 保留指定位数的小数点
     *
     * @param digits   2代表保留两位小数
     * @param floatNum
     * @return
     */
    public static String retainDecimal(int digits, float floatNum) {
        StringBuilder digitsBuilder = new StringBuilder("0.");
        for (int i = 0; i < digits; i++) {
            digitsBuilder.append("0");
        }
        DecimalFormat df = new DecimalFormat(digitsBuilder.toString());
        return df.format(floatNum);
    }

    /**
     * 保留指定位数的小数点
     *
     * @param digits   2代表保留两位小数
     * @param floatNum
     * @return
     */
    public static String retainDecimal(int digits, double floatNum) {
        StringBuilder digitsBuilder = new StringBuilder("0.");
        for (int i = 0; i < digits; i++) {
            digitsBuilder.append("0");
        }
        DecimalFormat df = new DecimalFormat(digitsBuilder.toString());
        return df.format(floatNum);
    }

    /**
     * 去除浮点数小数点后多余的0
     *
     * @param floatNum
     * @return
     */
    public static String floatReMoveZero(float floatNum) {
        String result = floatNum + "";
        if (result.indexOf(".") > 0) {
            result = result.replaceAll("0+?$", "");//去掉多余的0
            result = result.replaceAll("[.]$", "");//如最后一位是.则去掉
        }
        return result;
    }

    /**
     * 保留指定位数小数点同时去除尾部多余的0
     *
     * @param digits
     * @param floatNum
     * @return
     */
    public static String retainRemoveZero(int digits, float floatNum) {
        String result = retainDecimal(digits, floatNum);
        if (result.indexOf(".") > 0) {
            result = result.replaceAll("0+?$", "");//去掉多余的0
            result = result.replaceAll("[.]$", "");//如最后一位是.则去掉
        }
        return result;
    }

    /**
     * unicode 转字符串
     */
    public static String unicode2String(String theString) {
        char aChar;
        int len = theString.length();
        StringBuffer outBuffer = new StringBuffer(len);
        for (int x = 0; x < len; ) {
            aChar = theString.charAt(x++);
            if (aChar == '\\') {
                aChar = theString.charAt(x++);
                if (aChar == 'u') {
                    // Read the xxxx
                    int value = 0;
                    for (int i = 0; i < 4; i++) {
                        aChar = theString.charAt(x++);
                        switch (aChar) {
                            case '0':
                            case '1':
                            case '2':
                            case '3':
                            case '4':
                            case '5':
                            case '6':
                            case '7':
                            case '8':
                            case '9':
                                value = (value << 4) + aChar - '0';
                                break;
                            case 'a':
                            case 'b':
                            case 'c':
                            case 'd':
                            case 'e':
                            case 'f':
                                value = (value << 4) + 10 + aChar - 'a';
                                break;
                            case 'A':
                            case 'B':
                            case 'C':
                            case 'D':
                            case 'E':
                            case 'F':
                                value = (value << 4) + 10 + aChar - 'A';
                                break;
                            default:
                                throw new IllegalArgumentException(
                                        "Malformed   \\uxxxx   encoding.");
                        }

                    }
                    outBuffer.append((char) value);
                } else {
                    if (aChar == 't')
                        aChar = '\t';
                    else if (aChar == 'r')
                        aChar = '\r';
                    else if (aChar == 'n')
                        aChar = '\n';
                    else if (aChar == 'f')
                        aChar = '\f';
                    outBuffer.append(aChar);
                }
            } else
                outBuffer.append(aChar);
        }
        try {
            return URLDecoder.decode(outBuffer.toString(), "UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return outBuffer.toString();
    }

    /**
     * 字符串转换unicode
     */
    public static String string2Unicode(String string) {

        StringBuffer unicode = new StringBuffer();

        for (int i = 0; i < string.length(); i++) {

            // 取出每一个字符
            char c = string.charAt(i);

            // 转换为unicode
            unicode.append("\\u" + Integer.toHexString(c));
        }

        return unicode.toString();
    }

    /**
     * 6552秒  22：13
     */
    public static String convertProgress2Time(long milli) {
        int m = (int) (milli / (60));
        int s = (int) ((milli) % 60);
        String mm = String.format(Locale.getDefault(), "%02d", m);
        String ss = String.format(Locale.getDefault(), "%02d", s);
        return "mm:ss".replace("mm", mm).replace("ss", ss);
    }

    /**
     * @param numbers 464654 to 4.6w
     * @return 4.6w
     */
    public static String convertNormal2Unit(Object numbers) {
        if (numbers instanceof Long) {
            return showCount((Long) numbers);
        } else
            return numbers + "";
    }

    /**
     * @param price 99.4 to 99.<small>4</small>
     * @return 99.4
     */
    public static SpannableString dealPriceTu(String price, int bigSize, int smallSize) {
        try {
            float curPri = Float.parseFloat(price);
            int a = (int) curPri;
            int b = (int) (curPri % 1);
            SpannableStringBuilder spannableStringBuilder = new SpannableStringBuilder();
            SpannableString spa_a = new SpannableString(a + "");
            spa_a.setSpan(new AbsoluteSizeSpan(bigSize, true), 0, spa_a.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);

            SpannableString spa_b = new SpannableString("." + b);
            spa_b.setSpan(new AbsoluteSizeSpan(smallSize, true), 0, spa_b.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);

            spannableStringBuilder.append(spa_a).append(spa_b);
            return new SpannableString(spannableStringBuilder);
        } catch (NumberFormatException e) {
            e.printStackTrace();
            return new SpannableString(price);
        }
    }

    /**
     * @param str 3546 <s>3546</s>
     * @return
     */
    public static SpannableString dealStringShan(String str) {
        SpannableString spa_a = new SpannableString(str);
        spa_a.setSpan(new StrikethroughSpan(), 0, spa_a.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        return spa_a;
    }

    /**
     * @param phone 1386546467 to 138****6467
     * @return 138****6467
     */
    public static String convertPhone2Name(String phone) {
        if (!TextUtils.isEmpty(phone)) {
            if (phone.length() >= 4) {
                return "用户" + phone.substring(phone.length() - 4);
            } else {
                return "用户" + phone;
            }
        } else return "";
    }

    /**
     * @param time   243521554 to 2012-2-15 21:56
     * @param format yyyy-MM-dd
     * @return 2012-2-15 21:56
     */
    public static String timeStamp2Date(long time, String format) {
        if (format == null || format.isEmpty()) {
            format = "yyyy-MM-dd HH:mm:ss";
        }
        SimpleDateFormat sdf = new SimpleDateFormat(format);
        String ts = sdf.format(new Date(time));
        return ts;
    }


    /**
     * @param time   2012-2-15 21:56 to  243521554 秒级
     * @param format yyyy-MM-dd
     * @return 243521554
     */
    public static long string2timeLong(String time, String format) {
        if (format == null || format.isEmpty()) {
            format = "yyyy-MM-dd HH:mm:ss";
        }
        SimpleDateFormat sdf = new SimpleDateFormat(format);
        try {
            Date date = sdf.parse(time);
            return (date != null ? date.getTime() : 0) / 1000;
        } catch (ParseException e) {
            e.printStackTrace();
        }

        return 0;
    }

    /**
     * @param date      to 2012-2-15 21:56
     * @param formatStr yyyy-MM-dd
     * @return 2012-2-15 21:56
     */
    public static String data2String(Date date, String formatStr) {//可根据需要自行截取数据显示
        if (formatStr == null) {
            formatStr = "yyyy-MM-dd";
        }
        SimpleDateFormat format = new SimpleDateFormat(formatStr);
        return format.format(date);
    }

    /**
     * @param time 13654654654 "一小时前"
     * @return "一小时前"
     */
    public static String longTime2String(long time) {
        long nowTime = Calendar.getInstance().getTimeInMillis() / 1000;
        long disTime = nowTime - time;
        int MIN = 60;
        int HOUR = 60 * MIN;
        int DAY = 24 * HOUR;
        int YEAR = 365 * DAY;
        if (disTime < MIN) {//一分以内
            return "刚刚";
        } else if (disTime < HOUR) {//一小时以内
            int min = (int) (disTime / MIN);
            return min + "分钟前";
        } else if (disTime < 24 * HOUR) {//24小时以前
            int hour = (int) (disTime / HOUR);
            return hour + "小时前";
        } else if (disTime < 6 * DAY) {//5天以前
            int day = (int) (disTime / DAY);
            return day + "天前";
        } else if (disTime < YEAR) {//一年以内
            return timeStamp2Date(time * 1000, "MM/dd");
        } else //超过一年
            return timeStamp2Date(time * 1000, "yyyy-MM-dd");
    }

    /**
     * strTime要转换的string类型的时间，formatType要转换的格式yyyy-MM-dd HH:mm:ss//yyyy年MM月dd日
     * HH时mm分ss秒，
     * strTime的时间格式必须要与formatType的时间格式相同
     */
    public static Date stringToDate(String strTime, String formatType)
            throws ParseException {
        if (strTime == null) return null;
        SimpleDateFormat formatter = new SimpleDateFormat(formatType);
        Date date = null;
        date = formatter.parse(strTime);
        return date;
    }

    /**
     * 4532 -> 99+
     */
    public static String convertNum2SafeStr(int number) {
        if (number > 99) {
            return "99+";
        } else return number + "";
    }

    /**
     * @param number      52165 to 99+
     * @param limitNumber 99
     * @return 99+
     */
    public static String convertNum2SafeStr(int number, int limitNumber) {
        if (number > limitNumber) {
            return limitNumber + "+";
        } else return number + "";
    }

    /**
     * @param number 1 to 一月
     * @return 一月
     */
    public static String number2Month(int number) {
        switch (number) {
            case 1:
                return "一月";
            case 2:
                return "二月";
            case 3:
                return "三月";
            case 4:
                return "四月";
            case 5:
                return "五月";
            case 6:
                return "六月";
            case 7:
                return "七月";
            case 8:
                return "八月";
            case 9:
                return "九月";
            case 10:
                return "十月";
            case 11:
                return "十一月";
            default:
                return "十二月";
        }
    }

    /**
     * @param txt ABCD @param key A @return high A BCD
     */
    public static CharSequence highLightKey(String txt, String key) {
        if (!TextUtils.isEmpty(txt) && !TextUtils.isEmpty(key) && txt.contains(key)) {
            //关键代码
            SpannableStringBuilder builder = new SpannableStringBuilder(txt);
            ForegroundColorSpan foregroundColorSpan = new ForegroundColorSpan(Color.parseColor("#4B8EFF"));
            Pattern p = Pattern.compile(key);
            Matcher m = p.matcher(txt);
            while (m.find()) {
                int start = m.start();
                int end = m.end();
                builder.setSpan(foregroundColorSpan, start, end,
                        Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            }
            return builder;
        } else return txt;
    }


    /**
     * @param forTxt A  @param endTxt B @param forCol BLUE @param endCol GRAY @return <font color="blue">A</font><font color="gray">B</font>
     */
    public static CharSequence splicingColorWithForEnd(String forTxt, int forCol, String endTxt, int endCol) {
        SpannableStringBuilder spannableStringBuilder = new SpannableStringBuilder();
        SpannableString forSpan = new SpannableString(forTxt);
        forSpan.setSpan(new ForegroundColorSpan(forCol), 0, forSpan.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        SpannableString endSpan = new SpannableString(endTxt);
        endSpan.setSpan(new ForegroundColorSpan(endCol), 0, endSpan.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        spannableStringBuilder.append(forSpan);
        spannableStringBuilder.append(endSpan);
        return spannableStringBuilder;
    }

    /**
     * @param time 165465454798  @return MM月dd日 || yyyy年MM月dd日
     */
    public static String dakaDateString(long time) {
        Date today = Calendar.getInstance().getTime();
        Date date = new Date(time * 1000);
        if (date.getYear() == today.getYear()) {
            SimpleDateFormat format = new SimpleDateFormat("MM月dd日");
            return format.format(date);
        } else {
            SimpleDateFormat format = new SimpleDateFormat("yyyy年MM月dd日");
            return format.format(date);
        }
    }

    /**
     * 今天是否在此时间戳
     */
    public static boolean isToday(long time) {
        Date today = Calendar.getInstance().getTime();
        Date date = new Date(time * 1000);
        boolean isSameYear = date.getYear() == today.getYear();
        boolean isSameMonth = date.getMonth() == today.getMonth();
        boolean isSameDay = date.getDay() == today.getDay();
        return isSameYear && isSameMonth && isSameDay;
    }

    /**
     * 今天是否在此时间戳之后（含）
     */
    public static boolean isBehindToday(long time) {
        Date today = Calendar.getInstance().getTime();
        Date date = new Date(time * 1000);

        if (today.getYear() > date.getYear()) {
            return true;
        } else if(today.getYear() == date.getYear()){
            if(today.getMonth() > date.getMonth()){
                return true;
            } else if(today.getMonth() == date.getMonth()){
                return today.getDate() >= date.getDate();
            }
        }

        return false;
    }

    /**
     * 今天是否在此时间戳之前（含）
     */
    public static boolean isFrontToday(long time) {
        Date today = Calendar.getInstance().getTime();
        Date date = new Date(time * 1000);
        if (today.getYear() < date.getYear()) {
            return true;
        } else if(today.getYear() == date.getYear()){
            if(today.getMonth() < date.getMonth()){
                return true;
            } else if(today.getMonth() == date.getMonth()){
                return today.getDate() <= date.getDate();
            }
        }

        return false;
    }

    /**
     * 0 --》 A
     * */
    public static char convertNumber2ABC(int pos){
        char abc = (char) (Character.valueOf('A') + pos);
        return abc;
    }

    /** 1024 B -> 1KB*/
    public static String long2size(long size) {
        StringBuffer bytes = new StringBuffer();
        DecimalFormat format = new DecimalFormat("###.0");
        if (size >= 1024 * 1024 * 1024) {
            double i = (size / (1024.0 * 1024.0 * 1024.0));
            bytes.append(format.format(i)).append("GB");
        }
        else if (size >= 1024 * 1024) {
            double i = (size / (1024.0 * 1024.0));
            bytes.append(format.format(i)).append("MB");
        }
        else if (size >= 1024) {
            double i = (size / (1024.0));
            bytes.append(format.format(i)).append("KB");
        }
        else if (size < 1024) {
            if (size <= 0) {
                bytes.append("0B");
            }
            else {
                bytes.append((int) size).append("B");
            }
        }
        return bytes.toString();
    }

    /**
     * @param number 99 @return 90
     */
    public static int turnTen(int number){
        float f = (float) number/10;//9.9
        int i = (int) f;//9

        if(i < f){
            return i * 10;
        } else return number;//否则当前number就是 90 整除10
    }

    /**
     *
     * @param str 0193280128 @return 是否為数字
     */
    public static boolean isNumeric(String str){
        if(safeString(str).isEmpty()){
            str = "-1";
        }
        Pattern pattern = Pattern.compile("[0-9]*");
        Matcher isNum = pattern.matcher(str);
        return isNum.matches();
    }


    public static long convertStr2Long(String text) {
        if(text == null){
            return -1;
        }
        BigDecimal bigDecimal = null;
        try {
            bigDecimal = new BigDecimal(text.replace(",", "").trim());
        } catch (NumberFormatException e) {
            return -1;
        }
        String value = bigDecimal.setScale(0, RoundingMode.HALF_DOWN).toPlainString();
        return Long.parseLong(value);
    }
}
