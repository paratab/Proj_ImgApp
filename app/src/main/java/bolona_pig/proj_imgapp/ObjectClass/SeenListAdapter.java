package bolona_pig.proj_imgapp.ObjectClass;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import bolona_pig.proj_imgapp.R;

/**
 * Created by DreamMii on 14/2/2559.
 */
public class SeenListAdapter extends BaseAdapter {

    DateTime dateTime;
    private Context mContext;
    private ArrayList<GridItem> mGridData = new ArrayList<>();

    public SeenListAdapter(Context mContext, ArrayList<GridItem> mGridData) {
        this.mContext = mContext;
        this.mGridData = mGridData;
        dateTime = new DateTime(mContext);
    }

    @Override
    public int getCount() {
        return mGridData.size();
    }

    @Override
    public Object getItem(int position) {
        return mGridData.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        View gridView;

        if (convertView == null) {

            gridView = inflater.inflate(R.layout.list_item_layout, null);

            TextView textView1 = (TextView) gridView.findViewById(R.id.textView1);
            TextView textView2 = (TextView) gridView.findViewById(R.id.textView2);
            ImageView imageView = (ImageView) gridView.findViewById(R.id.imageView);

            GridItem item = mGridData.get(position);

            String temp = "สถานที่ :" + item.title1;
            textView1.setText(temp);
            temp = "วันที่แจ้ง : " + item.title2;
            textView2.setText(temp);
            Picasso.with(mContext).load(item.image).into(imageView);
        } else {
            gridView = convertView;
        }

        return gridView;
    }

    public void setListData(ArrayList<GridItem> mGridData) {
        this.mGridData = mGridData;
        notifyDataSetChanged();
    }
}



