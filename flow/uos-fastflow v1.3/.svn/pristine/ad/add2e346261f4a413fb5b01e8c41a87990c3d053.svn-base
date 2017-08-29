package com.ztesoft.uosflow.web.service.area;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.zterc.uos.base.helper.GsonHelper;
import com.zterc.uos.base.helper.LongHelper;
import com.zterc.uos.fastflow.dto.specification.AreaDto;
import com.zterc.uos.fastflow.service.AreaService;

@Service("AreaServ")
public class AreaServImpl implements AreaServ{
	//private static Logger logger = LoggerFactory.getLogger(AreaServiceImpl.class);
	
	@Autowired
	private AreaService areaService; 
	
	public String getAreaJsonTree(Map<String,Object>  map) throws SQLException{
		Long areaId = LongHelper.valueOf(map.get("areaId"));
		AreaDto[] areas = areaService.getAreas(areaId);
		return this.arrayToTreeJson(areas);
	}
	
	/**
     * 把地区对象数组转变为Json格式字符串(构成easyui-tree标准);
     * @param areas AreaDto[]
     * @return String
     */
    private String arrayToTreeJson(AreaDto[] areas){
    	JsonArray list = new JsonArray();
		if(areas!=null&&areas.length>0){
			Map<String , JsonObject> parentMap = new HashMap<String, JsonObject>();
			for(int i=0;i<areas.length;i++){
				AreaDto dto = areas[i];
				String pathCode = dto.getPathCode();
				if (pathCode.lastIndexOf(".") >= 0) { //不是最高层地区
					pathCode = pathCode.substring(0, pathCode.lastIndexOf("."));
				}
				//给节点赋值
				JsonObject area = new JsonObject();
				area.addProperty("id", dto.getAreaId());
				area.addProperty("text",  dto.getAreaName());
				 //节点的层次
                if (parentMap.containsKey(pathCode)) {
                	JsonObject parent = (JsonObject) parentMap.get(pathCode);
                	JsonArray children = parent.getAsJsonArray("children");
                	parent.addProperty("state", "closed");
                	if(children==null){
                		children = new JsonArray();
                		parent.add("children", children);
                	}
					children.add(area);
                }else {
                	list.add(area);
                }
                parentMap.put(dto.getPathCode(), area);
			}
		}
		return GsonHelper.toJson(list);
    }
}
