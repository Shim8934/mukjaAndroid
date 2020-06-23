package com.kosmo.mukja;

import android.content.Context;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import javax.mail.MessagingException;
import javax.mail.SendFailedException;

public class SendMail extends AppCompatActivity {
    String user = "wkdrns3213@gmail.com"; // 보내는 계정의 id
    String password = "tjddnd4216@"; //보내는 계정의 pw

    public void sendSecurityCode(Context context, String sendTo) {
        try {
            GMailSender gMailSender = new GMailSender(user, password);
            gMailSender.sendMail("안녕하세요! 골라먹자 회원가입 인증 이메일 입니다.",
                    "\r\n \r\n 안녕하세요! 회원님 저희 홈페이지를 찾아주셔서 감사합니다.\r\n \r\n"+" 인증번호는 "+gMailSender.getEmailCode()+" 입니다.\n"+
                            "\n받으신 인증번호를 홈페이지에 입력해 주시면 다음으로 넘어갑니다.", sendTo);
            Toast.makeText(context, "이메일을 성공적으로 보냈습니다.", Toast.LENGTH_SHORT).show();
        } catch (SendFailedException e) {
            Toast.makeText(context, "이메일 형식이 잘못되었습니다.", Toast.LENGTH_SHORT).show();
        } catch (MessagingException e) {
            Toast.makeText(context, "인터넷 연결을 확인해주십시오", Toast.LENGTH_SHORT).show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}


