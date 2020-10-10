package bankingsystemclientserver;


import java.sql.*;

interface databaseInterface
{
	void addBank(String bankName,int bankId,String bankAddress,String bankPassword);
	void addUser(String name,String id,String password,double contact,String acc,double mny,String bankName);
	void addTransactions(String string, String string0, String BN, String BN0, double mny,String Type);
	void addRequests(String req,String cname,double contact,String id,String Myb,double cbalance);
	
	void removeRequests(String BankName);
	void updateBalance(String accno, double balance, String BN);
	
	
}

public class MyDataBase implements databaseInterface
{
	static Connection conn=null;
	
	
	public static Connection getConn() 
	{
		return conn;
	}
	
	
	public static void setConn(Connection conn) 
	{
		MyDataBase.conn = conn;
	}
	
	
	static void doConnection()
	{
		System.out.println("");
		
		try 
		{
		Class.forName("com.mysql.jdbc.Driver");
		Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/bankingsystem", "root", "Krishna@sql");
		MyDataBase.setConn(con);
		}
		catch(Exception e)
		{
			System.out.println(e);
		}
		
		System.out.println("");
	}
	
	
	public void addBank(String bankName,int bankId,String bankAddress,String bankPassword)
	{
		System.out.println("");
		
		try
		{
			Connection con = MyDataBase.getConn();
			PreparedStatement ps=con.prepareStatement("insert into banks values(?,?,?,?)");
			ps.setString(1, bankName);
			ps.setInt(2, bankId);
			ps.setString(3, bankAddress);
			ps.setString(4, bankPassword);
			ps.executeUpdate();
		}
		catch(Exception e)
		{
			System.out.println(e);
			return;
		}
		
		System.out.println("");
	}
	
	public void addUser(String name,String id,String password,double contact,String acc,double mny,String bankName)
	{
		System.out.println("");
		
		try
		{
			Connection con = MyDataBase.getConn();
            PreparedStatement ps;           
            ps = con.prepareStatement("INSERT INTO accountholders VALUES(?,?,?,?,?,?,?)");            
            ps.setString(1, name);            
            ps.setString(2, id);            
            ps.setString(7, password);            
            ps.setDouble(4, contact);            
            ps.setString(3, acc);            
            ps.setDouble(5, mny);          
            ps.setString(6, bankName);                    
            try{ps.executeUpdate();}catch(Exception e){System.out.println("user not added");}            
            ps=con.prepareStatement("INSERT INTO transactions VALUES(?,?,?,?,now(),?,'ID')");
            ps.setString(1, acc);
            ps.setString(2, acc);
            ps.setString(3, bankName);
            ps.setString(4, bankName);
            ps.setDouble(5, mny);
            ps.executeUpdate();
		}
		catch(Exception e)
		{
			System.out.println(e);
			return;
		}	
		
		System.out.println("");
	}
	
	public void addTransactions(String string, String string0, String BN, String BN0, double mny,String Type)
	{
		System.out.println("");
		
		try
		{
			Connection con = MyDataBase.getConn();
            PreparedStatement ps=con.prepareStatement("insert into transactions values(?,?,?,?,now(),?,?)");
            ps.setString(1, string);
            ps.setString(2, string0);
            ps.setString(3, BN);
            ps.setString(4, BN0);
            ps.setDouble(5, mny);
            ps.setString(6, Type);
            ps.executeUpdate();
		}
		catch(Exception e)
		{
			System.out.println(e);
		}
		
		System.out.println("");
	}
	
	public void addRequests(String req,String cname,double contact,String id,String Myb,double cbalance)
	{
		System.out.println("");
		
		try
		{
			Connection con = MyDataBase.getConn();
            PreparedStatement pst=con.prepareStatement("insert into requests values(?,?,?,?,?,?)");
            pst.setString(1, "TO CHANGE PHONE NUMBER");
            pst.setString(2, cname);
            pst.setDouble(3, contact);
            pst.setString(4, id);
            pst.setString(5, Myb);
            pst.setDouble(6, cbalance);
            pst.executeUpdate();
		}
		catch(Exception e)
		{
			System.out.println(e);
		}		
		
		System.out.println("");
	}
	
	public static void addNotifications(String notify,String id,String bankname)
	{
		System.out.println("");
		
		try
		{
			Connection con = MyDataBase.getConn();
			PreparedStatement ps=con.prepareStatement("insert into notifications values(?,?,?)");
			ps.setString(1, notify);
			ps.setString(2, id);
			ps.setString(3, bankname);
			ps.executeUpdate();
		}
		catch(Exception e)
		{
			System.out.println(e);
		}		
		
		System.out.println("");
	}
	
	public void removeRequests(String BankName) 
	{
		System.out.println("");
		
		try
		{
			Connection con = MyDataBase.getConn();
			con.createStatement().executeUpdate("delete from requests where bname='"+BankName+"'");
		}
		catch(Exception e)
		{
			System.out.println(e);
		}		
		
		System.out.println("");		
	}
	
    public void updateBalance(String accno, double balance, String BN) 
    {
        try 
        {
            Connection con = MyDataBase.getConn();
			PreparedStatement ps=con.prepareStatement("update accountholders set ubalance=? where uaccno=? and bname=?");
            ps.setDouble(1, balance);
            ps.setString(2, accno);
            ps.setString(3, BN);
            ps.executeUpdate();
        } 
        catch (Exception e) 
        {
        	System.out.println(e);
        }
    }
    
    public static void resetDataBase()
    {
    	try
    	{
    		Connection con = MyDataBase.getConn();
            Statement st=con.createStatement();
            st.executeUpdate("delete from banks");
            st.executeUpdate("delete from accountholders");
            st.executeUpdate("delete from transactions");
            st.executeUpdate("delete from notifications");
            st.executeUpdate("delete from requests");
    	}
    	catch(Exception e)
    	{
    		System.out.println(e);
    	}
    }
    
    public static void updatePhoneAndName(User ra)
    {
    	try
    	{
    		Connection con=MyDataBase.getConn();
    		PreparedStatement ps=con.prepareStatement("update accountholders set uname=?, uphone=? where uid=? and bname=?");
    		ps.setString(1, ra.name);
    		ps.setDouble(2, ra.phoneNo);
    		ps.setString(3, ra.upiId);
    		ps.setString(4, ra.bankName);
    		ps.executeUpdate();
    	}
    	catch(Exception e)
    	{
    		System.out.println(e);
    	}
    }
}
