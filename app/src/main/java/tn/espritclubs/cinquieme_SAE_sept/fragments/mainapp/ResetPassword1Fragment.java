package tn.espritclubs.cinquieme_SAE_sept.fragments.mainapp;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import tn.espritclubs.cinquieme_SAE_sept.R;
import tn.espritclubs.cinquieme_SAE_sept.models.User;
import tn.espritclubs.cinquieme_SAE_sept.services.EmailSender;
import tn.espritclubs.cinquieme_SAE_sept.utils.CodeGenerator;


public class ResetPassword1Fragment extends Fragment {

    private TextView backToSignIn, warningTextView;
    private Button goToNext;
    private EditText emailorphone;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @SuppressLint("MissingInflatedId")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_reset_password1, container, false);
        backToSignIn = view.findViewById(R.id.backToSignIn);
        warningTextView = view.findViewById(R.id.warningTextView);
        goToNext = view.findViewById(R.id.goToNext);
        emailorphone = view.findViewById(R.id.emailInput);

        goToNext.setOnClickListener(houssem -> {
            String emailOrPhone = emailorphone.getText().toString().trim();
            User user = User.getUserForEmail(emailOrPhone);

            if (user == null) {
                user = User.getUserForPhone(emailOrPhone);
            }

            if (user == null) {
                warningTextView.setVisibility(View.VISIBLE);
                warningTextView.setText("Email or phone not found");
            } else {
                String code = CodeGenerator.generateSixDigitCode();

                String emailContent = "Your 6-digit verification code is: " + code;

                EmailSender emailSender= new EmailSender(getContext(), user.getEmail(), "Verification Code", emailContent);
                emailSender.execute();

                ResetPassword2Fragment resetPassword2Fragment = new ResetPassword2Fragment();
                Bundle bundle = new Bundle();
                bundle.putString("emailorphone", user.getEmail());
                bundle.putString("digitcode", code);
                bundle.putInt("userid", user.getId());
                resetPassword2Fragment.setArguments(bundle);
                getActivity()
                        .getSupportFragmentManager()
                        .beginTransaction()
                        .setCustomAnimations(R.anim.fade_in, R.anim.fade_out)
                        .replace(R.id.main_fragment_container, resetPassword2Fragment)
                        .commit();
            }
        });

        emailorphone.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                warningTextView.setVisibility(View.GONE);
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        backToSignIn.setOnClickListener(houssem->{
            getActivity().getSupportFragmentManager().beginTransaction().setCustomAnimations(R.anim.fade_in, R.anim.fade_out).replace(R.id.main_fragment_container, new LogInFragment()).commit();
        });
        return view;
    }
}