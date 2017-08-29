package com.zterc.uos.fastflow.dto.specification;

import java.io.Serializable;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * 工作时间段
 * @author Administrator
 *
 */
public class WorkTimeDetailDto implements Serializable {
	private static final long serialVersionUID = 1L;

    //时间段id
    private Long id;
    //工作时间Id
    private Long workTimeId;
    //工作时间name
    private String workTimeName;
    //开始时间
    private Date beginDate;
    //结束时间
    private Date endDate;
    //生效日期
    private Date effDate;
    //失效日期
    private Date expDate;
    
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public Long getWorkTimeId() {
		return workTimeId;
	}
	public void setWorkTimeId(Long workTimeId) {
		this.workTimeId = workTimeId;
	}
	public String getWorkTimeName() {
		return workTimeName;
	}
	public void setWorkTimeName(String workTimeName) {
		this.workTimeName = workTimeName;
	}
	public Date getBeginDate() {
		return beginDate;
	}
	public void setBeginDate(Date beginDate) {
		this.beginDate = beginDate;
	}
	public Date getEndDate() {
		return endDate;
	}
	public void setEndDate(Date endDate) {
		this.endDate = endDate;
	}
	public Date getEffDate() {
		return effDate;
	}
	public void setEffDate(Date effDate) {
		this.effDate = effDate;
	}
	public Date getExpDate() {
		return expDate;
	}
	public void setExpDate(Date expDate) {
		this.expDate = expDate;
	}

	public static Map<String, String> getMapper() {
		Map<String, String> mapper = new HashMap<String, String>();
		mapper.put("id", "ID");
		mapper.put("workTimeName", "WORK_TIME_NAME");
		mapper.put("workTimeId", "WORK_TIME_ID");
		mapper.put("beginDate", "BEGIN_DATE");
		mapper.put("endDate", "END_DATE");
		mapper.put("effDate", "EFF_DATE");
		mapper.put("expDate", "EXP_DATE");
		return mapper;
	}
}
