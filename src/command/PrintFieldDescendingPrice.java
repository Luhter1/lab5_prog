package command;

import receiver.VectorCollection;
import command.exception.LackOfDataException;

/** 
 * Реализации интерфейса {@link command.BaseCommand}. Эта конкретная команда инкапсулирует  запрос на вывод значения цен в порядке убывания и привязывает его к получателю, вызывая соответствующую операцию на получателе.
 * 
 * @author Timofei Kaparulin
 * @version 1.0
*/
public class PrintFieldDescendingPrice implements BaseCommand{

    /**
     * Метод, определяющий операции для выполнения команды по выводу значения цен в порядке убывания
     * @param args название команды и параметры для нее
     * @exception LackOfDataException вызывается при недостатке аргументов для команды
    */   
    public void execute(String[] args) throws LackOfDataException{ // name, price
        //if(args.length!=1){throw new LackOfDataException(args.length-1, 0);}  
        VectorCollection.PrintFieldDescendingPrice(); // create new ticket
    }

    public void execute(String[] args, boolean isScript) throws LackOfDataException{
        execute(args);
    }

    // создать обьект билет, получить его прайс и удалить
    public String call(){return "print_field_descending_price";}

    public String getName(){
        return call();
    }

    public String getDescription(){
        return "output all price's value in descending order";
    }
}
