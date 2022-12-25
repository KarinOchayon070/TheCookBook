package com.example.finalprojectandroidapp.adapters;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;
import com.example.finalprojectandroidapp.R;
import com.example.finalprojectandroidapp.UploadRecipeImage;
import com.squareup.picasso.Picasso;
import java.util.List;


public class UserRecipesAdapter extends RecyclerView.Adapter<UserRecipesAdapter.ImageViewHolder> {

    Context mContext;
    List<UploadRecipeImage> mUploads;

    public UserRecipesAdapter(Context context, List<UploadRecipeImage> uploads) {
        this.mContext = context;
        this.mUploads = uploads;
    }
    @Override
    public ImageViewHolder onCreateViewHolder( ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.user_rcepies, parent, false);
        return new ImageViewHolder(v);

    }

    @Override
    public void onBindViewHolder(ImageViewHolder holder, int position) {
//        UploadRecipeImage uploadCurrent = mUploads.get(position);
        UploadRecipeImage uploadCurrent = mUploads.get(position);
        String test = mUploads.get(position).getName();
        String testImage = mUploads.get(position).getImageUrl();
        holder.textViewNameUserUploads.setText(test);
        //holder.textViewIDUserUploads.setText(mUploads.get(position).getId());
        Picasso.get().load(uploadCurrent.getImageUrl())
                .placeholder(R.mipmap.ic_launcher)
                .fit()
                .centerCrop()
                .into(holder.imageViewUserUploads);
    }

    @Override
    public int getItemCount() {
        return mUploads.size();
    }



    public class ImageViewHolder extends RecyclerView.ViewHolder {
        TextView textViewNameUserUploads, textViewIDUserUploads;
        ImageView imageViewUserUploads;

        public ImageViewHolder( View itemView) {
            super(itemView);
            textViewNameUserUploads = itemView.findViewById(R.id.textViewUserRecipeNameTest);
            textViewIDUserUploads = itemView.findViewById(R.id.textViewID);
            imageViewUserUploads = itemView.findViewById(R.id.imageViewUserRecipeImageTest);
        }
    }

}

