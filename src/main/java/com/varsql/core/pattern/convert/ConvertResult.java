package com.varsql.core.pattern.convert;

import java.util.List;

/**
 * -----------------------------------------------------------------------------
* @fileName : ConvertResult.java
* @desc		:  ConvertResult
* @author	: ytkim
*-----------------------------------------------------------------------------
  DATE			AUTHOR			DESCRIPTION
*-----------------------------------------------------------------------------
*2017. 10. 31. 		ytkim			최초작성

*-----------------------------------------------------------------------------
 */
public class ConvertResult{
	
	private String cont;
	
	private List<?> parameterInfo;
	
	public ConvertResult(String cont) {
		this.cont = cont; 
	}
	
	public ConvertResult(String cont, List<?> parameterInfo) {
		this.cont = cont; 
		this.parameterInfo = parameterInfo; 
	}

	public String getCont() {
		return cont;
	}

	public void setCont(String cont) {
		this.cont = cont;
	}

	public List<?> getParameterInfo() {
		return parameterInfo;
	}

	public void setParameterInfo(List<?> parameterInfo) {
		this.parameterInfo = parameterInfo;
	}
	
	
	
}
