package uk.co.firstchoice.common.base.jdbc;

import java.io.Serializable;

public class DatabaseConstants implements Serializable {

	private DatabaseConstants() {
	}

	public static final int JAVA_STRING = 1;

	public static final int JAVA_DATE = 2;

	public static final int JAVA_INT = 3;

	public static final int JAVA_SHORT = 4;

	public static final int JAVA_FLOAT = 5;

	public static final int JAVA_DOUBLE = 6;

	public static final String NULL_STR = "";

	public static final java.util.Date NULL_DATE = new java.util.Date(0);

	public static final int NULL_INT = Integer.MIN_VALUE;

	public static final short NULL_SHORT = Short.MIN_VALUE;

	public static final float NULL_FLOAT = Float.MIN_VALUE;

	public static final double NULL_DOUBLE = Double.MIN_VALUE;
}