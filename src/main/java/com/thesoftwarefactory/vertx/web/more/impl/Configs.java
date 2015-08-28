package com.thesoftwarefactory.vertx.web.more.impl;

import java.util.HashMap;
import java.util.Map;

import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;

public class Configs {

	public final static Map<String, String> flattenAndExtract(JsonObject jsonObject, String prefixKey, boolean foldKeyNames) {
		return extract(flatten(jsonObject).getMap(), prefixKey, foldKeyNames);
	}
	
	public final static Map<String, String> extract(Map<String, Object> map, String prefixKey, boolean foldKeyNames) {
		Map<String, String> result = new HashMap<>();
		if (map!=null && prefixKey!=null) {
			for (String key: map.keySet()) {
				if (key.startsWith(prefixKey)) {
					Object value = map.get(key);
					String targetKey = foldKeyNames ? key.substring(prefixKey.length()+1) : key;
					result.put(targetKey, value.toString());
				}
			}
		}
		return result;
	}
	
	public final static JsonObject flatten(JsonObject input) {
		JsonObject result = new JsonObject();
		if (input!=null) {
			flatten(null, input, result);
		}
		return result;
	}

	private final static void flatten(String prefix, JsonObject input, JsonObject output) {
		if (input!=null && output!=null) {
			for (String fieldName: input.fieldNames()) {
				String targetFieldName = prefix!=null  ? prefix + "." + fieldName : fieldName;
				Object value = input.getValue(fieldName);
				if (value instanceof JsonObject) {
					flatten(targetFieldName, (JsonObject) value, output);
				}
				else if (value instanceof JsonArray) {
					flatten(targetFieldName, (JsonArray) value, output);
				}
				else {
					output.put(targetFieldName, value);
				}
			}
		}
	}

	private final static void flatten(String prefix, JsonArray input, JsonObject output) {
		if (input!=null && output!=null) {
			int index = 0;
			for (Object value: input) {
				String targetFieldName = prefix!=null  ? prefix + "." + index : "" +  index;
				if (value instanceof JsonObject) {
					flatten(targetFieldName, (JsonObject) value, output);
				}
				else if (value instanceof JsonArray) {
					flatten(targetFieldName, (JsonArray) value, output);
				}
				else {
					output.put(targetFieldName, value);
				}
				index++;
			}
		}
	}

}
