package com.github.swm.userclient.commands.entitymanagement

import com.github.swm.userclient.commands.CommandResponse
import com.github.swm.userclient.commands.EntityOperations
import com.github.swm.userclient.commands.entitymanagement.AbstractEntityCommand
import joptsimple.OptionParser
import joptsimple.OptionSet
import joptsimple.OptionSpec
import net.sf.json.JSONArray
import net.sf.json.JSONObject

/**
 * prototypical REST command for scripts
 * Created by paul.smout on 20/02/2015.
 */
class ScriptCmd extends AbstractEntityCommand {


    private OptionSpec<String> nameOptionSpec;

    public ScriptCmd(){
        super("script","manages scripts.", "script [-op NEW, EDIT] -name <name> -- <source> \n script [LIST_MATCHING] -name <name> -- <source> \n script DELETE -name <name>");

    }

    @Override
    protected void configureOptions(){
        nameOptionSpec = parser.accepts( "name" ).withOptionalArg().ofType(String.class);
    }

    @Override
    def buildEntity(OptionSet options){
        String name = getName(options);

        String source = options.nonOptionArguments().join(" ");

        checkParam(source,"source");

        return [name: name, source: source]
    }


    def String getName(OptionSet options){
        String name = null;
        if(options.has(nameOptionSpec)){
            name = options.valueOf(nameOptionSpec);
        }
        checkParam(name,"name");

        return name;
    }

    @Override
    def listOneParams(OptionSet options){
        String name = getName(options);
        return [name: name]
    }

    @Override
    def listMatchingParams(OptionSet options){
        String name = null;
        if(options.has(nameOptionSpec)){
            name = options.valueOf(nameOptionSpec);
        }
        if (name == null){
            name = "";
        }

        return [name: name.trim()]
    }

    @Override
    def String getBaseUrl() {
        return "/api/script";
    }

    /************ FORMAT OUTPUT **************/
    @Override
    protected void formatEditItemOutput(CommandResponse response) {
        response.output = "Script Updated.";
    }

    @Override
    protected void formatNewItemOutput(data, CommandResponse response) {
        response.output = "Script Created with id ${data.id}";
    }

    @Override
    protected void formatShowItemOutput(def data, CommandResponse commandResponse){
        commandResponse.output = "Got Script \n";
        JSONObject script = data;
        commandResponse.output += "(${script.id})  ${script.name} \n ${script.scriptContent}\n"
    }

    @Override
    protected void formatListItemsOutput(CommandResponse commandResponse, data) {
        commandResponse.output = "My Scripts \n";
        JSONArray scriptList = data.items;
        scriptList.each { script ->
            commandResponse.output += "(${script.id})  ${script.name} \n"
        }
    }
}
