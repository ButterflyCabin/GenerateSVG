package bean;

/**
 * Created by ButterflyCabin on 2018/3/18.
 */
public class Point {
    public int x;
    public int y;
    public int scaleTag;

    public Point(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public Point(int x, int y, int scaleTag) {
        this.x = x;
        this.y = y;
        this.scaleTag = scaleTag;
    }

    @Override
    public String toString() {
        return "Point{" +
                "x=" + x +
                ", y=" + y +
                ", scaleTag=" + scaleTag +
                '}';
    }
}
