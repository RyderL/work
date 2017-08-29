package com.ztesoft.uosflow.core.dbpersist.util;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.stereotype.Component;

import com.ztesoft.uosflow.core.dbpersist.dto.AsyncSqlDto;

@Component
public class DbPersist4BatchUtils {
//	private static Logger logger = Logger.getLogger(DbPersist4BatchUtils.class);
  
	/**
	 * 合并两个AsyncSqlDto对象，将params合并至paramsList
	 * 
	 * @return
	 * @author zhong.kaijie on 2017年2月13日 下午9:16:55
	 * @version 1.0.0
	 */
	public static AsyncSqlDto mergeAsyncSqlDtos(AsyncSqlDto batchAsyncSqlDto,
			AsyncSqlDto newAsyncSqlDto) {
		// 同一条sql
		List<Object[]> params = batchAsyncSqlDto.getParamsList();
		if (CollectionUtils.isEmpty(params)) {
			synchronized (batchAsyncSqlDto) {
				params = batchAsyncSqlDto.getParamsList();
				if (CollectionUtils.isEmpty(params)) {
					params = Collections
							.synchronizedList(new ArrayList<Object[]>());
					if (batchAsyncSqlDto.getParams() != null) {
						params.add(batchAsyncSqlDto.getParams());
						batchAsyncSqlDto.setParams(null);
					}
					List<Object[]> newAsyncSqlParams=newAsyncSqlDto.getParamsList();
					if (CollectionUtils.isNotEmpty(newAsyncSqlParams)) {
						params.addAll(newAsyncSqlParams);
					}else if (newAsyncSqlDto.getParams()!=null) {
						params.add(newAsyncSqlDto.getParams());
					}
				}
			}
		} else {
			List<Object[]> paramsList=newAsyncSqlDto.getParamsList();
			// 优先处理paramsList
			if (CollectionUtils.isNotEmpty(paramsList)) {
				params.addAll(paramsList);
			}else{
				params.add(newAsyncSqlDto.getParams());
			}
		}
		batchAsyncSqlDto.setParamsList(params);
		return batchAsyncSqlDto;
	}
	
	/**
	 * 判断是否是批量操作的异步持久化对象
	 * @param batchAsyncSqlDto
	 * @return
	 * @author  zhong.kaijie  on 2017年3月8日 上午11:55:18
	 * @version 1.0.0
	 */
	public static boolean isBatchAsyncSql(AsyncSqlDto batchAsyncSqlDto) {
		List<Object[]> paramsList = batchAsyncSqlDto.getParamsList();
		boolean result = CollectionUtils.isNotEmpty(paramsList);
		return result;
	}
	
	public static List<AsyncSqlDto> splitAsyncSqlDtos(AsyncSqlDto batchAsyncSqlDto) {
		List<AsyncSqlDto> result=new ArrayList<>();
		if (isBatchAsyncSql(batchAsyncSqlDto)) {
			List<Object[]> paramsList=batchAsyncSqlDto.getParamsList();
			for (Object[] objects : paramsList) {
				AsyncSqlDto asyncSqlDto=new AsyncSqlDto();
				asyncSqlDto.setDataSource(batchAsyncSqlDto.getDataSource());
				asyncSqlDto.setKey(batchAsyncSqlDto.getKey());
				asyncSqlDto.setParams(objects);
				asyncSqlDto.setSqlStr(batchAsyncSqlDto.getSqlStr());
				asyncSqlDto.setSqlSeq(batchAsyncSqlDto.getSqlType());
				result.add(asyncSqlDto);
			}
		}else{
			//如果paramsList为空，表示batchAsyncSqlDto并非批量的异步持久化对象
			result.add(batchAsyncSqlDto);
		}
		return  result;
	}

	
	public static AsyncSqlDto getFromAsyncSqlDtos(AsyncSqlDto batchAsyncSqlDto,int index) {
		AsyncSqlDto result=null;
		if (isBatchAsyncSql(batchAsyncSqlDto)) {
			List<Object[]> paramsList=batchAsyncSqlDto.getParamsList();
			result=new AsyncSqlDto();
			result.setDataSource(batchAsyncSqlDto.getDataSource());
			result.setKey(batchAsyncSqlDto.getKey());
			result.setParams(paramsList.get(index));
			result.setSqlStr(batchAsyncSqlDto.getSqlStr());
			result.setSqlSeq(batchAsyncSqlDto.getSqlType());
		}
		return  result;
	}
}
