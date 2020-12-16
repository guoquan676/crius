package com.pbkj.crius.common.base;

import com.alibaba.fastjson.JSONObject;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.http.ResponseEntity;

import java.io.Serializable;

@Data
@AllArgsConstructor
public class BaseResponse implements Serializable {

	private static final long serialVersionUID = -5055168314128607695L;

	public static ResponseEntity<Object> sendMessage(int code, Object obj) {
		if (code != ResultCode.SUCCESS) {
			JSONObject json = new JSONObject();
			json.put("msg", obj);
			obj = json;
		}
		return ResponseEntity.status(code).body(obj);
	}

	public static ResponseEntity<Object> sendMessageError() {
		return sendMessage(ResultCode.ERROR_SYSTEM, "系统错误");
	}

	public static ResponseEntity<Object> sendMessageError(Object obj) {
		return sendMessage(ResultCode.ERROR_SYSTEM, obj);
	}

	public static ResponseEntity<Object> sendMessageSuccess() {
		return sendMessage(ResultCode.SUCCESS, "操作成功");
	}

	public static ResponseEntity<Object> sendMessageSuccessJson() {
		return sendMessageJson(ResultCode.SUCCESS, "操作成功");
	}

	public static ResponseEntity<Object> sendMessageJson(int code, Object obj) {
		JSONObject json = new JSONObject();
		json.put("msg", obj);
		obj = json;
		return ResponseEntity.status(code).body(obj);
	}

	public static ResponseEntity<Object> sendMessageSuccess(Object obj) {
		return sendMessage(ResultCode.SUCCESS, obj);
	}

}