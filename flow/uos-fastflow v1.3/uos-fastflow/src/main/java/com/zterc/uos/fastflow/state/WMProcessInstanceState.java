package com.zterc.uos.fastflow.state;

public final class WMProcessInstanceState extends WMObjectState {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	/** @see #OPEN_NOTRUNNING_NOTSTARTED */
	public static final int OPEN_NOTRUNNING_NOTSTARTED_INT = 0;
	/** @see #OPEN_NOTRUNNING_SUSPENDED */
	public static final int OPEN_NOTRUNNING_SUSPENDED_INT = 1;
	/** @see #OPEN_RUNNING */
	public static final int OPEN_RUNNING_INT = 2;
	/** @see #CLOSED_ABORTED */
	public static final int CLOSED_ABORTED_INT = 3;
	/** @see #CLOSED_TERMINATED */
	public static final int CLOSED_TERMINATED_INT = 4;
	/** @see #CLOSED_COMPLETED */
	public static final int CLOSED_COMPLETED_INT = 5;
	/** @see #ARCHIVED */
	public static final int ARCHIVED_INT = 6;
	/** @see #OPEN_ERROR */
	public static final int ERROR_INT = 7;
	/** 流程回退到开始节点后 */
	public static final int CLOSED_ZEROED_INT = 8; // 归零状态 add by fang.jin
													// 2006-03-17

	public static final int OPEN_RUNNING_ROLLBACK_INT = 9; // 退单状态 add by
															// fang.jin
															// 2006-09-27

	/** @see #OPEN_NOTRUNNING_NOTSTARTED */
	public static final String OPEN_NOTRUNNING_NOTSTARTED_TAG = "open.notrunning.notstarted";
	/** @see #OPEN_NOTRUNNING_SUSPENDED */
	public static final String OPEN_NOTRUNNING_SUSPENDED_TAG = "open.notrunning.suspended";
	/** @see #OPEN_RUNNING */
	public static final String OPEN_RUNNING_TAG = "open.running";
	/** @see #CLOSED_ABORTED */
	public static final String CLOSED_ABORTED_TAG = "closed.aborted";
	/** @see #CLOSED_TERMINATED */
	public static final String CLOSED_TERMINATED_TAG = "closed.terminated";
	/** @see #CLOSED_COMPLETED */
	public static final String CLOSED_COMPLETED_TAG = "closed.completed";
	/** 流程回退到开始后 */
	public static final String CLOSED_ZEROED_TAG = "closed.zeroed";
	/** @see #ARCHIVED */
	public static final String ARCHIVED_TAG = "archived";
	/** @see #OPEN_ERROR */
	public static final String ERROR_TAG = "error";

	private static final String[] TAGS = { OPEN_NOTRUNNING_NOTSTARTED_TAG,
			OPEN_NOTRUNNING_SUSPENDED_TAG, OPEN_RUNNING_TAG,
			CLOSED_ABORTED_TAG, CLOSED_TERMINATED_TAG, CLOSED_COMPLETED_TAG,
			ARCHIVED_TAG, ERROR_TAG, CLOSED_ZEROED_TAG };

	/** The process instance has been created, but was not started yet. */
	public static final WMProcessInstanceState OPEN_NOTRUNNING_NOTSTARTED = new WMProcessInstanceState(
			OPEN_NOTRUNNING_NOTSTARTED_INT);
	/** Execution of the process instance was temporarily suspended. */
	public static final WMProcessInstanceState OPEN_NOTRUNNING_SUSPENDED = new WMProcessInstanceState(
			OPEN_NOTRUNNING_SUSPENDED_INT);
	/** The process instance is executing. */
	public static final WMProcessInstanceState OPEN_RUNNING = new WMProcessInstanceState(
			OPEN_RUNNING_INT);
	/** The process instance turn to an exception handle **/
	public static final WMProcessInstanceState ERROR = new WMProcessInstanceState(
			ERROR_INT);
	/**
	 * Enactment of the process instance has been aborted by a user. (See the
	 * specification of WMAbortProcessInstance for a definition of abortion in
	 * contrast to termination).
	 */
	public static final WMProcessInstanceState CLOSED_ABORTED = new WMProcessInstanceState(
			CLOSED_ABORTED_INT);
	/**
	 * Enactment of the process instance has been terminated by a user. (See the
	 * specification of WMTerminateProcessInstance for a definition of
	 * termination in contrast to abortion).
	 */
	public static final WMProcessInstanceState CLOSED_TERMINATED = new WMProcessInstanceState(
			CLOSED_TERMINATED_INT);
	/**
	 * Enactment of the process instance has completed normally. (i.e., was not
	 * forced by a user).
	 */
	public static final WMProcessInstanceState CLOSED_COMPLETED = new WMProcessInstanceState(
			CLOSED_COMPLETED_INT);
	/** archive the process instance */
	public static final WMProcessInstanceState ARCHIVED = new WMProcessInstanceState(
			ARCHIVED_INT);
	/** 归零状态 */
	public static final WMProcessInstanceState CLOSED_ZEROED = new WMProcessInstanceState(
			CLOSED_ZEROED_INT);

