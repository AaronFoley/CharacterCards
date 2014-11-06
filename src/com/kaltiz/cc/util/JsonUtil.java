package com.kaltiz.cc.util;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;


import java.io.IOException;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Created by kaltiz on 6/11/14.
 */
public class JsonUtil {

    public static Map<String, String> json_to_map(String json)
        throws JsonParseException, JsonMappingException, IOException
    {
        Map<String, String> map;

        ObjectMapper mapper = new ObjectMapper();
        map = mapper.readValue(json, new TypeReference<HashMap<String, Object>>() {});

        return map;
    }

    public static String map_to_json(HashMap<String, String> map)
        throws JsonProcessingException
    {
        ObjectMapper objectMapper = new ObjectMapper();
        return objectMapper.writeValueAsString(map);
    }
}
