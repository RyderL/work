package com.zterc.uos.fastflow.core;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.dom4j.Attribute;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.QName;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.zterc.uos.base.helper.DomHelper;
import com.zterc.uos.base.helper.IntegerHelper;
import com.zterc.uos.base.helper.LongHelper;
import com.zterc.uos.base.helper.StringHelper;
import com.zterc.uos.fastflow.dto.process.ActivityInstanceDto;
import com.zterc.uos.fastflow.dto.process.ProcessInstanceDto;
import com.zterc.uos.fastflow.dto.process.TracerDto;
import com.zterc.uos.fastflow.dto.process.TracerInstanceDto;
import com.zterc.uos.fastflow.dto.process.TransitionInstanceDto;
import com.zterc.uos.fastflow.dto.specification.TacheDto;
import com.zterc.uos.fastflow.model.Activity;
import com.zterc.uos.fastflow.model.Transition;
import com.zterc.uos.fastflow.model.WorkflowProcess;
import com.zterc.uos.fastflow.service.ProcessDefinitionService;
import com.zterc.uos.fastflow.service.ProcessInstanceHisService;
import com.zterc.uos.fastflow.service.TacheService;
import com.zterc.uos.fastflow.service.TransitionInstanceHisService;
import com.zterc.uos.fastflow.state.WMProcessInstanceState;

/**
 * 流程实例和定义xml组装类
 * 
 * @author gongyi
 * 
 */
public class FastflowTraceHis {
	private static final Logger LOGGER = LoggerFactory
			.getLogger(FastflowTraceHis.class);

	private static String PENDING_TACHE = "未处理"; // 待执行环节
	private static String SUBJUNCTIVE_LINE = "虚拟线"; // 虚拟线

	private ProcessInstanceHisService processInstanceHisService;

	private ProcessDefinitionService processDefinitionService;

	private TransitionInstanceHisService transitionInstanceHisService;
	
	private TacheService tacheService;
	
	// 活动实例ID与XML中的层次的对应关系
	private Hashtable<String, Element> activityAndElement = new Hashtable<String, Element>();

	// 记录控制结点的的开始结点
	private Hashtable<Long, ActivityInstanceDto> activityToControl = new Hashtable<Long, ActivityInstanceDto>();

	Map<String,Object> shadowMap = new HashMap<String,Object>();
	/**
	 * 查询流程实例的xml
	 */
	public String qryProcessInstanceForTrace(Long processInstanceId,
			boolean isHistory) {
		/** 通过流程实例Id找流程实例对象 */
		ProcessInstanceDto processInstanceDto = null;
		TracerInstanceDto tracers = null;
		processInstanceDto = processInstanceHisService
				.queryProcessInstance(processInstanceId.toString());
		tracers = transitionInstanceHisService
				.findTransitionInstancesByPid(processInstanceId.toString());
		activityAndElement.clear();
		activityToControl.clear();
		return qryProcessInstanceTraceNoContrl(processInstanceDto, tracers);
	}

	/**
	 * 查询流程定义的xml
	 */
	public String qryProcessDefineForTrace(Long processInstanceId,
			boolean isHistory) throws SQLException, DocumentException,
			IOException {
		/** 通过流程实例Id找流程实例对象 */
		ProcessInstanceDto processInstanceDto = null;
		processInstanceDto = processInstanceHisService
				.queryProcessInstance(processInstanceId.toString());
		activityAndElement.clear();
		activityToControl.clear();
		return qryProcessDefineTrace(processInstanceDto);
	}

