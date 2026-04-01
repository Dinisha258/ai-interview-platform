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
import com.ruoyi.aip.domain.AipQuestion;
import com.ruoyi.aip.service.IAipQuestionService;
import com.ruoyi.common.utils.poi.ExcelUtil;
import com.ruoyi.common.core.page.TableDataInfo;

/**
 * 面试题目库Controller
 *
 * @author ruoyi
 * @date 2026-03-31
 */
@RestController
@RequestMapping("/aip/manage/question")
public class AipQuestionController extends BaseController
{
    @Autowired
    private IAipQuestionService aipQuestionService;

    /**
     * 查询面试题目库列表
     */
    @GetMapping("/list")
    public TableDataInfo list(AipQuestion aipQuestion)
    {
        startPage();
        List<AipQuestion> list = aipQuestionService.selectAipQuestionList(aipQuestion);
        return getDataTable(list);
    }

    /**
     * 导出面试题目库列表
     */
    @Log(title = "面试题目库", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, AipQuestion aipQuestion)
    {
        List<AipQuestion> list = aipQuestionService.selectAipQuestionList(aipQuestion);
        ExcelUtil<AipQuestion> util = new ExcelUtil<AipQuestion>(AipQuestion.class);
        util.exportExcel(response, list, "面试题目库数据");
    }

    /**
     * 获取面试题目库详细信息
     */
    @GetMapping(value = "/{id}")
    public AjaxResult getInfo(@PathVariable("id") Long id)
    {
        return success(aipQuestionService.selectAipQuestionById(id));
    }

    /**
     * 新增面试题目库
     */
    @Log(title = "面试题目库", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody AipQuestion aipQuestion)
    {
        return toAjax(aipQuestionService.insertAipQuestion(aipQuestion));
    }

    /**
     * 修改面试题目库
     */
    @Log(title = "面试题目库", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody AipQuestion aipQuestion)
    {
        return toAjax(aipQuestionService.updateAipQuestion(aipQuestion));
    }

    /**
     * 删除面试题目库
     */
    @Log(title = "面试题目库", businessType = BusinessType.DELETE)
	@DeleteMapping("/{ids}")
    public AjaxResult remove(@PathVariable Long[] ids)
    {
        return toAjax(aipQuestionService.deleteAipQuestionByIds(ids));
    }
}
