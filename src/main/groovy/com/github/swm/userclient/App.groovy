package com.github.swm.userclient

import com.github.swm.userclient.commands.Command
import com.github.swm.userclient.commands.CommandResponse
import com.github.swm.userclient.commands.HelpCmd
import com.github.swm.userclient.commands.ListPlansCmd
import com.github.swm.userclient.commands.LoginCmd
import com.github.swm.userclient.context.CommandContext
import com.github.swm.userclient.http.Client

import static java.util.Arrays.asList;

class App {
    private static final String VERSION = "0.0.1";

    public static void main(String[] args){
        def params = new ArrayList<String>(asList(args))
        new App(params);
    }

    def config = null;

    public App(List<String> args){
        if (args.size() > 0){

            String fileName = args.remove(0);

            config = new ConfigSlurper().parse(new File(fileName).toURL())
            runRepl(config, args)
        } else {
            output("First Param is config file");
        }

    }

    private void runRepl(ConfigObject config, List<String> args) {
        Client client = new Client((String) config.serverAddress, VERSION);
        def context = new CommandContext(config: config, client: client);

        BufferedReader br = new BufferedReader(new InputStreamReader(System.in))

        String input = "";
        while (!input.equalsIgnoreCase("exit") && !input.equalsIgnoreCase("quit")){
            print ">:"

            input = br.readLine()

            def cmd = new ArrayList<String>(asList(input.split(" ")));

            runCommand(cmd, context);
        }

        output("Bye!")

    }


    private void runCommand(List<String> args, context) {
        def commands = getCommands();

        commands.each { cmd ->
            if (cmd.accept(args)) {
                output("Running Command ${cmd.name}");
                cmd.run(args, context);
            }
        }
    }


    def output(String buildable) {
        println buildable;
    }

    public List<Command> getCommands(){
        return asList(new LoginCmd(), new HelpCmd(), new ListPlansCmd())
    }

}

