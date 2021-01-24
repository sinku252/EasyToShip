package com.tws.courier.domain.utils;

import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

public class PermissionUtils {

    // Make sure you have added the permission in manifest file! It's necessary (^..^)

    public static final int REQ_PERMISSION_EXTERNAL_STORAGE = 49;
    public static final int REQ_PERMISSION_READ_EXTERNAL_STORAGE = 50;
    public static final int REQ_PERMISSION_WRITE_EXTERNAL_STORAGE = 51;
    public static final int REQ_PERMISSION_ACCESS_FINE_LOCATION = 52;

    private static final String MSG_RATIONAL_READ_EXTERNAL_STORAGE =
            "Read storage permission is needed for core functionality";
    private static final String MSG_RATIONAL_WRITE_EXTERNAL_STORAGE =
            "Write storage permission is needed for core functionality";
    private static final String MSG_RATIONAL_STORAGE =
            "Read and write storage permission is needed for core functionality";
    private static final String MSG_RATIONAL_ACCESS_FINE_LOCATION =
            "Location permission is needed for core functionality";

    private AppCompatActivity activity;
    private Fragment fragment;

    private WriteExternalStoragePermissionCallback writeExternalStoragePermissionCallback;
    private ReadExternalStoragePermissionCallback readExternalStoragePermissionCallback;
    private ExternalStoragePermissionsCallback externalStoragePermissionsCallback;
    private AccessFineLocationPermissionCallback accessFineLocationPermissionCallback;

    public PermissionUtils(AppCompatActivity activity) {
        this.activity = activity;
    }

    public PermissionUtils(Fragment fragment) {
        this.fragment = fragment;
    }

    public void validatePermissionWriteExternalStorage(@NonNull WriteExternalStoragePermissionCallback permissionCallback) {
        if (hasPermissionWriteExternalStorage()) {
            permissionCallback.onWriteExternalStoragePermissionGranted();
            return;
        }
        this.writeExternalStoragePermissionCallback = permissionCallback;
        requestPermissionWriteExternalStorage();
    }

    public void validatePermissionAccessFineLocation(@NonNull AccessFineLocationPermissionCallback permissionCallback) {
        if (hasPermissionAccessFineLocation()) {
            permissionCallback.onAccessFineLocationPermissionGranted();
            return;
        }
        this.accessFineLocationPermissionCallback = permissionCallback;
        requestPermissionAccessFineLocation();
    }

