package com.fkp.template.modules.httpclient.controller;

import com.google.api.client.http.HttpRequestInitializer;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.gax.core.CredentialsProvider;
import com.google.api.gax.core.FixedCredentialsProvider;
import com.google.api.gax.core.GoogleCredentialsProvider;
import com.google.auth.oauth2.GoogleCredentials;
import com.google.auth.oauth2.ServiceAccountCredentials;
import com.google.cloud.kms.v1.*;
import com.google.cloud.kms.v1.stub.KeyManagementServiceStubSettings;
import com.google.crypto.tink.subtle.*;
import com.google.protobuf.ByteString;
import com.google.protobuf.FieldMask;
import com.google.protobuf.Timestamp;
import com.google.protobuf.util.FieldMaskUtil;
import lombok.SneakyThrows;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.threeten.bp.Duration;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.spec.OAEPParameterSpec;
import javax.crypto.spec.PSource;
import java.io.ByteArrayInputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.InetSocketAddress;
import java.net.Proxy;
import java.net.SocketAddress;
import java.nio.charset.StandardCharsets;
import java.security.*;
import java.security.PublicKey;
import java.security.spec.ECGenParameterSpec;
import java.security.spec.MGF1ParameterSpec;
import java.security.spec.X509EncodedKeySpec;
import java.time.temporal.ChronoUnit;
import java.util.Base64;

/**
 * @author fengkunpeng
 * @version 1.0
 * @description
 * @date 2024/7/2 11:36
 */
@RestController
@RequestMapping(value = "/test")
public class GoogleTestController {

    @SneakyThrows
    @GetMapping(value = "/quickstart")
    public String quickStart(String projectId, String locationId, String credentialsPath){
//        String projectId = "vibrant-magpie-428105-d7";
//        String locationId = "asia";
        quickstart(projectId, locationId, credentialsPath);
        return "success";
    }
//C:\Users\fengkunpeng\AppData\Roaming\gcloud\application_default_credentials.json
    @SneakyThrows
    @GetMapping(value = "/createKeyRing")
    public String createKeyRingMapping(String projectId, String locationId, String id){
//        String projectId = "vibrant-magpie-428105-d7";
//        String locationId = "asia";
//        String id = "my-asymmetric-signing-key";
        createKeyRing(projectId, locationId, id);
        return "success";
    }

    @GetMapping(value = "/createImportJob")
    public void createImportJobMapping(String projectId, String locationId, String keyRingId, String id) throws IOException {
        // (developer): Replace these variables before running the sample.
//        String projectId = "vibrant-magpie-428105-d7";
//        String locationId = "asia";
//        String keyRingId = "my-key-ring";
//        String id = "my-import-job";
        createImportJob(projectId, locationId, keyRingId, id);
    }

    @GetMapping(value = "/createKeyForImport")
    public void createKeyForImportMapping(String projectId, String locationId, String keyRingId, String id) throws IOException {
        // (developer): Replace these variables before running the sample.
//        String projectId = "vibrant-magpie-428105-d7";
//        String locationId = "asia";
//        String keyRingId = "testKeyRing";
//        String id = "importKey";
        createKeyForImport(projectId, locationId, keyRingId, id);
    }

    @GetMapping(value = "/checkStateImportJob")
    public void checkStateImportJobMapping(String projectId, String locationId, String keyRingId, String importJobId) throws IOException {
        // (developer): Replace these variables before running the sample.
//        String projectId = "vibrant-magpie-428105-d7";
//        String locationId = "asia";
//        String keyRingId = "my-key-ring";
//        String importJobId = "my-import-job";
        checkStateImportJob(projectId, locationId, keyRingId, importJobId);
    }

    @GetMapping(value = "/importManuallyWrappedKey")
    public void importManuallyWrappedKeyMapping(String projectId, String locationId, String keyRingId, String importJobId, String cryptoKeyId) throws GeneralSecurityException, IOException {
        // (developer): Replace these variables before running the sample.
//        String projectId = "vibrant-magpie-428105-d7";
//        String locationId = "asia";
//        String keyRingId = "my-key-ring";
//        String cryptoKeyId = "my-crypto-key";
//        String importJobId = "my-import-job";
        importSymmetricKey(projectId, locationId, keyRingId, cryptoKeyId, importJobId);
    }

    @GetMapping(value = "/checkStateImportedKey")
    public void checkStateImportedKeyMapping(String projectId, String locationId, String keyRingId, String cryptoKeyVersionId, String cryptoKeyId) throws IOException {
        // (developer): Replace these variables before running the sample.
//        String projectId = "vibrant-magpie-428105-d7";
//        String locationId = "asia";
//        String keyRingId = "my-key-ring";
//        String cryptoKeyId = "my-crypto-key";
//        String cryptoKeyVersionId = "1";
        checkStateImportedKey(projectId, locationId, keyRingId, cryptoKeyId, cryptoKeyVersionId);
    }

