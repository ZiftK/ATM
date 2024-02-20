package atm;

import strio.CsvTable;
import strio.KeyFileItem;

import java.util.ArrayList;

public class UserATM extends ATM{

    Client clientData;

    String clientFilePath;


    public UserATM(){
        commands.clear();
    }

    public void deposit(){

        double ab = msg.getDoubleFromInput("Ingrese cantidad a depositar");
        clientData.increaseBalance(ab);

        msg.info(String.format("Nuevo saldo: $%,.2f",clientData.getBalance()));

    }

    public void withDraw(){

        msg.info(String.format("Saldo disponible: $%,.2f",clientData.getBalance()));
        double ab = msg.getDoubleFromInput("Ingrese la cantidad a retirar");

        if (ab > clientData.getBalance())
            msg.error("La cantidad ingresada es mayor al saldo disponible. Intente de nuevo");

        else{
            clientData.increaseBalance(-ab);
            msg.info(String.format("Retiro exitoso. Saldo restante: $%,.2f",clientData.getBalance()));
        }


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
