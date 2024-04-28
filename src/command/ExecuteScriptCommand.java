package command;

import command.exception.LackOfDataException;
import receiver.VectorCollection;
import receiver.exception.*;
import read.exception.*;
/** 
 * Реализации интерфейса {@link command.BaseCommand}. Эта конкретная команда инкапсулирует  запрос на исполнение команд из скрипта и привязывает его к получателю, вызывая соответствующую операцию на получателе.
 * 
 * @author Timofei Kaparulin
 * @version 1.0
*/
public class ExecuteScriptCommand implements BaseCommand{

    /**
     * Метод, определяющий операции для выполнения команды по дисполнение команд из скрипта
     * @param args название команды и параметры для нее
     * @exception LackOfDataException вызывается при недостатке аргументов для команды
     * @exception RecursionError ошибка зацикливания исполнения скрипта
    */
    public void execute(String[] args) throws LackOfDataException, RecursionError{
        if(args.length!=2){throw new LackOfDataException(args.length-1, 1);}
        VectorCollection.Execute(args[1]);
    }


    public void execute(String[] args, boolean isScript) throws LackOfDataException, RecursionError{
        execute(args);
    }

    public String call(){return "execute_script";}

    public String getName(){
        return call() + " [file_name]";   
    }

    public String getDescription(){
        return "execute commands from script";

    }
}
