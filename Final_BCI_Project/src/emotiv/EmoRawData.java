package emotiv;

import java.util.concurrent.ThreadLocalRandom;

import com.sun.jna.Pointer;
import com.sun.jna.ptr.IntByReference;

import GUI.RobotControlWindow;

public class EmoRawData extends Thread {
	// public static void main(String[] args)
	private RobotControlWindow RCWRef;

	public EmoRawData(RobotControlWindow ref)

	{
		RCWRef = ref;
	}

	private IntByReference pXOut = new IntByReference(0);
	private IntByReference pYOut = new IntByReference(0);

	public void run() {
		Pointer eEvent = Edk.INSTANCE.EE_EmoEngineEventCreate();
		Pointer eState = Edk.INSTANCE.EE_EmoStateCreate();
		IntByReference userID = null;
		IntByReference nSamplesTaken = null;
		short composerPort = 1726;
		int option = 1;
		int state = 0;
		int eventType = 0;
		float secs = 1;
		boolean readytocollect = false;

		userID = new IntByReference(0);
		nSamplesTaken = new IntByReference(0);

		switch (option) {
		case 1: {
			if (Edk.INSTANCE.EE_EngineConnect("Emotiv Systems-5") != EdkErrorCode.EDK_OK.ToInt()) {
				System.out.println("Emotiv Engine start up failed.");
				return;
			}
			break;
		}
		case 2: {
			System.out.println("Target IP of EmoComposer: [127.0.0.1] ");

			if (Edk.INSTANCE.EE_EngineRemoteConnect("127.0.0.1", composerPort,
					"Emotiv Systems-5") != EdkErrorCode.EDK_OK.ToInt()) {
				System.out.println("Cannot connect to EmoComposer on [127.0.0.1]");
				return;
			}
			System.out.println("Connected to EmoComposer on [127.0.0.1]");
			break;
		}
		default:
			System.out.println("Invalid option...");
			return;
		}

		Pointer hData = Edk.INSTANCE.EE_DataCreate();
		Edk.INSTANCE.EE_DataSetBufferSizeInSec(secs);
		System.out.print("Buffer size in secs: ");
		System.out.println(secs);

		System.out.println("Start receiving EEG Data!");
		while (true) {
			state = Edk.INSTANCE.EE_EngineGetNextEvent(eEvent);

			// New event needs to be handled
			if (state == EdkErrorCode.EDK_OK.ToInt()) {
				eventType = Edk.INSTANCE.EE_EmoEngineEventGetType(eEvent);
				Edk.INSTANCE.EE_EmoEngineEventGetUserId(eEvent, userID);

				// Log the EmoState if it has been updated
				if (eventType == Edk.EE_Event_t.EE_UserAdded.ToInt())
					if (userID != null) {
						System.out.println("Dongle Is Connected!");
						Edk.INSTANCE.EE_DataAcquisitionEnable(userID.getValue(), true);
						readytocollect = true;
					}
			} else if (state != EdkErrorCode.EDK_NO_EVENT.ToInt()) {
				System.out.println("Internal error in Emotiv Engine!");
				break;
			}

			if (readytocollect) {
				Edk.INSTANCE.EE_DataUpdateHandle(0, hData);

				Edk.INSTANCE.EE_DataGetNumberOfSample(hData, nSamplesTaken);

				if (nSamplesTaken != null) {
					if (nSamplesTaken.getValue() != 0) {

						// System.out.print("Updated: ");
						// System.out.println(nSamplesTaken.getValue());

						double[] data = new double[nSamplesTaken.getValue()];
						for (int sampleIdx = 0; sampleIdx < nSamplesTaken.getValue(); ++sampleIdx) {
							// for (int i = 0 ; i < 14 ; i++) {
							//
							// Edk.INSTANCE.EE_DataGet(hData, i, data,
							// nSamplesTaken.getValue());
							// System.out.print(data[sampleIdx]);
							// System.out.print(",");
							// }
							// System.out.println();
						}
						EmoProfileManagement.AddNewProfile("3");
//						if (eventType == Edk.EE_Event_t.EE_EmoStateUpdated.ToInt()) {
							Edk.INSTANCE.EE_EmoEngineEventGetEmoState(eEvent, eState);
//
//							// {
							
							int action = EmoState.INSTANCE.ES_CognitivGetCurrentAction(eState);
							double power = EmoState.INSTANCE.ES_CognitivGetCurrentActionPower(eState);
							if (power != 0) {
								System.out.println("Action:" + action);
								System.out.println("Power:" + power);
//							}
//							// }
						}

						Edk.INSTANCE.EE_HeadsetGetGyroDelta(userID.getValue(), pXOut, pYOut);
						if (pXOut.getValue() > 90) {
							RCWRef.sendBluetoothCommand("r");
							System.out.println("Right");
						} else if (pXOut.getValue() < -90) {
							RCWRef.sendBluetoothCommand("l");
							System.out.println("left");
						}
						if (pYOut.getValue() > 90) {
							RCWRef.sendBluetoothCommand("f");
							System.out.println("Forward");
						} else if (pYOut.getValue() < -90) {
							RCWRef.sendBluetoothCommand("b");
							System.out.println("Backward");
						}
					}
				}
			}
		}

		Edk.INSTANCE.EE_EngineDisconnect();
		Edk.INSTANCE.EE_EmoStateFree(eState);
		Edk.INSTANCE.EE_EmoEngineEventFree(eEvent);
		System.out.println("Disconnected!");
	}

}
