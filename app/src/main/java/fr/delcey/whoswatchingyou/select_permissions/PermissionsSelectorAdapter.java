package fr.delcey.whoswatchingyou.select_permissions;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.RecyclerView.Adapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.ImageView;
import android.widget.TextView;
import fr.delcey.whoswatchingyou.R;
import fr.delcey.whoswatchingyou.main.Permission;
import java.util.List;

/**
 * Created by Anthony "Nino" DELCEY on 20/08/18.
 */
class PermissionsSelectorAdapter extends Adapter<PermissionsSelectorAdapter.ViewHolder> {
    
    private final List<Permission> mPermissions;
    
    PermissionsSelectorAdapter(List<Permission> data) {
        mPermissions = data;
    }
    
    public List<Permission> getPermissionList() {
        return mPermissions;
    }
    
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(parent.getContext())
                                            .inflate(R.layout.dialog_select_permission_rv_item, parent, false));
    }
    
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.bind(mPermissions.get(position));
    }
    
    @Override
    public int getItemCount() {
        return mPermissions.size();
    }
    
    public class ViewHolder extends RecyclerView.ViewHolder implements OnCheckedChangeListener, OnClickListener {
    
        private CheckBox mCheckBox;
        private ImageView mIcon;
        private TextView mPermissionName;
    
        ViewHolder(View itemView) {
            super(itemView);
    
            mCheckBox = itemView.findViewById(R.id.dialog_selected_permission_item_cb);
            mIcon = itemView.findViewById(R.id.dialog_selected_permission_item_iv);
            mPermissionName = itemView.findViewById(R.id.dialog_selected_permission_item_tv);
        }
    
        void bind(Permission permission) {
            itemView.setOnClickListener(this);
            
            mCheckBox.setOnCheckedChangeListener(this);
            mCheckBox.setChecked(permission.isSelected());
            
            mIcon.setImageResource(permission.getIcon());
    
            mPermissionName.setText(permission.getHumanReadableName());
        }
    
        @Override
        public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
            mPermissions.get(getAdapterPosition()).setSelected(isChecked);
        }
    
        @Override
        public void onClick(View v) {
            mCheckBox.setChecked(!mCheckBox.isChecked());
        }
    }
}
