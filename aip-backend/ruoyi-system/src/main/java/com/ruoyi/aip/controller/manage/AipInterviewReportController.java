package com.ruoyi.aip.controller.manage;

import java.util.List;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.ruoyi.common.annotation.Log;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.enums.BusinessType;
import com.ruoyi.aip.domain.AipInterviewReport;
import com.ruoyi.aip.service.IAipInterviewReportService;
import com.ruoyi.common.utils.poi.ExcelUtil;
import com.ruoyi.common.core.page.TableDataInfo;

/**
 * 综合评估报告Controller
 *
 * @author ruoyi
 * @date 2026-03-31
 */
@RestController
@RequestMapping("/aip/report")
public class AipInterviewReportController extends BaseController
{
    @Autowired
    private IAipInterviewReportService aipInterviewReportService;

    /**
     * 查询综合评估报告列表
     */
    @GetMapping("/list")
    public TableDataInfo list(AipInterviewReport aipInterviewReport)
    {
        startPage();
        List<AipInterviewReport> list = aipInterviewReportService.selectAipInterviewReportList(aipInterviewReport);
        return getDataTable(list);
    }

    /**
     * 导出综合评估报告列表
     */
    @Log(title = "综合评估报告", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, AipInterviewReport aipInterviewReport)
    {
        List<AipInterviewReport> list = aipInterviewReportService.selectAipInterviewReportList(aipInterviewReport);
        ExcelUtil<AipInterviewReport> util = new ExcelUtil<AipInterviewReport>(AipInterviewReport.class);
        util.exportExcel(response, list, "综合评估报告数据");
    }

    /**
     * 获取综合评估报告详细信息
     */
    @GetMapping(value = "/{id}")
    public AjaxResult getInfo(@PathVariable("id") Long id)
    {
        return success(aipInterviewReportService.selectAipInterviewReportById(id));
    }

    /**
     * 新增综合评估报告
     */
    @Log(title = "综合评估报告", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody AipInterviewReport aipInterviewReport)
    {
        return toAjax(aipInterviewReportService.insertAipInterviewReport(aipInterviewReport));
    }

    /**
     * 修改综合评估报告
     */
    @Log(title = "综合评估报告", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody AipInterviewReport aipInterviewReport)
    {
        return toAjax(aipInterviewReportService.updateAipInterviewReport(aipInterviewReport));
    }

    /**
     * 删除综合评估报告
     */
    @Log(title = "综合评估报告", businessType = BusinessType.DELETE)
	@DeleteMapping("/{ids}")
    public AjaxResult remove(@PathVariable Long[] ids)
    {
        return toAjax(aipInterviewReportService.deleteAipInterviewReportByIds(ids));
    }
}
