package com.esdrasmorais.inspetoronline.ui.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.esdrasmorais.inspetoronline.R;
import com.esdrasmorais.inspetoronline.ui.models.InspectionTypeImageModel;

import java.util.ArrayList;

public class InspectionTypeImageAdapter
    extends ArrayAdapter<InspectionTypeImageModel>
{
    private Context context;
    private ArrayList<InspectionTypeImageModel> inspectionTypeImageModels;
    LayoutInflater inflater = null;

    public InspectionTypeImageAdapter(
        Context context, Integer resourceId,
        ArrayList<InspectionTypeImageModel> inspectionTypeImageModels
    ) {
        super(context, resourceId, inspectionTypeImageModels);

        this.context = context;
        this.inspectionTypeImageModels = inspectionTypeImageModels;
    }
//
//    @Override
//    public int getCount() {
//        return inspectionTypeImageModels.size();
//    }

//    @Override
//    public Object getItem(int position) {
//        return ((Object) inspectionTypeImageModels.get(position));
//    }

//    @Override
//    public long getItemId(int position) {
//        return position;
//    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
//        final ViewHolder viewHolder;
        //View view = convertView;

        if (convertView == null) {
            this.inflater = (LayoutInflater)
                context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(
                R.layout.inspection_type_list_item, null
            );
            //viewHolder = new ViewHolder();

          //  view.setTag(viewHolder);
        }
//        else {
//            viewHolder = (ViewHolder) view.getTag();
//        }

        TextView textView = convertView.findViewById(R.id.inspection_type_name);
        ImageView imageView = convertView.findViewById(R.id.inspection_type_icon);

        textView.setText(
            inspectionTypeImageModels.get(position).getName()
        );
        imageView.setImageResource(
            inspectionTypeImageModels.get(position).getImageDrawable()
        );

        return convertView;
    }

//    public static class ViewHolder {
//        private ImageView imageView;
//        protected TextView textView;
//    }
}