package com.zterc.uos.base.jdbc;

import java.beans.BeanInfo;
import java.beans.IntrospectionException;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.dbutils.handlers.AbstractListHandler;

import com.zterc.uos.base.helper.ClassHelper;

/**
 * 该类主要解决数据库字段与类属性之间的转换存在下划线的情况(如：taskId->task_id) 该类主要参考Spring JDBC的rowmapper方式
 * dbutils使用BeanHandler、BeanListHandler来处理返回集与bean的转换
 * 这里统一使用BeanPropertyHandler，当返回单条记录时，使用JDBCHelper的requiredSingleResult做处理
 * 
 * @author gongyi
 * 
 */
public class BeanPropertyHandler<T> extends AbstractListHandler<T> {

	private BeanMapperHandler beanMapperHandler;

	/**
	 * 需要映射的bean对象的class类型
	 */
	private Class<T> mappedClass;
	/**
	 * 映射的字段
	 */
	private Map<String, PropertyDescriptor> mappedFields;

	/**
	 * 构造函数，根据bean对象的class类型初始化mappedFields
	 * 
	 * @param mappedClass
	 */
	public BeanPropertyHandler(Class<T> mappedClass,
			BeanMapperHandler beanMapperHandler) {
		this.beanMapperHandler = beanMapperHandler;
		initialize(mappedClass);
	}

	/**
	 * ResultSet结果集处理
	 */
	protected T handleRow(ResultSet rs) throws SQLException {
		/**
		 * 根据bean的class类型实例化为对象
		 */
		T mappedObject = ClassHelper.instantiate(mappedClass);
		ResultSetMetaData rsmd = rs.getMetaData();
		int columnCount = rsmd.getColumnCount();
		/**
		 * 对ResultSet结果集字段进行循环
		 */
		for (int index = 1; index <= columnCount; index++) {
			/**
			 * 根据字段索引index获取字段名称
			 */
			String column = JDBCHelper.lookupColumnName(rsmd, index);
			/**
			 * 根据映射字段集合返回字段名称对应的属性描述符对象
			 */
			PropertyDescriptor pd = this.mappedFields.get(column.replaceAll(
					" ", "").toLowerCase());
			if (pd != null) {
				try {
					/**
					 * 根据字段index、属性类型返回字段值
					 */
					Object value = JDBCHelper.getResultSetValue(rs, index,
							pd.getPropertyType());
					try {
						/**
						 * 使用apache-beanutils设置对象的属性
						 */
						BeanUtils
								.setProperty(mappedObject, pd.getName(), value);
					} catch (Exception e) {
						e.printStackTrace();
					}
				} catch (Exception ex) {
					ex.printStackTrace();
				}
			}
		}
		return mappedObject;
	}

	/**
	 * 根据bean对象的class初始化字段映射集合
	 * 
	 * @param mappedClass
	 */
	protected void initialize(Class<T> mappedClass) {
		this.mappedClass = mappedClass;
		this.mappedFields = new HashMap<String, PropertyDescriptor>();
		PropertyDescriptor[] pds = null;
		try {
			/**
			 * 返回bean的属性描述对象数组
			 */
			pds = propertyDescriptors(mappedClass);
		} catch (SQLException e) {
			throw new RuntimeException(e.getMessage(), e);
		}
		for (PropertyDescriptor pd : pds) {
			if (pd.getWriteMethod() != null) {
				this.mappedFields.put(pd.getName().toLowerCase(), pd);
				String columnName = beanMapperHandler
						.getColumnNameFromPropName(mappedClass, pd.getName());
				if (columnName != null) {
					this.mappedFields.put(columnName.toLowerCase(), pd);
				}
			}
		}
	}

	/**
	 * 由Introspector返回指定类型的BeanInfo对象，再返回需要的属性描述对象数组PropertyDescriptor[]
	 * 
	 * @param c
	 * @return PropertyDescriptor[]
	 * @throws SQLException
	 */
	private PropertyDescriptor[] propertyDescriptors(Class<?> c)
			throws SQLException {
		BeanInfo beanInfo = null;
		try {
			beanInfo = Introspector.getBeanInfo(c);
		} catch (IntrospectionException e) {
			throw new SQLException("Bean introspection failed: "
					+ e.getMessage());
		}

		return beanInfo.getPropertyDescriptors();
	}
}
