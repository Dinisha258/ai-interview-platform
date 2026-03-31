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
import com.ruoyi.aip.domain.AipKnowledgeDoc;
import com.ruoyi.aip.service.IAipKnowledgeDocService;
import com.ruoyi.common.utils.poi.ExcelUtil;
import com.ruoyi.common.core.page.TableDataInfo;

/**
 * 知识库文档Controller
 *
 * @author ruoyi
 * @date 2026-03-31
 */
@RestController
@RequestMapping("/aip/doc")
public class AipKnowledgeDocController extends BaseController
{
    @Autowired
    private IAipKnowledgeDocService aipKnowledgeDocService;

    /**
     * 查询知识库文档列表
     */
    @GetMapping("/list")
    public TableDataInfo list(AipKnowledgeDoc aipKnowledgeDoc)
    {
        startPage();
        List<AipKnowledgeDoc> list = aipKnowledgeDocService.selectAipKnowledgeDocList(aipKnowledgeDoc);
        return getDataTable(list);
    }

    /**
     * 导出知识库文档列表
     */
    @Log(title = "知识库文档", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, AipKnowledgeDoc aipKnowledgeDoc)
    {
        List<AipKnowledgeDoc> list = aipKnowledgeDocService.selectAipKnowledgeDocList(aipKnowledgeDoc);
        ExcelUtil<AipKnowledgeDoc> util = new ExcelUtil<AipKnowledgeDoc>(AipKnowledgeDoc.class);
        util.exportExcel(response, list, "知识库文档数据");
    }

    /**
     * 获取知识库文档详细信息
     */
    @GetMapping(value = "/{id}")
    public AjaxResult getInfo(@PathVariable("id") Long id)
    {
        return success(aipKnowledgeDocService.selectAipKnowledgeDocById(id));
    }

    /**
     * 新增知识库文档
     */
    @Log(title = "知识库文档", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody AipKnowledgeDoc aipKnowledgeDoc)
    {
        return toAjax(aipKnowledgeDocService.insertAipKnowledgeDoc(aipKnowledgeDoc));
    }

    /**
     * 修改知识库文档
     */
    @Log(title = "知识库文档", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody AipKnowledgeDoc aipKnowledgeDoc)
    {
        return toAjax(aipKnowledgeDocService.updateAipKnowledgeDoc(aipKnowledgeDoc));
    }

    /**
     * 删除知识库文档
     */
    @Log(title = "知识库文档", businessType = BusinessType.DELETE)
	@DeleteMapping("/{ids}")
    public AjaxResult remove(@PathVariable Long[] ids)
    {
        return toAjax(aipKnowledgeDocService.deleteAipKnowledgeDocByIds(ids));
    }
}
