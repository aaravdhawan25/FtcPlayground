package org.firstinspires.ftc.teamcode.Robot.Commands;

import com.arcrobotics.ftclib.command.ParallelCommandGroup;
import com.arcrobotics.ftclib.command.ParallelRaceGroup;
import com.arcrobotics.ftclib.command.SequentialCommandGroup;

import org.firstinspires.ftc.teamcode.Robot.MyRobot;
import org.firstinspires.ftc.teamcode.Robot.Subsystems.Blocker;
import org.firstinspires.ftc.teamcode.Robot.Subsystems.Intake;
import org.firstinspires.ftc.teamcode.Robot.Subsystems.Shooter;

public class TransferCancelCommand extends ParallelCommandGroup {
    public TransferCancelCommand(MyRobot robot){
        addCommands(
                new BlockerCommand(robot, Blocker.BlockerState.CLOSED),
                new IntakeCommand(robot, Intake.IntakeState.OFF),
                new ShooterCommand(robot, Shooter.ShooterState.IDLE)
        );
    }
}
