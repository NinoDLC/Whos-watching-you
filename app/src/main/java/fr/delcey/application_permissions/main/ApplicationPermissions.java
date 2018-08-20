package fr.delcey.application_permissions.main;

import java.util.List;

/**
 * Created by Anthony "Nino" DELCEY on 16/08/18.
 */
public class ApplicationPermissions {
    
    private String mPackageName;
    private String mApplicationName;
    private List<Permission> mPermissions;
    
    ApplicationPermissions(String packageName, String applicationName, List<Permission> permissions) {
        mPackageName = packageName;
        mApplicationName = applicationName;
        mPermissions = permissions;
    }
    
    public String getPackageName() {
        return mPackageName;
    }
    
    public List<Permission> getPermissions() {
        return mPermissions;
    }
    
    public String getApplicationName() {
        return mApplicationName;
    }
}
