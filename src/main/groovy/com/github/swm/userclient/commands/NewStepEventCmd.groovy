package com.github.swm.userclient.commands

import com.github.swm.userclient.commands.apiactions.AbstractAPIPostAction
import com.github.swm.userclient.context.CommandContext
import com.github.swm.userclient.http.Client
import groovy.transform.Canonical

/**
 * Created by paul.smout on 20/02/2015.
 */
class NewStepEventCmd extends Command {

    public NewStepEventCmd(){

        super("newStepEvent","Creates a new plan step.", "newStepEvent <name> <type> <owningStepId> <nextStepId>");
    }

    @Override
    def boolean accept(final List<String> cmd){
        boolean ret = false;
        if (cmd.size() > 0){
            if (cmd[0] == "newStepEvent"){
                List<String> params = cmd.subList(1,cmd.size());
                ret = parseParams(params) != null;
            }
        }
        return ret;
    }

    private NewStepEventAction parseParams(params){
        def cmdString = params.join(" ");
        NewStepEventAction parsed = null;
        def matcher = cmdString =~ /(\w++) (\w++) ([0-9]*) ([0-9]*)/
        if (matcher){

            String eventName = matcher[0][1];
            String eventValidator = matcher[0][2];
            Long owningStepId = Long.parseLong(matcher[0][3]);
            Long nextStepId = null;

            try {
                nextStepId = Long.parseLong(matcher[0][4]);
            } catch (NumberFormatException nfe){

            }
            parsed = new NewStepEventAction(name: eventName, eventValidator: eventValidator, planStepId: owningStepId, nextStepId: nextStepId);
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
    public static class NewStepEventAction extends AbstractAPIPostAction{
        def String name;
        def String eventValidator;
        def Long planStepId;
        def Long nextStepId;

        def apiEndPoint(){
            return "/api/plan/event";
        }

        def buildRequestBody(){
            return  [name:name, eventValidator: eventValidator, planStepId: planStepId, nextStepId: nextStepId];
        }

        def formatOutput(data,CommandResponse response){
            response.output = "Event Created with id ${data.id}";
        }

    }

}
