package com.github.swm.userclient.commands

import com.github.swm.userclient.commands.apiactions.AbstractAPIPostAction
import com.github.swm.userclient.context.CommandContext
import com.github.swm.userclient.http.Client
import groovy.transform.Canonical

/**
 * Created by paul.smout on 20/02/2015.
 */
class NewEventParamCmd extends Command {

    public NewEventParamCmd(){

        super("newEventParam","Creates a new event parameter.", "newStepEvent <owningEventId> <name> <type>");
    }

    @Override
    def boolean accept(final List<String> cmd){
        boolean ret = false;
        if (cmd.size() > 0){
            if (cmd[0] == "newEventParam"){

                ret = true;
            }
        }
        return ret;
    }

    private NewEventParamAction parseParams(params){

        def cmdString = params.join(" ");
        NewEventParamAction parsed = null;
        def matcher = cmdString =~ /([0-9]*) (\w++) (\w++)/
        if (matcher){

            Long owningEventId = Long.parseLong(matcher[0][1]);
            String paramName = matcher[0][2];
            String type = matcher[0][3];


            parsed = new NewEventParamAction(owningEventId: owningEventId, paramName: paramName, type: type);
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
    public static class NewEventParamAction extends AbstractAPIPostAction{
        def Long owningEventId;
        def String paramName;

        def String type;

        def apiEndPoint(){
            return "/api/events/${owningEventId}/params";
        }

        def buildRequestBody(){
            return  [owningEventId:owningEventId, paramName: paramName, type: type];
        }

        def formatOutput(data,CommandResponse response){
            response.output = "Event Param Created with id ${data.id}";
        }

    }

}
