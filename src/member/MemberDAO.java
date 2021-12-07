package member;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.sql.DataSource;

public class MemberDAO {
	private Connection getConnection() throws Exception{
		Context init=new InitialContext();
		DataSource ds=(DataSource)init.lookup("java:comp/env/jdbc/MysqlDB");
		Connection con=ds.getConnection();
		return con;
	}
	
	public void insertMember(MemberBean mb) {
		Connection con=null;
		PreparedStatement pstmt=null;
		try {
		
			con=getConnection();
			
			 String sql="insert into member(id,pass,name,reg_date,email,address,phone,mobile,postcode) values(?,?,?,?,?,?,?,?,?)";
			 pstmt=con.prepareStatement(sql);
			 pstmt.setString(1, mb.getId());
			 pstmt.setString(2, mb.getPass());
			 pstmt.setString(3, mb.getName());
			 pstmt.setTimestamp(4, mb.getReg_date());
			 pstmt.setString(5, mb.getEmail());
			 pstmt.setString(6, mb.getAddress());
			 pstmt.setString(7, mb.getPhone());
			 pstmt.setString(8, mb.getMobile());
			 pstmt.setString(9, mb.getPostcode());
			 
			 // 4�떒怨� sql臾� �떎�뻾
			 pstmt.executeUpdate();
		} catch (Exception e) {
			// �삁�쇅媛� 諛쒖깮�븯硫� 泥섎━�븯�뒗 援щЦ
			e.printStackTrace();
		}finally {
			// �삁�쇅�긽愿��뾾�씠 留덈Т由ъ옉�뾽 援щЦ=> 湲곗뼲�옣�냼 �빐�젣
			if(pstmt!=null) try{pstmt.close();}catch(SQLException ex) {}
			if(con!=null) try{con.close();}catch(SQLException ex) {}
		}
	}// insertMember()
	
	// userCheck(�븘�씠�뵒,鍮꾨�踰덊샇)
		public int userCheck(String id,String pass) {
			int check=-1;
			Connection con=null;
			PreparedStatement pstmt=null;
			ResultSet rs=null;
			try {
				// 1�떒怨� �뱶�씪�씠踰꾨줈�뜑			 // 2�떒怨� �뵒鍮꾩뿰寃�
				con=getConnection();
		
				 String sql="select * from member where id=?";
				 pstmt=con.prepareStatement(sql);
				 pstmt.setString(1, id);
				 // 4�떒怨� �떎�뻾 => 寃곌낵 ���옣
				 rs=pstmt.executeQuery();
				 if(rs.next()){
				 	//�븘�씠�뵒 �씪移�
				 	if(pass.equals(rs.getString("pass"))){
				 		check=1;
				 	}else{
				 		check=0;
				 	}
				 }else{
				 	check=-1;
				 }
			} catch (Exception e) {
				e.printStackTrace();
			}finally {
				// �삁�쇅�긽愿��뾾�씠 留덈Т由ъ옉�뾽 援щЦ=> 湲곗뼲�옣�냼 �빐�젣
				if(rs!=null) try{rs.close();}catch(SQLException ex) {}
				if(pstmt!=null) try{pstmt.close();}catch(SQLException ex) {}
				if(con!=null) try{con.close();}catch(SQLException ex) {}
			}
			return check;
		}
		
