package com.br.ifsc.facade;

import com.at.archistar.crypto.secretsharing.ReconstructionException;
import com.br.ufsc.das.gcseg.pvss.exception.InvalidVSSScheme;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Map;

public interface Engine {
    public void split(Map<String, String> data) throws UnsupportedEncodingException, InvalidVSSScheme;

    public String reconstruct() throws UnsupportedEncodingException, InvalidVSSScheme, ReconstructionException;

    public ArrayList<String> getPieces();
}
