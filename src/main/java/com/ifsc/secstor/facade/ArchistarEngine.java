package com.ifsc.secstor.facade;

import com.at.archistar.crypto.CryptoEngine;
import com.at.archistar.crypto.data.*;
import com.at.archistar.crypto.secretsharing.ReconstructionException;
import com.ifsc.secstor.api.advice.exception.ValidationException;
import com.ifsc.secstor.api.model.CSSShareModel;
import com.ifsc.secstor.api.model.KrawczykShareModel;
import com.ifsc.secstor.api.model.PSSShareModel;
import com.ifsc.secstor.api.model.ShamirShareModel;
import lombok.RequiredArgsConstructor;
import org.apache.tomcat.util.codec.binary.Base64;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.http.HttpStatus;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import static com.ifsc.secstor.api.advice.messages.ErrorMessages.*;
import static com.ifsc.secstor.api.advice.paths.Paths.SECRET_SHARING_BASE_AND_RECONSTRUCT;

@RequiredArgsConstructor
public class ArchistarEngine implements Engine {
    private final CryptoEngine engine;

    @Override
    public Object split(String data) {
        Share[] shares = engine.share(data.getBytes(StandardCharsets.UTF_8));
        Object toReturn = null;

        if (engine.toString().contains("Shamir")) {
            toReturn = new ShamirShareModel(new ArrayList<>());

            for (Share share : shares) {
                ((ShamirShareModel) toReturn).getShares().add(Base64.encodeBase64String(share.getYValues()));
            }
        }

        if (engine.toString().contains("PSS")) {
            toReturn = new PSSShareModel(new ArrayList<>(), new ArrayList<>(), new ArrayList<>());

            for (Share share : shares) {
                ((PSSShareModel) toReturn).getShares().add(Base64.encodeBase64String(share.getYValues()));

                String[] auxMKeys = new String[((PSSShare) share).getMacKeys().size()];
                String[] auxMacs = new String[((PSSShare) share).getMacs().size()];

                int j = 1;
                for (int i = 0; i < shares.length; i++) {
                    auxMKeys[i] = Base64.encodeBase64String(((PSSShare) share).getMacKeys().get((byte) j));
                    auxMacs[i] = Base64.encodeBase64String(((PSSShare) share).getMacs().get((byte) j));
                    j++;
                }

                ((PSSShareModel) toReturn).getMacKeys().add(auxMKeys);
                ((PSSShareModel) toReturn).getMacs().add(auxMacs);
            }
        }

        if (engine.toString().contains("CSS")) {
            toReturn = new CSSShareModel(new ArrayList<>(), new ArrayList<>(),
                    shares[0].getOriginalLength(), ((CSSShare)shares[0]).getEncAlgorithm(), new ArrayList<>());

            int i = 1;
            for (Share share : shares) {
                ((CSSShareModel) toReturn).getShares().add(Base64.encodeBase64String(share.getYValues()));
                ((CSSShareModel) toReturn).getFingerprints().add(Base64.encodeBase64String(((CSSShare) share).getFingerprints().get((byte) i)));
                ((CSSShareModel) toReturn).getEncKeys().add(Base64.encodeBase64String(((CSSShare) share).getKey()));
                i++;
            }
        }

        if (engine.toString().contains("Krawczyk")) {
            toReturn = new KrawczykShareModel(new ArrayList<>(), shares[0].getOriginalLength(),
                    ((KrawczykShare)shares[0]).getEncAlgorithm(), new ArrayList<>());

            for (Share share : shares) {
                ((KrawczykShareModel) toReturn).getShares().add(Base64.encodeBase64String(share.getYValues()));
                ((KrawczykShareModel) toReturn).getEncKeys().add(Base64.encodeBase64String(((KrawczykShare) share).getKey()));
            }
        }

        return toReturn;
    }

