package com.zterc.uos.fastflow.state;

public final class WMWorkItemState extends WMObjectState {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/** @see #OPEN_SUSPENDED_RUNNING */
	public static final int OPEN_SUSPENDED_INT = 0;
	/** @see #OPEN_RUNNING */
	public static final int OPEN_RUNNING_INT = 1;
	/** @see #CLOSED_ABORTED */
	public static final int CLOSED_ABORTED_INT = 2;
	/** @see #CLOSED_TERMINATED */
	public static final int CLOSED_TERMINATED_INT = 3;
	/** @see #CLOSED_COMPLETED */
	public static final int CLOSED_COMPLETED_INT = 4;
	/** @see #CLOSED_TRANSMITTED */
	public static final int CLOSED_REASSIGNED_INT = 5;
	/** @see #DISABLED */
	public static final int DISABLED_INT = 6;
	/** @see #ARCHIVED */
	public static final int ARCHIVED_INT = 7;

	/** @see #OPEN_SUSPENDED */
	public static final String OPEN_SUSPENDED_TAG = "open.suspend";
	/** @see #OPEN_RUNNING */
	public static final String OPEN_RUNNING_TAG = "open.running";
	/** @see #CLOSED_ABORTED */
	public static final String CLOSED_ABORTED_TAG = "closed.aborted";
	/** @see #CLOSED_TERMINATED */
	public static final String CLOSED_TERMINATED_TAG = "closed.terminated";
	/** @see #CLOSED_COMPLETED */
	public static final String CLOSED_COMPLETED_TAG = "closed.completed";
	/** @see #CLOSED_TRANSMITTED */
	public static final String CLOSED_REASSIGNED_TAG = "closed.reassigned";
	/** @see #ARCHIVED */
	public static final String ARCHIVED_TAG = "archived";
	/** @see #DISABLED */
	public static final String DISABLED_TAG = "disabled";

	private static final String[] TAGS = { OPEN_SUSPENDED_TAG,
			OPEN_RUNNING_TAG, CLOSED_ABORTED_TAG, CLOSED_TERMINATED_TAG,
			CLOSED_COMPLETED_TAG, CLOSED_REASSIGNED_TAG, DISABLED_TAG,
			ARCHIVED_TAG };

	/** Execution of the work item was temporarily suspended. */
	public static final WMWorkItemState OPEN_SUSPENDED = new WMWorkItemState(
			OPEN_SUSPENDED_INT);
	/** The work item is executing. */
	public static final WMWorkItemState OPEN_RUNNING = new WMWorkItemState(
			OPEN_RUNNING_INT);
	/**
	 * Enactment of the work item has been aborted, probably due to abortion of
	 * the owning process instance. (See the specification of
	 * WMAbortProcessInstance for a definition of abortion in contrast to
	 * termination).
	 */
	public static final WMWorkItemState CLOSED_ABORTED = new WMWorkItemState(
			CLOSED_ABORTED_INT);
	/**
	 * Enactment of the work item has been terminated , probably due to
	 * termination of the owning process instance (see the specification of
	 * WMTerminateProcessInstance for a definition of termination in contrast to
	 * abortion).
	 */
	public static final WMWorkItemState CLOSED_TERMINATED = new WMWorkItemState(
			CLOSED_TERMINATED_INT);
	/**
	 * Enactment of the work item has completed normally. (i.e., was not forced
	 * by a user or by a state change of its owning process instance).
	 */
	public static final WMWorkItemState CLOSED_COMPLETED = new WMWorkItemState(
			CLOSED_COMPLETED_INT);

	/**
	 * Transmit the workitem
	 */
	public static final WMWorkItemState CLOSED_REASSIGNED = new WMWorkItemState(
			CLOSED_REASSIGNED_INT);

	/** disable the work item */
	public static final WMWorkItemState DISABLED = new WMWorkItemState(
			DISABLED_INT);

	/** archive the work item */
	public static final WMWorkItemState ARCHIVED = new WMWorkItemState(
			ARCHIVED_INT);

