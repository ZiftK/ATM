package atm;

import java.lang.Runnable;

public class Command implements Runnable{

    /** Runnable command*/
    Runnable command;
    /** Command description */
    String description;

    /**
     *
     * @param command : runnable method
     */
    public Command(Runnable command)
    {
        this.command = command;
    }

    /**
     *
     * @param command : runnable method
     * @param description : command description
     */
    public Command(Runnable command, String description)
    {
        this(command);
        this.description = description;
    }

    /**
     * Run command
     */
    @Override
    public void run()
    {
        this.command.run();
    }


    /**
     * Return description
     */
    @Override
    public String toString()
    {
        return this.description;
    }
}
