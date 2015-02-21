package com.github.swm.userclient

import com.github.swm.userclient.commands.Command
import com.github.swm.userclient.commands.LoginCmd
import com.github.swm.userclient.context.CommandContext
import com.github.swm.userclient.http.Client

import static java.util.Arrays.asList;

class App {
    private static final String VERSION = "0.0.1";

    public static void main(String[] args){
        println "hello ${args}";
        def params = new ArrayList<String>(asList(args))
        new App(params);
    }

    def config = null;

    public App(List<String> args){
output("" + args.getClass());
        if (args.size() > 0){

            String fileName = args.remove(0);

            config = new ConfigSlurper().parse(new File(fileName).toURL())
            Client client = new Client((String)config.serverAddress, VERSION);
            def context = new CommandContext(config:config, client:client );

            def commands = getCommands();



            commands.each { cmd ->
                if (cmd.accept(args)){
                    output("Running Command ${cmd.name}");
                    cmd.run(args,context);
                }
            }
        } else {
            output("First Param is config file");
        }

    }

    def output(String buildable) {
        println buildable;
    }

    public List<Command> getCommands(){
        return asList(new LoginCmd())
    }

}

