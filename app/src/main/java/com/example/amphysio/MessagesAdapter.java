package com.example.amphysio;

import android.content.Context;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.CircleCrop;
import com.bumptech.glide.request.BaseRequestOptions;
import com.bumptech.glide.request.RequestOptions;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;

import java.util.ArrayList;

public class MessagesAdapter extends BaseAdapter {

    private ArrayList<DataModel> List;
    public Context context;
    private FirebaseStorage storage = FirebaseStorage.getInstance();

    public MessagesAdapter(Context ctx, ArrayList<DataModel> lst) {
        this.List = lst;
        this.context = ctx;
    }

    public int getCount() {
        return this.List.size();
    }

    public Object getItem(int pos) {
        return this.List.get(pos);
    }

    public long getItemId(int i) {
        return (long) i;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View v;
        if (convertView == null) {  // if it's not recycled, initialize some attributes
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            v = inflater.inflate(R.layout.message_row, null);
        } else {
            v = convertView;
        }

        TextView name = (TextView) v.findViewById(R.id.m_name);
        TextView msg = (TextView) v.findViewById(R.id.m_msg);
        TextView _id = (TextView) v.findViewById(R.id.m_id);
        final ImageView Profile = (ImageView) v.findViewById(R.id.m_profile);

        DataModel st = (DataModel) this.List.get(position);
        name.setText(st.getPatientName());
        msg.setText(st.getMsg());
        _id.setText(st.getPatientId());

        try {
            if (!st.getPatientImageUrl().equals("default")) {
                this.storage.getReferenceFromUrl(st.getPatientImageUrl()).getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                    public void onSuccess(Uri uri) {
                        RequestOptions options = new RequestOptions();
                        options.skipMemoryCache(true);
                        options.centerInside();
                        options.placeholder((int) R.mipmap.default_pic_profile);
                        options.transforms(new CircleCrop());
                        Glide.with(context).load(uri).thumbnail(0.5f).apply((BaseRequestOptions<?>) options).into(Profile);
                    }
                });
            } else {
                RequestOptions options = new RequestOptions();
                options.skipMemoryCache(true);
                options.centerInside();
                options.placeholder((int) R.mipmap.default_pic_profile);
                options.transforms(new CircleCrop());
                Glide.with(this.context).load(Integer.valueOf(context.getResources().getIdentifier("default_profile_pic", "mipmap", this.context.getPackageName()))).thumbnail(0.5f).apply((BaseRequestOptions<?>) options).into(Profile);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return v;
    }
}
