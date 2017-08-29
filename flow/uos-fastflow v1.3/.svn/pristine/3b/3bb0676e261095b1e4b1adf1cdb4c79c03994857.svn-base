package com.zterc.uos.fastflow.core;

import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.apache.log4j.Logger;

import com.zterc.uos.base.helper.StringHelper;
import com.zterc.uos.fastflow.constant.CommonDomain;
import com.zterc.uos.fastflow.dto.process.WorkItemDto;
import com.zterc.uos.fastflow.parse.CalendarUtil;

public class TimeLimitClient {
	private Logger logger = Logger.getLogger(TimeLimitClient.class);
	
    public static String BLANK_BAR = " ";
    public static String HOUR_BAR = "-";
    public static String MINUTE_BAR = ":";
    public static final String YEAR = "YEAR";
    public static final String DAY = "DAY";
    public static final String HOUR = "HOR";
    public static final String MINUTE = "MIN";
    
	private HolidayRuleClient holidayRuleClient;
	public void setHolidayRuleClient(HolidayRuleClient holidayRuleClient) {
		this.holidayRuleClient = holidayRuleClient;
	}
	
	public Date calculateWorkTime(Date startDate, int timeLimit,
			String timeUnit, String areaId, String processDefineId) throws Exception {
		if (areaId == null) {
			areaId = "-1";
        }
        if (timeLimit < 0) {
            timeLimit = 1;
        }
        if (timeUnit != null && timeUnit.indexOf(HOUR) >= 0 && timeLimit > 23) {
            timeUnit = MINUTE;
            timeLimit = timeLimit * 60;
        }
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String startDateStr = df.format(startDate);
        String endDateStr = null;
        try {
			if (timeUnit != null && timeUnit.indexOf(YEAR) >= 0) {
			    endDateStr = CalendarUtil.getInstance().getPreDateTime(startDateStr, CalendarUtil.YEAR, timeLimit);
			    return df.parse(endDateStr);
			}
			else if (timeUnit != null && timeUnit.indexOf(DAY) >= 0) {
			    return calculateWorkTimeByDay(timeLimit, startDate, areaId, processDefineId);
			}
			else if (timeUnit != null && timeUnit.indexOf(HOUR) >= 0) {
			    return calculateWorkTimeByMinute(timeLimit * 60, startDate, areaId, processDefineId);
			}
			else if (timeUnit != null && timeUnit.indexOf(MINUTE) >= 0) {
			    return calculateWorkTimeByMinute(timeLimit, startDate,areaId, processDefineId);
			}
			else {
			    return null;
			}
		} catch (ParseException e) {
			logger.error("---计算工作时间异常："+e.getMessage(),e);
			return null;
		}
	}
	
