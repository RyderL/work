package com.zterc.uos.gid.service;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.ConcurrentMap;

import org.apache.log4j.Logger;

import com.zterc.uos.base.bean.BeanContextProxy;
import com.zterc.uos.base.sequence.SequenceService;
import com.zterc.uos.gid.dao.SequenceDAO;
import com.zterc.uos.gid.dao.SequenceDAOImpl;
import com.ztesoft.gidServer.inf.SequenceGenerator;

public class SequenceServiceGid implements SequenceService {

	private static final Logger logger = Logger
			.getLogger(SequenceServiceGid.class);
	private GidConfig gidConfig;
	private SequenceDAO sequenceDAO ;
	private String isUseNewGid;

	private ConcurrentMap<String, Queue<Long>> seqpool = new ConcurrentHashMap<String, Queue<Long>>();

	public void init() {
		sequenceDAO = new SequenceDAOImpl(gidConfig);
		
		Map<String, String> seqConfigMap = this.gidConfig.getSeqConfigMap();
		Set<String> seqs = seqConfigMap.keySet();
		Iterator<String> it = seqs.iterator();
		while (it.hasNext()) {
			String key = it.next();
			String value = seqConfigMap.get(key);
			String[] args = value.split(",");
			logger.info("--gid--" + key + ",value:" + value);
			// 序列名称
			String sequenceName = key;
			// 客户端缓存数量,缓存队列的最小个数,如果小于这个数值，则会重新从数据层中读取
			int minCacheSize = Integer.parseInt(args[0]);
			// 服务端缓存值
			int incrementSize = Integer.parseInt(args[1]);
			// 序列的最小值--用于zookeeper里没有配置此序列的时候
			long minValue = Long.valueOf(args[2]).longValue();

			Queue<Long> queue = new ConcurrentLinkedQueue<Long>();
			seqpool.put(sequenceName, queue);// 设置队列到池中

			logger.warn("初始化sequence=" + sequenceName + "中...");

			SequenceHandler sequencHandler = new SequenceHandler(sequenceName,
					minCacheSize, incrementSize, minValue, queue);
			Thread t = new Thread(sequencHandler, args[0] + "-Handler-");
			t.start();
		}
	}

	@Override
	public Long getGidByName(String name) {
		Long seq = null;
		int tryTimes = this.gidConfig.getTryTimes();
		for (int i = 0; i < tryTimes; i++) {
			String poolName = name;
			Queue<Long> queue = seqpool.get(poolName);
			if (queue == null) {
				// 没有配置的seq
				throw new RuntimeException("当前SEQ在应用配置文件中没配置，无法生成,SEQ NAME="
						+ name);
			}
			try {
				seq = queue.poll();
			} catch (Exception e1) {
			}
			if (seq != null) {
				return seq;
			} else {
				logger.warn("缓存sequence[" + name + "]消息队列中获取不到seq_id");
				try {
					Thread.sleep(1000);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					logger.warn("休眠失败!");
				}
			}
		}
		// tryTimes次还获取不到sequence
		throw new RuntimeException("从GID服务端获取数据失败" + tryTimes + "次仍无法获取" + name
				+ "，终止!");
	}

	/**
	 * 后台线程取数据
	 */
	class SequenceHandler implements Runnable {
		// 序列名称
		private String seqName;

		// 缓存队列的最小个数,如果小于这个数值，则会重新从数据库中读取
		private int minCacheSize;

		// 服务端步长
		private long incrementSize;

		// 序列的最小值--用于zookeeper里没有配置此序列的时候
		private long minValue;

		// 缓存队列
		private Queue<Long> seqQueue;

		private int tryTimes;

		public SequenceHandler(String seqName, int minCacheSize,
				int incrementSize, long minValue, Queue<Long> seqQueue) {
			this.seqName = seqName;
			this.minCacheSize = minCacheSize;
			this.minValue = minValue;
			this.seqQueue = seqQueue;
			this.incrementSize = incrementSize;
			this.tryTimes = gidConfig.getTryTimes();
		}

		@Override
		public void run() {
			// 循环为序列队列填充值。
			while (true) {
				int getSeqValueFaildTimes = 0;
				try {
					// 如果缓存队列中的数量小于最小缓存数，则会重新从数据层中获取
					while (seqQueue.size() < minCacheSize) {
						// 获取序列
						Long seqValue;
						// 重新获取
						if("Y".equals(isUseNewGid)){
							SequenceGenerator sequenceGenerator = (SequenceGenerator) BeanContextProxy.getBean("sequenceGenerator");
							seqValue = sequenceGenerator.getSequence(seqName, gidConfig.getSysCode());
						}else{
							seqValue = sequenceDAO.getSequence(seqName,incrementSize, minValue);
						}
						
						if (seqValue != null && -1 != seqValue) {
							List<Long> temp = new ArrayList<Long>();
							for (int i = 0; i < incrementSize; i++) {
								Long s = seqValue++;
								if (!seqQueue.contains(s)) {
									temp.add(s);
								}
							}
							seqQueue.addAll(temp);
						} else {
							if (getSeqValueFaildTimes++ > tryTimes) {
								throw new RuntimeException("获取序列值异常，得到的值为:"+ seqValue);
							}
							;
							Thread.sleep(30L);
						}
					}
					// 补充gid到队列的间隔时间
					Thread.sleep(100L);
				} catch (Exception ex) {
					try {
						// 如果失败次数超出一定次数，那么休眠时间变成30秒，否则是1秒
						if (getSeqValueFaildTimes > tryTimes) {
							Thread.sleep(1000 * 30L);
						} else {
							Thread.sleep(1000);
						}
					} catch (InterruptedException e) {
						logger.warn(ex.getMessage());
					}
					logger.warn(ex.getMessage());
				}
			}
		}
	}

	public GidConfig getGidConfig() {
		return gidConfig;
	}

	public void setGidConfig(GidConfig gidConfig) {
		this.gidConfig = gidConfig;
	}

	public SequenceDAO getSequenceDAO() {
		return sequenceDAO;
	}

	public void setSequenceDAO(SequenceDAO sequenceDAO) {
		this.sequenceDAO = sequenceDAO;
	}

	public String getIsUseNewGid() {
		return isUseNewGid;
	}

	public void setIsUseNewGid(String isUseNewGid) {
		this.isUseNewGid = isUseNewGid;
	}
}
