package com.campus.xiaozhao.basic.utils;

import java.io.Closeable;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.Reader;
import java.io.UnsupportedEncodingException;
import java.io.Writer;
import java.net.HttpURLConnection;
import java.security.MessageDigest;
import java.util.Arrays;
import java.util.Properties;
import java.util.regex.Pattern;

import android.content.Context;
import android.content.res.AssetManager;
import android.database.Cursor;
import android.os.Environment;
import android.os.Parcel;
import android.os.Parcelable;
import android.text.TextUtils;

import com.component.logger.Logger;

import dalvik.system.DexFile;

/**
 * Class FileUtils
 * @author antoniochen
 */
public final class FileUtils {
    /**
     * The file exists, this flag use with {@link #fileAccess(String, int)}.
     */
    public static final int F_OK = 0x00;

    /**
     * The file executable, this flag use with {@link #fileAccess(String, int)}.
     */
    public static final int X_OK = 0x01;

    /**
     * The file writable, this flag use with {@link #fileAccess(String, int)}.
     */
    public static final int W_OK = 0x02;

    /**
     * The file readable, this flag use with {@link #fileAccess(String, int)}.
     */
    public static final int R_OK = 0x04;

    /**
     * The permission of read, write and execute by all users.
     * Same as <em>S_IRWXU | S_IRWXG | S_IRWXO</em>.
     */
    public static final int S_IRWXA = 00777;

    /**
     * The default permissions.
     * Same as <em>S_IRWXU | S_IRWXG | S_IROTH | S_IXOTH</em>.
     */
    public static final int S_IRWXD = 00775;

    /**
     * The permission of read, write by all users.
     * Same as <em>S_IRWXU | S_IRGRP | S_IWGRP | S_IROTH | S_IWOTH</em>.
     */
    public static final int S_IRWDA = 00766;

    /**
     * The permission of read, execute by all users.
     * Same as <em>S_IRWXU | S_IRGRP | S_IXGRP | S_IROTH | S_IXOTH</em>.
     */
    public static final int S_IRXDA = 00755;

    /**
     * The permission of read, write and execute by owner.
     */
    public static final int S_IRWXU = 00700;

    /**
     * The permission of read by owner.
     */
    public static final int S_IRUSR = 00400;

    /**
     * The permission of write by owner.
     */
    public static final int S_IWUSR = 00200;

    /**
     * The permission of execute by owner.
     */
    public static final int S_IXUSR = 00100;

    /**
     * The permission of read, write and execute by group.
     */
    public static final int S_IRWXG = 00070;

    /**
     * The permission of read by group.
     */
    public static final int S_IRGRP = 00040;

    /**
     * The permission of write by group.
     */
    public static final int S_IWGRP = 00020;

    /**
     * The permission of execute by group.
     */
    public static final int S_IXGRP = 00010;

    /**
     * The permission of read, write and execute by others.
     */
    public static final int S_IRWXO = 00007;

    /**
     * The permission of read by others.
     */
    public static final int S_IROTH = 00004;

    /**
     * The permission of write by others.
     */
    public static final int S_IWOTH = 00002;

    /**
     * The permission of execute by others.
     */
    public static final int S_IXOTH = 00001;

    /**
     * The permission of set user ID.
     */
    public static final int S_ISUID = 04000;

    /**
     * The permission of set group ID.
     */
    public static final int S_ISGID = 02000;

    /**
     * The permission of sticky bit.
     */
    public static final int S_ISVTX = 01000;

    /**
     * The file format mask.
     */
    public static final int S_IFMT = 0170000;

    /**
     * The file is sock.
     */
    public static final int S_IFSOCK = 0140000;

    /**
     * The file is symbolic link.
     */
    public static final int S_IFLNK = 0120000;

    /**
     * The file is regular file.
     */
    public static final int S_IFREG = 0100000;

    /**
     * The file is block device.
     */
    public static final int S_IFBLK = 0060000;

    /**
     * The file is directory.
     */
    public static final int S_IFDIR = 0040000;

