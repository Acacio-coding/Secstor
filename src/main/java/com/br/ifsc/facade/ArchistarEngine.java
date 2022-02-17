package com.br.ifsc.facade;

import com.at.archistar.crypto.CryptoEngine;
import com.at.archistar.crypto.data.ReconstructionResult;
import com.at.archistar.crypto.data.Share;
import com.at.archistar.crypto.secretsharing.ReconstructionException;
import com.br.ifsc.facade.Engine;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Map;

public class ArchistarEngine implements Engine {
    private final CryptoEngine engine;
    private Share[] shares;
    private ReconstructionResult result;

    public ArchistarEngine(CryptoEngine engine) {
        this.engine = engine;
    }

    public String getEngine() {
        return engine.toString();
    }

    @Override
    public void split(Map<String, String> data) {
        this.shares = engine.share(data.toString().getBytes(StandardCharsets.UTF_8));
    }

    @Override
    public String reconstruct() throws ReconstructionException {
        this.result = engine.reconstruct(this.shares);
        return new String(result.getData(), StandardCharsets.UTF_8);
    }

    @Override
    public ArrayList<String> getPieces() {
        ArrayList<String> toReturn = new ArrayList<>();

        for (Share share : this.shares) {
            toReturn.add(String.valueOf(share.hashCode()));
        }

        return toReturn;
    }

}
