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
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(nullable = false)
    private String taskName;

    @Column(nullable = false)
    private Boolean enable;
    @Column(nullable = false)
    private String cronExpression;
}