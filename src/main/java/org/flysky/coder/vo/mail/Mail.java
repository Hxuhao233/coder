package org.flysky.coder.vo.mail;

import org.flysky.coder.config.SecurityUtil;

import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.util.Date;
import java.util.Properties;

public class Mail {
    public static void sendMail(String myEmailAccount,String myEmailPassword,String myEmailSMTPHost,String receiveMailAccount,Integer userId,String context) throws Exception{
        Properties props = new Properties();
        props.setProperty("mail.transport.protocol", "smtp");
        props.setProperty("mail.smtp.host", myEmailSMTPHost);
        props.setProperty("mail.smtp.auth", "true");

        Session session = Session.getDefaultInstance(props);
        session.setDebug(true);


        MimeMessage message = createMimeMessage(session, myEmailAccount, receiveMailAccount,userId,context);


        Transport transport = session.getTransport();
        transport.connect(myEmailAccount, myEmailPassword);
        transport.sendMessage(message, message.getAllRecipients());
        Thread.sleep(1000);
        transport.close();
    }
    public static MimeMessage createMimeMessage(Session session, String sendMail, String receiveMail,Integer userid,String context) throws Exception {

        MimeMessage message = new MimeMessage(session);
        message.setFrom(new InternetAddress(sendMail, "XXX", "UTF-8"));
        message.setRecipient(MimeMessage.RecipientType.TO, new InternetAddress(receiveMail, "用户", "UTF-8"));
        message.setSubject("激活链接", "UTF-8");
        long currentTime = System.currentTimeMillis();
        String content = String.valueOf(userid)+String.valueOf(currentTime);
        String encodedContent = SecurityUtil.encrypt(content);
        String c=context+"/activate/"+String.valueOf(userid)+"/"+String.valueOf(currentTime)+"/"+encodedContent;
        String b="<a href=http://"+c+">激活链接</a>";
        //String a="<a href=http://www.baidu.com>baidu</a>";
        System.out.print("MSG"+b);
        message.setContent(b, "text/html;charset=UTF-8");
        //System.out.println((String)message.getContent());
        message.setSentDate(new Date());
        message.saveChanges();

        return message;
    }
}