    /**
     * The file is character device.
     */
    public static final int S_IFCHR = 0020000;

    /**
     * The file is FIFO (named pipe).
     */
    public static final int S_IFIFO = 0010000;

    /**
     * This flag use with {@link #mkdirs(String, int) mkdirs}.
     * The last item in path is a file name, so ignore.
     * <P>Example: if path is "/mnt/sdcard/mydir/myfile",
     * only use "/mnt/sdcard/mydir"</P>.
     */
    public static final int FLAG_IGNORE_FILENAME = 0x01;

    /**
     * The file system names denoting the directory itself.
     */
    public static final String SELF_DIRECTORY = ".";

    /**
     * The file system names denoting the directory's parent directory.
     */
    public static final String PARENT_DIRECTORY = "..";

    /**
     * The directory containing the JNI libraries.
     */
    public static final String DIRECTORY_LIBRARIES = "lib";

    /**
     * The directory containing the databases.
     */
    public static final String DIRECTORY_DATABASES = "databases";

    /**
     * The directory containing the shared preferences.
     */
    public static final String DIRECTORY_SHAREDPREFS = "shared_prefs";

    /**
     * The directory containing android-specific data on the SD card.
     */
    public static final String DIRECTORY_ANDROID_DATA = "Android/data";

    /**
     * The directory containing movie cache
     */
    public static final String DIRECTORY_MOVE_CACHE = "Movie";

    /**
     * The directory of application
     */
    public static final String DIRECTORY_APPLICATION_PATH = "campus/xiaozhao";

    /**
     * Returns the application's root directory,
     * such as <tt>"/data/data/packagename"</tt>.
     * @param context The <tt>Context</tt>.
     * @return The application's root directory.
     * @see #getLibraryDirectory(Context)
     * @see #getDatabaseDirectory(Context)
     * @see #getSharedPreferencesDirectory(Context)
     * @see #getExternalStorageAppDataDirectory(String)
     * @see #getExternalStorageAppFilesDirectory(String)
     */
    public static String getApplicationDirectory(Context context) {
        return context.getApplicationInfo().dataDir;
    }

    /**
     * Returns the application JNI libraries directory,
     * such as <tt>"/data/data/packagename/lib"</tt>.
     * @param context The <tt>Context</tt>.
     * @return The application JNI libraries directory.
     * @see #getDatabaseDirectory(Context)
     * @see #getApplicationDirectory(Context)
     * @see #getSharedPreferencesDirectory(Context)
     * @see #getExternalStorageAppDataDirectory(String)
     * @see #getExternalStorageAppFilesDirectory(String)
     */
    public static File getLibraryDirectory(Context context) {
        return new File(context.getApplicationInfo().dataDir, DIRECTORY_LIBRARIES);
    }

    /**
     * Returns the application databases directory, such
     * as <tt>"/data/data/packagename/databases"</tt>.
     * @param context The <tt>Context</tt>.
     * @return The application databases directory.
     * @see #getLibraryDirectory(Context)
     * @see #getApplicationDirectory(Context)
     * @see #getSharedPreferencesDirectory(Context)
     * @see #getExternalStorageAppDataDirectory(String)
     * @see #getExternalStorageAppFilesDirectory(String)
     */
    public static File getDatabaseDirectory(Context context) {
        return new File(context.getApplicationInfo().dataDir, DIRECTORY_DATABASES);
    }

    /**
     * Returns the application shared preferences directory,
     * such as <tt>"/data/data/packagename/shared_prefs"</tt>.
     * @param context The <tt>Context</tt>.
     * @return The application shared preferences directory.
     * @see #getLibraryDirectory(Context)
     * @see #getDatabaseDirectory(Context)
     * @see #getApplicationDirectory(Context)
     * @see #getExternalStorageAppDataDirectory(String)
     * @see #getExternalStorageAppFilesDirectory(String)
     */
    public static File getSharedPreferencesDirectory(Context context) {
        return new File(context.getApplicationInfo().dataDir, DIRECTORY_SHAREDPREFS);
    }

