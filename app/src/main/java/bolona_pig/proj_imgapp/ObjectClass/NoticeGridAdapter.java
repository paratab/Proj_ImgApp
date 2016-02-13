package bolona_pig.proj_imgapp.ObjectClass;

import android.app.Activity;
import android.content.Context;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import bolona_pig.proj_imgapp.R;

/**
 * Created by DreamMii on 10/2/2559.
 */
public class NoticeGridAdapter extends ArrayAdapter<NoticeItem> {

    //private final ColorMatrixColorFilter grayscaleFilter;
    private Context mContext;
    private int layoutResourceId;
    private ArrayList<NoticeItem> mGridData;

    public NoticeGridAdapter(Context mContext, int layoutResourceId, ArrayList<NoticeItem> mGridData) {
        super(mContext, layoutResourceId, mGridData);
        mGridData = new ArrayList<>();
        this.layoutResourceId = layoutResourceId;
        this.mContext = mContext;
        this.mGridData = mGridData;
    }


    /**
     * Updates grid data and refresh grid items.
     *
     * @param mGridData
     */
    public void setGridData(ArrayList<NoticeItem> mGridData) {
        this.mGridData = mGridData;
        notifyDataSetChanged();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View row = convertView;
        ViewHolder holder;

        if (row == null) {
            LayoutInflater inflater = ((Activity) mContext).getLayoutInflater();
            row = inflater.inflate(layoutResourceId, parent, false);
            holder = new ViewHolder();
            holder.textViewLine1 = (TextView) row.findViewById(R.id.item_line1);
            holder.textViewLine2 = (TextView) row.findViewById(R.id.item_line1);
            holder.imageView = (ImageView) row.findViewById(R.id.item_image);
            row.setTag(holder);
        } else {
            holder = (ViewHolder) row.getTag();
        }

        NoticeItem item = mGridData.get(position);
        holder.textViewLine1.setText(Html.fromHtml(item.getTitleLine1()));
        holder.textViewLine2.setText(Html.fromHtml(item.getTitleLine2()));

        Picasso.with(mContext).load(item.getImage()).into(holder.imageView);
        return row;
    }

    static class ViewHolder {
        TextView textViewLine1;
        TextView textViewLine2;
        ImageView imageView;
    }
}