		// getMember(�븘�씠�뵒)
		public MemberBean getMember(String id) {
			MemberBean mb=new MemberBean();
			Connection con=null;
			PreparedStatement pstmt=null;
			ResultSet rs=null;
			try {
				// 1�떒怨� �뱶�씪�씠踰꾨줈�뜑			 // 2�떒怨� �뵒鍮꾩뿰寃�
							con=getConnection();
				 // 3�떒怨� sql 留뚮뱾怨� �떎�뻾�븷�닔 �엳�뒗 媛앹껜 留뚮뱾湲� select
				 String sql="select * from member where id=?";
				 pstmt=con.prepareStatement(sql);
				 pstmt.setString(1, id);
				 // 4�떒怨� 寃곌낵<=�떎�뻾 寃곌낵 ���옣
				 rs=pstmt.executeQuery();
				 // 5�떒怨� rs �궡�슜 �쐞移� 泥ロ뻾�씠�룞 �뜲�씠�꽣 �엳�쑝硫� 異쒕젰  
				 if(rs.next()){
					 // rs �궡�슜�쓣 MemberBean 硫ㅻ쾭蹂��닔 媛곴컖 ���옣
					 mb.setId(rs.getString("id"));
					 mb.setPass(rs.getString("pass"));
					 mb.setName(rs.getString("name"));
					 mb.setReg_date(rs.getTimestamp("reg_date"));
				 }
			} catch (Exception e) {
				e.printStackTrace();
			}finally {
				if(rs!=null) try{rs.close();}catch(SQLException ex) {}
				if(pstmt!=null) try{pstmt.close();}catch(SQLException ex) {}
				if(con!=null) try{con.close();}catch(SQLException ex) {}
			}
			return mb;
		}
		// updateMember(mb);
		public void updateMember(MemberBean mb) {
			Connection con=null;
			PreparedStatement pstmt=null;
			try {
				// 1�떒怨� �뱶�씪�씠踰꾨줈�뜑			 // 2�떒怨� �뵒鍮꾩뿰寃�
							con=getConnection();
//				 3�떒怨� �닔�젙
				String sql="update member set name=?, pass=?, email=?, address=?, postcode=?, phone=?, mobile=? where id=?";
		 		pstmt=con.prepareStatement(sql);
		 		pstmt.setString(1, mb.getName());
		 		pstmt.setString(2, mb.getPass());
		 		pstmt.setString(3, mb.getEmail());
		 		pstmt.setString(4, mb.getAddress());
		 		pstmt.setString(5, mb.getPostcode());
		 		pstmt.setString(6, mb.getPhone());
		 		pstmt.setString(7, mb.getMobile());
		 		pstmt.setString(8, mb.getId());
		 		
		 		// 4�떒怨�  媛앹껜 �떎�뻾 (sql援щЦ�씠 �떎�뻾) - insert, update, delete
		 		pstmt.executeUpdate();
			} catch (Exception e) {
				e.printStackTrace();
			}finally {
				if(pstmt!=null) try{pstmt.close();}catch(SQLException ex) {}
				if(con!=null) try{con.close();}catch(SQLException ex) {}
			}
		}
		
	
		
		// deleteMember(id,pass)
		public void deleteMember(String id,String pass) {
			Connection con=null;
			PreparedStatement pstmt=null;
			try {
				// 1�떒怨� �뱶�씪�씠踰꾨줈�뜑			 // 2�떒怨� �뵒鍮꾩뿰寃�
							con=getConnection();
//				 3�떒怨� �닔�젙
			 		String sql="delete from member where id=?";
			 		pstmt=con.prepareStatement(sql);
			 		pstmt.setString(1, id);
		 		// 4�떒怨�  媛앹껜 �떎�뻾 (sql援щЦ�씠 �떎�뻾) - insert, update, delete
		 		pstmt.executeUpdate();
			} catch (Exception e) {
				e.printStackTrace();
			}finally {
				if(pstmt!=null) try{pstmt.close();}catch(SQLException ex) {}
				if(con!=null) try{con.close();}catch(SQLException ex) {}
			}
		}
		
