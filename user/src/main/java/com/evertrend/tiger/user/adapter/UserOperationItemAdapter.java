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
import com.evertrend.tiger.user.bean.OperationItem;
import com.evertrend.tiger.user.bean.event.UserOperationItemEvent;

import org.greenrobot.eventbus.EventBus;

import java.util.List;

public class UserOperationItemAdapter extends  RecyclerView.Adapter<UserOperationItemAdapter.ViewHolder>{
    private static final String TAG = UserOperationItemAdapter.class.getSimpleName();
    private List<OperationItem> itemList;
    private Context mContext;

    public UserOperationItemAdapter() {
    }

    public UserOperationItemAdapter(Context context, List<OperationItem> itemList) {
        mContext = context;
        this.itemList = itemList;
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
        holder.itemName.setText(itemList.get(position).getName());
        holder.itemImage.setImageResource(itemList.get(position).getImageId());
        if (itemList.get(position).isShowCenter()) {
            holder.itemImage.setVisibility(View.GONE);
            holder.iv_right.setVisibility(View.GONE);
            holder.itemName.setTextColor(Color.RED);
            holder.itemName.setGravity(Gravity.CENTER);
        }
    }

    @Override
    public int getItemCount() {
        return itemList.size();
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
