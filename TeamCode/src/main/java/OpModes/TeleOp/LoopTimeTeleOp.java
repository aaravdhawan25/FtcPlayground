package opMode.TeleOp;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.HardwareMap;

import Robot.MyRobot;
import Utils.MyTelem;

@TeleOp(name = "Loop Time Tele", group = " ")

public class LoopTimeTeleOp extends LinearOpMode {



    @Override
    public void runOpMode() throws InterruptedException {
        MyTelem.init(telemetry);
        MyRobot robot = new MyRobot(hardwareMap);

        waitForStart();

        while (opModeInInit()){
            robot.init();

        }

        while (opModeIsActive()){
            robot.update();
            MyTelem.update();

        }
    }
}
