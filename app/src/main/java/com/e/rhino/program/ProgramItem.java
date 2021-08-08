package com.e.rhino.program;

import com.e.rhino.sessions.content.SessionContent;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ProgramItem {
    public final int id;
    public final String name;
    public final String description;
    public final int imageId;
    public final int sessionCount;
    public List<SessionContent.SessionItem> sessionItems = new ArrayList<SessionContent.SessionItem>();
    public Map<Integer, SessionContent.SessionItem> sessionMap = new HashMap<Integer, SessionContent.SessionItem>();

    public ProgramItem(int id, String name, String description, int imageId, int sessionCount,
                       List<SessionContent.SessionItem> sessions,
                       Map<Integer, SessionContent.SessionItem> sessionMap) {
        this.id = id;
        this.name = name;
        this.imageId = imageId;
        this.description = description;
        this.sessionCount = sessionCount;
        this.sessionItems = sessions;
        this.sessionMap = sessionMap;
    }

    @Override
    public String toString() {
        return name;
    }
}
