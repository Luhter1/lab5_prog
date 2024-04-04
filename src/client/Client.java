package client;

import invoker.CommandManager;
import java.io.BufferedReader;
import receiver.VectorCollection;
import java.io.IOException;
import read.ReadCSV;


/** 
 * Класс отвечающий за создание обьектов команды, настройку их получателей и связывание с вызывающим.
 * @author Timofei Kaparulin
 * @version 1.0
*/
public class Client{

    // <a>{@link BufferedReader}</a>
    // @see invoker.CommandManager#CommandManager()

    /** 
     * Запускает работу клиента
     * <p>
     * <ul>
     *   <li>Инициализирует коллекцию для хранения и генератор id.</li>
     *   <li>Считывает значения из файла-коллекции и записывает в коллекцию.</li>
     *   <li>Добавляет тэги для вызова команд.</li>
     *   <li>Сохраняет ссылку на обьект для считывания значений</li>
     *   <li>Запускает цикл ожидания и исполнения.</li>
     * </ul>
     * <p>
     * Если при попытке чтения файла-коллекции возникнет ошибка {@link IOException}, то все значения будут сохраняться в новый файл, по пути path.
     * <p>
     * После считывания всей коллекции из файла, реализует сортировку по значению id
     * @param path путь до файла, хранящего коллекцию
     * @param input обьект откуда происходит считывание команд, введенных пользователем
     * @exception IOException Если произошло исключение при чтении из input 
     *                       
     */
    public void start(String path, BufferedReader input) throws IOException{
        new VectorCollection(path); //создает коллекцию для хранения и генератор id (пока нулевой)    
        try{
            
            ReadCSV.read(path);
            System.out.println("\u001B[32m" + "The collection has been read, all data is correct");
            System.out.println("Program is running...\n" +  "\u001B[0m");
            VectorCollection.sort();
        }catch(IOException e){
            System.out.println("\u001B[31mКоллекция не была загружена\u001B[0m\n\nНовые значения будут сохраняться в файле:\n    "+path);
            

        }

        new CommandManager();
        CommandManager.setScan(input);
        // sort by id
        String str;

        do {// gthtytcnb exit to command and clear too
            System.out.print("\u001B[33m" + "Entry command: " +"\u001B[0m");
            str = input.readLine();

            CommandManager.execute(str);

        } while(true);  
  
    }
    // получаем команды из кс
    // передаем строку коммандному менеджеру
    // он определяет команду и вызывает ее
    // она в свою очередь выполняется
}
