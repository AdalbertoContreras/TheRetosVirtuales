package org.metatrans.apps.gravity.app;


import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.SetOptions;

import org.metatrans.apps.gravity.achievements.AchievementsManager_Gravity;
import org.metatrans.apps.gravity.cfg.world.ConfigurationUtils_Level;
import org.metatrans.apps.gravity.events.EventsManager_Gravity;
import org.metatrans.apps.gravity.main.Activity_Result;
import org.metatrans.apps.gravity.model.BitmapCache_Gravity;
import org.metatrans.apps.gravity.model.GameData_Gravity;
import org.metatrans.apps.gravity.model.UserSettings_Gravity;
import org.metatrans.apps.gravity.model.WorldGenerator_Gravity;
import org.metatrans.commons.achievements.IAchievementsManager;
import org.metatrans.commons.app.Application_Base;
import org.metatrans.commons.cfg.IConfigurationEntry;
import org.metatrans.commons.cfg.colours.ConfigurationUtils_Colours;
import org.metatrans.commons.cfg.menu.ConfigurationUtils_Base_MenuMain;
import org.metatrans.commons.engagement.ILeaderboardsProvider;
import org.metatrans.commons.engagement.leaderboards.LeaderboardsProvider_Base;
import org.metatrans.commons.events.api.IEventsManager;
import org.metatrans.commons.graphics2d.app.Application_2D_Base;
import org.metatrans.commons.graphics2d.model.GameData;
import org.metatrans.commons.graphics2d.model.IWorld;
import org.metatrans.commons.model.BitmapCache_Base;
import org.metatrans.commons.model.GameData_Base;
import org.metatrans.commons.model.I2DBitmapCache;
import org.metatrans.commons.model.UserSettings_Base;
import org.metatrans.commons.ui.utils.DebugUtils;

import java.util.HashMap;
import java.util.Map;

public abstract class Application_Gravity extends Application_2D_Base {
	private static final String COLLECTION_PUNTAJE = "puntaje";

	@Override
	public void onCreate() {

		ConfigurationUtils_Level.createInstance(this);

		super.onCreate();
		//Called when the application is starting, before any other application objects have been created.
		
		System.out.println("Application_EC: onCreate called " + System.currentTimeMillis());
		
		ConfigurationUtils_Colours.getAll();
		
		ConfigurationUtils_Base_MenuMain.createInstance();

		BitmapCache_Base.STATIC = new BitmapCache_Gravity(I2DBitmapCache.BITMAP_ID_STATIC);

		BitmapCache_Base.STATIC.initBitmaps();

		IConfigurationEntry levelUno = ConfigurationUtils_Level.getInstance().levelUno();
		if (levelUno != null) {
			getUserSettings().modeID = levelUno.getID();
			Application_Base.getInstance().storeUserSettings();
		}
	}

	
	@Override
	public void setNextLevel() {
		getUserSettings().modeID = ConfigurationUtils_Level.getInstance().getNextConfigID(getUserSettings().modeID);
		Application_Base.getInstance().storeUserSettings();
		if (getUserSettings().modeID ==3) {
			FirebaseAuth mAuth = FirebaseAuth.getInstance();
			FirebaseUser currentUser = mAuth.getCurrentUser();
			if (currentUser != null) {
				GameData data = getGameData();
				registrarPuntaje(currentUser.getUid(), getGameData().total_count_steps);
			} else {
				GameData data = getGameData();
				registrarPuntaje("none", getGameData().count_bullets);
			}
		}
		System.out.println("Next level: " + getUserSettings().modeID);
	}

	public static void registrarPuntaje(String userId, int puntaje) {
		// Obtén la instancia de Firestore
		FirebaseFirestore db = FirebaseFirestore.getInstance();

		// Crea un mapa con los datos que deseas almacenar
		Map<String, Object> puntajeData = new HashMap<>();
		puntajeData.put("userId", userId);
		puntajeData.put("puntaje", puntaje);

		// Genera una referencia única para el documento
		DocumentReference puntajeRef = db.collection(COLLECTION_PUNTAJE).document();

		// Agrega los datos a Firestore
		puntajeRef.set(puntajeData, SetOptions.merge())
				.addOnCompleteListener(new OnCompleteListener<Void>() {
					@Override
					public void onComplete(Task<Void> task) {
						if (task.isSuccessful()) {
							// La operación se completó con éxito
							// Puedes realizar acciones adicionales aquí si es necesario
							//Application_Base.getInstance().getCurrentActivity().finish();
							getInstance().getCurrentActivity().onBackPressed();
						} else {
							// Ocurrió un error al escribir en Firestore
							Exception e = task.getException();
							if (e != null) {
								e.printStackTrace();
							}
						}
					}
				});
	}
	
	
	@Override
	public IWorld createNewWorld() {

		return WorldGenerator_Gravity.generate(this,
				ConfigurationUtils_Level.getInstance().getConfigByID(Application_Base.getInstance().getUserSettings().modeID)
		);
	}
	
	
	@Override
	public GameData_Base createGameDataObject() {
		
		System.out.println("GAMEDATA CREATE");
		
		GameData_Gravity result = new GameData_Gravity();
		
		int levelID = getUserSettings().modeID;
		result.world = WorldGenerator_Gravity.generate(this, ConfigurationUtils_Level.getInstance().getConfigByID(levelID));
		
		result.timestamp_lastborn = System.currentTimeMillis();
		
		return result;
	}
	
	
	@Override
	protected UserSettings_Base createUserSettingsObject() {
		return new UserSettings_Gravity();
	}
	
	
	@Override
	protected ILeaderboardsProvider createLeaderboardsProvider() {
		return new LeaderboardsProvider_Base(this, Activity_Result.class);
	}
	
	
	@Override
	protected IEventsManager createEventsManager() {

		return new EventsManager_Gravity(getExecutor(), getAchievementsManager());
	}
	
	
	@Override
	protected IAchievementsManager createAchievementsManager() {
		return new AchievementsManager_Gravity(this);
	}

	
	@Override
	public boolean isTestMode() {
		boolean productiveMode = !DebugUtils.isDebuggable(this);
		return !productiveMode;
	}


}
