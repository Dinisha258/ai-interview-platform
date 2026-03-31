package com.ruoyi.aip.domain;

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
