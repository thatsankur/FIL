package fl.ub.utils;

import fl.ub.model.PhotoItem;

public class UrlUtil {
    public static String getImageUrl(PhotoItem item){
        //http://farm{farm}.static.flickr.com/{server}/{id}_{secret}.jpg
        StringBuilder builder = new StringBuilder();
        builder.append("http://farm");
        builder.append(item.getFarm());
        builder.append(".static.flickr.com");
        builder.append("/");
        builder.append(item.getServer());
        builder.append("/");
        builder.append(item.getId());
        builder.append("_");
        builder.append(item.getSecret());
        builder.append(".jpg");
        return builder.toString();
    }
}