	@SuppressWarnings("all")
	private String qryProcessDefineTrace(ProcessInstanceDto processInstanceDto) {
		String returnValue = null;
		/** 通过流程定义到内存中找流程对象 */
		WorkflowProcess process = processDefinitionService
				.findWorkflowProcessById(processInstanceDto
						.getProcessDefineId());
		Document document = DomHelper.createDocument();
		Element workflowElement = document.addElement(new QName(
				"WorkflowProcess"));
		Element activitiesElement = DomHelper.appendChild(workflowElement,
				"Activities");
		Element transitionsElement = DomHelper.appendChild(workflowElement,
				"Transitions");
		// 获取定义activity集合
		Collection<Activity> activities = process.getActivities();
		Iterator<Activity> iter = activities.iterator();
		Element element = activitiesElement;
		while (iter.hasNext()) {
			Activity activity = iter.next();
			if (activity.getNodeType().equalsIgnoreCase("exception")) {
				// 异常节点不处理
				continue;
			}
			// 设置起点信息
			setActivityInfo(activity, element);
			// 起点的父层次,用来确定终点的层次
			LOGGER.info("活动实例ID:" + activity.getId() + " 活动实例名称:"
					+ activity.getName());
			Element fromParentElement = ((Element) activityAndElement
					.get(activity.getId())).getParent();
			// 得到终点集合
			Collection<Activity> toActivities = getEfferentActivities(process,
					activity);
			// 设置终点信息
			setToActivitiesInfo(toActivities, activity, fromParentElement);
			// 设置转移信息
			setTransitionsInfo(activity.getEfferentTransitions(),
					transitionsElement);
		}
		returnValue = document.asXML();
		return returnValue;
	}

	/**
	 * 从流程定义中找某个活动的下一步活动集合
	 * 
	 * @param process
	 *            WorkflowProcess
	 * @param activity
	 *            Activity
	 * @return Collection
	 */
	private List<Activity> getEfferentActivities(WorkflowProcess process,
			Activity activity) {
		List<Activity> efferentActivities = new ArrayList<Activity>();
		@SuppressWarnings("all")
		List<Transition> transitions = activity.getEfferentTransitions();
		for (Transition transition : transitions) {
			if (!transition.isControl()) {
				efferentActivities.add(transition.getToActivity());
			}
		}
		return efferentActivities;
	}

	/**
	 * 设置终点活动信息
	 * 
	 * @param process
	 *            WorkflowProcess
	 * @param activities
	 *            TracerDto
	 * @param fromActivity
	 *            Activity
	 * @param parent
	 *            Element
	 */
	private void setToActivitiesInfo(Collection<Activity> activities,
			Activity fromActivity, Element parent) {
		String fromType = fromActivity.getNodeType();
		if (fromType.equalsIgnoreCase("parallel")) { // 增加并行元素
			parent = DomHelper.appendChild(parent, "Parallel");
		}
		Iterator<Activity> iter = activities.iterator();
		while (iter.hasNext()) {
			Activity activity = iter.next();
			String type = activity.getNodeType();
			Element parentElement = parent; // 环节的父层次
			if (fromType.equalsIgnoreCase("parallel")) {
				Element branchElement = DomHelper.appendChild(parentElement,
						"Branch");
				parentElement = branchElement;
			}
			if (type.equalsIgnoreCase("tache")
					|| type.equalsIgnoreCase("control")
					|| type.equalsIgnoreCase("parallel")
					|| type.equalsIgnoreCase("finish")
					|| type.equalsIgnoreCase("AtomFlow")) {// added by xujun
															// 2010-09-20
															// ur62311
				// 如果是环节或控制节点(非开始\结束\并行\合并)或正向的并行环节或反向的合并环节
				setActivityInfo(activity, parentElement);
			} else if (type.equalsIgnoreCase("relation")) {
				// 如果是反向的并行环节或正向的合并环节,如果是并行结构则加到起点的同一层次上,否则加到起点的三层以上
				setActivityInfo(activity, parentElement.getParent().getParent());
			}
		}
	}

	/**
	 * 设置转移信息
	 * 
	 * @param transitions
	 *            TracerDto
	 * @param transitionsElement
	 *            Element
	 */
	private void setTransitionsInfo(Collection<Transition> transitions,
			Element transitionsElement) {
		Iterator<Transition> iter = transitions.iterator();
		while (iter.hasNext()) {
			Transition transition = iter.next();
			Element transitionElement = DomHelper.appendChild(
					transitionsElement, "Transition");
			transitionElement.addAttribute("id", transition.getId());
			transitionElement.addAttribute("from", transition.getFrom());
			transitionElement.addAttribute("to", transition.getTo());
			transitionElement.addAttribute("isRunning", "false");
			transitionElement.addAttribute("name",
					transition.getName() == null ? "" : transition.getName());
			transitionElement
					.addAttribute("lineType", transition.getLineType());
		}
	}