	private Date calculateWorkTimeByMinute(int minute, Date date,
			String areaId, String processDefineId) throws Exception {
		Date startDate = getRealStartDate(areaId, processDefineId, date);
		Date endDate = traceMinute(startDate, minute, areaId, processDefineId);
		return endDate;
	}
	private Date calculateWorkTimeByDay(int dayCount, Date date,
			String areaId, String processDefineId) throws Exception {
		DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

        Date realStartDate = getRealStartDate(areaId,processDefineId, date);
        String realStartDateStr = CalendarUtil.getInstance().getDateTime(realStartDate);
        String nextDayStr = null;
        Date nextDay = null;
        int extraMinute = 0;
        int timeLength = getNextTimeLengthFromHoliday(areaId,processDefineId, date);
        while (dayCount > 0) {

            nextDayStr = CalendarUtil.getInstance().getPreDateTime(realStartDateStr, CalendarUtil.DAY, 1);
            nextDay = df.parse(nextDayStr);
            String workTimeType = holidayRuleClient.getWorkTimeType(areaId,processDefineId, nextDay);
            if (workTimeType.equals(HolidayRuleClient.DATE_STATE_NORMAL)) {
                dayCount = dayCount - 1;

            }
            else if (workTimeType.equals(HolidayRuleClient.DATE_STATE_HALF)) {
                dayCount = dayCount - 1;
                int[] temp = getWorkTimeArr(areaId,processDefineId, nextDay);
                int halfTime = 0;
                for (int i = 0; i < temp.length; i++) {
                    if (i % 2 == 0) {
                        halfTime = halfTime + (temp[i + 1] - temp[i]);
                    }
                }
                if (halfTime < timeLength) {
                    extraMinute = timeLength - halfTime;
                }

            }
            realStartDateStr = nextDayStr;
        }

        if (extraMinute > 0) {
            Date endDate = traceMinute(nextDay, extraMinute, areaId,processDefineId);
            return getRealStartDate(areaId,processDefineId, endDate);
        }
        else {
            return getRealStartDate(areaId,processDefineId, nextDay);
        }
	}
	private int getNextTimeLengthFromHoliday(String areaId,
			String processDefineId, Date date) throws Exception {
		String dateStr = CalendarUtil.getInstance().getDateTime(date);
		DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		boolean flag = true;
		String nextDayStr = null;
		Date nextDay = null;
		while (flag) {
			nextDayStr = CalendarUtil.getInstance().getPreDateTime(dateStr,
					CalendarUtil.DAY, 1);
			nextDay = df.parse(nextDayStr);
			String workTimeType = holidayRuleClient.getWorkTimeType(areaId, processDefineId, nextDay);
			if (workTimeType.equals(HolidayRuleClient.DATE_STATE_HOLIDAY)
					|| workTimeType.equals(HolidayRuleClient.DATE_STATE_HALF)) {
				flag = true;
			} else {
				flag = false;
			}
			dateStr = nextDayStr;
		}
		int[] timeBlot = getWorkTimeArr(areaId, processDefineId, nextDay);
		int timelength = 0;
		for (int i = 0; i < timeBlot.length; i++) {
			if (i % 2 == 0) {
				timelength = timelength + (timeBlot[i + 1] - timeBlot[i]);
			}
		}
		return timelength;
	}
	private Date traceMinute(Date realDate, int minute, String areaId,
			String processDefineId) throws Exception {
		// 开始计时
		String realDateStr = CalendarUtil.getInstance().getDateTime(realDate);
		DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

		Date nextOffTime = getNextOffDate(areaId, processDefineId, realDate);
		if (nextOffTime == null) {
			throw new Exception("next offTime is null");
		}
		if (minute < 0) {
			throw new Exception("minute is negative");

		}

		long interval = (nextOffTime.getTime() - realDate.getTime())
				/ (60 * 1000); // 分钟
		if (interval > minute) {
			String returnStr = CalendarUtil.getInstance().getPreDateTime(
					realDateStr, CalendarUtil.MINUTE, minute);
			return df.parse(returnStr);
		} else {
			Date nextStartDate = getNextStartDate(areaId, processDefineId,
					nextOffTime);
			return traceMinute(nextStartDate,
					minute - new Long(interval).intValue(), areaId, processDefineId);
		}
	}
	/**
     * 得到上班时间(输入时间为下班时间）
     * @param areaId strig
     * @param date Date
     * @return Date
	 * @throws Exception 
     */
	 private Date getNextStartDate(String areaId, String processDefineId,
			Date date) throws Exception {
		String dateStr = CalendarUtil.getInstance().getDateTime(date);

		int currentHour = Integer.parseInt(dateStr.substring(11, 13));
		int currentMinute = Integer.parseInt(dateStr.substring(14, 16));
		int currentTime = currentHour * 60 + currentMinute;

		int[] timeBlot = getWorkTimeArr(areaId, processDefineId, date);
		if (timeBlot.length == 2 && timeBlot[1] == 24 * 60) {
			return getNextStartFromHoliday(areaId, processDefineId, date);
		} else if (timeBlot.length == 2 && timeBlot[1] == 0) {
			return getNextStartFromHoliday(areaId, processDefineId, date);
		} else {
			for (int i = 0; i < timeBlot.length; i++) {
				if (currentTime == timeBlot[i]) {
					if ((i + 1) < timeBlot.length) {
						return compriseDate(dateStr, timeBlot[i + 1] / 60,
								timeBlot[i + 1] % 60);
					}
				}
			}
			return getNextStartFromHoliday(areaId, processDefineId, date);
		}
	}
	/**
     * 得到下班时间(输入时间为上班时间）
     * @param areaId String
     * @param date Date
     * @return Date
	 * @throws Exception 
     */
	private Date getNextOffDate(String areaId, String processDefineId,
			Date date) throws Exception {
		String dateStr = CalendarUtil.getInstance().getDateTime(date);

		int currentHour = Integer.parseInt(dateStr.substring(11, 13));
		int currentMinute = Integer.parseInt(dateStr.substring(14, 16));
		int currentTime = currentHour * 60 + currentMinute;

		int[] timeBlot = getWorkTimeArr(areaId,processDefineId, date);
		if (timeBlot.length == 2 && timeBlot[1] == 24 * 60) {
			return compriseDate(dateStr, 24, 0);
		} else if (timeBlot.length == 2 && timeBlot[1] == 0) {
			return null;
		} else {
			for (int i = 0; i < timeBlot.length; i++) {
				if (i % 2 == 0) {
					if (currentTime >= timeBlot[i]
							&& currentTime <= timeBlot[i + 1]) {
						return compriseDate(dateStr, timeBlot[i + 1] / 60,
								timeBlot[i + 1] % 60);
					}
				}
			}
			return null;
		}
	}
	private Date getRealStartDate(String areaId, String processDefineId,
			Date date) throws Exception {
		String dateStr = CalendarUtil.getInstance().getDateTime(date);
        int currentHour = Integer.parseInt(dateStr.substring(11, 13));
        int currentMinute = Integer.parseInt(dateStr.substring(14, 16));
        int currentTime = currentHour * 60 + currentMinute;

        int[] timeBlot = getWorkTimeArr(areaId, processDefineId, date);
        if (timeBlot.length == 2 && timeBlot[1] == 24 * 60) {
            return date;
        }
        else if (timeBlot.length == 2 && timeBlot[1] == 0) {
            return getNextStartFromHoliday(areaId, processDefineId, date);
        }
        else {
            for (int i = 0; i < timeBlot.length; i++) {
                if (i % 2 == 0) {
                    if (currentTime <= timeBlot[i]) {
                        return compriseDate(dateStr, timeBlot[i] / 60, timeBlot[i] % 60);
                    }
                    else if (timeBlot[i] < currentTime && currentTime <= timeBlot[i + 1]) {
                        return date;
                    }
                }
            }
            return getNextStartFromHoliday(areaId, processDefineId, date);

        }

	}
	private Date getNextStartFromHoliday(String areaId, String processDefineId,
			Date date) throws Exception {
		String dateStr = CalendarUtil.getInstance().getDateTime(date);
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        boolean flag = true;
        String nextDayStr = null;
        Date nextDay = null;
        while (flag) {
            nextDayStr = CalendarUtil.getInstance().getPreDateTime(dateStr, CalendarUtil.DAY, 1);
            nextDay = df.parse(nextDayStr);
            String workTimeType = holidayRuleClient.getWorkTimeType(areaId, processDefineId, nextDay);
            if (workTimeType.equals(HolidayRuleClient.DATE_STATE_HOLIDAY)) {
                flag = true;
            }
            else {
                flag = false;
            }
            dateStr = nextDayStr;
        }
        int[] timeBlot = getWorkTimeArr(areaId, processDefineId, nextDay);

        return compriseDate(nextDayStr, timeBlot[0] / 60, timeBlot[0] % 60);
	}
	
