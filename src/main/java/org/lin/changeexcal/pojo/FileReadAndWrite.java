package org.lin.changeexcal.pojo;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class FileReadAndWrite {

    @Value("${file.default.reader.path}")
    public String defaultReadPath;
    @Value("${file.default.writer.path}")
    public String defaultWriterPath;

    private static ThreadLocal<Map<String, Object>> readAndWritePath = new ThreadLocal<>();

    public void setReadAndWritePath(String read, String write) {
        if (StringUtils.isEmpty(read)) {
            read = defaultReadPath;
        }
        if (StringUtils.isEmpty(write)) {
            write = defaultWriterPath;
        }
        Map<String, Object> map = readAndWritePath.get();
        if (map == null) {
            map = new HashMap();
            map.put("currentPath", new ArrayList<String>());
        }
        map.put("readPath", read);
        map.put("writePath", write);
        readAndWritePath.set(map);
    }

    public Map<String, String> getReadAndWritePath() {
        Map<String, Object> map = readAndWritePath.get();
        if (map == null) {
            map = new HashMap();
            map.put("readPath", defaultReadPath);
            map.put("writePath", defaultWriterPath);
            map.put("currentPath", new ArrayList<String>());
            readAndWritePath.set(map);
        }
        Map<String, String> readMap = new HashMap<>();
        readMap.put("readPath", (String) map.get("readPath"));
        readMap.put("writePath", (String) map.get("writePath"));
        return readMap;
    }

    public String getCurrentPath() {
        List<String> currentPath = (List<String>) readAndWritePath.get().get("currentPath");
        StringBuffer path = new StringBuffer();
        currentPath.stream().forEach(item -> {
            path.append(item);
        });
        return path.toString();
    }

    public void addCurrentPath(String path) {
        List<String> currentPath = (List<String>) readAndWritePath.get().get("currentPath");
        currentPath.add(path);
    }

    public void subCurrentPath() {
        List<String> currentPath = (List<String>) readAndWritePath.get().get("currentPath");
        currentPath.remove(currentPath.size() - 1);
    }

    public void removeCurrentPath() {
        readAndWritePath.get().put("currentPath", new ArrayList<String>());
    }

    public void remove() {
        readAndWritePath.remove();
    }
}
