package fr.delcey.application_permissions.main;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import fr.delcey.application_permissions.R;
import fr.delcey.application_permissions.select_permissions.PermissionsSelectorDialog;
import fr.delcey.application_permissions.select_permissions.PermissionsSelectorDialog.Listener;

public class MainActivity extends AppCompatActivity implements ApplicationsAdapter.Listener, Listener {
    
    private RecyclerView mRecyclerView;
    
    private ApplicationPermissionBusinessService mBusinessService = new ApplicationPermissionBusinessService();
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        setContentView(R.layout.activity_main);
        
        mRecyclerView = findViewById(R.id.main_rv);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
    }
    
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main_menu, menu);
        return true;
    }
    
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.main_menu_settings:
                displayPermissionSelectionDialog();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
    
    @Override
    protected void onResume() {
        super.onResume();
        
        refreshData();
    }
    
    private void refreshData() {
        mRecyclerView.setAdapter(new ApplicationsAdapter(this, mBusinessService.getApplicationsPermissions(this)));
    }
    
    private void displayPermissionSelectionDialog() {
        new PermissionsSelectorDialog().show(getSupportFragmentManager(), null);
    }
    
    @Override
    public void onApplicationClicked(ApplicationPermissions applicationPermissions) {
        Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        Uri uri = Uri.fromParts("package", applicationPermissions.getPackageName(), null);
        intent.setData(uri);
        startActivity(intent);
    }
    
    @Override
    public void onSelectedPermissionsUpdated() {
        refreshData();
    }
}