	/**
	 * 设置活动信息
	 * 
	 * @param activity
	 *            Activity
	 * @param parent
	 *            Element
	 */
	private void setActivityInfo(Activity activity, Element parent) {
		if (!activityAndElement.containsKey(activity.getId())
				&& !activity.getNodeType().equalsIgnoreCase("Exception")) {
			Element activityElement = DomHelper.appendChild(parent, "Activity");
			activityElement
					.addAttribute("id", String.valueOf(activity.getId()));
			activityElement.addAttribute("isRuning", "true");
			activityElement.addAttribute("type", activity.getNodeType());
			activityElement.addAttribute("name", activity.getName());
			activityElement.addAttribute("tacheId",
					activity.getTacheId() == null ? "" : activity.getTacheId());
			activityElement.addAttribute("direction", "1");
			String tacheIconName = "PUBLIC";
			if(activity.getTacheId() != null && !"".equals(activity.getTacheId())){
				TacheDto tacheDto = tacheService.queryTache(LongHelper.valueOf(activity.getTacheId()));
				if(tacheDto != null && tacheDto.getTacheIconName() != null && !"".equals(tacheDto.getTacheIconName())){
					tacheIconName = tacheDto.getTacheIconName();
				}
			}
			activityElement.addAttribute("tacheIconName",tacheIconName);
			activityAndElement.put(activity.getId(), activityElement);
		}
	}

	/**
	 * 展示UOS流程实例，去除控制结点，将将同个环节出多个工作项的合成一个
	 * 
	 * @param processInstanceDto
	 * @param tracers
	 * @return @
	 */
	@SuppressWarnings("all")
	private String qryProcessInstanceTraceNoContrl(
			ProcessInstanceDto processInstanceDto, TracerInstanceDto tracers) {
		String returnValue = null;
		try {
			/** 通过流程定义到内存中找流程对象 */
			WorkflowProcess process = processDefinitionService
					.findWorkflowProcessById(processInstanceDto
							.getProcessDefineId());
			Document document = DomHelper.createDocument();

			Element workflowElement = document.addElement(new QName(
					"WorkflowProcess"));
			Element activitiesElement = DomHelper.appendChild(workflowElement,
					"Activities");
			Element transitionsElement = DomHelper.appendChild(workflowElement,
					"Transitions");

			Iterator<TracerDto> tracerIter = tracers.getTracers().iterator();
			Element element = activitiesElement;
			while (tracerIter.hasNext()) {
				TracerDto tracer = tracerIter.next();
				ActivityInstanceDto fromActivityInstance = tracer
						.getFromActivityInstance();
				// 设置起点信息
				String type = process.getActivityById(
						fromActivityInstance.getActivityDefinitionId())
						.getNodeType();

				if (type.equals("Control")) {// 如果from为控制结点，找出其之前的不为控制类型的from结点
					fromActivityInstance = findControlFromActivity(
							fromActivityInstance, process, 0);
					type = process.getActivityById(
							fromActivityInstance.getActivityDefinitionId())
							.getNodeType();
				}

				setActivityInstanceInfo(fromActivityInstance, type, element);
				// 起点的父层次,用来确定终点的层次
				Element fromParentElement = (activityAndElement
						.get(fromActivityInstance.getId().toString()))
						.getParent();
				// 设置终点信息
				setToActivityInstancesInfo(process, tracer,
						fromActivityInstance, fromParentElement);
				// 设置转移信息
				setTransitionInstancesInfo(process, tracer, transitionsElement,
						fromActivityInstance);
			}
			// 如果流程未结束，创建"待执行环节",并创建所有未执行结束的环节到"待执行"环节的边
			int notFinished = tracers.getActivityInstancesNoInFrom().size();
			if (notFinished > 0
					&& WMProcessInstanceState.isOpen(processInstanceDto
							.getState())) {
				// 创建"待执行"环节
				Element virtualEle = DomHelper.appendChild(activitiesElement,
						"Activity");
				virtualEle.addAttribute("id", "0000000000");
				virtualEle.addAttribute("isRuning", "false");
				virtualEle.addAttribute("type", "Control");
				// virtualEle.addAttribute("name", "待执行");
				virtualEle.addAttribute("name", PENDING_TACHE);
				virtualEle.addAttribute("tacheId", "");
				virtualEle.addAttribute("direction", "1");
				// 所有未执行结束的环节到"待执行"环节的边
				Collection<ActivityInstanceDto> activityInstancesNoInFrom = tracers
						.getActivityInstancesNoInFrom();

				Iterator<ActivityInstanceDto> iter = activityInstancesNoInFrom
						.iterator();

				int count = 0;
				while (iter.hasNext()) {
					ActivityInstanceDto activityInstance = (ActivityInstanceDto) iter
							.next();
					if (activityInstance.getName().equals("合并节点")) {

						for (Element el : (List<Element>) transitionsElement
								.elements()) {
							if (el.attributeValue("to").equals("0000000000")) {
								el.setAttributeValue("to", String
										.valueOf(activityInstance.getId()));
							}
						}
					}
					Element transitionElement = DomHelper.appendChild(
							transitionsElement, "Transition");
					transitionElement.addAttribute("id", "V000000000"
							+ (++count));
					transitionElement.addAttribute("from",
							String.valueOf(activityInstance.getId()));
					transitionElement.addAttribute("to", "0000000000");
					transitionElement.addAttribute("isRunning", "true");
					// transitionElement.addAttribute("name", "虚拟线");
					transitionElement.addAttribute("name", SUBJUNCTIVE_LINE);
					transitionElement.addAttribute("direction", "1");
					transitionElement.addAttribute("lineType", "Virtual");
				}
			}
			returnValue = document.asXML();
		} catch (Exception e) {
			LOGGER.error("系统异常：" + e, e);
		}
		if (LOGGER.isDebugEnabled())
			LOGGER.debug("组装报文：" + returnValue);
		return returnValue;
	}

