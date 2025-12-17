package com.ruoyi.common.utils.file;

/**
 * 媒体类型工具类
 * 
 * @author ruoyi
 */
public class MimeTypeUtils
{
    public static final String IMAGE_PNG = "image/png";

    public static final String IMAGE_JPG = "image/jpg";

    public static final String IMAGE_JPEG = "image/jpeg";

    public static final String IMAGE_BMP = "image/bmp";

    public static final String IMAGE_GIF = "image/gif";
    
    public static final String[] IMAGE_EXTENSION = { "bmp", "gif", "jpg", "jpeg", "png" };

    public static final String[] FLASH_EXTENSION = { "swf", "flv" };

    public static final String[] MEDIA_EXTENSION = { "swf", "flv", "mp3", "wav", "wma", "wmv", "mid", "avi", "mpg",
            "asf", "rm", "rmvb" };

    public static final String[] VIDEO_EXTENSION = { "mp4", "avi", "rmvb" };

    public static final String[] DEFAULT_ALLOWED_EXTENSION = {
            // 图片
            "bmp", "gif", "jpg", "jpeg", "png",
            // word excel powerpoint
            "doc", "docx", "xls", "xlsx", "ppt", "pptx", "html", "htm", "txt",
            // 压缩文件
            "rar", "zip", "gz", "bz2",
            // 视频格式
            "mp4", "avi", "rmvb",
            // pdf
            "pdf",
            // 数据集文件格式
            "pkl", "csv", "txt", "npy", "npyz", "npyx", "npyy", "npyz", "npyt", "npyh", "npyg", "npyf", "npye", "npyd", "npyc", "npyb", "npya", "npy9", "npy8", "npy7", "npy6", "npy5", "npy4", "npy3", "npy2", "npy1", "npy0",
            "json", "yaml", "yml", "xml", "jsonl", "jsonlz", "jsonlx", "jsonly", "jsonlz4", "jsonlz5", "jsonlz6", "jsonlz7", "jsonlz8", "jsonlz9", "jsonlz10", "jsonlz11", "jsonlz12", "jsonlz13", "jsonlz14", "jsonlz15", "jsonlz16", "jsonlz17", "jsonlz18", "jsonlz19", "jsonlz20", "jsonlz21",
            // 模型文件格式
            "pth", "pt", "bin", "onnx", "safetensors", "pt", "pkl", "pth", "onnx", "safetensors", "bin", "pt", "pkl",
            "onnx", "safetensors", "bin", "pt", "pkl", "pth",

    };

    public static String getExtension(String prefix)
    {
        switch (prefix)
        {
            case IMAGE_PNG:
                return "png";
            case IMAGE_JPG:
                return "jpg";
            case IMAGE_JPEG:
                return "jpeg";
            case IMAGE_BMP:
                return "bmp";
            case IMAGE_GIF:
                return "gif";
            default:
                return "";
        }
    }
}