    /**
     * Returns the raw path to an application's data directory on the
     * SD card. such as <tt>"/mnt/sdcard/Android/data/packageName"</tt>.
     * @param packageName The application's package name.
     * @return The application's data directory on the SD card.
     * @see #getLibraryDirectory(Context)
     * @see #getDatabaseDirectory(Context)
     * @see #getApplicationDirectory(Context)
     * @see #getSharedPreferencesDirectory(Context)
     * @see #getExternalStorageAppFilesDirectory(String)
     */
    public static File getExternalStorageAppDataDirectory(String packageName) {
        return new File(Environment.getExternalStorageDirectory(), new StringBuilder(64).append(DIRECTORY_ANDROID_DATA).append('/').append(packageName).toString());
    }

    /**
     * Returns the raw path to an application's files directory on the SD
     * card. such as <tt>"/mnt/sdcard/Android/data/packageName/files"</tt>.
     * @param packageName The application's package name.
     * @return The application's files directory on the SD card.
     * @see #getLibraryDirectory(Context)
     * @see #getDatabaseDirectory(Context)
     * @see #getApplicationDirectory(Context)
     * @see #getSharedPreferencesDirectory(Context)
     * @see #getExternalStorageAppDataDirectory(String)
     */
    public static File getExternalStorageAppFilesDirectory(String packageName) {
        return new File(Environment.getExternalStorageDirectory(), new StringBuilder(64).append(DIRECTORY_ANDROID_DATA).append('/').append(packageName).append("/files").toString());
    }

    public static File getExternalStorageFilesDirectory() {
        return new File(Environment.getExternalStorageDirectory(), DIRECTORY_APPLICATION_PATH);
    }

    public static File getExternalStorageMovieCachedDirectory() {
        return new File(getExternalStorageFilesDirectory(), DIRECTORY_MOVE_CACHE);
    }

    /**
     * Closes the stream and releases any system resources associated with it. If the
     * stream is <tt>null</tt> or already closed then invoking this method has no effect.
     * @param c A Closeable is a source or destination of data that can be closed.
     * @see #close(Cursor)
     * @see #close(DexFile)
     * @see #close(HttpURLConnection)
     */
    public static void close(Closeable c) {
        if (c != null) {
            try {
                c.close();
            } catch (IOException e) {
                Logger.e(FileUtils.class.getName(), "Couldn't close - " + c.getClass().getName() + e.getMessage());
            }
        }
    }

    /**
     * Closes the DEX file, handling <tt>null</tt> <em>file</em>.
     * @param file A DEX file to close.
     * @see #close(Cursor)
     * @see #close(Closeable)
     * @see #close(HttpURLConnection)
     */
    public static void close(DexFile file) {
        if (file != null) {
            try {
                file.close();
            } catch (IOException e) {
                Logger.e(FileUtils.class.getName(), "Couldn't close - " + file.getName() + e.getMessage());
            }
        }
    }

    /**
     * Closes the <tt>Cursor</tt>, handling <tt>null Cursor</tt>.
     * @param cursor A <tt>Cursor</tt> to close.
     * @see #close(DexFile)
     * @see #close(Closeable)
     * @see #close(HttpURLConnection)
     */
    public static void close(Cursor cursor) {
        if (cursor != null) {
            try {
                cursor.close();
            } catch (Throwable e) {
                Logger.e(FileUtils.class.getName(), "Couldn't close cursor - " + cursor + e.getMessage());
            }
        }
    }

    /**
     * Closes the http connection, handling <tt>null</tt> <em>conn</em>.
     * @param conn A http connection to close.
     * @see #close(Cursor)
     * @see #close(DexFile)
     * @see #close(Closeable)
     */
    public static void close(HttpURLConnection conn) {
        if (conn != null) {
            conn.disconnect();
        }
    }

