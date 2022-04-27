package com.ufsc.das.gcseg.secretsharing;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;

public class SplitedShares {
	private List<String> shareString;
	private BigInteger modulus;
	private String Key;
	private final String algorithm;

	public SplitedShares() {
		super();
		shareString = new ArrayList<>();
		this.algorithm = "PVSS";
	}

	public List<String> getShareString() {
		return shareString;
	}

	public void setShareString(List<String> shareString) {
		this.shareString = shareString;
	}
	
	public void addShare(String share){
		shareString.add(share);
	}

	public BigInteger getModulus() {
		return modulus;
	}

	public void setModulus(BigInteger modulus) {
		this.modulus = modulus;
	}

	public String getKey() {
		return Key;
	}

	public void setKey(String key) {
		Key = key;
	}

	public String getAlgorithm() {
		return algorithm;
	}
}
