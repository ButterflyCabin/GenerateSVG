package buidler;

import bean.Point;

import java.util.List;

/**
 * Created by ButterflyCabin on 2018/3/18.
 */
public class SVGBuilder {
    private static StringBuilder tagStart = new StringBuilder();
    private static StringBuilder tagEnd = new StringBuilder();

    public static void putTagNotes(String notes) {
        tagStart.append("<!-- \" + notes + \" -->");
    }

    public static void putTagHead(String content) {
        tagStart.append(content);
    }

    public static void putTagClosed(String tag, String attr) {
        tagStart.append("\r\n<" + tag + " " + attr + "/>");
    }

    public static void putTagAndAttr(String tag, String attr) {
        tagStart.append("\r\n<" + tag + " " + attr + ">");
        tagEnd.insert(0, "\r\n</" + tag + ">");
    }

    public static void putTagAndContentClosed(String tag, String content) {
        tagStart.append("\r\n<" + tag + ">" + content + "</" + tag + ">");
    }

    public static void putTagClosed(String tag, String attr, String content) {
        tagStart.append("\r\n<" + tag + " " + attr + ">" + content + "</" + tag + ">");
    }

    public static String create() {
        return tagStart.toString() + tagEnd.toString();
    }

    public static void putTagScaleText(List<Point> points, int offset, Point OPoint) {
        for (int i = 0; i < points.size(); i++) {
            Point point = points.get(i);
            String attr = "x=\"%1$d\" y=\"%2$d\" fill=\"#000\"";
            int x_offset = point.x;
            x_offset -= offset;
            if ((String.valueOf(point.scaleTag)).length() == 2) {
                x_offset -= offset;
            }
            if (OPoint.y == point.y) {// X 轴
                attr = String.format(attr, x_offset, point.y + 4 * offset);
            } else {
                x_offset -= 2 * offset;
                attr = String.format(attr, x_offset, point.y + offset);
            }
            putTagClosed("text", attr, point.scaleTag + "");
        }
    }

    public static void putTagOPointText(Point OPoint, int offset) {
        String attr = "x=\"%1$d\" y=\"%2$d\" fill=\"#f00\"";
        attr = String.format(attr, OPoint.x - 3 * offset, OPoint.y + 4 * offset);
        putTagClosed("text", attr, OPoint.scaleTag + "");
    }

    public static void putTagAxisDirection(List<Point> points) {
        //  <polyline points="20,20 40,25 60,40 80,120 120,140 200,180"  style="fill:none;stroke:black;stroke-width:3" />
        String attr = "points=\"%1$s \" style=\"fill:none;stroke:black;stroke-width:1.2\"";
        Point point = points.get(0);
        String data = "";
        for (int i = 0; i < points.size(); i++) {
            point = points.get(i);
            data += point.x + "," + point.y + " ";
        }
        putTagClosed("polyline", String.format(attr, data));
    }

}
