package com.scz.jxapi.netutils.rest;

public interface UrlParametersSerializer<T> {
	
	String getUrlParameters(T request);

}
