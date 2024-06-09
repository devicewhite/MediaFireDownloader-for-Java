package com.deviceblack;

import android.app.DownloadManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.Cursor;
import android.net.Uri;
import android.os.Handler;
import android.webkit.CookieManager;
import android.webkit.DownloadListener;
import android.webkit.URLUtil;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import java.io.File;

/**
 * Class for downloading files from MediaFire.
 */
public class MediaFireDownloader implements DownloadListener {

	private Context context;
	private DownloadListener listener;
	private WebView webView;
	private DownloadManager downloadManager;
	private long downloadId;
	private Runnable progressRunnable;
	private Handler handler;

	private boolean inProcess;
	private String downloadUrl;
	private File downloadPath;
	private MediaFireDownloader downloaderInstance;
	private DownloadCompleteReceiver downloadCompleteReceiver;

	private final int ERROR_INVALID_LINK = 1;
	private final int ERROR_DOWNLOAD_IN_PROGRESS = 2;
	private final int ERROR_FAILED_TO_DOWNLOAD = 3;

	/**
	 * Interface for download events.
	 */
	public interface DownloadListener {
		
		private final int ERROR_INVALID_LINK = 1;
		private final int ERROR_DOWNLOAD_IN_PROGRESS = 2;
		private final int ERROR_FAILED_TO_DOWNLOAD = 3;

		/**
		 * Called when the download is started.
		 *
		 * @param downloader The instance of MediaFireDownloader.
		 * @param url		The URL of the file being downloaded.
		 * @param path	   The path where the file will be saved.
		 */
		void onMediaFireDownloadStart(MediaFireDownloader downloader, String url, File path);

		/**
		 * Called when the download is successfully completed.
		 *
		 * @param downloader The instance of MediaFireDownloader.
		 * @param url		The URL of the downloaded file.
		 * @param path	   The path where the file was saved.
		 */
		void onMediaFireDownloadComplete(MediaFireDownloader downloader, String url, File path);

		/**
		 * Called when the download fails.
		 *
		 * @param downloader The instance of MediaFireDownloader.
		 * @param url		The URL of the file that failed to download.
		 * @param path	   The path where the file should have been saved.
		 * @param error	  The error code associated with the download failure.
		 */
		void onMediaFireDownloadFailed(MediaFireDownloader downloader, String url, File path, int error);

		/**
		 * Called periodically to update the download progress.
		 *
		 * @param downloader	 The instance of MediaFireDownloader.
		 * @param url			The URL of the file being downloaded.
		 * @param path		   The path where the file is being saved.
		 * @param bytesDownloaded The number of bytes downloaded so far.
		 * @param bytesTotal	 The total size of the file in bytes.
		 */
		void onMediaFireDownloadProgressUpdate(MediaFireDownloader downloader, String url, File path, int bytesDownloaded, int bytesTotal);
	}


	/**
	 * Non-static methods
	 */

	/**
	 * Constructor for the MediaFireDownloader class.
	 *
	 * @param context  The application context.
	 * @param listener The listener for download events.
	 */
	public MediaFireDownloader(Context context, DownloadListener listener) {
		this.context = context;
		this.listener = listener;
		this.downloadManager = (DownloadManager) context.getSystemService(Context.DOWNLOAD_SERVICE);
		this.handler = new Handler();
		this.downloadCompleteReceiver = new DownloadCompleteReceiver();
		context.registerReceiver(downloadCompleteReceiver, new IntentFilter(DownloadManager.ACTION_DOWNLOAD_COMPLETE));
	}

	/**
	 * Starts downloading a file from MediaFire.
	 *
	 * @param url  The URL of the file to be downloaded.
	 * @param path The path where the file should be saved.
	 */
	public void startDownload(String url, File path) {
		if (isValidMediaFireUrl(url)) {
			if (!inProcess) {
				inProcess = true;
				downloadUrl = url;
				downloadPath = path;
				downloaderInstance = this;

				webView = new WebView(context);
				webView.getSettings().setJavaScriptEnabled(true);
				webView.setWebViewClient(new CustomWebViewClient());
				webView.setDownloadListener(this);
				webView.loadUrl(url);
			} else {
				listener.onMediaFireDownloadFailed(this, url, path, ERROR_DOWNLOAD_IN_PROGRESS);
			}
		} else {
			listener.onMediaFireDownloadFailed(this, url, path, ERROR_INVALID_LINK);
		}
	}

	/**
	 * Checks if the provided URL is a valid MediaFire URL.
	 *
	 * @param url The URL to be checked.
	 * @return true if the URL is valid, false otherwise.
	 */
	public boolean isValidMediaFireUrl(String url) {
		if (!url.startsWith("https://www.mediafire.com/file/")) return false;
		return WebScraper.getContent(url).contains("id=\"downloadButton\"");
	}


