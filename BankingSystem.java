package bankingsystemclientserver;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.*;

public class BankingSystem
{

   
   
    public static void main(String[] args) throws Exception 
    {
    	MyDataBase.doConnection();
    	databaseInterface databaseListener = new MyDataBase();
    	Socket s=new Socket("localhost",5816);
    	DataOutputStream dout=new DataOutputStream(s.getOutputStream());
    	ObjectOutputStream cout=new ObjectOutputStream(s.getOutputStream());
    	DataInputStream din=new DataInputStream(s.getInputStream());
    	ObjectInputStream cin=new ObjectInputStream(s.getInputStream());
    	Scanner sc=new Scanner(System.in);
    	boolean end=false;
    	while(!end)
    	{
            System.out.println("1.ADD BANK");
            System.out.println("2.ADMIN LOGIN");
            System.out.println("3.USER LOGIN");
            System.out.println("4.EXIT");
            System.out.print("ENTER YOUR CHOICE: ");  
            int choicer=0;
            choicer=sc.nextInt();
            sc.nextLine();
    		if(choicer==1)
    		{
                    dout.writeInt(1);
                    String N,A,P;
                    int i;
                    System.out.print("ENTER NAME: ");                   
                    N=sc.nextLine();
                    System.out.print("ENTER ADDRESS: ");                    
                    A=sc.nextLine();
                    System.out.print("ENTER PASSWORD: ");                    
                    P=sc.nextLine();
                    System.out.print("ENTER ID: ");                    
                    i=sc.nextInt();
                    sc.nextLine();
                    System.out.println("");
                    Bank B=new Bank(N,A,P,i);
                    cout.writeObject(B);
                   // databaseListener.addBank(N, i, A, P);
                    new Thread(new Runnable()
                    {

								@Override
								public void run() {
									// TODO Auto-generated method stub
									 databaseListener.addBank(N, i, A, P);
								}
                    	
                    }).start();
    		}
    		else if(choicer==2)
    		{
    			dout.writeInt(2);
    			String pass;
    			int id;
    			System.out.println("ENTER ID");
    			id=sc.nextInt();
    			sc.nextLine();
    			System.out.println("ENTER PASSWORD");
    			pass=sc.nextLine();
    			
    			dout.writeInt(id);
    			dout.writeUTF(pass);
    			
    			int temp=din.readInt();
    			if(temp==0)
    			{
    				System.out.println("wrong credentials");
    			}
    			else
    			{
    				boolean end2=false;
    				while(!end2)
    				{
                        System.out.println("1.CREATE USERS");
                        System.out.println("2.SHOW ACCOUNT HOLDERS");
                        System.out.println("3.TRANSACTIONS");
                        System.out.println("4.REQUESTS");
                        System.out.println("5.SEARCH USER");
                        System.out.println("6.LOG OUT");
                        System.out.print("ENTER YOUR CHOICE: ");
                        
                        int choice2=0;
                        choice2=sc.nextInt();
                        sc.nextLine();
                        if(choice2==1)
                        {
                        	dout.writeInt(1);
                        	System.out.println("creating users");
                        	Bank Bk=(Bank)cin.readObject();
                        	User u=new User();
                        	u.input(Bk);
                        	cout.writeObject(u);
                        	System.out.println("user created");
                        	//databaseListener.addUser(u.name, u.upiId, u.password, u.phoneNo, u.accNo, u.balance, u.bankName);
                        	
                        	new Thread(new Runnable() {

								@Override
								public void run() {
									// TODO Auto-generated method stub
									databaseListener.addUser(u.name, u.upiId, u.password, u.phoneNo, u.accNo, u.balance, u.bankName);
								}
                        		
                        	}).start();
                        	
                        }
                        else if(choice2==2)
                        {
                        	dout.writeInt(2);
                        	System.out.println("showing users");
                            System.out.println("ACCOUNT HOLDERS IN THE BANK  ARE AS FOLLOWS:\n");                                  
                            String format = "|%1$-15s|%2$-10s|%3$-10s|%4$-15s|%5$-15s|%6$-10s|%7$-10s|\n";
                            System.out.format(format,"NAME","UPI_ID","PASSWORD","MOBILE_NUMBER","ACCOUNT_NUMBER","BALANCE","BANK_NAME");                        	
                        	
                        	Bank Bk=(Bank)cin.readObject();
                        	for(int i=0;i<Bk.accounts.size();i++)
                        	{
                        		Bk.accounts.get(i).showAccountHolders();
                        	}
                        }
                        else if(choice2==3)
                        {
                        	dout.writeInt(3);
                        	System.out.println("transactions");
                        	Bank Bk=(Bank)cin.readObject();
                        	System.out.println("");
                        	Bk.showTrans();
                        	System.out.println("");
                        }
                        else if(choice2==4)
                        {
                        	dout.writeInt(4);
                        	System.out.println("requests");
                        	Bank Bk=(Bank)cin.readObject();
                        	Bk.showRequest();
                        	cout.reset();
                        	cout.writeObject(Bk);
                        	//databaseListener.removeRequests(Bk.Name);
                        	new Thread(new Runnable() {

								@Override
								public void run() {
									// TODO Auto-generated method stub
									databaseListener.removeRequests(Bk.Name);
								}
                        		
                        	}).start();
                        }
                        else if(choice2==5)
                        {
                        	dout.writeInt(5);
                        	System.out.println("searching users");
                        	Bank Bk=(Bank)cin.readObject();
                        	Bk.searchUser();
                        }
                        else if(choice2==6)
                        {
                        	dout.writeInt(6);
                        	System.out.println("logging out");
                        	end2=true;
                        }
                        else
                        {
                        	dout.writeInt(10);
                        	System.out.println("invalid");
                        }
    				}
    			}
    			
    			
    		}
    		else if(choicer==3)
    		{    			
    			dout.writeInt(3);
    			String bankname;
    			String pass;
    			String id;
    			System.out.println("ENTER BANK NAME");
    			bankname=sc.nextLine();
    			System.out.println("ENTER ID");
    			id=sc.nextLine();
    			System.out.println("ENTER PASSWORD");
    			pass=sc.nextLine();	
    			dout.writeUTF(bankname); 
    			dout.writeUTF(id);
    			dout.writeUTF(pass);
    			
    			int temp=din.readInt();
    			if(temp==0)
    			{
    				System.out.println("wrong credentials");
    			}
    			else
    			{
    				boolean end2=false;
    				
    				while(!end2)
    				{
                        System.out.println("1.SEND MONEY");
                        System.out.println("2.ADD MONEY");
                        System.out.println("3.UPDATE INFORMATION");
                        System.out.println("4.SHOW TRANSACTIONS");
                        System.out.println("5.ACCOUNT DETAILS");
                        System.out.println("6.WITHDRAW MONEY");
                        System.out.println("7.NOTIFICATIONS");
                        System.out.println("8.LOG OUT");
                        System.out.print("ENTER YOUR CHOICE: ");
                        int choice3=0;
                        choice3=sc.nextInt();
                        sc.nextLine();
                        
                        if(choice3==1)
                        {
                        	dout.writeInt(1);
                        	System.out.println("sending money");
                            String rb,ra;
                            System.out.println("ENTER RECEIVERS BANK NAME");
                            rb=sc.nextLine();
                            System.out.println("ENTER RECEIVERS ACCOUNT NUMBER");
                            ra=sc.nextLine();
                            dout.writeUTF(rb);
                            dout.writeUTF(ra);
                            int t=0;
                            t=din.readInt();
							if(t==1)
                            {
                                double mny;
                                System.out.println("enter amount you want to send ");
                                mny=sc.nextDouble();
                                sc.nextLine();
                                dout.writeDouble(mny);
                                
                                int temp2=0;
                                temp2=din.readInt();
								if(temp2==1)
                                {
                                    System.out.println("money sent successfully");
                                }
                                else
                                {
                                    System.out.println("money not sent successfully");
                                }
                            }
                            else
                            {
                                System.out.println("wrong!!!"); 
                            }
                        }
                        else if(choice3==2)
                        {
                        	dout.writeInt(2);
                        	System.out.println("adding money");
                            double mny;
                            System.out.println("enter money you want to add");
                            mny=sc.nextDouble();
                            sc.nextLine();
                            dout.writeDouble(mny);
                            System.out.println("money added");
                        }
                        else if(choice3==3)
                        {
                        	dout.writeInt(3);
                        	System.out.println("updation");
                            System.out.println("1.to update mobile number");
                            System.out.println("2.to update name");
                            System.out.println("3.to update both");
                            System.out.println("4.to back");
                            System.out.println("enter choice");
                            int ch=sc.nextInt();
                            sc.nextLine();
                            if(ch==1)
                            {
                                double New;
                                System.out.print("ENTER UPDATED MOBILE NUMBER: ");
                                New=sc.nextDouble();
                                sc.nextLine();
                                dout.writeInt(1);
                                dout.writeDouble(New);
                            }
                            else if(ch==2)
                            {
                                String New;
                                System.out.print("ENTER Name: ");
                                New=sc.nextLine();
                                dout.writeInt(2);
                                dout.writeUTF(New);                                    
                            }
                            else if(ch==3)
                            {
                                double New;
                                System.out.print("ENTER UPDATED MOBILE NUMBER: ");
                                New=sc.nextDouble();                                                     
                                sc.nextLine();
                                String New2;
                                System.out.print("ENTER Name: ");
                                New2=sc.nextLine(); 
                                dout.writeInt(3);
                                dout.writeDouble(New);
                                dout.writeUTF(New2);
                            }
                            else
                            {
                                dout.writeInt(4);
                                System.out.println("invalid");
                            }
                        }
                        else if(choice3==4)
                        {
                        	dout.writeInt(4);
                        	System.out.println("transactions");
                        	User u=(User)cin.readObject();
                        	u.showTransactions();
                        }
                        else if(choice3==5)
                        {
                        	dout.writeInt(5);
                        	System.out.println("details of account");
                            String format = "|%1$-15s|%2$-10s|%3$-10s|%4$-15s|%5$-15s|%6$-10s|%7$-10s|\n";
                            System.out.format(format,"NAME","UPI_ID","PASSWORD","MOBILE_NUMBER","ACCOUNT_NUMBER","BALANCE","BANK_NAME");
                        	
                        	User u=(User)cin.readObject();
                        	u.showAccountHolders();
                        }
                        else if(choice3==6)
                        {
                        	dout.writeInt(6);
                        	System.out.println("withdraw of money");
                        	double mny;
                            System.out.println("enter money you want to withdraw");
                            mny=sc.nextDouble();
                            sc.nextLine();
                            dout.writeDouble(mny); 
                            int tempo;
                            tempo=din.readInt();
                            if(tempo==1)
                            {
                                System.out.println("money withdraw succcess");
                            }
                            else
                            {
                                System.out.println("money withdraw failed");
                            }
                        }
                        else if(choice3==7)
                        {
                        	dout.writeInt(7);
                        	System.out.println("notifications");
                        	User u=(User)cin.readObject();
                        	u.showNotifications();
                        } 
                        else if(choice3==8)
                        {
                        	dout.writeInt(8);
                        	System.out.println("logging out");
                        	end2=true;
                        }
                        else
                        {
                        	dout.writeInt(100);
                        	System.out.println("invalid");
                        }
    				}
    			}
    			
    			
    			
    		}
    		else if(choicer==4)
    		{
    			dout.writeInt(4);
    			cin.close();
    			din.close();
    			dout.close();
    			cout.close();
    			s.close();
    			end=true;
    		}
    		else
    		{
    			dout.writeInt(100);
    			System.out.println("wrong choice");
    		}
    	}
    			
    }
}
