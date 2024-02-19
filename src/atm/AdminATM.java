package atm;

import java.io.File;

import strio.CsvTable;
import strio.KeyFileItem;
import strio.Log;



public class AdminATM extends ATM {

    /** Linked Table */
    CsvTable<KeyFileItem> clientsTable;

    /**
     * Load the data in the table
     */
    public void load()
    {
        // load linked table
        clientsTable = new CsvTable<>("files/records.csv", new KeyFileItem());
        clientsTable.loadRecord();
    }

    @Override
    public void loadCommands()
    {
        super.loadCommands();
        commands.put("add", new Command(this::add,"Añade un registro"));
        commands.put("del", new Command(this::del,"Elimina un registro"));
    }

    /**
     * Guarda los datos de ejecución en la tabla
     */
    public void save()
    {
        // override linked table
        clientsTable.writeRecord();
    }

    /**
     * Añade un cliente al registro
     */
    public void add()
    {

        // --------------------------------- Datos de cliente ----------------------------------------
        // nuevo cliente en el registro
        Client client = new Client();
        // registro de rutas
        KeyFileItem item;

        // variable de captura
        String input;
        // array de nombre y apellidos
        String[] names;
        // variable para saldo
        double balance;

        // pedimos el nombre completo
        input = msg.getStringFromInput("Nombre completo [AAA BBBB CCC]");

        // separamos nombre y apellidos
        names = input.split(" ");

        /*
         * Registramos el nombre y apellidos de ingresados en la entrada.
         * Para evitar pedir entradas varias, se divide la entrada original
         * por espacios y solo se consideran los primeros tres elementos
         */
        try
        {
            client.setName(names[0]);
        }
        catch(IllegalArgumentException e)
        {
            msg.warning(e.getMessage() + "  Nombre establecido a NA");
        }
        catch(ArrayIndexOutOfBoundsException e)
        {
            msg.warning(e.getMessage() + " Nombre establecido a NA");
        }

        try
        {
            client.setMidName(names[1]);
        }
        catch(IllegalArgumentException | ArrayIndexOutOfBoundsException e)
        {
            msg.warning(e.getMessage() + "  Apellido paterno establecido a NA");
        }

        try
        {
            client.setLastName(names[2]);
        }
        catch(IllegalArgumentException | ArrayIndexOutOfBoundsException e)
        {
            msg.warning(e.getMessage() + "  Apellido materno establecido a NA");
        }


        /*
         *  Se establece el balance inicial del cliente, no puede establecerse
         *  un balance de deuda en el registro, así que se consideran solo
         *  valores positivos.
         */
        try
        {
            balance = msg.getDoubleFromInput("Saldo inicial");

            if (balance >= 0 )
            {
                client.setBalance(balance);
            }
            else
            {
                client.setBalance(0);
                msg.warning("El balance inicial no puede ser negativo, ha sido establecido a $0.00");
            }
        }
        catch (NumberFormatException e)// en caso que la entrada no sea un numero
        {
            msg.warning(e.getMessage() + "  Saldo establecido a $0.00");
        }


        // pedimos el tipo de fianza
        input = msg.getStringFromInput("Tipo de fianza [C/D (credito/debito)]");

        // limpiamos string
        input = input.toLowerCase();

        // si el tipo de fianza no es ninguna de las conocidas
        if ( (!input.equals("c") && !input.equals("d")))
            msg.warning("Tipo de fianza no reconocida, establecida a debito");

        /*
         * El tipo de fianza por defecto es debito, por lo que solo se setea
         * si el tipo de fianza es credito
         */
        client.setBailType(input.equals("c") ? BailType.CREDITO : client.getBailType());

        try
        {
            client.setPhoneNumber(msg.getStringFromInput("Numero de telefono"));
        }
        catch (IllegalArgumentException e)
        {
            msg.warning(e.getMessage() + "  Se ha establecido el numero de telefono a NA");
        }

        // Establecemos nuevo item que almacenara la ruta del registro del cliente
        item = new KeyFileItem();

        // añadimos registro
        clientsTable.addRecord(item);

        item.setFilePath(String.format("files/clients/c%d.csv", item.key));

        // creamos tabla de registro de cliente
        CsvTable <Client> cltbl = new CsvTable<>(item.getFilePath(),new Client());

        // seteamso la clave del cliente a la de su correspondiente en la tabla de enlaces
        client.key = item.key;

        /*
         *  añadimos datos del cliente a la tabla ignorando el index
         *  para que no se modifique
         */
        cltbl.addRecord(client,true);

        // escribimos datos en tabla
        cltbl.writeRecord();

        // escribimos datos en tabla
        clientsTable.writeRecord();

        // --------------------------------- Log creation ---------------------------------

        // create new log file
        Log log = new Log(String.format("files/logs/lg%d.txt",item.key));

        // initial log content
        String content = String.format("%s Alta de usuario\n%s",msg.repeatChar('>',10),client);

        // write initial content
        log.write(content);

    }

    /**
     * Elimina un cliente del registro
     */
    public void del()
    {
        // obtenemos la clave
        int key = msg.getIntFromInput("clave");
        // borramos el registro
        clientsTable.delRecord(key);


        // borramos archivos de registro
        File cf = new File(String.format("files/clients/c%d.csv", key));


        // borramos archivos de registro
        File cl = new File(String.format("files/logs/lg%d.txt", key));

        if (cf.delete() && cl.delete())
            msg.info("Elemento borrado");

        clientsTable.writeRecord();

    }


}
