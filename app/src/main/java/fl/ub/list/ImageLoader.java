package fl.ub.list;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Handler;
import android.os.Looper;
import android.util.LruCache;
import android.widget.ImageView;

import java.io.IOException;
import java.io.InputStream;
import java.lang.ref.WeakReference;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ImageLoader {
    private static Handler handler = new Handler(Looper.getMainLooper());
    private static ExecutorService executor = Executors.newFixedThreadPool(10);
    final static int maxMemory = (int) (Runtime.getRuntime().maxMemory() / 1024);
    // Use 1/8th of the available memory for this memory cache.
    final static int cacheSize = maxMemory / 8;

    private static LruCache<String, Bitmap> cache = new LruCache<String, Bitmap>(cacheSize) {
        protected int sizeOf(String key, Bitmap value) {
            return value.getByteCount() / 1024;
        }
    };

    public static void loadImage(final String photoUrl, ImageView imageView, int res) {

        imageView.setTag(photoUrl);
        final WeakReference<ImageView> imageViewWeakReference = new WeakReference<>(imageView);
        imageView.setImageResource(res);
        executor.execute(new Runnable() {
            @Override
            public void run() {
                Bitmap bmp = cache.get(photoUrl);
                if (bmp == null) {
                    final ImageView imageView = imageViewWeakReference.get();
                    if (imageView != null) {
                        bmp = getBitmapFromUrl(photoUrl, imageView.getHeight(), imageView.getWidth());
                        if(bmp!=null) {
                            cache.put(photoUrl, bmp);
                        }
                    }
                }
                if (bmp != null) {
                    final Bitmap imgBitMap = bmp;
                    final ImageView imageView = imageViewWeakReference.get();
                    if (imageView != null) {
                        String urlToLoad = (String) imageView.getTag();
                        if (photoUrl.equalsIgnoreCase(urlToLoad)) {
                            handler.post(new Runnable() {
                                @Override
                                public void run() {
                                    imageView.setImageBitmap(imgBitMap);
                                }
                            });
                        }
                    }
                }
            }
        });

    }

    private static Bitmap getBitmapFromUrl(String photoUrl, int TARGET_HEIGHT, int TARGET_WIDTH) {
        Bitmap bmp = null;
        URL url = null;
        try {
            url = new URL(photoUrl);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        try {
            InputStream is = url.openConnection().getInputStream();
            BitmapFactory.Options options = new BitmapFactory.Options();
            options.inJustDecodeBounds = true;

            BitmapFactory.decodeStream(is, null, options);
            if (TARGET_HEIGHT > 0 && TARGET_WIDTH > 0) {
                Boolean scaleByHeight = Math.abs(options.outHeight - TARGET_HEIGHT) >= Math.abs(options.outWidth - TARGET_WIDTH);

                if (options.outHeight * options.outWidth * 2 >= 200 * 200 * 2) {
                    // Load, scaling to smallest power of 2 that'll get it <= desired dimensions
                    double sampleSize = scaleByHeight
                            ? options.outHeight / TARGET_HEIGHT
                            : options.outWidth / TARGET_WIDTH;
                    options.inSampleSize =
                            (int) Math.pow(2d, Math.floor(
                                    Math.log(sampleSize) / Math.log(2d)));
                }
            }

            // Do the actual decoding
            options.inJustDecodeBounds = false;

            is.close();
            is = url.openConnection().getInputStream();
            bmp = BitmapFactory.decodeStream(is, null, options);
            is.close();

        } catch (IOException e) {
            e.printStackTrace();
        }
        return bmp;
    }
}
