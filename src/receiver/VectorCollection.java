package receiver;

import java.time.LocalDate;
import data.ticket.Ticket;
import data.id.IDGenerator;
import java.util.*;
import data.CreateTicketObject;
import command.BaseCommand;
import data.validation.validation;
import data.price.Price;
import receiver.exception.*;
import java.io.*;
import read.ReadScript;
import invoker.CommandManager;
import java.lang.StackOverflowError;
import exception.MyException;
import data.validation.exception.*;
import read.exception.*;

// Receiver

/**
* Класс, который выполняет фактическую операцию во время выполнении команды
* 
* @author   Timofei Kaparulin
* @version  1.0
*/
public class VectorCollection{
    /**
    * Список, содержащий все дотупные команды
    */
    private static ArrayList<BaseCommand> Commands = new ArrayList<BaseCommand>();
    /**
    * Очередь, содержащая последовательность команд, считанных из скипта
    */
    public static ArrayDeque<String> scriptQueue = new ArrayDeque<>();
    /**
    * Коллекция, содержащая все данные о билетах
    */
    private static LinkedHashSet<Ticket> table;
    /**
    * Дата инициализации коллекции
    */
    private static LocalDate date;
    /**
    * Список, содержащий историю о последних 5-ти командах
    */
    private static ArrayDeque<String> history;
    /**
    * Путь до файла-коллекции
    */
    private static String PATH=null;

    /**
     * Инициализирует начальные данный для работы с коллекцией
     * <p>
     * <ul>
     *  <li>Инициализирует пустое множество для коллекции</li>
     *  <li>Установка даты инициализации коллекции</li>
     *  <li>Пустой список id</li>
     *  <li>Пустой список цен</li>
     * </ul>
     * @param path путь до пути к файлу-коллекции
    */
    public VectorCollection(String path){
        table = new LinkedHashSet<>();
        date = LocalDate.now();
        history = new ArrayDeque<>(5);
        new IDGenerator();
        new Price();
        PATH = path;
    }

    // execute_script command start
    /** 
     * Выполнение команды execute_script.
     * <p>
     * <ul>
     *   <li>Чтение команд из скрипта</li>
     *   <li>Запись их в очередь на выполнение с конца в начало 
     *       (нужно для рекурсивного вызова данной функции)</li>
     *   <li>Поочередное выполнение команд</li>
     * </ul>
     * <p>
     * Если возникнет ошибка при попытке выполнить считанную команду, то выводится ошибка и очередь продолжает свое опустошение
     * <p>
     * При переполнении стэка из-за рекурсивного вызова скриптом самого себя, очередь очищается
     * @param path путь до скрипта
     * @exception RecursionError ошибка зацикливания выполнения скрипта
     * @exception MyFileNotFoundException ошибка при чтении файла                  
     */
    public static void Execute(String path) throws RecursionError{
        try{
        List<String> scriptCommands = ReadScript.read(path);
        ListIterator<String> iterComs = scriptCommands.listIterator(scriptCommands.size());
        
        while(iterComs.hasPrevious()) {
            scriptQueue.push(iterComs.previous());
        }

        while(!scriptQueue.isEmpty()){
            String com = scriptQueue.pop();
            System.out.println("\033[1;94m\nExecuted command:\033[0m "+com);
            CommandManager.execute(com, true);
        }
        }catch(StackOverflowError e){
            scriptQueue = new ArrayDeque<>();
            throw new RecursionError();
        }
    }
    // execute_script command end

    // save command start
    /** 
     * Выполнение команды save.
     * <p>
     * <ul>
     *   <li>Итерация по коллекции</li>
     *   <li>Получение {@link data.ticket.Ticket#toWrite()} вида для билета</li>
     *   <li>Запись в файл-коллекцию</li>
     * </ul>
     */
    public static void Save(){
        try(PrintWriter printer = new PrintWriter(new FileWriter(PATH))){
            for(Ticket t: table){
                printer.println(t.toWrite());        
            }
            System.out.println("\n\u001B[32mCollection was succesfully saved into file\u001B[0m\n");
        }catch(IOException e){
                System.out.println("Ошибка записи в файл"); 
        } 
    }
    // save command end


