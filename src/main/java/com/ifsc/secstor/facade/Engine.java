package com.ifsc.secstor.facade;

import com.at.archistar.crypto.secretsharing.ReconstructionException;
import com.ufsc.das.gcseg.pvss.exception.InvalidVSSScheme;

import java.io.UnsupportedEncodingException;

public interface Engine {
    Object split(String data) throws UnsupportedEncodingException, InvalidVSSScheme;
    String reconstruct(Object requestDTO, boolean doYourBest) throws UnsupportedEncodingException,
            InvalidVSSScheme, ReconstructionException;
}
