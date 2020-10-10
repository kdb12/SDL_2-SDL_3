package bankingsystemclientserver;

import java.io.Serializable;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;

public class Bank implements Serializable
{   
    String Name,Address,Password;
    int id;
    ArrayList<User> accounts = new ArrayList<>(50);
    Hashtable<String,String> upiHolders = new Hashtable<>(50);//upiid,pass
    LinkedList<String> transaction = new LinkedList<String> ();
    PriorityQueue<String> requests;
    
    Bank(String Name,String Address,String Password,int id)
    {
        this.Name=Name;
        this.Address=Address;
        this.Password=Password;
        this.id=id;
    }
    
    void setComparator()
    {
        requests=new PriorityQueue<> (new MyComparator(this)); 
    }
    
    Bank()
    {
        System.out.println("default");
    }


    void addTrans(String M)
    {
    	
        String D,T;
        D=this.getCurrentDate();
        T=this.getCurrentTime();
        this.transaction.add(M+" ON "+D+" AT "+T);
    }
    
    void showTrans()
    {
    	
        for (String k:transaction)
        {
        	
            System.out.println(k);
        }
    	
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
    
    void addRequest(String p)
    {
        requests.add(p);
    }
    
    void showRequest()
    {
        System.out.println("\n");
        while(!requests.isEmpty())
        {
            String p=requests.peek();
            User ra=null;
            System.out.println(requests.poll());
            System.out.println("PRESS 1 TO ACCEPT OR 0 TO PROCEED TO THE NEXT REQUEST");
            int k;
            Scanner sc=new Scanner(System.in);
            k=sc.nextInt();
            String q=p.substring(13,p.indexOf("TO")-1);
            for(User u:accounts)
            {
               if(q.equals(u.upiId))
               {
            	   
                   ra=u;
                   break;
               }
            }
            if(k==1)
            {
                ra.addNotifications("REQUEST HAS BEEN VERIFIED");
                if(p.contains("MOBILE")&&p.contains("NAME"))
                {
                    String r=p.substring(p.indexOf("TO",p.indexOf("TO")+1)+3);
                    String r1=r.substring(0,r.indexOf("AND")-1);
                    String r2=r.substring(r.indexOf("AND")+4);
                    Double pN=Double.parseDouble(r2);
                    ra.phoneNo=pN;
                    ra.name=r1;
                }
                else if(p.contains("MOBILE"))
                {
                    String r=p.substring(p.indexOf("TO",p.indexOf("TO")+1)+3);
                    Double pN=Double.parseDouble(r);
                    ra.phoneNo=pN;
                }
                else
                {
                    String r=p.substring(p.indexOf("TO",p.indexOf("TO")+1)+3);
                    ra.name=r;
                }
                MyDataBase.addNotifications("REQUEST HAS BEEN VERIFIED", ra.upiId, this.Name);
                MyDataBase.updatePhoneAndName(ra);
            }
            else
            {
                ra.addNotifications("REQUEST HAS BEEN REJECTED");
                MyDataBase.addNotifications("REQUEST HAS BEEN REJECTED", ra.upiId, this.Name);
            }
            
            
        }
        System.out.println("NO REQUEST IS PRESENT");
        
        System.out.println("");
    }

    void searchUser() 
    {
        System.out.println("");
        Scanner sc=new Scanner(System.in);
        boolean end=false;
        while(!end)
        {
            System.out.println("1.SEARCH BY NAME");
            System.out.println("2.SEARCH BY MOBILE_NUMBER");
            System.out.println("3.EXIT");
            System.out.print("ENTER YOUR CHOICE: ");
            int ch=sc.nextInt();
            sc.nextLine();
            switch (ch) 
            {
                case 1:
                {
                    System.out.print("ENTER NAME: ");
                    String p=sc.nextLine();
                    this.searchResult(p);
                }
                    break;
                case 2:
                {
                    System.out.print("ENTER MOBILE_NUMBER: ");
                    Double p=sc.nextDouble();
                    this.searchResult(p);
                }
                    break;
                case 3:
                {
                    end = true;
                }
                    break;
                default:
                {
                    System.out.println("WRONG CHOICE");
                }
                    break;
            }
        }
        System.out.println("");
    }

    private<T> void searchResult(T key) 
    {
        String format = "|%1$-15s|%2$-10s|%3$-10s|%4$-15s|%5$-15s|%6$-10s|%7$-10s|\n";                                   
        boolean found=false;
        if(key instanceof Double)
        {
            for(User U:this.accounts)
            {
                if(U.phoneNo == (Double) key)
                {
                    System.out.format(format,"NAME","UPI_ID","PASSWORD","MOBILE_NUMBER","ACCOUNT_NUMBER","BALANCE","BANK_NAME");
                    found=true;
                    U.showAccountHolders();
                    System.out.println("");
                }
            }
        }
        else if(key instanceof String)
        {
            for(User U:this.accounts)
            {
                if(U.name.equalsIgnoreCase(String.valueOf(key)))
                {
                    System.out.format(format,"NAME","UPI_ID","PASSWORD","MOBILE_NUMBER","ACCOUNT_NUMBER","BALANCE","BANK_NAME");
                    found=true;
                    U.showAccountHolders();
                    System.out.println("");
                }
            }            
        }
        
        if(!found)
        {
            System.out.println("THERE IS NO SUCH USER IN THE BANK");
        }
    }

	public void addInitialDeposit(double mny, String accNo)
	{
		 addTrans(mny+"$ -INITIAL DEPOSIT OF ACCOUNT NUMBER ="+accNo);
	}
}

