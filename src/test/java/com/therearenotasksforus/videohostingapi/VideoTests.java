package com.therearenotasksforus.videohostingapi;

import org.junit.jupiter.api.Test;
import org.springframework.test.web.servlet.MvcResult;

import static org.junit.Assert.assertEquals;

class VideoTests extends AbstractTest{

    @Test
    public void videoListLoads() throws Exception {
        super.register();
        String token = super.getToken();
        String uri = "/api/videos";

        MvcResult mvcResult = super.getRequest(uri, token);
        int status = mvcResult.getResponse().getStatus();

        assertEquals(200, status);
    }

    @Test
    public void videoLoadsById () throws Exception {
        super.register();
        String token = super.getToken();
        int channelId = (int)super.mapFromJson(super.crateChannel(token)).get("id");

        MvcResult uploadedVideo = super
                .uploadVideoWithUriAndToken("/api/channel/" + channelId + "/upload/video", token);
        String uri = "/api/video/" + (int)mapFromJson(uploadedVideo).get("id");

        MvcResult mvcResult = super.getRequest(uri, token);
        int status = mvcResult.getResponse().getStatus();

        assertEquals(200, status);

    }
}
