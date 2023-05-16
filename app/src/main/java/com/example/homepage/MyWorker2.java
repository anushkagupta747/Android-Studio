package com.example.homepage;


import static android.content.ContentValues.TAG;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.AppOpsManager;
import android.app.usage.UsageStats;
import android.app.usage.UsageStatsManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.location.Location;
import android.net.Uri;
import android.provider.CallLog;
import android.provider.MediaStore;
import android.provider.Settings;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.content.pm.PackageManager;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.work.ExistingWorkPolicy;
import androidx.work.OneTimeWorkRequest;
import androidx.work.WorkManager;
import androidx.work.Worker;
import androidx.work.WorkerParameters;

import com.example.homepage.ml.Model;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.type.LatLng;

import org.tensorflow.lite.DataType;
import org.tensorflow.lite.support.tensorbuffer.TensorBuffer;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.jakewharton.threetenabp.AndroidThreeTen;

import android.content.pm.PackageInfo;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import java.util.List;


public class MyWorker2 extends Worker {
    ImageView imageView;
    private Context mContext;
    FirebaseFirestore db;
    FusedLocationProviderClient fusedLocationProviderClient;
    public static int LOCATIONREQUESTCODE = 100;


//    DBHelper dbHelper;

    private final Executor backgroundExecutor = Executors.newSingleThreadExecutor();

    public MyWorker2(@NonNull Context context, @NonNull WorkerParameters workerParams) {
        super(context, workerParams);
        mContext = context;
    }

    @Override
    public Result doWork() {
        try {
            // Do your work here
            // Perform some asynchronous work in the background


            Context contexts = getApplicationContext();
            //        dbHelper = new DBHelper(contexts);
            //        dbHelper.resetCount();
            //        int count=dbHelper.getCount();
            //        count++;
            //        dbHelper.setCount(count,"current");


            backgroundExecutor.execute(new Runnable() {
                @Override
                public void run() {
                    int count = SPUMaster.getCount(contexts);
                    Log.d("Count : " + count, "Running asynchronous work...");
                    // Do your asynchronous work here
                    if (true) {
                        //FOR IMAGE DETECTION
                        long timestampLatest = getLatestImageTimestamp(contexts);
                        long timestampSaved = SPUMaster.getTimestamp(contexts);
                        Log.d(timestampSaved + " " + timestampLatest, "Running asynchronous work...");
                        if (timestampLatest > timestampSaved) {
                            // There is a new image, so we need to classify it
                            timestampSaved = timestampLatest;
                            // Update the timestamp of the last processed image
                            SPUMaster.saveTimestamp(contexts, timestampSaved);
                            Uri latestUri = getUriOfLatestImage();
                            Bitmap latestImage = getLatestImageFromGallery(latestUri);
                            int indexOfFirstConfidence = classifyImage(latestImage);
                            String[] Checking = {"Drawing", "Hentai", "Normal", "Porn", "Sexy"};
                            Log.d("RESULT = " + Checking[indexOfFirstConfidence], "");
                            if (indexOfFirstConfidence == 2 || indexOfFirstConfidence == 4) {
                                //handle notification to parent for sus picture
                                String token = SPUChildSupport.getToken(getApplicationContext());
                                FCMSend.pushNotification(
                                        mContext,
                                        token,
                                        "Suspicious Pic Detected"+timestampSaved,
                                        "Please check your child's phone"
                                );
                                Log.d("Notification sent", "run: ");
                            }
                        }

                    }
                    if ((count % 6) == 0) {
                        //FOR APP USAGE
                        getAppUsageStats();

                        //FOR GEOLOCATION
                        getUserLocation();

                        //FOR CALL HISTORY
                        CallLogsLatest(contexts);

                        //FOR APP LIST
                        getList();

                    }
                    if ((count % 60) == 0) {



                    }
                    count++;
                    SPUMaster.saveCount(contexts, count);
                }
            });
            // Create a new OneTimeWorkRequest with an initial delay of 10 seconds
            OneTimeWorkRequest nextRequest = new OneTimeWorkRequest.Builder(MyWorker2.class)
                    .setInitialDelay(10, TimeUnit.SECONDS)
                    .build();
            // Enqueue the next work request with WorkManager
            WorkManager.getInstance(getApplicationContext())
                    .enqueueUniqueWork("my_unique_work_name", ExistingWorkPolicy.REPLACE, nextRequest);
            // Return success
            return Result.success();
        } catch (Throwable throwable) {
            Log.e("MyWorker2", "Error executing work", throwable);
            // Return failure
            return Result.failure();
        }
    }


