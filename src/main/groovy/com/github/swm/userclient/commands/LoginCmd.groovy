package com.github.swm.userclient.commands

import com.github.swm.userclient.context.CommandContext
import com.github.swm.userclient.http.Client
import groovy.transform.Canonical

/**
 * Created by paul.smout on 20/02/2015.
 */
class LoginCmd extends Command {

    public LoginCmd(){
        super("login", "login <username> <password>");
    }

    @Override
    def boolean accept(final List<String> cmd){
        boolean ret = false;

        if (cmd.size() > 0){
            if (cmd[0] == "login"){
                List<String> params = cmd.subList(1,cmd.size());
println params;
                ret = parseParams(params) != null;
            }
        }
        return ret;
    }

    private LoginAction parseParams(params){
        String uname = params[0];
        String password = params[1];
        println "${uname} ${password}"
        LoginAction parsed = new LoginAction(userName: uname,password: password);

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
    public static class LoginAction{
        def String userName;
        def String password;

        def go(Client client){
            client.sendPost("/api/login",[userName:userName,password: password],{ resp, data -> println data;}, null);
        }
    }

}
