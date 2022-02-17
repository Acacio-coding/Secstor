package com.br.ifsc;

import com.at.archistar.crypto.secretsharing.ReconstructionException;
import com.at.archistar.crypto.secretsharing.WeakSecurityException;
import com.br.ifsc.facade.EngineMaker;
import com.br.ifsc.facade.NotSplittedException;
import com.br.ufsc.das.gcseg.pvss.exception.InvalidVSSScheme;

import java.io.FileWriter;
import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.util.HashMap;
import java.util.Map;

public class TimingExample {
    public static void main(String[] args) throws IOException, WeakSecurityException, NoSuchAlgorithmException, InvalidVSSScheme, NotSplittedException, ReconstructionException {
        FileWriter writer = new FileWriter("timings.txt");
        Map<String, String> data = new HashMap<>();

        data.put("Country",  "Brasil");
        data.put("Password", "Secret");
        data.put("Address", "Av. Central");
        data.put("Role", "Administrador");
        data.put("City", "Canoinhas");
        data.put("Date of Birth", "28/02/1984");
        data.put("Postalcode", "89460000");
        data.put("First name", "Luciano");
        data.put("Last name", "Barreto");
        data.put("Email", "lucianobarreto@gmail.com");

        getTimings(data, writer, new EngineMaker(10, 5));

        writer.close();
    }

    public static void getTimings(Map<String, String> data, FileWriter writer, EngineMaker engineMaker) throws IOException, InvalidVSSScheme, NotSplittedException, ReconstructionException {
        long start;
        long end;

        //Shamir ------------------------------------------------------------------------
        System.out.println("\n------------ Shamir");
        writer.write("Shamir algorithm:");

        //Split
        start = System.nanoTime();
        engineMaker.split(data, "shamir");
        end = System.nanoTime() - start;

        writer.write("\n- Share generation time: " + end / 1e+6 + "ms");

        //Reconstruction
        System.out.println("\nShare reconstructed:");
        start = System.nanoTime();
        System.out.println(engineMaker.reconstruct("shamir"));
        end = System.nanoTime() - start;

        writer.write("\n- Share reconstruction time: " + end / 1e+6 + "ms\n\n");


        //PSS ---------------------------------------------------------------------------
        System.out.println("\n------------ PSS");
        writer.write("PSS algorithm:");

        //Split
        start = System.nanoTime();
        engineMaker.split(data, "pss");
        end = System.nanoTime() - start;

        writer.write("\n- Share generation time: " + end / 1e+6 + "ms");

        //Reconstruction
        System.out.println("\nShare reconstructed:");
        start = System.nanoTime();
        System.out.println(engineMaker.reconstruct("pss"));
        end = System.nanoTime() - start;

        writer.write("\n- Share reconstruction time: " + end / 1e+6 + "ms\n\n");


        //CSS ---------------------------------------------------------------------------
        System.out.println("\n------------ CSS");
        writer.write("CSS algorithm:");

        //Split
        start = System.nanoTime();
        engineMaker.split(data, "css");
        end = System.nanoTime() - start;

        writer.write("\n- Share generation time: " + end / 1e+6 + "ms");

        //Reconstruction
        System.out.println("\nShare reconstructed:");
        start = System.nanoTime();
        System.out.println(engineMaker.reconstruct("css"));
        end = System.nanoTime() - start;

        writer.write("\n- Share reconstruction time: " + end / 1e+6 + "ms\n\n");


        //Krawczyk ----------------------------------------------------------------------
        System.out.println("\n------------ Krawczyk");
        writer.write("Krawczyk algorithm:");

        //Split
        start = System.nanoTime();
        engineMaker.split(data, "krawczyk");
        end = System.nanoTime() - start;

        writer.write("\n- Share generation time: " + end / 1e+6 + "ms");

        //Reconstruction
        System.out.println("\nShare reconstructed:");
        start = System.nanoTime();
        System.out.println(engineMaker.reconstruct("krawczyk"));
        end = System.nanoTime() - start;

        writer.write("\n- Share reconstruction time: " + end / 1e+6 + "ms\n\n");


        //PVSS --------------------------------------------------------------------------
        System.out.println("\n------------ PVSS");
        writer.write("PVSS algorithm:");

        //Split
        start = System.nanoTime();
        engineMaker.split(data, "pvss");
        end = System.nanoTime() - start;

        writer.write("\n- Share generation time: " + end / 1e+6 + "ms");

        //Reconstruction
        System.out.println("\nShare reconstructed:");
        start = System.nanoTime();
        System.out.println(engineMaker.reconstruct("pvss"));
        end = System.nanoTime() - start;

        writer.write("\n- Share reconstruction time: " + end / 1e+6 + "ms\n\n");
    }
}
