package com.example.demo.controller;

import cn.hutool.json.JSONArray;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.nio.charset.Charset;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping(value = "/func")
public class Select {

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
