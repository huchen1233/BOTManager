package com.evertrend.tiger.common.bean;

import org.litepal.crud.LitePalSupport;

import java.io.Serializable;

public class Device extends LitePalSupport implements Serializable {
    private int id;
    private String device_type;
    private String device_id;
    private int status;
    private String description;
    private long create_time;
    private String device_ip;
    private int owner_user_id;
    private String battery_level;
    private int is_recharge;
    private int is_adding_water;//加水状态，0：未在加水，1：正在手动加水，2：正在自动加水
    private int is_emptying_trash;//倾倒垃圾状态,0:未在倾倒，1：正在倾倒
    private int is_running;//运行状态
    private int main_sweep_status;//主扫状态
    private int side_sweep_status;//边扫状态
    private int sprinkling_water_status;//洒水状态
    private int alarm_light_status;//警告灯状态
    private int front_light_status;//前照灯状态
    private int tail_light_status;//尾灯状态
    private int left_tail_light_status;//左尾灯状态
    private int right_tail_light_status;//右尾灯状态
    private int horn_status;//喇叭状态
    private int suck_fan_status;//风机状态
    private int vibrating_dust_status;//振尘状态
    private int motor_release_status;//电机状态（释放、刹车）
    private int garbage_valve_status;//垃圾槽阀门状态
    private int emergency_stop_status;//急停状态（释放、使能）
    private String water_level;
    private double latitude;//纬度
    private double longitude;//经度
    private String local_ins_ip;//工控机内网IP
    private String local_chassis_ip;//底盘内网IP
    private String local_camera_ip;//摄像机内网IP
    private String work_mode;//手动模式还是智能模式
    private String clean_mode;//清洁模式
    private int current_trace_path_id;//当前所选择的循迹路径
    private String delete_trace_path;//要删除的路径，为路径ID数组，经过串化
    private int is_created_map;//是否建图标志
    private int save_map;//当前地图区域已经被保存或者要求保存
    private int current_map_page;//当前地图区域
    private int battery_low_limit;//低电量限位值,百分比值
    private int water_low_limit;//低水位限位值,百分比值
    private int empty_trash_interval;//倒垃圾时间间隔,分钟
    private int is_timing_path;//是否定时路径打扫, 0: 未定时，1：一个时段，2：二个时段，3：三个时段
    private int timing_start_1;
    private int timing_end_1;
    private int timing_start_2;
    private int timing_end_2;
    private int timing_start_3;
    private int timing_end_3;
    private int set_current_task;//当前执行的任务
    private int current_virtaul_track_group;//当前虚拟轨道组
    private int enable_auto_recharge;
    private int enable_auto_add_water;
    private int enable_auto_empty_trash;
    private String alarm_info;//报警信息
    private int grant_flag;//是否为授权设备，1是
    private int auto_record_trace_path;//
    private int enable_gps_fence;//
    private int log_gps_map_slam;//
    private int delete_gps_map_slam;//

    public int getAuto_record_trace_path() {
        return auto_record_trace_path;
    }

    public void setAuto_record_trace_path(int auto_record_trace_path) {
        this.auto_record_trace_path = auto_record_trace_path;
    }

    public int getEnable_gps_fence() {
        return enable_gps_fence;
    }

    public void setEnable_gps_fence(int enable_gps_fence) {
        this.enable_gps_fence = enable_gps_fence;
    }

    public int getLog_gps_map_slam() {
        return log_gps_map_slam;
    }

    public void setLog_gps_map_slam(int log_gps_map_slam) {
        this.log_gps_map_slam = log_gps_map_slam;
    }

    public int getDelete_gps_map_slam() {
        return delete_gps_map_slam;
    }

    public void setDelete_gps_map_slam(int delete_gps_map_slam) {
        this.delete_gps_map_slam = delete_gps_map_slam;
    }

    public int getGrant_flag() {
        return grant_flag;
    }

    public void setGrant_flag(int grant_flag) {
        this.grant_flag = grant_flag;
    }

    public String getAlarm_info() {
        return alarm_info;
    }

    public void setAlarm_info(String alarm_info) {
        this.alarm_info = alarm_info;
    }

    public int getEnable_auto_recharge() {
        return enable_auto_recharge;
    }

