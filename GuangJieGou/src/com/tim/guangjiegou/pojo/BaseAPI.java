package com.tim.guangjiegou.pojo;

import org.json.JSONException;
import org.json.JSONObject;

public class BaseAPI extends VO {

	/**
	 * 
	 */
	private static final long serialVersionUID = -4920392797714130751L;
	
	private String status;
	private String errorCode;
	private String errorMessage;
	
	public void initWithJson(String json) {
		if(json != null) {
			try {
				JSONObject jObject = new JSONObject(json);
				if(jObject != null) {
					errorCode = jObject.getString("error_code");
					errorMessage = jObject.getString("error_message");
					status = jObject.getString("status");
				}
			} catch (JSONException e) {
				e.printStackTrace();
			}
		}
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getErrorCode() {
		return errorCode;
	}

	public void setErrorCode(String errorCode) {
		this.errorCode = errorCode;
	}

	public String getErrorMessage() {
		return errorMessage;
	}

	public void setErrorMessage(String errorMessage) {
		this.errorMessage = errorMessage;
	}

}
