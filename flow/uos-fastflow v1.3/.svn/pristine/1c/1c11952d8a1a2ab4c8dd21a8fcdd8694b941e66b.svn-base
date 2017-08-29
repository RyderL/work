package com.zterc.uos.base.jdbc;

import org.apache.commons.lang.StringUtils;

public class QueryFilter {
	public static final String ASC = "asc";
	public static final String DESC = "desc";

	// 排序字段
	private String orderBy;
	// 排序类型ASC/DESC
	private String order;

	public String getOrderBy() {
		return orderBy;
	}

	public void setOrderBy(String orderBy) {
		this.orderBy = orderBy;
	}

	public String getOrder() {
		return order;
	}

	/**
	 * 设置排序类型.
	 * 
	 * @param order
	 *            可选值为desc或asc,多个排序字段时用','分隔.
	 */
	public void setOrder(String order) {
		String lowcaseOrder = StringUtils.lowerCase(order);
		// 检查order字符串的合法值
		String[] orders = StringUtils.split(lowcaseOrder, ',');
		for (String orderStr : orders) {
			if (!StringUtils.equals(DESC, orderStr)
					&& !StringUtils.equals(ASC, orderStr)) {
				throw new IllegalArgumentException("排序类型[" + orderStr
						+ "]不是合法值");
			}
		}
		this.order = lowcaseOrder;
	}
}
