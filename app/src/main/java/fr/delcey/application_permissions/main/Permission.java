package fr.delcey.application_permissions.main;

import android.support.annotation.DrawableRes;
import android.support.annotation.NonNull;
import android.support.annotation.StringRes;

/**
 * Created by Anthony "Nino" DELCEY on 20/08/18.
 */
public class Permission {
    
    private final String mManifestName;
    @StringRes
    private final int mHumanReadableName;
    @DrawableRes
    private final int mIcon;
    
    private boolean mIsSelected;
    
    Permission(@NonNull String manifestName, @StringRes int humanReadableName, @DrawableRes int icon) {
        mManifestName = manifestName;
        mHumanReadableName = humanReadableName;
        mIcon = icon;
    }
    
    @NonNull
    public String getManifestName() {
        return mManifestName;
    }
    
    @StringRes
    public int getHumanReadableName() {
        return mHumanReadableName;
    }
    
    @DrawableRes
    public int getIcon() {
        return mIcon;
    }
    
    public void setSelected(boolean selected) {
        mIsSelected = selected;
    }
    
    public boolean isSelected() {
        return mIsSelected;
    }
}
