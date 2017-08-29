package com.zterc.uos.fastflow.holder;

public class OperType {
	public static final int DEFAULT = -1;
	public static final int INSERT = 0;
	public static final int UPDATE = 1;

	public static Integer getOperType(Integer curType, Integer alterType) {
		if (alterType == INSERT || (curType != null && curType == INSERT)) {
			return INSERT;
		}
		return alterType;
	}

	public static boolean isPersist(Integer operType) {
		if (operType != null && (operType == INSERT || operType == UPDATE)) {
			return true;
		}
		return false;
	}

	public static boolean isInsert(Integer operType) {
		if (operType == null || operType == INSERT || operType == DEFAULT) {
			return true;
		}
		return false;
	}
}
