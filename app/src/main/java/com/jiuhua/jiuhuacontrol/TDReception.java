package com.jiuhua.jiuhuacontrol;

//TODO： 接收数据还可以正常的数据不需要大的修改。
public class TDReception {
    String status;
    String[] head;
    String[][] column_meta;
    String[][] data;

    public TDReception(String status, String[] head, String[][] column_meta, String[][] data) {
        this.status = status;
        this.head = head;
        this.column_meta = column_meta;
        this.data = data;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String[] getHead() {
        return head;
    }

    public void setHead(String[] head) {
        this.head = head;
    }

    public String[][] getColumn_meta() {
        return column_meta;
    }

    public void setColumn_meta(String[][] column_meta) {
        this.column_meta = column_meta;
    }

    public String[][] getData() {
        return data;
    }

    public void setData(String[][] data) {
        this.data = data;
    }

    public void show() {//经过测试完全正确接收了数据
        System.out.println("TDReception  status = " + status);
        for (int i = 0; i < head.length; i++) {
            System.out.println("TDReception  head[" + i + "] = " + head[i]);
        }
        for (int i = 0; i < column_meta.length; i++) {
            for (int j = 0; j < column_meta[i].length; j++) {
                System.out.println("TDReception  column_meta[" + i + "][" + j + "] = " + column_meta[i][j]);
            }
        }
        for (int i = 0; i < data.length; i++) {
            for (int j = 0; j < data[i].length; j++) {
                System.out.println("TDReception  data 第" + (i+1) + "行 " + head[j] + " = " + data[i][j]);
            }
        }
    }
}
