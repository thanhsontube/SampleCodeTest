package son.nt.en.elite;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;

import java.lang.ref.WeakReference;
import java.util.List;

import son.nt.en.R;
import son.nt.en.otto.OttoBus;

/**
 * Created by Sonnt on 7/7/15.
 */
public class AdapterEliteDaily extends RecyclerView.Adapter<AdapterEliteDaily.ViewHolder>
{

    private static final String          TAG      = AdapterEliteDaily.class.getSimpleName();
    public List<EliteDto>             mValues;
    Context                              context;
    private final WeakReference<Context> contextWeakReference;

    private int                          previous = 0;

    public AdapterEliteDaily(Context cx)
    {
        this.context = cx;
        this.contextWeakReference = new WeakReference<>(cx);
    }

    public void setData(List<EliteDto> mValues)
    {
        this.mValues = mValues;
        notifyDataSetChanged();
    }

    public EliteDto getItem(int position)
    {
        return mValues.get(position);
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i)
    {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.row_elite_daily, viewGroup, false);
        return new ViewHolder(view);
    }


    @Override
    public void onBindViewHolder(ViewHolder viewHolder, final int position)
    {
        EliteDto EliteDto = mValues.get(position);
        final EliteDto dto = EliteDto;

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

        viewHolder.messageTextView.setText(dto.title);
        viewHolder.messengerTextView.setText(dto.des);

        //        if (EliteDto.isPlaying())
        //        {
        //            viewHolder.view.setBackgroundResource(R.drawable.d_row_speaking);
        //            viewHolder.imageView.setVisibility(View.VISIBLE);
        //        }
        //        else
        //        {
        //            viewHolder.imageView.setVisibility(View.GONE);
        //            viewHolder.view.setBackgroundResource(android.R.color.transparent);
        //        }

        Glide.with(context).load(dto.image).centerCrop().diskCacheStrategy(DiskCacheStrategy.ALL)
                        .into(viewHolder.imgBackground);

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
        public TextView  messageTextView;
        public TextView  messengerTextView;
        public TextView  createdTimeTextView;

        public ImageView imgBackground;

        public ViewHolder(View v)
        {
            super(v);
            messageTextView = (TextView) itemView.findViewById(R.id.messageTextView);
            messengerTextView = (TextView) itemView.findViewById(R.id.messengerTextView);
            createdTimeTextView = (TextView) itemView.findViewById(R.id.createdTimeTextView);
            imgBackground = (ImageView) itemView.findViewById(R.id.imageView);
            v.setOnClickListener(new View.OnClickListener()
            {
                @Override
                public void onClick(View v)
                {
                                        OttoBus.post(new BusElite(getAdapterPosition()));

                }
            });
        }
    }
}
