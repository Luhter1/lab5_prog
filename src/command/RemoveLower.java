package command;

import receiver.VectorCollection;
import command.exception.LackOfDataException;

/** 
 * Реализации интерфейса {@link command.BaseCommand}. Эта конкретная команда инкапсулирует  запрос на удаление из коллекции всех обьектов с id меньше, чем указанное, и привязывает его к получателю, вызывая соответствующую операцию на получателе.
 * 
 * @author Timofei Kaparulin
 * @version 1.0
*/
public class RemoveLower implements BaseCommand{

    /**
     * Метод, определяющий операции для выполнения команды по удалению из коллекции всех обьектов с id меньше, чем указанное
     * @param args название команды и параметры для нее
     * @exception LackOfDataException вызывается при недостатке аргументов для команды
    */
    public void execute(String[] args) throws LackOfDataException{ // name, price
        if(args.length!=3){throw new LackOfDataException(args.length-1, 2);}  
        VectorCollection.removeLower(args); // create new ticket
    }

    // создать обьект билет, получить его прайс и удалить
    public String call(){return "remove_lower";}

    public String getName(){
        return call() + " [name price]";
    }

    public String getDescription(){
        return "remove all elements from the collection if their price lower then new element\n[name] cannot be null or empty\n[price] must be greater then 0 or be null";
    }
}
