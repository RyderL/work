package com.zterc.uos.fastflow.core;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import com.zterc.uos.fastflow.dto.specification.HolidayDto;
import com.zterc.uos.fastflow.dto.specification.HolidaySystemDto;
import com.zterc.uos.fastflow.dto.specification.WorkTimeDto;
import com.zterc.uos.fastflow.parse.CalendarUtil;
import com.zterc.uos.fastflow.service.HolidayService;
import com.zterc.uos.fastflow.service.WorkTimeService;

public class HolidayRuleClient {
	public static String NO_WORKING_TIME = "00:00-00:00";
	public static String FULL_WORKING_TIME = "00:00-24:00";
	public static String NOT_SURE = "not sure";

	public static String DATE_STATE_NORMAL = "normal";
	public static String DATE_STATE_HOLIDAY = "holiday";
	public static String DATE_STATE_HALF = "half";
	private HolidayService holidayService;
	private WorkTimeService workTimeService;

	public void setWorkTimeService(WorkTimeService workTimeService) {
		this.workTimeService = workTimeService;
	}
	public void setHolidayService(HolidayService holidayService) {
		this.holidayService = holidayService;
	}
 	Collection<HolidaySystemDto> holidaySysDtoes;
    Collection<HolidayDto> holiday_dtoes;
    private WorkTimeDto[]  activeWorkTimes;
    
    /**
     * 查询工作时间串
     * @param areaId  区域Id
     * @param date Date 日期
     * @throws UOSException
     * @throws ParseException
     * @return String 时间串
     */
	@SuppressWarnings("rawtypes")
	public String getWorkTime(String areaId, String processDefineId, Date date) throws Exception {
		 DateFormat df = new SimpleDateFormat("yyy-MM-dd");
	        String dateStr = CalendarUtil.getInstance().getDate(date);
	        String validdateStr = dateStr + " 00:00:01";
	        Date validdate = df.parse(validdateStr);
	        Collection[] cols = getProperHolidays(areaId, processDefineId, validdate);
	        if (cols != null) {
	            String workTime = getWorkTimeFromHoliday(date, cols[0], cols[1]);
	            if (workTime.equals(FULL_WORKING_TIME)) {
	                return getWorkTimeFromRule(areaId, validdate);
	            }
	            else {
	                return workTime;
	            }
	        }
	        else {
	            //mod by chen.zhikun 2008-08-13 UR-29680 对找不到节假日模板的组织，不直接认为它全天工作，而是再查询它的工作时间
	        	return getWorkTimeFromRule(areaId, validdate);
	        	//return FULL_WORKING_TIME;
	        }
	}
	 /**
     * 查询某区域的工作时间（如果查不到认为该区域全天工作）
     * @param areaId 区域Id
     * @param inputDate 输入的当前时间
     * @throws UOSException
     * @throws ParseException
     * @return String 工作时间串
	 * @throws Exception 
     */
	@SuppressWarnings("rawtypes")
	private String getWorkTimeFromRule(String areaId, Date inputDate) throws Exception {
		if (areaId == null) {
			throw new Exception("areaId is null");
		}

		WorkTimeDto[] activeWorkTimeDtoes;
		if (activeWorkTimes == null) {
			activeWorkTimeDtoes = workTimeService
					.findActiveWorkTimes(inputDate);
			activeWorkTimes = activeWorkTimeDtoes;
		} else{
			activeWorkTimeDtoes = activeWorkTimes;
		}
			
		Collection workTimes = findWorkTimeByAreaId(areaId, activeWorkTimeDtoes);
		if (workTimes.size() > 1) {
			throw new Exception("workTimeRule is more than one");
		} else if (workTimes.size() == 1) {
			Iterator iterator = workTimes.iterator();
			WorkTimeDto workTimeDto = (WorkTimeDto) iterator.next();
			return workTimeDto.getWorkTimeRule();
		} else {
			return FULL_WORKING_TIME;
		}

	}
	/**
     * 根据区域id,查询区域对应的工作时间
     * @param areaId 区域Id
     * @param workTimeDtoes 所有有效工作时间
     * @return Collection 该区域对应的工作时间
     */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	private Collection findWorkTimeByAreaId(String areaId,
			WorkTimeDto[] workTimeDtoes) {
		Collection col = new ArrayList();
		for (int i = 0; i < workTimeDtoes.length; i++) {
			if (workTimeDtoes[i].getAreaId() != null
					&& workTimeDtoes[i].getAreaId().toString().equals(areaId)) {
				col.add(workTimeDtoes[i]);
			}
		}
		return col;
	}
	/**
     * 判断是否为节假日
     * @param date 输入日期
     * @param holidays 节假日模板集合
     * @param holidaySystems 节假日休息制度集合
     *  0— 不完全放假（可能是半天假）
     *  1－休假 NO_WORKING_TIME
     *  2－工作 FULL_WORKING_TIME
	 * @throws Exception 

     */
	@SuppressWarnings("rawtypes")
	private String getWorkTimeFromHoliday(Date date, Collection holidays,
			Collection holidaySystems) throws Exception {
		String ret = judgeByDay(date, holidaySystems);
		if (ret.equals(FULL_WORKING_TIME)) {
			return FULL_WORKING_TIME;
		} else if (ret.equals(NO_WORKING_TIME)) {
			String res = judgeByDayRule(date, holidays);
			if ((!res.equals(FULL_WORKING_TIME))
					&& (!res.equals(NO_WORKING_TIME))) {
				return res; // 放半天
			} else {
				return NO_WORKING_TIME;
			}
		} else {
			String res2 = judgeByDayRule(date, holidays);
			if (res2.equals(FULL_WORKING_TIME)) {
				return judgeByWeekRule(date, holidays);
			} else {
				return res2;
			}

		}
	}

