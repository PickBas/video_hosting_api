package com.therearenotasksforus.videohostingapi.bucket;

public enum BucketName {
    PROFILE_IMAGE("therearenotasksforus-assets");

    private final String bucketName;

    BucketName(String bucketName) {
        this.bucketName = bucketName;
    }

    public String getBucketName() {
        return bucketName;
    }
}
