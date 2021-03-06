package com.github.swm.userclient.commands.apiactions

import com.github.swm.userclient.commands.CommandResponse
import com.github.swm.userclient.http.Client

/**
 * Created by paul.smout on 24/03/2015.
 */
abstract class AbstractAPIGetAction implements ApiAction {


    abstract def apiEndPoint();

    abstract def formatOutput(def data, CommandResponse commandResponse);


    def CommandResponse go(Client client){
        println "GETTING ${apiEndPoint()}";
        CommandResponse ret = null;
        client.sendGet(apiEndPoint(),
                [],
                { resp, data ->
                    ret = success(data);
                },
                { resp, data ->
                    ret = fail(resp, data);
                });


        return ret;
    }

    @Override
    def CommandResponse success(data){
        def ret = new CommandResponse();
        ret.success = true;
        formatOutput(data, ret)
        ret.data = data;
        return ret;
    }


    @Override
    def CommandResponse fail(resp, data){
        def ret = new CommandResponse();
        ret.success = false;
        if (resp.statusLine.statusCode == 403){
            ret.output = "Access Denied";
        } else {
            ret.output = "Return code: ${resp.statusLine.statusCode} ${resp.statusLine.reasonPhrase} ${resp.data}";
        }
        return ret;
    }

}