	private Date compriseDate(String dateStr, int inputHour, int inputMinute) throws ParseException {
		DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		int year = Integer.parseInt(dateStr.substring(0, 4));
		int month = Integer.parseInt(dateStr.substring(5, 7));
		int day = Integer.parseInt(dateStr.substring(8, 10));
		int hour = inputHour;
		int minute = inputMinute;
		int second = Integer.parseInt(dateStr.substring(17, 19));
		String returnDateStr = CalendarUtil.getInstance().getDateTime(year,
				month, day, hour, minute, second);
		return df.parse(returnDateStr);
	}
	/**
	 * 得到某一天的工作时间段
	 * @param areaId
	 * @param processDefineId
	 * @param date
	 * @return
	 * @throws Exception
	 */
	private int[] getWorkTimeArr(String areaId, String processDefineId,
			Date date) throws Exception {
		String workTime = holidayRuleClient.getWorkTime(areaId, processDefineId, date);
        String[] temp0 = StringHelper.split(workTime, BLANK_BAR);
        int temp = 2 * (temp0.length);
        int[] timeblotArr = new int[temp];
        int j = 0;
        for (int i = 0; i < temp0.length; i++) {
            String[] temp1 = StringHelper.split(temp0[i], HOUR_BAR);
            String[] temp2 = StringHelper.split(temp1[0], MINUTE_BAR);
            int time1 = Integer.parseInt(temp2[0]) * 60 + Integer.parseInt(temp2[1]);
            timeblotArr[j++] = time1;
            String[] temp3 = StringHelper.split(temp1[1], MINUTE_BAR);
            int time2 = Integer.parseInt(temp3[0]) * 60 + Integer.parseInt(temp3[1]);
            timeblotArr[j++] = time2;

        }
        return timeblotArr;
	}
	/**
     * 根据时间单位和间隔计算决定时间点
     *
     * @param date
     *            Date
     * @param timeUnit
     *            String
     * @param interVal
     *            int
     * @throws ParseException
     * @return Date
     */
	public Date getAbsDateByTimeUnit(Date date, String timeUnit,
			int interVal) throws ParseException {
		String startDateStr = CalendarUtil.getInstance().getDateTime(date);
		String returnDateStr = null;
		// DAY HOR YEAR MIN
		if (timeUnit != null) {
			if (timeUnit.equals(CommonDomain.DAY)) {
				returnDateStr = CalendarUtil.getInstance().getPreDateTime(
						startDateStr, CalendarUtil.DAY, interVal);
			} else if (timeUnit.equals(CommonDomain.HOR)) {
				returnDateStr = CalendarUtil.getInstance().getPreDateTime(
						startDateStr, CalendarUtil.HOUR, interVal);
			} else if (timeUnit.equals(CommonDomain.MIN)) {
				returnDateStr = CalendarUtil.getInstance().getPreDateTime(
						startDateStr, CalendarUtil.MINUTE, interVal);
			} else if (timeUnit.equals(CommonDomain.YEAR)) {
				returnDateStr = CalendarUtil.getInstance().getPreDateTime(
						startDateStr, CalendarUtil.YEAR, interVal);
			}
		}
		DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:Ss");
		if(returnDateStr != null){
			return df.parse(returnDateStr);
		}
		return null;
	}
	/**
	 * 计算工作项历时（带挂起时间，现在挂起没有涉及到工作项，只挂起了流程实例，所以暂时先不加上挂起时间）
	 * @param workItemDto
	 * @return
	 */
	public Long calPassTime(WorkItemDto workItemDto) {
		long passTimeMin = 0;
		String areaId = workItemDto.getAreaId();;
		Date[] sutimes = getSUTime(workItemDto);
		int m = 0;
		for (int i = 0; i < sutimes.length / 2; i++) {
			passTimeMin = passTimeMin
					+ calWorkTime(sutimes[m], sutimes[m + 1], areaId);
			m = m + 2;
		}
		return passTimeMin;
	}

