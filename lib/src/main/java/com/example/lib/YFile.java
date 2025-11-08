package com.example.lib;

import android.text.TextUtils;

import com.example.lib.uipage.MyInsAc;

public class YFile {

    private MyInsAc.Format format;
    private String url = "";

    YFile(MyInsAc.Format format, String url) {
        this.format = format;
        if(!TextUtils.isEmpty(url)){
            this.url = url.replace("\\", "");
        }
    }

    /**
     * The url to download the file.
     */
    public String getUrl() {
        return url;
    }

    /**
     * Format data for the specific file.
     */
    public MyInsAc.Format getFormat() {
        return format;
    }

    /**
     * Format data for the specific file.
     */
    @Deprecated
    public MyInsAc.Format getMeta() {
        return format;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        YFile yFile = (YFile) o;

        if (format != null ? !format.equals(yFile.format) : yFile.format != null) return false;
        return url != null ? url.equals(yFile.url) : yFile.url == null;
    }

    @Override
    public int hashCode() {
        int result = format != null ? format.hashCode() : 0;
        result = 31 * result + (url != null ? url.hashCode() : 0);
        return result;
    }

}
