package fl.ub.parser;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import fl.ub.model.PhotoItem;
import fl.ub.model.Photos;
import fl.ub.model.FlickerAPIResponse;
import fl.ub.utils.NetworkUtil;

public class ParsingUtil {
    public static FlickerAPIResponse loadResponse(String url) throws IOException, JSONException {
        String responseString = NetworkUtil.downloadUrl(new URL(url));
        JSONObject reader = new JSONObject(responseString);

        FlickerAPIResponse response = new FlickerAPIResponse();
        response.setPhotos(getPhotos(reader.getJSONObject("photos")));
        response.setStat(reader.getString("stat"));
        return response;
    }

    private static Photos getPhotos(JSONObject photos) throws JSONException {
        Photos photos1 = new Photos();
        photos1.setPage(photos.getInt("page"));
        photos1.setPages(photos.getInt("pages"));
        photos1.setPerpage(photos.getInt("perpage"));
        photos1.setTotal(photos.getString("total"));
        photos1.setPhoto(getPhotoList(photos.getJSONArray("photo")));
        return photos1;
    }

    private static List<PhotoItem> getPhotoList(JSONArray photo) {
        List<PhotoItem> photoItems = new ArrayList<>();
        for (int i=0;i<photo.length();i++){
            try {
                photoItems.add(getPhotoItem(photo.getJSONObject(i)));
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return photoItems;
    }

    private static PhotoItem getPhotoItem(JSONObject photoItem) throws JSONException {
        /**
         * {
         "id": "32052648308",
         "owner": "56569389@N05",
         "secret": "78666795af",
         "server": "4850",
         "farm": 5,
         "title": "Gato laranja",
         "ispublic": 1,
         "isfriend": 0,
         "isfamily": 0
         }
         */
        PhotoItem p = new PhotoItem();
        p.setId(photoItem.getString("id"));
        p.setOwner(photoItem.getString("owner"));
        p.setSecret(photoItem.getString("secret"));
        p.setServer(photoItem.getString("server"));
        p.setFarm(photoItem.getInt("farm"));
        p.setTitle(photoItem.getString("title"));

        return p;
    }
}
