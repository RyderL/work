package com.zterc.uos.test;

import java.sql.SQLException;
import java.util.Date;

import org.dom4j.DocumentException;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.transaction.TransactionConfiguration;

import com.zterc.uos.test.model.TestModel;
import com.zterc.uos.test.util.ContextUtil;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("classpath:application.xml")
@TransactionConfiguration(defaultRollback = true, transactionManager = "transactionManager")
public class TestBase {

	private static final Logger LOGGER = LoggerFactory.getLogger(TestBase.class);

	@Before
	public void before() throws ClassNotFoundException, SQLException, DocumentException {
		ContextUtil.init();
	}

	@Test
	public void test() throws ClassNotFoundException, SQLException {
		LOGGER.info("=========================华丽的分割线==================================");
		LOGGER.info("==========start test============" + new java.util.Date());
		// TestModel testModel = ContextUtil.getTestModels().get(8);
		for (TestModel testModel : ContextUtil.getTestModels()) {
			testModel.test();
		}
		LOGGER.info("==========end test============" + new Date());
	}
}