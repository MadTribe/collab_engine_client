package com.github.swm.userclient.commands.apiactions

import com.github.swm.userclient.commands.CommandResponse
import com.github.swm.userclient.http.Client

/**
 * Created by paul.smout on 24/03/2015.
 */
abstract class AbstractAPIPostAction implements ApiAction {

    abstract def buildRequestBody();

    abstract def apiEndPoint();

    abstract def formatOutput(def data, CommandResponse commandResponse);

    def CommandResponse go(Client client){
        println "POSTING TO ${apiEndPoint()} : ${buildRequestBody()}";

        CommandResponse ret = null;
        client.sendPost(apiEndPoint(),
                buildRequestBody(),
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
        println resp.data;
        def ret = new CommandResponse();
        ret.success = false;
        if (resp.statusLine.statusCode == 403){
            ret.output = "Access Denied";
        } else {
            ret.output = "Return code: ${resp.statusLine.statusCode} ${resp.statusLine.reasonPhrase} ${data.message}";
        }
        return ret;
    }

}
