package org.ebaloo.itkeeps.httpclient;

import java.util.BitSet;

import org.ebaloo.itkeeps.Rid;

public class ParameterEncoder {

	static BitSet dontNeedEncoding = null;
	static final int caseDiff = ('a' - 'A');
	static {
		dontNeedEncoding = new BitSet(256);
		int i;
		for (i = 'a'; i <= 'z'; i++) {
			dontNeedEncoding.set(i);
		}
		for (i = 'A'; i <= 'Z'; i++) {
			dontNeedEncoding.set(i);
		}
		for (i = '0'; i <= '9'; i++) {
			dontNeedEncoding.set(i);
		}
		dontNeedEncoding.set('-');
		dontNeedEncoding.set('_');
		// dontNeedEncoding.set('.');
		dontNeedEncoding.set('*');
		// dontNeedEncoding.set('&');
		dontNeedEncoding.set('=');
	}

	public static String char2Unicode(char c) {
		if (dontNeedEncoding.get(c)) {
			return String.valueOf(c);
		}
		StringBuilder resultBuffer = new StringBuilder();
		resultBuffer.append("%");
		char ch = Character.forDigit((c >> 4) & 0xF, 16);
		if (Character.isLetter(ch)) {
			ch -= caseDiff;
		}
		resultBuffer.append(ch);
		ch = Character.forDigit(c & 0xF, 16);
		if (Character.isLetter(ch)) {
			ch -= caseDiff;
		}
		resultBuffer.append(ch);
		return resultBuffer.toString();
	}


	public static String encoding(final String url) {
		try {
		StringBuilder stringBuffer = new StringBuilder();
		
		byte[] buff = url.getBytes("UTF-8");
			for (byte aBuff : buff) {
				stringBuffer.append(char2Unicode((char) aBuff));
			}
		return stringBuffer.toString();
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
		
		
	}

	public static String encoding(Rid rid) {
		return encoding(rid.get());
	}

}