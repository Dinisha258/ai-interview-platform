package com.ruoyi.aip.service.impl;

import java.util.List;
import com.ruoyi.common.utils.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.ruoyi.aip.mapper.AipPositionMapper;
import com.ruoyi.aip.domain.AipPosition;
import com.ruoyi.aip.service.IAipPositionService;

/**
 * 岗位分类Service业务层处理
 * 
 * @author ruoyi
 * @date 2026-03-31
 */
@Service
public class AipPositionServiceImpl implements IAipPositionService 
{
    @Autowired
    private AipPositionMapper aipPositionMapper;

    /**
     * 查询岗位分类
     * 
     * @param id 岗位分类主键
     * @return 岗位分类
     */
    @Override
    public AipPosition selectAipPositionById(Long id)
    {
        return aipPositionMapper.selectAipPositionById(id);
    }

    /**
     * 查询岗位分类列表
     * 
     * @param aipPosition 岗位分类
     * @return 岗位分类
     */
    @Override
    public List<AipPosition> selectAipPositionList(AipPosition aipPosition)
    {
        return aipPositionMapper.selectAipPositionList(aipPosition);
    }

    /**
     * 新增岗位分类
     * 
     * @param aipPosition 岗位分类
     * @return 结果
     */
    @Override
    public int insertAipPosition(AipPosition aipPosition)
    {
        aipPosition.setCreateTime(DateUtils.getNowDate());
        return aipPositionMapper.insertAipPosition(aipPosition);
    }

    /**
     * 修改岗位分类
     * 
     * @param aipPosition 岗位分类
     * @return 结果
     */
    @Override
    public int updateAipPosition(AipPosition aipPosition)
    {
        aipPosition.setUpdateTime(DateUtils.getNowDate());
        return aipPositionMapper.updateAipPosition(aipPosition);
    }

    /**
     * 批量删除岗位分类
     * 
     * @param ids 需要删除的岗位分类主键
     * @return 结果
     */
    @Override
    public int deleteAipPositionByIds(Long[] ids)
    {
        return aipPositionMapper.deleteAipPositionByIds(ids);
    }

    /**
     * 删除岗位分类信息
     * 
     * @param id 岗位分类主键
     * @return 结果
     */
    @Override
    public int deleteAipPositionById(Long id)
    {
        return aipPositionMapper.deleteAipPositionById(id);
    }
}
