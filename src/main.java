import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.IOException;
import client.Client;

/**
* Класс, запускаемый пользователем, передает клиенту путь до файла и обьект для считывания команд
* @see client.Client
* @author   Timofei Kaparulin
* @version  1.0
*/
public class main{

    /** 
     * Получает путь до файла-коллекции через переменную окружения, создает обьект для считывания ввода команд пользователя, передает их клиенту и запускает его работу.   
     * @param args аргументы, вводимые при старте программы в cmd                    
     */
    public static void main(String[] args){

        BufferedReader br = new BufferedReader(new InputStreamReader(System.in, System.console().charset()));
        String path = System.getenv("LAB5FILE");
        try{
            new Client().start(path, br);
        }catch(IOException e){
            e.printStackTrace();
        }
    }   

}
