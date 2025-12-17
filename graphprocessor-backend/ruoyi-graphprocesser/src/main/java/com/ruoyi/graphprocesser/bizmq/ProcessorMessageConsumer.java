package com.ruoyi.graphprocesser.bizmq;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import com.rabbitmq.client.Channel;
import com.ruoyi.common.constant.HttpStatus;
import com.ruoyi.common.constant.PathConstant;
import com.ruoyi.common.exception.ServiceException;
import com.ruoyi.common.utils.file.FileUtils;
import com.ruoyi.graphprocesser.domain.OfficialModel;
import com.ruoyi.graphprocesser.domain.OfficialModelTask;
import com.ruoyi.graphprocesser.domain.OfficialModelTaskResult;
import com.ruoyi.graphprocesser.model.enums.TaskStatusEnum;
import com.ruoyi.graphprocesser.service.IOfficialModelService;
import com.ruoyi.graphprocesser.service.IOfficialModelTaskResultService;
import com.ruoyi.graphprocesser.service.IOfficialModelTaskService;
import com.ruoyi.common.manager.CosManager;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.support.AmqpHeaders;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.stereotype.Component;


import javax.annotation.Resource;
import java.io.BufferedReader;
import java.io.File;
import java.io.InputStreamReader;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@Component
@Slf4j
public class ProcessorMessageConsumer {

    @Resource
    private IOfficialModelTaskService taskService;

    @Resource
    private CosManager cosManager;

    @Resource
    private IOfficialModelTaskResultService resultService;

    @Resource
    private PathConstant pathConstant;

    @Resource
    private IOfficialModelService officialModelService;

