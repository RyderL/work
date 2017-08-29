package com.zterc.uos.fastflow.holder;

import com.zterc.uos.fastflow.holder.model.ProcessModel;

public class ProcessLocalHolder {
	private static ThreadLocal<ProcessModel> processLocal = new ThreadLocal<ProcessModel>();

	public static ProcessModel get() {
		return processLocal.get();
	}

	public static void set(ProcessModel processModel) {
		processLocal.set(processModel);
	}

	public static void clear() {
		processLocal.remove();
	}
}
