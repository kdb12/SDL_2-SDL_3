
package bankingsystemclientserver;

import java.io.*;
import java.net.*;
import java.util.HashMap;
import java.util.Vector;

public class ClientHandler extends Thread
{

    Socket s;
    ObjectInputStream cin;
    ObjectOutputStream cout;
    DataOutputStream dout;
    DataInputStream din;
    databaseInterface databaseListener;
	
    static Vector<Bank> India=new Vector<>(10);
    static HashMap<Integer,String> IP=new HashMap<>(10);
    static HashMap<Integer,String> IN=new HashMap<>(10);    
    
    @Override
    public void run() 
    {
    	System.out.println(this.getName());
        try 
        {
           	boolean end=false;
        	while(!end)
        	{

                int choicer=0;
                choicer=din.readInt();
        		if(choicer==1)
        		{
        			System.out.println("in addition of bank");
                    Bank B=(Bank) cin.readObject();                   
                    if(IN.containsKey(B.id))
                    {
                        System.out.println("DUPLICATE BANK ID");
                    }
                    else if(IN.containsValue(B.Name))
                    {
                        System.out.println("DUPLICATE BANK NAME");
                    }
                    else
                    {
                        India.add(B);
                        B.setComparator();
                        IP.put(B.id,B.Password);
                        IN.put(B.id,B.Name);
                        System.out.println("bank added");
                    }
        		}
        		else if(choicer==2)
        		{
        			System.out.println("in admin login");
        			int bid=din.readInt();
        			String bpass=din.readUTF();
        			int temp=0;
        			System.out.println("id  "+bid+"   pass   "+bpass);
        			
                    if(IP.containsKey(bid)&&IP.containsValue(bpass))
                    {
                        if(!(IP.get(bid).equals(bpass)))
                        {
                           temp=0;
                        }
                        else
                        {
                        	temp=1;
                        }
                    }
        			
        			dout.writeInt(temp);
        			
					if(temp==0)
        			{
        				System.out.println("wrong credentials");
        			}
        			else
        			{
        				int MyIndex=0;
        				boolean kr = true;
                    	Bank Bk=null;
                        for(int i=0;i<India.size()&&kr;i++)
                        {
                            Bank B=India.get(i);
                            if(B.id==bid)
                            {
                            	//System.out.println("bank found");
                                kr=false;
                                Bk=B;
                                MyIndex=i;
                            }
                        }
        				boolean end2=false;
        				while(!end2)
        				{

                            
                            int choice2=0;
                          
                            choice2=din.readInt();
                            
                            if(choice2==1)
                            {
                            	cout.reset();
                            	System.out.println("creating users");
//                            	boolean kr = true;
//                            	Bank Bk=null;
//                                for(int i=0;i<India.size()&&kr;i++)
//                                {
//                                    Bank B=India.get(i);
//                                    if(B.id==bid)
//                                    {
//                                    	//System.out.println("bank found");
//                                        kr=false;
//                                        Bk=B;
//                                    }
//                                }
                            	cout.writeObject(Bk);
                            	
                            	User u=(User)cin.readObject();
                            	Bk.accounts.add(u);
                            	Bk.upiHolders.put(u.upiId, u.password);
                            	
                            	Bk.addInitialDeposit(u.balance,u.accNo);
                            }
                            else if(choice2==2)
                            {
                            	cout.reset();                            	
                            	System.out.println("showing users");                            	
//                            	boolean kr = true;
//                            	Bank Bk=null;
//                                for(int i=0;i<India.size()&&kr;i++)
//                                {
//                                    Bank B=India.get(i);
//                                    if(B.id==bid)
//                                    {
//                                    	//System.out.println("bank found");
//                                        kr=false;
//                                        Bk=B;
//                                    }
//                                }
                            	cout.writeObject(Bk);
                            }
                            else if(choice2==3)
                            {
                            	cout.reset();
                            	System.out.println("transactions");
//                              	boolean kr = true;
//                            	Bank Bk=null;
//                                for(int i=0;i<India.size()&&kr;i++)
//                                {
//                                    Bank B=India.get(i);
//                                    B.showTrans();
//                                    if(B.id==bid)
//                                    {
//                                    	System.out.println("bank found");
//                                        kr=false;
//                                        Bk=B;
//                                        
//                                    }
//                                }
                                
                            	cout.writeObject(Bk);                  	
                            }
                            else if(choice2==4)
                            {
                            	cout.reset();
                            	System.out.println("requests");
                            	System.out.println(Bk.requests);
                
                            	cout.writeObject(Bk); 
                            	Bk=(Bank) cin.readObject();
                            	
                            	India.remove(MyIndex);
                            	
                            	India.add(MyIndex, Bk);
                            	
                            	
                            }
                            else if(choice2==5)
                            {
                            	cout.reset();
                            	System.out.println("searching users");
//                              	boolean kr = true;
//                            	    Bank Bk=null;
//                                for(int i=0;i<India.size()&&kr;i++)
//                                {
//                                    Bank B=India.get(i);
//                                    if(B.id==bid)
//                                    {
//                                    	//System.out.println("bank found");
//                                        kr=false;
//                                        Bk=B;
//                                    }
//                                }
                            	cout.writeObject(Bk);  
                            }
                            else if(choice2==6)
                            {
                            	System.out.println("logging out");
                            	end2=true;
                            }
                            else
                            {
                            	System.out.println("invalid");
                            }
        				}
        			}
        			
        		}
        		
        		else if(choicer==3)
        		{    			

        			
        			int temp=0;
        			String bName,Id,Pass;
        			bName=din.readUTF();
        			Id=din.readUTF();
        			Pass=din.readUTF();
        			boolean checker=ClientHandler.CheckLogin(bName, Id, Pass);
        			
        			if(!checker)
        			{
        				temp=0;
        				System.out.println("wrong credentials");
        				dout.writeInt(temp);
        			}
        			else
        			{
        				temp=1;
        				dout.writeInt(temp);
        				boolean end2=false;
                        Bank Bk=null;
                        User U=null;
                        boolean kr=true;
                        for(int i=0;i<India.size()&&kr;i++)
                        {
                            Bank B=India.get(i);
                            if(B.Name.equals(bName))
                            {
                                kr=false;
                                Bk=B;
                            }
                        }
                        boolean lend = false;
                        for(int i=0;i<Bk.accounts.size()&&!lend;i++)
                        {
                            if(Bk.accounts.get(i).upiId.equals(Id))
                            {
                                lend=true;
                                U = Bk.accounts.get(i);
                            }
                        }
        				while(!end2)
        				{

                            int choice3=0;
                            System.out.println("reading choice 3");
                            choice3=din.readInt();
                            if(choice3==1)
                            {
                            	System.out.println("sending money");
                            	String rb=din.readUTF();
                            	String ra=din.readUTF();
                                Bank O=null;
                                User P=null;
                                if(IN.containsValue(rb))
                                {
                                    boolean fend=false;
                                    for(int i=0;i<India.size()&&!fend;i++)
                                    {
                                        if(India.get(i).Name.equals(rb))
                                        {
                                            O=India.get(i);
                                            fend = true;
                                        }
                                    }                                    
                                    for(int i=0;i<O.accounts.size()&&fend;i++)
                                    {
                                        if(O.accounts.get(i).accNo.equals(ra))
                                        {
                                            P=O.accounts.get(i);
                                            fend=false;
                                        }
                                    }
                                    if(!fend)
                                    {
                                    	dout.writeInt(1);
                                    	double mny=din.readDouble();
                                    	boolean sender=U.send(P, O, Bk, mny);
                                    	if(sender)
                                    	{
                                    		dout.writeInt(1);
                                    	}
                                    	else
                                    	{
                                    		dout.writeInt(0);
                                    	}
                                    }
                                    else
                                    {
                                    	dout.writeInt(0);
                                    }
                                }
                                else
                                {
                                	dout.writeInt(0);
                                }
                            	
                            }
                            else if(choice3==2)
                            {
                            	System.out.println("adding money");
                            	double mny=din.readDouble();
                            	U.deposit(Bk, mny);
                            }
                            else if(choice3==3)
                            {
                            	
                            	System.out.println("updation");
                            	int ch=din.readInt();
                            	if(ch==1)
                            	{
                                    double New = din.readDouble();
                                    Bk.addRequest("FROM UPI ID= "+U.upiId+" TO UPDATE MOBILE NUMBER TO "+New);
                                    databaseListener.addRequests("TO UPDATE MOBILE NUMBER", U.name, New, U.upiId, U.bankName,U.balance);
                            	}
                            	else if(ch==2)
                            	{
                            		String New=din.readUTF();
                                    Bk.addRequest("FROM UPI ID= "+U.upiId+" TO UPDATE NAME TO "+New);
                                    databaseListener.addRequests("TO UPDATE NAME", New, U.phoneNo, U.upiId, U.bankName,U.balance);
                            	}
                            	else if(ch==3)
                            	{
                            		double New = din.readDouble();
                                    String New2=din.readUTF();
                                    Bk.addRequest("FROM UPI ID= "+U.upiId+" TO UPDATE NAME AND MOBILE NUMBER TO "+New2+" AND "+New);
                                    databaseListener.addRequests("TO UPDATE NAME AND MOBILE NUMBER", U.name, U.phoneNo, U.upiId, U.bankName,U.balance);
                            	}
                            	else 
                            	{
                            		
                            	}                            		                           	
                            }
                            else if(choice3==4)
                            {
                            	cout.reset();
                            	System.out.println("transactions");
                                boolean pr=true;
                                for(int i=0;i<India.size()&&pr;i++)
                                {
                                    Bank B=India.get(i);
                                    if(B.Name.equals(bName))
                                    {
                                        pr=false;
                                        Bk=B;
                                    }
                                }
                                boolean pend = false;
                                for(int i=0;i<Bk.accounts.size()&&!pend;i++)
                                {
                                    if(Bk.accounts.get(i).upiId.equals(Id))
                                    {
                                        pend=true;
                                        U = Bk.accounts.get(i);
                                    }
                                }
                            	cout.writeObject(U);
                            }
                            else if(choice3==5)
                            {
                            	cout.reset();
                            	System.out.println("Account details");
                                boolean pr=true;
                                for(int i=0;i<India.size()&&pr;i++)
                                {
                                    Bank B=India.get(i);
                                    if(B.Name.equals(bName))
                                    {
                                        pr=false;
                                        Bk=B;
                                    }
                                }
                                boolean pend = false;
                                for(int i=0;i<Bk.accounts.size()&&!pend;i++)
                                {
                                    if(Bk.accounts.get(i).upiId.equals(Id))
                                    {
                                        pend=true;
                                        U = Bk.accounts.get(i);
                                    }
                                }
                            	cout.writeObject(U);
                            }
                            else if(choice3==6)
                            {
                            	System.out.println("withdraw of money");
                            	double mny=din.readDouble();
                            	boolean withdrawer=U.withdraw(Bk, mny);
                            	if(withdrawer)
                            	{
                            		dout.writeInt(1);
                            	}
                            	else
                            	{
                            		dout.writeInt(0);
                            	}
                            }
                            else if(choice3==7)
                            {
                            	cout.reset();
                            	System.out.println("notifications");
                            	cout.writeObject(U);
                            } 
                            else if(choice3==8)
                            {
                            	System.out.println("logging out");
                            	end2=true;
                            }
                            else
                            {
                            	System.out.println("invalid");
                            }
        				}
        			}       			
        			
        		}
        		else if(choicer==4)
        		{
        			System.out.println("connection closed with "+s);
        			cin.close();
        			din.close();
        			dout.close();
        			cout.close();
        			s.close();
        			end=true;
        		}
        		else
        		{
        			System.out.println("wrong choice");
        		}
        	}
        }
        catch (Exception e) 
        {
        	
        	System.out.println(e);
        }
    }

