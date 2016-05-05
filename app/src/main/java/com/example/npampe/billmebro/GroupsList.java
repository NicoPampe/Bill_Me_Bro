package com.example.npampe.billmebro;

import android.content.Context;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/** Static class containing all groups associated with a user.
 *
 */
public class GroupsList {
    private static GroupsList sGroupManager;
    List<Group> mGroups;
    private Context mContext;

    public static GroupsList get(Context context) {
        if (sGroupManager == null) {
            sGroupManager = new GroupsList(context);
        }
        return sGroupManager;
    }

    private GroupsList(Context context) {
        mContext = context.getApplicationContext();
        mGroups = new ArrayList<Group>();
    }

    public void addGroup(Group g) {
        mGroups.add(g);
    }

    public void deleteGroup(UUID id) {
        mGroups.remove(getGroup(id));
    }

    public void updateGroup(Group g) {
        Group old_crime = getGroup(g.getId());
        old_crime = g;
    }

    public List<Group> getGroups() {
        return mGroups;
    }

//    public File getPhotoFile(Group g) {
//        File externalFilesDir = mContext
//                .getExternalFilesDir(Environment.DIRECTORY_PICTURES);
//        if (externalFilesDir == null || g == null) {
//            return null;
//        }
//        return new File(externalFilesDir, g.getPhotoFilename());
//    }

    public Group getGroup(UUID id) {
        for (Group g : mGroups) {
            if (g.getId().equals(id)) {
                return g;
            }
        }
        return null;
    }
}
