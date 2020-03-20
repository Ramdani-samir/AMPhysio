package com.example.amphysio;

import android.content.Context;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.CircleCrop;
import com.bumptech.glide.request.BaseRequestOptions;
import com.bumptech.glide.request.RequestOptions;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.storage.FirebaseStorage;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class PatientsAdapter extends BaseAdapter implements Filterable {
    private FirebaseAuth Auth;
    private ArrayList<DataModel> arraylist;
    private ArrayList<DataModel> origineList;
    private ItemFilter mFilter = new ItemFilter();
    public Context context;
    private FirebaseStorage storage = FirebaseStorage.getInstance();

    public PatientsAdapter(ArrayList<DataModel> list, Context cont) {
        this.arraylist = list;
        this.origineList = list;
        this.context = cont;
        this.Auth = FirebaseAuth.getInstance();
    }

    public int getCount() {
        return this.arraylist.size();
    }

    public Object getItem(int pos) {
        return this.arraylist.get(pos);
    }

    public long getItemId(int i) {
        return (long) i;
    }

    public View getView(int position, View convertV, ViewGroup parent) {
        View v;
        if (convertV == null) {  // if it's not recycled, initialize some attributes
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            v = inflater.inflate(R.layout.patient_item, null);
        } else {
            v = convertV;
        }
        TextView Key = (TextView) v.findViewById(R.id.i_id);
        TextView name = (TextView) v.findViewById(R.id.i_name);
        TextView bat = (TextView) v.findViewById(R.id.i_bat);
        TextView temp = (TextView) v.findViewById(R.id.i_temp);
        final ImageView Profile = (ImageView) v.findViewById(R.id.i_profile);

        DataModel st = (DataModel) this.arraylist.get(position);
        name.setText(st.getPatientName());
        Key.setText(st.getPatientId());
        bat.setText(st.getBatt());
        temp.setText(st.getTemp());
        try {
            if (!st.getPatientImageUrl().toString().equals("default")) {
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

    @Override
    public Filter getFilter() {
        return mFilter;
    }

    private class ItemFilter extends Filter {
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {

            String filterString = constraint.toString().toLowerCase();

            FilterResults results = new FilterResults();

            final List<DataModel> list = origineList;

            int count = list.size();
            final ArrayList<DataModel> nlist = new ArrayList<>(count);

            String filterableString;

            for (int i = 0; i < count; i++) {
                filterableString = list.get(i).getPatientId();
                if (filterableString.toLowerCase().contains(filterString)) {
                    nlist.add(list.get(i));
                }
            }

            results.values = nlist;
            results.count = nlist.size();

            return results;
        }

        @SuppressWarnings("unchecked")
        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
           arraylist = (ArrayList<DataModel>) results.values;
            notifyDataSetChanged();
        }

    }
}