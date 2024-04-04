package data;

import java.util.Date;
import receiver.VectorCollection;
import java.util.Scanner;
import data.id.IDGenerator;
import data.coordinates.Coordinates;
import data.validation.validation;
import java.text.ParseException; // Data
import data.validation.exception.*;
import data.ticket.*;
import data.venue.*;
import data.price.Price;
import data.ticket.Ticket;
import java.io.BufferedReader;
import java.io.IOException;
import invoker.CommandManager;

/** 
 * Класс, отвечающий за создание обьектов типа {@link data.ticket.Ticket}.
 * @author Timofei Kaparulin
 * @version 1.0
*/
public class CreateTicketObject{

    /* обработка нужна, так как значения считываются из файла, можно изменить файл
    ++ id (unique, not null, >0)
    ++ name (not null, not empty)
    ++ coord (not null, parse error) - так как состоит из 2 координат, не нужно проверять их, просто парсить
    ++ date (not null)
    ++ price (might be null, >0)
    ++ type (not null)
    ++ venue_id (>0, unique)
    ++ venue_name (not null, not empty)
    ++ venue_capacity (might be null, >0)
    ++ venue_type (might be null)
    */

    /** 
     * Создает обьект типа {@link data.ticket.Ticket} из значений, получаемых при чтении файла-коллекции.
     * <p>
     * Впоследствии записывает его в коллекцию с помощью {@link receiver.VectorCollection#addToCollection(Ticket, long, boolean)}.
     * <p>
     * Для каждого поля этого обьекта реализована валидация значений, по условиям варианта, если в ходе нее возникает ошибка, будет выведена строка в файле-коллекции, где она произошла.
     * <p>
     * Для каждого обьекта реализовано: добавление его id в генератор id, а также в словарь цен по ключу равному цене за билет, проверка цены обьекта на максимальность.
     * <p>
     * При значении venue не равном null, его id записывается в генератор id для venue
     *
     * @param line строка в файле-коллекции, на которой находятся получаемые данные
     * @param args значения содержащиеся на строке line в файле-коллекции
     *
     * @see data.id.IDGenerator                       
     */
    public static void create(int line, String[] args){

        Long id;
        String name;
        Coordinates coord;
        Date date;
        Integer price = null;
        TicketType type;
        Venue venue = null;

        try{
            // id validation
            id =  validation.ID("ticket", args[0], true);

            IDGenerator.addIdT(id);
            
            // name validation
            name = validation.name("ticket", args[1]);

            // coordinate validation
            coord = validation.coordinates(args[2], args[3]);

            // date validation
            try{
                date = Ticket.formater.parse(args[4]);
            }catch(ParseException e){throw new StringParseException("Date with format yyyy-MM-dd", args[4]);}
            
            // price validation
            price = validation.realInt(args[5], 1);
            if(price!=null) {
                Price.put(price, id);
                Price.maxPrice(price);
            }else{Price.put(0, id);}
            // type validation
            type = validation.type(TicketType.class, args[6]);
            
            if(!args[7].equals("null")){
                long venue_id;
                String venue_name;
                Integer venue_capacity;
                VenueType venue_type = null;

                // venue id validation
                venue_id = validation.ID("venue", args[7], true); // venue_id

                IDGenerator.addIdV(venue_id);

                // venue name validation
                venue_name = validation.name("venue", args[8]);
                
                // venue capacity validation
                venue_capacity = validation.realInt(args[9], 0);
                if(!args[10].equals("null")){venue_type = validation.type(VenueType.class, args[10]);}

                venue = new Venue(venue_id, venue_name, venue_capacity, venue_type);
            }
            VectorCollection.addToCollection(new Ticket(id, name, coord, date, price, type, venue),
            0, false);

        }catch(Exception e){System.out.println("Data reading error on line "+ line+ ":\n    "+e); System.exit(0);}

    }

     /** 
     * Нужен для генерации одного из полей обьекта типа {@link data.ticket.Ticket} пользователем.
     * <p>
     * Реализована валидация значения, по условиям варианта, если в ходе нее возникает ошибка, пользователю будет предложено ввести значение заново.
     * 
     * @param SPrice стоимость билета, вводимая пользователем вместе с командой
     *          
     * @return возвращает стоимость билета       
     */
    public static Integer priceGenerate(String SPrice){
        BufferedReader scan = CommandManager.getScan();
        Integer price;
        try{
            price = validation.realInt(SPrice, 1);
        }catch(Exception e){

            System.out.println("\u001B[31m" + "\nError: " +"\u001B[0m"+e);

            while(true){
                try{
                    System.out.print("\nInput ticket price (int or null): ");
                    price = validation.realInt(scan.readLine(), 1);  
                    break;

                } catch(Exception ex){System.out.println("\u001B[31m" + "\nError: " +"\u001B[0m"+ex);}
            }
        }
        return price;
    }

