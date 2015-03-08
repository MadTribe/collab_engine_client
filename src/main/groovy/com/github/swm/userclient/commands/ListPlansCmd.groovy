package com.github.swm.userclient.commands

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
    public static class ListPlansAction{

        def go(Client client){
            CommandResponse ret = null;
            client.sendGet("/api/plan",[],{ resp, data ->
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
            ret.output = "My Plans \n";
            ret.data = data;

            JSONArray planList = data;
            planList.each { plan ->
                ret.output += "${plan.id}  ${plan.name} ${plan.description}\n"

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
