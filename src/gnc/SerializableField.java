package gnc;


import java.lang.annotation.*;

/**
 * Esta anotación se usa para marcar campos en un objeto
 * que se pretenden incluir en su serializable
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface SerializableField {

    public String key();
}
