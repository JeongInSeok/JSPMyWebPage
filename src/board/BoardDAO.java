package board;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.sql.DataSource;

import member.MemberBean;

public class BoardDAO {
	// 디비연결 1단계,2단계 메서드 정의
		private Connection getConnection() throws Exception{
			// 예외처리를 메서드호출한 곳에서 처리하겠다
			
//			// 1단계 드라이버로더
//			 Class.forName("com.mysql.jdbc.Driver");
//			 // 2단계 디비연결
//			 String dbUrl="jdbc:mysql://localhost:3306/jspdb1";
//			 String dbUser="jspid";
//			 String dbPass="jsppass";
//			 Connection con=DriverManager.getConnection(dbUrl, dbUser, dbPass);
//			 return con;
			 
			 // 커넥션 풀(connection Pool) : 미리 서버 연결정보를 저장 => 필요할때 이름을 불러서 사용
			 //      프로그램이 서버단에 미리저장 DBCP(DataBase Connection Pool) API
			 //  WebContent - META-INF - context.xml 파일 만들기
			
			Context init=new InitialContext();
			DataSource ds=(DataSource)init.lookup("java:comp/env/jdbc/MysqlDB");
			Connection con=ds.getConnection();
			return con;
		}
		
		public void insertBoard(BoardBean bb) {
			Connection con=null;
			PreparedStatement pstmt=null;
			ResultSet rs=null;
			int num=0;
			try {
				// 예외가 발생하것 같은 구문
				// 1단계 드라이버로더			 // 2단계 디비연결
				con=getConnection();
				
				// 3단계   sql board테이블내에서  num열 중에 가장큰번호 구해서 가져오기  count() sum() max() min() 
				String sql="select max(num) from board";
				pstmt=con.prepareStatement(sql);
				// 4단계 rs<-실행 결과 저장
				rs=pstmt.executeQuery();
				// 5단계 첫행으로 이동 결과값이 있으면 (max(num) 무조건 결과값이 있음)
				if(rs.next()){
					num=rs.getInt("max(num)")+1;
				}
				// 3단계 sql insert  board테이블에 글 저장  
//				      readcount=0  date = new Timestamp()
				sql="insert into board(num,name,pass,subject,content,readcount,date,file) values(?,?,?,?,?,?,?,?)";
				pstmt=con.prepareStatement(sql);
				pstmt.setInt(1, num);
				pstmt.setString(2, bb.getName());
				pstmt.setString(3, bb.getPass());
				pstmt.setString(4, bb.getSubject());
				pstmt.setString(5, bb.getContent());
				pstmt.setInt(6, bb.getReadcount());
				pstmt.setTimestamp(7, bb.getDate());
				pstmt.setString(8, bb.getFile());
				// 4단계 sql문 실행
				pstmt.executeUpdate();
			} catch (Exception e) {
				// 예외가 발생하면 처리하는 구문
				e.printStackTrace();
			}finally {
				// 예외상관없이 마무리작업 구문=> 기억장소 해제
				if(rs!=null) try {rs.close();}catch(SQLException ex) {}
				if(pstmt!=null) try {pstmt.close();}catch(SQLException ex) {}
				if(con!=null) try{con.close();} catch(SQLException ex) {}
			}
		}
		
		
		public List getBoardList(int startRow,int pageSize) {
			Connection con=null;
			PreparedStatement pstmt=null;
			ResultSet rs=null;
			List boardList=new ArrayList();
			try {
				// 예외가 발생하것 같은 구문
				// 1단계 드라이버로더			 // 2단계 디비연결
				con=getConnection();
				
				// 3단계 sql board테이블 모든글 가져오기(최근글이 맨처음에 나오게 정렬)
//				String sql="select * from board order by num desc";
				String sql="select * from board order by num desc limit ?,?";
				pstmt=con.prepareStatement(sql);
				pstmt.setInt(1, startRow-1);// startRow 시작행 포함하지 않아서 -1 
				pstmt.setInt(2, pageSize);
				// 4단계 rs <= 실행 결과 저장
				rs=pstmt.executeQuery();
				// 5단계   bb < =  rs    bb => 배열한칸에 저장
				while(rs.next()) {
					BoardBean bb=new BoardBean();
					bb.setNum(rs.getInt("num"));
					bb.setPass(rs.getString("pass"));
					bb.setName(rs.getString("name"));
					bb.setSubject(rs.getString("subject"));
					bb.setContent(rs.getString("content"));
					bb.setReadcount(rs.getInt("readcount"));
					bb.setDate(rs.getTimestamp("date"));
					// file추가
					bb.setFile(rs.getString("file"));
					// 한개의 글 배열 한컨에 저장
					boardList.add(bb);
				}
			} catch (Exception e) {
				// 예외가 발생하면 처리하는 구문
				e.printStackTrace();
			}finally {
				// 예외상관없이 마무리작업 구문=> 기억장소 해제
				if(rs!=null) try {rs.close();}catch(SQLException ex) {}
				if(pstmt!=null) try {pstmt.close();}catch(SQLException ex) {}
				if(con!=null) try{con.close();} catch(SQLException ex) {}
			}
			return boardList;
		}
		
