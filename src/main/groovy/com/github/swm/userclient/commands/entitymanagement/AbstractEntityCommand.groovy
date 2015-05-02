package com.github.swm.userclient.commands.entitymanagement

import com.github.swm.userclient.commands.Command
import com.github.swm.userclient.commands.CommandResponse
import com.github.swm.userclient.commands.EntityOperations;
import com.github.swm.userclient.commands.apiactions.AbstractAPIGetAction;
import com.github.swm.userclient.commands.apiactions.AbstractAPIPostAction;
import com.github.swm.userclient.commands.apiactions.AbstractAPIPutAction
import com.github.swm.userclient.commands.apiactions.ApiAction;
import com.github.swm.userclient.context.CommandContext
import com.github.swm.userclient.http.Client;
import groovy.transform.Canonical
import joptsimple.OptionParser
import joptsimple.OptionSet
import joptsimple.OptionSpec;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject

/**
 * Created by paul.smout on 21/04/2015.
 */
public abstract class AbstractEntityCommand extends Command{
    OptionParser parser = new OptionParser();
    OptionSpec<EntityOperations> operationsOptionSpec;

    public AbstractEntityCommand(String name, String description, String usage) {
        super(name, description, usage)
        this.parser = parser
        operationsOptionSpec = parser.accepts( "op" ).withRequiredArg().ofType( EntityOperations.class ).defaultsTo("LIST_MATCHING");
        configureOptions();
    }

    protected abstract void configureOptions();

    protected AbstractEntityCommand(Object name, Object description, Object usage) {
        super(name, description, usage);
    }

    def abstract String getBaseUrl();

    def makeNewItemAction(Map params){
        new NewItemAction(entity: params);
    }

    def makeEditItemAction(Map params){
        new EditItemAction(entity: params);
    }

    def makeListItemAction(Map params){
        new ListItemsAction(params.name);
    }

    def makeShowItemAction(Map params){
        new ShowItemAction(params.name);
    }


    @Override
    def boolean accept(final List<String> cmd){
        boolean ret = false;
        if (cmd.size() > 0){
            if (cmd[0] == this.name){
                List<String> params = cmd.subList(1,cmd.size());
                ret = parseParams(params) != null;
            }
        }
        return ret;
    }

    protected ApiAction parseParams(List<String> params){
        ApiAction ret = null;
        String[] paramArray = params as String[];
        System.err.println(paramArray);

        OptionSet options = parser.parse(paramArray);

        if (options.valueOf(operationsOptionSpec) == EntityOperations.NEW) {
            ret = makeNewItemAction(buildEntity(options));
        } else if (options.valueOf(operationsOptionSpec) == EntityOperations.EDIT) {
            ret = makeEditItemAction(buildEntity(options));
        } else if (options.valueOf(operationsOptionSpec) == EntityOperations.SHOW_ONE) {
            ret = makeShowItemAction(listOneParams(options));
        } else if (options.valueOf(operationsOptionSpec) == EntityOperations.LIST_MATCHING){
            ret = makeListItemAction(listMatchingParams(options));
        }

        return ret;
    }

    def abstract buildEntity(OptionSet options);

    def abstract String getName(OptionSet options);

    def abstract listOneParams(OptionSet options);

    def abstract  listMatchingParams(OptionSet options);

    @Override
    CommandResponse run(final List<String> cmd, CommandContext context) {

        Client client = context.getClient();
        List<String> params = cmd.subList(1,cmd.size());

        return parseParams(params).go(client);

    }

    @Canonical
    class NewItemAction extends AbstractAPIPostAction {
        def entity = [:]


        def apiEndPoint(){
            return  getBaseUrl();
        }

        def buildRequestBody(){
            return entity;
        }

        def formatOutput(data, CommandResponse response){
            formatNewItemOutput(data, response)
        }


    }

    @Canonical
    class EditItemAction extends AbstractAPIPutAction {
        def entity = [:]


        def apiEndPoint(){
            return  getBaseUrl();
        }

        def buildRequestBody(){
            return entity;
        }

        def formatOutput(data, CommandResponse response){
            formatEditItemOutput(response)
        }

    }

    @Canonical
    class ListItemsAction extends AbstractAPIGetAction {
        def name;

        def apiEndPoint(){
            return getBaseUrl() + "/summary/%${name}";
        }

        def formatOutput(def data, CommandResponse commandResponse){
            formatListItemsOutput(commandResponse, data)
        }

    }

    @Canonical
    class ShowItemAction extends AbstractAPIGetAction {
        def name;

        def apiEndPoint(){
            return getBaseUrl() + "/${name}";
        }

        def formatOutput(def data, CommandResponse commandResponse){
            formatShowItemOutput(data, commandResponse)
        }

    }

    // TODO PUSH DOWN
    abstract protected void formatEditItemOutput(CommandResponse response);

    abstract protected void formatNewItemOutput(data, CommandResponse response);

    abstract protected void formatShowItemOutput(def data, CommandResponse commandResponse);

    abstract protected void formatListItemsOutput(CommandResponse commandResponse, data);
}
