package command;

import receiver.VectorCollection;
import command.exception.LackOfDataException;

/** 
 * Реализации интерфейса {@link command.BaseCommand}. Эта конкретная команда инкапсулирует  запрос на вывод справки по доступным командам и привязывает его к получателю, вызывая соответствующую операцию на получателе.
 * 
 * @author Timofei Kaparulin
 * @version 1.0
*/
public class HelpCommand implements BaseCommand{
    /**
     * Метод, определяющий операции для выполнения команды по выводу справки по доступным командам
     * @param args название команды и параметры для нее
     * @exception LackOfDataException вызывается при недостатке аргументов для команды
    */
    public void execute(String[] args) throws LackOfDataException{
        if(args.length!=1){throw new LackOfDataException(args.length-1, 0);}
        VectorCollection.help();
    }

    public String call(){return "help";}

    public String getName(){
        return call();   
    }

    public String getDescription(){
        return "show informations about available commands";

    }
}
