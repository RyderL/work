package com.zterc.uos.base.helper;

import com.zterc.uos.base.sequence.SequenceService;

public class SequenceHelper {
	private static SequenceService sequenceService;
	
	private static int mod = 100;
	private static int fact = 100;
	
	public static int getMod() {
		return mod;
	}

	public static void setMod(int mod) {
		SequenceHelper.mod = mod;
	}
	
	public static int getFact() {
		return fact;
	}

	public static void setFact(int fact) {
		SequenceHelper.fact = fact;
	}

	public static void setSequenceService(SequenceService sequenceService) {
		SequenceHelper.sequenceService = sequenceService;
	}

	public static Long getId(String name) {
		return sequenceService.getGidByName(name);
	}
	
	public static Long getIdWithSeed(String name,String processInstanceId){
		Long id = sequenceService.getGidByName(name);
		if(StringHelper.isEmpty(processInstanceId)){
			return id;
		}
		Long newId = id*fact+LongHelper.valueOf(processInstanceId)%mod;
		if(newId<id){
			throw new RuntimeException("ÐòÁÐÒç³ö£º"+name+"[srcId="+id+";processInstanceId="+processInstanceId+";fact="+fact+";mod="+mod+"]");
		}
		return newId;
	}
	
	public static void main(String[] args) {
		System.out.println(Long.MAX_VALUE);
	}
}