	/**
	 * 根据输入日期在节假日模板（星期）中判断是否放假（6-7)
	 * 
	 * @param date
	 *            输入日期
	 * @param holidays
	 *            节假日模板集合
	 * @return boolean true 休假 NO_WORKING_TIME false 工作 FULL_WORKING_TIME
	 * @throws Exception 
	 */
	@SuppressWarnings("rawtypes")
	private String judgeByWeekRule(Date date, Collection holidays) throws Exception {
		if (holidays == null) {
			return FULL_WORKING_TIME;
		}
		int curWeek = CalendarUtil.getInstance().getCurWeekDay(date);
		for (Iterator iterator = holidays.iterator(); iterator.hasNext();) {
			HolidayDto holidayDto = (HolidayDto) iterator.next();
			if (holidayDto.getTimeUnit().equals(
					HolidayDto.HOLIDAY_TIMEUNIT_WEEK)) {
				String holidayRule = holidayDto.getHolidayRule();
				if (holidayRule == null) {
					throw new Exception("The holidayRule is null");
				}
				int startWeek = Integer.parseInt(holidayRule.substring(0, 1));
				int endWeek = Integer.parseInt(holidayRule.substring(2, 3));
				if (startWeek < endWeek) {
					if (startWeek <= curWeek && curWeek <= endWeek) {
						return NO_WORKING_TIME;
					}
				}
				// mod by 陈智堃 2009-12-16 修正这里一个计算的BUG
				else if (startWeek == endWeek) {
					if (startWeek == curWeek)
						return NO_WORKING_TIME;
				} else {
					if (startWeek <= curWeek || curWeek <= endWeek) {
						return NO_WORKING_TIME;
					}
				}
			}
		}
		return FULL_WORKING_TIME;
	}
	/**
	 * 根据输入日期在节假日模板（月、日）中判断是否放假（10.01-10.07)
	 * 
	 * @param date
	 *            输入日期
	 * @param holidays
	 *            节假日模板集合
	 * @throws ParseException
	 * @return int 0— 不完全放假（可能是半天假） 1－休假 NO_WORKING_TIME 2－工作 FULL_WORKING_TIME
	 * @throws Exception 
	 */
	@SuppressWarnings("rawtypes")
	private String judgeByDayRule(Date date, Collection holidays) throws Exception {
		if (holidays == null) {
			return FULL_WORKING_TIME;
		}

		String input = CalendarUtil.getInstance().getDate(date);
		int currYear = Integer.parseInt(input.substring(0, 4));
		int startYear = 0;
		int endYear = 0;
		boolean isIncludeWorkTime = false;
		for (Iterator iterator = holidays.iterator(); iterator.hasNext();) {
			HolidayDto holidayDto = (HolidayDto) iterator.next();
			if (holidayDto.getTimeUnit().equals(
					HolidayDto.HOLIDAY_TIMEUNIT_MONTH)) {
				String holidayRule = holidayDto.getHolidayRule();
				if (holidayRule == null) {
					throw new Exception("The holidayRule is null");
				}

				int startMonth = Integer.parseInt(holidayRule.substring(0, 2));
				int startDay = Integer.parseInt(holidayRule.substring(3, 5));
				int endMonth = Integer.parseInt(holidayRule.substring(6, 8));
				int endDay = Integer.parseInt(holidayRule.substring(9, 11));

				int length = holidayRule.trim().length();
				if (length > 12) {
					isIncludeWorkTime = true;
				} else {
					isIncludeWorkTime = false;
				}

				if (endMonth < startMonth) {
					endYear = currYear + 1;
				} else {
					endYear = currYear;

				}
				startYear = currYear;
				String startDateStr = CalendarUtil.getInstance().getDateTime(
						startYear, startMonth, startDay);
				String endDateStr = CalendarUtil.getInstance().getDateTime(
						endYear, endMonth, endDay);
				DateFormat df = new SimpleDateFormat("yyy-MM-dd");
				Date startDate = df.parse(startDateStr);
				Date endDate = df.parse(endDateStr);
				Date inputDate = df.parse(input);
				if (inputDate.equals(startDate)) {
					if (isIncludeWorkTime) {
						return holidayRule.substring(12, length);
					} else {

						return NO_WORKING_TIME;
					}
				} else if (inputDate.equals(endDate)) {
					if (isIncludeWorkTime) {
						return holidayRule.substring(12, length);
					} else {

						return NO_WORKING_TIME;
					}

				} else if (inputDate.after(startDate)
						&& inputDate.before(endDate)) {
					if (isIncludeWorkTime) {
						return holidayRule.substring(12, length);
					} else {

						return NO_WORKING_TIME;
					}

				}
			}

		}
		return FULL_WORKING_TIME;
	}
	/**
     * 根据输入日期在节假日休息制度中判断是否休假（制定具体开始、结束日期）
     * @param date 输入日期
     * @param holidaySystems 休假规则（非模板）
     * @throws ParseException
     * @return String
     *  0——不确定 NOT_SURE
     *  1－休假 NO_WORKING_TIME
     *  2－工作 FULL_WORKING_TIME
	 * @throws Exception 
     */
	@SuppressWarnings("rawtypes")
	private String judgeByDay(Date date, Collection holidaySystems) throws Exception {
		if (holidaySystems == null || date == null) {
			return NOT_SURE;
		}
		String input = CalendarUtil.getInstance().getDate(date);
		String ret = NOT_SURE;
		for (Iterator iterator = holidaySystems.iterator(); iterator.hasNext();) {
			HolidaySystemDto holidaySystemDto = (HolidaySystemDto) iterator
					.next();
			if (holidaySystemDto.getOperType() == null) {
				throw new Exception("The operType of holidaySystem is null");
			}
			if (holidaySystemDto.getBeginDate() == null) {
				throw new Exception("The beginDate of holidaySystem is null");
			}
			if (holidaySystemDto.getEndDate() == null) {
				throw new Exception("The endDate of holidaySystem is null");
			}

			if (holidaySystemDto.getOperType().equals(
					HolidaySystemDto.OPER_TYPE_INCLUDE_HOLIDAY)) {
				ret = NO_WORKING_TIME;
			} else if (holidaySystemDto.getOperType().equals(
					HolidaySystemDto.OPER_TYPE_EXCLUDE_HOLIDAY)) {
				ret = FULL_WORKING_TIME;
			}
			String startDateStr = CalendarUtil.getInstance().getDate(
					holidaySystemDto.getBeginDate());
			String endDateStr = CalendarUtil.getInstance().getDate(
					holidaySystemDto.getEndDate());
			DateFormat df = new SimpleDateFormat("yyy-MM-dd");
			Date startDate = df.parse(startDateStr);
			Date endDate = df.parse(endDateStr);
			Date inputDate = df.parse(input);

			if (inputDate.equals(startDate)) {
				return ret;
			} else if (inputDate.equals(endDate)) {
				return ret;
			} else if (inputDate.after(startDate) && inputDate.before(endDate)) {
				return ret;
			}
		}
		ret = NOT_SURE;
		return ret;
	}
	/**
	 * 根据区域获取对应的节假日信息
	 * @param areaId
	 * @param processDefineId
	 * @param validdate
	 * @return
	 * @throws Exception
	 */
	@SuppressWarnings("rawtypes")
	private Collection[] getProperHolidays(String areaId,
			String processDefineId, Date validdate) throws Exception {
		 if (areaId == null) {
	            throw new Exception("areaId is null");
	        }
		 	Collection<HolidaySystemDto> holidaySystemDtoes;
	        Collection<HolidayDto> holidayDtoes;
	        if(holidaySysDtoes == null){
	        
	        	holidaySystemDtoes =  holidayService.qryHolidaySystemsByArea(areaId);
	            holidaySysDtoes=holidaySystemDtoes;
	        }else{
	        	holidaySystemDtoes = holidaySysDtoes;
	        }
	        if(holiday_dtoes == null){
	        	holidayDtoes = holidayService.findHolidays(areaId,validdate);
	            holiday_dtoes =holidayDtoes;
	        } else{
	        	holidayDtoes = holiday_dtoes;
	        }

	        if(holidaySystemDtoes.size() == 0 && holidayDtoes.size() == 0){
	        	return null;
	        }
	        	
	        Collection[] cols = new Collection[2];
            cols[0] = holidayDtoes;
            cols[1] = holidaySystemDtoes;
	        return cols;
	}
	   /**
     * 查询工作时间类型
     * @param areaId  区域Id
     * @param date Date 日期
     * @throws UOSException
     * @throws ParseException
     * @return String
     *  NORMAL 正常上班
     *  HOLIDAY 休息
     *  HALF 可能半天休息
	 * @throws Exception 
     */
	@SuppressWarnings("rawtypes")
	public String getWorkTimeType(String areaId, String processDefineId,
			Date date) throws Exception {
		DateFormat df = new SimpleDateFormat("yyy-MM-dd");
		String dateStr = CalendarUtil.getInstance().getDate(date);
		String validdateStr = dateStr + " 00:00:01";
		Date validdate = df.parse(validdateStr);

		Collection[] cols = getProperHolidays(areaId, processDefineId,
				validdate);
		if (cols != null) {
			String workTime = getWorkTimeFromHoliday(date, cols[0], cols[1]);
			if (workTime.equals(FULL_WORKING_TIME)) {
				return DATE_STATE_NORMAL;
			} else if (workTime.equals(NO_WORKING_TIME)) {
				return DATE_STATE_HOLIDAY;
			} else {
				return DATE_STATE_HALF;
			}
		} else {
			return DATE_STATE_NORMAL;
		}

	}
	
