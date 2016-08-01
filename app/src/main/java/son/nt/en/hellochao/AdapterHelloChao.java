package son.nt.en.hellochao;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.lang.ref.WeakReference;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;
import son.nt.en.R;
import son.nt.en.otto.OttoBus;

/**
 * Created by Sonnt on 7/7/15.
 */
public class AdapterHelloChao extends RecyclerView.Adapter<AdapterHelloChao.ViewHolder>
{

    private static final String          TAG      = AdapterHelloChao.class.getSimpleName();
    public List<HelloChaoSentences>      mValues;
    Context                              context;
    private final WeakReference<Context> contextWeakReference;

    private int                          previous = 0;

    public AdapterHelloChao(Context cx)
    {
        this.context = cx;
        this.contextWeakReference = new WeakReference<>(cx);
    }

    public void setData(List<HelloChaoSentences> mValues)
    {
        this.mValues = mValues;
        notifyDataSetChanged();
    }

    public HelloChaoSentences getItem(int position)
    {
        return mValues.get(position);
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i)
    {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.row_hello_chao, viewGroup,
                        false);
        return new ViewHolder(view);
    }

//    public void setNewPos(int pos)
//    {
//        mValues.get(previous).setPlaying(false);
//        notifyItemChanged(previous);
//        mValues.get(pos).setPlaying(true);
//        notifyItemChanged(pos);
//
//        previous = pos;
//    }

    @Override
    public void onBindViewHolder(ViewHolder viewHolder, final int position)
    {
        HelloChaoSentences HelloChaoSentences = mValues.get(position);
        final HelloChaoSentences dto = HelloChaoSentences;

        String no = String.valueOf(position).trim();
        if (no.length() == 1)
        {
            no = no + "  ";
        }
        else if (no.length() == 2)
        {
            no = no + " ";
        }
        viewHolder.createdTimeTextView.setText(no);


        viewHolder.messageTextView.setText(dto.text);
        viewHolder.messengerTextView.setText(dto.translate);

//        if (HelloChaoSentences.isPlaying())
//        {
//            viewHolder.view.setBackgroundResource(R.drawable.d_row_speaking);
//            viewHolder.imageView.setVisibility(View.VISIBLE);
//        }
//        else
//        {
//            viewHolder.imageView.setVisibility(View.GONE);
//            viewHolder.view.setBackgroundResource(android.R.color.transparent);
//        }

//        Glide.with(context).load(HelloChaoSentences.getImage()).centerCrop().diskCacheStrategy(DiskCacheStrategy.ALL)
//                        .into(viewHolder.imageGroup);

    }

    @Override
    public int getItemCount()
    {
        return mValues == null ? 0 : mValues.size();
    }

//    public static class ViewHolder extends RecyclerView.ViewHolder
//    {
//        ImageView imageView;
//        ImageView imageGroup;
//        TextView  txtName;
//        View      view;
//        TextView  txtNo;
//
//        public ViewHolder(View itemView)
//        {
//            super(itemView);
//            this.view = itemView.findViewById(R.id.row_voice_main);
//            this.imageView = (ImageView) itemView.findViewById(R.id.row_voice_playing);
//            this.imageGroup = (ImageView) itemView.findViewById(R.id.row_voice_rival);
//            this.txtName = (TextView) itemView.findViewById(R.id.row_voice_text);
//            txtNo = (TextView) itemView.findViewById(R.id.row_voice_no);
//
//            this.view.setOnClickListener(new View.OnClickListener()
//            {
//                @Override
//                public void onClick(View v)
//                {
//                    /**
//                     * @see MusicPackDetailsActivity#itemClick(GoAdapterHelloChao)
//                     */
//                    OttoBus.post(new GoAdapterHelloChao(getAdapterPosition()));
//
//                }
//            });
//
//        }
//    }
    public static class ViewHolder extends RecyclerView.ViewHolder
    {
        public TextView        messageTextView;
        public TextView        messengerTextView;
        public TextView        createdTimeTextView;
        public CircleImageView messengerImageView;
        public CircleImageView levelImageView;

        public ViewHolder(View v)
        {
            super(v);
            messageTextView = (TextView) itemView.findViewById(R.id.messageTextView);
            messengerTextView = (TextView) itemView.findViewById(R.id.messengerTextView);
            createdTimeTextView = (TextView) itemView.findViewById(R.id.createdTimeTextView);
            messengerImageView = (CircleImageView) itemView.findViewById(R.id.messengerImageView);
            levelImageView = (CircleImageView) itemView.findViewById(R.id.levelImageView);
            v.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    OttoBus.post(new BusSentence(getAdapterPosition()));

                }
            });
        }
    }
}
