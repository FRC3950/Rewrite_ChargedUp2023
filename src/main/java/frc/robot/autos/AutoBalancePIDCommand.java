// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.autos;

import edu.wpi.first.math.controller.PIDController;
import edu.wpi.first.wpilibj2.command.PIDCommand;
import frc.robot.subsystems.*;

//34.25 is the angle to correct for!


// NOTE:  Consider using this command inline, rather than writing a subclass.  For more
// information, see:
// https://docs.wpilib.org/en/stable/docs/software/commandbased/convenience-features.html
public class AutoBalancePIDCommand extends PIDCommand {
  /** Creates a new AutoBalancePIDCommand. */
  public AutoBalancePIDCommand(Swerve swerve) {
    super(
        // The controller that the command will use
        new PIDController(0.0105, 0, 0),
        // This should return the measurement
        swerve::getRoll,
        // This should return the setpoint (can also be a constant)
        () -> 0,
        // This uses the output
        output -> {
          swerve.driveHorizontal(output); //consider output + base number (kF)
        }, swerve);

       // + Math.signum(output) * 0.02
    // Use addRequirements() here to declare subsystem dependencies.
    // Configure additional PID options by calling `getController` here.
    getController().setTolerance(1.5);
  }

  // Returns true when the command should end.
  @Override
  public boolean isFinished() {
    return false;
  }
}
