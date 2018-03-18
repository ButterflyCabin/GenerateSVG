import bean.Point;
import buidler.SVGBuilder;
import org.junit.Test;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by ButterflyCabin on 2018/3/18.
 */
public class GenerateSVGUtil {
    private static final int AM = 60;
    private static final int AL = 1600;
    private static final int AP = 150;
    private static int hMax = 100;
    private static int hMin = hMax / 10;
    private static final int scale_offset = 5;


    private static Map<String, Object> points = new HashMap();

    @Test
    public void testCreateSvg() {
        init();
        create();
    }

    public void init() {
        // 画布宽高
        initRect();
        // 原点
        initOPoint();
        // X轴起止点
        initAxisXLine();
        // Y轴起止点
        initAxisYLine();
        // X/Y轴箭头
        initAxisX_D();
        initAxisY_D();
        // X/Y轴箭头处单位文字
        initAxisText();
        // X刻度
        initAxisXScale();
        // Y刻度
        initAxisYScale();
    }

    public void create() {
        SVGBuilder.putTagHead("<?xml version=\"1.0\" encoding=\"utf-8\"?>\r\n");
        SVGBuilder.putTagAndAttr("svg", "xmlns=\"http://www.w3.org/2000/svg\" id=\"sw_svg\" stlye=\"position: absolute;\" version=\"1.1\" " +
                "width=\"" + getRect().x + "\" height=\"" + getRect().y + "\" xlink=\"http://www.w3.org/1999/xlink\"");
        SVGBuilder.putTagAndAttr("g", "transform=\"scale(0.6,0.6) translate(0,0)\"");
        SVGBuilder.putTagAndContentClosed("title", "background");
        SVGBuilder.putTagClosed("rect", "fill=\"#fff\" id=\"canvas_background\" height=\"" + getRect().y + "\" width=\"" + getRect().x + "\"");
        // X/Y轴
        SVGBuilder.putTagClosed("path", getLine(getAxisXLine()));
        SVGBuilder.putTagClosed("path", getLine(getAxisYLine()));
        // X/Y轴的刻度
        SVGBuilder.putTagClosed("path", getScale(getAxisXMinScale()));
        SVGBuilder.putTagClosed("path", getScale(getAxisYMinScale()));
        // X/Y轴的刻度文案
        SVGBuilder.putTagScaleText(getAxisXBaseScale(), scale_offset, getOPoint());
        SVGBuilder.putTagScaleText(getAxisYBaseScale(), scale_offset, getOPoint());
        // O刻度文案
        SVGBuilder.putTagOPointText(getOPoint(), scale_offset);
        // X/Y轴方向的箭头
        SVGBuilder.putTagAxisDirection(getAxisX_D());
        SVGBuilder.putTagAxisDirection(getAxisY_D());


        System.out.println(SVGBuilder.create());
    }

    public String getLine(List<Point> points) {
        String result = "";
        for (int i = 0; i < points.size(); i++) {
            if (i == 0) {
                result += "M " + points.get(i).x + " " + +points.get(i).y;
            }
            result += " L " + points.get(i).x + " " + +points.get(i).y;
        }
        return String.format("d=\" %s \" stroke=\"#000\" stroke-width=\"1.2\"", result);
    }

    public String getScale(List<Point> points) {
        String result = "";
        for (int i = 0; i < points.size(); i++) {
            Point point = points.get(i);
            int scaleLength = point.scaleTag == 0 ? scale_offset : scale_offset * 2;
            if (point.y == getOPoint().y) {// X轴
                result += "M " + point.x + " " + +point.y + " L " + point.x + " " + (point.y - scaleLength) + " ";
            } else {
                result += "M " + point.x + " " + +point.y + " L " + (point.x + scaleLength) + " " + point.y + " ";
            }
//            System.out.println(point.toString() + " " + scaleLength);
        }
        return String.format("d=\" %s \" stroke=\"#000\" stroke-width=\"1.2\"", result);
    }

    // Y刻度
    public void initAxisYScale() {
        List<Point> YBaseScale = new ArrayList<>();
        List<Point> YMinScale = new ArrayList<>();
        // Y负轴
        for (int i = getAxisYBottomPoint().y - hMin; i > getAxisYTopPoint().y + hMin && i <= getAxisYBottomPoint().y; i -= hMin) {
            if (i % hMin != 0) {
                i -= i % hMin;
            }
            if (i == getOPoint().y) {
                continue;
            }
            Point point = new Point(getOPoint().x, i);
            int difference = getOPoint().y - i;
            if (difference % hMax == 0) {
                point.scaleTag = difference / hMax;
                YBaseScale.add(point);
//                System.out.println("Y轴：" + getOPoint().y + " - " + i + " = " + difference + " " + (i % hMax == 0) + " " + (difference / hMax));
            }
            YMinScale.add(point);
        }
        points.put("YMinScale", YMinScale);
        points.put("YBaseScale", YBaseScale);
    }

    // 获取x刻度分度值
    public List<Point> getAxisYMinScale() {
        return (List<Point>) points.get("YMinScale");
    }

    // 获取Y轴刻度
    public List<Point> getAxisYBaseScale() {
        return (List<Point>) points.get("YBaseScale");
    }

