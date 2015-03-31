package com.github.swm.integrationtests;

import com.github.swm.userclient.App;
import com.github.swm.userclient.commands.CommandResponse;
import net.sf.json.JSONObject;

import java.util.List;

import static java.lang.String.format;
import static java.util.Arrays.asList;
import static org.hamcrest.core.Is.is;
import static org.hamcrest.core.IsEqual.equalTo;
import static org.hamcrest.core.IsNot.not;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;

/**
* Created by paul.smout on 08/03/2015.
*/
class OperationsHelper {
    private App app;

    OperationsHelper(App app) {
        this.app = app;
    }

    public void given_I_have_logged_in() {
        CommandResponse resp = app.runCommand(cmd("login TEST1 ABC"));

        assertThat(resp.getSuccess(),is(true));
        assertThat(resp.getOutput(),equalTo("Login Successful"));
        assertNotNull(((JSONObject) resp.getData()).get("sessionId"));
    }

    public void I_create_a_new_plan(String name, String desc) {
        CommandResponse resp = app.runCommand(cmd(format("newPlan %s ~ %s", name, desc)));

        assertThat(resp.getSuccess(),is(true));

    }

    public long i_create_a_new_plan_step(Long planId, String name, String desc) {
        CommandResponse resp = app.runCommand(cmd(format("newStep %d %s ~ %s",planId, name, desc)));

        assertThat(resp.getSuccess(),is(true));


        assertTrue(((JSONObject) resp.getData()).containsKey("id"));
        assertTrue(((JSONObject) resp.getData()).containsKey("createdAt"));

        return ((JSONObject)resp.getData()).getLong("id");
    }

    public CommandResponse showPlan(Long planId) {
        CommandResponse resp = app.runCommand(cmd(format("showPlan %d",planId)));

        assertThat(resp.getSuccess(),is(true));
        return resp;
    }


    public CommandResponse when_i_list_my_plans() {
        CommandResponse resp = app.runCommand(cmd(format("plans")));

        assertThat(resp.getSuccess(),is(true));
        return resp;

    }

    public void given_I_have_deleted_all_my_data() {
        CommandResponse resp = app.runCommand(cmd(format("deleteAll")));

        assertThat(resp.getSuccess(),is(true));

    }

    public void help( ) {
        CommandResponse resp = app.runCommand(cmd(format("?")));

        assertThat(resp.getSuccess(),is(true));

    }

    private List<String> cmd(String cmd){
        return asList(cmd.split(" "));
    }

    public void i_add_an_oncompleted_event_to_each_step(String eventName, String eventValidator, Long owningStepId, Long nextStepId) {
        CommandResponse resp = app.runCommand(cmd(format("newStepEvent %s %d %d", eventName, owningStepId, nextStepId)));
        assertThat(resp.getSuccess(),is(true));
    }

    public void when_i_begin_my_plan(Long planId) {
        CommandResponse resp = app.runCommand(cmd(format("beginPlan %d", planId)));
        assertThat(resp.getSuccess(),is(true));
    }

    public CommandResponse when_i_list_my_tasks() {
        CommandResponse resp = app.runCommand(cmd(format("tasks")));
        assertThat(resp.getSuccess(),is(true));
        return resp;
    }

    public CommandResponse when_i_complete_my_task(long taskId) {

        return sendTaskEvent(taskId, "Complete");
    }

    private CommandResponse sendTaskEvent(long taskId, String event) {
        CommandResponse resp = app.runCommand(cmd(format("event %d %s", taskId, event)));
        assertThat(resp.getSuccess(),is(true));
        return resp;
    }
}
