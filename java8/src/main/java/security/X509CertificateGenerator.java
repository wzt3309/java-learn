package security;

import org.bouncycastle.asn1.pkcs.PKCSObjectIdentifiers;
import org.bouncycastle.asn1.x500.X500Name;
import org.bouncycastle.asn1.x500.X500NameBuilder;
import org.bouncycastle.asn1.x500.style.RFC4519Style;
import org.bouncycastle.asn1.x509.Extension;
import org.bouncycastle.asn1.x509.GeneralName;
import org.bouncycastle.asn1.x509.GeneralNamesBuilder;
import org.bouncycastle.cert.CertIOException;
import org.bouncycastle.cert.X509CertificateHolder;
import org.bouncycastle.cert.jcajce.JcaX509v3CertificateBuilder;
import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.bouncycastle.jce.provider.X509CertificateObject;
import org.bouncycastle.operator.ContentSigner;
import org.bouncycastle.operator.OperatorCreationException;
import org.bouncycastle.operator.jcajce.JcaContentSignerBuilder;

import java.io.File;
import java.io.FileOutputStream;
import java.math.BigInteger;
import java.security.*;
import java.security.cert.Certificate;
import java.security.cert.CertificateParsingException;
import java.util.*;
import java.util.stream.Collectors;

public class X509CertificateGenerator {
    public static void main(String[] args) throws Exception {
        // add customer security Provider
        Security.addProvider(new BouncyCastleProvider());

        outputSecurityProviders();

        String keyPassword = "123456";
        String storePassword = "123456";
        String keystorePath = System.getProperty("java.io.tmpdir") + "/.keystore";
        System.out.println("output keystore file in " + keystorePath);
        File keystoreFile = new File(keystorePath);
        KeyStore ks = KeyStore.Builder
                .newInstance("JKS", null, new KeyStore.PasswordProtection(null))
                .getKeyStore();

        KeyPair rootKeyPair = createKeyPair();
        KeyPair keyPair = createKeyPair();

        X509CertificateHolder rootCert = createRootCert(rootKeyPair);
        X509CertificateHolder serverCert = createServerCert(
                rootKeyPair.getPrivate(), rootCert, keyPair, ofNames("test1", "test2"));

        Certificate jceRootCert = toJava(rootCert);
        Certificate jceServerCert = toJava(serverCert);

        ks.setKeyEntry("key",        // alias name for entry
                keyPair.getPrivate(),     // private key for server cert
                keyPassword.toCharArray(),  // password for the entry
                new Certificate[]{jceRootCert, jceServerCert}   // certificates chain
        );
        try (FileOutputStream fos = new FileOutputStream(keystoreFile)) {
            ks.store(fos, storePassword.toCharArray());
        }
        // you can use "keytool -list -v -keystore <keystore file>" to vertify
    }

    private static Set<String> ofNames(String... names) {
        return Arrays.stream(names).collect(Collectors.toSet());
    }

    private static X509CertificateHolder createRootCert(KeyPair rootKeyPair) throws OperatorCreationException {
        X500NameBuilder nb = new X500NameBuilder();
        nb.addRDN(RFC4519Style.c, "AQ");             //C
        nb.addRDN(RFC4519Style.o, "test");           //O
        nb.addRDN(RFC4519Style.l, "ShangHai/Asian"); //L
        nb.addRDN(PKCSObjectIdentifiers.pkcs_9_at_emailAddress, "test@sh.aq");
        X500Name issuer = nb.build();
        // self signed certificate
        JcaX509v3CertificateBuilder cb = X509CertificateGenerator.createCertBuilder(rootKeyPair, issuer, issuer);
        ContentSigner signer = new JcaContentSignerBuilder("SHA256withRSA").build(rootKeyPair.getPrivate());
        return cb.build(signer);
    }

    private static Certificate toJava(X509CertificateHolder certHolder) throws CertificateParsingException {
        return new X509CertificateObject(certHolder.toASN1Structure());
    }

    /**
     * Create server cert
     * @param rootKey root cert private key
     * @param root  root cert
     * @param keyPair server's private key and public key
     * @param names server's cert subject alternative names
     * @return  server cert
     * @throws CertIOException
     * @throws OperatorCreationException
     */
    private static X509CertificateHolder createServerCert(PrivateKey rootKey,
                                                          X509CertificateHolder root,
                                                          KeyPair keyPair,
                                                          Collection<String> names)
            throws CertIOException, OperatorCreationException {
        // RFC 4519 for LDPA which based X.500
        // to build cert subject (the server)
        X500NameBuilder nb = new X500NameBuilder(RFC4519Style.INSTANCE);
        // add Relative Distinguished Name, RDN
        nb.addRDN(RFC4519Style.name, "localhost");

        // create cert builder
        X500Name issuer = root.getIssuer();
        X500Name subject = nb.build();
        JcaX509v3CertificateBuilder cb = createCertBuilder(keyPair, issuer, subject);

        // encode and add subject alternative name
        GeneralNamesBuilder gnb = new GeneralNamesBuilder();
        for (String name : names) {
            gnb.addName(new GeneralName(GeneralName.dNSName, name));
        }
        cb.addExtension(Extension.subjectAlternativeName, true, gnb.build());

        // create content signer
        // key algorithm: RSA digest algorithm: SHA256
        // more details see https://www.programcreek.com/java-api-examples/?api=org.bouncycastle.operator.jcajce.JcaContentSignerBuilder
        ContentSigner signer = new JcaContentSignerBuilder("SHA256WithRSA").build(rootKey);
        return cb.build(signer);
    }

    /**
     * Return JCA helper class to allow JCA objects to be used in the construction of a Version 3 certificate.
     *
     * @param keyPair privateKey and publicKey
     * @param issuer  X500Name representing the issuer of this certificate.
     * @param subject X500Name representing the subject of this certificate.
     * @return JCA helper class
     */
    private static JcaX509v3CertificateBuilder createCertBuilder(KeyPair keyPair,
                                                                 X500Name issuer,
                                                                 X500Name subject) {
        Calendar calendar = Calendar.getInstance();
        Date notBefore = calendar.getTime();
        calendar.add(Calendar.YEAR, 5);
        Date notAfter = calendar.getTime();

        return new JcaX509v3CertificateBuilder(
                issuer,
                BigInteger.valueOf(System.currentTimeMillis()), // the serial number for the certificate.
                notBefore,  // date before which the certificate is not valid.
                notAfter,   // date after which the certificate is not valid.
                subject,
                keyPair.getPublic()
        );
    }

    /* Return rsa privateKey and publicKey */
    private static KeyPair createKeyPair() throws NoSuchProviderException, NoSuchAlgorithmException {
        KeyPairGenerator kpg = KeyPairGenerator.getInstance("RSA", "BC");
        kpg.initialize(2048);
        return kpg.generateKeyPair();
    }

    private static void outputSecurityProviders() {
        Provider[] providers = Security.getProviders();
        Arrays.stream(providers).map(Provider::getName).forEach(System.out::println);
    }
}