	/**
	 * 计算起止时间内的工作时间
	 * @param beginDate
	 * @param endDate
	 * @param areaId
	 * @return
	 */
	private long calWorkTime(Date beginDate, Date endDate, String areaId) {
		CalendarUtil calendarUtil = CalendarUtil.getInstance();
		boolean isSameday = false;
		boolean isbegholiday = false;
		boolean isendholiday = false;
		long passDay = 0;
		long worktime = 0;
		long totalMin = 0;
		try {
			// 前一天
			Date preDate = calendarUtil.getDateByFormat(calendarUtil
					.getPreDateTime(calendarUtil.getDateTime(beginDate),
							Calendar.DAY_OF_MONTH, -1));
			Date nextDate = calendarUtil.getDateByFormat(calendarUtil
					.getPreDateTime(calendarUtil.getDateTime(beginDate),
							Calendar.DAY_OF_MONTH, 1));
			// 起日期的24点
			Date beginDate24 = calendarUtil.getDateByFormat(calendarUtil
					.getDate(nextDate) + " 00:00:00");
			Date beginDate00 = calendarUtil.getDateByFormat(calendarUtil
					.getDate(beginDate) + " 00:00:00");
			// 止日期的0点
			Date endDate00 = calendarUtil.getDateByFormat(calendarUtil
					.getDate(endDate) + " 00:00:00");
			// 取起止时间内所有节假日
			java.util.Date[] holidays = holidayRuleClient.getHoldiaysNew(
					areaId, preDate, endDate);
			// 判断起止时间是否同一天
			if (calendarUtil.getDate(beginDate).equals(
					calendarUtil.getDate(endDate))) {
				isSameday = true;
			}
			if(holidays != null && holidays.length>0){
				// 判断起止时间是否节假日
				isbegholiday = isHoliday(beginDate, holidays);
				if (isSameday) {
					isendholiday = isbegholiday;
				} else {
					isendholiday = isHoliday(endDate, holidays);
				}
				// 计算起止时间天数
				if (isSameday) {
					passDay = 1;
				} else {
					passDay = calendarUtil.calculateDayInteval(beginDate00,
							endDate00) + 1;
				}
				// 计算完整一天的工作时间（分钟）
				if (!isSameday) {
					int[] timeBlot = getWorkTimeArr(areaId, null,
							calendarUtil.getDateByFormat("2011-03-21 00:00:00"));
					int m = 0;
					for (int i = 0; i < timeBlot.length / 2; i++) {
						worktime = worktime + (timeBlot[m + 1] - timeBlot[m]);
						m = m + 2;
					}
				}
				// --------------------------------------
				if (isSameday && isbegholiday) { // 起止同一天且是节假日
					totalMin = 0;
				} else if (isSameday && !isbegholiday) { // 起止同一天且工作日
					totalMin = getWorkTime(beginDate, endDate,areaId, null);
				} else if (!isSameday && isbegholiday && isendholiday) { // 起止不同天,起止都是节日
					totalMin = (passDay - holidays.length) * worktime;
				} else if (!isSameday && !isbegholiday && !isendholiday) { // 起止不同天,起止都非节日
					totalMin = ((passDay - holidays.length - 2) * worktime)
							+ getWorkTime(beginDate,
									beginDate24,areaId, null)
							+ getWorkTime(endDate00, endDate,areaId, null);
				} else if (!isSameday && !isbegholiday && isendholiday) { // 起止不同天,起非节日,止节日
					totalMin = ((passDay - holidays.length - 1) * worktime)
							+ getWorkTime(beginDate,
									beginDate24,areaId, null);
				} else if (!isSameday && isbegholiday && !isendholiday) { // 起止不同天,起节日,止非节日
					totalMin = ((passDay - holidays.length - 1) * worktime)
							+ getWorkTime(endDate00, endDate,areaId, null);
				}
				// --------------------------------------
			}
		} catch (ParseException ex) {
			ex.printStackTrace();
		} catch (Exception ex) {
			ex.printStackTrace();
		} finally {
			holidayRuleClient = null;
		}

		return totalMin;
	}

