package fl.ub.adapter;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import fl.ub.R;
import fl.ub.list.PhotoItemViewHolder;
import fl.ub.model.PhotoItem;
import fl.ub.utils.UrlUtil;

public class PhotoItemAdapter extends RecyclerView.Adapter<PhotoItemViewHolder> {
    private Map<Integer,List<PhotoItem>> itemMap;
    private List<PhotoItem> items;

    @NonNull
    @Override
    public PhotoItemViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        return new PhotoItemViewHolder(LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.grid_image_item,null));
    }

    @Override
    public void onBindViewHolder(@NonNull PhotoItemViewHolder photoItemAdapter, int i) {
        PhotoItem currenntPhotoItem = items.get(i);
        photoItemAdapter.loadImage(UrlUtil.getImageUrl(currenntPhotoItem));
    }

    @Override
    public int getItemCount() {
        return items!=null ? items.size() : 0;
    }

    public void setItem(int pageNo,List<PhotoItem> photoItems){
        addItemsInMap(pageNo, photoItems);
        addItemsInList(photoItems);
        notifyDataSetChanged();
    }

    private void addItemsInList(List<PhotoItem> photoItems) {
        if(items == null){
            items = new ArrayList<>();
        }
        items.addAll(photoItems);
    }

    private void addItemsInMap(int pageNo, List<PhotoItem> photoItems) {
        if(itemMap == null){
            itemMap = new LinkedHashMap<>();
        }
        itemMap.put(pageNo,photoItems);
    }
}
