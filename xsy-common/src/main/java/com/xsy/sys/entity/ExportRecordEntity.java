package com.xsy.sys.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.xsy.security.user.SecurityUser;
import com.xsy.security.user.UserDetail;
import com.xsy.sys.enums.ExportStatusEnum;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

@Data
@TableName("export_record")
public class ExportRecordEntity implements Serializable {
	private static final long serialVersionUID = 1L;

	/**
	 * 导出记录ID
	 */
	@TableId
	private Long id;
	/**
	 * 文件名称
	 */
	private String fileName;
	/**
	 * 导出类型：EVENT、SUBSCRIBE、ARCHIVES、TRAFFIC等
	 */
	private String code;
	/**
	 * 导出类型名称 例如：交通事件
	 */
	private String codeName;
	/**
	 * 下载条件
	 */
	private String conditions;
	/**
	 * 导出时长（秒）
	 */
	private Integer exportTime;
	/**
	 * 导出状态：0等待导出、1导出中、2导出成功、3导出失败
	 */
	private Integer status;

	private String statusName;
	/**
	 * v4.4.0 导出进度 0-100
	 */
	private Integer progress;
	/**
	 * 导出失败原因
	 */
	private String failReason;
	/**
	 * 文件id
	 */
	private Long fileId;
	/**
	 * 用户ID
	 */
	private Long userId;
	/**
	 * 用户名
	 */
	private String userName;
	/**
	 * 创建时间
	 */
	private Date createTime;
	/**
	 * 更新时间
	 */
	private Date updateTime;


	public static ExportRecordEntity init(String code, String codeName, String conditions) {
		ExportRecordEntity entity = new ExportRecordEntity();
		entity.setFileName(codeName + System.currentTimeMillis());
		entity.setCode(code);
		entity.setCodeName(codeName);
		entity.setConditions(conditions);
		entity.setStatus(ExportStatusEnum.WAIT.value);
		entity.setStatusName(ExportStatusEnum.WAIT.desc);
		entity.setProgress(0);
		UserDetail user = SecurityUser.getUser();
		entity.setUserId(user.getId());
		entity.setUserName(user.getRealName());
		entity.setCreateTime(new Date());
		return entity;
	}
}