    public int classifyImage(Bitmap image) {
        int indexOfFirstConfidence = 0;
        try {
            int imageSize = 224;
            Model model = Model.newInstance(getApplicationContext());
            // Creates inputs for reference.
            TensorBuffer inputFeature0 = TensorBuffer.createFixedSize(new int[]{1, 224, 224, 3}, DataType.FLOAT32);
            ByteBuffer byteBuffer = ByteBuffer.allocateDirect(4 * imageSize * imageSize * 3);
            byteBuffer.order(ByteOrder.nativeOrder());
            int[] intValues = new int[imageSize * imageSize];
            image.getPixels(intValues, 0, image.getWidth(), 0, 0, image.getWidth(), image.getHeight());
            int pixel = 0;
            //iterate over each pixel and extract R, G, and B values. Add those values individually to the byte buffer.
            for (int i = 0; i < imageSize; i++) {
                for (int j = 0; j < imageSize; j++) {
                    int val = intValues[pixel++]; // RGB
                    byteBuffer.putFloat(((val >> 16) & 0xFF) * (1.f / 255.f));
                    byteBuffer.putFloat(((val >> 8) & 0xFF) * (1.f / 255.f));
                    byteBuffer.putFloat((val & 0xFF) * (1.f / 255.f));
                }
            }
            inputFeature0.loadBuffer(byteBuffer);
            // Runs model inference and gets result.
            Model.Outputs outputs = model.process(inputFeature0);
            TensorBuffer outputFeature0 = outputs.getOutputFeature0AsTensorBuffer();
            //duplicate and find order of confidence
            float[] confidences = outputFeature0.getFloatArray();
            float[] originalconfidence = new float[5];
            System.arraycopy(confidences, 0, originalconfidence, 0, 5);
            String allresults = confidences[0] + " " + confidences[1] + " " + confidences[2] + " " + confidences[3] + " " + confidences[4] + " ";
            String[] Checking = {"Drawing", "Hentai", "Normal", "Porn", "Sexy"};
            Arrays.sort(confidences);
            //find highest confidence
            float highestConfidenceValue = confidences[4];
            int i;
            for (i = 0; i <= 4; i++) {
                if (originalconfidence[i] == highestConfidenceValue) {
                    indexOfFirstConfidence = i;
                    break;
                }
            }
            Log.d("MODEL RUN - ", allresults);
            model.close();
        } catch (IOException e) {
            // TODO Handle the exception
        }
        return indexOfFirstConfidence;
    }

