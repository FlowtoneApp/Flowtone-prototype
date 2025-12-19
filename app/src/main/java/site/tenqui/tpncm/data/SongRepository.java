package site.tenqui.tpncm.data;

import java.util.Arrays;
import java.util.List;

import site.tenqui.tpncm.Interface.SongDataSource;
import site.tenqui.tpncm.model.Song;

public class SongRepository implements SongDataSource {

    @Override
    public List<Song> getSongs() {
        return Arrays.asList(
                new Song(1, "晴天", "周杰伦"),
                new Song(2, "夜曲", "周杰伦"),
                new Song(3, "稻香", "周杰伦")
        );
    }
}
