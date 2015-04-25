package com.github.swm.integrationtests;

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

import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import static java.lang.String.format;
import static junit.framework.Assert.assertFalse;
import static junit.framework.Assert.fail;
import static org.hamcrest.core.Is.is;
import static org.hamcrest.core.IsEqual.equalTo;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThat;

/**
 * Created by paul.smout on 05/03/2015.
 */
public class PlanTest {

    private App app;
    private OperationsHelper ops;
    private ScriptOperationsHelper scriptOps;

    private static String NULL_VALIDATOR = "NULL";
    private static String PARAMETER_VALIDATOR = "PARAMETER";
    @Before
    public void setUp(){
        ConfigObject config = new ConfigObject();
        config.setProperty("serverAddress", System.getProperty("serverAddress","localhost:8080"));
        app = new App(config);
        ops = new OperationsHelper(app);
        scriptOps = new ScriptOperationsHelper(app);
    }

    @After
    public void teardown(){
       ops.given_I_have_deleted_all_my_data();
    }

    @Test
    public void should_log_in_and_create_a_plan(){
        _should_log_in_and_create_a_plan();
    }

  //  @Test
    public void load_should_log_in_and_create_a_plan() throws ExecutionException, InterruptedException {

        ExecutorService executorService = Executors.newFixedThreadPool(10);

        executorService.submit(new Runnable(){
            @Override
            public void run() {
                for (int i = 0; i < 20; i++){
                    _should_log_in_and_create_a_plan();
                }
            }
        }).get();
    }

    public void _should_log_in_and_create_a_plan(){
        ops.given_I_have_logged_in();
        // and
        ops.given_I_have_deleted_all_my_data();

        // and given
        ops.I_create_a_new_plan("My First Plan", "This plan is about attaining world peace.");

        // then I will have one plan in the system
        CommandResponse resp = ops.when_i_list_my_plans();
        System.err.println(">>> " + resp);
        assertThat(resp.getData().size(), equalTo(1));

        // from the plan id create a few plan steps
        Long planId = ((JSONObject)((JSONArray)resp.getData()).get(0)).getLong("id");

        Long step1Id = ops.i_create_a_new_plan_step(planId, "Buy baby rabbits", "Rabbits should be purchased from an organic rabbit dispensary");
        Long step2Id = ops.i_create_a_new_plan_step(planId, "Train rabbits to hand out flowers", "Rabbit training should use only humane ecologically sound techniques");
        Long step3Id = ops.i_create_a_new_plan_step(planId, "Train rabbits to give hugs", "Rabbit training should include counselling for emotionally fragile rabbits.");


        resp = ops.when_i_list_my_plans();
        assertThat(resp.getData().size(), equalTo(1));

        // then the plan I created should exist
        JSONArray plans = (JSONArray)resp.getData();
        JSONObject plan1 = plans.getJSONObject(0);
        Long id = validate_plan(plan1, "My First Plan", "This plan is about attaining world peace.", 3);


        resp = ops.showPlan(id);
        System.err.println(resp);

        //
        ops.i_add_a_named_event_to_the_step("complete", "" ,NULL_VALIDATOR, step1Id, step2Id);
        ops.i_add_a_named_event_to_the_step("complete", "" ,NULL_VALIDATOR, step2Id, step3Id);
        ops.i_add_a_named_event_to_the_step("complete", "" ,NULL_VALIDATOR, step3Id, null);

        ops.when_i_begin_my_plan(id);

        CommandResponse tasksResp = ops.when_i_list_my_tasks();
        JSONArray tasks = (JSONArray)tasksResp.getData();
        assertThat(tasks.size(), equalTo(1));
        JSONObject task1 = tasks.getJSONObject(0);

        validate_task(task1, "Buy baby rabbits", "Rabbits should be purchased from an organic rabbit dispensary");

        ops.when_i_complete_my_task(task1.getInt("id"));

        tasksResp = ops.when_i_list_my_tasks();
        tasks = (JSONArray)tasksResp.getData();
        assertThat(tasks.size(), equalTo(1));
        JSONObject task2 = tasks.getJSONObject(0);

        validate_task(task2, "Train rabbits to hand out flowers", "Rabbit training should use only humane ecologically sound techniques");


        ops.when_i_complete_my_task(task2.getInt("id"));

        tasksResp = ops.when_i_list_my_tasks();
        tasks = (JSONArray)tasksResp.getData();
        assertThat(tasks.size(), equalTo(1));
        JSONObject task3 = tasks.getJSONObject(0);

        validate_task(task3, "Train rabbits to give hugs", "Rabbit training should include counselling for emotionally fragile rabbits.");

        ops.when_i_complete_my_task(task3.getInt("id"));
        tasksResp = ops.when_i_list_my_tasks();
        tasks = (JSONArray)tasksResp.getData();
        assertThat(tasks.size(), equalTo(0));

    }

