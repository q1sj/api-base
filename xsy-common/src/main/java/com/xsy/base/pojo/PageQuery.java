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
     * 默认每页条数
     */
    public static final int DEFAULT_PAGE_SIZE = 10;

    /**
     * 当前页
     */
    private Integer page;
    /**
     * 每页条数
     */
    private Integer pageSize;

    public <T> IPage<T> initPage() {
        return new Page<>(page == null ? DEFAULT_PAGE : page, pageSize == null ? DEFAULT_PAGE_SIZE : pageSize);
    }
}
