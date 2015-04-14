package com.github.swm.userclient.commands

import com.github.swm.userclient.commands.apiactions.AbstractAPIGetAction
import com.github.swm.userclient.context.CommandContext
import com.github.swm.userclient.http.Client
import groovy.transform.Canonical
import net.sf.json.JSONArray

import java.util.concurrent.Future

/**
 * Created by paul.smout on 20/02/2015.
 */
class ListPlansCmd extends Command {

    public ListPlansCmd(){
        super("plans","Lists all your plans." ,"plans ");
    }

    @Override
    def boolean accept(final List<String> cmd){
        boolean ret = false;

        if (cmd.size() > 0){
            if (cmd[0] == "plans"){
                ret = true
            }
        }
        return ret;
    }

    private ListPlansAction parseParams(params){
        ListPlansAction parsed = new ListPlansAction();

        return parsed;
    }

    @Override
    CommandResponse run(final List<String> cmd, CommandContext context) {

        Client client = context.getClient();
        List<String> params = cmd.subList(1,cmd.size());


        return  parseParams(params).go(client);;

    }


    @Canonical
    public static class ListPlansAction extends AbstractAPIGetAction {

        def apiEndPoint(){
            return "/api/plan";
        }

        def formatOutput(def data, CommandResponse commandResponse){
            commandResponse.output = "My Plans \n";
            JSONArray planList = data;
            planList.each { plan ->
                commandResponse.output += "${plan.id})  ${plan.name} - ${plan.description}  (Steps = ${plan.numSteps})\n"
            }
        }

    }

}