	private ActivityInstanceDto findControlFromActivity(
			ActivityInstanceDto activityInstanceDto, WorkflowProcess process,
			int deep) {
		ActivityInstanceDto fromActivityInstance = activityInstanceDto;// 如果查找不到就返回自己
		ActivityInstanceDto ai = activityToControl.get(fromActivityInstance
				.getId());
		if (ai != null) {
			String t = process.getActivityById(ai.getActivityDefinitionId())
					.getNodeType();
			if (!t.equalsIgnoreCase("control")) {
				fromActivityInstance = ai;
			} else {
				if (deep < 10) {// 防止死循环，只深入到第10层
					findControlFromActivity(ai, process, ++deep);
				}
			}
		}
		return fromActivityInstance;
	}

	/**
	 * 在XML中添加活动实例信息,UOS适用
	 * 
	 * @param activityInstance
	 *            ActivityInstanceDto
	 * @param type
	 *            WorkflowProcess
	 * @param parent
	 *            Element 起点的父层次
	 */
	private void setActivityInstanceInfo(ActivityInstanceDto activityInstance,
			String type, Element parent) {
		if (!activityAndElement
				.containsKey(activityInstance.getId().toString())) {
			LOGGER.info("活动名称:" + activityInstance.getName());
			LOGGER.info("parent是否为null:" + (parent == null));
			// mod by lyh ur59082 2010-08-04 begin
			if (parent != null) {
				Element activityElement = DomHelper.appendChild(parent,
						"Activity");
				activityElement.addAttribute("id",
						String.valueOf(activityInstance.getId()));
				activityElement.addAttribute("isRuning", "true");
				activityElement.addAttribute("type", type);
				activityElement
						.addAttribute("name", activityInstance.getName());
				activityElement.addAttribute(
						"tacheId",
						activityInstance.getTacheId() == null ? "" : String
								.valueOf(activityInstance.getTacheId()));
				String tacheCode = "";
				String tacheIconName = "PUBLIC";
				if(activityInstance.getTacheId() != null){
					TacheDto tacheDto = tacheService.queryTache(activityInstance.getTacheId());
					if(tacheDto.getTacheIconName() != null && !"".equals(tacheDto.getTacheIconName())){
						tacheIconName = tacheDto.getTacheIconName();
					}
					tacheCode = tacheDto.getTacheCode();
				}
				activityElement.addAttribute("tacheCode",tacheCode);
				activityElement.addAttribute("tacheIconName",tacheIconName);
				activityElement.addAttribute("direction",
						activityInstance.getDirection());
				activityElement.addAttribute("defId",
						activityInstance.getActivityDefinitionId());
				int state = activityInstance.getState();
				String stateStr = "10F";
				if (LOGGER.isDebugEnabled())
					LOGGER.debug("活动实例状态：" + state);
				switch (state) {
				case 0:// 未处理
					stateStr = "FFF";
					break;
				case 1:// 挂起
					stateStr = "10I";
					break;
				case 2:// 处理中
					stateStr = "10D";
					break;
				case 3:// 废弃
					stateStr = "10A";
					break;
				case 4:// 终止
					stateStr = "10E";
					break;
				case 5:// 处理完成
					stateStr = "10F";
					break;
				case 6:// 失效
					stateStr = "10A";
					break;
				case 7:// 归档
					stateStr = "10R";
					break;
				default:
					break;
				}
				activityElement.addAttribute("state", stateStr);
				//活动实例节点增加workitmeid属性 for 云平台 20160630
				if("Tache".equals(type)){
//					WorkItemDto workItemDto = workItemService.qryWorkItemByActInstId(activityInstance.getId());
					activityElement.addAttribute("workItemId", StringHelper.valueOf(activityInstance.getWorkItemId()));
				}
				//end
				activityAndElement.put(activityInstance.getId().toString(),
						activityElement);
			}
			// mod by lyh ur59082 2010-08-04 end
		}
	}

