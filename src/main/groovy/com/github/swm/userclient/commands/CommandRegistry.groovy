package com.github.swm.userclient.commands

import com.github.swm.userclient.commands.entitymanagement.ScriptCmd
import com.github.swm.userclient.commands.entitymanagement.StepEventCmd

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
                                    new NewPlanStepCmd(),
                                    new ShowPlanCmd(),
                                    new NewStepEventCmd(), // soon to be deprecated
                                    new StepEventCmd(),
                                    new BeginPlanCmd(),
                                    new ListTasksCmd(),
                                    new SendTaskEventCmd(),
                                    new NewEventParamCmd(),
                                    new ScriptCmd())

    public List<Command> getCommands(){
        return commands;
    }
}
