package com.xsy.base.util;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.toolkit.LambdaUtils;
import com.baomidou.mybatisplus.core.toolkit.support.SFunction;
import com.xsy.base.exception.ParamValidationException;
import org.apache.ibatis.reflection.property.PropertyNamer;
import org.springframework.lang.Nullable;

/**
 * 数据库数据重复校验工具
 *
 * @author Q1sj
 * @date 2023.12.26 16:21
 */
public class DuplicateVerifyUtils {
	/**
	 * @param verifyField 校验字段
	 * @param verifyVal   校验值
	 * @param idField     id字段
	 * @param id          id值 可空
	 * @param baseMapper
	 * @param <T>
	 */
	public static <T> void verify(SFunction<T, ?> verifyField, Object verifyVal,
	                              SFunction<T, ?> idField, @Nullable Object id,
	                              BaseMapper<T> baseMapper) {
		Long count = baseMapper.selectCount(new LambdaQueryWrapper<T>()
				.eq(verifyField, verifyVal)
				.ne(id != null, idField, id)
		);
		if (count > 0) {
			String implMethodName = LambdaUtils.extract(verifyField).getImplMethodName();
			String fieldName = PropertyNamer.methodToProperty(implMethodName);
			throw new ParamValidationException(fieldName + "重复");
		}
	}
}