	/**
	 * 设置终点信息,适用于UOS流程引擎
	 * 
	 * @param process
	 *            WorkflowProcess
	 * @param tracer
	 *            TracerDto
	 * @param fromActivityInstance
	 *            ActivityInstanceDto
	 * @param parent
	 *            Element
	 */
	private void setToActivityInstancesInfo(WorkflowProcess process,
			TracerDto tracer, ActivityInstanceDto fromActivityInstance,
			Element parent) {
		Collection<TransitionInstanceDto> transitionInstances = tracer
				.getTransitionInstances();
		Iterator<TransitionInstanceDto> countIter = transitionInstances
				.iterator();
		// 估计从某个活动实例出发的边的方向都是相同的？？？ mod by fang.jin 2006.7.1
		String fromType = process.getActivityById(
				fromActivityInstance.getActivityDefinitionId()).getNodeType();
		String direction = ((TransitionInstanceDto) countIter.next())
				.getDirection(); // 边的方向
		boolean isParallel = ((fromType.equalsIgnoreCase("parallel") && direction
				.equals("1")) || (fromType.equalsIgnoreCase("relation") && direction
				.equals("0")));
		if (isParallel) { // 增加并行元素,且直接插入在起点元素后
			parent = DomHelper.insertNext(parent, "Parallel",
					(Element) activityAndElement.get(fromActivityInstance
							.getId().toString()));
		}
		Iterator<TransitionInstanceDto> iter = transitionInstances.iterator();
		while (iter.hasNext()) {
			TransitionInstanceDto transitionInstance = iter.next();
			ActivityInstanceDto activityInstance = getActivityInstance(
					tracer,
					String.valueOf(transitionInstance.getToActivityInstanceId()));
			String type = process.getActivityById(
					activityInstance.getActivityDefinitionId()).getNodeType();
			Element parentElement = parent; // 环节的父层次
			if (isParallel) {
				Element branchElement = DomHelper.appendChild(parentElement,
						"Branch");
				parentElement = branchElement;
			}
			String activityType = process.getActivityById(
					activityInstance.getActivityDefinitionId()).getNodeType(); // 活动的类型
			if ((type.equalsIgnoreCase("relation") && direction.equals("1"))
					|| (type.equalsIgnoreCase("parallel") && direction
							.equals("0"))) {
				LOGGER.info("parentElement.getParent().getParent()是否为null:"
						+ (parentElement.getParent().getParent() == null));
				setActivityInstanceInfo(activityInstance, activityType,
						parentElement.getParent().getParent());
			}
			if (type.equalsIgnoreCase("tache")
					// || type.equalsIgnoreCase("control")
					|| type.equalsIgnoreCase("start")
					|| type.equalsIgnoreCase("finish")
					|| (type.equalsIgnoreCase("parallel") && direction
							.equals("1"))
					|| (type.equalsIgnoreCase("relation") && direction
							.equals("0"))) {
				// 如果是环节或控制节点(非开始\结束\并行\合并)或正向的并行环节或反向的合并环节
				LOGGER.info("..........parentElement是否为null:"
						+ (parentElement == null));
				setActivityInstanceInfo(activityInstance, activityType,
						parentElement);
			} else if (type.equalsIgnoreCase("control")) {// 如to为控制结点，保存其from结点
				activityToControl.put(activityInstance.getId(),
						fromActivityInstance);
			}
		}
	}

