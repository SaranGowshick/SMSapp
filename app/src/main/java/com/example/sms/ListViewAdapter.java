package com.example.sms;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.RequiresApi;

import java.util.List;

class ListViewAdopter extends ArrayAdapter<UserModel> {

    private final List<UserModel> UserModelList;
    //the hero list that will be displayed

    //the context object
    private final Context mCtx;

    //here we are getting the herolist and context
    //so while creating the object of this adapter class we need to give herolist and context
    public ListViewAdopter(List<UserModel> UserModelList, Context mCtx) {
        super(mCtx, R.layout.messages_list_view, UserModelList);
        this.UserModelList = UserModelList;
        this.mCtx = mCtx;
    }

    //this method will return the list item
    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR1)
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        //getting the layoutinflater
        LayoutInflater inflater = LayoutInflater.from(mCtx);

        //creating a view with our xml layout
        @SuppressLint("ViewHolder") View listViewItem = inflater.inflate(R.layout.messages_list_view, null, true);

        //getting text views
        TextView textViewId = listViewItem.findViewById(R.id.textViewId);
        TextView textViewName = listViewItem.findViewById(R.id.textViewName);

        TextView textViewContact = listViewItem.findViewById(R.id.textViewContact);

        TextView textViewMessage = listViewItem.findViewById(R.id.textViewMessage);

        //Getting the superHero for the specified position
        UserModel superHero = UserModelList.get(position);

        //setting superHero values to textviews
        textViewId.setText(superHero.getId());
        textViewName.setText(superHero.getName());
        textViewContact.setText(superHero.getContact());
        textViewMessage.setText(superHero.getMessage());
        //returning the listitem
        return listViewItem;
    }
}

