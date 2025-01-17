// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.commands;

import edu.wpi.first.math.controller.PIDController;
import edu.wpi.first.wpilibj2.command.PIDCommand;
import frc.robot.subsystems.Arm;
import java.lang.Math;

// NOTE:  Consider using this command inline, rather than writing a subclass.  For more
// information, see:
// https://docs.wpilib.org/en/stable/docs/software/commandbased/convenience-features.html
public class ArmToAnglePID extends PIDCommand {
  /** Creates a new moveArmToAnglePID. */
  public ArmToAnglePID(Arm arm, double angle){

    super(
        // The controller that the command will use
        new PIDController(0.004, 0, 0), //originally .005
        // This should return the measurement
        arm::getArmEcnoderAngle,
        // This should return the setpoint (can also be a constant)
        angle,
        // This uses the output
        output -> {
          // Use the output here
          //arm.setMotors(output + 0.18);
          if(output > 0){
            arm.setMotors(output + (0.18 * Math.sin(Math.toRadians(arm.getArmEcnoderAngle()/2.4))));

          }

          if (output < 0){
            arm.setMotors( output + (0.18 * Math.sin(Math.toRadians(arm.getArmEcnoderAngle()/2.4))));

          }



          //Should compensate for gravity  maybe

        }, arm);
    // Use addRequirements() here to declare subsystem dependencies.
    // Configure additional PID options by calling `getController` here.

      getController().setTolerance(5); //Roughly 3.5ish degrees
  }

  // Returns true when the command should end.
  @Override
  public boolean isFinished() {
    return getController().atSetpoint();
  }
}
