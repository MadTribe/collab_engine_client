package com.github.swm.integrationtests.helpers;

import com.github.swm.userclient.App;
import com.github.swm.userclient.commands.CommandResponse;
import com.github.swm.userclient.commands.EntityOperations;

import java.util.List;

import static java.lang.String.format;
import static java.util.Arrays.asList;
import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;

/**
* Created by paul.smout on 08/03/2015.
*/
public class StepEventOperationsHelper {
    // final static Logger logger = LoggerFactory.getLogger(OperationsHelper.class);
    private App app;

    public StepEventOperationsHelper(App app) {
        this.app = app;
    }


    public CommandResponse I_create_a_new_script(String name, String source) {
        CommandResponse resp = app.runCommand(cmd(format("script -op %s -name %s -- %s", "NEW", name, source)));
        assertThat(resp.getSuccess(),is(true));
        return resp;
    }

    public CommandResponse I_edit_the_script(String name, String source) {
        CommandResponse resp = app.runCommand(cmd(format("script -op %s -name %s -- %s", EntityOperations.EDIT, name, source)));
        assertThat(resp.getSuccess(),is(true));
        return resp;
    }

    public CommandResponse I_list_my_scripts(String match) {
        CommandResponse resp = app.runCommand(cmd(format("script -op %s -name %s", EntityOperations.LIST_MATCHING, match)));
        assertThat(resp.getSuccess(),is(true));
        return resp;
    }

    public CommandResponse I_get_the_script_by_name(String name) {
        CommandResponse resp = app.runCommand(cmd(format("script -op %s -name %s", EntityOperations.SHOW_ONE, name)));
        assertThat(resp.getSuccess(),is(true));
        return resp;
    }

    private List<String> cmd(String cmd){
        return asList(cmd.split(" "));
    }
}
