package com.ruoyi.graphprocesser.controller;

import java.util.List;
import javax.servlet.http.HttpServletResponse;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
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
import com.ruoyi.graphprocesser.domain.ExperimentTheme;
import com.ruoyi.graphprocesser.service.IExperimentThemeService;
import com.ruoyi.common.utils.poi.ExcelUtil;
import com.ruoyi.common.core.page.TableDataInfo;

/**
 * 对比实验主题Controller
 * 
 * @author zgs
 * @date 2024-10-26
 */
@Api(tags = "对比实验主题Controller")
@RestController
@RequestMapping("/graphprocesser/experimenttheme")
public class ExperimentThemeController extends BaseController
{
    @Autowired
    private IExperimentThemeService experimentThemeService;

    /**
     * 查询对比实验主题列表
     */
    @ApiOperation(value = "查询对比实验主题列表")
//    @PreAuthorize("@ss.hasPermi('graphprocesser:experimenttheme:list')")
    @GetMapping("/list")
    public TableDataInfo list(ExperimentTheme experimentTheme)
    {
        startPage();
        List<ExperimentTheme> list = experimentThemeService.selectExperimentThemeList(experimentTheme);
        return getDataTable(list);
    }

    /**
     * 导出对比实验主题列表
     */
    @ApiOperation(value = "导出对比实验主题列表")
//    @PreAuthorize("@ss.hasPermi('graphprocesser:experimenttheme:export')")
    @Log(title = "对比实验主题", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, ExperimentTheme experimentTheme)
    {
        List<ExperimentTheme> list = experimentThemeService.selectExperimentThemeList(experimentTheme);
        ExcelUtil<ExperimentTheme> util = new ExcelUtil<ExperimentTheme>(ExperimentTheme.class);
        util.exportExcel(response, list, "对比实验主题数据");
    }

    /**
     * 获取对比实验主题详细信息
     */
    @ApiOperation(value = "获取对比实验主题详细信息")
//    @PreAuthorize("@ss.hasPermi('graphprocesser:experimenttheme:query')")
    @GetMapping(value = "/{id}")
    public AjaxResult getInfo(@PathVariable("id") Long id)
    {
        return success(experimentThemeService.selectExperimentThemeById(id));
    }

    /**
     * 新增对比实验主题
     */
    @ApiOperation(value = "新增对比实验主题")
//    @PreAuthorize("@ss.hasPermi('graphprocesser:experimenttheme:add')")
    @Log(title = "对比实验主题", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody ExperimentTheme experimentTheme)
    {
        return toAjax(experimentThemeService.insertExperimentTheme(experimentTheme));
    }

    /**
     * 修改对比实验主题
     */
    @ApiOperation(value = "修改对比实验主题")
//    @PreAuthorize("@ss.hasPermi('graphprocesser:experimenttheme:edit')")
    @Log(title = "对比实验主题", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody ExperimentTheme experimentTheme)
    {
        return toAjax(experimentThemeService.updateExperimentTheme(experimentTheme));
    }

    /**
     * 删除对比实验主题
     */
    @ApiOperation(value = "删除对比实验主题")
//    @PreAuthorize("@ss.hasPermi('graphprocesser:experimenttheme:remove')")
    @Log(title = "对比实验主题", businessType = BusinessType.DELETE)
	@DeleteMapping("/{ids}")
    public AjaxResult remove(@PathVariable Long[] ids)
    {
        return toAjax(experimentThemeService.deleteExperimentThemeByIds(ids));
    }
}
