package com.ztesoft.uosflow.jmx.server;

import java.io.File;
import java.lang.management.ManagementFactory;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.code.fqueue.FQueue;
import com.zterc.uos.fastflow.dto.specification.CommandCfgDto;
import com.zterc.uos.fastflow.service.CommandCfgService;

public class FqueueServer {
	private String address;
	// fqueu 文件夹路径
	private static String filePath;
	// 默认文件大小是100M
	private int filesize;

	private CommandCfgService commandCfgService;

	private Map<String, FQueue> fqueueMap = new HashMap<String, FQueue>();

	private static final Logger logger = LoggerFactory.getLogger(FqueueServer.class);

	public void init() {
		List<String> inputArguments = ManagementFactory.getRuntimeMXBean().getInputArguments();
		for(String inputArgument:inputArguments){
			if(inputArgument.contains("fqueue-filePath")){
				String[] array = inputArgument.split("=");
				filePath = array[1];
			}
		}
		List<CommandCfgDto> retList = commandCfgService.qryComandCfgs();
		for (int i = 0; i < retList.size(); i++) {
			CommandCfgDto dto = retList.get(i);
			for (int j = 0; j < dto.getModeCount(); j++) {
				String queueName = dto.getQueueName(j);
				String queueFilePath = (new StringBuilder(
						String.valueOf(filePath))).append(File.separator)
						.append(queueName).toString();
				FQueue fQueue = null;
				try {
					fQueue = new FQueue(queueFilePath, filesize);
					fqueueMap.put(queueName, fQueue);
				} catch (Exception e) {
					logger.error(
							"---创建队列" + queueName + "的fqueue实例失败："
									+ e.getMessage(), e);
				}
			}
		}
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public static String getFilePath() {
		return filePath;
	}

	public static void setFilePath(String filePath) {
		FqueueServer.filePath = filePath;
	}

	public int getFilesize() {
		return filesize;
	}

	public void setFilesize(int filesize) {
		this.filesize = filesize;
	}

	public CommandCfgService getCommandCfgService() {
		return commandCfgService;
	}

	public void setCommandCfgService(CommandCfgService commandCfgService) {
		this.commandCfgService = commandCfgService;
	}

	public FQueue getFqueue(String queueName) {
		FQueue fqueue = fqueueMap.get(queueName);
		if (fqueue == null) {
			String queueFilePath = (new StringBuilder(String.valueOf(filePath)))
					.append(File.separator).append(queueName).toString();
			FQueue fQueue = null;
			try {
				fQueue = new FQueue(queueFilePath, filesize);
				fqueueMap.put(queueName, fQueue);
				fqueue = fQueue;
			} catch (Exception e) {
				logger.error(
						"---创建队列" + queueName + "的fqueue实例失败：" + e.getMessage(),
						e);
			}
		}
		return fqueue;
	}
}
