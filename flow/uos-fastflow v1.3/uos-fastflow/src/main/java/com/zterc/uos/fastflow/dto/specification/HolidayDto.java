package com.zterc.uos.fastflow.dto.specification;

import java.io.Serializable;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * �ڼ���ģ��
 * @author Administrator
 *
 */
public class HolidayDto implements Serializable {

	private static final long serialVersionUID = 1L;
    //����״̬
    public static final String HOLIDAY_STATE_NORMAL = "10A";
    //ɾ��״̬
    public static final String HOLIDAY_STATE_DELETED = "10X";

    //�¡���
    public static final String HOLIDAY_TIMEUNIT_MONTH = "0";
    //��
    public static final String HOLIDAY_TIMEUNIT_WEEK = "1";

	 //id
    private Long id;
    //ģ������
    private String holidayName;
    //�ݼ�ʱ�䵥λ
    private String timeUnit;
    //����
    private String holidayRule;
    //��ʼ��Ч����
    private String effDate;
    //ʧЧ����
    private String expDate;
    //״̬
    private String state;
    //״̬ʱ��
    private Date stateDate;
    //����ʱ��
    private Date createDate;
    //˵��
    private String comments;
    private Long areaId;
    
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getHolidayName() {
		return holidayName;
	}
	public void setHolidayName(String holidayName) {
		this.holidayName = holidayName;
	}
	public String getTimeUnit() {
		return timeUnit;
	}
	public void setTimeUnit(String timeUnit) {
		this.timeUnit = timeUnit;
	}
	public String getHolidayRule() {
		return holidayRule;
	}
	public void setHolidayRule(String holidayRule) {
		this.holidayRule = holidayRule;
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
	public Date getCreateDate() {
		return createDate;
	}
	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
	}
	public String getComments() {
		return comments;
	}
	public void setComments(String comments) {
		this.comments = comments;
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
		mapper.put("holidayName", "HOLIDAY_NAME");
		mapper.put("timeUnit", "TIME_UNIT");
		mapper.put("createDate", "CREATE_DATE");
		mapper.put("stateDate", "STATE_DATE");
		mapper.put("state", "STATE");
		mapper.put("holidayRule", "HOLIDAY_RULE");
		mapper.put("effDate", "EFF_DATE");
		mapper.put("expDate", "EXP_DATE");
		mapper.put("comments", "COMMENTS");
		mapper.put("areaId", "AREA_ID");
		return mapper;
	}
}
