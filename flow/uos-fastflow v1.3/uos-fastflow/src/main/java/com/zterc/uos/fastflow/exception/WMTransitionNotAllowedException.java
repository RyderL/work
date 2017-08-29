package com.zterc.uos.fastflow.exception;

import com.zterc.uos.fastflow.state.WMObjectState;

/**
 * 状态变迁不合法的异常，会具体指明变迁前的状态和要变迁的状态
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
		return oldState.getClass().getName() + "状态无法变更：[from:"
				+ oldState.toString() + ",to:" + newState.toString() + "]";
	}
}
