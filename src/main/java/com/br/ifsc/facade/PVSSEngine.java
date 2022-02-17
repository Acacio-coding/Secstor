package com.br.ifsc.facade;

import com.at.archistar.crypto.secretsharing.ReconstructionException;
import com.br.ifsc.facade.Engine;
import com.br.ufsc.das.gcseg.pvss.exception.InvalidVSSScheme;
import com.br.ufsc.das.gcseg.secretsharing.SecretShareEngine;
import com.br.ufsc.das.gcseg.secretsharing.SharestoCombine;
import com.br.ufsc.das.gcseg.secretsharing.SplitedShares;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Map;

public class PVSSEngine implements Engine {
    private final SecretShareEngine engine;
    private SplitedShares shares;
    private final SharestoCombine sharestoCombine;

    public PVSSEngine(SecretShareEngine engine) {
        this.engine = engine;
        this.shares = new SplitedShares();
        this.sharestoCombine = new SharestoCombine();
    }

    public String getEngine() {
        return engine.toString();
    }

    @Override
    public void split(Map<String, String> data) throws UnsupportedEncodingException, InvalidVSSScheme {
        this.shares = engine.split(data.toString());
    }

    @Override
    public String reconstruct() throws UnsupportedEncodingException, InvalidVSSScheme {
        this.sharestoCombine.setModulus(shares.getModulus());
        this.sharestoCombine.setKey(shares.getKey());
        return engine.combine(this.sharestoCombine);
    }

    @Override
    public ArrayList<String> getPieces() {
        return new ArrayList<>(this.shares.getShareString());
    }
}