    /**
     * Tests if the filename is valid.
     * @param filename The filename to test.
     * @return <tt>true</tt> if the filename is valid, <tt>false</tt> otherwise.
     */
    public static boolean isFilenameValid(CharSequence filename) {
        return (!(TextUtils.isEmpty(filename) || SELF_DIRECTORY.contentEquals(filename)
                || PARENT_DIRECTORY.contentEquals(filename)) && PATTERN.matcher(filename).matches());
    }

    /**
     * Tests if the <em>path</em> is linux root directory ('/').
     * @param path The path to test.
     * @return <tt>true</tt> if the path is root directory, <tt>false</tt> otherwise.
     * @see #isAbsolutePath(CharSequence)
     */
    public static boolean isRootDir(CharSequence path) {
        return (path != null && path.length() == 1 && path.charAt(0) == File.separatorChar);
    }

    /**
     * Tests if the <em>path</em> is absolute path.
     * @param path The path to test.
     * @return <tt>true</tt> if the path is absolute path, <tt>false</tt> otherwise.
     * @see #isRootDir(CharSequence)
     */
    public static boolean isAbsolutePath(CharSequence path) {
        return (path != null && path.length() > 0 && path.charAt(0) == File.separatorChar);
    }

    /**
     * Searches the file extension position in the specified <em>path</em>.
     * @param path The path to search.
     * @return The index in <em>path</em>, or -1 if the extension was not found.
     * @see #getFileExtension(String)
     */
    public static int findFileExtension(CharSequence path) {
        final int end = path.length() - 1;
        for (int i = end; i >= 0; --i) {
            final char c = path.charAt(i);
            if (c == File.separatorChar) {
                return -1;
            } else if (c == '.') {
                return (i != end ? i + 1 : -1);
            }
        }

        return -1;
    }

    /**
     * Returns the file extension of the specified <em>path</em>.
     * @param path The file path.
     * @return The file extension, or <tt>null</tt> if the extension was not found.
     * @see #findFileExtension(CharSequence)
     */
    public static String getFileExtension(String path) {
        final int index = findFileExtension(path);
        return (index != -1 ? path.substring(index) : null);
    }

    /**
     * Changes the permissions of the file with the specified <em>path</em>.
     * @param path The path to change, must be absolute file path.
     * @param mode The permissions. Pass any combination of S_IXXX constants.
     * @return Returns <tt>0</tt> if the operation succeeded, Otherwise returns
     * an error code. See {@link Errors}.
     */
    public static native int chmod(String path, int mode);

    /**
     * Creates the directories with the specified <em>path</em>.
     * @param path The path to create, must be absolute file path.
     * @param flags Creating flags. Pass 0 or {@link #FLAG_IGNORE_FILENAME}.
     * @return Returns <tt>0</tt> if the necessary directories have been
     * created or the target directory already exists, Otherwise returns
     * an error code. See {@link Errors}.
     */
    public static native int mkdirs(String path, int flags);

    /**
     * Returns the file length in bytes.
     * @param filename The filename, must be absolute file path.
     * @return The file length if the operation succeeded, Otherwise
     * returns <tt>-1</tt>.
     */
    public static native long fileLength(String filename);

    /**
     * Checks the file access mode with the specified <em>path</em>.
     * This operation is supported for both file and directory.
     * @param path The file or directory path, must be absolute file path.
     * @param mode The access mode. May be any combination of {@link #R_OK},
     * {@link #W_OK}, {@link #X_OK} and {@link #F_OK}.
     * @return Returns <tt>0</tt> if the operation succeeded, Otherwise returns
     * an error code. See {@link Errors}.
     */
    public static native int fileAccess(String path, int mode);

    /**
     * Returns the file status (include mode, uid, gid, size, etc.) with the
     * specified <em>path</em>.
     * @param path The file or directory path, must be absolute file path.
     * @param outStat The {@link Stat} to store the result.
     * @return Returns <tt>0</tt> if the operation succeeded, Otherwise returns
     * an error code. See {@link Errors}.
     */
    public static native int fileStatus(String path, Stat outStat);

