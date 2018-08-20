package fr.delcey.whoswatchingyou.select_permissions;

import android.content.Context;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

/**
 * Created by Anthony "Nino" DELCEY on 20/08/18.
 */
public class SelectedPermissionsSharedPreferences {
    
    private static final String SHARED_PREFERENCE_KEY = "SELECTED_PERMISSIONS_KEY";
    
    private static final String SELECTED_PERMISSION_LIST_KEY = "SELECTED_PERMISSION_LIST_KEY";
    
    public Collection<String> getSelectedPermissions(Context context) {
        return Collections.unmodifiableCollection(
            context.getSharedPreferences(SHARED_PREFERENCE_KEY, Context.MODE_PRIVATE)
                   .getStringSet(SELECTED_PERMISSION_LIST_KEY, new HashSet<String>()));
    }
    
    public void setSelectedPermissions(Context context, Set<String> selectedPermissions) {
        context.getSharedPreferences(SHARED_PREFERENCE_KEY, Context.MODE_PRIVATE)
               .edit()
               .putStringSet(SELECTED_PERMISSION_LIST_KEY, selectedPermissions)
               .apply();
    }
}
