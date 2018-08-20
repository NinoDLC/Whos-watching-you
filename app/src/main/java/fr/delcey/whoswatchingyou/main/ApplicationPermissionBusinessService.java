package fr.delcey.whoswatchingyou.main;

import android.Manifest.permission;
import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Build.VERSION;
import android.os.Build.VERSION_CODES;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import fr.delcey.whoswatchingyou.R;
import fr.delcey.whoswatchingyou.select_permissions.SelectedPermissionsSharedPreferences;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Created by Anthony "Nino" DELCEY on 20/08/18.
 */
public class ApplicationPermissionBusinessService {
    
    private static final List<Permission> PERMISSION_LIST = Collections.unmodifiableList(new ArrayList<Permission>() {
        {
            // Camera
            add(new Permission(permission.CAMERA,
                               R.string.permission_camera,
                               R.drawable.ic_permission_camera));
            
            // Microphone
            add(new Permission(permission.RECORD_AUDIO,
                               R.string.permission_record_audio,
                               R.drawable.ic_permission_record_audio));
            
            // SMS
            add(new Permission(permission.SEND_SMS, R.string.permission_send_sms, R.drawable.ic_permission_send_sms));
            add(new Permission(permission.READ_SMS, R.string.permission_read_sms, R.drawable.ic_permission_read_sms));
            add(new Permission(permission.RECEIVE_SMS,
                               R.string.permission_receive_sms,
                               R.drawable.ic_permission_read_sms));
            add(new Permission(permission.RECEIVE_MMS,
                               R.string.permission_receive_mms,
                               R.drawable.ic_permission_receive_mms));
            
            // Calendar
            add(new Permission(permission.READ_CALENDAR,
                               R.string.permission_read_calendar,
                               R.drawable.ic_permission_calendar));
            add(new Permission(permission.WRITE_CALENDAR,
                               R.string.permission_write_calendar,
                               R.drawable.ic_permission_calendar));
            
            // Contacts
            add(new Permission(permission.READ_CONTACTS,
                               R.string.permission_read_contact,
                               R.drawable.ic_permission_contact));
            add(new Permission(permission.WRITE_CONTACTS,
                               R.string.permission_write_contact,
                               R.drawable.ic_permission_contact));
            
            // GPS
            add(new Permission(permission.ACCESS_FINE_LOCATION,
                               R.string.permission_fine_location,
                               R.drawable.ic_permission_fine_location));
            add(new Permission(permission.ACCESS_COARSE_LOCATION,
                               R.string.permission_coarse_location,
                               R.drawable.ic_permission_coarse_location));
            
            // Body sensors
            if (VERSION.SDK_INT >= VERSION_CODES.KITKAT_WATCH) {
                add(new Permission(permission.BODY_SENSORS,
                                   R.string.permission_body_sensors,
                                   R.drawable.ic_permission_body_sensors));
            }
            
            // Phone
            add(new Permission(permission.READ_PHONE_STATE,
                               R.string.permission_read_phone_state,
                               R.drawable.ic_permission_read_phone_state));
            if (VERSION.SDK_INT >= VERSION_CODES.O) {
                add(new Permission(permission.READ_PHONE_NUMBERS,
                                   R.string.permission_read_phone_numbers,
                                   R.drawable.ic_permission_read_phone_state));
            }
            add(new Permission(permission.CALL_PHONE,
                               R.string.permission_call_phone,
                               R.drawable.ic_permission_call_phone));
            if (VERSION.SDK_INT >= VERSION_CODES.O) {
                add(new Permission(permission.ANSWER_PHONE_CALLS,
                                   R.string.permission_answer_phone_calls,
                                   R.drawable.ic_permission_answer_phone_calls));
            }
            add(new Permission(permission.ADD_VOICEMAIL,
                               R.string.permission_add_voicemail,
                               R.drawable.ic_permission_add_voicemail));
            add(new Permission(permission.USE_SIP, R.string.permission_use_sip, R.drawable.ic_permission_use_sip));
        }
    });
    
