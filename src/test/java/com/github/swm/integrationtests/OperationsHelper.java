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
    // final static Logger logger = LoggerFactory.getLogger(OperationsHelper.class);
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

    public long I_create_a_new_plan(String name, String desc) {
        CommandResponse resp = app.runCommand(cmd(format("newPlan %s ~ %s", name, desc)));

        assertThat(resp.getSuccess(),is(true));
        assertTrue(((JSONObject) resp.getData()).containsKey("id"));

        return ((JSONObject)resp.getData()).getLong("id");

    }

    public long i_create_a_new_plan_step(Long planId, String name, String desc) {
        CommandResponse resp = app.runCommand(cmd(format("newStep %d %s ~ %s",planId, name, desc)));

        assertThat(resp.getSuccess(),is(true));


        assertTrue(((JSONObject) resp.getData()).containsKey("id"));
        assertTrue(((JSONObject) resp.getData()).containsKey("serverTime"));

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

    public long i_add_a_named_event_to_the_step(String eventName, String eventValidator, Long owningStepId, Long nextStepId) {
        CommandResponse resp = app.runCommand(cmd(format("newStepEvent %s %s %d %d", eventName, eventValidator, owningStepId, nextStepId)));
        assertThat(resp.getSuccess(),is(true));
        assertTrue(((JSONObject) resp.getData()).containsKey("id"));
        assertTrue(((JSONObject) resp.getData()).containsKey("serverTime"));

        return ((JSONObject)resp.getData()).getLong("id");
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
        CommandResponse resp = when_I_send_task_event(taskId, "Complete", "");
        assertThat(resp.getSuccess(),is(true));
        return resp;
    }


    public CommandResponse when_I_send_task_event(long taskId, String event, String params) {

        CommandResponse resp = app.runCommand(cmd(format("event %d %s %s", taskId, event, params)));
        assertThat(resp.getSuccess(),is(true));
        return resp;
    }

    public CommandResponse i_add_parameter_to_the_event(Long eventId, String paramName, String type) {
        CommandResponse resp = app.runCommand(cmd(format("newEventParam %d %s %s",eventId, paramName, type)));
        assertThat(resp.getSuccess(),is(true));
        return resp;
    }
}