	/**
	 * 设置转移信息,适用于UOS流程引擎
	 * 
	 * @param process
	 *            WorkflowProcess
	 * @param tracer
	 *            TracerDto
	 * @param transitionsElement
	 *            Element
	 */
	private void setTransitionInstancesInfo(WorkflowProcess process,
			TracerDto tracer, Element transitionsElement,
			ActivityInstanceDto fromActivityInstance) {
		Collection<TransitionInstanceDto> transitionInstances = tracer
				.getTransitionInstances();
		Iterator<TransitionInstanceDto> iter = transitionInstances.iterator();
		while (iter.hasNext()) {
			TransitionInstanceDto transitionInstance = iter.next();
			ActivityInstanceDto activityInstance = getActivityInstance(
					tracer,
					String.valueOf(transitionInstance.getToActivityInstanceId()));
			String type = process.getActivityById(
					activityInstance.getActivityDefinitionId()).getNodeType();
			if (!type.equalsIgnoreCase("control")) {
				Element transitionElement = DomHelper.appendChild(
						transitionsElement, "Transition");
				transitionElement.addAttribute("id",
						String.valueOf(transitionInstance.getId()));
				transitionElement.addAttribute("from",
						String.valueOf(fromActivityInstance.getId()));
				transitionElement.addAttribute("to", String
						.valueOf(transitionInstance.getToActivityInstanceId()));
				transitionElement.addAttribute("isRunning", "true");
				transitionElement
						.addAttribute(
								"name",
								transitionInstance
										.getTransitionDefinitionName() == null ? ""
										: transitionInstance
												.getTransitionDefinitionName());
				transitionElement.addAttribute("direction",
						transitionInstance.getDirection());
				String transitionDefinitionId = transitionInstance
						.getTransitionDefinitionId();
				if (transitionDefinitionId != null) {
					LOGGER.info("transitionDefinitionId="
							+ transitionDefinitionId);
					if (process
							.getTransitionByTransitionId(transitionDefinitionId) != null) {
						Transition transition = process
								.getTransitionByTransitionId(transitionDefinitionId);
						if (transition.isControl()) {
							transitionElement
									.addAttribute("lineType", "Normal");
						} else {
							transitionElement.addAttribute("lineType",
									transition.getLineType());
						}
					} else {
						LOGGER.info("边定义未找到，设置为Normal!");
						transitionElement.addAttribute("lineType", "Normal");
					}
				} else {
					// 针对跳转线，因为跳转时的线条上记录是之前的发生跳转的前一个环节，所以无法再次取到当时真正跳转的环节，所以这里改为Normal的话有可能会跟之前的线条重合
					transitionElement.addAttribute("lineType", "Normal");
				}
			}
		}
	}

	/**
	 * 在tracer的终点集合中查找活动实例
	 * 
	 * @param tracer
	 *            TracerDto
	 * @param activityInstanceId
	 *            String
	 * @return ActivityInstanceDto
	 */
	private ActivityInstanceDto getActivityInstance(TracerDto tracer,
			String activityInstanceId) {
		ActivityInstanceDto activityInstance = null;
		Collection<ActivityInstanceDto> activityInstances = tracer
				.getToActivityInstances();
		Iterator<ActivityInstanceDto> iter = activityInstances.iterator();
		while (iter.hasNext()) {
			ActivityInstanceDto toActivityInstance = iter.next();
			if (String.valueOf(toActivityInstance.getId()).equalsIgnoreCase(
					activityInstanceId)) {
				activityInstance = toActivityInstance;
				break;
			}
		}
		return activityInstance;
	}
	
