package com.example.myapplication.fragment.profile;

import android.app.Dialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatButton;
import androidx.fragment.app.Fragment;

import android.os.Handler;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.myapplication.MainActivity;
import com.example.myapplication.R;
import com.example.myapplication.api_service.AuthService;
import com.example.myapplication.fragment.auth.LoginFragment;
import com.example.myapplication.fragment.profile.components.ChangeProfileFragment;
import com.example.myapplication.model.User;
import com.example.myapplication.utils.AppPreferences;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.squareup.picasso.Picasso;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;


public class ProfileFragment extends Fragment {

    private Dialog dialog;
    private RelativeLayout mEditProfile;
    private ImageView mImgAvatar, close_dialog;
    private LinearLayout mLayoutChangePassword;
    private LinearLayout mLayoutLogOut;

    private TextView tv_fullName, tv_errorEditFullName;

    private EditText edt_oldPassword, edt_newPassword, edt_rePassword;
    private AppCompatButton btn_savePassword;
    private final String TAG = "PROFILE";

    private ChangeProfileFragment changeProfileFragment;

    private Bundle bundle;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_profile, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        findViews(view);
        fetchDataFromApi();

        mEditProfile.setOnClickListener(view1 -> {
            changeProfileFragment.setArguments(bundle);
            getActivity().getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.content_frame, changeProfileFragment)
                    .addToBackStack("")
                    .commit();
        });
        mLayoutLogOut.setOnClickListener(view1 -> {
            getActivity().getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.layout_container, new LoginFragment())
                    .commitNow();

        });
        mLayoutChangePassword.setOnClickListener(view1 -> showDiaLog());
    }

    private void showDiaLog() {
        dialog = new Dialog(getContext());
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialog_change_password);
        close_dialog = dialog.findViewById(R.id.img_closeDialog);
        edt_oldPassword = dialog.findViewById(R.id.edt_oldPassword);
        edt_newPassword = dialog.findViewById(R.id.edt_newPassword);
        edt_rePassword = dialog.findViewById(R.id.edt_rePassword);
        btn_savePassword = dialog.findViewById(R.id.btn_savePassword);
        tv_errorEditFullName = dialog.findViewById(R.id.tv_errorEditFullName);
        close_dialog.setOnClickListener(v1 -> {
            dialog.dismiss();
        });

        btn_savePassword.setOnClickListener(v1 -> {
            if (edt_oldPassword.getText().toString().isEmpty() || edt_rePassword.getText().toString().isEmpty() || edt_newPassword.getText().toString().isEmpty()) {
                tv_errorEditFullName.setText("Vui lòng không để trống");
                new Handler().postDelayed(() -> tv_errorEditFullName.setText(""), 3000);
            } else {
                if (!edt_newPassword.getText().toString().equals(edt_rePassword.getText().toString())) {
                    tv_errorEditFullName.setText("Mật khẩu nhập lại không đúng");
                    new Handler().postDelayed(() -> tv_errorEditFullName.setText(""), 3000);
                } else {
                    String _id = AppPreferences.getKeyIdUser(getContext());
                    Gson gson = new GsonBuilder().setLenient().create();
                    // Tạo retrofit
                    Retrofit retrofit = new Retrofit.Builder().baseUrl(MainActivity.BASE_URL).addConverterFactory(GsonConverterFactory.create(gson)).build();
                    //khởi tạo Interface
                    AuthService authService = retrofit.create(AuthService.class);
                    Log.e(TAG, "updateUser" + _id + "\n");
                    //tạo call
                    Call<User> objCall = authService.updateUser(_id, new User(edt_newPassword.getText().toString()));

                    //thực hiện gửi dữ liệu lên server
                    objCall.enqueue(new Callback<>() {
                        @Override
                        public void onResponse(Call<User> call, Response<User> response) {
                            Log.e(TAG, "onResponse: " + response.body());
                            Toast.makeText(getContext(), "Thay đổi mật khẩu thành công", Toast.LENGTH_SHORT).show();
                        }


                        @Override
                        public void onFailure(Call<User> call, Throwable t) {
                            Log.e("ERROR", t.toString());
                        }
                    });
                    dialog.dismiss();
                }
            }
        });


        dialog.show();
        dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.getWindow().setGravity(Gravity.BOTTOM);
    }

    private void findViews(View view) {
        mEditProfile = view.findViewById(R.id.editProfile);
        mImgAvatar = view.findViewById(R.id.img_avatar);
        mLayoutChangePassword = view.findViewById(R.id.layout_changePassword);
        mLayoutLogOut = view.findViewById(R.id.layout_logOut);
        tv_fullName = view.findViewById(R.id.tv_fullName);
        bundle = new Bundle();
        changeProfileFragment = new ChangeProfileFragment();

    }


    public void fetchDataFromApi() {
        String _id = AppPreferences.getKeyIdUser(getContext());
        Gson gson = new GsonBuilder().setLenient().create();
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(MainActivity.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build();

        AuthService authService = retrofit.create(AuthService.class);

        Call<User> call = authService.getUserById(_id);

        call.enqueue(new Callback<User>() {
            @Override
            public void onResponse(Call<User> call, Response<User> response) {
                if (response.isSuccessful() && response.body() != null) {
                    User user = response.body();
                    Picasso.get().load(user.getAvatar()).into(mImgAvatar);
                    tv_fullName.setText(user.getFullName());
                    bundle.putSerializable("objectUser", user);
                }
            }

            @Override
            public void onFailure(Call<User> call, Throwable t) {
                Log.e(TAG, "onFailure: " + t.getMessage());
            }
        });


    }
}
