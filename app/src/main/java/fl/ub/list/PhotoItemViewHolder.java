package fl.ub.list;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import fl.ub.R;

public class PhotoItemViewHolder extends RecyclerView.ViewHolder {
    private ImageView imageView;
    public PhotoItemViewHolder(@NonNull View itemView) {
        super(itemView);
        imageView = itemView.findViewById(R.id.image);
    }

    public void loadImage(String url){
        ImageLoader.loadImage(url,imageView,R.drawable.ic_launcher_background);
        Log.d("photo-to-load",url);
    }
}
