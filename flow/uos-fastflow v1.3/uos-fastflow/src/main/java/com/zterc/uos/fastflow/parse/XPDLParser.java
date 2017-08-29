package com.zterc.uos.fastflow.parse;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

import com.zterc.uos.fastflow.model.Activity;
import com.zterc.uos.fastflow.model.Transition;
import com.zterc.uos.fastflow.model.WorkflowPackage;
import com.zterc.uos.fastflow.model.WorkflowProcess;
import com.zterc.uos.fastflow.model.condition.BasicCondition;
import com.zterc.uos.fastflow.model.condition.BasicXpression;
import com.zterc.uos.fastflow.model.condition.Condition;
import com.zterc.uos.fastflow.model.condition.ConditionType;
import com.zterc.uos.fastflow.model.condition.Xpression;

public class XPDLParser {
	private static final String WORKFLOW_PROCESSES = "WorkflowProcesses";
	private static final String WORKFLOW_PROCESS = "WorkflowProcess";
	private static final String ACTIVITIES = "Activities";
	private static final String ACTIVITY = "Activity";
	private static final String TRANSITIONS = "Transitions";
	private static final String TRANSITION = "Transition";
	private static final String FROM = "From";
	private static final String TO = "To";
	private static final String ID = "Id";
	private static final String NAME = "Name";
	private static final String TRANSITIONRESTRICTIONS = "TransitionRestrictions";
	private static final String TRANSITIONRESTRICTION = "TransitionRestriction";
	private static final String JOIN = "Join";
	private static final String EXTENDED_ATTRIBUTES = "ExtendedAttributes";
	private static final String EXTENDED_ATTRIBUTE = "ExtendedAttribute";
	private static final String VALUE = "Value";
	private static final String CONDITION = "Condition";
	private static final String XPRESSION = "Xpression";
	private static final String TYPE = "Type";

	public static WorkflowPackage parse(InputStream inputStream)
			throws DocumentException {
		SAXReader reader = new SAXReader();
		Document document = reader.read(inputStream);

		Element root = document.getRootElement();

		WorkflowPackage pkg = new WorkflowPackage();

		// 解析workflowProcess
		loadWorkflowProcess(pkg, root.element(WORKFLOW_PROCESSES));

		return pkg;
	}

	private static void loadWorkflowProcess(WorkflowPackage pkg, Element element) {
		WorkflowProcess workflowProcess = new WorkflowProcess();
		Element pElement = element.element(WORKFLOW_PROCESS);

		loadActivities(workflowProcess, pElement.element(ACTIVITIES));

		loadTransitions(workflowProcess, pElement.element(TRANSITIONS));

		MatrixGraphic matrixGraphic = new MatrixGraphic(workflowProcess);
		workflowProcess.setMatrix(matrixGraphic);

		pkg.setWorkflowProcess(workflowProcess);
	}

