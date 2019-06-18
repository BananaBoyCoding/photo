package com.jiehun.album.vo;

import java.util.List;

import lombok.Data;

/**
 * Created by zg on 2018/1/17.
 */


public class UploadImgListVo {
    private List<String>    fail;
    private List<SuccessVo> success;

    public List<String> getFail() {
        return fail;
    }

    public void setFail(List<String> fail) {
        this.fail = fail;
    }

    public List<SuccessVo> getSuccess() {
        return success;
    }

    public void setSuccess(List<SuccessVo> success) {
        this.success = success;
    }

    public class SuccessVo {
        private String key;
        private String url;
        private String origname;

        public String getKey() {
            return key;
        }

        public void setKey(String key) {
            this.key = key;
        }

        public String getUrl() {
            return url;
        }

        public void setUrl(String url) {
            this.url = url;
        }

        public String getOrigname() {
            return origname;
        }

        public void setOrigname(String origname) {
            this.origname = origname;
        }
    }
}