    // history command start
    /** 
     * Выполнение команды history.
     * <p>
     * <ul>
     *   <li>Итерация по очереди команд в обратном порядке</li>
     *   <li>Вывод последних пяти команд, где под 1 находится наиболее старая</li>
     * </ul>
     */
    public static void History(){
        Iterator<String> HistoryIter = history.descendingIterator();
        int num = 1;
        System.out.println("\nHistory of command (from the latest):");
        while(HistoryIter.hasNext()){
            System.out.println(num++ +")"+HistoryIter.next());
        }
        System.out.println();
    }
    // history command end



    // print_field_descending_price command start
    /** 
     * Выполнение команды print_field_descending_price.
     * <p>
     * <ul>
     *   <li>Получение множества всех цен</li>
     *   <li>Запись его в структуру {@link java.util.TreeSet}</li>
     *   <li>Итерация в обратном порядке</li>
     *   <li>Вывод цены</li>
     * </ul>
     */
    public static void PrintFieldDescendingPrice(){
        TreeSet<Integer> Prices = new TreeSet<Integer>(Price.keySet());
        Iterator<Integer> PricesIter = Prices.descendingIterator();
        System.out.println("\nPrice by descending:");
        int i = 1;
        while(PricesIter.hasNext()){
            int price = PricesIter.next();
            ArrayList<Long> PriceId = Price.get(price);
            if(PriceId!=null&&!PriceId.isEmpty()){
                if(price!=0) System.out.println(i++ + ") " + price);
                if(price==0) System.out.println(i + ") " + null);
            }
        }
    }
    // print_field_descending_price command end



    // filter_by_price command start
    /** 
     * Выполнение команды filter_by_price.
     * <p>
     * <ul>
     *   <li>Валидация строкового значения цены</li>
     *   <li>Получения всех id билетов с данной ценой</li>
     *   <li>Вывод этих билетов, если они есть</li>
     *   <li>Вывод цены</li>
     * </ul>
     * 
     * @param args название команды и строковое значение цены
     * @exception PositiveException Причину возникновения ошибки смотрите в {@link data.validation.validation#realInt(String, int)}
     * @exception StringParseException Причину возникновения ошибки смотрите в {@link data.validation.validation#realInt(String, int)}
     */
    public static void FilterByPrice(String[] args) throws PositiveException, StringParseException{
        Integer price = validation.realInt(args[1], 1);
        ArrayList<Long> PriceId = Price.get(price);

        if(PriceId==null||PriceId.isEmpty()){System.out.println("\033[1;97m\nThere is no ticket with a value equals to the value entered\u001B[0m");}
        else{
            Iterator<Ticket> tableIter = table.iterator();
            Ticket ticket;
            while(tableIter.hasNext()){
                ticket = tableIter.next();
                if(PriceId.contains(ticket.id())){
                    System.out.println("\n"+ticket);
                }
            }
        }
        System.out.println();
    }
    // filter_by_price command end



    // remove_lower command start
    /** 
     * Выполнение команды remove_lower.
     * <p>
     * <ul>
     *   <li>Валидация строкового значения цены</li>
     *   <li>Получения всех id билетов с данной ценой</li>
     *   <li>Удаление этих билетов, если они есть</li>
     *   <li>Создание обьекта</li>
     * </ul>
     * @param args список с названием команды и параметрами для нее
     *
     */
    public static void removeLower(String[] args, boolean isScript){
        String name = CreateTicketObject.nameGenerate(args[1]);
        Integer price = CreateTicketObject.priceGenerate(args[2]);

        price = price == null ? 0 : price;
        ArrayList<Long> delete = new ArrayList<>();

        for(int key : Price.keySet()){
            if(key<price){
                for(long id : Price.get(key)){
                    delete.add(id);
                }
            }
        }

        if(delete.isEmpty()){
              System.out.println("\033[1;97m\nThere is no ticket with a value lower than the value entered\u001B[0m");          
        }

        for(long id: delete){
            String[] t = {"", ""+id};            
            remove(t);
        }
        String[] Nargs = {args[0], name, ""+price};
        add(Nargs, isScript, true);
    }
    // remove_lower command end



