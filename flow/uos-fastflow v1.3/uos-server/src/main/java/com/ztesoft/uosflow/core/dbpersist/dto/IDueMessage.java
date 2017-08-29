package com.ztesoft.uosflow.core.dbpersist.dto;

import java.util.Date;

/**
 * 预约执行的队列
 * @author zhong.kaijie  on 16/8/1 下午8:45.
 * @version 1.0.0
 */
public interface IDueMessage {
    /**
     * 获取唯一标志的ID,为空时,表示不需要过滤消息重复
     *
     * @return 消息的唯一标志的ID
     * @author zhong.kaijie  on 16/8/1 下午9:01
     * @version 1.0.0
     */
    public Object getKey();

    /**
     * 获取预约执行的时间
     * @return 预约执行的时间
     * @author zhong.kaijie  on 16/8/1 下午8:59
     * @version 1.0.0
     */
    public Date dueDate();
}
