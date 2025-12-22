package site.tenqui.tpncm.model;

public class Song {
    public final long id;
    public final String name;
    public final String artist;
    public final String path;

    public Song(long id, String name, String artist){
        this.id = id;
        this.name = name;
        this.artist = artist;
        this.path = null;
    }

    public Song(long id, String name, String artist, String path){
        this.id = id;
        this.name = name;
        this.artist = artist;
        this.path = path;
    }

}