		// getMemberList()
		public  List getMemberList() {
			// �뿬�윭紐낆쓽 �뜲�씠�꽣瑜� ���옣�븯�뒗 湲곗뼲�옣�냼,諛곗뿴API
			List memberList=new ArrayList();
			Connection con=null;
			PreparedStatement pstmt=null;
			ResultSet rs=null;
			try {
				// 1�떒怨� �뱶�씪�씠踰꾨줈�뜑			 // 2�떒怨� �뵒鍮꾩뿰寃�
							con=getConnection();
				 // 3�떒怨� sql member �쟾泥� 議고쉶
				 String sql="select * from member";
				 pstmt=con.prepareStatement(sql);
				 // 4�떒怨� rs <= �떎�뻾寃곌낵 ���옣
				 rs=pstmt.executeQuery();
				 // 5�떒怨� while 泥ロ뻾�쑝濡� �씠�룞 �뜲�씠�꽣 �엳�쑝硫�
				 //           �븳�궗�엺�쓽 �뜲�씠�꽣 ���옣  MemberBean mb 媛앹껜�깮�꽦 
				 //                           mb 硫ㅻ쾭蹂��닔 <= rs�뿉 ���옣�맂 �뿴�궡�슜
				 //           諛곗뿴 �븳移몄뿉 �븳�궗�엺�쓽 �뜲�씠�꽣 二쇱냼���옣  memberList.add(mb);
				 while(rs.next()) {
					 MemberBean mb=new MemberBean();
					 mb.setId(rs.getString("id"));
					 mb.setPass(rs.getString("pass"));
					 mb.setName(rs.getString("name"));
					 mb.setReg_date(rs.getTimestamp("reg_date"));
					 //諛곗뿴 �븳移몄뿉 �븳�궗�엺�쓽 �뜲�씠�꽣 二쇱냼���옣
					 memberList.add(mb);
				 }
			} catch (Exception e) {
				e.printStackTrace();
			}finally {
				if(rs!=null) try{rs.close();}catch(SQLException ex) {}
				if(pstmt!=null) try{pstmt.close();}catch(SQLException ex) {}
				if(con!=null) try{con.close();}catch(SQLException ex) {}
			}
			return memberList;
		}
		
		// idCheck(�븘�씠�뵒) �븘�씠�뵒 以묐났泥댄겕
				public int idCheck(String id) {
					int check=-1;
					Connection con=null;
					PreparedStatement pstmt=null;
					ResultSet rs=null;
					try {
						// 1�떒怨� �뱶�씪�씠踰꾨줈�뜑			 // 2�떒怨� �뵒鍮꾩뿰寃�
						con=getConnection();
						 // 3�떒怨� sql member�뀒�씠釉붿븞�뿉 �븘�씠�뵒 �씪移� �븳 �쉶�썝 �궡�슜 媛��졇�삤湲�, �뤌�븘�씠�뵒 �뵒鍮� �븘�씠�뵒 �씪移�
						 String sql="select * from member where id=?";
						 pstmt=con.prepareStatement(sql);
						 pstmt.setString(1, id);
						 // 4�떒怨� �떎�뻾 => 寃곌낵 ���옣
						 rs=pstmt.executeQuery();
						 // 5�떒怨�  rs 泥ロ뻾 �씠�룞 寃곌낵 �엳�쑝硫� �븘�씠�뵒 �씪移�  
						 //                    �뾾�쑝硫�  "�븘�씠�뵒 �뾾�쓬"
						 if(rs.next()){
						 	//�븘�씠�뵒 以묐났
						 	check=1;
						 }else{
							 // �븘�씠�뵒 �뾾�쓬 �궗�슜媛��뒫
							 check=0;
						 }
					} catch (Exception e) {
						e.printStackTrace();
					}finally {
						// �삁�쇅�긽愿��뾾�씠 留덈Т由ъ옉�뾽 援щЦ=> 湲곗뼲�옣�냼 �빐�젣
						if(rs!=null) try{rs.close();}catch(SQLException ex) {}
						if(pstmt!=null) try{pstmt.close();}catch(SQLException ex) {}
						if(con!=null) try{con.close();}catch(SQLException ex) {}
					}
					return check;
				}
				
}//�겢�옒�뒪










