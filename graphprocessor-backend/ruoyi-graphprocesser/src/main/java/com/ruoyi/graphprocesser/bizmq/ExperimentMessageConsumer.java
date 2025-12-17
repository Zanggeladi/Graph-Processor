package com.ruoyi.graphprocesser.bizmq;

import com.rabbitmq.client.Channel;
import com.ruoyi.common.constant.HttpStatus;
import com.ruoyi.common.constant.PathConstant;
import com.ruoyi.common.core.domain.entity.SysUser;
import com.ruoyi.common.core.domain.model.LoginUser;
import com.ruoyi.common.exception.ServiceException;
import com.ruoyi.common.manager.CosManager;
import com.ruoyi.common.utils.MailUtils;
import com.ruoyi.common.utils.SecurityUtils;
import com.ruoyi.graphprocesser.domain.Dataset;
import com.ruoyi.graphprocesser.domain.ExperimentResult;
import com.ruoyi.graphprocesser.domain.ExperimentTask;
import com.ruoyi.graphprocesser.model.enums.TaskStatusEnum;
import com.ruoyi.graphprocesser.service.IDatasetService;
import com.ruoyi.graphprocesser.service.IExperimentResultService;
import com.ruoyi.graphprocesser.service.IExperimentTaskService;
import com.ruoyi.system.service.ISysUserService;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.support.AmqpHeaders;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

//import static com.dbis.springbootinit.constant.CommonConstant.DATASET_STORAGE_PATH;

@Component
@Slf4j
public class ExperimentMessageConsumer {

    protected final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Resource
    private IExperimentTaskService experimentService;

    @Resource
    private IDatasetService datasetService;

    @Resource
    private CosManager cosManager;

    @Resource
    private IExperimentResultService resultService;

    @Resource
    private ISysUserService userService;

//    @Value("${codesandbox.path}") // 从application.yml文件中读取
//    private String basePath;

    @Resource
    private PathConstant pathConstant;


