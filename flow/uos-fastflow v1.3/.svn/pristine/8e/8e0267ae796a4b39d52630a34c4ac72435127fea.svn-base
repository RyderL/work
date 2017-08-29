package com.ztesoft.uosflow.jmx.server;

import java.lang.management.ManagementFactory;

import javax.management.InstanceAlreadyExistsException;
import javax.management.MBeanRegistrationException;
import javax.management.MBeanServer;
import javax.management.MalformedObjectNameException;
import javax.management.NotCompliantMBeanException;
import javax.management.ObjectName;

import org.apache.log4j.Logger;
import com.sun.jdmk.comm.HtmlAdaptorServer;
import com.ztesoft.uosflow.jmx.server.bl.fqueuemxbean.FQueueManager;
import com.ztesoft.uosflow.jmx.server.bl.fqueuemxbean.FQueueManagerMXBean;
import com.ztesoft.uosflow.jmx.server.bl.server.ServerManager;
import com.ztesoft.uosflow.jmx.server.bl.server.ServerManagerMXBean;

public class JmxServer {

	private static Logger logger = Logger.getLogger(JmxServer.class);

	public void initJmxHtmlServer(int port)
			throws MalformedObjectNameException, NullPointerException,
			InstanceAlreadyExistsException, MBeanRegistrationException,
			NotCompliantMBeanException {
		logger.info("##############################################initJmxHtmlServer is starting...");

		MBeanServer mbs = ManagementFactory.getPlatformMBeanServer();

		// ×¢²áFQueueManager
		String fqueueMgrJmxName = "JMX.FQueueManager:type=FQueueManager";
		ObjectName fqueueMgrObjName = new ObjectName(fqueueMgrJmxName);
		FQueueManagerMXBean fqueueMgrMBean = new FQueueManager();
		mbs.registerMBean(fqueueMgrMBean, fqueueMgrObjName);
		
		// ×¢²áserverManager
		String serverManagerJmxName = "JMX.ServerManager:type=ServerManager";
		ObjectName serverManagerObjName = new ObjectName(serverManagerJmxName);
		ServerManagerMXBean serverManagerMBean = new ServerManager();
		mbs.registerMBean(serverManagerMBean, serverManagerObjName);
		
		// ×¢²áhtmlÐ­ÒéÊÊÅäÆ÷
		ObjectName htmlAdtObjName = new ObjectName(
				"JMX.HtmlAdaptor:name=htmladaptor,port=" + port);
		HtmlAdaptorServer htmlAdtMBean = new HtmlAdaptorServer();
		mbs.registerMBean(htmlAdtMBean, htmlAdtObjName);

		// log4j
//		HierarchyDynamicMBean hdm = new HierarchyDynamicMBean();
//		ObjectName logName = new ObjectName("log4j:hiearchy=default");
//		hdm.addLoggerMBean(Logger.getRootLogger().getName());
//		mbs.registerMBean(hdm, logName);
//		// Get each logger from the Log4J Repository and add it to the Hierarchy
//		// MBean created above.
//		LoggerRepository r = LogManager.getLoggerRepository();
//
//		@SuppressWarnings("unchecked")
//		java.util.Enumeration<Logger> loggers = r.getCurrentLoggers();
//
//		int count = 1;
//		while (loggers.hasMoreElements()) {
//			String name = (loggers.nextElement()).getName();
//			if (logger.isDebugEnabled()) {
//				logger.debug("[contextInitialized]: Registering " + name);
//			}
//			hdm.addLoggerMBean(name);
//			count++;
//		}
//		if (logger.isInfoEnabled()) {
//			logger.info("[contextInitialized]: " + count
//					+ " log4j MBeans registered.");
//		}

		htmlAdtMBean.setMBeanServer(mbs);
		htmlAdtMBean.setPort(port);
		htmlAdtMBean.start();

		logger.info("##############################################initJmxHtmlServer started successfully.");
	}

	// public static void main(String[] args) throws Exception{
	// new JmxServer().initJmxHtmlServer(9526);
	// // InetAddress netAddress = InetAddress.getLocalHost();
	// // String ip = netAddress.getHostAddress();
	// // System.out.println("±¾»úIP=" + ip);
	// }
}
