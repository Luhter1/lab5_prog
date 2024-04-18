package command;

import command.exception.LackOfDataException;
import receiver.VectorCollection;
/** 
 * Реализации интерфейса {@link command.BaseCommand}. Эта конкретная команда инкапсулирует  запрос на сохранение коллекции в файл и привязывает его к получателю, вызывая соответствующую операцию на получателе.
 * 
 * @author Timofei Kaparulin
 * @version 1.0
*/
public class SaveCommand implements BaseCommand{

    /**
     * Метод, определяющий операции для выполнения команды по сохранению коллекции в файле
     * @param args название команды и параметры для нее
     * @exception LackOfDataException вызывается при недостатке аргументов для команды
    */  
    public void execute(String[] args) throws LackOfDataException{
        //if(args.length!=1){throw new LackOfDataException(args.length-1, 0);}
        VectorCollection.Save();
    }

    public void execute(String[] args, boolean isScript) throws LackOfDataException{
        execute(args);
    }

    public String call(){return "save";}

    public String getName(){
        return call();   
    }

    public String getDescription(){
        return "save the collection to file";

    }
}
