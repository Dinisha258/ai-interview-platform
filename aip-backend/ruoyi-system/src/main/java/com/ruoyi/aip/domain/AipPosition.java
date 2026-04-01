package com.ruoyi.aip.domain;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.ruoyi.common.annotation.Excel;
import com.ruoyi.common.core.domain.BaseEntity;

/**
 * 岗位分类对象 aip_position
 * 
 * @author ruoyi
 * @date 2026-03-31
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class AipPosition extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** 岗位主键ID */
    private Long id;

    /** 岗位名称 */
    @Excel(name = "岗位名称")
    private String name;

    /** 状态（0正常 1停用） */
    @Excel(name = "状态", readConverterExp = "0=正常,1=停用")
    private Integer status;


    @Override
    public String toString() {
        return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
            .append("id", getId())
            .append("name", getName())
            .append("status", getStatus())
            .append("createBy", getCreateBy())
            .append("createTime", getCreateTime())
            .append("updateBy", getUpdateBy())
            .append("updateTime", getUpdateTime())
            .append("remark", getRemark())
            .toString();
    }
}
