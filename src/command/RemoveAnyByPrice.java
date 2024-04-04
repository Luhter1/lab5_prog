package command;

import receiver.VectorCollection;
import command.exception.LackOfDataException;
import data.validation.exception.PositiveException;
import data.validation.exception.StringParseException;
/** 
 * Реализации интерфейса {@link command.BaseCommand}. Эта конкретная команда инкапсулирует  запрос на удаление 1-ого обьекта по указанной цене и привязывает его к получателю, вызывая соответствующую операцию на получателе.
 * 
 * @author Timofei Kaparulin
 * @version 1.0
*/
public class RemoveAnyByPrice implements BaseCommand{

    /**
     * Метод, определяющий операции для выполнения команды по удалению 1-ого обьекта по указанной цене
     * @param args название команды и параметры для нее
     * @exception LackOfDataException вызывается при недостатке аргументов для команды
     * @exception PositiveException не положительное значение цены
     * @exception StringParseException путстое или равное null значение
    */    
    public void execute(String[] args) throws LackOfDataException, PositiveException, StringParseException{ // name, price
        if(args.length!=2){throw new LackOfDataException(args.length-1, 1);}  
        VectorCollection.removeAnyByPrice(args); // create new ticket
    }

    // создать обьект билет, получить его прайс и удалить
    public String call(){return "remove_any_by_price";}

    public String getName(){
        return call() + " [price]";
    }

    public String getDescription(){
        return "remove 1 element from the collection by given price\n[price] must be greater then 0 or be null";
    }
}
