package fr.delcey.whoswatchingyou.select_permissions;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.DialogInterface.OnShowListener;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import fr.delcey.whoswatchingyou.R;
import fr.delcey.whoswatchingyou.main.ApplicationPermissionBusinessService;
import fr.delcey.whoswatchingyou.main.Permission;
import java.util.List;

/**
 * Created by Anthony "Nino" DELCEY on 20/08/18.
 */
public class PermissionsSelectorDialog extends DialogFragment {
    
    private ApplicationPermissionBusinessService mBusinessService = new ApplicationPermissionBusinessService();
    
    private Listener mListener;
    private List<Permission> mData;
    
    private RecyclerView mRecyclerView;
    private PermissionsSelectorAdapter mAdapter;
    
    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        
        mListener = (Listener) context;
    }
    
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        mData = mBusinessService.getListOfDangerousPermissions(getContext());
    }
    
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        mAdapter = new PermissionsSelectorAdapter(mData);
        mRecyclerView.setAdapter(mAdapter);
        
        return null;
    }
    
    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        View view = getActivity().getLayoutInflater().inflate(R.layout.dialog_select_permission, null);
        
        mRecyclerView = view.findViewById(R.id.dialog_selected_permission_rv);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        
        builder.setTitle(R.string.main_dialog_select_permissions)
               .setView(view)
               .setPositiveButton(android.R.string.ok, new OnClickListener() {
                   public void onClick(DialogInterface dialog, int id) {
                       mBusinessService.setSelectedApplicationPermissions(getContext(), mAdapter.getPermissionList());
                
                       mListener.onSelectedPermissionsUpdated();
                       dismiss();
                   }
               })
               .setNeutralButton(R.string.all, null)
               .setNegativeButton(android.R.string.cancel, new OnClickListener() {
                   public void onClick(DialogInterface dialog, int id) {
                       dismiss();
                   }
               });
        
        AlertDialog dialog = builder.create();
        
        dialog.setOnShowListener(new OnShowListener() {
            @Override
            public void onShow(DialogInterface dialog) {
                Button button = ((AlertDialog) dialog).getButton(AlertDialog.BUTTON_NEUTRAL);
                button.setOnClickListener(new View.OnClickListener() {
                    private boolean isAllSelected;
                    
                    @Override
                    public void onClick(View v) {
                        isAllSelected = !isAllSelected;
                        
                        for (Permission permission : mData) {
                            permission.setSelected(isAllSelected);
                        }
                        
                        mAdapter.notifyDataSetChanged();
                    }
                });
            }
        });
        
        return dialog;
    }
    
    public interface Listener {
        
        void onSelectedPermissionsUpdated();
    }
}
