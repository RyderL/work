package com.zterc.uos.fastflow.state;

public final class WMActivityInstanceState extends WMObjectState {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/** @see #OPEN_INITIATED */
	public static final int OPEN_INITIATED_INT = 0;
	/** @see #OPEN_SUSPENDED */
	public static final int OPEN_SUSPENDED_INT = 1;
	/** @see #OPEN_RUNNING */
	public static final int OPEN_RUNNING_INT = 2;
	/** @see #CLOSED_ABORTED */
	public static final int CLOSED_ABORTED_INT = 3;
	/** @see #CLOSED_TERMINATED */
	public static final int CLOSED_TERMINATED_INT = 4;
	/** @see #CLOSED_COMPLETED */
	public static final int CLOSED_COMPLETED_INT = 5;
	/** @see #DISABLED */
	public static final int DISABLED_INT = 6;
	/** @see #ARCHIVED */
	public static final int ARCHIVED_INT = 7;

	/** @see #OPEN_INITIATED */
	public static final String OPEN_INITIATED_TAG = "open.initiated";
	/** @see #OPEN_SUSPENDED */
	public static final String OPEN_SUSPENDED_TAG = "open.suspended";
	/** @see #OPEN_RUNNING */
	public static final String OPEN_RUNNING_TAG = "open.running";
	/** @see #CLOSED_ABORTED */
	public static final String CLOSED_ABORTED_TAG = "closed.aborted";
	/** @see #CLOSED_TERMINATED */
	public static final String CLOSED_TERMINATED_TAG = "closed.terminated";
	/** @see #CLOSED_COMPLETED */
	public static final String CLOSED_COMPLETED_TAG = "closed.completed";
	/** @see #DISABLED */
	public static final String DISABLED_TAG = "disabled";
	/** @see #ARCHIVED */
	public static final String ARCHIVED_TAG = "archived";

	/** 状态标签集合 */
	private static final String[] TAGS = { OPEN_INITIATED_TAG,
			OPEN_SUSPENDED_TAG, OPEN_RUNNING_TAG, CLOSED_ABORTED_TAG,
			CLOSED_TERMINATED_TAG, CLOSED_COMPLETED_TAG, DISABLED_TAG,
			ARCHIVED_TAG };

	/** 活动实例的初始化状态，当所有启动条件满足时进入open.notrunning状态 */
	public static final WMActivityInstanceState OPEN_INITIATED = new WMActivityInstanceState(
			OPEN_INITIATED_INT);
	/** Execution of the activity instance was temporarily suspended. */
	public static final WMActivityInstanceState OPEN_SUSPENDED = new WMActivityInstanceState(
			OPEN_SUSPENDED_INT);
	/** The activity instance is executing. */
	public static final WMActivityInstanceState OPEN_RUNNING = new WMActivityInstanceState(
			OPEN_RUNNING_INT);
	/**
	 * Enactment of the activity instance has been aborted, probably due to
	 * abortion of the owning process instance. (See the specification of
	 * WMAbortProcessInstance for a definition of abortion in contrast to
	 * termination).
	 */
	public static final WMActivityInstanceState CLOSED_ABORTED = new WMActivityInstanceState(
			CLOSED_ABORTED_INT);
	/**
	 * Enactment of the activity instance has been terminated , probably due to
	 * termination of the owning process instance (see the specification of
	 * WMTerminateProcessInstance for a definition of termination in contrast to
	 * abortion).
	 */
	public static final WMActivityInstanceState CLOSED_TERMINATED = new WMActivityInstanceState(
			CLOSED_TERMINATED_INT);
	/**
	 * Enactment of the activity instance has completed normally. (i.e., was not
	 * forced by a user or by a state change of its owning process instance).
	 */
	public static final WMActivityInstanceState CLOSED_COMPLETED = new WMActivityInstanceState(
			CLOSED_COMPLETED_INT);

	/** disable the activity instance */
	public static final WMActivityInstanceState DISABLED = new WMActivityInstanceState(
			DISABLED_INT);

	/** archive the activity instance */
	public static final WMActivityInstanceState ARCHIVED = new WMActivityInstanceState(
			ARCHIVED_INT);

