import java.io.File;
import java.util.*;

/**
 * GoPro文件的重命名,因为GoPro的命名会打乱文件夹的显示,导致看的很奇怪的排序
 * <p>
 * GH010151.MP4
 * GH,GX 01(正常的) 02(要修改) 03(要修改) 11(要修改)
 */
public class GoProRenamer {
    private final String path;
    private final List<String> files = new ArrayList<>();
    private final Map<String, String> pairs = new HashMap<>();

    public GoProRenamer(String path) {
        this.path = path;
        LogUtil.log("任务开始!");
        try {
            collect();
            check();
            rename();
        } catch (Exception ex) {
            LogUtil.log("异常:" + ex.getMessage());
        } finally {
            LogUtil.log("任务结束!");
        }
    }

    private void collect() {
        File dir = new File(path);
        if (dir.isDirectory()) {
            String[] fileNames = dir.list();
            if (fileNames == null || fileNames.length == 0) {
                LogUtil.log("文件夹为空!");
            } else {
                files.addAll(Arrays.asList(fileNames));
            }
        } else {
            throw new RuntimeException("path 必须为文件夹");
        }
    }

    private void check() {
        for (String item : files) {
            if (needRename(item)) {
                String sourceName = sourceName(item);
                String toName = toName(item);
                pairs.put(item, toName);
                pairs.put(sourceName, insertedName(sourceName, "01"));
            } else {
                LogUtil.log(item + "  不需要重命名.");
            }
        }
    }

    private void rename() {
        Set<Map.Entry<String, String>> entries = pairs.entrySet();
        for (Map.Entry<String, String> item : entries) {
            String from = item.getKey();
            String to = item.getValue();
            if (!TextUtil.isEmpty(from) && !TextUtil.isEmpty(to)) {
                rename(from, to);
            } else {
                LogUtil.log("出现空的情况:" + from + "|" + to);
            }
        }
    }

    private void rename(String from, String to) {
        File file = new File(path, from);
        if (file.exists()) {
            boolean success = file.renameTo(new File(path, to));
            LogUtil.log(String.format("%s 重命名为 %s 结果: %s", from, to, success ? "成功!" : "失败!"));
        } else {
            LogUtil.log("该文件不存在:" + from);
        }
    }

    private boolean needRename(String name) {
        String PRE_FIX_GH = "GH01";
        String PRE_FIX_GX = "GX01";
        return !TextUtil.isEmpty(name) && !name.startsWith(PRE_FIX_GH) && !name.startsWith(PRE_FIX_GX);
    }

    private String toName(String name) {
        String REPLACE = "01";
        String replace = String.valueOf(name.charAt(2)) + name.charAt(3);
        String fixedName = name.replaceFirst(replace, REPLACE);
        return insertedName(fixedName, replace);
    }

    private String sourceName(String name) {
        String REPLACE = "01";
        String fixed = String.valueOf(name.charAt(2)) + name.charAt(3);
        return name.replaceFirst(fixed, REPLACE);
    }

    private String insertedName(String name, String index) {
        String[] split = name.split("\\.");
        return String.format("%s-%s.%s", split[0], index, split[1]);
    }
}
