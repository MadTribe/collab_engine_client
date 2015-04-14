package com.github.swm.userclient.commands.apiactions

import com.github.swm.userclient.commands.CommandResponse
import com.github.swm.userclient.http.Client

/**
 * Created by paul.smout on 24/03/2015.
 */
abstract class AbstractAPIGetAction {


    abstract def apiEndPoint();

    abstract def formatOutput(def data, CommandResponse commandResponse);

    def CommandResponse go(Client client){
        CommandResponse ret = null;
        client.sendGet(apiEndPoint(),
                [],
                { resp, data ->
                    ret = success(data);
                },
                { resp ->
                    ret = fail(resp);
                });


        return ret;
    }

    def CommandResponse success(data){
        def ret = new CommandResponse();
        ret.success = true;
        formatOutput(data, ret)
        ret.data = data;
        return ret;
    }


    def CommandResponse fail(resp){
        def ret = new CommandResponse();
        ret.success = false;
        if (resp.statusLine.statusCode == 403){
            ret.output = "Access Denied";
        } else {
            ret.output = "Return code: ${resp.statusLine.statusCode} ${resp.statusLine.reasonPhrase}";
        }
        return ret;
    }

}
