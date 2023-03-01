package com.xsy.sys.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import javax.persistence.*;

/**
 * @author Q1sj
 */
@Entity(name = "sys_cron")
@Data
@TableName("sys_cron")
public class SysCron {
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