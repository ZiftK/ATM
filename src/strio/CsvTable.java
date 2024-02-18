package strio;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.HashMap;

import gnc.Serializer;

import java.lang.IllegalArgumentException;
import java.lang.RuntimeException;
import java.lang.reflect.InvocationTargetException;


/**
 * CsvTable genera una tabla formato csv en un archivo
 * con base en los parametros serializables de un objeto.
 *
 * Para marcar un parametro de objeto como serializable
 * se utiliza la anotacion {@link gnc.SerializableField}.
 *
 * CsvTable solo tomará en cuenta los campos marcados con
 * dicha anotación. Con ellos generará una tabla, cuyos
 * encabezados serán las "keys" de los parámetros serializados.
 * Toda anotación {@link gnc.SerializableField} debe incluir su campo
 * "key".
 *
 *
 *
 * @author alv
 */
public class CsvTable <T extends RowItem> {

    /** ruta del archivo en el sistema*/
    protected String filePath;
    /** separador para las columnas del archivo*/
    protected String splitter;

    /** registro en tiempo de ejecución*/
    protected ArrayList<T> record;

    /** instancia de objeto generico*/
    protected T obj;

    /** array de encabezados */
    protected String[] headers;

    public CsvTable(String filePath, T obj)
    {
        this.record = new ArrayList<>();
        this.filePath = filePath;
        this.obj = obj;
        this.headers = Serializer.getSerialFields(obj);
        this.splitter = ",";

    }

    public CsvTable(String filePath, T obj, String splitter)
    {
        this(filePath,obj);
        this.splitter = splitter;

    }


    /**
     * Crea un nuevo archivo csv con los encabezados especificados
     * @param fileName Nombre del archivo
     * @param headers Encabezados
     *
     */
    public void createRecord()
    {

        WNR.createFile(filePath, String.join(this.splitter, this.headers));
    }

    /**
     * Transforma una fila de texto plano separada por el 'splitter'
     * en un LinkedHashMap
     * @param stringRow string de la fila a formatear
     * @return LinkedHashMap compuesto por los valores de la fila y sus claves
     */
    protected LinkedHashMap<String,Object> format(String stringRow)
    {
        // declaramos el mapa de retorno
        LinkedHashMap<String,Object> map = new LinkedHashMap<>();

        // separamos los elementos de la fila
        String[] row = stringRow.split(this.splitter);
        for (int i = 0; i < row.length ; i ++)
        {
            // añadimos elementos con sus respectivas claves
            map.put(this.headers[i], row[i]);
        }

        return map;

    }


