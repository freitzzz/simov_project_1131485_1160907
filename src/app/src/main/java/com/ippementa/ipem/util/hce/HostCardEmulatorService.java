package com.ippementa.ipem.util.hce;

import android.nfc.cardemulation.HostApduService;
import android.os.Bundle;
import android.util.Log;

public class HostCardEmulatorService extends HostApduService {

    //CONSTANTS
    public static String  TAG = "Host Card Emulator";
    public static String  STATUS_SUCCESS = "9000";
    public static String STATUS_FAILED = "6F00";
    public static String CLA_NOT_SUPPORTED = "6E00";
    public static String INS_NOT_SUPPORTED = "6D00";
    public static String AID = "A0000002471001";
    public static String SELECT_INS = "A4";
    public static String DEFAULT_CLA = "00";
    public static int MIN_APDU_LENGTH = 12;

    @Override
    public byte[] processCommandApdu(byte[] bytes, Bundle bundle) {


        byte[] commandApdu = bytes;

        if (commandApdu == null) {
            return STATUS_FAILED.getBytes();

        }

        String hexCommandApdu = commandApdu.toString();
        if (hexCommandApdu.length() < MIN_APDU_LENGTH) {
            return STATUS_FAILED.getBytes();
        }

        if (hexCommandApdu.substring(0, 2) != DEFAULT_CLA) {
            return CLA_NOT_SUPPORTED.getBytes();
        }

        if (hexCommandApdu.substring(2, 4) != SELECT_INS) {
            return INS_NOT_SUPPORTED.getBytes();
        }

        if (hexCommandApdu.substring(10, 24) == AID)  {
            return STATUS_SUCCESS.getBytes();
        } else {
            return STATUS_FAILED.getBytes();
        }
    }

    @Override
    public void onDeactivated(int reason) { // method will be called when the a different AID has been selected or the NFC connection has been lost.

        Log.d(TAG, "Deactivated: " + reason);
    }
}