    //    private Bitmap getLatestImageFromGallery(Uri dat) {
//        int imageSize = 224;
//        // Set up a projection to get the image's ID and date added
//        String[] projection = {MediaStore.Images.Media._ID, MediaStore.Images.Media.DATE_ADDED};
//        // Query the gallery for images, sorted by date added
//        Cursor cursor = getApplicationContext().getContentResolver().query(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, projection,
//                null, null, MediaStore.Images.Media.DATE_ADDED + " DESC");
//        // Get the URI of the latest image, if there is one
//        Uri dat = null;
//        if (cursor.moveToFirst()) {
//            @SuppressLint("Range") long imageId = cursor.getLong(cursor.getColumnIndex(MediaStore.Images.Media._ID));
//            dat = Uri.withAppendedPath(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, Long.toString(imageId));
//        }
//        // Close the cursor to free up resources
//        cursor.close();
//
//
//        Bitmap image = null;
//        try {
//            image = MediaStore.Images.Media.getBitmap(getApplicationContext().getContentResolver(), dat);
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//        if(imageView != null) {
//            imageView.setImageBitmap(image);
//        }
//        image = Bitmap.createScaledBitmap(image, imageSize, imageSize, false);
//        Log.d("GOT LATEST IMAGE","");
//        return image;
//    }
    private Bitmap getLatestImageFromGallery(Uri dat) {
        int imageSize = 224;
        Bitmap image = null;
        try {
            image = MediaStore.Images.Media.getBitmap(getApplicationContext().getContentResolver(), dat);
        } catch (IOException e) {
            e.printStackTrace();
        }
        if (imageView != null) {
            imageView.setImageBitmap(image);
        }
        image = Bitmap.createScaledBitmap(image, imageSize, imageSize, false);
        Log.d("GOT LATEST IMAGE", "");
        return image;
    }


//    private long getTimestampOfImage(Bitmap image) {
//        Uri uri = getUriOfImage(image);
//        String[] projection = {MediaStore.Images.Media.DATE_ADDED};
//        Cursor cursor = getApplicationContext().getContentResolver().query(uri, projection, null, null, null);
//        cursor.moveToFirst();
//        long timestamp = cursor.getLong(0);
//        cursor.close();
//        return timestamp;
//    }

    private Uri getUriOfLatestImage() {
        // Set up a projection to get the image's ID and date added
        String[] projection = {MediaStore.Images.Media._ID, MediaStore.Images.Media.DATE_ADDED};
        // Set up a selection parameter to query only for images
        String selection = MediaStore.Images.Media.MIME_TYPE + "=?";
        String[] selectionArgs = new String[] { "image/jpeg" }; // Add any other image MIME types as needed
        // Query the gallery for images, sorted by date added
        Cursor cursor = getApplicationContext().getContentResolver().query(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, projection,
                selection, selectionArgs, MediaStore.Images.Media.DATE_ADDED + " DESC");
        // Get the URI of the latest image, if there is one
        Uri dat = null;
        if (cursor.moveToFirst()) {
            @SuppressLint("Range") long imageId = cursor.getLong(cursor.getColumnIndex(MediaStore.Images.Media._ID));
            dat = Uri.withAppendedPath(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, Long.toString(imageId));
        }
        // Close the cursor to free up resources
        cursor.close();
        return dat;
    }