    /**
     * Carga toda la información de un archivo csv a objetos independientes.
     */
    @SuppressWarnings("unchecked")
    public void loadRecord()
    {

        this.createRecord();

        /*
         * Es necesario que la primer columna no se tome en cuenta,
         * pues esta incluye solamente los encabezados.
         * Esta variable se establece a true para omitir
         * la lectrua de la primera linea, después se establece en
         * false.
         */
        boolean pass= true;

        /**
         * Mapa en el cual se cargaran los elementos de las filas
         * según usando como clave su encabezado
         */
        LinkedHashMap<String,Object> map = new LinkedHashMap<>();

        // declaramos instancia generica;
        T newObj;

        /*
         * Obtenemos meta datos del objeto generico
         * pasado como argumento generar el patron
         * de tabla
         */
        Class<? extends RowItem> clss = obj.getClass();

        // limpiamos el registro en memoria.
        this.record.clear();


        try {

            /*
             * Recorremos las lineas del documento
             */
            for (String line : WNR.readFileLines(this.filePath))
            {
                if (pass)
                {// si es la primer linea, la omitimos
                    pass = false;
                    continue;
                }

                // transformamos la linea de texto en un HashMap ordenado
                map = format(line);
                /*
                 * Usamos reflection para instanciar un objeto
                 * de tipo genérico, luego hacemos un parse al tipo
                 * establecido
                 */
                newObj = (T) clss.getDeclaredConstructor().newInstance();

                /*
                 * Cargamos toda la informacion almacenada en el
                 * HashMap en el objeto, usando la clave de sus
                 * parametros marcados como serializables.
                 */
                Serializer.load(map, newObj);

                /*
                 * Añadimos objeto al registro
                 */
                this.record.add( newObj);
            }
        }
        catch (InstantiationException | IllegalAccessException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IllegalArgumentException | InvocationTargetException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        catch (NoSuchMethodException | SecurityException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

    }


    /**
     * Sobre escribe el contenido del archivo con el contenido
     * del registro en memoria
     */
    public void writeRecord()
    {


        this.createRecord();

        /*
         * HashMap que sirve para almacenar los campos
         * serializables de los objetos en registro
         */
        LinkedHashMap <String,Object> map = new LinkedHashMap<>();

        // linea de escritura
        String content = "";
        // final para linea de escritura
        String end = "";

        /*
         * Convertimos los encabezados a un string
         * formateado con el 'splitter'
         */
        content += String.join(this.splitter, this.headers) + "\n";



        // recorremos todos los registros en memoria
        for (T item : record)
        {
            // serializamos objeto en registro
            map = Serializer.dump(item);

            // recorremos encabezados
            for (int i = 0; i < this.headers.length; i++)
            {
                /*
                 * Si la columna añadida no es la ultima, añadimos
                 * separador
                 */
                end = (i < this.headers.length-1) ? this.splitter : "";

                /*
                 * Emparejamos la informacion del mapa del objeto
                 * serializado con su correspondiente encabezado
                 */
                content += map.get(headers[i]) + end;
            }

            // añadimos salto de linea
            content += "\n";
        }

        WNR.write(this.filePath, content);

    }

    /**
     * Retorna el item con la clave especificada
     * @param key : Clave
     * @return Item
     */
    public T findItem(int key)
    {
        return record.get(key);
    }


    /**
     * Busca el primer elemento que contenga el valor especificado
     * en la columna con el nombre de encabezado especificado
     * @param header : Nombre de encabezado
     * @param val : Valor para comparar
     * @return Si encuentra un item, que contenga el valor especificado
     * en la columna con el encabezado especificado, retorna el item, de lo
     * contrario retorna null. Solo retorna el primer item.
     */
    public T findItem(String header, Object val)
    {
        // Recorremos items
        for (T item : record)
        {
            if(item.equals(val))
            {
                // Si se encuentran coincidencias
                // retornamos item
                return item;
            }
        }

        return null;
    }

    /**
     * Busca los elementos que contengan el valor especificado
     * en la columna con el nombre de encabezado especificado
     * @param header : Nombre de encabezado
     * @param val : Valor para comparar
     * @return Lista con coincidencias
     */
    @SuppressWarnings("unchecked")
    public T[] findItems(String header, Object val)
    {

        ArrayList<T> rtn = new ArrayList<>();

        // Recorremos items
        for (T item : record)
        {
            if(item.equals(val))
            {
                // Si se encuentran coincidencias
                // añadimos item a la lista
                rtn.add(item);
            }
        }

        return (T[]) rtn.toArray();
    }


    /**
     * Añade un registro a la tabla
     * @param obj : Objeto de registro
     */
    public void addRecord(T obj)
    {

        addRecord(obj,false);

    }

    /**
     * Añade un registro a la tabla
     * @param obj : Objeto de registro
     */
    public void addRecord(T obj,boolean ignoreIndex)
    {

        if (!ignoreIndex)
        {
            // Seteamos clave
            obj.key = record.size();
        }
        // guardamos registro
        record.add(obj);

    }

    /**
     * Reemplaza el objeto en el registro por el objeto
     * especificado
     * @param key : Clave del objeto
     * @param obj : Objeto
     */
    public void modifyRecord(int key, T obj)
    {
        record.set(key, obj);
    }

    /**
     * Elimina un registro de la tabla
     * @param key
     */
    public void delRecord(int key)
    {

        // Eliminamos elemento
        record.remove(key);

        // Recorremos indices
        for (int i = key; i <record.size() ; i++)
        {
            record.get(i).key = 0;
        }
    }


    /**
     *
     * @return Cantidad de filas en la tabla
     */
    public int rowsCount() {
        return record.size();
    }


}

