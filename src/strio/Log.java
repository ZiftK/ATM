package strio;

public class Log {

    /** Log file path */
    protected String logPath;

    /**
     * Create new log file in specified path
     * @param logPath : log file path
     */
    public Log(String logPath){

        // set log path
        this.logPath = logPath;

        // create log file
        this.create();
    }

    /**
     * Create new empty log file
     */
    public void create(){
        WNR.createFile(logPath,"");
    }

    /**
     * Add text to log file
     * @param newContent : new text
     */
    public void write(String newContent){

        // old log content
        String content = "";

        // concatenate content lines
        for(String line : WNR.readFileLines(logPath)){
            content += line;
        }

        // add new lines
        content += newContent;

        // override log file
        WNR.write(logPath,content);
    }
}