	/** 退单状态 */
	public static final WMProcessInstanceState OPEN_RUNNING_ROLLBACK = new WMProcessInstanceState(
			OPEN_RUNNING_ROLLBACK_INT);

	private static final WMProcessInstanceState[] VALUES = {
			OPEN_NOTRUNNING_NOTSTARTED, OPEN_NOTRUNNING_SUSPENDED,
			OPEN_RUNNING, CLOSED_ABORTED, CLOSED_TERMINATED, CLOSED_COMPLETED,
			ARCHIVED, ERROR, CLOSED_ZEROED, OPEN_RUNNING_ROLLBACK };

	/** Abort the instance. */
	public static final int ABORT_ACTION = 0;
	/** Complete the instance. */
	public static final int COMPLETE_ACTION = 1;
	/** Create the instance. */
	public static final int CREATE_ACTION = 2;
	/** Resume the instance. */
	public static final int RESUME_ACTION = 3;
	/** Start the instance. */
	public static final int START_ACTION = 4;
	/** Suspend the instance. */
	public static final int SUSPEND_ACTION = 5;
	/** Terminate the instance. */
	public static final int TERMINATE_ACTION = 6;
	/** Archive the instance. */
	public static final int ARCHIVE_ACTION = 7;
	/** RESTART the instance. */
	public static final int RESTART_ACTION = 8;
	/** ZERO the instance. */
	public static final int ZERO_ACTION = 9;
	/** Rollback the instance. */
	public static final int ROLLBACK_ACTION = 10;

