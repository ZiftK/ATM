package consoleio;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import java.util.ArrayList;

/**
 * Esta clase está diseñada para agilizar la entrada y salida
 * de datos mediante la linea de comandos.
 * [11/16/2023]
 *
 * @author ZiftK
 */
public class Msg {

    /* scanner para entrada y salida de datos*/
    BufferedReader reader;
    /** Niveles de comandos */
    private ArrayList<String> levels;
    /** Variable de impresión para niveles de comandos */
    private String level = "cli";

    /*Instancia Singleton */
    public static Msg instance;


    /**
     * Objeto Msg
     */
    public Msg()
    {
        // valores iniciales
        reader = new BufferedReader(new InputStreamReader(System.in));
        levels = new ArrayList<>();
    }

    /**
     * Método para obtener la instancia de Msg
     *
     * @return
     */
    public static Msg getInstance() {
        if (Msg.instance == null)
        {
            Msg.instance = new Msg();
        }

        return Msg.instance;
    }

    /**
     * Limpieza de variables
     */
    public void kill()
    {
        try{
            // cerramos el lector
            reader.close();
        }
        catch(IOException e){
            System.out.println(e.getMessage());
        }
    }

    /**
     * Imprime el super encabezado
     *
     * @throws InterruptedException
     */
    public void superHeader() throws InterruptedException {
        String[] lines = {
                " ____   _     ____            ___     ____    ____  ",
                "| ___| | |   |_  _|  ___     / _ \\   |    |  |    | ",
                "| |__  | |_   _||_  |___|   / /_\\ \\  | ___|  |  __| ",
                "|____| |___| |____|        /_/   \\_\\ |_|     |_|    "
        };

        int delay = 5; // milisegundos de retardo entre letras

        // Imprime cada caracter progresivamente
        for (String line : lines) {
            for (int i = 0; i < line.length(); i++) {
                System.out.print(line.charAt(i));
                Thread.sleep(delay); // tiempo de retardo
            }
            Thread.sleep(5); // Retardo adicional entre líneas
            System.out.println();
        }

        System.out.println();

        /*
         * Una vez impreso el super encabezado, se imprimirá una animación de flecha.
         * La flecha cruzará de izquierda a derecha desacelerando y dejando atrás un
         * texto: "Por: Ximena Viveros Perez y Alvaro Caballero Laguna", por lo que
         * el texto debe ir apareciendo detras de la flecha.
         * Esto lo lograremos reemplazando cada caracter que deje la fecha detras
         * por el caracter correspondiente que ocupe ese espacio.
         * Como si el texto ya estuviera escrito y la flecha solo lo estuviese
         * revelando.
         *
         * Para esto, la flehca debe recorrer justo la distancia que tienen los caracteres
         * del texto, por lo que debemos guardamos la cantidad de espacios que recorerá la
         * flecha como la longitud del string a imprimir.
         *
         *
         */

        String text = "Por: Ximena Viveros Perez y Alvaro Caballero Laguna "; // string a reemplazar
        int espacios = text.length(); // Espacios a recorrer
        String arrow = ">-----|>"; // flecha
        String aux = ""; // variable auxiliar de impresion

        //sp: ----------------- Aparicion progresiva de la flecha ----------------- */
        /*
         * Recorremos el array de la flecha para dibujarla
         * progresivamente.
         */
        for (char charac : arrow.toCharArray()) {
            aux += charac; // añadimos uno a uno al auxiliar
            System.out.print(aux); // imprimimos auxiliar
            System.out.print("\r"); // regresamos puntero
        }

        //sp: ----------------- Aparicion de texto ----------------- */

        aux = ""; // limpiamos auxiliar
        char[] textArray = text.toCharArray(); // convertimos el texto a un array

        // lanzamos flecha
        for (int i = 0; i < espacios; i++) {

            // agregamos cada caracter al auxiliar de impresion
            aux += textArray[i];
            // imptimimos auxiliar de impresion
            System.out.print(aux);

            // imprimimos flecha
            System.out.print(arrow);
            try {
                Thread.sleep(Math.max(i + 1 - 3, 1)); // retardo para simular movimiento
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            System.out.print("\r"); // retornamos puntero
        }

        System.out.println("\n");

    }

    public void finalHeader()
    {
        System.out.println();
        // Define los caracteres para el guiño
        String[] winkingFaces = { "O_O","^_^","O_O",};

        // Número de veces que se repetirá la animación
        int repetitions = 3;

        // Imprime la animación
        for (int i = 0; i < repetitions; i++) {
            for (String winkingFace : winkingFaces) {
                System.out.print(winkingFace + "  Hasta pronto!!!  " + winkingFace); // Imprime el rostro de guiño
                try {
                    Thread.sleep(200); // Pausa entre cada rostro
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                System.out.print("\r");
            }
        }

        System.out.println();
    }

    /**
     * Imprime un mensaje como encabezado
     *
     * @param message : mensaje a imprimir
     */
    public void ph(String message, char charac) {

        String str1 = repeatChar(charac, message.length() + 2);
        message += charac;
        message = charac + message;

        System.out.printf("%s\n%s\n%s\n\n", str1, message, str1);

    }

    /**
     * Imprime un mensaje como encabezado subrayado,
     * con una longitud extra
     *
     * @param message : mensaje a imprimir
     * @param charac  : caracter para subrayar
     * @param offset  : extra de longitud
     */
    public void pDownH(String message, char charac, int offset) {

        String str1 = repeatChar(charac, message.length() + 2);
        str1 += repeatChar(charac, offset);

        System.out.printf("%s\n%s\n\n", message, str1);

    }

    public void pDownH(String message, char charac) {
        pDownH(message, charac, 2);
    }


    /**
     * Genera un mensaje por consola y recibe un double para retornarlo
     *
     * @param message : mensaje a imprimir
     * @exception NumberFormatException IOException
     * @return
     */
    public Double getDoubleFromInput(String message) {


        double cap; // variable de de retorno
        String scap=""; // string de captura




        // intentamos obtener captura
        System.out.printf("%s%s> ", level, message);

        try {

            scap = reader.readLine();


        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        cap = Double.parseDouble(scap); // parseamos a double

        return cap;// retornamos double
    }

    /**
     * Genera un mensaje por consola y recibe un entero para retornarlo
     *
     * @param message : mensaje a imprimir
     * @return
     */
    public int getIntFromInput(String message) {

        int cap; // variable de de retorno
        String scap; // string de captura

        while (true) {// ciclamos introducción de variable

            try{
                // intentamos obtener captura
                System.out.printf("%s%s> ", level, message);

                scap = reader.readLine(); // leemos entrada

                cap = Integer.parseInt(scap); // parseamos a entero

                // si se obtiene la captura rompemos el ciclo
                break;

            }
            catch (NumberFormatException e){
                instance.error("El valor debe ser numérico");
            }
            catch (IOException e)
            {
                instance.error("Error de entrada : "+ e.getMessage());
            }
        }


        return cap;
    }

    /**
     * Genera un mensaje por consola y recibe un string para retornarlo
     *
     * @param message
     * @return
     */
    public String getStringFromInput(String message) {


        String cap = ""; // string de retorno

        try{
            // intentamos obtener captura
            System.out.printf("%s%s> ", level, message);
            cap = reader.readLine();

        }
        catch (NumberFormatException e){
            instance.error("El valor debe ser numérico");
        }
        catch (IOException e)
        {
            instance.error("Error de entrada : "+ e.getMessage());
        }

        return cap;
    }

    /**
     * Imprime mensaje de error
     * @param message : mensaje de error
     */
    public void error(String message) {
        System.out.println("\n[Error] " + message + "\n");
    }

    /**
     * Imprime mensaje de advertencia
     * @param message : mensaje de error
     */
    public void warning(String message)
    {
        System.out.println("\n[Warning] " + message + "\n");
    }


    /**
     * Print an info message
     * @param message
     */
    public void info(String message) { System.out.println("\n[Info] "+ message + "\n");}

    /**
     * Agrega un comando al nivel de comandos
     *
     * @param level : nivel de comando
     */
    public void upLevel(String level) {

        this.level += ">" + level + ">> ";

        levels.add(level);


    }

    /**
     * Remueve el ultimo nivel de comandos
     */
    public void downLevel() {

        level = "cli";
    }

    /**
     * Genera un string usando un carácter una cantidad determinada de veces
     *
     * @param charac : carácter a imprimir
     * @param count  : cantidad de carácteres
     * @return : string con carácter repetido
     */
    public String repeatChar(char charac, int count) {

        return new String(new char[count]).replace('\0', charac);
    }
}
