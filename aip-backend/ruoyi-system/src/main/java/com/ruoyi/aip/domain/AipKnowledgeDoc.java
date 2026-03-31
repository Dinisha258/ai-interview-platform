package com.ruoyi.aip.domain;

import java.util.Date;
import com.fasterxml.jackson.annotation.JsonFormat;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.ruoyi.common.annotation.Excel;
import com.ruoyi.common.core.domain.BaseEntity;

/**
 * 知识库文档对象 aip_knowledge_doc
 * 
 * @author ruoyi
 * @date 2026-03-31
 */
public class AipKnowledgeDoc extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** 文档主键ID */
    private Long id;

    /** 所属岗位ID */
    @Excel(name = "所属岗位ID")
    private Long positionId;

    /** 文档标题 */
    @Excel(name = "文档标题")
    private String docTitle;

    /** MinIO中的实际文件名 */
    @Excel(name = "MinIO中的实际文件名")
    private String fileName;

    /** MinIO访问路径 */
    @Excel(name = "MinIO访问路径")
    private String fileUrl;

    /** 文件大小 */
    @Excel(name = "文件大小")
    private Long fileSize;

    /** 文件后缀名 */
    @Excel(name = "文件后缀名")
    private String fileType;

    /** 向量状态(0未同步 1同步中 2已同步 3失败) */
    @Excel(name = "向量状态(0未同步 1同步中 2已同步 3失败)")
    private Integer vectorStatus;

    /** 最近一次Embedding时间 */
    @JsonFormat(pattern = "yyyy-MM-dd")
    @Excel(name = "最近一次Embedding时间", width = 30, dateFormat = "yyyy-MM-dd")
    private Date lastVectorTime;

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

    public void setDocTitle(String docTitle) 
    {
        this.docTitle = docTitle;
    }

    public String getDocTitle() 
    {
        return docTitle;
    }

    public void setFileName(String fileName) 
    {
        this.fileName = fileName;
    }

    public String getFileName() 
    {
        return fileName;
    }

    public void setFileUrl(String fileUrl) 
    {
        this.fileUrl = fileUrl;
    }

    public String getFileUrl() 
    {
        return fileUrl;
    }

    public void setFileSize(Long fileSize) 
    {
        this.fileSize = fileSize;
    }

    public Long getFileSize() 
    {
        return fileSize;
    }

    public void setFileType(String fileType) 
    {
        this.fileType = fileType;
    }

    public String getFileType() 
    {
        return fileType;
    }

    public void setVectorStatus(Integer vectorStatus) 
    {
        this.vectorStatus = vectorStatus;
    }

    public Integer getVectorStatus() 
    {
        return vectorStatus;
    }

    public void setLastVectorTime(Date lastVectorTime) 
    {
        this.lastVectorTime = lastVectorTime;
    }

    public Date getLastVectorTime() 
    {
        return lastVectorTime;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
            .append("id", getId())
            .append("positionId", getPositionId())
            .append("docTitle", getDocTitle())
            .append("fileName", getFileName())
            .append("fileUrl", getFileUrl())
            .append("fileSize", getFileSize())
            .append("fileType", getFileType())
            .append("vectorStatus", getVectorStatus())
            .append("lastVectorTime", getLastVectorTime())
            .append("createBy", getCreateBy())
            .append("createTime", getCreateTime())
            .append("updateBy", getUpdateBy())
            .append("updateTime", getUpdateTime())
            .append("remark", getRemark())
            .toString();
    }
}
