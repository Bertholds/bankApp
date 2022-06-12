package com.codetreatise.service;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.springframework.data.domain.AbstractPageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import java.io.Serializable;

/**
* Created by Ergin
**/
public class OffsetBasedPageRequest implements Pageable {

	private int limit = 0;
	private int offset = 0;

	public OffsetBasedPageRequest(int skip, int offset) {
	    if (skip < 0)
	        throw new IllegalArgumentException("Skip must not be less than zero!");

	    if (offset < 0)
	        throw new IllegalArgumentException("Offset must not be less than zero!");

	    this.limit = offset;
	    this.offset = skip;
	}

	@Override
	public int getPageNumber() {
	    return 0;
	}

	@Override
	public int getPageSize() {
	    return limit;
	}

	@Override
	public int getOffset() {
	    return offset;
	}

	@Override
	public Sort getSort() {
	    return null;
	}

	@Override
	public Pageable next() {
	    return null;
	}

	@Override
	public Pageable previousOrFirst() {
	    return this;
	}

	@Override
	public Pageable first() {
	    return this;
	}

	@Override
	public boolean hasPrevious() {
	    return false;
	}


}
