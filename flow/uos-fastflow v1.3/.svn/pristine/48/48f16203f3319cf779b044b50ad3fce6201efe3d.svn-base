package com.zterc.uos.test.model;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.dom4j.Element;
import org.springframework.transaction.annotation.Transactional;

import com.zterc.uos.test.util.DomUtil;

/**
 * 
 * test½Úµã
 * 
 * @author gong.yi
 * 
 */
public class TestModel {
	private static final Map<String, Class<?>> REFLECT_MAP = new HashMap<String, Class<?>>();

	static {
		REFLECT_MAP.put("execute", ExecuteModel.class);
		REFLECT_MAP.put("assert", AssertModel.class);
	}

	private String name;

	private Map<String, String> params = new HashMap<String, String>();
	public List<IExecutor> iExecutors = new ArrayList<IExecutor>();

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public void initSub(List<Element> list) {
		for (int i = 0; i < list.size(); i++) {
			try {
				Object executor = REFLECT_MAP.get(list.get(i).getName()).newInstance();
				DomUtil.initAttr(executor, list.get(i));
				iExecutors.add((IExecutor) (executor));
			} catch (InstantiationException e) {
				e.printStackTrace();
			} catch (IllegalAccessException e) {
				e.printStackTrace();
			}
		}
	}

	@Transactional
	public void test() throws SQLException {
		System.out.println("====start testModel======" + this.getName());
		for (int i = 0; i < this.iExecutors.size(); i++) {
			IExecutor model = this.iExecutors.get(i);
			model.execute(this);
		}
		System.out.println("====end testModel======" + this.getName());
	}

	public String getParam(String key) {
		return params.get(key);
	}

	@Override
	public String toString() {
		return this.name;
	}
}
