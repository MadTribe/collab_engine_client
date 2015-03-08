package com.github.swm.userclient.commands

import static java.util.Arrays.asList

/**
 * Created by paul.smout on 08/03/2015.
 */
class CommandRegistry {
    List<Command> commands = asList(new LoginCmd(),
                                    new HelpCmd(this),
                                    new ListPlansCmd(),
                                    new NewPlanCmd(),
                                    new DeletePersonalDataCmd(),
                                    new NewPlanStepCmd())

    public List<Command> getCommands(){
        return commands;
    }
}
