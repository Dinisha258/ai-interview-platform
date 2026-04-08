package com.ruoyi.aip.domain;

import java.math.BigDecimal;
import java.util.Date;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.ruoyi.common.annotation.Excel;
import com.ruoyi.common.core.domain.BaseEntity;

/**
 * 面试会话流水对象 aip_interview_session
 *
 * @author ruoyi
 * @date 2026-03-31
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class AipInterviewSession extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** 场次主键ID */
    private Long id;

    /** 面试学生ID */
    @Excel(name = "面试学生ID")
    private Long userId;

    /** 面试的目标岗位ID */
    @Excel(name = "面试的目标岗位ID")
    private Long positionId;

    /** 目标级别（1实习 2初级 3高级等） */
    @Excel(name = "目标级别", readConverterExp = "1=实习,2=初级,3=高级等")
    private Integer targetLevel;

    /** 状态（0:进行中, 1:已完成, 2:已异常中断） */
    @Excel(name = "状态", readConverterExp = "0=:进行中,,1=:已完成,,2=:已异常中断")
    private Integer status;

    /** 最终综合得分 */
    @Excel(name = "最终综合得分")
    private BigDecimal totalScore;

    /** 面试开始时间 */
    @JsonFormat(pattern = "yyyy-MM-dd")
    @Excel(name = "面试开始时间", width = 30, dateFormat = "yyyy-MM-dd")
    private Date startTime;

    /** 面试结束时间 */
    @JsonFormat(pattern = "yyyy-MM-dd")
    @Excel(name = "面试结束时间", width = 30, dateFormat = "yyyy-MM-dd")
    private Date endTime;

    /** 岗位名称（联表查询） */
    @Excel(name = "岗位名称")
    private String positionName;

    @Override
    public String toString() {
        return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
            .append("id", getId())
            .append("userId", getUserId())
            .append("positionId", getPositionId())
            .append("targetLevel", getTargetLevel())
            .append("status", getStatus())
            .append("totalScore", getTotalScore())
            .append("startTime", getStartTime())
            .append("endTime", getEndTime())
            .append("createBy", getCreateBy())
            .append("createTime", getCreateTime())
            .append("updateBy", getUpdateBy())
            .append("updateTime", getUpdateTime())
            .append("remark", getRemark())
            .toString();
    }
}