    /**
     * Copies the <em>src</em> file to <em>dst</em> file. If the <em>dst</em> file
     * already exists, it can be overrided to. <p>Note that this method creates the
     * <em>dst</em> file necessary directories.</p>
     * @param src The source file to read, must be absolute file path.
     * @param dst The destination file to write, must be absolute file path.
     * @return Returns <tt>0</tt> if the operation succeeded, Otherwise returns an
     * error code. See {@link Errors}.
     */
    public static native int copyFile(String src, String dst);

    /**
     * Copies the specified <tt>InputStream</tt> the contents to <em>dst</em> file.
     * If the <em>dst</em> file already exists, it can be overrided to. <p>Note that
     * this method creates the <em>dst</em> file necessary directories.</p>
     * @param src The <tt>InputStream</tt> to read.
     * @param dst The destination file to write, must be absolute file path.
     * @param closeStream Whether to close the <em>src</em> after copy finished.
     * @param md5code Whether to check the <em>src</em> contents MD5. This parameter
     * can be <tt>null</tt>.
     * @return <tt>true</tt> if the operation succeeded, <tt>false</tt> otherwise.
     */
    public static boolean copyStream(InputStream src, String dst, boolean closeStream, byte[] md5code) {
        boolean successful = false;
        try {
            if (mkdirs(dst, FLAG_IGNORE_FILENAME) == 0) {
                copyStream(src, new FileOutputStream(dst), closeStream, md5code);
                successful = (fileAccess(dst, F_OK) == 0);
            }
        } catch (Exception e) {
            Logger.e(FileUtils.class.getName(), "Couldn't copy - '" + src.getClass().getName() + "' to '" + dst + "'" + e.getMessage());
            deleteFiles(dst, false);
        }

        return successful;
    }

    /**
     * Copies the "assets" directory <em>src</em> file to <em>dst</em> file. If the
     * <em>dst</em> file already exists, it can be overrided to. <p>Note that this
     * method creates the <em>dst</em> file necessary directories.</p>
     * @param assetManager The <tt>AssetManager</tt>.
     * @param src A relative path within the assets, such as <tt>"docs/home.html"</tt>.
     * @param dst The destination file to write, must be absolute file path.
     * @return Returns <tt>0</tt> if the operation succeeded, Otherwise returns an
     * error code. See {@link Errors}.
     */
    public static native int copyAssetFile(AssetManager assetManager, String src, String dst);

    /**
     * Reads the specified file contents to a byte array.
     * @param filename The file to read, must be absolute file path.
     * @return A byte array if the operation succeeded, <tt>null</tt> otherwise.
     */
    public static native byte[] readFile(String filename);

    /**
     * Reads the specified file contents to a string.
     * @param filename The file to read, must be absolute file path.
     * @return A string if the operation succeeded, <tt>null</tt> otherwise.
     * @see #readFileString(String, String)
     */
    public static String readFileString(String filename) {
        final byte[] data = readFile(filename);
        return (data != null ? new String(data) : null);
    }

    /**
     * Reads the specified file contents to a string.
     * @param filename The file to read, must be absolute file path.
     * @param charsetName The charset name of the file contents.
     * @return A string if the operation succeeded, <tt>null</tt> otherwise.
     * @see #readFileString(String)
     */
    public static String readFileString(String filename, String charsetName) {
        String result = null;
        try {
            final byte[] data = readFile(filename);
            if (data != null) {
                result = new String(data, charsetName);
            }
        } catch (UnsupportedEncodingException e) {
            Logger.e(FileUtils.class.getName(), "Unsupported charset - " + charsetName + e.getMessage());
        }

        return result;
    }

    /**
     * Reads the "assets" directory file contents to a byte array.
     * @param assetManager The <tt>AssetManager</tt>.
     * @param filename A relative path within the assets, such as <tt>"docs/home.html"</tt>.
     * @return A byte array if the operation succeeded, <tt>null</tt> otherwise.
     */
    public static native byte[] readAssetFile(AssetManager assetManager, String filename);