     /** 
     * Нужен для генерации одного из полей обьекта типа {@link data.ticket.Ticket} пользователем.
     * <p>
     * Реализована валидация значения, по условиям варианта, если в ходе нее возникает ошибка, пользователю будет предложено ввести значение заново.
     * 
     * @param SId значение id, вводимое пользователем вместе с командой update
     *          
     * @return возвращает значение id билета        
     */
    public static long idGenerate(String SId){
        BufferedReader scan = CommandManager.getScan();
        long Id;
        try{
                Id = validation.ID("ticket", SId, false);

            }catch(Exception e){

                System.out.println("\u001B[31m" + "\nError: " +"\u001B[0m"+e);

                while(true){
                    try{
                        System.out.print("\nInput id (long): ");
                        Id = validation.ID("ticket", scan.readLine(), false);  
                        break;

                    } catch(Exception ex){System.out.println("\u001B[31m" + "\nError: " +"\u001B[0m"+ex);}

                }
            }
        return Id;

    }

     /** 
     * Нужен для генерации одного из полей обьекта типа {@link data.ticket.Ticket} пользователем.
     * <p>
     * Реализована валидация значения, по условиям варианта, если в ходе нее возникает ошибка, пользователю будет предложено ввести значение заново.
     * 
     * @param name имя билета, вводимое пользователем вместе с командой
     *          
     * @return возвращает тип билета         
     */
    public static String nameGenerate(String name){
        BufferedReader scan = CommandManager.getScan();
        try{
            name = validation.name("ticket", name);
        }catch(Exception e){

            System.out.println("\u001B[31m" + "\nError: " +"\u001B[0m"+e);

            while(true){
                try{
                    System.out.print("\nInput ticket name (String): ");
                    name = validation.name("ticket", scan.readLine());  
                    break;

                } catch(Exception ex){System.out.println("\u001B[31m" + "\nError: " +"\u001B[0m"+ex);}

            }
        }

        return name;
    }

     /** 
     * Нужен для генерации одного из полей обьекта типа {@link data.ticket.Ticket} пользователем.
     * <p>
     * Реализована валидация значения, по условиям варианта, если в ходе нее возникает ошибка, пользователю будет предложено ввести значение заново.
     * 
     *          
     * @return возвращает координаты места         
     */
    public static Coordinates coordinateGenerate(){
        BufferedReader scan = CommandManager.getScan();
        String x, y;
        Coordinates coord;
        while(true){
            try{
                System.out.print("\nInput x (double): ");
                x = scan.readLine(); 

                System.out.print("Input y (double): ");
                y = scan.readLine();    

                coord = validation.coordinates(x, y);
                break;

            } catch(Exception e){System.out.println("\u001B[31m" + "\nError: " +"\u001B[0m"+e);}

        }

        return coord;
    }


     /** 
     * Нужен для генерации одного из полей обьекта типа {@link data.ticket.Ticket} пользователем.
     * <p>
     * Реализована валидация значения, по условиям варианта, если в ходе нее возникает ошибка, пользователю будет предложено ввести значение заново.
     * 
     *          
     * @return возвращает тип билета         
     */
    public static TicketType typeGenerate(){
        BufferedReader scan = CommandManager.getScan();
        TicketType type;
        while(true){
            try{
                System.out.print("\nSelect ticket type from:\n-- \"vip\"\n-- \"usual\"\n-- \"budgetary\"\n-- \"cheap\"\nInput: ");
                type = validation.type(TicketType.class, scan.readLine().toUpperCase());                 
                break;

            } catch(Exception e){System.out.println("\u001B[31m" + "\nError: " +"\u001B[0m"+e);}

        }
    
        return type;
    }

