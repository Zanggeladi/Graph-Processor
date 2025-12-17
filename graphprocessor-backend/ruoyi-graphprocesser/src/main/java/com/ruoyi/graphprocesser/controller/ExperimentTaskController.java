package com.ruoyi.graphprocesser.controller;

import java.io.IOException;
import java.util.List;
import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.qcloud.cos.model.COSObject;
import com.qcloud.cos.model.COSObjectInputStream;
import com.qcloud.cos.utils.IOUtils;
import com.ruoyi.common.constant.HttpStatus;
import com.ruoyi.common.exception.ServiceException;
import com.ruoyi.common.manager.CosManager;
import com.ruoyi.common.manager.RedisLimiterManager;
import com.ruoyi.common.utils.SecurityUtils;
import com.ruoyi.graphprocesser.bizmq.ExperimentMessageProducer;
import com.ruoyi.graphprocesser.bizmq.ProcessorMessageProducer;
import com.ruoyi.graphprocesser.domain.OfficialModelTask;
import com.ruoyi.graphprocesser.model.dto.experimenttask.ExperimentTaskAddRequest;
import com.ruoyi.graphprocesser.model.dto.officialmodeltask.OfficialModelTaskAddRequest;
import com.ruoyi.graphprocesser.model.enums.TaskStatusEnum;
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
import com.ruoyi.graphprocesser.domain.ExperimentTask;
import com.ruoyi.graphprocesser.service.IExperimentTaskService;
import com.ruoyi.common.utils.poi.ExcelUtil;
import com.ruoyi.common.core.page.TableDataInfo;

/**
 * 对比实验任务Controller
 * 
 * @author zgs
 * @date 2024-10-27
 */
@Api(tags = "对比实验任务Controller")
@RestController
@RequestMapping("/graphprocesser/experimenttask")
public class ExperimentTaskController extends BaseController
{
    @Autowired
    private IExperimentTaskService experimentTaskService;

    @Resource
    private RedisLimiterManager redisLimiterManager;

    @Resource
    private ExperimentMessageProducer experimentMessageProducer;

    @Resource
    private CosManager cosManager;

    /**
     * 提交新的官方模型实验任务到消息队列
     */
    @ApiOperation(value = "提交新的对比实验任务到消息队列")
    @PreAuthorize("@ss.hasPermi('graphprocesser:experimenttask:add')")
    @Log(title = "提交图对比实验任务", businessType = BusinessType.INSERT)
    @PostMapping("/submit")
    public AjaxResult submit(@RequestBody ExperimentTaskAddRequest experimentTaskAddRequest) throws JsonProcessingException {
        System.out.println("***************************************experimentTaskAddRequest = " + experimentTaskAddRequest);

        String taskName = experimentTaskAddRequest.getTaskName();
        Long datasetId = experimentTaskAddRequest.getDatasetId();
        String modelCode = experimentTaskAddRequest.getModelCode();
        String parameters = experimentTaskAddRequest.getParameters();
        Long themeId = experimentTaskAddRequest.getThemeId();
        Long userId = getUserId();


        logger.info("taskName:{}, datasetId:{}, modelCode:{}, parameters:{}, themeId:{}, userId:{}", taskName, datasetId, modelCode, parameters, themeId, userId);

        //限流判断，每个用户一个限流器
        redisLimiterManager.doRateLimit("processTaskByPy_" + String.valueOf(userId));

        // 将任务插入数据库
        ExperimentTask experimentTask = new ExperimentTask();
        experimentTask.setStatus(Long.valueOf(TaskStatusEnum.PENDING.getValue()));
        experimentTask.setTaskName(taskName);
        experimentTask.setDatasetId(datasetId);
        experimentTask.setModeCode(modelCode);

        ObjectMapper objectMapper = new ObjectMapper();
        String parametersJson = objectMapper.writeValueAsString(parameters);
        experimentTask.setHyperparameters(parametersJson);

        experimentTask.setUserId(userId);
        experimentTask.setThemeId(themeId);



        int saveResult = experimentTaskService.insertExperimentTask(experimentTask);
        if(saveResult != 1) {
            throw new ServiceException("任务插入数据库失败", HttpStatus.ERROR);
        }

        //将任务加入消息队列
        Long newTaskId = experimentTask.getId();
        experimentMessageProducer.sendMessage(String.valueOf(newTaskId));

        return success(newTaskId);
    }

