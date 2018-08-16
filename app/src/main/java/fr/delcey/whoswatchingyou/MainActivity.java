package fr.delcey.whoswatchingyou;

import android.Manifest;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements ApplicationsAdapter.Listener {
    
    private RecyclerView mRecyclerView;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        setContentView(R.layout.activity_main);
        
        mRecyclerView = findViewById(R.id.main_rv);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
    }
    
    @Override
    protected void onResume() {
        super.onResume();
    
        mRecyclerView.setAdapter(new ApplicationsAdapter(this, getApplicationsPermissions()));
    }
    
    @NonNull
    private List<ApplicationPermissions> getApplicationsPermissions() {
        List<ApplicationPermissions> result = new ArrayList<>();
        
        List<PackageInfo> installedPackages = getPackageManager().getInstalledPackages(0);
        
        for (PackageInfo installedPackage : installedPackages) {
            if ((installedPackage.applicationInfo.flags & ApplicationInfo.FLAG_SYSTEM) == 0) {
                ApplicationPermissions applicationPermissions = getApplicationsPermissionIfNeeded(installedPackage);
                
                if (applicationPermissions != null) {
                    result.add(applicationPermissions);
                }
            }
        }
        
        return result;
    }
    
    @Nullable
    private ApplicationPermissions getApplicationsPermissionIfNeeded(PackageInfo installedPackage) {
        try {
            PackageInfo pi = getPackageManager().getPackageInfo(installedPackage.packageName,
                                                                PackageManager.GET_PERMISSIONS);
            
            boolean accessCamera = false;
            boolean accessAudio = false;
            
            for (int i = 0; i < pi.requestedPermissions.length; i++) {
                if ((pi.requestedPermissionsFlags[i] & PackageInfo.REQUESTED_PERMISSION_GRANTED) != 0) {
                    switch (pi.requestedPermissions[i]) {
                        case Manifest.permission.CAMERA:
                            accessCamera = true;
                            break;
                        case Manifest.permission.RECORD_AUDIO:
                            accessAudio = true;
                            break;
                    }
                }
            }
            
            if (accessCamera || accessAudio) {
                return new ApplicationPermissions(installedPackage.packageName,
                                                  getAppNameFromPackageName(installedPackage.packageName),
                                                  accessCamera,
                                                  accessAudio);
            }
        } catch (Exception ignored) {
        }
        
        return null;
    }
    
    @NonNull
    public String getAppNameFromPackageName(String packagename) {
        try {
            ApplicationInfo info = getPackageManager().getApplicationInfo(packagename, PackageManager.GET_META_DATA);
            return (String) getPackageManager().getApplicationLabel(info);
        } catch (PackageManager.NameNotFoundException ignored) {
            return "Package inconnu";
        }
    }
    
    @Override
    public void onApplicationClicked(ApplicationPermissions applicationPermissions) {
        Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        Uri uri = Uri.fromParts("package", applicationPermissions.getPackageName(), null);
        intent.setData(uri);
        startActivity(intent);
    }
}
