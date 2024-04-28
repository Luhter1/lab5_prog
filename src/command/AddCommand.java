package command;

import receiver.VectorCollection;
import command.exception.LackOfDataException;

/** 
 * Реализации интерфейса {@link command.BaseCommand}. Эта конкретная команда инкапсулирует  запрос на добавление пользователем нового обьекта в коллекцию и привязывает его к получателю, вызывая соответствующую операцию на получателе.
 * 
 * @author Timofei Kaparulin
 * @version 1.0
*/
public class AddCommand implements BaseCommand{

    /**
     * Метод, определяющий операции для выполнения команды по добавлению пользователем нового обьекта
     * @param args название команды и параметры для нее
     * @exception LackOfDataException вызывается при недостатке аргументов для команды
    */
    public void execute(String[] args) throws LackOfDataException{ // name, price
        execute(args, false);
    }

    public void execute(String[] args, boolean isScript) throws LackOfDataException{
    
        if(args.length==1){
            String[] command={args[0], "", null};
            VectorCollection.add(command, isScript, true);
        }else if(args.length==2){
            String[] command={args[0], args[1], null};
            VectorCollection.add(command, isScript, true);
        }else if(args.length==3){
            String[] command={args[0], args[1], args[2]};
            VectorCollection.add(command, isScript, true);
        }else{throw new LackOfDataException(args.length-1, 2);}  
    }

    public String call(){return "add";}

    public String getName(){
        return call() + " [name price]";
    }

    public String getDescription(){
        return "add new element to the end of vector's collection\n[name] cannot be null or empty\n[price] must be greater then 0 or be null";
    }
}
