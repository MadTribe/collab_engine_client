package com.github.swm.userclient.context

import com.github.swm.userclient.http.Client
import groovy.transform.Canonical

/**
 * Created by paul.smout on 20/02/2015.
 */
@Canonical
class CommandContext {
    def ConfigObject config;
    def Client client;

}
