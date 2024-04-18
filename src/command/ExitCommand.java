package command;

import receiver.VectorCollection;
import command.exception.LackOfDataException;

/** 
 * Реализации интерфейса {@link command.BaseCommand}. Эта конкретная команда инкапсулирует  запрос на выход без сохранения коллекции в файл и привязывает его к получателю, вызывая соответствующую операцию на получателе.
 * 
 * @author Timofei Kaparulin
 * @version 1.0
*/
public class ExitCommand implements BaseCommand{

    /**
     * Метод, определяющий операции для выполнения команды по выходу без сохранения коллекции в файл
     * @param args название команды и параметры для нее
     * @exception LackOfDataException вызывается при недостатке аргументов для команды
    */
    public void execute(String[] args) throws LackOfDataException{ // empty
        //if(args.length!=1){throw new LackOfDataException(args.length-1, 0);}
        VectorCollection.exit();   
    }

    public void execute(String[] args, boolean isScript) throws LackOfDataException{
        execute(args);
    }

    public String call(){return "exit";}

    public String getName(){
        return call();
    }

    public String getDescription(){
        return "terminate the program without saving to a file";
    }
}
