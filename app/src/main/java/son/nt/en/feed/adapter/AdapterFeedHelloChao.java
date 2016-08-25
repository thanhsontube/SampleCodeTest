package son.nt.en.feed.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.lang.ref.WeakReference;
import java.util.List;

import son.nt.en.R;
import son.nt.en.hellochao.BusSentence;
import son.nt.en.hellochao.HelloChaoSentences;
import son.nt.en.otto.OttoBus;

/**
 * Created by Sonnt on 7/7/15.
 */
public class AdapterFeedHelloChao extends RecyclerView.Adapter<AdapterFeedHelloChao.ViewHolder> {

    private static final String TAG = AdapterFeedHelloChao.class.getSimpleName();
    public List<HelloChaoSentences> mValues;
    Context context;
    private final WeakReference<Context> contextWeakReference;

    private int previous = 0;

    public AdapterFeedHelloChao(Context cx) {
        this.context = cx;
        this.contextWeakReference = new WeakReference<>(cx);
    }

    public void setData(List<HelloChaoSentences> mValues) {
        this.mValues = mValues;
        notifyDataSetChanged();
    }

    public HelloChaoSentences getItem(int position) {
        return mValues.get(position);
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_feed_hello_chao, viewGroup,
                false);
        return new ViewHolder(view);
    }


    @Override
    public void onBindViewHolder(ViewHolder viewHolder, final int position) {
        HelloChaoSentences HelloChaoSentences = mValues.get(position);
        final HelloChaoSentences dto = HelloChaoSentences;


        viewHolder.messageTextView.setText(dto.text);
        viewHolder.messengerTextView.setText(dto.translate);


    }

    @Override
    public int getItemCount() {
        return mValues == null ? 0 : mValues.size();
    }


    public static class ViewHolder extends RecyclerView.ViewHolder {
        public TextView messageTextView;
        public TextView messengerTextView;

        public ViewHolder(View v) {
            super(v);
            messageTextView = (TextView) itemView.findViewById(R.id.messageTextView);
            messengerTextView = (TextView) itemView.findViewById(R.id.messengerTextView);
            v.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    OttoBus.post(new BusSentence(getAdapterPosition()));

                }
            });
        }
    }
}
