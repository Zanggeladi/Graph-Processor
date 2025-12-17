package com.ruoyi.graphprocesser.controller;

import java.util.List;
import javax.servlet.http.HttpServletResponse;

import com.ruoyi.graphprocesser.domain.Model;
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
import com.ruoyi.graphprocesser.domain.Dataset;
import com.ruoyi.graphprocesser.service.IDatasetService;
import com.ruoyi.common.utils.poi.ExcelUtil;
import com.ruoyi.common.core.page.TableDataInfo;

/**
 * 数据集Controller
 *
 * @author zgs
 * @date 2024-10-26
 */
@Api(tags = "数据集Controller")
@RestController
@RequestMapping("/graphprocesser/task")
public class TaskController extends BaseController
{
    @Autowired
    private IDatasetService datasetService;

    /**
     * 获取当前用户的所有数据集
     */
    @ApiOperation(value = "获取当前用户的所有数据集")
    @GetMapping("/listMy")
    public AjaxResult listMyDataset()
    {
        Long userId = getUserId();
        if (userId == null){
            return warn("用户未登录");
        }
        Dataset dataset = new Dataset();
        dataset.setUserId(userId);
        return success(datasetService.selectDatasetList(dataset));
    }

    /**
     * 查询数据集列表
     */
    @ApiOperation(value = "查询数据集列表")
    @PreAuthorize("@ss.hasPermi('graphprocesser:dataset:list')")
    @GetMapping("/list")
    public TableDataInfo list(Dataset dataset)
    {
        startPage();
        List<Dataset> list = datasetService.selectDatasetList(dataset);
        return getDataTable(list);
    }

    /**
     * 导出数据集列表
     */
    @ApiOperation(value = "导出数据集列表")
    @PreAuthorize("@ss.hasPermi('graphprocesser:dataset:export')")
    @Log(title = "数据集", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, Dataset dataset)
    {
        List<Dataset> list = datasetService.selectDatasetList(dataset);
        ExcelUtil<Dataset> util = new ExcelUtil<Dataset>(Dataset.class);
        util.exportExcel(response, list, "数据集数据");
    }

    /**
     * 获取数据集详细信息
     */
    @ApiOperation(value = "获取数据集详细信息")
    @PreAuthorize("@ss.hasPermi('graphprocesser:dataset:query')")
    @GetMapping(value = "/{id}")
    public AjaxResult getInfo(@PathVariable("id") Long id)
    {
        return success(datasetService.selectDatasetById(id));
    }

    /**
     * 新增数据集
     */
    @ApiOperation(value = "新增数据集")
    @PreAuthorize("@ss.hasPermi('graphprocesser:dataset:add')")
    @Log(title = "数据集", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody Dataset dataset)
    {
        return toAjax(datasetService.insertDataset(dataset));
    }

    /**
     * 修改数据集
     */
    @ApiOperation(value = "修改数据集")
    @PreAuthorize("@ss.hasPermi('graphprocesser:dataset:edit')")
    @Log(title = "数据集", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody Dataset dataset)
    {
        return toAjax(datasetService.updateDataset(dataset));
    }

    /**
     * 删除数据集
     */
    @ApiOperation(value = "删除数据集")
    @PreAuthorize("@ss.hasPermi('graphprocesser:dataset:remove')")
    @Log(title = "数据集", businessType = BusinessType.DELETE)
	@DeleteMapping("/{ids}")
    public AjaxResult remove(@PathVariable Long[] ids)
    {
        return toAjax(datasetService.deleteDatasetByIds(ids));
    }
}