		public List getBoardList(int startRow,int pageSize,String search) {
			Connection con=null;
			PreparedStatement pstmt=null;
			ResultSet rs=null;
			List boardList=new ArrayList();
			try {
				// 예외가 발생하것 같은 구문
				// 1단계 드라이버로더			 // 2단계 디비연결
				con=getConnection();
				
				// 3단계 sql board테이블 모든글 가져오기(최근글이 맨처음에 나오게 정렬)
//				String sql="select * from board order by num desc";
//				String sql="select * from board where subject like '%검색어%' order by num desc limit ?,?";
				String sql="select * from board where subject like ? order by num desc limit ?,?";
				pstmt=con.prepareStatement(sql);
				pstmt.setString(1, "%"+search+"%");
				pstmt.setInt(2, startRow-1);// startRow 시작행 포함하지 않아서 -1 
				pstmt.setInt(3, pageSize);
				// 4단계 rs <= 실행 결과 저장
				rs=pstmt.executeQuery();
				// 5단계   bb < =  rs    bb => 배열한칸에 저장
				while(rs.next()) {
					BoardBean bb=new BoardBean();
					bb.setNum(rs.getInt("num"));
					bb.setPass(rs.getString("pass"));
					bb.setName(rs.getString("name"));
					bb.setSubject(rs.getString("subject"));
					bb.setContent(rs.getString("content"));
					bb.setReadcount(rs.getInt("readcount"));
					bb.setDate(rs.getTimestamp("date"));
					// file추가
					bb.setFile(rs.getString("file"));
					// 한개의 글 배열 한컨에 저장
					boardList.add(bb);
				}
			} catch (Exception e) {
				// 예외가 발생하면 처리하는 구문
				e.printStackTrace();
			}finally {
				// 예외상관없이 마무리작업 구문=> 기억장소 해제
				if(rs!=null) try {rs.close();}catch(SQLException ex) {}
				if(pstmt!=null) try {pstmt.close();}catch(SQLException ex) {}
				if(con!=null) try{con.close();} catch(SQLException ex) {}
			}
			return boardList;
		}
		