    //查询某个主题对应的所有任务的列表
    @ApiOperation(value = "查询某个主题对应的所有任务的列表")
    @GetMapping("/list/{themeId}")
    public TableDataInfo listExperimentTaskByTheme(@PathVariable("themeId") Long themeId)
    {
        startPage();
        ExperimentTask experimentTask = new ExperimentTask();
        experimentTask.setThemeId(themeId);
        List<ExperimentTask> list = experimentTaskService.selectExperimentTaskList(experimentTask);
        return getDataTable(list);
    }

    private void downloadFileFromCOS(String filepath, HttpServletResponse response) throws IOException {
        COSObjectInputStream cosObjectInput = null;
        try {
            COSObject cosObject = cosManager.getObject(filepath);
            cosObjectInput = cosObject.getObjectContent();
            //处理下载到的流
            byte[] bytes = IOUtils.toByteArray(cosObjectInput);
            //设置响应头
            response.setContentType("application/octet-stream;charset=UTF-8");
            response.setHeader("Content-Disposition", "attachment; filename=" + filepath);
            System.out.println("filepath = " + filepath);
            //写入响应
            response.getOutputStream().write(bytes);
            response.getOutputStream().flush();
        } catch (Exception e) {
            logger.error("file download error, filepath = " + filepath, e);
            throw new ServiceException("文件下载失败", HttpStatus.ERROR);
//            throw new BusinessException(ErrorCode.SYSTEM_ERROR, "下载失败");
        } finally {
            if (cosObjectInput != null) {
                cosObjectInput.close();
            }
        }
    }

    /**
     * 查询对比实验任务列表
     */
    @ApiOperation(value = "查询对比实验任务列表")
    @PreAuthorize("@ss.hasPermi('graphprocesser:experimenttask:list')")
    @GetMapping("/list")
    public TableDataInfo list(ExperimentTask experimentTask)
    {
        startPage();
        List<ExperimentTask> list = experimentTaskService.selectExperimentTaskList(experimentTask);
        return getDataTable(list);
    }

    /**
     * 导出对比实验任务列表
     */
    @ApiOperation(value = "导出对比实验任务列表")
    @PreAuthorize("@ss.hasPermi('graphprocesser:experimenttask:export')")
    @Log(title = "对比实验任务", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, ExperimentTask experimentTask)
    {
        List<ExperimentTask> list = experimentTaskService.selectExperimentTaskList(experimentTask);
        ExcelUtil<ExperimentTask> util = new ExcelUtil<ExperimentTask>(ExperimentTask.class);
        util.exportExcel(response, list, "对比实验任务数据");
    }

    /**
     * 获取对比实验任务详细信息
     */
    @ApiOperation(value = "获取对比实验任务详细信息")
    @PreAuthorize("@ss.hasPermi('graphprocesser:experimenttask:query')")
    @GetMapping(value = "/{id}")
    public AjaxResult getInfo(@PathVariable("id") Long id)
    {
        return success(experimentTaskService.selectExperimentTaskById(id));
    }

    /**
     * 新增对比实验任务
     */
    @ApiOperation(value = "新增对比实验任务")
    @PreAuthorize("@ss.hasPermi('graphprocesser:experimenttask:add')")
    @Log(title = "对比实验任务", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody ExperimentTask experimentTask)
    {
        return toAjax(experimentTaskService.insertExperimentTask(experimentTask));
    }

    /**
     * 修改对比实验任务
     */
    @ApiOperation(value = "修改对比实验任务")
    @PreAuthorize("@ss.hasPermi('graphprocesser:experimenttask:edit')")
    @Log(title = "对比实验任务", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody ExperimentTask experimentTask)
    {
        return toAjax(experimentTaskService.updateExperimentTask(experimentTask));
    }

    /**
     * 删除对比实验任务
     */
    @ApiOperation(value = "删除对比实验任务")
    @PreAuthorize("@ss.hasPermi('graphprocesser:experimenttask:remove')")
    @Log(title = "对比实验任务", businessType = BusinessType.DELETE)
	@DeleteMapping("/{ids}")
    public AjaxResult remove(@PathVariable Long[] ids)
    {
        return toAjax(experimentTaskService.deleteExperimentTaskByIds(ids));
    }
}
