package com.github.swm.userclient.commands

import com.github.swm.userclient.context.CommandContext
import com.github.swm.userclient.http.Client
import groovy.transform.Canonical
import net.sf.json.JSONArray

/**
 * Created by paul.smout on 20/02/2015.
 */
class ListTasksCmd extends Command {

    public ListTasksCmd(){
        super("tasks","Lists all your tasks." ,"plans ");
    }

    @Override
    def boolean accept(final List<String> cmd){
        boolean ret = false;

        if (cmd.size() > 0){
            if (cmd[0] == name){
                ret = true
            }
        }
        return ret;
    }

    private ListTasksAction parseParams(params){
        ListTasksAction parsed = new ListTasksAction();

        return parsed;
    }

    @Override
    CommandResponse run(final List<String> cmd, CommandContext context) {
        Client client = context.getClient();
        List<String> params = cmd.subList(1,cmd.size());


        return  parseParams(params).go(client);;

    }

    @Canonical
    public static class ListTasksAction {

        def go(Client client){
            CommandResponse ret = null;
            client.sendGet("/api/task",[],{ resp, data ->
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
            ret.output = "My Tasks \n";
            ret.data = data;

            JSONArray taskList = data;
            taskList.each { task ->
                ret.output += "${task.id})  ${task.name} - ${task.description} \n"

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
