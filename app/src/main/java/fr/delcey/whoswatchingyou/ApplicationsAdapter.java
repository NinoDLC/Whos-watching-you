package fr.delcey.whoswatchingyou;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
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
        private ImageView mIvAudio;
        private ImageView mIvCamera;
        
        ViewHolder(View itemView) {
            super(itemView);
            
            mTvTitle = itemView.findViewById(R.id.main_item_tv_title);
            mIvAudio = itemView.findViewById(R.id.main_item_iv_audio);
            mIvCamera = itemView.findViewById(R.id.main_item_iv_camera);
        }
        
        void bind(ApplicationPermissions applicationPermissions) {
            itemView.setOnClickListener(this);
            
            mTvTitle.setText(applicationPermissions.getApplicationName());
            mIvAudio.setVisibility(applicationPermissions.isAccessAudio() ? View.VISIBLE : View.GONE);
            mIvCamera.setVisibility(applicationPermissions.isAccessCamera() ? View.VISIBLE : View.GONE);
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
