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
import com.ruoyi.graphprocesser.domain.Model;
import com.ruoyi.graphprocesser.service.IModelService;
import com.ruoyi.common.utils.poi.ExcelUtil;
import com.ruoyi.common.core.page.TableDataInfo;

/**
 * 模型Controller
 * 
 * @author zgs
 * @date 2024-10-26
 */
@Api(tags = "模型Controller")
@RestController
@RequestMapping("/graphprocesser/model")
public class ModelController extends BaseController
{
    @Autowired
    private IModelService modelService;

    /**
     * 获取当前用户的所有模型
     */
    @ApiOperation(value = "获取当前用户的所有模型")
    @GetMapping("/listMy")
    public AjaxResult listMyModel()
    {
        Long userId = getUserId();
        if (userId == null){
            return warn("用户未登录");
        }
        Model model = new Model();
        model.setUserId(userId);
        return success(modelService.selectModelList(model));
    }

    /**
     * 查询模型列表
     */
    @ApiOperation(value = "查询模型列表")
    @PreAuthorize("@ss.hasPermi('graphprocesser:model:list')")
    @GetMapping("/list")
    public TableDataInfo list(Model model)
    {
        startPage();
        List<Model> list = modelService.selectModelList(model);
        return getDataTable(list);
    }

    /**
     * 导出模型列表
     */
    @ApiOperation(value = "导出模型列表")
    @PreAuthorize("@ss.hasPermi('graphprocesser:model:export')")
    @Log(title = "模型", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, Model model)
    {
        List<Model> list = modelService.selectModelList(model);
        ExcelUtil<Model> util = new ExcelUtil<Model>(Model.class);
        util.exportExcel(response, list, "模型数据");
    }

    /**
     * 获取模型详细信息
     */
    @ApiOperation(value = "获取模型详细信息")
    @PreAuthorize("@ss.hasPermi('graphprocesser:model:query')")
    @GetMapping(value = "/{id}")
    public AjaxResult getInfo(@PathVariable("id") Long id)
    {
        return success(modelService.selectModelById(id));
    }

    /**
     * 新增模型
     */
    @ApiOperation(value = "新增模型")
    @PreAuthorize("@ss.hasPermi('graphprocesser:model:add')")
    @Log(title = "模型", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody Model model)
    {
        return toAjax(modelService.insertModel(model));
    }

    /**
     * 修改模型
     */
    @ApiOperation(value = "修改模型")
    @PreAuthorize("@ss.hasPermi('graphprocesser:model:edit')")
    @Log(title = "模型", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody Model model)
    {
        return toAjax(modelService.updateModel(model));
    }

    /**
     * 删除模型
     */
    @ApiOperation(value = "删除模型")
    @PreAuthorize("@ss.hasPermi('graphprocesser:model:remove')")
    @Log(title = "模型", businessType = BusinessType.DELETE)
	@DeleteMapping("/{ids}")
    public AjaxResult remove(@PathVariable Long[] ids)
    {
        return toAjax(modelService.deleteModelByIds(ids));
    }
}
