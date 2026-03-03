package site.tenqui.tpncm.data;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import site.tenqui.tpncm.Interface.SongDataSource;
import site.tenqui.tpncm.model.Song;

/**
 * Repository：把多个数据源的结果合并起来。
 * 现在只塞本地 MusicFolder 数据源；未来你塞 Online 数据源也不会动 UI。
 */
public class SongRepository implements SongDataSource {

    private final List<SongDataSource> sources;

    public SongRepository(List<SongDataSource> sources) {
        this.sources = (sources == null) ? Collections.emptyList() : sources;
    }

    @Override
    public List<Song> getSongs() {
        List<Song> result = new ArrayList<>();

        for (SongDataSource source : sources) {
            try {
                List<Song> part = source.getSongs();
                if (part != null) result.addAll(part);
            } catch (Exception ignored) {
            }
        }

        return result;
    }
}