import java.io.*;
import java.util.*;

public class Accountant {

    String name = null;
    String password = null;
    int balance = 10000;
    int id = 0;
    static int MIN_BALANCE = 1000;
    int account_number = MIN_BALANCE;
    String filename;
    List<Map<Details,String>> map;
    int transaction_id;

    Accountant(String name,String password,int balance,int id,int account_number) throws IOException
    {
        this.name = name;
        this.password = password;
        this.balance = balance;
        this.id = id;
        this.account_number = account_number;
        this.filename = account_number+".txt";
        create_txt();
        this.map = getMap();
    }

    Accountant(String name,String password,int id) throws IOException
    {
        this.name = name;
        this.password = password;
        ++id;
        this.id = id;
        this.account_number = id + account_number;
        this.filename = account_number+".txt";
        create_txt();
        this.map = getMap();
    }

    public void first_transaction() throws IOException
    {
        Transaction transaction = new Transaction(this, Type.OPENING, 0);
        transaction.write_transaction();
    }

    public List<Map<Details,String>> getMap() throws IOException
    {
        List<Map<Details,String>> list = new ArrayList<>();

        File file = new File(filename);
        Scanner scan = new Scanner(file);

        while(scan.hasNext())
        {
            String[] line = scan.nextLine().split(" ");
            String id = line[0];
            String type = line[1];
            String amount = line[2];
            String balance = line[3];

            Map<Details,String> map_temp = new HashMap<>();
            map_temp.put(Details.ID,id);
            map_temp.put(Details.TYPE,type);
            map_temp.put(Details.AMMOUNT,amount);
            map_temp.put(Details.BALANCE,balance);

            list.add(map_temp);
        }

        this.transaction_id = list.size();

        scan.close();
        return list;
    }

    public void create_txt() throws IOException
    {
        File file = new File(filename);
        if(!file.exists()) file.createNewFile();
    }

    public static String addLine(Accountant accountant)
    {
        String line = accountant.id + " " + accountant.account_number + " " + accountant.name + " " + accountant.balance + " " + accountant.password;
        return line;
    }

    public static String encryptPassword(String password)
    {
        String encrypt = "";
        for(char c:password.toCharArray())
        {
            if(Character.isDigit(c))
            {
                if(c=='9') c = '0';
                else c+=1;
            }
            if(c>='a' && c<='z')
            {
                if(c=='z') c = 'a';
                else c+=1;
            }
            if(c>='A' && c<='Z')
            {
                if(c=='Z') c = 'A';
                else c+=1;
            }

            encrypt+=String.valueOf(c);
        }

        return encrypt;
    }
}
