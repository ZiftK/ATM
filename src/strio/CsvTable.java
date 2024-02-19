package strio;


import gnc.Serializer;

import java.lang.reflect.InvocationTargetException;
import java.util.*;
import java.util.function.Consumer;
import java.util.function.Predicate;


/**
 * CsvTable genera una tabla formato csv en un archivo
 * con base en los parametros serializables de un objeto.
 * Para marcar un parametro de objeto como serializable
 * se utiliza la anotacion {@link gnc.SerializableField}.
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

    /** Unused Keys */
    protected Queue<Integer> unusedKeys;

    /** instancia de objeto generico*/
    protected T obj;

    /** array de encabezados */
    protected String[] headers;

    public CsvTable(String filePath, T obj)
    {
        this.record = new ArrayList<>();
        this.unusedKeys = new ArrayDeque<>();
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
     * Sort objects in record using an object key
     */
    protected void sort()
    {
        // compare keys of objects in record
        record.sort((o1, o2) -> Integer.compare(o1.key, o2.key));
    }

    /**
     * Load unused keys in record
     */
    protected  void loadKeys(){

        /*
        Generate a set to store all the keys of objects in the table
         */
        HashSet<Integer> keySet = new HashSet<>();

        // Add each object key to set
        for (T obj : record){
            keySet.add(obj.key);
        }

        // We iterate through the range of possible keys within the record
        for (int i = 0; i < record.size();i++){

            if (!keySet.contains(i)){
                unusedKeys.add(i); // Add key if set does not contain it
            }
        }

    }

    /**
     * Crea un nuevo archivo csv con los encabezados especificados
     *
     */
    public void createRecord()
    {

        WNR.createFile(filePath, String.join(this.splitter, this.headers));
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
        boolean pass = true;

        /*
         * Mapa en el cual se cargaran los elementos de las filas
         * según usando como clave su encabezado
         */
        LinkedHashMap<String,Object> map;

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
        catch (InstantiationException | IllegalAccessException | IllegalArgumentException | InvocationTargetException |
               NoSuchMethodException | SecurityException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        this.loadKeys();

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
        LinkedHashMap <String,Object> map;

        // linea de escritura
        StringBuilder content = new StringBuilder();
        // final para linea de escritura
        String end;

        /*
         * Convertimos los encabezados a un string
         * formateado con el 'splitter'
         */
        content.append(String.join(this.splitter, this.headers)).append("\n");



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
                content.append(map.get(headers[i])).append(end);
            }

            // añadimos salto de linea
            content.append("\n");
        }

        WNR.write(this.filePath, content.toString());

    }




    /**
     * Add object to record
     * @param obj : Object to be recorded
     */
    public void addRecord(T obj){

        addRecord(obj,false);
    }

    /**
     * Add an object to the record. If 'ignoreKey' is set to true,
     * the current key of the object is respected unless that key
     * already exists in the records. If the key already exists in
     * the records, the key is set to an unused key or a new one.
     * @param obj : Object to be recorded
     * @param ignoreKey : Decide whether the key of the element is
     *                  automatically set internally or the original key is respected
     */
    public void addRecord(T obj, boolean ignoreKey){
        addRecord(obj,ignoreKey,false);
    }

    /**
     * Add an object to the record, ignoring whether the assigned key already
     * exists in it, only if 'ignoreKey' is set and 'repeatKey' are set to true
     * @param obj : Object to be recorded
     * @param ignoreKey : Decide whether the key of the element is
     *                  automatically set internally or the original key is respected
     * @param repeatKeys : Decide whether to check for the existence of an object
     *                  with the key of the object before recording it or not
     */
    public void addRecord(T obj, boolean ignoreKey, boolean repeatKeys)
    {
        /*
            We check if a key is available, if there are no keys available we assign a new key.
            This procedure will only execute if key ignore is not desired.
        */
        if (!ignoreKey) {

            obj.key = (unusedKeys.isEmpty()) ? record.size() : unusedKeys.poll();
        }

        /*
        We check if the object's key is within the range of existing keys.
        If it is within the range, we check if it is within the unused keys.
        If there are no unused keys available and the current key is within the -
        range of existing keys, then the key is set to the next key higher than the highest key.
        This procedure will only execute if key repetition is not desired.
         */
        if (!repeatKeys && ignoreKey){

            obj.key = (obj.key < record.size() && !unusedKeys.contains(obj.key)) ? obj.key : record.size();
        }

        // add object to record
        record.add(obj);

        // sort record
        this.sort();
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
     * Delete a record from table
     * @param key : Record key
     */
    public void delRecord(int key)
    {

        // We remove the object with the specified key from the record
        record.remove(key);

        // add unused key to queue
        unusedKeys.add(key);
    }

    /**
     * Returns all elements that satisfy the predicate
     * @param condition : Return condition
     * @return Objects that meet the condition
     */
    @SuppressWarnings("unchecked")
    public T[] find(Predicate<T> condition){

        // init return array list
        ArrayList<T> list = new ArrayList<>();

        // for each object in record
        for (T obj : record){

            // compare using predicate
            if (condition.test(obj)){
                // if is true, add to return list
                list.add(obj);
            }
        }

        return (T[]) list.toArray();
    }

    /**
     *
     * @return All table items
     */
    @SuppressWarnings("unchecked")
    public T[] items(){
        return (T[]) record.toArray();
    }

    /**
     *
     * @return Table rows count
     */
    public int rowsCount() {
        return record.size();
    }


}

