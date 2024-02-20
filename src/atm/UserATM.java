package atm;

import strio.CsvTable;
import strio.KeyFileItem;

import java.util.ArrayList;

public class UserATM extends ATM{

    Client clientData;


    public UserATM(){

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
        ArrayList<KeyFileItem> keyItem = clientsTable.find(item -> item.key == finalInput);

        CsvTable<Client> ctl = new CsvTable<>(keyItem.get(0).getFilePath(),new Client());
        ctl.loadRecord();
        clientData = ctl.items().get(0);
    }
    public void save(){


    }

    public void show(){
        msg.info(clientData.toString());
    }
}
