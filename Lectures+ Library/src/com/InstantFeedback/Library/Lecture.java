package com.InstantFeedback.Library;

import android.net.nsd.NsdServiceInfo;

/**
 * Created by Jasiek on 14/04/2014.
 */
public class Lecture {
    //TODO - whole class is only a placeholder right now

    private NsdServiceInfo lectureInfo;

    public Lecture(NsdServiceInfo serviceInfo) {
        lectureInfo = serviceInfo;
    }

    public NsdServiceInfo getLectureInfo() {
        return lectureInfo;
    }
}
