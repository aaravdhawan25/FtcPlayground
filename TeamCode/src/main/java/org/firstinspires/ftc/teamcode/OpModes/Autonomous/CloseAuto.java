package org.firstinspires.ftc.teamcode.OpModes.Autonomous;

import com.arcrobotics.ftclib.command.CommandScheduler;
import com.arcrobotics.ftclib.command.ConditionalCommand;
import com.arcrobotics.ftclib.command.ParallelCommandGroup;
import com.arcrobotics.ftclib.command.SequentialCommandGroup;
import com.arcrobotics.ftclib.command.WaitCommand;
import com.arcrobotics.ftclib.command.WaitUntilCommand;
import com.pedropathing.follower.Follower;
import com.pedropathing.localization.Pose;
import com.pedropathing.pathgen.BezierCurve;
import com.pedropathing.pathgen.BezierLine;
import com.pedropathing.pathgen.PathChain;
import com.pedropathing.util.DashboardPoseTracker;
import com.pedropathing.util.Drawing;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;

import org.firstinspires.ftc.teamcode.Robot.Commands.BlockerCommand;
import org.firstinspires.ftc.teamcode.Robot.Commands.FollowPathCommand;
import org.firstinspires.ftc.teamcode.Robot.Commands.IntakeCommand;
import org.firstinspires.ftc.teamcode.Robot.Commands.ShooterCommand;
import org.firstinspires.ftc.teamcode.Robot.Commands.TransferCancelCommand;
import org.firstinspires.ftc.teamcode.Robot.Commands.TransferCommand;
import org.firstinspires.ftc.teamcode.Robot.MyRobot;
import org.firstinspires.ftc.teamcode.Robot.Subsystems.Blocker;
import org.firstinspires.ftc.teamcode.Robot.Subsystems.Intake;
import org.firstinspires.ftc.teamcode.Robot.Subsystems.Shooter;
import org.firstinspires.ftc.teamcode.Utils.CloseAutoPoseData;
import org.firstinspires.ftc.teamcode.Utils.Constants.AutoConstants;

public class CloseAuto extends OpMode {

    MyRobot robot;
    String color;
    DashboardPoseTracker dashboardPoseTracker;

    SequentialCommandGroup auton;

    private CloseAutoPaths paths;

    public CloseAuto(String color){
        this.color = color;
    }


    @Override
    public void init() {
        dashboardPoseTracker = new DashboardPoseTracker(robot.follower.poseUpdater);
        robot = new MyRobot(hardwareMap, telemetry, color);
        paths = new CloseAutoPaths(robot.follower, color);
        CommandScheduler.getInstance().schedule(
                new BlockerCommand(robot, Blocker.BlockerState.CLOSED)

        );
        auton = new SequentialCommandGroup(
                new FollowPathCommand(robot.follower, paths.ReverseOut),
                shoot(),
                intake(paths.IntakeSpike2),
                new FollowPathCommand(robot.follower, paths.ReturnSpike2),
                shoot(),
                intake(paths.IntakeSpike1),
                new FollowPathCommand(robot.follower, paths.ReturnSpike1),
                shoot(),
                new FollowPathCommand(robot.follower, paths.Gate),
                intake(paths.GateIntake),
                new FollowPathCommand(robot.follower, paths.ReturnGate),
                shoot(),
                intake(paths.IntakeSpike3),
                new FollowPathCommand(robot.follower, paths.ReturnSpike3),
                shoot()

        );
        Drawing.drawRobot(robot.follower.poseUpdater.getPose(), "#4CAF50");
        Drawing.sendPacket();


    }

    @Override
    public void loop() {
        robot.update();
        dashboardPoseTracker.update();
        Drawing.drawPoseHistory(dashboardPoseTracker, "#4CAF50");
        Drawing.drawRobot(robot.follower.poseUpdater.getPose(), "#4CAF50");
        Drawing.sendPacket();

    }

    @Override
    public void start(){
        CommandScheduler.getInstance().schedule(
                auton
        );
    }

    public SequentialCommandGroup shoot(){
        return new SequentialCommandGroup(
                new ShooterCommand(robot, Shooter.ShooterState.CLOSE),
                new TransferCommand(robot),
                new WaitCommand(500),
                new TransferCancelCommand(robot)
        );
    }

    public ParallelCommandGroup intake(PathChain path){
        return new ParallelCommandGroup(
                new FollowPathCommand(robot.follower, path),
                new SequentialCommandGroup(
                        new WaitCommand(500),
                        new IntakeCommand(robot, Intake.IntakeState.ON),
                        new WaitUntilCommand(() -> !robot.follower.isBusy()),
                        new WaitCommand(100),
                        new IntakeCommand(robot, Intake.IntakeState.OFF)
                )
        );
    }

    public static class CloseAutoPaths {
        public PathChain ReverseOut, IntakeSpike2, ReturnSpike2, Gate, GateIntake;
        public PathChain ReturnGate, IntakeSpike1, ReturnSpike1, IntakeSpike3, ReturnSpike3;

