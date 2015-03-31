package com.github.swm.userclient.commands

import com.github.swm.userclient.context.CommandContext
import com.github.swm.userclient.http.Client
import groovy.transform.Canonical

/**
 * Created by paul.smout on 20/02/2015.
 */
class NewStepEventCmd extends Command {

    public NewStepEventCmd(){

        super("newStepEvent","Creates a new plan step.", "newStepEvent <name> <owningStepId> <nextStepId>");
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
println "£££££££ ${params}"
        NewStepEventAction parsed = null;
        def matcher = cmdString =~ /(\w++) ([0-9]*) ([0-9]*)/
        if (matcher){

            String eventName = matcher[0][1];


            Long owningStepId = Long.parseLong(matcher[0][2]);


            Long nextStepId = null;

            try {
                nextStepId = Long.parseLong(matcher[0][3]);
            } catch (NumberFormatException nfe){

            }
            parsed = new NewStepEventAction(name: eventName, planStepId: owningStepId, nextStepId: nextStepId);
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
    public static class NewStepEventAction {
        def String name;
        def Long planStepId;
        def Long nextStepId;

        def CommandResponse go(Client client){
            CommandResponse ret = null;
            String path = "/api/plan/event";
            client.sendPost(path,
                            [name:name, planStepId: planStepId, nextStepId: nextStepId],
                            { resp, data ->
                                ret = success(data);
                            },
                            { resp ->
                                ret = fail(resp);
                            });


            return ret;
        }

       def CommandResponse success(data){
            def ret = new CommandResponse();
            ret.success = true;
            ret.output = "Event Created";
            ret.data = data;
            return ret;
       }

        def CommandResponse fail(resp){
            def ret = new CommandResponse();
            ret.success = false;
            if (resp.statusLine.statusCode == 403){
                ret.output = "Access Denied";
            } else {
                ret.output = "Return code: ${resp.statusLine.statusCode} ${resp.statusLine.reasonPhrase}";
            }
            return ret;
        }

    }

}
