package com.pratyusha.myemp.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;


import com.pratyusha.myemp.EmployyeListActivity;
import com.pratyusha.myemp.data.Employee;
import com.pratyusha.myemp.R;


import java.util.ArrayList;
import java.util.Comparator;

public class EmployeeAdapter extends RecyclerView.Adapter<EmployeeAdapter.ViewHolder> {
    // creating variables for our list, context, interface and position.
    private ArrayList<Employee> arrEmpList;
    private Context context;
    int lastPos = -1;

    // creating a constructor.
    public EmployeeAdapter(ArrayList<Employee> arrEmpList, Context context) {
        this.arrEmpList = arrEmpList;
        this.context = context;
    }




    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // inflating our layout file on below line.
        View view = LayoutInflater.from(context).inflate(R.layout.contact_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, @SuppressLint("RecyclerView") int position) {
        // setting data to our recycler view item on below line.
        Employee emp = arrEmpList.get(position);
        holder.tvEmpoyeeName.setText(emp.getStrFirstName() +" " +emp.getStrLastName());
        holder.tvDepartment.setText(emp.getStrDepartment());
        holder.tvEmpoyeeBasePoint.setText("Point : " +emp.getStrBasePoint());

        holder.imgEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(context, EmployyeListActivity.class);
                // on below line we are passing our contact modal
                i.putExtra("ID", emp.getId().toString());
                i.putExtra("emp_FullName", emp.getStrFirstName().toString() + " " + emp.getStrLastName().toString());
                i.putExtra("emp_Dep", emp.getStrDepartment().toString() );
                i.putExtra("emp_Base", emp.getStrBasePoint().toString() );
               i.putExtra("emp_Firstname", emp.getStrFirstName().toString());
                i.putExtra("emp_LastName", emp.getStrLastName().toString() );
                context.startActivity(i);

            }
        });


//        Picasso.get().load(contact.get()).into(holder.courseIV);
        // adding animation to recycler view item on below line.
        setAnimation(holder.itemView, position);
    }


    private void setAnimation(View itemView, int position) {
        if (position > lastPos) {
            // on below line we are setting animation.
            Animation animation = AnimationUtils.loadAnimation(context, android.R.anim.slide_in_left);
            itemView.setAnimation(animation);
            lastPos = position;
        }
    }
  private   void sort()

    {
        new Comparator<Employee>() {
            @Override
            public int compare(Employee employee, Employee t1) {

                Double d1 = Double.parseDouble(employee.getStrBasePoint());
                Double d2 = Double.parseDouble(t1.getStrBasePoint());
                return Double.compare(d1, d2);
            }
        };
    }


    @Override
    public int getItemCount() {
        return arrEmpList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        // creating variable for our image view and text view on below line.
        private ImageView idIVContact,imgCall,imgEdit;
        private TextView tvEmpoyeeName,tvDepartment,tvEmpoyeeBasePoint;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            // initializing all our variables on below line.
            tvEmpoyeeName = itemView.findViewById(R.id.tvEmpoyeeName);
            tvDepartment = itemView.findViewById(R.id.tvDepartment);
            tvEmpoyeeBasePoint = itemView.findViewById(R.id.tvEmpoyeeBasePoint);
            imgEdit= itemView.findViewById(R.id.imgEdit);
        }
    }

    // creating a interface for on click

}
