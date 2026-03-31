package com.ruoyi.aip.mapper;

import java.util.List;
import com.ruoyi.aip.domain.AipKnowledgeDoc;

/**
 * 知识库文档Mapper接口
 * 
 * @author ruoyi
 * @date 2026-03-31
 */
public interface AipKnowledgeDocMapper 
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
     * 删除知识库文档
     * 
     * @param id 知识库文档主键
     * @return 结果
     */
    public int deleteAipKnowledgeDocById(Long id);

    /**
     * 批量删除知识库文档
     * 
     * @param ids 需要删除的数据主键集合
     * @return 结果
     */
    public int deleteAipKnowledgeDocByIds(Long[] ids);
}
