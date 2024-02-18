package strio;

import java.io.File;
import gnc.SerializableField;

import java.lang.IllegalArgumentException;

/**
 * Esta clase está pensada como una fila genérica de una tabla csv.
 * Solo contiene un campo, el campo 'key', pues toda tabla debe de tener
 * una columna que sirva como identificador para sus elementos
 *
 * @author alv
 *
 */
public class KeyFileItem extends RowItem {



    @SerializableField(key="Path")
    protected String filePath;


    public KeyFileItem()
    {
        this.filePath = "";
    }

    public KeyFileItem(String filePath)
    {
        this.filePath = filePath;
    }


    /**
     * Establece la ruta del registro de objeto
     * @implNote La ruta no puede estar vacia
     * @exception {@link java.lang.IllegalArgumentException}
     * @param filePath Nueva ruta de archivo
     */
    public void setFilePath(String filePath)
    {
        if (filePath.equals(""))
        {
            throw new IllegalArgumentException("La ruta no puede estar vacia");
        }

        this.filePath = filePath;
    }


    /**
     * Ruta de registro de objeto
     * @return Ruta de archivo de registro de objeto
     */
    public String getFilePath()
    {
        return this.filePath;
    }



}