    @Test
    public void should_not_be_able_to_multiple_plans_with_same_name(){


        ops.given_I_have_logged_in();
        // and
        ops.given_I_have_deleted_all_my_data();

        ops.I_create_a_new_plan("My First Plan", "This plan is about attaining world peace.");


        CommandResponse resp = ops.I_try_to_create_a_new_plan("My First Plan", "This plan is about attaining world peace.");
        assertFalse("Commant should fail", resp.getSuccess());


    }

    @Test
    public void announceBaby(){
        ops.given_I_have_logged_in();
        // and
        ops.given_I_have_deleted_all_my_data();

        //
        scriptOps.I_create_a_new_script("storeParams", "def item = api.getParamsAsObject('emailAddress','name'); \n" +
                                                       "api.context().saveToList('/announcebaby/emailrecipientslist',item)");


        // and given
        long planId = ops.I_create_a_new_plan("Announce New Baby", "Plan and automate the announcing of the new baby.");

        Long step1Id = ops.i_create_a_new_plan_step(planId, "Make List of People to send to", "This step accepts multiple add names events");


        Long addNameEventId = ops.i_add_a_named_event_to_the_step("addName", "storeParams", PARAMETER_VALIDATOR, step1Id, step1Id);

        ops.i_add_parameter_to_the_event(addNameEventId, "emailAddress","ValidEmail");
        ops.i_add_parameter_to_the_event(addNameEventId, "name","FullName");


        // TODO add validator
        // TODO add params
        Long completeEventId = ops.i_add_a_named_event_to_the_step("complete", "", null, step1Id, null);

        ops.when_i_begin_my_plan(planId);

        long task1Id = validate_single_task_in_list("Make List of People to send to", "This step accepts multiple add names events");

        String params = "{\"emailAddress\":\"person@example.com\", " +
                "\"name\":\"Holden McCrotch\"}";

        ops.when_I_send_task_event(task1Id,"addName", params);

        task1Id = validate_single_task_in_list("Make List of People to send to", "This step accepts multiple add names events");



    }

    private long validate_single_task_in_list(String expectedName, String expectedDescription){
        CommandResponse tasksResp = ops.when_i_list_my_tasks();
        JSONArray tasks = (JSONArray)tasksResp.getData();
        assertThat(tasks.size(), equalTo(1));
        JSONObject task1 = tasks.getJSONObject(0);

        validate_task(task1, expectedName,  expectedDescription);

        return task1.getLong("id");

    }


    private long validate_task(JSONObject task, String expectedName, String expectedDescription){

        Long id = task.getLong("id");
        String name = task.getString("name");
        String description = task.getString("description");

        assertThat(name, equalTo(expectedName));
        assertThat(description, equalTo(expectedDescription));

        return id;
    }


    private long validate_plan(JSONObject plan, String expectedName, String expectedDescription, int expectedNumSteps){

        Long id = plan.getLong("id");
        String name = plan.getString("name");
        String description = plan.getString("description");
        int numSteps = plan.getInt("numSteps");
        assertThat(numSteps, equalTo(expectedNumSteps));

        assertThat(name, equalTo(expectedName));
        assertThat(description, equalTo(expectedDescription));

        return id;
    }

}
