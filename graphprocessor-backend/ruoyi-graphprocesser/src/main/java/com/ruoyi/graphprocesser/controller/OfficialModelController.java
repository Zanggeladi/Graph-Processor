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
import com.ruoyi.graphprocesser.domain.OfficialModel;
import com.ruoyi.graphprocesser.service.IOfficialModelService;
import com.ruoyi.common.utils.poi.ExcelUtil;
import com.ruoyi.common.core.page.TableDataInfo;

/**
 * 官方模型Controller
 * 
 * @author zgs
 * @date 2024-11-10
 */
@Api(tags = "官方模型Controller")
@RestController
@RequestMapping("/graphprocesser/officialmodel")
public class OfficialModelController extends BaseController
{
    @Autowired
    private IOfficialModelService officialModelService;

    /**
     * 获取所有的模型
     */
    @ApiOperation(value = "获取所有的模型")
    @GetMapping("/list/all")
    public AjaxResult listAll()
    {
        return success(officialModelService.selectOfficialModelList(null));
    }

    /**
     * 查询官方模型列表
     */
    @ApiOperation(value = "查询官方模型列表")
//    @PreAuthorize("@ss.hasPermi('graphprocesser:officialmodel:list')")
    @GetMapping("/list")
    public TableDataInfo list(OfficialModel officialModel)
    {
        startPage();
        List<OfficialModel> list = officialModelService.selectOfficialModelList(officialModel);
        return getDataTable(list);
    }

    /**
     * 导出官方模型列表
     */
    @ApiOperation(value = "导出官方模型列表")
//    @PreAuthorize("@ss.hasPermi('graphprocesser:officialmodel:export')")
    @Log(title = "官方模型", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, OfficialModel officialModel)
    {
        List<OfficialModel> list = officialModelService.selectOfficialModelList(officialModel);
        ExcelUtil<OfficialModel> util = new ExcelUtil<OfficialModel>(OfficialModel.class);
        util.exportExcel(response, list, "官方模型数据");
    }

    /**
     * 获取官方模型详细信息
     */
    @ApiOperation(value = "获取官方模型详细信息")
//    @PreAuthorize("@ss.hasPermi('graphprocesser:officialmodel:query')")
//    @PreAuthorize("@ss.hasAnyRoles('admin,common')")
    @GetMapping(value = "/{id}")
    public AjaxResult getInfo(@PathVariable("id") Long id)
    {
        return success(officialModelService.selectOfficialModelById(id));
    }

    /**
     * 新增官方模型
     */
    @ApiOperation(value = "新增官方模型")
//    @PreAuthorize("@ss.hasPermi('graphprocesser:officialmodel:add')")
    @Log(title = "官方模型", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody OfficialModel officialModel)
    {
        return toAjax(officialModelService.insertOfficialModel(officialModel));
    }

    /**
     * 修改官方模型
     */
    @ApiOperation(value = "修改官方模型")
//    @PreAuthorize("@ss.hasPermi('graphprocesser:officialmodel:edit')")
    @Log(title = "官方模型", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody OfficialModel officialModel)
    {
        return toAjax(officialModelService.updateOfficialModel(officialModel));
    }

    /**
     * 删除官方模型
     */
    @ApiOperation(value = "删除官方模型")
//    @PreAuthorize("@ss.hasPermi('graphprocesser:officialmodel:remove')")
    @Log(title = "官方模型", businessType = BusinessType.DELETE)
	@DeleteMapping("/{ids}")
    public AjaxResult remove(@PathVariable Long[] ids)
    {
        return toAjax(officialModelService.deleteOfficialModelByIds(ids));
    }
}
