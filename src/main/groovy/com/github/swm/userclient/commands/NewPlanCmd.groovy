package com.github.swm.userclient.commands

import com.github.swm.userclient.commands.apiactions.AbstractAPIPostAction
import com.github.swm.userclient.context.CommandContext
import com.github.swm.userclient.http.Client
import groovy.transform.Canonical

/**
 * Created by paul.smout on 20/02/2015.
 */
class NewPlanCmd extends Command {

    public NewPlanCmd(){
        super("newPlan","Creates a new plan.", "newPlan <name>  ~ <description>");
    }

    @Override
    def boolean accept(final List<String> cmd){
        boolean ret = false;
        if (cmd.size() > 0){
            if (cmd[0] == "newPlan"){
                List<String> params = cmd.subList(1,cmd.size());
                ret = parseParams(params) != null;
            }
        }
        return ret;
    }

    private NewPlanAction parseParams(params){
        def cmdString = params.join(" ");

        String name = "";
        String description = "";

        boolean readingName = true;
        boolean skip = false;
        cmdString.each{ c ->
            if (c == '~' ){
                readingName = false;
                skip = true;
            }

            if (!skip){
                if (readingName){
                    name += c;
                } else {
                    description += c;
                }
            }

            skip = false
        }


        NewPlanAction parsed = new NewPlanAction(name: name.trim(),description: description.trim());

        return parsed;
    }

    @Override
    CommandResponse run(final List<String> cmd, CommandContext context) {

        Client client = context.getClient();
        List<String> params = cmd.subList(1,cmd.size());

        return parseParams(params).go(client);

    }

    @Canonical
    public static class NewPlanAction extends AbstractAPIPostAction{
        def String name;
        def String description;

        def apiEndPoint(){
            return  "/api/plan";
        }

        def buildRequestBody(){
            return [name: name, description: description];
        }

        def formatOutput(data, CommandResponse response){
            response.output = "Plan Created with id ${data.id}";
        }

    }

}