    // 指定程序监听的消息队列和确认机制(手动确认)
    @SneakyThrows
    @RabbitListener(queues = {MqConstant.PROCESSOR_QUEUE_NAME}, ackMode = "MANUAL")
    public void receiveMessage(String message, Channel channel, @Header(AmqpHeaders.DELIVERY_TAG) long deliveryTag) {

        // 1.获取消息，从消息中读出任务ID
//        log.info("________________________________________________MQ receiveMessage message = {}", message);
        if (StringUtils.isBlank(message)) {
            // 如果失败，消息拒绝
            channel.basicNack(deliveryTag, false, false);
            throw new ServiceException("消息为空", HttpStatus.ERROR);
        }
        long taskId = Long.parseLong(message);
        OfficialModelTask task = taskService.selectOfficialModelTaskById(taskId);
        if (task == null) {
            channel.basicNack(deliveryTag, false, false);
            throw new ServiceException("数据处理任务为空", HttpStatus.ERROR);
        }
        log.info("Current task : {}", task);
//        System.out.println("task = " + task);

        // 2. 获取任务信息
        Long officialModelId = task.getOfficialModelId();
        Integer datasetType = task.getDatasetType();
        String datasetName = task.getDatasetName();
        String datasetPath = task.getDatasetPath();
        String parameters = task.getParameters();
        Long status = task.getStatus();
        Long userId = task.getUserId();
        //部分参数预处理
        if (StringUtils.isNotBlank(parameters)) {
            parameters = parameters.replace("\\", "");
            parameters = parameters.substring(1, parameters.length() - 1);
            System.out.println("parameters = " + parameters);
        }
        if (StringUtils.isNotBlank(datasetPath)) {
            datasetPath = FileUtils.getAbsolutePath(datasetPath);
            System.out.println("datasetPath = " + datasetPath);
        }


//        /**
//         * 测试json解析
//         */
//        System.out.println("------------------------------------------------------parameters = " + parameters);
//        //处理parameters，将\去掉，并去掉两头的"
//        parameters = parameters.replace("\\", "");
//        parameters = parameters.substring(1, parameters.length() - 1);
//        System.out.println("------------------------------------------------------parameters = " + parameters);
//
//        String jsonString = "{\"learning_rate\":\"1\",\"epochs\":\"222\",\"batch_size\":\"64\"}";
//        System.out.println("jsonString = " + jsonString);
//
//        ObjectMapper objectMapper = new ObjectMapper();
//        try {
//            Map<String, String> configMap = objectMapper.readValue(parameters, Map.class);
//            configMap.forEach((key, value) -> System.out.println(key + ": " + value));
//        } catch (Exception e) {
//            e.printStackTrace();
//        }

        // 3. 获取模型信息
        OfficialModel officialModel = officialModelService.selectOfficialModelById(officialModelId);
        //检查模型是否存在
        if (officialModel == null){
            channel.basicNack(deliveryTag, false, false);
            throw new ServiceException("模型不存在", HttpStatus.ERROR);
        }
        String codePath = officialModel.getCodePath();
        if (StringUtils.isBlank(codePath)) {
            channel.basicNack(deliveryTag, false, false);
            throw new ServiceException("模型代码为空", HttpStatus.ERROR);
        }
        Integer type = officialModel.getType();
        String subtask = officialModel.getSubtask();

        // 4. 更新任务状态为 “执行中”
        // 先修改图表任务状态为 “执行中”。等执行成功后，修改为 “已完成”、保存执行结果；执行失败后，状态修改为 “失败”，记录任务失败信息。
        OfficialModelTask updateTask = new OfficialModelTask();
        updateTask.setId(taskId);
        updateTask.setStatus(Long.valueOf(TaskStatusEnum.RUNNING.getValue()));
//        System.out.println("______________________________updateTask = " + updateTask);

        int b = taskService.updateOfficialModelTask(updateTask);
        if (b == 0) {
            handleTaskUpdateError(task.getId(), "更新任务 执行中 状态失败");
            return;
        }

        String uniqueId = UUID.randomUUID().toString();

        // 5. 执行任务，流模式与非流模式区分开
        // 定义命令和参数

        // 流模式
        if(type == 0){
            Path cppPath = Paths.get(codePath);
//            log.error(String.valueOf(cppPath.getParent()));
//            log.error(String.valueOf(cppPath.getParent().getParent()));
//            log.error(String.valueOf(cppPath.getParent().getParent().resolve(File.separator)));
//            log.error(String.valueOf(cppPath.getParent().getParent().resolve("output" + uniqueId)));
            Path outputPath = cppPath.getParent().getParent().resolve("subtaskexe").resolve("output" + uniqueId);
//            log.error(String.valueOf(cppPath));
//            log.error(String.valueOf(outputPath));
            // 1. 编译
            List<String> compileCommand = new ArrayList<>();
            compileCommand.add("g++");
            compileCommand.add("-o");
            compileCommand.add(outputPath.toString());
            compileCommand.add(cppPath.toString());
            log.info("执行命令：" + compileCommand);

            ProcessBuilder compileProcessBuilder = new ProcessBuilder(compileCommand);
            compileProcessBuilder.directory(cppPath.getParent().toFile());
            compileProcessBuilder.redirectErrorStream(true);
            int exitCode;
            Process compileProcess = compileProcessBuilder.start();
            try (BufferedReader errorReader = new BufferedReader(new InputStreamReader(compileProcess.getErrorStream()))) {
                exitCode = compileProcess.waitFor();
                if (exitCode != 0) {
                    StringBuilder errorMsg = new StringBuilder();
                    String errorLine;
                    while ((errorLine = errorReader.readLine()) != null) {
                        errorMsg.append(errorLine).append("\n");
                    }
                    System.out.println("编译失败，错误信息：" + errorMsg);
                    handleTaskUpdateError(task.getId(), "编译失败，错误信息：" + errorMsg);
                }
            } finally {
                compileProcess.destroy();
            }

            //2. 运行
            List<String> runCommand = new ArrayList<>();
            runCommand.add(outputPath.toString());
            runCommand.add(String.valueOf(datasetType));
            if(datasetType == 0){
                runCommand.add(datasetName);
            }else if (datasetType == 1){
                runCommand.add(datasetPath);
            }

            ObjectMapper objectMapper = new ObjectMapper();
            try {
                Map<String, String> configMap = objectMapper.readValue(parameters, Map.class);
                configMap.forEach((key, value) -> runCommand.add(value));
            } catch (Exception e) {
                e.printStackTrace();
            }

            log.info("执行命令：" + runCommand);
            ProcessBuilder runProcessBuilder = new ProcessBuilder(runCommand);
            runProcessBuilder.directory(outputPath.getParent().toFile());
            runProcessBuilder.redirectErrorStream(true);

            StringBuilder execMessage = new StringBuilder();
            Process runProcess = runProcessBuilder.start();
            try (BufferedReader outputReader = new BufferedReader(new InputStreamReader(runProcess.getInputStream()))) {
                String line;
                while ((line = outputReader.readLine()) != null) {
                    System.out.println(line); // 输出调试信息
                    execMessage.append(line).append('\n');
                }
                exitCode = runProcess.waitFor();
                if (exitCode == 0) {
                    // 更新result
                    OfficialModelTaskResult result = new OfficialModelTaskResult();
                    result.setTaskId(taskId);
                    result.setOutputMessage(execMessage.toString());
                    resultService.insertOfficialModelTaskResult(result);

                    OfficialModelTask updateTaskResult = new OfficialModelTask();
                    updateTaskResult.setId(taskId);
                    updateTaskResult.setResultId(result.getId());
                    updateTaskResult.setStatus(Long.valueOf(TaskStatusEnum.COMPLETED.getValue()));
                    updateTaskResult.setExecMessage(String.valueOf(execMessage));
                    int updateResultCount = taskService.updateOfficialModelTask(updateTaskResult);
                    if (updateResultCount == 0) {
                        handleTaskUpdateError(taskId, "更新图表成功状态失败");
                    }
                    // 消息确认
                    channel.basicAck(deliveryTag, false);
                }else{
                    //设置执行信息execMessage：错误信息
                    System.out.println("exitCode = " + exitCode);
                    OfficialModelTask updateTaskResult = new OfficialModelTask();
                    updateTaskResult.setId(taskId);
                    updateTaskResult.setStatus(Long.valueOf(TaskStatusEnum.FAILED.getValue()));
                    updateTaskResult.setExecMessage("任务脚本执行失败，exit with:\n" + execMessage);
                    int updateResult = taskService.updateOfficialModelTask(updateTaskResult);
                    if (updateResult == 0) {
                        handleTaskUpdateError(task.getId(), "更新任务状态为failing时失败");
                    }

                    System.out.println("运行失败，错误代码：" + exitCode);
                    handleTaskUpdateError(task.getId(), "运行失败，错误代码：" + exitCode);
                }
            } finally {
                runProcess.destroy();
            }
        }else{
            // 非流模式
            List<String> command = new ArrayList<>();
            command.add(String.valueOf(pathConstant.CONDA_PYTHON_PATH()));
            command.add(codePath);

            if(datasetType == 0){
                if (ObjectUtils.isNotEmpty(datasetName)) {
                    command.add("--dataset");
                    command.add(datasetName);
                }
            } else if (datasetType == 1) {
                if (ObjectUtils.isNotEmpty(datasetPath)) {
                    command.add("--data_path");
                    command.add(datasetPath);
                }
            }

//        String output_curve_path = "";
            // 生成唯一标识符作为文件名的一部分
//        String uniqueId = UUID.randomUUID().toString();
            //训练曲线的输出路径
//        String output_curve_path = CommonConstant.OUTPUT_FILE_PATH + "curve_" + uniqueId + ".png";
            String separator = File.separator;
            String output_curve_path = pathConstant.OUTPUT_FILE_PATH()+ separator + "curve_" + uniqueId + ".png";
            String output_tsne_path = pathConstant.OUTPUT_FILE_PATH()+ separator + "tsne_" + uniqueId + ".png";

            command.add("--output_curve_path");
            command.add(output_curve_path);

            command.add("--output_tsne_path");
            command.add(output_tsne_path);

            // 调用Python脚本处理文件
            ProcessBuilder processBuilder;
            processBuilder = new ProcessBuilder(command).redirectErrorStream(true);

            Process process = null;
            process = processBuilder.start();
            // 打印命令
            String commandString = String.join(" ", command);
            System.out.println("执行的命令行：" + commandString);
            // 读取进程的输出流
            StringBuilder pyExecMessage = new StringBuilder();
            BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
            String line;
            while ((line = reader.readLine()) != null) {
                pyExecMessage.append(line + '\n');
            }
            int exitCode = 0;
            exitCode = process.waitFor();
            if (exitCode == 0) {
                //上传图片到COS对象存储平台
                StringBuilder curvePath = new StringBuilder();
                curvePath.append("/output_files")
                        .append(String.format("/%s", userId))
                        .append(String.format("/%s", task.getId()))
                        .append(String.format("/%s", "curve_" + uniqueId + ".png"));
                StringBuilder tsnePath = new StringBuilder();
                tsnePath.append("/output_files")
                        .append(String.format("/%s", userId))
                        .append(String.format("/%s", task.getId()))
                        .append(String.format("/%s", "tsne_" + uniqueId + ".png"));

                cosManager.putObject(String.valueOf(curvePath), output_curve_path);
                cosManager.putObject(String.valueOf(tsnePath), output_tsne_path);
                //更新result
                OfficialModelTaskResult result = new OfficialModelTaskResult();
                result.setTaskId(taskId);
                result.setCurvePath(String.valueOf(curvePath));
                result.setTsnePath(String.valueOf(tsnePath));
                result.setOutputMessage(String.valueOf(pyExecMessage));
                resultService.insertOfficialModelTaskResult(result);

                //更新task
                OfficialModelTask updateTaskResult = new OfficialModelTask();
                updateTaskResult.setId(taskId);
                updateTaskResult.setResultId(result.getId());
                updateTaskResult.setStatus(Long.valueOf(TaskStatusEnum.COMPLETED.getValue()));
                updateTaskResult.setExecMessage(String.valueOf(pyExecMessage));
                int updateResultCount = taskService.updateOfficialModelTask(updateTaskResult);
                if (updateResultCount == 0) {
                    handleTaskUpdateError(taskId, "更新图表成功状态失败");
                }

                // 消息确认
                channel.basicAck(deliveryTag, false);
            } else {
                //设置执行信息execMessage：错误信息
                System.out.println("exitCode = " + exitCode);
                OfficialModelTask updateTaskResult = new OfficialModelTask();
                updateTaskResult.setId(taskId);
                updateTaskResult.setStatus(Long.valueOf(TaskStatusEnum.FAILED.getValue()));
                updateTaskResult.setExecMessage("任务脚本执行失败，exit with:\n" + pyExecMessage);
                int updateResult = taskService.updateOfficialModelTask(updateTaskResult);
                if (updateResult == 0) {
                    handleTaskUpdateError(task.getId(), "更新任务状态为failing时失败");
                }
            }
        }

    }

    private void handleTaskUpdateError(long taskId, String execMessage) {
        OfficialModelTask updateTaskResult = new OfficialModelTask();
        updateTaskResult.setId(taskId);
        updateTaskResult.setStatus(Long.valueOf(TaskStatusEnum.FAILED.getValue()));
//        updateTaskResult.setExecMessage("execMessage");
        int updateResult = taskService.updateOfficialModelTask(updateTaskResult);
        if (updateResult == 0) {
            log.error("更新图表失败状态失败" + taskId + "," + execMessage);
        }
    }
}
