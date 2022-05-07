package com.ifsc.secstor.facade;

import java.io.UnsupportedEncodingException;

import com.at.archistar.crypto.data.InvalidParametersException;
import com.at.archistar.crypto.secretsharing.ReconstructionException;
import com.ufsc.das.gcseg.pvss.exception.InvalidVSSScheme;

import org.json.JSONObject;

public interface Engine {
    Object split(String data) throws UnsupportedEncodingException, InvalidVSSScheme;

    String reconstruct(JSONObject secret) throws UnsupportedEncodingException, InvalidVSSScheme, ReconstructionException, InvalidParametersException;
}
