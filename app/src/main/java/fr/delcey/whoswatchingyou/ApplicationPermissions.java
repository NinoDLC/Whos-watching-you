package fr.delcey.whoswatchingyou;

/**
 * Created by Anthony "Nino" DELCEY on 16/08/18.
 */
class ApplicationPermissions {
    
    private String mPackageName;
    private String mApplicationName;
    private boolean mAccessCamera;
    private boolean mAccessAudio;
    
    ApplicationPermissions(String packageName, String applicationName, boolean accessCamera, boolean accessAudio) {
        mPackageName = packageName;
        mApplicationName = applicationName;
        mAccessCamera = accessCamera;
        mAccessAudio = accessAudio;
    }
    
    public String getPackageName() {
        return mPackageName;
    }
    
    public boolean isAccessCamera() {
        return mAccessCamera;
    }
    
    public boolean isAccessAudio() {
        return mAccessAudio;
    }
    
    public String getApplicationName() {
        return mApplicationName;
    }
}
