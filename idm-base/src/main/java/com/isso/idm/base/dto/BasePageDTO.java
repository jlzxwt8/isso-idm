
package com.isso.idm.base.dto;

import java.io.Serializable;
import java.util.List;

public class BasePageDTO<T> implements Serializable {

	private long total = 0;
	private int totalPages = 0;
	private int pageSize = 0;
	private int pageIndex = 0;
	private boolean nextPage = true;
	private boolean previousPage = true;
	private boolean firstPage = true;
	private boolean lastPage = true;
	private List<T> rows = null;

	public BasePageDTO(long total, int pageSize, int pageIndex, List<T> rows) {
		if (total > 0 && pageSize > 0 && pageIndex > 0) {
			this.total = total;
			this.pageSize = pageSize;
			this.pageIndex = pageIndex;
			this.rows = rows;
			
			this.totalPages = Long.valueOf(total / pageSize).intValue();
			if (total % pageSize > 0) {
				this.totalPages++;
			}

			if (pageIndex <= 1) {
				this.firstPage = false;
				this.previousPage = false;
			}
			if (pageIndex >= totalPages) {
				this.nextPage = false;
				this.lastPage = false;
			}
		} else {
			this.firstPage = false;
			this.previousPage = false;
			this.nextPage = false;
			this.lastPage = false;
		}
	}

	/**
	 * @Date : Aug 21, 2014
	 * @return the total
	 */
	public long getTotal() {
		return total;
	}

	/**
	 * @Date : Aug 21, 2014
	 * @return the totalPages
	 */
	public int getTotalPages() {
		return totalPages;
	}

	/**
	 * @Date : Aug 21, 2014
	 * @return the pageSize
	 */
	public int getPageSize() {
		return pageSize;
	}

	/**
	 * @Date : Aug 21, 2014
	 * @return the pageIndex
	 */
	public int getPageIndex() {
		return pageIndex;
	}

	/**
	 * @Date : Aug 21, 2014
	 * @return the nextPage
	 */
	public boolean isNextPage() {
		return nextPage;
	}

	/**
	 * @Date : Aug 21, 2014
	 * @return the previousPage
	 */
	public boolean isPreviousPage() {
		return previousPage;
	}

	/**
	 * @Date : Aug 21, 2014
	 * @return the firstPage
	 */
	public boolean isFirstPage() {
		return firstPage;
	}

	/**
	 * @Date : Aug 21, 2014
	 * @return the lastPage
	 */
	public boolean isLastPage() {
		return lastPage;
	}

	/**
	 * @return the rows
	 */
	public List<T> getRows() {
		return rows;
	}

}
