package strio;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

public class WNR {

    /**
     * Create new file in specified file path
     * @param filePath : file path
     * @param content : file content
     */
    public static void createFile(String filePath, String content)
    {
        if (!new File(filePath).exists())// si el archivo existe
        {

            // intentamos abrir una nueva locación para el archivo
            try (BufferedWriter writer = new BufferedWriter(new FileWriter(filePath))) {
                // escribimos el contenido
                writer.write(content);
                System.out.printf("\n El archivo %s se ha creado exitosamente.\n",filePath);

            } catch (IOException e) {
                e.printStackTrace();
            }

        }
    }


    /**
     * Return array of lines in specified file
     * @param filePath : file path
     * @return Array where each element is a file line
     */
    public static ArrayList<String> readFileLines(String filePath)
    {

        ArrayList<String> lineList = new ArrayList<>();

        /**
         * Intentamos leer el archivo especificado.
         * Usamos un loque "try with resources" para asegurarnos
         * del cierre del lector de archivos tras su uso
         */
        try(BufferedReader reader = new BufferedReader(new FileReader(filePath)))
        {
            // linea de lectura
            String line;

            /*
             * Mientras exista una linea que leer, seguimos
             * leyendo
             */
            while((line = reader.readLine()) != null)
            {


                //Añadimos linea
                lineList.add(line);
            }


        }
        catch(IOException e)
        {
            e.printStackTrace();
        }

        // retornamos array
        return lineList;
    }

    /**
     * Over write file in file path with specified content
     * @param filePath : file path
     * @param content : content to over wride
     */
    public static void write(String filePath, String content)
    {
        /*
         * Intentamos abrir el archivo en modo de escritura.
         * Declaramos el escritor en el bloque try with resources
         * para asegurar el cierre del mismo tras su uso en
         * el bloque try
         */
        try(BufferedWriter writer = new BufferedWriter(new FileWriter(filePath)))
        {

            // Escribimos contenido
            writer.write(content);

        } catch (IOException e) {

            e.printStackTrace();
        }
    }
}
