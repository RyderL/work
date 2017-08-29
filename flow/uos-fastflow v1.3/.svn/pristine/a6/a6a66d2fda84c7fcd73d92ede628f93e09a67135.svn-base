package com.zterc.uos.fastflow.state;

public class WMTransitionInstanceState extends WMObjectState {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/** @see #ENABLED */
	public static final int ENABLED_INT = 0;
	/** @see #DISABLED */
	public static final int DISABLED_INT = 1;
	/** @see #ARCHIVED */
	public static final int ARCHIVED_INT = 2;

	/** @see #ENABLED */
	public static final String ENABLED_TAG = "enabled";
	/** @see #DISABLED */
	public static final String DISABLED_TAG = "disabled";
	/** @see #ARCHIVED */
	public static final String ARCHIVED_TAG = "archived";

	private static final String[] TAGS = { ENABLED_TAG, DISABLED_TAG, ARCHIVED_TAG };

	/** The work item is ready, but only read. */
	public static final WMTransitionInstanceState ENABLED = new WMTransitionInstanceState(ENABLED_INT);
	/** The work item is ready, but has not been started yet. */
	public static final WMTransitionInstanceState DISABLED = new WMTransitionInstanceState(DISABLED_INT);
	/** Execution of the work item was temporarily suspended. */
	public static final WMTransitionInstanceState ARCHIVED = new WMTransitionInstanceState(ARCHIVED_INT);

	private static final WMTransitionInstanceState[] VALUES = { ENABLED, DISABLED, ARCHIVED };

	/** create transitioninstance because of complete the work item */
	public static final int COMPLETE_ACTION = 0;
	/** rollback processinstance */
	public static final int ROLLBACK_ACTION = 1;
	/** archive */
	public static final int ARCHIVE_ACTION = 2;
	/** compensate */
	public static final int COMPENSATE_ACTION = 3;

	// The following state transitions apply to ActivityInstance and WorkItem
	// new state = STATES[state][action]
	private static final int[][] STATES = {
			// COMPLETE, ROLLBACK, ARCHIVE, COMPENSATE
			{ ILLEGAL_ACTION, DISABLED_INT, ARCHIVED_INT, }, { // ENABLED
			ILLEGAL_ACTION, ILLEGAL_ACTION, ARCHIVED_INT }, { // DISABLED
			ILLEGAL_ACTION, ILLEGAL_ACTION, ARCHIVED_INT }, // ARCHIVED
	};

	// action = ACTIONS[state][newState]
	private static final int[][] ACTIONS = {
			// ENABLED, DIABLED, ARCHIVED,
			{ NO_ACTION, ROLLBACK_ACTION, ARCHIVE_ACTION }, { // ENABLED
			ILLEGAL_ACTION, NO_ACTION, ARCHIVE_ACTION }, { // DISABLED
			ILLEGAL_ACTION, ILLEGAL_ACTION, NO_ACTION }, // ARCHIVED
	};

	private WMTransitionInstanceState(int state) {
		super(state);
	}

	public static WMTransitionInstanceState[] states() {
		return VALUES;
	}

	public static WMTransitionInstanceState fromTag(String state) {
		return (WMTransitionInstanceState) fromTag(TAGS, VALUES, state);
	}

	public static WMTransitionInstanceState fromInt(int state) {
		return VALUES[state];
	}

	public boolean isEnabled() {
		return _state == ENABLED_INT;
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
