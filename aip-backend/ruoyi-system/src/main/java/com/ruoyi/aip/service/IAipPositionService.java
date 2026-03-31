package com.ruoyi.aip.service;

import java.util.List;
import com.ruoyi.aip.domain.AipPosition;

/**
 * 岗位分类Service接口
 * 
 * @author ruoyi
 * @date 2026-03-31
 */
public interface IAipPositionService 
{
    /**
     * 查询岗位分类
     * 
     * @param id 岗位分类主键
     * @return 岗位分类
     */
    public AipPosition selectAipPositionById(Long id);

    /**
     * 查询岗位分类列表
     * 
     * @param aipPosition 岗位分类
     * @return 岗位分类集合
     */
    public List<AipPosition> selectAipPositionList(AipPosition aipPosition);

    /**
     * 新增岗位分类
     * 
     * @param aipPosition 岗位分类
     * @return 结果
     */
    public int insertAipPosition(AipPosition aipPosition);

    /**
     * 修改岗位分类
     * 
     * @param aipPosition 岗位分类
     * @return 结果
     */
    public int updateAipPosition(AipPosition aipPosition);

    /**
     * 批量删除岗位分类
     * 
     * @param ids 需要删除的岗位分类主键集合
     * @return 结果
     */
    public int deleteAipPositionByIds(Long[] ids);

    /**
     * 删除岗位分类信息
     * 
     * @param id 岗位分类主键
     * @return 结果
     */
    public int deleteAipPositionById(Long id);
}
