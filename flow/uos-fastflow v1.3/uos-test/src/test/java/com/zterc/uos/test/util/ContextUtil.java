package com.zterc.uos.test.util;

import java.io.File;
import java.net.URL;
import java.util.List;

import org.dom4j.DocumentException;

import com.zterc.uos.test.model.ContextModel;
import com.zterc.uos.test.model.TestModel;

/**
 * 
 * context.xml加载和管理工具类
 * 
 * @author gong.yi
 * 
 */
public class ContextUtil {
	private static boolean inited = false;

	private static final String CONTEXT_PATH = "conf/context.xml";

	private static ContextModel contextModel;

	public static void init() throws DocumentException {
		if (inited) {
			return;
		}
		inited = true;
		URL url = ClassLoader.getSystemClassLoader().getResource(CONTEXT_PATH);
		contextModel = new ContextModel(new File(url.getPath()));
	}

	public static List<TestModel> getTestModels() {
		return contextModel.getTestModels();
	}

	public static ContextModel getContextModel() {
		return contextModel;
	}

}
