package org.firstinspires.ftc.teamcode.OpModes.Testers;

import com.acmerobotics.dashboard.config.Config;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.Servo;

@Config
@TeleOp(name = "Servo Tester")
public class ServoTester extends LinearOpMode {
    public static String hmName;
    public static double pos = 0;
    @Override
    public void runOpMode() throws InterruptedException {
        Servo servo = hardwareMap.get(Servo.class, hmName);

        waitForStart();
        while (opModeIsActive()){
            servo.setPosition(pos);
        }

    }
}