	/**
	 * 查询影子流程xml
	 * @param processInstanceId
	 * @param isHistory
	 * @return
	 */
	public Object qryProcInstShadowForTrace(Long processInstanceId,
			Boolean isHistory) {
		/** 通过流程实例Id找流程实例对象 */
		ProcessInstanceDto processInstanceDto = null;
		TracerInstanceDto tracers = null;
		processInstanceDto = processInstanceHisService
				.queryProcessInstance(processInstanceId.toString());
		tracers = transitionInstanceHisService
				.findTransitionInstancesByPid(processInstanceId.toString());
		activityAndElement.clear();
		activityToControl.clear();
		shadowMap.clear();
		return qryProcessInstanceTraceShadow(processInstanceDto,tracers);
	}

	@SuppressWarnings("unchecked")
	private Object qryProcessInstanceTraceShadow(
			ProcessInstanceDto processInstanceDto,TracerInstanceDto tracers) {
		String returnValue = null;
		try {
			/** 通过流程定义到内存中找流程对象 */
			WorkflowProcess process = processDefinitionService
					.findWorkflowProcessById(processInstanceDto
							.getProcessDefineId());
			Document document = DomHelper.createDocument();

			Element workflowElement = document.addElement(new QName(
					"WorkflowProcess"));
			Element activitiesElement = DomHelper.appendChild(workflowElement,
					"Activities");
			Element transitionsElement = DomHelper.appendChild(workflowElement,
					"Transitions");

			Iterator<TracerDto> activityIter = tracers.getTracers().iterator();
			Element element = activitiesElement;
			while (activityIter.hasNext()) {
				TracerDto tracer = activityIter.next();
				ActivityInstanceDto fromActivityInstance = tracer.getFromActivityInstance();
				// 设置起点信息
				Activity fromActivity = process.getActivityById(
						fromActivityInstance.getActivityDefinitionId());
				String type = fromActivity.getNodeType();
				TacheDto tacheDto = null;
				if("Tache".equals(type) || fromActivity.isEndActivity() || fromActivity.isStartActivity()){
					if("Tache".equals(type)){
						tacheDto = tacheService.queryTache(fromActivityInstance.getTacheId());
					}
					setShadowActInstInfo(fromActivityInstance, type, element,tacheDto);
				}
				setShadowToActInstInfo(tracer,fromActivityInstance, element,process);
			}
			//设置线条信息
			List<Element> elements = activitiesElement.elements("Activity");
			
			for(int i=0;i<elements.size() && i+1<elements.size();i++){
				Element actEle = elements.get(i);
				Element toActEle = elements.get(i+1);
				setShadowTransistionInfo(process,transitionsElement,actEle,toActEle);
			}
			// 如果流程未结束，创建"待执行环节",并创建所有未执行结束的环节到"待执行"环节的边
			
			returnValue = document.asXML();
		} catch (Exception e) {
			LOGGER.error("系统异常：" + e, e);
		}
		if (LOGGER.isDebugEnabled())
			LOGGER.debug("组装报文：" + returnValue);
		return returnValue;
	}

	private void setShadowToActInstInfo(TracerDto tracer,
			ActivityInstanceDto fromActivityInstance, Element element,
			WorkflowProcess process) {
		Collection<TransitionInstanceDto> transitionInstances = tracer
				.getTransitionInstances();
		Iterator<TransitionInstanceDto> iter = transitionInstances.iterator();
		while (iter.hasNext()) {
			TransitionInstanceDto transitionInstance = iter.next();
			ActivityInstanceDto toActivityInstanceDto = getActivityInstance(
					tracer,
					String.valueOf(transitionInstance.getToActivityInstanceId()));
			Activity activity = process.getActivityById(toActivityInstanceDto.getActivityDefinitionId());
			String type = activity.getNodeType();
			if("Tache".equals(type) || activity.isEndActivity() || activity.isStartActivity()){
				TacheDto tacheDto = null;
				if("Tache".equals(type)){
					tacheDto = tacheService.queryTache(toActivityInstanceDto.getTacheId());
				}
				setShadowActInstInfo(toActivityInstanceDto, type, element, tacheDto);
			}
		}
	}

