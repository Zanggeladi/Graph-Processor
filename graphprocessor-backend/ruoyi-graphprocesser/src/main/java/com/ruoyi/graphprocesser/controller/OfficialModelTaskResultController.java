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
import com.ruoyi.graphprocesser.domain.OfficialModelTaskResult;
import com.ruoyi.graphprocesser.service.IOfficialModelTaskResultService;
import com.ruoyi.common.utils.poi.ExcelUtil;
import com.ruoyi.common.core.page.TableDataInfo;

/**
 * 官方模型图数据处理任务结果Controller
 * 
 * @author zgs
 * @date 2024-11-20
 */
@Api(tags = "官方模型图数据处理任务结果Controller")
@RestController
@RequestMapping("/graphprocesser/officialmodeltaskresult")
public class OfficialModelTaskResultController extends BaseController
{
    @Autowired
    private IOfficialModelTaskResultService officialModelTaskResultService;

    /**
     * 根据taskId查询官方模型图数据处理任务结果
     */
    @ApiOperation(value = "根据taskId查询官方模型图数据处理任务结果")
//    @PreAuthorize("@ss.hasPermi('graphprocesser:officialmodeltaskresult:query')")
    @GetMapping("/taskId/{taskId}")
    public AjaxResult getInfoByTaskId(@PathVariable("taskId") Long taskId)
    {
        OfficialModelTaskResult officialModelTaskResult = officialModelTaskResultService.selectOfficialModelTaskResultByTaskId(taskId);
        System.out.println("officialModelTaskResult = " + officialModelTaskResult);
        return success(officialModelTaskResultService.selectOfficialModelTaskResultByTaskId(taskId));
    }

    /**
     * 查询官方模型图数据处理任务结果列表
     */
    @ApiOperation(value = "查询官方模型图数据处理任务结果列表")
//    @PreAuthorize("@ss.hasPermi('graphprocesser:officialmodeltaskresult:list')")
    @GetMapping("/list")
    public TableDataInfo list(OfficialModelTaskResult officialModelTaskResult)
    {
        startPage();
        List<OfficialModelTaskResult> list = officialModelTaskResultService.selectOfficialModelTaskResultList(officialModelTaskResult);
        return getDataTable(list);
    }

    /**
     * 导出官方模型图数据处理任务结果列表
     */
    @ApiOperation(value = "导出官方模型图数据处理任务结果列表")
//    @PreAuthorize("@ss.hasPermi('graphprocesser:officialmodeltaskresult:export')")
    @Log(title = "官方模型图数据处理任务结果", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, OfficialModelTaskResult officialModelTaskResult)
    {
        List<OfficialModelTaskResult> list = officialModelTaskResultService.selectOfficialModelTaskResultList(officialModelTaskResult);
        ExcelUtil<OfficialModelTaskResult> util = new ExcelUtil<OfficialModelTaskResult>(OfficialModelTaskResult.class);
        util.exportExcel(response, list, "官方模型图数据处理任务结果数据");
    }

    /**
     * 获取官方模型图数据处理任务结果详细信息
     */
    @ApiOperation(value = "获取官方模型图数据处理任务结果详细信息")
//    @PreAuthorize("@ss.hasPermi('graphprocesser:officialmodeltaskresult:query')")
    @GetMapping(value = "/{id}")
    public AjaxResult getInfo(@PathVariable("id") Long id)
    {
        return success(officialModelTaskResultService.selectOfficialModelTaskResultById(id));
    }

    /**
     * 新增官方模型图数据处理任务结果
     */
    @ApiOperation(value = "新增官方模型图数据处理任务结果")
//    @PreAuthorize("@ss.hasPermi('graphprocesser:officialmodeltaskresult:add')")
    @Log(title = "官方模型图数据处理任务结果", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody OfficialModelTaskResult officialModelTaskResult)
    {
        return toAjax(officialModelTaskResultService.insertOfficialModelTaskResult(officialModelTaskResult));
    }

    /**
     * 修改官方模型图数据处理任务结果
     */
    @ApiOperation(value = "修改官方模型图数据处理任务结果")
//    @PreAuthorize("@ss.hasPermi('graphprocesser:officialmodeltaskresult:edit')")
    @Log(title = "官方模型图数据处理任务结果", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody OfficialModelTaskResult officialModelTaskResult)
    {
        return toAjax(officialModelTaskResultService.updateOfficialModelTaskResult(officialModelTaskResult));
    }

    /**
     * 删除官方模型图数据处理任务结果
     */
    @ApiOperation(value = "删除官方模型图数据处理任务结果")
//    @PreAuthorize("@ss.hasPermi('graphprocesser:officialmodeltaskresult:remove')")
    @Log(title = "官方模型图数据处理任务结果", businessType = BusinessType.DELETE)
	@DeleteMapping("/{ids}")
    public AjaxResult remove(@PathVariable Long[] ids)
    {
        return toAjax(officialModelTaskResultService.deleteOfficialModelTaskResultByIds(ids));
    }
}
