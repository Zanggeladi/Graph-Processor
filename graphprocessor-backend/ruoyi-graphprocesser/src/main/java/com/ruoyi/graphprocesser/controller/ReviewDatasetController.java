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
import com.ruoyi.graphprocesser.domain.ReviewDataset;
import com.ruoyi.graphprocesser.service.IReviewDatasetService;
import com.ruoyi.common.utils.poi.ExcelUtil;
import com.ruoyi.common.core.page.TableDataInfo;

/**
 * 审核数据集Controller
 * 
 * @author zgs
 * @date 2024-09-10
 */
@Api(tags = "审核数据集Controller")
@RestController
@RequestMapping("/graphprocesser/reviewdataset")
public class ReviewDatasetController extends BaseController
{
    @Autowired
    private IReviewDatasetService reviewDatasetService;

    /**
     * 查询审核数据集列表
     */
    @ApiOperation(value = "查询审核数据集列表")
    @PreAuthorize("@ss.hasPermi('graphprocesser:reviewdataset:list')")
    @GetMapping("/list")
    public TableDataInfo list(ReviewDataset reviewDataset)
    {
        startPage();
        List<ReviewDataset> list = reviewDatasetService.selectReviewDatasetList(reviewDataset);
        return getDataTable(list);
    }

    /**
     * 导出审核数据集列表
     */
    @ApiOperation(value = "导出审核数据集列表")
    @PreAuthorize("@ss.hasPermi('graphprocesser:reviewdataset:export')")
    @Log(title = "审核数据集", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, ReviewDataset reviewDataset)
    {
        List<ReviewDataset> list = reviewDatasetService.selectReviewDatasetList(reviewDataset);
        ExcelUtil<ReviewDataset> util = new ExcelUtil<ReviewDataset>(ReviewDataset.class);
        util.exportExcel(response, list, "审核数据集数据");
    }

    /**
     * 获取审核数据集详细信息
     */
    @ApiOperation(value = "获取审核数据集详细信息")
    @PreAuthorize("@ss.hasPermi('graphprocesser:reviewdataset:query')")
    @GetMapping(value = "/{datasetId}")
    public AjaxResult getInfo(@PathVariable("datasetId") Long datasetId)
    {
        return success(reviewDatasetService.selectReviewDatasetByDatasetId(datasetId));
    }

    /**
     * 新增审核数据集
     */
    @ApiOperation(value = "新增审核数据集")
    @PreAuthorize("@ss.hasPermi('graphprocesser:reviewdataset:add')")
    @Log(title = "审核数据集", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody ReviewDataset reviewDataset)
    {
        return toAjax(reviewDatasetService.insertReviewDataset(reviewDataset));
    }

    /**
     * 修改审核数据集
     */
    @ApiOperation(value = "修改审核数据集")
    @PreAuthorize("@ss.hasPermi('graphprocesser:reviewdataset:edit')")
    @Log(title = "审核数据集", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody ReviewDataset reviewDataset)
    {
        return toAjax(reviewDatasetService.updateReviewDataset(reviewDataset));
    }

    /**
     * 删除审核数据集
     */
    @ApiOperation(value = "删除审核数据集")
    @PreAuthorize("@ss.hasPermi('graphprocesser:reviewdataset:remove')")
    @Log(title = "审核数据集", businessType = BusinessType.DELETE)
	@DeleteMapping("/{datasetIds}")
    public AjaxResult remove(@PathVariable Long[] datasetIds)
    {
        return toAjax(reviewDatasetService.deleteReviewDatasetByDatasetIds(datasetIds));
    }
}
