package br.com.octabus.Util;

import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.res.AssetManager;
import android.content.res.XmlResourceParser;
import android.os.Build;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by marcatti on 09/04/17.
 */

public class PermissionApp {

    public static boolean checkPermissionsManifest(Activity activity, Context context)
    {
        List _permissions = getListOfPermissions(context);

        List<String> listPermissionsNeeded = new ArrayList<>();

        for (int i=0; i<_permissions.size(); i++) {
            String permission = _permissions.get(i).toString();

            if(!checkIfAlreadyHavePermission(context, permission))
            {
                listPermissionsNeeded.add(permission);
            }
        }

        if(listPermissionsNeeded.size() > 0)
        {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                ActivityCompat.requestPermissions(activity, listPermissionsNeeded.toArray
                        (new String[listPermissionsNeeded.size()]), 1);
                return false;
            }
        }

        return true;
    }



    /**/
    private static boolean checkIfAlreadyHavePermission(Context context, String permission) {
        int result = ContextCompat.checkSelfPermission(context, permission);
        if (result == PackageManager.PERMISSION_GRANTED) {
            return true;
        } else {
            return false;
        }
    }

    public static boolean hasPermissionInManifest(Context context, String permissionName) {

        final String packageName = context.getPackageName();
        try {
            final PackageInfo packageInfo = context.getPackageManager()
                    .getPackageInfo(packageName, PackageManager.GET_PERMISSIONS);
            final String[] declaredPermisisons = packageInfo.requestedPermissions;
            if (declaredPermisisons != null && declaredPermisisons.length > 0) {
                for (String p : declaredPermisisons) {
                    if (p.equals(permissionName)) {
                        return true;
                    }
                }
            }
        } catch (PackageManager.NameNotFoundException e) {

        }
        return false;
    }

    public static List getListOfPermissions(final Context context)
    {
        List _permissions = new ArrayList();
        try
        {
            final AssetManager _am = context.createPackageContext(context.getPackageName(), 0).getAssets();
            final XmlResourceParser _xmlParser = _am.openXmlResourceParser(0, "AndroidManifest.xml");
            int _eventType = _xmlParser.getEventType();
            while (_eventType != XmlPullParser.END_DOCUMENT)
            {
                if ((_eventType == XmlPullParser.START_TAG) && "uses-permission".equals(_xmlParser.getName()))
                {
                    for (byte i = 0; i < _xmlParser.getAttributeCount(); i ++)
                    {
                        if (_xmlParser.getAttributeName(i).equals("name"))
                        {
                            _permissions.add(_xmlParser.getAttributeValue(i));
                        }
                    }
                }
                _eventType = _xmlParser.nextToken();
            }
            _xmlParser.close(); // Pervents memory leak.
        }
        catch (final XmlPullParserException exception)
        {
            exception.printStackTrace();
        }
        catch (final PackageManager.NameNotFoundException exception)
        {
            exception.printStackTrace();
        }
        catch (final IOException exception)
        {
            exception.printStackTrace();
        }

        return _permissions;
    }
}
