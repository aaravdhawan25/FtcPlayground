package org.firstinspires.ftc.teamcode.Robot.Commands;

import com.arcrobotics.ftclib.command.InstantCommand;
import com.arcrobotics.ftclib.command.SequentialCommandGroup;

import org.firstinspires.ftc.teamcode.Robot.Robot;
import org.firstinspires.ftc.teamcode.Robot.Subsystems.Shooter;

public class ShooterCommand extends SequentialCommandGroup {
    public ShooterCommand(Robot robot, Shooter.ShooterState state){
        addCommands(
                new InstantCommand(()->robot.shooter.setState(state), robot.shooter)
        );
    }
}
