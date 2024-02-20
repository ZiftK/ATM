package atm;

import strio.CsvTable;
import strio.KeyFileItem;

import java.util.ArrayList;
import java.util.HashMap;

public class UserATM extends ATM{

    Client clientData;

    String clientFilePath;

    /**
     * Print user menu
     */
    void menu(){

        // print empty line
        System.out.println("\n");

        // print header
        msg.ph("Menu de usuario",'*');

        // format client info
        String info = String.format(
                "\n\nBienvenido %s\tTu saldo es: $%,.2f\n",
                clientData.getName(),
                clientData.getBalance()
        );

        // print client info
        msg.info(info);

        // print option
        help();
    }

    /**
     * Increase client balance
     */
    public void deposit(){

        // get increase amount
        double ab = msg.getDoubleFromInput("Ingrese cantidad a depositar");

        // increase balance
        clientData.increaseBalance(ab);

        // print info message
        msg.info(String.format("Nuevo saldo: $%,.2f",clientData.getBalance()));

        // sve data
        this.save();

    }

    /**
     * Decrease client balance
     */
    public void withDraw(){

        // show balance info
        msg.info(String.format("Saldo disponible: $%,.2f",clientData.getBalance()));

        // get decrease amount
        double ab = msg.getDoubleFromInput("Ingrese la cantidad a retirar");

        // only withDraw if balance is greater than withDraw
        if (ab > clientData.getBalance())
            msg.error("La cantidad ingresada es mayor al saldo disponible. Intente de nuevo");

        else{
            clientData.increaseBalance(-ab);
            msg.info(String.format("Retiro exitoso. Saldo restante: $%,.2f",clientData.getBalance()));
        }

        this.save();// save data
    }

    @Override
    public  void loadCommands(){
        commands = new HashMap<>();
        commands.put("0",new Command(this::leave,"Salir"));
        commands.put("1",new Command(this::deposit,"Deposita efectivo"));
        commands.put("2",new Command(this::withDraw,"Retira efectivo"));

    }

    @Override
    public void load(){

        // We execute the load method of the superclass
        super.load();
        // ATM input key
        int input;

        /*
        While the user does not enter a valid key, we maintain the loop
        */
        while (true){

            // get key
            input = msg.getIntFromInput("Introduce tu clave");

            /*
            If the key is in the records, we stop the looping; if
            it is not in the records, we continue with the loop
            and throw an error message
            */
            if (clientsTable.exists(input)) break;

            msg.error("La clave ingresada no existe en los registros");

        }

        /*At this point, we already have an existing key, so we
        retrieve the item in the table with that key and
        load the customer data*/
        int finalInput = input;
        ArrayList<KeyFileItem> keyItems = clientsTable.find(item -> item.key == finalInput);

        // save client file path
        clientFilePath = keyItems.get(0).getFilePath();

        CsvTable<Client> ctl = new CsvTable<>(clientFilePath,new Client());
        ctl.loadRecord();
        clientData = ctl.items().get(0);

        menu();
    }
    public void save(){

        // init client table
        CsvTable<Client> ctl = new CsvTable<>(clientFilePath,clientData);
        // load record
        ctl.loadRecord();
        // modify record
        ctl.modifyRecord(clientData.key,clientData);
        // save record
        ctl.writeRecord();
    }

    public void show(){
        msg.info(clientData.toString());
    }

}