	/**
	 * Static methods 
	 */

	/**
	 * Starts downloading a file from MediaFire.
	 *
	 * @param context  The application context.
	 * @param listener The listener for download events.
	 * @param url  The URL of the file to be downloaded.
	 * @param path The path where the file should be saved.
	 */
	public static void startDownload(Context context, DownloadListener listener, String url, File path) {
		new MediaFireDownloader(context, listener).startDownload(url, path);
	}

	/**
	 * Checks if the provided URL is a valid MediaFire URL.
	 *
	 * @param context  The application context.
	 * @param url The URL to be checked.
	 * @return true if the URL is valid, false otherwise.
	 */
	public static boolean isValidMediaFireUrl(Context context, String url) {
		return new MediaFireDownloader(context, null).isValidMediaFireUrl(url);
	}


	/**
	 * Called when the download is started.
	 *
	 * @param url			   The URL of the file to be downloaded.
	 * @param userAgent		 The user agent of the browser.
	 * @param contentDisposition The content disposition of the file.
	 * @param mimeType		  The MIME type of the file.
	 * @param contentLength	 The length of the content to be downloaded.
	 */
	@Override
	public void onDownloadStart(String url, String userAgent, String contentDisposition, String mimeType, long contentLength) {
		DownloadManager.Request request = new DownloadManager.Request(Uri.parse(url));
		String cookies = CookieManager.getInstance().getCookie(url);

		request.setMimeType(mimeType);
		request.addRequestHeader("cookie", cookies);
		request.addRequestHeader("User-Agent", userAgent);
		request.setDescription("Downloading file...");
		request.setTitle(URLUtil.guessFileName(url, contentDisposition, mimeType));
		request.allowScanningByMediaScanner();
		request.setDestinationUri(Uri.fromFile(downloadPath));
		request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE);
		downloadId = downloadManager.enqueue(request);

		progressRunnable = new ProgressRunnable();
		handler.post(progressRunnable);

		listener.onMediaFireDownloadStart(downloaderInstance, downloadUrl, downloadPath);
	}

	/**
	 * Runnable to periodically update the download progress.
	 */
	private class ProgressRunnable implements Runnable {
		@Override
		public void run() {
			DownloadManager.Query query = new DownloadManager.Query();
			query.setFilterById(downloadId);

			Cursor cursor = downloadManager.query(query);
			if (cursor != null && cursor.moveToFirst()) {
				int bytesDownloaded = cursor.getInt(cursor.getColumnIndex(DownloadManager.COLUMN_BYTES_DOWNLOADED_SO_FAR));
				int bytesTotal = cursor.getInt(cursor.getColumnIndex(DownloadManager.COLUMN_TOTAL_SIZE_BYTES));

				if (bytesTotal > 0) {
					listener.onMediaFireDownloadProgressUpdate(downloaderInstance, downloadUrl, downloadPath, bytesDownloaded, bytesTotal);
				}

				cursor.close();
			}

			handler.postDelayed(this, 250);
		}
	}

	/**
	 * Receiver to handle download complete broadcasts.
	 */
	private class DownloadCompleteReceiver extends BroadcastReceiver {
		@Override
		public void onReceive(Context context, Intent intent) {
			if (intent.getLongExtra(DownloadManager.EXTRA_DOWNLOAD_ID, -1) == downloadId) {
				handler.removeCallbacks(progressRunnable);
				DownloadManager.Query query = new DownloadManager.Query();
				query.setFilterById(downloadId);

				Cursor cursor = downloadManager.query(query);
				if (cursor != null && cursor.moveToFirst()) {
					int status = cursor.getInt(cursor.getColumnIndex(DownloadManager.COLUMN_STATUS));

					if (DownloadManager.STATUS_SUCCESSFUL == status) {
						listener.onMediaFireDownloadComplete(downloaderInstance, downloadUrl, downloadPath);
					} else {
						listener.onMediaFireDownloadFailed(downloaderInstance, downloadUrl, downloadPath, ERROR_FAILED_TO_DOWNLOAD);
					}

					inProcess = false;
					cursor.close();
				}
				context.unregisterReceiver(downloadCompleteReceiver);
			}
		}
	}

	/**
	 * Custom WebViewClient to handle web page loading.
	 */
	private class CustomWebViewClient extends WebViewClient {
		@Override
		public void onPageFinished(WebView view, String url) {
			super.onPageFinished(view, url);

			if (view.equals(webView)) {
				view.evaluateJavascript("document.getElementById('downloadButton').click();", null);
			}
		}
	}
}