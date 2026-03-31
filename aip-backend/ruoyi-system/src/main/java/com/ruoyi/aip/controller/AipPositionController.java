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
import com.ruoyi.aip.domain.AipPosition;
import com.ruoyi.aip.service.IAipPositionService;
import com.ruoyi.common.utils.poi.ExcelUtil;
import com.ruoyi.common.core.page.TableDataInfo;

/**
 * 岗位分类Controller
 *
 * @author ruoyi
 * @date 2026-03-31
 */
@RestController
@RequestMapping("/aip/position")
public class AipPositionController extends BaseController
{
    @Autowired
    private IAipPositionService aipPositionService;

    /**
     * 查询岗位分类列表
     */
    @GetMapping("/list")
    public TableDataInfo list(AipPosition aipPosition)
    {
        startPage();
        List<AipPosition> list = aipPositionService.selectAipPositionList(aipPosition);
        return getDataTable(list);
    }

    /**
     * 导出岗位分类列表
     */
    @Log(title = "岗位分类", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, AipPosition aipPosition)
    {
        List<AipPosition> list = aipPositionService.selectAipPositionList(aipPosition);
        ExcelUtil<AipPosition> util = new ExcelUtil<AipPosition>(AipPosition.class);
        util.exportExcel(response, list, "岗位分类数据");
    }

    /**
     * 获取岗位分类详细信息
     */
    @GetMapping(value = "/{id}")
    public AjaxResult getInfo(@PathVariable("id") Long id)
    {
        return success(aipPositionService.selectAipPositionById(id));
    }

    /**
     * 新增岗位分类
     */
    @Log(title = "岗位分类", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody AipPosition aipPosition)
    {
        return toAjax(aipPositionService.insertAipPosition(aipPosition));
    }

    /**
     * 修改岗位分类
     */
    @Log(title = "岗位分类", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody AipPosition aipPosition)
    {
        return toAjax(aipPositionService.updateAipPosition(aipPosition));
    }

    /**
     * 删除岗位分类
     */
    @Log(title = "岗位分类", businessType = BusinessType.DELETE)
	@DeleteMapping("/{ids}")
    public AjaxResult remove(@PathVariable Long[] ids)
    {
        return toAjax(aipPositionService.deleteAipPositionByIds(ids));
    }
}