    public void setEnable_auto_recharge(int enable_auto_recharge) {
        this.enable_auto_recharge = enable_auto_recharge;
    }

    public int getEnable_auto_add_water() {
        return enable_auto_add_water;
    }

    public void setEnable_auto_add_water(int enable_auto_add_water) {
        this.enable_auto_add_water = enable_auto_add_water;
    }

    public int getEnable_auto_empty_trash() {
        return enable_auto_empty_trash;
    }

    public void setEnable_auto_empty_trash(int enable_auto_empty_trash) {
        this.enable_auto_empty_trash = enable_auto_empty_trash;
    }

    public int getCurrent_virtaul_track_group() {
        return current_virtaul_track_group;
    }

    public void setCurrent_virtaul_track_group(int current_virtaul_track_group) {
        this.current_virtaul_track_group = current_virtaul_track_group;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getHorn_status() {
        return horn_status;
    }

    public void setHorn_status(int horn_status) {
        this.horn_status = horn_status;
    }

    public int getSuck_fan_status() {
        return suck_fan_status;
    }

    public void setSuck_fan_status(int suck_fan_status) {
        this.suck_fan_status = suck_fan_status;
    }

    public int getVibrating_dust_status() {
        return vibrating_dust_status;
    }

    public void setVibrating_dust_status(int vibrating_dust_status) {
        this.vibrating_dust_status = vibrating_dust_status;
    }

    public int getMotor_release_status() {
        return motor_release_status;
    }

    public void setMotor_release_status(int motor_release_status) {
        this.motor_release_status = motor_release_status;
    }

    public int getGarbage_valve_status() {
        return garbage_valve_status;
    }

    public void setGarbage_valve_status(int garbage_valve_status) {
        this.garbage_valve_status = garbage_valve_status;
    }

    public int getEmergency_stop_status() {
        return emergency_stop_status;
    }

    public void setEmergency_stop_status(int emergency_stop_status) {
        this.emergency_stop_status = emergency_stop_status;
    }

    public int getLeft_tail_light_status() {
        return left_tail_light_status;
    }

    public void setLeft_tail_light_status(int left_tail_light_status) {
        this.left_tail_light_status = left_tail_light_status;
    }

    public int getRight_tail_light_status() {
        return right_tail_light_status;
    }

    public void setRight_tail_light_status(int right_tail_light_status) {
        this.right_tail_light_status = right_tail_light_status;
    }

    public int getCurrent_map_page() {
        return current_map_page;
    }

    public void setCurrent_map_page(int current_map_page) {
        this.current_map_page = current_map_page;
    }

    public int getTiming_start(int i) {
        switch (i) {
            case 1:
                return timing_start_1;
            case 2:
                return timing_start_2;
            case 3:
                return timing_start_3;
            default:
                return 0;
        }
    }

    public int getTiming_end(int i) {
        switch (i) {
            case 1:
                return timing_end_1;
            case 2:
                return timing_end_2;
            case 3:
                return timing_end_3;
            default:
                return 0;
        }
    }

    public int getIs_timing_path() {
        return is_timing_path;
    }

    public void setIs_timing_path(int is_timing_path) {
        this.is_timing_path = is_timing_path;
    }

    public int getTiming_start_1() {
        return timing_start_1;
    }

    public void setTiming_start_1(int timing_start_1) {
        this.timing_start_1 = timing_start_1;
    }

    public int getTiming_end_1() {
        return timing_end_1;
    }

    public void setTiming_end_1(int timing_end_1) {
        this.timing_end_1 = timing_end_1;
    }

    public int getTiming_start_2() {
        return timing_start_2;
    }

    public void setTiming_start_2(int timing_start_2) {
        this.timing_start_2 = timing_start_2;
    }

    public int getTiming_end_2() {
        return timing_end_2;
    }

    public void setTiming_end_2(int timing_end_2) {
        this.timing_end_2 = timing_end_2;
    }

    public int getTiming_start_3() {
        return timing_start_3;
    }

    public void setTiming_start_3(int timing_start_3) {
        this.timing_start_3 = timing_start_3;
    }

    public int getTiming_end_3() {
        return timing_end_3;
    }

    public void setTiming_end_3(int timing_end_3) {
        this.timing_end_3 = timing_end_3;
    }

    public int getBattery_low_limit() {
        return battery_low_limit;
    }

    public void setBattery_low_limit(int battery_low_limit) {
        this.battery_low_limit = battery_low_limit;
    }

    public int getWater_low_limit() {
        return water_low_limit;
    }

    public void setWater_low_limit(int water_low_limit) {
        this.water_low_limit = water_low_limit;
    }

    public int getEmpty_trash_interval() {
        return empty_trash_interval;
    }

    public void setEmpty_trash_interval(int empty_trash_interval) {
        this.empty_trash_interval = empty_trash_interval;
    }

    public int getSave_map() {
        return save_map;
    }

    public void setSave_map(int save_map) {
        this.save_map = save_map;
    }

    public int getIs_adding_water() {
        return is_adding_water;
    }

    public void setIs_adding_water(int is_adding_water) {
        this.is_adding_water = is_adding_water;
    }

    public int getIs_emptying_trash() {
        return is_emptying_trash;
    }

    public void setIs_emptying_trash(int is_emptying_trash) {
        this.is_emptying_trash = is_emptying_trash;
    }

    public int getIs_running() {
        return is_running;
    }

    public void setIs_running(int is_running) {
        this.is_running = is_running;
    }

    public int getMain_sweep_status() {
        return main_sweep_status;
    }

    public void setMain_sweep_status(int main_sweep_status) {
        this.main_sweep_status = main_sweep_status;
    }

    public int getSide_sweep_status() {
        return side_sweep_status;
    }

    public void setSide_sweep_status(int side_sweep_status) {
        this.side_sweep_status = side_sweep_status;
    }

    public int getSprinkling_water_status() {
        return sprinkling_water_status;
    }

    public void setSprinkling_water_status(int sprinkling_water_status) {
        this.sprinkling_water_status = sprinkling_water_status;
    }

    public int getAlarm_light_status() {
        return alarm_light_status;
    }

    public void setAlarm_light_status(int alarm_light_status) {
        this.alarm_light_status = alarm_light_status;
    }

    public int getFront_light_status() {
        return front_light_status;
    }

    public void setFront_light_status(int front_light_status) {
        this.front_light_status = front_light_status;
    }

    public int getTail_light_status() {
        return tail_light_status;
    }

    public void setTail_light_status(int tail_light_status) {
        this.tail_light_status = tail_light_status;
    }

    public int getIs_created_map() {
        return is_created_map;
    }

    public void setIs_created_map(int is_created_map) {
        this.is_created_map = is_created_map;
    }

    public String getLocal_ins_ip() {
        return local_ins_ip;
    }

    public void setLocal_ins_ip(String local_ins_ip) {
        this.local_ins_ip = local_ins_ip;
    }

    public String getLocal_chassis_ip() {
        return local_chassis_ip;
    }

    public void setLocal_chassis_ip(String local_chassis_ip) {
        this.local_chassis_ip = local_chassis_ip;
    }

    public String getLocal_camera_ip() {
        return local_camera_ip;
    }

    public void setLocal_camera_ip(String local_camera_ip) {
        this.local_camera_ip = local_camera_ip;
    }

    public String getWork_mode() {
        return work_mode;
    }

    public void setWork_mode(String work_mode) {
        this.work_mode = work_mode;
    }

    public String getClean_mode() {
        return clean_mode;
    }

    public void setClean_mode(String clean_mode) {
        this.clean_mode = clean_mode;
    }

    public int getCurrent_trace_path_id() {
        return current_trace_path_id;
    }

    public void setCurrent_trace_path_id(int current_trace_path_id) {
        this.current_trace_path_id = current_trace_path_id;
    }

    public String getDelete_trace_path() {
        return delete_trace_path;
    }

    public void setDelete_trace_path(String delete_trace_path) {
        this.delete_trace_path = delete_trace_path;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public String getBattery_level() {
        return battery_level;
    }

    public void setBattery_level(String battery_level) {
        this.battery_level = battery_level;
    }

    public int getIs_recharge() {
        return is_recharge;
    }

    public void setIs_recharge(int is_recharge) {
        this.is_recharge = is_recharge;
    }

    public String getWater_level() {
        return water_level;
    }

    public void setWater_level(String water_level) {
        this.water_level = water_level;
    }

    public Device() {
    }

    public String getDevice_type() {
        return device_type;
    }

    public void setDevice_type(String device_type) {
        this.device_type = device_type;
    }

    public String getDevice_id() {
        return device_id;
    }

    public void setDevice_id(String device_id) {
        this.device_id = device_id;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public long getCreate_time() {
        return create_time;
    }

    public void setCreate_time(long create_time) {
        this.create_time = create_time;
    }

    public String getDevice_ip() {
        return device_ip;
    }

    public void setDevice_ip(String device_ip) {
        this.device_ip = device_ip;
    }

    public int getOwner_user_id() {
        return owner_user_id;
    }

    public void setOwner_user_id(int owner_user_id) {
        this.owner_user_id = owner_user_id;
    }

    public int getSet_current_task() {
        return set_current_task;
    }

    public void setSet_current_task(int set_current_task) {
        this.set_current_task = set_current_task;
    }

    @Override
    public String toString() {
        return "Device{" +
                "id=" + id +
                ", device_type='" + device_type + '\'' +
                ", device_id='" + device_id + '\'' +
                ", status=" + status +
                ", description='" + description + '\'' +
                ", create_time=" + create_time +
                ", device_ip='" + device_ip + '\'' +
                ", owner_user_id=" + owner_user_id +
                ", battery_level='" + battery_level + '\'' +
                ", is_recharge=" + is_recharge +
                ", is_adding_water=" + is_adding_water +
                ", is_emptying_trash=" + is_emptying_trash +
                ", is_running=" + is_running +
                ", main_sweep_status=" + main_sweep_status +
                ", side_sweep_status=" + side_sweep_status +
                ", sprinkling_water_status=" + sprinkling_water_status +
                ", alarm_light_status=" + alarm_light_status +
                ", front_light_status=" + front_light_status +
                ", tail_light_status=" + tail_light_status +
                ", left_tail_light_status=" + left_tail_light_status +
                ", right_tail_light_status=" + right_tail_light_status +
                ", horn_status=" + horn_status +
                ", suck_fan_status=" + suck_fan_status +
                ", vibrating_dust_status=" + vibrating_dust_status +
                ", motor_release_status=" + motor_release_status +
                ", garbage_valve_status=" + garbage_valve_status +
                ", emergency_stop_status=" + emergency_stop_status +
                ", water_level='" + water_level + '\'' +
                ", latitude=" + latitude +
                ", longitude=" + longitude +
                ", local_ins_ip='" + local_ins_ip + '\'' +
                ", local_chassis_ip='" + local_chassis_ip + '\'' +
                ", local_camera_ip='" + local_camera_ip + '\'' +
                ", work_mode='" + work_mode + '\'' +
                ", clean_mode='" + clean_mode + '\'' +
                ", current_trace_path_id=" + current_trace_path_id +
                ", delete_trace_path='" + delete_trace_path + '\'' +
                ", is_created_map=" + is_created_map +
                ", save_map=" + save_map +
                ", current_map_page=" + current_map_page +
                ", battery_low_limit=" + battery_low_limit +
                ", water_low_limit=" + water_low_limit +
                ", empty_trash_interval=" + empty_trash_interval +
                ", is_timing_path=" + is_timing_path +
                ", timing_start_1=" + timing_start_1 +
                ", timing_end_1=" + timing_end_1 +
                ", timing_start_2=" + timing_start_2 +
                ", timing_end_2=" + timing_end_2 +
                ", timing_start_3=" + timing_start_3 +
                ", timing_end_3=" + timing_end_3 +
                ", set_current_task=" + set_current_task +
                ", current_virtaul_track_group=" + current_virtaul_track_group +
                ", enable_auto_recharge=" + enable_auto_recharge +
                ", enable_auto_add_water=" + enable_auto_add_water +
                ", enable_auto_empty_trash=" + enable_auto_empty_trash +
                ", alarm_info=" + alarm_info +
                ", grant_flag=" + grant_flag +
                ", auto_record_trace_path=" + auto_record_trace_path +
                ", enable_gps_fence=" + enable_gps_fence +
                ", log_gps_map_slam=" + log_gps_map_slam +
                ", delete_trace_path=" + delete_trace_path +
                '}';
    }
}
