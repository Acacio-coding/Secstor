package com.ifsc.secstor.facade;

import com.at.archistar.crypto.data.InvalidParametersException;
import com.at.archistar.crypto.secretsharing.ReconstructionException;
import com.ifsc.secstor.api.dto.ReconstructDTO;
import com.ufsc.das.gcseg.pvss.exception.InvalidVSSScheme;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.sql.SQLException;

public interface Engine {
    Object split(String data) throws UnsupportedEncodingException, InvalidVSSScheme;

    String reconstruct(JSONObject secret) throws UnsupportedEncodingException, InvalidVSSScheme, ReconstructionException, InvalidParametersException;
}
