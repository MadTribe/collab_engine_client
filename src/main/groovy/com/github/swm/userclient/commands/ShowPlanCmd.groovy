package com.github.swm.userclient.commands

import com.github.swm.userclient.commands.apiactions.AbstractAPIGetAction
import com.github.swm.userclient.context.CommandContext
import com.github.swm.userclient.http.Client
import groovy.transform.Canonical
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
    public static class ShowPlanAction extends AbstractAPIGetAction {
        def int planId;

        def apiEndPoint(){
            return "/api/plan/" + planId;
        }

        def formatOutput(def data, CommandResponse commandResponse){
            commandResponse.output = "My Plans \n";
            JSONObject plan = data;
            commandResponse.output += "${plan.id})  ${plan.name} - ${plan.description}  (Steps = ${plan.steps?.size()})\n"

            plan.steps.each{ step ->
                commandResponse.output += "     ${step.name}  ${step.description} \n";
            }
        }

    }


}