	/** 活动实例集合 */
	private static final WMActivityInstanceState[] VALUES = { OPEN_INITIATED,
			OPEN_SUSPENDED, OPEN_RUNNING, CLOSED_ABORTED, CLOSED_TERMINATED,
			CLOSED_COMPLETED, DISABLED, ARCHIVED };

	/** Abort the instance. */
	public static final int ABORT_ACTION = 0;
	/** Complete the instance. */
	public static final int COMPLETE_ACTION = 1;
	/** Create the instance. */
	public static final int CREATE_ACTION = 2;
	/** Suspend the instance. */
	public static final int SUSPEND_ACTION = 3;
	/** Terminate the instance. */
	public static final int TERMINATE_ACTION = 4;
	/** Archive the instance. */
	public static final int ARCHIVE_ACTION = 5;
	/** Enable the instance. */
	public static final int ENABLE_ACTION = 6;
	/** Disable the instance. */
	public static final int DISABLE_ACTION = 7;

	// The following state transitions apply to ActivityInstance and WorkItem
	// new state = STATES[state][action]
	private static final int[][] STATES = {
			// ABORT0,COMPLETE1，CREATE2,SUSPEND3,TERMINATE4,ARCHIVE5,ENABLE6,DISABLE7
			{ // INITIATED(注：initiated通过suspend动作到suspended状态)
			CLOSED_ABORTED_INT, ILLEGAL_STATE_INT, ILLEGAL_STATE_INT,
					OPEN_SUSPENDED_INT, CLOSED_TERMINATED_INT,
					ILLEGAL_STATE_INT, OPEN_RUNNING_INT, DISABLED_INT }, { // SUSPENDED
			CLOSED_ABORTED_INT, ILLEGAL_STATE_INT, ILLEGAL_STATE_INT,
					ILLEGAL_STATE_INT, CLOSED_TERMINATED_INT,
					ILLEGAL_STATE_INT, OPEN_RUNNING_INT, DISABLED_INT }, { // RUNNING
			CLOSED_ABORTED_INT, CLOSED_COMPLETED_INT, ILLEGAL_STATE_INT,
					OPEN_SUSPENDED_INT, CLOSED_TERMINATED_INT,
					ILLEGAL_STATE_INT, ILLEGAL_STATE_INT, DISABLED_INT }, { // ABORTED
			ILLEGAL_STATE_INT, ILLEGAL_STATE_INT, ILLEGAL_STATE_INT,
					ILLEGAL_STATE_INT, ILLEGAL_STATE_INT, ARCHIVED_INT,
					ILLEGAL_STATE_INT, ILLEGAL_STATE_INT }, { // TERMINATED
			ILLEGAL_STATE_INT, ILLEGAL_STATE_INT, ILLEGAL_STATE_INT,
					ILLEGAL_STATE_INT, ILLEGAL_STATE_INT, ARCHIVED_INT,
					ILLEGAL_STATE_INT, ILLEGAL_STATE_INT }, { // COMPLETED
			ILLEGAL_STATE_INT, ILLEGAL_STATE_INT, ILLEGAL_STATE_INT,
					ILLEGAL_STATE_INT, ILLEGAL_STATE_INT, ARCHIVED_INT,
					ILLEGAL_STATE_INT, ILLEGAL_STATE_INT }, { // DISABLED
			ILLEGAL_STATE_INT, ILLEGAL_STATE_INT, ILLEGAL_STATE_INT,
					ILLEGAL_STATE_INT, ILLEGAL_STATE_INT, ARCHIVED_INT,
					ILLEGAL_STATE_INT, ILLEGAL_STATE_INT }, { // ARCHIVED
			ILLEGAL_STATE_INT, ILLEGAL_STATE_INT, ILLEGAL_STATE_INT,
					ILLEGAL_STATE_INT, ILLEGAL_STATE_INT, ILLEGAL_STATE_INT,
					ILLEGAL_STATE_INT, ILLEGAL_STATE_INT } };