    public long getLatestImageTimestamp(Context context) {
        // Define the content URI for the image media files
        final Uri imageUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
        // Define the columns to retrieve from the query
        final String[] projection = {MediaStore.Images.Media.DATE_ADDED};
        // Define the sort order for the query
        final String sortOrder = MediaStore.Images.Media.DATE_ADDED + " DESC";
        // Execute the query to get the latest image added to the gallery
        Cursor cursor = context.getContentResolver().query(imageUri, projection, null, null, sortOrder);
        // Get the timestamp of the latest image added to the gallery
        long timestamp = 0;
        if (cursor != null && cursor.moveToFirst()) {
            timestamp = cursor.getLong(cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATE_ADDED));
            cursor.close();
        }
        return timestamp;
    }


    public void CallLogsLatest(Context context) {
        db = FirebaseFirestore.getInstance();
        long timestamp = System.currentTimeMillis();
        String parentid = SPUMaster.getParentId(getApplicationContext());
        String parentPassword = SPUMaster.getParentPassword(getApplicationContext());
        String childid = parentid+parentPassword;
        // Get the start date for the query (7 days ago)
        long startDate = System.currentTimeMillis() - (7 * 24 * 60 * 60 * 1000);
        // Define the projection and selection arguments for the query
        String[] projection = null;
        String selection = CallLog.Calls.DATE + " >= ?";
        String[] selectionArgs = new String[]{String.valueOf(startDate)};
        Cursor cursor = getApplicationContext().getContentResolver().query(CallLog.Calls.CONTENT_URI, projection, selection, selectionArgs, CallLog.Calls.DATE + " DESC");
        if (cursor != null && cursor.moveToFirst()) {
            do {
                @SuppressLint("Range") String name = cursor.getString(cursor.getColumnIndex(CallLog.Calls.CACHED_NAME));
                @SuppressLint("Range") String number = cursor.getString(cursor.getColumnIndex(CallLog.Calls.NUMBER));
                @SuppressLint("Range") String type = cursor.getString(cursor.getColumnIndex(CallLog.Calls.TYPE));
                @SuppressLint("Range") String date = cursor.getString(cursor.getColumnIndex(CallLog.Calls.DATE));
                @SuppressLint("Range") String duration = cursor.getString(cursor.getColumnIndex(CallLog.Calls.DURATION));
                @SuppressLint("Range") String callid = cursor.getString(cursor.getColumnIndex(CallLog.Calls._ID));
                String numberindian = "";
                if (number != null && number.length() > 10) {
                    numberindian = number.substring(number.length() - 10);
                }
                String databaseid = childid + callid + date + numberindian;
                // Do something with the call logs
                // Concatenate the call log details
                // Check if the document with the same databaseid already exists
                db.collection("call_logs").document(databaseid).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if (task.isSuccessful()) {
                            DocumentSnapshot document = task.getResult();
                            if (document.exists()) {
//                                Log.d("CallLog exists", "Call log exists with databaseid: " + databaseid);
                            } else {
                                // Do something with the call logs
                                // Concatenate the call log details
                                String callLogDetails = "Name: " + name + ", Number: " + number + ", Type: " + type + ", Date: " + date + ", Duration: " + duration + ", Call ID: " + callid;
                                // Log the call log details
                                Log.d("CallLog Added", callLogDetails);
                                ModelClassFirestore data = new ModelClassFirestore(timestamp, childid, parentid, databaseid, name, number, type, date, duration, callid);
                                db.collection("call_logs").document(databaseid).set(data).addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        if (task.isSuccessful()) {

                                        } else {
                                            Log.d("CallLog upload problem", " ", task.getException());
                                        }
                                    }
                                });
                            }
                        } else {
                            Log.d("CallLog Complete", " ", task.getException());
                        }
                    }
                });
            } while (cursor.moveToNext());
        } else {
            // Handle the case when there are no call logs in the cursor
            Log.d("CallLog", "No call logs found.");
        }
        // Close the cursor to avoid memory leaks
        if (cursor != null) {
            cursor.close();
        }
    }

    //DOES RUN IN BACKGROUND
    private void getUserLocation() {
        Context contexts = getApplicationContext();
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(contexts);
        String parentid = SPUMaster.getParentId(getApplicationContext());
        String parentPassword = SPUMaster.getParentPassword(getApplicationContext());
        String childid = parentid+parentPassword;
        if (ActivityCompat.checkSelfPermission(contexts, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(contexts, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        Task<Location> task = fusedLocationProviderClient.getLastLocation();
        task.addOnSuccessListener(new OnSuccessListener<Location>() {
            @Override
            public void onSuccess(Location location) {
                double longitude, latitude, altitude;
                LatLng userLocation;
                if (location != null) {
                    longitude = location.getLatitude();
                    latitude = location.getLongitude();
                    altitude = location.getAltitude();



                    // Get the current speed in meters/second
                    float speed = location.getSpeed();
                    // Convert the speed to km/h
                    float speedKMH = (speed * 3600) / 1000;
                    Date currentDate = new Date();
                    long milliSeconds = currentDate.getTime();

                    db = FirebaseFirestore.getInstance();
                    Log.d("Location Service", longitude + " " + latitude + " " + speedKMH);
                    ModelClassFirestoreLocation data = new ModelClassFirestoreLocation(childid, parentid, longitude, latitude, altitude, speedKMH, milliSeconds);
                    db.collection("child_location").document().set(data).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()) {
                                Log.d("Location uploaded", " ", task.getException());
                            } else {
                                Log.d("Location upload problem", " ", task.getException());
                            }
                        }
                    });
                }
            }
        });
    }

//    public void getAppUsageStats() {
//        Log.d("APP USAGE 1", "");
//        Context context = getApplicationContext();
//        List<UsageStats> stats = new ArrayList<>();
//        long totalUsageToday = 0;
//        Date date = new Date();
//        db = FirebaseFirestore.getInstance();

