package com.ruoyi.aip.controller;

import java.util.List;
import javax.servlet.http.HttpServletResponse;
import org.springframework.security.access.prepost.PreAuthorize;
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
import com.ruoyi.aip.domain.AipInterviewSession;
import com.ruoyi.aip.service.IAipInterviewSessionService;
import com.ruoyi.common.utils.poi.ExcelUtil;
import com.ruoyi.common.core.page.TableDataInfo;

/**
 * 面试会话流水Controller
 *
 * @author ruoyi
 * @date 2026-03-31
 */
@RestController
@RequestMapping("/aip/session")
public class AipInterviewSessionController extends BaseController
{
    @Autowired
    private IAipInterviewSessionService aipInterviewSessionService;

    /**
     * 查询面试会话流水列表
     */
    @GetMapping("/list")
    public TableDataInfo list(AipInterviewSession aipInterviewSession)
    {
        startPage();
        List<AipInterviewSession> list = aipInterviewSessionService.selectAipInterviewSessionList(aipInterviewSession);
        return getDataTable(list);
    }

    /**
     * 导出面试会话流水列表
     */
    @Log(title = "面试会话流水", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, AipInterviewSession aipInterviewSession)
    {
        List<AipInterviewSession> list = aipInterviewSessionService.selectAipInterviewSessionList(aipInterviewSession);
        ExcelUtil<AipInterviewSession> util = new ExcelUtil<AipInterviewSession>(AipInterviewSession.class);
        util.exportExcel(response, list, "面试会话流水数据");
    }

    /**
     * 获取面试会话流水详细信息
     */
    @GetMapping(value = "/{id}")
    public AjaxResult getInfo(@PathVariable("id") Long id)
    {
        return success(aipInterviewSessionService.selectAipInterviewSessionById(id));
    }

    /**
     * 新增面试会话流水
     */
    @Log(title = "面试会话流水", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody AipInterviewSession aipInterviewSession)
    {
        return toAjax(aipInterviewSessionService.insertAipInterviewSession(aipInterviewSession));
    }

    /**
     * 修改面试会话流水
     */
    @Log(title = "面试会话流水", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody AipInterviewSession aipInterviewSession)
    {
        return toAjax(aipInterviewSessionService.updateAipInterviewSession(aipInterviewSession));
    }

    /**
     * 删除面试会话流水
     */
    @Log(title = "面试会话流水", businessType = BusinessType.DELETE)
	@DeleteMapping("/{ids}")
    public AjaxResult remove(@PathVariable Long[] ids)
    {
        return toAjax(aipInterviewSessionService.deleteAipInterviewSessionByIds(ids));
    }
}
