package com.word.generate;

import java.io.BufferedReader;
import java.io.File;
import java.io.InputStreamReader;
import java.util.ArrayList;

import com.word.generate.ExecShell.SHELL_CMD;

import android.util.Log;

/** @author Kevin Kowalewski */
public class Root {

    public boolean isDeviceRooted() {
        return checkRootMethod1() || checkRootMethod2() || checkRootMethod3();
    }

    public boolean checkRootMethod1() {
        String buildTags = android.os.Build.TAGS;
        Log.d("test", buildTags);
        return buildTags != null && buildTags.contains("test-keys");
    }

    public boolean checkRootMethod2() {
        try {
            File file = new File("/system/app/Superuser.apk");
            return file.exists();
        } catch (Exception e){
        	return false;
        }
    }

    public boolean checkRootMethod3() {
        return new ExecShell().executeCommand(SHELL_CMD.check_su_binary)!=null;
    }
}   

/** @author Kevin Kowalewski */
class ExecShell {

    private static String LOG_TAG = ExecShell.class.getName();

    public static enum SHELL_CMD {
        check_su_binary(new String[] { "/system/xbin/which", "su" });

        String[] command;

        SHELL_CMD(String[] command) {
            this.command = command;
        }
    }

    public ArrayList<String> executeCommand(SHELL_CMD shellCmd) {
        String line = null;
        ArrayList<String> fullResponse = new ArrayList<String>();
        Process localProcess = null;
        try {
            localProcess = Runtime.getRuntime().exec(shellCmd.command);
        } catch (Exception e) {
            return null;
        }
        BufferedReader in = new BufferedReader(new InputStreamReader(
                localProcess.getInputStream()));
        try {
            while ((line = in.readLine()) != null) {
                Log.d(LOG_TAG, "--> Line received: " + line);
                fullResponse.add(line);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        Log.d(LOG_TAG, "--> Full response was: " + fullResponse);
        return fullResponse;
    }
}