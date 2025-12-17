package com.ruoyi.common.constant;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.File;
import java.nio.file.Path;
import java.nio.file.Paths;

@Component
public class PathConstant {

    @Value("${paths.root}")
    public String rootPath;

    @Value("${paths.output_files}")
    public String outputFilesPath;

    @Value("${paths.dataset_storage}")
    public String datasetStoragePath;

    @Value("${paths.codesandbox}")
    public String codesandboxPath;

//    @Value("${paths.gnn_models}")
//    public String gnnModelsPath;

    @Value("${paths.conda_python_exe}")
    public String condaPythonExePath;



    String separator = File.separator;

    public Path ROOT_PATH() {
        return Paths.get(rootPath.replace('/', separator.charAt(0)).replace('\\', separator.charAt(0)));
    }

    // 将字符串转换为Path，并确保分隔符正确
//    public Path PY_MODAL_PATH() {
//        String tempGnnModelsPath = gnnModelsPath + separator + "processer.py";
//        return Paths.get(tempGnnModelsPath.replace('/', separator.charAt(0)).replace('\\', separator.charAt(0)));
//    }
//
//    public Path GNN_MODELS_PATH() {
//        return Paths.get(gnnModelsPath.replace('/', separator.charAt(0)).replace('\\', separator.charAt(0)));
//    }

    public Path OUTPUT_FILE_PATH() {
        String tempOutputFilesPath = outputFilesPath + separator;
        return Paths.get(tempOutputFilesPath.replace('/', separator.charAt(0)).replace('\\', separator.charAt(0)));
    }

    public Path CONDA_PYTHON_PATH() {
        return Paths.get(condaPythonExePath.replace('/', separator.charAt(0)).replace('\\', separator.charAt(0)));
    }

    public Path DATASET_STORAGE_PATH() {
        String tempDatasetStoragePath = datasetStoragePath + separator;
        return Paths.get(tempDatasetStoragePath.replace('/', separator.charAt(0)).replace('\\', separator.charAt(0)));
    }

    public Path CODESANDBOX_PATH() {
        return Paths.get(codesandboxPath.replace('/', separator.charAt(0)).replace('\\', separator.charAt(0)));
    }
}