    // remove_any_by_price command start
    /** 
     * Выполнение команды remove_any_by_price.
     * <p>
     * <ul>
     *   <li>Валидация строкового значения цены</li>
     *   <li>Получения всех id билетов с данной ценой</li>
     *   <li>Удаление 1 из этих билетов, если они есть</li>
     *   <li>Вывод цены</li>
     * </ul>
     * 
     * @exception PositiveException Причину возникновения ошибки смотрите в {@link data.validation.validation#realInt(String, int)}
     * @exception StringParseException Причину возникновения ошибки смотрите в {@link data.validation.validation#realInt(String, int)}
     * @param args список с названием команды и ценой для удаления
     */
    public static void removeAnyByPrice(String[] args) throws PositiveException, StringParseException{
        Integer price = validation.realInt(args[1], 1);
        price = price == null ? 0 : price;
        ArrayList<Long> PriceId;
        if(price!=null){
            PriceId = Price.get(price);
        }else{
            PriceId = Price.get(0);
        }        
        if(PriceId==null||PriceId.isEmpty()){System.out.println("\033[1;97m\nThere is no ticket with a value equals to the value entered\u001B[0m\n");}
        else{
            String[] t = {"", ""+PriceId.get(0)};
            remove(t);
        }
    }
    // remove_any_by_price command end 



    // add_if_max command start
    /** 
     * Выполнение команды remove_any_by_price.
     * <p>
     * <ul>
     *   <li>Валидация строкового значения цены</li>
     *   <li>Создание обьекта, если цена больше максимальной в коллекции</li>
     * </ul>
     * @param args список с названием команды и параметрами команды
     */
    public static void addIf(String[] args, boolean isScript){
        String name = CreateTicketObject.nameGenerate(args[1]);
        Integer price = CreateTicketObject.priceGenerate(args[2]);
        String[] Nargs = {args[0], name, ""+price};
        if(price!=null&&price>Price.getMaxPrice()){add(Nargs, isScript, true);}
        else{add(Nargs, isScript, false);System.out.println("\033[1;97m\nElement will not be created because it does not have a maximum price value\n\u001B[0m");}
    }
    // add_if_max command end



    // remove command start
    /** 
     * Выполнение команды remove.
     * <p>
     * <ul>
     *   <li>Валидация строкового значения id</li>
     *   <li>Создание обьекта-заглушки по id (смотрите: {@link data.ticket.Ticket#toRemove(int)})</li>
     *   <li>Если обьект с данным id есть в коллекции, он удаляется</li>
     * </ul>
     * @param args список с названием команды и id для удаления
     */
    public static void remove(String[] args){
        long id = CreateTicketObject.idGenerate(args[1]);
        Ticket ticket = Ticket.toRemove(id);
        if(table.contains(ticket)){
            deepRemove(ticket, true);
            System.out.println("\n\u001B[32mTicket was succesfully deleted by id equals " + id+"\u001B[0m\n");
        }else{
            System.out.println("\033[1;97m\nThere is no ticket with that value in the collection\n\u001B[0m");
        }
    }
    // remove command end



    // info command start
    /** 
     * Выполнение команды info.
     * <p>
     * Выведение информации о коллекции
     */
    public static void info(){ // тип, дата инициализации, кол-во эл-ов
        System.out.println("\n\033[4;37mElement's type:\u001B[0m Ticket" + "\n\033[4;37mNumber of elements:\u001B[0m " + table.size()  + "\n\033[4;37mInitialization date:\u001B[0m " + date + "\n");
    }
    // info command end


    
    // show command start
    /** 
     * Выполнение команды show.
     * <p>
     * Выведение информации обо всех элементах коллекции
     */
    public static void show(){
        if(table.size()==0) System.out.println("\nCollection is an empty\n");
        for(Ticket i: table){
            System.out.println("\n"+i+"\n");
        }
    }
    // show command end



    // help command start
    /** 
     * Выполнение команды help.
     * <p>
     * Выведение информации обо всех доступных командах и их аргументах
     */
    public static void help(){
        for(BaseCommand i: Commands){
            System.out.println("\n\u001B[47m\u001B[30mCommand:\u001B[0m "+i.getName());
            System.out.println("  -- \033[4;37m" +  i.getDescription() + "\u001B[0m");
        }
        System.out.println();
    }

    /** 
     * Добавление команды в список команд.
     * <p>
     * @param command команда, реализованная в программе
     */
    public static void addCommand(BaseCommand command){
        Commands.add(command);
    }
    //help command end