    @GetMapping(value = "/createKeySymmetricEncryptDecrypt")
    public void createKeySymmetricEncryptDecryptMapping(String projectId, String locationId, String keyRingId, String id) throws IOException {
        // (developer): Replace these variables before running the sample.
//        String projectId = "your-project-id";
//        String locationId = "us-east1";
//        String keyRingId = "my-key-ring";
//        String id = "my-key";
        createKeySymmetricEncryptDecrypt(projectId, locationId, keyRingId, id);
    }

    @GetMapping(value = "/enableKeyVersion")
    public void enableKeyVersionMapping(String projectId, String locationId, String keyRingId, String keyId, String keyVersionId) throws IOException {
        // (developer): Replace these variables before running the sample.
//        String projectId = "your-project-id";
//        String locationId = "us-east1";
//        String keyRingId = "my-key-ring";
//        String keyId = "my-key";
//        String keyVersionId = "123";
        enableKeyVersion(projectId, locationId, keyRingId, keyId, keyVersionId);
    }

    @GetMapping(value = "/disableKeyVersion")
    public void disableKeyVersionMapping(String projectId, String locationId, String keyRingId, String keyId, String keyVersionId) throws IOException {
        // (developer): Replace these variables before running the sample.
//        String projectId = "your-project-id";
//        String locationId = "us-east1";
//        String keyRingId = "my-key-ring";
//        String keyId = "my-key";
//        String keyVersionId = "123";
        disableKeyVersion(projectId, locationId, keyRingId, keyId, keyVersionId);
    }

    @GetMapping(value = "/destroyKeyVersion")
    public void destroyKeyVersionMapping(String projectId, String locationId, String keyRingId, String keyId, String keyVersionId) throws IOException {
        // (developer): Replace these variables before running the sample.
//        String projectId = "your-project-id";
//        String locationId = "us-east1";
//        String keyRingId = "my-key-ring";
//        String keyId = "my-key";
//        String keyVersionId = "123";
        destroyKeyVersion(projectId, locationId, keyRingId, keyId, keyVersionId);
    }

    @GetMapping(value = "/restoreKeyVersion")
    public void restoreKeyVersionMapping(String projectId, String locationId, String keyRingId, String keyId, String keyVersionId) throws IOException {
        // (developer): Replace these variables before running the sample.
//        String projectId = "your-project-id";
//        String locationId = "us-east1";
//        String keyRingId = "my-key-ring";
//        String keyId = "my-key";
//        String keyVersionId = "123";
        restoreKeyVersion(projectId, locationId, keyRingId, keyId, keyVersionId);
    }

    @GetMapping(value = "/updateKeySetPrimary")
    public void updateKeySetPrimaryMapping(String projectId, String locationId, String keyRingId, String keyId, String keyVersionId) throws IOException {
        // (developer): Replace these variables before running the sample.
//        String projectId = "your-project-id";
//        String locationId = "us-east1";
//        String keyRingId = "my-key-ring";
//        String keyId = "my-key";
//        String keyVersionId = "123";
        updateKeySetPrimary(projectId, locationId, keyRingId, keyId, keyVersionId);
    }

//    @GetMapping(value = "/createKeyRotationSchedule")
//    public void createKeyRotationScheduleMapping(String projectId, String locationId, String keyRingId, String id, Integer day) throws IOException {
//        // (developer): Replace these variables before running the sample.
////        String projectId = "your-project-id";
////        String locationId = "us-east1";
////        String keyRingId = "my-key-ring";
////        String id = "my-key";
//        createKeyRotationSchedule(projectId, locationId, keyRingId, id, day);
//    }

    @GetMapping(value = "/updateKeyRemoveRotation")
    public void updateKeyRemoveRotationMapping(String projectId, String locationId, String keyRingId, String keyId) throws IOException {
        // (developer): Replace these variables before running the sample.
//        String projectId = "your-project-id";
//        String locationId = "us-east1";
//        String keyRingId = "my-key-ring";
//        String keyId = "my-key";
        updateKeyRemoveRotation(projectId, locationId, keyRingId, keyId);
    }

    @GetMapping(value = "/updateKeyAddRotation")
    public void updateKeyAddRotationMapping(String projectId, String locationId, String keyRingId, String keyId) throws IOException {
        // (developer): Replace these variables before running the sample.
//        String projectId = "your-project-id";
//        String locationId = "us-east1";
//        String keyRingId = "my-key-ring";
//        String keyId = "my-key";
        updateKeyAddRotation(projectId, locationId, keyRingId, keyId);
    }

    @GetMapping(value = "/createKeyVersion")
    public void createKeyVersionMapping(String projectId, String locationId, String keyRingId, String keyId) throws IOException {
        // (developer): Replace these variables before running the sample.
//        String projectId = "your-project-id";
//        String locationId = "us-east1";
//        String keyRingId = "my-key-ring";
//        String keyId = "my-key";
        createKeyVersion(projectId, locationId, keyRingId, keyId);
    }

