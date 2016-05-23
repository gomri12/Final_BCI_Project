package emotiv;

import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JOptionPane;

import com.sun.jna.Pointer;
import com.sun.jna.ptr.IntByReference;

import GUI.TrainNewUserWindow;

public class EmoTrainNewUser extends Thread {
	
	public static JComboBox comboBox;
	private TrainNewUserWindow TNURef;
	public static int[] cognitivActionList ={
			   EmoState.EE_CognitivAction_t.COG_NEUTRAL.ToInt(),
			   EmoState.EE_CognitivAction_t.COG_PUSH.ToInt(),
			   EmoState.EE_CognitivAction_t.COG_PULL.ToInt(),
			   EmoState.EE_CognitivAction_t.COG_LIFT.ToInt(),
			   EmoState.EE_CognitivAction_t.COG_DROP.ToInt(),
			   EmoState.EE_CognitivAction_t.COG_LEFT.ToInt(),
			   EmoState.EE_CognitivAction_t.COG_RIGHT.ToInt(),
			   EmoState.EE_CognitivAction_t.COG_ROTATE_LEFT.ToInt(),
			   EmoState.EE_CognitivAction_t.COG_ROTATE_RIGHT.ToInt(),
			   EmoState.EE_CognitivAction_t.COG_ROTATE_CLOCKWISE.ToInt(),
               EmoState.EE_CognitivAction_t.COG_ROTATE_COUNTER_CLOCKWISE.ToInt(),
               EmoState.EE_CognitivAction_t.COG_ROTATE_FORWARDS.ToInt(),
               EmoState.EE_CognitivAction_t.COG_ROTATE_REVERSE.ToInt(),
               EmoState.EE_CognitivAction_t.COG_DISAPPEAR.ToInt()
				};
	
	public static Boolean[] cognitivActionsEnabled = new Boolean[cognitivActionList.length];
	
	
	public EmoTrainNewUser(TrainNewUserWindow TNU){
		cognitivActionsEnabled[0] = true;
        for (int i = 1; i < cognitivActionList.length; i++)
        {
            cognitivActionsEnabled[i] = false;
        }
        
        TNURef = TNU;
	    
			
	}
    public static void StartTrainingCognitiv(EmoState.EE_CognitivAction_t cognitivAction)
    {
        if (cognitivAction == EmoState.EE_CognitivAction_t.COG_NEUTRAL)
        {
        	Edk.INSTANCE.EE_CognitivSetTrainingAction(0,EmoState.EE_CognitivAction_t.COG_NEUTRAL.ToInt());
			Edk.INSTANCE.EE_CognitivSetTrainingControl(0, Edk.EE_CognitivTrainingControl_t.COG_START.getType());
        }
        else
            for (int i = 1; i < cognitivActionList.length; i++)
            {
                if (cognitivAction.ToInt() == cognitivActionList[i])
                {
                    
                    if (cognitivActionsEnabled[i])
                    {
                    	Edk.INSTANCE.EE_CognitivSetTrainingAction(0, cognitivAction.ToInt());
                    	Edk.INSTANCE.EE_CognitivSetTrainingControl(0, Edk.EE_CognitivTrainingControl_t.COG_START.getType());
                    }
                    
                }
            }

    }
    
    public static void EnableCognitivAction(EmoState.EE_CognitivAction_t cognitivAction, Boolean iBool)
    {
        for (int i = 1; i < cognitivActionList.length; i++)
        {
            if (cognitivAction.ToInt() == cognitivActionList[i])
            {
                cognitivActionsEnabled[i] = iBool;
                
            }
        }

    }
    
    public static void EnableCognitivActionsList()
    {
        long cognitivActions = 0x0000;
        for (int i = 1; i < cognitivActionList.length; i++)
        {
            if (cognitivActionsEnabled[i])
            {
                cognitivActions = cognitivActions | ((long)cognitivActionList[i]);
                
            }
        }
        Edk.INSTANCE.EE_CognitivSetActiveActions(0, cognitivActions);
        
    }
    