    public List<Permission> getListOfDangerousPermissions(Context context) {
        SelectedPermissionsSharedPreferences prefs = new SelectedPermissionsSharedPreferences();
        
        Collection<String> selectedPermissions = prefs.getSelectedPermissions(context);
        
        boolean atLeastOneSelected = false;
        
        for (Permission permission : PERMISSION_LIST) {
            boolean found = false;
            
            for (String selectedPermission : selectedPermissions) {
                if (permission.getManifestName().equalsIgnoreCase(selectedPermission)) {
                    permission.setSelected(true);
                    found = true;
                    atLeastOneSelected = true;
                    
                    break;
                }
            }
            
            if (!found) {
                permission.setSelected(false);
            }
        }
        
        if (!atLeastOneSelected) {
            for (Permission permission : PERMISSION_LIST) {
                permission.setSelected(true);
            }
        }
        
        return PERMISSION_LIST;
    }
    
    public void setSelectedApplicationPermissions(Context context, List<Permission> permissionList) {
        SelectedPermissionsSharedPreferences prefs = new SelectedPermissionsSharedPreferences();
        
        Set<String> permissions = new HashSet<>(permissionList.size());
        for (Permission permission : permissionList) {
            if (permission.isSelected()) {
                permissions.add(permission.getManifestName());
            }
        }
        
        prefs.setSelectedPermissions(context, permissions);
    }
    
    @NonNull
    List<ApplicationPermissions> getApplicationsPermissions(Context context) {
        List<ApplicationPermissions> result = new ArrayList<>();
        
        List<PackageInfo> installedPackages = context.getPackageManager().getInstalledPackages(0);
        List<Permission> selectedPermissions = getListOfDangerousPermissions(context);
        
        for (PackageInfo installedPackage : installedPackages) {
            if ((installedPackage.applicationInfo.flags & ApplicationInfo.FLAG_SYSTEM) == 0) {
                ApplicationPermissions applicationPermissions = getApplicationsPermissionIfNeeded(context,
                                                                                                  selectedPermissions,
                                                                                                  installedPackage);
                
                if (applicationPermissions != null) {
                    result.add(applicationPermissions);
                }
            }
        }
        
        return result;
    }
    
    @Nullable
    private ApplicationPermissions getApplicationsPermissionIfNeeded(Context context,
                                                                     List<Permission> selectedPermissions,
                                                                     PackageInfo installedPackage) {
        try {
            PackageInfo pi = context.getPackageManager().getPackageInfo(installedPackage.packageName,
                                                                        PackageManager.GET_PERMISSIONS);
            
            List<Permission> permissionsForThatApp = new ArrayList<>();
            
            for (int i = 0; i < pi.requestedPermissions.length; i++) {
                if ((pi.requestedPermissionsFlags[i] & PackageInfo.REQUESTED_PERMISSION_GRANTED) != 0) {
                    for (Permission permission : selectedPermissions) {
                        if (permission.isSelected()
                            && permission.getManifestName().equalsIgnoreCase(pi.requestedPermissions[i])) {
                            permissionsForThatApp.add(permission);
                        }
                    }
                }
            }
            
            if (permissionsForThatApp.size() >= 1) {
                return new ApplicationPermissions(installedPackage.packageName,
                                                  getAppNameFromPackageName(context, installedPackage.packageName),
                                                  permissionsForThatApp);
            }
        } catch (Exception ignored) {
        }
        
        return null;
    }
    
    @NonNull
    private String getAppNameFromPackageName(Context context, String packagename) {
        try {
            ApplicationInfo info = context.getPackageManager()
                                          .getApplicationInfo(packagename, PackageManager.GET_META_DATA);
            return (String) context.getPackageManager().getApplicationLabel(info);
        } catch (PackageManager.NameNotFoundException ignored) {
            return "Package inconnu";
        }
    }
}
