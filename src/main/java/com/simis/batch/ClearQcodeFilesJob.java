package com.simis.batch;

import com.simis.util.DeleteFileUtil;
import org.springframework.stereotype.Component;

/**
 * 定期清除qcode生成的文件
 *
 * @author ArnoldLee
 * @date 17/5/1
 */
@Component
public class ClearQcodeFilesJob{

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


}
