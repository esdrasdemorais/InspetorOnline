package br.com.octabus.Util;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

public class SessionManager
{
    // Shared Preferences
    SharedPreferences pref;

    // Editor for Shared preferences
    Editor editor;

    // Context
    Context _context;

    // Shared pref mode
    int PRIVATE_MODE = 0;

    // Sharedpref file name
    private static final String PREF_NAME = "AppFiscalOnibus";

    // All Shared Preferences Keys
    private static final String IS_LOGIN = "IsLoggedIn";
    private static final String KEY_NOME = "nome";
    private static final String KEY_CARGO = "cargo";
    private static final String KEY_ID_GRUPO = "idGrupo";
    private static final String KEY_GRUPO = "grupo";
    private static final String KEY_LOGIN = "re";
    private static final String KEY_IDFUNCIONARIO = "idFuncionario";
    private static final String KEY_ID = "idLogin";
    private static final String KEY_MENU_PERMITIDO = "menuPermitido";
    private static final String KEY_LOCAL_CONFIRMADO = "localConfirmado";
    private static final String KEY_DATALOGIN = "dataLogin";

    // Constructor
    public SessionManager(Context context){
        this._context = context;
        pref = _context.getSharedPreferences(PREF_NAME, PRIVATE_MODE);
        editor = pref.edit();
    }

    /**
     * Create login session
     * */
    public void createLoginSession(int idLogin, String login, int idFuncionario, String nomeFuncionario,
                                   int idGrupo, String nomeGrupo, ArrayList menuPermitido, String dataLogin)
    {
        editor.putBoolean(IS_LOGIN, true);
        editor.putString(KEY_NOME, nomeFuncionario);
        editor.putInt(KEY_ID_GRUPO, idGrupo);
        editor.putString(KEY_GRUPO, nomeGrupo);
        editor.putString(KEY_LOGIN, login);
        editor.putInt(KEY_ID, idLogin);
        editor.putInt(KEY_IDFUNCIONARIO, idFuncionario);
        editor.putBoolean(KEY_LOCAL_CONFIRMADO, false);
        editor.putString(KEY_DATALOGIN, dataLogin);


        Set<String> set = new HashSet<String>();
        set.addAll(menuPermitido);
        editor.putStringSet(KEY_MENU_PERMITIDO, set);

        editor.commit();
    }


    /**
     * Get stored session data
     * */
    public HashMap<Object, Object> getUserDetails(){
        HashMap<Object, Object> user = new HashMap<Object, Object>();

        user.put(KEY_NOME, pref.getString(KEY_NOME, null));
        user.put(KEY_ID_GRUPO, pref.getInt(KEY_ID_GRUPO, 0));
        user.put(KEY_GRUPO, pref.getString(KEY_GRUPO, null));
        user.put(KEY_LOGIN, pref.getString(KEY_LOGIN, null));
        user.put(KEY_ID, pref.getInt(KEY_ID, 0));
        user.put(KEY_MENU_PERMITIDO, pref.getStringSet(KEY_MENU_PERMITIDO, null));
        user.put(KEY_LOCAL_CONFIRMADO, pref.getBoolean(KEY_LOCAL_CONFIRMADO, false));
        user.put(KEY_IDFUNCIONARIO, pref.getInt(KEY_IDFUNCIONARIO, 0));
        user.put(KEY_DATALOGIN, pref.getString(KEY_DATALOGIN, null));

        // return user
        return user;
    }

    /**
     * Clear session details
     * */
    public void logoutUser()
    {
        editor.clear();
        editor.commit();
    }

    // Get Login State
    public boolean isLoggedIn(){
        return pref.getBoolean(IS_LOGIN, false);
    }

    public void confirmarProgramacao()
    {
        editor.putBoolean(KEY_LOCAL_CONFIRMADO, true);
        editor.commit();
    }
}
