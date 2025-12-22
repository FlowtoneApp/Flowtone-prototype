package site.tenqui.tpncm.data;

import java.util.Arrays;
import java.util.List;
import java.util.ArrayList;
import java.util.Locale;

import site.tenqui.tpncm.Interface.SongDataSource;
import site.tenqui.tpncm.model.Song;
import android.media.MediaMetadataRetriever;
import java.io.File;
import android.content.Context;
import android.net.Uri;
import androidx.documentfile.provider.DocumentFile;

public class SongRepository implements SongDataSource {

    private List<Song> songs = null;
    private long nextId = 1;

    @Override
    public List<Song> getSongs() {
        if (songs != null) return songs;
        return Arrays.asList(
                new Song(1, "晴天", "周杰伦"),
                new Song(2, "夜曲", "周杰伦"),
                new Song(3, "稻香", "周杰伦")
        );
    }

    public void scanDirectory(String path) {
        File dir = new File(path);
        if (!dir.exists() || !dir.isDirectory()) {
            return;
        }
        List<Song> list = new ArrayList<>();
        for (File f : dir.listFiles()) {
            if (f == null || !f.isFile()) continue;
            String name = f.getName();
            String lower = name.toLowerCase(Locale.ROOT);
            if (!(lower.endsWith(".mp3") || lower.endsWith(".wav") || lower.endsWith(".flac") || lower.endsWith(".m4a"))) {
                continue;
            }
            String title = name;
            String artist = "";
            try {
                MediaMetadataRetriever mmr = new MediaMetadataRetriever();
                mmr.setDataSource(f.getAbsolutePath());
                String t = mmr.extractMetadata(MediaMetadataRetriever.METADATA_KEY_TITLE);
                String a = mmr.extractMetadata(MediaMetadataRetriever.METADATA_KEY_ARTIST);
                if (t != null && !t.isEmpty()) title = t;
                if (a != null && !a.isEmpty()) artist = a;
                mmr.release();
            } catch (Exception ignored) {}
            list.add(new Song(nextId++, title, artist, f.getAbsolutePath()));
        }
        songs = list;
    }

    public void scanTree(Context context, Uri treeUri) {
        DocumentFile root = DocumentFile.fromTreeUri(context, treeUri);
        if (root == null || !root.isDirectory()) return;
        List<Song> list = new ArrayList<>();
        for (DocumentFile df : root.listFiles()) {
            if (df == null || !df.isFile()) continue;
            String type = df.getType();
            String name = df.getName();
            if (type == null) type = "";
            if (!(type.startsWith("audio/") || (name != null && name.toLowerCase(Locale.ROOT).matches(".*\\.(mp3|wav|flac|m4a)$")))) {
                continue;
            }
            String title = name != null ? name : "";
            String artist = "";
            try {
                MediaMetadataRetriever mmr = new MediaMetadataRetriever();
                mmr.setDataSource(context, df.getUri());
                String t = mmr.extractMetadata(MediaMetadataRetriever.METADATA_KEY_TITLE);
                String a = mmr.extractMetadata(MediaMetadataRetriever.METADATA_KEY_ARTIST);
                if (t != null && !t.isEmpty()) title = t;
                if (a != null && !a.isEmpty()) artist = a;
                mmr.release();
            } catch (Exception ignored) {}
            list.add(new Song(nextId++, title, artist, df.getUri().toString()));
        }
        songs = list;
    }
}
