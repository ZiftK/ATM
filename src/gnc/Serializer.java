package gnc;

import java.lang.reflect.Field;
import java.lang.IllegalAccessException;

import java.util.LinkedHashMap;

import atm.BailType;

import java.util.ArrayList;
import java.util.Collections;

/**
 * Esta clase sirve para serializar objetos cuyos campos
 * estén anotados por {@link gnc.SerializableField}
 *
 * @author alv
 */
public class Serializer  {

    /**
     * Toma todos los campos marcados por {@link gnc.SerializableField} dentro
     * de la clase y los transforma en un json.
     * @param obj objeto a serializar
     * @return json de campos
     */
    public static  String[] getSerialFields(Object obj)
    {


        /**
         * Esta lista de mapas guardará los los diferentes campos de las
         * clases padre a la clase en recorrido
         */
        ArrayList<ArrayList<String>> fieldKeysList = new ArrayList<>();

        // esta lista guardara los campos de la clase en recorrido
        ArrayList<String> fieldKeys;

        /**
         * Array donde se almacenan los campos declarados en
         * la clase en recorrido
         */
        Field[] fields;

        // Array de retorno
        String[] rtn;

        /**
         * Obtenemos el genérico de Meta datos de la clase a analizar, seguimos el bucle
         * mientras dicha clase no sea nula (cuando la clase a analizar sea la clase primaria object,
         * este valor se hará null para el siguiente ciclo, pues no hay padres de object). Y, subimos
         * en la jerarquia de clases.
         */
        for (Class<?> cls = obj.getClass(); cls != null; cls = cls.getSuperclass()){


            fields = cls.getDeclaredFields();

            // inicializamos de nuevo la lista de campos de clase en recorrido
            fieldKeys = new ArrayList<>();

            // Recorremos los campos de la clase
            for (Field field : fields) {

                // Si el campo es serializable
                if (field.isAnnotationPresent(SerializableField.class)) {

                    // Recuperamos anotacion, para conocer la clave del campo
                    SerializableField annotation = field.getAnnotation(SerializableField.class);

                    // Guardamos la clave establecida en la anotacion
                    fieldKeys.add(annotation.key());

                }


            }

            // Añadimos dicha lista de claves a la lista de listas
            fieldKeysList.add(fieldKeys);
        }

        // Instanciamos una nueva lista de claves
        fieldKeys = new ArrayList<>();

        /*
         * Debido a que las clases fueron recorridas en
         * sentido inverso con respecto a su jerarquia,
         * la lista final debe invertirse para recuperar el orden
         * original de herencia
         */
        Collections.reverse(fieldKeysList);

        /*
         * Concatenamos las claves en orden
         */
        for ( ArrayList<String> m : fieldKeysList)
        {
            fieldKeys.addAll(m);
        }

        // instanciamos array de retorno
        rtn = new String[fieldKeys.size()];

        // vaciamos contenido a array de retorno
        for (int i = 0; i < fieldKeys.size(); i++)
        {
            rtn[i] = fieldKeys.get(i);
        }

        return  rtn;
    }


