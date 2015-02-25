package com.github.swm.integrationtests;

import com.github.swm.userclient.App;
import com.github.swm.userclient.commands.CommandResponse;
import groovy.util.ConfigObject;
import org.junit.Before;
import org.junit.Test;

import java.util.List;
import java.util.concurrent.Future;

import static java.util.Arrays.asList;
import static org.hamcrest.core.Is.is;
import static org.hamcrest.core.IsEqual.equalTo;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThat;

/**
 * Created by paul.smout on 24/02/2015.
 */
public class LoginTest {

    private App app;

    @Before
    public void setUp(){
        ConfigObject config = new ConfigObject();
        config.setProperty("serverAddress", System.getProperty("serverAddress","localhost:8080"));
        app = new App(config);
    }

    @Test
    public void should_not_let_invalid_user_log_in(){
        CommandResponse resp = app.runCommand(cmd("login aaa bbb"));

        assertThat(resp.getSuccess(),is(false));
        assertThat(resp.getOutput(),equalTo("Access Denied"));

    }

    @Test
    public void should_let_valid_user_log_in(){

        CommandResponse resp = app.runCommand(cmd("login TEST1 ABC"));

        assertThat(resp.getSuccess(),is(true));
        assertThat(resp.getOutput(),equalTo("Login Successful"));
        assertNotNull(resp.getData().get("sessionId"));

    }


    private List<String> cmd(String cmd){
        return asList(cmd.split(" "));
    }
}