	private static final WMWorkItemState[] VALUES = { OPEN_SUSPENDED,
			OPEN_RUNNING, CLOSED_ABORTED, CLOSED_TERMINATED, CLOSED_COMPLETED,
			CLOSED_REASSIGNED, DISABLED, ARCHIVED };

	/** Abort the work item. */
	public static final int ABORT_ACTION = 0;
	/** Assign the work item. */
	public static final int REASSIGN_ACTION = 1;
	/** Complete the work item. */
	public static final int COMPLETE_ACTION = 2;
	/** Create the work item. */
	public static final int CREATE_ACTION = 3;
	/** Resume the work item. */
	public static final int RESUME_ACTION = 4;
	/** Suspend the work item. */
	public static final int SUSPEND_ACTION = 5;
	/** Terminate the work item. */
	public static final int TERMINATE_ACTION = 6;
	/** Archive(delete) the work item */
	public static final int ARCHIVE_ACTION = 7;
	/** Disable the work item */
	public static final int DISABLE_ACTION = 8;
	/** Accept the work item */
	public static final int ACCEPT_ACTION = 9;

	// The following state transitions apply to ActivityInstance and WorkItem
	// new state = STATES[state][action](10*10)
	// columns:ABORT, REASSIGN, COMPLETE, CREATE, RESUME, SUSPEND, TERMINATE,
	// ARCHIVE, DISABLE, ACCEPT
	private static final int[][] STATES = { { // SUSPENDED
			CLOSED_ABORTED_INT, ILLEGAL_STATE_INT, ILLEGAL_STATE_INT,
					ILLEGAL_STATE_INT, OPEN_RUNNING_INT, ILLEGAL_STATE_INT,
					CLOSED_TERMINATED_INT, ILLEGAL_STATE_INT, DISABLED_INT,
					ILLEGAL_STATE_INT }, { // RUNNING
			CLOSED_ABORTED_INT, CLOSED_REASSIGNED_INT, CLOSED_COMPLETED_INT,
					ILLEGAL_STATE_INT, ILLEGAL_STATE_INT, OPEN_SUSPENDED_INT,
					CLOSED_TERMINATED_INT, ARCHIVED_INT, ILLEGAL_STATE_INT,
					ILLEGAL_STATE_INT }, { // ABORTED
			ILLEGAL_STATE_INT, ILLEGAL_STATE_INT, ILLEGAL_STATE_INT,
					ILLEGAL_STATE_INT, ILLEGAL_STATE_INT, ILLEGAL_STATE_INT,
					ILLEGAL_STATE_INT, ARCHIVED_INT, ILLEGAL_STATE_INT,
					ILLEGAL_STATE_INT }, { // TERMINATED
			ILLEGAL_STATE_INT, ILLEGAL_STATE_INT, ILLEGAL_STATE_INT,
					ILLEGAL_STATE_INT, ILLEGAL_STATE_INT, ILLEGAL_STATE_INT,
					ILLEGAL_STATE_INT, ARCHIVED_INT, ILLEGAL_STATE_INT,
					ILLEGAL_STATE_INT }, { // COMPLETED
			ILLEGAL_STATE_INT, ILLEGAL_STATE_INT, ILLEGAL_STATE_INT,
					ILLEGAL_STATE_INT, ILLEGAL_STATE_INT, ILLEGAL_STATE_INT,
					ILLEGAL_STATE_INT, ARCHIVED_INT, ILLEGAL_STATE_INT,
					ILLEGAL_STATE_INT }, { // REASSIGNED
			ILLEGAL_STATE_INT, ILLEGAL_STATE_INT, ILLEGAL_STATE_INT,
					ILLEGAL_STATE_INT, ILLEGAL_STATE_INT, ILLEGAL_STATE_INT,
					ILLEGAL_STATE_INT, ARCHIVED_INT, ILLEGAL_STATE_INT,
					ILLEGAL_STATE_INT }, { // DISABLED
			ILLEGAL_STATE_INT, ILLEGAL_STATE_INT, ILLEGAL_STATE_INT,
					ILLEGAL_STATE_INT, ILLEGAL_STATE_INT, ILLEGAL_STATE_INT,
					ILLEGAL_STATE_INT, ARCHIVED_INT, ILLEGAL_STATE_INT,
					ILLEGAL_STATE_INT }, { // ARCHIVE
			ILLEGAL_STATE_INT, ILLEGAL_STATE_INT, ILLEGAL_STATE_INT,
					ILLEGAL_STATE_INT, ILLEGAL_STATE_INT, ILLEGAL_STATE_INT,
					ILLEGAL_STATE_INT, ILLEGAL_STATE_INT, ILLEGAL_STATE_INT,
					ILLEGAL_STATE_INT } };

