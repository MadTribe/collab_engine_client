package com.github.swm.integrationtests;

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

    @Before
    public void setUp(){
        ConfigObject config = new ConfigObject();
        config.setProperty("serverAddress", System.getProperty("serverAddress","localhost:8080"));
        app = new App(config);
        ops = new OperationsHelper(app);

    }

    @After
    public void teardown(){
       ops.given_I_have_deleted_all_my_data();
    }

    @Test
    public void should_log_in_and_create_a_plan(){
        _should_log_in_and_create_a_plan();
    }

    @Test
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
        ops.i_add_an_oncompleted_event_to_each_step("complete", null, step1Id, step2Id);
        ops.i_add_an_oncompleted_event_to_each_step("complete", null, step2Id, step3Id);
        ops.i_add_an_oncompleted_event_to_each_step("complete", null, step3Id, null);

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