        public CloseAutoPaths(Follower follower, String color) {

            Pose startPose = CloseAutoPoseData.mirror(CloseAutoPoseData.START_POSE, color);
            Pose shootingPose = CloseAutoPoseData.mirror(CloseAutoPoseData.SHOOTING_POSE, color);
            Pose finalShootingPose = CloseAutoPoseData.mirror(CloseAutoPoseData.FINAL_SHOOT, color);
            Pose firstIntake = CloseAutoPoseData.mirror(CloseAutoPoseData.INTAKE1, color);
            Pose mid2Curve = CloseAutoPoseData.mirror(CloseAutoPoseData.MID2_CURVE, color);
            Pose secondIntake = CloseAutoPoseData.mirror(CloseAutoPoseData.SECOND_INTAKE, color);
            Pose leverRetCont = CloseAutoPoseData.mirror(CloseAutoPoseData.LEVER_RETURN_CONTROL, color);
            Pose finalIntake = CloseAutoPoseData.mirror(CloseAutoPoseData.FINAL_INTAKE, color);
            Pose leverPose = CloseAutoPoseData.mirror(CloseAutoPoseData.LEVER, color);
            Pose intake3Cont = CloseAutoPoseData.mirror(CloseAutoPoseData.INTAKE3Cont, color);
            Pose finalShootCont = CloseAutoPoseData.mirror(CloseAutoPoseData.finalShootCont, color);
            Pose leverIntakePose = CloseAutoPoseData.mirror(CloseAutoPoseData.LEVER_INTAKE, color);
            Pose leverCont = CloseAutoPoseData.mirror(CloseAutoPoseData.LEVER_CONTROL, color);

            double heading180 = CloseAutoPoseData.mirrorHeading(AutoConstants.Heading180, color);
            double leverHitHeading = CloseAutoPoseData.mirrorHeading(AutoConstants.leverHitHeading, color);
            double leverHeading = CloseAutoPoseData.mirrorHeading(AutoConstants.leverHeading, color);
            double finalShootHeading = CloseAutoPoseData.mirrorHeading(AutoConstants.finalShootHeading, color);
            double whileMovingStartHeading = CloseAutoPoseData.mirrorHeading(AutoConstants.startShootMoveHeading, color);
            double shootHeading = CloseAutoPoseData.mirrorHeading(AutoConstants.endShootMoveHeading, color);

            follower.setStartingPose(startPose);

            ReverseOut = follower.pathBuilder()
                    .addPath(new BezierLine(startPose, shootingPose))
                    .setLinearHeadingInterpolation(Math.toRadians(whileMovingStartHeading), Math.toRadians(shootHeading))
                    .setZeroPowerAccelerationMultiplier(AutoConstants.decelMultiplier)
                    .build();

            IntakeSpike2 = follower.pathBuilder()
                    .addPath(new BezierCurve(shootingPose, mid2Curve, secondIntake))
                    .setTangentHeadingInterpolation()
                    .setZeroPowerAccelerationMultiplier(AutoConstants.decelMultiplier)
                    .build();

            ReturnSpike2 = follower.pathBuilder()
                    .addPath(new BezierLine(secondIntake, shootingPose))
                    .setLinearHeadingInterpolation(Math.toRadians(heading180), Math.toRadians(shootHeading))
                    .setZeroPowerAccelerationMultiplier(AutoConstants.decelMultiplier)
                    .build();

            Gate = follower.pathBuilder()
                    .addPath(new BezierLine(shootingPose, leverPose))
                    .setLinearHeadingInterpolation(Math.toRadians(shootHeading), Math.toRadians(leverHitHeading))
                    .setZeroPowerAccelerationMultiplier(AutoConstants.decelMultiplier)
                    .build();

            GateIntake = follower.pathBuilder()
                    .addPath(new BezierCurve(leverPose, leverCont, leverIntakePose))
                    .setLinearHeadingInterpolation(Math.toRadians(leverHitHeading), Math.toRadians(leverHeading))
                    .setZeroPowerAccelerationMultiplier(AutoConstants.decelMultiplier)

                    .build();

            ReturnGate = follower.pathBuilder()
                    .addPath(new BezierCurve(leverIntakePose, leverRetCont, shootingPose))
                    .setLinearHeadingInterpolation(Math.toRadians(leverHeading), Math.toRadians(shootHeading))
                    .setZeroPowerAccelerationMultiplier(AutoConstants.decelMultiplierGate)
                    .build();

            IntakeSpike1 = follower.pathBuilder()
                    .addPath(new BezierLine(shootingPose, firstIntake))
                    .setTangentHeadingInterpolation()
                    .setZeroPowerAccelerationMultiplier(AutoConstants.decelMultiplier)
                    .build();

            ReturnSpike1 = follower.pathBuilder()
                    .addPath(new BezierLine(firstIntake, shootingPose))
                    .setLinearHeadingInterpolation(Math.toRadians(heading180), Math.toRadians(shootHeading))
                    .setZeroPowerAccelerationMultiplier(AutoConstants.decelMultiplier)
                    .build();

            IntakeSpike3 = follower.pathBuilder()
                    .addPath(new BezierCurve(shootingPose, intake3Cont, finalIntake))
                    .setTangentHeadingInterpolation()
                    .setZeroPowerAccelerationMultiplier(AutoConstants.decelMultiplier)
                    .build();

            ReturnSpike3 = follower.pathBuilder()
                    .addPath(new BezierCurve(finalIntake, finalShootCont, finalShootingPose))
                    .setLinearHeadingInterpolation(Math.toRadians(heading180), Math.toRadians(finalShootHeading))
                    .setZeroPowerAccelerationMultiplier(AutoConstants.decelMultiplier)
                    .build();
        }
    }
}
