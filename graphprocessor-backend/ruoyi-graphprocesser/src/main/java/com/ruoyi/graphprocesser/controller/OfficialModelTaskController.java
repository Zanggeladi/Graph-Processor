package com.ruoyi.graphprocesser.controller;

import java.util.List;
import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.ruoyi.common.constant.HttpStatus;
import com.ruoyi.common.exception.ServiceException;
import com.ruoyi.common.manager.RedisLimiterManager;
import com.ruoyi.common.utils.SecurityUtils;
import com.ruoyi.graphprocesser.bizmq.ProcessorMessageProducer;
import com.ruoyi.graphprocesser.model.dto.officialmodeltask.OfficialModelTaskAddRequest;
import com.ruoyi.graphprocesser.model.enums.TaskStatusEnum;
import com.ruoyi.system.service.ISysUserService;
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
import com.ruoyi.graphprocesser.domain.OfficialModelTask;
import com.ruoyi.graphprocesser.service.IOfficialModelTaskService;
import com.ruoyi.common.utils.poi.ExcelUtil;
import com.ruoyi.common.core.page.TableDataInfo;

/**
 * 官方模型实验任务Controller
 * 
 * @author zgs
 * @date 2024-10-17
 */
@Api(tags = "官方模型实验任务Controller")
@RestController
@RequestMapping("/graphprocesser/officialmodeltask")
public class OfficialModelTaskController extends BaseController
{
    @Autowired
    private IOfficialModelTaskService officialModelTaskService;

    @Autowired
    private ISysUserService userService;

    @Resource
    private RedisLimiterManager redisLimiterManager;

    @Resource
    private ProcessorMessageProducer processorMessageProducer;

    /**
     * 获取任务状态
     */
    @ApiOperation(value = "获取任务状态")
    @GetMapping("/status/{id}")
    public AjaxResult getTaskStatus(@PathVariable("id") Long id)
    {
        OfficialModelTask task = officialModelTaskService.selectOfficialModelTaskById(id);
        if (task == null) {
            return error("任务未找到");
        }
        return success(task.getStatus());
    }

    /**
     * 提交新的官方模型实验任务到消息队列
     */
    @ApiOperation(value = "提交新的官方模型实验任务到消息队列")
//    @PreAuthorize("@ss.hasPermi('graphprocesser:officialmodeltask:add')")
    @Log(title = "提交官方模型实验任务", businessType = BusinessType.INSERT)
    @PostMapping("/submit")
    public AjaxResult submit(@RequestBody OfficialModelTaskAddRequest officialModelTaskAddRequest) throws JsonProcessingException {
        System.out.println("***************************************officialModelTaskAddRequest = " + officialModelTaskAddRequest);

        Long officialModelId = officialModelTaskAddRequest.getOfficialModelId();
        Integer datasetType = officialModelTaskAddRequest.getDatasetType();
        String datasetName = officialModelTaskAddRequest.getDatasetName();
        String datasetPath = officialModelTaskAddRequest.getDatasetPath();
        String parameters = officialModelTaskAddRequest.getParameters();

        logger.info("officialModelId:{}, datasetType:{}, datasetName:{}, datasetPath:{}, parameters:{}", officialModelId, datasetType, datasetName, datasetPath, parameters);

        // 校验参数
//        if (officialModelId == null || datasetType == null) {
//            throw new ServiceException("参数错误", HttpStatus.BAD_REQUEST);
//        }

//        // 参数校验
//        if (officialModelId == null || datasetType == null || datasetName == null || datasetPath == null) {
//            throw new ServiceException("参数错误", HttpStatus.BAD_REQUEST);
//        }

        // 获取发起请求的用户信息 使用userService
        Long userId = SecurityUtils.getUserId();
        //限流判断，每个用户一个限流器
        redisLimiterManager.doRateLimit("processTaskByPy_" + String.valueOf(userId));

        // 将任务插入数据库
        OfficialModelTask officialModelTask = new OfficialModelTask();
        officialModelTask.setOfficialModelId(officialModelId);
        officialModelTask.setDatasetType(datasetType);
        officialModelTask.setDatasetName(datasetName);
        officialModelTask.setDatasetPath(datasetPath);
        ObjectMapper objectMapper = new ObjectMapper();
        String parametersJson = objectMapper.writeValueAsString(officialModelTaskAddRequest.getParameters());
        officialModelTask.setParameters(parametersJson);
        officialModelTask.setUserId(userId);
        officialModelTask.setStatus(Long.valueOf(TaskStatusEnum.PENDING.getValue()));

        int saveResult = officialModelTaskService.insertOfficialModelTask(officialModelTask);
        if(saveResult != 1) {
            throw new ServiceException("任务插入数据库失败", HttpStatus.ERROR);
        }

        //将任务加入消息队列
        Long newTaskId = officialModelTask.getId();
        processorMessageProducer.sendMessage(String.valueOf(newTaskId));

        return success(newTaskId);
    }

