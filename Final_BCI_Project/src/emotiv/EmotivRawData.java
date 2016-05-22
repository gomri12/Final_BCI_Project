package emotiv;
import com.sun.jna.Pointer;
import com.sun.jna.ptr.IntByReference;

public class EmotivRawData {
	 public static void main(String[] args)
	 {

		new Thread() {
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
				float secs = 1;
				boolean readytocollect = false;

				userID = new IntByReference(0);
				nSamplesTaken = new IntByReference(0);

				switch (option) {
				case 1: {
					if (Edk.INSTANCE.EE_EngineConnect("emotiv Systems-5") != EdkErrorCode.EDK_OK.ToInt()) {
						System.out.println("emotiv Engine start up failed.");
						return;
					}
					break;
				}
				case 2: {
					System.out.println("Target IP of EmoComposer: [127.0.0.1] ");

					if (Edk.INSTANCE.EE_EngineRemoteConnect("127.0.0.1", composerPort,
							"emotiv Systems-5") != EdkErrorCode.EDK_OK.ToInt()) {
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
						int eventType = Edk.INSTANCE.EE_EmoEngineEventGetType(eEvent);
						Edk.INSTANCE.EE_EmoEngineEventGetUserId(eEvent, userID);

						// Log the EmoState if it has been updated
						if (eventType == Edk.EE_Event_t.EE_UserAdded.ToInt())
							if (userID != null) {
								System.out.println("User added");
								Edk.INSTANCE.EE_DataAcquisitionEnable(userID.getValue(), true);
								readytocollect = true;
							}
					} else if (state != EdkErrorCode.EDK_NO_EVENT.ToInt()) {
						System.out.println("Internal error in emotiv Engine!");
						break;
					}

					if (readytocollect) {
						Edk.INSTANCE.EE_DataUpdateHandle(0, hData);

						Edk.INSTANCE.EE_DataGetNumberOfSample(hData, nSamplesTaken);

						if (nSamplesTaken != null) {
							if (nSamplesTaken.getValue() != 0) {

							//	System.out.print("Updated: ");
							//	System.out.println(nSamplesTaken.getValue());

								double[] data = new double[nSamplesTaken.getValue()];
								for (int sampleIdx = 0; sampleIdx < nSamplesTaken.getValue(); ++sampleIdx) {
									//System.out.println("X    ,    Y");
									for (int i = 17; i < 19; i++) {
									//	Edk.INSTANCE.EE_DataGet(hData, i, data, nSamplesTaken.getValue());

									//	System.out.print(data[sampleIdx]);
									//	System.out.print(",");

										//System.out.print(",");
									}
								//	System.out.println();


								}
								Edk.INSTANCE.EE_HeadsetGetGyroDelta(userID.getValue(), pXOut, pYOut);
								if(pXOut.getValue()>150){
									System.out.println("left");
								}else if(pXOut.getValue()<-150){
									System.out.println("Right");
								}
								if(pYOut.getValue()>150){
									System.out.println("Forward");
								}else if(pYOut.getValue()<-150)
								{
									System.out.println("Backward");
								}
							//	System.out.println("\n"+pXOut.getValue()+" , "+pYOut.getValue());
							}
						}
					}
				}
				Edk.INSTANCE.EE_EngineDisconnect();
				Edk.INSTANCE.EE_EmoStateFree(eState);
				Edk.INSTANCE.EE_EmoEngineEventFree(eEvent);
				System.out.println("Disconnected!");
			}
		}.start();

	}
}
