package com.github.swm.userclient.commands

import com.github.swm.userclient.context.CommandContext
import com.github.swm.userclient.http.Client
import groovy.transform.Canonical
import net.sf.json.JSONArray
import net.sf.json.JSONObject

/**
 * Created by paul.smout on 20/02/2015.
 */
class ShowPlanCmd extends Command {

    public ShowPlanCmd(){
        super("showPlan","Shows the steps in one plan." ,"showPlan <id>");
    }

    @Override
    def boolean accept(final List<String> cmd){
        boolean ret = false;

        if (cmd.size() > 0){
            if (cmd[0] == "showPlan"){
                ret = true
            }
        }
        return ret;
    }

    private ShowPlanAction parseParams(params){
        ShowPlanAction parsed = null;

        if(params.size() > 0){
            parsed = new ShowPlanAction(planId: Integer.parseInt(params[0]));
        }

        return parsed;
    }

    @Override
    CommandResponse run(final List<String> cmd, CommandContext context) {
        Client client = context.getClient();
        List<String> params = cmd.subList(1,cmd.size());


        return  parseParams(params)?.go(client);;

    }

    @Canonical
    public static class ShowPlanAction{

        def int planId;

        def go(Client client){
            CommandResponse ret = null;
            client.sendGet("/api/plan/" + planId,[],{ resp, data ->
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
            ret.output = "Plan \n";
            ret.data = data;

            JSONObject plan = data;

            ret.output += "${plan.id})  ${plan.name} - ${plan.description}  (Steps = ${plan.numSteps})\n"

            plan.steps.each{ step ->
                ret.output += "     ${step.name}  ${step.description} \n";
            }


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
