package com.ztesoft.uosflow.core.dbpersist.dto;

import java.util.Date;

/**
 * ԤԼִ�еĶ���
 * @author zhong.kaijie  on 16/8/1 ����8:45.
 * @version 1.0.0
 */
public interface IDueMessage {
    /**
     * ��ȡΨһ��־��ID,Ϊ��ʱ,��ʾ����Ҫ������Ϣ�ظ�
     *
     * @return ��Ϣ��Ψһ��־��ID
     * @author zhong.kaijie  on 16/8/1 ����9:01
     * @version 1.0.0
     */
    public Object getKey();

    /**
     * ��ȡԤԼִ�е�ʱ��
     * @return ԤԼִ�е�ʱ��
     * @author zhong.kaijie  on 16/8/1 ����8:59
     * @version 1.0.0
     */
    public Date dueDate();
}
