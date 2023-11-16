package com.example.demo.controller;

import cn.hutool.json.JSONArray;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.nio.charset.Charset;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping(value = "/func")
public class Select {

    @Value("${uploadFilePath}")
    private String uploadFilePath;


    @PostMapping("/uploadFile")
    public String FileUpload(@RequestParam("jsonFile") MultipartFile file){
        String name = file.getOriginalFilename();
        if (!file.isEmpty()) {
            try {
                // 设置文件存储路径，确保该路径已存在
                String filePath =  uploadFilePath + name;
                BufferedOutputStream stream = new BufferedOutputStream(new FileOutputStream(new File(filePath)));
                stream.write(file.getBytes());
                stream.close();
                return "成功上传 " + name + " !";
            } catch (IOException e) {
                return "上传 " + name + " 失败 => " + e.getMessage();
            }
        } else {
            return "上传失败，因为文件是空的.";
        }
    }

    @PostMapping("/chooseFile")
    public Map<String, String> chooseFile(@RequestParam("uploadedFile") MultipartFile multipartFile) {
        String PATH = "src/main/resources/static/";
        File fileDir = new File(PATH);
        if (!fileDir.exists()) {
            fileDir.mkdirs();
        }
        File file = new File(multipartFile.getName());
        InputStream stream = null;
        try (BufferedOutputStream outputStream = new BufferedOutputStream(new FileOutputStream(file))) {
            stream = multipartFile.getInputStream();
            byte[] buffer = new byte[8192];
            int byteRead;
            while ((byteRead = stream.read(buffer)) != -1) {
                outputStream.write(buffer, 0, byteRead);
            }
        } catch (IOException e) {
        }
        JSONArray jsonArray = JSONUtil.readJSONArray(file, Charset.forName("utf-8"));
        JSONObject jsonObject = jsonArray.getJSONObject(0);
        JSONObject headerData = jsonObject.getJSONObject("header");
        String reftime = headerData.getStr("refTime");
        String parameterCategoryName = headerData.getStr("parameterCategoryName");
        HashMap<String, String> resultMap = new HashMap<>();
        if (reftime != null && parameterCategoryName != null) {
            resultMap.put("date", reftime);
            resultMap.put("parameterCategoryName", parameterCategoryName);
            resultMap.put("status", "success");
        } else {
            resultMap.put("status", "success");
        }
        return resultMap;

    }
}
