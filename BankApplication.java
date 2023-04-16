import java.io.*;
import java.util.*;

public class BankApplication {

    public static String db_path = "db.txt";
    static List<Accountant> accounts;

    public static void write_file(File file) throws IOException
    {
        String to_write = "";
        FileWriter writer = new FileWriter(file);
        for(Accountant a:accounts)
        {
            to_write+=Accountant.addLine(a) + "\n";
        }
        writer.write(to_write);
        writer.close();
    }

    public static Accountant withdraw(Accountant accountant,Scanner scan) throws IOException
    {
        System.out.print("Enter the amount to withdraw : ");
        int withdraw_amount = scan.nextInt();
        scan.nextLine();
        int diff = accountant.balance - withdraw_amount;
        // System.out.println(diff);
        if(diff<=Accountant.MIN_BALANCE)
        {
            System.out.println("Minimum amount is 10,000");
        }
        else 
        {
            accountant.balance-=withdraw_amount;
            System.out.println("Amount withdrawn");
        }
        
        Transaction transaction = new Transaction(accountant, Type.WITHDRAW, withdraw_amount);
        transaction.write_transaction();

        return accountant;
    }

    public static Accountant deposit(Accountant accountant,Scanner scan) throws IOException
    {
        System.out.print("Enter the amount to deposit : ");
        int deposit = scan.nextInt();
        scan.nextLine();
        accountant.balance+=deposit;
        Transaction transaction = new Transaction(accountant, Type.DEPOSIT, deposit);
        transaction.write_transaction();
        return accountant;
    }

    public static Accountant withdraw(Accountant accountant,int withdraw_amount) throws IOException
    {
        int diff = accountant.balance - withdraw_amount;
        // System.out.println(diff);
        if(diff<=Accountant.MIN_BALANCE)
        {
            System.out.println("Minimum amount is 10,000");
        }
        else 
        {
            accountant.balance-=withdraw_amount;
        }
        return accountant;
    }

    public static Accountant deposit(Accountant accountant,int deposit) throws IOException
    {
        accountant.balance+=deposit;
        return accountant;
    }

    public static Accountant find_account(int account_number)
    {
        Accountant accountant = null;

        for(Accountant a:accounts)
        {
            //System.out.println(a.account_number);
            if(a.account_number==account_number)
            {
                accountant = a;
                break;
            }
        }

        return accountant;
    }

    public static void main(String[] args) throws IOException {

        FileWriter writer;
        
        File file = new File(db_path);
        Scanner scan = new Scanner(file);

        accounts = new ArrayList<>();

        while(scan.hasNextLine())
        {
            String line = scan.nextLine();
           // System.out.println(line);
            String[] temp = line.split(" ");
            Accountant accountant = new Accountant(temp[2],temp[4],Integer.parseInt(temp[3]),Integer.parseInt(temp[0]),Integer.parseInt(temp[1]));
            accounts.add(accountant);
        }

        scan.close();

        scan = new Scanner(System.in);
        int number = 1;
        while(number>0 && number<3)
        {
            System.out.println("1.Add Account.\n2.Use ATM\n\nEnter the input : ");
            number = scan.nextInt();
            scan.nextLine();

            switch(number)
            {
                case 1:
                    System.out.println("Enter your name : ");
                    String name = scan.nextLine();
        
                    //System.out.println(name);
        
                    System.out.println("Enter your password : ");
                    String password = scan.nextLine();
        
                    System.out.println("Re-enter your password: ");
                    String retype = scan.nextLine();
                    
                    if(!password.equals(retype))
                    {
                        System.out.println("Passwords doesn't match");
                        continue;
                    }
        
                //  System.out.println("p"+ Accountant.encryptPassword(password));
        
                    Accountant accountant = new Accountant(name, Accountant.encryptPassword(password),accounts.size());
                    accounts.add(accountant);

                    accountant.first_transaction();
        
                    String add_line = Accountant.addLine(accountant);
                    add_line+="\n";
                    writer = new FileWriter(file,true);
                    writer.append(add_line);
                    writer.flush();
                    writer.close();
        
                    System.out.println("Account added");
                    break;


                case 2:
                    System.out.println("Enter the Account Number : ");
                    int account_number = scan.nextInt();
                    scan.nextLine();
                    System.out.println("Enter the password : ");
                    password = Accountant.encryptPassword(scan.nextLine());

                    accountant = find_account(account_number);

                    if(accountant==null)
                    {
                        System.out.println("Account not found");
                        break;
                    }
                    else if(!password.equals(accountant.password))
                    {
                        System.out.println("Incorrect password");
                        break;
                    }
                    else 
                    {
                        System.out.println("Aunthentication successful");
                    }

                    while(number>0 && number<5)
                    {
                        System.out.println("1.Cash deposit\n2.withdraw\n3.Money Transfer\n4.see transaction");
                        number = scan.nextInt();
                        scan.nextLine();
                        switch(number)
                        {
                            case 2:
                                accountant = withdraw(accountant, scan);
                                break;

                            case 1:
                                accountant =  deposit(accountant, scan);
                                break;

                            case 3:
                                System.out.println("Enter the account number to transfer : ");
                                int account_number_to_transfer = scan.nextInt();
                                scan.nextLine();
                                Accountant to_transfer = find_account(account_number_to_transfer);
                                System.out.println("Enter the amount to transfer : ");
                                if(to_transfer==null)
                                {
                                    System.out.print("Account not found");
                                    break;
                                }
                                int amount_to_transfer = scan.nextInt();
                                scan.nextLine();
                                if(accountant.balance-Accountant.MIN_BALANCE<amount_to_transfer)
                                {
                                    System.out.println("Insufficient funds");
                                }
                                else 
                                {
                                    withdraw(accountant, amount_to_transfer);
                                    Transaction transaction = new Transaction(accountant, Type.TRANSFER, amount_to_transfer);
                                    transaction.write_transaction();

                                    deposit(to_transfer, amount_to_transfer);
                                    transaction = new Transaction(to_transfer, Type.TRANSFER, amount_to_transfer);
                                    transaction.write_transaction();
                                }
                                break;


                            case 4:
                                System.out.println("ID  TYPE    AMOUNT  BALANCE");
                                file = new File(accountant.filename);
                                FileReader reader = new FileReader(file);
                                Scanner scanner = new Scanner(file);
                                while(scanner.hasNext())
                                {
                                    System.out.println(scanner.nextLine());
                                }
                                reader.close();
                                scanner.close();
                                break;
                        }

                        write_file(file);
                }


                default:
                    scan.close();
                    System.exit(0);
            }
        }
    }
}