package com.zterc.uos.fastflow.core.assist;

import com.zterc.uos.fastflow.dto.process.ActivityInstanceDto;
import com.zterc.uos.fastflow.model.Activity;

public class ActivityContext implements java.io.Serializable {
	private static final long serialVersionUID = 1L;
	private Activity activity;
	private ActivityInstanceDto instance;

	public ActivityContext(Activity activity, ActivityInstanceDto instance) {
		this.activity = activity;
		this.instance = instance;
	}

	public Activity getActivity() {
		return activity;
	}

	public ActivityInstanceDto getInstance() {
		return instance;
	}
}