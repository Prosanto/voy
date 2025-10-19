package eu.coach_yourself.app;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.Window;
import android.widget.ProgressBar;
import android.widget.Toast;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.net.URLConnection;

import eu.coach_yourself.app.utils.AlertMessage;
import eu.coach_yourself.app.utils.ConstantFunctions;

public class DownloadedActivity extends Activity {
    private Context mContext;
    private String fileName = "", Download_path = "", errorMessage = "";
    private ProgressBar circularProgressBar;
    private AlbumStorageDirFactory mAlbumStorageDirFactory = null;
    private boolean downloadSuccess = false;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext = this;
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setTheme(android.R.style.Theme_Translucent);
        setContentView(R.layout.home_downloadpopup);
        Download_path = getIntent().getStringExtra("download_path");
        fileName = ConstantFunctions.trackurlSplit(Download_path);// mJobList.getTitle() + ".mp3";
        circularProgressBar = (ProgressBar) this.findViewById(R.id.progressBar2);

        showPopuWondows(createDownload(fileName));

    }

    public void showPopuWondows(String filePathName) {
        if (!Download_path.equalsIgnoreCase(""))
            grabURL(Download_path, filePathName);
    }

    public void grabURL(String url, String filePathName) {
        new GrabURL().execute(url, filePathName);
    }
//    public void grabURL(String url, String filePathName) {
//        new GrabURL().execute(url);
//    }

    private class GrabURL extends AsyncTask<String, String, String> {
        String destinationFilePath = "";

        @Override
        protected void onPreExecute() {

        }

        @Override
        protected String doInBackground(String... f_url) {
            int count;

            try {
                URL url = new URL(f_url[0]);
                URLConnection connection = url.openConnection();
                connection.connect();
                int lengthOfFile = connection.getContentLength();
                InputStream input = new BufferedInputStream(url.openStream());
                fileName = f_url[0].substring(f_url[0].lastIndexOf('/') + 1, f_url[0].length());
                String destinationFilePath = f_url[1];
                OutputStream output = new FileOutputStream(destinationFilePath);
                byte data[] = new byte[1024];
                long total = 0;
                while ((count = input.read(data)) != -1) {
                    total += count;
                    publishProgress("" + (int) ((total * 100) / lengthOfFile));
                    output.write(data, 0, count);
                }
                downloadSuccess = true;
                output.flush();
                output.close();
                input.close();


            } catch (Exception e) {
                downloadSuccess = false;
                errorMessage = e.getMessage();
                Log.e("Exception", e.getMessage());
            }

            return fileName;

        }

        @Override
        protected void onProgressUpdate(String... progress) {
            circularProgressBar.setProgress(Integer.parseInt(progress[0]));
        }

        @Override
        protected void onCancelled() {
            Toast toast = Toast.makeText(getBaseContext(),
                    "Error connecting to Server", Toast.LENGTH_LONG);
            toast.setGravity(Gravity.TOP, 25, 400);
            toast.show();

        }

        String songSavePath = "";

        protected void onPostExecute(String filename) {
            if (downloadSuccess) {
                galleryAddPic(Download_path);
                String fileName = ConstantFunctions.trackurlSplit(Download_path);
                Intent mIntent = getIntent();
                mIntent.putExtra("audioName", fileName);
                setResult(RESULT_OK, mIntent);
                DownloadedActivity.this.finish();
            } else {
                AlertMessage.showMessage(DownloadedActivity.this, getResources().getString(R.string.app_download_information), getResources().getString(R.string.app_download_information_2));
            }

        }
    }

//    private File downloadAndUnzipContent(String filename) {
//        File mdir = getAlbumDir();
//        File mainDri = new File(mdir.getAbsolutePath() + "/" + filename);
//        String pathFull = mainDri.getAbsolutePath();
//        File alreadyDri = new File(pathFull);
//        return alreadyDri;
//
//    }

    //    private File getAlbumDir() {
//        File storageDir = null;
//        String pathName = getAlbumName();
//        if (Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState())) {
//            storageDir = mAlbumStorageDirFactory.getAlbumStorageDir(pathName);
//            if (storageDir != null) {
//                if (!storageDir.mkdirs()) {
//                    if (!storageDir.exists()) {
//                        return null;
//                    }
//                }
//            }
//        }
//        return storageDir;
//    }
    private String createDownload(String FolderName) {
        File d = new File(getExternalFilesDir(Environment.DIRECTORY_DOWNLOADS), getAlbumName());
        if (!d.exists()) {
            d.mkdirs();
        }
        String path = d.getAbsolutePath().concat("/").concat(FolderName);
        return path;

//        downloadInfo = new DownloadInfo.Builder().setUrl(DEFAULT_URL)
//                .setPath(path)
//                .build();
//        setDownloadListener();
//        downloadManager.download(downloadInfo);
    }

//    public File downloadAndUnzipContent(String FolderName) {
//        File dir = null;
//        if (SDK_INT >= Build.VERSION_CODES.R) {
//            File root = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOCUMENTS);
//            File myDir = new File(root + "/" + getAlbumName());
//            if (!myDir.isFile()) {
//                myDir.mkdirs();
//            }
//            dir = new File(myDir, FolderName);
//        } else {
//            String root = Environment.getExternalStorageDirectory().toString();
//            File myDir = new File(root + "/" + getAlbumName());
//            if (!myDir.isFile()) {
//                myDir.mkdirs();
//            }
//            dir = new File(myDir, FolderName);
//        }
//        return dir;
//
//    }


    private String getAlbumName() {
        return "voy"; //getString(R.string.app_name);
    }

    public boolean onKeyDown(int keyCode, KeyEvent event) {
        switch (keyCode) {
            case KeyEvent.KEYCODE_BACK:
                return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    private void galleryAddPic(String currentPhotoPath) {
        Intent mediaScanIntent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
        File f = new File(currentPhotoPath);
        Uri contentUri = Uri.fromFile(f);
        mediaScanIntent.setData(contentUri);
        this.sendBroadcast(mediaScanIntent);
    }

}