		// updateReadcount(num)
		public void updateReadcount(int num) {
			Connection con=null;
			PreparedStatement pstmt=null;
			ResultSet rs=null;
			try {
				// 예외가 발생하것 같은 구문
				// 1단계 드라이버로더			 // 2단계 디비연결
				con=getConnection();
				
				// 3단계 sql 
				String sql="update board set readcount=readcount+1 where num=?";
				pstmt=con.prepareStatement(sql);
				pstmt.setInt(1, num);
				// 4단계 sql문 실행
				pstmt.executeUpdate();
			} catch (Exception e) {
				// 예외가 발생하면 처리하는 구문
				e.printStackTrace();
			}finally {
				// 예외상관없이 마무리작업 구문=> 기억장소 해제
				if(rs!=null) try {rs.close();}catch(SQLException ex) {}
				if(pstmt!=null) try {pstmt.close();}catch(SQLException ex) {}
				if(con!=null) try{con.close();} catch(SQLException ex) {}
			}
		}
		// BoardBean bb  =  getBoard(num) 
		public BoardBean getBoard(int num) {
			Connection con=null;
			PreparedStatement pstmt=null;
			ResultSet rs=null;
			BoardBean bb=new BoardBean();
			try {
				// 예외가 발생하것 같은 구문
				// 1단계 드라이버로더			 // 2단계 디비연결
				con=getConnection();
				// 3단계 sql board테이블 모든글 가져오기(최근글이 맨처음에 나오게 정렬)
				String sql="select * from board where num=?";
				pstmt=con.prepareStatement(sql);
				pstmt.setInt(1, num);
				// 4단계 rs <= 실행 결과 저장
				rs=pstmt.executeQuery();
				// 5단계   bb < =  rs    
				while(rs.next()) {
					bb.setNum(rs.getInt("num"));
					bb.setPass(rs.getString("pass"));
					bb.setName(rs.getString("name"));
					bb.setSubject(rs.getString("subject"));
					bb.setContent(rs.getString("content"));
					bb.setReadcount(rs.getInt("readcount"));
					bb.setDate(rs.getTimestamp("date"));
					// file 추가
					bb.setFile(rs.getString("file"));
				}
			} catch (Exception e) {
				// 예외가 발생하면 처리하는 구문
				e.printStackTrace();
			}finally {
				// 예외상관없이 마무리작업 구문=> 기억장소 해제
				if(rs!=null) try {rs.close();}catch(SQLException ex) {}
				if(pstmt!=null) try {pstmt.close();}catch(SQLException ex) {}
				if(con!=null) try{con.close();} catch(SQLException ex) {}
			}
			return bb;
		}
		
		//passCheck(bb)
		public int passCheck(BoardBean bb) {
			Connection con=null;
			PreparedStatement pstmt=null;
			ResultSet rs=null;
			int check=0;
			try {
				// 예외가 발생하것 같은 구문
				// 1단계 드라이버로더			 // 2단계 디비연결
				con=getConnection();
				// 3단계 sql board테이블 
				String sql="select * from board where num=?";
				pstmt=con.prepareStatement(sql);
				pstmt.setInt(1, bb.getNum());
				// 4단계 rs <= 실행 결과 저장
				rs=pstmt.executeQuery();
				// 5단계     
				if(rs.next()) {
					if(bb.getPass().equals(rs.getString("pass"))) {
						check=1;
					}else {
						check=0;
					}
				}
			} catch (Exception e) {
				// 예외가 발생하면 처리하는 구문
				e.printStackTrace();
			}finally {
				// 예외상관없이 마무리작업 구문=> 기억장소 해제
				if(rs!=null) try {rs.close();}catch(SQLException ex) {}
				if(pstmt!=null) try {pstmt.close();}catch(SQLException ex) {}
				if(con!=null) try{con.close();} catch(SQLException ex) {}
			}
			return check;
		}
		
		//updateBoard(bb)
		public void updateBoard(BoardBean bb) {
			Connection con=null;
			PreparedStatement pstmt=null;
			ResultSet rs=null;
			try {
				// 예외가 발생하것 같은 구문
				// 1단계 드라이버로더			 // 2단계 디비연결
				con=getConnection();
				// 3단계 sql 
				String sql="update board set name=?,subject=?,content=? where num=?";
				pstmt=con.prepareStatement(sql);
				pstmt.setString(1, bb.getName());
				pstmt.setString(2, bb.getSubject());
				pstmt.setString(3, bb.getContent());
				pstmt.setInt(4, bb.getNum());
				// 4단계 sql문 실행
				pstmt.executeUpdate();
			} catch (Exception e) {
				// 예외가 발생하면 처리하는 구문
				e.printStackTrace();
			}finally {
				// 예외상관없이 마무리작업 구문=> 기억장소 해제
				if(rs!=null) try {rs.close();}catch(SQLException ex) {}
				if(pstmt!=null) try {pstmt.close();}catch(SQLException ex) {}
				if(con!=null) try{con.close();} catch(SQLException ex) {}
			}
		}
		
