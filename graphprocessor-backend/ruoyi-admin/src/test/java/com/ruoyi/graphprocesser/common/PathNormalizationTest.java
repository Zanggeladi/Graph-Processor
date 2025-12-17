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
public class PathNormalizationTest {
    public static String resolvePath(String mixedPath) {
        // 解析并规范化路径
        Path path = Paths.get(mixedPath).normalize();
        return path.toString();
    }

    @DisplayName("文件路径规范化测试")
    @Test
    public void main() {
        String mixedPath = "E:\\code_gnn\\root_graphprocesser\\upload/upload/2024/11/20/cora_data_20241120193347A002.pkl";
        String resolvedPath = resolvePath(mixedPath);

        System.out.println("解析后的路径: " + resolvedPath);
    }

//    @DisplayName("CPP编译运行测试")
//    @Test
//    public void test() throws Exception {
//        String output = runCppFile();
//        System.out.println("输出结果: " + output);
//    }
}
