package com.kosmo.mukja;

import android.os.Bundle;
import android.os.StrictMode;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ComponentActivity;

import com.google.android.material.textfield.TextInputLayout;

import java.util.regex.Pattern;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.OnEditorAction;
import butterknife.OnFocusChange;
import butterknife.OnTextChanged;

public class RegisterActivity extends AppCompatActivity {

    @BindView(R.id.email)
    EditText email;
    @BindView(R.id.password)
    EditText password;
    @BindView(R.id.confirmPassword)
    EditText confirmPassword;
    @BindView(R.id.emailNumberLayout)
    TextInputLayout emailLayout;
    @BindView(R.id.emailNumber)
    EditText emailNumber;
    @BindView(R.id.emailVerify)
    Button confirm;

    Boolean isEmail = false, isPassWord = false, isConfirmPassword = false, isEmailNumber = false;

    String emailCode="";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_register);

        ButterKnife.bind(this);
    }

    @OnFocusChange(R.id.email)
    void onEmailKeyPress() {
        Log.i("com.kosmo.mukja", "onEmailKeyPress");
        isEmail = validateEmailInput(email.getText().toString());
    }

    @OnFocusChange(R.id.password)
    void onPasswordKeyPress() {
        Log.i("com.kosmo.mukja", "onPasswordKeyPress");
        isPassWord = validatePasswordInput(password.getText().toString());
    }

    @OnFocusChange(R.id.confirmPassword)
    void onCPasswordKeyPress() {
        Log.i("com.kosmo.mukja", "onCPasswordKeyPress");
        Log.i("com.kosmo.mukja", password.getText().toString() + confirmPassword.getText().toString());
        isConfirmPassword = validateCPasswordInput(password.getText().toString(), confirmPassword.getText().toString());
    }

    @OnFocusChange(R.id.emailNumber)
    void onEmailNumberKeyPress() {
        Log.i("com.kosmo.mukja", "onEmailNumberKeyPress");
        isEmailNumber = validateEmailNumberInput(emailNumber.getText().toString());
    }

    @OnClick(R.id.emailVerify)
    void onButtonClicked() {
        Log.i("com.kosmo.mukja", "onButtonClicked");

        isEmail = validateEmailInput(email.getText().toString());
        isPassWord = validatePasswordInput(password.getText().toString());
        isConfirmPassword = validateCPasswordInput(password.getText().toString(), confirmPassword.getText().toString());
        isEmailNumber = validateEmailNumberInput(emailNumber.getText().toString());

        //인증번호 입력 칸을 보여주면서 메일 보내는 곳
        if (emailLayout.getVisibility() == View.GONE && (isEmail && isPassWord && isConfirmPassword)) {
            emailLayout.setVisibility(View.VISIBLE);

            //이메일 보내기
            StrictMode.setThreadPolicy(
                    new StrictMode.ThreadPolicy.Builder()
                            .permitDiskReads().permitDiskWrites()
                            .permitNetwork().build());

            SendMail mailServer = new SendMail();
            mailServer.sendSecurityCode(getApplicationContext(), email.getText().toString());

            emailCode = GMailSender.getEmailCode();
            Log.i("com.kosmo.mukja", "emailCode" + emailCode);
        }


        Log.i("com.kosmo.mukja", "isEmail" + isEmail);
        Log.i("com.kosmo.mukja", "isPassWord" + isPassWord);
        Log.i("com.kosmo.mukja", "isConfirmPassword" + isConfirmPassword);
        Log.i("com.kosmo.mukja", "isEmailNumber" + isEmailNumber);

        if (isEmail && isPassWord && isConfirmPassword && isEmailNumber) {
            Log.i("com.kosmo.mukja", "인증");

            finish();
        }
    }

    public boolean validateEmailInput(String inemail) {

        if (inemail.isEmpty()) {
            email.setError("이메일을 입력하세요");
            return false;
        } else if (!Patterns.EMAIL_ADDRESS.matcher(inemail).matches()) {
            email.setError("이메일 형식이 아닙니다");
            return false;
        }
        return true;
    }

    public boolean validatePasswordInput(String inpassword) {

        if (inpassword.isEmpty()) {
            password.setError("비밀번호를 입력하세요");
            return false;
        }
        //정규표현식 교체!!!!!!!!!!!!!!!!!!!!
        /*else if(!Pattern.matches("^(?=.*\\d)(?=.*[~`!@#$%\\^&*()-])(?=.*[a-zA-Z]).{8,20}$", inpassword)) {
            password.setError(" 아닙니다");
            return false;
        }*/
        return true;
    }

    public boolean validateCPasswordInput(String inpassword, String incpassword) {

        if (incpassword.isEmpty()) {
            confirmPassword.setError("비밀번호를 입력하세요");
            return false;
        } else if (!inpassword.equals(incpassword)) {
            Log.i("com.kosmo.mukja", String.valueOf(!inpassword.equals(incpassword)));
            confirmPassword.setError("비밀번호가 일치하지 않습니다");
            return false;
        }
        //정규표현식 교체!!!!!!!!!!!!!!!!!!!!
        /*else if(!Pattern.matches("^(?=.*\\d)(?=.*[~`!@#$%\\^&*()-])(?=.*[a-zA-Z]).{8,20}$", incpassword)) {
            confirmPassword.setError(" 아닙니다");
            return false;
        }*/
        return true;
    }

    public boolean validateEmailNumberInput(String inemailnumber) {

        if(!inemailnumber.equals(emailCode)){
            emailNumber.setError("인증번호가 틀립니다.");
            return false;
        }
        else if (inemailnumber.isEmpty()) {
            emailNumber.setError("인증번호를 입력하세요");
            return false;
        }
        Toast.makeText(RegisterActivity.this,"인증이 완료되었습니다.",Toast.LENGTH_SHORT).show();
        return true;
    }
}