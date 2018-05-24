package io.zensoft.open.api.domain

import org.springframework.data.domain.Page

/**
 * @author Kadach Alexey
 */
data class PageResponse<T>(var totalCount: Long, var list: List<T>) {
    constructor(page: Page<T>) : this(page.totalElements, page.content)
}