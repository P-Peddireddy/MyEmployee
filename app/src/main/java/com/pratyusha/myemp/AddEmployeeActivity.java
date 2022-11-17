package com.pratyusha.myemp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import android.view.View;

import android.widget.ProgressBar;

import android.widget.Toast;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.pratyusha.myemp.data.ConnectionDetector;
import com.pratyusha.myemp.data.Employee;


public class AddEmployeeActivity extends AppCompatActivity {
    private TextInputEditText edFirstName , edLastname ,edDepartment, edBasePoint;
    private MaterialButton btSave;
    private  FirebaseDatabase firebaseDatabase;
    private DatabaseReference databaseReference;
    private StorageReference mStorageRef;
    private ProgressBar savePB;
    public long lngContactID=0;
    Employee emp;
    boolean isAllFieldsChecked = false;
    String strID= "";
    private static long tempId = 0;
    private static int RESULT_LOAD_IMG = 1;
    String imgDecodableString;
    private Uri selectedImage ;
    String strFileName="";
    private ConnectionDetector cd;
    private boolean isInternetPresent = false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_contact);
        bindID();
    }

    private void bindID() {
        edFirstName = findViewById(R.id.edFirstName);
        edLastname = findViewById(R.id.edLastName);
        edDepartment= findViewById(R.id.edDepartment);
        edBasePoint = findViewById(R.id.edBasePoint);
        btSave= findViewById(R.id.btSave);
        firebaseDatabase = FirebaseDatabase.getInstance("https://employee-3c3e1-default-rtdb.firebaseio.com/");
        mStorageRef = FirebaseStorage.getInstance().getReference("Employee");
        // find highest key
        // set tempId = highestKey+1
        emp = new Employee();
        cd = new ConnectionDetector(AddEmployeeActivity.this);
        isInternetPresent = cd.isConnectingToInternet();
        // on below line creating our database reference.
        databaseReference = firebaseDatabase.getReference().child("MyEmployee");
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                // on below line we are setting data in our firebase database.
                if(snapshot.exists())
                    lngContactID=(snapshot.getChildrenCount());
                }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                // displaying a failure message on below line.
            }
        });

        btSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                isAllFieldsChecked = CheckAllFields();
              if(isInternetPresent) {
                  if (isAllFieldsChecked) {
                      String strFirstName = edFirstName.getText().toString();
                      String strLastName = edLastname.getText().toString();
                      String strDepartment = edDepartment.getText().toString();
                      String strBasePoint = edBasePoint.getText().toString();
                      tempId++;
                              emp.setId(String.valueOf(tempId));
                              emp.setStrFirstName(strFirstName);
                              emp.setStrLastName(strLastName);
                              emp.setStrDepartment(strDepartment);
                              emp.setStrBasePoint(strBasePoint);
                              databaseReference.child(String.valueOf(tempId)).setValue(emp);
                              startActivity(new Intent(AddEmployeeActivity.this, MainActivity.class));
                              // displaying a toast message.
                              Toast.makeText(AddEmployeeActivity.this, "Employee Added", Toast.LENGTH_SHORT).show();
                  }
              }else
              {
                  Snackbar snackbar = Snackbar.make(AddEmployeeActivity.this.findViewById(android.R.id.content), "Check your connectivity", Snackbar.LENGTH_LONG);
                  snackbar.show();
              }
            }
        });

    }


    private boolean CheckAllFields() {
        if (edFirstName.length() == 0) {
            edFirstName.setError("Enter name");
            return false;
        }

        if (edLastname.length() == 0) {
            edLastname.setError("Enter Last Name");
            return false;
        }

        if (edDepartment.length() == 0) {
            edDepartment.setError("Enter Department");
            return false;

        }
        if (edBasePoint.length() == 0) {
            edBasePoint.setError("Enter Base Point");
            return false;

        }

        return true;
    }


}