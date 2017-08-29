package com.zterc.uos.fastflow.state;

public abstract class WMObjectAction implements java.io.Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private int _action_int;

	protected WMObjectAction(int action_int) {
		this._action_int = action_int;
	}

	public int intValue() {
		return _action_int;
	}

	public String codeValue() {
		return getActionCodes()[_action_int];
	}

	protected abstract String[] getActionCodes();

	public boolean equals(Object parm) {
		if (parm instanceof WMObjectAction) {
			return ((WMObjectAction) parm).intValue() == _action_int;
		} else {
			return super.equals(parm);
		}
	}

	public int hashCode() {
		return _action_int;
	}

	public String toString() {
		return codeValue();
	}

}