	// action = ACTIONS[state][newState]
	private static final int[][] ACTIONS = {
			// SUSPENDED,RUNNING,ABORTED,TERMINATED,COMPLETED,REASSIGNED,DISABLED,ARCHIVED
			{ // SUSPENDED
			NO_ACTION, RESUME_ACTION, ABORT_ACTION, TERMINATE_ACTION,
					ILLEGAL_ACTION, ILLEGAL_ACTION, DISABLE_ACTION,
					ILLEGAL_ACTION },
			{ // RUNNING
			SUSPEND_ACTION, NO_ACTION, ABORT_ACTION, TERMINATE_ACTION,
					COMPLETE_ACTION, REASSIGN_ACTION, DISABLE_ACTION,
					ILLEGAL_ACTION },
			{ // ABORTED
			ILLEGAL_ACTION, ILLEGAL_ACTION, NO_ACTION, ILLEGAL_ACTION,
					ILLEGAL_ACTION, ILLEGAL_ACTION, ILLEGAL_ACTION,
					ARCHIVE_ACTION },
			{ // TERMINATED
			ILLEGAL_ACTION, ILLEGAL_ACTION, ILLEGAL_ACTION, NO_ACTION,
					ILLEGAL_ACTION, ILLEGAL_ACTION, ILLEGAL_ACTION,
					ARCHIVE_ACTION },
			{ // COMPLETED
			ILLEGAL_ACTION, ILLEGAL_ACTION, ILLEGAL_ACTION, ILLEGAL_ACTION,
					NO_ACTION, ILLEGAL_ACTION, ILLEGAL_ACTION, ARCHIVE_ACTION },
			{ // REASSIGNED
			ILLEGAL_ACTION, ILLEGAL_ACTION, ILLEGAL_ACTION, ILLEGAL_ACTION,
					ILLEGAL_ACTION, NO_ACTION, ILLEGAL_ACTION, ARCHIVE_ACTION },
			{ // DISABLED
			ILLEGAL_ACTION, ILLEGAL_ACTION, ILLEGAL_ACTION, ILLEGAL_ACTION,
					ILLEGAL_ACTION, ILLEGAL_ACTION, NO_ACTION, ARCHIVE_ACTION },
			{ // ARCHIVED
			ILLEGAL_ACTION, ILLEGAL_ACTION, ILLEGAL_ACTION, ILLEGAL_ACTION,
					ILLEGAL_ACTION, ILLEGAL_ACTION, ILLEGAL_ACTION, NO_ACTION } };

	private WMWorkItemState(int state) {
		super(state);
	}

	public static WMWorkItemState[] states() {
		return VALUES;
	}

	public static WMWorkItemState fromTag(String state) {
		return (WMWorkItemState) fromTag(TAGS, VALUES, state);
	}

	public static WMWorkItemState fromInt(int state) {
		return VALUES[state];
	}

	public boolean isClosed() {
		return _state == CLOSED_ABORTED_INT || _state == CLOSED_TERMINATED_INT
				|| _state == CLOSED_REASSIGNED_INT
				|| _state == CLOSED_COMPLETED_INT;
	}

	public boolean isOpen() {
		return _state == OPEN_SUSPENDED_INT || _state == OPEN_RUNNING_INT;
	}

	public boolean isArchived() {
		return _state == ARCHIVED_INT;
	}

	public boolean isDisabled() {
		return _state == DISABLED_INT;
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
