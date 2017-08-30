package com.simis.util;

import java.io.Serializable;
import java.util.List;

/**
 * Created by 一拳超人 on 17/3/29.
 */
public class PageTool<R extends Serializable> implements Serializable {
    private static final long serialVersionUID = -660593215151789696L;
    private List<R> records;
    private int pageSize = 25;
    private int pageNo = 1;
    private int rowCount;
    private int pageCount;

    public PageTool() {
    }

    public int getRowCount() {
        return this.rowCount;
    }

    public int getPageCount() {
        return this.pageCount;
    }

    public void setRowCount(int rowCount) {
        this.rowCount = rowCount;
        this.pageCount = rowCount % this.pageSize == 0?rowCount / this.pageSize:rowCount / this.pageSize + 1;
        if(this.pageNo > this.pageCount) {
            this.pageNo = this.pageCount;
        }

    }

    public int getFirstPage() {
        return 1;
    }

    public int getLastPage() {
        return this.pageCount;
    }

    public int getNextPage() {
        return this.isLastPage()?this.pageNo:this.pageNo + 1;
    }

    public int getPrevPage() {
        return this.isFirstPage()?1:this.pageNo - 1;
    }

    public int getStart() {
        int start = (this.pageNo - 1) * this.pageSize;
        return start > 0?start:0;
    }

    public int getEnd() {
        int end = this.getStart() + this.pageSize;
        if(end > this.rowCount) {
            end = this.rowCount;
        }

        return end;
    }

    public int getPageNo() {
        return this.pageNo;
    }

    public void setPageNo(int pageNo) {
        if(pageNo <= 0) {
            this.pageNo = 1;
        } else {
            this.pageNo = pageNo;
        }

    }

    private boolean isFirstPage() {
        return 1 == this.pageNo;
    }

    private boolean isLastPage() {
        return this.pageCount == this.pageNo;
    }

    public List<R> getRecords() {
        return this.records;
    }

    public void setRecords(List<R> records) {
        this.records = records;
    }

    public int getPageSize() {
        return this.pageSize;
    }

    public void setPageSize(int pageSize) {
        if(0 != pageSize) {
            this.pageSize = pageSize;
        }

    }
}
