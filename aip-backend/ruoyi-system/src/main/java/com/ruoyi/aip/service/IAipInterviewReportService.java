package com.ruoyi.aip.service;

import java.util.List;
import com.ruoyi.aip.domain.AipInterviewReport;

/**
 * 综合评估报告Service接口
 * 
 * @author ruoyi
 * @date 2026-03-31
 */
public interface IAipInterviewReportService 
{
    /**
     * 查询综合评估报告
     * 
     * @param id 综合评估报告主键
     * @return 综合评估报告
     */
    public AipInterviewReport selectAipInterviewReportById(Long id);

    /**
     * 查询综合评估报告列表
     * 
     * @param aipInterviewReport 综合评估报告
     * @return 综合评估报告集合
     */
    public List<AipInterviewReport> selectAipInterviewReportList(AipInterviewReport aipInterviewReport);

    /**
     * 新增综合评估报告
     * 
     * @param aipInterviewReport 综合评估报告
     * @return 结果
     */
    public int insertAipInterviewReport(AipInterviewReport aipInterviewReport);

    /**
     * 修改综合评估报告
     * 
     * @param aipInterviewReport 综合评估报告
     * @return 结果
     */
    public int updateAipInterviewReport(AipInterviewReport aipInterviewReport);

    /**
     * 批量删除综合评估报告
     * 
     * @param ids 需要删除的综合评估报告主键集合
     * @return 结果
     */
    public int deleteAipInterviewReportByIds(Long[] ids);

    /**
     * 删除综合评估报告信息
     * 
     * @param id 综合评估报告主键
     * @return 结果
     */
    public int deleteAipInterviewReportById(Long id);
}