    /**
     * 查询官方模型实验任务列表
     */
    @ApiOperation(value = "查询官方模型实验任务列表")
//    @PreAuthorize("@ss.hasPermi('graphprocesser:officialmodeltask:list')")
    @GetMapping("/list/my")
    public TableDataInfo listMy(OfficialModelTask officialModelTask)
    {
        Long userId = getUserId();
        officialModelTask.setUserId(userId);
        startPage();
        List<OfficialModelTask> list = officialModelTaskService.selectOfficialModelTaskList(officialModelTask);
        return getDataTable(list);
    }

    /**
     * 查询官方模型实验任务列表
     */
    @ApiOperation(value = "查询官方模型实验任务列表")
//    @PreAuthorize("@ss.hasPermi('graphprocesser:officialmodeltask:list')")
    @GetMapping("/list")
    public TableDataInfo list(OfficialModelTask officialModelTask)
    {
        startPage();
        List<OfficialModelTask> list = officialModelTaskService.selectOfficialModelTaskList(officialModelTask);
        return getDataTable(list);
    }

    /**
     * 导出官方模型实验任务列表
     */
    @ApiOperation(value = "导出官方模型实验任务列表")
//    @PreAuthorize("@ss.hasPermi('graphprocesser:officialmodeltask:export')")
    @Log(title = "官方模型实验任务", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, OfficialModelTask officialModelTask)
    {
        List<OfficialModelTask> list = officialModelTaskService.selectOfficialModelTaskList(officialModelTask);
        ExcelUtil<OfficialModelTask> util = new ExcelUtil<OfficialModelTask>(OfficialModelTask.class);
        util.exportExcel(response, list, "官方模型实验任务数据");
    }

    /**
     * 获取官方模型实验任务详细信息
     */
    @ApiOperation(value = "获取官方模型实验任务详细信息")
//    @PreAuthorize("@ss.hasPermi('graphprocesser:officialmodeltask:query')")
    @GetMapping(value = "/{id}")
    public AjaxResult getInfo(@PathVariable("id") Long id)
    {
        return success(officialModelTaskService.selectOfficialModelTaskById(id));
    }

    /**
     * 新增官方模型实验任务
     */
    @ApiOperation(value = "新增官方模型实验任务")
//    @PreAuthorize("@ss.hasPermi('graphprocesser:officialmodeltask:add')")
    @Log(title = "官方模型实验任务", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody OfficialModelTask officialModelTask)
    {
        System.out.println("officialModelTask = " + officialModelTask);
        return toAjax(officialModelTaskService.insertOfficialModelTask(officialModelTask));
    }

    /**
     * 修改官方模型实验任务
     */
    @ApiOperation(value = "修改官方模型实验任务")
//    @PreAuthorize("@ss.hasPermi('graphprocesser:officialmodeltask:edit')")
    @Log(title = "官方模型实验任务", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody OfficialModelTask officialModelTask)
    {
        return toAjax(officialModelTaskService.updateOfficialModelTask(officialModelTask));
    }

    /**
     * 删除官方模型实验任务
     */
    @ApiOperation(value = "删除官方模型实验任务")
//    @PreAuthorize("@ss.hasPermi('graphprocesser:officialmodeltask:remove')")
    @Log(title = "官方模型实验任务", businessType = BusinessType.DELETE)
	@DeleteMapping("/{ids}")
    public AjaxResult remove(@PathVariable Long[] ids)
    {
        return toAjax(officialModelTaskService.deleteOfficialModelTaskByIds(ids));
    }
}
