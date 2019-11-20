package com.example.controller;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.List;
import java.util.UUID;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

/**
 * 文件上传controller
 *
 */
@RestController
public class FileUploadController {

    public static String FILE_PATH = "/Users/zyfine/Desktop/";
    /**
     * 文件上传
     *
     * @return
     */
    @RequestMapping(value = "/upload", method = RequestMethod.POST)
    public String handlFileUpload(@RequestParam("file") MultipartFile file) {

        if (file.isEmpty()) {
            return "文件是空的";
        }
        // 读取文件内容并写入 指定目录中
        String fileName = file.getOriginalFilename();
        String suffixName = fileName.substring(fileName.lastIndexOf("."));
        fileName = UUID.randomUUID() + "" + suffixName;

        File dest = new File(FILE_PATH + fileName);
        // 判断目录是否存在
        if (!dest.getParentFile().exists()) {
            dest.getParentFile().mkdirs();
        }

        try {
            file.transferTo(dest);
        } catch (IOException e) {
            return "后台也不知道为什么, 反正就是上传失败了";
        }
        return "上传成功";
    }


    /**
     * 多文件上传
     * 类似单文件上传, 遍历
     * @return
     */
    @RequestMapping(value = "/multiUpload", method = RequestMethod.POST)
    public String handleMultiFileupload(HttpServletRequest request) {
        List<MultipartFile> files = ((MultipartHttpServletRequest) request).getFiles("file");

        for (MultipartFile multipartFile : files) {
            if (multipartFile.isEmpty()) {
                return "文件是空的";
            }

            // 读取文件内容并写入 指定目录中
            String fileName = multipartFile.getOriginalFilename();
             String suffixName = fileName.substring(fileName.lastIndexOf("."));
            fileName = UUID.randomUUID() + "" + suffixName;

            File dest = new File(FILE_PATH + fileName);
            // 判断目录是否存在
            if (!dest.getParentFile().exists()) {
                dest.getParentFile().mkdirs();
            }

            try {
                multipartFile.transferTo(dest);
            } catch (IOException e) {
                return "后台也不知道为什么, 反正就是上传失败了";
            }
        }
        return "上传成功";
    }

    /**
     * 文件下载
     *
     * @return
     */
    @RequestMapping("/download")
    public String downLoadFile(HttpServletRequest request, HttpServletResponse response) {
        // 文件名可以从request中获取, 这儿为方便, 写死了
        String fileName = "rtsch_ex.json";
        // String path = request.getServletContext().getRealPath("/");
        String path = "E:/test";
        File file = new File(path, fileName);

        if (file.exists()) {
            // 设置强制下载打开
            response.setContentType("application/force-download");
            // 文件名乱码, 使用new String() 进行反编码
            response.addHeader("Content-Disposition", "attachment;fileName=" + fileName);

            // 读取文件
            BufferedInputStream bi = null;
            try {
                byte[] buffer = new byte[1024];
                bi = new BufferedInputStream(new FileInputStream(new File("")));
                ServletOutputStream outputStream = response.getOutputStream();
                int i = -1;
                while (-1 != (i = bi.read(buffer))) {
                    outputStream.write(buffer, 0, i);
                }
                return "下载成功";
            } catch (Exception e) {
                return "程序猿真不知道为什么, 反正就是下载失败了";
            } finally {
                if (bi != null) {
                    try {
                        bi.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
        return "文件不存在";
    }




}

