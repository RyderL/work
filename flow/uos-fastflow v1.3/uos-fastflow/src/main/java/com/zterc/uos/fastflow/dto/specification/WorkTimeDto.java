package com.zterc.uos.fastflow.dto.specification;

import java.io.Serializable;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * ����ʱ��
 * @author Administrator
 *
 */
public class WorkTimeDto implements Serializable {

	private static final long serialVersionUID = 1L;
    //����״̬
    public static final String WORKTIME_STATE_NORMAL = "10A";
    //ɾ��״̬
    public static final String WORKTIME_STATE_DELETED = "10X";
	
	private Long id;//id��ʶ	
	private String workTimeName;//����ʱ������
	private String workTimeRule;//����ʱ�����
	private String effDate;//��Ч����
	private String expDate;//ʧЧ����
	private String state;//״̬
	private Date stateDate;//״̬ʱ��
	private String comments;//��ע
	private Date createDate;//����ʱ��
	private Integer routeId;//·�ɱ�ʶ
	private Long areaId;
	
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getWorkTimeName() {
		return workTimeName;
	}
	public void setWorkTimeName(String workTimeName) {
		this.workTimeName = workTimeName;
	}
	public String getWorkTimeRule() {
		return workTimeRule;
	}
	public void setWorkTimeRule(String workTimeRule) {
		this.workTimeRule = workTimeRule;
	}
	public String getEffDate() {
		return effDate;
	}
	public void setEffDate(String effDate) {
		this.effDate = effDate;
	}
	public String getExpDate() {
		return expDate;
	}
	public void setExpDate(String expDate) {
		this.expDate = expDate;
	}
	public String getState() {
		return state;
	}
	public void setState(String state) {
		this.state = state;
	}
	public Date getStateDate() {
		return stateDate;
	}
	public void setStateDate(Date stateDate) {
		this.stateDate = stateDate;
	}
	public String getComments() {
		return comments;
	}
	public void setComments(String comments) {
		this.comments = comments;
	}
	public Date getCreateDate() {
		return createDate;
	}
	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
	}
	public Integer getRouteId() {
		return routeId;
	}
	public void setRouteId(Integer routeId) {
		this.routeId = routeId;
	}
	public Long getAreaId() {
		return areaId;
	}
	public void setAreaId(Long areaId) {
		this.areaId = areaId;
	}

	public static Map<String, String> getMapper() {
		Map<String, String> mapper = new HashMap<String, String>();
		mapper.put("id", "ID");
		mapper.put("workTimeName", "WORK_TIME_NAME");
		mapper.put("workTimeRule", "WORK_TIME_RULE");
		mapper.put("createDate", "CREATE_DATE");
		mapper.put("stateDate", "STATE_DATE");
		mapper.put("state", "STATE");
		mapper.put("comments", "COMMENTS");
		mapper.put("areaId", "AREA_ID");
		mapper.put("effDate", "EFF_DATE");
		mapper.put("expDate", "EXP_DATE");
		return mapper;
	}
}
