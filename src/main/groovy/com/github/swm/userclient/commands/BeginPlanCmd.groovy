package com.github.swm.userclient.commands

import com.github.swm.userclient.commands.apiactions.AbstractAPIPostAction
import com.github.swm.userclient.context.CommandContext
import com.github.swm.userclient.http.Client
import joptsimple.NonOptionArgumentSpec
import joptsimple.OptionParser;
import groovy.transform.Canonical
import joptsimple.OptionSet

/**
 * Created by paul.smout on 20/02/2015.
 */
class BeginPlanCmd extends Command {

    OptionParser parser = new OptionParser();
    NonOptionArgumentSpec<String> nonOpts = null;

    public BeginPlanCmd(){
        super("beginPlan","Starts a new plan.", "beginPlan <planId>");
        nonOpts = parser.nonOptions("planId").ofType(Integer.class);
    }

    @Override
    def boolean accept(final List<String> cmd){
        boolean ret = false;
        if (cmd.size() > 0){
            if (cmd[0] == "beginPlan"){
                List<String> params = cmd.subList(1,cmd.size());
                ret = parseParams(params) != null;
            }
        }
        return ret;
    }

    private BeginPlanAction parseParams(List<String> params){
        BeginPlanAction action = null;

        String[] paramsArray = params.toArray(new String[params.size()]);

        OptionSet options = parser.parse(paramsArray);

        if (options.valuesOf(nonOpts).size() > 0){
            action = new BeginPlanAction(id: options.valuesOf(nonOpts).get(0));
        }

        return action;
    }

    @Override
    CommandResponse run(final List<String> cmd, CommandContext context) {

        Client client = context.getClient();
        List<String> params = cmd.subList(1,cmd.size());

        return parseParams(params).go(client);

    }

    @Canonical
    public static class BeginPlanAction extends AbstractAPIPostAction{
        def String id;


        def apiEndPoint(){
            return  "/api/plan/begin";
        }

        def buildRequestBody(){
            return [id: id];
        }

        def formatOutput(data,CommandResponse response){
            response.output = "Plan Started";
        }


    }

}
