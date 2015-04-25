package com.github.swm.userclient.commands.entitymanagement

import com.github.swm.userclient.commands.Command
import com.github.swm.userclient.commands.CommandResponse
import com.github.swm.userclient.commands.apiactions.AbstractAPIPostAction
import com.github.swm.userclient.context.CommandContext
import com.github.swm.userclient.http.Client
import groovy.transform.Canonical
import joptsimple.OptionSet
import joptsimple.OptionSpec

/**
 * Created by paul.smout on 20/02/2015.
 */
class StepEventCmd extends AbstractEntityCommand {

    private OptionSpec<String> nameOptionSpec;
    private OptionSpec<String> typeOptionSpec;
    private OptionSpec<Long> owningStepIdOptionSpec;
    private OptionSpec<Long> nextStepIdOptionSpec;
    private OptionSpec<String> eventValidatorOptionSpec;
    private OptionSpec<String> eventHandlerOptionSpec;


    public StepEventCmd(){

        super("stepEvent","Manages plan steps.", "stepEvent -op [-op NEW, EDIT, LIST_MATCHING, DELETE] -name <name> -type <type> -stepId <owningStepId> -nextStep <nextStepId>");
    }

    @Override
    protected void configureOptions() {
        nameOptionSpec = parser.accepts( "name" ).withOptionalArg().ofType(String.class);

        owningStepIdOptionSpec = parser.accepts( "stepId" ).withOptionalArg().ofType(Long.class);
        nextStepIdOptionSpec = parser.accepts( "nextStepId" ).requiredIf(owningStepIdOptionSpec).withOptionalArg().ofType(Long.class);
        eventValidatorOptionSpec = parser.accepts( "eventValidator" ).requiredIf(owningStepIdOptionSpec).withRequiredArg().ofType(String.class);
        eventHandlerOptionSpec = parser.accepts( "eventHandler" ).requiredIf(owningStepIdOptionSpec).withRequiredArg().ofType(String.class);

     //   typeOptionSpec = parser.accepts( "type" ).requiredIf(owningStepIdOptionSpec).withRequiredArg().ofType(String.class);
    }

    @Override
    String getBaseUrl() {
        return "/api/plan/event";
    }

    @Override
    def boolean accept(final List<String> cmd){
        boolean ret = false;
        if (cmd.size() > 0){
            if (cmd[0] == name){
                List<String> params = cmd.subList(1,cmd.size());
                ret = parseParams(params) != null;
            }
        }
        return ret;
    }

    @Override
    def buildEntity(OptionSet options) {
        return [name:options.valueOf(nameOptionSpec),
                eventValidator: options.valueOf(eventValidatorOptionSpec),
                planStepId: options.valueOf(owningStepIdOptionSpec),
                nextStepId: options.valueOf(nextStepIdOptionSpec),
                handlerName: options.valueOf(eventHandlerOptionSpec)];
    }

    @Override
    String getName(OptionSet options) {
        return null
    }

    @Override
    def listOneParams(OptionSet options) {
        return null
    }

    @Override
    def listMatchingParams(OptionSet options) {
        return null
    }

    @Override
    protected void formatEditItemOutput(CommandResponse response) {

    }

    @Override
    protected void formatNewItemOutput(def data, CommandResponse response) {
        response.output = "Event Created with id ${data.id}";
    }

    @Override
    protected void formatShowItemOutput(def data, CommandResponse commandResponse) {

    }

    @Override
    protected void formatListItemsOutput(CommandResponse commandResponse, Object data) {

    }
}
