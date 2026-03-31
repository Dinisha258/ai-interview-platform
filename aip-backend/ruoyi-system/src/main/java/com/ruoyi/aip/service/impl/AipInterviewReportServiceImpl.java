package com.ruoyi.aip.service.impl;

import java.util.List;
import com.ruoyi.common.utils.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.ruoyi.aip.mapper.AipInterviewReportMapper;
import com.ruoyi.aip.domain.AipInterviewReport;
import com.ruoyi.aip.service.IAipInterviewReportService;

/**
 * 综合评估报告Service业务层处理
 * 
 * @author ruoyi
 * @date 2026-03-31
 */
@Service
public class AipInterviewReportServiceImpl implements IAipInterviewReportService 
{
    @Autowired
    private AipInterviewReportMapper aipInterviewReportMapper;

    /**
     * 查询综合评估报告
     * 
     * @param id 综合评估报告主键
     * @return 综合评估报告
     */
    @Override
    public AipInterviewReport selectAipInterviewReportById(Long id)
    {
        return aipInterviewReportMapper.selectAipInterviewReportById(id);
    }

    /**
     * 查询综合评估报告列表
     * 
     * @param aipInterviewReport 综合评估报告
     * @return 综合评估报告
     */
    @Override
    public List<AipInterviewReport> selectAipInterviewReportList(AipInterviewReport aipInterviewReport)
    {
        return aipInterviewReportMapper.selectAipInterviewReportList(aipInterviewReport);
    }

    /**
     * 新增综合评估报告
     * 
     * @param aipInterviewReport 综合评估报告
     * @return 结果
     */
    @Override
    public int insertAipInterviewReport(AipInterviewReport aipInterviewReport)
    {
        aipInterviewReport.setCreateTime(DateUtils.getNowDate());
        return aipInterviewReportMapper.insertAipInterviewReport(aipInterviewReport);
    }

    /**
     * 修改综合评估报告
     * 
     * @param aipInterviewReport 综合评估报告
     * @return 结果
     */
    @Override
    public int updateAipInterviewReport(AipInterviewReport aipInterviewReport)
    {
        return aipInterviewReportMapper.updateAipInterviewReport(aipInterviewReport);
    }

    /**
     * 批量删除综合评估报告
     * 
     * @param ids 需要删除的综合评估报告主键
     * @return 结果
     */
    @Override
    public int deleteAipInterviewReportByIds(Long[] ids)
    {
        return aipInterviewReportMapper.deleteAipInterviewReportByIds(ids);
    }

    /**
     * 删除综合评估报告信息
     * 
     * @param id 综合评估报告主键
     * @return 结果
     */
    @Override
    public int deleteAipInterviewReportById(Long id)
    {
        return aipInterviewReportMapper.deleteAipInterviewReportById(id);
    }
}
