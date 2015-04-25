package com.github.swm.integrationtests.entitytests;

import com.github.swm.integrationtests.helpers.OperationsHelper;
import com.github.swm.integrationtests.helpers.ScriptOperationsHelper;
import com.github.swm.userclient.App;
import com.github.swm.userclient.commands.CommandResponse;
import groovy.util.ConfigObject;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static junit.framework.Assert.assertFalse;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;

/**
 * Created by paul.smout on 19/04/2015.
 */
public class ScriptsTest extends AbstractEntityTest {


    private App app;
    private OperationsHelper ops;
    private ScriptOperationsHelper scriptOps;

    protected String name;
    protected String initialSource;
    protected String editedSource;

    @Before
    public void setUp(){

        ConfigObject config = new ConfigObject();
        config.setProperty("serverAddress", System.getProperty("serverAddress","localhost:8080"));
        app = new App(config);
        ops = new OperationsHelper(app);
        scriptOps = new ScriptOperationsHelper(app);

        setUpFixtures();

    }

    public void setUpFixtures(){
        name = "multiply3and6";
        initialSource = "def total = 0; \n" +
                "for (i=0; i < 6; i++) {\n" +
                "   total += 3; \n" +
                "}\n" +
                " return total;     ";
        editedSource = "def total = 3 * 6 \n" +
                " return total;";


    }

    @After
    public void teardown(){
       ops.given_I_have_deleted_all_my_data();
    }

    @Test
    public void should_be_able_to_create_and_edit_a_script(){


        ops.given_I_have_logged_in();
        // and
        ops.given_I_have_deleted_all_my_data();

        given_i_create_the_item();

        // then
        CommandResponse resp = when_i_list__all_the_items();
        JSONArray listData = then_the_response_will_have_one_item(resp);
        the_list_response_will_have_the_correct_fields_for_the_new_item(listData);

        when_i_edit_the_item();


        CommandResponse resp2 = when_i_list__all_the_items();
        JSONArray listData2 = then_the_response_will_have_one_item(resp2);

        CommandResponse resp3 = when_i_get_the_item_by_its_key();
        assertJsonObjectProperty((JSONObject)resp3.getData(), "name", name);
        assertJsonObjectProperty((JSONObject)resp3.getData(), "scriptContent", editedSource);
    }

    private void the_list_response_will_have_the_correct_fields_for_the_new_item(JSONArray listData) {
        assertJsonObjectProperty(listData.getJSONObject(0), "name", name);
    }

    private JSONArray then_the_response_will_have_one_item(CommandResponse resp) {
        JSONArray listData = (JSONArray) ((JSONObject)resp.getData()).get("items");
        assertThat(listData.size(),equalTo(1));
        return listData;
    }


    public void given_i_create_the_item(){
        scriptOps.I_create_a_new_script(name, initialSource);
    }

    public CommandResponse when_i_list__all_the_items(){
        return scriptOps.I_list_my_scripts("");
    }

    public CommandResponse when_i_edit_the_item(){
        return scriptOps.I_edit_the_script(name, editedSource);
    }

    public CommandResponse when_i_get_the_item_by_its_key(){
        return scriptOps.I_get_the_script_by_name(name);
    }
}
