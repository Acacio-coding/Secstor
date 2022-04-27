package com.ufsc.das.gcseg.secretsharing;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;

public class SharestoCombine {

	private List<String> shareString;
	private BigInteger modulus;
	private String Key;

	public SharestoCombine(List<String> shareString, BigInteger modulus, String key) {
		this.shareString = shareString;
		this.modulus = modulus;
		this.Key = key;
	}

    public SharestoCombine() {
		this.shareString = new ArrayList<>();
    }

	/**
	* Retur the list of shares given a secret
	*
	* @return list of parts in string format
	* 	*/	
	public List<String> getShareString() {
		return shareString;
	}

	/**
	* Create a list of string containg secret parts
	* 	*/	
	public void setShareString(List<String> shareString) {
		this.shareString = shareString;
	}
	
	/**
	* Add a share to the list
	*
	* @param share the ith share in string
	* 	*/		
	public void addShare(String share){
		shareString.add(share);
	}

	/**
	* Return de Modulus used in the SS Scheme. The modulus need to be as big as the secret
	*
	* @return BigInteger modulos for the SS Scheme
	* 	*/		
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

}
