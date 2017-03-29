package com.open.yun.common.model;

/**
 * 分页请求信息
 * @author tang
 */
public class PagePojo extends BasePojo{
	/** 每页记录数(默认每页10条) */
    private int pageSize = 10;

    /** 当前页码(默认第一页) */
    private int currentPage = 1;

	public int getPageSize() {
		return pageSize;
	}

	public void setPageSize(int pageSize) {
		this.pageSize = pageSize;
	}

	public int getCurrentPage() {
		return currentPage;
	}

	public void setCurrentPage(int currentPage) {
		this.currentPage = currentPage;
	}
}
