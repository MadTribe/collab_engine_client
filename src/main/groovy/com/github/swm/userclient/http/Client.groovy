package com.github.swm.userclient.http

import groovyx.net.http.*
import static groovyx.net.http.ContentType.*
import static groovyx.net.http.Method.*

/**
 * Created by paul.smout on 20/02/2015.
 */
class Client {
    def http
    def version;

    public Client(String serverAddress, String version){
        this.version = version
        http = new HTTPBuilder( "http://${serverAddress}" )
    }

    def sendPost(path,entity, onSuccess, onFailure){

        // perform a POST request, expecting JSON response data
        http.request( POST, JSON ) {

            uri.path = path

            body = entity;
            headers.'User-Agent' = "SWM client ${version}"
            headers.Accept = 'application/json';

            // response handler for a success response code:
            response.success = { resp, json ->
                    onSuccess(resp, json);

            }

            // handler for any failure status code:
            response.failure = { resp ->

                log "Unexpected error: ${resp} ${resp.statusLine.statusCode} : ${resp.statusLine.reasonPhrase}"
                if (onFailure){
                    onFailure(resp);
                }
            }
        }

    }

    def sendGet(path,params, onSuccess, onFailure){

        // perform a GET request, expecting JSON response data
        http.request( GET, JSON ) {

            uri.path = path

            params = params;
            headers.'User-Agent' = "SWM client ${version}"
            headers.Accept = 'application/json';


            // response handler for a success response code:
            response.success = { resp, json ->
                onSuccess(resp, json);

            }

            // handler for any failure status code:
            response.failure = { resp ->

                log "Unexpected error: ${resp} ${resp.statusLine.statusCode} : ${resp.statusLine.reasonPhrase}"
                if (onFailure){
                    onFailure(resp);
                }
            }
        }

    }

    def log(GString gString) {
        println gString; // TODO add logger [t=0.1h]
    }
}
