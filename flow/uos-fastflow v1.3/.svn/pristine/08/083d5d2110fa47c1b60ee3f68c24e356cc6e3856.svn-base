package com.zterc.uos.fastflow.state;

public class WMTransitionInstanceAction extends WMObjectAction {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	/** @see #ACTION_COMPLETE */
	public static final String ACTION_COMPLETE_CODE = "complete"; // 0
	/** @see #ACTION_ROLLBACK */
	public static final String ACTION_ROLLBACK_CODE = "rollback"; // 1
	/** @see #ACTION_ARCHIVE */
	public static final String ACTION_ARCHIVE_CODE = "archive"; // 2
	/** @see #ACTION_COMPENSATE */
	public static final String ACTION_COMPENSATE_CODE = "compensate"; // 3
	/** 变迁实例的完成操作 */
	public static final WMTransitionInstanceAction ACTION_COMPLETE = new WMTransitionInstanceAction(
			WMTransitionInstanceState.COMPLETE_ACTION);
	/** 变迁实例的回退操作 */
	public static final WMTransitionInstanceAction ACTION_ROLLBACK = new WMTransitionInstanceAction(
			WMTransitionInstanceState.ROLLBACK_ACTION);
	/** 变迁实例的归档操作 */
	public static final WMTransitionInstanceAction ACTION_ARCHIVE = new WMTransitionInstanceAction(
			WMTransitionInstanceState.ARCHIVE_ACTION);
	/** 因退单到目标环节后添加边操作 **/
	public static final WMTransitionInstanceAction ACTION_COMPENSATE = new WMTransitionInstanceAction(
			WMTransitionInstanceState.COMPENSATE_ACTION);
	public static final String[] CODES = { ACTION_COMPLETE_CODE, ACTION_ROLLBACK_CODE, ACTION_ARCHIVE_CODE,
			ACTION_COMPENSATE_CODE };

	private static final WMTransitionInstanceAction[] VALUES = { ACTION_COMPLETE, ACTION_ROLLBACK, ACTION_ARCHIVE,
			ACTION_COMPENSATE };

	protected WMTransitionInstanceAction(int action_int) {
		super(action_int);
	}

	public static WMTransitionInstanceAction fromInt(int action_int) {
		return new WMTransitionInstanceAction(action_int);
	}

	public static WMTransitionInstanceAction fromCode(String code) {
		for (int i = 0; i < CODES.length; i++) {
			if (CODES[i].equals(code)) {
				return VALUES[i];
			}
		}
		throw new IllegalArgumentException(code);
	}

	public static WMTransitionInstanceAction[] ACTIONS() {
		return VALUES;
	}

	protected String[] getActionCodes() {
		return CODES;
	}
}
