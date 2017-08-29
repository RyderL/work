package com.zterc.uos.fastflow.exception;

import com.zterc.uos.fastflow.state.WMObjectState;

/**
 * ״̬��Ǩ���Ϸ����쳣�������ָ����Ǩǰ��״̬��Ҫ��Ǩ��״̬
 */
public class WMTransitionNotAllowedException extends FastflowException {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private WMObjectState oldState;
	private WMObjectState newState;

	public WMTransitionNotAllowedException(WMObjectState oldState,
			WMObjectState newState) {
		this.oldState = oldState;
		this.newState = newState;
	}

	public String getMessage() {
		return oldState.getClass().getName() + "״̬�޷������[from:"
				+ oldState.toString() + ",to:" + newState.toString() + "]";
	}
}
