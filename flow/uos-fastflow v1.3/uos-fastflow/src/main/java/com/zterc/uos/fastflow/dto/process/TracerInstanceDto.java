package com.zterc.uos.fastflow.dto.process;

import java.util.Collection;



public class TracerInstanceDto {
    private Collection<TracerDto> tracers;
    private Collection<ActivityInstanceDto> activityInstancesNoInFrom;//δ�������յ�ʵ������

    public TracerInstanceDto() {
    }
    public Collection<ActivityInstanceDto> getActivityInstancesNoInFrom() {
        return activityInstancesNoInFrom;
    }
    public Collection<TracerDto> getTracers() {
        return tracers;
    }
    public void setActivityInstancesNoInFrom(Collection<ActivityInstanceDto> activityInstancesNoInFrom) {
        this.activityInstancesNoInFrom = activityInstancesNoInFrom;
    }
    public void setTracers(Collection<TracerDto> tracers) {
        this.tracers = tracers;
    }
}
