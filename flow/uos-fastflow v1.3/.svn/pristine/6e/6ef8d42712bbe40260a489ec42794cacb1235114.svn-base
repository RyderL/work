package com.ztesoft.uosflow.core.processor;

import java.util.List;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.zterc.uos.base.helper.StringHelper;
import com.zterc.uos.fastflow.dto.specification.CommandCfgDto;
import com.ztesoft.uosflow.core.processor.pull.QryThread;
import com.ztesoft.uosflow.core.processor.push.MqPushThread;
import com.ztesoft.uosflow.core.util.CommandPropUtil;

public class Controller implements Cloneable {
	private static Logger _logger = LoggerFactory.getLogger(Controller.class);

	private List<String> commandNameList;

	private static final String MODEL_PUSH = "PUSH";
	@SuppressWarnings("all")
	private static final String MODEL_PULL = "PULL";

	private String msgModel = "PULL";
	private String commandName;
	private String controllerName;
	private String queueName;

	private long nonDataSleepTime;
	private int dealThreadLiveTime = 1000;// 处理线程存活时间
	private int dealThreadCount;
	private int qryThreadNum = 1;// 查询线程数

	private EventInterface eventInterface;// 事件处理逻辑
	private MqInterface mqInterface;// 队列接收

	private ThreadPoolExecutor qryThreadPoolExecutor;// 线程执行器
	private ThreadPoolExecutor dealThreadPoolExecutor;// 线程执行器
	private BlockingQueue<DealThread> dealThreadQueue;

	public void init() {
		if(StringHelper.isEmpty(queueName)){
			for (int i = 0; i < commandNameList.size(); i++) {
				String commandName = commandNameList.get(i);
				// 根据commandName获取配置信息
				CommandCfgDto commandCfgDto = CommandPropUtil.getInstance()
						.getCommandCfgDtoByQue(commandName);
				for (int j = 0; j < commandCfgDto.getModeCount(); j++) {
					Controller controller = (Controller) this.clone();
					controller.setCommandName(commandName);
					
					controller.setControllerName(commandName + "-" + j);
					controller.setQueueName(commandCfgDto.getQueueName(j));
					controller.getMqInterface().setQueueName(controller.getQueueName());
					
					controller.setDealThreadCount(commandCfgDto.getDealCount());
					controller.setQryThreadNum(commandCfgDto.getQryCount());
					controller.setDealThreadLiveTime(commandCfgDto.getDealLiveTime());
					controller.setNonDataSleepTime(commandCfgDto.getSleepTime());
					
					controller.start();
				}
			}
		}else{
			_logger.info("---controller配置文件中传入的queueName="+queueName);
			// 如果配置文件中配置了队列名，则以配置文件为准进行初始化
			Controller controller = (Controller) this.clone();
//			controller.setCommandName(queueName);
			
			controller.setControllerName(queueName);
			controller.setQueueName(queueName);
			controller.getMqInterface().setQueueName(queueName);
			
			controller.setDealThreadCount(dealThreadCount);
			controller.setQryThreadNum(qryThreadNum);
			controller.setDealThreadLiveTime(dealThreadLiveTime);
			controller.setNonDataSleepTime(nonDataSleepTime);
			
			controller.start();
		}
		
	}

	public void start() {
		_logger.error("init Contoller:" + controllerName);
		this.initDealThread();
		this.startQryThread();
	}
	//modify by bobping  给线程增加线程名称
	private void initDealThread() {
		if(dealThreadCount==1){
			dealThreadPoolExecutor = new ThreadPoolExecutor(dealThreadCount,
					dealThreadCount, dealThreadLiveTime, TimeUnit.SECONDS,
					new LinkedBlockingQueue<Runnable>(),new ZThreadFactory(controllerName + "_Deal"));
		}else{
			dealThreadPoolExecutor = new ThreadPoolExecutor(dealThreadCount,
					dealThreadCount + 10, dealThreadLiveTime, TimeUnit.SECONDS,
					new LinkedBlockingQueue<Runnable>(),new ZThreadFactory(controllerName + "_Deal"));
		}
		

		dealThreadQueue = new LinkedBlockingQueue<DealThread>();

		for (int i = 0; i < dealThreadCount; i++) {
			String dealThreadName = controllerName + "_Deal[" + i + "]";
			DealThread dealThread = new DealThread(dealThreadName,
					dealThreadQueue, eventInterface);
			dealThreadQueue.add(dealThread);
		}
	}

	private void startQryThread() {
		if (MODEL_PUSH.equals(msgModel)) {
			MqPushThread mqPushThread = new MqPushThread(dealThreadQueue,
					dealThreadPoolExecutor, mqInterface);
			mqPushThread.start();
		} else {
			qryThreadPoolExecutor = new ThreadPoolExecutor(qryThreadNum,
					qryThreadNum + 10, dealThreadLiveTime, TimeUnit.SECONDS,
					new LinkedBlockingQueue<Runnable>());
			for (int i = 0; i < qryThreadNum; i++) {
				// 初始化查询线程
				String qryThreadName = controllerName + "_Qry[" + i + "]";
				QryThread qryThread = new QryThread(qryThreadName,
						nonDataSleepTime, dealThreadQueue,
						dealThreadPoolExecutor, mqInterface);
				qryThreadPoolExecutor.execute(qryThread);
			}
		}
	}

	public Object clone() {
		Controller con = null;
		try {
			con = (Controller) super.clone();
		} catch (Exception ex) {

		}
		if (con != null) {
			con.mqInterface = (MqInterface) mqInterface.clone();
		}
		return con;
	}

	public long getNonDataSleepTime() {
		return nonDataSleepTime;
	}

	public void setNonDataSleepTime(long nonDataSleepTime) {
		this.nonDataSleepTime = nonDataSleepTime;
	}

	public int getDealThreadCount() {
		return dealThreadCount;
	}

	public void setDealThreadCount(int dealThreadCount) {
		this.dealThreadCount = dealThreadCount;
	}

	public void setEventInterface(EventInterface eventInterface) {
		this.eventInterface = eventInterface;
	}

	public void setMqInterface(MqInterface mqInterface) {
		this.mqInterface = mqInterface;
	}

	public int getQryThreadNum() {
		return qryThreadNum;
	}

	public void setQryThreadNum(int qryThreadNum) {
		this.qryThreadNum = qryThreadNum;
	}

	public void setDealThreadLiveTime(int dealThreadLiveTime) {
		this.dealThreadLiveTime = dealThreadLiveTime;
	}

	public String getMsgModel() {
		return msgModel;
	}

	public void setMsgModel(String msgModel) {
		this.msgModel = msgModel;
	}

	public List<String> getCommandNameList() {
		return commandNameList;
	}

	public void setCommandNameList(List<String> commandNameList) {
		this.commandNameList = commandNameList;
	}

	public String getCommandName() {
		return commandName;
	}

	public void setCommandName(String commandName) {
		this.commandName = commandName;
	}

	public String getControllerName() {
		return controllerName;
	}

	public void setControllerName(String controllerName) {
		this.controllerName = controllerName;
	}

	public String getQueueName() {
		return queueName;
	}

	public void setQueueName(String queueName) {
		this.queueName = queueName;
	}

	public MqInterface getMqInterface() {
		return mqInterface;
	}
}
