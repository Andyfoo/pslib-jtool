package com.pslib.jtool.captcha.util;

import java.util.HashMap;

import cn.hutool.core.io.FileUtil;
import cn.hutool.http.HttpUtil;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;

public class CaptchaRecognition {
	// 用户名
	public String username = "";
	// 密码
	public String password = "";
	// 验证码类型(默认 3 数英混合)：16:汉字 14:图片旋转 11:计算题 7:无感学习，4:闪动GIF，3:数英混合， 2:纯英文，1:纯数字
	public String typeid = "";
	// 定制识别的模型id,发布成功后的模型id。注：有modelid为定向识别，不存在modelid为通用识别：可空
	public String modelid = "";

	public JSONObject send(String filepath) {
		HashMap<String, Object> paramMap = new HashMap<>();
		paramMap.put("username", username);
		paramMap.put("password", password);
		paramMap.put("typeid", typeid);
		paramMap.put("modelid", modelid);
		paramMap.put("image", FileUtil.file(filepath));

		String result = HttpUtil.post("http://api.ttshitu.com/create.json", paramMap);
		return JSONUtil.parseObj(result);
	}
}
