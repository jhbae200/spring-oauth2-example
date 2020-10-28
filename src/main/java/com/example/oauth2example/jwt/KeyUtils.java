package com.example.oauth2example.jwt;

import org.bouncycastle.util.io.pem.PemObject;
import org.bouncycastle.util.io.pem.PemReader;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;
import java.security.*;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.KeySpec;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;

public class KeyUtils {
    public static KeyPair readKeyPair(File publicKeyFile, File privateKeyFile) throws IOException, NoSuchAlgorithmException, InvalidKeySpecException {
        return new KeyPair(readPublicKey(publicKeyFile, "RSA"), readPrivateKey(privateKeyFile, "RSA"));
    }

    public static PrivateKey readPrivateKey(File privateKeyFile, String algorithm) throws IOException, InvalidKeySpecException, NoSuchAlgorithmException {
        return readPrivateKey(new FileReader(privateKeyFile), algorithm);
    }

    public static PublicKey readPublicKey(File publicKeyFile, String algorithm) throws IOException, InvalidKeySpecException, NoSuchAlgorithmException {
        return readPublicKey(new FileReader(publicKeyFile), algorithm);
    }

    public static PrivateKey readPrivateKey(Reader reader, String algorithm) throws IOException, InvalidKeySpecException, NoSuchAlgorithmException {
        if (!"RSA".equals(algorithm) && !"EC".equals(algorithm)) {
            throw new IllegalArgumentException("only support RSA, sEC algorithm.");
        }
        KeyFactory keyFactory = KeyFactory.getInstance(algorithm);
        PemReader pemReader = new PemReader(reader);
        PemObject pemObject = pemReader.readPemObject();
        KeySpec keySpec = new PKCS8EncodedKeySpec(pemObject.getContent());
        pemReader.close();
        return keyFactory.generatePrivate(keySpec);
    }

    public static PublicKey readPublicKey(Reader reader, String algorithm) throws IOException, InvalidKeySpecException, NoSuchAlgorithmException {
        if (!"RSA".equals(algorithm)) {
            throw new IllegalArgumentException("only support RSA algorithm.");
        }
        KeyFactory keyFactory = KeyFactory.getInstance(algorithm);
        PemReader pemReader = new PemReader(reader);
        PemObject pemObject = pemReader.readPemObject();
        KeySpec keySpec = new X509EncodedKeySpec(pemObject.getContent());
        pemReader.close();
        return keyFactory.generatePublic(keySpec);
    }
}
