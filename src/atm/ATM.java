package atm;

import java.util.HashMap;

import consoleio.Msg;
import strio.CsvTable;
import strio.KeyFileItem;

public abstract class ATM {

    /** Diccionario de comandos */
    protected HashMap<String,Command> commands;

    /** Variable de ciclado */
    protected boolean loop;

    /** CLI IO class */
    protected Msg msg;

    /** Linked Table */
    CsvTable<KeyFileItem> clientsTable;

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

        commands.put("show", new Command(this::show,"Muestra todos los registros"));
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


    /**
     * Load data from csv file to table
     */
    public void load(){
        // load linked table
        clientsTable = new CsvTable<>("files/records.csv", new KeyFileItem());
        clientsTable.loadRecord();
    }

    /**
     * Dump data from table to csv file
     */
    public abstract void save();

    /**
     * Show info
     */
    public abstract void show();

}
