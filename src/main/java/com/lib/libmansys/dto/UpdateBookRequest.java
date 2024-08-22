package com.lib.libmansys.dto;

import com.lib.libmansys.entity.Enum.BookStatus;
import lombok.Data;

import java.util.List;
@Data
public class UpdateBookRequest {
    private String title;
    private List<Long> authorIds;
    private List<Long> publisherIds;
    private List<Long> genreIds;
    private BookStatus status;

}