    // clear command start
    /** 
     * Выполнение команды clear.
     * <p>
     * <ul>
     *   <li>Очистка коллекции</li>
     *   <li>Обновление даты инициализации коллекции</li>
     *   <li>Очистка истории</li>
     *   <li>Очистка списков id</li>
     *   <li>Очистка списка цен</li>
     * </ul>
     * 
     */
    public static void clear(){
        new VectorCollection(PATH);
        System.out.println("\n\u001B[32mCollection was succesfully cleared\u001B[0m\n");
    }
    // clear command end

    
    // add and update command start
    /** 
     * Выполнение команды add и update.
     * <p>
     * Генерация обьекта или его обновление по id.
     * <p>
     * Для выполнения add передается 2 аргумента - название билета и стоимость, при 3 аргументах - id, название, цена - выполняется update.
     * @param args список: команда и передаваемые значения
     */
    public static void add(String[] args){
        add(args, false, true);       
    }

    public static void add(String[] args, boolean isScript, boolean isMax){
        if(args.length==3){
            CreateTicketObject.generate("null", args, isScript, isMax);
            if(isMax){
                System.out.println("\n\u001B[32mA new element of type Ticket was added succesfully\u001B[0m\n");
            }
        }else{
            CreateTicketObject.generate(args[1], Arrays.copyOfRange(args, 1, 4), isScript, isMax);
            System.out.println("\n\u001B[32mThe element of type Ticket was updated succesfully\u001B[0m\n");
        }            
    }

    /** 
     * Добавление обьекта в коллекцию.
     * 
     * @param ticket обьект для добавления в коллекцию
     * @param not_last нужна ли сортировка коллекции
     * @param remove нужно ли удалить обьект с тем же id (для update)
     */
    public static void addToCollection(Ticket ticket,  long not_last, boolean remove){
        
        if(remove){
            deepRemove(ticket, false);
        }
        table.add(ticket);
        if(not_last==1) VectorCollection.sort();
    }
    // add and update command end



    // exit command start
    /** 
     * Выполнение команды exit.
     * <p>
     * Завершение работы программы без сохранения коллеции в файл
     */
    public static void exit(){
        System.out.println("\n\033[0;34mTerminating programm...\033[0m");
        System.exit(0);
    }
    // exit command end


    // starts some non-command functions
    /** 
     * Возвращает размер истории комманд.
     * @return размер истории команд     
    */
    public static int HistorySize(){
        return history.size();
    }

    /** 
     * Удаление самой старой команды из истории.
     */
    public static void removeLastHistory(){
        history.removeLast();
    }

    /** 
     * Добавление новой комманды.
     * @param command имя команды     
    */
    public static void pushHistory(String command){
        history.push(command);
    }
    /** 
     * Сортировка коллекции.
     */
    public static void sort(){
        table = new LinkedHashSet<Ticket>(new TreeSet<Ticket>(table));
    }
    /** 
     * Возвращает дату инициализации коллекции.
     * @return дата инициализаии коллекции
     */
    public static LocalDate getDate(){
        return date;
    }

    /** 
     * Удаление данных для обьектов коллекции
     * <p>
     * <ul>
     *   <li>При команде update несуществующего обьекта, добавляем его id</li>
     *   <li>Удаляем старую цену</li>
     *   <li>Если venue был не null, то удаляем его id</li>
     *   <li>При команде remove удаляем id обьекта</li>
     * @param ticket обьект для полного или частичного удаления значений
     * @param isTicketIdRemove удалять ли id обьекта
     */
    private static void deepRemove(Ticket ticket, boolean isTicketIdRemove){
        //System.out.println(IDGenerator.toAr());
        //System.out.println(IDGenerator.toArT()+"\n");
        //for(int i: Price.keySet()){
        //    System.out.println(i+ " " +Price.get(i));
        //}

        if(!table.remove(ticket)){IDGenerator.addIdT(ticket.id());}
        else if(Ticket.lastPrice()!=null){Price.removeIdPrice(Ticket.lastPrice(), ticket.id());}
        else{Price.removeIdPrice(0, ticket.id());}

        //for(int i: Price.keySet()){
        //    System.out.println(i+ " "+Price.get(i));
        //}


        if(Ticket.lastComparableVenueId()!=0){
            IDGenerator.removeIdV(Ticket.lastComparableVenueId());
            
        }
        
        if(isTicketIdRemove){
            IDGenerator.removeIdT(ticket.id());
                  
        }

        //System.out.println(IDGenerator.toAr());
        //System.out.println(IDGenerator.toArT());

    }
    

}
