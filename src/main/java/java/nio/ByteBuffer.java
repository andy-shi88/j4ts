package java.nio;

import def.jsbn.BigInteger;

public class ByteBuffer {

    private byte[] bytes;

    private int cursor;

    private boolean bigEndian = true;

//    private static byte long7(long x) { return (byte)(x >> 56); }
//    private static byte long6(long x) { return (byte)(x >> 48); }
//    private static byte long5(long x) { return (byte)(x >> 40); }
//    private static byte long4(long x) { return (byte)(x >> 32); }
//    private static byte long3(long x) { return (byte)(x >> 24); }
//    private static byte long2(long x) { return (byte)(x >> 16); }
//    private static byte long1(long x) { return (byte)(x >>  8); }
//    private static byte long0(long x) { return (byte)(x      ); }

//    private static byte int3(int x) { return (byte)(x >> 24); }
//    private static byte int2(int x) { return (byte)(x >> 16); }
//    private static byte int1(int x) { return (byte)(x >>  8); }
//    private static byte int0(int x) { return (byte)(x      ); }

    private static byte short1(short x) { return (byte)(x >> 8); }
    private static byte short0(short x) { return (byte)(x     ); }

    public ByteBuffer(int size) {
        bytes = new byte[size];
        this.cursor = 0;
    }

    public static ByteBuffer allocate(int size) {
        return new ByteBuffer(size);
    }

    public final ByteBuffer order(ByteOrder bo) {
        bigEndian = (bo == ByteOrder.BIG_ENDIAN);
//        nativeByteOrder = (bigEndian == (Bits.byteOrder() == ByteOrder.BIG_ENDIAN));
        return this;
    }

    public void putInt(int x) {
//        this.bytes[this.cursor] = int0(x);
//        this.bytes[this.cursor + 1] = int1(x);
//        this.bytes[this.cursor + 2] = int2(x);
//        this.bytes[this.cursor + 3] = int3(x);
        BigInteger biginteger = new BigInteger(String.valueOf(x));
        byte[] bytes = biginteger.toByteArray();
        for (byte i = 0; i < 4; i++) {
            this.bytes[this.cursor + i] = bytes[3 - i];
        }
        cursor += 4;
    }

    public void put(byte x) {
        this.bytes[this.cursor] = x;
        cursor += 1;
    }

    public void put(byte[] src) {
//        int end = cursor + src.length;
        for (int i = 0; i < src.length; i++)
            this.put(src[i]);
//        cursor += src.length;
    }

    public void putShort(short x) {
        this.bytes[this.cursor] = short0(x);
        this.bytes[this.cursor + 1] = short1(x);
        cursor += 2;
    }

    public void putLong(long x) {
//        this.bytes[this.cursor] = long0(x);
//        this.bytes[this.cursor + 1] = long1(x);
//        this.bytes[this.cursor + 2] = long2(x);
//        this.bytes[this.cursor + 3] = long3(x);
//        this.bytes[this.cursor + 4] = long4(x);
//        this.bytes[this.cursor + 5] = long5(x);
//        this.bytes[this.cursor + 6] = long6(x);
//        this.bytes[this.cursor + 7] = long7(x);
        BigInteger biginteger = new BigInteger(String.valueOf(x));
        byte[] bytes = biginteger.toByteArray();
        for (byte i = 0; i < 8; i++) {
            this.bytes[this.cursor + i] = bytes[7 - i];
        }
        cursor += 8;
    }

    public byte[] array() {
        return this.bytes;

    }

}
