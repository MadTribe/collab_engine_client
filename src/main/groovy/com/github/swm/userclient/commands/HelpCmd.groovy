package com.github.swm.userclient.commands

import com.github.swm.userclient.context.CommandContext
import com.github.swm.userclient.http.Client
import groovy.transform.Canonical

import java.util.concurrent.Future

/**
 * Created by paul.smout on 20/02/2015.
 */
class HelpCmd extends Command {
    CommandRegistry commandRegistry;

    public HelpCmd(CommandRegistry cmds){
        super("help","Gives usage information for a command.","help or ?  <command name>");
        this.commandRegistry = cmds;
    }

    @Override
    def boolean accept(final List<String> cmd){
        // TODO add logging framework [t:1h]]
        boolean ret = false;

        if (cmd.size() > 0){
            if (cmd[0].equalsIgnoreCase("help") || cmd[0].equals("?")){
                ret = true;
            }
        }
        return ret;
    }



    @Override
    CommandResponse run(final List<String> cmd, CommandContext context) {
        // TODO read commands on classpath and generate usage string [t:1.5h]
        // TODO sort out command progressive output and return codes [t:1.5h]
        def ret = new CommandResponse()
        def foundCmd = null
        ret.output = "";
        if (cmd.size()){
            foundCmd =  commandRegistry.getCommands().find{ it.name == cmd[1]}
        }
        if (foundCmd){
            ret.output = foundCmd.usage;
        } else {
            commandRegistry.commands.each { c -> ret.output += "${c.name}   -  ${c.description}\n"}
        }
        ret.success = true;

        return ret;

    }



}