    /**
     * Reads the "assets" directory file contents to a string.
     * @param assetManager The <tt>AssetManager</tt>.
     * @param filename A relative path within the assets, such as <tt>"docs/home.html"</tt>.
     * @return A string if the operation succeeded, <tt>null</tt> otherwise.
     * @see #readAssetString(AssetManager, String, String)
     */
    public static String readAssetString(AssetManager assetManager, String filename) {
        final byte[] data = readAssetFile(assetManager, filename);
        return (data != null ? new String(data) : null);
    }

    /**
     * Reads the "assets" directory file contents to a string.
     * @param assetManager The <tt>AssetManager</tt>.
     * @param filename A relative path within the assets, such as <tt>"docs/home.html"</tt>.
     * @param charsetName The charset name of the file contents.
     * @return A string if the operation succeeded, <tt>null</tt> otherwise.
     * @see #readAssetString(AssetManager, String)
     */
    public static String readAssetString(AssetManager assetManager, String filename, String charsetName) {
        String result = null;
        try {
            final byte[] data = readAssetFile(assetManager, filename);
            if (data != null) {
                result = new String(data, charsetName);
            }
        } catch (UnsupportedEncodingException e) {
            Logger.e(FileUtils.class.getName(), "Unsupported charset - " + charsetName + e.getMessage());
        }

        return result;
    }

    /**
     * Moves the file from <em>oldPath</em> to <em>newPath</em>. If the <em>newPath</em>
     * file already exists, it can be replaced to. This operation is supported for both
     * file and directory. <p>Note that this method creates the <em>newPath</em> necessary
     * directories.</p>
     * @param oldPath The old path, must be absolute file path.
     * @param newPath The new path, must be absolute file path.
     * @return Returns <tt>0</tt> if the operation succeeded,
     * Otherwise returns an error code. See {@link Errors}.
     * @see #deleteFiles(String, boolean)
     */
    public static native int moveFile(String oldPath, String newPath);

    /**
     * Deletes a file or directory with specified <em>path</em>. if <em>path</em>
     * is a directory, all sub files and sub directories will be deleted.
     * @param path The file or directory to delete, must be absolute file path.
     * @param deleteSelf Whether to delete the <em>path</em> itself. If the
     * <em>path</em> is a file path, this parameter is ignored.
     * @return Returns <tt>0</tt> if the operation succeeded, Otherwise returns an
     * error code. See {@link Errors}.
     * @see #moveFile(String, String)
     */
    public static native int deleteFiles(String path, boolean deleteSelf);

    /**
     * Creates a file with the specified <em>filename</em>. If the file was
     * created the file length is the <em>length</em> and the content is empty.
     * If the specified file already exists, it can be overrided to. <p>Note
     * that this method creates the necessary directories.</p>
     * @param filename The filename to create, must be absolute file path.
     * @param length The desired file length in bytes.
     * @return Returns <tt>0</tt> if the operation succeeded, Otherwise returns
     * an error code. See {@link Errors}.
     * @see #createUniqueFile(String, int)
     */
    public static native int createFile(String filename, int length);

    /**
     * Creates a unique file with the specified <em>filename</em>. If the file
     * was created the file length is the <em>length</em> and the content is
     * empty. <p>Note that this method creates the necessary directories.</p>
     * @param filename The original filename to create, must be absolute file path.
     * @param length The desired file length.
     * @return Returns the unique filename (include file path), or <tt>null</tt>
     * if the file could't be created.
     * @see #createFile(String, int)
     */
    public static native String createUniqueFile(String filename, int length);

    /**
     * Loads {@link Properties} from the specified file, assumed to be default charset.
     * @param filename The filename to load, must be absolute file path.
     * @return A <tt>Properties</tt> if the operation succeeded, <tt>null</tt> otherwise.
     * @see #loadProperties(AssetManager, String)
     * @see #saveProperties(Properties, String)
     */
    public static Properties loadProperties(String filename) {
        try {
            return loadProperties(new FileInputStream(filename));
        } catch (FileNotFoundException e) {
            return null;
        }
    }

