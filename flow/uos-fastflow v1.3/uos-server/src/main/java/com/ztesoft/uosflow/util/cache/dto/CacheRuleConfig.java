/**
 * @author zhang.qiaoxian
 * @date 2015Äê4ÔÂ24ÈÕ
 * @project 0_JSKT_Base
 *
 */
package com.ztesoft.uosflow.util.cache.dto;

import com.ztesoft.uosflow.util.cache.CacheInterface;


public class CacheRuleConfig {
	private int digit;
	private int from;
	private int to;
	private CacheInterface cache;

	public int getDigit() {
		return digit;
	}

	public void setDigit(int digit) {
		this.digit = digit;
	}

	public int getFrom() {
		return from;
	}

	public void setFrom(int from) {
		this.from = from;
	}

	public int getTo() {
		return to;
	}

	public void setTo(int to) {
		this.to = to;
	}

	public CacheInterface getCache() {
		return cache;
	}

	public void setCache(CacheInterface cache) {
		this.cache = cache;
	}

}