	/**
	 * 得到休息日数组
	 * @param areaId
	 * @param beginDate
	 * @param endDate
	 * @return
	 * @throws Exception 
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public Date[] getHoldiaysNew(String areaId, Date beginDate, Date endDate) throws Exception {
		int startMonth = 0;
		if (areaId == null) {
			throw new Exception("areaId is null");
		}
		List holidays = getAllHolidays(areaId);
		if (beginDate == null) {
			return null;
		}
		if (endDate == null) {
			endDate = new Date();
		}
		if (endDate.before(beginDate)) {
			Date temp = (Date) endDate.clone();
			endDate = beginDate;
			beginDate = temp;
		}
		Calendar beginCal = Calendar.getInstance();
		beginCal.setTime(beginDate);
		Calendar endCal = Calendar.getInstance();
		endCal.setTime(endDate);
		Collection dateArr = new ArrayList();
		do {
			Date[] dates = getHoldiaysByTime(beginCal.get(Calendar.YEAR),
					beginCal.get(Calendar.MONTH) + 1, holidays,areaId);
			if (dates != null && dates.length > 0) {
				for (int i = 0; i < dates.length; i++) {
					if (dates[i].after(beginDate) && dates[i].before(endDate)) {
						dateArr.add(dates[i]);
					}
				}
			}
			if (beginCal.get(Calendar.YEAR) == endCal.get(Calendar.YEAR)
					&& beginCal.get(Calendar.MONTH) == 11) {
				startMonth = 11;
			} else {
				beginCal.add(Calendar.MONTH, 1);
				startMonth = beginCal.get(Calendar.MONTH) - 1;
			}
		} while (beginCal.get(Calendar.YEAR) != endCal.get(Calendar.YEAR)
				|| startMonth != endCal.get(Calendar.MONTH));
		return (Date[]) dateArr.toArray(new Date[dateArr.size()]);
	}
	/**
	 * 得到休息日数组
	 * @param year
	 * @param month
	 * @param holidayList
	 * @return
	 * @throws Exception 
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	private Date[] getHoldiaysByTime(int year, int month, List holidayList,String areaId) throws Exception {
		Collection dateArr = new ArrayList();
		DateFormat df = new SimpleDateFormat("yyy-MM-dd");
		int realDay = CalendarUtil.getInstance().getRealDays(year, month);
		for (int day = 1; day <= realDay; day++) {
			String dateStr = CalendarUtil.getInstance().getDateTime(year,
					month, day);
			// updated by caozz for 根据输入时间来判断规则是否有效。须加上时间段，生效时间为 00：00：00
			// 失效时间为。。23:59:59 此处时间与后面的时间不一样，所以分开新建一个validdateStr
			String validdateStr = dateStr + " 00:00:01";
			Date date = df.parse(dateStr);
			Date validdate = df.parse(validdateStr);
			Collection[] cols = getProperHolidaysEx(areaId,
					holidayList, validdate);
			// System.out.println("++++++++++++++++++=colsLength: "+cols.length);
			if (cols != null) {
				if (getWorkTimeFromHoliday(date, cols[0], cols[1]).equals(
						NO_WORKING_TIME)) {
					dateArr.add(date);
				}
				if (!(getWorkTimeFromHoliday(date, cols[0], cols[1])
						.equals(NO_WORKING_TIME))
						&& !(getWorkTimeFromHoliday(date, cols[0], cols[1])
								.equals(FULL_WORKING_TIME))) {
					dateArr.add(date);
				}
			}

		}

		return (Date[]) dateArr.toArray(new Date[dateArr.size()]);
	}
	/**
	 * 根据组织,职位查找节假日和节假日制度
	 * @param areaId
	 * @param holidayList
	 * @param validdate
	 * @return
	 * @throws ParseException 
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	private Collection[] getProperHolidaysEx(String areaId, List holidayList,
			Date validdate) throws ParseException {
		Collection[] cols = null;
    	HolidaySystemDto[] holidaySystemDtoes = (HolidaySystemDto[])holidayList.get(0);
    	HolidayDto[] allHolidayDtoes = (HolidayDto[])holidayList.get(1);
    	List holiday = new ArrayList();
    	//从所有的节假日中找出当前时间有效的节假日模板
    	for(int i=0;i<allHolidayDtoes.length;i++){
    		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
    		if (format.parse(allHolidayDtoes[i].getEffDate()).getTime() < validdate.getTime()
    				&& format.parse(allHolidayDtoes[i].getExpDate()).getTime() > validdate.getTime()){
    			holiday.add(allHolidayDtoes[i]);
    		}
    	}
    	HolidayDto[] holidayDtoes = (HolidayDto[]) holiday.toArray(new HolidayDto[holiday.size()]);
         //找到该区域的节假日规则
         if(areaId != null){
         	cols = new Collection[2];
             cols[0] = holidayService.findHolidayByArea(areaId, holidayDtoes);
             cols[1] = holidayService.findHolidaySystemByArea(areaId, holidaySystemDtoes);
         }
         return cols;
	}
	/**
	 * 根据areaid查询适合该区域的所有节假日和节假日制度
	 * @param areaId
	 * @return
	 * @throws Exception 
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	private List getAllHolidays(String areaId) throws Exception {
		if (areaId == null) {
            throw new Exception("areaId is null");
        }
    	List holidayList = new ArrayList();
        Collection<HolidaySystemDto> holidaySystemDtoes = holidayService.qryHolidaySystemsByArea(areaId);
        HolidayDto[] holidayDtoes = holidayService.qryAllHolidays();
        holidayList.add(holidaySystemDtoes.toArray(new HolidaySystemDto[holidaySystemDtoes.size()]));
        holidayList.add(holidayDtoes);
        return holidayList;
	}
	
}