    /**
     * Loads {@link Properties} from the "assets" directory's file, assumed to be default charset.
     * @param assetManager The <tt>AssetManager</tt>.
     * @param filename A relative path within the assets, such as <tt>"docs/home.html"</tt>.
     * @return A <tt>Properties</tt> if the operation succeeded, <tt>null</tt> otherwise.
     * @see #loadProperties(String)
     * @see #saveProperties(Properties, String)
     */
    public static Properties loadProperties(AssetManager assetManager, String filename) {
        try {
            return loadProperties(assetManager.open(filename, AssetManager.ACCESS_STREAMING));
        } catch (IOException e) {
            return null;
        }
    }

    /**
     * Saves the specified {@link Properties} to the specified file, using default charset.
     * <p>Note that this method creates the necessary directories.</p>
     * @param props The <tt>Properties</tt> to save.
     * @param filename The filename to save, must be absolute file path.
     * @return <tt>true</tt> if the operation succeeded, <tt>false</tt> otherwise.
     * @see #loadProperties(String)
     * @see #loadProperties(AssetManager, String)
     */
    public static boolean saveProperties(Properties props, String filename) {
        Writer writer = null;
        try {
            final int errno = mkdirs(filename, FLAG_IGNORE_FILENAME);
            if (errno != 0) {
                throw new IOException(new StringBuilder("Couldn't create file '").append(filename).append("' - errno = ").append(errno).append(", error = ").append(Errors.toString(errno)).toString());
            }

            writer = new OutputStreamWriter(new FileOutputStream(filename));
            props.store(writer, null);
            return true;
        } catch (IOException e) {
            Logger.e(FileUtils.class.getName(), "Couldn't save Properties - " + props + e.getMessage());
            return false;
        } finally {
            close(writer);
        }
    }

    /**
     * Loads {@link Properties} from <tt>InputStream</tt>, assumed to be default charset.
     * @param is The <tt>InputStream</tt> to load.
     * @return A <tt>Properties</tt> if the operation succeeded, <tt>null</tt> otherwise.
     */
    private static Properties loadProperties(InputStream is) {
        final Reader reader = new InputStreamReader(is);
        try {
            final Properties props = new Properties();
            props.load(reader);
            return props;
        } catch (IOException e) {
            Logger.e(FileUtils.class.getName(), "Couldn't load properties - " + is.getClass().getName() +  e.getMessage());
            return null;
        } finally {
            close(reader);
        }
    }

    /**
     * Copies the specified <tt>InputStream</tt> the contents to <tt>OutputStream</tt>
     * <em>dst</em>.
     * @param src The <tt>InputStream</tt> to read.
     * @param dst The <tt>OutputStream</tt> to write.
     * @param closeStream Whether to close the <em>src</em> after copy finished.
     * @param md5code Whether to check the <em>src</em> contents MD5. This parameter
     * can be <tt>null</tt>.
     * @return <tt>true</tt> if the operation succeeded, <tt>false</tt> otherwise.
     * @throws Exception if an error occurs while writing to <em>dst</em>.
     */
    public static void copyStream(InputStream src, OutputStream dst, boolean closeStream, byte[] md5code) throws Exception {
        try {
            final byte[] buf = new byte[8192];
            if (ArrayUtils.isEmpty(md5code)) {
                // Copies the src contents to dst.
                for (int readBytes = 0; (readBytes = src.read(buf)) > 0; ) {
                    dst.write(buf, 0, readBytes);
                }
            } else {
                // Copies the src contents to dst with check MD5.
                final MessageDigest md5 = MessageDigest.getInstance("MD5");
                for (int readBytes = 0; (readBytes = src.read(buf)) > 0; ) {
                    dst.write(buf, 0, readBytes);
                    md5.update(buf, 0, readBytes);
                }

                // Checks the contents MD5.
                final byte[] digest = md5.digest();
                if (digest != null && !Arrays.equals(digest, md5code)) {
                    throw new IOException("Checked the '" + src.getClass().getName() + "' MD5 failed - MD5 : " + StringUtils.toHexString(md5code, true) + ", computed MD5 : " + StringUtils.toHexString(digest, true));
                }
            }
        } finally {
            close(dst);
            if (closeStream) close(src);
        }
    }