    @GetMapping(value = "/getKeyLabels")
    public void getKeyLabelsMapping(String projectId, String locationId, String keyRingId, String keyId) throws IOException {
        // (developer): Replace these variables before running the sample.
//        String projectId = "your-project-id";
//        String locationId = "us-east1";
//        String keyRingId = "my-key-ring";
//        String keyId = "my-key";
        getKeyLabels(projectId, locationId, keyRingId, keyId);
    }

    // Get the labels associated with a key.
    public void getKeyLabels(String projectId, String locationId, String keyRingId, String keyId)
            throws IOException {
        // Initialize client that will be used to send requests. This client only
        // needs to be created once, and can be reused for multiple requests. After
        // completing all of your requests, call the "close" method on the client to
        // safely clean up any remaining background resources.
        try (KeyManagementServiceClient client = KeyManagementServiceClient.create()) {
            // Build the name from the project, location, key ring, and keyId.
            CryptoKeyName keyName = CryptoKeyName.of(projectId, locationId, keyRingId, keyId);

            // Get the key.
            CryptoKey key = client.getCryptoKey(keyName);

            // Print out each label.
            key.getLabelsMap().forEach((k, v) -> System.out.printf("%s=%s%n", k, v));
        }
    }

    // Create a new key version.
    public void createKeyVersion(String projectId, String locationId, String keyRingId, String keyId)
            throws IOException {
        // Initialize client that will be used to send requests. This client only
        // needs to be created once, and can be reused for multiple requests. After
        // completing all of your requests, call the "close" method on the client to
        // safely clean up any remaining background resources.
        try (KeyManagementServiceClient client = KeyManagementServiceClient.create()) {
            // Build the parent name from the project, location, and key ring.
            CryptoKeyName cryptoKeyName = CryptoKeyName.of(projectId, locationId, keyRingId, keyId);

            // Build the key version to create.
            CryptoKeyVersion keyVersion = CryptoKeyVersion.newBuilder().build();

            // Create the key.
            CryptoKeyVersion createdVersion = client.createCryptoKeyVersion(cryptoKeyName, keyVersion);
            System.out.printf("Created key version %s%n", createdVersion.getName());
        }
    }

    // Update a key to add or change a rotation schedule.
    public void updateKeyAddRotation(
            String projectId, String locationId, String keyRingId, String keyId) throws IOException {
        // Initialize client that will be used to send requests. This client only
        // needs to be created once, and can be reused for multiple requests. After
        // completing all of your requests, call the "close" method on the client to
        // safely clean up any remaining background resources.
        try (KeyManagementServiceClient client = KeyManagementServiceClient.create()) {
            // Build the name from the project, location, and key ring.
            CryptoKeyName cryptoKeyName = CryptoKeyName.of(projectId, locationId, keyRingId, keyId);

            // Calculate the date 24 hours from now (this is used below).
            long tomorrow = java.time.Instant.now().plus(24, ChronoUnit.HOURS).getEpochSecond();

            // Build the key to update with a rotation schedule.
            CryptoKey key =
                    CryptoKey.newBuilder()
                            .setName(cryptoKeyName.toString())
                            .setPurpose(CryptoKey.CryptoKeyPurpose.ENCRYPT_DECRYPT)
                            .setVersionTemplate(
                                    CryptoKeyVersionTemplate.newBuilder()
                                            .setAlgorithm(CryptoKeyVersion.CryptoKeyVersionAlgorithm.GOOGLE_SYMMETRIC_ENCRYPTION))

                            // Rotate every 30 days.
                            .setRotationPeriod(
                                    com.google.protobuf.Duration.newBuilder().setSeconds(java.time.Duration.ofDays(30).getSeconds()))

                            // Start the first rotation in 24 hours.
                            .setNextRotationTime(Timestamp.newBuilder().setSeconds(tomorrow))
                            .build();

            // Construct the field mask.
            FieldMask fieldMask = FieldMaskUtil.fromString("rotation_period,next_rotation_time");

            // Update the key.
            CryptoKey updatedKey = client.updateCryptoKey(key, fieldMask);
            System.out.printf("Updated key %s%n", updatedKey.getName());
        }
    }

    // Update a key to remove all labels.
    public void updateKeyRemoveRotation(
            String projectId, String locationId, String keyRingId, String keyId) throws IOException {
        // Initialize client that will be used to send requests. This client only
        // needs to be created once, and can be reused for multiple requests. After
        // completing all of your requests, call the "close" method on the client to
        // safely clean up any remaining background resources.
        try (KeyManagementServiceClient client = KeyManagementServiceClient.create()) {
            // Build the name from the project, location, key ring, and keyId.
            CryptoKeyName cryptoKeyName = CryptoKeyName.of(projectId, locationId, keyRingId, keyId);

            // Build an empty key with no labels.
            CryptoKey key =
                    CryptoKey.newBuilder()
                            .setName(cryptoKeyName.toString())
                            .clearRotationPeriod()
                            .clearNextRotationTime()
                            .build();

            // Construct the field mask.
            FieldMask fieldMask = FieldMaskUtil.fromString("rotation_period,next_rotation_time");

            // Create the key.
            CryptoKey createdKey = client.updateCryptoKey(key, fieldMask);
            System.out.printf("Updated key %s%n", createdKey.getName());
        }
    }

