package com.zterc.uos.fastflow.parse;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

public class JoinType implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	public static final int AND_INT = 0;
	public static final int XOR_INT = 1;
	public static final int OR_INT = 2;
	public static final JoinType AND = new JoinType(AND_INT);
	public static final JoinType XOR = new JoinType(XOR_INT);
	public static final JoinType OR = new JoinType(OR_INT);

	private static final String[] TAGS = { "AND", "XOR", "OR" };
	private static final JoinType[] VALUES = { AND, XOR, OR };
	private static final Map<String, JoinType> tagMap = new HashMap<String, JoinType>();

	private final int _value;

	static {
		for (int i = 0; i < TAGS.length; i++) {
			tagMap.put(TAGS[i], VALUES[i]);
		}
	}

	public static JoinType fromString(String tag) {
		return (JoinType) tagMap.get(tag);
	}

	private JoinType(int value) {
		_value = value;
	}

	public int getValue() {
		return _value;
	}

	public String toString() {
		return TAGS[_value];
	}

	public Object readResolve() {
		return VALUES[_value];
	}
}