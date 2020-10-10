package bankingsystemclientserver;

import java.io.Serializable;
import java.util.*;
import java.text.*;

interface UserInterface
{
    String C="$  HAS BEEN CREDITED TO YOUR ACCOUNT ON ";
    String D="$  HAS BEEN DEBITED FROM YOUR ACCOUNT ON ";
    final double tCharges = 0.04;
    void deposit(Bank b,double money);
    boolean send(User e,Bank b,Bank s,double mny);
    boolean withdraw(Bank b,double mny);
}

public class User implements Serializable , UserInterface
{
    double phoneNo;
    String upiId,name,password,bankName,accNo;
    User()
    {
    	
    }
    @Override
	public String toString()
    {
		return "User [phoneNo=" + phoneNo + ", upiId=" + upiId + ", name=" + name + ", password=" + password
				+ ", bankName=" + bankName + ", accNo=" + accNo + ", balance=" + balance + ", transactions="
				+ transactions + ", notifications=" + notifications + "]";
	}

	double balance;
    LinkedList<String> transactions = new LinkedList<>();
    LinkedList<String> notifications =new LinkedList<>();
    
    void input(Bank b)
    {
        System.out.println("");
        Scanner sc=new Scanner(System.in);
        System.out.print("ENTER NAME: ");
        name=sc.nextLine();
        bankName=b.Name;
        System.out.print("ENTER UPIID: ");
        upiId=sc.nextLine();
        System.out.print("ENTER PASSWORD: ");
        password=sc.nextLine();
        System.out.print("ENTER CONTACT NUMBER: ");
        phoneNo=sc.nextDouble();
        sc.nextLine();
        System.out.print("ENTER ACCOUNT NUMBER: ");
        accNo=sc.nextLine();
        System.out.print("ENTER INITIAL DEPOSIT: ");
        balance=sc.nextDouble();
        addTransactions("INITIAL DEPOSIT :"+balance+" ON  ");
        b.addTrans(balance+"$ -INITIAL DEPOSIT OF ACCOUNT NUMBER ="+accNo);
        System.out.println("");
        
    }

    void showAccountHolders()
    {
        String format = "|%1$-15s|%2$-10s|%3$-10s|%4$-15s|%5$-15s|%6$-10s|%7$-10s|\n";
        System.out.format(format,name,upiId,password,phoneNo,accNo,balance,bankName);
    }

    @Override
    public void deposit(Bank b,double money) 
    {
    	databaseInterface database=new MyDataBase();
        System.out.println("in deposition of money ");
        System.out.println("");
        System.out.println("money = "+money);
        Scanner sc=new Scanner(System.in);
        balance+=money;
        this.addTransactions(money+C);
        b.addTrans(money+"$ ADDED IN ACCOUNT NUMBER ="+this.accNo);
//        database.addTransactions(this.accNo, this.accNo, this.bankName, this.bankName, money, "D");
//        database.updateBalance(this.accNo,this.balance, this.bankName);
        User u=this;
        new Thread(new Runnable() {

			@Override
			public void run() {
				// TODO Auto-generated method stub
				database.addTransactions(u.accNo, u.accNo, u.bankName, u.bankName, money, "D");
		        database.updateBalance(u.accNo,u.balance, u.bankName);
			}
        	
        }).start();
        System.out.println("");
    }

    @Override
    public boolean send(User e,Bank b,Bank s,double mny) 
    {
        System.out.println("");
        databaseInterface database=new MyDataBase();
        
        boolean sender=false;
        
        if(e.bankName.equals(this.bankName))
        {
            if(this.balance >= mny)
            {
                sender=true;
                balance-=mny;
                e.balance+=mny;
                e.addTransactions(mny+C);
                this.addTransactions(mny+D);
                s.addTrans(mny+"$ TRANSFERRED BY ACCOUNT NUMBER: "+this.accNo+" TO ACCOUNT NUMBER: "+e.accNo);
                database.addTransactions(this.accNo, e.accNo, this.bankName, e.bankName, mny, "T");
                database.updateBalance(this.accNo,this.balance, this.bankName);
                database.updateBalance(e.accNo, e.balance, e.bankName);
            }
            else
            {
                System.out.println("LOW BALANCE");
                sender=false;
            }
        }
        else
        {
            if(((this.balance)-(tCharges)*this.balance) >= mny)
            {
                sender=true;
                balance-=(mny+mny*tCharges);
                e.balance+=mny;
                e.addTransactions(mny+C);
                this.addTransactions(mny+mny*tCharges+D);
                s.addTrans(mny+"$ TRANSFERRED BY ACCOUNT NUMBER: "+this.accNo+" TO ACCOUNT NUMBER: "+e.accNo+" OF BANK "+e.bankName);
                b.addTrans(mny+"$ RECEIVED IN ACCOUNT NUMBER: "+e.accNo+" BY ACCOUNT NUMBER: "+this.accNo+" OF BANK "+this.bankName);
                database.addTransactions(this.accNo, e.accNo, this.bankName, e.bankName, mny, "T");
                database.updateBalance(this.accNo,this.balance, this.bankName);
                database.updateBalance(e.accNo, e.balance, e.bankName);
            }
            else
            {
                sender=false;
                System.out.println("LOW BALANCE");
                              
            }
        }
        System.out.println("");
        return sender;
    }

    @Override
    public boolean withdraw(Bank b,double mny) 
    {
        System.out.println("");
        Scanner sc=new Scanner(System.in);
        boolean withdrawer=false;
        databaseInterface database=new MyDataBase();
        if(mny <= balance)
        {
            balance-=mny;
            this.addTransactions(mny+D);
            b.addTrans(mny+"$ WITHDRAW FROM ACCOUNT NUMBER ="+this.accNo);
            withdrawer=true;
            database.addTransactions(this.accNo, this.accNo, this.bankName, this.bankName, mny, "W");
            database.updateBalance(this.accNo,this.balance, this.bankName);
            
//            new Thread(new Runnable() {
//
//				@Override
//				public void run() {
//					// TODO Auto-generated method stub
//					database.addTransactions(this.accNo, this.accNo, this.bankName, this.bankName, mny, "W");
//		            database.updateBalance(this.accNo,this.balance, this.bankName);
//				}
//            	
//            }).start();
        }
        else
        {
            System.out.println("LOW BALANCE!!! TRY AGAIN");
            
        }
        System.out.println("");
        return withdrawer;
    }

    private void addTransactions(String string) 
    {
        System.out.println("");
        String d = getCurrentDate();
        String t = getCurrentTime();
        transactions.addFirst(string+d+" At "+t); 
    }

    private String getCurrentDate() 
    {
        Date date =Calendar.getInstance().getTime();
        DateFormat dateFormat = new SimpleDateFormat("E, dd MMMM yyyy");
        String strDate = dateFormat.format(date);
        return strDate;    
    }

    private String getCurrentTime() 
    {
        Date date =Calendar.getInstance().getTime();
        DateFormat dateFormat = new SimpleDateFormat("HH:mm:ss");
        String strDate = dateFormat.format(date);
        return strDate;      
    }
    
    void showTransactions() 
    {
        System.out.println("");
        for(String p:transactions)
        {
            System.out.println(p);
        }
        System.out.println("");
    }
    
    void addNotifications(String p)
    {
        notifications.addFirst(p);
    }

    void showNotifications() 
    {
        System.out.println("");
        if(notifications.isEmpty())
        {
            System.out.println("NO NEW NOTIFICATIONS");
        }
        for(String k:notifications)
        {
            System.out.println(k);
        }
        System.out.println("");
    }
}
