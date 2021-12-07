package jmail;
import javax.servlet.*;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import javax.mail.*;
import javax.mail.internet.*;

import java.io.*;
import java.util.*;
@SuppressWarnings("serial")
@WebServlet("/mailServlet")
public class MailServlet extends HttpServlet{
	public void doGet(HttpServletRequest req, HttpServletResponse res) 
			throws ServletException, IOException{
		  Properties p = System.getProperties();
	        p.put("mail.smtp.starttls.enable", "true");    
	        p.put("mail.smtp.host", "smtp.gmail.com");    
	        p.put("mail.smtp.auth","true");                
	        p.put("mail.smtp.port", "587");                 
	        
	        Authenticator auth = new MyAuthentication();
	         
	        //session ���� ��  MimeMessage����
	        Session session = Session.getDefaultInstance(p, auth);
	        MimeMessage msg = new MimeMessage(session);
	         
	        try{
	            //���������ð�
	            msg.setSentDate(new Date());
	             
	            InternetAddress from = new InternetAddress() ;
	             
	             
	            from = new InternetAddress("kxv1031@naver.com");
	             
	            // �̸��� �߽���
	            msg.setFrom(from);
	             
	             
	            // �̸��� ������
	            InternetAddress to = new InternetAddress("kxv1031@naver.com");
	            msg.setRecipient(Message.RecipientType.TO, to);
	             
	            // �̸��� ����
	            msg.setSubject("test입니다.", "UTF-8");
	             
	            // �̸��� ���� 
	            msg.setText("테스트입니다.", "UTF-8");
	             
	            // �̸��� ��� 
	            msg.setHeader("content-Type", "text/html");
	             
	            //���Ϻ�����
	            javax.mail.Transport.send(msg);
	             
	        }catch (AddressException addr_e) {
	            addr_e.printStackTrace();
	        }catch (MessagingException msg_e) {
	            msg_e.printStackTrace();
	        }
	       
	        
	    }

	}

	class MyAuthentication extends Authenticator {
	    
	    PasswordAuthentication pa;
	    
	 
	    public MyAuthentication(){
	         
	        String id = "kxv1031@gmail.com";       // ���� ID
	        String pw = "Gksfk019";          // ���� ��й�ȣ
	 
	        // ID�� ��й�ȣ�� �Է��Ѵ�.
	        pa = new PasswordAuthentication(id, pw);
	      
	    }
	 
	    // �ý��ۿ��� ����ϴ� ��������
	    public PasswordAuthentication getPasswordAuthentication() {
	        return pa;
	    }

	}