    // Create a new key that automatically rotates on a schedule.
    public void createKeyRotationSchedule(
            String projectId, String locationId, String keyRingId, String id, Integer day) throws IOException {
        // Initialize client that will be used to send requests. This client only
        // needs to be created once, and can be reused for multiple requests. After
        // completing all of your requests, call the "close" method on the client to
        // safely clean up any remaining background resources.
        try (KeyManagementServiceClient client = KeyManagementServiceClient.create()) {
            // Build the parent name from the project, location, and key ring.
            KeyRingName keyRingName = KeyRingName.of(projectId, locationId, keyRingId);

            // Calculate the date 24 hours from now (this is used below).
            long tomorrow = java.time.Instant.now().plus(day, ChronoUnit.DAYS).getEpochSecond();

            // Build the key to create with a rotation schedule.
            CryptoKey key =
                    CryptoKey.newBuilder()
                            .setPurpose(CryptoKey.CryptoKeyPurpose.ENCRYPT_DECRYPT)
                            .setVersionTemplate(
                                    CryptoKeyVersionTemplate.newBuilder()
                                            .setAlgorithm(CryptoKeyVersion.CryptoKeyVersionAlgorithm.GOOGLE_SYMMETRIC_ENCRYPTION))

                            // Rotate every 30 days.
                            .setRotationPeriod(
                                    com.google.protobuf.Duration.newBuilder().setSeconds(java.time.Duration.ofDays(day).getSeconds()))

                            // Start the first rotation in 24 hours.
                            .setNextRotationTime(Timestamp.newBuilder().setSeconds(tomorrow))
                            .build();

            // Create the key.
            CryptoKey createdKey = client.createCryptoKey(keyRingName, id, key);
            System.out.printf("Created key with rotation schedule %s%n", createdKey.getName());
        }
    }

    // Update a key's primary version.
    public void updateKeySetPrimary(
            String projectId, String locationId, String keyRingId, String keyId, String keyVersionId)
            throws IOException {
        // Initialize client that will be used to send requests. This client only
        // needs to be created once, and can be reused for multiple requests. After
        // completing all of your requests, call the "close" method on the client to
        // safely clean up any remaining background resources.
        try (KeyManagementServiceClient client = KeyManagementServiceClient.create()) {
            // Build the name from the project, location, key ring, and keyId.
            CryptoKeyName cryptoKeyName = CryptoKeyName.of(projectId, locationId, keyRingId, keyId);

            // Create the key.
            CryptoKey createdKey = client.updateCryptoKeyPrimaryVersion(cryptoKeyName, keyVersionId);
            System.out.printf("Updated key primary version %s%n", createdKey.getName());
        }
    }

    // Schedule destruction of the given key version.
    public void restoreKeyVersion(
            String projectId, String locationId, String keyRingId, String keyId, String keyVersionId)
            throws IOException {
        // Initialize client that will be used to send requests. This client only
        // needs to be created once, and can be reused for multiple requests. After
        // completing all of your requests, call the "close" method on the client to
        // safely clean up any remaining background resources.
        try (KeyManagementServiceClient client = KeyManagementServiceClient.create()) {
            // Build the key version name from the project, location, key ring, key,
            // and key version.
            CryptoKeyVersionName keyVersionName =
                    CryptoKeyVersionName.of(projectId, locationId, keyRingId, keyId, keyVersionId);

            // Restore the key version.
            CryptoKeyVersion response = client.restoreCryptoKeyVersion(keyVersionName);
            System.out.printf("Restored key version: %s%n", response.getName());
        }
    }

    // Schedule destruction of the given key version.
    public void destroyKeyVersion(
            String projectId, String locationId, String keyRingId, String keyId, String keyVersionId)
            throws IOException {
        // Initialize client that will be used to send requests. This client only
        // needs to be created once, and can be reused for multiple requests. After
        // completing all of your requests, call the "close" method on the client to
        // safely clean up any remaining background resources.
        try (KeyManagementServiceClient client = KeyManagementServiceClient.create()) {
            // Build the key version name from the project, location, key ring, key,
            // and key version.
            CryptoKeyVersionName keyVersionName =
                    CryptoKeyVersionName.of(projectId, locationId, keyRingId, keyId, keyVersionId);

            // Destroy the key version.
            CryptoKeyVersion response = client.destroyCryptoKeyVersion(keyVersionName);
            System.out.printf("Destroyed key version: %s%n", response.getName());
        }
    }

