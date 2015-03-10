package com.github.swm.integrationtests;

import com.github.swm.userclient.App;
import com.github.swm.userclient.commands.CommandResponse;
import net.sf.json.JSONObject;

import java.util.List;

import static java.lang.String.format;
import static java.util.Arrays.asList;
import static org.hamcrest.core.Is.is;
import static org.hamcrest.core.IsEqual.equalTo;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThat;

/**
* Created by paul.smout on 08/03/2015.
*/
class OperationsHelper {
    private App app;

    OperationsHelper(App app) {
        this.app = app;
    }

    public void login() {
        CommandResponse resp = app.runCommand(cmd("login TEST1 ABC"));

        assertThat(resp.getSuccess(),is(true));
        assertThat(resp.getOutput(),equalTo("Login Successful"));
        assertNotNull(((JSONObject) resp.getData()).get("sessionId"));
    }

    public void newPlan(String name,String desc ) {
        CommandResponse resp = app.runCommand(cmd(format("newPlan %s ~ %s", name, desc)));

        assertThat(resp.getSuccess(),is(true));

    }

    public void newPlanStep(Long planId, String name,String desc ) {
        CommandResponse resp = app.runCommand(cmd(format("newStep %d %s ~ %s",planId, name, desc)));

        assertThat(resp.getSuccess(),is(true));

    }

    public CommandResponse showPlan(Long planId) {
        CommandResponse resp = app.runCommand(cmd(format("showPlan %d",planId)));

        assertThat(resp.getSuccess(),is(true));
        return resp;
    }


    public CommandResponse listPlans() {
        CommandResponse resp = app.runCommand(cmd(format("plans")));

        assertThat(resp.getSuccess(),is(true));
        return resp;

    }

    public void deleteEverything( ) {
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
}
