package com.simis.batch;

import com.simis.util.DateTimeUtil;
import com.simis.util.DeleteFileUtil;
import org.springframework.stereotype.Component;

import java.util.Calendar;
import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;

/**
 * 定期清除qcode生成的文件
 * Created by ArnoldLee on 17/5/1.
 */
@Component
public class ClearQcodeFilesJob extends TimerTask {

    protected static org.slf4j.Logger LOGGER = org.slf4j.LoggerFactory.getLogger(ClearQcodeFilesJob.class);

    public static void main(String[] args) {
        String path = "/Users/ArnoldLee/Downloads/intellj-workspace/simis/target/simis-1.0-SNAPSHOT/WEB-INF/resource/qcode";
        DeleteFileUtil.deleteDirectory(path);
    }

    public void clearQcodeFiles(){
        LOGGER.info("#######定时清除文件,开始###########");
        String path = "Users/ArnoldLee/Downloads/intellj-workspace/simis/target/simis-1.0-snapshot/WEB-INF/resource/qcode";
        DeleteFileUtil.deleteDirectory(path);
        LOGGER.info("#######定时清除文件,结束###########");
    }

    @Override
    public void run() {
        Calendar calendar = Calendar.getInstance();

        int hour = calendar.get(Calendar.HOUR_OF_DAY);
        LOGGER.info("检查当前时间为{}",hour);
        if(22 == hour){
            clearQcodeFiles();
        }

    }
}