    /* нужна обработка
    ++ name
    ++ price
    ++ Coordinates(x,y)
    ++ type
    */
    /** 
     * Создает обьект типа {@link data.ticket.Ticket} из значений, вводимых пользователем.
     * <p>
     * Впоследствии записывает его в коллекцию с помощью {@link receiver.VectorCollection#addToCollection(Ticket, long, boolean)}.
     * <p>
     * Для каждого поля этого обьекта реализована валидация значений, по условиям варианта, если в ходе нее возникает ошибка, пользователю будет предложено ввести некорректное значение заново.
     * <p>
     * Для каждого обьекта реализовано: генерация id обьекта с помощью генератора id, 
     * если, он не передается вручную, также в словарь цен по ключу равному цене за билет записывается id обьекта и идет проверка цены на максимальность.
     *
     * @param SId нужен для задания id пользователем вручную, в противном случае содержит значение "null"
     * @param args значения, получаемые при вызове пользователем команды,
     *             содержат название команды, имя обьекта и его стоимость
     *          
     * @see data.id.IDGenerator              
     */
    public static void generate(String SId, String[] args){
        Coordinates coord;
        TicketType type;
        Venue venue;
        String name;
        Integer price;
        boolean remove = false;
        long[] id = {0, 1};

        BufferedReader scan = CommandManager.getScan();

        if(SId.equals("null")){
            id = IDGenerator.currentIdT();
        }else{
            remove = true;       
            id[0] = idGenerate(SId);
        }

        // name generation
        name = nameGenerate(args[1]);        

        // price generation
        price = priceGenerate(args[2]);
        
        if(price!=null){
            Price.put(price, id[0]);
            Price.maxPrice(price);
        }else{Price.put(0, id[0]);}
    
        // Coordinates generation
        coord = coordinateGenerate();

        // Date generation
        Date date = new Date();
        
        // Ticket Type generation
        type = typeGenerate();       

        // Is user want to add venue
        String ans;
        while(true){

            System.out.print("\nCreate venue (yes or no): ");
            try{
                ans = scan.readLine().toLowerCase();
                if(ans.equals("no")){venue = null; break;}
                else if(ans.equals("yes")){venue = CreateTicketObject.venueGenerate(); break;}
            }catch(IOException e){
                System.out.println("\u001B[31m" + "\nError: " +"\u001B[0m incorrect velue");
            }          
        }

       VectorCollection.addToCollection(new Ticket(id[0], name, coord, date, price, type, venue),
            id[1], remove);
    }


    /* нужна обработка  и id генератор
    ++ name
    ++ capacity
    ++ type
    */
    /** 
     * Создает обьект типа {@link data.venue.Venue} из значений, вводимых пользователем.
     * Вызывается, если пользователь укажет создать обьект данного типа, как одно из полей, во время создания обьекта типа {@link data.ticket.Ticket}.
     * <p>
     * Для каждого поля этого обьекта реализована валидация значений, по условиям варианта, если в ходе нее возникает ошибка, пользователю будет предложено ввести некорректное значение заново.
     * <p>
     * Для каждого обьекта реализовано: генерация id обьекта с помощью генератора id для обьектов данного типа. 
     * @return возвращает заведение для билета
     * @see data.id.IDGenerator                        
     */
    public static Venue venueGenerate(){
        long id = IDGenerator.currentIdV();
        String name;
        Integer capacity;
        VenueType type = null;

        BufferedReader scan = CommandManager.getScan();

        // venue name generation
        while(true){
            try{
                System.out.print("\nInput venue name (String): ");
                name = validation.name("venue", scan.readLine());                
                break;

            } catch(Exception e){System.out.println("\u001B[31m" + "\nError: " +"\u001B[0m"+e);}

        }

        // venue capacity generation
        while(true){
            try{
                System.out.print("\nInput venue capacity (int or null): ");
                capacity = validation.realInt(scan.readLine(), 0); //Передать на валидацию,                 
                break;

            } catch(Exception e){System.out.println("\u001B[31m" + "\nError: " +"\u001B[0m"+e);}

        }


        // venue type generation
        String line;
        while(true){
            try{
                System.out.print("\nSelect venue type from:\n-- \"pub\"\n-- \"bar\"\n-- \"mall\"\n-- null\nInput: ");
                
                line = scan.readLine();
                if(!line.equals("null")){
                    type = validation.type(VenueType.class, line.toUpperCase());
                }                 
                break;

            } catch(Exception e){System.out.println("\u001B[31m" + "\nError: " +"\u001B[0m"+e);}

        }

        return new Venue(id, name, capacity, type);
    }
    
}
