package com.ruoyi.aip.service;

import java.util.List;
import com.ruoyi.aip.domain.AipKnowledgeDoc;

/**
 * 知识库文档Service接口
 * 
 * @author ruoyi
 * @date 2026-03-31
 */
public interface IAipKnowledgeDocService 
{
    /**
     * 查询知识库文档
     * 
     * @param id 知识库文档主键
     * @return 知识库文档
     */
    public AipKnowledgeDoc selectAipKnowledgeDocById(Long id);

    /**
     * 查询知识库文档列表
     * 
     * @param aipKnowledgeDoc 知识库文档
     * @return 知识库文档集合
     */
    public List<AipKnowledgeDoc> selectAipKnowledgeDocList(AipKnowledgeDoc aipKnowledgeDoc);

    /**
     * 新增知识库文档
     * 
     * @param aipKnowledgeDoc 知识库文档
     * @return 结果
     */
    public int insertAipKnowledgeDoc(AipKnowledgeDoc aipKnowledgeDoc);

    /**
     * 修改知识库文档
     * 
     * @param aipKnowledgeDoc 知识库文档
     * @return 结果
     */
    public int updateAipKnowledgeDoc(AipKnowledgeDoc aipKnowledgeDoc);

    /**
     * 批量删除知识库文档
     * 
     * @param ids 需要删除的知识库文档主键集合
     * @return 结果
     */
    public int deleteAipKnowledgeDocByIds(Long[] ids);

    /**
     * 删除知识库文档信息
     * 
     * @param id 知识库文档主键
     * @return 结果
     */
    public int deleteAipKnowledgeDocById(Long id);
}
