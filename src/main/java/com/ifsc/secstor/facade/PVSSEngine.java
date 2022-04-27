package com.ifsc.secstor.facade;

import com.ifsc.secstor.api.advice.exception.ValidationException;
import com.ifsc.secstor.api.model.PVSSShareModel;
import com.ufsc.das.gcseg.pvss.exception.InvalidVSSScheme;
import com.ufsc.das.gcseg.secretsharing.SecretShareEngine;
import com.ufsc.das.gcseg.secretsharing.SharestoCombine;
import com.ufsc.das.gcseg.secretsharing.SplitedShares;
import lombok.RequiredArgsConstructor;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.http.HttpStatus;

import java.io.UnsupportedEncodingException;
import java.math.BigInteger;

@RequiredArgsConstructor
public class PVSSEngine implements Engine {
    private final SecretShareEngine engine;

    @Override
    public Object split(String data) throws UnsupportedEncodingException, InvalidVSSScheme {
        SplitedShares splitedShares = engine.split(data);
        return new PVSSShareModel(splitedShares.getShareString(), splitedShares.getKey(),
                splitedShares.getModulus());
    }

    @Override
    public String reconstruct(JSONObject secret) throws UnsupportedEncodingException, InvalidVSSScheme {
        JSONArray shares = (JSONArray) secret.get("shares");

        if (shares.isEmpty())
            throw new ValidationException(HttpStatus.BAD_REQUEST,
                    "No secret provided to be reconstructed",
                    "api/v1/secret-sharing/reconstruct");

        if (shares.length() < 5)
            throw new ValidationException(HttpStatus.BAD_REQUEST,
                    "Not enough shares to reconstruct the secret, it must be at least 5",
                    "api/v1/secret-sharing/reconstruct");

        String key = (String) secret.get("key");

        if (key.isBlank())
            throw new ValidationException(HttpStatus.BAD_REQUEST,
                    "There was a missing parameter: KEY",
                    "api/v1/secret-sharing/reconstruct");

        BigInteger modulus = (BigInteger) secret.get("modulus");

        if (modulus == null)
            throw new ValidationException(HttpStatus.BAD_REQUEST,
                    "There was a missing parameter: MODULUS",
                    "api/v1/secret-sharing/reconstruct");

        SharestoCombine sharestoCombine = new SharestoCombine();
        sharestoCombine.setKey(key);
        sharestoCombine.setModulus(modulus);

        shares.forEach(share -> sharestoCombine.addShare((String) share));

        return this.engine.combine(sharestoCombine);
    }
}