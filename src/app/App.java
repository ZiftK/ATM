package app;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;

import atm.UserATM;
import strio.CsvTable;
import strio.KeyFileItem;
import gnc.SerializableField;
import gnc.Serializer;


import java.util.LinkedHashMap;

import atm.BailType;
import atm.Client;

import java.util.HashMap;

import atm.AdminATM;
/**
 *
 * Falta corregir errores, el programa peta si el archivo en cuestion no existe.
 * Se debe automatizar esta parte. Si el archivo ya existe, debe abrirse y leerse,
 * si el archivo no existe debe crearse y escribirse.
 *
 * Falta añadir funcionalidades a la variable "record", que es la que controla
 * los registros. Se deben poder añadir registros, modificarlos, borrarlos.
 *
 * Falta automatizar la asignación del parámetro key
 *
 * Se debe crear una funcionalidad para añadir directorios en caso que desee
 * crearse una sub tabla
 *
 * Corregir recorrido de ruta, está mal implementado
 */

public class App {



    public static void main(String[] args){

//        new AdminATM().loop();
        new UserATM().loop();
    }




}