    // Disable a key version from use.
    public void disableKeyVersion(
            String projectId, String locationId, String keyRingId, String keyId, String keyVersionId)
            throws IOException {
        // Initialize client that will be used to send requests. This client only
        // needs to be created once, and can be reused for multiple requests. After
        // completing all of your requests, call the "close" method on the client to
        // safely clean up any remaining background resources.
        try (KeyManagementServiceClient client = KeyManagementServiceClient.create()) {
            // Build the key version name from the project, location, key ring, key,
            // and key version.
            CryptoKeyVersionName keyVersionName =
                    CryptoKeyVersionName.of(projectId, locationId, keyRingId, keyId, keyVersionId);

            // Build the updated key version, setting it to disbaled.
            CryptoKeyVersion keyVersion =
                    CryptoKeyVersion.newBuilder()
                            .setName(keyVersionName.toString())
                            .setState(CryptoKeyVersion.CryptoKeyVersionState.DISABLED)
                            .build();

            // Create a field mask of updated values.
            FieldMask fieldMask = FieldMaskUtil.fromString("state");

            // Disable the key version.
            CryptoKeyVersion response = client.updateCryptoKeyVersion(keyVersion, fieldMask);
            System.out.printf("Disabled key version: %s%n", response.getName());
        }
    }

    // Enable a disabled key version to be used again.
    public void enableKeyVersion(
            String projectId, String locationId, String keyRingId, String keyId, String keyVersionId)
            throws IOException {
        // Initialize client that will be used to send requests. This client only
        // needs to be created once, and can be reused for multiple requests. After
        // completing all of your requests, call the "close" method on the client to
        // safely clean up any remaining background resources.
        try (KeyManagementServiceClient client = KeyManagementServiceClient.create()) {
            // Build the key version name from the project, location, key ring, key,
            // and key version.
            CryptoKeyVersionName keyVersionName =
                    CryptoKeyVersionName.of(projectId, locationId, keyRingId, keyId, keyVersionId);

            // Build the updated key version, setting it to enabled.
            CryptoKeyVersion keyVersion =
                    CryptoKeyVersion.newBuilder()
                            .setName(keyVersionName.toString())
                            .setState(CryptoKeyVersion.CryptoKeyVersionState.ENABLED)
                            .build();

            // Create a field mask of updated values.
            FieldMask fieldMask = FieldMaskUtil.fromString("state");

            // Enable the key version.
            CryptoKeyVersion response = client.updateCryptoKeyVersion(keyVersion, fieldMask);
            System.out.printf("Enabled key version: %s%n", response.getName());
        }
    }

    // Create a new key that is used for symmetric encryption and decryption.
    public void createKeySymmetricEncryptDecrypt(
            String projectId, String locationId, String keyRingId, String id) throws IOException {
        // Initialize client that will be used to send requests. This client only
        // needs to be created once, and can be reused for multiple requests. After
        // completing all of your requests, call the "close" method on the client to
        // safely clean up any remaining background resources.
        try (KeyManagementServiceClient client = KeyManagementServiceClient.create()) {
            // Build the parent name from the project, location, and key ring.
            KeyRingName keyRingName = KeyRingName.of(projectId, locationId, keyRingId);

            // Build the symmetric key to create.
            CryptoKey key =
                    CryptoKey.newBuilder()
                            .setPurpose(CryptoKey.CryptoKeyPurpose.ENCRYPT_DECRYPT)
                            .setVersionTemplate(
                                    CryptoKeyVersionTemplate.newBuilder()
                                            .setAlgorithm(CryptoKeyVersion.CryptoKeyVersionAlgorithm.GOOGLE_SYMMETRIC_ENCRYPTION))
                            .build();

            // Create the key.
            CryptoKey createdKey = client.createCryptoKey(keyRingName, id, key);
            System.out.printf("Created symmetric key %s%n", createdKey.getName());
        }
    }

    // Check the state of an imported key in Cloud KMS.
    public void checkStateImportedKey(
            String projectId,
            String locationId,
            String keyRingId,
            String cryptoKeyId,
            String cryptoKeyVersionId)
            throws IOException {
        // Initialize client that will be used to send requests. This client only
        // needs to be created once, and can be reused for multiple requests. After
        // completing all of your requests, call the "close" method on the client to
        // safely clean up any remaining background resources.
        try (KeyManagementServiceClient client = KeyManagementServiceClient.create()) {
            // Build the version name from its path components.
            CryptoKeyVersionName versionName =
                    CryptoKeyVersionName.of(
                            projectId, locationId, keyRingId, cryptoKeyId, cryptoKeyVersionId);

            // Retrieve the state of an existing version.
            CryptoKeyVersion version = client.getCryptoKeyVersion(versionName);
            System.out.printf(
                    "Current state of crypto key version %s: %s%n", version.getName(), version.getState());
        }
    }

    public static void main(String[] args) {
        String material = "/Qe21jZyP08hyhyS2oJdH4lNJLqggVwpdZbAru7msoE=";
        System.out.println(org.apache.commons.codec.binary.Base64.decodeBase64(material).length);
    }

