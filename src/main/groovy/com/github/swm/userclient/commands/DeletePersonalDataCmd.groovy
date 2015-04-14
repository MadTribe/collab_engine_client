package com.github.swm.userclient.commands

import com.github.swm.userclient.commands.apiactions.AbstractAPIPostAction
import com.github.swm.userclient.context.CommandContext
import com.github.swm.userclient.http.Client
import groovy.transform.Canonical

/**
 * Created by paul.smout on 20/02/2015.
 */
class DeletePersonalDataCmd extends Command {

    public DeletePersonalDataCmd(){
        super("deleteAll","Deletes all your data irretrievably. (Use if you are feeling stupid)", "deleteAll");
    }

    @Override
    def boolean accept(final List<String> cmd){
        boolean ret = false;
        if (cmd.size() > 0){
            if (cmd[0] == "deleteAll"){
                List<String> params = cmd.subList(1,cmd.size());
                ret = parseParams(params) != null;
            }
        }
        return ret;
    }

    private DeleteAllAction parseParams(params){


        DeleteAllAction parsed = new DeleteAllAction();

        return parsed;
    }

    @Override
    CommandResponse run(final List<String> cmd, CommandContext context) {

        Client client = context.getClient();
        List<String> params = cmd.subList(1,cmd.size());

        return parseParams(params).go(client);

    }


    @Canonical
    public static class DeleteAllAction extends AbstractAPIPostAction{


        def apiEndPoint(){
            return  "/api/teardown";
        }

        def buildRequestBody(){
            return [:];
        }

        def formatOutput(data,CommandResponse response){
            response.output = "Content Deleted";
        }

    }

}