//        SimpleDateFormat formatter = new SimpleDateFormat("yyyy/mm/dd");
//        String str = formatter.format(date);
//        long currentTime = System.currentTimeMillis();
//        long startOfDay = getStartOfDay(currentTime);
//        Log.d("APP USAGE 3", "");
//        UsageStatsManager usageStatsManager = (UsageStatsManager) context.getSystemService(Context.USAGE_STATS_SERVICE);
//        if (usageStatsManager != null) {
//            List<UsageStats> usageStatsList = usageStatsManager.queryUsageStats(UsageStatsManager.INTERVAL_DAILY, startOfDay, currentTime);
//            if (usageStatsList != null) {
//                stats.addAll(usageStatsList);
//                for (UsageStats usageStats : usageStatsList) {
//                    totalUsageToday += usageStats.getTotalTimeInForeground();
//                }
//            }
//        }
//
//
//
//    }

    private long getStartOfDay(long currentTime) {
        Log.d("APP USAGE 2", "");
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(currentTime);
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);
        return calendar.getTimeInMillis();
    }
    private long getStartOfNextDay(long currentTime) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(currentTime);
        calendar.add(Calendar.DAY_OF_YEAR, 1);
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);
        return calendar.getTimeInMillis();
    }

    private List<UsageStats> getAppUsageStatsOld() {
        Log.d("APP USAGE 2", "");
        List<UsageStats> stats = new ArrayList<>();
        long currentTime = System.currentTimeMillis();
        long startOfDay = getStartOfDay(currentTime);
        long startofnextday=getStartOfNextDay(currentTime);
        Log.d(""+currentTime, "");
        UsageStatsManager usageStatsManager = (UsageStatsManager) mContext.getSystemService(Context.USAGE_STATS_SERVICE);
        if (usageStatsManager != null) {
            List<UsageStats> usageStatsList = usageStatsManager.queryUsageStats(UsageStatsManager.INTERVAL_DAILY, startOfDay, currentTime);
            if (usageStatsList != null) {
                stats.addAll(usageStatsList);
            }
        }
        return stats;
    }
    public void getAppUsageStats() {
        Log.d("APP USAGE 1", "");
        Map<String, Long> FinalResult = new HashMap<>();
        String parentid = SPUMaster.getParentId(getApplicationContext());
        String parentPassword = SPUMaster.getParentPassword(getApplicationContext());
        String childid = parentid+parentPassword;
        List<UsageStats> appUsageStats = getAppUsageStatsOld();
        // Sort the appUsageStats list in descending order
        Collections.sort(appUsageStats, new Comparator<UsageStats>() {
            @Override
            public int compare(UsageStats lhs, UsageStats rhs) {
                long lhsTime = lhs.getTotalTimeInForeground();
                long rhsTime = rhs.getTotalTimeInForeground();
                return Long.compare(rhsTime, lhsTime);
            }
        });
        Log.d("APP USAGE 3", "");
        String currentPackageName = "";
        long totalTime = 0;
        for (UsageStats usageStats : appUsageStats) {
            currentPackageName = usageStats.getPackageName();
            long time = usageStats.getTotalTimeInForeground();
            time = time / (1000*60);
            if (time > 0) {
                totalTime+=time;
                if (!currentPackageName.isEmpty()) {
                    int lastDotIndex = currentPackageName.lastIndexOf(".");
                    currentPackageName = currentPackageName.substring(lastDotIndex + 1);
                    currentPackageName = currentPackageName.substring(0, 1).toUpperCase() + currentPackageName.substring(1);
                    FinalResult.put(currentPackageName, time);
                    Log.d("APPNAMES", currentPackageName+" "+time+" "+totalTime);
                }
            }
        }
        FinalResult.put("TotalUsage", totalTime);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
        String currentDate = sdf.format(new Date());
        db = FirebaseFirestore.getInstance();
        String collectionPath = "child_app_usage";
        String documentId = childid+currentDate;
        // Get the document reference
        DocumentReference documentRef = db.collection(collectionPath).document(documentId);
        documentRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {
                        // The document exists
                        Log.d(TAG, "Document exists!");
                        db.collection("child_app_usage").document(documentId).update("finalResult",FinalResult).addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if (task.isSuccessful()) {
                                    Log.d("Document updates", " ", task.getException());
                                } else {
                                    Log.d("Document update problem", " ", task.getException());
                                }
                            }
                        });
                    } else {
                        // The document doesn't exist
                        Log.d(TAG, "Document does not exist!");
                        ModalClassFirestoreAppUsage data = new ModalClassFirestoreAppUsage(childid, parentid,FinalResult,currentDate);
                        db.collection("child_app_usage").document(documentId).set(data).addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if (task.isSuccessful()) {
                                    Log.d("Document uploaded", " ", task.getException());
                                } else {
                                    Log.d("Document upload problem", " ", task.getException());
                                }
                            }
                        });
                        Log.d(TAG, "Document Added!");
                    }
                } else {
                    // An error occurred while retrieving the document
                    Log.d(TAG, "Error getting document: ", task.getException());
                }
            }
        });
    }

