package com.company;

import java.awt.*;
import java.io.*;
import java.util.ArrayList;
import java.util.LinkedList;

public class Main {

    private static String[][] readFile() throws IOException {
        ArrayList<String[]> arrayList = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new FileReader(choseTextFile()))) {
            String line;
            while ((line = reader.readLine()) != null) {
                arrayList.add(line.split(""));
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
        String[][] tmp = new String[arrayList.size()][arrayList.get(0).length];
        for (int i = 0; i < arrayList.size(); i++) {
            tmp[i] = arrayList.get(i);
        }
        return tmp;
    }

    private static File choseTextFile() {
        FileDialog dialog = new FileDialog((Frame) null, "select File to open");
        dialog.setMode(FileDialog.LOAD);
        dialog.setVisible(true);
        File[] file = dialog.getFiles();
        return file[0];
    }


    public static void main(String[] args) throws IOException {
        String[][] iceCave1 = readFile();
        boolean sFound = false;
        boolean fFound = false;
        int startX = 0, startY = 0, endX = 0, endY = 0;
        for (int i = 0; i < iceCave1.length; i++) {
            if (!(sFound && fFound)) {
                for (int j = 0; j < iceCave1[0].length; j++) {
                    switch (iceCave1[i][j]) {
                        case "S":
                            startX = j;
                            startY = i;
                            sFound = true;
                            break;
                        case "F":
                            endX = j;
                            endY = i;
                            fFound = true;
                            break;
                    }
                }
            } else
                break;
        }
        String[][] iceCave2 = {
                {"s", ".", "."},
                {"f", ".", "."},
                {"0", ".", "."},

        };

        System.out.println(solve(iceCave1, startX, startY, endX,endY));

        //System.out.println(solve(iceCave2, 0, 0, 0,1));
        //System.out.println();


    }


    public static int solve(String[][] iceCave, int startX, int startY, int endX, int endY) {

        Point startPoint = new Point(startX, startY);

        LinkedList<Point> queue = new LinkedList<>();
        Point[][] iceCaveColors = new Point[iceCave.length][iceCave[0].length];

        queue.addLast(new Point(startX, startY));
        iceCaveColors[startY][startX] = startPoint;

        while (queue.size() != 0) {
            Point currPos = queue.pollFirst();
            System.out.println(currPos + " TO end " + endX+ " "+endY);
            // traverse adjacent nodes while sliding on the ice
            for (Direction dir : Direction.values()) {
                Point nextPos = move(iceCave, iceCaveColors, currPos, dir);
                System.out.println("\t" +"move "+ dir+" to "+ nextPos);
                if (nextPos != null) {
                    queue.addLast(nextPos);
                    iceCaveColors[nextPos.getY()][nextPos.getX()] = new Point(currPos.getX(), currPos.getY());
                    if (nextPos.getY() == endY && nextPos.getX() == endX) {
                        // we found the end point
                        Point tmp = currPos;  // if we start from nextPos we will count one too many edges
                        int count = 0;
                        while (tmp != startPoint) {
                            count++;
                            tmp = iceCaveColors[tmp.getY()][tmp.getX()];
                        }
                        return count;
                    }
                }
            }
            System.out.println();
        }
        return -1;
    }

    public static Point move(String[][] iceCave, Point[][] iceCaveColors, Point currPos, Direction dir) {
        int x = currPos.getX();
        int y = currPos.getY();

        int diffX = (dir == Direction.LEFT ? -1 : (dir == Direction.RIGHT ? 1 : 0));
        int diffY = (dir == Direction.UP ? -1 : (dir == Direction.DOWN ? 1 : 0));

        int i = 1;
        while (x + i * diffX >= 0
                && x + i * diffX < iceCave[0].length
                && y + i * diffY >= 0
                && y + i * diffY < iceCave.length
                && ((!iceCave[y + i * diffY][x + i * diffX].equals("0")) ) ) {
            i++;
        }

        i--;  // reverse the last step

        if (iceCaveColors[y + i * diffY][x + i * diffX] != null) {
            // we've already seen this point
            return null;
        }

        return new Point(x + i * diffX, y + i * diffY);
    }

    public static class Point {
        int x;
        int y;

        public Point(int x, int y) {
            this.x = x;
            this.y = y;
        }

        public int getX() {
            return x;
        }

        public int getY() {
            return y;
        }

        @Override
        public String toString() {
            return "Point{" +
                    "x=" + x +
                    ", y=" + y +
                    '}';
        }
    }

    public enum Direction {
        LEFT,
        RIGHT,
        UP,
        DOWN
    }
}