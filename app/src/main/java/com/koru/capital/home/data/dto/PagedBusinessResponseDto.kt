package com.koru.capital.home.data.dto

data class PagedBusinessResponseDto(
    val data: List<BusinessFeedItemDto>,
    val pagination: PaginationInfoDto?
)