	/**
	 * 计算非节日的同一天内2个时间的工作分钟
	 * @param beginDate
	 * @param endDate
	 * @param areaId
	 * @param processDefineId
	 * @return
	 * @throws Exception 
	 */
	private long getWorkTime(Date beginDate, Date endDate, String areaId,String processDefineId) throws Exception {
		CalendarUtil calendarUtil = CalendarUtil.getInstance();
		int passmin = 0;
		Date realStartDate = getRealStartDate(areaId, processDefineId,
				beginDate);
		if (calendarUtil.getDate(beginDate).equals(
				calendarUtil.getDate(realStartDate))) {
			if (realStartDate.after(endDate)) {
				passmin = 0;
			} else {
				passmin = calculateWorkTimeByMinute(realStartDate, endDate, 0,areaId, processDefineId);
			}
		} else {
			passmin = 0;
		}
		return passmin;
	}
	/**
	 * 计算2个时间相隔的分钟数（剔除下班时间及节假日)
	 * @param startDate
	 * @param endDate
	 * @param minute
	 * @param areaId
	 * @param processDefineId
	 * @return
	 * @throws Exception 
	 */
	private int calculateWorkTimeByMinute(Date startDate, Date endDate,
			int minute, String areaId, String processDefineId) throws Exception {
		//算出下次下班时间
	    Date nextOffTime = getNextOffDate(areaId, processDefineId, startDate);
	    if (nextOffTime == null)
	        throw new Exception("next offTime is null");

	    if (minute < 0)
	        throw new Exception("minute is negative");

	    //如果下次下班时间在结束时间之后，那么间隔的分钟数就是开始时间和结束时间之间相隔的时间了
	    if(nextOffTime.after(endDate)){
	    	long interval = (endDate.getTime() - startDate.getTime()) / (60 * 1000); //分钟
	    	return minute + (int)interval;
	    }else {	//下次下班时间在结束时间之前，先算出从开始时间到下班时间所间隔的分钟数
	    	long interval = (nextOffTime.getTime() - startDate.getTime()) / (60 * 1000); //分钟
	    	minute = minute + (int)interval;
	    	//计算下次上班时间
	    	Date nextStartDate = getNextStartDate(areaId, processDefineId, nextOffTime);
	    	//如果下次上班时间在结束时间之后，那间隔时间就是minute了
	    	if(nextStartDate.after(endDate))
	    		return minute;
	    	//否则，以下次上班时间为新的起点，继续递归计算
	    	return calculateWorkTimeByMinute(nextStartDate, endDate, minute, areaId, processDefineId);
	    }
	}
	/**
	 * 判断某日期是否节假日 
	 * @param indate
	 * @param holidays
	 * @return
	 */
	private boolean isHoliday(Date indate, Date[] holidays) {
		boolean ret = false;
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		String indatestr = sdf.format(indate);
		for (int i = 0; i < holidays.length; i++) {
			if (indatestr.equals(sdf.format(holidays[i]))) {
				ret = true;
			}
		}
		return ret;
	}
	/**
	 * 获取工作项开始、结束时间
	 * @param workItemDto
	 * @return
	 */
	private Date[] getSUTime(WorkItemDto workItemDto) {
		Date createDate = workItemDto.getStartedDate(); 
		Date finishDate = workItemDto.getCompletedDate();
		List<Date> dates = new ArrayList<Date>();
		dates.add(createDate);
		if(finishDate != null){
			dates.add(finishDate);
		}else{
			dates.add(new Date());
		}
		
		return (Date[]) dates.toArray(new Date[dates.size()]);
	}
	/**
	 *  计算实际完成时间与要求完成时限是超时还是剩余时间
	 * @param workItemDto
	 * @return
	 */
	public Long calculateWorkItemTime(WorkItemDto workItemDto) {
		Long time = 0l;
    	try{
    		if(null != workItemDto.getLimitDate() && null != workItemDto.getCompletedDate()){
    		   time = workItemDto.getLimitDate().getTime()-workItemDto.getCompletedDate().getTime();
    		}
    	}catch(Exception e){
    	   e.printStackTrace();
    	}
    	return time;
	}
	/**
	 * 计算工作时限(剔除指定的一段时间)
     * 计算removeStartDate至removeEndDate之间到底经历了多少工作时间，然后把这段工作时间作为时限，以startDate为起点计算工作时限
	 * @param startDate
	 * @param removeStartDate
	 * @param removeEndDate
	 * @param timeLimit
	 * @param timeUnit
	 * @param areaId
	 * @param processDefineId
	 * @return
	 */
	public Date reCalculateWorkTime(Timestamp startDate, Date removeStartDate,
			Date removeEndDate, int timeLimit, String timeUnit, String areaId,
			String processDefineId) {
		//如果要剔除的开始和结束时间有其中一个为空，不再进行计算
        if(removeStartDate == null || removeEndDate == null || startDate == null){
        	return null;
        }
        	
        try {
			if (timeUnit != null && timeUnit.indexOf(DAY) >= 0) {
				return reCalculateWorkTimeByDay(startDate, removeStartDate,removeEndDate, timeLimit, areaId, processDefineId);
			}
			else if (timeUnit != null && timeUnit.indexOf(HOUR) >= 0) {
			    return reCalculateWorkTimeByMinute(startDate, removeStartDate,removeEndDate, timeLimit*60,  areaId, processDefineId);
			}
			else if (timeUnit != null && timeUnit.indexOf(MINUTE) >= 0) {
				return reCalculateWorkTimeByMinute(startDate, removeStartDate,removeEndDate, timeLimit, areaId, processDefineId);
			}
			else {
			    return null;
			}
		} catch (Exception e) {
			logger.error("----重新计算工作时间异常，异常信息："+e.getMessage(),e);
			return null;
		}
	}
	/**
	 * 重新计算两个时间之间的工作时间（以分钟为单位）
	 * @param date
	 * @param removeStartDate
	 * @param removeEndDate
	 * @param timeLimit
	 * @param areaId
	 * @param processDefineId
	 * @return
	 * @throws Exception 
	 */
	private Date reCalculateWorkTimeByMinute(Timestamp date,
			Date removeStartDate, Date removeEndDate, int timeLimit, String areaId,
			String processDefineId) throws Exception {
		//date到removeStartDate相隔的工作时间
    	int interval;

    	Date startDate = getRealStartDate(areaId, processDefineId, date);
	    //如果上班时间在剔除开始时间之后，那就代表要相隔的工作时间长度为0
	    if(startDate.after(removeStartDate))
	    	interval = 0;
	    else
    	   	//计算date到removeStartDate所经历的工作时间
	    	interval = calculateWorkTimeByMinute(startDate, removeStartDate, 0, areaId, processDefineId);

	    //时限值减去间隔的值，得到新的时限值
	    if(timeLimit >= interval)
	        timeLimit = timeLimit - interval;
	    else
	    	//如果时限值比间隔值要小，代表在剔除时间之前就已经超时，不进行计算，直接返回null
	    	return null;

	    //以removeEndDate为起点，重新计算工作时限
	    startDate = getRealStartDate(areaId, processDefineId, removeEndDate);
	    return traceMinute(startDate, timeLimit, areaId, processDefineId);
	}
	/**
	 * 重新计算两个时间之间的工作时间（以天为单位）
	 * @param date
	 * @param removeStartDate
	 * @param removeEndDate
	 * @param timeLimit
	 * @param areaId
	 * @param processDefineId
	 * @return
	 * @throws Exception 
	 */
	private Date reCalculateWorkTimeByDay(Timestamp date,
			Date removeStartDate, Date removeEndDate, int timeLimit,
			String areaId, String processDefineId) throws Exception {
		//date到removeStartDate相隔的工作时间
    	int interval;

    	DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
    	Date realStartDate = getRealStartDate(areaId, processDefineId, date);
    	String realStartStr = df.format(realStartDate);
    	String removeStartStr = df.format(removeStartDate);

    	Date formatStartDate = df.parse(realStartStr);
    	Date formatRemoveStartDate = df.parse(removeStartStr);

    	//如果上班日期在剔除开始日期之后，那就代表要相隔的时间长度为0
    	if(formatStartDate.after(formatRemoveStartDate))
    		interval = 0;
    	else
    	{	//计算上班日期和剔除开始日期相差多少个自然日
    		interval = daysOfTwo(formatStartDate, formatRemoveStartDate);
    		//这里realStartStr要加上" 00:00:00"，不然调CalendarUtil.getInstance().getPreDateTime会报错
    		String nextDayStr = CalendarUtil.getInstance().getPreDateTime(realStartStr + " 00:00:00", CalendarUtil.DAY, 1);
            Date nextDay = df.parse(nextDayStr);

    		while(nextDay.before(formatRemoveStartDate))
    		{
    			String workTimeType = holidayRuleClient.getWorkTimeType(areaId, processDefineId, nextDay);
                //如果当天放假，则相隔天数减1，不考虑上半天班的情况了，不然太复杂。。。。
                if (workTimeType.equals(HolidayRuleClient.DATE_STATE_HOLIDAY))
                	interval--;

                nextDayStr = CalendarUtil.getInstance().getPreDateTime(nextDayStr, CalendarUtil.DAY, 1);
                nextDay = df.parse(nextDayStr);
    		}
	    	//如果时限值比间隔值要小，代表在剔除时间之前就已经超时，不进行计算，直接返回null
    		if(timeLimit < interval)
    			return null;
    	}

    	Date startDate = getRealStartDate(areaId, processDefineId, removeEndDate);
    	return calculateWorkTimeByDay(timeLimit - interval, startDate, areaId, processDefineId);
	}
	/**
	 * 计算2个时间相隔的天数
     * 只精确到天，不包括小时，例如2010-07-19 23:59:00和2010-07-20 00:01:00比较，也是返回1
	 * @param fDate
	 * @param oDate
	 * @return
	 */
	private int daysOfTwo(Date fDate, Date oDate) {
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(fDate);
		int day1 = calendar.get(Calendar.DAY_OF_YEAR);
		calendar.setTime(oDate);
		int day2 = calendar.get(Calendar.DAY_OF_YEAR);
		return day2 - day1;
	}
	
}
