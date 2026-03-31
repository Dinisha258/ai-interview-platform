package com.ruoyi.aip.domain;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.ruoyi.common.annotation.Excel;
import com.ruoyi.common.core.domain.BaseEntity;

/**
 * 面试题目库对象 aip_question
 * 
 * @author ruoyi
 * @date 2026-03-31
 */
public class AipQuestion extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** 题目主键ID */
    private Long id;

    /** 关联的岗位ID */
    @Excel(name = "关联的岗位ID")
    private Long positionId;

    /** 题目分类（基础/进阶/场景/项目） */
    @Excel(name = "题目分类", readConverterExp = "基=础/进阶/场景/项目")
    private String type;

    /** 题目内容 */
    @Excel(name = "题目内容")
    private String content;

    /** 标准参考答案及踩分点 */
    @Excel(name = "标准参考答案及踩分点")
    private String answer;

    /** 难度系数（1-5） */
    @Excel(name = "难度系数", readConverterExp = "1=-5")
    private Long difficulty;

    public void setId(Long id) 
    {
        this.id = id;
    }

    public Long getId() 
    {
        return id;
    }

    public void setPositionId(Long positionId) 
    {
        this.positionId = positionId;
    }

    public Long getPositionId() 
    {
        return positionId;
    }

    public void setType(String type) 
    {
        this.type = type;
    }

    public String getType() 
    {
        return type;
    }

    public void setContent(String content) 
    {
        this.content = content;
    }

    public String getContent() 
    {
        return content;
    }

    public void setAnswer(String answer) 
    {
        this.answer = answer;
    }

    public String getAnswer() 
    {
        return answer;
    }

    public void setDifficulty(Long difficulty) 
    {
        this.difficulty = difficulty;
    }

    public Long getDifficulty() 
    {
        return difficulty;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
            .append("id", getId())
            .append("positionId", getPositionId())
            .append("type", getType())
            .append("content", getContent())
            .append("answer", getAnswer())
            .append("difficulty", getDifficulty())
            .append("createBy", getCreateBy())
            .append("createTime", getCreateTime())
            .append("updateBy", getUpdateBy())
            .append("updateTime", getUpdateTime())
            .append("remark", getRemark())
            .toString();
    }
}