	private static void loadActivities(WorkflowProcess workflowProcess,
			Element activitiesEl) {
		List<Activity> activities = new ArrayList<Activity>();

		Map<String, Activity> activitiesMap = new HashMap<String, Activity>();

		@SuppressWarnings("unchecked")
		List<Element> actElList = activitiesEl.elements(ACTIVITY);
		for (int i = 0; i < actElList.size(); i++) {
			Element actEl = actElList.get(i);
			Activity activity = new Activity();
			activity.setWorkflowProcess(workflowProcess);
			activity.setId(actEl.attributeValue(ID));
			activity.setName(actEl.attributeValue(NAME));
			activity.setAfferentTransitions(new ArrayList<Transition>());
			activity.setEfferentTransitions(new ArrayList<Transition>());

			if (actEl.element(TRANSITIONRESTRICTIONS) != null) {
				activity.setJoinType(actEl.element(TRANSITIONRESTRICTIONS)
						.element(TRANSITIONRESTRICTION).element(JOIN)
						.attributeValue(TYPE));
			}

			@SuppressWarnings("unchecked")
			List<Element> attrsList = actEl.element(EXTENDED_ATTRIBUTES)
					.elements(EXTENDED_ATTRIBUTE);

			for (int j = 0; j < attrsList.size(); j++) {
				Element attrEl = attrsList.get(j);
				if (attrEl.attributeValue(NAME).equals("branchIndex")) {
					activity.setBranchIndex(attrEl.attributeValue(VALUE));
				} else if (attrEl.attributeValue(NAME).equals("nodeIndex")) {
					activity.setNodeIndex(attrEl.attributeValue(VALUE));
				} else if (attrEl.attributeValue(NAME).equals("nodeType")) {
					activity.setNodeType(attrEl.attributeValue(VALUE));
				} else if (attrEl.attributeValue(NAME).equals("ExTacheId")) {
					activity.setTacheId(attrEl.attributeValue(VALUE));
				} else if (attrEl.attributeValue(NAME).equals("ExTacheCode")) {
					activity.setTacheCode(attrEl.attributeValue(VALUE));
				} else if (attrEl.attributeValue(NAME).equals("ExTacheName")) {
					activity.setTacheName(attrEl.attributeValue(VALUE));
				} else if (attrEl.attributeValue(NAME).equals("numOfBranch")) {
					activity.setNumOfBranch(Integer.valueOf(attrEl
							.attributeValue(VALUE)));
				}
			}

			activitiesMap.put(activity.getId(), activity);
			activities.add(activity);
		}
		workflowProcess.setActivities(activities);
		workflowProcess.setActivitiesMap(activitiesMap);
	}

	private static void loadTransitions(WorkflowProcess workflowProcess,
			Element transEl) {
		List<Transition> transitions = new ArrayList<Transition>();
		Map<String, Transition> transitionsMap = new HashMap<String, Transition>();

		@SuppressWarnings("unchecked")
		List<Element> tranElList = transEl.elements(TRANSITION);
		for (int i = 0; i < tranElList.size(); i++) {
			Element tranEl = tranElList.get(i);
			Transition transition = new Transition();
			transition.setWorkflowProcess(workflowProcess);
			transition.setId(tranEl.attributeValue(ID));
			transition.setName(tranEl.attributeValue(NAME));
			transition.setFrom(tranEl.attributeValue(FROM));
			transition.setTo(tranEl.attributeValue(TO));

			@SuppressWarnings("unchecked")
			List<Element> attrsList = tranEl.element(EXTENDED_ATTRIBUTES)
					.elements(EXTENDED_ATTRIBUTE);

			for (int j = 0; j < attrsList.size(); j++) {
				Element attrEl = attrsList.get(j);
				if (attrEl.attributeValue(NAME).equals("LineType")) {
					transition.setLineType(attrEl.attributeValue(VALUE));
				}
			}

			// 设置条件
			Element conditionEl = tranEl.element(CONDITION);
			Condition condition = new BasicCondition();
			List<Xpression> xpressions = condition.getXpressions();
			@SuppressWarnings("unchecked")
			List<Element> epreList = conditionEl.elements(XPRESSION);
			for (int j = 0; j < epreList.size(); j++) {
				Element xpressionElement = epreList.get(j);
				xpressions.add(new BasicXpression(xpressionElement.getText()));
			}
			String typeString = conditionEl.attributeValue(TYPE);
			if (typeString != null) {
				condition.setType(ConditionType.fromString(typeString));
			}
			condition.setValue(conditionEl.getText());
			transition.setCondition(condition);

			transitionsMap.put(transition.getId(), transition);
			transitions.add(transition);

			workflowProcess.getActivitiesMap().get(transition.getFrom())
					.getEfferentTransitions().add(transition);
			workflowProcess.getActivitiesMap().get(transition.getTo())
					.getAfferentTransitions().add(transition);
		}
		workflowProcess.setTransitions(transitions);
		workflowProcess.setTransitionsMap(transitionsMap);
	}

}
