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
    //    ops.deleteEverything();
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
       ops.login();
       ops.deleteEverything();
       ops.newPlan("My First Plan","This plan is about attaining world peace.");

       CommandResponse resp = ops.listPlans();
       assertThat(resp.getData().size(), equalTo(1));

       // from the plan id create a few plan steps
       Long planId = ((JSONObject)((JSONArray)resp.getData()).get(0)).getLong("id");

       ops.newPlanStep(planId, "Buy baby rabbits","Rabbits should be purchased from an organic rabbit dispensiary");
       ops.newPlanStep(planId, "Train rabbits to hand out flowers","Rabbit training should use only humane ecologically sound techniques");
       ops.newPlanStep(planId, "Train rabbits to give hugs","Rabbit training should use only humane ecologically sound techniques");

    }



}
