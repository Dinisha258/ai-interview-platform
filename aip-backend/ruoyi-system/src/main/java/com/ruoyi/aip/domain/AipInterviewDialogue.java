package com.ruoyi.aip.domain;

import java.math.BigDecimal;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.ruoyi.common.annotation.Excel;
import com.ruoyi.common.core.domain.BaseEntity;

/**
 * 问答对话明细对象 aip_interview_dialogue
 *
 * @author ruoyi
 * @date 2026-03-31
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class AipInterviewDialogue extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** 对话主键ID */
    private Long id;

    /** 归属的面试场次ID */
    @Excel(name = "归属的面试场次ID")
    private Long sessionId;

    /** 轮次序号 */
    @Excel(name = "轮次序号")
    private Integer roundNum;

    /** 是否为AI发起的追问（0否 1是） */
    @Excel(name = "是否为AI发起的追问", readConverterExp = "0=否,1=是")
    private Integer isFollowUp;

    /** AI抛出的问题 */
    @Excel(name = "AI抛出的问题")
    private String aiContent;

    /** 学生回答的文本 */
    @Excel(name = "学生回答的文本")
    private String userContent;

    /** 学生答题录音文件路径 */
    @Excel(name = "学生答题录音文件路径")
    private String audioUrl;

    /** 单点打分 */
    @Excel(name = "单点打分")
    private BigDecimal turnScore;

    @Override
    public String toString() {
        return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
            .append("id", getId())
            .append("sessionId", getSessionId())
            .append("roundNum", getRoundNum())
            .append("isFollowUp", getIsFollowUp())
            .append("aiContent", getAiContent())
            .append("userContent", getUserContent())
            .append("audioUrl", getAudioUrl())
            .append("turnScore", getTurnScore())
            .append("createTime", getCreateTime())
            .toString();
    }
}
