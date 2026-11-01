package com.ruoyi.aip.domain;

import java.math.BigDecimal;
import java.util.Date;

import com.fasterxml.jackson.annotation.JsonFormat;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.ruoyi.common.annotation.Excel;
import com.ruoyi.common.core.domain.BaseEntity;

/**
 * 综合评估报告对象 aip_interview_report
 *
 * @author ruoyi
 * @date 2026-03-31
 */
public class AipInterviewReport extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** 报告主键ID */
    private Long id;

    /** 关联的面试场次ID */
    @Excel(name = "关联的面试场次ID")
    private Long sessionId;

    /** 雷达图数据(JSON) */
    @Excel(name = "雷达图数据(JSON)")
    private String radarDimensions;

    /** AI总结的面试亮点 */
    @Excel(name = "AI总结的面试亮点")
    private String highlights;

    /** AI总结的核心薄弱点 */
    @Excel(name = "AI总结的核心薄弱点")
    private String weaknesses;

    /** 针对性学习和练习建议 */
    @Excel(name = "针对性学习和练习建议")
    private String learningAdvice;

    // ===== 以下为联表查询展示字段(来自 aip_interview_session / aip_position),不存于本表 =====

    /** 面试学生ID */
    private Long userId;

    /** 目标等级(1实习 2初级 3中级 4高级) */
    private Integer targetLevel;

    /** 最终综合得分(会话表) */
    private BigDecimal totalScore;

    /** 面试结束时间(会话表) */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date endTime;

    /** 岗位名称(联表查询) */
    private String positionName;

    public void setId(Long id) 
    {
        this.id = id;
    }

    public Long getId() 
    {
        return id;
    }

    public void setSessionId(Long sessionId) 
    {
        this.sessionId = sessionId;
    }

    public Long getSessionId() 
    {
        return sessionId;
    }

    public void setRadarDimensions(String radarDimensions) 
    {
        this.radarDimensions = radarDimensions;
    }

    public String getRadarDimensions() 
    {
        return radarDimensions;
    }

    public void setHighlights(String highlights) 
    {
        this.highlights = highlights;
    }

    public String getHighlights() 
    {
        return highlights;
    }

    public void setWeaknesses(String weaknesses) 
    {
        this.weaknesses = weaknesses;
    }

    public String getWeaknesses() 
    {
        return weaknesses;
    }

    public void setLearningAdvice(String learningAdvice) 
    {
        this.learningAdvice = learningAdvice;
    }

    public String getLearningAdvice()
    {
        return learningAdvice;
    }

    public Long getUserId()
    {
        return userId;
    }

    public void setUserId(Long userId)
    {
        this.userId = userId;
    }

    public Integer getTargetLevel()
    {
        return targetLevel;
    }

    public void setTargetLevel(Integer targetLevel)
    {
        this.targetLevel = targetLevel;
    }

    public BigDecimal getTotalScore()
    {
        return totalScore;
    }

    public void setTotalScore(BigDecimal totalScore)
    {
        this.totalScore = totalScore;
    }

    public Date getEndTime()
    {
        return endTime;
    }

    public void setEndTime(Date endTime)
    {
        this.endTime = endTime;
    }

    public String getPositionName()
    {
        return positionName;
    }

    public void setPositionName(String positionName)
    {
        this.positionName = positionName;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
            .append("id", getId())
            .append("sessionId", getSessionId())
            .append("radarDimensions", getRadarDimensions())
            .append("highlights", getHighlights())
            .append("weaknesses", getWeaknesses())
            .append("learningAdvice", getLearningAdvice())
            .append("createTime", getCreateTime())
            .toString();
    }
}
