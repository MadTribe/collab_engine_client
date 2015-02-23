package com.github.swm.userclient.commands

import com.github.swm.userclient.context.CommandContext
import com.github.swm.userclient.http.Client
import groovy.transform.Canonical

/**
 * Created by paul.smout on 20/02/2015.
 */
class HelpCmd extends Command {

    public HelpCmd(){
        super("help","help");
    }

    @Override
    def boolean accept(final List<String> cmd){
        // TODO add logging framework [t:1h]]
        boolean ret = false;

        if (cmd.size() > 0){
            if (cmd[0].equalsIgnoreCase("help")){
                ret = true;
            }
        }
        return ret;
    }



    @Override
    CommandResponse run(final List<String> cmd, CommandContext context) {
        // TODO read commands on classpath and generate usage string [t:1.5h]
        // TODO sort out command progressive output and return codes [t:1.5h]
        println "Some help you are";
        return new CommandResponse(output: "Some help you are");

    }



}
