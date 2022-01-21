package com.therearenotasksforus.videohostingapi.bucket;

public enum BucketName {
    BUCKET("video-hosting-api-bucket", "eu-central-1");

    private final String bucketName;
    private final String bucketRegion;

    BucketName(String bucketName, String bucketRegion) {
        this.bucketName = bucketName;
        this.bucketRegion = bucketRegion;
    }

    public String getBucketName() {
        return bucketName;
    }
    public String getBucketRegion() {
        return bucketRegion;
    }
}
