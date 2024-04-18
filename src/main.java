import java.io.*;
import client.Client;
import java.lang.NullPointerException;
/**
* Класс, запускаемый пользователем, передает клиенту путь до файла и обьект для считывания команд
* @see client.Client
* @author   Timofei Kaparulin
* @version  1.0
*/
public class main{

// сделать повторный запрос всех параметров, чтобы исполнять скрипт
// выполнение cat ... | java -jar ...
    /** 
     * Получает путь до файла-коллекции через переменную окружения, создает обьект для считывания ввода команд пользователя, передает их клиенту и запускает его работу.   
     * @param args аргументы, вводимые при старте программы в cmd                    
     */
    public static void main(String[] args){

            BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
            String path = null;
            
            try{
                path = System.getenv("LAB5FILE");
            }catch(NullPointerException e){

                System.out.println("\u001B[31m\nError: \u001B[0m переменая окружения не обнаружена\n");
                System.exit(0);
            }
           
            try{
                new Client().start(path, br);
            }catch(IOException e){
                e.printStackTrace();
            }
    }   

}
