package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.CRServo;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.hardware.DcMotor;

@TeleOp(name = "MentorBot", group = "Linear Opmode")
public class MentorBot extends LinearOpMode {
  @Override
  public void runOpMode() {
    CRServo liftLower, liftUpper;
    Servo clawLeft, clawRight;
    DcMotor frontLeftMotor, frontRightMotor, backLeftMotor, backRightMotor;

    // Initialize the hardware variables
    liftLower = hardwareMap.get(CRServo.class, "servo0"); // 0
    liftUpper = hardwareMap.get(CRServo.class, "servo1"); // 1
    clawLeft = hardwareMap.get(Servo.class, "servo2"); // 2
    clawRight = hardwareMap.get(Servo.class, "servo3"); // 3

    frontLeftMotor = hardwareMap.get(DcMotor.class, "mfl"); // 0
    frontRightMotor = hardwareMap.get(DcMotor.class, "mfr"); // 1
    backLeftMotor = hardwareMap.get(DcMotor.class, "mbl"); // 2
    backRightMotor = hardwareMap.get(DcMotor.class, "mbr"); // 3

    // Set the direction of the motors
    backLeftMotor.setDirection(DcMotor.Direction.REVERSE);
    backRightMotor.setDirection(DcMotor.Direction.REVERSE);

    // Wait for the game to start (driver presses PLAY)
    waitForStart();

    if (isStopRequested()) {
      return;
    }

    // Run until the end of the match (driver presses STOP)
    while (opModeIsActive()) {
      // Use Robot Centric Mecanum (Holonomic) Control
      // https://gm0.org/en/latest/docs/software/tutorials/mecanum-drive.html#robot-centric-final-sample-code
      double y = -gamepad1.left_stick_y; // Remember, Y stick value is reversed
      double x = gamepad1.left_stick_x * 1.1; // Counteract imperfect strafing
      double rx = gamepad1.right_stick_x;

      // Denominator is the largest motor power (absolute value) or 1
      // This ensures all the powers maintain the same ratio,
      // but only if at least one is out of the range [-1, 1]
      double denominator = Math.max(Math.abs(y) + Math.abs(x) + Math.abs(rx), 1);
      double frontLeftPower = (y + x + rx) / denominator;
      double backLeftPower = (y - x + rx) / denominator;
      double frontRightPower = (y - x - rx) / denominator;
      double backRightPower = (y + x - rx) / denominator;

      frontLeftMotor.setPower(frontLeftPower);
      backLeftMotor.setPower(backLeftPower);
      frontRightMotor.setPower(frontRightPower);
      backRightMotor.setPower(backRightPower);

      // Show the current motor powers on the telemetry
      telemetry.addData("Front Left Drive Power", frontLeftPower);
      telemetry.addData("Back Left Drive Power", backLeftPower);
      telemetry.addData("Front Right Drive Power", frontRightPower);
      telemetry.addData("Back Right Drive Power", backRightPower);

      // Use gamepad2 left stick y-axis to control liftLower
      double liftLowerSpeed = -gamepad2.left_stick_y;
      liftLower.setPower(liftLowerSpeed);

      // Use gamepad2 right stick y-axis to control liftUpper
      double liftUpperSpeed = -gamepad2.right_stick_y;
      liftUpper.setPower(liftUpperSpeed);

      // Use gamepad2 left stick x-axis to control clawLeft
      double clawLeftPosition = (gamepad2.left_stick_x + 1) / 2; // Convert from -1 to 1 range to 0 to 1 range
      clawLeft.setPosition(clawLeftPosition);

      // Use gamepad2 right stick x-axis to control clawRight
      double clawRightPosition = (gamepad2.right_stick_x + 1) / 2; // Convert from -1 to 1 range to 0 to 1 range
      clawRight.setPosition(clawRightPosition);

      // Show the current servo positions on the telemetry
      telemetry.addData("Lift Lower Speed", liftLowerSpeed);
      telemetry.addData("Lift Upper Speed", liftUpperSpeed);
      telemetry.addData("Claw Left Position", clawLeftPosition);
      telemetry.addData("Claw Right Position", clawRightPosition);

      telemetry.update();
    }
  }
}