    /**
     * Convierte los campos marcados como serializables del objeto
     * en un Hash map
     * @param obj objeto del que se revisan los campos
     * @return HashMap de campos serializados
     */
    public static LinkedHashMap <String,Object> dump(Object obj) {
        /*
         * Este mapa nos servirá para almacenar los campos
         * de la clase en recorrido
         */
        LinkedHashMap<String,Object> map ;

        /**
         * Array donde se almacenan los campos declarados en
         * la clase en recorrido
         */
        Field[] fields;

        // anotacion de campo
        SerializableField annotation;

        // Variable para regresar campo a acceso inicial
        boolean isAccess;

        /**
         * Esta lista de mapas guardará los los diferentes campos de las
         * clases padre a la clase en recorrido
         */
        ArrayList<LinkedHashMap<String,Object>> mapList = new ArrayList<LinkedHashMap<String,Object>>();

        /**
         * Obtenemos el genérico de Meta datos de la clase a analizar, seguimos el bucle
         * mientras dicha clase no sea nula (cuando la clase a analizar sea la clase primaria object,
         * este valor se hará null para el siguiente ciclo, pues no hay padres de object). Y, subimos
         * en la jerarquia de clases.
         */
        for (Class<?> cls = obj.getClass(); cls != null; cls = cls.getSuperclass())
        {

            // recuperamos campos
            fields = cls.getDeclaredFields();

            /**
             * Instanciamos un nuevo mapa
             */
            map = new LinkedHashMap<>();

            // Recorremos los campos de la clase
            for (Field field : fields) {

                // Si el campo es serializable
                if (field.isAnnotationPresent(SerializableField.class)) {

                    // Recuperamos anotacion, para conocer la clave del campo
                    annotation = field.getAnnotation(SerializableField.class);

                    // guardamos estado de acceso
                    isAccess = field.canAccess(obj);

                    // Establecemos el campo como accesible para evitar errores
                    field.setAccessible(true);

                    try
                    {
                        /*
                         *  Si el campo es accesible, lo introducimos en el
                         *  diccionario de campos para esta clase
                         */
                        map.put(annotation.key(), field.get(obj));

                        // regresamos campo a estado de acceso inicial
                        field.setAccessible(isAccess);
                    }
                    catch( IllegalAccessException e)
                    {
                        System.out.println("Param: " + field.getName() + " is no accessible");
                    }
                }
                // añadimos los campos recolectados de la clase a la lista de diccionarios
                mapList.add(map);
            }
        }

        // Instanciamos un nuevo mapa
        map = new LinkedHashMap<>();

        /*
         * Debido a que las clases fueron recorridas en
         * sentido inverso con respecto a su jerarquia,
         * la lista final debe invertirse para recuperar el orden
         * original de herencia
         */
        Collections.reverse(mapList);

        /*
         * Concatenamos los mapas en orden
         */
        for (LinkedHashMap<String,Object> m : mapList)
        {
            map.putAll(m);
        }

        return map;
    }


    /**
     * Toma los valores del HashMap y los carga en los parámetros
     * del objeto marcados como serializados. Solo carga los valores
     * del HashMap cuyas claves coincidan con las claves de la anotacion
     * {@link gnc.SerializableField}
     * @param <T> tipo del objeto destino
     * @param map parametros a cargar
     * @param obj objeto destino
     */
    public static<T> void load(LinkedHashMap<String,Object> map, T obj)
    {

        /**
         * Array donde se almacenan los campos declarados en
         * la clase en recorrido
         */
        Field[] fields;

        // estado inicial de accesibilidad para campo
        boolean isAccess;

        // anotacion de campo
        SerializableField annotation;

        // tipo del campo
        Class<?> fieldType;

        // valor a setear
        String setVal;

        /**
         * Obtenemos el genérico de Meta datos de la clase a analizar, seguimos el bucle
         * mientras dicha clase no sea nula (cuando la clase a analizar sea la clase primaria object,
         * este valor se hará null para el siguiente ciclo, pues no hay padres de object). Y, subimos
         * en la jerarquia de clases.
         */
        for (Class<?> cls = obj.getClass(); cls != null; cls = cls.getSuperclass())
        {

            fields = cls.getDeclaredFields();


            // Recorremos los campos de la clase
            for (Field field : fields) {

                // Si el campo es serializable
                if (field.isAnnotationPresent(SerializableField.class)) {

                    // Recuperamos anotacion, para conocer la clave del campo
                    annotation = field.getAnnotation(SerializableField.class);

                    // Revisamos si el campo es accesible
                    isAccess = field.canAccess(obj);

                    // Establecemos el campo como accesible para evitar errores
                    field.setAccessible(true);

                    try
                    {
                        /*
                         *  Si el campo es accesible, lo introducimos en el
                         *  diccionario de campos para esta clase
                         */
                        fieldType = field.getType();
                        setVal = (String) map.get(annotation.key());

                        if (fieldType == String.class)
                        {
                            field.set(obj, setVal);
                        }
                        if (fieldType == int.class)
                        {
                            field.setInt(obj, Integer.parseInt(setVal));

                        }
                        if (fieldType == double.class) {
                            field.setDouble(obj, Double.parseDouble(setVal));
                        }
                        if (fieldType == BailType.class)
                        {
                            BailType bailType = BailType.valueOf(setVal);
                            field.set(obj, bailType);
                        }


                        // regresamos el campo a su estado original
                        field.setAccessible(isAccess);
                    }
                    catch( IllegalAccessException e)
                    {
                        System.out.println("Param: " + field.getName() + " is no accessible");
                    }


                }

            }
        }
    }


}
