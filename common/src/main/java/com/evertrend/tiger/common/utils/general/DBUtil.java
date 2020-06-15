package com.evertrend.tiger.common.utils.general;

import com.evertrend.tiger.common.bean.Device;
import com.evertrend.tiger.common.bean.MapPages;
import com.evertrend.tiger.common.bean.RobotSpot;
import com.evertrend.tiger.common.bean.TracePath;
import com.slamtec.slamware.geometry.Line;
import com.slamtec.slamware.geometry.PointF;

import org.litepal.LitePal;

import java.util.ArrayList;
import java.util.List;

public class DBUtil {
    private static final String TAG = "DBUtil";

    public static void saveSpotListToLocal(Device device, MapPages mapPages, TracePath tracePath, List<String> mTraceSpotList) {
        for (int i = 0; i < mTraceSpotList.size(); i++) {
            saveSpotToLocal(device.getId(), mapPages.getId(), tracePath.getId(), 0, mTraceSpotList.get(i));
        }
    }

    public static void saveSpotToLocal(int id, int mapPageId, int tracePathId, int spotFlag, String currentPose) {
        RobotSpot robotSpot = new RobotSpot();
        String[] pose = currentPose.split(",");
        robotSpot.setDevice_id(id);
        robotSpot.setMap_page(mapPageId);
        robotSpot.setTrace_path_id(tracePathId);
        robotSpot.setX(Float.parseFloat(pose[0]));
        robotSpot.setY(Float.parseFloat(pose[1]));
        robotSpot.setZ(Float.parseFloat(pose[2]));
        robotSpot.setYaw(Float.parseFloat(pose[3]));
        robotSpot.setSpot_flag(spotFlag);
        if (!robotSpot.save()) {
            LogUtil.d("TAG", "saveSpotToLocal fail");
        }
    }

    public static List<Line> getTracePathLines(List<RobotSpot> list) {
//        List<RobotSpot> list = getTracePathSpotList(tracePath, mapPages);
        List<Line> lines = new ArrayList<>();
        if (list.size() > 0) {
            if (list.size() == 1) {
                PointF startF = new PointF(list.get(0).getX(), list.get(0).getY());
                PointF endF = new PointF(list.get(0).getX(), list.get(0).getY());
                Line line = new Line(0, startF, endF);
                lines.add(line);
            } else {
                for (int i = 0; i < list.size() - 1; i++) {
                    PointF startF = new PointF(list.get(i).getX(), list.get(i).getY());
                    PointF endF = new PointF(list.get(i + 1).getX(), list.get(i + 1).getY());
                    Line line = new Line(0, startF, endF);
                    lines.add(line);
                }
            }
        }
        return lines;
    }

    public static List<Line> getTracePathLines(TracePath tracePath, MapPages mapPages) {
        List<RobotSpot> list = getTracePathSpotList(tracePath, mapPages);
        List<Line> lines = new ArrayList<>();
        if (list.size() > 0) {
            if (list.size() == 1) {
                PointF startF = new PointF(list.get(0).getX(), list.get(0).getY());
                PointF endF = new PointF(list.get(0).getX(), list.get(0).getY());
                Line line = new Line(0, startF, endF);
                lines.add(line);
            } else {
                for (int i = 0; i < list.size() - 1; i++) {
                    PointF startF = new PointF(list.get(i).getX(), list.get(i).getY());
                    PointF endF = new PointF(list.get(i + 1).getX(), list.get(i + 1).getY());
                    Line line = new Line(0, startF, endF);
                    lines.add(line);
                }
            }
        }
        return lines;
    }

    public static List<RobotSpot> getTracePathSpotList(TracePath tracePath, MapPages mapPages) {
        List<RobotSpot> list = LitePal.where("map_page = ? and trace_path_id = ?", String.valueOf(mapPages.getId())
                , String.valueOf(tracePath.getId())).find(RobotSpot.class);
        LogUtil.d("TAG", "list size : "+list.size());
        return list;
    }

    public static List<Line> rollbackTracePathLines(List<Line> showTracePathLines, MapPages mapPages) {
        List<Line> lines = new ArrayList<>();


        return lines;
    }
}
