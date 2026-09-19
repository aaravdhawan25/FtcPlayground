package org.firstinspires.ftc.teamcode.Robot.Subsystems;

import static org.firstinspires.ftc.teamcode.Utils.Constants.CameraConstants.cameraYaw;
import static org.firstinspires.ftc.teamcode.Utils.Constants.CameraConstants.isAligning;
import static org.firstinspires.ftc.teamcode.Utils.Constants.CameraConstants.kP;
import static org.firstinspires.ftc.teamcode.Utils.Constants.CameraConstants.rotationalPower;

import com.arcrobotics.ftclib.command.Subsystem;
import com.pedropathing.follower.Follower;
import com.qualcomm.hardware.limelightvision.LLResult;
import com.qualcomm.hardware.limelightvision.Limelight3A;
import com.qualcomm.robotcore.hardware.Gamepad;

import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;
import org.firstinspires.ftc.robotcore.external.navigation.DistanceUnit;
import org.firstinspires.ftc.robotcore.external.navigation.Pose3D;
import org.firstinspires.ftc.robotcore.external.navigation.Position;
import org.firstinspires.ftc.robotcore.external.navigation.YawPitchRollAngles;
import org.firstinspires.ftc.teamcode.Utils.Constants.CameraConstants;
import org.firstinspires.ftc.teamcode.Utils.MyTelem;
import org.opencv.core.Mat;

public class LLCam implements Subsystem {

    public CamState state;

    public boolean isBlue = false;
    Limelight3A limelight3A;

    public static TagTarget target = new TagTarget();

    private Follower follower;

    long time;

    private long lastSeenTimeMs = 0;

    private static final long TARGET_HOLD_MS = 250;

    private double lastTy = 0.0;
    private double lastTx = 0.0;

    public static class TagTarget {
        public boolean hasTarget = false;
        public Pose3D botPose;
        public static int id = -1;
        public double tX = 0.0;
        public double tY = 0.0;
        public double ambiguity;
    }

    public LLCam(Limelight3A limelight3A, String color, Follower follower){
        this.follower = follower;
        this.limelight3A = limelight3A;
        this.isBlue = color.equals("BLUE");
        int pipelineIndex = getPipeline();
        limelight3A.pipelineSwitch(pipelineIndex);
        limelight3A.start();
    }

    public void setState(CamState state){
        this.state = state;
        switch (state){
            case ALIGN:
                TagTarget tag = target;
                if (tag == null || !tag.hasTarget) {
                }else {
                    CameraConstants.distanceToGoalLL = updateDistanceToGoalLL(target);
                    pointToGoalCamera(target);
                }
                break;
            case STOP:
                CameraConstants.isAligning = false;
                CameraConstants.isAligned = false;
                break;

        }

    }

    public static double updateDistanceToGoalLL(TagTarget target){
        double totalPitchDeg = target.tY + cameraYaw;
        if (totalPitchDeg == 90 || totalPitchDeg == 270) {
            return 0;
        }
        double tan = Math.tan(Math.toRadians(totalPitchDeg));
        if (tan == 0) {
            return 0;
        }

        CameraConstants.distanceToGoalLL = (CameraConstants.goalDY / tan);

        return CameraConstants.distanceToGoalLL;

    }

    public void pointToGoalCamera(TagTarget tag) {
        if (tag == null || !tag.hasTarget) return;

        double tX = tag.tX;
        double tY = tag.tY;
        double realAngle = Math.abs(tX);
        boolean isLeft = tX < 0;
//        double rotationPower = kP * tX;

        CameraConstants.isAligning = true;
//        rotationPower = Math.max(-CameraConstants.maxPower, Math.min(CameraConstants.maxPower, rotationPower));
//        CameraConstants.rotationalPower = rotationPower;

        CameraConstants.isAligned = (Math.abs(tX) < CameraConstants.angleTolerance) ? true : false;

        MyTelem.addData("raw x", tX);
        MyTelem.addData("real x", realAngle);
        MyTelem.addData("y",tY);
        MyTelem.addData("Is Left:", isLeft);
        MyTelem.addData("Aligned Properly", CameraConstants.isAligned && CameraConstants.isAligning);
        MyTelem.addData("Math Camera", true);

//        follower.setTeleOpMovementVectors(0,0,rotationPower, true);
        follower.turnDegrees(realAngle, isLeft);

        if (Math.abs(tX) < 0.5){
            CameraConstants.isAligning = false;
            return;
        }
    }

    public TagTarget getTargetTag() {return target;}


    private void updateTarget() {
        LLResult result = limelight3A.getLatestResult();
        long now = getTime();
        if (result != null && result.isValid()) {
            target.hasTarget = true;
            target.tX = result.getTx();
            target.tY = result.getTy();
            lastTx = target.tX;
            lastTy = target.tY;
            CameraConstants.distanceToGoalLL = updateDistanceToGoalLL(target);
            return;
        }

        if (now - lastSeenTimeMs <= TARGET_HOLD_MS) {
            target.hasTarget = true;
            target.tX = lastTx;
            target.tY = lastTy;
            return;
        }
        target.hasTarget = false;
        target.id = getPipeline();
        MyTelem.addData("ts works", true);
        CameraConstants.distanceToGoalLL = updateDistanceToGoalLL(target);
    }

    private void publishTelem() {
        MyTelem.addData("LL Has Target", target.hasTarget);
        if (!target.hasTarget) return;

        MyTelem.addData("LL Tag ID", target.id);
        MyTelem.addData("LL Target Distance", CameraConstants.distanceToGoalLL);
    }

    public long getTime(){
        return time;
    }
    public void updateTime(long time){
        this.time = time;

    }

    public int getPipeline(){
        return isBlue ? 0 : 1;
    }

    @Override
    public void periodic(){
        updateTarget();
        publishTelem();
        updateDistanceToGoalLL(target);
        setState(state);
    }

    public enum CamState{
        ALIGN,
        STOP
    }

}

