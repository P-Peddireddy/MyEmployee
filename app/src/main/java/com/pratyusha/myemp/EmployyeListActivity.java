package com.pratyusha.myemp;

import static android.content.ContentValues.TAG;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.util.Patterns;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.pratyusha.myemp.data.ConnectionDetector;
import com.pratyusha.myemp.data.Employee;


public class EmployyeListActivity extends AppCompatActivity {
    private TextInputEditText tvName ,tvDepartment;
    TextInputEditText edBasePoint,edFirstName;
    ImageView ivProfile;
    MaterialButton btUpdate;
    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;
    String strID ;
    private Uri selectedImage ;
    String strFileName="";
    TextView tvUpload;
    boolean isAllFieldsChecked = false;
    StorageReference mStorageRef;
    String strImage ,strImagName;
    LinearLayout lvCall,lvEmail,lvAddress;
    private static int RESULT_UPDATE_IMG = 1;
    private ConnectionDetector cd;
    private boolean isInternetPresent = false;
    private ProgressDialog progress;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(
                R.layout.activity_contact_details);
        bindID();
    }

    private void bindID() {
        tvName= findViewById(R.id.tvName);
        tvDepartment= findViewById(R.id.tvDepartment);
        edBasePoint = findViewById(R.id.edBasePoint);
        edFirstName= findViewById(R.id.edFirstName);
        btUpdate= findViewById(R.id.btUpdate);
        cd = new ConnectionDetector(EmployyeListActivity.this);
        progress=new ProgressDialog(this);
        isInternetPresent = cd.isConnectingToInternet();
         setData();
      btUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(isInternetPresent) {
                    isAllFieldsChecked = CheckAllFields();
                    if (isAllFieldsChecked) {
                        Employee emp = new Employee();
                        emp.setId(getIntent().getExtras().getString("ID"));
                        emp.setStrFirstName(edFirstName.getText().toString().trim());
                        emp.setStrLastName(tvName.getText().toString().trim());
                        emp.setStrDepartment(tvDepartment.getText().toString().trim());
                        emp.setStrBasePoint(edBasePoint.getText().toString().trim());
                        databaseReference.setValue(emp);
                        Snackbar snackbar = Snackbar.make(EmployyeListActivity.this.findViewById(android.R.id.content), "Employee Updated", Snackbar.LENGTH_SHORT);
                        snackbar.show();

                        Intent intent = new Intent(EmployyeListActivity.this, MainActivity.class);
                        startActivity(intent);
                    } else {
                        Snackbar snackbar = Snackbar
                                .make(EmployyeListActivity.this.findViewById(android.R.id.content), "Please connect to Internet first", Snackbar.LENGTH_LONG);
                        snackbar.show();
                    }
                }
            }
        });

    }

    private void setData() {
        strID = getIntent().getExtras().getString("ID");
        String strDep = getIntent().getExtras().getString("emp_Dep");
        String strBase = getIntent().getExtras().getString("emp_Base");
        String strFirstName = getIntent().getExtras().getString("emp_Firstname");
        String strLastName = getIntent().getExtras().getString("emp_LastName");

        firebaseDatabase = FirebaseDatabase.getInstance("https://employee-3c3e1-default-rtdb.firebaseio.com/");
        databaseReference = firebaseDatabase.getReference("MyEmployee").child(strID);
        tvName.setText(strLastName.toString());
        tvDepartment.setText(strDep.toString());
        edBasePoint.setText(strBase.toString());
        edFirstName.setText(strFirstName);
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.menu_main , menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId())
        {
            case R.id.action_delete:
                MaterialAlertDialogBuilder materialAlertDialogBuilder = new MaterialAlertDialogBuilder(EmployyeListActivity.this);
                materialAlertDialogBuilder.setTitle("Are you sure want to Remove this Employee ?");
                materialAlertDialogBuilder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        Query applesQuery = databaseReference.child("Employee").orderByChild("id").equalTo(strID);

                        applesQuery.addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {
                                for (DataSnapshot appleSnapshot: dataSnapshot.getChildren()) {
                                    appleSnapshot.getRef().removeValue();
                                }
                            }

                            @Override
                            public void onCancelled(DatabaseError databaseError) {
                                Log.e(TAG, "onCancelled", databaseError.toException());
                            }
                        });

                        databaseReference.removeValue();
                        // displaying a toast message on below line.
                        // opening a main activity on below line.
                        startActivity(new Intent(EmployyeListActivity.this, MainActivity.class));
                        Snackbar.make(EmployyeListActivity.this.findViewById(android.R.id.content) , "Done",Snackbar.LENGTH_LONG).show();
                    }
                });
                materialAlertDialogBuilder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                    }
                });
//        materialAlertDialogBuilder.setBackground(getResources().get)
                materialAlertDialogBuilder.show();
        }
        return super.onOptionsItemSelected(item);
    }

    private boolean CheckAllFields() {
        if (edFirstName.length() == 0) {
            edFirstName.setError("Enter name");
            return false;
        }



        if (tvDepartment.length() == 0) {
            tvDepartment.setError("Enter Department");
            return false;

        }
        if (edBasePoint.length() == 0) {
            edBasePoint.setError("Enter Base Point");
            return false;

        }

        return true;
    }

}

