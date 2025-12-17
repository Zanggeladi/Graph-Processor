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
import com.ruoyi.graphprocesser.domain.ExperimentResult;
import com.ruoyi.graphprocesser.service.IExperimentResultService;
import com.ruoyi.common.utils.poi.ExcelUtil;
import com.ruoyi.common.core.page.TableDataInfo;

/**
 * 实验结果Controller
 * 
 * @author zgs
 * @date 2024-09-02
 */
@Api(tags = "实验结果Controller")
@RestController
@RequestMapping("/graphprocesser/experimentresult")
public class ExperimentResultController extends BaseController
{
    @Autowired
    private IExperimentResultService experimentResultService;

    /**
     * 查询实验结果列表
     */
    @ApiOperation(value = "查询实验结果列表")
    @PreAuthorize("@ss.hasPermi('graphprocesser:experimentresult:list')")
    @GetMapping("/list")
    public TableDataInfo list(ExperimentResult experimentResult)
    {
        startPage();
        List<ExperimentResult> list = experimentResultService.selectExperimentResultList(experimentResult);
        return getDataTable(list);
    }

    /**
     * 导出实验结果列表
     */
    @ApiOperation(value = "导出实验结果列表")
    @PreAuthorize("@ss.hasPermi('graphprocesser:experimentresult:export')")
    @Log(title = "实验结果", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, ExperimentResult experimentResult)
    {
        List<ExperimentResult> list = experimentResultService.selectExperimentResultList(experimentResult);
        ExcelUtil<ExperimentResult> util = new ExcelUtil<ExperimentResult>(ExperimentResult.class);
        util.exportExcel(response, list, "实验结果数据");
    }

    /**
     * 获取实验结果详细信息
     */
    @ApiOperation(value = "获取实验结果详细信息")
    @PreAuthorize("@ss.hasPermi('graphprocesser:experimentresult:query')")
    @GetMapping(value = "/{id}")
    public AjaxResult getInfo(@PathVariable("id") Long id)
    {
        return success(experimentResultService.selectExperimentResultById(id));
    }

    /**
     * 新增实验结果
     */
    @ApiOperation(value = "新增实验结果")
    @PreAuthorize("@ss.hasPermi('graphprocesser:experimentresult:add')")
    @Log(title = "实验结果", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody ExperimentResult experimentResult)
    {
        return toAjax(experimentResultService.insertExperimentResult(experimentResult));
    }

    /**
     * 修改实验结果
     */
    @ApiOperation(value = "修改实验结果")
    @PreAuthorize("@ss.hasPermi('graphprocesser:experimentresult:edit')")
    @Log(title = "实验结果", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody ExperimentResult experimentResult)
    {
        return toAjax(experimentResultService.updateExperimentResult(experimentResult));
    }

    /**
     * 删除实验结果
     */
    @ApiOperation(value = "删除实验结果")
    @PreAuthorize("@ss.hasPermi('graphprocesser:experimentresult:remove')")
    @Log(title = "实验结果", businessType = BusinessType.DELETE)
	@DeleteMapping("/{ids}")
    public AjaxResult remove(@PathVariable Long[] ids)
    {
        return toAjax(experimentResultService.deleteExperimentResultByIds(ids));
    }
}
