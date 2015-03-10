package com.github.swm.userclient.commands

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
    public static class NewPlanAction{
        def String name;
        def String description;

        def CommandResponse go(Client client){
            CommandResponse ret = null;
            client.sendPost("/api/plan",
                            [name:name,description: description],
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
            ret.output = "Plan Created";
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