	private void setShadowTransistionInfo(WorkflowProcess process,
			Element transitionsElement, Element actEle,
			Element toActEle) {
			Element transitionElement = DomHelper.appendChild(
					transitionsElement, "Transition");
			transitionElement.addAttribute("id",UUID.randomUUID().toString());
			transitionElement.addAttribute("from",actEle.attributeValue("id"));
			transitionElement.addAttribute("to", toActEle.attributeValue("id"));
			transitionElement.addAttribute("isRunning", "true");
			transitionElement.addAttribute("name","");
			transitionElement.addAttribute("direction",actEle.attributeValue("direction"));
			transitionElement.addAttribute("lineType", "Normal");
	}

	private void setShadowActInstInfo(ActivityInstanceDto activityInstance,
			String type, Element parent,TacheDto tacheDto) {
		String shadowName = activityInstance.getName();
		if(tacheDto != null && tacheDto.getShadowName() != null
				&& !"".equals(tacheDto.getShadowName())){
			shadowName = tacheDto.getShadowName();
		}
		int state = IntegerHelper.valueOf(activityInstance.getActiveState()).intValue();
		String stateStr = "10F";	
		switch (state) {
		case 0:// 未处理
			stateStr = "FFF";
			break;
		case 1:// 挂起
			stateStr = "10I";
			break;
		case 2:// 处理中
			stateStr = "10D";
			break;
		case 3:// 废弃
			stateStr = "10A";
			break;
		case 4:// 终止
			stateStr = "10E";
			break;
		case 5:// 处理完成
			stateStr = "10F";
			break;
		case 6:// 失效
			stateStr = "10A";
			break;
		case 7:// 归档
			stateStr = "10R";
			break;
		default:
			break;
		}
		if (!activityAndElement.containsKey(shadowName)) {
			if (parent != null) {
				Element activityElement = DomHelper.appendChild(parent,
						"Activity");
				activityElement.addAttribute("id",
						String.valueOf(activityInstance.getId()));
				activityElement.addAttribute("isRuning", "true");
				activityElement.addAttribute("type", type);
				String tacheIconName = "PUBLIC";
				if(tacheDto != null && tacheDto.getTacheIconName() != null
						&& !"".equals(tacheDto.getTacheIconName())){
					tacheIconName = tacheDto.getTacheIconName();
				}
				activityElement.addAttribute("name", shadowName);
				activityElement.addAttribute("tacheIconName",tacheIconName);
				activityElement.addAttribute("direction",
						activityInstance.getDirection());
				activityElement.addAttribute("defId","");
				activityElement.addAttribute("state", stateStr);
				activityAndElement.put(shadowName,activityElement);
				shadowMap.put(shadowName, activityInstance);
			}
		}else{
			Element activityElement = activityAndElement.get(shadowName);
			Attribute attr = activityElement.attribute("state");
			String oldState = attr.getText();
			if(!oldState.equals("10D")){
				attr.setValue(stateStr);
				Attribute idAttr = activityElement.attribute("id");
				idAttr.setValue(String.valueOf(activityInstance.getId()));
			}
			
		}
	}

	public ProcessInstanceHisService getProcessInstanceHisService() {
		return processInstanceHisService;
	}

	public void setProcessInstanceHisService(
			ProcessInstanceHisService processInstanceHisService) {
		this.processInstanceHisService = processInstanceHisService;
	}

	public ProcessDefinitionService getProcessDefinitionService() {
		return processDefinitionService;
	}

	public void setProcessDefinitionService(
			ProcessDefinitionService processDefinitionService) {
		this.processDefinitionService = processDefinitionService;
	}

	public TransitionInstanceHisService getTransitionInstanceHisService() {
		return transitionInstanceHisService;
	}

	public void setTransitionInstanceHisService(
			TransitionInstanceHisService transitionInstanceHisService) {
		this.transitionInstanceHisService = transitionInstanceHisService;
	}

	public TacheService getTacheService() {
		return tacheService;
	}

	public void setTacheService(TacheService tacheService) {
		this.tacheService = tacheService;
	}

}
