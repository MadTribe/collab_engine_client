package com.github.swm.userclient.commands.apiactions;

import com.github.swm.userclient.commands.CommandResponse;
import com.github.swm.userclient.http.Client;

/**
 * Created by paul.smout on 20/04/2015.
 */
public interface ApiAction {

    CommandResponse go(Client client);

    def CommandResponse success(data)

    def CommandResponse fail(resp, data)
}
