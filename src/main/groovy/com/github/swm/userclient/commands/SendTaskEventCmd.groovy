package com.github.swm.userclient.commands

import com.github.swm.userclient.commands.apiactions.AbstractAPIPostAction
import com.github.swm.userclient.context.CommandContext
import com.github.swm.userclient.http.Client
import groovy.transform.Canonical
import net.sf.json.JSON
import net.sf.json.JSONSerializer
import net.sf.json.JsonConfig

/**
 * Created by paul.smout on 20/02/2015.
 */
class SendTaskEventCmd extends Command {


    public SendTaskEventCmd(){
        super("event","Sends a Task Event.", "event <taskId> <eventName> <json parameter string>");
    }

    @Override
    def boolean accept(final List<String> cmd){
        println "accept send event "

        boolean ret = false;
        if (cmd.size() > 0){
            if (cmd[0] == name){
                ret = true;
            }
        }
        return ret;
    }

    private SendEventAction parseParams(List<String> params){
        SendEventAction action = null;

        int tasdId =  getIntParam(params, 0, "taskId");
        String eventName = getStringParam(params, 1, "eventName");
        String parameters = getRemainingParams(params, 2, "paramString");


        action = new SendEventAction(tasdId, eventName, parameters);

        return action;
    }


    @Override
    CommandResponse run(final List<String> cmd, CommandContext context) {

        Client client = context.getClient();
        List<String> params = cmd.subList(1,cmd.size());

        return parseParams(params).go(client);
    }

    @Canonical
    public static class SendEventAction extends AbstractAPIPostAction{
        def int taskId;
        def String eventName;
        def paramString = "";

        def apiEndPoint(){
            return  "/api/events/task";
        }

        def buildRequestBody(){
            JSONSerializer serializer = new JSONSerializer();

            JSON json = serializer.toJSON(paramString, new JsonConfig());

            return [taskId: taskId, eventName: eventName, params: json];
        }

        def formatOutput(data,CommandResponse response){
            response.output = "Event Response  <<<" + data + ">>";
        }


    }

}
