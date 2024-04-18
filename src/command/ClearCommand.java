package command;

import receiver.VectorCollection;
import command.exception.LackOfDataException;

/** 
 * Реализации интерфейса {@link command.BaseCommand}. Эта конкретная команда инкапсулирует  запрос на очистку коллекции и привязывает его к получателю, вызывая соответствующую операцию на получателе.
 * 
 * @author Timofei Kaparulin
 * @version 1.0
*/
public class ClearCommand implements BaseCommand{

    /**
     * Метод, определяющий операции для выполнения команды по очистке коллекции
     * @param args название команды и параметры для нее
     * @exception LackOfDataException вызывается при недостатке аргументов для команды
    */
    public void execute(String[] args) throws LackOfDataException{ // name, price
        //if(args.length!=1){throw new LackOfDataException(args.length-1, 0);}
        VectorCollection.clear();
        
    }

    public void execute(String[] args, boolean isScript) throws LackOfDataException{
        execute(args);
    }

    public String call(){return "clear";}

    public String getName(){
        return call();
    }

    public String getDescription(){
        return "collection's clean-up";
    }
}
