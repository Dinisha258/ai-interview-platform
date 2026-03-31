package com.ruoyi.aip.service.impl;

import java.util.List;
import com.ruoyi.common.utils.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.ruoyi.aip.mapper.AipKnowledgeDocMapper;
import com.ruoyi.aip.domain.AipKnowledgeDoc;
import com.ruoyi.aip.service.IAipKnowledgeDocService;

/**
 * 知识库文档Service业务层处理
 * 
 * @author ruoyi
 * @date 2026-03-31
 */
@Service
public class AipKnowledgeDocServiceImpl implements IAipKnowledgeDocService 
{
    @Autowired
    private AipKnowledgeDocMapper aipKnowledgeDocMapper;

    /**
     * 查询知识库文档
     * 
     * @param id 知识库文档主键
     * @return 知识库文档
     */
    @Override
    public AipKnowledgeDoc selectAipKnowledgeDocById(Long id)
    {
        return aipKnowledgeDocMapper.selectAipKnowledgeDocById(id);
    }

    /**
     * 查询知识库文档列表
     * 
     * @param aipKnowledgeDoc 知识库文档
     * @return 知识库文档
     */
    @Override
    public List<AipKnowledgeDoc> selectAipKnowledgeDocList(AipKnowledgeDoc aipKnowledgeDoc)
    {
        return aipKnowledgeDocMapper.selectAipKnowledgeDocList(aipKnowledgeDoc);
    }

    /**
     * 新增知识库文档
     * 
     * @param aipKnowledgeDoc 知识库文档
     * @return 结果
     */
    @Override
    public int insertAipKnowledgeDoc(AipKnowledgeDoc aipKnowledgeDoc)
    {
        aipKnowledgeDoc.setCreateTime(DateUtils.getNowDate());
        return aipKnowledgeDocMapper.insertAipKnowledgeDoc(aipKnowledgeDoc);
    }

    /**
     * 修改知识库文档
     * 
     * @param aipKnowledgeDoc 知识库文档
     * @return 结果
     */
    @Override
    public int updateAipKnowledgeDoc(AipKnowledgeDoc aipKnowledgeDoc)
    {
        aipKnowledgeDoc.setUpdateTime(DateUtils.getNowDate());
        return aipKnowledgeDocMapper.updateAipKnowledgeDoc(aipKnowledgeDoc);
    }

    /**
     * 批量删除知识库文档
     * 
     * @param ids 需要删除的知识库文档主键
     * @return 结果
     */
    @Override
    public int deleteAipKnowledgeDocByIds(Long[] ids)
    {
        return aipKnowledgeDocMapper.deleteAipKnowledgeDocByIds(ids);
    }

    /**
     * 删除知识库文档信息
     * 
     * @param id 知识库文档主键
     * @return 结果
     */
    @Override
    public int deleteAipKnowledgeDocById(Long id)
    {
        return aipKnowledgeDocMapper.deleteAipKnowledgeDocById(id);
    }
}
