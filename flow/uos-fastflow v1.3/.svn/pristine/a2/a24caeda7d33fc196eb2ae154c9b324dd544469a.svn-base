package com.zterc.uos.fastflow.parse;

import java.io.Serializable;
import java.util.Iterator;
import java.util.List;

import com.zterc.uos.fastflow.model.Activity;
import com.zterc.uos.fastflow.model.Transition;
import com.zterc.uos.fastflow.model.WorkflowProcess;

public class MatrixGraphic implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private int size; // 节点个数
	private boolean[][] graphic;
	private WorkflowProcess process;

	public MatrixGraphic(WorkflowProcess process) {
		this.process = process;
		this.size = process.getActivities().size();
		graphic = new boolean[size][size];
		List<Activity> activities = process.getActivities();
		Iterator<Activity> iter = activities.iterator();
		for (int i = 0; iter.hasNext(); i++) {
			Activity activity = (Activity) iter.next();
			activity.setOrder(i);
		}
		createGraphic();
		calculateReached();
		process.setMatrix(this);
	}

	// 连通矩阵初始化
	private void createGraphic() {
		List<Activity> activities = process.getActivities();
		Iterator<Activity> iter = activities.iterator();
		while (iter.hasNext()) {
			Activity fromActivity = (Activity) iter.next();
			int x = fromActivity.getOrder();
			List<Transition> transitions = fromActivity.getEfferentTransitions();
			Iterator<Transition> transIter = transitions.iterator();
			while (transIter.hasNext()) {
				Transition tran = (Transition) transIter.next();
				Activity toActivity = process.getActivityById(tran.getTo());
				graphic[x][toActivity.getOrder()] = true;
			}
		}
	}

	// floyd算法判断连通性
	private void calculateReached() {
		int i, j, k;
		for (i = 0; i < size; i++) {
			for (j = 0; j < size; j++) {
				for (k = 0; k < size; k++) {
					if (graphic[i][k] && graphic[k][j]) {
						graphic[i][j] = true;
					}
				}
			}
		}
	}

	/**
	 * 判断连通性
	 * 
	 * @param fromActivityId
	 *            String
	 * @param toActivityId
	 *            String
	 * @return boolean
	 */
	public boolean isCanReached(String fromActivityId, String toActivityId) {
		int x = process.getActivityById(fromActivityId).getOrder();
		int y = process.getActivityById(toActivityId).getOrder();
		return graphic[x][y];
	}

	public static void main(String[] args) {

	}
}
