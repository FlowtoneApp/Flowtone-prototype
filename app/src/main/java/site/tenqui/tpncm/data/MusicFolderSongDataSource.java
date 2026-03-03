package site.tenqui.tpncm.data;

import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.provider.MediaStore;

import java.util.ArrayList;
import java.util.List;

import site.tenqui.tpncm.Interface.SongDataSource;
import site.tenqui.tpncm.model.Song;

/**
 * 扫描系统媒体库中 Music/ 文件夹下的音频。
 * 关键：用 MediaStore + RELATIVE_PATH 来过滤路径（Android 10+靠谱）。
 */
public class MusicFolderSongDataSource implements SongDataSource {

    private final Context context;

    public MusicFolderSongDataSource(Context context) {
        this.context = context.getApplicationContext();
    }

    @Override
    public List<Song> getSongs() {
        List<Song> songs = new ArrayList<>();

        ContentResolver resolver = context.getContentResolver();

        String[] projection = new String[] {
                MediaStore.Audio.Media._ID,
                MediaStore.Audio.Media.TITLE,
                MediaStore.Audio.Media.ARTIST,
                MediaStore.Audio.Media.RELATIVE_PATH,
                MediaStore.Audio.Media.IS_MUSIC
        };

        // 只要 Music 文件夹里的 + 只要“音乐”（过滤铃声/通知音）
        String selection =
                MediaStore.Audio.Media.RELATIVE_PATH + " LIKE ? AND " +
                        MediaStore.Audio.Media.IS_MUSIC + "!= 0";

        String[] selectionArgs = new String[] { "%Music/%" };

        Cursor cursor = resolver.query(
                MediaStore.Audio.Media.EXTERNAL_CONTENT_URI,
                projection,
                selection,
                selectionArgs,
                MediaStore.Audio.Media.DATE_ADDED + " DESC"
        );

        if (cursor == null) return songs;

        int idCol = cursor.getColumnIndexOrThrow(MediaStore.Audio.Media._ID);
        int titleCol = cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.TITLE);
        int artistCol = cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.ARTIST);

        while (cursor.moveToNext()) {
            long id = cursor.getLong(idCol);
            String title = cursor.getString(titleCol);
            String artist = cursor.getString(artistCol);

            songs.add(new Song((int) id, title, artist));
        }

        cursor.close();
        return songs;
    }
}