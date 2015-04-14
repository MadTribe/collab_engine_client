package com.github.swm.userclient.commands

import com.github.swm.userclient.commands.apiactions.AbstractAPIPostAction
import com.github.swm.userclient.context.CommandContext
import com.github.swm.userclient.http.Client
import groovy.transform.Canonical


/**
 * Created by paul.smout on 20/02/2015.
 */
class LoginCmd extends Command {

    public LoginCmd(){
        super("login","Lets you log in for 1 session.", "login <username> <password>");
    }

    @Override
    def boolean accept(final List<String> cmd){
        boolean ret = false;

        if (cmd.size() > 0){
            if (cmd[0] == "login"){
                List<String> params = cmd.subList(1,cmd.size());
                ret = parseParams(params) != null;
            }
        }
        return ret;
    }

    private LoginAction parseParams(params){
        String uname = params[0];
        String password = params[1];
        LoginAction parsed = new LoginAction(userName: uname,password: password);

        return parsed;
    }

    @Override
    CommandResponse run(final List<String> cmd, CommandContext context) {

        Client client = context.getClient();
        List<String> params = cmd.subList(1,cmd.size());

        return parseParams(params).go(client);

    }

    @Canonical
    public static class LoginAction extends AbstractAPIPostAction{
        def String userName;
        def String password;

        def apiEndPoint(){
            return  "/login";
        }

        def buildRequestBody(){
            return [userName:userName,password: password];
        }

        def formatOutput(data,CommandResponse response){
            response.output = "Login Successful";
        }

    }


}
