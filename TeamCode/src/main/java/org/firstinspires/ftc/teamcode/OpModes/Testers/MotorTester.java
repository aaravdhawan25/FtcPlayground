package org.firstinspires.ftc.teamcode.OpModes.Testers;

import com.acmerobotics.dashboard.config.Config;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.Servo;

@Config
@TeleOp(name = "Motor Tester")
public class MotorTester extends LinearOpMode {
    public static String hmName;
    public static double power = 0;
    @Override
    public void runOpMode() throws InterruptedException {
        DcMotorEx motor = hardwareMap.get(DcMotorEx.class, hmName);

        waitForStart();
        while (opModeIsActive()){
            motor.setPower(power);
        }

    }
}
