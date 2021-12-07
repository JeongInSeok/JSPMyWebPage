package jmail;

import java.io.IOException;
import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import javax.servlet.*;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import java.util.Properties;
import java.io.PrintWriter;


@WebServlet("/mailSend")
public class MailSendServlet extends HttpServlet{
	private static final long serialVersionUID = 1L;
	
	public MailSendServlet() {
		super();
	}
	protected void doPost (HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException{
	
		request.setCharacterEncoding("UTF-8"); // 한글 정보 안 깨지도록
		String sender = request.getParameter("sender"); // 메일전송에 사용하기 위해 전송되온 파라미터들
		String receiver = request.getParameter("receiver");
		String subject = request.getParameter("subject");
		String content = request.getParameter("content");
		
		response.setContentType("text/html;charset=UTF-8");
		PrintWriter out = response.getWriter();
		try {
			Properties properties = System.getProperties();
			properties.put("mail.smtp.starttls.enable", "true"); // Starttls Command 사용할 수 있게, gmail은 무조건 true 고정
			properties.put("mail.smtp.host", "smtp.gmail.com"); // smtp 서버를 지정하는 부분  smtp주소
			
			properties.put("mail.smtp.auth", "true"); // gmail은 무조건 true 고정
			properties.put("mail.smtp.port", "587"); // gmail 포트
			Authenticator auth = new GoogleAuthentication(); // 인증정보 생성하는 부분
			Session s = Session.getDefaultInstance(properties, auth);
			//Session s = Session.getdefultInstance(properties, auth);
			
			Message message = new MimeMessage(s); // 생성한 세션 객체를 사용해 전송할 message객체 생성
			Address sender_address = new InternetAddress(sender); // 메일 송신할 송신 주소 생성
			Address receiver_address = new InternetAddress(receiver); // 메일 수신할 수신 주소 생성
			
			// 메일 전송에 필요한 값 설정하는 부분
			
			message.setHeader("content-type", "text/html;charset=UTF-8");
			
			message.setFrom(sender_address);
			message.addRecipient(Message.RecipientType.TO, receiver_address);
			message.setSubject(subject);
			message.setContent(content, "text/html;charset=UTF-8");
			message.setSentDate(new java.util.Date());
			
			Transport.send(message);
			out.println("<h3>메일이 정상적으로 전송되었습니다.</h3>");
		} catch (Exception e) {
			out.println("SMTP 서버가 잘못 설정되었거나, 서비스에 문제가 있습니다.");
			e.printStackTrace();
		}
	
	}

}
