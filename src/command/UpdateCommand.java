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
        execute(args, false);
    }

    public void execute(String[] args, boolean isScript) throws LackOfDataException{ // name, price
        if(args.length==1){
            String[] command={args[0], null, "", null};
            VectorCollection.add(command, isScript, true);
        }else if(args.length==2){
            String[] command={args[0], args[1], "", null};
            VectorCollection.add(command, isScript, true);
        }else if(args.length==3){
            String[] command={args[0], args[1], args[2], null};
            VectorCollection.add(command, isScript, true);
        }else if(args.length==4){
            String[] command={args[0], args[1], args[2], args[3]};
            VectorCollection.add(command, isScript, true);
        }else{throw new LackOfDataException(args.length-1, 3);}  
        
    }

    public String call(){return "update";}

    public String getName(){
        return call() + " [id name price]";
    }

    public String getDescription(){
        return "update element's value by id\n[id] must be greater then 0 and not null\n[name] cannot be null or empty\n[price] must be greater then 0 or be null";
    }
}
