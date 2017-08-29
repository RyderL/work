package com.zterc.uos.fastflow.state;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import com.zterc.uos.fastflow.exception.WMTransitionNotAllowedException;
import com.zterc.uos.fastflow.exception.WMUnsupportedOperationException;

public abstract class WMObjectState implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/** an invalid state **/
	public static final int ILLEGAL_STATE_INT = -2;
	/** Action does not cause a state transition. */
	public static final int NO_ACTION = -1;
	/** Action is invalid for the current state. */
	public static final int ILLEGAL_ACTION = -2;
	/** Action is illegal to API caller, but is being forced by the engine. */
	public static final int FORCED_ACTION = -3;
	/** turn to the ERROR state **/
	public static final int ERROR_ACTION = -4;
	/** come back from ERROR state **/
	public static final int ERROR_RESOLVED_ACTION = -5;
	/** The object state code. */
	protected int _state;

	/**
	 * Construct a new <code>WMObjectState</code>. The array type parameters are
	 * references to arrays statically defined in the calling subclass.
	 * 
	 * @param state
	 *            The integer code for this state.
	 */
	protected WMObjectState(int state) {
		_state = state;
	}

	/**
	 * ���ַ���ǩ�õ�״̬����
	 * 
	 * @param tags
	 *            ��ǩ����
	 * @param values
	 *            ״̬���󼯺�
	 * @param state
	 *            ״̬��ǩ
	 * @return ״̬����
	 */
	protected static WMObjectState fromTag(final String[] tags,
			final WMObjectState[] values, String state) {

		for (int i = 0; i < tags.length; i++) {
			if (tags[i].equals(state)) {
				return values[i];
			}
		}
		throw new IllegalArgumentException(state);
	}

	/**
	 * Returns the list of states to which legal transitions are possible.
	 * 
	 * @return List of legal states.
	 */
	public WMObjectState[] getValidStates() {
		int[] thisState = getStatesByAction();
		int n = thisState.length;
		List<WMObjectState> statesList = new ArrayList<WMObjectState>(n);
		for (int i = 0; i < n; i++) {
			if ((thisState[i] != ILLEGAL_ACTION) && (thisState[i] != NO_ACTION)) {
				statesList.add(getValues()[thisState[i]]);
			}
		}
		return (WMObjectState[]) statesList
				.toArray(new WMObjectState[statesList.size()]);
	}

	/**
	 * Returns the action required to transition to a specified state.
	 * 
	 * @param newState
	 *            The new state required.
	 * @param throwException
	 *            Causes an exception to be thrown if the transition would be
	 *            illegal.
	 * @return <code>int</code>
	 * @throws WMTransitionNotAllowedException
	 *             if a transition from the current state to the new state would
	 *             be illegal.
	 */
	public int checkTransition(WMObjectState newState, boolean throwException) {
		int action = getActionsByState()[newState.intValue()];
		if ((action == ILLEGAL_ACTION || action == NO_ACTION) && throwException) {
			throw new WMTransitionNotAllowedException(this, newState);
		}
		return action;
	}

	/**
	 * Returns the action required to transition to a specified state.
	 * 
	 * @param newState
	 *            The new state required.
	 * @param throwException
	 *            Causes an exception to be thrown if the transition would be
	 *            illegal.
	 * @return <code>int</code>
	 * @throws WMTransitionNotAllowedException
	 *             if a transition from the current state to the new state would
	 *             be illegal.
	 */
	public int checkTransition(int newState, boolean throwException)
			throws WMTransitionNotAllowedException {
		int action = getActionsByState()[newState];
		if ((action == ILLEGAL_ACTION || action == NO_ACTION) && throwException) {
			WMObjectState[] states = getValues();
			throw new WMTransitionNotAllowedException(states[_state],
					states[newState]);
		}
		return action;
	}

	/**
	 * �ж��ڵ�ǰ״̬��,�����Ĳ����Ƿ��ܹ���״̬�Ϸ���Ǩ
	 * 
	 * @param action
	 *            �����Ĳ���
	 * @param throwException
	 *            ���Ϊ�棬��ô���Ϸ�ʱ�׳��쳣
	 * @return ״ֵ̬
	 * @throws WMUnsupportedOperationException
	 */
	public int checkAction(int action, boolean throwException)
			throws WMUnsupportedOperationException {
		int newState = getStatesByAction()[action];
		if (((newState == ILLEGAL_ACTION) || (newState == NO_ACTION))
				&& throwException) {
			throw new WMUnsupportedOperationException(Integer.toString(action));
		}
		return newState;
	}

	/**
	 * Returns the list of all state values applicable to this instance's class.
	 * This is a reference to a final array defined statically in the instance's
	 * subclass.
	 * 
	 * @return Array of state objects.
	 */
	protected abstract WMObjectState[] getValues();

	/**
	 * Returns the transitions from the current state, indexed by action.
	 * Illegal transitions are marked by the array element value
	 * {@link #ILLEGAL_ACTION}.
	 * 
	 * @return Array of state codes. This is a reference to a final array
	 *         defined statically in the instance's subclass.
	 */
	protected abstract int[] getStatesByAction();

	/**
	 * Returns the transitions from the current state, indexed by new state.
	 * Illegal transitions are marked by the array element value
	 * {@link #ILLEGAL_ACTION}.
	 * 
	 * @return Array of action codes. This is a reference to a final array
	 *         defined statically in the instance's subclass.
	 */
	protected abstract int[] getActionsByState();

	/**
	 * Returns the list of all state tags applicable to this instance's class.
	 * The array is indexed by state code. This is be a reference to a final
	 * array defined statically in the instance's subclass.
	 * 
	 * @return Array of state tags.
	 */
	protected abstract String[] getStateTags();

	/**
	 * Tests for object identity. Only one instance of each ordinal value can
	 * ever exist.
	 * 
	 * @param obj
	 *            The with which to compare object this instance.
	 * @return <code>true</code> if the two references point to the same object.
	 */
	public final boolean equals(Object obj) {
		// This works because we have ensured that only one instance of each
		// value can exist in a VM, even when deserializing instances from an
		// ObjectInputStream.
		if (obj instanceof WMObjectState) {
			return ((WMObjectState) obj).intValue() == _state;
		} else {
			return this == obj;
		}
	}

	/**
	 * Equal objects must have equal hash codes.
	 * 
	 * @return The hash code.
	 */
	public final int hashCode() {
		return _state;
	}

	/**
	 * Returns the object state as an integer. This ordinal state number is how
	 * the state is represented in persistence storage.
	 * 
	 * @return Ordinal state number, as defined in subclasses.
	 */
	public final int intValue() {
		return _state;
	}

	/**
	 * Returns the object state as a string.
	 * 
	 * @return The object state.
	 */
	public final String stringValue() {
		return getStateTags()[_state];
	}

	/**
	 * ���أ��õ��ض�����Ϣ��������״̬�ı����ǩ
	 * 
	 * @return <code>String</code>
	 */
	public final String toString() {
		String clazz = getClass().getName();
		clazz = clazz.substring(clazz.lastIndexOf('.') + 1);
		return clazz + "[state=" + stringValue() + ']';
	}
}