	// action = ACTIONS[state][newState]
	private static final int[][] ACTIONS = {
			// INITIATED,SUSPENDED,RUNNING,ABORTED,TERMINATED,COMPLETED,DISABLED,ARCHIVED
			{ // INITIATED
			NO_ACTION, SUSPEND_ACTION, ENABLE_ACTION, ABORT_ACTION,
					TERMINATE_ACTION, ILLEGAL_ACTION, DISABLE_ACTION,
					ILLEGAL_ACTION },
			{ // SUSPENDED
			ILLEGAL_ACTION, NO_ACTION, ENABLE_ACTION, ILLEGAL_ACTION,
					ABORT_ACTION, TERMINATE_ACTION, ILLEGAL_ACTION,
					DISABLE_ACTION, ILLEGAL_ACTION },
			{ // RUNNING
			ILLEGAL_ACTION, SUSPEND_ACTION, NO_ACTION, ABORT_ACTION,
					TERMINATE_ACTION, COMPLETE_ACTION, DISABLE_ACTION,
					ILLEGAL_ACTION },
			{ // ABORTED
			ILLEGAL_ACTION, ILLEGAL_ACTION, ILLEGAL_ACTION, NO_ACTION,
					ILLEGAL_ACTION, ILLEGAL_ACTION, ILLEGAL_ACTION,
					ARCHIVE_ACTION },
			{ // TERMINATED
			ILLEGAL_ACTION, ILLEGAL_ACTION, ILLEGAL_ACTION, ILLEGAL_ACTION,
					NO_ACTION, ILLEGAL_ACTION, ILLEGAL_ACTION, ARCHIVE_ACTION },
			{ // COMPLETED
			ILLEGAL_ACTION, ILLEGAL_ACTION, ILLEGAL_ACTION, ILLEGAL_ACTION,
					ILLEGAL_ACTION, NO_ACTION, ILLEGAL_ACTION, ARCHIVE_ACTION },
			{ // DISABLED
			ILLEGAL_ACTION, ILLEGAL_ACTION, ILLEGAL_ACTION, ILLEGAL_ACTION,
					ILLEGAL_ACTION, ILLEGAL_ACTION, NO_ACTION, ARCHIVE_ACTION },
			{ // ARCHIVED
			ILLEGAL_ACTION, ILLEGAL_ACTION, ILLEGAL_ACTION, ILLEGAL_ACTION,
					ILLEGAL_ACTION, ILLEGAL_ACTION, ILLEGAL_ACTION,
					ILLEGAL_ACTION, NO_ACTION } };

	/**
	 * 构造函数，通过状态编码构造活动实例状态对象
	 * 
	 * @param state
	 *            状态编码
	 */
	private WMActivityInstanceState(int state) {
		super(state);
	}

	/**
	 * 得到活动实例状态对象集合
	 * 
	 * @return 状态对象集合
	 */
	public static WMActivityInstanceState[] states() {
		return VALUES;
	}

	/**
	 * 从状态标签得到活动实例状态对象
	 * 
	 * @param state
	 *            状态标签
	 * @return 活动实例状态
	 */
	public static WMActivityInstanceState fromTag(String state) {
		return (WMActivityInstanceState) fromTag(TAGS, VALUES, state);
	}

	/**
	 * 从状态编码得到活动实例状态对象
	 * 
	 * @param state
	 *            状态编码
	 * @return 活动实例状态
	 */
	public static WMActivityInstanceState fromInt(int state) {
		return VALUES[state];
	}

	/**
	 * 状态是否为closed状态
	 * 
	 * @return 如果是closed状态，那么返回真，否则返回假。
	 */
	public boolean isClosed() {
		return _state == CLOSED_ABORTED_INT || _state == CLOSED_TERMINATED_INT
				|| _state == CLOSED_COMPLETED_INT;
	}

	/**
	 * 状态是否为open状态
	 * 
	 * @return 如果是open状态，那么返回真，否则返回假。
	 */
	public static boolean isOpen(int state) {
		return state == OPEN_INITIATED_INT || state == OPEN_SUSPENDED_INT
				|| state == OPEN_RUNNING_INT;
	}

