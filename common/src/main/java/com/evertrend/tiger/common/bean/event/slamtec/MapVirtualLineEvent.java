package com.evertrend.tiger.common.bean.event.slamtec;

import com.slamtec.slamware.geometry.Line;
import com.slamtec.slamware.robot.ArtifactUsage;

import java.util.List;

public class MapVirtualLineEvent {
    private boolean operationFlag;
    private ArtifactUsage artifactUsage;
    private List<Line> lineList;
    public MapVirtualLineEvent(boolean operationFlag, ArtifactUsage artifactUsage, List<Line> lineList) {
        this.operationFlag = operationFlag;
        this.artifactUsage = artifactUsage;
        this.lineList = lineList;
    }
    public boolean getOperationFlag() {
        return this.operationFlag;
    }

    public ArtifactUsage getArtifactUsage() {
        return artifactUsage;
    }

    public List<Line> getLineList() {
        return lineList;
    }
}
