package com.scz.jxapi.exchange;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import com.scz.jxapi.observability.ExchangeApiObserver;

public abstract class AbstractExchange implements Exchange {

	protected final String name;
	protected final String id;
	protected final Properties properties;
	protected final Map<String, ExchangeApi> apis = new HashMap<>();
	
	public AbstractExchange(String id, String name, Properties properties) {
		this.id = id;
		this.name = name;
		this.properties = properties;
	}

	@Override
	public Properties getProperties() {
		return properties;
	}

	@Override
	public String getName() {
		return name;
	}

	@Override
	public String getId() {
		return id;
	}
	
	public List<ExchangeApi> getApis() {
		return List.copyOf(apis.values());
	}

	@Override
	public void subscribeObserver(ExchangeApiObserver exchangeApiObserver) {
		apis.values().forEach(api -> api.subscribeObserver(exchangeApiObserver));
	}

	@Override
	public boolean unsubscribeObserver(ExchangeApiObserver exchangeApiObserver) {
		boolean res = false;
		for (ExchangeApi api: getApis()) {
			res |= api.unsubscribeObserver(exchangeApiObserver);
		}
		return res;
	}
	
	protected <T extends ExchangeApi> T addApi(T api) {
		apis.put(api.getName(), api);
		return api;
	}

}
