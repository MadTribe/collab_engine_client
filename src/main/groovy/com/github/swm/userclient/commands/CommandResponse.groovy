package com.github.swm.userclient.commands

import groovy.transform.Canonical
import net.sf.json.JSONObject

/**
 * Created by paul.smout on 20/02/2015.
 */
@Canonical
class CommandResponse {
    def boolean success;
    def String output;
    def net.sf.json.JSON data;
}
