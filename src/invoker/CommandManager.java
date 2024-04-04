package invoker;

import java.util.LinkedHashMap;
import command.*;
import invoker.exception.*;
import java.lang.NullPointerException;
import receiver.VectorCollection;
import exception.MyException;
import java.io.BufferedReader;

/** 
 * Класс, отвечающий за инициирование выполнения команды.
 * <p>
 * Он содержит ссылку на объект команды и может выполнить команду, вызвав ее метод execute().
 * @author Timofei Kaparulin
 * @version 1.0
*/
public class CommandManager{
    /**
     * Обьект, с которого считываются команды
    */
    private static BufferedReader Scan;
    /**
     * Словарь команд, ключ - значение {@link command.BaseCommand#call()} для конкретной команды
    */
    private static LinkedHashMap<String, BaseCommand> commandlist;
    /**
     * Все доступные команды
    */    
    private static BaseCommand[] commands = {
        new AddCommand(), //
        new HelpCommand(), //
        new ExitCommand(), //
        new ShowCommand(), //
        new ClearCommand(), //
        new InfoCommand(), //
        new UpdateCommand(), //
        new RemoveByIdCommand(), //
        new AddIfMaxCommand(), //  
        new RemoveAnyByPrice(), //
        new RemoveLower(), //
        new FilterByPrice(), //
        new PrintFieldDescendingPrice(), // 
        new HistoryCommand(), //
        new SaveCommand(), //
        new ExecuteScriptCommand(), //
    };

    /**
     * Инициализирует пустой словарь с командами.
     * <p>
     * Ключ - значение {@link command.BaseCommand#call()} для конкретной команды
    */
    public CommandManager(){
        commandlist = new LinkedHashMap<>();
        for(BaseCommand i : commands){
            commandlist.put(i.call(), i);
            VectorCollection.addCommand(i);
        }
    }

    /**
     * Определяет введенную команду и исполняет ее.
     * <p>
     * Вызывает метод {@link command.BaseCommand#execute(String[])} для конкретной команды
     * Добавляет команду в очередь истории команд
     * @param line Введенная строка с командой
    */ 
    public static void execute(String line){
        try{
            String commandName = line.split(" ")[0];
            
            // обработка, если нет команды
            try{
                commandlist.get(commandName).execute(line.split(" "));
                if(VectorCollection.HistorySize()==5) VectorCollection.removeLastHistory();
                VectorCollection.pushHistory(commandName);
            }catch(NullPointerException e){throw new CommandException(commandName);}

        }catch(Exception e){
            System.out.print("\u001B[31m\nError: \u001B[0m" + e); 
            if(((MyException)e).NeedArgs()){            
                System.out.println("\nUse help to see available commands and arguments' properties\n");
            }else{System.out.println("\n");}        
}
    }

    /**
     * Возвращает обьект, с которого считываются команды
     * @return обьект, с которого считываются команды
    */ 
    public static BufferedReader getScan(){
        return Scan;
    }

    /**
     * Устанавливает обьект, с которого считываются команды
     * @param scan обьект, с которого считываются команды
    */ 
    public static void setScan(BufferedReader scan){
        Scan = scan;
    }
} // сортировка по id, поиск по id
