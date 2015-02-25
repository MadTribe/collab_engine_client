package com.github.swm.userclient.commands

import com.github.swm.userclient.context.CommandContext
import com.github.swm.userclient.http.Client
import groovy.transform.Canonical

import java.util.concurrent.Future

/**
 * Created by paul.smout on 20/02/2015.
 */
class ListPlansCmd extends Command {

    public ListPlansCmd(){
        super("plans", "plans ");
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

        parseParams(params).go(client);

        return null;

    }

    @Canonical
    public static class ListPlansAction{

        def go(Client client){
            client.sendGet("/api/plan",[], { resp, data -> println data;}, null);
        }
    }

}
