package son.nt.en.feed.adapter;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.lang.ref.WeakReference;
import java.util.List;

import son.nt.en.R;
import son.nt.en.elite.BusElite;
import son.nt.en.elite.EliteDto;
import son.nt.en.otto.OttoBus;

/**
 * Created by Sonnt on 7/7/15.
 */
public class AdapterFeedElite extends RecyclerView.Adapter<AdapterFeedElite.ViewHolder> {

    private static final String TAG = AdapterFeedElite.class.getSimpleName();
    public List<EliteDto> mValues;
    Context context;
    private final WeakReference<Context> contextWeakReference;

    private int previous = 0;

    public AdapterFeedElite(Context cx) {
        this.context = cx;
        this.contextWeakReference = new WeakReference<>(cx);
    }

    public void setData(List<EliteDto> mValues) {
        this.mValues = mValues;
        notifyDataSetChanged();
    }

    public EliteDto getItem(int position) {
        return mValues.get(position);
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_feed_elite_daily, viewGroup, false);
        return new ViewHolder(view);
    }


    @Override
    public void onBindViewHolder(ViewHolder viewHolder, final int position) {
        EliteDto EliteDto = mValues.get(position);
        final EliteDto dto = EliteDto;

        String no = String.valueOf(position).trim();
        if (no.length() == 1) {
            no = no + "  ";
        } else if (no.length() == 2) {
            no = no + " ";
        }
        viewHolder.createdTimeTextView.setText(no);

        viewHolder.messageTextView.setText(dto.title);
        viewHolder.messengerTextView.setText(dto.des);


        Glide.with(context) //
                .load(dto.image)//
                .centerCrop()//
                .diskCacheStrategy(DiskCacheStrategy.ALL)//
                .into(viewHolder.imgBackground);

    }

    @Override
    public int getItemCount() {
        return mValues == null ? 0 : mValues.size();
    }


    public static class ViewHolder extends RecyclerView.ViewHolder {
        public TextView messageTextView;
        public TextView messengerTextView;
        public TextView createdTimeTextView;

        public ImageView imgBackground;

        public ViewHolder(View v) {
            super(v);
            messageTextView = (TextView) itemView.findViewById(R.id.messageTextView);
            messengerTextView = (TextView) itemView.findViewById(R.id.messengerTextView);
            createdTimeTextView = (TextView) itemView.findViewById(R.id.createdTimeTextView);
            imgBackground = (ImageView) itemView.findViewById(R.id.imageView);
            v.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    OttoBus.post(new BusElite(getAdapterPosition()));

                }
            });
        }
    }
}
