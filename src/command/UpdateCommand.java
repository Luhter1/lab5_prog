package command;

import receiver.VectorCollection;
import command.exception.LackOfDataException;
/** 
 * Реализации интерфейса {@link command.BaseCommand}. Эта конкретная команда инкапсулирует  запрос на обновление значения обьекта коллекции по его id и привязывает его к получателю, вызывая соответствующую операцию на получателе.
 * 
 * @author Timofei Kaparulin
 * @version 1.0
*/
public class UpdateCommand implements BaseCommand{

    /**
     * Метод, определяющий операции для выполнения команды по обновлению значения обьекта коллекции по его id
     * @param args название команды и параметры для нее
     * @exception LackOfDataException вызывается при недостатке аргументов для команды
    */        
    public void execute(String[] args) throws LackOfDataException{ // name, price
        if(args.length!=4){throw new LackOfDataException(args.length-1, 3);}
        VectorCollection.add(args); // create new ticket
        
    }

    public String call(){return "update";}

    public String getName(){
        return call() + " [id name price]";
    }

    public String getDescription(){
        return "update element's value by id\n[id] must be greater then 0 and not null\n[name] cannot be null or empty\n[price] must be greater then 0 or be null";
    }
}
