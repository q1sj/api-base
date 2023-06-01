package com.xsy.sys.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import javax.persistence.*;
import java.util.Date;

/**
 * @author Q1sj
 * @date 2023.6.1 14:54
 */
@Entity(name = "sys_task_log")
@Table(indexes = {
		@Index(name = "idx_create_time", columnList = "createTime"),
		@Index(name = "idx_task_id", columnList = "taskId")
})
@TableName("sys_task_log")
@Data
public class SysTaskLogEntity {
	public static final int SUCCESS_STATUS = 0;
	public static final int FAIL_STATUS = 1;
	private static final int MSG_MAX_LENGTH = 2048;
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@TableId(type = IdType.AUTO)
	private Long id;
	@Column(nullable = false)
	private Long taskId;

	@Column(nullable = false)
	private String taskName;
	/**
	 * @see SysTaskLogEntity#SUCCESS_STATUS
	 * @see SysTaskLogEntity#FAIL_STATUS
	 */
	@Column(nullable = false)
	private Integer status;
	@Column(length = MSG_MAX_LENGTH)
	private String msg;

	@Column(nullable = false)
	private Integer cost;

	@Column(nullable = false)
	private Date createTime;

	public void setMsg(String msg) {
		if (msg != null && msg.length() > MSG_MAX_LENGTH) {
			msg = msg.substring(0, MSG_MAX_LENGTH);
		}
		this.msg = msg;
	}
}
