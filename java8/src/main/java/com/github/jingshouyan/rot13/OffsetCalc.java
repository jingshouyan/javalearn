package com.github.jingshouyan.rot13;

/**
 * @author jingshouyan
 * #date 2020/08/03 16:56
 */
public class OffsetCalc {

    private int[] range;
    private int offset;

    public static final int PAIR = 2;

    public OffsetCalc(int[] range, int offset) {
        if (range.length % PAIR != 0) {
            throw new IllegalArgumentException("range must be pairs");
        }
        for (int i = 0; i < range.length - 1; i++) {
            if (range[i + 1] <= range[i]) {
                String message = String.format("range[%d] <= range[%d]", i + 1, i);
                throw new IllegalArgumentException(message);
            }
        }
        int len = 0;
        for (int i = 0; i< range.length / PAIR;i++) {
            len += range[i+1] - range[i];
        }
        this.range = range;
        this.offset = offset % len;
    }

    /**
     * 偏移编码
     *
     * @param in 输入
     * @return 偏移编码后数值
     */
    public int encode(int in) {
        return offsetCalc(in, offset);
    }

    /**
     * 偏移解码
     *
     * @param in 输入
     * @return 偏移解码后数值
     */
    public int decode(int in) {
        return offsetCalc(in, -offset);
    }


    /**
     * 偏移计算
     *
     * @param in     输入
     * @param offset 偏移量
     * @return 偏移后数值
     */
    private int offsetCalc(int in, int offset) {
        if (offset == 0) {
            return in;
        }
        int position = -1;
        for (int i = 0; i < range.length; i++) {
            if (in >= range[i]) {
                position = i;
            } else {
                break;
            }
        }
        if (position % PAIR != 0) {
            return in;
        }
        int out = in;
        int _offset = offset;
        if (_offset > 0) {
            while (true) {
                int siteV = range[position + 1];
                if (out + _offset < siteV) {
                    return out + _offset;
                }
                _offset = _offset - (siteV - out);
                position += PAIR;
                if (position == range.length) {
                    position = 0;
                }
                out = range[position];
            }
        } else {
            while (true) {
                int siteV = range[position];
                if (out + _offset >= siteV) {
                    return out + _offset;
                }
                _offset = _offset + (out - siteV);
                if (position == 0) {
                    position = range.length;
                }
                position -= PAIR;
                out = range[position + 1];
            }
        }

    }

    public static void main(String[] args) {
        int[] range = new int[]{4, 13, 14, 20, 21, 30};
        OffsetCalc offsetCalc = new OffsetCalc(range, 61);
        for (int i = 1; i < 25; i++) {
            int encode = offsetCalc.encode(i);
            int decode = offsetCalc.decode(encode);
            String str = String.format("%d: encode=%d, decode=%d, ok=%b", i, encode, decode, i == decode);
            System.out.println(str);
        }
    }
}