    @Override
    public String reconstruct(JSONObject secret) throws ReconstructionException, InvalidParametersException {
        JSONArray auxShares = (JSONArray) secret.get("shares");

        if (auxShares.isEmpty())
            throw new ValidationException(HttpStatus.BAD_REQUEST,
                    NULL_SECRET, SECRET_SHARING_BASE_AND_RECONSTRUCT);

        if (auxShares.length() < 5)
            throw new ValidationException(HttpStatus.BAD_REQUEST,
                    NOT_ENOUGH_SHARES, SECRET_SHARING_BASE_AND_RECONSTRUCT);

        Share[] shares = new Share[auxShares.length()];

        if (engine.toString().contains("Shamir")) {
            int j = 1;
            for (int i = 0; i < auxShares.length(); i++) {
                shares[i] = new ShamirShare((byte) j, Base64.decodeBase64((String) auxShares.get(i)));
                j++;
            }
        }

        if (engine.toString().contains("PSS")) {
            JSONArray allMacKeys = (JSONArray) secret.get("macKeys");

            if (allMacKeys.isEmpty())
                throw new ValidationException(HttpStatus.BAD_REQUEST,
                        MISSING_MAC_KEYS, SECRET_SHARING_BASE_AND_RECONSTRUCT);

            JSONArray allMacs = (JSONArray) secret.get("macs");

            if (allMacs.isEmpty())
                throw new ValidationException(HttpStatus.BAD_REQUEST,
                        MISSING_MACS, SECRET_SHARING_BASE_AND_RECONSTRUCT);

            int k = 1;
            for (int i = 0; i < auxShares.length(); i++) {
                Map<Byte, byte[]> macKeys = new HashMap<>();
                Map<Byte, byte[]> macs = new HashMap<>();
                JSONArray oneMacKeyArray = (JSONArray) allMacKeys.get(i);
                JSONArray oneMacArray = (JSONArray) allMacs.get(i);

                int l = 1;
                for (int j = 0; j < auxShares.length(); j++) {
                    macKeys.put((byte) l, Base64.decodeBase64((String) oneMacKeyArray.get(j)));
                    macs.put((byte) l, Base64.decodeBase64((String) oneMacArray.get(j)));
                    l++;
                }

                shares[i] = new PSSShare((byte) k, Base64.decodeBase64((String) auxShares.get(i)), macKeys, macs);
                k++;
            }
        }

        if (engine.toString().contains("CSS")) {
            JSONArray jsonFingerPrints = (JSONArray) secret.get("fingerprints");

            if (jsonFingerPrints.isEmpty())
                throw new ValidationException(HttpStatus.BAD_REQUEST,
                        MISSING_FINGERPRINTS, SECRET_SHARING_BASE_AND_RECONSTRUCT);

            JSONArray encKeys = (JSONArray) secret.get("encKeys");
            Integer encAlgorithm = (Integer) secret.get("encAlgorithm");
            Integer originalLength = (Integer) secret.get("originalLength");

            validateInfo(encKeys, encAlgorithm, originalLength);

            Map<Byte, byte[]> fingerprints = new HashMap<>();

            int j = 1;
            for (int i = 0; i < auxShares.length(); i++) {
                fingerprints.put((byte) j, Base64.decodeBase64((String) jsonFingerPrints.get(i)));
                j++;
            }

            j = 1;
            for (int i = 0; i < auxShares.length(); i++) {
                shares[i] = new CSSShare((byte) j, Base64.decodeBase64((String) auxShares.get(i)), fingerprints,
                        originalLength, encAlgorithm, Base64.decodeBase64((String) encKeys.get(i)));
                j++;
            }
        }

        if (engine.toString().contains("Krawczyk")) {
            JSONArray encKeys = (JSONArray) secret.get("encKeys");
            Integer encAlgorithm = (Integer) secret.get("encAlgorithm");
            Integer originalLength = (Integer) secret.get("originalLength");

            validateInfo(encKeys, encAlgorithm, originalLength);

            int j = 1;
            for (int i = 0; i < auxShares.length(); i++) {
                shares[i] = new KrawczykShare((byte) j, Base64.decodeBase64((String) auxShares.get(i)),
                        originalLength, encAlgorithm, Base64.decodeBase64((String) encKeys.get(i)));
                j++;
            }
        }

        ReconstructionResult result = engine.reconstruct(shares);
        return new String(result.getData(), StandardCharsets.UTF_8);
    }

    private void validateInfo(JSONArray encKeys, Integer encAlgorithm, Integer originalLength) {
        if (encKeys.isEmpty())
            throw new ValidationException(HttpStatus.BAD_REQUEST,
                    MISSING_ENCKEYS, SECRET_SHARING_BASE_AND_RECONSTRUCT);

        if (encAlgorithm == null)
            throw new ValidationException(HttpStatus.BAD_REQUEST,
                    MISSING_ENCALGORITHM, SECRET_SHARING_BASE_AND_RECONSTRUCT);

        if (originalLength == null)
            throw new ValidationException(HttpStatus.BAD_REQUEST,
                    MISSING_ORIGINALLENGTH, SECRET_SHARING_BASE_AND_RECONSTRUCT);
    }
}

