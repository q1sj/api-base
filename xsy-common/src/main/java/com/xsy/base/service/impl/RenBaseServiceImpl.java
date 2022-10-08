package com.xsy.base.service.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.metadata.OrderItem;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.xsy.base.dao.RenBaseDao;
import com.xsy.base.enums.RenConstant;
import com.xsy.base.service.RenBaseService;
import com.xsy.base.util.ConvertUtils;
import com.xsy.base.util.PageData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.util.CollectionUtils;

import java.io.Serializable;
import java.util.Collection;
import java.util.List;
import java.util.Map;

/**
 * @author Q1sj
 * @date 2022.8.29 16:16
 */
@Deprecated
public abstract class RenBaseServiceImpl<M extends RenBaseDao<T>,T> implements RenBaseService<T> {
    @Autowired
    protected M baseDao;
    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Override
    public boolean insert(T entity) {
        return baseDao.insert(entity) > 0;
    }

    @Override
    public boolean insertBatch(Collection<T> entityList) {
        if (CollectionUtils.isEmpty(entityList)) {
            return false;
        }
        entityList.forEach(this::insert);
        return true;
    }

    @Override
    public boolean updateById(T entity) {
        return baseDao.updateById(entity) > 0;
    }

    @Override
    public boolean updateBatchById(Collection<T> entityList) {
        return false;
    }

    @Override
    public T selectById(Serializable id) {
        return baseDao.selectById(id);
    }

    @Override
    public boolean deleteById(Serializable id) {
        return baseDao.deleteById(id) > 0;
    }

    @Override
    public boolean deleteBatchIds(Collection<? extends Serializable> idList) {
        return baseDao.deleteBatchIds(idList) > 0;
    }

    public void exec(String sql) {
        jdbcTemplate.execute(sql);
    }
    /**
     * 获取分页对象
     * @param params      分页查询参数
     * @param defaultOrderField  默认排序字段
     * @param isAsc              排序方式
     */
    @Deprecated
    protected IPage<T> getPage(Map<String, Object> params, String defaultOrderField, boolean isAsc) {
        //分页参数
        long curPage = 1;
        long limit = 10;

        if(params.get(RenConstant.PAGE) != null){
            curPage = Long.parseLong((String)params.get(RenConstant.PAGE));
        }
        if(params.get(RenConstant.LIMIT) != null){
            limit = Long.parseLong((String)params.get(RenConstant.LIMIT));
        }

        //分页对象
        Page<T> page = new Page<>(curPage, limit);

        //分页参数
        params.put(RenConstant.PAGE, page);

        //排序字段
        String orderField = (String)params.get(RenConstant.ORDER_FIELD);
        String order = (String)params.get(RenConstant.ORDER);

        //前端字段排序
        if(StringUtils.isNotBlank(orderField) && StringUtils.isNotBlank(order)){
            if(RenConstant.ASC.equalsIgnoreCase(order)) {
                return page.addOrder(OrderItem.asc(orderField));
            }else {
                return page.addOrder(OrderItem.desc(orderField));
            }
        }

        //没有排序字段，则不排序
        if(StringUtils.isBlank(defaultOrderField)){
            return page;
        }

        //默认排序
        if(isAsc) {
            page.addOrder(OrderItem.asc(defaultOrderField));
        }else {
            page.addOrder(OrderItem.desc(defaultOrderField));
        }

        return page;
    }

    @Deprecated
    protected <T> PageData<T> getPageData(List<?> list, long total, Class<T> target){
        List<T> targetList = ConvertUtils.sourceToTarget(list, target);

        return new PageData<>(targetList, total);
    }

    @Deprecated
    protected <T> PageData<T> getPageData(IPage page, Class<T> target){
        return getPageData(page.getRecords(), page.getTotal(), target);
    }
    @Deprecated
    protected void paramsToLike(Map<String, Object> params, String... likes){
        for (String like : likes){
            String val = (String)params.get(like);
            if (StringUtils.isNotBlank(val)){
                params.put(like, "%" + val + "%");
            }else {
                params.put(like, null);
            }
        }
    }
}