    public boolean hasPermissionAccessFineLocation() {
        if (activity != null)
            return ContextCompat.checkSelfPermission(activity,
                    Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED;
        else if (fragment != null)
            return ContextCompat.checkSelfPermission(fragment.getContext(),
                    Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED;
        return false;
    }

    public boolean hasPermissionWriteExternalStorage() {
        if (activity != null)
            return ContextCompat.checkSelfPermission(activity,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED;
        else if (fragment != null)
            return ContextCompat.checkSelfPermission(fragment.getContext(),
                    Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED;
        return false;
    }

    public void validatePermissionReadExternalStorage(@NonNull ReadExternalStoragePermissionCallback permissionCallback) {
        if (hasPermissionReadExternalStorage()) {
            permissionCallback.onReadExternalStoragePermissionGranted();
            return;
        }
        this.readExternalStoragePermissionCallback = permissionCallback;
        requestPermissionReadExternalStorage();
    }

    public boolean hasPermissionReadExternalStorage() {
        if (activity != null)
            return ContextCompat.checkSelfPermission(activity,
                    Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED;
        else if (fragment != null)
            return ContextCompat.checkSelfPermission(fragment.getContext(),
                    Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED;
        return false;
    }

    public void validateExternalStoragePermissions(@NonNull ExternalStoragePermissionsCallback permissionCallback) {
        if (hasExternalStoragePermissions()) {
            permissionCallback.onExternalStoragePermissionsGranted();
            return;
        }
        this.externalStoragePermissionsCallback = permissionCallback;
        requestExternalStoragePermissions();
    }

    public boolean hasExternalStoragePermissions() {
        return hasPermissionReadExternalStorage() && hasPermissionWriteExternalStorage();
    }

    public void requestPermissionReadExternalStorage() {
        if (activity != null)
            if (ActivityCompat.shouldShowRequestPermissionRationale(activity,
                    Manifest.permission.READ_EXTERNAL_STORAGE))
                showRationalDialog(activity, MSG_RATIONAL_READ_EXTERNAL_STORAGE, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        ActivityCompat.requestPermissions(activity, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                                REQ_PERMISSION_READ_EXTERNAL_STORAGE);
                    }
                });
            else
                ActivityCompat.requestPermissions(activity, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                        REQ_PERMISSION_READ_EXTERNAL_STORAGE);
        else if (fragment != null)
            if (fragment.shouldShowRequestPermissionRationale(Manifest.permission.READ_EXTERNAL_STORAGE))
                showRationalDialog(fragment.getContext(), MSG_RATIONAL_READ_EXTERNAL_STORAGE, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        fragment.requestPermissions(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                                REQ_PERMISSION_READ_EXTERNAL_STORAGE);
                    }
                });
            else
                fragment.requestPermissions(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                        REQ_PERMISSION_READ_EXTERNAL_STORAGE);
    }

    public void requestPermissionWriteExternalStorage() {
        if (activity != null)
            if (ActivityCompat.shouldShowRequestPermissionRationale(activity,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE))
                showRationalDialog(activity, MSG_RATIONAL_WRITE_EXTERNAL_STORAGE, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        ActivityCompat.requestPermissions(activity, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                                REQ_PERMISSION_WRITE_EXTERNAL_STORAGE);
                    }
                });
            else
                ActivityCompat.requestPermissions(activity, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                        REQ_PERMISSION_WRITE_EXTERNAL_STORAGE);
        else if (fragment != null)
            if (fragment.shouldShowRequestPermissionRationale(Manifest.permission.WRITE_EXTERNAL_STORAGE))
                showRationalDialog(fragment.getContext(), MSG_RATIONAL_WRITE_EXTERNAL_STORAGE, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        fragment.requestPermissions(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                                REQ_PERMISSION_WRITE_EXTERNAL_STORAGE);
                    }
                });
            else
                fragment.requestPermissions(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                        REQ_PERMISSION_WRITE_EXTERNAL_STORAGE);
    }

    public void requestPermissionAccessFineLocation() {
        if (activity != null)
            if (ActivityCompat.shouldShowRequestPermissionRationale(activity,
                    Manifest.permission.ACCESS_FINE_LOCATION))
                showRationalDialog(activity, MSG_RATIONAL_ACCESS_FINE_LOCATION, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        ActivityCompat.requestPermissions(activity, new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                                REQ_PERMISSION_ACCESS_FINE_LOCATION);
                    }
                });
            else
                ActivityCompat.requestPermissions(activity, new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                        REQ_PERMISSION_ACCESS_FINE_LOCATION);
        else if (fragment != null)
            if (fragment.shouldShowRequestPermissionRationale(Manifest.permission.ACCESS_FINE_LOCATION))
                showRationalDialog(fragment.getContext(), MSG_RATIONAL_ACCESS_FINE_LOCATION, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        fragment.requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                                REQ_PERMISSION_ACCESS_FINE_LOCATION);
                    }
                });
            else
                fragment.requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                        REQ_PERMISSION_ACCESS_FINE_LOCATION);
    }

    public void requestExternalStoragePermissions() {
        if (activity != null)
            if (ActivityCompat.shouldShowRequestPermissionRationale(activity, Manifest.permission.READ_EXTERNAL_STORAGE)
                    || ActivityCompat.shouldShowRequestPermissionRationale(activity, Manifest.permission.WRITE_EXTERNAL_STORAGE))
                showRationalDialog(activity, MSG_RATIONAL_STORAGE, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        ActivityCompat.requestPermissions(activity, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE,
                                        Manifest.permission.WRITE_EXTERNAL_STORAGE},
                                REQ_PERMISSION_EXTERNAL_STORAGE);
                    }
                });
            else
                ActivityCompat.requestPermissions(activity, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE,
                                Manifest.permission.WRITE_EXTERNAL_STORAGE},
                        REQ_PERMISSION_EXTERNAL_STORAGE);
        else if (fragment != null)
            if (fragment.shouldShowRequestPermissionRationale(Manifest.permission.READ_EXTERNAL_STORAGE)
                    || fragment.shouldShowRequestPermissionRationale(Manifest.permission.WRITE_EXTERNAL_STORAGE))
                showRationalDialog(fragment.getContext(), MSG_RATIONAL_STORAGE, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        fragment.requestPermissions(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE,
                                        Manifest.permission.WRITE_EXTERNAL_STORAGE},
                                REQ_PERMISSION_EXTERNAL_STORAGE);
                    }
                });
            else
                fragment.requestPermissions(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE,
                                Manifest.permission.WRITE_EXTERNAL_STORAGE},
                        REQ_PERMISSION_EXTERNAL_STORAGE);
    }

    // Call this method from onRequestPermissionsResult() method of activity or fragment! It's necessary (^..^)
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == REQ_PERMISSION_EXTERNAL_STORAGE) {
            if (hasExternalStoragePermissions()) {
                if (externalStoragePermissionsCallback != null)
                    externalStoragePermissionsCallback.onExternalStoragePermissionsGranted();
            } else if (externalStoragePermissionsCallback != null)
                externalStoragePermissionsCallback.onExternalStoragePermissionsCanceled();
        } else if (requestCode == REQ_PERMISSION_READ_EXTERNAL_STORAGE) {
            if (hasPermissionReadExternalStorage()) {
                if (readExternalStoragePermissionCallback != null)
                    readExternalStoragePermissionCallback.onReadExternalStoragePermissionGranted();
            } else if (readExternalStoragePermissionCallback != null)
                readExternalStoragePermissionCallback.onReadExternalStoragePermissionCanceled();
        } else if (requestCode == REQ_PERMISSION_WRITE_EXTERNAL_STORAGE) {
            if (hasPermissionWriteExternalStorage()) {
                if (writeExternalStoragePermissionCallback != null)
                    writeExternalStoragePermissionCallback.onWriteExternalStoragePermissionGranted();
            } else if (writeExternalStoragePermissionCallback != null)
                writeExternalStoragePermissionCallback.onWriteExternalStoragePermissionCanceled();
        } else if (requestCode == REQ_PERMISSION_ACCESS_FINE_LOCATION) {
            if (hasPermissionAccessFineLocation()) {
                if (accessFineLocationPermissionCallback != null)
                    accessFineLocationPermissionCallback.onAccessFineLocationPermissionGranted();
            } else if (accessFineLocationPermissionCallback != null)
                accessFineLocationPermissionCallback.onAccessFineLocationPermissionCanceled();
        }
    }

    private void showRationalDialog(Context context, String rationalMessage, DialogInterface.OnClickListener clickListener) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setMessage(rationalMessage);
        builder.setNeutralButton(android.R.string.ok, clickListener);
        builder.create().show();
    }

    public interface WriteExternalStoragePermissionCallback {
        void onWriteExternalStoragePermissionGranted();

        void onWriteExternalStoragePermissionCanceled();
    }

    public interface ReadExternalStoragePermissionCallback {
        void onReadExternalStoragePermissionGranted();

        void onReadExternalStoragePermissionCanceled();
    }

    public interface ExternalStoragePermissionsCallback {
        void onExternalStoragePermissionsGranted();

        void onExternalStoragePermissionsCanceled();
    }

    public interface AccessFineLocationPermissionCallback {
        void onAccessFineLocationPermissionGranted();

        void onAccessFineLocationPermissionCanceled();
    }
}