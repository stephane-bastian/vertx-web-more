package com.thesoftwarefactory.vertx.web.more.impl;

import static org.junit.Assert.*;

import java.util.Map;

import org.junit.Test;

import io.vertx.core.json.JsonObject;

public class TestConfigs {

	private JsonObject testJson() {
		String json = "{"
				+ "\"boolean\" : true,"
				+ "\"int\" : 1,"
				+ "\"string\" : \"string value\","
				+ "\"array\" : ["
					+ "\"string value 0\","
					+ "{"
							+ "\"boolean 1\" : false"
					+ "},"
					+ "["
						+ "true,"
						+ "\"string value 2.1\""
					+ "]"
				+ "],"
				+ "\"object\" : {"
					+ "\"boolean\" : true,"
					+ "\"int\" : 2,"
					+ "\"string\" : \"string value\","
					+ "\"object\" : {"
						+ "\"boolean\" : true,"
						+ "\"int\" : 3,"
						+ "\"string\" : \"string value\""
					+ "},"
					+ "\"array\" : ["
						+ "true,"
						+ "\"string value\""
					+ "]"
				+ "}"
				+ "}";
		return new JsonObject(json);
	}

	@Test
	public void testExtract() {
		JsonObject flattenJson = Configs.flatten(testJson());
		Map<String, Object> map = flattenJson.getMap();
		Map<String, String> map2 = Configs.extract(map, "object.object", false);
		assertTrue(map2.size()==3);
		assertTrue(map2.get("object.object.boolean").equals("true"));
		assertTrue(map2.get("object.object.int").equals("3"));
		assertTrue(map2.get("object.object.string").equals("string value"));

		Map<String, String> map3 = Configs.extract(map, "object.object", true);
		assertTrue(map3.size()==3);
		assertTrue(map3.get("boolean").equals("true"));
		assertTrue(map3.get("int").equals("3"));
		assertTrue(map3.get("string").equals("string value"));
	}
	
	@Test
	public void testFlatten() {
		JsonObject flattenJson = Configs.flatten(testJson());
		assertTrue(flattenJson.size()==15);
		assertTrue(flattenJson.getValue("boolean").equals(true));
		assertTrue(flattenJson.getValue("int").equals(1));
		assertTrue(flattenJson.getValue("string").equals("string value"));
		assertTrue(flattenJson.getValue("array.0").equals("string value 0"));
		assertTrue(flattenJson.getValue("array.1.boolean 1").equals(false));
		assertTrue(flattenJson.getValue("array.2.0").equals(true));
		assertTrue(flattenJson.getValue("array.2.1").equals("string value 2.1"));
		assertTrue(flattenJson.getValue("object.boolean").equals(true));
		assertTrue(flattenJson.getValue("object.int").equals(2));
		assertTrue(flattenJson.getValue("object.string").equals("string value"));
		assertTrue(flattenJson.getValue("object.object.boolean").equals(true));
		assertTrue(flattenJson.getValue("object.object.int").equals(3));
		assertTrue(flattenJson.getValue("object.object.string").equals("string value"));
		assertTrue(flattenJson.getValue("object.array.0").equals(true));
		assertTrue(flattenJson.getValue("object.array.1").equals("string value"));
	}

}