	/**
	 * 状态是否为archived状态
	 * 
	 * @return 如果是archived状态，那么返回真，否则返回假。
	 */
	public boolean isArchived() {
		return _state == ARCHIVED_INT;
	}

	/**
	 * 状态是否为disabled状态
	 * 
	 * @return 如果是disabled状态，那么返回真，否则返回假。
	 */
	public boolean isDisabled() {
		return _state == DISABLED_INT;
	}

	/**
	 * 返回活动包含的子流程的状态
	 * 
	 * @return WMProcessInstanceState
	 */
	public WMProcessInstanceState tranformProcessState() {
		switch (_state) {
		case OPEN_INITIATED_INT:
			return WMProcessInstanceState.OPEN_NOTRUNNING_NOTSTARTED;
		case OPEN_SUSPENDED_INT:
			return WMProcessInstanceState.OPEN_NOTRUNNING_SUSPENDED;
		case OPEN_RUNNING_INT:
			return WMProcessInstanceState.OPEN_RUNNING;
		case CLOSED_ABORTED_INT:
			return WMProcessInstanceState.CLOSED_ABORTED;
		case CLOSED_TERMINATED_INT:
			return WMProcessInstanceState.CLOSED_TERMINATED;
		case CLOSED_COMPLETED_INT:
			return WMProcessInstanceState.CLOSED_COMPLETED;
		case DISABLED_INT:
			return WMProcessInstanceState.OPEN_NOTRUNNING_NOTSTARTED;
		case ARCHIVED_INT:
			return WMProcessInstanceState.ARCHIVED;
		}
		return WMProcessInstanceState.OPEN_NOTRUNNING_NOTSTARTED;
	}

	/**
	 * 返回活动中的转移状态
	 * 
	 * @return WMTransitionInstanceState
	 */
	public WMTransitionInstanceState transformTransitionState() {
		switch (_state) {
		case OPEN_INITIATED_INT:
		case OPEN_SUSPENDED_INT:
		case OPEN_RUNNING_INT:
		case CLOSED_ABORTED_INT:
		case CLOSED_TERMINATED_INT:
		case CLOSED_COMPLETED_INT:
			return WMTransitionInstanceState.ENABLED;
		case DISABLED_INT:
			return WMTransitionInstanceState.DISABLED;
		case ARCHIVED_INT:
			return WMTransitionInstanceState.ARCHIVED;
		}
		return WMTransitionInstanceState.ENABLED;
	}

	public WMWorkItemState transformWorkItemState() {
		switch (_state) {
		case OPEN_SUSPENDED_INT:
			return WMWorkItemState.OPEN_SUSPENDED;
		case OPEN_RUNNING_INT:
			return WMWorkItemState.OPEN_RUNNING;
		case CLOSED_ABORTED_INT:
			return WMWorkItemState.CLOSED_ABORTED;
		case CLOSED_TERMINATED_INT:
			return WMWorkItemState.CLOSED_TERMINATED;
		case CLOSED_COMPLETED_INT:
			return WMWorkItemState.CLOSED_COMPLETED;
		case DISABLED_INT:
			return WMWorkItemState.DISABLED;
		case ARCHIVED_INT:
			return WMWorkItemState.ARCHIVED;
		}
		return null;
	}

	/**
	 * 返回活动实例状态标签集合
	 * 
	 * @return 标签集合
	 */
	protected String[] getStateTags() {
		return TAGS;
	}

	/**
	 * 返回活动实例的对象集合
	 * 
	 * @return 对象集合
	 */
	protected WMObjectState[] getValues() {
		return VALUES;
	}

	/**
	 * 通过操作得到状态集合，主要是用于在当前状态下给定操作得到返回的状态
	 * 
	 * @return 状态对象集合
	 */
	protected int[] getStatesByAction() {
		return STATES[_state];
	}

	/**
	 * 通过状态得到状态集合，主要是用于在当前状态下在给定变迁状态得到返回的操作
	 * 
	 * @return 状态对象集合
	 */
	protected int[] getActionsByState() {
		return ACTIONS[_state];
	}

}
