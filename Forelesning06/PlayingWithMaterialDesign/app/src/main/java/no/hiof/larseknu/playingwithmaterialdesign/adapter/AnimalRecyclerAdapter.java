package no.hiof.larseknu.playingwithmaterialdesign.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import no.hiof.larseknu.playingwithmaterialdesign.R;
import no.hiof.larseknu.playingwithmaterialdesign.model.Animal;

public class AnimalRecyclerAdapter extends RecyclerView.Adapter<AnimalRecyclerAdapter.AnimalViewHolder> {

    private static final String TAG = AnimalRecyclerAdapter.class.getSimpleName();

    private List<Animal> mData;
    private LayoutInflater mInflater;

    public AnimalRecyclerAdapter(Context context, List<Animal> data) {
        this.mData = data;
        this.mInflater = LayoutInflater.from(context);
    }

    @Override
    public AnimalViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.animal_list_item, parent, false);
        AnimalViewHolder holder = new AnimalViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(AnimalViewHolder holder, int position) {
        Animal currentObj = mData.get(position);
        holder.setData(currentObj);
    }

    @Override
    public int getItemCount() {
        return mData.size();
    }

    class AnimalViewHolder extends RecyclerView.ViewHolder {

        TextView title;
        ImageView imgThumb;

        public AnimalViewHolder(View itemView) {
            super(itemView);
            title       = itemView.findViewById(R.id.animal_text);
            imgThumb    = itemView.findViewById(R.id.animal_image);
        }

        public void setData(Animal current) {
            this.title.setText(current.getTitle());
            this.imgThumb.setImageResource(current.getImageId());
        }
    }
}
