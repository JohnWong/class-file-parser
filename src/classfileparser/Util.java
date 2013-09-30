package classfileparser;

public class Util {
	static final String HEX_CHAR = "0123456789ABCDEF";

	public static int byteToInt(byte[] src) {
		return byteToInt(src, 0, src.length);
	}

	public static int byteToInt(byte[] src, int start, int length) {
		int ret = 0;
		for (int i = 0; i < length; i++) {
			byte b = src[start + i];
			ret = ret * 0x100 + (b & 0xff);
		}
		return ret;
	}

	static String byteToHex(byte[] src) {
		StringBuilder sb = new StringBuilder();
		for (byte b : src) {
			int t = b & 0xff;
			sb.append(HEX_CHAR.charAt(t / 16));
			sb.append(HEX_CHAR.charAt(t % 16));
			sb.append(" ");
		}
		return sb.toString();
	}

	static String byteToBin(byte[] src) {
		StringBuilder sb = new StringBuilder();
		for (byte b : src) {
			int t = b & 0xff;
			for (int i = 0x80; i > 0; i /= 2) {
				if ((t & i) > 0)
					sb.append(1);
				else
					sb.append(0);
			}
			sb.append(" ");
		}
		return sb.toString();
	}

	public static String byteToHex(byte src) {
		StringBuilder sb = new StringBuilder();
		int t = src & 0xff;
		sb.append(HEX_CHAR.charAt(t / 16));
		sb.append(HEX_CHAR.charAt(t % 16));
		return sb.toString();
	}

	public static double byteToDouble(byte[] bytes) {
		int isNegtive = getBit(bytes, 0);
		int e = 0;
		for (int i = 1; i < 12; i++) {
			e = e * 2 + getBit(bytes, i);
		}
		e -= 1023;
		double m = 0.0;
		for (int i = 63; i >= 12; i--) {
			m = m / 2 + getBit(bytes, i);
		}
		m = m / 2 + 1;
		return (isNegtive > 0 ? -1.0f : 1.0f) * m * Math.pow(2, e);
	}

	public static float byteToFloat(byte[] bytes) {
		int isNegtive = getBit(bytes, 0);
		int e = 0;
		for (int i = 1; i < 9; i++) {
			e = e * 2 + getBit(bytes, i);
		}
		e -= 127;
		float m = 0f;
		for (int i = 31; i >= 9; i--) {
			m = m / 2 + getBit(bytes, i);
		}
		m = m / 2 + 1;
		return (isNegtive > 0 ? -1.0f : 1.0f) * m * (float) Math.pow(2, e);
	}

	public static int getBit(byte[] bytes, int index) {
		return (bytes[index / 8] & (1 << (7 - index % 8))) > 0 ? 1 : 0;
	}

	public static long byteToLong(byte[] bytes) {
		long ret = 0;
		for (byte b : bytes) {
			ret = ret * 0x100 + (b & 0xff);
		}
		return ret;
	}

	public static void main(String[] args) {

		// Integer
		// max
		byte[] src = new byte[] { 0x7f, (byte) 0xff, (byte) 0xff, (byte) 0xff };
		System.out.println(byteToInt(src));
		System.out.println(Integer.MAX_VALUE);
		// min
		src = new byte[] { (byte) 0x80, 0x00, 0x00, 0x00 };
		System.out.println(byteToInt(src));
		System.out.println(Integer.MIN_VALUE);

		// Float
		src = new byte[] { (byte) 0x7f, (byte) 0xef, (byte) 0xff, (byte) 0xff,
				(byte) 0xff, (byte) 0xff, (byte) 0xff, (byte) 0xff };
		System.out.println(byteToBin(src));
		System.out.println(byteToDouble(src));
		System.out.println(Double.MAX_VALUE);
		System.out.println(Double.toHexString(Float.MAX_VALUE));
	}

}
