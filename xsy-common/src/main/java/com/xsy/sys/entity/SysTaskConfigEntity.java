package com.xsy.sys.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import javax.persistence.*;

/**
 * @author Q1sj
 */
@Entity(name = "sys_task_config")
@Table(indexes = {@Index(name = "uk_task_name", columnList = "taskName", unique = true)})
@Data
@TableName("sys_task_config")
public class SysTaskConfigEntity {
    /**
     * 默认允许重复执行cron定时任务
     */
    public static final boolean DEFAULT_ALLOW_RE_FIRES_CRON = true;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(nullable = false)
    private String taskName;

    @Column(nullable = false)
    private Boolean enable;
    @Column(nullable = false)
    private String cronExpression;
    /**
     * 是否允许同一时间重复执行cron定时任务
     * 默认允许
     */
    @Column
    private Boolean allowReFiresCron;

    public boolean getAllowReFiresCron() {
        return allowReFiresCron == null ? DEFAULT_ALLOW_RE_FIRES_CRON : allowReFiresCron;
    }

    public void setAllowReFiresCron(Boolean allowReFiresCron) {
        this.allowReFiresCron = allowReFiresCron;
    }
}