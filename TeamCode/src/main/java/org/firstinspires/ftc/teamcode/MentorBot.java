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
    // Define a multiplier to reduce motor power
    final double POWER_MULTIPLIER = 0.5;

    // Multipliers for how fast the claw and tilt servos move from the joystick input
    final double CLAW_SERVO_SPEED = 0.01;
    final double TILT_SERVO_SPEED = 0.01;

    // Constants to limit the claw and tilt servo positions. Adjust as needed.
    final double CLAW_SERVO_MIN_POSITION = 0; // Do not set the minimum position to less than 0.0
    final double CLAW_SERVO_MAX_POSITION = 1; // Do not set the maximum position to more than 1.0
    final double TILT_SERVO_MIN_POSITION = 0; // Do not set the minimum position to less than 0.0
    final double TILT_SERVO_MAX_POSITION = 1; // Do not set the maximum position to more than 1.0

    CRServo liftLower, liftUpper;
    Servo tiltServo, clawServo;
    DcMotor frontLeftMotor, frontRightMotor, backLeftMotor, backRightMotor;

    // Initialize the hardware variables
    liftLower = hardwareMap.get(CRServo.class, "servo0"); // 0
    liftUpper = hardwareMap.get(CRServo.class, "servo1"); // 1
    tiltServo = hardwareMap.get(Servo.class, "servo2"); // 2
    clawServo = hardwareMap.get(Servo.class, "servo3"); // 3

    frontLeftMotor = hardwareMap.get(DcMotor.class, "mfl"); // 0
    frontRightMotor = hardwareMap.get(DcMotor.class, "mfr"); // 1
    backLeftMotor = hardwareMap.get(DcMotor.class, "mbl"); // 2
    backRightMotor = hardwareMap.get(DcMotor.class, "mbr"); // 3

    // Set the direction of the motors
    // backLeftMotor.setDirection(DcMotor.Direction.REVERSE);
    backRightMotor.setDirection(DcMotor.Direction.REVERSE);

    // Initialize the claw and tilt servo positions to 0.5
    clawServo.setPosition(0.5);
    double clawServoPosition = 0.5;

    tiltServo.setPosition(0.5);
    double tiltServoPosition = 0.5;

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
      double frontLeftPower = (y + x + rx) / denominator * POWER_MULTIPLIER;
      double backLeftPower = (y - x + rx) / denominator * POWER_MULTIPLIER;
      double frontRightPower = (y - x - rx) / denominator * POWER_MULTIPLIER;
      double backRightPower = (y + x - rx) / denominator * POWER_MULTIPLIER;

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

      // Use gamepad2 left stick x-axis to control tiltServo
      double tiltVelocity = gamepad2.left_stick_x * TILT_SERVO_SPEED; // Adjust the multiplier as needed for desired speed
      tiltServoPosition += tiltVelocity;
      tiltServoPosition = Math.max(TILT_SERVO_MIN_POSITION, Math.min(TILT_SERVO_MAX_POSITION, tiltServoPosition)); // Ensure the position stays within range

      // Despite the TILT_SERVO_MIN_POSITION and TILT_SERVO_MAX_POSITION, we never want to exceed the range [0, 1]
      // Do a final check to ensure the position is within the range [0, 1]
      tiltServoPosition = Math.max(0, Math.min(1, tiltServoPosition));

      tiltServo.setPosition(tiltServoPosition);

      // Use gamepad2 right stick x-axis to control clawServo
      double clawVelocity = gamepad2.right_stick_x * CLAW_SERVO_SPEED; // Adjust the multiplier as needed for desired speed
      clawServoPosition += clawVelocity;
      clawServoPosition = Math.max(CLAW_SERVO_MIN_POSITION, Math.min(CLAW_SERVO_MAX_POSITION, clawServoPosition)); // Ensure the position stays within range

      // Despite the CLAW_SERVO_MIN_POSITION and CLAW_SERVO_MAX_POSITION, we never want to exceed the range [0, 1]
      // Do a final check to ensure the position is within the range [0, 1]
      clawServoPosition = Math.max(0, Math.min(1, clawServoPosition));

      clawServo.setPosition(clawServoPosition);

      // Show the current servo positions on the telemetry
      telemetry.addData("Lift Lower Speed", liftLowerSpeed);
      telemetry.addData("Lift Upper Speed", liftUpperSpeed);
      telemetry.addData("Tilt Servo Position", tiltServoPosition);
      telemetry.addData("Claw Servo Position", clawServoPosition);

      telemetry.update();
    }
  }
}
