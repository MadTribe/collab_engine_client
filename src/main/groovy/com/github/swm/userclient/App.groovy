package com.github.swm.userclient

import com.github.swm.userclient.commands.Command
import com.github.swm.userclient.commands.CommandResponse
import com.github.swm.userclient.commands.HelpCmd
import com.github.swm.userclient.commands.ListPlansCmd
import com.github.swm.userclient.commands.LoginCmd
import com.github.swm.userclient.context.CommandContext
import com.github.swm.userclient.http.Client

import java.util.concurrent.TimeUnit

import static java.util.Arrays.asList;

class App {
    private static final String VERSION = "0.0.1";

    public static void main(String[] args){
        def params = new ArrayList<String>(asList(args))
        new App(params);
    }

    def config = null;
    Client client;
    CommandContext context;

    public App(List<String> args){
        if (args.size() > 0){

            String fileName = args.remove(0);

            config = new ConfigSlurper().parse(new File(fileName).toURL())

            initialize(config);

            runRepl(config, args)
        } else {
            output("First Param is config file");
        }

    }

    public App(ConfigObject config){
        this.config = config;
        initialize(config);
    }

    private void initialize(config){
        Client client = new Client((String) config.serverAddress, VERSION);
        context = new CommandContext(config: config, client: client);

    }

    private void runRepl(ConfigObject config, List<String> args) {
        // TODO add propper REPL with something like jline [t=2h]
        // TODO add logger [t=0.1h]


        BufferedReader br = new BufferedReader(new InputStreamReader(System.in))

        String input = "";
        while (!input.equalsIgnoreCase("exit") && !input.equalsIgnoreCase("quit")){
            print "> "

            input = br.readLine()

            def cmd = new ArrayList<String>(asList(input.split(" ")));

            runCommand(cmd);
        }

        output("Bye!")

    }


    public CommandResponse runCommand(List<String> args) {
        CommandResponse ret = null;

        def commands = getCommands();

        commands.each { cmd ->
            if (cmd.accept(args)) {
                ret = cmd.run(args, context);
                output "  " + ret .output.toString();
            }
        }

        return ret;
    }


    def output(String buildable) {
        println buildable;
    }

    public List<Command> getCommands(){
        return asList(new LoginCmd(), new HelpCmd(), new ListPlansCmd())
    }

}

