package com.github.swm.userclient.commands

import com.github.swm.userclient.context.CommandContext
import groovy.transform.Canonical

/**
 * Created by paul.smout on 20/02/2015.
 */
@Canonical
abstract class Command {
    def name;
    def usage;

    def abstract boolean accept(List<String> cmd);


    def abstract CommandResponse run(List<String> cmd, CommandContext context);

}
