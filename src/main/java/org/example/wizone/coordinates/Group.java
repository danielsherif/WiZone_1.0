package org.example.wizone.coordinates;
import java.util.ArrayList;
import java.util.List;


public class Group {
    public List<Line> lines;

    public Group() {
        lines = new ArrayList<>();
    }

    public void addLine(Line line) {
        lines.add(line);
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("(");
        for (Line line : lines) {
            sb.append(line).append(",");
        }
        if (!lines.isEmpty()) {
            sb.deleteCharAt(sb.length() - 1);
        }
        sb.append(")");
        return sb.toString();
    }
}
