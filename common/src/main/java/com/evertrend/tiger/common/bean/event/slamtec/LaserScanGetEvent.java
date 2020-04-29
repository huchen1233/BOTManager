package com.evertrend.tiger.common.bean.event.slamtec;

import com.slamtec.slamware.robot.LaserScan;


public class LaserScanGetEvent {
    private LaserScan laserScan;

    public LaserScanGetEvent(LaserScan laserScan) {
        this.laserScan = laserScan;
    }

    public LaserScan getLaserScan() {
        return laserScan;
    }
}
