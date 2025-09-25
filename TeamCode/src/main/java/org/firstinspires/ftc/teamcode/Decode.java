package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.util.ElapsedTime;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.hardware.IMU;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.hardware.rev.RevHubOrientationOnRobot;
import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;
import com.qualcomm.robotcore.util.ElapsedTime;

@TeleOp
public class Decode extends LinearOpMode {
    private ElapsedTime runtime = new ElapsedTime();

    @Override
    public void runOpMode() throws InterruptedException {
        telemetry.addData("Status", "Initialized");
        telemetry.update();

        DcMotor acceleratorL = hardwareMap.get(DcMotor.class, "dev-name-left");
        DcMotor acceleratorR = hardwareMap.get(DcMotor.class, "dev-name-right");

        boolean acceleraters_on = false;
        boolean test = true;

        DcMotor frontLeftMotor = hardwareMap.dcMotor.get("mecFrontLeft");
        DcMotor backLeftMotor = hardwareMap.dcMotor.get("mecBackLeft");
        DcMotor frontRightMotor = hardwareMap.dcMotor.get("mecFrontRight");
        DcMotor backRightMotor = hardwareMap.dcMotor.get("mecBackRight");

        frontLeftMotor.setDirection(DcMotorSimple.Direction.REVERSE);
        backLeftMotor.setDirection(DcMotorSimple.Direction.REVERSE);
        //acceleratorL.setDirection(DcMotorSimple.Direction.REVERSE);

        waitForStart();

        if (isStopRequested()) return;

        while (opModeIsActive()) {
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

            // accelerators

            if (gamepad2.x && !acceleraters_on) {
                acceleratorR.setPower(1);
                acceleratorL.setPower(-1);
                acceleraters_on = true;

            } else if (gamepad2.x && acceleraters_on) {
                acceleratorR.setPower(0);
                acceleratorL.setPower(0);
                acceleraters_on = false;
            }

            telemetry.addData("Status", "Run Time: " + runtime.toString());

            telemetry.update();
        }
    }
}
