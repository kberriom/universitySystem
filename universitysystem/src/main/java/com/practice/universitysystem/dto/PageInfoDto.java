package com.practice.universitysystem.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class PageInfoDto {

    long currentPage;

    long currentPageSize;

    long maxPages;
}