    @SuppressWarnings("deprecation")
    public void importSymmetricKey(
            String projectId, String locationId, String keyRingId, String cryptoKeyId, String importJobId)
            throws GeneralSecurityException, IOException {

        // Generate a new AES key.
        KeyGenerator keyGen = KeyGenerator.getInstance("AES");
        keyGen.init(256); // Specify the key size.
        SecretKey aesKey = keyGen.generateKey();
        byte[] aesKeyBytes = aesKey.getEncoded();

        // Initialize client that will be used to send requests. This client only
        // needs to be created once, and can be reused for multiple requests. After
        // completing all of your requests, call the "close" method on the client to
        // safely clean up any remaining background resources.
        try (KeyManagementServiceClient client = KeyManagementServiceClient.create()) {
            // Build the crypto key and import job names from the project, location,
            // key ring, and ID.
            final CryptoKeyName cryptoKeyName =
                    CryptoKeyName.of(projectId, locationId, keyRingId, cryptoKeyId);
            final ImportJobName importJobName =
                    ImportJobName.of(projectId, locationId, keyRingId, importJobId);

            // Generate a temporary 32-byte key for AES-KWP and wrap the key material.
            byte[] kwpKey = new byte[32];
            new SecureRandom().nextBytes(kwpKey);
            Kwp kwp = new Kwp(kwpKey);
            final byte[] wrappedTargetKey = kwp.wrap(aesKeyBytes);

            // Retrieve the public key from the import job.
            ImportJob importJob = client.getImportJob(importJobName);
            String publicKeyStr = importJob.getPublicKey().getPem();
            // Manually convert PEM to DER.
            publicKeyStr = publicKeyStr.replace("-----BEGIN PUBLIC KEY-----", "");
            publicKeyStr = publicKeyStr.replace("-----END PUBLIC KEY-----", "");
            publicKeyStr = publicKeyStr.replaceAll("\n", "");
            byte[] publicKeyBytes = Base64.getDecoder().decode(publicKeyStr);
            PublicKey publicKey =
                    KeyFactory.getInstance("RSA").generatePublic(new X509EncodedKeySpec(publicKeyBytes));

            // Wrap the KWP key using the import job key.
            Cipher cipher = Cipher.getInstance("RSA/ECB/OAEPWithSHA-1AndMGF1Padding");
            cipher.init(
                    Cipher.ENCRYPT_MODE,
                    publicKey,
                    new OAEPParameterSpec(
                            "SHA-1", "MGF1", MGF1ParameterSpec.SHA1, PSource.PSpecified.DEFAULT));
            byte[] wrappedWrappingKey = cipher.doFinal(kwpKey);

            // Concatenate the wrapped KWP key and the wrapped target key.
            ByteString combinedWrappedKeys =
                    ByteString.copyFrom(wrappedWrappingKey).concat(ByteString.copyFrom(wrappedTargetKey));

            // Import the wrapped key material.
            CryptoKeyVersion version =
                    client.importCryptoKeyVersion(
                            ImportCryptoKeyVersionRequest.newBuilder()
                                    .setParent(cryptoKeyName.toString())
                                    .setImportJob(importJobName.toString())
                                    .setAlgorithm(CryptoKeyVersion.CryptoKeyVersionAlgorithm.GOOGLE_SYMMETRIC_ENCRYPTION)
                                    .setRsaAesWrappedKey(combinedWrappedKeys)
                                    .build());

            System.out.printf("Imported: %s%n", version.getName());
        }
    }

    // Generates and imports local key material into Cloud KMS.
    public void importManuallyWrappedKey(
            String projectId, String locationId, String keyRingId, String cryptoKeyId, String importJobId)
            throws GeneralSecurityException, IOException {

        // Generate a new ECDSA keypair, and format the private key as PKCS #8 DER.
        KeyPairGenerator generator = KeyPairGenerator.getInstance("EC");
        generator.initialize(new ECGenParameterSpec("secp256r1"));
        KeyPair kp = generator.generateKeyPair();
        byte[] privateBytes = kp.getPrivate().getEncoded();

        // Initialize client that will be used to send requests. This client only
        // needs to be created once, and can be reused for multiple requests. After
        // completing all of your requests, call the "close" method on the client to
        // safely clean up any remaining background resources.
        try (KeyManagementServiceClient client = KeyManagementServiceClient.create()) {
            // Build the crypto key and import job names from the project, location,
            // key ring, and ID.
            final CryptoKeyName cryptoKeyName =
                    CryptoKeyName.of(projectId, locationId, keyRingId, cryptoKeyId);
            final ImportJobName importJobName =
                    ImportJobName.of(projectId, locationId, keyRingId, importJobId);

            // Generate a temporary 32-byte key for AES-KWP and wrap the key material.
            String material = "/Qe21jZyP08hyhyS2oJdH4lNJLqggVwpdZbAru7msoE=";
            byte[] kwpKey = org.apache.commons.codec.binary.Base64.decodeBase64(material);
//            Kwp kwp = new Kwp(kwpKey);
//            final byte[] wrappedTargetKey = kwp.wrap(privateBytes);



            // Retrieve the public key from the import job.
            ImportJob importJob = client.getImportJob(importJobName);
            String publicKeyStr = importJob.getPublicKey().getPem();
            // Manually convert PEM to DER. :-(
            publicKeyStr = publicKeyStr.replace("-----BEGIN PUBLIC KEY-----", "");
            publicKeyStr = publicKeyStr.replace("-----END PUBLIC KEY-----", "");
            publicKeyStr = publicKeyStr.replaceAll("\n", "");
            byte[] publicKeyBytes = Base64.getDecoder().decode(publicKeyStr);
            PublicKey publicKey =
                    KeyFactory.getInstance("RSA").generatePublic(new X509EncodedKeySpec(publicKeyBytes));

            // Wrap the KWP key using the import job key.
            Cipher cipher = Cipher.getInstance("RSA/ECB/OAEPWithSHA-1AndMGF1Padding");
            cipher.init(
                    Cipher.ENCRYPT_MODE,
                    publicKey,
                    new OAEPParameterSpec(
                            "SHA-1", "MGF1", MGF1ParameterSpec.SHA1, PSource.PSpecified.DEFAULT));
            byte[] wrappedWrappingKey = cipher.doFinal(kwpKey);

            // Concatenate the wrapped KWP key and the wrapped target key.
            ByteString combinedWrappedKeys =
                    ByteString.copyFrom(wrappedWrappingKey);
//                            .concat(ByteString.copyFrom(wrappedTargetKey));

            // Import the wrapped key material.
            CryptoKeyVersion version =
                    client.importCryptoKeyVersion(
                            ImportCryptoKeyVersionRequest.newBuilder()
                                    .setParent(cryptoKeyName.toString())
                                    .setImportJob(importJobName.toString())
                                    .setAlgorithm(CryptoKeyVersion.CryptoKeyVersionAlgorithm.AES_256_CBC)
                                    .setRsaAesWrappedKey(combinedWrappedKeys)
                                    .build());

            System.out.printf("Imported: %s%n", version.getName());
        }
    }

