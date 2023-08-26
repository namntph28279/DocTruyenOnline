package com.example.myapplication.fragment.auth;

import android.annotation.SuppressLint;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatButton;
import androidx.fragment.app.Fragment;

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
import com.example.myapplication.model.User;
import com.example.myapplication.utils.LoadingDialog;
import com.example.myapplication.utils.Validator;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;


public class RegisterFragment extends Fragment {
    private EditText edt_usr, edt_pwd, edt_rePwd;
    private ImageView img_visible, img_rePwdVisible;
    private AppCompatButton btn_signUp;
    private TextView tv_login;
    private final String TAG="REGISTER";
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_register, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        findViews(view);
        tv_login.setOnClickListener(view1 -> getActivity().getSupportFragmentManager().popBackStack());
        btn_signUp.setOnClickListener(view1 -> handleSignUp(
                edt_usr.getText().toString(),
                edt_pwd.getText().toString(),
                edt_rePwd.getText().toString()));
        img_visible.setOnClickListener(view1 -> togglePasswordVisibility());
        img_rePwdVisible.setOnClickListener(view1 -> toggleRePasswordVisibility());
    }

    private void registerResponse(User user) {
        //Tạo đối tượng chuyển đối kiểu dữ liệu
        Gson gson = new GsonBuilder().setLenient().create();
        // Tạo retrofit
        Retrofit retrofit = new Retrofit.Builder().baseUrl(MainActivity.BASE_URL).addConverterFactory(GsonConverterFactory.create(gson)).build();
        //khởi tạo Interface
        AuthService authService = retrofit.create(AuthService.class);
        //tạo call
        Call<User> objCall = authService.register(user);
        Log.e(TAG, "registerResponse: "+objCall.toString() );
        //thực hiện gửi dữ liệu lên server
        objCall.enqueue(new Callback<>() {
            @Override
            public void onResponse(Call<User> call, Response<User> response) {
                //kết quả server trả về
                if (response.isSuccessful()) {
                    //lấy kết quả trả về
                    getActivity().getSupportFragmentManager().popBackStack();
                    Toast.makeText(getContext(), "Đăng ký thành công", Toast.LENGTH_SHORT).show();
                }
                LoadingDialog.dialogLoading.dismiss();
            }


            @Override
            public void onFailure(Call<User> call, Throwable t) {
                Log.e("ERROR", t.toString());
            }
        });
    }
    @SuppressLint("ResourceAsColor")
    private void findViews(View v) {
        tv_login = v.findViewById(R.id.tv_login);
        edt_usr = v.findViewById(R.id.edt_usr);
        edt_pwd = v.findViewById(R.id.edt_pwd);
        edt_rePwd = v.findViewById(R.id.edt_rePwd);
        btn_signUp = v.findViewById(R.id.btn_signUp);
        img_rePwdVisible = v.findViewById(R.id.img_rePwdVisible);
        img_visible = v.findViewById(R.id.img_visible);

        img_visible.setImageResource(R.drawable.ic_baseline_visibility_off_24);
        img_rePwdVisible.setImageResource(R.drawable.ic_baseline_visibility_off_24);

        edt_pwd.setTextColor(R.color.white);
        edt_pwd.setTextColor(R.color.white);

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

    private void toggleRePasswordVisibility() {
        int currentInputType = edt_rePwd.getInputType();
        if ((currentInputType == (InputType.TYPE_TEXT_VARIATION_PASSWORD | InputType.TYPE_CLASS_TEXT))) {
            edt_rePwd.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
            img_rePwdVisible.setImageResource(R.drawable.ic_baseline_visibility_24);
        } else {
            edt_rePwd.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
            img_rePwdVisible.setImageResource(R.drawable.ic_baseline_visibility_off_24);
        }
        edt_rePwd.setSelection(edt_rePwd.getText().length());
    }

    private void handleSignUp(String username, String password, String rePassword) {

        switch (Validator.validateRegisterInputs(username, password, rePassword)) {
            case "empty" ->
                    Toast.makeText(getContext(), "Vui lòng không để trống", Toast.LENGTH_LONG).show();
            case "pwd_invalid" -> Toast.makeText(
                    getContext(),
                    "Mật khẩu tối thiểu 6 kí tự",
                    Toast.LENGTH_LONG
            ).show();
            case "re_pwd_incorrect" -> Toast.makeText(
                    getContext(),
                    "Mật khẩu nhập lại không đúng",
                    Toast.LENGTH_LONG
            ).show();
            case "success" ->
                registerResponse(new User(username,password,1));

        }

    }
}