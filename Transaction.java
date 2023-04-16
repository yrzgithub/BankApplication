import java.util.*;
import java.io.*;


public class Transaction {

    int account_number;
    Type type;
    int balance;
    int amount;
    int id;
    Accountant accountant;

    Transaction(Accountant accountant,Type type,int amount) throws IOException
    {
        this.accountant = accountant;
        this.account_number = accountant.account_number;
        this.type = type;
        this.id = accountant.transaction_id;
        this.balance = accountant.balance;
        this.amount = amount;
        accountant.transaction_id++;
    }

    public void write_transaction() throws IOException
    {
        String line = accountant.transaction_id + " " + this.type + " " + this.amount + " " + this.balance+"\n";
        File file = new File(account_number+".txt");
        FileWriter writer = new FileWriter(file,true);
        writer.write(line);
        writer.close();
    }
}