    // Check the state of an import job in Cloud KMS.
    public void checkStateImportJob(
            String projectId, String locationId, String keyRingId, String importJobId)
            throws IOException {
        // Initialize client that will be used to send requests. This client only
        // needs to be created once, and can be reused for multiple requests. After
        // completing all of your requests, call the "close" method on the client to
        // safely clean up any remaining background resources.
        try (KeyManagementServiceClient client = KeyManagementServiceClient.create()) {
            // Build the parent name from the project, location, and key ring.
            ImportJobName importJobName = ImportJobName.of(projectId, locationId, keyRingId, importJobId);

            // Retrieve the state of an existing import job.
            ImportJob importJob = client.getImportJob(importJobName);
            System.out.printf(
                    "Current state of import job %s: %s%n", importJob.getName(), importJob.getState());
        }
    }

    // Create a new crypto key to hold imported key versions.
    public void createKeyForImport(String projectId, String locationId, String keyRingId, String id)
            throws IOException {
        // Initialize client that will be used to send requests. This client only
        // needs to be created once, and can be reused for multiple requests. After
        // completing all of your requests, call the "close" method on the client to
        // safely clean up any remaining background resources.
        try (KeyManagementServiceClient client = KeyManagementServiceClient.create()) {
            // Build the parent name from the project, location, and key ring.
            KeyRingName keyRingName = KeyRingName.of(projectId, locationId, keyRingId);

            // Create the crypto key.
            CryptoKey createdKey =
                    client.createCryptoKey(
                            CreateCryptoKeyRequest.newBuilder()
                                    .setParent(keyRingName.toString())
                                    .setCryptoKeyId(id)
                                    .setCryptoKey(
                                            CryptoKey.newBuilder()
                                                    .setPurpose(CryptoKey.CryptoKeyPurpose.ENCRYPT_DECRYPT)
                                                    .setVersionTemplate(
                                                            CryptoKeyVersionTemplate.newBuilder()
                                                                    .setProtectionLevel(ProtectionLevel.SOFTWARE)
                                                                    .setAlgorithm(CryptoKeyVersion.CryptoKeyVersionAlgorithm.GOOGLE_SYMMETRIC_ENCRYPTION))
                                                    // Ensure that only imported versions may be
                                                    // added to this key.
                                                    .setImportOnly(true))
                                    .setSkipInitialVersionCreation(true)
                                    .build());

            System.out.printf("Created crypto key %s%n", createdKey.getName());
        }
    }



    // Create a new import job.
    public void createImportJob(String projectId, String locationId, String keyRingId, String id)
            throws IOException {
        // Initialize client that will be used to send requests. This client only
        // needs to be created once, and can be reused for multiple requests. After
        // completing all of your requests, call the "close" method on the client to
        // safely clean up any remaining background resources.
        try (KeyManagementServiceClient client = KeyManagementServiceClient.create()) {
            // Build the parent name from the project, location, and key ring.
            KeyRingName keyRingName = KeyRingName.of(projectId, locationId, keyRingId);

            // Build the import job to create, with parameters.
            ImportJob importJob =
                    ImportJob.newBuilder()
                            // See allowed values and their descriptions at
                            // https://cloud.google.com/kms/docs/algorithms#protection_levels
                            .setProtectionLevel(ProtectionLevel.SOFTWARE)
                            // See allowed values and their descriptions at
                            // https://cloud.google.com/kms/docs/key-wrapping#import_methods
                            .setImportMethod(ImportJob.ImportMethod.RSA_OAEP_3072_SHA1_AES_256)
                            .build();

            // Create the import job.
            ImportJob createdImportJob = client.createImportJob(keyRingName, id, importJob);
            System.out.printf("Created import job %s%n", createdImportJob.getName());
        }
    }


