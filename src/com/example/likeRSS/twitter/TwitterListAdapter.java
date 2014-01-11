package com.example.likeRSS.twitter;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Handler;
import android.os.Message;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.*;
import com.example.likeRSS.R;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: Ruslik
 * Date: 10.12.13
 * Time: 12:45
 * To change this template use File | Settings | File Templates.
 */
public class TwitterListAdapter extends ArrayAdapter<JSONObject> {

    private ListView list;

    public TwitterListAdapter(Activity activity,
                              List<JSONObject> imageAndTexts, ListView listView) {
        super(activity, 0, imageAndTexts);
        list = listView;
        DatabaseMgr.getInstance().connect(getContext());
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        LinearLayout rowView = (LinearLayout) convertView;
        ViewHolder holder;

        if (rowView == null) {
            Activity activity = (Activity) getContext();
            LayoutInflater inflater = activity.getLayoutInflater();

            rowView = (LinearLayout) inflater.inflate(
                    R.layout.twitter_list_item, null);
            holder = new ViewHolder();
            holder.pbar = (ProgressBar) rowView.findViewById(R.id.item_pbar);
            holder.image = (ImageView) rowView.findViewById(R.id.item_icon);
            holder.label = (TextView) rowView.findViewById(R.id.item_text);
            rowView.setTag(R.id.viewHolder, holder);
        } else {
            holder = (ViewHolder) rowView.getTag(R.id.viewHolder);
        }

        JSONObject jsonImageText = getItem(position);

        try {
            String avatar = (String) jsonImageText.get("avatar");
            String tweet = (String) jsonImageText.get("tweet");
            String auth = (String) jsonImageText.get("author");
            String date = (String) jsonImageText.get("tweetDate");
            if (date.length() > 0) {
                String latest = tweet + "<br><br><i>" + auth + " - " + date
                        + "</i>";
                holder.label.setText(Html.fromHtml(latest));
            } else {
                holder.label.setText(Html.fromHtml(tweet) + "\n"
                        + Html.fromHtml(auth));
            }

            if (DatabaseMgr.getInstance().isCachedImageExists(avatar)) {
                byte[] data = DatabaseMgr.getInstance().getCachedImage(avatar);
                if (data != null) {
                    Bitmap bm = BitmapFactory.decodeByteArray(data, 0,
                            data.length);
                    holder.image.setImageBitmap(bm);
                } else {
                    // TODO: set default "noimage" resource
                }
                rowView.setTag(null);
                holder.image.setVisibility(View.VISIBLE);
                holder.pbar.setVisibility(View.GONE);
            } else {
                holder.image.setVisibility(View.GONE);
                holder.pbar.setVisibility(View.VISIBLE);

                rowView.setTag(avatar);
                GetImageThread task = new GetImageThread(handler, avatar);
                HttpQueue.getInstance().addTask(task);
            }

        } catch (JSONException e) {
            holder.image.setVisibility(View.GONE);
            holder.pbar.setVisibility(View.VISIBLE);
            holder.label.setText("Trying to refresh chat...");
        }

        return rowView;

    }

    static private class ViewHolder {

        public ProgressBar pbar;

        public ImageView image;

        public TextView label;

    }

    private Handler handler = new Handler() {

        @Override
        public void handleMessage(Message msg) {
            if (msg.arg1 != HttpBaseThread.TASK_STATUS_OK)
                return;
            String url = (String) msg.obj;

            View view = list.findViewWithTag(url);
            if (view != null) {
                ViewHolder holder = (ViewHolder) view.getTag(R.id.viewHolder);
                byte[] data = DatabaseMgr.getInstance().getCachedImage(url);
                if (data != null) {
                    Bitmap bm = BitmapFactory.decodeByteArray(data, 0,
                            data.length);
                    holder.image.setImageBitmap(bm);
                }
                view.setTag(null);
                holder.image.setVisibility(View.VISIBLE);
                holder.pbar.setVisibility(View.GONE);
            }
        }
    };

    @Override
    public boolean areAllItemsEnabled() {
        return false;
    }

    @Override
    public boolean isEnabled(int position) {
        return false;
    }

}
