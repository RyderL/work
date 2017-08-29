package com.zterc.uos.fastflow.dto.specification;

import java.io.Serializable;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * 节假日制度
 * @author Administrator
 *
 */
public class HolidaySystemDto implements Serializable {
	private static final long serialVersionUID = 1L;
    //正常状态
    public static final String HOLIDAY_SYSTEM_STATE_NORMAL = "10A";
    //删除状态
    public static final String HOLIDAY_SYSTEM_STATE_DELETED = "10X";
    //包含节假日
    public static final String OPER_TYPE_INCLUDE_HOLIDAY = "1";
    //排除工作日
    public static final String OPER_TYPE_EXCLUDE_HOLIDAY = "0";
    
    //节假日制度Id
    private Long id;
    //节假日制度名称
    private String holidaySystemName;
    //休假开始时间
    private Date beginDate;
    //休假结束时间
    private Date endDate;
    //操作类型
    private String operType;
    //状态
    private String state;
    //状态时间
    private Date stateDate;
    //创建日期
    private Date createDate;
    //备注
    private String comments;
    //路由id
    private Integer routeId;
    private Long areaId;
    
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getHolidaySystemName() {
		return holidaySystemName;
	}
	public void setHolidaySystemName(String holidaySystemName) {
		this.holidaySystemName = holidaySystemName;
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
	public String getOperType() {
		return operType;
	}
	public void setOperType(String operType) {
		this.operType = operType;
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
		mapper.put("holidaySystemName", "HOLIDAY_SYSTEM_NAME");
		mapper.put("beginDate", "BEGIN_DATE");
		mapper.put("endDate", "END_DATE");
		mapper.put("createDate", "CREATE_DATE");
		mapper.put("stateDate", "STATE_DATE");
		mapper.put("state", "STATE");
		mapper.put("operType", "OPER_TYPE");
		mapper.put("comments", "COMMENTS");
		mapper.put("areaId", "AREA_ID");
		return mapper;
	}
}
