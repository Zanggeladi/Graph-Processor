package com.ruoyi.graphprocesser.common;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

@SpringBootTest
@DisplayName("单元测试Cpp")
public class CppTest {

    // 编译并运行C++文件的方法
    public String runCppFile() throws Exception {
        // 获取资源目录路径
        Path cppPath = Paths.get("C:\\Users\\zgs\\Desktop\\LSketch\\query_NoW.cpp");
        Path outputPath = cppPath.getParent().resolve("query_NoW");

        System.out.println("cppPath = " + cppPath);
        System.out.println("outputPath = " + outputPath);

        // 编译命令（Windows示例）
        List<String> compileCommand = new ArrayList<>();
        compileCommand.add("g++");
        compileCommand.add("-o");
        compileCommand.add(outputPath.toString());
        compileCommand.add(cppPath.toString());

        // 运行命令
        List<String> runCommand = new ArrayList<>();
        runCommand.add(outputPath.toString());

        // 执行编译命令
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
                return "编译失败，错误信息：" + errorMsg;
            }
        } finally {
            compileProcess.destroy();
        }

        // 执行运行命令
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
            if (exitCode != 0) {
                System.out.println("运行失败，错误代码：" + exitCode);
                return "运行失败，错误代码：" + exitCode;
            }
        } finally {
            runProcess.destroy();
        }

        // 返回C++程序的输出
        return execMessage.toString();
    }

    @DisplayName("CPP编译运行测试")
    @Test
    public void test() throws Exception {
        String output = runCppFile();
        System.out.println("输出结果: " + output);
    }
}