    /**
     * Class Stat is wrapper for linux structure <tt>stat</tt>.
     */
    public static class Stat implements Parcelable {
        /**
         * The protection.
         */
        public int mode;

        /**
         * The user ID of owner.
         */
        public int uid;

        /**
         * The group ID of owner.
         */
        public int gid;

        /**
         * The total size in bytes.
         */
        public long size;

        /**
         * The number of 512B blocks allocated.
         */
        public long blocks;

        /**
         * The blocksize for file system I/O in bytes.
         */
        public int blksize;

        /**
         * The last access time, in milliseconds.
         */
        public long atime;

        /**
         * The last modify time, in milliseconds.
         */
        public long mtime;

        /**
         * The last status change time, in milliseconds.
         */
        public long ctime;

        /**
         * Tests if the file is regular file.
         * @return <tt>true</tt> if the file is regular file, <tt>false</tt> otherwise.
         * @see #isDirectory()
         * @see #contains(int)
         */
        public final boolean isFile() {
            return ((mode & S_IFMT) == S_IFREG);
        }

        /**
         * Tests if the file is directory.
         * @return <tt>true</tt> if the file is directory, <tt>false</tt> otherwise.
         * @see #isFile()
         * @see #contains(int)
         */
        public final boolean isDirectory() {
            return ((mode & S_IFMT) == S_IFDIR);
        }

        /**
         * Tests if the file contains the specified <em>mask</em>.
         * @param mask The mask to test. Pass any combination of S_IXXX constants.
         * @return <tt>true</tt> if the file contains, <tt>false</tt> otherwise.
         * @see #isFile()
         * @see #isDirectory()
         */
        public final boolean contains(int mask) {
            return ((mode & S_IFMT) == mask);
        }

        /**
         * Reads this object from the data stored in the specified parcel. To
         * write this object to a parcel, call {@link #writeToParcel(Parcel, int)}.
         * @param source The parcel to read the data.
         */
        public void readFromParcel(Parcel source) {
            mode    = source.readInt();
            uid     = source.readInt();
            gid     = source.readInt();
            size    = source.readLong();
            blocks  = source.readLong();
            blksize = source.readInt();
            atime   = source.readLong();
            mtime   = source.readLong();
            ctime   = source.readLong();
        }

        @Override
        public void writeToParcel(Parcel dest, int flags) {
            dest.writeInt(mode);
            dest.writeInt(uid);
            dest.writeInt(gid);
            dest.writeLong(size);
            dest.writeLong(blocks);
            dest.writeInt(blksize);
            dest.writeLong(atime);
            dest.writeLong(mtime);
            dest.writeLong(ctime);
        }

        @Override
        public int describeContents() {
            return 0;
        }

        @Override
        public String toString() {
            return new StringBuilder(192)
                .append("[ mode = ").append(Integer.toOctalString(mode))
                .append(", uid = ").append(uid)
                .append(", gid = ").append(gid)
                .append(", size = ").append(size)
                .append(", blocks = ").append(blocks)
                .append(", blksize = ").append(blksize)
                .append(", atime = ").append(atime)
                .append(", mtime = ").append(mtime)
                .append(", ctime = ").append(ctime)
                .append(" ]").toString();
        }

        public static final Creator<Stat> CREATOR = new Creator<Stat>() {
            @Override
            public Stat createFromParcel(Parcel source) {
                final Stat stat = new Stat();
                stat.readFromParcel(source);
                return stat;
            }

            @Override
            public Stat[] newArray(int size) {
                return new Stat[size];
            }
        };
    }

    /**
     * Regular expression for valid filenames : no spaces or metacharacters.
     */
    private static final Pattern PATTERN = Pattern.compile("[\\w%+,./=_-]+");

    /**
     * This utility class cannot be instantiated.
     */
    private FileUtils() {
    }
}