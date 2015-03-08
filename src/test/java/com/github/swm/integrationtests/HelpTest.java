package com.github.swm.integrationtests;

import com.github.swm.userclient.App;
import com.github.swm.userclient.commands.CommandResponse;
import groovy.util.ConfigObject;
import org.junit.Before;
import org.junit.Test;

import static org.hamcrest.core.IsEqual.equalTo;
import static org.junit.Assert.assertThat;

/**
 * Created by paul.smout on 05/03/2015.
 */
public class HelpTest {

    private App app;

    @Before
    public void setUp(){
        ConfigObject config = new ConfigObject();
        config.setProperty("serverAddress", System.getProperty("serverAddress","localhost:8080"));
        app = new App(config);


    }

    @Test
    public void help_should_not_throw_errors(){
       OperationsHelper ops = new OperationsHelper(app);
       ops.help();
    }

}