	// new state = STATES[state][action](9*11)
	// columns:ABORT0, COMPLETE1, CREATE2, RESUME3, START4, SUSPEND5,
	// TERMINATE6, ARCHIVE7,RESTART8,ZERO9,ROLLBACK10
	private static final int[][] STATES = {
			{ // NOTSTARTED
			CLOSED_ABORTED_INT, ILLEGAL_STATE_INT, ILLEGAL_STATE_INT,
					ILLEGAL_STATE_INT, OPEN_RUNNING_INT, ILLEGAL_STATE_INT,
					CLOSED_TERMINATED_INT, ILLEGAL_STATE_INT,
					ILLEGAL_STATE_INT, ILLEGAL_STATE_INT, ILLEGAL_STATE_INT },
			{ // SUSPENDED
			CLOSED_ABORTED_INT, ILLEGAL_STATE_INT, ILLEGAL_STATE_INT,
					OPEN_RUNNING_INT, ILLEGAL_STATE_INT, ILLEGAL_STATE_INT,
					CLOSED_TERMINATED_INT, ILLEGAL_STATE_INT,
					OPEN_NOTRUNNING_NOTSTARTED_INT, ILLEGAL_STATE_INT,
					OPEN_RUNNING_ROLLBACK_INT }, { // RUNNING
			CLOSED_ABORTED_INT, CLOSED_COMPLETED_INT, ILLEGAL_STATE_INT,
					ILLEGAL_STATE_INT, ILLEGAL_STATE_INT,
					OPEN_NOTRUNNING_SUSPENDED_INT, CLOSED_TERMINATED_INT,
					ILLEGAL_STATE_INT, OPEN_NOTRUNNING_NOTSTARTED_INT,
					OPEN_RUNNING_INT, CLOSED_ZEROED_INT,
					OPEN_RUNNING_ROLLBACK_INT }, { // ABORTED
			ILLEGAL_STATE_INT, ILLEGAL_STATE_INT, ILLEGAL_STATE_INT,
					ILLEGAL_STATE_INT, ILLEGAL_STATE_INT, ILLEGAL_STATE_INT,
					ILLEGAL_STATE_INT, ARCHIVED_INT, ILLEGAL_STATE_INT,
					ILLEGAL_STATE_INT, ILLEGAL_STATE_INT }, { // TERMINATED
			ILLEGAL_STATE_INT, ILLEGAL_STATE_INT, ILLEGAL_STATE_INT,
					ILLEGAL_STATE_INT, ILLEGAL_STATE_INT, ILLEGAL_STATE_INT,
					ILLEGAL_STATE_INT, ARCHIVED_INT, ILLEGAL_STATE_INT,
					ILLEGAL_STATE_INT, ILLEGAL_STATE_INT }, { // COMPLETED
			ILLEGAL_STATE_INT, ILLEGAL_STATE_INT, ILLEGAL_STATE_INT,
					ILLEGAL_STATE_INT, ILLEGAL_STATE_INT, ILLEGAL_STATE_INT,
					ILLEGAL_STATE_INT, ARCHIVED_INT, ILLEGAL_STATE_INT,
					ILLEGAL_STATE_INT, ILLEGAL_STATE_INT }, { // ARCHIVED
			ILLEGAL_STATE_INT, ILLEGAL_STATE_INT, ILLEGAL_STATE_INT,
					ILLEGAL_STATE_INT, ILLEGAL_STATE_INT, ILLEGAL_STATE_INT,
					ILLEGAL_STATE_INT, ILLEGAL_STATE_INT, ILLEGAL_STATE_INT,
					ILLEGAL_STATE_INT, ILLEGAL_STATE_INT }, { // ZEROED
			ILLEGAL_STATE_INT, ILLEGAL_STATE_INT, ILLEGAL_STATE_INT,
					ILLEGAL_STATE_INT, ILLEGAL_STATE_INT, ILLEGAL_STATE_INT,
					ILLEGAL_STATE_INT, ILLEGAL_STATE_INT, ILLEGAL_STATE_INT,
					ILLEGAL_STATE_INT, ILLEGAL_STATE_INT }, { // ROLLBACK
			ILLEGAL_STATE_INT, ILLEGAL_STATE_INT, ILLEGAL_STATE_INT,
					OPEN_RUNNING_INT, ILLEGAL_STATE_INT, ILLEGAL_STATE_INT,
					ILLEGAL_STATE_INT, ILLEGAL_STATE_INT, ILLEGAL_STATE_INT,
					ILLEGAL_STATE_INT, ILLEGAL_STATE_INT } };

