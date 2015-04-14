package com.github.swm.userclient.commands

import com.github.swm.userclient.context.CommandContext
import groovy.transform.Canonical

import java.util.concurrent.Future

/**
 * Created by paul.smout on 20/02/2015.
 */
@Canonical
abstract class Command {
    def name;
    def description;
    def usage;

    def abstract boolean accept(List<String> cmd);


    def abstract CommandResponse run(List<String> cmd, CommandContext context);

    def parseNamedParams(){

    }

    def String getRemainingParams(List<String> params, int idx , String name){
        StringBuffer ret = new StringBuffer();
        if (params.size() > idx){
            for (int i = idx; i < params.size(); i++){
                ret.append(params[i]);
            }
        }
        return ret.toString();
    }

    def String getStringParam(List<String> params, int idx , String name){
        String ret = null;
        if (params.size() > idx){
            ret = params[idx]
        } else {
            throw new ParameterNotFound(name);
        }
        return ret;
    }

    def int getIntParam(List<String> params, int idx , String name){
        Integer ret = null;
        if (params.size() > idx){
            println params[idx]
            try {
                ret = Integer.valueOf(params[idx])
            } catch (NumberFormatException nfe){
                throw new ParameterFormatException(name);
            }
        } else {
            throw new ParameterNotFound(name);
        }
        return ret;
    }

}
