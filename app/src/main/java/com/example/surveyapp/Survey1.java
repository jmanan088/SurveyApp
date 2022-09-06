 package com.example.surveyapp;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.drawable.BitmapDrawable;
import android.icu.text.SimpleDateFormat;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.UUID;

 public class Survey1 extends AppCompatActivity {

     public static final int Camera_request = 101;
     public static final int Open_cam_req = 102;
     public static final int REQUEST_LOCATION = 12;
     private static final int REQUEST_CODE = 56;
     private Button but;
     private FirebaseAuth auth;
     private TextView usr, click,caption;
     private Button addLocation,send;
     LocationManager locationManager;
     private ImageView img;
     String latitude,longitude;
     String currentPhotoPath;
     EditText nam,email,gender,number,marital,village, address, dob,family_income;
     String name, eml, gndr,mrgsts,vllg,addrs,dateofbirth,income,phone;
     boolean loca,pic;
     FirebaseStorage storage;
     StorageReference storageReference;
     Bitmap processedBitmap;
     Uri uploads;
     FusedLocationProviderClient fusedLocationProviderClient;
     String fulladdress;
     MyLocationProvider provider;
     @Override
     protected void onCreate(Bundle savedInstanceState) {
         super.onCreate(savedInstanceState);
         setContentView(R.layout.activity_survey1);
         locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
         loca=false;
         pic=false;
         storage=FirebaseStorage.getInstance();
         storageReference=storage.getReference();
         nam=findViewById(R.id.name);
         email= findViewById(R.id.email_can);
         gender= findViewById(R.id.gender);
         number= findViewById(R.id.phone);
         marital= findViewById(R.id.marital);
         village= findViewById(R.id.village);
         address=findViewById(R.id.address);
         dob=findViewById(R.id.dob);
         family_income= findViewById(R.id.income);
         usr = findViewById(R.id.textView);
         click = findViewById(R.id.click);
         send = findViewById(R.id.submit);
         addLocation = findViewById(R.id.location);
         img = findViewById(R.id.img);
         send.setEnabled(false);
         caption=findViewById(R.id.caption);
         fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);
         click.setOnClickListener(new View.OnClickListener() {
             @RequiresApi(api = Build.VERSION_CODES.N)
             @Override
             public void onClick(View view) {
                 askCameraPermission();
             }
         });
         addLocation.setOnClickListener(new View.OnClickListener() {
             @Override
             public void onClick(View view) {

                 if (ActivityCompat.checkSelfPermission(Survey1.this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                     getLocation();
                 }
                 else {
                     ActivityCompat.requestPermissions(Survey1.this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 44);
                 }
             }
         });
         send.setOnClickListener(new View.OnClickListener() {
             @Override
             public void onClick(View view) {
                   // Database
                 name = String.valueOf(nam.getText());
                 eml = String.valueOf(email.getText());
                 gndr = String.valueOf(gender.getText());
                 vllg = String.valueOf(village.getText());
                 income = String.valueOf(family_income.getText());
                 mrgsts = String.valueOf(marital.getText());
                 addrs = String.valueOf(address.getText());
                 dateofbirth = String.valueOf(dob.getText());
                 phone = String.valueOf(number.getText());
//
                 if (( TextUtils.isEmpty(name)|| TextUtils.isEmpty(eml)||TextUtils.isEmpty(vllg)||TextUtils.isEmpty(gndr)||TextUtils.isEmpty(income)||TextUtils.isEmpty(mrgsts)||TextUtils.isEmpty(addrs)||TextUtils.isEmpty(phone)||TextUtils.isEmpty(dateofbirth))){
                     Toast.makeText(Survey1.this, "Please Enter Complete Data.", Toast.LENGTH_SHORT).show();

                 }
                 else{
                     // Image storage
                     Toast.makeText(Survey1.this, "Starting uploads", Toast.LENGTH_SHORT).show();
                     StorageReference reference=storageReference.child("pic/"+ UUID.randomUUID().toString());
                     reference.putFile(getImageUri(Survey1.this,processedBitmap)).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                         @Override
                         public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                             Toast.makeText(Survey1.this, "Upload successful!", Toast.LENGTH_SHORT).show();
                             reference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                 @Override
                                 public void onSuccess(Uri uri) {
                                     uploads=uri;
                                     Map<String,String> v = new HashMap<>();
                                     v.put("name",name);
                                     v.put("email",eml);
                                     v.put("gender",gndr);
                                     v.put("village",vllg);
                                     v.put("family_income",income);
                                     v.put("marital income",mrgsts);
                                     v.put("address",addrs);
                                     v.put("date of birth",dateofbirth);
                                     v.put("phone number",phone);
                                     v.put("latitude",latitude);
                                     v.put("longitude",longitude);
                                     v.put("image",String.valueOf(uploads));
                                     FirebaseFirestore.getInstance().collection("data").document(""+UUID.randomUUID().toString()).set(v).addOnSuccessListener(new OnSuccessListener<Void>() {
                                         @Override
                                         public void onSuccess(Void unused) {
                                             Toast.makeText(Survey1.this, "Data Submitted!", Toast.LENGTH_SHORT).show();
                                             Intent i = new Intent(Survey1.this,MainActivity.class);
                                             startActivity(i);
                                             finish();
                                         }
                                     }).addOnFailureListener(new OnFailureListener() {
                                         @Override
                                         public void onFailure(@NonNull Exception e) {
                                             Toast.makeText(Survey1.this, "Failed!", Toast.LENGTH_SHORT).show();
                                         }
                                     });
                                 }
                             });
                         }
                     }).addOnFailureListener(new OnFailureListener() {
                         @Override
                         public void onFailure(@NonNull Exception e) {
                             Toast.makeText(Survey1.this, "Upload failed!", Toast.LENGTH_SHORT).show();
                         }
                     });

                 }
             }
         });
     }

     public Uri getImageUri(Context inContext, Bitmap inImage) {
         ByteArrayOutputStream bytes = new ByteArrayOutputStream();
         inImage.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
         String path = MediaStore.Images.Media.insertImage(inContext.getContentResolver(), inImage, "Title", null);
         return Uri.parse(path);
     }

     @RequiresApi(api = Build.VERSION_CODES.N)
     private void askCameraPermission() {
         if(ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA)!= PackageManager.PERMISSION_GRANTED){

             ActivityCompat.requestPermissions(this, new String[] {Manifest.permission.CAMERA}, Camera_request);
         }
         else{
             dispatchTakePictureIntent();
         }
     }

     @RequiresApi(api = Build.VERSION_CODES.N)
     @Override
     public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
         super.onRequestPermissionsResult(requestCode, permissions, grantResults);
         if(requestCode == Camera_request){
             if(grantResults.length > 0 && grantResults[0]==PackageManager.PERMISSION_GRANTED){
                 dispatchTakePictureIntent();
             }
             else{
                 Toast.makeText(this, "Camera permission required to use camera.", Toast.LENGTH_SHORT).show();
             }
         }
     }

     @Override
     protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
         super.onActivityResult(requestCode, resultCode, data);
         if(requestCode==Open_cam_req){
             if(resultCode == Activity.RESULT_OK){
                 File f = new File(currentPhotoPath);
                 img.setImageURI(Uri.fromFile(f));
             }
         }
         else if(requestCode == MyLocation.REQUEST_CHECK_SETTINGS){
             if(resultCode == RESULT_OK){
                 provider.onLocationRequestAllowed();
             }
             else{
                 Toast.makeText(this, "Location is not allowed", Toast.LENGTH_SHORT).show();
             }
         }
     }

     //android documentation code

     @RequiresApi(api = Build.VERSION_CODES.N)
     private File createImageFile() throws IOException {
         // Create an image file name
         String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
         String imageFileName = "JPEG_" + timeStamp + "_";
         File storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
         File image = File.createTempFile(
                 imageFileName,  /* prefix */
                 ".jpg",         /* suffix */
                 storageDir      /* directory */
         );

         // Save a file: path for use with ACTION_VIEW intents
         currentPhotoPath = image.getAbsolutePath();
         return image;
     }


     @RequiresApi(api = Build.VERSION_CODES.N)
     private void dispatchTakePictureIntent() {
         Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
         // Ensure that there's a camera activity to handle the intent
         if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
             // Create the File where the photo should go
             File photoFile = null;
             try {
                 photoFile = createImageFile();
             } catch (IOException ex) {
                 // Error occurred while creating the File
             }
             // Continue only if the File was successfully created
             if (photoFile != null) {
                 Uri photoURI = FileProvider.getUriForFile(this,
                         "com.example.android.fireproof",
                         photoFile);
                 takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
                 pic=true;
                 startActivityForResult(takePictureIntent, Open_cam_req);
             }
         }
     }

     private Bitmap ProcessingBitmap(){
         Bitmap bm1 = null;
         Bitmap newBitmap = null;

         //             bm1 = BitmapFactory.decodeStream(
//                     getContentResolver().openInputStream(source1));
         bm1 = ((BitmapDrawable)img.getDrawable()).getBitmap();

         Bitmap.Config config = bm1.getConfig();
         if(config == null){
             config = Bitmap.Config.ARGB_8888;
         }

         newBitmap = Bitmap.createBitmap(bm1.getWidth(), bm1.getHeight(), config);
         Canvas newCanvas = new Canvas(newBitmap);

         newCanvas.drawBitmap(bm1, 0, 0, null);

//         String captionString = caption.getText().toString();
         String captionString = "Longitude"+longitude+"Latitude"+latitude;
         if(captionString != null){

             Paint paintText = new Paint(Paint.ANTI_ALIAS_FLAG);
             paintText.setColor(Color.BLACK);
             paintText.setTextSize(200);
             paintText.setStrokeWidth(17);
//                paintText
             paintText.setStyle(Paint.Style.FILL_AND_STROKE);
//                paintText.setShadowLayer(10f, 10f, 10f, Color.BLACK);

             Rect rectText = new Rect();
             paintText.getTextBounds(captionString, 0, captionString.length(), rectText);

             newCanvas.drawText(captionString,
                     0,  rectText.height(), paintText);
             newCanvas.drawText("jiojhiuj",
                     0,  rectText.height()+10, paintText);

             loca = true;
         }
         else{
             Toast.makeText(getApplicationContext(),
                     "Try Again!",
                     Toast.LENGTH_LONG).show();
         }
         return newBitmap;
     }

     private void getLocation() {
         Log.d("LocationDebugging","Inside Function");
         /*if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
             Log.d("LocationDebugging","Location Denied");
             return;
         }

         fusedLocationProviderClient.getLastLocation().addOnCompleteListener(new OnCompleteListener<Location>() {
            @Override
             public void onComplete(@NonNull Task<Location> task) {
                Log.d("LocationDebugging","Inside Complete");
                 Location location = task.getResult();
                 if (location != null) {
                     try {
                         Geocoder geocoder = new Geocoder(Survey1.this, Locale.getDefault());
                         List<Address> addresses = geocoder.getFromLocation(location.getLatitude(), location.getLongitude(), 1);
                         longitude= String.valueOf(addresses.get(0).getLongitude());
                         latitude = String.valueOf(addresses.get(0).getLatitude());
                         fulladdress = addresses.get(0).getAddressLine(0);
                         caption.setText("Lattitide:"+latitude+"\nLongitude:"+longitude);
                         processedBitmap = ProcessingBitmap();
                         if(processedBitmap != null){
                             img.setImageBitmap(processedBitmap);
                             Toast.makeText(getApplicationContext(),
                                     "Done",
                                     Toast.LENGTH_LONG).show();
                         }else{
                             Toast.makeText(getApplicationContext(),
                                     "Something wrong in processing!",
                                     Toast.LENGTH_LONG).show();
                         }
                         if(loca && pic){
                             send.setEnabled(true);
                         }
                     }
                     catch (IOException e) {
                         Log.d("LocationDebugging","Crash");
                         Log.d("LocationDebugging",e.getMessage());
                         e.printStackTrace();
                     }
                 }
                 else{
                     Log.d("LocationDebugging","Location is null");
                 }
             }
         });*/
         provider=new MyLocationProvider();
         provider.getUserLastLocation(this, new MyLocationListener() {
             @Override
             public void onLocationReceived(Location location) {
                 if (location != null) {
                     try {
                         Geocoder geocoder = new Geocoder(Survey1.this, Locale.getDefault());
                         List<Address> addresses = geocoder.getFromLocation(location.getLatitude(), location.getLongitude(), 1);
                         longitude= String.valueOf(addresses.get(0).getLongitude());
                         latitude = String.valueOf(addresses.get(0).getLatitude());
                         fulladdress = addresses.get(0).getAddressLine(0);
                         caption.setText("Lattitide:"+latitude+"\nLongitude:"+longitude);
                         processedBitmap = ProcessingBitmap();
                         if(processedBitmap != null){
                             img.setImageBitmap(processedBitmap);
                             Toast.makeText(getApplicationContext(),
                                     "Done",
                                     Toast.LENGTH_LONG).show();
                         }else{
                             Toast.makeText(getApplicationContext(),
                                     "Something wrong in processing!",
                                     Toast.LENGTH_LONG).show();
                         }
                         if(loca && pic){
                             send.setEnabled(true);
                         }
                     }
                     catch (IOException e) {
                         Log.d("LocationDebugging","Crash");
                         Log.d("LocationDebugging",e.getMessage());
                         e.printStackTrace();
                     }
                 }
                 else{
                     Log.d("LocationDebugging","Location is null");
                 }

             }
         });
     }
}