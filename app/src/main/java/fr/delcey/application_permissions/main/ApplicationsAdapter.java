package fr.delcey.application_permissions.main;

import android.graphics.PorterDuff;
import android.graphics.PorterDuffColorFilter;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.TextView;
import fr.delcey.application_permissions.R;
import java.util.List;

/**
 * Created by Anthony "Nino" DELCEY on 16/08/18.
 */
class ApplicationsAdapter extends RecyclerView.Adapter<ApplicationsAdapter.ViewHolder> {
    
    private final List<ApplicationPermissions> mApplicationPermissions;
    
    private final Listener mListener;
    
    ApplicationsAdapter(Listener listener, @NonNull List<ApplicationPermissions> applicationsPermissions) {
        mListener = listener;
        mApplicationPermissions = applicationsPermissions;
    }
    
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.main_rv_item, parent, false));
    }
    
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.bind(mApplicationPermissions.get(position));
    }
    
    @Override
    public int getItemCount() {
        return mApplicationPermissions.size();
    }
    
    class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        
        private TextView mTvTitle;
        private LinearLayout mLlIcons;
        
        ViewHolder(View itemView) {
            super(itemView);
            
            mTvTitle = itemView.findViewById(R.id.main_item_tv_title);
            mLlIcons = itemView.findViewById(R.id.main_item_ll_icons);
        }
        
        void bind(ApplicationPermissions applicationPermissions) {
            itemView.setOnClickListener(this);
            
            mTvTitle.setText(applicationPermissions.getApplicationName());
            for (Permission permission : applicationPermissions.getPermissions()) {
                ImageView view = new ImageView(itemView.getContext());
                
                LayoutParams params = new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
                params.setMargins(itemView.getResources().getDimensionPixelSize(R.dimen.default_margin), 0, 0, 0);
                view.setLayoutParams(params);
                
                view.setImageResource(permission.getIcon());
                
                mLlIcons.addView(view);
            }
        }
        
        @Override
        public void onClick(View v) {
            mListener.onApplicationClicked(mApplicationPermissions.get(getAdapterPosition()));
        }
    }
    
    interface Listener {
        
        void onApplicationClicked(ApplicationPermissions applicationPermissions);
    }
}
