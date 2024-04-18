package command;

import receiver.VectorCollection;
import command.exception.LackOfDataException;
/** 
 * Реализации интерфейса {@link command.BaseCommand}. Эта конкретная команда инкапсулирует  запрос на вывод всех элементов коллекции и привязывает его к получателю, вызывая соответствующую операцию на получателе.
 * 
 * @author Timofei Kaparulin
 * @version 1.0
*/
public class ShowCommand implements BaseCommand{
    /**
     * Метод, определяющий операции для выполнения команды по выводу всех элементов коллекции
     * @param args название команды и параметры для нее
     * @exception LackOfDataException вызывается при недостатке аргументов для команды
    */
    public void execute(String[] args) throws LackOfDataException{
        //if(args.length!=1){throw new LackOfDataException(args.length-1, 0);}
        VectorCollection.show();
    }

    public void execute(String[] args, boolean isScript) throws LackOfDataException{
        execute(args);
    }

    public String call(){return "show";}

    public String getName(){
        return call();   
    }

    public String getDescription(){
        return "output all elements of the collection in string representation to the standard output stream";

    }
}
