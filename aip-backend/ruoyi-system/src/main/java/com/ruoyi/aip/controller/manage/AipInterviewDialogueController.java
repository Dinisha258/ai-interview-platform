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
import com.ruoyi.aip.domain.AipInterviewDialogue;
import com.ruoyi.aip.service.IAipInterviewDialogueService;
import com.ruoyi.common.utils.poi.ExcelUtil;
import com.ruoyi.common.core.page.TableDataInfo;

/**
 * 问答对话明细Controller
 *
 * @author ruoyi
 * @date 2026-03-31
 */
@RestController
@RequestMapping("/aip/manage/dialogue")
public class AipInterviewDialogueController extends BaseController
{
    @Autowired
    private IAipInterviewDialogueService aipInterviewDialogueService;

    /**
     * 查询问答对话明细列表
     */
    @GetMapping("/list")
    public TableDataInfo list(AipInterviewDialogue aipInterviewDialogue)
    {
        startPage();
        List<AipInterviewDialogue> list = aipInterviewDialogueService.selectAipInterviewDialogueList(aipInterviewDialogue);
        return getDataTable(list);
    }

    /**
     * 导出问答对话明细列表
     */
    @Log(title = "问答对话明细", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, AipInterviewDialogue aipInterviewDialogue)
    {
        List<AipInterviewDialogue> list = aipInterviewDialogueService.selectAipInterviewDialogueList(aipInterviewDialogue);
        ExcelUtil<AipInterviewDialogue> util = new ExcelUtil<AipInterviewDialogue>(AipInterviewDialogue.class);
        util.exportExcel(response, list, "问答对话明细数据");
    }

    /**
     * 获取问答对话明细详细信息
     */
    @GetMapping(value = "/{id}")
    public AjaxResult getInfo(@PathVariable("id") Long id)
    {
        return success(aipInterviewDialogueService.selectAipInterviewDialogueById(id));
    }

    /**
     * 新增问答对话明细
     */
    @Log(title = "问答对话明细", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody AipInterviewDialogue aipInterviewDialogue)
    {
        return toAjax(aipInterviewDialogueService.insertAipInterviewDialogue(aipInterviewDialogue));
    }

    /**
     * 修改问答对话明细
     */
    @Log(title = "问答对话明细", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody AipInterviewDialogue aipInterviewDialogue)
    {
        return toAjax(aipInterviewDialogueService.updateAipInterviewDialogue(aipInterviewDialogue));
    }

    /**
     * 删除问答对话明细
     */
    @Log(title = "问答对话明细", businessType = BusinessType.DELETE)
	@DeleteMapping("/{ids}")
    public AjaxResult remove(@PathVariable Long[] ids)
    {
        return toAjax(aipInterviewDialogueService.deleteAipInterviewDialogueByIds(ids));
    }
}