	// action = ACTIONS[state][newState] (10*10)
	// columns:
	// NOTSTARTED,SUSPENDED,RUNNING,ABORTED,TERMINATED,COMPLETED,ARCHIVED,ERROR,ZERO,ROLLBACK
	private static final int[][] ACTIONS = { { // NOTSTARTED
			NO_ACTION, ILLEGAL_ACTION, START_ACTION, ABORT_ACTION,
					TERMINATE_ACTION, ILLEGAL_ACTION, ILLEGAL_ACTION,
					ERROR_ACTION, ILLEGAL_ACTION, ILLEGAL_ACTION }, { // SUSPENDED
			RESTART_ACTION, NO_ACTION, RESUME_ACTION, ABORT_ACTION,
					TERMINATE_ACTION, ILLEGAL_ACTION, ILLEGAL_ACTION,
					ERROR_ACTION, ILLEGAL_ACTION, ROLLBACK_ACTION }, { // RUNNING
			RESTART_ACTION, SUSPEND_ACTION, NO_ACTION, ABORT_ACTION,
					TERMINATE_ACTION, COMPLETE_ACTION, ILLEGAL_ACTION,
					ERROR_ACTION, ZERO_ACTION, ROLLBACK_ACTION }, { // ABORTED
			ILLEGAL_ACTION, ILLEGAL_ACTION, ILLEGAL_ACTION, NO_ACTION,
					ILLEGAL_ACTION, ILLEGAL_ACTION, ARCHIVE_ACTION,
					ERROR_ACTION, ILLEGAL_ACTION, ILLEGAL_ACTION }, { // TERMINATED
			ILLEGAL_ACTION, ILLEGAL_ACTION, ILLEGAL_ACTION, ILLEGAL_ACTION,
					NO_ACTION, ILLEGAL_ACTION, ARCHIVE_ACTION, ERROR_ACTION,
					ILLEGAL_ACTION, ILLEGAL_ACTION }, { // COMPLETED
			ILLEGAL_ACTION, ILLEGAL_ACTION, ILLEGAL_ACTION, ILLEGAL_ACTION,
					ILLEGAL_ACTION, NO_ACTION, ARCHIVE_ACTION, ERROR_ACTION,
					ILLEGAL_ACTION, ILLEGAL_ACTION }, { // ERROR
			ERROR_RESOLVED_ACTION, ERROR_RESOLVED_ACTION,
					ERROR_RESOLVED_ACTION, ERROR_RESOLVED_ACTION,
					ERROR_RESOLVED_ACTION, ERROR_RESOLVED_ACTION,
					ERROR_RESOLVED_ACTION, NO_ACTION, ILLEGAL_ACTION,
					ILLEGAL_ACTION }, { // ZERO
			ILLEGAL_ACTION, ILLEGAL_ACTION, ILLEGAL_ACTION, ILLEGAL_ACTION,
					ILLEGAL_ACTION, ILLEGAL_ACTION, ARCHIVE_ACTION,
					ERROR_ACTION, NO_ACTION, ILLEGAL_ACTION }, {// ROLLBACK
			ILLEGAL_ACTION, ILLEGAL_ACTION, RESUME_ACTION, ILLEGAL_ACTION,
					ILLEGAL_ACTION, ILLEGAL_ACTION, ARCHIVE_ACTION,
					ERROR_ACTION, NO_ACTION, ILLEGAL_ACTION } };

	//

	private WMProcessInstanceState(int state) {
		super(state);
	}

	public static WMProcessInstanceState[] states() {
		return VALUES;
	}

	public static WMProcessInstanceState fromTag(String state) {
		return (WMProcessInstanceState) fromTag(TAGS, VALUES, state);
	}

	public static WMProcessInstanceState fromInt(int state) {
		return VALUES[state];
	}

	public boolean isClosed() {
		return _state == CLOSED_ABORTED_INT || _state == CLOSED_TERMINATED_INT
				|| _state == CLOSED_COMPLETED_INT
				|| _state == CLOSED_ZEROED_INT;
	}

	public static boolean isOpen(int state) {
		return state == OPEN_NOTRUNNING_NOTSTARTED_INT
				|| state == OPEN_NOTRUNNING_SUSPENDED_INT
				|| state == OPEN_RUNNING_INT
				|| state == OPEN_RUNNING_ROLLBACK_INT;
	}

	public boolean isArchived() {
		return _state == ARCHIVED_INT;
	}

	/**
	 * 返回流程中的活动状态
	 * 
	 * @return WMActivityInstanceState
	 */
	public WMActivityInstanceState transformActivityState() {
		switch (_state) {
		case OPEN_NOTRUNNING_NOTSTARTED_INT:
			return WMActivityInstanceState.OPEN_INITIATED;
		case OPEN_NOTRUNNING_SUSPENDED_INT:
			return WMActivityInstanceState.OPEN_SUSPENDED;
		case OPEN_RUNNING_INT:
			return WMActivityInstanceState.OPEN_RUNNING;
		case CLOSED_ABORTED_INT:
			return WMActivityInstanceState.CLOSED_ABORTED;
		case CLOSED_TERMINATED_INT:
			return WMActivityInstanceState.CLOSED_TERMINATED;
		case CLOSED_COMPLETED_INT:
			return WMActivityInstanceState.CLOSED_COMPLETED;
		case ARCHIVED_INT:
			return WMActivityInstanceState.ARCHIVED;
		}
		return WMActivityInstanceState.OPEN_INITIATED;
	}

	protected String[] getStateTags() {
		return TAGS;
	}

	protected WMObjectState[] getValues() {
		return VALUES;
	}

	protected int[] getStatesByAction() {
		return STATES[_state];
	}

	protected int[] getActionsByState() {
		return ACTIONS[_state];
	}

}
