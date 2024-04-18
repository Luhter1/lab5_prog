package command;

import receiver.VectorCollection;
import command.exception.LackOfDataException;

/** 
 * Реализации интерфейса {@link command.BaseCommand}. Эта конкретная команда инкапсулирует  запрос на вывод истории последних 5-ти команд и привязывает его к получателю, вызывая соответствующую операцию на получателе.
 * 
 * @author Timofei Kaparulin
 * @version 1.0
*/
public class HistoryCommand implements BaseCommand{

    /**
     * Метод, определяющий операции для выполнения команды по выводу истории последних 5-ти команд
     * @param args название команды и параметры для нее
     * @exception LackOfDataException вызывается при недостатке аргументов для команды
    */
    public void execute(String[] args) throws LackOfDataException{ // empty
        //if(args.length!=1){throw new LackOfDataException(args.length-1, 0);}
        VectorCollection.History();
        
    }

    public void execute(String[] args, boolean isScript) throws LackOfDataException{
        execute(args);
    }

    public String call(){return "history";}

    public String getName(){
        return call();
    }

    public String getDescription(){
        return "show the 5 previous commands";
    }
}
