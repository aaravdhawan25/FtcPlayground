package Robot;

import com.qualcomm.hardware.lynx.LynxModule;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.util.ElapsedTime;

import java.util.List;

import Utils.MyTelem;

public class MyRobot {

    public List <LynxModule> hubs;

    public ElapsedTime loopTime = new ElapsedTime();

    double lastTime = 0;



    public MyRobot(HardwareMap map){
        hubs = map.getAll(LynxModule.class);
        for(LynxModule hub : hubs){
            hub.setBulkCachingMode(LynxModule.BulkCachingMode.MANUAL);
        }
    }

    public void init(){
        for (LynxModule hub : hubs){
            hub.clearBulkCache();
        }



        resetRuntime();
        startRuntime();

        lastTime = loopTime.milliseconds();
    }

    public void update(){



        double currentTimeMillis = loopTime.milliseconds();
        double loopTimeMs = currentTimeMillis - lastTime;

        lastTime = currentTimeMillis;

        double loopHz = (loopTimeMs > 0 ) ? (1000.0 / loopTimeMs) : 0;

        MyTelem.addFormatedData("Loop Time", "%.2f", loopTimeMs);
        MyTelem.addFormatedData("Frequency", "%.2f", loopHz);

        resetRuntime();

        MyTelem.update();
        for (LynxModule hub : hubs){
            hub.clearBulkCache();
        }
    }

    public void resetRuntime(){
        loopTime.reset();
    }

    public void startRuntime(){
        loopTime.startTime();
    }
}
