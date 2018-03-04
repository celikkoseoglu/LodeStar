package com.lodestarapp.cs491.lodestar.Adapters;

import android.app.Activity;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ActionMenuView;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.lodestarapp.cs491.lodestar.R;

import java.util.List;

/**
 * Created by efeulasakayseyitoglu on 21/02/2018.
 */

public class UsersList extends ArrayAdapter<User> {

    private Activity context;
    private List<User> userlist;

    public UsersList(Activity context2, List<User> userList2) {
        super(context2, R.layout.activity_search_user2,userList2);
        userlist = userList2;
        context = context2;

    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
     //   return super.getView(position, convertView, parent);
        LayoutInflater inf = context.getLayoutInflater();

        View listViewItem = inf.inflate(R.layout.activity_search_user2,null,true);

        TextView userName = (TextView) listViewItem.findViewById(R.id.textName);
        TextView userEmail = (TextView) listViewItem.findViewById(R.id.textEmail);

        return listViewItem;
    }
}
