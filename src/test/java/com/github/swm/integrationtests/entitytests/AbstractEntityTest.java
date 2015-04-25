package com.github.swm.integrationtests.entitytests;

import net.sf.json.JSONObject;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

/**
 * Created by paul.smout on 21/04/2015.
 */
public class AbstractEntityTest {
    protected void assertJsonObjectProperty(JSONObject object, String key, Object value){
        assertTrue("Property " + key + " not found.", object.containsKey(key));
        assertEquals("Value for key  " + key + " incorrect.", object.get(key), value);
    }
}
