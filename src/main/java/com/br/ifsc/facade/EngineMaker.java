package com.br.ifsc.facade;

import com.at.archistar.crypto.CSSEngine;
import com.at.archistar.crypto.KrawczykEngine;
import com.at.archistar.crypto.PSSEngine;
import com.at.archistar.crypto.ShamirEngine;
import com.at.archistar.crypto.secretsharing.ReconstructionException;
import com.at.archistar.crypto.secretsharing.WeakSecurityException;
import com.br.ufsc.das.gcseg.pvss.exception.InvalidVSSScheme;
import com.br.ufsc.das.gcseg.secretsharing.SecretShareEngine;

import java.io.UnsupportedEncodingException;
import java.security.NoSuchAlgorithmException;
import java.util.Map;

public class EngineMaker {
    private final Engine shamir;
    private final Engine pss;
    private final Engine css;
    private final Engine krawczyk;
    private final Engine pvss;

    public EngineMaker(int n, int k) throws WeakSecurityException, NoSuchAlgorithmException {
        this.shamir = new ArchistarEngine(new ShamirEngine(n, k));
        this.pss = new ArchistarEngine(new PSSEngine(n, k));
        this.css = new ArchistarEngine(new CSSEngine(n, k));
        this.krawczyk = new ArchistarEngine(new KrawczykEngine(n, k));
        this.pvss = new PVSSEngine(new SecretShareEngine(n, k));
    }

    public void split(Map<String, String> data, String algorithm) throws UnsupportedEncodingException, InvalidVSSScheme {
        if (algorithm.equalsIgnoreCase("shamir"))
            this.shamir.split(data);

        if (algorithm.equalsIgnoreCase("pss"))
            this.pss.split(data);

        if (algorithm.equalsIgnoreCase("css"))
            this.css.split(data);

        if (algorithm.equalsIgnoreCase("krawczyk"))
            this.krawczyk.split(data);

        if (algorithm.equalsIgnoreCase("pvss"))
            this.pvss.split(data);
    }

    public String reconstruct(String algorithm) throws UnsupportedEncodingException, ReconstructionException, InvalidVSSScheme, NotSplittedException {
        if (algorithm.equalsIgnoreCase("shamir") && !this.shamir.getPieces().isEmpty())
            return this.shamir.reconstruct();

        if (algorithm.equalsIgnoreCase("pss") && !this.pss.getPieces().isEmpty())
            return this.pss.reconstruct();

        if (algorithm.equalsIgnoreCase("css") && !this.css.getPieces().isEmpty())
            return this.css.reconstruct();

        if (algorithm.equalsIgnoreCase("krawczyk") && !this.krawczyk.getPieces().isEmpty())
            return this.krawczyk.reconstruct();

        if (algorithm.equalsIgnoreCase("pvss") && !this.pvss.getPieces().isEmpty())
            return this.krawczyk.reconstruct();

        throw new NotSplittedException("You need to split the shares in order to reconstruct them!");
    }
}
