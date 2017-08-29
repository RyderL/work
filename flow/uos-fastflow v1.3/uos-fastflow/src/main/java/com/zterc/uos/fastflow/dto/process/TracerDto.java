package com.zterc.uos.fastflow.dto.process;

import java.util.Collection;



public class TracerDto {
    private ActivityInstanceDto fromActivityInstance;//起点
    private Collection<ActivityInstanceDto> toActivityInstances;//终点集合
    private Collection<TransitionInstanceDto> transitionInstances;//边集合

    public TracerDto() {
    }
    
    public ActivityInstanceDto getFromActivityInstance() {
        return fromActivityInstance;
    }
    
    public void setFromActivityInstance(ActivityInstanceDto fromActivityInstance) {
        this.fromActivityInstance = fromActivityInstance;
    }
    
    public Collection<ActivityInstanceDto> getToActivityInstances() {
        return toActivityInstances;
    }
    
    public void setToActivityInstances(Collection<ActivityInstanceDto> toActivityInstances) {
        this.toActivityInstances = toActivityInstances;
    }
    
    public Collection<TransitionInstanceDto> getTransitionInstances() {
        return transitionInstances;
    }
    
    public void setTransitionInstances(Collection<TransitionInstanceDto> transitionInstances) {
        this.transitionInstances = transitionInstances;
    }

}
