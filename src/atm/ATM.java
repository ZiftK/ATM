package atm;

import java.util.HashMap;

import consoleio.Msg;

public abstract class ATM {

    /** Diccionario de comandos */
    protected HashMap<String,Command> commands;

    /** Variable de ciclado */
    protected boolean loop;

    /** CLI IO class */
    protected Msg msg;

    public ATM()
    {
        msg = Msg.getInstance();
    }

    /**
     * Ciclo de aplicación
     */
    public void loop()
    {
        // seteamos la variable de ciclado a true
        loop = true;
        // definimos la variable de entrada
        String input;

        // cargamos comandos y tabla
        this.loadCommands();
        this.load();

        while (loop)// ciclo de ejecucion
        {
            // limpiamos entrada
            input = msg.getStringFromInput("").toLowerCase();

            // ejecutamos comando
            this.execute(input);
        }

        // guardar cambios
        this.save();


    }

    /**
     * Execute the command with specified key
     * @param command : Command to execute
     */
    public void execute(String command)
    {
        if (commands.containsKey(command))
        {
            // up command level
            msg.upLevel(command);
            // execute command
            commands.get(command).run();
            // down command level
            msg.downLevel();
        }
        else
        {
            msg.error("El comando especificado no existe");
        }
    }



    /**
     * Load ATM commands
     */
    public void loadCommands()
    {
        // init new HashMap
        commands = new HashMap<>();


        // save command
        commands.put("leave",new Command(this::leave, "Termina la ejecución y guarda los cambios."));

        commands.put("?", new Command(this::help,"Imprime los comandos disponibles"));
    }


    // -------------------------------- Commands --------------------------------

    /**
     * Break loop
     */
    public void leave() {
        loop = false;
    }

    /**
     * Print all comands and their descriptions
     */
    public void help() {

        // print each command and his description
        for (String key : commands.keySet())
        {
            System.out.printf("\n%s\t:\t%s\n",key,commands.get(key));
        }
    }


    public abstract void load();

    public abstract void save();

    public abstract void show();

}
