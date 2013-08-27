package com.duomn.action;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.duomn.service.IUserService;
import com.duomn.util.JsonUtils;

@Controller
@RequestMapping("IndexAction.action")
public class IndexAction {
	
	@Autowired
	private IUserService userService;
	
	@RequestMapping(params="method=usualRequest")
	public String usualRequest() {
		return "/usualResponse";
	}

	@ResponseBody
	@RequestMapping(params="method=ajaxRequest")
	public Map<String, Object> ajaxRequest() {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("flag", "1");
		map.put("msg", "Operation Success");
		return map;
	}
	
	@ResponseBody
	@RequestMapping(params="method=getUserCount")
	public Map<String, Object> getUserCount() {
		int count = userService.count();
		if (count < 0) {
			return JsonUtils.getSimpleErrorMap("错误的结果");
		}
			
		return JsonUtils.getSimpleSuccessMap(count);
	}
	
}
