package com.evertrend.tiger.user.adapter;

import android.content.Context;
import android.graphics.Color;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.evertrend.tiger.user.R;
import com.evertrend.tiger.user.bean.event.UserOperationItemEvent;

import org.greenrobot.eventbus.EventBus;

public class UserOperationItemAdapter extends  RecyclerView.Adapter<UserOperationItemAdapter.ViewHolder>{
    private static final String TAG = UserOperationItemAdapter.class.getSimpleName();
    private String[] userOperationItems;
    private Context mContext;

    public UserOperationItemAdapter() {
    }

    public UserOperationItemAdapter(Context context, String[] userOperationItems) {
        mContext = context;
        this.userOperationItems = userOperationItems;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.yl_user_fragment_user_operation_item, parent, false);
        final UserOperationItemAdapter.ViewHolder viewHolder = new UserOperationItemAdapter.ViewHolder(view);

        viewHolder.itemViwe.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EventBus.getDefault().post(new UserOperationItemEvent(viewHolder.getAbsoluteAdapterPosition()));
            }
        });
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.itemName.setText(userOperationItems[position]);
        switch (position) {
            case 0:
                holder.itemImage.setImageResource(R.drawable.yl_user_ic_settings_blue_36dp);
                break;
            case 1:
                holder.itemImage.setImageResource(R.drawable.yl_user_ic_update_orange__36dp);
                break;
            case 2:
                holder.itemImage.setVisibility(View.GONE);
                holder.iv_right.setVisibility(View.GONE);
                holder.itemName.setTextColor(Color.RED);
                holder.itemName.setGravity(Gravity.CENTER);
                break;
        }
    }

    @Override
    public int getItemCount() {
        return userOperationItems.length;
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        TextView itemName;
        ImageView itemImage;
        ImageView iv_right;
        View itemViwe;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            this.itemViwe = itemView;
            itemName = itemView.findViewById(R.id.tv_name);
            itemImage = itemView.findViewById(R.id.iv_user_operation);
            iv_right = itemView.findViewById(R.id.iv_right);
        }
    }
}