    public void run()
    {
    	
    	Pointer eEvent			= Edk.INSTANCE.EE_EmoEngineEventCreate();
    	Pointer eState			= Edk.INSTANCE.EE_EmoStateCreate();
    	IntByReference userID 	= null;
    	short composerPort		= 1726;
    	int option 				= 1;
    	int state  				= 0;
    	
    	
    	
    	userID = new IntByReference(0);
    	
    	switch (option) {
		case 1:
		{
			if (Edk.INSTANCE.EE_EngineConnect("Emotiv Systems-5") != EdkErrorCode.EDK_OK.ToInt()) {
				System.out.println("Emotiv Engine start up failed.");
				return;
			}
			break;
		}
		case 2:
		{
			System.out.println("Target IP of EmoComposer: [127.0.0.1] ");

			if (Edk.INSTANCE.EE_EngineRemoteConnect("127.0.0.1", composerPort, "Emotiv Systems-5") != EdkErrorCode.EDK_OK.ToInt()) {
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
    	
		while (true) 
			{
				state = Edk.INSTANCE.EE_EngineGetNextEvent(eEvent);
				
				// New event needs to be handled
				if (state == EdkErrorCode.EDK_OK.ToInt()) {
	
					int eventType = Edk.INSTANCE.EE_EmoEngineEventGetType(eEvent);
					Edk.INSTANCE.EE_EmoEngineEventGetUserId(eEvent, userID);
					if(eventType == Edk.EE_Event_t.EE_UserAdded.ToInt())
					{
						EmoProfileManagement.AddNewProfile("3");
						JOptionPane.showMessageDialog(new JFrame(), "Emotiv Dongle Is Connected", "Dialog",
					        JOptionPane.INFORMATION_MESSAGE);
					}
					
					if(eventType == Edk.EE_Event_t.EE_CognitivEvent.ToInt())
					{
						int cogType = Edk.INSTANCE.EE_CognitivEventGetType(eEvent);
						
						if(cogType ==Edk.EE_CognitivEvent_t.EE_CognitivTrainingStarted.getType())
						{
							JOptionPane.showMessageDialog(new JFrame(), "Cognitiv Training Start", "Dialog",
							        JOptionPane.INFORMATION_MESSAGE);
//							System.out.println("Cognitiv Training Start");
						}
						if(cogType == Edk.EE_CognitivEvent_t.EE_CognitivTrainingCompleted.getType())
						{
							JOptionPane.showMessageDialog(new JFrame(), "Cognitiv Training Complete", "Dialog",
							        JOptionPane.INFORMATION_MESSAGE);
							TNURef.SetUIEnable(true);
						}
						if(cogType == Edk.EE_CognitivEvent_t.EE_CognitivTrainingSucceeded.getType())
						{
							Edk.INSTANCE.EE_CognitivSetTrainingControl(0,Edk.EE_CognitivTrainingControl_t.COG_ACCEPT.getType());
							JOptionPane.showMessageDialog(new JFrame(), "Cognitiv Training Succeeded", "Dialog",
							        JOptionPane.INFORMATION_MESSAGE);
							
						}
						if(cogType == Edk.EE_CognitivEvent_t.EE_CognitivTrainingFailed.getType())
						{
							JOptionPane.showMessageDialog(new JFrame(), "Cognitiv Training Failed", "Dialog",
							        JOptionPane.ERROR_MESSAGE);
							TNURef.SetUIEnable(true);
						}
						if(cogType == Edk.EE_CognitivEvent_t.EE_CognitivTrainingRejected.getType())
						{
							JOptionPane.showMessageDialog(new JFrame(), "Cognitiv Training Rejected", "Dialog",
							        JOptionPane.ERROR_MESSAGE);
							TNURef.SetUIEnable(true);
						}
					}
					if(eventType == Edk.EE_Event_t.EE_EmoStateUpdated.ToInt())
					{
//						Edk.INSTANCE.EE_EmoEngineEventGetEmoState(eEvent, eState);
//						
//						//{
//							int action = EmoState.INSTANCE.ES_CognitivGetCurrentAction(eState);
//							double power = EmoState.INSTANCE.ES_CognitivGetCurrentActionPower(eState);
//							if(power!=0)
//							{
//								System.out.println("Action:" + action);
//								System.out.println("Power:" + power);
//							}
//						//}
					}
				}
				else if (state != EdkErrorCode.EDK_NO_EVENT.ToInt()) {
					System.out.println("Internal error in Emotiv Engine!");
					break;
				}
			}
	    	
	    	Edk.INSTANCE.EE_EngineDisconnect();
	    	System.out.println("Disconnected!");
    }
    
	public void StartTraining(int index) {
		if(index == 0)
		{
			Edk.INSTANCE.EE_CognitivSetTrainingAction(0,EmoState.EE_CognitivAction_t.COG_NEUTRAL.ToInt());
			Edk.INSTANCE.EE_CognitivSetTrainingControl(0, Edk.EE_CognitivTrainingControl_t.COG_START.getType());
		}
		if(index ==1)
		{
			try
			{
				EnableCognitivAction(EmoState.EE_CognitivAction_t.COG_PUSH, true);
				EnableCognitivActionsList();
				StartTrainingCognitiv(EmoState.EE_CognitivAction_t.COG_PUSH);
			}
			catch(Exception ex){
				ex.printStackTrace();
			}
		}
		if(index == 2)
		{
			try
			{
				EnableCognitivAction(EmoState.EE_CognitivAction_t.COG_PULL, true);
				EnableCognitivActionsList();
				StartTrainingCognitiv(EmoState.EE_CognitivAction_t.COG_PULL);
			}
			catch(Exception ex){
				ex.printStackTrace();
			}
		}
		if(index == 3)
		{
			try
			{
				EnableCognitivAction(EmoState.EE_CognitivAction_t.COG_LEFT, true);
				EnableCognitivActionsList();
				StartTrainingCognitiv(EmoState.EE_CognitivAction_t.COG_LEFT);
			}
			catch(Exception ex){
				ex.printStackTrace();
			}
		}
		if(index == 4)
		{
			try
			{
				EnableCognitivAction(EmoState.EE_CognitivAction_t.COG_RIGHT, true);
				EnableCognitivActionsList();
				StartTrainingCognitiv(EmoState.EE_CognitivAction_t.COG_RIGHT);
			}
			catch(Exception ex){
				ex.printStackTrace();
			}
		}
		
	}
}
