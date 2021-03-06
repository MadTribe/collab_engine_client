package com.github.swm.userclient.commands

import com.github.swm.userclient.commands.apiactions.AbstractAPIPostAction
import com.github.swm.userclient.context.CommandContext
import com.github.swm.userclient.http.Client
import groovy.transform.Canonical

/**
 * Created by paul.smout on 20/02/2015.
 */
class NewPlanStepCmd extends Command {

    public NewPlanStepCmd(){
        super("newStep","Creates a new plan step.", "newStep <planId> <step-name> ~ <step-description>");
    }

    @Override
    def boolean accept(final List<String> cmd){
        boolean ret = false;
        if (cmd.size() > 0){
            if (cmd[0] == "newStep"){
                List<String> params = cmd.subList(1,cmd.size());
                ret = parseParams(params) != null;
            }
        }
        return ret;
    }

    private NewPlanStepAction parseParams(params){
        def cmdString = params.join(" ");

        NewPlanStepAction parsed = null;
        def matcher = cmdString =~ /\s*+(\d++) ([\w*\W*]*)~([\w*+\W*+]*)/
        if (matcher){
            Long planId = Long.parseLong(matcher[0][1]);
            String name = matcher[0][2];
            String description = matcher[0][3];
            parsed = new NewPlanStepAction(planId: planId, name: name?.trim(),description: description?.trim());
        }



        return parsed;
    }

    @Override
    CommandResponse run(final List<String> cmd, CommandContext context) {

        Client client = context.getClient();
        List<String> params = cmd.subList(1,cmd.size());
        def ret = null;
        def action =  parseParams(params);
        if (action){
            ret = action.go(client);
        } else {
            ret = new CommandResponse(success: false, output: "unable to parse command",data: null);
        }

        return ret;
    }


    @Canonical
    public static class NewPlanStepAction extends AbstractAPIPostAction{
        def Long planId;
        def String name;
        def String description;

        def apiEndPoint(){
            return "/api/plan/" + planId + "/step";
        }

        def buildRequestBody(){
            return [name:name,description: description];
        }

        def formatOutput(data,CommandResponse response){
            response.output = "Step Created";
        }

    }

}
