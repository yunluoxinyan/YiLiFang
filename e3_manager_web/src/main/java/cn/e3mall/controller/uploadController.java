package cn.e3mall.controller;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import cn.e3mall.common.utils.FastDFSClient;
import cn.e3mall.common.utils.JsonUtils;

@Controller
public class uploadController {
	
	@Value("${image.resource.url}")
	private String imageResourceUrl;
	
	@RequestMapping(value="/pic/upload",produces="text/plain;charset=utf-8")
	@ResponseBody
	public String uploadImage(MultipartFile uploadFile){
		try {
			FastDFSClient client = new FastDFSClient("classpath:conf/client.conf");
			String filename = uploadFile.getOriginalFilename();
			String extName = filename.substring(filename.lastIndexOf(".") + 1);
			String filePath = client.uploadFile(uploadFile.getBytes(), extName);
			String url  = imageResourceUrl + filePath;
			Map result = new HashMap<>();
			result.put("error", 0);
			result.put("url", url);
			return JsonUtils.objectToJson(result);
		} catch (Exception e) {
			Map result = new HashMap<>();
			result.put("error", 1);
			result.put("message", "文件上传失败");
			return JsonUtils.objectToJson(result);
		}
	}
}