		//deleteBoard(bb)
		public void deleteBoard(BoardBean bb) {
			Connection con=null;
			PreparedStatement pstmt=null;
			ResultSet rs=null;
			try {
				// 예외가 발생하것 같은 구문
				// 1단계 드라이버로더			 // 2단계 디비연결
				con=getConnection();
				// 3단계 sql 
				String sql="delete from board where num=?";
				pstmt=con.prepareStatement(sql);
				pstmt.setInt(1, bb.getNum());
				// 4단계 sql문 실행
				pstmt.executeUpdate();
			} catch (Exception e) {
				// 예외가 발생하면 처리하는 구문
				e.printStackTrace();
			}finally {
				// 예외상관없이 마무리작업 구문=> 기억장소 해제
				if(rs!=null) try {rs.close();}catch(SQLException ex) {}
				if(pstmt!=null) try {pstmt.close();}catch(SQLException ex) {}
				if(con!=null) try{con.close();} catch(SQLException ex) {}
			}
		}
		
		//getBoardCount()
		public int getBoardCount() {
			Connection con=null;
			PreparedStatement pstmt=null;
			ResultSet rs=null;
			int count=0;
			try {
				// 예외가 발생하것 같은 구문
				// 1단계 드라이버로더			 // 2단계 디비연결
				con=getConnection();
				// 3단계 sql board테이블 
				String sql="select count(*) from board";
				pstmt=con.prepareStatement(sql);
				// 4단계 rs <= 실행 결과 저장
				rs=pstmt.executeQuery();
				// 5단계     
				if(rs.next()) {
						count=rs.getInt("count(*)");
				}
			} catch (Exception e) {
				// 예외가 발생하면 처리하는 구문
				e.printStackTrace();
			}finally {
				// 예외상관없이 마무리작업 구문=> 기억장소 해제
				if(rs!=null) try {rs.close();}catch(SQLException ex) {}
				if(pstmt!=null) try {pstmt.close();}catch(SQLException ex) {}
				if(con!=null) try{con.close();} catch(SQLException ex) {}
			}
			return count;
		}
		// 메서드 오버로딩 이용 매개변수 개수 틀리게
		public int getBoardCount(String search) {
			Connection con=null;
			PreparedStatement pstmt=null;
			ResultSet rs=null;
			int count=0;
			try {
				// 예외가 발생하것 같은 구문
				// 1단계 드라이버로더			 // 2단계 디비연결
				con=getConnection();
				// 3단계 sql board테이블 
//				String sql="select count(*) from board where subject like '%검색어%' ";
				String sql="select count(*) from board where subject like ? ";
				pstmt=con.prepareStatement(sql);
				pstmt.setString(1, "%"+search+"%");
				// 4단계 rs <= 실행 결과 저장
				rs=pstmt.executeQuery();
				// 5단계     
				if(rs.next()) {
						count=rs.getInt("count(*)");
				}
			} catch (Exception e) {
				// 예외가 발생하면 처리하는 구문
				e.printStackTrace();
			}finally {
				// 예외상관없이 마무리작업 구문=> 기억장소 해제
				if(rs!=null) try {rs.close();}catch(SQLException ex) {}
				if(pstmt!=null) try {pstmt.close();}catch(SQLException ex) {}
				if(con!=null) try{con.close();} catch(SQLException ex) {}
			}
			return count;
		}
		
}










