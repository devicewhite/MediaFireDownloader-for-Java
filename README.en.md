# MediaFireDownloader
[![pt-br](https://img.shields.io/badge/PORTUGUES-Clique%20para%20Traduzir-blue.svg)](README.md)

## Description
`MediaFireDownloader` is a Java library designed to facilitate downloading files from MediaFire in Android applications. With a simple interface and intuitive methods, the library enables you to start, monitor, and manage downloads directly from MediaFire, handling various events such as the start, progress, completion, and failure of the download.

### Key Features:
- **Simplified Downloading**: Easily start file downloads from MediaFire by simply providing the URL and the destination path.
- **URL Validation**: Check if a URL is valid for MediaFire download before starting the process.
- **Download Events**: Receive detailed notifications about the download status through a listener interface.
- **Error Handling**: Identify and handle common errors during the download process, such as invalid links or download failures.

This library is ideal for developers looking to quickly and efficiently integrate MediaFire file downloading functionality into their Android applications.

## Error Codes
- `ERROR_INVALID_LINK`: Error code for an invalid link.<br />
- `ERROR_DOWNLOAD_IN_PROGRESS`: Error code for download in progress.<br />
- `ERROR_FAILED_TO_DOWNLOAD`: Error code for download failure.<br />

## Public Methods
#### `MediaFireDownloader`:
This constructor initializes a new instance of `MediaFireDownloader` with the provided context and a download listener.

| Parameter  | Type               | Description                                                         |
|------------|--------------------|-------------------------------------------------------------------|
| `context`  | `Context`          | The application context, used to access system services.           |
| `listener` | `DownloadListener` | A listener that will be notified about download events.             |
>
**Return** <br />
This constructor does not return anything, but initializes the instance of `MediaFireDownloader`.

#### `startDownload`:
This method starts the process of downloading a file from a specified URL.

| Parameter | Type    | Description                                      |
|-----------|---------|------------------------------------------------|
| `url`     | `String`| The URL of the file to be downloaded.            |
| `path`    | `File`  | The path where the file will be saved after the download. |
>
**Return** <br />
This method does not return anything.

> [!Warning]
> If the provided link is invalid, the method will emit an error to `onDownloadFailed` with the value `ERROR_INVALID_LINK`.

#### `isValidMediaFireUrl`:
This method checks if a provided URL is a valid MediaFire link.

| Parameter | Type    | Description                               |
|-----------|---------|-----------------------------------------|
| `url`     | `String`| The URL to be checked.                   |
>
**Return** <br />
This method returns a boolean value:
- `true` if the URL is a valid MediaFire link.
- `false` otherwise.

#### `startDownload`:
This static method starts the process of downloading a file from a specified URL.

| Parameter  | Type               | Description                                                         |
|------------|--------------------|-------------------------------------------------------------------|
| `context`  | `Context`          | The application context, used to access system services.            |
| `listener` | `DownloadListener` | A listener that will be notified about download events.             |
| `url`      | `String`           | The URL of the file to be downloaded.                               |
| `path`     | `File`             | The path where the file will be saved after the download.           |
>
**Return** <br />
This method does not return anything, but starts the download process.

> [!Warning]
> If the provided link is invalid, the method will emit an error to `onDownloadFailed` with the value `ERROR_INVALID_LINK`.

#### `isValidMediaFireUrl`:
This static method checks if a provided URL is a valid MediaFire link.

| Parameter | Type    | Description                               |
|-----------|---------|-----------------------------------------|
| `context` | `Context`| The application context, used to access system services. |
| `url`     | `String`| The URL to be checked.                   |
>
**Return** <br />
This method returns a boolean value:
- `true` if the URL is a valid MediaFire link.
- `false` otherwise.

## Listener Public Methods
The `DownloadListener` interface provides methods to listen for download-related events.

#### `onMediaFireDownloadStart`:
Called when the download is started.

| Parameter  | Type                   | Description                                                        |
|------------|------------------------|------------------------------------------------------------------|
| `downloader` | `MediaFireDownloader` | The instance of `MediaFireDownloader`.                            |
| `url`       | `String`              | The URL of the file being downloaded.                              |
| `path`      | `File`                | The path where the file will be saved.                             |
>
**Return** <br />
This method does not return anything.

#### `onMediaFireDownloadComplete`:
Called when the download is successfully completed.

| Parameter  | Type                   | Description                                                        |
|------------|------------------------|------------------------------------------------------------------|
| `downloader` | `MediaFireDownloader` | The instance of `MediaFireDownloader`.                            |
| `url`       | `String`              | The URL of the downloaded file.                                    |
| `path`      | `File`                | The path where the file was saved.                                 |
>
**Return** <br />
This method does not return anything.

#### `onMediaFireDownloadFailed`:
Called when the download fails.

| Parameter  | Type                   | Description                                                        |
|------------|------------------------|------------------------------------------------------------------|
| `downloader` | `MediaFireDownloader` | The instance of `MediaFireDownloader`.                            |
| `url`       | `String`              | The URL of the file that failed to download.                       |
| `path`      | `File`                | The path where the file should have been saved.                    |
| `error`     | `int`                 | The error code associated with the download failure.               |
>
**Return** <br />
This method does not return anything.

#### `onMediaFireDownloadProgressUpdate`:
Called periodically to update the download progress.

| Parameter         | Type                   | Description                                                        |
|-------------------|------------------------|------------------------------------------------------------------|
| `downloader`      | `MediaFireDownloader`  | The instance of `MediaFireDownloader`.                            |
| `url`             | `String`               | The URL of the file being downloaded.                              |
| `path`            | `File`                 | The path where the file is being saved.                            |
| `bytesDownloaded` | `int`                  | The number of bytes downloaded so far.                             |
| `bytesTotal`      | `int`                  | The total size of the file in bytes.                               |
>
**Return** <br />
This method does not return anything.

# License

This project is licensed under the GNU Affero General Public License v3.0 (AGPL-3.0), with the following additional clause:

You may not sell the software, whether modified or not, independently. However, you may redistribute, modify, and create derivative works of the software as long as you keep the same license and attribution, and these derivative works are also distributed non-commercially.

For more details, see the [LICENSE](LICENSE) file.