//        public void getAllAppsList(View view)
//        {
//            // get list of all the apps installed
//            List<ApplicationInfo> infos = getPackageManager().getInstalledApplications(PackageManager.GET_META_DATA);
//            // create a list with size of total number of apps
//            String[] apps = new String[infos.size()];
//            int i = 0;
//            // add all the app name in string list
//            for (ApplicationInfo info : infos) {
//                apps[i] = info.packageName;
//                i++;
//            }
//        }


    public void getList() {
        // Get the list of installed apps
        List<String> installedApps = getInstalledAppNames();
//        for (String appName : installedApps) {
//            Log.d("InstalledApp", appName);
//        }
        // Get the necessary data for Firestore upload
        String parentid = SPUMaster.getParentId(getApplicationContext());
        String parentPassword = SPUMaster.getParentPassword(getApplicationContext());
        String childId = parentid+parentPassword;
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
        String currentDate = sdf.format(new Date());
        db = FirebaseFirestore.getInstance();
        String collectionPath = "child_app_list";
        String documentId = childId+currentDate;
        // Get the document reference
        DocumentReference documentRef = db.collection(collectionPath).document(documentId);
        documentRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {
                        // The document exists
                        Log.d(TAG, "Document exists!");
                        db.collection("child_app_list").document(documentId).update("InstalledApps",installedApps).addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if (task.isSuccessful()) {
                                    Log.d("List updated", " ", task.getException());
                                } else {
                                    Log.d("List update problem", " ", task.getException());
                                }
                            }
                        });
                    } else {
                        // The document doesn't exist
                        Log.d(TAG, "Document does not exist!");
                        ModelClassFirestoreAppList data = new ModelClassFirestoreAppList(childId, parentid, currentDate, installedApps);
                        db.collection("child_app_list").document(documentId).set(data).addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if (task.isSuccessful()) {
                                    Log.d("List new uploaded", " ", task.getException());
                                } else {
                                    Log.d("List upload problem", " ", task.getException());
                                }
                            }
                        });
                        Log.d(TAG, "List final Added!");
                    }
                } else {
                    // An error occurred while retrieving the document
                    Log.d(TAG, "Error getting List: ", task.getException());
                }
            }
        });

    }

//    private List<ApplicationInfo> getInstalledApps() {
//        // Get a reference to the PackageManager
//        PackageManager packageManager = getApplicationContext().getPackageManager();
//        // Get a list of all installed applications on the device
//        List<ApplicationInfo> applications = packageManager.getInstalledApplications(0);
//        return applications;
//    }

    private List<String> getInstalledAppNames() {
        PackageManager packageManager = getApplicationContext().getPackageManager();
        List<ApplicationInfo> applications = packageManager.getInstalledApplications(0);
        List<String> appNames = new ArrayList<>();
        for (ApplicationInfo appInfo : applications) {
            String appName = appInfo.loadLabel(packageManager).toString();
            if (!appName.startsWith("com.")) {
                appNames.add(appName);
            }
        }
        return appNames;
    }




}






