package com.example.myapplication.fragment.profile.components;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;

import com.example.myapplication.MainActivity;
import com.example.myapplication.R;
import com.example.myapplication.api_service.AuthService;
import com.example.myapplication.model.User;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.squareup.picasso.Picasso;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;


public class ChangeProfileFragment extends Fragment {
    private Bundle bundle;
    private final String TAG = "ChangeProfileFragment";
    private ImageView mImgAvatar;
    private ImageView mEditAvatarButton;
    private ImageView img_Back;
    private EditText mEdtFullName;
    private EditText mEdtUsername;
    User argument;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        bundle = this.getArguments();

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_change_profile, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initView(view);

        if (argument != null) {
            mEdtFullName.setText(argument.getFullName());
            mEdtUsername.setText(argument.getUsername());
            Picasso.get().load(argument.getAvatar()).into(mImgAvatar);
        } else {
            Log.e(TAG, "Đối tượng argument là null");
        }

        User user = new User();
        user.setFullName(mEdtFullName.getText().toString());
        user.setUsername(mEdtUsername.getText().toString());
        img_Back.setOnClickListener(view1 -> {
            updateUser(new User(mEdtFullName.getText().toString(),mEdtUsername.getText().toString()));
            getActivity().getSupportFragmentManager().popBackStack();
        });

    }

    private void updateUser(User user) {
        //Tạo đối tượng chuyển đối kiểu dữ liệu
        Gson gson = new GsonBuilder().setLenient().create();
        // Tạo retrofit
        Retrofit retrofit = new Retrofit.Builder().baseUrl(MainActivity.BASE_URL).addConverterFactory(GsonConverterFactory.create(gson)).build();
        //khởi tạo Interface
        AuthService authService = retrofit.create(AuthService.class);
        if (argument != null) {
            Log.e(TAG, "updateUser"+argument.get_id()+"\n"+user );
            // Tiếp tục xử lý
        } else {
            Log.e(TAG, "Đối tượng argument là null");
        }
        //tạo call
        if (argument != null) {
            Call<User> objCall = authService.updateUser(argument.get_id(),user);

            //thực hiện gửi dữ liệu lên server
            objCall.enqueue(new Callback<>() {
                @Override
                public void onResponse(Call<User> call, Response<User> response) {
                    Log.e(TAG, "onResponse: " + response.body());
                }

                @Override
                public void onFailure(Call<User> call, Throwable t) {
                    Log.e("ERROR", t.toString());
                }
            });
        } else {
            Log.e(TAG, "Đối tượng argument là null");
        }



    }

    public void initView(View view) {
        mImgAvatar = view.findViewById(R.id.img_avatar);
        mEditAvatarButton = view.findViewById(R.id.editAvatarButton);
        mEdtUsername = view.findViewById(R.id.edt_username);
        mEdtFullName = view.findViewById(R.id.edt_fullName);
        img_Back = view.findViewById(R.id.img_Back);
        argument = (User) bundle.getSerializable("objectUser");

        // Kiểm tra 'argument' có bị null hay không
        if (argument != null) {
            // 'argument' không bị null, bạn có thể sử dụng nó ở đây
            Log.d(TAG, "Argument không bị null: " + argument.toString());

            // Tiếp tục thực hiện các thao tác khác liên quan đến 'argument'
        } else {
            // 'argument' bị null, in log để kiểm tra và xử lý tùy ý
            Log.e(TAG, "Argument bị null");
        }
    }
}