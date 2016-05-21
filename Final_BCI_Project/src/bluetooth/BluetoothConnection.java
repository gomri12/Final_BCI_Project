package bluetooth;

import java.util.Scanner;
import jssc.SerialPort;
import jssc.SerialPortException;
import jssc.SerialPortList;

public class BluetoothConnection {

	private String[] portNames;
	private SerialPort SP;

	public String[] getCOMPorts() {
		portNames = SerialPortList.getPortNames();
		if (portNames.length != 0)
			return portNames;
		portNames = new String[1];
		portNames[0] = "No Bluetooth";
		return portNames;
	}

	public void setCOMPort(int choise) {
		SP = new SerialPort(portNames[choise]);

	}

	public String connectToBluetooth() {

		try {

			SP.openPort();// Open serial port
		} catch (SerialPortException ex) {
			System.out.println(ex);
			return "Failed";
		}
		return "Success";
	}

	public void sendCommand(String command) {
		try {
			SP.setParams(SerialPort.BAUDRATE_9600, SerialPort.DATABITS_8, SerialPort.STOPBITS_1,
					SerialPort.PARITY_NONE);
			// Set params. Also you can set
			// params by this string:
			// serialPort.setParams(9600, 8,
			// 1, 0);

			SP.writeBytes(command.getBytes());// Write data to port
		} catch (SerialPortException ex) {
			System.out.println(ex);
		}

	}

	public void closeConnection() {
		try {
			SP.closePort();
		} catch (SerialPortException ex) {
			System.out.println(ex);
		}
	}
}
