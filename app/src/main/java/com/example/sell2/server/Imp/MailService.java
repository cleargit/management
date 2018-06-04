package com.example.sell2.server.Imp;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import javax.mail.internet.MimeMessage;

@Service
public class MailService {
    @Value("${spring.mail.username}")
    private String from;
    @Autowired
    private JavaMailSender sender;
    public void sendSimple(String to, String title, String content){
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom(from); //发送者
        message.setTo(to); //接受者
        message.setSubject(title); //发送标题
        message.setText(content);  //发送内容
        sender.send(message);

    }
    @Async
    public void sendhtml(String to,String title,String token)
    {
        MimeMessage message = null;
        try{
            message=sender.createMimeMessage();
            MimeMessageHelper helper=new MimeMessageHelper(message,true);
            helper.setFrom(from);
            helper.setTo(to);
            helper.setSubject(title);

            StringBuffer sb=new StringBuffer("<div style=\"width: 660px;overflow: hidden;margin: 0 auto;\">\n" +
                    "    <div style=\"height:70px;overflow:hidden;border:1px solid #464c51;background:#353b3f\">\n" +
                    "        <a href=\"http://www.fadeoooo.top/blog/index.html\" target=\"_blank\"\n" +
                    "           style=\"display:block;height: 86px;width:268px;margin:0 0 0 20px;overflow:hidden;text-indent:-2000px;background:url(http://www.fadeoooo.top/blog/images/logo.png) no-repeat;\">sham</a>\n" +
                    "    </div>\n" +
                    "    <div style=\"padding: 20px 20px;font-size: 16px\">\n" +
                    "\n" +
                    "        欢迎注册\n" +
                    "        <br>\n" +
                    "        <br>\n" +
                    "        请点击下面链接激活账号，5分钟生效，否则重新注册账号，链接只能使用一次，请尽快激活！\n" +
                    "        <br>\n" +
                    "        <br>");
            sb.append("<a href=\"http://www.fadeoooo.top/user/confirm.do?token=");
            sb.append(token);
            sb.append("\">点击激活");
            sb.append("</a>");
            sb.append("<br>\n" +
                    "        如果以上链接无法点击，请把上面网页地址复制到浏览器地址栏中打开\n" +
                    "        <br>\n" +
                    "        <br>\n" +
                    "\n" +
                    "        sham\n" +
                    "    </div>\n" +
                    "\n" +
                    "</div>");
            helper.setText(sb.toString(), true);
        }catch (Exception e)
        {
        e.printStackTrace();
        }
        sender.send(message);
    }

}