    public ClientHandler(Socket s) 
    {
        this.s = s;
        try 
        {
            cout=new ObjectOutputStream(s.getOutputStream());
            dout=new DataOutputStream(s.getOutputStream());
            din=new DataInputStream(s.getInputStream());
            cin=new ObjectInputStream(s.getInputStream());
            databaseListener=new MyDataBase();
        } 
        catch (Exception e) 
        {}
        System.out.println("ini done");     
    }

    private static boolean CheckLogin(String bName, String Id, String Pass) 
    {
        boolean checker = false;
        if(!(IN.containsValue(bName)))
        {
            System.out.println("BANK NAME IS WRONG");
            checker=false;
        }
        else
        {
            Bank Bk = null;
            boolean kr=true;
            for(int i=0;i<India.size()&&kr;i++)
            {
                Bank B=India.get(i);
                if(B.Name.equals(bName))
                {
                    kr=false;
                    Bk=B;
                }
            } 
            if(Bk.upiHolders.containsKey(Id)&& Bk.upiHolders.containsValue(Pass))
            {
                if(Bk.upiHolders.get(Id).equals(Pass))
                {
                    System.out.println("");
                    System.out.println("LOGIN SUCCESSFULL"); 
                    checker=true;
                }
            }                
        }
        return checker;
    }
    
}
