package com.zterc.uos.test.model;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

import com.zterc.uos.test.util.DomUtil;

/**
 * 
 * context½Úµã
 * @author gong.yi
 *
 */
public class ContextModel {
	private static final String NODE_TEST="test";
	
	private List<TestModel> testModels = new ArrayList<TestModel>();
	
	public ContextModel(File file) throws DocumentException
	{
		SAXReader reader = new SAXReader();
		Document document = reader.read(file);
		Element root = document.getRootElement();
		
		DomUtil.initAttr(this,root);
		
		@SuppressWarnings("unchecked")
		List<Element> tests = root.elements(NODE_TEST);
		for (int i = 0; i < tests.size(); i++) {
			TestModel testModel = new TestModel();
			DomUtil.initAttr(testModel,tests.get(i));
			@SuppressWarnings("unchecked")
			List<Element> subEls = tests.get(i).elements();
			testModel.initSub(subEls);
			testModels.add(testModel);
		}
	}
	
	public List<TestModel> getTestModels()
	{
		return testModels;
	}
	
	public static void main(String[] args) throws DocumentException
	{
		ContextModel cm = new ContextModel(new File("D:\\ztesoft\\workspace\\ws_daas\\zdaas-test\\conf\\context.xml"));
		
		for (TestModel testModel:cm.testModels) {
			System.out.println(testModel.toString());
		}
	}
}
