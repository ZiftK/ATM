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
        super.load();

        int input;

        while (true){

            input = msg.getIntFromInput("Introduce tu clave");

            if (clientsTable.exists(input)) break;
            msg.error("La clave ingresada no existe en los registros");

        }

        int finalInput = input;
        ArrayList<KeyFileItem> keyItem = clientsTable.find(item -> item.key == finalInput);

        CsvTable<Client> ctbl = new CsvTable<>(keyItem.get(0).getFilePath(),new Client());
        ctbl.loadRecord();
        clientData = ctbl.items().get(0);
    }
    public void save(){

    }

    public void show(){
        msg.info(clientData.toString());
    }
}
