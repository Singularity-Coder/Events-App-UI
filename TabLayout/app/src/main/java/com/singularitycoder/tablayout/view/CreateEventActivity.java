package com.singularitycoder.tablayout.view;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.app.ActivityCompat;
import androidx.core.content.FileProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.ParcelFileDescriptor;
import android.provider.MediaStore;
import android.provider.Settings;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.karumi.dexter.Dexter;
import com.karumi.dexter.MultiplePermissionsReport;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.multi.MultiplePermissionsListener;
import com.singularitycoder.tablayout.BuildConfig;
import com.singularitycoder.tablayout.R;
import com.singularitycoder.tablayout.adapter.CreateEventGalleryAdapter;
import com.singularitycoder.tablayout.model.CreateEventGalleryModel;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileDescriptor;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class CreateEventActivity extends AppCompatActivity {

    Toolbar createEventToolbar;
    TextView eventTypeDialog, eventCategoryDialog, eventFrequencyDialog, eventStartDateDialog, eventEndDateDialog, eventStartTimeDialog, eventEndTimeDialog;
    ImageView imgSetEventHeaderPic, imgSetEventPicIndicator;
    TextView tvSetEventPicIndicatorText;
    ConstraintLayout conLayHeaderImage;

    RecyclerView galleryRecyclerView;
    public static CreateEventGalleryAdapter galleryAdapter;
    public static ArrayList<CreateEventGalleryModel> imgGalleryList;
    public static ImageView imgAddGalleryImage;

    /* Camera stuff*/

    // Activity request codes
    private static final int BANNER_CAMERA_CAPTURE_IMAGE_REQUEST_CODE = 100;
    private static final int BANNER_GALLERY_CAPTURE_IMAGE_REQUEST_CODE = 200;

    private static final int GALLERY_CAMERA_CAPTURE_IMAGE_REQUEST_CODE = 300;
    private static final int GALLERY_GALLERY_CAPTURE_IMAGE_REQUEST_CODE = 400;

    // Gallery directory name to store the images or videos
    public static final String GALLERY_DIRECTORY_NAME = "Camera Files";

    // key to store image path in savedInstance state
    public static final String KEY_IMAGE_STORAGE_PATH = "image_path";
    public static final String KEY_GALLERY_IMAGE_STORAGE_PATH = "gallery_image_path";

    public static final int MEDIA_TYPE_IMAGE = 1;
    public static final int MEDIA_TYPE_VIDEO = 2;

    // Bitmap sampling size
    public static final int BITMAP_SAMPLE_SIZE = 8;

    // Image and Video file extensions
    public static final String IMAGE_EXTENSION = "jpg";
    public static final String VIDEO_EXTENSION = "mp4";

    private static String imageStoragePath;

    Uri finalUri;

    /* Camera stuff*/

    public CreateEventActivity(ArrayList<CreateEventGalleryModel> imgGalleryList) {
        this.imgGalleryList = imgGalleryList;
    }

    public CreateEventActivity() {
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_event);

        // Checking availability of the camera
        if (!isDeviceSupportCamera(getApplicationContext())) {
            Toast.makeText(getApplicationContext(), "Sorry! Your device doesn't support camera", Toast.LENGTH_LONG).show();
            // will close the app if the device doesn't have camera
            finish();
        }

        imgSetEventHeaderPic = findViewById(R.id.img_create_event_header);  // img preview

        imgSetEventPicIndicator = findViewById(R.id.imgbtn_create_event_upload_pic);
        tvSetEventPicIndicatorText = findViewById(R.id.tv_create_event_upload_pic);

        initToolBar();
        listeners();
        fillGalleryList();
        setGalleryAdapter();
        setGalleryRecyclerView();

        // restoring storage image path from saved instance state
        // otherwise the path will be null on device rotation
        restoreFromBundle(savedInstanceState);
    }

    private void listeners() {
        conLayHeaderImage = findViewById(R.id.con_lay_create_event_banner);
        conLayHeaderImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (checkPermissions(getApplicationContext())) {
                    showImagePickerOptions("banner");
                } else {
                    requestCameraPermission("banner");
                }

            }
        });

        eventTypeDialog = findViewById(R.id.tv_create_event_type_dialog);
        eventTypeDialog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                eventTypeDialogFunc();
            }
        });

        eventCategoryDialog = findViewById(R.id.tv_create_event_category_dialog);
        eventCategoryDialog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                eventCategoryDialogFunc();
            }
        });

        eventFrequencyDialog = findViewById(R.id.tv_create_event_frequency_dialog);
        eventFrequencyDialog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                eventFrequencyDialogFunc();
            }
        });

        eventStartDateDialog = findViewById(R.id.tv_create_event_start_date_dialog);
        eventStartDateDialog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showDatePicker(eventStartDateDialog, CreateEventActivity.this);
            }
        });

        eventEndDateDialog = findViewById(R.id.tv_create_event_end_date_dialog);
        eventEndDateDialog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showDatePicker(eventEndDateDialog, CreateEventActivity.this);
            }
        });

        eventStartTimeDialog = findViewById(R.id.tv_create_event_start_time_dialog);
        eventStartTimeDialog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showTimePicker(eventStartTimeDialog);
            }
        });

        eventEndTimeDialog = findViewById(R.id.tv_create_event_end_time_dialog);
        eventEndTimeDialog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showTimePicker(eventEndTimeDialog);
            }
        });
    }


    // Checks whether device has camera or not. This method not necessary if android:required="true" is used in manifest file
    public static boolean isDeviceSupportCamera(Context context) {
        if (context.getPackageManager().hasSystemFeature(PackageManager.FEATURE_CAMERA)) {
            return true;    // this device has a camera
        } else {
            return false;   // no camera on this device
        }
    }

    public static boolean checkPermissions(Context context) {
        return ActivityCompat.checkSelfPermission(context, Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(context, Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED;
    }

    /**
     * Requesting permissions using Dexter library
     */
    private void requestCameraPermission(final String type) {
        Dexter.withActivity(this)
                .withPermissions(Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.RECORD_AUDIO)
                .withListener(new MultiplePermissionsListener() {
                    @Override
                    public void onPermissionsChecked(MultiplePermissionsReport report) {
                        if (report.areAllPermissionsGranted()) {

                            if (type.contains("banner")) {
                                takeCameraImage("banner");
                            } else {
                                takeCameraImage("gallery");
                            }

                        } else if (report.isAnyPermissionPermanentlyDenied()) {
                            showPermissionsAlert();
                        }
                    }

                    @Override
                    public void onPermissionRationaleShouldBeShown(List<PermissionRequest> permissions, PermissionToken token) {
                        token.continuePermissionRequest();
                    }
                }).check();
    }

    /**
     * Alert dialog to navigate to app settings
     * to enable necessary permissions
     */
    private void showPermissionsAlert() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Permissions required!")
                .setMessage("Camera needs few permissions to work properly. Grant them in settings.")
                .setPositiveButton("GOTO SETTINGS", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        openSettings(CreateEventActivity.this);
                    }
                })
                .setNegativeButton("CANCEL", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {

                    }
                }).show();
    }

    private void showSettingsDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Give Permissions!");
        builder.setMessage("We need you to grant the permissions for the camera feature to work!");
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
                openSettings(getApplicationContext());
            }
        });
        builder.setNegativeButton("CANCEL", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });
        builder.show();
    }

    // Open device app settings to allow user to enable permissions
    public static void openSettings(Context context) {
        Intent intent = new Intent();
        intent.setAction(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
        intent.setData(Uri.fromParts("package", BuildConfig.APPLICATION_ID, null));
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent);
    }

    // Refreshes gallery on adding new image/video. Gallery won't be refreshed on older devices until device is rebooted
    public static void refreshGallery(Context context, String filePath) {
        MediaScannerConnection.scanFile(context, new String[]{filePath}, null, new MediaScannerConnection.OnScanCompletedListener() {       // ScanFile so it will be appeared on Gallery
            public void onScanCompleted(String path, Uri uri) {

            }
        });
    }

    // Creates and returns the image or video file before opening the camera
    public static File getOutputMediaFile(int type) {
        // External sdcard location
        File mediaStorageDir = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES), CreateEventActivity.GALLERY_DIRECTORY_NAME);
        if (!mediaStorageDir.exists()) {        // Create the storage directory if it does not exist
            if (!mediaStorageDir.mkdirs()) {
                Log.e(CreateEventActivity.GALLERY_DIRECTORY_NAME, "Oops! Failed create " + CreateEventActivity.GALLERY_DIRECTORY_NAME + " directory");
                return null;
            }
        }

        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(new Date());      // Preparing media file naming convention adds timestamp
        File mediaFile;
        if (type == CreateEventActivity.MEDIA_TYPE_IMAGE) {
            mediaFile = new File(mediaStorageDir.getPath() + File.separator + "IMG_" + timeStamp + "." + CreateEventActivity.IMAGE_EXTENSION);
        } else if (type == CreateEventActivity.MEDIA_TYPE_VIDEO) {
            mediaFile = new File(mediaStorageDir.getPath() + File.separator + "VID_" + timeStamp + "." + CreateEventActivity.VIDEO_EXTENSION);
        } else {
            return null;
        }

        return mediaFile;
    }

    public Uri getOutputMediaFileUri(final Context context, final File file) {

        Dexter.withActivity(this)
                .withPermissions(Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                .withListener(new MultiplePermissionsListener() {
                    @Override
                    public void onPermissionsChecked(MultiplePermissionsReport report) {
                        if (report.areAllPermissionsGranted()) {
                            finalUri = FileProvider.getUriForFile(context, context.getPackageName() + ".provider", file);
                        }

                        if (report.isAnyPermissionPermanentlyDenied()) {
                            showSettingsDialog();
                        }
                    }

                    @Override
                    public void onPermissionRationaleShouldBeShown(List<PermissionRequest> permissions, PermissionToken token) {
                        token.continuePermissionRequest();
                    }
                }).check();

        return finalUri;
    }

    // Downsizing the bitmap to avoid OutOfMemory exceptions
    public static Bitmap optimizeBitmap(int sampleSize, String filePath) {
        BitmapFactory.Options options = new BitmapFactory.Options();  // bitmap factory
        options.inSampleSize = sampleSize;      // downsizing image as it throws OutOfMemory Exception for larger images
        return BitmapFactory.decodeFile(filePath, options);
    }

    /**
     * Saving stored image path to saved instance state
     */
    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        // save file url in bundle as it will be null on screen orientation
        // changes
        outState.putString(KEY_IMAGE_STORAGE_PATH, imageStoragePath);
        outState.putString(KEY_GALLERY_IMAGE_STORAGE_PATH, imageStoragePath);
    }

    /**
     * Restoring image path from saved instance state
     */
    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);

        // get the file url
        imageStoragePath = savedInstanceState.getString(KEY_IMAGE_STORAGE_PATH);
        imageStoragePath = savedInstanceState.getString(KEY_GALLERY_IMAGE_STORAGE_PATH);
    }

    /**
     * Restoring stored image path from saved instance state
     */
    private void restoreFromBundle(Bundle savedInstanceState) {
        if (savedInstanceState != null) {
            if (savedInstanceState.containsKey(KEY_IMAGE_STORAGE_PATH)) {
                imageStoragePath = savedInstanceState.getString(KEY_IMAGE_STORAGE_PATH);
                if (!TextUtils.isEmpty(imageStoragePath)) {
                    if (imageStoragePath.substring(imageStoragePath.lastIndexOf(".")).equals("." + IMAGE_EXTENSION)) {
                        previewCapturedImage();
                    }
                }
            } else if (savedInstanceState.containsKey(KEY_GALLERY_IMAGE_STORAGE_PATH)) {
                imageStoragePath = savedInstanceState.getString(KEY_GALLERY_IMAGE_STORAGE_PATH);
                if (!TextUtils.isEmpty(imageStoragePath)) {
                    if (imageStoragePath.substring(imageStoragePath.lastIndexOf(".")).equals("." + IMAGE_EXTENSION)) {
                        previewCapturedImage();
                    }
                }
            }
        }
    }

    /**
     * Display image from gallery
     */
    private void previewCapturedImage() {
        try {
            Bitmap bitmap = optimizeBitmap(BITMAP_SAMPLE_SIZE, imageStoragePath);
            imgSetEventHeaderPic.setImageBitmap(bitmap);

        } catch (NullPointerException e) {
            e.printStackTrace();
        }
    }

    /**
     * Display image from gallery
     */
    private void previewGalleryImage() {
        try {
            Bitmap bitmap = optimizeBitmap(BITMAP_SAMPLE_SIZE, imageStoragePath);
            imgSetEventHeaderPic.setImageBitmap(bitmap);

        } catch (NullPointerException e) {
            e.printStackTrace();
        }
    }

    private void showImagePickerOptions(final String type) {
        // setup the alert builder
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Set Image");

        // add a homeList
        String[] selectArray = {"Capture photo from camera", "Select photo from gallery"};
        builder.setItems(selectArray, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                switch (which) {
                    case 0:
                        if (type.contains("banner")) {
                            takeCameraImage("banner");
                        } else {
                            takeCameraImage("gallery");
                        }
                        break;
                    case 1:
                        if (type.contains("banner")) {
                            chooseImageFromGallery("banner");
                        } else {
                            chooseImageFromGallery("gallery");
                        }
                        break;
                }
            }
        });

        // create and show the alert dialog
        AlertDialog dialog = builder.create();
        dialog.show();
    }


    private void takeCameraImage(String type) {

        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

        File file = getOutputMediaFile(MEDIA_TYPE_IMAGE);
        System.out.println("has someting: " + file);
        if (file != null) {
            System.out.println("happened");
            imageStoragePath = file.getAbsolutePath();
        }

        Uri fileUri = getOutputMediaFileUri(getApplicationContext(), file);

        intent.putExtra(MediaStore.EXTRA_OUTPUT, fileUri);

        // start the image capture Intent
        if (type.contains("banner"))
            startActivityForResult(intent, BANNER_CAMERA_CAPTURE_IMAGE_REQUEST_CODE);
        if (type.contains("gallery"))
            startActivityForResult(intent, GALLERY_CAMERA_CAPTURE_IMAGE_REQUEST_CODE);

    }

    private void chooseImageFromGallery(String type) {

        Intent choosePicIntent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        choosePicIntent.setType("image/*");
        if (type.contains("gallery")) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR2) {
//                choosePicIntent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);
                choosePicIntent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, false);
            }
        }
        choosePicIntent.setAction(Intent.ACTION_GET_CONTENT);
        if (type.contains("banner"))
            startActivityForResult(Intent.createChooser(choosePicIntent, "Select Picture"), BANNER_GALLERY_CAPTURE_IMAGE_REQUEST_CODE);
        if (type.contains("gallery"))
            startActivityForResult(Intent.createChooser(choosePicIntent, "Select Picture"), GALLERY_GALLERY_CAPTURE_IMAGE_REQUEST_CODE);
    }

    private void fillGalleryList() {
        imgGalleryList = new ArrayList<>();
        imgAddGalleryImage = findViewById(R.id.img_create_event_add_extra_image);
        imgAddGalleryImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (imgGalleryList.size() < 5) {
                    showImagePickerOptions("gallery");
                } else {
                    imgAddGalleryImage.setBackgroundColor(getResources().getColor(R.color.colorDarkGrey));
                    Toast.makeText(getApplicationContext(), "You have reached max limit! Long press added pictures to delete!", Toast.LENGTH_LONG).show();
                }
            }
        });
    }


    private void setGalleryAdapter() {
        galleryAdapter = new CreateEventGalleryAdapter(imgGalleryList, this);
        galleryAdapter.setHasStableIds(true);
        galleryAdapter.setFuncOnItemClickListener(new CreateEventGalleryAdapter.InterfaceOnItemClicked() {
            @Override
            public void onStuffClicked(View view, int position) {
                Toast.makeText(getApplicationContext(), position + " got clicked", Toast.LENGTH_LONG).show();
            }
        });
    }

    private void setGalleryRecyclerView() {
        galleryRecyclerView = findViewById(R.id.recycler_create_event_add_more_pics);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getBaseContext());
        galleryRecyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        galleryRecyclerView.setHasFixedSize(true);
        galleryRecyclerView.setItemViewCacheSize(20);
        galleryRecyclerView.setDrawingCacheEnabled(true);
        galleryRecyclerView.setDrawingCacheQuality(View.DRAWING_CACHE_QUALITY_HIGH);
        galleryRecyclerView.setAdapter(galleryAdapter);
    }

    public void showDatePicker(final TextView datefield, Context context) {
// Get Current Date
        final Calendar c = Calendar.getInstance();
        int mYear = c.get(Calendar.YEAR);
        int mMonth = c.get(Calendar.MONTH);
        int mDay = c.get(Calendar.DAY_OF_MONTH);

        new DatePickerDialog(
                context,
                new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
//                        txtDate.setText(dayOfMonth + "/" + (monthOfYear + 1) + "/" + year);
                        c.set(Calendar.YEAR, year);
                        c.set(Calendar.MONTH, monthOfYear);
                        c.set(Calendar.DAY_OF_MONTH, dayOfMonth);

                        String myFormat = "dd/MM/yy";
                        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);
                        datefield.setText(sdf.format(c.getTime()));
                    }
                },
                mYear,
                mMonth,
                mDay).show();
    }

    public void showTimePicker(final TextView timeField) {
// Get Current Time
        final Calendar c = Calendar.getInstance();
        int mHour = c.get(Calendar.HOUR_OF_DAY);
        int mMinute = c.get(Calendar.MINUTE);

        // Launch Time Picker Dialog
        new TimePickerDialog(
                this,
                new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
//                        txtTime.setText(hourOfDay + ":" + minute);
                        c.set(Calendar.HOUR_OF_DAY, hourOfDay);
                        c.set(Calendar.MINUTE, minute);

                        String myFormat = "hh:mm aa";
                        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);
                        timeField.setText(sdf.format(c.getTime()));
                    }
                },
                mHour,
                mMinute,
                false).show();
    }

    private void eventTypeDialogFunc() {
        // setup the alert builder
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Select Event Type");

        final String[] selectArray = {"Free", "Paid"};
        builder.setItems(selectArray, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                switch (which) {
                    case 0:
                        eventTypeDialog.setText(selectArray[0]);
                        break;
                    case 1:
                        eventTypeDialog.setText(selectArray[1]);
                        break;
                }
            }
        });

        // create and show the alert dialog
        AlertDialog dialog = builder.create();
        dialog.show();
    }

    private void eventCategoryDialogFunc() {
        // setup the alert builder
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Select Event Category");

        final String[] selectArray = {"Conference", "Continuing Education", "Congress", "Convention", "Course", "Exhibition", "Expo", "Fundraiser", "International", "Lecture", "Networking", "Symposium", "Seminar", "Trade Show", "Webinar", "Workshop"};
        builder.setItems(selectArray, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                switch (which) {
                    case 0:
                        eventCategoryDialog.setText(selectArray[0]);
                        break;
                    case 1:
                        eventCategoryDialog.setText(selectArray[1]);
                        break;
                    case 2:
                        eventCategoryDialog.setText(selectArray[2]);
                        break;
                    case 3:
                        eventCategoryDialog.setText(selectArray[3]);
                        break;
                    case 4:
                        eventCategoryDialog.setText(selectArray[4]);
                        break;
                    case 5:
                        eventCategoryDialog.setText(selectArray[5]);
                        break;
                    case 6:
                        eventCategoryDialog.setText(selectArray[6]);
                        break;
                    case 7:
                        eventCategoryDialog.setText(selectArray[7]);
                        break;
                    case 8:
                        eventCategoryDialog.setText(selectArray[8]);
                        break;
                    case 9:
                        eventCategoryDialog.setText(selectArray[9]);
                        break;
                    case 10:
                        eventCategoryDialog.setText(selectArray[10]);
                        break;
                    case 11:
                        eventCategoryDialog.setText(selectArray[11]);
                        break;
                    case 12:
                        eventCategoryDialog.setText(selectArray[12]);
                        break;
                    case 13:
                        eventCategoryDialog.setText(selectArray[13]);
                        break;
                    case 14:
                        eventCategoryDialog.setText(selectArray[14]);
                        break;
                    case 15:
                        eventCategoryDialog.setText(selectArray[15]);
                        break;
                }
            }
        });

        // create and show the alert dialog
        AlertDialog dialog = builder.create();
        dialog.show();
    }

    private void eventFrequencyDialogFunc() {
        // setup the alert builder
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Select Event Frequency");

        final String[] selectArray = {"Occurs Once", "Daily", "Weekly", "Custom"};
        builder.setItems(selectArray, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                switch (which) {
                    case 0:
                        eventFrequencyDialog.setText(selectArray[0]);
                        break;
                    case 1:
                        eventFrequencyDialog.setText(selectArray[1]);
                        break;
                    case 2:
                        eventFrequencyDialog.setText(selectArray[2]);
                        break;
                    case 3:
                        eventFrequencyDialog.setText(selectArray[3]);
                        break;
                }
            }
        });

        // create and show the alert dialog
        AlertDialog dialog = builder.create();
        dialog.show();
    }

    private void initToolBar() {
        createEventToolbar = findViewById(R.id.toolbar_create_event);
        createEventToolbar.setTitleTextColor(getResources().getColor(R.color.colorWhite));
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            createEventToolbar.setElevation(10);
        }
        setSupportActionBar(createEventToolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle("Create Event");
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_create_event, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_create_event_done:
                finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    // Get uri from bitmap obj
    public Uri getImageUri(Context inContext, Bitmap inImage) {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        inImage.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
        String path = MediaStore.Images.Media.insertImage(inContext.getContentResolver(), inImage, "Title", null);
        return Uri.parse(path);
    }

    /**
     * Activity result method will be called after closing the camera
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        // if the result is capturing Image
        if (requestCode == BANNER_CAMERA_CAPTURE_IMAGE_REQUEST_CODE) {
            if (resultCode == RESULT_OK) {
                // Refreshing the gallery
                refreshGallery(getApplicationContext(), imageStoragePath);
                imgSetEventPicIndicator.setVisibility(View.GONE);
                tvSetEventPicIndicatorText.setVisibility(View.GONE);
                // successfully captured the image display it in image view
                previewCapturedImage();
            } else if (resultCode == RESULT_CANCELED) {
                // user cancelled Image capture
                Toast.makeText(getApplicationContext(), "User cancelled image capture", Toast.LENGTH_SHORT).show();
            } else {
                // failed to capture image
                Toast.makeText(getApplicationContext(), "Sorry! Failed to capture image", Toast.LENGTH_SHORT).show();
            }
        } else if (requestCode == BANNER_GALLERY_CAPTURE_IMAGE_REQUEST_CODE) {
            if (resultCode == RESULT_OK) {

                Uri selectedImage = data.getData();
                String[] filePathColumn = { MediaStore.Images.Media.DATA };

                Cursor cursor = getContentResolver().query(selectedImage, filePathColumn, null, null, null);
                cursor.moveToFirst();

                int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
                String picturePath = cursor.getString(columnIndex);
                cursor.close();

                Bitmap bmp = null;
                try {
                    ParcelFileDescriptor parcelFileDescriptor = getContentResolver().openFileDescriptor(selectedImage, "r");
                    FileDescriptor fileDescriptor = parcelFileDescriptor.getFileDescriptor();
                    Bitmap image = BitmapFactory.decodeFileDescriptor(fileDescriptor);
                    parcelFileDescriptor.close();
                    bmp = image;
                } catch (IOException e) {
                    e.printStackTrace();
                }
                imgSetEventHeaderPic.setImageBitmap(bmp);
                imgSetEventPicIndicator.setVisibility(View.GONE);
                tvSetEventPicIndicatorText.setVisibility(View.GONE);

            } else if (resultCode == RESULT_CANCELED) {
                // user cancelled choosing image from gallery
                Toast.makeText(getApplicationContext(), "User cancelled getting pic from gallery", Toast.LENGTH_SHORT).show();
            } else {
                // failed to choose image from gallery
                Toast.makeText(getApplicationContext(), "Sorry! Failed to get image from gallery", Toast.LENGTH_SHORT).show();
            }
        } else if (requestCode == GALLERY_CAMERA_CAPTURE_IMAGE_REQUEST_CODE) {
            if (resultCode == RESULT_OK) {
                refreshGallery(getApplicationContext(), imageStoragePath);
                imgGalleryList.add(new CreateEventGalleryModel(finalUri));
                galleryAdapter.notifyDataSetChanged();
            } else if (resultCode == RESULT_CANCELED) {
                // user cancelled Image capture
                Toast.makeText(getApplicationContext(), "User cancelled image capture", Toast.LENGTH_SHORT).show();
            } else {
                // failed to capture image
                Toast.makeText(getApplicationContext(), "Sorry! Failed to capture image", Toast.LENGTH_SHORT).show();
            }
        } else if (requestCode == GALLERY_GALLERY_CAPTURE_IMAGE_REQUEST_CODE) {
            if (resultCode == RESULT_OK) {
                Uri selectedImage = data.getData();
                String[] filePathColumn = { MediaStore.Images.Media.DATA };

                Cursor cursor = getContentResolver().query(selectedImage, filePathColumn, null, null, null);
                cursor.moveToFirst();

                int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
                String picturePath = cursor.getString(columnIndex);
                cursor.close();

                Bitmap bmp = null;
                try {
                    ParcelFileDescriptor parcelFileDescriptor = getContentResolver().openFileDescriptor(selectedImage, "r");
                    FileDescriptor fileDescriptor = parcelFileDescriptor.getFileDescriptor();
                    Bitmap image = BitmapFactory.decodeFileDescriptor(fileDescriptor);
                    parcelFileDescriptor.close();
                    bmp = image;
                } catch (IOException e) {
                    e.printStackTrace();
                }
                imgGalleryList.add(new CreateEventGalleryModel(getImageUri(getApplicationContext(), bmp)));
                galleryAdapter.notifyDataSetChanged();
            } else if (resultCode == RESULT_CANCELED) {
                // user cancelled recording
                Toast.makeText(getApplicationContext(), "User cancelled getting pic from gallery", Toast.LENGTH_SHORT).show();
            } else {
                // failed to record video
                Toast.makeText(getApplicationContext(), "Sorry! Failed to get image from gallery", Toast.LENGTH_SHORT).show();
            }
        }
    }
}
