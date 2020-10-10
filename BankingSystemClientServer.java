package bankingsystemclientserver;

import java.net.*;
import java.util.concurrent.*;


public class BankingSystemClientServer 
{    
    public static void main(String[] args) 
    {
    	ExecutorService ES=Executors.newFixedThreadPool(5);
        System.out.println("SERVER ");
        ServerSocket ss;
        try 
        { 
           MyDataBase.doConnection();
           MyDataBase.resetDataBase();
           ss=new ServerSocket(5816);
           Socket s;
           while(true)
           {
        	   System.out.println("SERVER IS WAITING FOR THE CONNECTION");
               s = ss.accept();
               System.out.println("connected with "+s);
               ES.execute(new ClientHandler(s));                        
           }
        }
        catch (Exception e) 
        {
        	e.printStackTrace();
        }
    }    
}