    // 指定程序监听的消息队列和确认机制(手动确认)
    @SneakyThrows
    @RabbitListener(queues = {MqConstant.EXPERIMENT_QUEUE_NAME}, ackMode = "MANUAL")
    public void receiveMessage(String message, Channel channel, @Header(AmqpHeaders.DELIVERY_TAG) long deliveryTag) {
        // 1.获取图对比实验任务信息
        log.info("MQ receiveMessage message = {}", message);
        if (StringUtils.isBlank(message)) {
            // 如果失败，消息拒绝
            channel.basicNack(deliveryTag, false, false);
            throw new ServiceException("消息为空", HttpStatus.ERROR);
        }
        long taskId = Long.parseLong(message);
        ExperimentTask experiment = experimentService.selectExperimentTaskById(taskId);
        if (experiment == null) {
            channel.basicNack(deliveryTag, false, false);
            throw new ServiceException("图对比实验任务为空", HttpStatus.ERROR);
        }
        System.out.println("experiment = " + experiment);

        Long userId = experiment.getUserId();
        String code = "import os\nos.chdir(os.path.dirname(os.path.abspath(__file__)))\n" + experiment.getModeCode();
        Long datasetId = experiment.getDatasetId();
        Dataset dataset = datasetService.selectDatasetById(datasetId);
        if (dataset == null) {
            channel.basicNack(deliveryTag, false, false);
            throw new ServiceException("图数据集为空", HttpStatus.ERROR);
        }
        String datasetPath = dataset.getDatasetPath();
        System.out.println("datasetPath = " + datasetPath);

        // 2.处理代码和图数据集文件路径
        Path experimentFolderPath = Paths.get(String.valueOf(pathConstant.CODESANDBOX_PATH()), String.valueOf(taskId));
        Path codeFilePath = experimentFolderPath.resolve("train.py");

        try {
            // 如果用户文件夹不存在，则创建它
            if (!Files.exists(experimentFolderPath)) {
                Files.createDirectories(experimentFolderPath);
            }
            // 写入Python代码到文件
            Files.write(codeFilePath, code.getBytes());
            System.out.println("Python代码已写入文件：" + codeFilePath);
        } catch (IOException e) {
            System.out.println("写入文件时出错：" + e.getMessage());
        }

        Path sourcePath = Paths.get(datasetPath);
        System.out.println("sourcePath = " + sourcePath);
//        Path targetDirectory = experimentFolderPath;

        try {
            // 将源文件复制到目标文件夹中
            Path targetPath = experimentFolderPath.resolve(sourcePath.getFileName());
            System.out.println("targetPath = " + targetPath);
            Files.copy(sourcePath, targetPath);
            System.out.println("图数据集文件复制成功到目标文件夹！");
        } catch (IOException e) {
            System.out.println("图数据集文件复制失败：" + e.getMessage());
        }


        // 3.修改任务状态    先修改任务状态为 “执行中”。等执行成功后，修改为 “已完成”、保存执行结果；执行失败后，状态修改为 “失败”，记录任务失败信息。
        ExperimentTask updateExperiment = new ExperimentTask();
        updateExperiment.setId(taskId);
        updateExperiment.setStatus(Long.valueOf(TaskStatusEnum.RUNNING.getValue()));
        int b = experimentService.updateExperimentTask(updateExperiment);
        if (b == 0) {
            log.error("experiment taskId:" + taskId + ",更新 running 状态失败");
        }


        // 4.构建进程并执行
        // 定义命令和参数
        List<String> command = new ArrayList<>();

//        command.add(CommonConstant.CONDA_PYTHON_PATH);
        command.add(String.valueOf(pathConstant.CONDA_PYTHON_PATH()));
        command.add(String.valueOf(codeFilePath));

        command.add("--data_path");
        command.add("E:\\code_gnn\\testGNN\\cora_data.pkl");

        command.add("--output_curve_path");
        command.add("./curve.png");

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


        // 5.执行完成保存result
        if (exitCode == 0) {

            // 构造路径
            StringBuilder filepath = new StringBuilder();
            filepath.append(String.format("/%s", userId))
                    .append(String.format("/%s", taskId))
                    .append(String.format("/%s", "curve.png"));

            Path localCurvePath = experimentFolderPath.resolve("curve.png");

            cosManager.putObject(String.valueOf(filepath), String.valueOf(localCurvePath));
            //将中间向量结果输出到数据库
            ExperimentResult result = new ExperimentResult();
            result.setTaskId(taskId);
            result.setResultFilePath(String.valueOf(filepath));
            resultService.insertExperimentResult(result);


            ExperimentTask updateExperimentResult = new ExperimentTask();
            updateExperimentResult.setId(taskId);
//            updateExperimentResult.setResultPath(String.valueOf(filepath));
            updateExperimentResult.setStatus(Long.valueOf(TaskStatusEnum.COMPLETED.getValue()));
//            updateExperimentResult.setExecMessage(String.valueOf(pyExecMessage));
            int updateResult = experimentService.updateExperimentTask(updateExperimentResult);
            if (updateResult == 0) {
                log.error("experiment taskId:" + taskId + ",更新 completed 状态失败");
            }


            // 消息确认
            channel.basicAck(deliveryTag, false);
            // 日志 + 通知
            log.info("experiment taskId:" + taskId + ", 执行成功");
            handleExperimentTaskSuccess(userId, taskId, pyExecMessage.toString());
        } else {
            //设置执行信息execMessage：错误信息
            System.out.println("exitCode = " + exitCode);
            ExperimentTask updateExperimentTask = new ExperimentTask();
            updateExperimentTask.setId(taskId);
            updateExperimentTask.setStatus(Long.valueOf(TaskStatusEnum.FAILED.getValue()));
            updateExperimentTask.setExecMessage("任务脚本执行失败，exit with:\n" + pyExecMessage);
            int updateResult = experimentService.updateExperimentTask(updateExperimentTask);
            if (updateResult == 0) {
                log.error("experiment taskId:" + taskId + ",更新 failed 状态失败");
            }

            // 此处添加失败处理逻辑
            try {
                // 1. 消息拒绝或重新入队
                // 若希望消息重新入队以便稍后重试，可以将 requeue 设置为 true
                channel.basicNack(deliveryTag, false, false);

                // 2. 记录日志
                log.error("任务执行失败，任务ID：" + taskId + "，退出代码：" + exitCode + "，错误信息：" + pyExecMessage.toString());

                // 3. 发送告警通知
                handleExperimentTaskFailed(userId, taskId, pyExecMessage.toString());

            } catch (IOException ioException) {
                log.error("消息拒绝或重新入队失败", ioException);
            } finally {
                // 4. 资源清理（如果需要）
                // 如删除临时文件、释放资源等
                try {
                    Files.deleteIfExists(codeFilePath);
                } catch (IOException e) {
                    log.error("无法删除代码文件：" + codeFilePath, e);
                }
            }
        }
    }

    //成功的通知
    private void handleExperimentTaskSuccess(long userId, long taskId, String execMessage) {
        // 发送通知
        handleExperimentTaskNotification(
                userId,
                "图对比实验任务执行成功",
                "任务ID：" + taskId + " 执行成功，执行信息：" + execMessage
        );
    }

    private void handleExperimentTaskFailed(long userId, long taskId, String execMessage) {
        // 发送通知
        handleExperimentTaskNotification(
                userId,
                "图对比实验任务失败告警",
                "任务ID：" + taskId + " 失败，错误信息：" + execMessage
        );
    }

    private void handleExperimentTaskNotification(long userId, String subject, String message) {
        SysUser userInfo = userService.selectUserById(userId);
        String email = userInfo.getEmail();
        if(ObjectUtils.isNotEmpty(email)){
            MailUtils.sendText(email, subject, message);
        }else{
            log.warn("用户ID" + userId + "邮箱为空，无法发送邮件");
        }
    }
}
