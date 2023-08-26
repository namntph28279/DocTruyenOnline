package com.example.myapplication.fragment.auth;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatButton;
import androidx.fragment.app.Fragment;

import android.os.Handler;
import android.text.InputType;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.myapplication.MainActivity;
import com.example.myapplication.R;
import com.example.myapplication.api_service.AuthService;
import com.example.myapplication.fragment.MainFragment;
import com.example.myapplication.model.User;
import com.example.myapplication.utils.AppPreferences;
import com.example.myapplication.utils.LoadingDialog;
import com.example.myapplication.utils.Validator;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class LoginFragment extends Fragment {
    private EditText edt_usr, edt_pwd;
    private AppCompatButton btn_login;
    private TextView tv_signUp, tv_errorUsr, tv_errorPwd;
    private ImageView img_visible;

    private final String TAG = "LOGIN";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_login, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        findViews(view);

//        btn_login.setOnClickListener(view1 -> handleLogin(new LoginRequest(edt_usr.getText().toString(),edt_pwd.getText().toString())));
        btn_login.setOnClickListener(view1 -> handleLogin(edt_usr.getText().toString(), edt_pwd.getText().toString()));
        tv_signUp.setOnClickListener(view1 -> getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.layout_container, new RegisterFragment()).addToBackStack(null).commit());
        img_visible.setOnClickListener(view1 -> togglePasswordVisibility());
    }


    private void togglePasswordVisibility() {
        int currentInputType = edt_pwd.getInputType();
        if ((currentInputType == (InputType.TYPE_TEXT_VARIATION_PASSWORD | InputType.TYPE_CLASS_TEXT))) {
            edt_pwd.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
            img_visible.setImageResource(R.drawable.ic_baseline_visibility_24);
        } else {
            edt_pwd.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
            img_visible.setImageResource(R.drawable.ic_baseline_visibility_off_24);
        }
        edt_pwd.setSelection(edt_pwd.getText().length());
    }


    private void handleLogin(String username, String password) {
        if (!Validator.validateInputs(username, password)) {
            handleShowError(tv_errorUsr, "Vui lòng không để trống");
            handleShowError(tv_errorPwd, "Vui lòng không để trống");
        } else {
            try {
                LoadingDialog.dialogLoading.show();

                loginResponse(new User(edt_usr.getText().toString(), edt_pwd.getText().toString(),1));

            } catch (Exception ex) {
                Log.e(TAG, "handleLogin: " + ex);
            }
        }
    }

    private void loginResponse(User user) {
        //Tạo đối tượng chuyển đối kiểu dữ liệu
       try {
           Gson gson = new GsonBuilder().setLenient().create();
           // Tạo retrofit
           Retrofit retrofit = new Retrofit.Builder().baseUrl(MainActivity.BASE_URL).addConverterFactory(GsonConverterFactory.create(gson)).build();
           //khởi tạo Interface
           AuthService authService = retrofit.create(AuthService.class);

           //tạo call
           Call<User> objCall = authService.login(user);

           //thực hiện gửi dữ liệu lên server
           objCall.enqueue(new Callback<>() {
               @Override
               public void onResponse(Call<User> call, Response<User> response) {
                   //kết quả server trả về


                   if (response.isSuccessful()) {
                       String _id = response.body().get_id();
                       AppPreferences.saveKeyIdUser(getContext(), _id);
                       Log.e(TAG, "onResponse: " + response.body().get_id());
                       //lấy kết quả trả về
                       getActivity().getSupportFragmentManager().beginTransaction()
                               .replace(R.id.layout_container, new MainFragment())
                               .commit();
                   } else {
                       Toast.makeText(getContext(), "Sai tài khoản hoặc mật khẩu", Toast.LENGTH_SHORT).show();
                   }
                   LoadingDialog.dialogLoading.dismiss();
               }


               @Override
               public void onFailure(Call<User> call, Throwable t) {
                   Log.e("ERROR", t.toString());
               }
           });

       }catch (Exception ignored)
       {}
    }

    private void handleShowError(TextView tv, String msg) {
        tv.setVisibility(View.VISIBLE);
        tv.setText(msg);
        new Handler().postDelayed(() -> {
            tv.setText("");
            tv.setVisibility(View.GONE);
        }, 3000);

    }

    private void findViews(View v) {
        edt_pwd = v.findViewById(R.id.edt_pwd);
        edt_usr = v.findViewById(R.id.edt_usr);
        btn_login = v.findViewById(R.id.btn_login);
        tv_signUp = v.findViewById(R.id.tv_signUp);
        tv_errorPwd = v.findViewById(R.id.tv_errorPwd);
        img_visible = v.findViewById(R.id.img_visible);
        tv_errorUsr = v.findViewById(R.id.tv_errorUsr);
        img_visible.setImageResource(R.drawable.ic_baseline_visibility_off_24);

        tv_errorUsr.setVisibility(View.GONE);
        tv_errorPwd.setVisibility(View.GONE);
    }


}