    // X刻度
    public void initAxisXScale() {
        List<Point> XBaseScale = new ArrayList<>();
        List<Point> XMinScale = new ArrayList<>();
        // X负轴
        for (int i = getAxisXLeftPoint().x + hMin; getAxisXLeftPoint().x <= i && i < getAxisXRightPoint().x - hMin; i += hMin) {
            if (i % hMin != 0) {
                i += i % hMin;
            }
            if (i == getOPoint().x) {
                continue;
            }
            Point point = new Point(i, getOPoint().y);
            int difference = i - getOPoint().x;
            if (difference % hMax == 0) {
                point.scaleTag = difference / hMax;
                XBaseScale.add(point);
//                System.out.println("X轴：" + i + " - " + getOPoint().y + " = " + difference + " " + (i % hMax == 0) + " " + (difference / hMax));
            }
            XMinScale.add(point);
        }
        points.put("XBaseScale", XBaseScale);
        points.put("XMinScale", XMinScale);
    }

    // 获取X刻度分度值
    public List<Point> getAxisXMinScale() {
        return (List<Point>) points.get("XMinScale");
    }

    // 获取X轴刻度
    public List<Point> getAxisXBaseScale() {
        return (List<Point>) points.get("XBaseScale");
    }

    // X/Y轴箭头处单位文字
    public void initAxisText() {
        points.put("axis_x_r_t", new Point(getAxisXRightPoint().x - scale_offset * 2, getAxisXRightPoint().y - scale_offset * 2));
        points.put("axis_y_t_t", new Point(getAxisYTopPoint().x + scale_offset * 2, getAxisYTopPoint().y - scale_offset * 2));
    }

    // 获取X轴箭头处单位文字坐标
    public Point getAxisXTextPoint() {
        return (Point) points.get("axis_x_r_t");
    }

    // 获取Y轴箭头处单位文字坐标
    public Point getAxisYTextPoint() {
        return (Point) points.get("axis_y_t_t");
    }

    // Y轴箭头
    public void initAxisY_D() {
        List<Point> axis_y_d = new ArrayList<>();
        axis_y_d.add(new Point(getAxisYTopPoint().x - scale_offset, getAxisYTopPoint().y + scale_offset * 2));
        axis_y_d.add(getAxisYTopPoint());
        axis_y_d.add(new Point(getAxisYTopPoint().x + scale_offset, getAxisYTopPoint().y + scale_offset * 2));
        points.put("axis_y_d", axis_y_d);
    }

    // 获取Y轴箭头数据
    public List<Point> getAxisY_D() {
        return (List<Point>) points.get("axis_y_d");
    }

    // X轴箭头
    public void initAxisX_D() {
        List<Point> axis_x_d = new ArrayList<>();
        axis_x_d.add(new Point(getAxisXRightPoint().x - scale_offset * 2, getAxisXRightPoint().y - scale_offset));
        axis_x_d.add(getAxisXRightPoint());
        axis_x_d.add(new Point(getAxisXRightPoint().x - scale_offset * 2, getAxisXRightPoint().y + scale_offset));
        points.put("axis_x_d", axis_x_d);
    }

    // 获取X轴箭头数据
    public List<Point> getAxisX_D() {
        return (List<Point>) points.get("axis_x_d");
    }

    // Y轴起止点
    public void initAxisYLine() {
        List<Point> axis_y = new ArrayList<>();
        axis_y.add(new Point(getOPoint().x, getOPoint().y + AP));
        axis_y.add(new Point(getOPoint().x, getOPoint().y - AL));
        points.put("axis_y", axis_y);
    }

    // 获取Y轴起止点
    public List<Point> getAxisYLine() {
        return (List<Point>) points.get("axis_y");
    }

    // 获取Y轴起点
    public Point getAxisYBottomPoint() {
        return (Point) getAxisYLine().get(0);
    }

    // 获取Y轴终点
    public Point getAxisYTopPoint() {
        return (Point) getAxisYLine().get(getAxisYLine().size() - 1);
    }

    // X轴起止点
    public void initAxisXLine() {
        List<Point> axis_x = new ArrayList<>();
        axis_x.add(new Point(getOPoint().x - AP, getOPoint().y));
        axis_x.add(new Point(getOPoint().x + AL, getOPoint().y));
        points.put("axis_x", axis_x);
    }

    // 获取X轴起止点
    public List<Point> getAxisXLine() {
        return (List<Point>) points.get("axis_x");
    }

    // 获取X轴左边的点
    public Point getAxisXLeftPoint() {
        return getAxisXLine().get(0);
    }

    // 获取X轴右边的点
    public Point getAxisXRightPoint() {
        return getAxisXLine().get(getAxisXLine().size() - 1);
    }

    // 设置O点
    public void initOPoint() {
        points.put("OPoint", new Point(AM + AP, AM + AL, 0));
    }

    // 获取O点
    public Point getOPoint() {
        return (Point) points.get("OPoint");
    }

    // 设置画布宽高
    public void initRect() {
        points.put("rect", new Point((AM + AP) * 2 + AL, (AM + AP) * 2 + AL));
    }

    // 获取画布宽高
    public Point getRect() {
        return (Point) points.get("rect");
    }


}
