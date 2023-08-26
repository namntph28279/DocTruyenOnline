package com.example.myapplication.adapter;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.widget.AppCompatButton;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myapplication.MainActivity;

import com.example.myapplication.R;
import com.example.myapplication.api_service.CommentService;
import com.example.myapplication.model.Comment;
import com.example.myapplication.utils.AppPreferences;
import com.example.myapplication.utils.ConvertTime;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.squareup.picasso.Picasso;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class CommentAdapter extends RecyclerView.Adapter<CommentAdapter.ViewHolder> {
    private Context context;
    private List<Comment> list;
    private Dialog dialog;
    private ImageView img_closeDialog;
    private EditText edit_text;
    private TextView tv_title;
    public static boolean isUpdate = false;
    private AppCompatButton btn_sendComment;

    public CommentAdapter(Context context, List<Comment> list) {
        this.context = context;
        this.list = list;
        notifyDataSetChanged();
    }


    public void updateCommentData(List<Comment> newList) {
        list.clear();
        list.addAll(newList);
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_comment, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        int i = position;
        Comment comment = list.get(i);
        String loggedInUserId = AppPreferences.getKeyIdUser(context);

        holder.tv_dataComment.setText(comment.getData());

        if (comment.getId_user().get_id().equals(loggedInUserId)) {
            holder.tv_nameUser.setText("Bạn");
            holder.tv_delete.setVisibility(View.VISIBLE);
            holder.tv_edit.setVisibility(View.VISIBLE);
            holder.tv_edit.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    updateComment(i, comment);
                }
            });
            holder.tv_delete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    showDeleteDialog(comment);
                }
            });
        } else {
            holder.tv_nameUser.setText(comment.getId_user().getFullName());
        }

        Picasso.get().load(comment.getId_user().getAvatar()).into(holder.img_avatarUser);
        holder.tv_timeComment.setText(ConvertTime.convertTimestampToVietnameseTime(comment.getTime()));
    }

    private void updateComment(int position, Comment comment) {
        dialog = new Dialog(context);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialog_write_comment);
        img_closeDialog = dialog.findViewById(R.id.img_closeDialog);
        tv_title = dialog.findViewById(R.id.tv_title);

        edit_text = dialog.findViewById(R.id.edit_text);
        btn_sendComment = dialog.findViewById(R.id.btn_sendComment);
        tv_title.setText("Sửa bình luận");
        img_closeDialog.setOnClickListener(v1 -> {
            dialog.dismiss();
        });
        btn_sendComment.setOnClickListener(view -> {
            Gson gson = new GsonBuilder().setLenient().create();
            Retrofit retrofit = new Retrofit.Builder()
                    .baseUrl(MainActivity.BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create(gson))
                    .build();

            CommentService commentService = retrofit.create(CommentService.class);

            Call<List<Comment>> call = commentService.updateComment(comment.get_id(), new Comment(edit_text.getText().toString()));
            call.enqueue(new Callback<List<Comment>>() {
                @Override
                public void onResponse(Call<List<Comment>> call, Response<List<Comment>> response) {
                    if (response.isSuccessful()) {
                        List<Comment> updatedComments = response.body();
                        if (updatedComments != null && !updatedComments.isEmpty()) {
                            Comment updatedComment = updatedComments.get(0);
                            ((Activity) context).onBackPressed();

                            Log.e("TAG", "Updated Comment Data: " + updatedComment.getData());

                        } else {
                            Log.e("TAG", "Response body is null or empty");
                        }
                    } else {
                        Log.e("TAG", "Response is not successful: " + response.message());
                    }
                }

                @Override
                public void onFailure(Call<List<Comment>> call, Throwable t) {

                }
            });

            dialog.dismiss();

        });
        dialog.show();
        dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.getWindow().setGravity(Gravity.BOTTOM);
    }

    private void deleteComment(Comment comment) {
        Gson gson = new GsonBuilder().setLenient().create();
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(MainActivity.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build();

        CommentService commentService = retrofit.create(CommentService.class);
        Call<Void> call = commentService.deleteComment(comment.get_id());
        call.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if (response.isSuccessful()) {
                    list.remove(comment);
                    notifyDataSetChanged();
                    Toast.makeText(context, "User deleted successfully", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(context, "Failed to delete user", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                Toast.makeText(context, "Failed to delete user", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void showDeleteDialog(Comment comment) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle("Delete User");
        builder.setMessage("Are you sure you want to delete this comment?");

        builder.setPositiveButton("Delete", (dialog, which) -> {
            deleteComment(comment);
        });

        builder.setNegativeButton("Cancel", (dialog, which) -> dialog.dismiss());

        AlertDialog dialog = builder.create();
        dialog.show();
    }


    @Override
    public int getItemCount() {
        return list.size();
    }


    public static class ViewHolder extends RecyclerView.ViewHolder {
        ImageView img_avatarUser;
        TextView tv_dataComment, tv_nameUser, tv_timeComment, tv_edit, tv_delete;
        LinearLayout layout_comment;

        public ViewHolder(View itemView) {
            super(itemView);
            img_avatarUser = itemView.findViewById(R.id.img_avatarUser);
            tv_dataComment = itemView.findViewById(R.id.tv_dataComment);
            tv_nameUser = itemView.findViewById(R.id.tv_nameUser);
            tv_timeComment = itemView.findViewById(R.id.tv_timeComment);
            layout_comment = itemView.findViewById(R.id.layout_comment);
            tv_edit = itemView.findViewById(R.id.tv_edit);
            tv_delete = itemView.findViewById(R.id.tv_delete);
        }
    }
}
