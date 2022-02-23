package com.br.ifsc;

import com.at.archistar.crypto.secretsharing.ReconstructionException;
import com.at.archistar.crypto.secretsharing.WeakSecurityException;
import com.br.ifsc.facade.EngineMaker;
import com.br.ifsc.facade.NotSplittedException;
import com.br.ufsc.das.gcseg.pvss.exception.InvalidVSSScheme;
import org.apache.commons.lang3.RandomStringUtils;

import java.io.FileWriter;
import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

public class ProfilingExample {
    public static void main(String[] args) throws IOException, WeakSecurityException, NoSuchAlgorithmException, InvalidVSSScheme, NotSplittedException, ReconstructionException {
        Scanner scanner = new Scanner(System.in);
        int op = 0;

        FileWriter writer = new FileWriter("timings2.txt");
        Map<String, String> data = new HashMap<>();

        data.put("1",  RandomStringUtils.random(90000000, true, true));


        while(op != 1) {
            System.out.println("\n1 - Sair");
            System.out.println("2 - Share secret");
            System.out.println("Option:");
            op = scanner.nextInt();

            if (op == 2)
                getTimings(data, writer, new EngineMaker(10, 5));
        }

        writer.close();
    }

    public static void getTimings(Map<String, String> data, FileWriter writer, EngineMaker engineMaker) throws IOException, InvalidVSSScheme, NotSplittedException, ReconstructionException {
        long start;
        long end;

//        //Shamir ------------------------------------------------------------------------
//        System.out.println("\n------------ Shamir");
//        writer.write("Shamir algorithm:");
//
//        //Split
//        start = System.nanoTime();
//        engineMaker.split(data, "shamir");
//        end = System.nanoTime() - start;
//
//        writer.write("\n- Share generation time: " + end / 1e+6 + "ms");
//
//        //Reconstruction
//        start = System.nanoTime();
//        engineMaker.reconstruct("shamir");
//        end = System.nanoTime() - start;
//
//        writer.write("\n- Share reconstruction time: " + end / 1e+6 + "ms\n\n");
//        System.out.println("Done!");


//        //PSS ---------------------------------------------------------------------------
//        System.out.println("\n------------ PSS");
//        writer.write("PSS algorithm:");
//
//        //Split
//        start = System.nanoTime();
//        engineMaker.split(data, "pss");
//        end = System.nanoTime() - start;
//
//        writer.write("\n- Share generation time: " + end / 1e+6 + "ms");
//
//        //Reconstruction
//        start = System.nanoTime();
//        engineMaker.reconstruct("pss");
//        end = System.nanoTime() - start;
//
//        writer.write("\n- Share reconstruction time: " + end / 1e+6 + "ms\n\n");
//        System.out.println("Done!");


//        //CSS ---------------------------------------------------------------------------
//        System.out.println("\n------------ CSS");
//        writer.write("CSS algorithm:");
//
//        //Split
//        start = System.nanoTime();
//        engineMaker.split(data, "css");
//        end = System.nanoTime() - start;
//
//        writer.write("\n- Share generation time: " + end / 1e+6 + "ms");
//
//        //Reconstruction
//        start = System.nanoTime();
//        engineMaker.reconstruct("css");
//        end = System.nanoTime() - start;
//
//        writer.write("\n- Share reconstruction time: " + end / 1e+6 + "ms\n\n");
//        System.out.println("Done!");

//
        //Krawczyk ----------------------------------------------------------------------
        System.out.println("\n------------ Krawczyk");
        writer.write("Krawczyk algorithm:");

        //Split
        start = System.nanoTime();
        engineMaker.split(data, "krawczyk");
        end = System.nanoTime() - start;

        writer.write("\n- Share generation time: " + end / 1e+6 + "ms");

        //Reconstruction
        start = System.nanoTime();
        engineMaker.reconstruct("krawczyk");
        end = System.nanoTime() - start;

        writer.write("\n- Share reconstruction time: " + end / 1e+6 + "ms\n\n");
        System.out.println("Done!");

//
//        //PVSS --------------------------------------------------------------------------
//        System.out.println("\n------------ PVSS");
//        writer.write("PVSS algorithm:");
//
//        //Split
//        start = System.nanoTime();
//        engineMaker.split(data, "pvss");
//        end = System.nanoTime() - start;
//
//        writer.write("\n- Share generation time: " + end / 1e+6 + "ms");
//
//        //Reconstruction
//        start = System.nanoTime();
//        engineMaker.reconstruct("pvss");
//        end = System.nanoTime() - start;
//
//        writer.write("\n- Share reconstruction time: " + end / 1e+6 + "ms\n\n");
//        System.out.println("Done!");
    }
}
