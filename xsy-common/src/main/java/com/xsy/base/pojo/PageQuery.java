package com.xsy.base.pojo;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.Data;

/**
 * @author Q1sj
 * @date 2022.9.26 17:21
 */
@Data
public class PageQuery {
    /**
     * 默认页码
     */
    public static final int DEFAULT_PAGE = 1;
    /**
     * 最大页码
     */
    public static final int MAX_PAGE = 100;
    /**
     * 默认每页条数
     */
    public static final int DEFAULT_PAGE_SIZE = 10;
    /**
     * 最大每页条数
     */
    public static final int MAX_PAGE_SIZE = 100;
    /**
     * 当前页
     */
    private Integer page;
    /**
     * 每页条数
     */
    private Integer pageSize;

    public <T> IPage<T> initPage() {
        return new Page<>(
                page == null ? DEFAULT_PAGE : Math.max(page, MAX_PAGE),
                pageSize == null ? DEFAULT_PAGE_SIZE : Math.max(pageSize, MAX_PAGE_SIZE)
        );
    }
}
