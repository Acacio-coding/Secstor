package com.ifsc.secstor.facade;

import com.at.archistar.crypto.data.Share;

public class ArchistarShare {
    private final Share[] shares;

    public ArchistarShare(Share[] shares) {
        this.shares = shares;
    }

    public Share[] getShares() {
        return shares;
    }
}
