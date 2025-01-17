// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.commands;

import edu.wpi.first.wpilibj.AddressableLED;
import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.subsystems.Intake;

public class IntakeUntilLimit extends CommandBase {
  /** Creates a new intakeUntilLimit. */
  private final Intake intake;
  public IntakeUntilLimit(Intake intake) {
    this.intake = intake;
    addRequirements(intake);

  }

  // Called when the command is initially scheduled.
  @Override
  public void initialize() {

    intake.setIntake(0.65);
  }

  // Called every time the scheduler runs while the command is scheduled.
  @Override
  public void execute() {
    intake.setIntake(0.65);
  }

  // Called once the command ends or is interrupted.
  @Override
  public void end(boolean interrupted) {
    intake.setIntake(0);
  }

  // Returns true when the command should end.
  @Override
  public boolean isFinished() {
    return intake.limitIsEngaged();
  }
}
