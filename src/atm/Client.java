package atm;


import gnc.SerializableField;

import java.lang.IllegalArgumentException;

import strio.RowItem;

public class Client extends RowItem{

    /** Nombre */
    @SerializableField(key="Nombre")
    protected String name;
    /** Apellido paterno */
    @SerializableField(key="Apellido Paterno")
    protected String midName;
    /** Apellido materno */
    @SerializableField(key="Apellido Materno")
    protected String lastName;
    /** Saldo en la cuenta */
    @SerializableField(key="Saldo")
    protected double balance;
    /** Tipo de fianza (debito o credito) */
    @SerializableField(key="Tipo de Fianza")
    protected BailType bailType;
    /** Numero de telefono */
    @SerializableField(key="Numero de Telefono")
    protected String phoneNumber;

    /**
     * Establece todos los parámetros por defecto
     */
    public Client()
    {
        this.name = "NA";
        this.midName = "NA";
        this.lastName = "NA";
        this.balance = 0;
        this.bailType = BailType.DEBITO;
        this.phoneNumber = "NA";
    }

    /**
     * Establece los parámetros del cliente
     * @param name : Nombre del cliente
     * @param midName : Apellido paterno
     * @param lastName : Apellido materno
     * @param balance : Saldo
     * @param bailType : Tipo de fianza
     * @param phoneNumber : Numero de teléfono
     */
    public Client(String name,String midName,String lastName,double balance,BailType bailType, String phoneNumber)
    {
        this.name = name;
        this.midName = midName;
        this.lastName = lastName;
        this.balance = balance;
        this.bailType = bailType;
        this.phoneNumber = phoneNumber;
    }


    @Override
    public String toString()
    {
        String rtn="\n";

        rtn += String.format("ID: %d\n", this.key);
        rtn += String.format("Nombre: %s\n", this.name);
        rtn += String.format("Apellido Paterno: %s\n", this.midName);
        rtn += String.format("Apellido Materno: %s\n", this.lastName);
        rtn += String.format("Saldo: %,.2f\n", this.balance);
        rtn += String.format("Tipo de Fianza: %s\n", this.bailType);
        rtn += String.format("Numero de telefono: %s\n", this.phoneNumber);

        return rtn + "\n";
    }


    //---------------------------------------------------

    /**
     * Establece el nombre al especificado
     * @implNote El nombre no puede estar vacio, ni contener numeros
     * @exception {@link java.lang.IllegalArgumentException}
     * @param name : Nombre
     */
    public void setName(String name)
    {
        if (name.equals(""))
        {
            throw new IllegalArgumentException("El nombre no puede estar vacío");
        }
        if (name.replaceAll("[a-zA-Z]", "").length() > 0)
        {
            System.out.println(name.replace("[^a-zA-Z]", ""));
            throw new IllegalArgumentException("El nombre debe limitarse a letras");
        }
        this.name = name;
    }

    /**
     * Establece el apellido paterno al especificado
     * @implNote El apellido paterno no puede estar vacio, ni contener numeros
     * @exception {@link java.lang.IllegalArgumentException}
     * @param midName : Apellido paterno
     */
    public void setMidName(String midName)
    {
        if (midName.equals(""))
        {
            throw new IllegalArgumentException("El apellido paterno no puede estar vacío");
        }
        if (midName.replaceAll("[a-zA-Z]", "").length() > 0)
        {
            throw new IllegalArgumentException("El apellido materno debe limitarse a letras");
        }
        this.midName = midName;
    }

    /**
     * Establece el apellido materno al especificado
     * @implNote El apellido materno no puede estar vacio, ni contener numeros
     * @exception {@link java.lang.IllegalArgumentException}
     * @param lastName : Apellido materno
     */
    public void setLastName(String lastName)
    {
        if (lastName.equals(""))
        {
            throw new IllegalArgumentException("El apellido materno no puede estar vacío");
        }
        if (lastName.replaceAll("[a-zA-Z]", "").length() > 0)
        {
            throw new IllegalArgumentException("El apellido paterno debe limitarse a letras");
        }
        this.lastName = lastName;
    }

    /**
     * Establece el saldo al especificado
     * @param balance : Nuevo saldo
     */
    public void setBalance(double balance)
    {
        this.balance = balance;
    }

    /**
     * Increase currently balance with specified amount
     * @param amount : amount to increase
     */
    public void increaseBalance(double amount){
        this.balance += amount;
    }

    /**
     * Establece el tipo de fianza al especificado
     * @param bailType : Tipo de fianza
     */
    public void setBailType(BailType bailType)
    {
        this.bailType = bailType;
    }

    /**
     * Establece el numero de teléfono al especificado
     * @implNote El numero de teléfono no puede estar vacio y solo debe tener numeros.
     * @exception {@link java.lang.IllegalArgumentException}
     * @param phoneNumber : Numero de teléfono
     */
    public void setPhoneNumber(String phoneNumber)
    {
        if (phoneNumber.equals(""))
        {
            throw new IllegalArgumentException("El numero de telefono no puede estar vacío");
        }
        if (phoneNumber.replaceAll("[0-9]", "").length() > 0)
        {
            throw new IllegalArgumentException("El numero de telefono debe limitarse a numeros");
        }
        this.phoneNumber = phoneNumber;
    }

    //------------------------------------

    /**
     * Retorna el nombre
     * @return : Nombre
     */
    public String getName()
    {
        return this.name;
    }

    /**
     * Retorna el apellido paterno
     * @return : Apellido paterno
     */
    public String getMidName()
    {
        return this.midName;
    }

    /**
     * Retorna el apellido materno
     * @return : Apellido materno
     */
    public String getLastName()
    {
        return this.lastName;
    }

    /**
     * Retorna el saldo
     * @return : Saldo
     */
    public double getBalance()
    {
        return this.balance;
    }

    /**
     * Retorna el tipo de fianza
     * @return : Tipo de fianza
     */
    public BailType getBailType()
    {
        return this.bailType;
    }

    /**
     * Retorna el numero de teléfono
     * @return : Numero de teléfono
     */
    public String gettPhoneNumber()
    {
        return this.phoneNumber;
    }
}
