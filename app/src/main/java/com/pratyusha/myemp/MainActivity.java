package com.pratyusha.myemp;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.github.clans.fab.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.annotations.Nullable;
import com.pratyusha.myemp.adapter.EmployeeAdapter;
import com.pratyusha.myemp.data.ConnectionDetector;
import com.pratyusha.myemp.data.Employee;


import java.util.ArrayList;
import java.util.Collections;

public class MainActivity extends AppCompatActivity {
    FloatingActionButton fabAddNew , fabDialer , fabEdit;
    private SwipeRefreshLayout pullToRefresh ;
    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;
    private RecyclerView rvEmp;
    TextView tvWinner;
    private ArrayList<Employee> arrEmp;
    private EmployeeAdapter apEmp;
    private ProgressBar igProgress;
    private ConnectionDetector cd;
    private boolean isInternetPresent = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        bindID();
    }
    private void bindID() {
        fabAddNew = findViewById(R.id.fabAddNew);
        tvWinner = findViewById(R.id.tvWinner);
        fabDialer = findViewById(R.id.fabDialer);
        fabEdit = findViewById(R.id.fabEdit);
        rvEmp= findViewById(R.id.rvContact);
        igProgress= findViewById(R.id.igProgress);
        pullToRefresh = findViewById(R.id.pullToRefresh);
        cd = new ConnectionDetector(MainActivity.this);
        isInternetPresent = cd.isConnectingToInternet();
        arrEmp = new ArrayList<>();
        if (isInternetPresent) {
            setData();
        } else {
            Snackbar snackbar = Snackbar.make(MainActivity.this.findViewById(android.R.id.content), "Please Check your connectivity.", Snackbar.LENGTH_LONG);
            snackbar.show();
            igProgress.setVisibility(View.GONE);

        }

        pullToRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                if (isInternetPresent) {
                    setData();
                    pullToRefresh.setRefreshing(false);
                }
                else {
                    Snackbar snackbar = Snackbar.make(MainActivity.this.findViewById(android.R.id.content), "Please Check your connectivity.", Snackbar.LENGTH_LONG);
                    snackbar.show();
                    igProgress.setVisibility(View.GONE);
                    pullToRefresh.setRefreshing(false);
                }
            }
        });
        fabAddNew.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this,AddEmployeeActivity.class);
                startActivity(intent);
            }
        });


        fabEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, EmployyeListActivity.class);
                startActivity(intent);
            }
        });

    }

    private void setData() {
        firebaseDatabase = FirebaseDatabase.getInstance("https://employee-3c3e1-default-rtdb.firebaseio.com/");
        // on below line we are getting database reference.

        databaseReference = firebaseDatabase.getReference().child("MyEmployee");
 getContact();

    }
private void setWinnweText()
{
    try {
        String name = arrEmp.get(0).getStrFirstName();
        if (name.length() == 0) {
            tvWinner.setText("Please add Employee to see winner");

        } else {
            tvWinner.setText(name + " is Leading");
        }
    }catch (Exception e)
    {
        tvWinner.setText("Please add Employee to see winner");
    }

}

    private void getContact() {
        arrEmp.clear();
        igProgress.setVisibility(View.GONE);
        Collections.sort(arrEmp,Employee.idasc);
        setWinnweText();
        rvEmp.setAdapter(apEmp);
        apEmp = new EmployeeAdapter(arrEmp,MainActivity.this);
        rvEmp.setLayoutManager(new LinearLayoutManager(MainActivity.this));

        apEmp.notifyDataSetChanged();

        // on below line we are calling add child event listener method to read the data.
        databaseReference.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                // on below line we are hiding our progress bar.
                igProgress.setVisibility(View.GONE);
                // adding snapshot to our array list on below line.
                arrEmp.add(snapshot.getValue(Employee.class));
               String strName =  snapshot.getKey().toString();

                // setting layout malinger to recycler view on below line.
                rvEmp.setLayoutManager(new LinearLayoutManager(MainActivity.this));
                // setting adapter to recycler view on below line.
                Collections.sort(arrEmp,Employee.idasc);
                setWinnweText();
                rvEmp.setAdapter(apEmp);
//
                // notifying our adapter that data has changed.
                apEmp.notifyDataSetChanged();
//                itCount =  apEmp.getItemCount();
//                SharedPrefManager.putInt("ID",itCount);
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                // this method is called when new child is added
                // we are notifying our adapter and making progress bar
                // visibility as gone.
                igProgress.setVisibility(View.GONE);
                Collections.sort(arrEmp,Employee.idasc);
                setWinnweText();

                apEmp.notifyDataSetChanged();
//                itCount =  apEmp.getItemCount();
//                SharedPrefManager.putInt("ID",itCount);
            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot snapshot) {
                // notifying our adapter when child is removed.
                Collections.sort(arrEmp,Employee.idasc);
                setWinnweText();


                apEmp.notifyDataSetChanged();
                igProgress.setVisibility(View.GONE);
//                itCount =  apEmp.getItemCount();
//                SharedPrefManager.putInt("ID",itCount);

            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                // notifying our adapter when child is moved.
             Collections.sort(arrEmp,Employee.idasc);
                apEmp.notifyDataSetChanged();
                setWinnweText();

                igProgress.setVisibility(View.GONE);
//                itCount =  apEmp.getItemCount();
//                SharedPrefManager.putInt("ID",itCount);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Collections.sort(arrEmp,Employee.idasc);

                apEmp.notifyDataSetChanged();
                setWinnweText();

                igProgress.setVisibility(View.GONE);
//                itCount =  apEmp.getItemCount();
//                SharedPrefManager.putInt("ID",itCount);
            }
        });
    }


    private void onContactClick(int i) {

    }
}