    public void createKeyRing(String projectId, String locationId, String id) throws IOException {
        // Initialize client that will be used to send requests. This client only
        // needs to be created once, and can be reused for multiple requests. After
        // completing all of your requests, call the "close" method on the client to
        // safely clean up any remaining background resources.

        KeyManagementServiceSettings.Builder keyManagementServiceSettingsBuilder = KeyManagementServiceSettings.newBuilder();
        keyManagementServiceSettingsBuilder.createKeyRingSettings().setRetrySettings(keyManagementServiceSettingsBuilder.
                getKeyRingSettings().getRetrySettings().toBuilder().setMaxRpcTimeout(Duration.ofSeconds(3000))
                .setTotalTimeout(Duration.ofSeconds(3000)).setLogicalTimeout(Duration.ofSeconds(3000)).setInitialRpcTimeout(Duration.ofSeconds(3000)).build());
        KeyManagementServiceSettings keyManagementServiceSettings = keyManagementServiceSettingsBuilder.build();
        try (KeyManagementServiceClient client = KeyManagementServiceClient.create(keyManagementServiceSettings)) {
            // Build the parent name from the project and location.
            LocationName locationName = LocationName.of(projectId, locationId);

            // Build the key ring to create.
            KeyRing keyRing = KeyRing.newBuilder().build();
            // Create the key ring.
            KeyRing createdKeyRing = client.createKeyRing(locationName, id, keyRing);
            System.out.printf("Created key ring %s%n", createdKeyRing.getName());
        }
    }
    public void quickstart(String projectId, String locationId, String credentialsPath) throws IOException {
        // Initialize client that will be used to send requests. This client only
        // needs to be created once, and can be reused for multiple requests. After
        // completing all of your requests, call the "close" method on the client to
        // safely clean up any remaining background resources.
//        String json = "{}";
//        InputStream inputStream = IOUtils.toInputStream(json, StandardCharsets.UTF_8);
//        System.setProperty("socksProxyHost", "127.0.0.1");
//        System.setProperty("socksProxyPort", String.valueOf(10808 ));
        if(StringUtils.isNotBlank(credentialsPath)){
            GoogleCredentials googleCredentials = GoogleCredentials.fromStream(new FileInputStream(credentialsPath))
                    .createScoped(KeyManagementServiceSettings.getDefaultServiceScopes());
            KeyManagementServiceSettings settings = KeyManagementServiceSettings.newBuilder()
                    .setCredentialsProvider(FixedCredentialsProvider.create(googleCredentials))
                    .build();
            try (KeyManagementServiceClient client = KeyManagementServiceClient.create(settings)) {
                // Build the parent from the project and location.
                LocationName parent = LocationName.of(projectId, locationId);

                // Call the API.
                KeyManagementServiceClient.ListKeyRingsPagedResponse response = client.listKeyRings(parent);

                // Iterate over each key ring and print its name.
                System.out.println("key rings:");
                for (KeyRing keyRing : response.iterateAll()) {
                    System.out.printf("%s%n", keyRing.getName());
                }
            }
        }else {
            // 检查代理设置
//            Proxy proxy = new Proxy(Proxy.Type.SOCKS, new InetSocketAddress("127.0.0.1", 10808));
//            java.net.Socket socket = new java.net.Socket(proxy);
//            socket.connect(new InetSocketAddress("www.google.com", 80)); // 试图通过代理连接到Google

            SocketAddress proxyAddress = new InetSocketAddress("127.0.0.1", 10808);
            Proxy proxy2 = new Proxy(Proxy.Type.SOCKS, proxyAddress);

            HttpTransport httpTransport = new NetHttpTransport.Builder()
                    .setProxy(proxy2)
                    .build();

//            HttpRequestInitializer requestInitializer = request -> {
//                request.setTransport(httpTransport);
//            };
            KeyManagementServiceSettings settings = KeyManagementServiceSettings.newBuilder()
                    .setTransportChannelProvider(
                            KeyManagementServiceSettings.defaultHttpJsonTransportProviderBuilder()
                                    .setHttpTransport(httpTransport)
                                    .build()).build();
            try (KeyManagementServiceClient client = KeyManagementServiceClient.create(settings)) {
                // Build the parent from the project and location.
                LocationName parent = LocationName.of(projectId, locationId);

                // Call the API.
                KeyManagementServiceClient.ListKeyRingsPagedResponse response = client.listKeyRings(parent);

                // Iterate over each key ring and print its name.
                System.out.println("key rings:");
                for (KeyRing keyRing : response.iterateAll()) {
                    System.out.printf("%s%n", keyRing.getName());
                }
            }
        }

    }
}
