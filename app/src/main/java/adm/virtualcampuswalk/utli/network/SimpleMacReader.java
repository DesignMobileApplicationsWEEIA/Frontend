package adm.virtualcampuswalk.utli.network;

import android.util.Log;

import java.net.NetworkInterface;
import java.util.Collections;
import java.util.List;

import static adm.virtualcampuswalk.utli.Util.TAG;

/**
 * Created by mariusz on 26.11.16.
 */

public class SimpleMacReader implements MacReader {

    public static final String WLAN_INTERFACE = "wlan0";
    public static final String DEFAULT_MAC_ADDRESS = "02:00:00:00:00:00";
    private static final String GET_MAC_ADDRESS_DEFAULT_MAC_ADDRESS = "getMacAddress: Returning default mac address!";

    @Override
    public String getMacAddress() {
        try {
            List<NetworkInterface> networkInterfaceList = Collections.list(NetworkInterface.getNetworkInterfaces());
            for (NetworkInterface currentNetworkInterface : networkInterfaceList) {
                if (!currentNetworkInterface.getName().equalsIgnoreCase(WLAN_INTERFACE)) continue;

                byte[] macBytes = currentNetworkInterface.getHardwareAddress();
                if (macBytes == null) {
                    Log.w(TAG, GET_MAC_ADDRESS_DEFAULT_MAC_ADDRESS);
                    return DEFAULT_MAC_ADDRESS;
                }

                StringBuilder result = new StringBuilder();
                for (byte currentByte : macBytes) {
                    result.append(Integer.toHexString(currentByte & 0xFF)).append(":");
                }

                if (result.length() > 0) {
                    result.deleteCharAt(result.length() - 1);
                }
                return result.toString();
            }
        } catch (Exception ex) {
            Log.e(TAG, "getMacAddress: " + ex.getMessage(), ex);
        }
        Log.w(TAG, GET_MAC_ADDRESS_DEFAULT_MAC_ADDRESS);
        return DEFAULT_MAC_ADDRESS;
    }
}
