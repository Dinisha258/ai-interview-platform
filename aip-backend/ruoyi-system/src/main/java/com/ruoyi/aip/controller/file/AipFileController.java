package com.ruoyi.aip.controller.file;

import com.ruoyi.aip.utils.MinioUtil;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api/file")
public class AipFileController extends BaseController {

    @Autowired
    private MinioUtil minioUtil;

    @PostMapping("/upload")
    public AjaxResult upload(MultipartFile file) {
        try {
            // 上传文件、返回url
            String fileUrl = minioUtil.uploadFile(file);

            AjaxResult ajax = AjaxResult.success();
            ajax.put("url", fileUrl);
            ajax.put("fileName", file.getOriginalFilename());
            return ajax;
        } catch (Exception e) {
            return AjaxResult.error(e.getMessage());
        }
    }
}
