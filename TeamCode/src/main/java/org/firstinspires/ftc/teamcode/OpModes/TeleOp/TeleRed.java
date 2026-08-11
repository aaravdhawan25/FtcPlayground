package org.firstinspires.ftc.teamcode.OpModes.TeleOp;

import com.arcrobotics.ftclib.command.CommandScheduler;
import com.arcrobotics.ftclib.command.InstantCommand;
import com.arcrobotics.ftclib.command.ParallelCommandGroup;
import com.arcrobotics.ftclib.gamepad.GamepadEx;
import com.arcrobotics.ftclib.gamepad.GamepadKeys;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.teamcode.Robot.Commands.BlockerCommand;
import org.firstinspires.ftc.teamcode.Robot.Commands.IntakeCommand;
import org.firstinspires.ftc.teamcode.Robot.Commands.PointToGoalCommand;
import org.firstinspires.ftc.teamcode.Robot.Commands.ShooterCommand;
import org.firstinspires.ftc.teamcode.Robot.Commands.TransferCancelCommand;
import org.firstinspires.ftc.teamcode.Robot.Commands.TransferCommand;
import org.firstinspires.ftc.teamcode.Robot.Robot;
import org.firstinspires.ftc.teamcode.Robot.Subsystems.Blocker;
import org.firstinspires.ftc.teamcode.Robot.Subsystems.Intake;
import org.firstinspires.ftc.teamcode.Robot.Subsystems.LLCam;
import org.firstinspires.ftc.teamcode.Robot.Subsystems.Shooter;

@TeleOp(name = "TeleOp Red", group = " ")
public class TeleRed extends LinearOpMode {

    @Override
    public void runOpMode() throws InterruptedException {
        Robot robot = new Robot(hardwareMap, telemetry, "RED", false);
        GamepadEx gp1 = new GamepadEx(gamepad1);
        GamepadEx gp2 = new GamepadEx(gamepad2);

        gp1.getGamepadButton(GamepadKeys.Button.RIGHT_BUMPER).whenPressed(
                new IntakeCommand(robot, Intake.IntakeState.ON)
        );
        gp1.getGamepadButton(GamepadKeys.Button.RIGHT_BUMPER).whenReleased(
                new IntakeCommand(robot, Intake.IntakeState.OFF)
        );
        gp1.getGamepadButton(GamepadKeys.Button.LEFT_BUMPER).whenPressed(
                new IntakeCommand(robot, Intake.IntakeState.REVERSE)
        );
        gp1.getGamepadButton(GamepadKeys.Button.LEFT_BUMPER).whenReleased(
                new IntakeCommand(robot, Intake.IntakeState.OFF)
        );
        gp1.getGamepadButton(GamepadKeys.Button.X).whenPressed(
                new PointToGoalCommand(robot, LLCam.CamState.ALIGN)
        );
        gp1.getGamepadButton(GamepadKeys.Button.X).whenReleased(
                new PointToGoalCommand(robot, LLCam.CamState.STOP)
        );
        gp1.getGamepadButton(GamepadKeys.Button.DPAD_DOWN).whenPressed(
                new InstantCommand(robot::holding)
        );
        gp1.getGamepadButton(GamepadKeys.Button.DPAD_DOWN).whenPressed(
                new InstantCommand(robot::stopHolding)
        );
        gp2.getGamepadButton(GamepadKeys.Button.Y).whenPressed(
                new ParallelCommandGroup(
                        new ShooterCommand(robot, Shooter.ShooterState.MATH),
                        new TransferCommand(robot)
                )
        );
        gp2.getGamepadButton(GamepadKeys.Button.Y).whenReleased(
                new TransferCancelCommand(robot)
        );

        gp2.getGamepadButton(GamepadKeys.Button.X).whenPressed(
                new ParallelCommandGroup(
                        new ShooterCommand(robot, Shooter.ShooterState.CLOSE),
                        new TransferCommand(robot)
                )

        );
        gp2.getGamepadButton(GamepadKeys.Button.X).whenReleased(
                new TransferCancelCommand(robot)

        );

        CommandScheduler.getInstance().schedule(new BlockerCommand(robot, Blocker.BlockerState.CLOSED));

        while (opModeInInit()){
            robot.init();
        }

        waitForStart();

        if(isStarted()){
            robot.follower.startTeleopDrive();
        }

        if (isStopRequested()){
            robot.stop();
            return;
        }

        while (opModeIsActive()){
            robot.update();
            robot.setTeleOpMovementVectors(-gamepad1.left_stick_y, -gamepad1.left_stick_x, -gamepad1.right_stick_x, true);
        }

    }
}


