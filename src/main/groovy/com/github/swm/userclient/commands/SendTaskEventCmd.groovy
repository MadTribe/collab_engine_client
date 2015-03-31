package com.github.swm.userclient.commands

import com.github.swm.userclient.commands.apiactions.AbstractAPIPostAction
import com.github.swm.userclient.context.CommandContext
import com.github.swm.userclient.http.Client
import groovy.transform.Canonical
import joptsimple.NonOptionArgumentSpec
import joptsimple.OptionParser
import joptsimple.OptionSet

/**
 * Created by paul.smout on 20/02/2015.
 */
class SendTaskEventCmd extends Command {


    public SendTaskEventCmd(){
        super("event","Sends a Task Event.", "event <taskId> <eventName>");
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

        action = new SendEventAction(tasdId, eventName);

        return action;
    }


    def String getStringParam(List<String> params, int idx , String name){
        String ret = null;
        if (params.size() > idx){
            ret = params[idx]
        } else {
            throw new ParameterNotFound(name);
        }
        return ret;
    }

    def int getIntParam(List<String> params, int idx , String name){
        Integer ret = null;
        if (params.size() > idx){
            println params[idx]
            try {
              ret = Integer.valueOf(params[idx])
            } catch (NumberFormatException nfe){
                throw new ParameterFormatException(name);
            }
        } else {
            throw new ParameterNotFound(name);
        }
        return ret;
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

        def apiEndPoint(){
            return  "/api/events/task";
        }

        def buildRequestBody(){
            return [taskId: taskId, eventName: eventName ];
        }

        def formatOutput(data,CommandResponse response){
            response.output = "Event Sent";
        }


    }

}
