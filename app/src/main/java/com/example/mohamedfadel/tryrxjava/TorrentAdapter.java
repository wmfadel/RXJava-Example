package com.example.mohamedfadel.tryrxjava;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

public class TorrentAdapter extends RecyclerView.Adapter<TorrentAdapter.TorrentViewHolder>{

    private List<Torrent> torrentList;
    final private ListItemClickListener mOnClickListener;

    public TorrentAdapter(ListItemClickListener mOnClickListener) {
        this.mOnClickListener = mOnClickListener;
    }

    public List<Torrent> getTorrentList() {
        return torrentList;
    }

    public void setTorrentList(List<Torrent> torrentList) {
        this.torrentList = torrentList;
        notifyDataSetChanged();
    }

    public interface ListItemClickListener {
        void onListItemClick(int clickedItemIndex);
    }

    @NonNull
    @Override
    public TorrentViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View rootView = LayoutInflater.from(parent.getContext()).inflate(R.layout.torrent_card, null, false);
        RecyclerView.LayoutParams lp = new RecyclerView.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        rootView.setLayoutParams(lp);
        return new TorrentViewHolder(rootView);
    }

    @Override
    public void onBindViewHolder(@NonNull TorrentViewHolder holder, int position) {
        Torrent torrent = torrentList.get(position);
        String torrentQuality = torrent.getQuality();
        String torrentSize = torrent.getSize();
        holder.quality.setText(torrentQuality);
        holder.size.setText(torrentSize);
    }

    @Override
    public int getItemCount() {
        if (torrentList == null)
            return 0;
        return torrentList.size();
    }


    public class TorrentViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        public TextView size;
        public TextView quality;
        public TorrentViewHolder(View itemView) {
            super(itemView);
            size = itemView.findViewById(R.id.tv_torrent_size);
            quality = itemView.findViewById(R.id.tv_torrent_quality);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            int clickedPosition = getAdapterPosition();
            mOnClickListener.onListItemClick(clickedPosition);
        }
    }



}
