package com.example.mycontact;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import java.util.ArrayList;

public class contactAdapter extends RecyclerView.Adapter<vh> implements Filterable {

    ArrayList<contactModel> contacts;
    ArrayList<contactModel> backup;
    Context context;

    //THIS IS A CONSTRUCTER, USED TO GET ARRAY LIST DATA & CONTEXT FROM CALLING ACTIVITY
    public contactAdapter(ArrayList<contactModel> contacts, Context context) {
        this.contacts = contacts;
        this.context = context;
        backup=new ArrayList<>(contacts);
    }


    @NonNull
    @Override
    public vh onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        LayoutInflater layoutInflater=LayoutInflater.from(parent.getContext());
        View view = layoutInflater.inflate(R.layout.contact_sample_layout,parent,false);
        return new vh(view);
    }


    @Override
    public void onBindViewHolder(@NonNull vh holder,  int position) {
        final contactModel temp =contacts.get(position);
        holder.name.setText(contacts.get(position).getName());
        holder.number.setText(contacts.get(position).getNumber());
        holder.image.setImageResource(R.drawable.user_default);


        holder.constraintLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(context, profile.class);
                intent.putExtra("image",R.drawable.user_default);
                intent.putExtra("name",temp.getName());
                intent.putExtra("number",temp.getNumber());

                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(intent);
            }
        });
        holder.message.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(Intent.ACTION_SENDTO);
                intent.setData(Uri.parse("smsto:"+Uri.encode(temp.getNumber())));

                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(intent);
            }
        });
        holder.call.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(Intent.ACTION_CALL);
                intent.setData(Uri.parse("tel:"+temp.getNumber()));

                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(intent);
            }
        });

        holder.constraintLayout.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {

                LinearLayout constraintLayout =v.findViewById(R.id.showdpdailog);
                View view=LayoutInflater.from(context).inflate(R.layout.edit_delete_layout,null);

                ImageView dp =view.findViewById(R.id.editDp);
                TextView removedp =view.findViewById(R.id.removeDp);
                TextView changedp=view.findViewById(R.id.changeDp);
                ImageView delete=view.findViewById(R.id.deleteContact);
                TextView saveBtn=view.findViewById(R.id.saveBtn);
                EditText etName =view.findViewById(R.id.etName);
                EditText etNumber=view.findViewById(R.id.etNumber);

                AlertDialog.Builder builder=new AlertDialog.Builder(holder.itemView.getContext());
                builder.setView(view);

                AlertDialog alertDialog=builder.create();

                dp.setImageResource(R.drawable.user_default);
                etName.setText(temp.getName());
                etNumber.setText(temp.getNumber());
                delete.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        alertDialog.dismiss();
                        LinearLayout linearLayout=v.findViewById(R.id.deletedailog);
                        View view1 =LayoutInflater.from(context).inflate(R.layout.delete_confirm,null);
                        TextView yes =view1.findViewById(R.id.yesDelete);
                        TextView no = view1.findViewById(R.id.noDelete);
                        AlertDialog.Builder builder1=new AlertDialog.Builder(holder.itemView.getContext());
                        builder1.setView(view1);
                        AlertDialog alertDialog1=builder1.create();
                        yes.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {

                                Boolean isDelete = new contactDB(context).deleteItem(temp.getId());
                                if (isDelete){
                                    contacts.remove(position);
                                    notifyItemRemoved(position);
                                    notifyItemRangeChanged(position,contacts.size());
                                    Toast.makeText(context, "Contact Deleted", Toast.LENGTH_SHORT).show();
                                }
                                else {
                                    Toast.makeText(context, "ERROR IN DELETING", Toast.LENGTH_SHORT).show();
                                }
                                alertDialog1.dismiss();
                            }
                        });

                       no.setOnClickListener(new View.OnClickListener() {
                           @Override
                           public void onClick(View v) {
                               alertDialog1.dismiss();
                           }
                       });

                        alertDialog1.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
                        alertDialog1.show();
                    }
                });

                removedp.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                       temp.setImage(R.drawable.user_default);
                       notifyItemChanged(position);
                        alertDialog.dismiss();
                    }
                });
                saveBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        String name1="",number1="";
                        if (!etName.getText().toString().equals("")){
                            name1=etName.getText().toString();
                        }
                        if (!etNumber.getText().toString().equals("")){
                            number1=etNumber.getText().toString();
                        }
                        if (name1.equals("")){
                            Toast.makeText(context, "Please enter contact name", Toast.LENGTH_LONG).show();
                        } else if (number1.equals("")) {
                            Toast.makeText(context, "Please enter contact number", Toast.LENGTH_LONG).show();
                        }
                        else {
                            Boolean isUpdate = new contactDB(context).updateContact(temp.getId(),name1,number1);
                            if (isUpdate){
                                contacts.get(position).setName(name1);
                                contacts.get(position).setNumber(number1);
                                notifyItemChanged(position);
                                Toast.makeText(context, "Contact Updated", Toast.LENGTH_SHORT).show();
                            }
                            else {
                                Toast.makeText(context, "Error in updating contact", Toast.LENGTH_SHORT).show();
                            }
                        }
                        alertDialog.dismiss();
                    }
                });
                alertDialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
                alertDialog.show();
                return true;
            }
        });
        holder.image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LinearLayout cardView=v.findViewById(R.id.dp_dailog);
                View view2=LayoutInflater.from(context).inflate(R.layout.dp_layout,null);

                ImageView imageView=view2.findViewById(R.id.bigdp);
                TextView bigdpName=view2.findViewById(R.id.bigdpName);

                AlertDialog.Builder builder2=new AlertDialog.Builder(holder.itemView.getContext());
                builder2.setView(view2);
                AlertDialog alertDialog2=builder2.create();

                bigdpName.setText(temp.getName());
                imageView.setImageResource(R.drawable.user_default);
                alertDialog2.getWindow().setBackgroundDrawableResource(android.R.color.transparent);

                alertDialog2.show();
            }
        });
    }

    @Override //IN THIS METHOD WE HAVE SET THE SIZE OF OUR ARRAY LIST
    public int getItemCount() {
        return contacts.size();
    }
    @Override
    public Filter getFilter() {
        return filter;
    }
    Filter filter=new Filter() {
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {

            ArrayList<contactModel> filteredData=new ArrayList<>();
            if (constraint.toString().isEmpty()){
                filteredData.addAll(backup);
            }
            else {
                for (contactModel obj : backup){
                    if (obj.getName().toString().toLowerCase().contains(constraint.toString().toLowerCase())){
                        filteredData.add(obj);
                    }
                }
            }
            FilterResults filterResults=new FilterResults();
            filterResults.values=filteredData;

            return filterResults;
        }
        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {

            contacts.clear();
            contacts.addAll((ArrayList<contactModel>)results.values);
            notifyDataSetChanged();
        }
    };
}
class vh extends RecyclerView.ViewHolder{

    ImageView image,message,call;
    TextView name,number;
   ConstraintLayout constraintLayout;
    public vh(@NonNull View itemView) {
        super(itemView);

        //HERE I FIND ID'S OF ALL ELEMENT USED IN RECYCLER VIEW SAMPLE LAYOUT
        image= itemView.findViewById(R.id.image);
        name=itemView.findViewById(R.id.userName);
        number=itemView.findViewById(R.id.userNumber);
        message=itemView.findViewById(R.id.messageIcon);
        call=itemView.findViewById(R.id.callIcon);
        constraintLayout=itemView.findViewById(R.id.layout);
    }
}
