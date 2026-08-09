package org.firstinspires.ftc.teamcode.Robot;

import com.arcrobotics.ftclib.command.CommandScheduler;
import com.pedropathing.follower.Follower;
import com.qualcomm.hardware.limelightvision.Limelight3A;
import com.qualcomm.hardware.lynx.LynxModule;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.util.ElapsedTime;

import java.util.List;

import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.firstinspires.ftc.teamcode.Robot.Subsystems.Blocker;
import org.firstinspires.ftc.teamcode.Robot.Subsystems.Intake;
import org.firstinspires.ftc.teamcode.Robot.Subsystems.LLCam;
import org.firstinspires.ftc.teamcode.Robot.Subsystems.Shooter;
import org.firstinspires.ftc.teamcode.Utils.Constants.CameraConstants;
import org.firstinspires.ftc.teamcode.Utils.MyTelem;
import org.firstinspires.ftc.teamcode.pedroPathing.constants.FConstants;
import org.firstinspires.ftc.teamcode.pedroPathing.constants.LConstants;

public class MyRobot {

    public List <LynxModule> hubs;

    public ElapsedTime loopTime = new ElapsedTime();

    double lastTime = 0;

    public LLCam cam;

    String color;
    public DcMotorEx shooterOne, shooterTwo, intakeMotor, transferMotor;

    public Limelight3A limelight3A;

    public Servo hoodServo, blockerServo;

    public Shooter shooter;

    public Intake intake;

    public Blocker blocker;

    public Follower follower;



    public MyRobot(HardwareMap map, Telemetry tel, String color){
        this.color = color;
        hubs = map.getAll(LynxModule.class);
        for(LynxModule hub : hubs){
            hub.setBulkCachingMode(LynxModule.BulkCachingMode.MANUAL);
        }
        limelight3A = map.get(Limelight3A.class, "limelight");
        blockerServo = map.get(Servo.class, "blocker");
        shooterOne =  map.get(DcMotorEx.class, "shooter");
        shooterTwo =  map.get(DcMotorEx.class, "shooter2");
        intakeMotor = map.get(DcMotorEx.class, "intake");
        transferMotor = map.get(DcMotorEx.class, "transfer");
        shooterOne.setMode(DcMotorEx.RunMode.RUN_WITHOUT_ENCODER);
        shooterTwo.setMode(DcMotorEx.RunMode.RUN_WITHOUT_ENCODER);
        shooterOne.setDirection(DcMotorSimple.Direction.REVERSE);
        transferMotor.setDirection(DcMotorSimple.Direction.REVERSE);
        follower = new Follower(map, FConstants.class, LConstants.class);
        intake = new Intake(intakeMotor, transferMotor);
        shooter = new Shooter(shooterOne, shooterTwo, hoodServo);
        blocker = new Blocker(blockerServo);
        cam = new LLCam(limelight3A, color, follower);
        MyTelem.init(tel);
        CommandScheduler.getInstance().reset();
        CommandScheduler.getInstance().registerSubsystem(shooter, intake, blocker, cam);

    }

    public void setTeleOpMovementVectors(double forward, double strafe, double turn, boolean robotCentric){
        if (!CameraConstants.isAligning){
            follower.setTeleOpMovementVectors(forward, strafe, turn, robotCentric);
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

        CommandScheduler.getInstance().run();

        double currentTimeMillis = loopTime.milliseconds();
        double loopTimeMs = currentTimeMillis - lastTime;

        lastTime = currentTimeMillis;

        double loopHz = (loopTimeMs > 0 ) ? (1000.0 / loopTimeMs) : 0;

        MyTelem.addData("Loop Time", "%.2f", loopTimeMs);
        MyTelem.addData("Frequency", "%.2f", loopHz);

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
